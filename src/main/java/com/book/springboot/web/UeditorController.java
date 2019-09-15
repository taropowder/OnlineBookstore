package com.book.springboot.web;

import com.book.springboot.pojo.Ueditor;
import com.book.springboot.util.PublicMsg;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Random;


@Controller
public class UeditorController {
    @RequestMapping("/test")
    private String showPage(){
        return "admin/index";
    }

    @RequestMapping(value="/ueditor")
    @ResponseBody
    public String ueditor(HttpServletRequest request) {

        return PublicMsg.UEDITOR_CONFIG;
    }

//    @RequestMapping(value="/imgUpload")
//    @ResponseBody
//    public Ueditor imgUpload(MultipartFile upfile) {
//        Ueditor ueditor = new Ueditor();
//        return ueditor;
//    }

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

    @RequestMapping(value = "/imgUpload")
    @ResponseBody
    public String imgUpdate(@RequestParam("upfile") MultipartFile file) {
        if (file.isEmpty()) {
            return "error";
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        // 这里我使用随机字符串来重新命名图片
        fileName = Calendar.getInstance().getTimeInMillis() + getRandomString(4) + suffixName;
        // 这里的路径为Nginx的代理路径，这里是/data/images/xxx.png
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        File dest = new File(path+"../../src/main/webapp/static/ueditor/data/" + fileName);
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);

            //url的值为图片的实际访问地址 这里我用了Nginx代理，访问的路径是http://localhost/xxx.png
//            String config = "{\"state\": \"SUCCESS\"," +
//                    "\"url\": \"" + ConstantL.BASE_URL + fileName + "\"," +
//                    "\"title\": \"" + fileName + "\"," +
//                    "\"original\": \"" + fileName + "\"}";
            Ueditor ueditor = new Ueditor();
            ueditor.setState("SUCCESS");
            ueditor.setTitle(fileName);
            ueditor.setOriginal(fileName);
            ueditor.setUrl("/static/ueditor/data/"+fileName);
            return ueditor.toString();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "error";
    }
}
