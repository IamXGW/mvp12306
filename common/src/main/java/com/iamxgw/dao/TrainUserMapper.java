package com.iamxgw.dao;

import com.iamxgw.model.TrainUser;

public interface TrainUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TrainUser record);

    int insertSelective(TrainUser record);

    TrainUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TrainUser record);

    int updateByPrimaryKey(TrainUser record);
}