package com.dayou.crm.service;

import com.dayou.crm.base.BaseService;
import com.dayou.crm.dao.PermissionMapper;
import com.dayou.crm.vo.Permission;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description:
 * @author: dayou
 * @create: 2022-03-15 9:56
 */
@Service
public class PermissionService extends BaseService<Permission,Integer> {

    @Resource
    private PermissionMapper permissionMapper;


    public List<String> queryUserHasRolesHasPermissions(Integer userId) {
        return permissionMapper.queryUserHasRoleHasPermissionByUserId(userId);
    }
}
