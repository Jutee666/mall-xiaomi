package com.imooc.mall2.enums;

import lombok.Getter;

/**
 * ClassName: OrderStatusEnum
 * Package: com.imooc.mall2.enums
 *
 * @Author 马学兴
 * @Create 2023/6/23 16:28
 * @Version 1.0
 * Description:订单状态:0-已取消-10-未付款，20-已付款，40-已发货，50-交易成功，60-交易关闭
 */
@Getter
public enum OrderStatusEnum {
    CANCELED(0, "已取消"),
    NO_PAY(10, "未付款"),
    PAID(20, "已付款"),
    SHIPPED(40, "已发货"),
    TRADE_SUCCESS(50, "交易成功"),
    TRADE_CLOSE(60, "交易关闭"),
    ;
    Integer code;
    String desc;//描述

    OrderStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
