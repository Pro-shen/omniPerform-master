package com.omniperform.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import javax.validation.constraints.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.omniperform.common.annotation.Excel;
import com.omniperform.common.annotation.Excel.ColumnType;
import com.omniperform.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 会员基础信息表 member_info
 * 
 * @author omniperform
 */
public class MemberInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 会员ID */
    @Excel(name = "会员ID", cellType = ColumnType.NUMERIC)
    private Long memberId;

    /** 会员编号 */
    @Excel(name = "会员编号")
    private String memberCode;

    /** 会员姓名 */
    @Excel(name = "会员姓名")
    private String memberName;

    /** 手机号码 */
    @Excel(name = "手机号码")
    private String phone;

    /** 邮箱地址 */
    @Excel(name = "邮箱地址")
    private String email;

    /** 性别：1-男，2-女 */
    @Excel(name = "性别", readConverterExp = "1=男,2=女")
    private Integer gender;

    /** 出生日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出生日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date birthDate;

    /** 宝宝出生日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "宝宝出生日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date babyBirthDate;

    /** 宝宝阶段 */
    @Excel(name = "宝宝阶段")
    private String babyStage;

    /** 注册时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "注册时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date registrationDate;

    /** 注册来源 */
    @Excel(name = "注册来源")
    private String registrationSource;

    /** 专属导购ID */
    @Excel(name = "专属导购ID", cellType = ColumnType.NUMERIC)
    private Long guideId;

    /** 所属区域代码 */
    @Excel(name = "所属区域代码")
    private String regionCode;

    /** 所在城市 */
    @Excel(name = "所在城市")
    private String city;

    /** 状态：1-正常，2-冻结，3-注销 */
    @Excel(name = "状态", readConverterExp = "1=正常,2=冻结,3=注销")
    private Integer status;

    /** 最后登录时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "最后登录时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date lastLoginTime;

    /** 最后购买时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "最后购买时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date lastPurchaseTime;

    /** 最后互动时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "最后互动时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date lastInteractionTime;

    /** 累计购买金额 */
    @Excel(name = "累计购买金额", cellType = ColumnType.NUMERIC)
    private BigDecimal totalPurchaseAmount;

    /** 累计购买次数 */
    @Excel(name = "累计购买次数", cellType = ColumnType.NUMERIC)
    private Integer totalPurchaseCount;

    public Long getMemberId()
    {
        return memberId;
    }

    public void setMemberId(Long memberId)
    {
        this.memberId = memberId;
    }

    @NotBlank(message = "会员编号不能为空")
    @Size(min = 0, max = 50, message = "会员编号长度不能超过50个字符")
    public String getMemberCode()
    {
        return memberCode;
    }

    public void setMemberCode(String memberCode)
    {
        this.memberCode = memberCode;
    }

    @NotBlank(message = "会员姓名不能为空")
    @Size(min = 0, max = 100, message = "会员姓名长度不能超过100个字符")
    public String getMemberName()
    {
        return memberName;
    }

    public void setMemberName(String memberName)
    {
        this.memberName = memberName;
    }

    @Size(min = 0, max = 20, message = "手机号码长度不能超过20个字符")
    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    @Size(min = 0, max = 100, message = "邮箱地址长度不能超过100个字符")
    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public Integer getGender()
    {
        return gender;
    }

    public void setGender(Integer gender)
    {
        this.gender = gender;
    }

    public Date getBirthDate()
    {
        return birthDate;
    }

    public void setBirthDate(Date birthDate)
    {
        this.birthDate = birthDate;
    }

    public Date getBabyBirthDate()
    {
        return babyBirthDate;
    }

    public void setBabyBirthDate(Date babyBirthDate)
    {
        this.babyBirthDate = babyBirthDate;
    }

    @Size(min = 0, max = 20, message = "宝宝阶段长度不能超过20个字符")
    public String getBabyStage()
    {
        return babyStage;
    }

    public void setBabyStage(String babyStage)
    {
        this.babyStage = babyStage;
    }

    public Date getRegistrationDate()
    {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate)
    {
        this.registrationDate = registrationDate;
    }

    @Size(min = 0, max = 50, message = "注册来源长度不能超过50个字符")
    public String getRegistrationSource()
    {
        return registrationSource;
    }

    public void setRegistrationSource(String registrationSource)
    {
        this.registrationSource = registrationSource;
    }

    public Long getGuideId()
    {
        return guideId;
    }

    public void setGuideId(Long guideId)
    {
        this.guideId = guideId;
    }

    @Size(min = 0, max = 20, message = "所属区域代码长度不能超过20个字符")
    public String getRegionCode()
    {
        return regionCode;
    }

    public void setRegionCode(String regionCode)
    {
        this.regionCode = regionCode;
    }

    @Size(min = 0, max = 50, message = "所在城市长度不能超过50个字符")
    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Date getLastLoginTime()
    {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime)
    {
        this.lastLoginTime = lastLoginTime;
    }

    public Date getLastPurchaseTime()
    {
        return lastPurchaseTime;
    }

    public void setLastPurchaseTime(Date lastPurchaseTime)
    {
        this.lastPurchaseTime = lastPurchaseTime;
    }

    public Date getLastInteractionTime()
    {
        return lastInteractionTime;
    }

    public void setLastInteractionTime(Date lastInteractionTime)
    {
        this.lastInteractionTime = lastInteractionTime;
    }

    public BigDecimal getTotalPurchaseAmount()
    {
        return totalPurchaseAmount;
    }

    public void setTotalPurchaseAmount(BigDecimal totalPurchaseAmount)
    {
        this.totalPurchaseAmount = totalPurchaseAmount;
    }

    public Integer getTotalPurchaseCount()
    {
        return totalPurchaseCount;
    }

    public void setTotalPurchaseCount(Integer totalPurchaseCount)
    {
        this.totalPurchaseCount = totalPurchaseCount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("memberId", getMemberId())
            .append("memberCode", getMemberCode())
            .append("memberName", getMemberName())
            .append("phone", getPhone())
            .append("email", getEmail())
            .append("gender", getGender())
            .append("birthDate", getBirthDate())
            .append("babyBirthDate", getBabyBirthDate())
            .append("babyStage", getBabyStage())
            .append("registrationDate", getRegistrationDate())
            .append("registrationSource", getRegistrationSource())
            .append("guideId", getGuideId())
            .append("regionCode", getRegionCode())
            .append("city", getCity())
            .append("status", getStatus())
            .append("lastLoginTime", getLastLoginTime())
            .append("lastPurchaseTime", getLastPurchaseTime())
            .append("lastInteractionTime", getLastInteractionTime())
            .append("totalPurchaseAmount", getTotalPurchaseAmount())
            .append("totalPurchaseCount", getTotalPurchaseCount())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}