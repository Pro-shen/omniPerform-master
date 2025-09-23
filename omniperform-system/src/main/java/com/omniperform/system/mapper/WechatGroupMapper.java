package com.omniperform.system.mapper;

import java.util.List;
import com.omniperform.system.domain.WechatGroup;

/**
 * 微信群组Mapper接口
 * 
 * @author omniperform
 */
public interface WechatGroupMapper 
{
    /**
     * 查询微信群组
     * 
     * @param groupId 微信群组主键
     * @return 微信群组
     */
    public WechatGroup selectWechatGroupByGroupId(Long groupId);

    /**
     * 根据微信群聊ID查询群组
     * 
     * @param wechatGroupId 微信群聊ID
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
    public List<WechatGroup> selectWechatGroupListByType(Integer groupType);

    /**
     * 查询活跃微信群组列表
     * 
     * @param wechatGroup 微信群组
     * @return 活跃微信群组集合
     */
    public List<WechatGroup> selectActiveWechatGroupList(WechatGroup wechatGroup);

    /**
     * 根据区域代码查询微信群组列表
     * 
     * @param regionCode 区域代码
     * @return 微信群组集合
     */
    public List<WechatGroup> selectWechatGroupListByRegion(String regionCode);

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
     * 删除微信群组
     * 
     * @param groupId 微信群组主键
     * @return 结果
     */
    public int deleteWechatGroupByGroupId(Long groupId);

    /**
     * 批量删除微信群组
     * 
     * @param groupIds 需要删除的微信群组主键集合
     * @return 结果
     */
    public int deleteWechatGroupByGroupIds(Long[] groupIds);

    /**
     * 统计群组数量
     * 
     * @param wechatGroup 查询条件
     * @return 群组数量
     */
    public int countWechatGroups(WechatGroup wechatGroup);
}