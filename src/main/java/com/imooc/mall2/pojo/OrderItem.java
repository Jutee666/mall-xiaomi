package com.imooc.mall2.pojo;

import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Date;
@Data
public class OrderItem {
    private Integer id;

    private Integer userId;

    private Long orderNo;

    private Integer productId;

    private String productName;

    private String productImage;

    private BigDecimal currentUnitPrice;

    private Integer quantity;

    private BigDecimal totalPrice;

    private Date createTime;

    private Date updateTime;


}