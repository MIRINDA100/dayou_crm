package com.dayou.crm.config;

import com.dayou.crm.interceptors.NoLoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @Description:
 * @author: dayou
 * @create: 2022-03-07 18:12
 */
@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Bean
    public NoLoginInterceptor noLoginInterceptor() {
        return new NoLoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // NoLoginInterceptor用来实现HandlerInterceptor接口的拦截器实例
        registry.addInterceptor(noLoginInterceptor())
                // 用于设置拦截器的过滤路径规则
                .addPathPatterns("/**")
                // 用于设置不需要拦截的过滤规则
                .excludePathPatterns("/index","/user/login","/css/**","/images/**","/js/**","/lib/**");
    }
}














