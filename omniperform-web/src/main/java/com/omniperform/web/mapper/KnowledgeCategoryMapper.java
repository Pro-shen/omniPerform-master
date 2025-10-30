package com.omniperform.web.mapper;

import com.omniperform.web.domain.KnowledgeCategory;
import java.util.List;

/**
 * 知识库分类Mapper接口
 * 
 * @author omniperform
 * @date 2025-01-16
 */
public interface KnowledgeCategoryMapper {
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
     * 删除知识库分类
     * 
     * @param categoryId 知识库分类主键
     * @return 结果
     */
    public int deleteKnowledgeCategoryByCategoryId(Long categoryId);

    /**
     * 批量删除知识库分类
     * 
     * @param categoryIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteKnowledgeCategoryByCategoryIds(Long[] categoryIds);

    /**
     * 查询所有正常状态的分类（用于前端显示）
     * 
     * @return 分类列表
     */
    public List<KnowledgeCategory> selectActiveCategories();

    /**
     * 根据分类ID统计知识数量
     * 
     * @param categoryId 分类ID
     * @return 知识数量
     */
    public int countKnowledgeByCategoryId(Long categoryId);
}