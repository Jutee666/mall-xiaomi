package com.imooc.mall2.service.impl;

import com.imooc.mall2.dao.UserMapper;
import com.imooc.mall2.enums.ResponserEnem;
import com.imooc.mall2.enums.RoleEnum;
import com.imooc.mall2.pojo.User;
import com.imooc.mall2.service.IUserService;
import com.imooc.mall2.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static com.imooc.mall2.enums.ResponserEnem.*;

/**
 * ClassName: UserServiceImpl
 * Package: com.imooc.mall2.service.impl
 *
 * @Author 马学兴
 * @Create 2023/6/19 22:44
 * @Version 1.0
 * Description:
 */
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
   private UserMapper userMapper;

    @Override
    public ResponseVo<User> register(User user) {
       // error();
        //username不能重复
        int countByUsername = userMapper.countByUsername(user.getUsername());
        if (countByUsername>0){
            return ResponseVo.error(USERNAME_EXIST);
        }
        //email不能重复
        int countByEmail = userMapper.countByEmail(user.getEmail());
        if (countByEmail>0){
            return ResponseVo.error(EMAIL_EXIST);
        }
        user.setRole(RoleEnum.CUSTOMER.getCode());
        //MD5摘要算法(spring自带)
         user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8)));
        //写入数据库
        int resultCount = userMapper.insertSelective(user);
        if (resultCount==0){
            return ResponseVo.error(ERROR);
        }
        return ResponseVo.success();
    }

    @Override
    public ResponseVo<User> login(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if (user==null){
            //用户不存在
            return ResponseVo.error(USERNAME_OR_PASSWORD_ERROR);
        }
        if (!user.getPassword().equalsIgnoreCase(
                DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8)))) {
            //密码错误
            return ResponseVo.error(PASSWORD_ERROR);
        }
        user.setPassword("");
        return ResponseVo.success(user);
    }


    private void error(){throw new RuntimeException("意外错误");}
}
