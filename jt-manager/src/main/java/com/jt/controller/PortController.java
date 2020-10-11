package com.jt.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PortController {

    //考点:动态复制操作
    @Value("${server.port}")
    private Integer port;


    @RequestMapping("/getPort")
    public String getPort(){

        return "当前访问的端口号:"+port;
    }


}
