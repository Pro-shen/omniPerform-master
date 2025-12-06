package com.omniperform.system.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.omniperform.system.mapper.MemberProfileAnalysisMapper;
import com.omniperform.system.domain.MemberProfileAnalysis;
import com.omniperform.system.service.IMemberProfileAnalysisService;
import com.omniperform.common.utils.DateUtils;

/**
 * 会员画像分析Service业务层处理
 * 
 * @author omniperform
 * @date 2025-01-09
 */
@Service
public class MemberProfileAnalysisServiceImpl implements IMemberProfileAnalysisService 
{
    @Autowired
    private MemberProfileAnalysisMapper memberProfileAnalysisMapper;

    /**
     * 查询会员画像分析
     * 
     * @param id 会员画像分析主键
     * @return 会员画像分析
     */
    @Override
    public MemberProfileAnalysis selectMemberProfileAnalysisById(Long id)
    {
        return memberProfileAnalysisMapper.selectMemberProfileAnalysisById(id);
    }

    /**
     * 查询会员画像分析列表
     * 
     * @param memberProfileAnalysis 会员画像分析
     * @return 会员画像分析
     */
    @Override
    public List<MemberProfileAnalysis> selectMemberProfileAnalysisList(MemberProfileAnalysis memberProfileAnalysis)
    {
        return memberProfileAnalysisMapper.selectMemberProfileAnalysisList(memberProfileAnalysis);
    }

    /**
     * 根据分析日期和画像类型查询
     * 
     * @param analysisDate 分析日期
     * @param profileType 画像类型
     * @param regionCode 区域代码
     * @return 会员画像分析集合
     */
    @Override
    public List<MemberProfileAnalysis> selectMemberProfileAnalysisByDateAndType(Date analysisDate, String profileType, String regionCode)
    {
        return memberProfileAnalysisMapper.selectMemberProfileAnalysisByDateAndType(analysisDate, profileType, regionCode);
    }

    /**
     * 查询最新的会员画像分析
     * 
     * @param profileType 画像类型
     * @param regionCode 区域代码
     * @return 会员画像分析集合
     */
    @Override
    public List<MemberProfileAnalysis> selectLatestMemberProfileAnalysis(String profileType, String regionCode)
    {
        return memberProfileAnalysisMapper.selectLatestMemberProfileAnalysis(profileType, regionCode);
    }

    /**
     * 查询指定日期范围内的会员画像分析
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param profileType 画像类型
     * @param regionCode 区域代码
     * @return 会员画像分析集合
     */
    @Override
    public List<MemberProfileAnalysis> selectMemberProfileAnalysisByDateRange(Date startDate, Date endDate, String profileType, String regionCode)
    {
        return memberProfileAnalysisMapper.selectMemberProfileAnalysisByDateRange(startDate, endDate, profileType, regionCode);
    }

    /**
     * 获取会员画像数据（前端接口）
     * 
     * @param profileType 画像类型
     * @param regionCode 区域代码
     * @return 会员画像数据
     */
    @Override
    public Map<String, Object> getMemberProfileData(String profileType, String regionCode)
    {
        Map<String, Object> result = new HashMap<>();
        
        // 获取最新的画像分析数据
        List<MemberProfileAnalysis> profileList = selectLatestMemberProfileAnalysis(profileType, regionCode);
        result.put("profileList", profileList);
        result.put("totalCount", profileList.size());
        
        // 统计各画像类型的会员数量
        List<Map<String, Object>> typeCount = countMembersByProfileType(DateUtils.getNowDate(), regionCode);
        result.put("typeCount", typeCount);
        
        // 统计各区域的会员画像分布
        List<Map<String, Object>> regionCount = countMembersByRegion(DateUtils.getNowDate(), profileType);
        result.put("regionCount", regionCount);
        
        return result;
    }

