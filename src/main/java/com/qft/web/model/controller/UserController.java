package com.qft.web.model.controller;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.qft.web.config.redis.RedisService;
import com.qft.web.model.entity.User;
import com.qft.web.model.service.UserService;
import com.qft.web.util.HttpClientUtils;
import com.qft.web.util.JwtUtils;
import com.qft.web.util.MyConstant;
import com.qft.web.util.Result;
import com.qft.web.vo.TokenVO;

import com.qft.web.vo.query.IdQueryVO;
import com.qft.web.vo.query.RefereeQueryVO;
import com.qft.web.vo.result.RegulationsVO;
import com.qft.web.vo.result.ResultRegulationsVO;
import com.qft.web.vo.user.UserCenterVO;
import com.qft.web.vo.user.UserInfoVO;
import com.qft.web.vo.user.RegisterVO;
import io.jsonwebtoken.Jwts;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Api(value = "用户",tags = "用户",description = "用户")
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private RedisService redisService;
    @Resource
    private JwtUtils jwtUtils;


    /**
     * 查询所有用户列表
     * @return
     */
    @ApiOperation("查询所有用户列表")
    @GetMapping("/listAll")
    public Result listAll(){
        return Result.ok(userService.list());
    }


    /**
     * 修改密码
     * @return
     */
    @ApiOperation("修改密码")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户名",name = "username",dataType = "String"),
            @ApiImplicitParam(value = "密码",name = "password",dataType = "String")
    })
    @PostMapping("/updatePassword")
    public Result updatePassword(String username, String password){
        userService.updatePassword(username,password);

        return Result.ok().msg("密码更新成功");
    }





    /**
     * 注册
     *
     * @return
     */
    @ApiOperation("注册")
    @ApiImplicitParam(value = "registerVO",name = "registerVO",dataType = "RegisterVO")
    @PostMapping("/register")
    public Result register(@RequestBody RegisterVO registerVO) {
        //先写一个功能短信发送

        userService.register(registerVO);
        return Result.ok().msg("注册成功");
    }

    /**
     * 获取用户中心
     *
     * @return
     */
    @ApiOperation("获取用户中心")
    @GetMapping("/userCenter")
    public Result getUserCenter() throws Exception {
        //从Spring Security上下文获取用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Long userId = user.getId();

        List<Object> lawIdList = redisService.lRange(MyConstant.FAVORITES + userId, 0, -1);
        //去重
        LinkedHashSet<Object> hashSet = new LinkedHashSet<>(lawIdList);
        //System.out.println(lawIdList);
        ArrayList<Object> arrayList = new ArrayList<>(hashSet);

//###########################测试###################################
//        HashMap<String, List<String>> map = new HashMap<>();
//        for (Object o:arrayList){
//            String lawId = (String) o;
//            Set<Object> indexSet = redisService.setMembers(lawId);
//            System.out.println(indexSet);
//            List<String> indexList = new ArrayList<>();
//            Iterator iterator = indexSet.iterator();
//            while (iterator.hasNext()){
//                String index = (String) iterator.next();
//                indexList.add(index);
//            }
//            map.put(lawId,indexList);
//        }
//        //创建用户信息对象
//        UserCenterVO userCenterVO = new UserCenterVO();
//        BeanUtils.copyProperties(user,userCenterVO);
//        userCenterVO.setFavorites(map);
//        //返回数据
//        return Result.ok(userCenterVO);
//################################################3
        HashMap<String, List<String>> map = new HashMap<>();
        for (Object o:arrayList){
            String lawId = (String) o;
            Set<Object> indexSet = redisService.setMembers(lawId);
            List<String> indexList = new ArrayList<>();
            Iterator iterator = indexSet.iterator();
            while (iterator.hasNext()){
                String index = (String) iterator.next();
                indexList.add(index);
            }
            map.put(lawId,indexList);
        }
        HashMap<String, List<String>> result = new HashMap<>();
        Iterator<String> lawIdKey = map.keySet().iterator();
        List<String> list = new ArrayList<>();

        while (lawIdKey.hasNext()){
            String lawId = lawIdKey.next();
            List<String> strings = map.get(lawId);
            IdQueryVO queryVO = new IdQueryVO();
            queryVO.setCat("LEGAL_PROVISION");
            queryVO.setId(lawId);
//            HashMap<String, Integer> option = new HashMap<>();
//            option.put("条",Integer.valueOf(1));
//            option.put("款",Integer.valueOf(-1));
//            queryVO.setOption(option);
            String objJson = JSON.toJSONString(queryVO, SerializerFeature.WriteClassName);
            String s = HttpClientUtils.doPost(
                    "http://houlong66.cn:27623/iais/sdk/api/v1/search-by-id",
                    "post",
                    objJson);

            System.out.println(s);
            ResultRegulationsVO regulationsVO =JSON.parseObject(s, ResultRegulationsVO.class);
            RegulationsVO regulationsVO1 = regulationsVO.getResult().get(0);
            List<String> hit_strips = regulationsVO1.getStrip_list();
            for (String s2:strings){
                for (String s1:hit_strips) {
                    boolean contains = s1.contains(s2);
                    if (contains){
                        System.out.println("****");
                        System.out.println(s1);
                    }
                }
            }






//            for (Integer integer:integers){
//                System.out.println(lawId+"  "+integer );
//                list.add(hit_strips.get(integer-1));
//            }

            result.put(regulationsVO1.getTitle(),list);
        }
        //创建用户信息对象
        UserCenterVO userCenterVO = new UserCenterVO();
        BeanUtils.copyProperties(user,userCenterVO);
        userCenterVO.setFavorites(result);
        //返回数据
        return Result.ok(userCenterVO);
    }


    /**
     * 获取用户信息
     *
     * @return
     */
    @ApiOperation("获取用户信息")
    @GetMapping("/getInfo")
    public Result getInfo() {
        //从Spring Security上下文获取用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //判断authentication对象是否为空
        if (authentication == null) {
            return Result.error().msg("用户信息查询失败");
        }
        //获取用户信息
        User user = (User) authentication.getPrincipal();

        //创建用户信息对象
        UserInfoVO userInfo = new UserInfoVO();
        BeanUtils.copyProperties(user,userInfo);

        //UserInfo userInfo = new UserInfo(user.getId(), user.getUsername(), user.getAvatar(),user.getPhone(),userType.getTypeName());
        //返回数据
        return Result.ok(userInfo);
    }


    /**
     * 刷新token
     *
     * @param request
     * @return
     */
    @ApiOperation("刷新token")
    @ApiImplicitParam(value = "请求",name = "request",dataType = "HttpServletRequest")
    @PostMapping("/refreshToken")
    public Result refreshToken(HttpServletRequest request) {
        //从header中获取前端提交的token
        String token = request.getHeader("token");
        //如果header中没有token，则从参数中获取
        if (ObjectUtils.isEmpty(token)) {
            token = request.getParameter("token");
        }
        //从Spring Security上下文获取用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //获取身份信息
        UserDetails details = (UserDetails) authentication.getPrincipal();
        //重新生成token
        String reToken = "";
        //验证原来的token是否合法
        if (jwtUtils.validateToken(token, details)) {
            //生成新的token
            reToken = jwtUtils.refreshToken(token);
        }
        //获取本次token的到期时间，交给前端做判断
        long expireTime = Jwts.parser().setSigningKey(jwtUtils.getSecret())
                .parseClaimsJws(reToken.replace("jwt_", ""))
                .getBody().getExpiration().getTime();
        //清除原来的token信息
        String oldTokenKey = "token_" + token;
        redisService.del(oldTokenKey);
        //存储新的token
        String newTokenKey = "token_" + reToken;
        redisService.set(newTokenKey, reToken, jwtUtils.getExpiration() / 1000);
        //创建TokenVo对象
        TokenVO tokenVo = new TokenVO(expireTime, reToken);
        //返回数据
        return Result.ok(tokenVo).msg("token生成成功");
    }

    /**
     * 用户退出
     *
     * @param request
     * @param response
     * @return
     */
    @ApiOperation("用户退出")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "请求",name = "request",dataType = "HttpServletRequest"),
            @ApiImplicitParam(value = "响应",name = "response",dataType = "HttpServletResponse")
    })
    @PostMapping("/logout")
    public Result logout(HttpServletRequest request, HttpServletResponse response) {
        //获取token
        String token = request.getParameter("token");
        //如果没有从头部获取token，那么从参数里面获取
        if (ObjectUtils.isEmpty(token)) {
            token = request.getHeader("token");
        }
        //获取用户相关信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            //清空用户信息
            new SecurityContextLogoutHandler().logout(request, response,authentication);
            //清空redis里面的token
            String key = "token_" + token;
            redisService.del(key);
        }
        return Result.ok().msg("用户退出成功");
    }



}

