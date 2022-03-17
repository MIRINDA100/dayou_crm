package com.dayou.crm.aspect;

import com.dayou.crm.annotation.RequiredPermission;
import com.dayou.crm.exceptions.AuthException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Description:
 * @author: dayou
 * @create: 2022-03-15 14:48
 */
@Component
@Aspect
public class PermissionProxy {

    @Resource
    private HttpSession session;

    /**
     * 切面会拦截指定包下的指定注解
     * 拦截com.dayou.crm.annotation.RequiredPermission注解
     * @param pjp
     * @return
     */
    @Around(value = "@annotation(com.dayou.crm.annotation.RequiredPermission)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object result = null;
        //得到当前登录用户拥有的权限 (session 作用域)
        List<String> permissions = (List<String>) session.getAttribute("permissions");
        if(null == permissions || permissions.size() <1){
            //抛出认证异常
            throw new AuthException();
        }
        //得到对应的目标
        MethodSignature  methodSignature = (MethodSignature)pjp.getSignature();
        //得到方法上的注解
        RequiredPermission requiredPermission = methodSignature.getMethod().getDeclaredAnnotation(RequiredPermission.class);
        //判断注解上对应的状态码
        if(!(permissions.contains(requiredPermission.code())))
            //如果权限中不包含当前方法上注解指定的权限码,则抛出异常
            throw new AuthException();


        result = pjp.proceed();
        return result;
    }

}
