package com.jt.service;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;

public interface DubboItemService {
    Item findItemById(long itemId);

    ItemDesc findItemDescById(long itemId);
}
