package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.controller.Code;
import com.example.controller.Result;
import com.example.dao.MerchantDao;
import com.example.domain.Comment;
import com.example.dao.CommentDao;
import com.example.domain.Complaint;
import com.example.domain.Merchant;
import com.example.service.ICommentService;
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
public class CommentServiceImpl extends ServiceImpl<CommentDao, Comment> implements ICommentService {

    @Autowired
    private CommentDao commentDao;
    @Autowired
    private MerchantDao merchantDao;

    @Override
    public Result add(Comment comment) {
        Map<String, Object> map = ThreadLocalUtil.get();
        comment.setUserID((String) map.get("userID"));
        if (comment.getRating() == null) {
            comment.setRating(0);
        }
        comment.setTimestamp(LocalDate.now().toString());
        int insert = commentDao.insert(comment);
        if (insert == 0) {
            return new Result(null, Code.SAVE_ERR, "新增失败");
        }
        return new Result(comment, Code.SAVE_OK, "新增成功");
    }

    @Override
    public Result selectByUserID() {
        QueryWrapper<Merchant> queryWrapper = new QueryWrapper<>();
        Map<String, Object> map = ThreadLocalUtil.get();
        queryWrapper.eq("userID", (String) map.get("userID")); // 指定查询条件，这里假设字段名为userId
        queryWrapper.eq("rating", 0);
        Merchant merchant = merchantDao.selectOne(queryWrapper);
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        if (merchant != null) {
            wrapper.eq("merchantID", merchant.getMerchantID());
        }
        wrapper.eq("userID", (String) map.get("userID"));
        List<Comment> comments = commentDao.selectList(wrapper);
        return new Result(comments, Code.GET_OK, "查询成功");
    }

    @Override
    public Result selectByMerchantID(Integer merchantID) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("merchantID", merchantID);
        wrapper.eq("rating", 0);
        List<Comment> comments = commentDao.selectList(wrapper);
        for (Comment comment : comments) {
            QueryWrapper<Comment> replyWrapper = new QueryWrapper<>();
            replyWrapper.eq("rating", comment.getOrderItemID());
            List<Comment> replies = commentDao.selectList(replyWrapper);
            comment.setReply(replies);
        }
        return new Result(comments, Code.GET_OK, "查询成功");
    }

    @Override
    public Result deleteById(Integer id) {
        int i = commentDao.deleteById(id);
        if (i == 0) {
            return new Result(null, Code.DELETE_ERR, "删除失败");
        }
        return new Result(null, Code.DELETE_OK, "删除成功");
    }
}
