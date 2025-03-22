package com.iamxgw.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import com.iamxgw.common.TrainType;
import com.iamxgw.common.TrainTypeSeatConstant;
import com.iamxgw.dao.TrainNumberDetailMapper;
import com.iamxgw.dao.TrainNumberMapper;
import com.iamxgw.exception.BusinessException;
import com.iamxgw.model.TrainNumber;
import com.iamxgw.model.TrainNumberDetail;
import com.iamxgw.model.TrainSeat;
import com.iamxgw.param.GeneratorTicketParam;
import com.iamxgw.seatDao.TrainSeatMapper;
import com.iamxgw.util.BeanValidator;
import javafx.util.Pair;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: IamXGW
 * @create: 2024-07-24 20:42
 */
@Service
public class TrainSeatService {

    @Resource
    private TrainNumberMapper trainNumberMapper;

    @Resource
    private TrainNumberDetailMapper trainNumberDetailMapper;

    @Resource
    private TrainSeatMapper trainSeatMapper;

    public void generate(GeneratorTicketParam param) {
        BeanValidator.check(param);
        // 车次
        TrainNumber trainNumber = trainNumberMapper.selectByPrimaryKey(param.getTrainNumberId());
        if (trainNumber == null) {
            throw new BusinessException("该车次不存在");
        }

        // 车次详情
        List<TrainNumberDetail> detailList = trainNumberDetailMapper.getByTrainNumberId(param.getTrainNumberId());
        if (CollectionUtils.isEmpty(detailList)) {
            throw new BusinessException("该车次无详情，请先添加详情");
        }
//        Collections.sort(detailList, Comparator.comparingInt(TrainNumberDetail::getStationIndex));

        // 座位类型
        TrainType trainType = TrainType.valueOf(trainNumber.getTrainType());

        // 座位配置
        Table<Integer, Integer, Pair<Integer, Integer>> seatTable = TrainTypeSeatConstant.getTable(trainType);

        // 时间
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime fromLocalDateTime = LocalDateTime.parse(param.getFromTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        List<TrainSeat> list = Lists.newArrayList();
        String ticket = fromLocalDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // 遍历车次每一段
        for (TrainNumberDetail trainNumberDetail : detailList) {
            // 每一段的发车时间
            Date fromDate = Date.from(fromLocalDateTime.atZone(zoneId).toInstant());
            // 每一段的到达时间
            Date toDate = Date.from(fromLocalDateTime.plusMinutes(trainNumberDetail.getRelativeMinute()).atZone(zoneId).toInstant());

            
        }

    }
}