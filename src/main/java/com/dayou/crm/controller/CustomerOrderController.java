package com.dayou.crm.controller;

import com.dayou.crm.base.BaseController;
import com.dayou.crm.query.CustomerOrderQuery;
import com.dayou.crm.service.CustomerOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Description:
 * @author: dayou
 * @create: 2022-03-16 10:39
 */
@Controller
@RequestMapping("order")
public class CustomerOrderController extends BaseController {

    @Resource
    private CustomerOrderService customerOrderService;

    /**
     * 分页多条件查询客户订单列表
     * @param
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryCustomerOrderByParams(CustomerOrderQuery customerOrderQuery) {
        return customerOrderService.queryCustomerOrderByParams(customerOrderQuery);
    }

    /**
     * 打开订单详情的页面
     * @param orderId
     * @param model
     * @return
     */
    @RequestMapping("toOrderDetailPage")
    public String toOrderDetailPage(Integer orderId, Model model) {

        // 通过订单ID查询对应的订单记录
        Map<String,Object> map = customerOrderService.queryOrderById(orderId);
        // 将数据设置到请求域中
        model.addAttribute("order",map);

        return "customer/customer_order_detail";
    }


}
