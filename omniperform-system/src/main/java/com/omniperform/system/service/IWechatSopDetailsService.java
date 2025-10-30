package com.omniperform.system.service;

import java.util.List;
import com.omniperform.system.domain.WechatSopDetails;

/**
 * SOP详情Service接口
 * 
 * @author omniperform
 */
public interface IWechatSopDetailsService 
{
    /**
     * 查询SOP详情
     * 
     * @param detailId SOP详情主键
     * @return SOP详情
     */
    public WechatSopDetails selectWechatSopDetailsByDetailId(Long detailId);

    /**
     * 查询SOP详情列表
     * 
     * @param wechatSopDetails SOP详情
     * @return SOP详情集合
     */
    public List<WechatSopDetails> selectWechatSopDetailsList(WechatSopDetails wechatSopDetails);

    /**
     * 根据SOP类型查询SOP详情
     * 
     * @param sopType SOP类型
     * @return SOP详情
     */
    public WechatSopDetails selectWechatSopDetailsBySopType(String sopType);

    /**
     * 新增SOP详情
     * 
     * @param wechatSopDetails SOP详情
     * @return 结果
     */
    public int insertWechatSopDetails(WechatSopDetails wechatSopDetails);

    /**
     * 修改SOP详情
     * 
     * @param wechatSopDetails SOP详情
     * @return 结果
     */
    public int updateWechatSopDetails(WechatSopDetails wechatSopDetails);

    /**
     * 批量删除SOP详情
     * 
     * @param detailIds 需要删除的SOP详情主键集合
     * @return 结果
     */
    public int deleteWechatSopDetailsByDetailIds(Long[] detailIds);

    /**
     * 删除SOP详情信息
     * 
     * @param detailId SOP详情主键
     * @return 结果
     */
    public int deleteWechatSopDetailsByDetailId(Long detailId);
}