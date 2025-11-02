package com.omniperform.web.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.omniperform.common.annotation.Excel;
import com.omniperform.common.core.domain.BaseEntity;

/**
 * 导购绩效对象 guide_performance
 * 
 * @author omniperform
 * @date 2025-01-09
 */
public class GuidePerformance extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 绩效记录ID */
    private Long performanceId;

    /** 导购ID */
    @Excel(name = "导购ID", type = Excel.Type.ALL)
    private String guideId;

    /** 数据月份(YYYY-MM) */
    @Excel(name = "数据月份", type = Excel.Type.ALL)
    private String dataMonth;

    /** 新增会员数 */
    @Excel(name = "新增会员数", type = Excel.Type.ALL)
    private Integer newMembers;

    /** 总服务会员数 */
    @Excel(name = "总服务会员数", type = Excel.Type.ALL)
    private Integer totalMembers;

    /** 活跃会员数 */
    @Excel(name = "活跃会员数", type = Excel.Type.ALL)
    private Integer activeMembers;

    /** 销售金额 */
    @Excel(name = "销售金额", type = Excel.Type.ALL)
    private BigDecimal salesAmount;

    /** 订单数量 */
    @Excel(name = "订单数量", type = Excel.Type.ALL)
    private Integer orderCount;

    /** MOT任务完成数 */
    @Excel(name = "MOT任务完成数", type = Excel.Type.ALL)
    private Integer motTasksCompleted;

    /** MOT完成率(%) */
    @Excel(name = "MOT完成率", type = Excel.Type.ALL)
    private BigDecimal motCompletionRate;

    /** 客户满意度(1-5分) */
    @Excel(name = "客户满意度", type = Excel.Type.ALL)
    private BigDecimal customerSatisfaction;

    /** 平均响应时间(小时) */
    @Excel(name = "平均响应时间", type = Excel.Type.ALL)
    private BigDecimal responseTime;

    /** 转化率(%) */
    @Excel(name = "转化率", type = Excel.Type.ALL)
    private BigDecimal conversionRate;

    /** 复购率(%) */
    @Excel(name = "复购率", type = Excel.Type.ALL)
    private BigDecimal repeatPurchaseRate;

    /** 会员扫码率(%) */
    @Excel(name = "会员扫码率", type = Excel.Type.ALL)
    private BigDecimal memberScanRate;

    /** 互动次数 */
    @Excel(name = "互动次数", type = Excel.Type.ALL)
    private Integer interactionCount;

    /** 综合绩效评分 */
    @Excel(name = "综合绩效评分", type = Excel.Type.ALL)
    private BigDecimal performanceScore;

    /** 区域内排名 */
    @Excel(name = "区域内排名", type = Excel.Type.ALL)
    private Integer rankInRegion;

    /** 全国排名 */
    @Excel(name = "全国排名", type = Excel.Type.ALL)
    private Integer rankOverall;

    /** CAI指数 */
    @Excel(name = "CAI指数", type = Excel.Type.ALL)
    private BigDecimal caiScore;

    /** RMV指数 */
    @Excel(name = "RMV指数", type = Excel.Type.ALL)
    private BigDecimal rmvScore;

    /** 九宫格位置 */
    @Excel(name = "九宫格位置", type = Excel.Type.ALL)
    private String matrixPosition;

    /** 九宫格类型 */
    @Excel(name = "九宫格类型", type = Excel.Type.ALL)
    private String matrixType;

    /** 趋势 */
    @Excel(name = "趋势", type = Excel.Type.ALL)
    private String trend;

    // 关联查询字段
    /** 导购姓名 */
    @Excel(name = "导购姓名", type = Excel.Type.ALL)
    private String guideName;

    /** 导购编号 */
    @Excel(name = "导购编码", type = Excel.Type.ALL)
    private String guideCode;

    /** 区域名称 */
    @Excel(name = "区域名称", type = Excel.Type.ALL)
    private String regionName;

    /** 门店名称 */
    @Excel(name = "门店名称", type = Excel.Type.ALL)
    private String storeName;

    public void setPerformanceId(Long performanceId) 
    {
        this.performanceId = performanceId;
    }

    public Long getPerformanceId() 
    {
        return performanceId;
    }
    public void setGuideId(String guideId) 
    {
        this.guideId = guideId;
    }

    public String getGuideId() 
    {
        return guideId;
    }
    public void setDataMonth(String dataMonth) 
    {
        this.dataMonth = dataMonth;
    }

    public String getDataMonth() 
    {
        return dataMonth;
    }
    public void setNewMembers(Integer newMembers) 
    {
        this.newMembers = newMembers;
    }

    public Integer getNewMembers() 
    {
        return newMembers;
    }
    public void setTotalMembers(Integer totalMembers) 
    {
        this.totalMembers = totalMembers;
    }

    public Integer getTotalMembers() 
    {
        return totalMembers;
    }
    public void setActiveMembers(Integer activeMembers) 
    {
        this.activeMembers = activeMembers;
    }

    public Integer getActiveMembers() 
    {
        return activeMembers;
    }
    public void setSalesAmount(BigDecimal salesAmount) 
    {
        this.salesAmount = salesAmount;
    }

    public BigDecimal getSalesAmount() 
    {
        return salesAmount;
    }
    public void setOrderCount(Integer orderCount) 
    {
        this.orderCount = orderCount;
    }

    public Integer getOrderCount() 
    {
        return orderCount;
    }
    public void setMotTasksCompleted(Integer motTasksCompleted) 
    {
        this.motTasksCompleted = motTasksCompleted;
    }

    public Integer getMotTasksCompleted() 
    {
        return motTasksCompleted;
    }
    public void setMotCompletionRate(BigDecimal motCompletionRate) 
    {
        this.motCompletionRate = motCompletionRate;
    }

    public BigDecimal getMotCompletionRate() 
    {
        return motCompletionRate;
    }
    public void setCustomerSatisfaction(BigDecimal customerSatisfaction) 
    {
        this.customerSatisfaction = customerSatisfaction;
    }

    public BigDecimal getCustomerSatisfaction() 
    {
        return customerSatisfaction;
    }
    public void setResponseTime(BigDecimal responseTime) 
    {
        this.responseTime = responseTime;
    }

    public BigDecimal getResponseTime() 
    {
        return responseTime;
    }
    public void setConversionRate(BigDecimal conversionRate) 
    {
        this.conversionRate = conversionRate;
    }

    public BigDecimal getConversionRate() 
    {
        return conversionRate;
    }
    public void setRepeatPurchaseRate(BigDecimal repeatPurchaseRate) 
    {
        this.repeatPurchaseRate = repeatPurchaseRate;
    }

    public BigDecimal getRepeatPurchaseRate() 
    {
        return repeatPurchaseRate;
    }
    public void setMemberScanRate(BigDecimal memberScanRate) 
    {
        this.memberScanRate = memberScanRate;
    }

    public BigDecimal getMemberScanRate() 
    {
        return memberScanRate;
    }
    public void setInteractionCount(Integer interactionCount) 
    {
        this.interactionCount = interactionCount;
    }

    public Integer getInteractionCount() 
    {
        return interactionCount;
    }
    public void setPerformanceScore(BigDecimal performanceScore) 
    {
        this.performanceScore = performanceScore;
    }

    public BigDecimal getPerformanceScore() 
    {
        return performanceScore;
    }
    public void setRankInRegion(Integer rankInRegion) 
    {
        this.rankInRegion = rankInRegion;
    }

    public Integer getRankInRegion() 
    {
        return rankInRegion;
    }
    public void setRankOverall(Integer rankOverall) 
    {
        this.rankOverall = rankOverall;
    }

    public Integer getRankOverall() 
    {
        return rankOverall;
    }

    public String getGuideName() {
        return guideName;
    }

    public void setGuideName(String guideName) {
        this.guideName = guideName;
    }

    public String getGuideCode() {
        return guideCode;
    }

    public void setGuideCode(String guideCode) {
        this.guideCode = guideCode;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public BigDecimal getCaiScore() {
        return caiScore;
    }

    public void setCaiScore(BigDecimal caiScore) {
        this.caiScore = caiScore;
    }

    public BigDecimal getRmvScore() {
        return rmvScore;
    }

    public void setRmvScore(BigDecimal rmvScore) {
        this.rmvScore = rmvScore;
    }

    public String getMatrixPosition() {
        return matrixPosition;
    }

    public void setMatrixPosition(String matrixPosition) {
        this.matrixPosition = matrixPosition;
    }

    public String getMatrixType() {
        return matrixType;
    }

    public void setMatrixType(String matrixType) {
        this.matrixType = matrixType;
    }

    public String getTrend() {
        return trend;
    }

    public void setTrend(String trend) {
        this.trend = trend;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("performanceId", getPerformanceId())
            .append("guideId", getGuideId())
            .append("dataMonth", getDataMonth())
            .append("newMembers", getNewMembers())
            .append("totalMembers", getTotalMembers())
            .append("activeMembers", getActiveMembers())
            .append("salesAmount", getSalesAmount())
            .append("orderCount", getOrderCount())
            .append("motTasksCompleted", getMotTasksCompleted())
            .append("motCompletionRate", getMotCompletionRate())
            .append("customerSatisfaction", getCustomerSatisfaction())
            .append("responseTime", getResponseTime())
            .append("conversionRate", getConversionRate())
            .append("repeatPurchaseRate", getRepeatPurchaseRate())
            .append("memberScanRate", getMemberScanRate())
            .append("interactionCount", getInteractionCount())
            .append("performanceScore", getPerformanceScore())
            .append("rankInRegion", getRankInRegion())
            .append("rankOverall", getRankOverall())
            .append("caiScore", getCaiScore())
            .append("rmvScore", getRmvScore())
            .append("matrixPosition", getMatrixPosition())
            .append("matrixType", getMatrixType())
            .append("trend", getTrend())
            .append("guideName", getGuideName())
            .append("guideCode", getGuideCode())
            .append("regionName", getRegionName())
            .append("storeName", getStoreName())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}