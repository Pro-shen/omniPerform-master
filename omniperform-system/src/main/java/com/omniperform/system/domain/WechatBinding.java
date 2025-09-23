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
 * 微信绑定表 wechat_binding
 * 
 * @author omniperform
 */
public class WechatBinding extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 绑定ID */
    @Excel(name = "绑定ID", cellType = ColumnType.NUMERIC)
    private Long bindingId;

    /** 会员ID */
    @Excel(name = "会员ID", cellType = ColumnType.NUMERIC)
    private Long memberId;

    /** 微信OpenID */
    @Excel(name = "微信OpenID")
    private String openId;

    /** 微信UnionID */
    @Excel(name = "微信UnionID")
    private String unionId;

    /** 微信昵称 */
    @Excel(name = "微信昵称")
    private String nickname;

    /** 微信头像URL */
    @Excel(name = "微信头像URL")
    private String avatarUrl;

    /** 绑定时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "绑定时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date bindingTime;

    /** 绑定状态：1-已绑定，2-已解绑 */
    @Excel(name = "绑定状态", readConverterExp = "1=已绑定,2=已解绑")
    private Integer bindingStatus;

    /** 最后活跃时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "最后活跃时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date lastActiveTime;

    /** 专属导购ID */
    @Excel(name = "专属导购ID", cellType = ColumnType.NUMERIC)
    private Long guideId;

    public Long getBindingId()
    {
        return bindingId;
    }

    public void setBindingId(Long bindingId)
    {
        this.bindingId = bindingId;
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

    @Size(min = 0, max = 100, message = "微信UnionID长度不能超过100个字符")
    public String getUnionId()
    {
        return unionId;
    }

    public void setUnionId(String unionId)
    {
        this.unionId = unionId;
    }

    @Size(min = 0, max = 100, message = "微信昵称长度不能超过100个字符")
    public String getNickname()
    {
        return nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    @Size(min = 0, max = 500, message = "微信头像URL长度不能超过500个字符")
    public String getAvatarUrl()
    {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl)
    {
        this.avatarUrl = avatarUrl;
    }

    public Date getBindingTime()
    {
        return bindingTime;
    }

    public void setBindingTime(Date bindingTime)
    {
        this.bindingTime = bindingTime;
    }

    public Integer getBindingStatus()
    {
        return bindingStatus;
    }

    public void setBindingStatus(Integer bindingStatus)
    {
        this.bindingStatus = bindingStatus;
    }

    public Date getLastActiveTime()
    {
        return lastActiveTime;
    }

    public void setLastActiveTime(Date lastActiveTime)
    {
        this.lastActiveTime = lastActiveTime;
    }

    public Long getGuideId()
    {
        return guideId;
    }

    public void setGuideId(Long guideId)
    {
        this.guideId = guideId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("bindingId", getBindingId())
            .append("memberId", getMemberId())
            .append("openId", getOpenId())
            .append("unionId", getUnionId())
            .append("nickname", getNickname())
            .append("avatarUrl", getAvatarUrl())
            .append("bindingTime", getBindingTime())
            .append("bindingStatus", getBindingStatus())
            .append("lastActiveTime", getLastActiveTime())
            .append("guideId", getGuideId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}