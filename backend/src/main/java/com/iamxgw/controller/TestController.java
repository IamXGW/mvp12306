package com.iamxgw.controller;

import com.iamxgw.common.JsonData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * @author IamXGW
 * @since 2024-07-09 21:21
 */
@Controller
public class TestController {

    @RequestMapping("/test")
    @ResponseBody
    public JsonData test() {
        return JsonData.success();
    }
}