    /**
     * 获取会员画像数据（前端接口，支持月份参数）
     * 
     * @param profileType 画像类型
     * @param regionCode 区域代码
     * @param monthYear 月份（格式：YYYY-MM）
     * @return 会员画像数据
     */
    @Override
    public Map<String, Object> getMemberProfileData(String profileType, String regionCode, String monthYear)
    {
        Map<String, Object> result = new HashMap<>();
        
        if (monthYear != null && !monthYear.trim().isEmpty()) {
            // 根据指定月份查询画像分析数据
            List<MemberProfileAnalysis> profileList = memberProfileAnalysisMapper.selectMemberProfileAnalysisByMonth(monthYear, profileType, regionCode);
            result.put("profileList", profileList);
            result.put("totalCount", profileList.size());
            
            // 统计各画像类型的会员数量
            List<Map<String, Object>> typeCount = memberProfileAnalysisMapper.countMembersByProfileTypeByMonth(monthYear, regionCode);
            result.put("typeCount", typeCount);
            
            // 统计各区域的会员画像分布
            List<Map<String, Object>> regionCount = memberProfileAnalysisMapper.countMembersByRegionByMonth(monthYear, profileType);
            result.put("regionCount", regionCount);
        } else {
            // 如果没有指定月份，使用原有逻辑获取最新数据
            return getMemberProfileData(profileType, regionCode);
        }
        
        return result;
    }

    /**
     * 获取会员画像趋势数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param profileType 画像类型
     * @param regionCode 区域代码
     * @return 趋势数据
     */
    @Override
    public List<Map<String, Object>> getMemberProfileTrend(Date startDate, Date endDate, String profileType, String regionCode)
    {
        List<MemberProfileAnalysis> analysisList = selectMemberProfileAnalysisByDateRange(startDate, endDate, profileType, regionCode);
        List<Map<String, Object>> trendData = new java.util.ArrayList<>();
        
        for (MemberProfileAnalysis analysis : analysisList)
        {
            Map<String, Object> data = new HashMap<>();
            data.put("analysisDate", analysis.getAnalysisDate());
            data.put("profileType", analysis.getProfileType());
            data.put("memberCount", analysis.getMemberCount());
            data.put("percentage", analysis.getPercentage());
            data.put("avgPurchaseAmount", analysis.getAvgPurchaseAmount());
            data.put("avgInteractionFrequency", analysis.getAvgInteractionFrequency());
            trendData.add(data);
        }
        
        return trendData;
    }

    /**
     * 统计各画像类型的会员数量
     * 
     * @param analysisDate 分析日期
     * @param regionCode 区域代码
     * @return 统计结果
     */
    @Override
    public List<Map<String, Object>> countMembersByProfileType(Date analysisDate, String regionCode)
    {
        return memberProfileAnalysisMapper.countMembersByProfileType(analysisDate, regionCode);
    }

    /**
     * 统计各区域的会员画像分布
     * 
     * @param analysisDate 分析日期
     * @param profileType 画像类型
     * @return 统计结果
     */
    @Override
    public List<Map<String, Object>> countMembersByRegion(Date analysisDate, String profileType)
    {
        return memberProfileAnalysisMapper.countMembersByRegion(analysisDate, profileType);
    }

    /**
     * 新增会员画像分析
     * 
     * @param memberProfileAnalysis 会员画像分析
     * @return 结果
     */
    @Override
    public int insertMemberProfileAnalysis(MemberProfileAnalysis memberProfileAnalysis)
    {
        memberProfileAnalysis.setCreateTime(DateUtils.getNowDate());
        return memberProfileAnalysisMapper.insertMemberProfileAnalysis(memberProfileAnalysis);
    }

    /**
     * 修改会员画像分析
     * 
     * @param memberProfileAnalysis 会员画像分析
     * @return 结果
     */
    @Override
    public int updateMemberProfileAnalysis(MemberProfileAnalysis memberProfileAnalysis)
    {
        memberProfileAnalysis.setUpdateTime(DateUtils.getNowDate());
        return memberProfileAnalysisMapper.updateMemberProfileAnalysis(memberProfileAnalysis);
    }

