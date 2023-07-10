package com.imooc.mall2.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * ClassName: ShippingForm
 * Package: com.imooc.mall2.form
 *
 * @Author 马学兴
 * @Create 2023/6/22 20:46
 * @Version 1.0
 * Description:
 */
@Data
public class ShippingForm {
    @NotBlank
    private String receiverName;
    @NotBlank
    private String receiverPhone;
    @NotBlank
    private String receiverMobile;
    @NotBlank
    private String receiverProvince;
    @NotBlank
    private String receiverCity;
    @NotBlank
    private String receiverDistrict;
    @NotBlank
    private String receiverAddress;
    @NotBlank
    private String receiverZip;
}
