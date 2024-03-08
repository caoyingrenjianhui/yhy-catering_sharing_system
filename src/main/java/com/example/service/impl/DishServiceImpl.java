package com.example.service.impl;

import com.example.domain.Dish;
import com.example.dao.DishDao;
import com.example.service.IDishService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 尹洪运
 * @since 2024-02-29
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishDao, Dish> implements IDishService {

}
