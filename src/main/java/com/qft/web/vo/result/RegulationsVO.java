package com.qft.web.vo.result;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@ToString
@Data
public class RegulationsVO {
    private String _id;
    private String area;
    private String context;
    private String detailLink;
    private String expiry;
    private List<String> keyword_hit_strips;
    private String law_type;
    private String level;
    private String office;
    private String publish;
    private String status;
    private String title;
    private List<String> strip_list;
}
