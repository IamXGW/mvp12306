package com.iamxgw.service;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.iamxgw.beans.PageQuery;
import com.iamxgw.common.TrainSeatLevel;
import com.iamxgw.common.TrainType;
import com.iamxgw.common.TrainTypeSeatConstant;
import com.iamxgw.dao.TrainNumberDetailMapper;
import com.iamxgw.dao.TrainNumberMapper;
import com.iamxgw.exception.BusinessException;
import com.iamxgw.model.TrainNumber;
import com.iamxgw.model.TrainNumberDetail;
import com.iamxgw.model.TrainSeat;
import com.iamxgw.param.GeneratorTicketParam;
import com.iamxgw.param.PublishTicketParam;
import com.iamxgw.param.TrainSeatSearchParam;
import com.iamxgw.seatDao.TrainSeatMapper;
import com.iamxgw.util.BeanValidator;
import com.iamxgw.util.StringUtil;
import javafx.util.Pair;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author IamXGW
 * @since 2024-07-24 20:42
 */
@Service
public class TrainSeatService {

    @Resource
    private TrainNumberMapper trainNumberMapper;

    @Resource
    private TrainNumberDetailMapper trainNumberDetailMapper;

    @Resource
    private TrainSeatMapper trainSeatMapper;

    @Resource
    private TransactionService transactionService;

    public List<TrainSeat> searchList(TrainSeatSearchParam param, PageQuery pageQuery) {
        BeanValidator.check(param);
        BeanValidator.check(pageQuery);
        TrainNumber trainNumber = trainNumberMapper.findByName(param.getTrainNumber());
        if (trainNumber == null) {
            throw new BusinessException("该车次不存在");
        }
        return trainSeatMapper.searchList(trainNumber.getId(), param.getTicket(), param.getStatus(),
                param.getCarriageNum(), param.getRowNum(), param.getSeatNum(),
                pageQuery.getOffset(), pageQuery.getPageSize());
    }

    public int countList(TrainSeatSearchParam param) {
        BeanValidator.check(param);
        TrainNumber trainNumber = trainNumberMapper.findByName(param.getTrainNumber());
        if (trainNumber == null) {
            throw new BusinessException("待查询的车次不存在");
        }
        return trainSeatMapper.countList(trainNumber.getId(), param.getTicket(), param.getStatus(),
                param.getCarriageNum(), param.getRowNum(), param.getSeatNum());
    }

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
        // 座位列表，用于批量更新数据库
//        List<TrainSeat> list = Lists.newArrayList();
        List<TrainSeat> list = new ArrayList<>();
        String ticket = fromLocalDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        // 遍历车次每一段
        for (TrainNumberDetail trainNumberDetail : detailList) {

            // 每一段的发车时间
            Date fromDate = Date.from(fromLocalDateTime.atZone(zoneId).toInstant());
            // 每一段的到达时间
            Date toDate = Date.from(fromLocalDateTime.plusMinutes(trainNumberDetail.getRelativeMinute()).atZone(zoneId).toInstant());
            // 每一类座位的价格
            Map<Integer, Integer> seatMoneyMap = splitSeatMoney(trainNumberDetail.getMoney());
            // 遍历每一节车厢去生成座位
            for (Table.Cell<Integer, Integer, Pair<Integer, Integer>> cell : seatTable.cellSet()) {
                Integer carriage = cell.getRowKey();
                Integer row = cell.getColumnKey();
                Pair<Integer, Integer> rowSeatRange = seatTable.get(carriage, row);
                TrainSeatLevel seatLevel = TrainTypeSeatConstant.getSeatLevel(trainType, carriage);
                Integer money = seatMoneyMap.get(seatLevel.getLevel());

                for (int idx = rowSeatRange.getKey(); idx <= rowSeatRange.getValue(); idx++) {
                    TrainSeat trainSeat = TrainSeat.builder()
                            .carriageNumber(carriage)
                            .rowNumber(row)
                            .seatNumber(idx)
                            .money(money)
                            .ticket(ticket)
                            .seatLevel(seatLevel.getLevel())
                            .trainStart(fromDate)
                            .trainEnd(toDate)
                            .trainNumberId(trainNumber.getId())
                            .showNumber(carriage + " 车厢 " + row + " 排 " + idx + " 座")
                            .status(0)
                            .fromStationId(trainNumberDetail.getFromStationId())
                            .toStationId(trainNumberDetail.getToStationId())
                            .build();

                    list.add(trainSeat);
                }
            }
            fromLocalDateTime = fromLocalDateTime.plusMinutes(trainNumberDetail.getRelativeMinute()
                    + trainNumberDetail.getWaitMinute());
        }
        transactionService.batchInsertSeat(list);
    }

    /**
     * 处理车次详情中的价格
     *
     * @param money money
     * @return java.util.Map<java.lang.Integer, java.lang.Integer>
     * @author IamXGW
     * @since 2025/3/25
     */
    private Map<Integer, Integer> splitSeatMoney(String money) {
        Map<Integer, Integer> map = Maps.newHashMap();
        List<String> list = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(money);
        list.forEach(s -> {
            String[] arr = s.split(":");
            map.put(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]));
        });
        return map;
    }

    @Transactional(rollbackFor = Exception.class)
    public void publish(PublishTicketParam param) {
        BeanValidator.check(param);
        TrainNumber trainNumber = trainNumberMapper.findByName(param.getTrainNumber());
        if (trainNumber == null) {
            throw new BusinessException("车次不存在");
        }
        String trainSeatId = param.getTrainSeatIds();
        List<Long> trainSeatList = StringUtil.splitToListLong(trainSeatId);
        List<List<Long>> idPartitionList = Lists.partition(trainSeatList, 1000);
        for (List<Long> partition : idPartitionList) {
            int cnt = trainSeatMapper.batchPublish(trainNumber.getId(), partition);
            if (cnt != partition.size()) {
                throw new BusinessException("部分座位不满足条件，请重新查询[初始]状态的座位进行放票");
            }
        }
    }

}