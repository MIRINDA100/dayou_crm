package com.dayou.crm.controller;

import com.dayou.crm.base.BaseController;
import com.dayou.crm.base.BaseService;
import com.dayou.crm.base.ResultInfo;
import com.dayou.crm.query.CustomerQuery;
import com.dayou.crm.service.CustomerOrderService;
import com.dayou.crm.service.CustomerService;
import com.dayou.crm.vo.Customer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Description:
 * @author: dayou
 * @create: 2022-03-15 22:24
 */
@Controller
@RequestMapping("customer")
public class CustomerController extends BaseController {

    @Resource
    private CustomerService customerService;
    @Resource
    private CustomerOrderService orderService;

    /**
     * 显示客户页面
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "customer/customer";
    }

    /**
     * 加载表格信息
     * @param customerQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryCustomersByParams(CustomerQuery customerQuery){
        return customerService.queryByParamsForTable(customerQuery);
    }

    /**
     * 跳转页面
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("toAddOrUpdateCustomerPage")
    public String addOrUpdateSaleChancePage(Integer id, Model model){
        model.addAttribute("customer",customerService.selectByPrimaryKey(id)) ;
        return "customer/add_update";
    }

    /**
     * 添加客户信息
     * @param customer
     * @return
     */
    @RequestMapping("add")
    @ResponseBody
    public ResultInfo addCustomer(Customer customer){
        customerService.addCustomer(customer);
        return success("客户添加成功");
    }

    /**
     * 修改客户信息
     *
     * @param customer
     * @return com.dayou.crm.base.ResultInfo
     */
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateCustomer(Customer customer) {
        customerService.updateCustomer(customer);
        return success("修改客户信息成功！");
    }


    /**
     * 删除客户信息
     *
     * @param id
     * @return ResultInfo
     */
    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteCustomer(Integer id) {
        customerService.deleteCustomer(id);
        return success("删除客户信息成功！");
    }

    /**
     * 打开客户的订单页面
     * @param
     * @return java.lang.String
     */
    @RequestMapping("toCustomerOrderPage")
    public String toCustomerOrderPage(Integer customerId, Model model) {
        // 通过客户ID查询客户记录，设置到请求域中
        model.addAttribute("customer", customerService.selectByPrimaryKey(customerId));
        return "customer/customer_order";
    }

    /**
     * 客户贡献分析
     * @param customerQuery
     * @return
     */
    @RequestMapping("queryCustomerContributionByParams")
    @ResponseBody
    public Map<String,Object> queryCustomerContributionByParams(CustomerQuery customerQuery) {
        return customerService.queryCustomerContributionByParams(customerQuery);
    }


    /**
     * 查询客户构成 （折线图）
     *
     *
     * 乐字节：专注线上IT培训
     * 答疑老师微信：lezijie
     * @param
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @RequestMapping("countCustomerMake")
    @ResponseBody
    public Map<String, Object> countCustomerMake() {
        return customerService.countCustomerMake();
    }


    /**
     * 查询客户构成 （饼状图）
     *
     *
     * 乐字节：专注线上IT培训
     * 答疑老师微信：lezijie
     * @param
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @RequestMapping("countCustomerMake02")
    @ResponseBody
    public Map<String, Object> countCustomerMake02() {
        return customerService.countCustomerMake02();
    }





}
