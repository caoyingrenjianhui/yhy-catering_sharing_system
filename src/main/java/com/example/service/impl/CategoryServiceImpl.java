package com.example.service.impl;

import com.example.controller.Code;
import com.example.controller.Result;
import com.example.dao.CategoryDao;
import com.example.domain.Category;
import com.example.service.ICategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, Category> implements ICategoryService {

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public Result add(Category category) {
        category.setCreateTime(LocalDateTime.now().toString());
        Map<String, Object> map = ThreadLocalUtil.get();
        category.setUserID((String) map.get("userID"));
        int insert = categoryDao.insert(category);
        if (insert != 0) {
            return new Result(category, Code.SAVE_OK, "新增成功");
        } else {
            return new Result(category, Code.SAVE_ERR, "新增失败，请联系管理员");
        }

    }

    @Override
    public Result getAll() {
        List<Category> list = categoryDao.getAll();
        return new Result(list, Code.GET_OK, "查询成功");
    }
}
