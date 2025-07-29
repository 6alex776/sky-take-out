//redis方法
package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("userShopController")
@Slf4j
@Api(tags = "客户端店铺相关接口")
@RequestMapping("/user/shop")
public class ShopController {

    @Autowired
    private RedisTemplate redisTemplate;

    //管理端查询状态
    @GetMapping("/status")
    public Result<Integer> getStatus() {

        Integer status = (Integer) redisTemplate.opsForValue().get("status");
        log.info("getStatus{}", status == 1 ? "营业中" : "打样中");
        return Result.success();
    }
}