    /**
     * 批量删除会员画像分析
     * 
     * @param ids 需要删除的会员画像分析主键
     * @return 结果
     */
    @Override
    public int deleteMemberProfileAnalysisByIds(Long[] ids)
    {
        return memberProfileAnalysisMapper.deleteMemberProfileAnalysisByIds(ids);
    }

    /**
     * 删除会员画像分析信息
     * 
     * @param id 会员画像分析主键
     * @return 结果
     */
    @Override
    public int deleteMemberProfileAnalysisById(Long id)
    {
        return memberProfileAnalysisMapper.deleteMemberProfileAnalysisById(id);
    }

    /**
     * 批量导入会员画像Excel数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importMemberProfileExcel(List<MemberProfileAnalysis> dataList,
                                                        boolean updateSupport,
                                                        String defaultRegion) {
        if (dataList == null) {
            dataList = java.util.Collections.emptyList();
        }

        int successCount = 0;
        int failCount = 0;
        List<String> errors = new ArrayList<>();

        for (int i = 0; i < dataList.size(); i++) {
            MemberProfileAnalysis item = dataList.get(i);
            try {
                // 默认区域填充
                if ((item.getRegionCode() == null || item.getRegionCode().trim().isEmpty())
                        && defaultRegion != null && !defaultRegion.trim().isEmpty()) {
                    item.setRegionCode(defaultRegion.trim());
                }

                // 基础校验
                if (item.getAnalysisDate() == null) {
                    throw new IllegalArgumentException("分析日期不能为空");
                }
                
                // 设置月份字段
                if (item.getAnalysisDate() != null) {
                    item.setMonthYear(DateUtils.parseDateToStr("yyyy-MM", item.getAnalysisDate()));
                }

                if (item.getProfileType() == null || item.getProfileType().trim().isEmpty()) {
                    throw new IllegalArgumentException("画像类型不能为空");
                }

                // 查找是否存在同日期/类型/区域记录
                List<MemberProfileAnalysis> existing = selectMemberProfileAnalysisByDateAndType(
                        item.getAnalysisDate(), item.getProfileType(), item.getRegionCode());

                if (existing != null && !existing.isEmpty()) {
                    // 始终执行覆盖操作（如果 updateSupport 为 true）
                    // 或者如果用户要求强制覆盖，这里可以忽略 updateSupport。但为了兼容性，我们假设 Controller 传来了 true。
                    if (updateSupport) {
                        // 1. 更新第一条记录
                        MemberProfileAnalysis toUpdate = existing.get(0);
                        toUpdate.setMemberCount(item.getMemberCount());
                        toUpdate.setPercentage(item.getPercentage());
                        toUpdate.setAvgPurchaseAmount(item.getAvgPurchaseAmount());
                        toUpdate.setAvgInteractionFrequency(item.getAvgInteractionFrequency());
                        toUpdate.setRegionCode(item.getRegionCode());
                        toUpdate.setMonthYear(item.getMonthYear()); // 更新月份
                        toUpdate.setUpdateTime(DateUtils.getNowDate());
                        int r = updateMemberProfileAnalysis(toUpdate);
                        if (r > 0) {
                            successCount++;
                        } else {
                            throw new RuntimeException("更新失败");
                        }
                        
                        // 2. 清理重复数据（如果有）
                        if (existing.size() > 1) {
                            for (int k = 1; k < existing.size(); k++) {
                                deleteMemberProfileAnalysisById(existing.get(k).getId());
                            }
                        }
                    } else {
                        failCount++;
                        errors.add("第 " + (i + 1) + " 行：已存在相同日期/类型/区域记录，未开启更新");
                    }
                } else {
                    item.setCreateTime(DateUtils.getNowDate());
                    int r = insertMemberProfileAnalysis(item);
                    if (r > 0) {
                        successCount++;
                    } else {
                        throw new RuntimeException("插入失败");
                    }
                }
            } catch (Exception ex) {
                failCount++;
                String msg = "第 " + (i + 1) + " 行：" + ex.getMessage();
                errors.add(msg);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("totalCount", successCount + failCount);
        result.put("errors", errors);
        return result;
    }
}