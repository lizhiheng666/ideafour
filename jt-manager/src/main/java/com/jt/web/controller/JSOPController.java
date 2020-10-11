package com.jt.web.controller;



import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.ItemDesc;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JSOPController {
    @RequestMapping("/web/testJSONP")
    public JSONPObject jsonp(String callback){
        ItemDesc itemDesc=new ItemDesc();
        itemDesc.setItemId(101L).setItemDesc("我是商品详情");
        return new JSONPObject(callback, itemDesc);
    }
}
