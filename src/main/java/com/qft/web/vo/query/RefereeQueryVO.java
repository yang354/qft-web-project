package com.qft.web.vo.query;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class RefereeQueryVO {
    private String cat;
    private String type;
    private List<String> keywords;
    private Integer page_num;
    private Integer page_size;
    private Map<String,Object> filter;
}
