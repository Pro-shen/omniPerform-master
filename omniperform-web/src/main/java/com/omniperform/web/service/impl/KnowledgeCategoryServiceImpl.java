package com.omniperform.web.service.impl;

import com.omniperform.web.domain.KnowledgeCategory;
import com.omniperform.web.mapper.KnowledgeCategoryMapper;
import com.omniperform.web.mapper.KnowledgeBaseMapper;
import com.omniperform.web.service.IKnowledgeCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 知识库分类Service业务层处理
 * 
 * @author omniperform
 * @date 2025-01-16
 */
@Service
public class KnowledgeCategoryServiceImpl implements IKnowledgeCategoryService {
    @Autowired
    private KnowledgeCategoryMapper knowledgeCategoryMapper;

    @Autowired
    private KnowledgeBaseMapper knowledgeBaseMapper;

    /**
     * 查询知识库分类
     * 
     * @param categoryId 知识库分类主键
     * @return 知识库分类
     */
    @Override
    public KnowledgeCategory selectKnowledgeCategoryByCategoryId(Long categoryId) {
        return knowledgeCategoryMapper.selectKnowledgeCategoryByCategoryId(categoryId);
    }

    /**
     * 根据分类代码查询知识库分类
     * 
     * @param categoryCode 分类代码
     * @return 知识库分类
     */
    @Override
    public KnowledgeCategory selectKnowledgeCategoryByCategoryCode(String categoryCode) {
        return knowledgeCategoryMapper.selectKnowledgeCategoryByCategoryCode(categoryCode);
    }

    /**
     * 查询知识库分类列表
     * 
     * @param knowledgeCategory 知识库分类
     * @return 知识库分类
     */
    @Override
    public List<KnowledgeCategory> selectKnowledgeCategoryList(KnowledgeCategory knowledgeCategory) {
        return knowledgeCategoryMapper.selectKnowledgeCategoryList(knowledgeCategory);
    }

    /**
     * 新增知识库分类
     * 
     * @param knowledgeCategory 知识库分类
     * @return 结果
     */
    @Override
    public int insertKnowledgeCategory(KnowledgeCategory knowledgeCategory) {
        return knowledgeCategoryMapper.insertKnowledgeCategory(knowledgeCategory);
    }

    /**
     * 修改知识库分类
     * 
     * @param knowledgeCategory 知识库分类
     * @return 结果
     */
    @Override
    public int updateKnowledgeCategory(KnowledgeCategory knowledgeCategory) {
        return knowledgeCategoryMapper.updateKnowledgeCategory(knowledgeCategory);
    }

    /**
     * 批量删除知识库分类
     * 
     * @param categoryIds 需要删除的知识库分类主键
     * @return 结果
     */
    @Override
    public int deleteKnowledgeCategoryByCategoryIds(Long[] categoryIds) {
        return knowledgeCategoryMapper.deleteKnowledgeCategoryByCategoryIds(categoryIds);
    }

    /**
     * 删除知识库分类信息
     * 
     * @param categoryId 知识库分类主键
     * @return 结果
     */
    @Override
    public int deleteKnowledgeCategoryByCategoryId(Long categoryId) {
        return knowledgeCategoryMapper.deleteKnowledgeCategoryByCategoryId(categoryId);
    }

    /**
     * 获取前端显示的分类列表（包含知识数量统计）
     * 
     * @return 分类列表（包含统计信息）
     */
    @Override
    public List<Map<String, Object>> getCategoriesWithCount() {
        List<Map<String, Object>> result = new ArrayList<>();
        
        // 添加"全部知识"选项
        Map<String, Object> allCategory = new HashMap<>();
        allCategory.put("code", "all");
        allCategory.put("name", "全部知识");
        allCategory.put("count", knowledgeBaseMapper.countAll());
        allCategory.put("active", true);
        result.add(allCategory);
        
        // 获取所有正常状态的分类，排除code为"all"的分类以避免重复
        List<KnowledgeCategory> categories = knowledgeCategoryMapper.selectActiveCategories();
        for (KnowledgeCategory category : categories) {
            // 跳过code为"all"的分类，避免与手动添加的"全部知识"重复
            if ("all".equals(category.getCategoryCode())) {
                continue;
            }
            
            Map<String, Object> categoryMap = new HashMap<>();
            categoryMap.put("code", category.getCategoryCode());
            categoryMap.put("name", category.getCategoryName());
            categoryMap.put("count", knowledgeBaseMapper.countByCategoryId(category.getCategoryId()));
            categoryMap.put("active", false);
            result.add(categoryMap);
        }
        
        return result;
    }
}