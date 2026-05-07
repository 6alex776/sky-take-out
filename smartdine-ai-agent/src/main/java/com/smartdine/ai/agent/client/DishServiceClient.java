package com.smartdine.ai.agent.client;

import com.smartdine.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 菜品服务Feign客户端
 */
@FeignClient(name = "smartdine-service", contextId = "dishServiceClient")
public interface DishServiceClient {

    @GetMapping("/admin/dish/list")
    Result<List<Map<String, Object>>> list(@RequestParam("categoryId") Long categoryId);

    @GetMapping("/admin/dish/{id}")
    Result<Map<String, Object>> getById(@PathVariable("id") Long id);

    @GetMapping("/admin/dish/page")
    Result<Map<String, Object>> page(
            @RequestParam("page") Integer page,
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "status", required = false) Integer status);
}
