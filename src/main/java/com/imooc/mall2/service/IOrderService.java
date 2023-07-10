package com.imooc.mall2.service;

import com.github.pagehelper.PageInfo;
import com.imooc.mall2.vo.OrderVo;
import com.imooc.mall2.vo.ResponseVo;

/**
 * ClassName: IOrderService
 * Package: com.imooc.mall2.service
 *
 * @Author 马学兴
 * @Create 2023/6/23 13:20
 * @Version 1.0
 * Description:
 */
public interface IOrderService {
    ResponseVo<OrderVo> create(Integer uid,Integer shippingId);
    ResponseVo<PageInfo> list(Integer uid, Integer pageNum,Integer pageSize);
    ResponseVo<OrderVo> detail(Integer uid, Long orderNo);

    ResponseVo cancel(Integer uid, Long orderNo);
    void paid(Long orderNo);
}
