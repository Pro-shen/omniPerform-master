package com.omniperform.system.mapper;

import java.util.List;
import java.util.Map;
import com.omniperform.system.domain.MemberInfo;

/**
 * 会员信息Mapper接口
 * 
 * @author omniperform
 */
public interface MemberInfoMapper 
{
    /**
     * 查询会员信息
     * 
     * @param memberId 会员信息主键
     * @return 会员信息
     */
    public MemberInfo selectMemberInfoByMemberId(Long memberId);

    /**
     * 根据手机号查询会员信息
     * 
     * @param phone 手机号
     * @return 会员信息
     */
    public MemberInfo selectMemberInfoByPhone(String phone);

    /**
     * 查询会员信息列表
     * 
     * @param memberInfo 会员信息
     * @return 会员信息集合
     */
    public List<MemberInfo> selectMemberInfoList(MemberInfo memberInfo);

    /**
     * 新增会员信息
     * 
     * @param memberInfo 会员信息
     * @return 结果
     */
    public int insertMemberInfo(MemberInfo memberInfo);

    /**
     * 修改会员信息
     * 
     * @param memberInfo 会员信息
     * @return 结果
     */
    public int updateMemberInfo(MemberInfo memberInfo);

    /**
     * 删除会员信息
     * 
     * @param memberId 会员信息主键
     * @return 结果
     */
    public int deleteMemberInfoByMemberId(Long memberId);

    /**
     * 批量删除会员信息
     * 
     * @param memberIds 需要删除的会员信息主键集合
     * @return 结果
     */
    public int deleteMemberInfoByMemberIds(Long[] memberIds);

    /**
     * 计算会员CRFM-E评分
     * 
     * @param memberId 会员ID
     * @return CRFM-E评分数据
     */
    public Map<String, Object> calculateMemberCrfme(Long memberId);
}