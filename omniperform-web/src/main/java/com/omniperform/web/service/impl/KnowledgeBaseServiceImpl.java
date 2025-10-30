package com.omniperform.web.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.omniperform.web.domain.KnowledgeBase;
import com.omniperform.web.domain.KnowledgeCategory;
import com.omniperform.web.mapper.KnowledgeBaseMapper;
import com.omniperform.web.mapper.KnowledgeCategoryMapper;
import com.omniperform.web.service.IKnowledgeBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 知识库Service业务层处理
 * 
 * @author omniperform
 * @date 2025-01-16
 */
@Service
public class KnowledgeBaseServiceImpl implements IKnowledgeBaseService {
    @Autowired
    private KnowledgeBaseMapper knowledgeBaseMapper;

    @Autowired
    private KnowledgeCategoryMapper knowledgeCategoryMapper;

    /**
     * 查询知识库
     * 
     * @param knowledgeId 知识库主键
     * @return 知识库
     */
    @Override
    public KnowledgeBase selectKnowledgeBaseByKnowledgeId(Long knowledgeId) {
        return knowledgeBaseMapper.selectKnowledgeBaseByKnowledgeId(knowledgeId);
    }

    /**
     * 查询知识库列表
     * 
     * @param knowledgeBase 知识库
     * @return 知识库
     */
    @Override
    public List<KnowledgeBase> selectKnowledgeBaseList(KnowledgeBase knowledgeBase) {
        return knowledgeBaseMapper.selectKnowledgeBaseList(knowledgeBase);
    }

    /**
     * 新增知识库
     * 
     * @param knowledgeBase 知识库
     * @return 结果
     */
    @Override
    public int insertKnowledgeBase(KnowledgeBase knowledgeBase) {
        return knowledgeBaseMapper.insertKnowledgeBase(knowledgeBase);
    }

    /**
     * 修改知识库
     * 
     * @param knowledgeBase 知识库
     * @return 结果
     */
    @Override
    public int updateKnowledgeBase(KnowledgeBase knowledgeBase) {
        return knowledgeBaseMapper.updateKnowledgeBase(knowledgeBase);
    }

    /**
     * 批量删除知识库
     * 
     * @param knowledgeIds 需要删除的知识库主键
     * @return 结果
     */
    @Override
    public int deleteKnowledgeBaseByKnowledgeIds(Long[] knowledgeIds) {
        return knowledgeBaseMapper.deleteKnowledgeBaseByKnowledgeIds(knowledgeIds);
    }

    /**
     * 删除知识库信息
     * 
     * @param knowledgeId 知识库主键
     * @return 结果
     */
    @Override
    public int deleteKnowledgeBaseByKnowledgeId(Long knowledgeId) {
        return knowledgeBaseMapper.deleteKnowledgeBaseByKnowledgeId(knowledgeId);
    }

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
    @Override
    public Map<String, Object> getKnowledgeListWithConditions(int page, int size, String categoryCode, String search, String sortBy) {
        // 根据分类代码获取分类ID
        Long categoryId = null;
        if (categoryCode != null && !"all".equals(categoryCode)) {
            KnowledgeCategory category = knowledgeCategoryMapper.selectKnowledgeCategoryByCategoryCode(categoryCode);
            if (category != null) {
                categoryId = category.getCategoryId();
            }
        }

        // 设置分页
        PageHelper.startPage(page, size);
        
        // 查询数据
        List<KnowledgeBase> list = knowledgeBaseMapper.selectKnowledgeBaseListWithConditions(categoryId, search, sortBy);
        
        // 获取分页信息
        PageInfo<KnowledgeBase> pageInfo = new PageInfo<>(list);
        
        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", pageInfo.getTotal());
        result.put("page", page);
        result.put("size", size);
        result.put("totalPages", pageInfo.getPages());
        
        return result;
    }

    /**
     * 获取知识详情（同时增加浏览次数）
     * 
     * @param knowledgeId 知识ID
     * @return 知识详情
     */
    @Override
    public KnowledgeBase getKnowledgeDetail(Long knowledgeId) {
        // 增加浏览次数
        knowledgeBaseMapper.incrementViews(knowledgeId);
        
        // 返回详情
        return knowledgeBaseMapper.selectKnowledgeBaseByKnowledgeId(knowledgeId);
    }

    /**
     * 增加点赞次数
     * 
     * @param knowledgeId 知识ID
     * @return 结果
     */
    @Override
    public int incrementLikes(Long knowledgeId) {
        return knowledgeBaseMapper.incrementLikes(knowledgeId);
    }
}