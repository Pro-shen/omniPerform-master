package com.omniperform.system.service;

import java.util.List;
import com.omniperform.system.domain.WechatGroup;

/**
 * 微信群组 业务层
 * 
 * @author omniperform
 */
public interface IWechatGroupService
{
    /**
     * 查询微信群组
     * 
     * @param groupId 微信群组主键
     * @return 微信群组
     */
    public WechatGroup selectWechatGroupByGroupId(Long groupId);

    /**
     * 根据微信群组ID查询微信群组
     * 
     * @param wechatGroupId 微信群组ID
     * @return 微信群组
     */
    public WechatGroup selectWechatGroupByWechatGroupId(String wechatGroupId);

    /**
     * 查询微信群组列表
     * 
     * @param wechatGroup 微信群组
     * @return 微信群组集合
     */
    public List<WechatGroup> selectWechatGroupList(WechatGroup wechatGroup);

    /**
     * 根据导购ID查询微信群组列表
     * 
     * @param guideId 导购ID
     * @return 微信群组集合
     */
    public List<WechatGroup> selectWechatGroupListByGuideId(Long guideId);

    /**
     * 根据群组类型查询微信群组列表
     * 
     * @param groupType 群组类型
     * @return 微信群组集合
     */
    public List<WechatGroup> selectWechatGroupListByGroupType(Integer groupType);

    /**
     * 根据区域代码查询微信群组列表
     * 
     * @param regionCode 区域代码
     * @return 微信群组集合
     */
    public List<WechatGroup> selectWechatGroupListByRegionCode(String regionCode);

    /**
     * 查询活跃微信群组列表
     * 
     * @param wechatGroup 微信群组
     * @return 微信群组集合
     */
    public List<WechatGroup> selectActiveWechatGroupList(WechatGroup wechatGroup);

    /**
     * 新增微信群组
     * 
     * @param wechatGroup 微信群组
     * @return 结果
     */
    public int insertWechatGroup(WechatGroup wechatGroup);

    /**
     * 修改微信群组
     * 
     * @param wechatGroup 微信群组
     * @return 结果
     */
    public int updateWechatGroup(WechatGroup wechatGroup);

    /**
     * 更新群组成员数量
     * 
     * @param wechatGroup 微信群组
     * @return 结果
     */
    public int updateMemberCount(WechatGroup wechatGroup);

    /**
     * 批量删除微信群组
     * 
     * @param groupIds 需要删除的微信群组主键集合
     * @return 结果
     */
    public int deleteWechatGroupByGroupIds(String groupIds);

    /**
     * 删除微信群组信息
     * 
     * @param groupId 微信群组主键
     * @return 结果
     */
    public int deleteWechatGroupByGroupId(Long groupId);

    /**
     * 统计微信群组数量
     * 
     * @param wechatGroup 微信群组
     * @return 数量
     */
    public int countWechatGroups(WechatGroup wechatGroup);

    /**
     * 检查微信群组ID是否唯一
     * 
     * @param wechatGroup 微信群组信息
     * @return 结果
     */
    public boolean checkWechatGroupIdUnique(WechatGroup wechatGroup);
}