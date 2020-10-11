package com.jt.aop;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.vo.SysResult;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice   //定义异常处理的通知.  只拦截Controller层抛出的异常. 并且返回值JSON串
public class SystemExceptionAOP {

    /**
     * 如果跨域访问出现了异常 返回值必须经过特殊的封装才可以
     * 如果是跨域的形式访问。全局处理的异常跨域正确的返回
     * 思路1 判断用户提交的参数是否含有callback参数
     * @param e
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    public Object fail(Exception e, HttpServletRequest request){
        //1获取用户请求的参数
        String callback = request.getParameter("callback");
        //2 判断参数是否有值
        if(StringUtils.isEmpty(callback)){
            //为空则不是jsonp的跨域访问
        //打印异常信息
        e.printStackTrace();
        return SysResult.fail();
        }else {
            e.printStackTrace();
            return  new JSONPObject(callback, SysResult.fail());

        }

    }
}
