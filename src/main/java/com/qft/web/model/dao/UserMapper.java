package com.qft.web.model.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qft.web.model.entity.User;
import org.apache.ibatis.annotations.Select;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yyyz
 * @since 2022-08-02
 */
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from qft_user where username=#{username} or phone=#{phone} limit 1")
    User selectUser(String phone, String username);
}
