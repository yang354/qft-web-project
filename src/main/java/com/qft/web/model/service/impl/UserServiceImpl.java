package com.qft.web.model.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.qft.web.exception.MyException;
import com.qft.web.model.dao.UserMapper;
import com.qft.web.model.entity.User;
import com.qft.web.model.service.UserService;
import com.qft.web.util.MD5;
import com.qft.web.util.ResultCode;
import com.qft.web.vo.user.RegisterVO;

import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;


@Transactional
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private PasswordEncoder passwordEncoder;


    @Override
    public User findUserByUserName(String userName) {
        //创建条件构造器
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>(); //用户名
        queryWrapper.eq("username",userName);
        //返回查询记录
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public void register(RegisterVO registerVO) {
        //对象关键字段非空判断
        if (ObjectUtils.isEmpty(registerVO.getPhone())){
            throw new MyException(ResultCode.ERROR,"手机号为空");
        }
        if (ObjectUtils.isEmpty(registerVO.getPassword())){
            throw new MyException(ResultCode.ERROR,"密码为空");
        }
        if (ObjectUtils.isEmpty(registerVO.getUsername())){
            throw new MyException(ResultCode.ERROR,"用户名为空");
        }
        //查询手机号或用户名是否重复
        //使用这种还是出现可以注册通过
//        QueryWrapper<User> wrapper = new QueryWrapper<>();
//        wrapper.eq("phone",registerVO.getPhone()).or().eq("username",registerVO.getUsername());
//        User user = baseMapper.selectOne(wrapper);
        User user = baseMapper.selectUser(registerVO.getPhone(),registerVO.getUsername());
        if (user != null){
            throw new MyException(ResultCode.ERROR,"手机号或用户名已注册");
        }


        //对密码加密，设置用户不禁用等等

        String newPassword = passwordEncoder.encode(registerVO.getPassword());
        System.out.println(newPassword);
        User addUser = new User();
        BeanUtils.copyProperties(registerVO,addUser);
        addUser.setPassword(newPassword);
        addUser.setIsDelete(0);
        //添加
        baseMapper.insert(addUser);
    }

    @Override
    public void updatePassword(String username, String password) {
        if (ObjectUtils.isEmpty(password)){
            throw new MyException(ResultCode.ERROR,"密码为空");
        }
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();

        updateWrapper.eq("username",username).set("password",passwordEncoder.encode(password));
        baseMapper.update(null,updateWrapper);
    }
}
