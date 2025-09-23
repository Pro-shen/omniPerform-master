package com.omniperform.system.domain;

import java.util.Date;
import javax.validation.constraints.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.omniperform.common.annotation.Excel;
import com.omniperform.common.annotation.Excel.ColumnType;
import com.omniperform.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 微信运营指标表 wechat_operation_metrics
 * 
 * @author omniperform
 */
public class WechatOperationMetrics extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 指标ID */
    @Excel(name = "指标ID", cellType = ColumnType.NUMERIC)
    private Long metricId;

    /** 统计日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "统计日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date statDate;

    /** 统计月份 */
    @Excel(name = "统计月份")
    private String statMonth;

    /** 用户ID */
    @Excel(name = "用户ID", cellType = ColumnType.NUMERIC)
    private Long userId;

    /** 用户姓名 */
    @Excel(name = "用户姓名")
    private String userName;

    /** 部门 */
    @Excel(name = "部门")
    private String department;

    /** 好友申请数 */
    @Excel(name = "好友申请数", cellType = ColumnType.NUMERIC)
    private Integer friendRequests;

    /** 好友通过数 */
    @Excel(name = "好友通过数", cellType = ColumnType.NUMERIC)
    private Integer friendAccepts;

    /** 好友总数 */
    @Excel(name = "好友总数", cellType = ColumnType.NUMERIC)
    private Integer friendTotal;

    /** 活跃好友数 */
    @Excel(name = "活跃好友数", cellType = ColumnType.NUMERIC)
    private Integer friendActive;

    /** 聊天会话数 */
    @Excel(name = "聊天会话数", cellType = ColumnType.NUMERIC)
    private Integer chatSessions;

    /** 聊天消息数 */
    @Excel(name = "聊天消息数", cellType = ColumnType.NUMERIC)
    private Integer chatMessages;

    /** 聊天回复数 */
    @Excel(name = "聊天回复数", cellType = ColumnType.NUMERIC)
    private Integer chatReplies;

    /** 聊天时长(分钟) */
    @Excel(name = "聊天时长", cellType = ColumnType.NUMERIC)
    private Integer chatDuration;

    /** 群聊消息数 */
    @Excel(name = "群聊消息数", cellType = ColumnType.NUMERIC)
    private Integer groupMessages;

    /** 群聊互动数 */
    @Excel(name = "群聊互动数", cellType = ColumnType.NUMERIC)
    private Integer groupInteractions;

    /** 朋友圈点赞数 */
    @Excel(name = "朋友圈点赞数", cellType = ColumnType.NUMERIC)
    private Integer momentsLikes;

    /** 朋友圈评论数 */
    @Excel(name = "朋友圈评论数", cellType = ColumnType.NUMERIC)
    private Integer momentsComments;

    /** 朋友圈分享数 */
    @Excel(name = "朋友圈分享数", cellType = ColumnType.NUMERIC)
    private Integer momentsShares;

    /** 活动推送数 */
    @Excel(name = "活动推送数", cellType = ColumnType.NUMERIC)
    private Integer activityPushes;

    /** 活动查看数 */
    @Excel(name = "活动查看数", cellType = ColumnType.NUMERIC)
    private Integer activityViews;

    /** 活动参与数 */
    @Excel(name = "活动参与数", cellType = ColumnType.NUMERIC)
    private Integer activityParticipations;

    /** 活动转化数 */
    @Excel(name = "活动转化数", cellType = ColumnType.NUMERIC)
    private Integer activityConversions;

    /** 数据查看数 */
    @Excel(name = "数据查看数", cellType = ColumnType.NUMERIC)
    private Integer dataReviews;

    /** 报告生成数 */
    @Excel(name = "报告生成数", cellType = ColumnType.NUMERIC)
    private Integer reportGenerates;

    /** 洞察分享数 */
    @Excel(name = "洞察分享数", cellType = ColumnType.NUMERIC)
    private Integer insightsShared;

    public Long getMetricId()
    {
        return metricId;
    }

    public void setMetricId(Long metricId)
    {
        this.metricId = metricId;
    }

    public Date getStatDate()
    {
        return statDate;
    }

    public void setStatDate(Date statDate)
    {
        this.statDate = statDate;
    }

    public String getStatMonth()
    {
        return statMonth;
    }

    public void setStatMonth(String statMonth)
    {
        this.statMonth = statMonth;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    @Size(min = 0, max = 50, message = "用户姓名长度不能超过50个字符")
    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    @Size(min = 0, max = 100, message = "部门长度不能超过100个字符")
    public String getDepartment()
    {
        return department;
    }

    public void setDepartment(String department)
    {
        this.department = department;
    }

    public Integer getFriendRequests()
    {
        return friendRequests;
    }

    public void setFriendRequests(Integer friendRequests)
    {
        this.friendRequests = friendRequests;
    }

    public Integer getFriendAccepts()
    {
        return friendAccepts;
    }

    public void setFriendAccepts(Integer friendAccepts)
    {
        this.friendAccepts = friendAccepts;
    }

    public Integer getFriendTotal()
    {
        return friendTotal;
    }

    public void setFriendTotal(Integer friendTotal)
    {
        this.friendTotal = friendTotal;
    }

    public Integer getFriendActive()
    {
        return friendActive;
    }

    public void setFriendActive(Integer friendActive)
    {
        this.friendActive = friendActive;
    }

    public Integer getChatSessions()
    {
        return chatSessions;
    }

    public void setChatSessions(Integer chatSessions)
    {
        this.chatSessions = chatSessions;
    }

    public Integer getChatMessages()
    {
        return chatMessages;
    }

    public void setChatMessages(Integer chatMessages)
    {
        this.chatMessages = chatMessages;
    }

    public Integer getChatReplies()
    {
        return chatReplies;
    }

    public void setChatReplies(Integer chatReplies)
    {
        this.chatReplies = chatReplies;
    }

    public Integer getChatDuration()
    {
        return chatDuration;
    }

    public void setChatDuration(Integer chatDuration)
    {
        this.chatDuration = chatDuration;
    }

    public Integer getGroupMessages()
    {
        return groupMessages;
    }

    public void setGroupMessages(Integer groupMessages)
    {
        this.groupMessages = groupMessages;
    }

    public Integer getGroupInteractions()
    {
        return groupInteractions;
    }

    public void setGroupInteractions(Integer groupInteractions)
    {
        this.groupInteractions = groupInteractions;
    }

    public Integer getMomentsLikes()
    {
        return momentsLikes;
    }

    public void setMomentsLikes(Integer momentsLikes)
    {
        this.momentsLikes = momentsLikes;
    }

    public Integer getMomentsComments()
    {
        return momentsComments;
    }

    public void setMomentsComments(Integer momentsComments)
    {
        this.momentsComments = momentsComments;
    }

    public Integer getMomentsShares()
    {
        return momentsShares;
    }

    public void setMomentsShares(Integer momentsShares)
    {
        this.momentsShares = momentsShares;
    }

    public Integer getActivityPushes()
    {
        return activityPushes;
    }

    public void setActivityPushes(Integer activityPushes)
    {
        this.activityPushes = activityPushes;
    }

    public Integer getActivityViews()
    {
        return activityViews;
    }

    public void setActivityViews(Integer activityViews)
    {
        this.activityViews = activityViews;
    }

    public Integer getActivityParticipations()
    {
        return activityParticipations;
    }

    public void setActivityParticipations(Integer activityParticipations)
    {
        this.activityParticipations = activityParticipations;
    }

    public Integer getActivityConversions()
    {
        return activityConversions;
    }

    public void setActivityConversions(Integer activityConversions)
    {
        this.activityConversions = activityConversions;
    }

    public Integer getDataReviews()
    {
        return dataReviews;
    }

    public void setDataReviews(Integer dataReviews)
    {
        this.dataReviews = dataReviews;
    }

    public Integer getReportGenerates()
    {
        return reportGenerates;
    }

    public void setReportGenerates(Integer reportGenerates)
    {
        this.reportGenerates = reportGenerates;
    }

    public Integer getInsightsShared()
    {
        return insightsShared;
    }

    public void setInsightsShared(Integer insightsShared)
    {
        this.insightsShared = insightsShared;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("metricId", getMetricId())
            .append("statDate", getStatDate())
            .append("statMonth", getStatMonth())
            .append("userId", getUserId())
            .append("userName", getUserName())
            .append("department", getDepartment())
            .append("friendRequests", getFriendRequests())
            .append("friendAccepts", getFriendAccepts())
            .append("friendTotal", getFriendTotal())
            .append("friendActive", getFriendActive())
            .append("chatSessions", getChatSessions())
            .append("chatMessages", getChatMessages())
            .append("chatReplies", getChatReplies())
            .append("chatDuration", getChatDuration())
            .append("groupMessages", getGroupMessages())
            .append("groupInteractions", getGroupInteractions())
            .append("momentsLikes", getMomentsLikes())
            .append("momentsComments", getMomentsComments())
            .append("momentsShares", getMomentsShares())
            .append("activityPushes", getActivityPushes())
            .append("activityViews", getActivityViews())
            .append("activityParticipations", getActivityParticipations())
            .append("activityConversions", getActivityConversions())
            .append("dataReviews", getDataReviews())
            .append("reportGenerates", getReportGenerates())
            .append("insightsShared", getInsightsShared())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}