package com.qft.web.model.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qft.web.config.redis.RedisService;
import com.qft.web.exception.MyException;
import com.qft.web.model.entity.Favorite;
import com.qft.web.model.dao.FavoriteMapper;
import com.qft.web.model.entity.User;
import com.qft.web.model.service.FavoriteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qft.web.model.service.UserService;
import com.qft.web.util.MyConstant;
import com.qft.web.util.Result;
import com.qft.web.util.ResultCode;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yyyz
 * @since 2022-08-08
 */
@Transactional
@Service
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements FavoriteService {
    @Resource
    private RedisService redisService;
    @Resource
    private RedisTemplate redisTemplate;




    @Override
    public Result favorite(String lawId,String index, Boolean isFavorite) {
        //从Spring Security上下文获取用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //获取用户信息
        User user = (User) authentication.getPrincipal();
        Long userId = user.getId();
        String key = MyConstant.FAVORITES +userId;
        if (isFavorite){
            Favorite favorite = new Favorite();
            favorite.setUserId(userId);
            favorite.setLawId(lawId+":"+index);
            Long favorite1 = isFavorite(lawId, index);
            if (favorite1>0){
                throw new MyException(ResultCode.ERROR,"已收藏，不能再收藏");
            }
            int isSuccess = baseMapper.insert(favorite);
            if (isSuccess>0){
                redisService.lPush(key,lawId);
                redisService.add(lawId,index);
            }
            return Result.ok().msg("收藏成功");
        } else {
            QueryWrapper<Favorite> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id",userId).eq("law_id",lawId+":"+index);
            int isSuccess = baseMapper.delete(wrapper);
            if (isSuccess<=0){
                throw new MyException(ResultCode.ERROR,"不存在收藏，无法取消");
            }
            redisTemplate.opsForList().remove(key,1,lawId);
            redisTemplate.opsForSet().remove(lawId,1,index);
            return Result.ok().msg("取消收藏成功");

        }
    }

//    @Override
//    public void favorite(String lawId,Long index, Boolean isFavorite) {
//        //从Spring Security上下文获取用户信息
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        //获取用户信息
//        User user = (User) authentication.getPrincipal();
//        Long userId = user.getId();
//        String key = MyConstant.FAVORITES +userId;
//        if (isFavorite){
//            Favorite favorite = new Favorite();
//            favorite.setUserId(userId);
//            favorite.setLawId(lawId+":"+index);
//            int isSuccess = baseMapper.insert(favorite);
//            if (isSuccess>0){
//                redisService.add(key,lawId+":"+index);
//            }
//        } else {
//            QueryWrapper<Favorite> wrapper = new QueryWrapper<>();
//            wrapper.eq("user_id",userId).eq("law_id",lawId+":"+index);
//            int isSuccess = baseMapper.delete(wrapper);
//            if (isSuccess>0){
//                redisService.remove(key,lawId+":"+index);
//            }
//
//        }
//    }

    @Override
    public Long isFavorite(String lawId, String index) {
        //从Spring Security上下文获取用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //获取用户信息
        User user = (User) authentication.getPrincipal();
        Long userId = user.getId();
        QueryWrapper<Favorite> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId).eq("law_id",lawId+":"+index);
        Long count = baseMapper.selectCount(wrapper);
        return count;
    }
}
















