package com.iamxgw.controller;

import com.iamxgw.common.JsonData;
import com.iamxgw.dto.TrainNumberDTO;
import com.iamxgw.model.TrainNumber;
import com.iamxgw.model.TrainStation;
import com.iamxgw.param.TrainNumberParam;
import com.iamxgw.service.TrainNumberService;
import com.iamxgw.service.TrainStationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *  车次
 * @author IamXGW
 * @since 2024-07-10 20:58
 */
@Controller
@RequestMapping("/admin/train/number")
public class TrainNumberController {

    @Resource
    private TrainNumberService trainNumberService;
    @Resource
    private TrainStationService trainStationService;

    @RequestMapping("/list.page")
    public ModelAndView page() {
        return new ModelAndView("trainNumber");
    }

    @RequestMapping("/list.json")
    @ResponseBody
    public JsonData list() {
        List<TrainNumber> numberList = trainNumberService.getAll();
        List<TrainStation> stationList = trainStationService.getAll();
        Map<Integer, String> stationMap = stationList.stream().collect(Collectors.toMap(TrainStation::getId, TrainStation::getName));
        List<TrainNumberDTO> dtoList = numberList.stream().map(number -> {
            TrainNumberDTO dto = new TrainNumberDTO();
            dto.setId(number.getId());
            dto.setName(number.getName());
            dto.setFromStationId(number.getFromStationId());
            dto.setFromStation(stationMap.get(number.getFromStationId()));
            dto.setToStationId(number.getToStationId());
            dto.setToStation(stationMap.get(number.getToStationId()));
            dto.setFromCityId(number.getFromCityId());
            dto.setToCityId(number.getToCityId());
            dto.setType(number.getType());
            dto.setSeatNum(number.getSeatNum());
            dto.setTrainType(number.getTrainType());
            return dto;
        }).collect(Collectors.toList());
        return JsonData.success(dtoList);
    }

    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData save(TrainNumberParam param) {
        trainNumberService.save(param);
        return JsonData.success();
    }

    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData update(TrainNumberParam param) {
        trainNumberService.update(param);
        return JsonData.success();
    }
}