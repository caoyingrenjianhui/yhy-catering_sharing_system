package com.example.service.impl;

import com.example.domain.Orderitem;
import com.example.dao.OrderitemDao;
import com.example.service.IOrderitemService;
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
public class OrderitemServiceImpl extends ServiceImpl<OrderitemDao, Orderitem> implements IOrderitemService {

}
