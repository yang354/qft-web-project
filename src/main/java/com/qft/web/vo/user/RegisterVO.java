package com.qft.web.vo.user;

import lombok.Data;

@Data
public class RegisterVO {
    /**
     * 登录名称(用户名)
     */
    private String username;
    /**
     * 电话
     */
    private String phone;

    /**
     * 登录密码
     */
    private String password;

    private Integer userTypeId;

}
