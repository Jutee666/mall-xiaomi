package com.imooc.pay.config;

import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.service.BestPayService;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * ClassName: BestPayConfig
 * Package: com.imooc.pay.config
 *
 * @Author 马学兴
 * @Create 2023/6/19 10:55
 * @Version 1.0
 * Description:
 */
@Component
public class BestPayConfig {
    @Autowired
    private WxAccoutConfig wxAccoutConfig;

    @Bean
    public BestPayService bestPayService(WxPayConfig wxPayConfig){
        BestPayServiceImpl bestPayService=new BestPayServiceImpl();
        bestPayService.setWxPayConfig(wxPayConfig);
        return bestPayService;
    }

    @Bean
    public WxPayConfig wxPayConfig(){
        WxPayConfig wxPayConfig = new WxPayConfig();
        wxPayConfig.setAppId(wxAccoutConfig.getAppId());//公众号id
        wxPayConfig.setMchId(wxAccoutConfig.getMchId());//商户id和key
        wxPayConfig.setMchKey(wxAccoutConfig.getMchKey());
        //NotifyUrl不用在微信后台设置，不一定要用域名
        //同一局域网可以访问，云服务可行，家庭宽带不行
        wxPayConfig.setNotifyUrl(wxAccoutConfig.getNotifyUrl());
        wxPayConfig.setReturnUrl(wxAccoutConfig.getReturnUrl());
        return wxPayConfig;
    }
}
