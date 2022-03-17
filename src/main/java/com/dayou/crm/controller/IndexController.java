package com.dayou.crm.controller;

import com.dayou.crm.base.BaseController;
import com.dayou.crm.dao.PermissionMapper;
import com.dayou.crm.dao.UserMapper;
import com.dayou.crm.service.PermissionService;
import com.dayou.crm.service.UserService;
import com.dayou.crm.utils.LoginUserUtil;
import com.dayou.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description: 系统登录，主页面转发功能
 * @author: dayou
 * @create: 2022-03-06 10:42
 */
@Controller
public class IndexController extends BaseController {

    @Resource
    private UserService userService;
    @Resource
    private PermissionService permissionService;

    /**
     * 系统登录页
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "index";
    }

    /**
     * 系统界面欢迎页
     * @return
     */
    @RequestMapping("welcome")
    public String welcome() {
        return "welcome";
    }


    /**
     * 后端管理主页面
     * 查询登录用户信息并放置到 request 域。
     * 在 main.ftl 中获取作用域中的 user 对象，显示登录用户信息
     * @return
     */
    @RequestMapping("main")
    public String main(HttpServletRequest request) {
        // 通过工具类，从cookie中获取userId
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        // 调用对应Service层的方法，通过userId主键查询用户对象
        request.setAttribute("user",userService.selectByPrimaryKey(userId));
        List<String>  permissions=permissionService.queryUserHasRolesHasPermissions(userId);
        // 将用户对象设置到request作用域中
        request.getSession().setAttribute("permissions", permissions);
        return "main";
    }

}
