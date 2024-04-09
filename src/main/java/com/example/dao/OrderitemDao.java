package com.example.dao;

import com.example.domain.Orderitem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface OrderitemDao extends BaseMapper<Orderitem> {

    @Select("select * from orderitem where merchantID = #{merchantID}")
    List<Orderitem> selectListByMerchantID(Integer merchantID);
}
