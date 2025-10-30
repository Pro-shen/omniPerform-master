package com.omniperform.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.omniperform.common.annotation.Excel;
import com.omniperform.common.core.domain.BaseEntity;

/**
 * 微信SOP方案详情对象 wechat_sop_details
 * 
 * @author omniperform
 */
public class WechatSopDetails extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 详情ID */
    private Long detailId;

    /** SOP类型 */
    @Excel(name = "SOP类型")
    private String sopType;

    /** 方案标题 */
    @Excel(name = "方案标题")
    private String title;

    /** 方案概述 */
    @Excel(name = "方案概述")
    private String overview;

    /** 关键要点(JSON数组) */
    @Excel(name = "关键要点")
    private String keyPoints;

    /** 详细内容 */
    @Excel(name = "详细内容")
    private String content;

    /** 状态(1:启用 0:禁用) */
    @Excel(name = "状态", readConverterExp = "1=启用,0=禁用")
    private Integer status;

    /** 创建者 */
    private String createBy;

    /** 更新者 */
    private String updateBy;

    public void setDetailId(Long detailId) 
    {
        this.detailId = detailId;
    }

    public Long getDetailId() 
    {
        return detailId;
    }
    public void setSopType(String sopType) 
    {
        this.sopType = sopType;
    }

    public String getSopType() 
    {
        return sopType;
    }
    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }
    public void setOverview(String overview) 
    {
        this.overview = overview;
    }

    public String getOverview() 
    {
        return overview;
    }
    public void setKeyPoints(String keyPoints) 
    {
        this.keyPoints = keyPoints;
    }

    public String getKeyPoints() 
    {
        return keyPoints;
    }
    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }
    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }
    public void setCreateBy(String createBy) 
    {
        this.createBy = createBy;
    }

    public String getCreateBy() 
    {
        return createBy;
    }
    public void setUpdateBy(String updateBy) 
    {
        this.updateBy = updateBy;
    }

    public String getUpdateBy() 
    {
        return updateBy;
    }

    @Override
    public String toString() {
        return "WechatSopDetails{" +
                "detailId=" + detailId +
                ", sopType='" + sopType + '\'' +
                ", title='" + title + '\'' +
                ", overview='" + overview + '\'' +
                ", keyPoints='" + keyPoints + '\'' +
                ", content='" + content + '\'' +
                ", status=" + status +
                ", createBy='" + createBy + '\'' +
                ", updateBy='" + updateBy + '\'' +
                '}';
    }
}