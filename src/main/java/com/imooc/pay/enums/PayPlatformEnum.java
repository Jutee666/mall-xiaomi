package com.imooc.pay.enums;

import com.lly835.bestpay.enums.BestPayTypeEnum;

/**
 * ClassName: payPlatformEnum
 * Package: com.imooc.pay.enums
 *
 * @Author 马学兴
 * @Create 2023/6/19 14:01
 * @Version 1.0
 * Description:
 */
public enum PayPlatformEnum {
    //1.支付宝，2微信
    ALIPAY(1),
        WX(2),
    ;
    Integer code;

    PayPlatformEnum(Integer code) {
        this.code = code;
    }
    public static PayPlatformEnum getByBestPayTypeEnum(BestPayTypeEnum bestPayTypeEnum){
//        if (bestPayTypeEnum.getPlatform().name().equals( PayPlatformEnum.ALIPAY.name())) {
//            return PayPlatformEnum.ALIPAY;
//        } else if (bestPayTypeEnum.getPlatform().name().equals( PayPlatformEnum.WX.name())) {
//            return PayPlatformEnum.WX;
//        }
        for (PayPlatformEnum payPlatformEnum :PayPlatformEnum.values()) {
            if (bestPayTypeEnum.getPlatform().name().equals(payPlatformEnum.name())){
                return payPlatformEnum;
            }
        }
        throw new RuntimeException("错误的支付平台:"+bestPayTypeEnum.name());
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
