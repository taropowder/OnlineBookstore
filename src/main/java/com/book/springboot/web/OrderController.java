package com.book.springboot.web;


import com.book.springboot.mapper.AddressMapper;
import com.book.springboot.mapper.BookMapper;
import com.book.springboot.mapper.ShoppingCartMapper;
import com.book.springboot.pojo.Address;
import com.book.springboot.pojo.Order;
import com.book.springboot.pojo.ShoppingCart;
import com.book.springboot.mapper.OrderMapper;
import com.book.springboot.pojo.User;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class OrderController {
    @Autowired
    ShoppingCartMapper shoppingCartMapper;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    AddressMapper addressMapper;
    @Autowired
    BookMapper bookMapper;

    @GetMapping("/confirmOrder")
    public String Add(HttpSession session,Model model){
        User user = (User) session.getAttribute("user");
        if (user==null){
            model.addAttribute("title","请登录");
            model.addAttribute("message","请登录后操作");
            model.addAttribute("url","/login");
            return "alert";
        }
        List<ShoppingCart> shoppingCarts =  shoppingCartMapper.getCartByUserId(user.getId());
        List<Address> addresses=addressMapper.getAddressByUserId(user.getId());
        model.addAttribute("addresses",addresses);
        model.addAttribute("carts",shoppingCarts);
        return "order/add";

    }


    @PostMapping("/confirmOrder")
    public String Save(int address_id, Model model, HttpSession session){
        User user = (User) session.getAttribute("user");
        if (user==null){
            model.addAttribute("title","请登录");
            model.addAttribute("message","请登录后操作");
            model.addAttribute("url","/login");
            return "alert";
        }
        List<ShoppingCart> shoppingCarts =  shoppingCartMapper.getCartByUserId(user.getId());
        for (ShoppingCart cart: shoppingCarts
             ) {
            Order order = new Order();
            order.setUser_id(user.getId());
            order.setStatus("待发货");
            order.setAddress_id(address_id);
            order.setBook_id(cart.getBook().getId());
            SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a为am/pm的标记
            Date date = new Date();// 获取当前时间
            String orderTime =sdf.format(date);
            order.setOrderTime(orderTime);
            orderMapper.save(order);
            bookMapper.buyBook(cart.getBook());

        }
        shoppingCartMapper.deleteByUserId(user.getId());
        return "redirect:MyOrder";
//        List<Address> addresses=addressMapper.getAddressByUserId(1);
//        model.addAttribute("addresses",addresses);
//        model.addAttribute("carts",shoppingCarts);
//        return "order/add";

    }

//    @GetMapping("/confirmOrder")
//    public String (Model model){
//        List<ShoppingCart> shoppingCarts =  shoppingCartMapper.getCartByUserId(1);
//        model.addAttribute("carts",shoppingCarts);
//        return "order/add";
//
//    }

    @GetMapping("/goodsOrder")
    public String Good(int id,Model model){
        Order order = orderMapper.getOrderById(id);
        order.setStatus("已收货");
        orderMapper.updateStatus(order);
        return "redirect:order?id="+id;

    }


    @PostMapping("/commentOrder")
    public String Comment(int id,String comment,Model model){
        Order order = orderMapper.getOrderById(id);
        order.setComment(comment);
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a为am/pm的标记
        Date date = new Date();// 获取当前时间
        String commentTime =sdf.format(date);
        order.setStatus("已评论");
        order.setCommentTime(commentTime);
        orderMapper.updateComment(order);
        orderMapper.updateStatus(order);
        return "redirect:order?id="+id;

    }

    @GetMapping("/order")
    public String OneOrder(int id,Model model){
        Order order = orderMapper.getOrderById(id);
        model.addAttribute("order",order);
//        List<ShoppingCart> shoppingCarts =  shoppingCartMapper.getCartByUserId(1);
//        model.addAttribute("carts",shoppingCarts);
        return "order/one";

    }

    @GetMapping("/MyOrder")
    public String MyOrder(Model model, HttpSession session,@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "5") int size) throws Exception {
        User user = (User) session.getAttribute("user");
        if (user==null){
            model.addAttribute("title","请登录");
            model.addAttribute("message","请登录后操作");
            model.addAttribute("url","/login");
            return "alert";
        }
        PageHelper.startPage(start,size,"id desc");
        List<Order> orders=orderMapper.getOrderByUserId(user.getId());
        PageInfo<Order> page = new PageInfo<>(orders);
        model.addAttribute("page", page);
        return "order/list";

    }

//    GetMapping("")

}
