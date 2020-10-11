package com.jt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.jt.mapper")//执行mapper接口的包路径
public class SpringBootRun {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootRun.class, args);
    }
}
