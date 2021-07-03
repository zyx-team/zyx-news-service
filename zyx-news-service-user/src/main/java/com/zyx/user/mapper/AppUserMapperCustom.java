package com.zyx.user.mapper;

import com.zyx.my.mapper.MyMapper;
import com.zyx.pojo.AppUser;
import com.zyx.pojo.vo.PublisherVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AppUserMapperCustom {

    public List<PublisherVO> getUserList(Map<String, Object> map);

}