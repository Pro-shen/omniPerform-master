package com.omniperform.web.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.omniperform.common.annotation.Excel;

import java.util.Date;

/**
 * 知识库Excel导入DTO（用于模板与解析映射）
 */
public class KnowledgeImportDTO {
    @Excel(name = "标题")
    private String title;

    @Excel(name = "摘要")
    private String summary;

    @Excel(name = "内容")
    private String content;

    @Excel(name = "标签", prompt = "多个标签用逗号分隔")
    private String tags;

    @Excel(name = "作者")
    private String author;

    @Excel(name = "来源")
    private String source;

    @Excel(name = "附件URL")
    private String attachmentUrl;

    @Excel(name = "分类代码", prompt = "如：product/sales/member/faq/competitor")
    private String categoryCode;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "发布时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;

    @Excel(name = "状态", prompt = "0正常,1停用")
    private String status;

    @Excel(name = "是否推荐", prompt = "0否,1是")
    private String isFeatured;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getAttachmentUrl() { return attachmentUrl; }
    public void setAttachmentUrl(String attachmentUrl) { this.attachmentUrl = attachmentUrl; }
    public String getCategoryCode() { return categoryCode; }
    public void setCategoryCode(String categoryCode) { this.categoryCode = categoryCode; }
    public Date getPublishTime() { return publishTime; }
    public void setPublishTime(Date publishTime) { this.publishTime = publishTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getIsFeatured() { return isFeatured; }
    public void setIsFeatured(String isFeatured) { this.isFeatured = isFeatured; }
}