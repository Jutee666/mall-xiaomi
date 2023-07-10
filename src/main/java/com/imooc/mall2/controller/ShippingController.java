package com.imooc.mall2.controller;

import com.imooc.mall2.consts.MallConst;
import com.imooc.mall2.form.ShippingForm;
import com.imooc.mall2.pojo.User;
import com.imooc.mall2.service.IShippintService;
import com.imooc.mall2.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * ClassName: ShippingController
 * Package: com.imooc.mall2.controller
 *
 * @Author 马学兴
 * @Create 2023/6/23 11:49
 * @Version 1.0
 * Description:
 */
@RestController
public class ShippingController {

    @Autowired
    private IShippintService shippintService;
    @PostMapping("/shippings")
    public ResponseVo add(@Valid @RequestBody ShippingForm form, HttpSession session){
        User user =(User) session.getAttribute(MallConst.CURRENT_USER);
        return shippintService.add(user.getId(),form);
    }

    @DeleteMapping("/shippings/{shippingId}")
    public ResponseVo delete(@PathVariable Integer shippingId, HttpSession session){
        User user =(User) session.getAttribute(MallConst.CURRENT_USER);
        return shippintService.delete(user.getId(),shippingId);
    }
    @PutMapping("/shippings/{shippingId}")
    public ResponseVo update(@PathVariable Integer shippingId, HttpSession session, @Valid @RequestBody ShippingForm form){
        User user =(User) session.getAttribute(MallConst.CURRENT_USER);
        return shippintService.update(user.getId(),shippingId,form);
    }
    @GetMapping("/shippings")
    public ResponseVo list(@RequestParam(required = false,defaultValue = "1")Integer pageNum,
                           @RequestParam(required = false,defaultValue = "10")Integer pageSize,
                           HttpSession session){
        User user =(User) session.getAttribute(MallConst.CURRENT_USER);
        return shippintService.list(user.getId(),pageNum,pageSize);
    }


}
