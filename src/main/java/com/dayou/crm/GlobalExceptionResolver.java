package com.dayou.crm;

import com.alibaba.fastjson.JSON;
import com.dayou.crm.base.ResultInfo;
import com.dayou.crm.exceptions.AuthException;
import com.dayou.crm.exceptions.NoLoginException;
import com.dayou.crm.exceptions.ParamsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 控制层的方法返回的内容两种情况
 *   1. 视图:视图异常
 *   2. Json:方法执行错误 返回错误json信息
 * @Description:全局异常统一处理
 * @author: dayou
 * @create: 2022-03-07 17:21
 */
@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {

    /**
     * 方法返回值类型
     *   视图
     *   JSON
     *   如果方法级别配置了 @ResponseBody 注解，表示方法返回的是JSON；
     * 反之，返回的是视图页面
     * @param request
     * @param response
     * @param o
     * @param e
     * @return
     */

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) {

        // 判断异常类型,如果是未登录异常，则先执行相关的拦截操作
        if (e instanceof NoLoginException) {
            // 如果捕获的是未登录异常，则重定向到登录页面
            ModelAndView mv = new ModelAndView("redirect:/index");
            return mv;
        }

        // 设置默认异常处理
        ModelAndView mv = new ModelAndView();
        mv.setViewName("");
        mv.addObject("code", 400);
        mv.addObject("msg", "系统异常，请稍后再试...");

        // 判断 HandlerMethod
        if (o instanceof HandlerMethod) {
            // 类型转换
            HandlerMethod handlerMethod = (HandlerMethod) o;
            // 获取方法上的 ResponseBody 注解
            ResponseBody responseBody = handlerMethod.getMethod().getDeclaredAnnotation(ResponseBody.class);
            // 判断 ResponseBody 注解是否存在 (如果不存在，表示返回的是视图;如果存在，表示返回的是JSON)
            if (null == responseBody) {
                /**
                 * 方法返回视图
                 */
                //判断异常类型
                if (e instanceof ParamsException) {
                    ParamsException pe = (ParamsException) e;
                    mv.addObject("code", pe.getCode());
                    mv.addObject("msg", pe.getMsg());
                }else if(e instanceof AuthException){//认证异常
                    AuthException a = (AuthException) e;
                    mv.addObject("code", a.getCode());
                    mv.addObject("msg", a.getMsg());
                }
                return mv;
            } else {
                /**
                 * 方法上返回JSON
                 */
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setCode(300);
                resultInfo.setMsg("系统异常，请重试！");

                // 如果捕获的是自定义异常
                if (e instanceof ParamsException) {
                    ParamsException pe = (ParamsException) e;
                    resultInfo.setCode(pe.getCode());
                    resultInfo.setMsg(pe.getMsg());
                }else if (e instanceof AuthException) { //认证异常
                    AuthException a = (AuthException) e;
                    resultInfo.setCode(a.getCode());
                    resultInfo.setMsg(a.getMsg());
                }



                // 设置响应类型和编码格式 （响应JSON格式）
                response.setContentType("application/json;charset=utf-8");
                // 得到输出流
                PrintWriter out = null;
                try {
                    out = response.getWriter();
                    // 将对象转换成JSON格式，通过输出流输出
                    out.write(JSON.toJSONString(resultInfo));
                    out.flush();
                } catch (Exception exception) {
                    exception.printStackTrace();
                } finally {
                    if (out != null) {
                        out.close();
                    }
                }
                return null;
            }
        }
        return mv;
    }
}
