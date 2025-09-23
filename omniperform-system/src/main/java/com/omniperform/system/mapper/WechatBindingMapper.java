package com.omniperform.system.mapper;

import java.util.List;
import com.omniperform.system.domain.WechatBinding;

/**
 * 微信绑定Mapper接口
 * 
 * @author omniperform
 */
public interface WechatBindingMapper 
{
    /**
     * 查询微信绑定
     * 
     * @param bindingId 微信绑定主键
     * @return 微信绑定
     */
    public WechatBinding selectWechatBindingByBindingId(Long bindingId);

    /**
     * 根据会员ID查询微信绑定
     * 
     * @param memberId 会员ID
     * @return 微信绑定
     */
    public WechatBinding selectWechatBindingByMemberId(Long memberId);

    /**
     * 根据OpenID查询微信绑定
     * 
     * @param openId 微信OpenID
     * @return 微信绑定
     */
    public WechatBinding selectWechatBindingByOpenId(String openId);

    /**
     * 根据UnionID查询微信绑定
     * 
     * @param unionId 微信UnionID
     * @return 微信绑定
     */
    public WechatBinding selectWechatBindingByUnionId(String unionId);

    /**
     * 查询微信绑定列表
     * 
     * @param wechatBinding 微信绑定
     * @return 微信绑定集合
     */
    public List<WechatBinding> selectWechatBindingList(WechatBinding wechatBinding);

    /**
     * 根据导购ID查询微信绑定列表
     * 
     * @param guideId 导购ID
     * @return 微信绑定集合
     */
    public List<WechatBinding> selectWechatBindingListByGuideId(Long guideId);

    /**
     * 查询活跃微信绑定列表
     * 
     * @param wechatBinding 微信绑定
     * @return 活跃微信绑定集合
     */
    public List<WechatBinding> selectActiveWechatBindingList(WechatBinding wechatBinding);

    /**
     * 新增微信绑定
     * 
     * @param wechatBinding 微信绑定
     * @return 结果
     */
    public int insertWechatBinding(WechatBinding wechatBinding);

    /**
     * 修改微信绑定
     * 
     * @param wechatBinding 微信绑定
     * @return 结果
     */
    public int updateWechatBinding(WechatBinding wechatBinding);

    /**
     * 更新最后活跃时间
     * 
     * @param wechatBinding 微信绑定
     * @return 结果
     */
    public int updateLastActiveTime(WechatBinding wechatBinding);

    /**
     * 删除微信绑定
     * 
     * @param bindingId 微信绑定主键
     * @return 结果
     */
    public int deleteWechatBindingByBindingId(Long bindingId);

    /**
     * 批量删除微信绑定
     * 
     * @param bindingIds 需要删除的微信绑定主键集合
     * @return 结果
     */
    public int deleteWechatBindingByBindingIds(Long[] bindingIds);

    /**
     * 统计绑定用户数量
     * 
     * @param wechatBinding 查询条件
     * @return 绑定用户数量
     */
    public int countWechatBindings(WechatBinding wechatBinding);
}