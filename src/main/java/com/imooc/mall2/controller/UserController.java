package com.imooc.mall2.controller;

import com.imooc.mall2.consts.MallConst;
import com.imooc.mall2.enums.ResponserEnem;
import com.imooc.mall2.form.UserLoginForm;
import com.imooc.mall2.form.UserRegisterForm;
import com.imooc.mall2.pojo.User;
import com.imooc.mall2.service.IUserService;
import com.imooc.mall2.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Objects;

import static com.imooc.mall2.enums.ResponserEnem.PARAM_ERROR;

/**
 * ClassName: UserController
 * Package: com.imooc.mall2.controller
 *
 * @Author 马学兴
 * @Create 2023/6/20 0:37
 * @Version 1.0
 * Description:
 */
@RestController
@Slf4j
public class UserController {
    @Autowired
    private IUserService userService;

    @PostMapping("/user/register")
    public ResponseVo<User> register(@Valid @RequestBody UserRegisterForm userRegisterForm){

        User user = new User();
        BeanUtils.copyProperties(userRegisterForm,user);
        return userService.register(user);
    }

    @PostMapping("/user/login")
    public ResponseVo<User> login(@Valid @RequestBody UserLoginForm userLoginForm,
                                  HttpSession session ) {

        ResponseVo<User> userResponseVo = userService.login(userLoginForm.getUsername(), userLoginForm.getPassword());
        //设置session
        session.setAttribute(MallConst.CURRENT_USER,userResponseVo.getData());
        log.info("/login sessionId={}",session.getId());
        return userResponseVo;
    }
    //session保存在内存里,改进版本token+redis
    @GetMapping("/user")
    public ResponseVo<User> userInfo(HttpSession session){
        log.info("/user sessionId={}",session.getId());
        User user =(User) session.getAttribute(MallConst.CURRENT_USER);
        return ResponseVo.success(user);
    }
    @PostMapping("/user/logout")
    public ResponseVo logout(HttpSession session){
        log.info("/user/logout sessionId={}",session.getId());
        session.removeAttribute(MallConst.CURRENT_USER);
        return ResponseVo.success();
    }
}
