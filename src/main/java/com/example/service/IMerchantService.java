package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.controller.Result;
import com.example.domain.Merchant;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.domain.User;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 尹洪运
 * @since 2024-02-29
 */
public interface IMerchantService extends IService<Merchant> {

    Result add(Merchant merchant);

    Result deleteById(Integer id);

    Result select(Merchant merchant);

    Result update(Merchant merchant);

    Result getAll();

    void upphoto(Merchant merchant);

    Result handle(Integer id);

    Result byMerchant(Integer merchantID);
}
