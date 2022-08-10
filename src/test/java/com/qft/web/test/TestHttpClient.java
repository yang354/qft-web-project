package com.qft.web.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import com.qft.web.util.HttpClientUtils;

import com.qft.web.util.JsonParseUtil;
import com.qft.web.vo.query.RefereeQueryVO;


import com.qft.web.vo.query.RegulationsQueryVO;
import com.qft.web.vo.result.RefereeVO;
import com.qft.web.vo.result.RegulationsVO;
import com.qft.web.vo.result.ResultRefereeVO;
import com.qft.web.vo.result.ResultRegulationsVO;
import com.qft.web.vo.test.Goods;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SpringBootTest
public class TestHttpClient {

    @Test
    public void test1() throws IOException {
        String s1 = "   第一条 总分多少第一条你好";
        String split = s1.substring(s1.indexOf("第"),s1.indexOf("条")+1);
        System.out.println(split);
        RegulationsQueryVO queryVO = new RegulationsQueryVO();
        queryVO.setCat("LEGAL_PROVISION");
        queryVO.setType("precise");
        List<String> keywords = new ArrayList<>();
        keywords.add("民族");
        keywords.add("精神");
        queryVO.setKeywords(keywords);
        queryVO.setPage_num(0);
        queryVO.setPage_size(10);
        HashMap<String, Object> filter = new HashMap<>();
        filter.put("place","context");
        filter.put("range","条");
        filter.put("level","地方性法规");
//        filter.put("area","重庆");
//        filter.put("status","有效");
//        List<String> valid_range = new ArrayList<>();
//        valid_range.add("1998-01-01");
//        valid_range.add("2022-01-01");
//        filter.put("valid_range",valid_range);

        String objJson = JSON.toJSONString(queryVO, SerializerFeature.WriteClassName);
        String s = HttpClientUtils.doPost(
                "http://houlong66.cn:27623/iais/sdk/api/v1/search",
                "post",
                objJson);
        System.out.println(s);

        ResultRegulationsVO regulationsVO =JSON.parseObject(s, ResultRegulationsVO.class);


        List<RegulationsVO> result = regulationsVO.getResult();
        for (RegulationsVO regulationsVO1:result){
            System.out.println(regulationsVO1.getContext());
        }



    }

    @Test
    public void test() throws IOException {
        //1.封装成对象
        RefereeQueryVO testVO = new RefereeQueryVO();
        testVO.setCat("JUDGEMENT_DOCUMENT");
        testVO.setType("precise");
        ArrayList<String> keywords = new ArrayList<>();
        keywords.add("犯罪");
        testVO.setKeywords(keywords);
        testVO.setPage_size(10);
        testVO.setPage_num(0);
        HashMap<String, Object> filter = new HashMap<>();
        filter.put("place","context");
        filter.put("range","篇");
        filter.put("court","湖南省衡南县人民法院");
        filter.put("document_type","02");
        filter.put("case_number","（2020）湘0422执55号");
        filter.put("case_type","执行案件");
        List<String> party = new ArrayList<>();
        party.add("谢林达");
        party.add("殷爱军");
        List<String> referee = new ArrayList<>();
        referee.add("2019-04-06");
        referee.add("2022-05-01");
        filter.put("party",party);
        filter.put("referee",referee);
        testVO.setFilter(filter);


        String objJson = JSON.toJSONString(testVO, SerializerFeature.WriteClassName);
        String s = HttpClientUtils.doPost(
                "http://houlong66.cn:27623/iais/sdk/api/v1/search",
                "post",
                objJson);

        ResultRefereeVO resultRefereeVO =JSON.parseObject(s, ResultRefereeVO.class);
        System.out.println(resultRefereeVO.getCode());
        System.out.println(resultRefereeVO.getTotal());
        List<RefereeVO> result = resultRefereeVO.getResult();
        for (RefereeVO refereeVO:result){
            System.out.println(refereeVO.getCourt_category());
        }
    }
}
