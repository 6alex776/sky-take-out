package com.smartdine.ai.agent.core.tool;

import com.smartdine.ai.agent.client.CategoryServiceClient;
import com.smartdine.ai.agent.client.DishServiceClient;
import com.smartdine.result.Result;
import dev.langchain4j.agent.tool.Tool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 菜单/菜品查询工具类
 * 供AI Agent查询菜品、分类等信息
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MenuTools {

    private final DishServiceClient dishServiceClient;
    private final CategoryServiceClient categoryServiceClient;

    /**
     * 查询菜品分类列表
     */
    @Tool("查询菜品分类列表，type=1为菜品分类，type=2为套餐分类")
    public String getCategoryList(Integer type) {
        try {
            Result<List<Map<String, Object>>> result = categoryServiceClient.list(type);
            if (result != null && result.getData() != null) {
                return formatCategoryList(result.getData());
            }
            return "暂无分类数据";
        } catch (Exception e) {
            log.error("获取分类列表失败", e);
            return "获取数据失败: " + e.getMessage();
        }
    }

    /**
     * 查询分类下的菜品列表
     */
    @Tool("根据分类ID查询该分类下的所有菜品")
    public String getDishListByCategory(Long categoryId) {
        try {
            Result<List<Map<String, Object>>> result = dishServiceClient.list(categoryId);
            if (result != null && result.getData() != null) {
                return formatDishList(result.getData());
            }
            return "暂无菜品数据";
        } catch (Exception e) {
            log.error("获取菜品列表失败", e);
            return "获取数据失败: " + e.getMessage();
        }
    }

    /**
     * 查询菜品详情
     */
    @Tool("根据菜品ID查询菜品详细信息")
    public String getDishDetail(Long dishId) {
        try {
            Result<Map<String, Object>> result = dishServiceClient.getById(dishId);
            if (result != null && result.getData() != null) {
                return "菜品详情: " + result.getData().toString();
            }
            return "未找到该菜品";
        } catch (Exception e) {
            log.error("获取菜品详情失败", e);
            return "获取数据失败: " + e.getMessage();
        }
    }

    /**
     * 分页查询菜品
     */
    @Tool("分页查询菜品列表，支持按名称和分类筛选")
    public String getDishPage(Integer page, Integer pageSize, String name, Long categoryId) {
        try {
            Result<Map<String, Object>> result = dishServiceClient.page(page, pageSize, name, categoryId, null);
            if (result != null && result.getData() != null) {
                return "菜品列表: " + result.getData().toString();
            }
            return "暂无菜品数据";
        } catch (Exception e) {
            log.error("获取菜品分页失败", e);
            return "获取数据失败: " + e.getMessage();
        }
    }

    private String formatCategoryList(List<Map<String, Object>> categories) {
        StringBuilder sb = new StringBuilder();
        sb.append("【分类列表】\n");
        for (Map<String, Object> cat : categories) {
            sb.append("- ").append(cat.getOrDefault("name", "未知"))
              .append(" (ID: ").append(cat.getOrDefault("id", "N/A")).append(")\n");
        }
        return sb.toString();
    }

    private String formatDishList(List<Map<String, Object>> dishes) {
        StringBuilder sb = new StringBuilder();
        sb.append("【菜品列表】\n");
        for (Map<String, Object> dish : dishes) {
            sb.append("- ").append(dish.getOrDefault("name", "未知"))
              .append(" 价格: ¥").append(dish.getOrDefault("price", "N/A"))
              .append(" 状态: ").append(dish.getOrDefault("status", 0).equals(1) ? "在售" : "停售")
              .append("\n");
        }
        return sb.toString();
    }
}
