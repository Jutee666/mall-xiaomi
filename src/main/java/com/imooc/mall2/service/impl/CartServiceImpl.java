package com.imooc.mall2.service.impl;

import com.google.gson.Gson;
import com.imooc.mall2.dao.ProductMapper;
import com.imooc.mall2.enums.ProductStatusEnum;
import com.imooc.mall2.enums.ResponserEnem;
import com.imooc.mall2.form.CartAddForm;
import com.imooc.mall2.form.CartUpdateForm;
import com.imooc.mall2.pojo.Cart;
import com.imooc.mall2.pojo.Product;
import com.imooc.mall2.service.ICartService;
import com.imooc.mall2.vo.CartProductVo;
import com.imooc.mall2.vo.CartVo;
import com.imooc.mall2.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.Struct;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * ClassName: CartServiceImpl
 * Package: com.imooc.mall2.service.impl
 *
 * @Author 马学兴
 * @Create 2023/6/21 23:33
 * @Version 1.0
 * Description:
 */
@Service
public class CartServiceImpl implements ICartService {
    private final static String CART_REDIS_KEY_TEMPLATE = "cart_%d";//%d 表示一个整数的占位符
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;

    private Gson gson = new Gson();

    @Override
    public ResponseVo<CartVo> add(Integer uid, CartAddForm form) {
        Integer quantity = 1;
        Product product = productMapper.selectByPrimaryKey(form.getProductId());
        //判断商品是否存在
        if (product == null) {
            return ResponseVo.error(ResponserEnem.PRODUCT_NOT_EXIST);
        }
        //商品是否正常在售
        if (!product.getStatus().equals(ProductStatusEnum.ON_SALE.getCode())) {
            return ResponseVo.error(ResponserEnem.PRODUCT_OFF_SALE_OR_DELETE);
        }
        //判断商品库存是否充足
        if (product.getStock() <= 0) {
            return ResponseVo.error(ResponserEnem.PRODUCT_STOCK_NOTENOUGH);
        }
        //写入到redis
        //key：cart_1
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);//uid 的值为 1,使用 String.format() 方法将其插入到模板中，得到的 redisKey 将是 "cart_1"。

        Cart cart = new Cart();
        //读取redis中的
        String value = opsForHash.get(redisKey, String.valueOf(product.getId()));
        if (StringUtils.isEmpty(value)) {
            //说明redis中没有该商品
            cart = new Cart(product.getId(), quantity, form.getSelected());
        } else {
            //已经有了，数量+1
            cart = gson.fromJson(value, Cart.class);//将 JSON 字符串 value 转换为一个 Cart 类型的对象，并将其赋值给 cart 变量
            cart.setQuantity(cart.getQuantity() + quantity);
        }

        opsForHash.put(String.format(CART_REDIS_KEY_TEMPLATE, uid),//生成了 Redis 的键，其中 CART_REDIS_KEY_TEMPLATE 是一个字符串模板，用来构建 Redis 键，uid 是用户的唯一标识
                String.valueOf(product.getId()),//生成了哈希表的字段，它是商品对象的 ID
                gson.toJson(cart));//把cart对象转成string

