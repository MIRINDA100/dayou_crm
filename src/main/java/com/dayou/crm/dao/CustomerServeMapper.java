package com.dayou.crm.dao;

import com.dayou.crm.base.BaseMapper;
import com.dayou.crm.vo.CustomerServe;

import java.util.List;
import java.util.Map;

public interface CustomerServeMapper extends BaseMapper<CustomerServe,Integer> {


    // 查询客户服务组成
    List<Map<String, Object>> countCustomerServeMake();
}