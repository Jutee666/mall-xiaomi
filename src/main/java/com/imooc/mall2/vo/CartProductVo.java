package com.imooc.mall2.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * ClassName: CartProductVo
 * Package: com.imooc.mall2.vo
 *
 * @Author 马学兴
 * @Create 2023/6/21 22:32
 * @Version 1.0
 * Description:
 */
@Data
public class CartProductVo {
    private Integer productId;
    /**
     * 购买的数量
     */
    private Integer quantity;
    private String productName;
    private String productSubtitle;
    private String productMainImage;

    private BigDecimal productPrice;

    private Integer productStatus;
    private BigDecimal productTotalPrice;

    private Integer productStock;
//商品是否选中
    private Boolean productSelected;

    public CartProductVo(Integer productId, Integer quantity, String productName, String productSubtitle, String productMainImage, BigDecimal productPrice, Integer productStatus, BigDecimal productTotalPrice, Integer productStock, Boolean productSelected) {
        this.productId = productId;
        this.quantity = quantity;
        this.productName = productName;
        this.productSubtitle = productSubtitle;
        this.productMainImage = productMainImage;
        this.productPrice = productPrice;
        this.productStatus = productStatus;
        this.productTotalPrice = productTotalPrice;
        this.productStock = productStock;
        this.productSelected = productSelected;
    }
}
