package com.omniperform.web.mapper;

import com.omniperform.web.domain.KnowledgeBase;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 知识库Mapper接口
 * 
 * @author omniperform
 * @date 2025-01-16
 */
public interface KnowledgeBaseMapper {
    /**
     * 查询知识库
     * 
     * @param knowledgeId 知识库主键
     * @return 知识库
     */
    public KnowledgeBase selectKnowledgeBaseByKnowledgeId(Long knowledgeId);

    /**
     * 查询知识库列表
     * 
     * @param knowledgeBase 知识库
     * @return 知识库集合
     */
    public List<KnowledgeBase> selectKnowledgeBaseList(KnowledgeBase knowledgeBase);

    /**
     * 根据条件查询知识库列表（支持搜索和分类筛选）
     * 
     * @param categoryId 分类ID
     * @param search 搜索关键词
     * @param sortBy 排序方式（1-浏览次数，2-点赞次数，其他-更新时间）
     * @return 知识库集合
     */
    public List<KnowledgeBase> selectKnowledgeBaseListWithConditions(
        @Param("categoryId") Long categoryId,
        @Param("search") String search,
        @Param("sortBy") String sortBy
    );

    /**
     * 新增知识库
     * 
     * @param knowledgeBase 知识库
     * @return 结果
     */
    public int insertKnowledgeBase(KnowledgeBase knowledgeBase);

    /**
     * 修改知识库
     * 
     * @param knowledgeBase 知识库
     * @return 结果
     */
    public int updateKnowledgeBase(KnowledgeBase knowledgeBase);

    /**
     * 删除知识库
     * 
     * @param knowledgeId 知识库主键
     * @return 结果
     */
    public int deleteKnowledgeBaseByKnowledgeId(Long knowledgeId);

    /**
     * 批量删除知识库
     * 
     * @param knowledgeIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteKnowledgeBaseByKnowledgeIds(Long[] knowledgeIds);

    /**
     * 增加浏览次数
     * 
     * @param knowledgeId 知识库主键
     * @return 结果
     */
    public int incrementViews(Long knowledgeId);

    /**
     * 增加点赞次数
     * 
     * @param knowledgeId 知识库主键
     * @return 结果
     */
    public int incrementLikes(Long knowledgeId);

    /**
     * 根据分类统计知识数量
     * 
     * @param categoryId 分类ID
     * @return 知识数量
     */
    public int countByCategoryId(Long categoryId);

    /**
     * 统计所有知识数量
     * 
     * @return 总数量
     */
    public int countAll();
}