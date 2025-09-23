package com.omniperform.system.mapper;

import java.util.List;
import com.omniperform.system.domain.WechatGroupMember;

/**
 * 微信群组成员Mapper接口
 * 
 * @author omniperform
 */
public interface WechatGroupMemberMapper 
{
    /**
     * 查询微信群组成员
     * 
     * @param membershipId 微信群组成员主键
     * @return 微信群组成员
     */
    public WechatGroupMember selectWechatGroupMemberByMembershipId(Long membershipId);

    /**
     * 根据群组ID和会员ID查询群组成员
     * 
     * @param groupId 群组ID
     * @param memberId 会员ID
     * @return 微信群组成员
     */
    public WechatGroupMember selectWechatGroupMemberByGroupAndMember(Long groupId, Long memberId);

    /**
     * 根据群组ID和OpenID查询群组成员
     * 
     * @param groupId 群组ID
     * @param openId 微信OpenID
     * @return 微信群组成员
     */
    public WechatGroupMember selectWechatGroupMemberByGroupAndOpenId(Long groupId, String openId);

    /**
     * 查询微信群组成员列表
     * 
     * @param wechatGroupMember 微信群组成员
     * @return 微信群组成员集合
     */
    public List<WechatGroupMember> selectWechatGroupMemberList(WechatGroupMember wechatGroupMember);

    /**
     * 根据群组ID查询群组成员列表
     * 
     * @param groupId 群组ID
     * @return 微信群组成员集合
     */
    public List<WechatGroupMember> selectWechatGroupMemberListByGroupId(Long groupId);

    /**
     * 根据会员ID查询群组成员列表
     * 
     * @param memberId 会员ID
     * @return 微信群组成员集合
     */
    public List<WechatGroupMember> selectWechatGroupMemberListByMemberId(Long memberId);

    /**
     * 查询活跃群组成员列表
     * 
     * @param groupId 群组ID
     * @return 活跃微信群组成员集合
     */
    public List<WechatGroupMember> selectActiveWechatGroupMemberList(Long groupId);

    /**
     * 根据成员角色查询群组成员列表
     * 
     * @param groupId 群组ID
     * @param memberRole 成员角色
     * @return 微信群组成员集合
     */
    public List<WechatGroupMember> selectWechatGroupMemberListByRole(Long groupId, Integer memberRole);

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
     * 更新最后发言时间
     * 
     * @param wechatGroupMember 微信群组成员
     * @return 结果
     */
    public int updateLastSpeakTime(WechatGroupMember wechatGroupMember);

    /**
     * 更新发言次数
     * 
     * @param wechatGroupMember 微信群组成员
     * @return 结果
     */
    public int updateSpeakCount(WechatGroupMember wechatGroupMember);

    /**
     * 删除微信群组成员
     * 
     * @param membershipId 微信群组成员主键
     * @return 结果
     */
    public int deleteWechatGroupMemberByMembershipId(Long membershipId);

    /**
     * 批量删除微信群组成员
     * 
     * @param membershipIds 需要删除的微信群组成员主键集合
     * @return 结果
     */
    public int deleteWechatGroupMemberByMembershipIds(Long[] membershipIds);

    /**
     * 根据群组ID删除所有成员
     * 
     * @param groupId 群组ID
     * @return 结果
     */
    public int deleteWechatGroupMemberByGroupId(Long groupId);

    /**
     * 统计群组成员数量
     * 
     * @param groupId 群组ID
     * @return 成员数量
     */
    public int countWechatGroupMembers(Long groupId);

    /**
     * 统计活跃群组成员数量
     * 
     * @param groupId 群组ID
     * @return 活跃成员数量
     */
    public int countActiveWechatGroupMembers(Long groupId);
}