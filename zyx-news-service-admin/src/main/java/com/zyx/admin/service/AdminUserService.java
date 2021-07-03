package com.zyx.admin.service;

import com.zyx.pojo.AdminUser;
import com.zyx.pojo.bo.NewAdminBO;
import com.zyx.utils.PagedGridResult;

public interface AdminUserService {

    /**
     * 获得管理员的用户信息
     */
    public AdminUser queryAdminByUsername(String username);

    /**
     * 新增管理员
     */
    public void createAdminUser(NewAdminBO newAdminBO);

    /**
     * 分页查询admin列表
     */
    public PagedGridResult queryAdminList(Integer page, Integer pageSize);

}
