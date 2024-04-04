package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.controller.Code;
import com.example.controller.Result;
import com.example.domain.Merchant;
import com.example.dao.MerchantDao;
import com.example.domain.User;
import com.example.service.IMerchantService;
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
public class MerchantServiceImpl extends ServiceImpl<MerchantDao, Merchant> implements IMerchantService {

    @Autowired
    private MerchantDao merchantDao;

    @Override
    public Result add(Merchant merchant) {
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
            wrapper.like("merchantName", merchant.getMerchantName());
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
    public void upphoto(User user) {
        merchantDao.updatePhoto(user.getPhoto(), user.getUserID());
    }
}
