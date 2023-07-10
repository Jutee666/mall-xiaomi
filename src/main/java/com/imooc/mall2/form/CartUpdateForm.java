package com.imooc.mall2.form;

import lombok.Data;

/**
 * ClassName: CartUpdateForm
 * Package: com.imooc.mall2.form
 *
 * @Author 马学兴
 * @Create 2023/6/22 15:19
 * @Version 1.0
 * Description:
 */
@Data
public class CartUpdateForm {
    private Integer quantity;
    protected  Boolean selected;
}
