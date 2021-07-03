package com.zyx.user.mapper;

import com.zyx.my.mapper.MyMapper;
import com.zyx.pojo.AppUser;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserMapper extends MyMapper<AppUser> {
}