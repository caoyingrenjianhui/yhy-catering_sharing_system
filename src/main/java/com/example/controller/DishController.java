package com.example.controller;


import com.example.domain.Dish;
import com.example.domain.Merchant;
import com.example.domain.User;
import com.example.service.IDishService;
import com.example.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 尹洪运
 * @since 2024-02-29
 */
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private IDishService dishService;

    @PostMapping("/add")
    public Result add(@RequestBody Dish dish){
        return dishService.add(dish);
    }

    @PostMapping("/update")
    public Result update(@RequestBody Dish dish){
        return dishService.update(dish);
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){
        return dishService.delete(id);
    }

    @GetMapping("/getAll")
    public Result getAll() {
        return dishService.getAll();
    }

    @GetMapping("/getByMerchant/{id}")
    public Result getByMerchant(@PathVariable Integer id) {
        return dishService.getByMerchant(id);
    }

    @PostMapping("/select")
    public Result select(@RequestBody Dish dish){
        return dishService.select(dish);
    }

    //    上传头像
    @PostMapping("/upload")
    public String up(MultipartFile photo, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //  获取图片的原始名称
        System.out.println("mingcheng:" + photo.getOriginalFilename());
//        获取文件类型
        System.out.println("leixing:" + photo.getContentType());
//        获取在网络上运行的路径
        String path = request.getServletContext().getRealPath("/upload/");
        System.out.println("path" + path);
        saveFile(photo, path);
//        保存到数据库
        String pic = "http://localhost:9080/upload/" + photo.getOriginalFilename();
        System.out.println("pic" + pic);
//        页面路径
        String referer = request.getHeader("referer");
//        获取偶用户id，组成user，更改头像
        Map<String, Object> map = ThreadLocalUtil.get();
        String userID = (String) map.get("userID");
        //        判断是否登陆过
        if (userID != null) {
            User user = new User();
            user.setUserID((String) userID);
            user.setPhoto(pic);
            dishService.upphoto(user);
//            不跳转页面
            response.sendRedirect(referer);
            return ("上传成功");
        }
        response.sendRedirect(referer);
        return ("上传失败");
    }

    //    上传头像
    private void saveFile(MultipartFile photo, String path) throws IOException {
//        存储目录是否存在，不存在就创建
        File dir = new File(path);
        if (!dir.exists()) {
//            创建目录
            dir.mkdir();
        }
        File file = new File(path + photo.getOriginalFilename());
        photo.transferTo(file);
        System.out.println(file);
    }
}

