package com.dayou.crm.service;

import com.dayou.crm.base.BaseService;
import com.dayou.crm.dao.CustomerMapper;
import com.dayou.crm.dao.CustomerServeMapper;
import com.dayou.crm.dao.UserMapper;
import com.dayou.crm.enums.CustomerServeStatus;
import com.dayou.crm.query.CustomerServeQuery;
import com.dayou.crm.utils.AssertUtil;
import com.dayou.crm.vo.CustomerServe;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Description:
 * @author: dayou
 * @create: 2022-03-16 21:15
 */
@Service
public class CustomerServeService extends BaseService<CustomerServe,Integer> {

    @Resource
    private CustomerServeMapper customerServeMapper;
    @Resource
    private CustomerMapper customerMapper;
    @Resource
    private UserMapper userMapper;


    /**
     * 多条件分页查询服务数据列表
     * @param
     * @return
     */
    public Map<String, Object> queryCustomerServeByParams(CustomerServeQuery customerServeQuery) {
        Map<String, Object> map = new HashMap<>();

        // 开启分页
        PageHelper.startPage(customerServeQuery.getPage(), customerServeQuery.getLimit());
        // 得到对应分页对象
        PageInfo<CustomerServe> pageInfo = new PageInfo<>(customerServeMapper.selectByParams(customerServeQuery));

        // 设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        // 设置分页好的列表
        map.put("data",pageInfo.getList());

        return map;
    }


    /**
     * 添加服务操作
     * @param customerServe
     * @return void
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addCustomerServe(CustomerServe customerServe) {

        /* 1. 参数校验 */
        // 客户名 customer     非空
        AssertUtil.isTrue(StringUtils.isBlank(customerServe.getCustomer()), "客户名不能为空！");
        // 客户名 customer     客户表中存在客户记录
        AssertUtil.isTrue(customerMapper.queryCustomerByName(customerServe.getCustomer()) == null, "客户不存在！" );

        // 服务类型 serveType  非空
        AssertUtil.isTrue(StringUtils.isBlank(customerServe.getServeType()), "请选择服务类型！");

        //  服务请求内容  serviceRequest  非空
        AssertUtil.isTrue(StringUtils.isBlank(customerServe.getServiceRequest()), "服务请求内容不能为空！");

        /* 2. 设置参数的默认值 */
        //  服务状态    服务创建状态  fw_001
        customerServe.setState(CustomerServeStatus.CREATED.getState());
        customerServe.setIsValid(1);
        customerServe.setCreateDate(new Date());
        customerServe.setUpdateDate(new Date());

        /* 2. 执行添加操作，判断受影响的行数 */
        AssertUtil.isTrue(customerServeMapper.insertSelective(customerServe) < 1, "添加服务失败！");
    }


    /**
     * 服务分配/服务处理/服务反馈
     * @param customerServe
     * @return void
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCustomerServe(CustomerServe customerServe) {
        // 客户服务ID  非空且记录存在
        AssertUtil.isTrue(customerServe.getId() == null
                || customerServeMapper.selectByPrimaryKey(customerServe.getId()) == null, "待更新的服务记录不存在！");

        // 判断客户服务的服务状态
        if (CustomerServeStatus.ASSIGNED.getState().equals(customerServe.getState())) {
            // 服务分配操作
            // 分配人       非空，分配用户记录存在
            AssertUtil.isTrue(StringUtils.isBlank(customerServe.getAssigner()), "待分配用户不能为空！");
            AssertUtil.isTrue(userMapper.selectByPrimaryKey(Integer.parseInt(customerServe.getAssigner())) == null, "待分配用户不存在！");
            // 分配时间     系统当前时间
            customerServe.setAssignTime(new Date());


        } else if (CustomerServeStatus.PROCED.getState().equals(customerServe.getState())) {
            // 服务处理操作
            // 服务处理内容   非空
            AssertUtil.isTrue(StringUtils.isBlank(customerServe.getServiceProce()), "服务处理内容不能为空！");
            // 服务处理时间   系统当前时间
            customerServe.setServiceProceTime(new Date());

        } else if (CustomerServeStatus.FEED_BACK.getState().equals(customerServe.getState())) {
            // 服务反馈操作
            // 服务反馈内容   非空
            AssertUtil.isTrue(StringUtils.isBlank(customerServe.getServiceProceResult()), "服务反馈内容不能为空！");
            // 服务满意度     非空
            AssertUtil.isTrue(StringUtils.isBlank(customerServe.getMyd()), "请选择服务反馈满意度！");
            // 服务状态      设置为 服务归档状态 fw_005
            customerServe.setState(CustomerServeStatus.ARCHIVED.getState());

        }

        // 更新时间     系统当前时间
        customerServe.setUpdateDate(new Date());

        // 执行更新操作，判断受影响的行数
        AssertUtil.isTrue(customerServeMapper.updateByPrimaryKeySelective(customerServe)< 1, "服务更新失败！");

    }


    /**
     * 查询客户服务构成 （折线图数据处理）
     */
    public Map<String, Object> countCustomerServeMake() {
        Map<String, Object> map = new HashMap<>();
        // 查询客户构成数据的列表
        List<Map<String,Object>> dataList = customerServeMapper.countCustomerServeMake();
        // 折线图X轴数据  数组
        List<String> data1 = new ArrayList<>();
        // 折线图Y轴数据  数组
        List<Integer> data2 = new ArrayList<>();

        // 判断数据列表 循环设置数据
        if (dataList != null && dataList.size() > 0) {
            // 遍历集合
            dataList.forEach(m -> {
                // 获取"level"对应的数据，设置到X轴的集合中
                data1.add(m.get("dicValue").toString());
                // 获取"total"对应的数据，设置到Y轴的集合中
                data2.add(Integer.parseInt(m.get("total").toString()));
            });
        }

        // 将X轴的数据集合与Y轴的数据集合，设置到map中
        map.put("data1",data1);
        map.put("data2",data2);

        return map;
    }

    /**
     * 查询客户服务构成 （饼状图数据处理）
     */
    public Map<String, Object> countCustomerServeMake02() {
        Map<String, Object> map = new HashMap<>();
        // 查询客户构成数据的列表
        List<Map<String,Object>> dataList = customerServeMapper.countCustomerServeMake();
        // 饼状图数据   数组（数组中是字符串）
        List<String> data1 = new ArrayList<>();
        // 饼状图的数据  数组（数组中是对象）
        List<Map<String, Object>> data2 = new ArrayList<>();

        // 判断数据列表 循环设置数据
        if (dataList != null && dataList.size() > 0) {
            // 遍历集合
            dataList.forEach(m -> {
                // 饼状图数据   数组（数组中是字符串）
                data1.add(m.get("dicValue").toString());
                // 饼状图的数据  数组（数组中是对象）
                Map<String,Object> dataMap = new HashMap<>();
                dataMap.put("name", m.get("dicValue"));
                dataMap.put("value", m.get("total"));
                data2.add(dataMap);
            });
        }

        // 将X轴的数据集合与Y轴的数据集合，设置到map中
        map.put("data1",data1);
        map.put("data2",data2);

        return map;
    }
}