package com.qft.web.exception;




import com.qft.web.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //指定出现什么异常执行这个方法
    @ExceptionHandler(Exception.class)
    @ResponseBody //为了返回数据
    public Result error(Exception e) {
        e.printStackTrace();
        return Result.error().msg("执行了全局异常处理..");
    }



    //自定义异常（这种就要捕获异常自己手动抛出异常，常用）
    @ExceptionHandler(MyException.class)
    @ResponseBody //为了返回数据
    public Result error(MyException e) {
        log.error(e.getMessage());
        e.printStackTrace();

        return Result.error().code(e.getCode()).msg(e.getMsg());
    }

}
