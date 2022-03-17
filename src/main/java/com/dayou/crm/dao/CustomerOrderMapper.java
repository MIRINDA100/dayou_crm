package com.dayou.crm.dao;

import com.dayou.crm.base.BaseMapper;
import com.dayou.crm.vo.CustomerOrder;

import java.util.Map;

public interface CustomerOrderMapper extends BaseMapper<CustomerOrder,Integer> {

    // 通过订单ID查询订单记录
    Map<String, Object> queryOrderById(Integer orderId);

    CustomerOrder queryLossCustomerOrderByCustomerId(Integer id);
}