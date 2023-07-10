package com.imooc.mall2.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mall2.dao.OrderItemMapper;
import com.imooc.mall2.dao.OrderMapper;
import com.imooc.mall2.dao.ProductMapper;
import com.imooc.mall2.dao.ShippingMapper;
import com.imooc.mall2.enums.OrderStatusEnum;
import com.imooc.mall2.enums.PaymentTypeEnum;
import com.imooc.mall2.enums.ProductStatusEnum;
import com.imooc.mall2.enums.ResponserEnem;
import com.imooc.mall2.pojo.*;
import com.imooc.mall2.service.ICartService;
import com.imooc.mall2.service.IOrderService;
import com.imooc.mall2.vo.OrderItemVo;
import com.imooc.mall2.vo.OrderVo;
import com.imooc.mall2.vo.ResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ClassName: IOrderServiceImpl
 * Package: com.imooc.mall2.service.impl
 *
 * @Author 马学兴
 * @Create 2023/6/23 13:21
 * @Version 1.0
 * Description:
 */
@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private ShippingMapper shippingMapper;
    @Autowired
    private ICartService cartService;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    @Transactional
    public ResponseVo<OrderVo> create(Integer uid, Integer shippingId) {
        //收获地址校验（总之要查出来）
        Shipping shipping = shippingMapper.selectByUidAndShippingId(uid, shippingId);
        if (shipping == null) {
            return ResponseVo.error(ResponserEnem.SHIPPING_NOT_EXIST);
        }
        //获取购物车，校验（是否有商品、库存）
        List<Cart> cartList = cartService.listForCart(uid).stream()
                .filter(Cart::getProductSelected)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(cartList)) {
            return ResponseVo.error(ResponserEnem.CART_SELECTED_IS_EMPTY);
        }
        //获取cartList里的productIds
        Set<Integer> productIdSet = cartList.stream()
                .map(Cart::getProductId)//调用map()方法，对流中的每个购物车对象(Cart)执行映射操作，提取出其中的商品ID(getProductId()
                .collect(Collectors.toSet());
        List<Product> productList = productMapper.selectByProductIdSet(productIdSet);
        Map<Integer, Product> map = productList.stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        List<OrderItem> orderItemList = new ArrayList<>();
        Long orderNo = generateOrderNo();//生成订单号
        for (Cart cart : cartList) {
            //根据productId查数据库
            Product product = map.get(cart.getProductId());
            //是否有该商品
            if (product == null) {
                return ResponseVo.error(ResponserEnem.PRODUCT_NOT_EXIST,
                        "商品不存在 productId=" + cart.getProductId());
            }
            //商品上下架状态
            if (!ProductStatusEnum.ON_SALE.getCode().equals(product.getStatus())) {
                return ResponseVo.error(ResponserEnem.PRODUCT_OFF_SALE_OR_DELETE,
                        "商品不是在售状态." + product.getName());
            }
            //库存是否充足
            if (product.getStock() < cart.getQuantity()) {
                return ResponseVo.error(ResponserEnem.PRODUCT_STOCK_NOTENOUGH,
                        "库存不足." + product.getName());
            }
            OrderItem orderItem = buildOrderItem(uid, orderNo, cart.getQuantity(), product);
            orderItemList.add(orderItem);
            //减库存
            product.setStock(product.getStock()-cart.getQuantity());
            int row = productMapper.updateByPrimaryKeySelective(product);
            if (row<=0){
                return ResponseVo.error(ResponserEnem.ERROR);
            }
        }
        //计算总价格，只计算被选择的商品
        //生成订单，入库：order和order_item,事务
        Order order = buildOrder(uid, orderNo, shippingId, orderItemList);
        int rowForOrder = orderMapper.insertSelective(order);
        if (rowForOrder <= 0) {
            return ResponseVo.error(ResponserEnem.ERROR);
        }
        int rowForOrderItem = orderItemMapper.batchInsert(orderItemList);
        if (rowForOrderItem <= 0) {
            return ResponseVo.error(ResponserEnem.ERROR);
        }

        //清空购物车（选中的商品）
        //Redis有事务（打包命令），不能回滚
        for (Cart cart : cartList) {
        cartService.delete(uid,cart.getProductId());
        }

        //构造OrderVo
        OrderVo orderVo = buildOrderVo(order, orderItemList, shipping);
        return ResponseVo.success(orderVo);
    }
    private OrderVo buildOrderVo(Order order, List<OrderItem> orderItemList, Shipping shipping) {
        OrderVo orderVo = new OrderVo();
        BeanUtils.copyProperties(order,orderVo);
        List<OrderItemVo> orderItemVoList = orderItemList.stream().map(e -> {
            OrderItemVo orderItemVo = new OrderItemVo();
            BeanUtils.copyProperties(e, orderItemVo);
            return orderItemVo;
        }).collect(Collectors.toList());
        orderVo.setOrderItemVoList(orderItemVoList);
        if (shipping!=null){
            orderVo.setShippingId(shipping.getId());
            orderVo.setShippingVo(shipping);
        }

        return orderVo;
    }
    private Order buildOrder(Integer uid,
                             Long orderNo,
                             Integer shippingId,
                             List<OrderItem> orderItemList) {
        BigDecimal payment = orderItemList.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(uid);
        order.setShippingId(shippingId);
        order.setPayment(payment);
        order.setPaymentType(PaymentTypeEnum.PAY_ONLINE.getCode());
        order.setPostage(0);
        order.setStatus(OrderStatusEnum.NO_PAY.getCode());
        return order;
    }
    //企业级：分布式唯一id
    private Long generateOrderNo() {
        return System.currentTimeMillis() + new Random().nextInt(999);
    }

    private OrderItem buildOrderItem(Integer uid, Long orderNo, Integer quantity, Product product) {
        OrderItem item = new OrderItem();
        item.setUserId(uid);
        item.setOrderNo(orderNo);
        item.setProductId(product.getId());
        item.setProductName(product.getName());
        item.setProductImage(product.getMainImage());
        item.setCurrentUnitPrice(product.getPrice());
        item.setQuantity(quantity);
        item.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        return item;
    }

    @Override
    public ResponseVo<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Order> orderList = orderMapper.selectByUid(uid);// 查询指定用户的订单列表

        Set<Long> orderNoSet = orderList.stream()// 获取订单列表中的订单号集合
                .map(Order::getOrderNo)
                .collect(Collectors.toSet());
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNoSet(orderNoSet);// 根据订单号集合查询订单商品列表
        Map<Long , List<OrderItem>> orderItemMap=orderItemList.stream()// 将订单商品列表按订单号进行分组，得到一个以订单号为键，订单商品列表为值的Map
                .collect(Collectors.groupingBy(OrderItem::getOrderNo));

        Set<Integer>  shippingIdSet= orderList.stream()// 获取订单列表中的收货地址ID集合
                .map(Order::getShippingId)
                .collect(Collectors.toSet());
        List<Shipping> shippingList = shippingMapper.selectByIdSet(shippingIdSet);// 根据收货地址ID集合查询收货地址列表
        Map<Integer,Shipping> shippingMap=shippingList.stream() // 将收货地址列表转换为以收货地址ID为键，收货地址对象为值的Map
                .collect(Collectors.toMap(Shipping::getId,shipping -> shipping));

        List<OrderVo> orderVoList=new ArrayList<>();// 构建OrderVo对象列表
        for (Order order : orderList) {
            OrderVo orderVo = buildOrderVo(order,
                    orderItemMap.get(order.getOrderNo()),
                    shippingMap.get(order.getShippingId()));
            orderVoList.add(orderVo);
        }
        PageInfo pageInfo = new PageInfo<>(orderList);
        pageInfo.setList(orderVoList);
        return ResponseVo.success(pageInfo);
    }

    @Override
    public ResponseVo<OrderVo> detail(Integer uid, Long orderNo) {
        //查看订单是否属于这个用户
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order==null||!order.getUserId().equals(uid)){
            return ResponseVo.error(ResponserEnem.ORDER_NOT_EXIST);
        }
        Set<Long> orderNoSet = new HashSet<>();
        orderNoSet.add(order.getOrderNo());
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNoSet(orderNoSet);// 根据订单号集合查询订单商品列表
        Shipping shipping = shippingMapper.selectByPrimaryKey(order.getShippingId());
        OrderVo orderVo = buildOrderVo(order, orderItemList, shipping);
        return ResponseVo.success(orderVo);
    }

    @Override
    public ResponseVo cancel(Integer uid, Long orderNo) {
        //查看订单是否属于这个用户
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order==null||!order.getUserId().equals(uid)){
            return ResponseVo.error(ResponserEnem.ORDER_NOT_EXIST);
        }
        //只有未付款订单可以取消
        if (!order.getStatus().equals(OrderStatusEnum.NO_PAY.getCode())){
            return ResponseVo.error(ResponserEnem.ORDER_STATUS_ERROR);
        }
        order.setStatus(OrderStatusEnum.CANCELED.getCode());
        order.setCloseTime(new Date());
        int row = orderMapper.updateByPrimaryKeySelective(order);
        if (row<=0){
            return ResponseVo.error(ResponserEnem.ERROR);
        }
        return ResponseVo.success();


    }

    @Override
    public void paid(Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order==null){
            throw new RuntimeException(ResponserEnem.ORDER_NOT_EXIST.getDesc()+"订单id："+orderNo);
        }
        //只有未付款订单可以变成已付款
        if (!order.getStatus().equals(OrderStatusEnum.NO_PAY.getCode())){
            throw new RuntimeException(ResponserEnem.ORDER_STATUS_ERROR.getDesc()+"订单id："+orderNo);
        }
        order.setStatus(OrderStatusEnum.PAID.getCode());
        order.setPaymentTime(new Date());
        int row = orderMapper.updateByPrimaryKeySelective(order);
        if (row<=0){
           throw new RuntimeException("将订单更新已付款状态失败，订单id："+orderNo);
        }

    }


}
