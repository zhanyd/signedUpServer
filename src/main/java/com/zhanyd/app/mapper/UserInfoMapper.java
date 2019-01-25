package com.zhanyd.app.mapper;

import java.util.List;
import java.util.Map;

import com.zhanyd.app.model.UserInfo;

public interface UserInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);
    
    UserInfo selectByTel(String tel);

    List<UserInfo> selectByParam(Map<String,String> param);
    
    UserInfo selectByOpenid(String openid);
}