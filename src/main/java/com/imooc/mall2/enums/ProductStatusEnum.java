package com.imooc.mall2.enums;

import lombok.Data;
import lombok.Getter;

/**
 * ClassName: ProductStatusEnum
 * Package: com.imooc.mall2.enums
 *
 * @Author 马学兴
 * @Create 2023/6/21 16:45
 * @Version 1.0
 * Description:
 */
@Getter
public enum ProductStatusEnum {
    ON_SALE(1),
    OFF_SALE(2),
    DELETE(3),
    ;
    Integer code;

    ProductStatusEnum(Integer code) {
        this.code = code;
    }
}
