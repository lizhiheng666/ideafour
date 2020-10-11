package com.jt.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperUtil {

    //1.创建工具API对象
    private static final ObjectMapper MAPPER = new ObjectMapper();

    //2.封装API 将对象转化为JSON
    public static String toJSON(Object object){

        if(object == null){
            throw new RuntimeException("传入对象不能为null");
        }

        try {
            String json = MAPPER.writeValueAsString(object);
            return json;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 3.将JSON串转化为对象  用户传递什么类型,则返回什么对象
     *                      user.class        User对象
     *
     */
    public static <T> T toObject(String json,Class<T> target){

        //1.校验参数是否有效
        if(json == null || "".equals(json) || target == null){
            //参数有误.
            throw new RuntimeException("参数不能为null");
        }

        //2.执行业务处理
        try {
            T t = MAPPER.readValue(json, target);
            return t;
        } catch (JsonProcessingException e) {
            //将报错信息通知其他人
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }


}
