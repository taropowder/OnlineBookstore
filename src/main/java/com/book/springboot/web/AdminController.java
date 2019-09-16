package com.book.springboot.web;

import com.book.springboot.mapper.AdminMapper;
import com.book.springboot.mapper.BookMapper;
import com.book.springboot.mapper.OrderMapper;
import com.book.springboot.mapper.UserMapper;
import com.book.springboot.pojo.Admin;
import com.book.springboot.pojo.Book;
import com.book.springboot.pojo.Order;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

@Controller
public class AdminController {
    @Autowired
    AdminMapper adminMapper;
    @Autowired
    BookMapper bookMapper;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    UserMapper userMapper;


    @PostMapping("/admin/login")
    public String login(Admin checkAdmin, Model model, HttpSession session) throws Exception {
        Admin admin = adminMapper.getUserByUsername(checkAdmin);
        if (admin == null) {
            model.addAttribute("title", "登录失败");
            model.addAttribute("message", "用户名不存在");
            model.addAttribute("url", "/admin/login");
            return "alert";
        }
        if (admin.getPassword().equals(checkAdmin.getPassword())) {
            session.setAttribute("admin", admin);
//            session.setAttribute("admin", true);
            return "redirect:/admin";
        } else {

            return "login";
        }

    }

    @GetMapping("/admin/login")
    public String login() {
        return "admin/login";
    }

    @GetMapping("/admin")
    public String home(Model model, HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin==null){
            model.addAttribute("title","请登录");
            model.addAttribute("message","请登录后操作");
            model.addAttribute("url","/admin/login");
            return "alert";
        }
        int book_count = bookMapper.countBook();
        int order_count = orderMapper.countOrder();
        int user_count = userMapper.countUser();
        model.addAttribute("book_count",book_count);
        model.addAttribute("order_count",order_count);
        model.addAttribute("user_count",user_count);
        return "admin/layout";
    }



    public static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
    @GetMapping("/admin/book")
    public String bookList(Model m, HttpSession session,@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "9") int size) throws Exception {
//        Car car = carMapper.getCarById(id);
//        model.addAttribute("car",car);
        PageHelper.startPage(start,size,"id desc");
        List<Book> books=bookMapper.findAll();
        PageInfo<Book> page = new PageInfo<>(books);
        m.addAttribute("page", page);
        return "admin/book/list";
    }

    @GetMapping("/admin/book/add")
    public String addBook() {
        return "admin/book/add";
    }


    @PostMapping("/admin/book/add")
    public String saveBook(Book book,Model model, @RequestParam("images") MultipartFile file, HttpSession session) {
        String fileName = file.getOriginalFilename();
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        // 这里我使用随机字符串来重新命名图片
        fileName = Calendar.getInstance().getTimeInMillis() + getRandomString(4) + suffixName;
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        File dest = new File(path+"../../src/main/webapp/images/" + fileName);
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);

            //url的值为图片的实际访问地"" + fileName + "\"}";

        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        book.setImage(fileName);
        bookMapper.save(book);
        return "redirect:/book?id="+book.getId();
    }


    @GetMapping("/admin/book/edit")
    public String editBook(int id,Model model) {
        Book book = bookMapper.getBookById(id);
        model.addAttribute("book",book);
        return "admin/book/edit";
    }

    @PostMapping("/admin/book/edit")
    public String changeCar(Book book, @RequestParam("images") MultipartFile file,Model model, HttpSession session) {
//        Car car = carMapper.getCarById(id);
        if (file.isEmpty()) {
            bookMapper.changeWithOutImage(book);
        } else {
            String fileName = file.getOriginalFilename();
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            fileName = Calendar.getInstance().getTimeInMillis() + getRandomString(4) + suffixName;
            String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
            File dest = new File(path + "../../src/main/webapp/images/" + fileName);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            try {
                file.transferTo(dest);
                book.setImage(fileName);
                bookMapper.changeWithImage(book);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "redirect:/admin/book/edit?id=" + book.getId();
    }

    @GetMapping("/admin/order")
    public String orderList(Model m, HttpSession session,@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "5") int size) throws Exception {
//        Car car = carMapper.getCarById(id);
//        model.addAttribute("car",car);
        PageHelper.startPage(start,size,"id desc");
        List<Order> orders=orderMapper.getAll();
        PageInfo<Order> page = new PageInfo<>(orders);
        m.addAttribute("page", page);
        return "admin/order/list";
    }

    @GetMapping("/admin/order/good")
    public String orderBook(int id,Model model) {
        Order order = orderMapper.getOrderById(id);
        order.setStatus("已发货");
        orderMapper.updateStatus(order);
        return "redirect:/admin/order";
    }
}
