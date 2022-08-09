package com.qft.web.model.controller;

import com.qft.web.util.Result;
import com.qft.web.vo.query.RefereeQueryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@Api(value = "裁判文书",tags = "裁判文书",description = "裁判文书")
@RestController
@RequestMapping("/api/referee")
public class RefereeController {

    /**
     * 裁判文书搜索
     * @return
     */
    @ApiOperation("裁判文书搜索")
    @PostMapping("/refereeSearch")
    public Result refereeSearch(@RequestBody RefereeQueryVO queryVO){





        return null;
    }
}
