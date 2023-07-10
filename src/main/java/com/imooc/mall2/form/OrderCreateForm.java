package com.imooc.mall2.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * ClassName: OrderCreateForm
 * Package: com.imooc.mall2.form
 *
 * @Author 马学兴
 * @Create 2023/6/24 17:40
 * @Version 1.0
 * Description:
 */
@Data
public class OrderCreateForm {
    @NotNull
    private Integer shippingId;
}
