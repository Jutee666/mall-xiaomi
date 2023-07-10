package com.imooc.pay.service;

import com.imooc.pay.pojo.PayInfo;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayResponse;

import java.math.BigDecimal;

/**
 * ClassName: PayService
 * Package: com.imooc.pay.service
 *
 * @Author 马学兴
 * @Create 2023/6/18 10:47
 * @Version 1.0
 * Description:
 */
public interface IPayService {
    //创建/发起支付
    PayResponse create(String orderId, BigDecimal amount, BestPayTypeEnum bestPayTypeEnum);
    //异步通知处理
    String asyncNotify(String notifyData);
    //查询支付记录（通过订单号）
    PayInfo queryByOrderId(String orderId);
}
