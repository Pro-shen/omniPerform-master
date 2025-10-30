package com.omniperform.web.controller;

import com.omniperform.common.annotation.Anonymous;
import com.omniperform.web.common.Result;
import com.omniperform.web.domain.KnowledgeBase;
import com.omniperform.web.domain.KnowledgeCategory;
import com.omniperform.web.service.IKnowledgeBaseService;
import com.omniperform.web.service.IKnowledgeCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 知识库管理
 * 
 * @author omniperform
 */
@RestController
@RequestMapping("/knowledge")
@Api(tags = "知识库管理")
@Anonymous
@CrossOrigin(origins = "*")
public class KnowledgeController {
    
    private static final Logger logger = LoggerFactory.getLogger(KnowledgeController.class);
    
    @Autowired
    private IKnowledgeCategoryService knowledgeCategoryService;
    
    @Autowired
    private IKnowledgeBaseService knowledgeBaseService;

    /**
     * 获取知识库分类列表
     */
    @GetMapping("/categories")
    @ApiOperation("获取知识库分类列表")
    public Result categories() {
        try {
            List<Map<String, Object>> categories = knowledgeCategoryService.getCategoriesWithCount();
            
            logger.info("获取知识库分类列表成功，共{}个分类", categories.size());
            return Result.success("获取知识库分类列表成功", categories);
        } catch (Exception e) {
            logger.error("获取知识库分类失败: {}", e.getMessage(), e);
            return Result.error("获取知识库分类失败");
        }
    }

    /**
     * 获取知识列表
     */
    @GetMapping("/list")
    @ApiOperation("获取知识列表")
    public Result list(@RequestParam(defaultValue = "1") int page,
                      @RequestParam(defaultValue = "10") int size,
                      @RequestParam(required = false) String category,
                      @RequestParam(required = false) String search,
                      @RequestParam(defaultValue = "views") String sortBy) {
        try {
            // Convert category parameter (could be ID or code) to categoryCode
            String categoryCode = null;
            if (category != null && !category.isEmpty()) {
                // Try to parse as ID first
                 try {
                     Long categoryId = Long.parseLong(category);
                     KnowledgeCategory categoryObj = knowledgeCategoryService.selectKnowledgeCategoryByCategoryId(categoryId);
                     if (categoryObj != null) {
                         categoryCode = categoryObj.getCategoryCode();
                     }
                 } catch (NumberFormatException e) {
                     // If not a number, treat as category code
                     categoryCode = category;
                 }
            }
            
            // Get knowledge list with conditions
            Map<String, Object> result = knowledgeBaseService.getKnowledgeListWithConditions(page, size, categoryCode, search, sortBy);
            
            return Result.success(result);
        } catch (Exception e) {
            logger.error("获取知识库列表失败", e);
            return Result.error("获取知识库列表失败");
        }
    }

    /**
     * 获取知识详情
     */
    @GetMapping("/detail/{id}")
    @ApiOperation("获取知识详情")
    public Result getKnowledgeDetail(@PathVariable String id) {
        try {
            Long knowledgeId = Long.parseLong(id);
            KnowledgeBase knowledgeDetail = knowledgeBaseService.getKnowledgeDetail(knowledgeId);
            
            if (knowledgeDetail == null) {
                return Result.error("知识库文章不存在");
            }
            
            logger.info("获取知识详情成功，ID: {}", id);
            return Result.success(knowledgeDetail);
        } catch (NumberFormatException e) {
            logger.error("无效的知识库ID: {}", id);
            return Result.error("无效的知识库ID");
        } catch (Exception e) {
            logger.error("获取知识详情失败", e);
            return Result.error("获取知识详情失败");
        }
    }


}