package com.jt.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.CartMapper;
import com.jt.pojo.Cart;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class DubboCartServiceImpl implements DubboCartService{
    @Autowired
    private CartMapper cartMapper;
    @Override
    public List<Cart> findCartListById(Long userId) {
        QueryWrapper<Cart> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return cartMapper.selectList(queryWrapper);
    }

    @Override
    public void updateCartNum(Cart cart) {
        Cart cartTemp=new Cart();
        cartTemp.setNum(cart.getNum());
        UpdateWrapper updateWrapper=new UpdateWrapper();
        updateWrapper.eq("user_id", cart.getUserId());
        updateWrapper.eq("item_id", cart.getItemId());
        cartMapper.update(cartTemp, updateWrapper);
    }

    @Override
    public void saveCart(Cart cart) {
        QueryWrapper<Cart> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("user_id", cart.getUserId());
        queryWrapper.eq("item_id", cart.getItemId());
        Cart cartDB = cartMapper.selectOne(queryWrapper);
        if (cartDB==null){
            cartMapper.insert(cart);
        }else {
            int num =cart.getNum()+cartDB.getNum();
            Cart cartTemp=new Cart();
            cartTemp.setNum(num)
                    .setId(cartDB.getId());
            cartMapper.updateById(cartTemp);

        }
    }

    @Override
    public void deleteCart(Cart cart) {
        //将对象中不为空的元素当作where条件去删除
        cartMapper.delete(new QueryWrapper<>(cart));
    }
}
