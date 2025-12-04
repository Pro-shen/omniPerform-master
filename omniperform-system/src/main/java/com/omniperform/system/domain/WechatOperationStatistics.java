package com.omniperform.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.omniperform.common.annotation.Excel;
import com.omniperform.common.core.domain.BaseEntity;

/**
 * 微信运营统计汇总对象 wechat_operation_statistics
 * 
 * @author omniperform
 */
public class WechatOperationStatistics extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 统计ID */
    private Long statId;

    /** 统计月份 */
    @Excel(name = "统计月份")
    private String statMonth;

    /** 总成员数 */
    @Excel(name = "总成员数")
    private Long totalMembers;

    /** 绑定成员数 */
    @Excel(name = "绑定成员数")
    private Long boundMembers;

    /** 群成员数 */
    @Excel(name = "群成员数")
    private Long groupMembers;

    /** 活跃群数 */
    @Excel(name = "活跃群数")
    private Long activeGroups;

    /** 总群数 */
    @Excel(name = "总群数")
    private Long totalGroups;

    /** 月度转化数 */
    @Excel(name = "月度转化数")
    private Long monthlyConversions;

    /** 平均响应时间 */
    @Excel(name = "平均响应时间")
    private String avgResponseTime;

    /** 满意度评分 */
    @Excel(name = "满意度评分")
    private BigDecimal satisfactionRate;

    /** 绑定率(%) */
    @Excel(name = "企业微信绑定率")
    private BigDecimal bindingRate;

    /** 转化率(%) */
    @Excel(name = "企微转化率")
    private BigDecimal conversionRate;

    /** 入群率(%) */
    @Excel(name = "会员入群率")
    private BigDecimal joinRate;

    /** 活跃度评分 */
    @Excel(name = "社群活跃度")
    private BigDecimal activityScore;

    public void setStatId(Long statId) 
    {
        this.statId = statId;
    }

    public Long getStatId() 
    {
        return statId;
    }
    public void setStatMonth(String statMonth) 
    {
        this.statMonth = statMonth;
    }

    public String getStatMonth() 
    {
        return statMonth;
    }
    public void setTotalMembers(Long totalMembers) 
    {
        this.totalMembers = totalMembers;
    }

    public Long getTotalMembers() 
    {
        return totalMembers;
    }
    public void setBoundMembers(Long boundMembers) 
    {
        this.boundMembers = boundMembers;
    }

    public Long getBoundMembers() 
    {
        return boundMembers;
    }
    public void setGroupMembers(Long groupMembers) 
    {
        this.groupMembers = groupMembers;
    }

    public Long getGroupMembers() 
    {
        return groupMembers;
    }
    public void setActiveGroups(Long activeGroups) 
    {
        this.activeGroups = activeGroups;
    }

    public Long getActiveGroups() 
    {
        return activeGroups;
    }
    public void setTotalGroups(Long totalGroups) 
    {
        this.totalGroups = totalGroups;
    }

    public Long getTotalGroups() 
    {
        return totalGroups;
    }
    public void setMonthlyConversions(Long monthlyConversions) 
    {
        this.monthlyConversions = monthlyConversions;
    }

    public Long getMonthlyConversions() 
    {
        return monthlyConversions;
    }
    public void setAvgResponseTime(String avgResponseTime) 
    {
        this.avgResponseTime = avgResponseTime;
    }

    public String getAvgResponseTime() 
    {
        return avgResponseTime;
    }
    public void setSatisfactionRate(BigDecimal satisfactionRate) 
    {
        this.satisfactionRate = satisfactionRate;
    }

    public BigDecimal getSatisfactionRate() 
    {
        return satisfactionRate;
    }
    public void setBindingRate(BigDecimal bindingRate) 
    {
        this.bindingRate = bindingRate;
    }

    public BigDecimal getBindingRate() 
    {
        return bindingRate;
    }
    public void setConversionRate(BigDecimal conversionRate) 
    {
        this.conversionRate = conversionRate;
    }

    public BigDecimal getConversionRate() 
    {
        return conversionRate;
    }

    public void setJoinRate(BigDecimal joinRate) 
    {
        this.joinRate = joinRate;
    }

    public BigDecimal getJoinRate() 
    {
        return joinRate;
    }

    public void setActivityScore(BigDecimal activityScore) 
    {
        this.activityScore = activityScore;
    }

    public BigDecimal getActivityScore() 
    {
        return activityScore;
    }

    @Override
    public String toString() {
        return "WechatOperationStatistics{" +
                "statId=" + statId +
                ", statMonth='" + statMonth + '\'' +
                ", totalMembers=" + totalMembers +
                ", boundMembers=" + boundMembers +
                ", groupMembers=" + groupMembers +
                ", activeGroups=" + activeGroups +
                ", totalGroups=" + totalGroups +
                ", monthlyConversions=" + monthlyConversions +
                ", avgResponseTime='" + avgResponseTime + '\'' +
                ", satisfactionRate=" + satisfactionRate +
                ", bindingRate=" + bindingRate +
                ", conversionRate=" + conversionRate +
                '}';
    }
}