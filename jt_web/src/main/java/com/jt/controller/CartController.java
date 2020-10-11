package com.jt.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.User;
import com.jt.service.DubboCartService;
import com.jt.util.UserThreadLocal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CartController {
    private static final String JTUSER ="JT_USER";
    @Reference
    private DubboCartService dubboCartService;
    /**
     * 1 购物车数据的呈现
     * url： http://www.jt.com/cart/update/num//cart/show.html
     * 参数无
     * 返回值Cart
     * 页面取值数据 ${Cartlist}
     * 核心业务流程 根据userId查询购物车的信息userId=7
     */
    @RequestMapping("/cart/show")
    public String show(Model model, HttpServletRequest request){
        User user=(User)request.getAttribute(JTUSER);
        Long userId=user.getId();
        List<Cart> cartList= dubboCartService.findCartListById(userId);
        model.addAttribute("cartList", cartList);
        return "cart";
    }

    /**
     * 业务需求：修改购物车的数量
     * url： http://www.jt.com/cart/update/num/cart/update/num/21213212/8
     * 参数：itemId/num
     * 返回值 void
     * 条件 userid=7 暂时写死
     */
    @RequestMapping("http://www.jt.com/cart/update/num/{itrmId}/{num}")
    @ResponseBody
    public void updateCartNum(Cart cart){//restful参数中与对象的属性一致可以用对象接

        Long userId= UserThreadLocal.get().getId();
        cart.setUserId(userId);
        dubboCartService.updateCartNum(cart);

    }
    /**
     * 业务需求： 删除购物车信息
     * url；http://www.jt.com/cart/update/cart/delete/num/21213212。html
     * 参数 itemID
     * 返回值 从定向到购物车页面
     */

    @RequestMapping("/cart/delete/{itemId}")
    public String deleteCart(Cart cart){

        Long userId=UserThreadLocal.get().getId();
        cart.setUserId(userId);
        dubboCartService.deleteCart(cart);
        return "redirect:/cart/show.html";
    }

    /**
     * 需求： 完成购物车的新增
     * url：http://www.jt.com/cart/add/562379.html
     * 参数：整合cart的form表单
     * 返回值：新增完成之后 从定向的购物车页面
     * 如果重复添加 只修改数量即可
     */
    @RequestMapping("/cart/add/{itemId}")
    public String saveCart(Cart cart,HttpServletRequest request){
        User user=(User)request.getAttribute(JTUSER);
        Long userId=user.getId();
        cart.setUserId(userId);
        dubboCartService.saveCart(cart);

        return "redirect:/cart/show.html";
    }





}
