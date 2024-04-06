package com.example.service;

import com.example.controller.Result;
import com.example.domain.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 尹洪运
 * @since 2024-02-29
 */
public interface ICommentService extends IService<Comment> {

    Result add(Comment comment);

    Result selectByUserID();

    Result deleteById(Integer id);

    Result selectByMerchantID(Integer merchamtID);
}
