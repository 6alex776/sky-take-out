//redis方法
package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@Slf4j
@Api(tags = "店铺相关接口")
@RequestMapping("/admin/shop")
public class ShopController {

    @Autowired
    private RedisTemplate redisTemplate;

    //设置店铺状态
    @PutMapping("/{status}")
    public Result setStatus(@PathVariable Integer status) {

        log.info("setStatus{}", status == 1 ? "营业中" : "打样中");
        redisTemplate.opsForValue().set("status", status);
        return Result.success();
    }


    //管理端查询状态
    @GetMapping("/status")
    public Result<Integer> getStatus() {

        Integer status = (Integer) redisTemplate.opsForValue().get("status");
        log.info("getStatus{}", status == 1 ? "营业中" : "打样中");
        return Result.success(status);
    }
}
