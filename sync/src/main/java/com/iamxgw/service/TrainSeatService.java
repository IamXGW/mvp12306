package com.iamxgw.service;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.iamxgw.model.TrainSeat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author IamXGW
 * @since 2025-04-12
 */
@Service
@Slf4j
public class TrainSeatService {

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
            log.info("status not update, no need care");
            return;
        }
        log.info("train seat status update, trainSeat:{}", trainSeat);
    }

}