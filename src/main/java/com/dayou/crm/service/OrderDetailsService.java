package com.dayou.crm.service;

import com.dayou.crm.base.BaseService;
import com.dayou.crm.dao.OrderDetailsMapper;
import com.dayou.crm.query.OrderDetailsQuery;
import com.dayou.crm.vo.OrderDetails;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @author: dayou
 * @create: 2022-03-16 11:02
 */
@Service
public class OrderDetailsService extends BaseService<OrderDetails,Integer> {


    @Resource
    private OrderDetailsMapper orderDetailsMapper;

    /**
     * 分页条件查询订单详情列表
     * @param orderDetailsQuery
     * @return
     */
    public Map<String, Object> queryOrderDetailsByParams(OrderDetailsQuery orderDetailsQuery) {
        Map<String, Object> map = new HashMap<>();

        // 开启分页
        PageHelper.startPage(orderDetailsQuery.getPage(), orderDetailsQuery.getLimit());
        // 得到对应分页对象
        PageInfo<OrderDetails> pageInfo = new PageInfo<>(orderDetailsMapper.selectByParams(orderDetailsQuery));

        // 设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        // 设置分页好的列表
        map.put("data",pageInfo.getList());

        return map;
    }
}
