package com.omniperform.web.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.omniperform.common.annotation.Excel;
import com.omniperform.common.core.domain.BaseEntity;

/**
 * 导购基础信息对象 guide_info
 * 
 * @author omniperform
 * @date 2025-01-09
 */
public class GuideInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 导购ID */
    private Long guideId;

    /** 导购编号 */
    @Excel(name = "导购编号")
    private String guideCode;

    /** 导购姓名 */
    @Excel(name = "导购姓名")
    private String guideName;

    /** 手机号码 */
    @Excel(name = "手机号码")
    private String phone;

    /** 邮箱地址 */
    @Excel(name = "邮箱地址")
    private String email;

    /** 员工工号 */
    @Excel(name = "员工工号")
    private String employeeId;

    /** 所属部门 */
    @Excel(name = "所属部门")
    private String department;

    /** 所属区域代码 */
    @Excel(name = "所属区域代码")
    private String regionCode;

    /** 所属区域名称 */
    @Excel(name = "所属区域名称")
    private String regionName;

    /** 所属门店ID */
    @Excel(name = "所属门店ID")
    private Long storeId;

    /** 所属门店名称 */
    @Excel(name = "所属门店名称")
    private String storeName;

    /** 督导ID */
    @Excel(name = "督导ID")
    private Long supervisorId;

    /** 督导姓名 */
    @Excel(name = "督导姓名")
    private String supervisorName;

    /** 导购等级：Junior,Senior,Expert,Master */
    @Excel(name = "导购等级", readConverterExp = "Junior=初级,Senior=高级,Expert=专家,Master=大师")
    private String level;

    /** 入职日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "入职日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date hireDate;

    /** 状态：1-在职，2-离职，3-休假 */
    @Excel(name = "状态", readConverterExp = "1=在职,2=离职,3=休假")
    private Integer status;

    /** 月度销售目标 */
    @Excel(name = "月度销售目标")
    private BigDecimal monthlyTarget;

    /** 上级管理者ID */
    @Excel(name = "上级管理者ID")
    private Long managerId;

    /** 企业微信ID */
    @Excel(name = "企业微信ID")
    private String wechatId;

    public void setGuideId(Long guideId) 
    {
        this.guideId = guideId;
    }

    public Long getGuideId() 
    {
        return guideId;
    }
    public void setGuideCode(String guideCode) 
    {
        this.guideCode = guideCode;
    }

    public String getGuideCode() 
    {
        return guideCode;
    }
    public void setGuideName(String guideName) 
    {
        this.guideName = guideName;
    }

    public String getGuideName() 
    {
        return guideName;
    }
    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }
    public void setEmail(String email) 
    {
        this.email = email;
    }

    public String getEmail() 
    {
        return email;
    }
    public void setEmployeeId(String employeeId) 
    {
        this.employeeId = employeeId;
    }

    public String getEmployeeId() 
    {
        return employeeId;
    }
    public void setDepartment(String department) 
    {
        this.department = department;
    }

    public String getDepartment() 
    {
        return department;
    }
    public void setRegionCode(String regionCode) 
    {
        this.regionCode = regionCode;
    }

    public String getRegionCode() 
    {
        return regionCode;
    }
    public void setRegionName(String regionName) 
    {
        this.regionName = regionName;
    }

    public String getRegionName() 
    {
        return regionName;
    }
    public void setStoreId(Long storeId) 
    {
        this.storeId = storeId;
    }

    public Long getStoreId() 
    {
        return storeId;
    }
    public void setStoreName(String storeName) 
    {
        this.storeName = storeName;
    }

    public String getStoreName() 
    {
        return storeName;
    }
    public void setSupervisorId(Long supervisorId) 
    {
        this.supervisorId = supervisorId;
    }

    public Long getSupervisorId() 
    {
        return supervisorId;
    }
    public void setSupervisorName(String supervisorName) 
    {
        this.supervisorName = supervisorName;
    }

    public String getSupervisorName() 
    {
        return supervisorName;
    }
    public void setLevel(String level) 
    {
        this.level = level;
    }

    public String getLevel() 
    {
        return level;
    }
    public void setHireDate(Date hireDate) 
    {
        this.hireDate = hireDate;
    }

    public Date getHireDate() 
    {
        return hireDate;
    }
    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }
    public void setMonthlyTarget(BigDecimal monthlyTarget) 
    {
        this.monthlyTarget = monthlyTarget;
    }

    public BigDecimal getMonthlyTarget() 
    {
        return monthlyTarget;
    }
    public void setManagerId(Long managerId) 
    {
        this.managerId = managerId;
    }

    public Long getManagerId() 
    {
        return managerId;
    }
    public void setWechatId(String wechatId) 
    {
        this.wechatId = wechatId;
    }

    public String getWechatId() 
    {
        return wechatId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("guideId", getGuideId())
            .append("guideCode", getGuideCode())
            .append("guideName", getGuideName())
            .append("phone", getPhone())
            .append("email", getEmail())
            .append("employeeId", getEmployeeId())
            .append("department", getDepartment())
            .append("regionCode", getRegionCode())
            .append("regionName", getRegionName())
            .append("storeId", getStoreId())
            .append("storeName", getStoreName())
            .append("supervisorId", getSupervisorId())
            .append("supervisorName", getSupervisorName())
            .append("level", getLevel())
            .append("hireDate", getHireDate())
            .append("status", getStatus())
            .append("monthlyTarget", getMonthlyTarget())
            .append("managerId", getManagerId())
            .append("wechatId", getWechatId())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}