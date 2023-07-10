package com.imooc.mall2.service;

import com.imooc.mall2.pojo.User;
import com.imooc.mall2.vo.ResponseVo;

/**
 * ClassName: IUserService
 * Package: com.imooc.mall2.service
 *
 * @Author 马学兴
 * @Create 2023/6/19 22:40
 * @Version 1.0
 * Description:
 */
public interface IUserService {
    //注册
    ResponseVo<User> register(User user);
    //登录
    ResponseVo<User> login(String username,String password);

}
