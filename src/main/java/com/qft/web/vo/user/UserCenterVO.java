package com.qft.web.vo.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCenterVO {
    private Long id;//用户ID
    private String username;//用户名称
    private String avatar;//头像
    private String phone;//手机号
    private Integer userTypeId;//用户类型Id
    private Map<String, List<String>> favorites;  //收藏夹
}
