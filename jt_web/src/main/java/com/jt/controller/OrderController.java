package com.jt.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.Order;
import com.jt.service.DubboCartService;
import com.jt.service.DubboOrderService;
import com.jt.util.UserThreadLocal;
import com.jt.vo.SysResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    //添加接口
    @Reference(check = false)
    private DubboCartService dubboCartService;
    @Reference
    private DubboOrderService dubboOrderService;

    /**
     * url：www.jt.com/order/create.html
     * 参数没有 null
     * 返回值：order-cart
     * 页面取值：{carts} 查询购物车的信息返回给页面
     * @return
     */
    @RequestMapping("/create")
    public String create(Model model){
        Long userId= UserThreadLocal.get().getId();
        List<Cart> cartList = dubboCartService.findCartListById(userId);
        model.addAttribute("carts", cartList);
        return "order-cart";
    }
    /**
     * 目的：实现订单的入库
     * uurl：http：//www.jt.com/order/submit
     * 参数：整个form表单 利用order对象接受
     * 返回值 SysResult 返回orderId
     * 业务：订单入库时 入库三张表 order orderShapping orderItems
     * orderId由登陆用户的id+当前的时间戳完成 要求三个对象的主键值相同
     * */
    @RequestMapping("/submit")
    @ResponseBody
    public SysResult saveOrder(Order order){
        Long userId=UserThreadLocal.get().getId();
        order.setUserId(userId);
        String orderId= dubboOrderService.saveOrder(order);
        if (StringUtils.isEmpty(orderId)){
            return SysResult.fail();
        }

        return SysResult.success(orderId);
    }
    /**
     * 实现订单的查询 根据orderId
     * url地址: http://www.jt.com/order/success.html?id=71598258019985
     * 参数: id=71598258019985
     * 返回值: success页面
     * 页面参数: ${order.orderId}
     */
    @RequestMapping("/success")
    public String findOrderById(String id,Model model){
        Order order=dubboOrderService.findOrderById(id);
        model.addAttribute("order", order);
        return "success";
    }
}
