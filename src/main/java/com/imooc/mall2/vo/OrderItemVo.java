package com.imooc.mall2.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * ClassName: OrderItemVo
 * Package: com.imooc.mall2.vo
 *
 * @Author 马学兴
 * @Create 2023/6/23 13:17
 * @Version 1.0
 * Description:
 */
@Data
public class OrderItemVo {
    private Long orderNo;

    private Integer productId;

    private String productName;

    private String productImage;

    private BigDecimal currentUnitPrice;

    private Integer quantity;

    private BigDecimal totalPrice;

    private Date createTime;

}
