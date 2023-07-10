package com.imooc.pay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * ClassName: WxAccoutConfig
 * Package: com.imooc.pay.config
 *
 * @Author 马学兴
 * @Create 2023/6/19 17:33
 * @Version 1.0
 * Description:
 */
@Component
@ConfigurationProperties(prefix = "wx")
@Data
public class WxAccoutConfig {
    private String appId;
    private String mchId;
    private String mchKey;
    private String notifyUrl;
    private String returnUrl;
}
