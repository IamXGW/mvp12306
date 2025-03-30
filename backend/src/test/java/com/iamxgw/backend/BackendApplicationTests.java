package com.iamxgw.backend;

import com.iamxgw.BackendApplication;
import com.iamxgw.dao.TrainNumberMapper;
import com.iamxgw.model.TrainNumber;
import com.iamxgw.model.TrainSeat;
import com.iamxgw.seatDao.TrainSeatMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ContextConfiguration
@SpringBootTest(classes = BackendApplication.class)
class BackendApplicationTests {

    @Resource
    TrainSeatMapper trainSeatMapper;

    @Resource
    TrainNumberMapper trainNumberMapper;

    @Test
    void contextLoads() {
        TrainSeat trainSeat = TrainSeat.builder()
                .carriageNumber(1)
                .ticket("1")
                .rowNumber(1)
                .seatNumber(2)
                .money(1)
                .status(2)
                .showNumber("1")
                .trainNumberId(8)
                .seatLevel(1)
                .fromStationId(1)
                .toStationId(6)
                .trainStart(new java.sql.Date(System.currentTimeMillis()))
                .trainEnd(new java.sql.Date(System.currentTimeMillis()))
                .build();
        List<TrainSeat> list = new ArrayList<>();
        list.add(trainSeat);
        trainSeatMapper.batchInsert(list);
//        trainSeatMapper.insert(trainSeat);
//        trainSeatMapper.insertSelective(trainSeat);
//        System.out.println(trainSeatMapper.selectByPrimaryKey(12L));
    }

    @Test
    void trainNumberTest() {
        System.out.println(trainNumberMapper.selectByPrimaryKey(8));
    }
}
