package com.imooc.mall2.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * ClassName: CartVo
 * Package: com.imooc.mall2.vo
 *
 * @Author 马学兴
 * @Create 2023/6/21 22:29
 * @Version 1.0
 * Description:
 */
@Data
public class CartVo {
    private List<CartProductVo> cartProductVoList;
    private Boolean selectedAll;
    private BigDecimal cartTotalPrice;
    private Integer cartTotalQuantity;
}
