package com.qft.web.model.service;

import com.qft.web.model.entity.Favorite;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qft.web.util.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yyyz
 * @since 2022-08-08
 */
public interface FavoriteService extends IService<Favorite> {

    Result favorite(String lawId, Long index, Boolean isFavorite);

    Long isFavorite(String lawId,Long index);
}
