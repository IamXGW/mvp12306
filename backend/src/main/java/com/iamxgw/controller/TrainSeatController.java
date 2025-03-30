package com.iamxgw.controller;

import com.iamxgw.beans.PageQuery;
import com.iamxgw.beans.PageResult;
import com.iamxgw.common.JsonData;
import com.iamxgw.dto.TrainSeatDTO;
import com.iamxgw.model.TrainSeat;
import com.iamxgw.model.TrainStation;
import com.iamxgw.param.GeneratorTicketParam;
import com.iamxgw.param.TrainSeatSearchParam;
import com.iamxgw.service.TrainSeatService;
import com.iamxgw.service.TrainStationService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 
 * @author IamXGW
 * @since 2024-07-24 20:36
 */
@Controller
@RequestMapping("/admin/train/seat")
public class TrainSeatController {

    @Resource
    TrainSeatService trainSeatService;
    @Autowired
    private TrainStationService trainStationService;

    @RequestMapping("list.page")
    public ModelAndView page() {
        return new ModelAndView("trainSeat");
    }

    @RequestMapping("search.json")
    @ResponseBody
    public JsonData search(TrainSeatSearchParam param, PageQuery pageQuery) {
        int total = trainSeatService.countList(param);
        if (total == 0) {
            return JsonData.success(PageResult.<TrainSeatDTO>builder().total(0).data(null).build());
        }
        List<TrainSeat> trainSeatList = trainSeatService.searchList(param, pageQuery);
        if (CollectionUtils.isEmpty(trainSeatList)) {
            return JsonData.success(PageResult.<TrainSeatDTO>builder().total(total).build());
        }
        List<TrainStation> stationList = trainStationService.getAll();
        Map<Integer, String> stationMap = stationList.stream().collect(Collectors.toMap(TrainStation::getId, TrainStation::getName));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        ZoneId zoneId = ZoneId.systemDefault();
        List<TrainSeatDTO> dtoList = trainSeatList.stream().map(trainSeat -> {
            TrainSeatDTO dto = new TrainSeatDTO();
            dto.setId(trainSeat.getId());
            dto.setFromStationId(trainSeat.getFromStationId());
            dto.setFromStation(stationMap.get(trainSeat.getFromStationId()));
            dto.setToStationId(trainSeat.getToStationId());
            dto.setToStation(stationMap.get(trainSeat.getToStationId()));
            dto.setTrainNumberId(trainSeat.getTrainNumberId());
            dto.setTrainNumber(param.getTrainNumber());
            dto.setShowStart(LocalDateTime.ofInstant(trainSeat.getTrainStart().toInstant(), zoneId).format(formatter));
            dto.setShowEnd(LocalDateTime.ofInstant(trainSeat.getTrainEnd().toInstant(), zoneId).format(formatter));
            dto.setStatus(trainSeat.getStatus());
            dto.setSeatLevel(trainSeat.getSeatLevel());
            dto.setCarriageNumber(trainSeat.getCarriageNumber());
            dto.setRowNumber(trainSeat.getRowNumber());
            dto.setSeatNumber(trainSeat.getSeatNumber());
            dto.setMoney(trainSeat.getMoney());
            return dto;
        }).collect(Collectors.toList());
        return JsonData.success(PageResult.<TrainSeatDTO>builder().data(dtoList).total(total).build());
    }

    @RequestMapping("generate.json")
    @ResponseBody
    public JsonData generate(GeneratorTicketParam param) {
        trainSeatService.generate(param);
        return JsonData.success();
    }
}