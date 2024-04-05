package com.example.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.domain.Merchant;
import com.example.domain.User;
import com.example.service.IMerchantService;
import com.example.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 尹洪运
 * @since 2024-02-29
 * 商家
 */
@RestController
@RequestMapping("/merchant")
public class MerchantController {

    @Autowired
    private IMerchantService merchantService;

    @PostMapping("/add")
    public Result add(@RequestBody Merchant merchant) {
        return merchantService.add(merchant);
    }

    @PostMapping("/searchNearbyMerchants")
    public Result searchNearbyMerchants(@RequestBody User user) {

        // 构造查询条件
        QueryWrapper<Merchant> queryWrapper = Wrappers.<Merchant>query();
        queryWrapper.apply("6371 * acos(cos(radians(?)) * cos(radians(latitude)) * cos(radians(longitude) - radians(?)) + sin(radians(?)) * sin(radians(latitude)) < ?",
                user.getUserLat(), user.getUserLng(), user.getUserLat(), user.getMaxDistance());

        // 调用 MyBatis-Plus 的 list 方法查询符合条件的商家信息
        List<Merchant> nearbyMerchants = merchantService.list(queryWrapper);
        if (nearbyMerchants.size() == 0) {
            return new Result(null, Code.GET_ERR, "未查询到相关商家");
        }
        return new Result(nearbyMerchants, Code.GET_OK, "查询成功");
    }

    @DeleteMapping("/delete")
    public Result delete(@PathVariable Integer id){
        return merchantService.deleteById(id);
    }

    @PostMapping("/select")
    public Result select(@RequestBody Merchant merchant){
        return merchantService.select(merchant);
    }

    @PutMapping("/update")
    public Result update(@RequestBody Merchant merchant){
        return merchantService.update(merchant);
    }

    @GetMapping("/getAll")
    public Result getAll() {
        return merchantService.getAll();
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
            merchantService.upphoto(user);
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

