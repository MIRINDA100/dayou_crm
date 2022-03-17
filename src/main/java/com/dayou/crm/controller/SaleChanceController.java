package com.dayou.crm.controller;

import com.dayou.crm.annotation.RequiredPermission;
import com.dayou.crm.base.BaseController;
import com.dayou.crm.base.ResultInfo;
import com.dayou.crm.query.SaleChanceQuery;
import com.dayou.crm.service.SaleChanceService;
import com.dayou.crm.utils.CookieUtil;
import com.dayou.crm.utils.LoginUserUtil;
import com.dayou.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Description:
 * @author: dayou
 * @create: 2022-03-07 22:02
 */
@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {

    @Resource
    private SaleChanceService saleChanceService;

    /**
     * 显示营销机会首页
     * @return
     */
    @RequiredPermission(code="1010")
    @RequestMapping("index")
    public String index(){
        return "saleChance/sale_chance";
    }

    /**
     * 多条件分页查询营销机会
     * @param query
     * @return
     */
    @RequiredPermission(code="101001")
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> querySaleChanceByParams (SaleChanceQuery query,Integer flag, HttpServletRequest request) {
        // 查询参数 flag=1 代表当前查询为开发计划数据，设置查询分配人参数
        if (null != flag && flag == 1) {
            // 获取当前登录用户的ID
            Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
            query.setAssignMan(userId);
        }

        return saleChanceService.querySaleChanceByParams(query);
    }

    /**
     * 机会数据添加与更新表单页面视图转发
     *     id为空 添加操作
     *     id非空 修改操作
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("addOrUpdateSaleChancePage")
    public String addOrUpdateSaleChancePage(Integer id, Model model){
        // 如果id不为空，表示是修改操作，修改操作需要查询被修改的数据
        if (null != id) {
            // 通过主键查询营销机会数据
            SaleChance saleChance = saleChanceService.selectByPrimaryKey(id);
            // 将数据存到作用域中
            model.addAttribute("saleChance", saleChance);
        }
        return "saleChance/add_update";

    }


    /**
     * 添加营销机会数据
     * @param request
     * @param saleChance
     * @return
     */
    @RequiredPermission(code="101002")
    @RequestMapping("save")
    @ResponseBody
    public ResultInfo saveSaleChance(HttpServletRequest request, SaleChance saleChance){
        // 从cookie中获取用户姓名
        String userName = CookieUtil.getCookieValue(request, "userName");
        // 设置营销机会的创建人
        saleChance.setCreateMan(userName);
        // 添加营销机会的数据
        saleChanceService.saveSaleChance(saleChance);
        return success("营销机会数据添加成功！");
    }



    /**
     * 更新营销机会数据
     * @param request
     * @param saleChance
     * @return
     */
    @RequiredPermission(code="101004")
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateSaleChance(HttpServletRequest request, SaleChance saleChance){
        // 更新营销机会的数据
        saleChanceService.updateSaleChance(saleChance);
        return success("营销机会数据更新成功！");
    }

    /**
     * 删除营销机会数据
     * @param ids
     * @return
     */
    @RequiredPermission(code="101003")
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteSaleChance (Integer[] ids) {
        // 删除营销机会的数据
        saleChanceService.deleteBatch(ids);
        return success("营销机会数据删除成功！");
    }

    /**
     * 更新营销机会的开发状态
     * @param id
     * @param devResult
     * @return
     */
    @RequestMapping("updateSaleChanceDevResult")
    @ResponseBody
    public ResultInfo updateSaleChanceDevResult(Integer id,Integer devResult){
        saleChanceService.updateSaleChanceDevResult(id,devResult);
        return success("开发状态更新成功！");
    }


}















