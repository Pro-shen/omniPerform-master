package com.omniperform.web.service;

import com.omniperform.web.domain.KnowledgeBase;
import java.util.List;
import java.util.Map;

/**
 * 知识库Service接口
 * 
 * @author omniperform
 * @date 2025-01-16
 */
public interface IKnowledgeBaseService {
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
     * 批量删除知识库
     * 
     * @param knowledgeIds 需要删除的知识库主键集合
     * @return 结果
     */
    public int deleteKnowledgeBaseByKnowledgeIds(Long[] knowledgeIds);

    /**
     * 删除知识库信息
     * 
     * @param knowledgeId 知识库主键
     * @return 结果
     */
    public int deleteKnowledgeBaseByKnowledgeId(Long knowledgeId);

    /**
     * 根据条件查询知识库列表（支持分页、搜索、排序）
     * 
     * @param page 页码
     * @param size 每页大小
     * @param categoryCode 分类代码
     * @param search 搜索关键词
     * @param sortBy 排序方式
     * @return 分页结果
     */
    public Map<String, Object> getKnowledgeListWithConditions(int page, int size, String categoryCode, String search, String sortBy);

    /**
     * 获取知识详情（同时增加浏览次数）
     * 
     * @param knowledgeId 知识ID
     * @return 知识详情
     */
    public KnowledgeBase getKnowledgeDetail(Long knowledgeId);

    /**
     * 增加点赞次数
     * 
     * @param knowledgeId 知识ID
     * @return 结果
     */
    public int incrementLikes(Long knowledgeId);
}