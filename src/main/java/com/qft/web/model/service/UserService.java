package com.qft.web.model.service;


import com.baomidou.mybatisplus.extension.service.IService;

import com.qft.web.model.entity.User;
import com.qft.web.vo.user.RegisterVO;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yyyz
 * @since 2022-08-02
 */
public interface UserService extends IService<User> {

    /**
     * 根据用户名查询用户信息 * @param userName
     * @return
     */
    User findUserByUserName(String userName);

    void register(RegisterVO registerVO);

    void updatePassword(String username, String password);
}
