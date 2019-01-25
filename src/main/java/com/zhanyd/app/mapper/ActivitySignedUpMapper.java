package com.zhanyd.app.mapper;

import com.zhanyd.app.model.ActivitySignedUp;

public interface ActivitySignedUpMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ActivitySignedUp record);

    int insertSelective(ActivitySignedUp record);

    ActivitySignedUp selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ActivitySignedUp record);

    int updateByPrimaryKey(ActivitySignedUp record);
}