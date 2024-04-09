package com.example.dao;

import com.example.domain.Complaint;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 尹洪运
 * @since 2024-02-29
 */
@Mapper
public interface ComplaintDao extends BaseMapper<Complaint> {

    @Select("select * from complaint where isdel = 0")
    List<Complaint> getAll();
}
