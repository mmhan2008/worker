package com.cnstock.controller;

import com.alibaba.fastjson.JSON;
import com.cnstock.service.impl.TestServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/12/27.
 */
@RestController
public class TestController {

    @Autowired
    private TestServiceImpl testService;

    @RequestMapping(value = "/hello")
    public String say(){
        return "worker successful";
    }
}
