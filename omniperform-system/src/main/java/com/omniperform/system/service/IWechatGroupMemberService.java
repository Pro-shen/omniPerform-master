package com.omniperform.system.service;

import java.util.List;
import com.omniperform.system.domain.WechatGroupMember;

/**
 * 微信群组成员 业务层
 * 
 * @author omniperform
 */
public interface IWechatGroupMemberService
{
    /**
     * 查询微信群组成员
     * 
     * @param membershipId 微信群组成员主键
     * @return 微信群组成员
     */
    public WechatGroupMember selectWechatGroupMemberByMembershipId(Long membershipId);

    /**
     * 根据群组ID和会员ID查询微信群组成员
     * 
     * @param groupId 群组ID
     * @param memberId 会员ID
     * @return 微信群组成员
     */
    public WechatGroupMember selectWechatGroupMemberByGroupIdAndMemberId(Long groupId, Long memberId);

    /**
     * 根据群组ID和OpenID查询微信群组成员
     * 
     * @param groupId 群组ID
     * @param openId OpenID
     * @return 微信群组成员
     */
    public WechatGroupMember selectWechatGroupMemberByGroupIdAndOpenId(Long groupId, String openId);

    /**
     * 查询微信群组成员列表
     * 
     * @param wechatGroupMember 微信群组成员
     * @return 微信群组成员集合
     */
    public List<WechatGroupMember> selectWechatGroupMemberList(WechatGroupMember wechatGroupMember);

    /**
     * 根据群组ID查询微信群组成员列表
     * 
     * @param groupId 群组ID
     * @return 微信群组成员集合
     */
    public List<WechatGroupMember> selectWechatGroupMemberListByGroupId(Long groupId);

    /**
     * 根据会员ID查询微信群组成员列表
     * 
     * @param memberId 会员ID
     * @return 微信群组成员集合
     */
    public List<WechatGroupMember> selectWechatGroupMemberListByMemberId(Long memberId);

    /**
     * 查询活跃微信群组成员列表
     * 
     * @param wechatGroupMember 微信群组成员
     * @return 微信群组成员集合
     */
    public List<WechatGroupMember> selectActiveWechatGroupMemberList(WechatGroupMember wechatGroupMember);

    /**
     * 根据成员角色查询微信群组成员列表
     * 
     * @param memberRole 成员角色
     * @return 微信群组成员集合
     */
    public List<WechatGroupMember> selectWechatGroupMemberListByRole(Integer memberRole);

    /**
     * 新增微信群组成员
     * 
     * @param wechatGroupMember 微信群组成员
     * @return 结果
     */
    public int insertWechatGroupMember(WechatGroupMember wechatGroupMember);

    /**
     * 修改微信群组成员
     * 
     * @param wechatGroupMember 微信群组成员
     * @return 结果
     */
    public int updateWechatGroupMember(WechatGroupMember wechatGroupMember);

    /**
     * 更新最后发言时间和发言次数
     * 
     * @param wechatGroupMember 微信群组成员
     * @return 结果
     */
    public int updateLastSpeakTimeAndCount(WechatGroupMember wechatGroupMember);

    /**
     * 批量删除微信群组成员
     * 
     * @param membershipIds 需要删除的微信群组成员主键集合
     * @return 结果
     */
    public int deleteWechatGroupMemberByMembershipIds(String membershipIds);

    /**
     * 删除微信群组成员信息
     * 
     * @param membershipId 微信群组成员主键
     * @return 结果
     */
    public int deleteWechatGroupMemberByMembershipId(Long membershipId);

    /**
     * 根据群组ID删除微信群组成员
     * 
     * @param groupId 群组ID
     * @return 结果
     */
    public int deleteWechatGroupMemberByGroupId(Long groupId);

    /**
     * 统计群组总成员数
     * 
     * @param groupId 群组ID
     * @return 数量
     */
    public int countTotalMembersByGroupId(Long groupId);

    /**
     * 统计群组活跃成员数
     * 
     * @param groupId 群组ID
     * @return 数量
     */
    public int countActiveMembersByGroupId(Long groupId);
}