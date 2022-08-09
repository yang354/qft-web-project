package com.qft.web.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author yyyz
 * @since 2022-08-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("qft_user_type")
public class UserType implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 类型编号
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户类型
     */
    private String typeName;


}
