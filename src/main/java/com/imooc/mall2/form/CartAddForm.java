package com.imooc.mall2.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * ClassName: CartAddForm
 * Package: com.imooc.mall2.form
 *
 * @Author 马学兴
 * @Create 2023/6/21 22:37
 * @Version 1.0
 * Description:添加商品
 */
@Data
public class CartAddForm {
    @NotNull
    private Integer productId;

    private Boolean selected=true;
}
