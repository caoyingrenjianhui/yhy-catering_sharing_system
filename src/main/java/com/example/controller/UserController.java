package com.example.controller;


import com.example.domain.User;
import com.example.service.IUserService;
import com.example.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 尹洪运
 * @since 2024-02-29
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;

    //    根据id查询
    @GetMapping("/{id}")
    public Result getById(@PathVariable String id) {
        User user = userService.getById(id);
        Integer code = user != null ? Code.GET_OK : Code.GET_ERR;
        String msg = code == Code.GET_OK ? "" : "数据查询失败，请重试";
        return new Result(user, code, msg);
    }

    @PostMapping("/register")
    public Result register(@RequestBody @Validated User user) {
        return userService.register(user);
    }

    @GetMapping("/all")
    public Result getAllUser() {
        return userService.getAllUser();
    }

    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        return userService.login(user);
    }

    /**
     * 获取用户详细信息
     *
     * @param
     * @return
     */
    @GetMapping("/userInfo")
    public Result userInfo() {
        return userService.userInfo();
    }

    @PutMapping("update")
    public Result update(@RequestBody @Validated User user) {
        return userService.update(user);
    }

    @PatchMapping("/updatePhoto")
    public Result updatePhoto(@RequestParam String photo) {
        return userService.updatePhoto(photo);
    }

    @PutMapping("updatePassword")
    public Result updatePassword(@RequestBody User user, @RequestHeader("Authorization") String token) {
        return userService.updatePassword(user, token);
    }

    //    上传头像
    @PostMapping("/upload")
    public String up(@RequestParam("userID") String userID, MultipartFile photo, HttpServletRequest request, HttpServletResponse response) throws IOException {
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
        if (userID != null || userID != "") {
            User user = new User();
            user.setUserID(userID);
            user.setPhoto(pic);
            userService.upphoto(user);
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

