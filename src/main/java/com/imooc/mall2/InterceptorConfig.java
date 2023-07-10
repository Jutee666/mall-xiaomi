package com.imooc.mall2;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * ClassName: InterceptorConfig
 * Package: com.imooc.mall2
 *
 * @Author 马学兴
 * @Create 2023/6/20 17:39
 * @Version 1.0
 * Description:
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserLoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login","/user/register","/categories","/products","/products/*");

    }
}
