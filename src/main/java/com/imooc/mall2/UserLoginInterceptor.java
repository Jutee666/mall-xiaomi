package com.imooc.mall2;

import com.imooc.mall2.consts.MallConst;
import com.imooc.mall2.enums.ResponserEnem;
import com.imooc.mall2.exception.UserLoginException;
import com.imooc.mall2.pojo.User;
import com.imooc.mall2.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ClassName: UserLoginInterceptor
 * Package: com.imooc.mall2
 *
 * @Author 马学兴
 * @Create 2023/6/20 17:32
 * @Version 1.0
 * Description:
 */
@Slf4j

public class UserLoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("preHandle...");
        User user =(User)  request.getSession().getAttribute(MallConst.CURRENT_USER);
        if (user==null){
            log.info("user=null");
            throw new UserLoginException();
//            return false;
//            return ResponseVo.error(ResponserEnem.NEED_LOGIN);
        }
        return true;
    }
}
