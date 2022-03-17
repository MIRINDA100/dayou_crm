package com.dayou.crm.controller;

import com.dayou.crm.base.BaseController;
import com.dayou.crm.base.ResultInfo;
import com.dayou.crm.exceptions.ParamsException;
import com.dayou.crm.model.UserModel;
import com.dayou.crm.query.UserQuery;
import com.dayou.crm.service.UserService;
import com.dayou.crm.utils.LoginUserUtil;
import com.dayou.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @author: dayou
 * @create: 2022-03-06 19:58
 */
@Controller
@RequestMapping("user")
public class UserController extends BaseController {

    @Resource
    private UserService userService;


    /**
     * 用户登录
     * @param userName
     * @param userPwd
     * @return
     */
    @PostMapping("login")
    @ResponseBody
    public ResultInfo userLogin (String userName, String userPwd) {

        ResultInfo resultInfo = new ResultInfo();

        //捕获 Service 层抛出的异常
        try {
            // 调用Service层的登录方法，得到返回的用户对象
            UserModel userModel = userService.userLogin(userName, userPwd);
            /**
             * 登录成功后，有两种处理：
             * 1. 将用户的登录信息存入 Session （ 问题：重启服务器，Session 失效，客户端
             需要重复登录 ）
             * 2. 将用户信息返回给客户端，由客户端（Cookie）保存
             */
            // 将返回的UserModel对象设置到 ResultInfo 对象中
            resultInfo.setResult(userModel);
        } catch (ParamsException e) { // 自定义异常
            e.printStackTrace();
            // 设置状态码和提示信息
            resultInfo.setCode(e.getCode());
            resultInfo.setMsg(e.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("操作失败！");
        }
        return resultInfo;

    }

    @RequestMapping("toPasswordPage")
    public String toPasswordPage(){
        return "user/password";
    }



    @PostMapping("updatePassword")
    @ResponseBody
    public ResultInfo updateUserPassword (HttpServletRequest request, String oldPassword, String newPassword, String confirmPassword) {
        ResultInfo resultInfo = new ResultInfo();
        // 通过 try catch 捕获 Service 层抛出的异常
        try {
            // 获取userId
            Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
            // 调用Service层的密码修改方法
            userService.updateUserPassword(userId, oldPassword, newPassword, confirmPassword);
        } catch (ParamsException e) { // 自定义异常
            e.printStackTrace();
            // 设置状态码和提示信息
            resultInfo.setCode(e.getCode());
            resultInfo.setMsg(e.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("操作失败！");
        }
        return resultInfo;
    }

    /**
     * 查询所有的销售人员
     * @return
     */
    @RequestMapping("queryAllSales")
    @ResponseBody
    public List<Map<String, Object>> queryAllSales() {
        return userService.queryAllSales();
    }

    /**
     * 多条件查询用户数据
     * @param userQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> queryUserByParams(UserQuery userQuery) {
        return userService.queryUserByParams(userQuery);
    }

    /**
     * 进入用户页面
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "user/user";
    }

    /**
     * 添加用户
     * @param user
     * @return
     */
    @RequestMapping("save")
    @ResponseBody
    public ResultInfo saveUser(User user) {
        userService.saveUser(user);
        return success("用户添加成功！");
    }


    /**
     * 更新用户
     * @param user
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateUser(User user) {
        userService.updateUser(user);
        return success("用户更新成功！");
    }

    /**
     * 进入用户添加或更新页面
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("addOrUpdateUserPage")
    public String addUserPage(Integer id, Model model){
        if(null != id){
            model.addAttribute("userInfo",userService.selectByPrimaryKey(id));
        }
        return "user/add_update";
    }

    /**
     * 删除用户
     * @param ids
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteUser(Integer[] ids){
        userService.deleteBatch(ids);
        return success("用户记录删除成功");
    }


    /**
     * 查询所有的客户经理
     * @param
     * @return
     */
    @RequestMapping("queryAllCustomerManagers")
    @ResponseBody
    public List<Map<String,Object>> queryAllCustomerManagers(){
        return userService.queryAllCustomerManagers();
    }





}








