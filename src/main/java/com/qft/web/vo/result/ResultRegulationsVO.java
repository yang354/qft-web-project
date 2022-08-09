package com.qft.web.vo.result;

import lombok.Data;

import java.util.List;

@Data
public class ResultRegulationsVO {
    private Integer code;
    private List<RegulationsVO> result;
    private Integer total;

}