        return list(uid);
    }

    @Override
    public ResponseVo<CartVo> list(Integer uid) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);
        Map<String, String> entries = opsForHash.entries(redisKey);

        boolean selectAll = true;
        int cartTotalQuantity = 0;
        BigDecimal cartTotalPrice = BigDecimal.ZERO;
        CartVo cartVo = new CartVo();
        List<CartProductVo> cartProductVoList = new ArrayList<>();
        //将redis中的商品的id全部获取出来
        Set<Integer> productIdSet = new HashSet<>();
        for (Map.Entry<String, String> entry : entries.entrySet()) {
            Integer productId = Integer.valueOf(entry.getKey());
            productIdSet.add(productId);
        }
        //根据redis中查出来的id查找mysql中对应的商品看商品详情
        List<Product> products = productMapper.selectByProductIdSet(productIdSet);
        Map<Integer, Product> productMap = new HashMap<>();
        for (Product product : products) {
            productMap.put(product.getId(), product);
        }

        for (Map.Entry<String, String> entry : entries.entrySet()) {//查询数据库
            Integer productId = Integer.valueOf(entry.getKey());
            Cart cart = gson.fromJson(entry.getValue(), Cart.class);

            Product product = productMap.get(productId);
            //TODO 需要优化，使用MySQL里面的in,可以应用于面试
            // Product product = productMapper.selectByPrimaryKey(productId);//根据redis数据库的商品id查看mysql数据库中的商品详情
            if (product != null) {
                CartProductVo cartProductVo = new CartProductVo(productId,
                        cart.getQuantity(),
                        product.getName(),
                        product.getSubtitle(),
                        product.getMainImage(),
                        product.getPrice(),
                        product.getStatus(),
                        product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())),
                        product.getStock(),
                        cart.getProductSelected()
                );
                cartProductVoList.add(cartProductVo);

                if (!cart.getProductSelected()) {
                    selectAll = false;
                }
                //计算总价，只计算选中
                if (cart.getProductSelected()) {
                    cartTotalPrice = cartTotalPrice.add(cartProductVo.getProductTotalPrice());
                }
                cartTotalQuantity += cart.getQuantity();
            }
        }
        //是否全选
        cartVo.setSelectedAll(selectAll);
        cartVo.setCartTotalQuantity(cartTotalQuantity);
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setCartProductVoList(cartProductVoList);
        return ResponseVo.success(cartVo);
    }

    @Override
    public ResponseVo<CartVo> update(Integer uid, Integer productId, CartUpdateForm form) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);//uid 的值为 1,使用 String.format() 方法将其插入到模板中，得到的 redisKey 将是 "cart_1"。

        //读取redis中的
        String value = opsForHash.get(redisKey, String.valueOf(productId));
        if (StringUtils.isEmpty(value)) {
            //没有该商品，报错
            return ResponseVo.error(ResponserEnem.CART_PRODUCT_NOT_EXIST);
        }
        //已经有了，修改内容
        Cart cart = gson.fromJson(value, Cart.class);//将 JSON 字符串 value 转换为一个 Cart 类型的对象，并将其赋值给 cart 变量
        if (form.getQuantity() != null && form.getQuantity() >= 0) {
            cart.setQuantity(form.getQuantity());
        }
        if (form.getSelected() != null) {
            cart.setProductSelected(form.getSelected());
        }
        opsForHash.put(redisKey, String.valueOf(productId), gson.toJson(cart));
        return list(uid);
    }

    @Override
    public ResponseVo<CartVo> delete(Integer uid, Integer productId) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);//uid 的值为 1,使用 String.format() 方法将其插入到模板中，得到的 redisKey 将是 "cart_1"。

        //读取redis中的
        String value = opsForHash.get(redisKey, String.valueOf(productId));
        if (StringUtils.isEmpty(value)) {
            //没有该商品，报错
            return ResponseVo.error(ResponserEnem.CART_PRODUCT_NOT_EXIST);
        }
        opsForHash.delete(redisKey, String.valueOf(productId));
        return list(uid);
    }

    @Override
    public ResponseVo<CartVo> selectAll(Integer uid) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);
        for (Cart cart : listForCart(uid)) {
            cart.setProductSelected(true);
            opsForHash.put(redisKey, String.valueOf(cart.getProductId()), gson.toJson(cart));
        }
        return list(uid);
    }

    @Override
    public ResponseVo<CartVo> unselectAll(Integer uid) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);
        for (Cart cart : listForCart(uid)) {
            cart.setProductSelected(false);
            opsForHash.put(redisKey, String.valueOf(cart.getProductId()), gson.toJson(cart));
        }
        return list(uid);
    }

    @Override
    public ResponseVo<Integer> sum(Integer uid) {
        Integer sum = listForCart(uid).stream()
                .map(Cart::getQuantity)
                .reduce(0, Integer::sum);
        return ResponseVo.success(sum);
    }

    public List<Cart> listForCart(Integer uid) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);
        Map<String, String> entries = opsForHash.entries(redisKey);
        List<Cart> cartList = new ArrayList<>();
        for (Map.Entry<String, String> entry : entries.entrySet()) {
            cartList.add(gson.fromJson(entry.getValue(), Cart.class));
        }
        return cartList;
    }
}
