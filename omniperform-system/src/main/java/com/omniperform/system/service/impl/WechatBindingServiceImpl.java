package com.omniperform.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.omniperform.common.core.text.Convert;
import com.omniperform.common.utils.StringUtils;
import com.omniperform.system.mapper.WechatBindingMapper;
import com.omniperform.system.domain.WechatBinding;
import com.omniperform.system.service.IWechatBindingService;

/**
 * 微信绑定Service业务层处理
 * 
 * @author omniperform
 */
@Service
public class WechatBindingServiceImpl implements IWechatBindingService 
{
    @Autowired
    private WechatBindingMapper wechatBindingMapper;

    /**
     * 查询微信绑定
     * 
     * @param bindingId 微信绑定主键
     * @return 微信绑定
     */
    @Override
    public WechatBinding selectWechatBindingByBindingId(Long bindingId)
    {
        return wechatBindingMapper.selectWechatBindingByBindingId(bindingId);
    }

    /**
     * 根据会员ID查询微信绑定
     * 
     * @param memberId 会员ID
     * @return 微信绑定
     */
    @Override
    public WechatBinding selectWechatBindingByMemberId(Long memberId)
    {
        return wechatBindingMapper.selectWechatBindingByMemberId(memberId);
    }

    /**
     * 根据OpenID查询微信绑定
     * 
     * @param openId OpenID
     * @return 微信绑定
     */
    @Override
    public WechatBinding selectWechatBindingByOpenId(String openId)
    {
        return wechatBindingMapper.selectWechatBindingByOpenId(openId);
    }

    /**
     * 根据UnionID查询微信绑定
     * 
     * @param unionId UnionID
     * @return 微信绑定
     */
    @Override
    public WechatBinding selectWechatBindingByUnionId(String unionId)
    {
        return wechatBindingMapper.selectWechatBindingByUnionId(unionId);
    }

    /**
     * 查询微信绑定列表
     * 
     * @param wechatBinding 微信绑定
     * @return 微信绑定
     */
    @Override
    public List<WechatBinding> selectWechatBindingList(WechatBinding wechatBinding)
    {
        return wechatBindingMapper.selectWechatBindingList(wechatBinding);
    }

    /**
     * 根据导购ID查询微信绑定列表
     * 
     * @param guideId 导购ID
     * @return 微信绑定集合
     */
    @Override
    public List<WechatBinding> selectWechatBindingListByGuideId(Long guideId)
    {
        return wechatBindingMapper.selectWechatBindingListByGuideId(guideId);
    }

    /**
     * 新增微信绑定
     * 
     * @param wechatBinding 微信绑定
     * @return 结果
     */
    @Override
    public int insertWechatBinding(WechatBinding wechatBinding)
    {
        return wechatBindingMapper.insertWechatBinding(wechatBinding);
    }

    /**
     * 修改微信绑定
     * 
     * @param wechatBinding 微信绑定
     * @return 结果
     */
    @Override
    public int updateWechatBinding(WechatBinding wechatBinding)
    {
        return wechatBindingMapper.updateWechatBinding(wechatBinding);
    }

    /**
     * 更新最后活跃时间
     * 
     * @param wechatBinding 微信绑定
     * @return 结果
     */
    @Override
    public int updateLastActiveTime(WechatBinding wechatBinding)
    {
        return wechatBindingMapper.updateLastActiveTime(wechatBinding);
    }

    /**
     * 批量删除微信绑定
     * 
     * @param bindingIds 需要删除的微信绑定主键
     * @return 结果
     */
    @Override
    public int deleteWechatBindingByBindingIds(String bindingIds)
    {
        return wechatBindingMapper.deleteWechatBindingByBindingIds(Convert.toLongArray(bindingIds));
    }

    /**
     * 删除微信绑定信息
     * 
     * @param bindingId 微信绑定主键
     * @return 结果
     */
    @Override
    public int deleteWechatBindingByBindingId(Long bindingId)
    {
        return wechatBindingMapper.deleteWechatBindingByBindingId(bindingId);
    }

    /**
     * 统计微信绑定数量
     * 
     * @param wechatBinding 微信绑定
     * @return 数量
     */
    @Override
    public int countWechatBindings(WechatBinding wechatBinding)
    {
        return wechatBindingMapper.countWechatBindings(wechatBinding);
    }

    /**
     * 检查OpenID是否唯一
     * 
     * @param wechatBinding 微信绑定信息
     * @return 结果
     */
    @Override
    public boolean checkOpenIdUnique(WechatBinding wechatBinding)
    {
        Long bindingId = StringUtils.isNull(wechatBinding.getBindingId()) ? -1L : wechatBinding.getBindingId();
        WechatBinding info = wechatBindingMapper.selectWechatBindingByOpenId(wechatBinding.getOpenId());
        if (StringUtils.isNotNull(info) && info.getBindingId().longValue() != bindingId.longValue())
        {
            return false;
        }
        return true;
    }

    /**
     * 检查UnionID是否唯一
     * 
     * @param wechatBinding 微信绑定信息
     * @return 结果
     */
    @Override
    public boolean checkUnionIdUnique(WechatBinding wechatBinding)
    {
        Long bindingId = StringUtils.isNull(wechatBinding.getBindingId()) ? -1L : wechatBinding.getBindingId();
        WechatBinding info = wechatBindingMapper.selectWechatBindingByUnionId(wechatBinding.getUnionId());
        if (StringUtils.isNotNull(info) && info.getBindingId().longValue() != bindingId.longValue())
        {
            return false;
        }
        return true;
    }
}