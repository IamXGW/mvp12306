package com.iamxgw.service;

import com.iamxgw.common.TrainType;
import com.iamxgw.dao.TrainNumberMapper;
import com.iamxgw.exception.BusinessException;
import com.iamxgw.model.TrainNumber;
import com.iamxgw.param.TrainNumberParam;
import com.iamxgw.util.BeanValidator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 
 * @author IamXGW
 * @since 2024-07-13 14:25
 */
@Service
public class TrainNumberService {

    @Resource
    private TrainNumberMapper trainNumberMapper;

    public List<TrainNumber> getAll() {
        return trainNumberMapper.getAll();
    }

    public void save(TrainNumberParam param) {
        BeanValidator.check(param);
        TrainNumber origin = trainNumberMapper.findByName(param.getName());
        if (origin != null) {
            throw new BusinessException("该车次已经存在");
        }
        TrainNumber trainNumber = TrainNumber.builder()
                .name(param.getName())
                .trainType(param.getTrainType())
                .type(param.getType().shortValue())
                .seatNum(TrainType.valueOf(param.getTrainType()).getCount())
                .build();
        trainNumberMapper.insertSelective(trainNumber);
    }

    public void update(TrainNumberParam param) {
        BeanValidator.check(param);
        TrainNumber origin = trainNumberMapper.findByName(param.getName());
        if (origin != null && origin.getId().intValue() != param.getId().intValue()) {
            throw new BusinessException("该车次已经存在");
        }
        // TODO：seat 判断是否有被分配过，如果被分配过，不推荐去修改
        TrainNumber trainNumber = TrainNumber.builder()
                .id(param.getId())
                .name(param.getName())
                .trainType(param.getTrainType())
                .type(param.getType().shortValue())
                // TODO
                .seatNum(TrainType.valueOf(param.getTrainType()).getCount())
                .build();
        trainNumberMapper.updateByPrimaryKeySelective(trainNumber);
    }
}