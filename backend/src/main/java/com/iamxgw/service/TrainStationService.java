package com.iamxgw.service;

import com.iamxgw.dao.TrainCityMapper;
import com.iamxgw.dao.TrainStationMapper;
import com.iamxgw.exception.BusinessException;
import com.iamxgw.model.TrainCity;
import com.iamxgw.model.TrainStation;
import com.iamxgw.param.TrainStationParam;
import com.iamxgw.util.BeanValidator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 
 * @author IamXGW
 *@since 2024-07-13 14:25
 */
@Service
public class TrainStationService {

    @Resource
    private TrainStationMapper trainStationMapper;

    @Resource
    private TrainCityMapper trainCityMapper;

    public List<TrainStation> getAll() {
        return trainStationMapper.getAll();
    }

    public void save(TrainStationParam param) {
        BeanValidator.check(param);
        TrainCity trainCity = trainCityMapper.selectByPrimaryKey(param.getCityId());
        if (trainCity == null) {
            throw new BusinessException("站点所属城市不存在");
        }
        if (checkExist(param.getName(), param.getId(), param.getCityId())) {
            throw new BusinessException("该城市下存在相同名称站点");
        }
        TrainStation trainStation = TrainStation.builder().name(param.getName()).cityId(param.getCityId()).build();
        trainStationMapper.insertSelective(trainStation);
    }

    public void update(TrainStationParam param) {
        BeanValidator.check(param);
        TrainCity trainCity = trainCityMapper.selectByPrimaryKey(param.getCityId());
        if (trainCity == null) {
            throw new BusinessException("站点所属城市不存在");
        }
        if (checkExist(param.getName(), param.getId(), param.getCityId())) {
            throw new BusinessException("该城市下存在相同名称站点");
        }
        TrainStation before = trainStationMapper.selectByPrimaryKey(param.getId());
        if (before == null) {
            throw new BusinessException("待更新的站点不存在");
        }
        TrainStation trainStation = TrainStation.builder().name(param.getName()).id(param.getId()).cityId(param.getCityId()).build();
        trainStationMapper.updateByPrimaryKeySelective(trainStation);
    }

    private boolean checkExist(String name, Integer stationId, Integer cityId) {
        return trainStationMapper.countByIdAndNameAndCityId(name, stationId, cityId) > 0;
    }

    public Integer getCityIdByStationId(Integer stationId) {
        TrainStation trainStation = trainStationMapper.selectByPrimaryKey(stationId);
        if (trainStation == null) {
            throw new BusinessException("站点不存在");
        }
        return trainStation.getCityId();
    }
}