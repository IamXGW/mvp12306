package com.iamxgw.controller;

import com.iamxgw.common.JsonData;
import com.iamxgw.dto.TrainStationDTO;
import com.iamxgw.model.TrainCity;
import com.iamxgw.model.TrainStation;
import com.iamxgw.param.TrainStationParam;
import com.iamxgw.service.TrainCityService;
import com.iamxgw.service.TrainStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *  车站
 * @author IamXGW
 *@since 2024-07-10 20:57
 */
@Controller
@RequestMapping("/admin/train/station")
public class TrainStationController {

    @Resource
    private TrainStationService trainStationService;
    @Resource
    private TrainCityService trainCityService;

    @RequestMapping("/list.page")
    public ModelAndView page() {
        return new ModelAndView("trainStation");
    }

    @RequestMapping("/list.json")
    @ResponseBody
    public JsonData list() {
        List<TrainStation> stationList = trainStationService.getAll();
        List<TrainCity> cityList = trainCityService.getAll();
        Map<Integer, String> cityMap = cityList.stream().collect(Collectors.toMap(TrainCity::getId, TrainCity::getName));
        List<TrainStationDTO> dtoList = stationList.stream().map(station -> {
            TrainStationDTO dto = new TrainStationDTO();
            dto.setId(station.getId());
            dto.setName(station.getName());
            dto.setCityId(station.getCityId());
            dto.setCityName(cityMap.get(station.getCityId()));
            return dto;
        }).collect(Collectors.toList());
        return JsonData.success(dtoList);
    }

    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData save(TrainStationParam param) {
        trainStationService.save(param);
        return JsonData.success();
    }

    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData update(TrainStationParam param) {
        trainStationService.update(param);
        return JsonData.success();
    }
}