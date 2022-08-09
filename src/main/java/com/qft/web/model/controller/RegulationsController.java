package com.qft.web.model.controller;

import com.qft.web.util.Result;
import com.qft.web.vo.query.RefereeQueryVO;
import com.qft.web.vo.query.RegulationsQueryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "法律法规",tags = "法律法规",description = "法律法规")
@RestController
@RequestMapping("/api/regulations")
public class RegulationsController {
    /**
     * 法律法规搜索
     * @return
     */
    @ApiOperation("法律法规搜索")
    @PostMapping("/regulationsSearch")
    public Result regulationsSearch(@RequestBody RegulationsQueryVO queryVO){





        return null;
    }
}
