package com.dayou.crm.service;

import com.dayou.crm.base.BaseService;
import com.dayou.crm.dao.ModuleMapper;
import com.dayou.crm.dao.PermissionMapper;
import com.dayou.crm.dao.RoleMapper;
import com.dayou.crm.utils.AssertUtil;
import com.dayou.crm.vo.Permission;
import com.dayou.crm.vo.Role;
import com.dayou.crm.vo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @author: dayou
 * @create: 2022-03-12 10:12
 */
@Service
public class RoleService extends BaseService<Role,Integer> {

    @Resource
    RoleMapper roleMapper;
    @Resource
    PermissionMapper permissionMapper;
    @Resource
    ModuleMapper moduleMapper;

    /**
     * 查询角色列表
     * @return
     */
    public List<Map<String, Object>> queryAllRoles(Integer userId){
        return roleMapper.queryAllRoles(userId);
    }

    /**
     * 角色的添加
     * @param role
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveRole(Role role){
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"请输入角色名!");
        Role temp = roleMapper.queryRoleByRoleName(role.getRoleName());
        AssertUtil.isTrue(null !=temp,"该角色已存在!");
        role.setIsValid(1);
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(insertSelective(role)<1,"角色记录添加失败!");
    }

    /**
     * 角色的更新
     * @param role
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void  updateRole(Role role){

        AssertUtil.isTrue(null==role.getId()||null==selectByPrimaryKey(role.getId())," 待修改的记录不存在!");
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"请输入角色名!");
        Role temp = roleMapper.queryRoleByRoleName(role.getRoleName());
        AssertUtil.isTrue(null !=temp && !(temp.getId().equals(role.getId())),"该角色 已存在!");
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(updateByPrimaryKeySelective(role)<1,"角色记录更新失败!");
    }

    /**
     * 角色的删除(设置表不可用,但实际还存在数据库中)
     * @param roleId
     */
    public void deleteRole(Integer roleId){
        // 判断角色Id是否为空
        AssertUtil.isTrue(roleId == null,"待删除记录不存在！");
        // 通过角色Id查询角色记录
        Role role = roleMapper.selectByPrimaryKey(roleId);
        // 判断角色记录是否存在
        AssertUtil.isTrue(role == null, "待删除记录不存在！");
        // 设置删除记录
        role.setIsValid(0);
        role.setUpdateDate(new Date());
        // 执行更新操作
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role) < 1,"角色删除失败！");
    }

    /**
     * 角色的添加
     * @param mids
     * @param roleId
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addGrant(Integer[] mids, Integer roleId) {
        /**
         * 核心表-t_permission t_role(校验角色存在)
         *   如果角色存在原始权限 删除角色原始权限
         *     然后添加角色新的权限 批量添加权限记录到t_permission
         */
        Role temp =selectByPrimaryKey(roleId);
        AssertUtil.isTrue(null==roleId||null==temp,"待授权的角色不存在!");
        int count = permissionMapper.countPermissionByRoleId(roleId);
        if(count>0){
            permissionMapper.deletePermissionsByRoleId(roleId) ;
        }
        if(null !=mids && mids.length>0){
            List<Permission> permissions=new ArrayList<Permission>();
            for (Integer mid : mids) {
                Permission permission=new Permission();
                permission.setCreateDate(new Date());
                permission.setUpdateDate(new Date());
                permission.setModuleId(mid);
                permission.setRoleId(roleId);

                permission.setAclValue(moduleMapper.selectByPrimaryKey(mid).getOptValue());
                permissions.add(permission);
            }
            permissionMapper.insertBatch(permissions);
        }
    }






}
























