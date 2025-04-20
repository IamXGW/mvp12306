package com.iamxgw.service;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.iamxgw.dao.TrainNumberMapper;
import com.iamxgw.model.TrainNumber;
import com.iamxgw.model.TrainSeat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author IamXGW
 * @since 2025-04-12
 */
@Service
@Slf4j
public class TrainSeatService {

    @Resource
    TrainNumberMapper trainNumberMapper;

    @Resource
    TrainCacheService trainCacheService;

    public void handle(List<CanalEntry.Column> columns, CanalEntry.EventType eventType) {
        if (eventType != CanalEntry.EventType.UPDATE) {
            log.info("not update, not handle");
            return;
        }
        TrainSeat trainSeat = new TrainSeat();
        boolean isStatusUpdated = false;
        for (CanalEntry.Column column : columns) {
            if (column.getName().equals("status")) {
                trainSeat.setStatus(Integer.parseInt(column.getValue()));
                if (column.getUpdated()) {
                    isStatusUpdated = true;
                } else {
                    break;
                }
            } else if (column.getName().equals("id")) {
                trainSeat.setId(Long.parseLong(column.getValue()));
            } else if (column.getName().equals("carriage_number")) {
                trainSeat.setCarriageNumber(Integer.parseInt(column.getValue()));
            } else if (column.getName().equals("row_number")) {
                trainSeat.setRowNumber(Integer.parseInt(column.getValue()));
            } else if (column.getName().equals("seat_number")) {
                trainSeat.setSeatNumber(Integer.parseInt(column.getValue()));
            } else if (column.getName().equals("train_number_id")) {
                trainSeat.setTrainNumberId(Integer.parseInt(column.getValue()));
            } else if (column.getName().equals("id")) {
                trainSeat.setId(Long.parseLong(column.getValue()));
            } else if (column.getName().equals("ticket")) {
                trainSeat.setTicket(column.getValue());
            } else if (column.getName().equals("from_station_id")) {
                trainSeat.setFromStationId(Integer.parseInt(column.getValue()));
            } else if (column.getName().equals("to_station_id")) {
                trainSeat.setToStationId(Integer.parseInt(column.getValue()));
            }
        }
        if (!isStatusUpdated) {
            log.info("train seat status not update, no need care");
            return;
        }
        log.info("train seat status update, trainSeat: {}", trainSeat);

        /*
         * 设计了两种缓存
         *
         * 1. 缓存指定座位被占：hash
         * key: 车次_日期
         * field: carriage_row_seat_fromStation_toStation
         * value: 0，空闲，1，已占
         *
         * 2. 缓存每个座位详情剩余的座位数
         * key: 车次_日期
         * field: fromStation_toStation
         * value: 实际座位数
         */

        TrainNumber trainNumber = trainNumberMapper.selectByPrimaryKey(trainSeat.getTrainNumberId());
        if (trainSeat.getStatus() == 1) {
            // 放票
            trainCacheService.hset(
                    trainNumber.getName() + "_" + trainSeat.getTicket(),
                    trainSeat.getCarriageNumber() + "_" + trainSeat.getRowNumber() + "_" + trainSeat.getSeatNumber() + "_" +
                            trainSeat.getFromStationId() + "_" + trainSeat.getToStationId(),
                    "0"
            );
            trainCacheService.hincrBy(
                    trainNumber.getName() + "_" + trainSeat.getTicket() + "_Count",
                    trainSeat.getFromStationId() + "_" + trainSeat.getToStationId(),
                    1L
            );
            log.info("seat+1, trainNumber:{}, trainSeat:{}", trainNumber.getName(), trainSeat);
        } else if (trainSeat.getStatus() == 2) {
            // 占座
            trainCacheService.hset(
                    trainNumber.getName() + "_" + trainSeat.getTicket(),
                    trainSeat.getCarriageNumber() + "_" + trainSeat.getRowNumber() + "_" + trainSeat.getSeatNumber() + "_" +
                            trainSeat.getFromStationId() + "_" + trainSeat.getToStationId(),
                    "1"
            );
            trainCacheService.hincrBy(
                    trainNumber.getName() + "_" + trainSeat.getTicket() + "_Count",
                    trainSeat.getFromStationId() + "_" + trainSeat.getToStationId(),
                    -1L
            );
            log.info("seat-1, trainNumber:{}, trainSeat:{}", trainNumber.getName(), trainSeat);
        } else {
            log.info("train seat status update not 1 or 2, no need care");
        }
    }

}