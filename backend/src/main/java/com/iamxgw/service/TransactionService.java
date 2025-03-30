package com.iamxgw.service;

import com.google.common.collect.Lists;
import com.iamxgw.model.TrainSeat;
import com.iamxgw.seatDao.TrainSeatMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * TransactionService
 *
 * @author IamXGW
 * @since 2025-03-26
 */
@Service
public class TransactionService {
    @Resource
    private TrainSeatMapper trainSeatMapper;

    @Transactional(rollbackFor = Exception.class)
    public void batchInsertSeat(List<TrainSeat> seatList) {
        List<List<TrainSeat>> partition = Lists.partition(seatList, 1000);
        partition.parallelStream().forEach(item -> {
            trainSeatMapper.batchInsert(item);
        });
    }
}