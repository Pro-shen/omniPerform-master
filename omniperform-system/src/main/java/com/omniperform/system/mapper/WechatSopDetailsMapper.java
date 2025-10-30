package com.omniperform.system.mapper;

import java.util.List;
import com.omniperform.system.domain.WechatSopDetails;
import org.apache.ibatis.annotations.Param;

/**
 * 微信SOP方案详情Mapper接口
 * 
 * @author omniperform
 */
public interface WechatSopDetailsMapper 
{
    /**
     * 查询微信SOP方案详情
     * 
     * @param detailId 微信SOP方案详情主键
     * @return 微信SOP方案详情
     */
    public WechatSopDetails selectWechatSopDetailsByDetailId(Long detailId);

    /**
     * 查询微信SOP方案详情列表
     * 
     * @param wechatSopDetails 微信SOP方案详情
     * @return 微信SOP方案详情集合
     */
    public List<WechatSopDetails> selectWechatSopDetailsList(WechatSopDetails wechatSopDetails);

    /**
     * 根据SOP类型查询方案详情
     * 
     * @param sopType SOP类型
     * @return 微信SOP方案详情
     */
    public WechatSopDetails selectWechatSopDetailsBySopType(@Param("sopType") String sopType);

    /**
     * 新增微信SOP方案详情
     * 
     * @param wechatSopDetails 微信SOP方案详情
     * @return 结果
     */
    public int insertWechatSopDetails(WechatSopDetails wechatSopDetails);

    /**
     * 修改微信SOP方案详情
     * 
     * @param wechatSopDetails 微信SOP方案详情
     * @return 结果
     */
    public int updateWechatSopDetails(WechatSopDetails wechatSopDetails);

    /**
     * 删除微信SOP方案详情
     * 
     * @param detailId 微信SOP方案详情主键
     * @return 结果
     */
    public int deleteWechatSopDetailsByDetailId(Long detailId);

    /**
     * 批量删除微信SOP方案详情
     * 
     * @param detailIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWechatSopDetailsByDetailIds(Long[] detailIds);
}