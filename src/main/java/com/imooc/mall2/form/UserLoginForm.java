package com.imooc.mall2.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * ClassName: UserForm
 * Package: com.imooc.mall2.form
 *
 * @Author 马学兴
 * @Create 2023/6/20 11:25
 * @Version 1.0
 * Description:
 */
@Data
public class UserLoginForm {
    @NotBlank
    private String username;
    @NotBlank
    private String password;

}
