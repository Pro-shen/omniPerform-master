package com.omniperform.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.omniperform.common.core.text.Convert;
import com.omniperform.system.mapper.WechatGroupMemberMapper;
import com.omniperform.system.domain.WechatGroupMember;
import com.omniperform.system.service.IWechatGroupMemberService;

/**
 * 微信群组成员Service业务层处理
 * 
 * @author omniperform
 */
@Service
public class WechatGroupMemberServiceImpl implements IWechatGroupMemberService 
{
    @Autowired
    private WechatGroupMemberMapper wechatGroupMemberMapper;

    /**
     * 查询微信群组成员
     * 
     * @param membershipId 微信群组成员主键
     * @return 微信群组成员
     */
    @Override
    public WechatGroupMember selectWechatGroupMemberByMembershipId(Long membershipId)
    {
        return wechatGroupMemberMapper.selectWechatGroupMemberByMembershipId(membershipId);
    }

    /**
     * 根据群组ID和会员ID查询微信群组成员
     * 
     * @param groupId 群组ID
     * @param memberId 会员ID
     * @return 微信群组成员
     */
    @Override
    public WechatGroupMember selectWechatGroupMemberByGroupIdAndMemberId(Long groupId, Long memberId)
    {
        return wechatGroupMemberMapper.selectWechatGroupMemberByGroupAndMember(groupId, memberId);
    }

    /**
     * 根据群组ID和OpenID查询微信群组成员
     * 
     * @param groupId 群组ID
     * @param openId OpenID
     * @return 微信群组成员
     */
    @Override
    public WechatGroupMember selectWechatGroupMemberByGroupIdAndOpenId(Long groupId, String openId)
    {
        return wechatGroupMemberMapper.selectWechatGroupMemberByGroupAndOpenId(groupId, openId);
    }

    /**
     * 查询微信群组成员列表
     * 
     * @param wechatGroupMember 微信群组成员
     * @return 微信群组成员
     */
    @Override
    public List<WechatGroupMember> selectWechatGroupMemberList(WechatGroupMember wechatGroupMember)
    {
        return wechatGroupMemberMapper.selectWechatGroupMemberList(wechatGroupMember);
    }

    /**
     * 根据群组ID查询微信群组成员列表
     * 
     * @param groupId 群组ID
     * @return 微信群组成员集合
     */
    @Override
    public List<WechatGroupMember> selectWechatGroupMemberListByGroupId(Long groupId)
    {
        return wechatGroupMemberMapper.selectWechatGroupMemberListByGroupId(groupId);
    }

    /**
     * 根据会员ID查询微信群组成员列表
     * 
     * @param memberId 会员ID
     * @return 微信群组成员集合
     */
    @Override
    public List<WechatGroupMember> selectWechatGroupMemberListByMemberId(Long memberId)
    {
        return wechatGroupMemberMapper.selectWechatGroupMemberListByMemberId(memberId);
    }

    /**
     * 查询活跃微信群组成员列表
     * 
     * @param wechatGroupMember 微信群组成员
     * @return 微信群组成员集合
     */
    @Override
    public List<WechatGroupMember> selectActiveWechatGroupMemberList(WechatGroupMember wechatGroupMember)
    {
        // 从wechatGroupMember对象中获取groupId
        return wechatGroupMemberMapper.selectActiveWechatGroupMemberList(wechatGroupMember.getGroupId());
    }

    /**
     * 根据成员角色查询微信群组成员列表
     * 
     * @param memberRole 成员角色
     * @return 微信群组成员集合
     */
    @Override
    public List<WechatGroupMember> selectWechatGroupMemberListByRole(Integer memberRole)
    {
        // 这里需要传入groupId，但Service接口只有memberRole参数
        // 可以通过查询所有群组然后过滤，或者修改接口设计
        // 暂时返回空列表，需要后续优化
        return wechatGroupMemberMapper.selectWechatGroupMemberListByRole(null, memberRole);
    }

    /**
     * 新增微信群组成员
     * 
     * @param wechatGroupMember 微信群组成员
     * @return 结果
     */
    @Override
    public int insertWechatGroupMember(WechatGroupMember wechatGroupMember)
    {
        return wechatGroupMemberMapper.insertWechatGroupMember(wechatGroupMember);
    }

    /**
     * 修改微信群组成员
     * 
     * @param wechatGroupMember 微信群组成员
     * @return 结果
     */
    @Override
    public int updateWechatGroupMember(WechatGroupMember wechatGroupMember)
    {
        return wechatGroupMemberMapper.updateWechatGroupMember(wechatGroupMember);
    }

    /**
     * 更新最后发言时间和发言次数
     * 
     * @param wechatGroupMember 微信群组成员
     * @return 结果
     */
    @Override
    public int updateLastSpeakTimeAndCount(WechatGroupMember wechatGroupMember)
    {
        // Mapper中分别有updateLastSpeakTime和updateSpeakCount方法
        int result1 = wechatGroupMemberMapper.updateLastSpeakTime(wechatGroupMember);
        int result2 = wechatGroupMemberMapper.updateSpeakCount(wechatGroupMember);
        return result1 + result2;
    }

    /**
     * 批量删除微信群组成员
     * 
     * @param membershipIds 需要删除的微信群组成员主键
     * @return 结果
     */
    @Override
    public int deleteWechatGroupMemberByMembershipIds(String membershipIds)
    {
        return wechatGroupMemberMapper.deleteWechatGroupMemberByMembershipIds(Convert.toLongArray(membershipIds));
    }

    /**
     * 删除微信群组成员信息
     * 
     * @param membershipId 微信群组成员主键
     * @return 结果
     */
    @Override
    public int deleteWechatGroupMemberByMembershipId(Long membershipId)
    {
        return wechatGroupMemberMapper.deleteWechatGroupMemberByMembershipId(membershipId);
    }

    /**
     * 根据群组ID删除微信群组成员
     * 
     * @param groupId 群组ID
     * @return 结果
     */
    @Override
    public int deleteWechatGroupMemberByGroupId(Long groupId)
    {
        return wechatGroupMemberMapper.deleteWechatGroupMemberByGroupId(groupId);
    }

    /**
     * 统计群组总成员数量
     * 
     * @param groupId 群组ID
     * @return 成员数量
     */
    @Override
    public int countTotalMembersByGroupId(Long groupId)
    {
        return wechatGroupMemberMapper.countWechatGroupMembers(groupId);
    }

    /**
     * 统计群组活跃成员数量
     * 
     * @param groupId 群组ID
     * @return 活跃成员数量
     */
    @Override
    public int countActiveMembersByGroupId(Long groupId)
    {
        return wechatGroupMemberMapper.countActiveWechatGroupMembers(groupId);
    }
}