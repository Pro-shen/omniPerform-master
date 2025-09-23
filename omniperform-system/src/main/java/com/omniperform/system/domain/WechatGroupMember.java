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
 * 微信群组成员表 wechat_group_member
 * 
 * @author omniperform
 */
public class WechatGroupMember extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 成员关系ID */
    @Excel(name = "成员关系ID", cellType = ColumnType.NUMERIC)
    private Long membershipId;

    /** 群组ID */
    @Excel(name = "群组ID", cellType = ColumnType.NUMERIC)
    private Long groupId;

    /** 会员ID */
    @Excel(name = "会员ID", cellType = ColumnType.NUMERIC)
    private Long memberId;

    /** 微信OpenID */
    @Excel(name = "微信OpenID")
    private String openId;

    /** 群内昵称 */
    @Excel(name = "群内昵称")
    private String groupNickname;

    /** 加入时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "加入时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date joinTime;

    /** 成员角色：1-普通成员，2-管理员，3-群主 */
    @Excel(name = "成员角色", readConverterExp = "1=普通成员,2=管理员,3=群主")
    private Integer memberRole;

    /** 成员状态：1-正常，2-已退群，3-被踢出 */
    @Excel(name = "成员状态", readConverterExp = "1=正常,2=已退群,3=被踢出")
    private Integer memberStatus;

    /** 最后发言时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "最后发言时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date lastSpeakTime;

    /** 发言次数 */
    @Excel(name = "发言次数", cellType = ColumnType.NUMERIC)
    private Integer speakCount;

    /** 退群时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "退群时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date leaveTime;

    public Long getMembershipId()
    {
        return membershipId;
    }

    public void setMembershipId(Long membershipId)
    {
        this.membershipId = membershipId;
    }

    public Long getGroupId()
    {
        return groupId;
    }

    public void setGroupId(Long groupId)
    {
        this.groupId = groupId;
    }

    public Long getMemberId()
    {
        return memberId;
    }

    public void setMemberId(Long memberId)
    {
        this.memberId = memberId;
    }

    @NotBlank(message = "微信OpenID不能为空")
    @Size(min = 0, max = 100, message = "微信OpenID长度不能超过100个字符")
    public String getOpenId()
    {
        return openId;
    }

    public void setOpenId(String openId)
    {
        this.openId = openId;
    }

    @Size(min = 0, max = 100, message = "群内昵称长度不能超过100个字符")
    public String getGroupNickname()
    {
        return groupNickname;
    }

    public void setGroupNickname(String groupNickname)
    {
        this.groupNickname = groupNickname;
    }

    public Date getJoinTime()
    {
        return joinTime;
    }

    public void setJoinTime(Date joinTime)
    {
        this.joinTime = joinTime;
    }

    public Integer getMemberRole()
    {
        return memberRole;
    }

    public void setMemberRole(Integer memberRole)
    {
        this.memberRole = memberRole;
    }

    public Integer getMemberStatus()
    {
        return memberStatus;
    }

    public void setMemberStatus(Integer memberStatus)
    {
        this.memberStatus = memberStatus;
    }

    public Date getLastSpeakTime()
    {
        return lastSpeakTime;
    }

    public void setLastSpeakTime(Date lastSpeakTime)
    {
        this.lastSpeakTime = lastSpeakTime;
    }

    public Integer getSpeakCount()
    {
        return speakCount;
    }

    public void setSpeakCount(Integer speakCount)
    {
        this.speakCount = speakCount;
    }

    public Date getLeaveTime()
    {
        return leaveTime;
    }

    public void setLeaveTime(Date leaveTime)
    {
        this.leaveTime = leaveTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("membershipId", getMembershipId())
            .append("groupId", getGroupId())
            .append("memberId", getMemberId())
            .append("openId", getOpenId())
            .append("groupNickname", getGroupNickname())
            .append("joinTime", getJoinTime())
            .append("memberRole", getMemberRole())
            .append("memberStatus", getMemberStatus())
            .append("lastSpeakTime", getLastSpeakTime())
            .append("speakCount", getSpeakCount())
            .append("leaveTime", getLeaveTime())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}