package com.iamxgw.dao;

import com.iamxgw.model.TrainUserTraveller;

public interface TrainUserTravellerMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TrainUserTraveller record);

    int insertSelective(TrainUserTraveller record);

    TrainUserTraveller selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TrainUserTraveller record);

    int updateByPrimaryKey(TrainUserTraveller record);
}