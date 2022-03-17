package com.dayou.crm.dao;

import com.dayou.crm.base.BaseMapper;
import com.dayou.crm.vo.Role;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseMapper<Role,Integer> {

    // 查询角色列表 (返回id和roleName)
    public List<Map<String,Object>> queryAllRoles(Integer userId);

    // 通过角色名查询角色记录
    public Role queryRoleByRoleName(String roleName);

}