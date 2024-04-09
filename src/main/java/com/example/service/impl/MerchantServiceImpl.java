package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.controller.Code;
import com.example.controller.Result;
import com.example.dao.OrderitemDao;
import com.example.dao.UserDao;
import com.example.domain.*;
import com.example.dao.MerchantDao;
import com.example.service.IMerchantService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 尹洪运
 * @since 2024-02-29
 */
@Service
public class MerchantServiceImpl extends ServiceImpl<MerchantDao, Merchant> implements IMerchantService {

    @Autowired
    private MerchantDao merchantDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private OrderitemDao orderitemDao;

    @Override
    public Result add(Merchant merchant) {
        User user = userDao.selectById(merchant.getUserID());
        if (user == null) {
            return new Result(null, Code.SAVE_ERR, "系统中无此用户，请先让商家自行注册");
        }
        user.setUserType(UserType.merchant.getCode());
        user.setModifyTime(LocalDate.now().toString());
        userDao.updateById(user);
        Map<String, Object> map = ThreadLocalUtil.get();
        String userID = (String) map.get("userID");
        if (merchant.getUserID().equals(userID)) {
            merchant.setApprovalStatus(1);
        }
        merchantDao.insert(merchant);
        return new Result(merchant, Code.SAVE_OK, "新增成功");
    }

    @Override
    public Result deleteById(Integer id) {
        int deleteById = merchantDao.deleteById(id);
        if (deleteById == 0) {
            return new Result(null, Code.DELETE_ERR, "删除失败");
        }
        return new Result(null, Code.DELETE_OK, "删除成功");
    }

    @Override
    public Result select(Merchant merchant) {
        QueryWrapper<Merchant> wrapper = new QueryWrapper<>();
        if (merchant.getMerchantID() != null) {
            wrapper.eq("merchantID", merchant.getMerchantID());
        }
        if (merchant.getMerchantName() != null && !"".equals(merchant.getMerchantName())) {
            wrapper.like("merchant_Name", merchant.getMerchantName());
        }
        if (merchant.getApprovalStatus() != null && !"".equals(merchant.getApprovalStatus())) {
            wrapper.eq("approval_Status", merchant.getApprovalStatus());
        }
        if (merchant.getUserID() != null) {
            wrapper.eq("userID", merchant.getUserID());
        }
        List<Merchant> list = merchantDao.selectList(wrapper);
        if (list == null) {
            return new Result(null, Code.GET_ERR, "查询不到数据");
        }
        return new Result(list, Code.GET_OK, "查询成功");
    }

    @Override
    public Result update(Merchant merchant) {
        Merchant selectById = merchantDao.selectById(merchant.getMerchantID());
        if (selectById == null) {
            return new Result(null, Code.UPDATE_ERR, "无此商家");
        }
        merchantDao.updateById(merchant);
        return new Result(merchant, Code.UPDATE_OK, "修改成功");
    }

    @Override
    public Result getAll() {
        List<Merchant> list = merchantDao.getAll();
        return new Result(list, Code.GET_OK, "查询成功");
    }

    @Override
    public void upphoto(Merchant merchant) {
        merchantDao.updatePhoto(merchant.getPhoto(), merchant.getMerchantID());
    }

    @Override
    public Result handle(Integer id) {
        Merchant merchant = merchantDao.selectById(id);
        merchant.setApprovalStatus(1);
        int i = merchantDao.updateById(merchant);
        if (i == 0) {
            return new Result(null, Code.UPDATE_ERR, "处理失败");
        }
        return new Result(merchant, Code.UPDATE_OK, "处理成功");
    }

    @Override
    public Result byMerchant(Integer merchantID) {
        // 查询数据库，获取该商家的订单项数据
        List<Orderitem> orderItems = orderitemDao.selectListByMerchantID(merchantID);

        // 统计销售数量
        Map<String, Integer> salesStatistics = new HashMap<>();
        for (Orderitem orderItem : orderItems) {
            // 解析订单项中的菜品ID
            String[] dishIDs = orderItem.getDishID().split(";");
            for (String dishID : dishIDs) {
                // 更新销售数量统计
                salesStatistics.put(dishID, salesStatistics.getOrDefault(dishID, 0) + 1);
            }
        }

        return new Result(salesStatistics,Code.GET_OK,"查询成功");
    }

}
