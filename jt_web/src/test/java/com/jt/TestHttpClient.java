package com.jt;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TestHttpClient {
/**
 * 业务需求  在java程序中访问百度
 * url:http://www.baidu.com html代码片段
 * 1：获取httpclient对象
 * 2：自定义url地址
 * 3：定义请求类型
 * 4：发起请求
 * 5：校验返回值的结果是否正确
 * 6：获取返回值的信息  进行之后的业务处理
 */
@Test
public  void test01(){
    HttpClient httpClient= HttpClients.createDefault();
    String url ="http://www.baidu.com";
    HttpGet httpGet=new HttpGet(url);
    try {
        HttpResponse response= httpClient.execute(httpGet);
        int status=response.getStatusLine().getStatusCode();
        if(status==200){
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "utf-8");
            System.out.println(result);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }


}
}
