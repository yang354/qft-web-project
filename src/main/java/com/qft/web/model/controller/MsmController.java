package com.qft.web.model.controller;


import com.qft.web.config.redis.RedisService;
import com.qft.web.model.service.MsmService;
import com.qft.web.util.MyConstant;
import com.qft.web.util.RandomUtil;
import com.qft.web.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

//通用注册和忘记密码的发送短信和验证码确认
@Api(value = "发送短信",tags = "发送短信",description = "发送短信")
@RestController
@RequestMapping("/api/msm")
public class MsmController {

    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisService redisService;


    @ApiOperation("判断验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "验证码",name = "num",dataType = "String"),
            @ApiImplicitParam(value = "手机号",name = "phone",dataType = "String")
    })
    @PostMapping("/verification")
    public Result verification(String num,String phone){
        //判断空
        if (ObjectUtils.isEmpty(num)){
            return Result.error().msg("请输入验证码");
        }
        if (ObjectUtils.isEmpty(phone)){
            return Result.error().msg("请输入手机号");
        }
        String code = redisService.get(MyConstant.MSG + phone);
        if (!num.equals(code)){
            return Result.error().msg("验证码错误");
        }
        return Result.ok().msg("验证成功");
    }



    //发送短信的方法
    @ApiOperation("发送短信")
    @ApiImplicitParam(value = "手机号",name = "phone",dataType = "String")
    @GetMapping("/send")
    public Result sendMsm(String phone){
        if(ObjectUtils.isEmpty(phone)){
            return Result.error().msg("手机号为空");
        }
        //1、从redis中获取验证码，如果获取到直接返回
        String code = redisService.get(MyConstant.MSG+phone);
        if (!ObjectUtils.isEmpty(code)){
            return Result.ok().msg("短信已发送成功");
        }
        //2、如果redis获取不到，进行阿里云发送

        //生成随机数，传递阿里云进行发送
        code = RandomUtil.getFourBitRandom();

        redisService.set(MyConstant.MSG+phone,code,300L);
        return Result.ok();


//        Map<String, Object> param = new HashMap<>();
//        param.put("code",code);
//        //调用service的方法，发送
//        boolean isSend = msmService.send(param,phone);
//        if (isSend) {
//            //阿里云发送成功，把发送成功的验证码放入redis缓存中
//            //设置有效时间
//            redisService.set(MyConstant.MSG+phone,code,500000L);
//            return Result.ok().msg("短信发送成功");
//        }else{
//            return Result.error().msg("短信发送失败");
//        }

    }
}
