package com.book.springboot.web;

import com.book.springboot.mapper.ShoppingCartMapper;
import com.book.springboot.pojo.ShoppingCart;
import com.book.springboot.pojo.User;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class ShoppingCartController {
    @Autowired
    ShoppingCartMapper shoppingCartMapper;

    @GetMapping("/cart")
    public String home(Model m,HttpSession session, @RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "5") int size) throws Exception {
        User user = (User) session.getAttribute("user");
        if (user==null){
            m.addAttribute("title","请登录");
            m.addAttribute("message","请登录后操作");
            m.addAttribute("url","/login");
            return "alert";
        }
        PageHelper.startPage(start,size,"id desc");
        List<ShoppingCart> shoppingCarts=shoppingCartMapper.getCartByUserId(user.getId());
        PageInfo<ShoppingCart> page = new PageInfo<>(shoppingCarts);
        m.addAttribute("page", page);
        return "shopCar";
    }

    @GetMapping("/addCart")
    public String addCart(int id, Model model, HttpSession session){
        User user = (User) session.getAttribute("user");
        if (user==null){
            model.addAttribute("title","请登录");
            model.addAttribute("message","请登录后操作");
            model.addAttribute("url","/login");
            return "alert";
        }
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setBook_id(id);
//        shoppingCart.setUser_id(user.getId());
        shoppingCart.setUser_id(user.getId());
        ShoppingCart check = shoppingCartMapper.check(shoppingCart);
        if (check!=null){
            model.addAttribute("title","加入失败");
            model.addAttribute("message","购物车中已存在此书");
            model.addAttribute("url","/cart");
            return "alert";
        }
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a为am/pm的标记
        Date date = new Date();// 获取当前时间
        String addTime =sdf.format(date);
        shoppingCart.setAddTime(addTime);
        shoppingCartMapper.save(shoppingCart);
        return "redirect:cart";
    }

    @GetMapping("/deleteCart")
    public String deleteCart(int id){
        shoppingCartMapper.delete(id);
        return "redirect:cart";

    }
}
