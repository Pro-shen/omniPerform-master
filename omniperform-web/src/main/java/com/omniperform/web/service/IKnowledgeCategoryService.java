package com.omniperform.web.service;

import com.omniperform.web.domain.KnowledgeCategory;
import java.util.List;
import java.util.Map;

/**
 * 知识库分类Service接口
 * 
 * @author omniperform
 * @date 2025-01-16
 */
public interface IKnowledgeCategoryService {
    /**
     * 查询知识库分类
     * 
     * @param categoryId 知识库分类主键
     * @return 知识库分类
     */
    public KnowledgeCategory selectKnowledgeCategoryByCategoryId(Long categoryId);

    /**
     * 根据分类代码查询知识库分类
     * 
     * @param categoryCode 分类代码
     * @return 知识库分类
     */
    public KnowledgeCategory selectKnowledgeCategoryByCategoryCode(String categoryCode);

    /**
     * 查询知识库分类列表
     * 
     * @param knowledgeCategory 知识库分类
     * @return 知识库分类集合
     */
    public List<KnowledgeCategory> selectKnowledgeCategoryList(KnowledgeCategory knowledgeCategory);

    /**
     * 新增知识库分类
     * 
     * @param knowledgeCategory 知识库分类
     * @return 结果
     */
    public int insertKnowledgeCategory(KnowledgeCategory knowledgeCategory);

    /**
     * 修改知识库分类
     * 
     * @param knowledgeCategory 知识库分类
     * @return 结果
     */
    public int updateKnowledgeCategory(KnowledgeCategory knowledgeCategory);

    /**
     * 批量删除知识库分类
     * 
     * @param categoryIds 需要删除的知识库分类主键集合
     * @return 结果
     */
    public int deleteKnowledgeCategoryByCategoryIds(Long[] categoryIds);

    /**
     * 删除知识库分类信息
     * 
     * @param categoryId 知识库分类主键
     * @return 结果
     */
    public int deleteKnowledgeCategoryByCategoryId(Long categoryId);

    /**
     * 获取前端显示的分类列表（包含知识数量统计）
     * 
     * @return 分类列表（包含统计信息）
     */
    public List<Map<String, Object>> getCategoriesWithCount();
}