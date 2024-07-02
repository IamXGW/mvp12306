package com.iamxgw.dao;

import com.iamxgw.model.TrainCity;

public interface TrainCityMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TrainCity record);

    int insertSelective(TrainCity record);

    TrainCity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TrainCity record);

    int updateByPrimaryKey(TrainCity record);
}