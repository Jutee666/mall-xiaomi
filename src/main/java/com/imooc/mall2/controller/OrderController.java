package com.imooc.mall2.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.mall2.consts.MallConst;
import com.imooc.mall2.form.OrderCreateForm;
import com.imooc.mall2.pojo.User;
import com.imooc.mall2.service.IOrderService;
import com.imooc.mall2.vo.OrderVo;
import com.imooc.mall2.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * ClassName: OrderController
 * Package: com.imooc.mall2.controller
 *
 * @Author 马学兴
 * @Create 2023/6/24 16:40
 * @Version 1.0
 * Description:
 */
@RestController
public class OrderController {
    @Autowired
    private IOrderService orderService;
    @PostMapping("/orders")
    public ResponseVo<OrderVo> create(@Valid @RequestBody OrderCreateForm form,
                                      HttpSession session){
        User user =(User) session.getAttribute(MallConst.CURRENT_USER);
        return orderService.create(user.getId(), form.getShippingId());
    }

    @GetMapping("/orders")
    public ResponseVo<PageInfo> list(@RequestParam(required = false,defaultValue = "1")Integer pageNum,
                                     @RequestParam(required = false,defaultValue = "10") Integer pageSize,
                                     HttpSession session){
        User user =(User) session.getAttribute(MallConst.CURRENT_USER);
        return orderService.list(user.getId(), pageNum,pageSize);
    }
    @GetMapping("/orders/{orderNo}")
    public ResponseVo<OrderVo> detail(@PathVariable Long orderNo,
                                     HttpSession session){
        User user =(User) session.getAttribute(MallConst.CURRENT_USER);
        return orderService.detail(user.getId(), orderNo);
    }

    @PutMapping("/orders/{orderNo}")
    public ResponseVo cancel(@PathVariable Long orderNo,
                             HttpSession session){
        User user =(User) session.getAttribute(MallConst.CURRENT_USER);
        return orderService.cancel(user.getId(), orderNo);
    }
}
