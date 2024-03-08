package com.example.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
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
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "commentID", type = IdType.AUTO)
    private Integer commentID;

    private Integer userID;

    private Integer dishID;

    private Integer rating;

    private String content;

    private LocalDateTime timestamp;

    private Integer merchantID;

    private Integer reply;

    @TableLogic
    private Integer isdel;


}
