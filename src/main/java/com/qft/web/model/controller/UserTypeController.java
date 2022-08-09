package com.qft.web.model.controller;


import com.qft.web.model.entity.UserType;
import com.qft.web.model.service.UserTypeService;
import com.qft.web.util.HttpClient;
import com.qft.web.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yyyz
 * @since 2022-08-03
 */
@Controller
@RequestMapping("/userType")
public class UserTypeController {

    @Autowired
    private UserTypeService userTypeService;

    @GetMapping("getType")
    public Result getType(){
        UserType userType = userTypeService.getById(1);
        return Result.ok(userType);
    }

}

