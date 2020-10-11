package com.jt.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.OrderItemMapper;
import com.jt.mapper.OrderMapper;
import com.jt.mapper.OrderShippingMapper;
import com.jt.pojo.Order;
import com.jt.pojo.OrderItem;
import com.jt.pojo.OrderShipping;
import com.jt.service.DubboOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DubboOrderServiceImpl  implements DubboOrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private OrderShippingMapper orderShippingMapper;

    @Override
    public Order findOrderById(String id) {
        //查询订单
        Order order = orderMapper.selectById(id);
        //查询物流
        OrderShipping orderShipping = orderShippingMapper.selectById(id);
        //查询商品
        QueryWrapper<OrderItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", id);
        List<OrderItem> orderItems = orderItemMapper.selectList(queryWrapper);

        return order.setOrderItems(orderItems).setOrderShipping(orderShipping);
    }

    @Transactional
    @Override
    public String saveOrder(Order order) {
        String orderId=""+order.getUserId()+System.currentTimeMillis();
        order.setOrderId(orderId).setStatus(1);
        orderMapper.insert(order);

        OrderShipping orderShipping=order.getOrderShipping();
        orderShipping.setOrderId(orderId);
        orderShippingMapper.insert(orderShipping);

        List<OrderItem> list=order.getOrderItems();
        for (OrderItem orderItem : list) {
            orderItem.setOrderId(orderId);
            orderItemMapper.insert(orderItem);
        }
        return orderId;
    }
}
