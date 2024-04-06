package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.controller.Code;
import com.example.controller.Result;
import com.example.dao.MerchantDao;
import com.example.domain.*;
import com.example.dao.OrderitemDao;
import com.example.service.IOrderitemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
public class OrderitemServiceImpl extends ServiceImpl<OrderitemDao, Orderitem> implements IOrderitemService {

    @Autowired
    private OrderitemDao orderitemDao;
    @Autowired
    private MerchantDao merchantDao;

    @Override
    public Result add(Orderitem orderitem) {
        Map<String, Object> map = ThreadLocalUtil.get();
        orderitem.setUserID((String) map.get("userID"));
        String dishIds = "";
        for (Dish dish : orderitem.getSelectedDishes()) {
            dishIds += dish.getDishID() + ",";
        }
        // 去除最后一个逗号
        if (!dishIds.isEmpty()) {
            dishIds = dishIds.substring(0, dishIds.length() - 1);
        }
        orderitem.setDishID(dishIds);
        int insert = orderitemDao.insert(orderitem);
        if (insert != 0) {
            return new Result(orderitem, Code.SAVE_OK, "新增成功");
        } else {
            return new Result(orderitem, Code.SAVE_ERR, "新增失败，请联系管理员");
        }
    }

    @Override
    public Result finish(Integer id) {
        Orderitem orderitem = orderitemDao.selectById(id);
        if (orderitem == null) {
            return new Result(null, Code.GET_ERR, "无此订单");
        }
        int i = orderitemDao.updateById(orderitem);
        if (i == 0) {
            return new Result(orderitem, Code.SAVE_ERR, "修改失败");
        }
        return new Result(orderitem, Code.SAVE_OK, "修改成功");
    }

    @Override
    public Result receive(Integer id) {
        Orderitem orderitem = orderitemDao.selectById(id);
        orderitem.setStatus("1");
        int i = orderitemDao.updateById(orderitem);
        if (i == 0) {
            return new Result(orderitem, Code.SAVE_ERR, "修改失败");
        }
        return new Result(orderitem, Code.SAVE_OK, "修改成功");
    }

    @Override
    public Result delete(Integer id) {
        Orderitem orderitem = orderitemDao.selectById(id);
        orderitem.setIsdel(0);
        int i = orderitemDao.updateById(orderitem);
        if (i != 0) {
            return new Result(null, Code.DELETE_OK, "删除成功");
        } else {
            return new Result(null, Code.DELETE_ERR, "删除失败");
        }
    }

    @Override
    public Result getMyOrder(String id) {
        QueryWrapper<Merchant> queryWrapper = new QueryWrapper<>();
        Map<String, Object> map = ThreadLocalUtil.get();
        queryWrapper.eq("userID", (String) map.get("userID")); // 指定查询条件，这里假设字段名为userId
        Merchant merchant = merchantDao.selectOne(queryWrapper);
        QueryWrapper<Orderitem> wrapper = new QueryWrapper<>();
        wrapper.eq("userID", id);
        if (merchant.getMerchantID() != null) {
            wrapper.eq("merchantID", merchant.getMerchantID());
        }
        List<Orderitem> list = orderitemDao.selectList(wrapper);
        if (list.size() == 0) {
            return new Result(null, Code.GET_ERR, "查询不到数据");
        }
        return new Result(list, Code.GET_OK, "查询成功");
    }
}
