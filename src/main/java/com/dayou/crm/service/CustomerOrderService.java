package com.dayou.crm.service;

import com.dayou.crm.base.BaseService;
import com.dayou.crm.dao.CustomerOrderMapper;
import com.dayou.crm.query.CustomerOrderQuery;
import com.dayou.crm.vo.CustomerOrder;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @author: dayou
 * @create: 2022-03-15 22:28
 */
@Service
public class CustomerOrderService extends BaseService<CustomerOrder,Integer> {

    @Resource
    CustomerOrderMapper customerOrderMapper;


    /**
     * 多条件分页查询客户 （返回的数据格式必须满足LayUi中数据表格要求的格式）
     * @param customerOrderQuery
     * @return
     */
    public Map<String, Object> queryCustomerOrderByParams(CustomerOrderQuery customerOrderQuery) {
        Map<String, Object> map = new HashMap<>();

        // 开启分页
        PageHelper.startPage(customerOrderQuery.getPage(), customerOrderQuery.getLimit());
        // 得到对应分页对象
        PageInfo<CustomerOrder> pageInfo = new PageInfo<>(customerOrderMapper.selectByParams(customerOrderQuery));

        // 设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        // 设置分页好的列表
        map.put("data",pageInfo.getList());

        return map;
    }

    public Map<String, Object> queryOrderById(Integer orderId) {
        return customerOrderMapper.queryOrderById(orderId);
    }
}
