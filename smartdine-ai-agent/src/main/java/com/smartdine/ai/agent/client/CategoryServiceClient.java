package com.smartdine.ai.agent.client;

import com.smartdine.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 分类服务Feign客户端
 */
@FeignClient(name = "smartdine-service", contextId = "categoryServiceClient")
public interface CategoryServiceClient {

    @GetMapping("/admin/category/list")
    Result<List<Map<String, Object>>> list(@RequestParam("type") Integer type);
}
