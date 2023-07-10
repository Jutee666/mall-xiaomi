package com.imooc.mall2.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * ClassName: ProducctVo
 * Package: com.imooc.mall2.vo
 *
 * @Author 马学兴
 * @Create 2023/6/21 11:08
 * @Version 1.0
 * Description:
 */
@Data
public class ProductVo {
    private Integer id;

    private Integer categoryId;

    private String name;

    private String subtitle;

    private String mainImage;
    private Integer status;
    private BigDecimal price;
}
