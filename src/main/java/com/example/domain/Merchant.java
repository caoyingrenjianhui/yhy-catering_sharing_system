package com.example.domain;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import java.io.Serializable;
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
public class Merchant implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "merchantID", type = IdType.AUTO)
    private Integer merchantID;

    private String merchantName;

    private String email;

    private String phone;

    private String address;

    private String approvalStatus;

    private Integer userID;

    private BigDecimal longitude;

    private BigDecimal latitude;

    @TableLogic
    private Integer isdel;

    private String photo;


}
