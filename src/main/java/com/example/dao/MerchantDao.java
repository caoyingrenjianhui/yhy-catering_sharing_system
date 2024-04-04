package com.example.dao;

import com.example.domain.Merchant;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
public interface MerchantDao extends BaseMapper<Merchant> {

    @Select("select * from Merchant where isdel=0")
    List<Merchant> getAll();

    @Update("update Merchant set photo = #{photo}, modify_time = now() where userID = #{userID}")
    void updatePhoto(String photo, String userID);
}
