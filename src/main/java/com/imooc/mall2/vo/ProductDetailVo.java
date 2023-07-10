package com.imooc.mall2.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * ClassName: ProductDetailVo
 * Package: com.imooc.mall2.vo
 *
 * @Author 马学兴
 * @Create 2023/6/21 16:17
 * @Version 1.0
 * Description:
 */
@Data
public class ProductDetailVo {
    private Integer id;

    private Integer categoryId;

    private String name;

    private String subtitle;

    private String mainImage;

    private String subImages;

    private String detail;

    private BigDecimal price;

    private Integer stock;

    private Integer status;

    private Date createTime;

    private Date updateTime;
}
