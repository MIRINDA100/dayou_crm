package com.dayou.crm.service;

import com.dayou.crm.base.BaseMapper;
import com.dayou.crm.base.BaseService;
import com.dayou.crm.dao.CusDevPlanMapper;
import com.dayou.crm.query.CusDevPlanQuery;
import com.dayou.crm.utils.AssertUtil;
import com.dayou.crm.utils.PhoneUtil;
import com.dayou.crm.vo.CusDevPlan;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @author: dayou
 * @create: 2022-03-10 9:55
 */
@Service
public class CusDevPlanService extends BaseService<CusDevPlan,Integer> {

    @Resource
    private CusDevPlanMapper cusDevPlanMapper;
    @Resource
    private BaseMapper saleChanceMapper;


    /**
     * 多条件查询计划项列表
     * @param cusDevPlanQuery
     * @return
     */
    public Map<String,Object> queryCusDevPlansByParams(CusDevPlanQuery cusDevPlanQuery){
        Map<String,Object> map = new HashMap<String,Object>();
        PageHelper.startPage(cusDevPlanQuery.getPage(),cusDevPlanQuery.getLimit());
        PageInfo<CusDevPlan> pageInfo = new PageInfo<CusDevPlan>(selectByParams(cusDevPlanQuery));
        map.put("code",0);
        map.put("msg","");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return  map;
    }

    /**
     * 保存计划项
     * @param cusDevPlan
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveCusDevPlan(CusDevPlan cusDevPlan){
        //1 参数校验
        checkParams(cusDevPlan.getSaleChanceId(),cusDevPlan.getPlanItem(),cusDevPlan.getPlanDate());
        //2 设置参数默认值
        cusDevPlan.setIsValid(1);
        cusDevPlan.setCreateDate(new Date());
        cusDevPlan.setUpdateDate(new Date());
        //3 执行添加,判断结果
        AssertUtil.isTrue(insertSelective(cusDevPlan) < 1, "计划项记录添加失败！");
    }

    /**
     * 更新计划项
     * @param cusDevPlan
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCusDevPlan(CusDevPlan cusDevPlan){
        AssertUtil.isTrue(null == cusDevPlan.getId() || null == selectByPrimaryKey(cusDevPlan.getId()),"待更新记录不 存在!");
        // 1. 参数校验
        checkParams(cusDevPlan.getSaleChanceId(), cusDevPlan.getPlanItem(),cusDevPlan.getPlanDate());
        // 2. 设置参数默认值
        cusDevPlan.setUpdateDate(new Date());
        // 3. 执行添加，判断结果
        AssertUtil.isTrue(updateByPrimaryKeySelective(cusDevPlan)<1,"计划项记录更新失 败!");
    }

    /**
     * 删除计划项
     * @param id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void delCusDevPlan(Integer id){
        CusDevPlan cusDevPlan =selectByPrimaryKey(id);
        AssertUtil.isTrue(null==id || null ==cusDevPlan ,"待删除记录不存在!");
        cusDevPlan.setIsValid(0);
        AssertUtil.isTrue(updateByPrimaryKeySelective(cusDevPlan)<1,"计划项记录删除失败!");
    }


    /**
     * 参数校验
     * @param saleChanceId
     * @param planItem
     * @param planDate
     */
    private void checkParams(Integer saleChanceId, String planItem, Date planDate) {
        System.out.println(String.valueOf(saleChanceId));
        AssertUtil.isTrue(null == saleChanceId || saleChanceMapper.selectByPrimaryKey(saleChanceId) == null, "请设置营销机会ID！");
        AssertUtil.isTrue(StringUtils.isBlank(planItem), "请输入计划项内容！");
        AssertUtil.isTrue(null == planDate, "请指定计划项日期！");
    }

}












