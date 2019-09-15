package com.book.springboot.web;

import com.book.springboot.mapper.AddressMapper;
import com.book.springboot.mapper.BookMapper;
import com.book.springboot.pojo.Address;
import com.book.springboot.pojo.ShoppingCart;
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
import java.util.List;

@Controller
public class AddressController {
    @Autowired
    AddressMapper addressMapper;

    @GetMapping("/address")
    public String home(Model m, HttpSession session,@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "5") int size) throws Exception {
        PageHelper.startPage(start,size,"id desc");
        User user = (User) session.getAttribute("user");
        if (user==null){
            m.addAttribute("title","请登录");
            m.addAttribute("message","请登录后操作");
            m.addAttribute("url","/login");
            return "alert";
        }
        List<Address> addresses=addressMapper.getAddressByUserId(user.getId());
        PageInfo<Address> page = new PageInfo<>(addresses);
        m.addAttribute("page", page);
        return "address/address";
    }

    @GetMapping("/addAddress")
    public String Add(){
        return "address/add";

    }

    @PostMapping("/addAddress")
    public String SaveAddress(HttpSession session, Model model, Address address){
        User user = (User) session.getAttribute("user");
        if (user==null){
            model.addAttribute("title","请登录");
            model.addAttribute("message","请登录后操作");
            model.addAttribute("url","/login");
            return "alert";
        }
        address.setUser_id(user.getId());
        addressMapper.save(address);
        return "redirect:address";

    }

    @GetMapping("/deleteAddress")
    public String deleteCart(int id){
        addressMapper.delete(id);
        return "redirect:address";

    }
}
