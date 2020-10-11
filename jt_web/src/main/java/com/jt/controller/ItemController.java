package com.jt.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.DubboItemService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/items")
public class ItemController {
    @Reference(check = false)
    private DubboItemService dubboItemService;
    /**
     * 根据商品的id查询商品的信息
     * url：http：//www。jt。com/items/562379.html
     * 参数：商品的id
     * 页面的取值要求：${item.title} ${item.item.desc}
     * 返回值：页面的逻辑名称 item
     */
    @RequestMapping("/{itemId}")
    public String findItemById(@PathVariable long itemId, Model model){
        //1 远程访问商品信息
        Item item=dubboItemService.findItemById(itemId);
        //2远程访问商品描述信息
        ItemDesc itemDesc=dubboItemService.findItemDescById(itemId);
        //将数据传到页面中
        model.addAttribute("item", item);
        model.addAttribute("itemDesc", itemDesc);
        return "item";
    }

}
