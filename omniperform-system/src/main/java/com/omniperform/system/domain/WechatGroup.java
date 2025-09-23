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
 * 微信群组表 wechat_group
 * 
 * @author omniperform
 */
public class WechatGroup extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 群组ID */
    @Excel(name = "群组ID", cellType = ColumnType.NUMERIC)
    private Long groupId;

    /** 群组名称 */
    @Excel(name = "群组名称")
    private String groupName;



    /** 群组类型：normal-普通群，work-工作群，customer-客户群，vip-VIP群，product-产品群，activity-活动群 */
    @Excel(name = "群组类型", readConverterExp = "normal=普通群,work=工作群,customer=客户群,vip=VIP群,product=产品群,activity=活动群")
    private String groupType;

    /** 群组描述 */
    @Excel(name = "群组描述")
    private String description;

    /** 群主ID */
    @Excel(name = "群主ID", cellType = ColumnType.NUMERIC)
    private Long ownerId;



    /** 群组状态：1-活跃，2-非活跃，3-已解散 */
    @Excel(name = "群组状态", readConverterExp = "1=活跃,2=非活跃,3=已解散")
    private Integer status;

    /** 成员数量 */
    @Excel(name = "成员数量", cellType = ColumnType.NUMERIC)
    private Integer memberCount;

    /** 最大成员数 */
    @Excel(name = "最大成员数", cellType = ColumnType.NUMERIC)
    private Integer maxMembers;





    public Long getGroupId()
    {
        return groupId;
    }

    public void setGroupId(Long groupId)
    {
        this.groupId = groupId;
    }

    @NotBlank(message = "群组名称不能为空")
    @Size(min = 0, max = 100, message = "群组名称长度不能超过100个字符")
    public String getGroupName()
    {
        return groupName;
    }

    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }



    public String getGroupType()
    {
        return groupType;
    }

    public void setGroupType(String groupType)
    {
        this.groupType = groupType;
    }

    @Size(min = 0, max = 500, message = "群组描述长度不能超过500个字符")
    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Long getOwnerId()
    {
        return ownerId;
    }

    public void setOwnerId(Long ownerId)
    {
        this.ownerId = ownerId;
    }



    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Integer getMemberCount()
    {
        return memberCount;
    }

    public void setMemberCount(Integer memberCount)
    {
        this.memberCount = memberCount;
    }

    public Integer getMaxMembers()
    {
        return maxMembers;
    }

    public void setMaxMembers(Integer maxMembers)
    {
        this.maxMembers = maxMembers;
    }



    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("groupId", getGroupId())
            .append("groupName", getGroupName())
            .append("groupType", getGroupType())
            .append("description", getDescription())
            .append("ownerId", getOwnerId())
            .append("status", getStatus())
            .append("memberCount", getMemberCount())
            .append("maxMembers", getMaxMembers())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}