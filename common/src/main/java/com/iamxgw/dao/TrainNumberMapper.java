package com.iamxgw.dao;

import com.iamxgw.model.TrainNumber;

public interface TrainNumberMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TrainNumber record);

    int insertSelective(TrainNumber record);

    TrainNumber selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TrainNumber record);

    int updateByPrimaryKey(TrainNumber record);
}