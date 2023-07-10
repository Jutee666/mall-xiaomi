package com.imooc.mall2.vo;

import com.imooc.mall2.pojo.Shipping;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * ClassName: OrderVo
 * Package: com.imooc.mall2.vo
 *
 * @Author 马学兴
 * @Create 2023/6/23 13:15
 * @Version 1.0
 * Description:
 */
@Data
public class OrderVo {

    private Long orderNo;

    private BigDecimal payment;

    private Integer paymentType;

    private Integer postage;

    private Integer status;

    private Date paymentTime;

    private Date sendTime;

    private Date endTime;

    private Date closeTime;

    private Date createTime;
    private List<OrderItemVo> orderItemVoList;
    private Integer shippingId;
    private Shipping shippingVo;
}
