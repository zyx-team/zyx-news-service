package com.zyx.admin.service;

import com.zyx.pojo.AdminUser;
import com.zyx.pojo.bo.NewAdminBO;
import com.zyx.pojo.mo.FriendLinkMO;
import com.zyx.utils.PagedGridResult;

import java.util.List;

public interface FriendLinkService {

    /**
     * 新增或者更新友情链接
     */
    public void saveOrUpdateFriendLink(FriendLinkMO friendLinkMO);

    /**
     * 查询友情链接
     */
    public List<FriendLinkMO> queryAllFriendLinkList();

    /**
     * 删除友情链接
     */
    public void delete(String linkId);

    /**
     * 首页查询友情链接
     */
    public List<FriendLinkMO> queryPortalAllFriendLinkList();
}
