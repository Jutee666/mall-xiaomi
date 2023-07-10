package com.imooc.mall2.pojo;

import lombok.Data;

/**
 * ClassName: Cart
 * Package: com.imooc.mall2.pojo
 *
 * @Author 马学兴
 * @Create 2023/6/22 0:29
 * @Version 1.0
 * Description:
 */
@Data
public class Cart {
    private Integer productId;
    private Integer quantity;
    private Boolean productSelected;

    public Cart() {
    }

    public Cart(Integer productId, Integer quantity, Boolean productSelected) {
        this.productId = productId;
        this.quantity = quantity;
        this.productSelected = productSelected;
    }
}
