package com.dayou.crm.controller;

import com.dayou.crm.base.BaseController;
import com.dayou.crm.query.OrderDetailsQuery;
import com.dayou.crm.service.OrderDetailsService;
import com.dayou.crm.vo.OrderDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Description:
 * @author: dayou
 * @create: 2022-03-16 11:00
 */
@Controller
@RequestMapping("order_details")
public class OrderDetailsController extends BaseController {

    @Resource
    private OrderDetailsService orderDetailsService;

    /**
     * 分页条件查询订单详情列表
     * @param orderDetailsQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryOrderDetailsByParams(OrderDetailsQuery orderDetailsQuery) {
        return orderDetailsService.queryOrderDetailsByParams(orderDetailsQuery);
    }

}
