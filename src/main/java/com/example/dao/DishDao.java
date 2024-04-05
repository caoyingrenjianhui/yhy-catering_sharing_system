package com.example.dao;

import com.example.domain.Dish;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 尹洪运
 * @since 2024-02-29
 */
@Mapper
public interface DishDao extends BaseMapper<Dish> {

    @Select("select * from dish")
    List<Dish> getAll();

    @Select("select * from dish where merchantID = #{id}")
    List<Dish> getByMerchant(Integer id);

    @Update("update dish set photo = #{photo}, modify_time = now() where userID = #{userID}")
    void updatePhoto(String photo, String userID);
}
