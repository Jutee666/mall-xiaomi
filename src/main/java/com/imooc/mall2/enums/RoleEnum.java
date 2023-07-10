package com.imooc.mall2.enums;

import lombok.Getter;

/**
 * ClassName: RoleEnum
 * Package: com.imooc.mall2.enums
 *
 * @Author 马学兴
 * @Create 2023/6/19 23:09
 * @Version 1.0
 * Description:
 */
@Getter
public enum RoleEnum {
    ADMIN(0),
    CUSTOMER(1),;
    Integer code;

    RoleEnum(Integer code) {
        this.code = code;
    }
}
