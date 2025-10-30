package com.omniperform.web.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 知识库对象 knowledge_base
 * 
 * @author omniperform
 * @date 2025-01-16
 */
public class KnowledgeBase {
    private static final long serialVersionUID = 1L;

    /** 知识ID */
    private Long knowledgeId;

    /** 分类ID */
    private Long categoryId;

    /** 知识标题 */
    private String title;

    /** 知识摘要 */
    private String summary;

    /** 知识内容 */
    private String content;

    /** 标签（多个标签用逗号分隔） */
    private String tags;

    /** 作者 */
    private String author;

    /** 来源 */
    private String source;

    /** 附件URL */
    private String attachmentUrl;

    /** 浏览次数 */
    private Long views;

    /** 点赞次数 */
    private Long likes;

    /** 下载次数 */
    private Long downloads;

    /** 状态（0正常 1停用） */
    private String status;

    /** 是否推荐（0否 1是） */
    private String isFeatured;

    /** 发布时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 分类名称（关联查询字段） */
    private String categoryName;

    /** 分类代码（关联查询字段） */
    private String categoryCode;

    public void setKnowledgeId(Long knowledgeId) {
        this.knowledgeId = knowledgeId;
    }

    public Long getKnowledgeId() {
        return knowledgeId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSummary() {
        return summary;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getTags() {
        return tags;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    public Long getViews() {
        return views;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }

    public Long getLikes() {
        return likes;
    }

    public void setDownloads(Long downloads) {
        this.downloads = downloads;
    }

    public Long getDownloads() {
        return downloads;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setIsFeatured(String isFeatured) {
        this.isFeatured = isFeatured;
    }

    public String getIsFeatured() {
        return isFeatured;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("knowledgeId", getKnowledgeId())
            .append("categoryId", getCategoryId())
            .append("title", getTitle())
            .append("summary", getSummary())
            .append("content", getContent())
            .append("tags", getTags())
            .append("author", getAuthor())
            .append("source", getSource())
            .append("attachmentUrl", getAttachmentUrl())
            .append("views", getViews())
            .append("likes", getLikes())
            .append("downloads", getDownloads())
            .append("status", getStatus())
            .append("isFeatured", getIsFeatured())
            .append("publishTime", getPublishTime())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("categoryName", getCategoryName())
            .append("categoryCode", getCategoryCode())
            .toString();
    }
}