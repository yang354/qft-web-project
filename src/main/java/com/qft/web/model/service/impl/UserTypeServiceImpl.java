package com.qft.web.model.service.impl;

import com.qft.web.model.dao.UserTypeMapper;
import com.qft.web.model.entity.UserType;
import com.qft.web.model.service.UserTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yyyz
 * @since 2022-08-03
 */
@Transactional
@Service
public class UserTypeServiceImpl extends ServiceImpl<UserTypeMapper, UserType> implements UserTypeService {

}
