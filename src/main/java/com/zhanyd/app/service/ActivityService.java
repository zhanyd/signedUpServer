package com.zhanyd.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhanyd.app.mapper.ActivityInfoMapper;
import com.zhanyd.app.model.ActivityInfo;

@Service
public class ActivityService {

	@Autowired
	ActivityInfoMapper activityInfoMapper;
	
	public int insertSelective(ActivityInfo record) {
		return activityInfoMapper.insertSelective(record);
	}

	public ActivityInfo selectByPrimaryKey(Integer id) {
		return activityInfoMapper.selectByPrimaryKey(id);
	}

	public int updateByPrimaryKeySelective(ActivityInfo record) {
		return activityInfoMapper.updateByPrimaryKeySelective(record);
	}
}
