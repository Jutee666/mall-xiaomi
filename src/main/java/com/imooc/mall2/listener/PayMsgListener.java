package com.imooc.mall2.listener;

import com.google.gson.Gson;
import com.imooc.mall2.pojo.PayInfo;
import com.imooc.mall2.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ClassName: PayMsgListener
 * Package: com.imooc.mall2.listener
 *
 * @Author 马学兴
 * @Create 2023/6/25 14:01
 * @Version 1.0
 * Description:关于PayInfo,正确姿势：pay项目提供client.jar,mall项目引用jar包
 */
@Component
@RabbitListener(queues = "payNotify")
@Slf4j
public class PayMsgListener {
    @Autowired
    private IOrderService orderService;
    @RabbitHandler
    public void process(String msg){
      log.info("[接收到消息]{}",msg);
        PayInfo payInfo = new Gson().fromJson(msg, PayInfo.class);
        if (payInfo.getPlatformStatus().equals("SUCCESS")){
            //修改订单里的状态
            orderService.paid(payInfo.getOrderNo());
        }
    }
}
