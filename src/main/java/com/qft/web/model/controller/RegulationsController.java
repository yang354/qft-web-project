package com.qft.web.model.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.qft.web.config.redis.RedisService;
import com.qft.web.model.entity.User;
import com.qft.web.model.service.FavoriteService;
import com.qft.web.util.HttpClientUtils;
import com.qft.web.util.MyConstant;
import com.qft.web.util.Result;

import com.qft.web.vo.query.RegulationsQueryVO;
import com.qft.web.vo.result.RegulationsVO;
import com.qft.web.vo.result.ResultRegulationsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

import java.util.*;


@Api(value = "法律法规",tags = "法律法规",description = "法律法规")
@RestController
@RequestMapping("/api/regulations")
public class RegulationsController {

    @Resource
    private FavoriteService favoriteService;
    @Resource
    private RedisService redisService;
    /**
     * 法律法规搜索
     * @return
     */
//    @ApiOperation("法律法规搜索")
//    @PostMapping("/regulationsSearch")
//    public Result regulationsSearch(@RequestBody RegulationsQueryVO queryVO) throws IOException {
//        String objJson = JSON.toJSONString(queryVO, SerializerFeature.WriteClassName);
//        String s = HttpClientUtils.doPost(
//                "http://houlong66.cn:27623/iais/sdk/api/v1/search",
//                "post",
//                objJson);
//        //System.out.println(s);
//
//        ResultRegulationsVO resultRegulationsVO =JSON.parseObject(s, ResultRegulationsVO.class);
//        List<RegulationsVO> result = resultRegulationsVO.getResult();
//
//        for (RegulationsVO regulationsVO : result) {
//            String regulationsVO_id = regulationsVO.get_id();
//            //从Spring Security上下文获取用户信息
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            User user = (User) authentication.getPrincipal();
//            Long userId = user.getId();
//            List<Object> lawIdList = redisService.lRange(MyConstant.FAVORITES + userId, 0, -1);
//            //去重
//            LinkedHashSet<Object> hashSet = new LinkedHashSet<>(lawIdList);
//            //System.out.println(lawIdList);
//            ArrayList<Object> arrayList = new ArrayList<>(hashSet);
//            ArrayList<String> list = new ArrayList<>();
//            for (Object o : arrayList) {
//                list.add((String)o);
//            }
//            if (list.contains(regulationsVO_id)){
//                Set<Object> objects = redisService.setMembers(regulationsVO_id);
//                HashSet<String> hashSet1 = new HashSet<>();
//                Iterator<Object> iterator = objects.iterator();
//                while (iterator.hasNext()){
//                    hashSet1.add((String) iterator.next());
//                }
//                List<String> keyword_hit_strips = regulationsVO.getKeyword_hit_strips();
//                for (int i=0; i<keyword_hit_strips.size();i++) {
//                    //System.out.println(keyword_hit_strips.get(i));
//                    String s1 = keyword_hit_strips.get(i);
//                    Iterator<String> iterator1 = hashSet1.iterator();
//                    while (iterator1.hasNext()){
//                        if (s1.contains(iterator1.next())){
//                            keyword_hit_strips.set(i,keyword_hit_strips.get(i)+" "+1);
//                            System.out.println(keyword_hit_strips.get(i));
//                        }
//                    }
//                }
//            }
//        }
//        return Result.ok(result);
//    }

    public Long isFavorite(String lawId,String index){
        Long count = favoriteService.isFavorite(lawId,index);
        return count;
    }

//    @GetMapping("/or/not/{id}/{index}")
//    public Result isFavorite(@PathVariable("id") String lawId,@PathVariable("index") String index){
//        Long count = favoriteService.isFavorite(lawId,index);
//        return Result.ok(count>0);
//    }



//############################测试收藏点亮使用###################################################
    @GetMapping("/regulationsSearch")
    public Result regulationsSearch() throws IOException {

        RegulationsQueryVO regulationsQueryVO = new RegulationsQueryVO();
        regulationsQueryVO.setCat("LEGAL_PROVISION");
        regulationsQueryVO.setType("precise");
        List<String> keywords = new ArrayList<>();
        keywords.add("民族");
        keywords.add("精神");
        regulationsQueryVO.setKeywords(keywords);
        regulationsQueryVO.setPage_num(0);
        regulationsQueryVO.setPage_size(10);
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

        String objJson = JSON.toJSONString(regulationsQueryVO, SerializerFeature.WriteClassName);
        String s = HttpClientUtils.doPost(
                "http://houlong66.cn:27623/iais/sdk/api/v1/search",
                "post",
                objJson);
        System.out.println(s);

        ResultRegulationsVO resultRegulationsVO =JSON.parseObject(s, ResultRegulationsVO.class);
        List<RegulationsVO> result = resultRegulationsVO.getResult();

        for (RegulationsVO regulationsVO : result) {
            String regulationsVO_id = regulationsVO.get_id();
            //从Spring Security上下文获取用户信息
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();
            Long userId = user.getId();
            List<Object> lawIdList = redisService.lRange(MyConstant.FAVORITES + userId, 0, -1);
            //去重
            LinkedHashSet<Object> hashSet = new LinkedHashSet<>(lawIdList);
            //System.out.println(lawIdList);
            ArrayList<Object> arrayList = new ArrayList<>(hashSet);
            ArrayList<String> list = new ArrayList<>();
            for (Object o : arrayList) {
                list.add((String)o);
            }
            if (list.contains(regulationsVO_id)){
                Set<Object> objects = redisService.setMembers(regulationsVO_id);
                HashSet<String> hashSet1 = new HashSet<>();
                Iterator<Object> iterator = objects.iterator();
                while (iterator.hasNext()){
                    hashSet1.add((String) iterator.next());
                }
                List<String> keyword_hit_strips = regulationsVO.getKeyword_hit_strips();
                for (int i=0; i<keyword_hit_strips.size();i++) {
                    //System.out.println(keyword_hit_strips.get(i));
                    String s1 = keyword_hit_strips.get(i);
                    Iterator<String> iterator1 = hashSet1.iterator();
                    while (iterator1.hasNext()){
                        if (s1.contains(iterator1.next())){
                            keyword_hit_strips.set(i,keyword_hit_strips.get(i)+" "+1);
                            System.out.println(keyword_hit_strips.get(i));
                        }
                    }
                }
            }
        }


        return Result.ok(result);
    }
}
