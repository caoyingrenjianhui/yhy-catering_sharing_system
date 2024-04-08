package com.example.domain;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author 尹洪运
 * @since 2024-02-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Orderitem implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "orderID", type = IdType.AUTO)
    private Integer orderID;

    @TableField(value = "userID")
    private String userID;

    @TableField(value = "dishID")
    private String dishID;

    @TableField(value = "merchantID")
    private String merchantID;

    private BigDecimal totalPrice;

    /**
     * 是否接单
     */
    private String status;

    @TableField(exist = false)
    private List<Dish> selectedDishes;

    @TableLogic
    private Integer isdel;


}
