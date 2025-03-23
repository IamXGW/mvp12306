package com.iamxgw.service;

import com.iamxgw.dao.TrainNumberDetailMapper;
import com.iamxgw.dao.TrainNumberMapper;
import com.iamxgw.exception.BusinessException;
import com.iamxgw.model.TrainNumber;
import com.iamxgw.model.TrainNumberDetail;
import com.iamxgw.param.TrainNumberDetailParam;
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
public class TrainNumberDetailService {

    @Resource
    private TrainNumberDetailMapper trainNumberDetailMapper;

    @Resource
    private TrainNumberMapper trainNumberMapper;

    @Resource
    private TrainStationService trainStationService;

    public List<TrainNumberDetail> getAll() {
        return trainNumberDetailMapper.getAll();
    }

    public void save(TrainNumberDetailParam param) {
        BeanValidator.check(param);
        TrainNumber trainNumber = trainNumberMapper.selectByPrimaryKey(param.getTrainNumberId());
        if (trainNumber == null) {
            throw new BusinessException("相关车次不存在");
        }
        List<TrainNumberDetail> detailList = trainNumberDetailMapper.getByTrainNumberId(param.getTrainNumberId());
        TrainNumberDetail trainNumberDetail = TrainNumberDetail.builder()
                .trainNumberId(param.getTrainNumberId())
                .fromStationId(param.getFromStationId())
                .toStationId(param.getToStationId())
                .stationIndex(detailList.size())
                .relativeMinute(param.getRelativeMinute())
                .waitMinute(param.getWaitMinute())
                .money(param.getMoney())
                .fromCityId(trainStationService.getCityIdByStationId(param.getFromStationId()))
                .toCityId(trainStationService.getCityIdByStationId(param.getToStationId()))
                .build();
        trainNumberDetailMapper.insertSelective(trainNumberDetail);
        if (param.getEnd() == 1) {
            detailList.add(trainNumberDetail);
            trainNumber.setFromStationId(detailList.get(0).getFromStationId());
            trainNumber.setToStationId(detailList.get(detailList.size() - 1).getToStationId());
            trainNumber.setFromCityId(detailList.get(0).getFromCityId());
            trainNumber.setToCityId(detailList.get(detailList.size() - 1).getToCityId());
            trainNumberMapper.updateByPrimaryKeySelective(trainNumber);

            // TODO: 考虑方便前台用户查询两个车站涉及的所有车次
        }
    }

    public void delete(int id) {
        trainNumberDetailMapper.deleteByPrimaryKey(id);
    }
}