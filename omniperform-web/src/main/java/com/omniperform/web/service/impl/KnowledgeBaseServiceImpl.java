package com.omniperform.web.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.omniperform.web.domain.KnowledgeBase;
import com.omniperform.web.domain.KnowledgeCategory;
import com.omniperform.web.domain.KnowledgeImportDTO;
import com.omniperform.web.mapper.KnowledgeBaseMapper;
import com.omniperform.web.mapper.KnowledgeCategoryMapper;
import com.omniperform.web.service.IKnowledgeBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Objects;

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

    /**
     * 导入知识库Excel数据
     */
    @Override
    public Map<String, Object> importKnowledgeExcel(List<KnowledgeImportDTO> dataList, boolean updateSupport, String defaultCategory) {
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        int failCount = 0;
        List<String> errors = new ArrayList<>();

        String defaultCat = defaultCategory == null ? "" : defaultCategory.trim();

        if (dataList == null || dataList.isEmpty() || dataList.stream().allMatch(Objects::isNull)) {
            result.put("successCount", 0);
            result.put("failCount", 0);
            result.put("errors", errors);
            return result;
        }

        for (int i = 0; i < dataList.size(); i++) {
            KnowledgeImportDTO dto = dataList.get(i);
            try {
                if (dto == null) {
                    failCount++;
                    errors.add("第" + (i + 1) + "条记录为空或字段映射失败");
                    continue;
                }

                String title = dto.getTitle() == null ? "" : dto.getTitle().trim();
                if (title.isEmpty()) {
                    failCount++;
                    errors.add("第" + (i + 1) + "条记录标题为空");
                    continue;
                }

                String categoryCode = (dto.getCategoryCode() != null ? dto.getCategoryCode().trim() : "");
                if (categoryCode.isEmpty()) {
                    categoryCode = defaultCat;
                }
                if (categoryCode.isEmpty()) {
                    failCount++;
                    errors.add("第" + (i + 1) + "条记录分类代码为空，且未提供默认分类代码");
                    continue;
                }

                KnowledgeCategory category = knowledgeCategoryMapper.selectKnowledgeCategoryByCategoryCode(categoryCode);
                if (category == null) {
                    failCount++;
                    errors.add("第" + (i + 1) + "条记录分类代码不存在: " + categoryCode);
                    continue;
                }

                Long categoryId = category.getCategoryId();
                KnowledgeBase existing = knowledgeBaseMapper.selectByTitleAndCategoryId(title, categoryId);

                KnowledgeBase kb = new KnowledgeBase();
                kb.setCategoryId(categoryId);
                kb.setTitle(title);
                kb.setSummary(dto.getSummary());
                kb.setContent(dto.getContent());
                kb.setTags(dto.getTags());
                // 仅存储支持列：status、views、likes 等，其余列在当前XML未映射将被忽略
                String status = dto.getStatus();
                kb.setStatus((status == null || status.trim().isEmpty()) ? "0" : status.trim());

                if (existing != null) {
                    if (updateSupport) {
                        kb.setKnowledgeId(existing.getKnowledgeId());
                        knowledgeBaseMapper.updateKnowledgeBase(kb);
                        successCount++;
                    } else {
                        failCount++;
                        errors.add("第" + (i + 1) + "条记录已存在（同标题同分类），未更新");
                    }
                } else {
                    // 初始化浏览/点赞计数
                    kb.setViews(0L);
                    kb.setLikes(0L);
                    knowledgeBaseMapper.insertKnowledgeBase(kb);
                    successCount++;
                }
            } catch (Exception e) {
                failCount++;
                errors.add("第" + (i + 1) + "条处理失败：" + e.getMessage());
            }
        }

        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("errors", errors);
        return result;
    }
}
