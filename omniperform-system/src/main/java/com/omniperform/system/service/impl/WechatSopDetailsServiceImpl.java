package com.omniperform.system.service.impl;

import java.util.List;
import com.omniperform.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.omniperform.system.mapper.WechatSopDetailsMapper;
import com.omniperform.system.domain.WechatSopDetails;
import com.omniperform.system.service.IWechatSopDetailsService;

/**
 * SOP详情Service业务层处理
 * 
 * @author omniperform
 */
@Service
public class WechatSopDetailsServiceImpl implements IWechatSopDetailsService 
{
    @Autowired
    private WechatSopDetailsMapper wechatSopDetailsMapper;

    /**
     * 查询SOP详情
     * 
     * @param detailId SOP详情主键
     * @return SOP详情
     */
    @Override
    public WechatSopDetails selectWechatSopDetailsByDetailId(Long detailId)
    {
        return wechatSopDetailsMapper.selectWechatSopDetailsByDetailId(detailId);
    }

    /**
     * 查询SOP详情列表
     * 
     * @param wechatSopDetails SOP详情
     * @return SOP详情
     */
    @Override
    public List<WechatSopDetails> selectWechatSopDetailsList(WechatSopDetails wechatSopDetails)
    {
        return wechatSopDetailsMapper.selectWechatSopDetailsList(wechatSopDetails);
    }

    /**
     * 根据SOP类型查询SOP详情
     * 
     * @param sopType SOP类型
     * @return SOP详情
     */
    @Override
    public WechatSopDetails selectWechatSopDetailsBySopType(String sopType)
    {
        return wechatSopDetailsMapper.selectWechatSopDetailsBySopType(sopType);
    }

    /**
     * 新增SOP详情
     * 
     * @param wechatSopDetails SOP详情
     * @return 结果
     */
    @Override
    public int insertWechatSopDetails(WechatSopDetails wechatSopDetails)
    {
        wechatSopDetails.setCreateTime(DateUtils.getNowDate());
        return wechatSopDetailsMapper.insertWechatSopDetails(wechatSopDetails);
    }

    /**
     * 修改SOP详情
     * 
     * @param wechatSopDetails SOP详情
     * @return 结果
     */
    @Override
    public int updateWechatSopDetails(WechatSopDetails wechatSopDetails)
    {
        wechatSopDetails.setUpdateTime(DateUtils.getNowDate());
        return wechatSopDetailsMapper.updateWechatSopDetails(wechatSopDetails);
    }

    /**
     * 批量删除SOP详情
     * 
     * @param detailIds 需要删除的SOP详情主键
     * @return 结果
     */
    @Override
    public int deleteWechatSopDetailsByDetailIds(Long[] detailIds)
    {
        return wechatSopDetailsMapper.deleteWechatSopDetailsByDetailIds(detailIds);
    }

    /**
     * 删除SOP详情信息
     * 
     * @param detailId SOP详情主键
     * @return 结果
     */
    @Override
    public int deleteWechatSopDetailsByDetailId(Long detailId)
    {
        return wechatSopDetailsMapper.deleteWechatSopDetailsByDetailId(detailId);
    }
}