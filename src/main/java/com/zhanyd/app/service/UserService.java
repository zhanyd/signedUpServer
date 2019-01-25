package com.zhanyd.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhanyd.app.mapper.UserInfoMapper;
import com.zhanyd.app.model.UserInfo;

import java.util.List;
import java.util.Map;

@Service
public class UserService {

	@Autowired
	UserInfoMapper userInfoMapper;
	
	public int insertSelective(UserInfo record){
		return userInfoMapper.insertSelective(record);
	}
	
	public UserInfo selectByPrimaryKey(Integer id){
		return userInfoMapper.selectByPrimaryKey(id);
	}
	
	public  UserInfo selectByTel(String tel){
		return userInfoMapper.selectByTel(tel);
	}

	public List<UserInfo> selectByParam(Map<String,String> param){
		return userInfoMapper.selectByParam(param);
	}
	
	public UserInfo selectByOpenid(String openid){
		return userInfoMapper.selectByOpenid(openid);
	}


}
