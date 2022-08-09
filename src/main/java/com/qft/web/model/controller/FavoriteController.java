package com.qft.web.model.controller;


import com.qft.web.model.service.FavoriteService;
import com.qft.web.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yyyz
 * @since 2022-08-08
 */
@Api(value = "收藏",tags = "收藏",description = "收藏")
@RestController
@RequestMapping("/api/favorite")
public class FavoriteController {
    @Resource
    private FavoriteService favoriteService;

    @ApiOperation("收藏和取消收藏")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "法律id",name = "lawId",dataType = "String"),
            @ApiImplicitParam(value = "索引",name = "index",dataType = "Long"),
            @ApiImplicitParam(value = "是否收藏",name = "isFavorite",dataType = "Boolean")
    })
    @PutMapping("/{id}/{index}/{isFavorite}")
    public Result favorite(
            @PathVariable("id") String lawId,
            @PathVariable("index") Long index,
            @PathVariable("isFavorite") Boolean isFavorite){
        return favoriteService.favorite(lawId,index,isFavorite);

    }

    @ApiOperation("是否收藏")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "法律id",name = "lawId",dataType = "String"),
            @ApiImplicitParam(value = "索引",name = "index",dataType = "Long")
    })
    @GetMapping("/or/not/{id}/{index}")
    public Result isFavorite(@PathVariable("id") String lawId,@PathVariable("index") Long index){
        Long count = favoriteService.isFavorite(lawId,index);
        return Result.ok(count>0);
    }
























}

