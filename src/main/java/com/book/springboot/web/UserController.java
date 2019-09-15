package com.book.springboot.web;

import com.book.springboot.mapper.UserMapper;
import com.book.springboot.pojo.User;
//import com.car.springboot.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    @Autowired
    UserMapper userMapper;

    @PostMapping("/login")
    public String login(User checkUser, Model model, HttpSession session) throws Exception {
        User user = userMapper.getUserByUsername(checkUser);
        if (user == null) {
            model.addAttribute("title", "登录失败");
            model.addAttribute("message", "用户名不存在");
            model.addAttribute("url", "/login");
            return "alert";
        }
        if (user.getPassword().equals(checkUser.getPassword())) {
            session.setAttribute("user", user);
            session.setAttribute("username", user.getUsername());
            return "redirect:";
        } else {

            return "login";
        }

    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        session.removeAttribute("admin");
        return "redirect:";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String register(User checkUser, Model model, HttpSession session) throws Exception {
        User user = userMapper.getUserByUsername(checkUser);
        if (user != null) {
            model.addAttribute("title", "注册失败");
            model.addAttribute("message", "用户名已被注册");
            model.addAttribute("url", "/login");
            return "alert";
        } else {
            int res = userMapper.save(checkUser);
            if (res == 1) {
                model.addAttribute("title", "注册成功");
                model.addAttribute("message", "恭喜您注册成功，自动跳转到登录页面");
                model.addAttribute("url", "/login");
                return "alert";
            }
        }
        model.addAttribute("title", "注册失败");
        model.addAttribute("message", "未知异常");
        model.addAttribute("url", "/register");
        return "alert";

    }


}
