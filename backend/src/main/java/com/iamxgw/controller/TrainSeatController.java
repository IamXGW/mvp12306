package com.iamxgw.controller;

import com.iamxgw.common.JsonData;
import com.iamxgw.param.GeneratorTicketParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * @author IamXGW
 * @since 2024-07-24 20:36
 */
@Controller
@RequestMapping("/admin/train/seat")
public class TrainSeatController {

    @RequestMapping("list.page")
    public ModelAndView page() {
        return new ModelAndView("trainSeat");
    }

    @RequestMapping("search.json")
    @ResponseBody
    public JsonData search() {
        return JsonData.success();
    }

    @RequestMapping("generate.json")
    @ResponseBody
    public JsonData generate(GeneratorTicketParam param) {
        return JsonData.success();
    }
}