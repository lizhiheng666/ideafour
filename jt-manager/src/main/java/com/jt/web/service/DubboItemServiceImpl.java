package com.jt.web.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.jt.anno.CacheFind;
import com.jt.mapper.ItemDescMapper;
import com.jt.mapper.ItemMapper;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.DubboItemService;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class DubboItemServiceImpl implements DubboItemService {
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private ItemDescMapper itemDescMapper;

    @Override
    @CacheFind(key="ITEM_ID")
    public Item findItemById(long itemId) {
        return itemMapper.selectById(itemId);
    }

    @Override
    @CacheFind(key="ITEM_DESC_ID")
    public ItemDesc findItemDescById(long itemId) {
        return itemDescMapper.selectById(itemId);
    }
}
