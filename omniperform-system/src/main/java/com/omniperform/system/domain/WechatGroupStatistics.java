package com.omniperform.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.omniperform.common.annotation.Excel;
import com.omniperform.common.core.domain.BaseEntity;

/**
 * 微信群组统计对象 wechat_group_statistics
 * 
 * @author omniperform
 */
public class WechatGroupStatistics extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 统计ID */
    private Long statId;

    /** 群组ID */
    @Excel(name = "群组ID")
    private Long groupId;

    /** 统计月份 */
    @Excel(name = "统计月份")
    private String statMonth;

    /** 活跃度评分 */
    @Excel(name = "活跃度评分")
    private BigDecimal activityScore;

    /** 入群率(%) */
    @Excel(name = "入群率(%)")
    private BigDecimal joinRate;

    /** 互动次数 */
    @Excel(name = "互动次数")
    private Long interactionCount;

    /** 消息数量 */
    @Excel(name = "消息数量")
    private Long messageCount;

    /** 活跃成员数 */
    @Excel(name = "活跃成员数")
    private Long activeMemberCount;

    /** 群组名称 */
    @Excel(name = "社群名称")
    private String groupName;

    /** 群组成员总数 */
    @Excel(name = "人数")
    private Long memberCount;

    public void setStatId(Long statId) 
    {
        this.statId = statId;
    }

    public Long getStatId() 
    {
        return statId;
    }
    public void setGroupId(Long groupId) 
    {
        this.groupId = groupId;
    }

    public Long getGroupId() 
    {
        return groupId;
    }
    public void setStatMonth(String statMonth) 
    {
        this.statMonth = statMonth;
    }

    public String getStatMonth() 
    {
        return statMonth;
    }
    public void setActivityScore(BigDecimal activityScore) 
    {
        this.activityScore = activityScore;
    }

    public BigDecimal getActivityScore() 
    {
        return activityScore;
    }
    public void setJoinRate(BigDecimal joinRate) 
    {
        this.joinRate = joinRate;
    }

    public BigDecimal getJoinRate() 
    {
        return joinRate;
    }
    public void setInteractionCount(Long interactionCount) 
    {
        this.interactionCount = interactionCount;
    }

    public Long getInteractionCount() 
    {
        return interactionCount;
    }
    public void setMessageCount(Long messageCount) 
    {
        this.messageCount = messageCount;
    }

    public Long getMessageCount() 
    {
        return messageCount;
    }
    public void setActiveMemberCount(Long activeMemberCount) 
    {
        this.activeMemberCount = activeMemberCount;
    }

    public Long getActiveMemberCount() 
    {
        return activeMemberCount;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Long getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(Long memberCount) {
        this.memberCount = memberCount;
    }

    @Override
    public String toString() {
        return "WechatGroupStatistics{" +
                "statId=" + statId +
                ", groupId=" + groupId +
                ", statMonth='" + statMonth + '\'' +
                ", activityScore=" + activityScore +
                ", joinRate=" + joinRate +
                ", interactionCount=" + interactionCount +
                ", messageCount=" + messageCount +
                ", activeMemberCount=" + activeMemberCount +
                ", groupName='" + groupName + '\'' +
                ", memberCount=" + memberCount +
                '}';
    }
}