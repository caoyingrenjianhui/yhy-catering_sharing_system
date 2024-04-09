package com.example.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.domain.Merchant;
import com.example.domain.User;
import com.example.service.IMerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

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
        // 计算距离并添加到查询结果中
        String distanceField = "6371 * acos(cos(radians(" + user.getUserLat() + ")) * cos(radians(latitude)) * cos(radians(longitude) - radians(" + user.getUserLng() + ")) + sin(radians(" + user.getUserLat() + ")) * sin(radians(latitude)))";

        // 构造查询条件
        QueryWrapper<Merchant> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("isdel", 0); // 添加查询条件：is_del = 0
        queryWrapper.select("*," + distanceField + " AS distance"); // 将距离作为字段添加到查询结果中
        queryWrapper.orderByAsc("distance"); // 按距离升序排序

        // 调用 MyBatis-Plus 的查询方法执行查询
        List<Merchant> nearbyMerchants = merchantService.list(queryWrapper);

        if (nearbyMerchants.isEmpty()) {
            return new Result(null, Code.GET_ERR, "未查询到相关商家");
        }
        return new Result(nearbyMerchants, Code.GET_OK, "查询成功");
    }

    @DeleteMapping("/delete")
    public Result delete(@PathVariable Integer id) {
        return merchantService.deleteById(id);
    }

    @PostMapping("/select")
    public Result select(@RequestBody Merchant merchant) {
        return merchantService.select(merchant);
    }

    @PutMapping("/update")
    public Result update(@RequestBody Merchant merchant) {
        return merchantService.update(merchant);
    }

    @GetMapping("/getAll")
    public Result getAll() {
        return merchantService.getAll();
    }

    /**
     * 审核商家
     *
     * @param id
     * @return
     */
    @PutMapping("/handle/{id}")
    public Result handle(@PathVariable Integer id) {
        return merchantService.handle(id);
    }

    @GetMapping("/byMerchant/{merchantID}")
    public Result byMerchant(@PathVariable Integer merchantID) {
        return merchantService.byMerchant(merchantID);
    }


    //    上传头像
    @PostMapping("/upload")
    public String up(@RequestParam("merchantID") String merchantID, MultipartFile photo, HttpServletRequest request, HttpServletResponse response) throws IOException {
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
        if (merchantID != null && merchantID != "") {
            Merchant merchant = new Merchant();
            merchant.setMerchantID(Integer.valueOf(merchantID));
            merchant.setPhoto(pic);
            merchantService.upphoto(merchant);
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

