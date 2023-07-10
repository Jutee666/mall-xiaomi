package com.imooc.mall2.enums;

import lombok.Getter;

/**
 * ClassName: PaymentType
 * Package: com.imooc.mall2.enums
 *
 * @Author 马学兴
 * @Create 2023/6/23 16:22
 * @Version 1.0
 * Description:
 */
@Getter

public enum PaymentTypeEnum {
    PAY_ONLINE(1),
    ;
    Integer code;

    PaymentTypeEnum(Integer code) {
        this.code = code;
    }
}
