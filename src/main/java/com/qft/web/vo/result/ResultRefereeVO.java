package com.qft.web.vo.result;

import lombok.Data;

import java.util.List;


//接收响应的json数据
@Data
public class ResultRefereeVO {
    private Integer code;
    private List<RefereeVO> result;
    private Integer total;
}
