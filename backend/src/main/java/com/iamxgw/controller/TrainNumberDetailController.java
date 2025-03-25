package com.iamxgw.controller;

import com.iamxgw.common.JsonData;
import com.iamxgw.dto.TrainNumberDetailDTO;
import com.iamxgw.model.TrainNumber;
import com.iamxgw.model.TrainNumberDetail;
import com.iamxgw.model.TrainStation;
import com.iamxgw.param.TrainNumberDetailParam;
import com.iamxgw.service.TrainNumberDetailService;
import com.iamxgw.service.TrainNumberService;
import com.iamxgw.service.TrainStationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 车次详情
 *
 * @author IamXGW
 * @since 2024-07-10 20:57
 */
@Controller
@RequestMapping("/admin/train/numberDetail")
public class TrainNumberDetailController {

    @Resource
    private TrainNumberDetailService trainNumberDetailService;

    @Resource
    private TrainStationService trainStationService;

    @Resource
    TrainNumberService trainNumberService;

    @RequestMapping("/list.page")
    public ModelAndView page() {
        return new ModelAndView("trainNumberDetail");
    }

    @RequestMapping("/list.json")
    @ResponseBody
    public JsonData list() {
        List<TrainNumberDetail> detailList = trainNumberDetailService.getAll();
        List<TrainStation> stationList = trainStationService.getAll();
        Map<Integer, String> stationMap = stationList.stream().collect(Collectors.toMap(TrainStation::getId, TrainStation::getName));
        List<TrainNumber> numberList = trainNumberService.getAll();
        Map<Integer, String> numberMap = numberList.stream().collect(Collectors.toMap(TrainNumber::getId, TrainNumber::getName));
        List<TrainNumberDetailDTO> dtoList = detailList.stream().map(detail -> {
            TrainNumberDetailDTO dto = new TrainNumberDetailDTO();
            dto.setId(detail.getId());
            dto.setTrainNumberId(detail.getTrainNumberId());
            dto.setTrainNumber(numberMap.get(detail.getTrainNumberId()));
            dto.setFromStationId(detail.getFromStationId());
            dto.setFromStation(stationMap.get(detail.getFromStationId()));
            dto.setToStationId(detail.getToStationId());
            dto.setToStation(stationMap.get(detail.getToStationId()));
            dto.setFromCityId(detail.getFromCityId());
            dto.setToCityId(detail.getToCityId());
            dto.setStationIndex(detail.getStationIndex());
            dto.setRelativeMinute(detail.getRelativeMinute());
            dto.setWaitMinute(detail.getWaitMinute());
            dto.setMoney(detail.getMoney());
            return dto;
        }).collect(Collectors.toList());
        return JsonData.success(dtoList);
    }

    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData save(TrainNumberDetailParam param) {
        trainNumberDetailService.save(param);
        return JsonData.success();
    }

    @RequestMapping("/delete.json")
    @ResponseBody
    public JsonData delete(@RequestParam("id") Integer id) {
        trainNumberDetailService.delete(id);
        return JsonData.success();
    }

}