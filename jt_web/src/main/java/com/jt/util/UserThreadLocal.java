package com.jt.util;

import com.jt.pojo.User;

public class UserThreadLocal {
    //定义本地线程
    public static ThreadLocal<User> threadLocal=new ThreadLocal<>();
    //定义数据新增的方法
    public static void set(User user){
        threadLocal.set(user);
    }
    //获取对象
    public static User get(){
        return threadLocal.get();
    }
    //移除方法， 使用threadLocal是切记数据移除 否则极端条件下容易造成数据的泄露
    public static void remove(){
        threadLocal.remove();
    }
}