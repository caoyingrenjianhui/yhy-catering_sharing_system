package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.controller.Code;
import com.example.controller.Result;
import com.example.domain.*;
import com.example.dao.DishDao;
import com.example.service.IDishService;
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
public class DishServiceImpl extends ServiceImpl<DishDao, Dish> implements IDishService {

    @Autowired
    private DishDao dishDao;

    @Override
    public Result add(Dish dish) {
        Map<String, Object> map = ThreadLocalUtil.get();
        int insert = dishDao.insert(dish);
        if (insert == 0) {
            return new Result(null, Code.SAVE_ERR, "新增失败");
        }
        return new Result(dish, Code.SAVE_OK, "新增成功");
    }

    @Override
    public Result update(Dish dish) {
        int update = dishDao.updateById(dish);
        if (update == 0) {
            return new Result(null, Code.UPDATE_ERR, "修改失败");
        }
        return new Result(dish, Code.UPDATE_OK, "修改成功");
    }

    @Override
    public Result delete(Integer id) {
        Dish dish = dishDao.selectById(id);
        dish.setIsdel(0);
        int i = dishDao.updateById(dish);
        if (i != 0) {
            return new Result(null, Code.DELETE_OK, "删除成功");
        } else {
            return new Result(null, Code.DELETE_ERR, "删除失败");
        }
    }

    @Override
    public Result getAll() {
        List<Dish> list = dishDao.getAll();
        return new Result(list, Code.GET_OK, "查询成功");
    }

    @Override
    public Result getByMerchant(Integer id) {
        List<Dish> list = dishDao.getByMerchant(id);
        return new Result(list, Code.GET_OK, "查询成功");
    }

    @Override
    public Result select(Dish dish) {
        QueryWrapper<Dish> wrapper = new QueryWrapper<>();
        if (dish.getMerchantID() != null) {
            wrapper.eq("merchantID", dish.getMerchantID());
        }
        if (dish.getDishID() != null) {
            wrapper.eq("dishID", dish.getDishID());
        }
        if (dish.getDishName() != null && !"".equals(dish.getDishName())) {
            wrapper.like("dishName", dish.getDishName());
        }
        if (dish.getCategoryID() != null && !"".equals(dish.getCategoryID())) {
            wrapper.like("categoryID", dish.getCategoryID());
        }
        List<Dish> list = dishDao.selectList(wrapper);
        if (list == null) {
            return new Result(null, Code.GET_ERR, "查询不到数据");
        }
        return new Result(list, Code.GET_OK, "查询成功");
    }

    @Override
    public void upphoto(Dish dish) {
        dishDao.updatePhoto(dish.getPhoto(), dish.getDishID());
    }
}
