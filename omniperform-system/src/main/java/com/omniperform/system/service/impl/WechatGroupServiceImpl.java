package com.omniperform.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.omniperform.common.core.text.Convert;
import com.omniperform.common.utils.StringUtils;
import com.omniperform.system.mapper.WechatGroupMapper;
import com.omniperform.system.domain.WechatGroup;
import com.omniperform.system.service.IWechatGroupService;

/**
 * 微信群组Service业务层处理
 * 
 * @author omniperform
 */
@Service
public class WechatGroupServiceImpl implements IWechatGroupService 
{
    @Autowired
    private WechatGroupMapper wechatGroupMapper;

    /**
     * 查询微信群组
     * 
     * @param groupId 微信群组主键
     * @return 微信群组
     */
    @Override
    public WechatGroup selectWechatGroupByGroupId(Long groupId)
    {
        return wechatGroupMapper.selectWechatGroupByGroupId(groupId);
    }

    /**
     * 根据微信群组ID查询微信群组
     * 
     * @param wechatGroupId 微信群组ID
     * @return 微信群组
     */
    @Override
    public WechatGroup selectWechatGroupByWechatGroupId(String wechatGroupId)
    {
        return wechatGroupMapper.selectWechatGroupByWechatGroupId(wechatGroupId);
    }

    /**
     * 查询微信群组列表
     * 
     * @param wechatGroup 微信群组
     * @return 微信群组
     */
    @Override
    public List<WechatGroup> selectWechatGroupList(WechatGroup wechatGroup)
    {
        return wechatGroupMapper.selectWechatGroupList(wechatGroup);
    }

    /**
     * 根据导购ID查询微信群组列表
     * 
     * @param guideId 导购ID
     * @return 微信群组集合
     */
    @Override
    public List<WechatGroup> selectWechatGroupListByGuideId(Long guideId)
    {
        return wechatGroupMapper.selectWechatGroupListByGuideId(guideId);
    }

    /**
     * 根据群组类型查询微信群组列表
     * 
     * @param groupType 群组类型
     * @return 微信群组集合
     */
    @Override
    public List<WechatGroup> selectWechatGroupListByGroupType(Integer groupType)
    {
        return wechatGroupMapper.selectWechatGroupListByType(groupType);
    }

    /**
     * 根据区域代码查询微信群组列表
     * 
     * @param regionCode 区域代码
     * @return 微信群组集合
     */
    @Override
    public List<WechatGroup> selectWechatGroupListByRegionCode(String regionCode)
    {
        return wechatGroupMapper.selectWechatGroupListByRegion(regionCode);
    }

    /**
     * 查询活跃微信群组列表
     * 
     * @param wechatGroup 微信群组
     * @return 微信群组集合
     */
    @Override
    public List<WechatGroup> selectActiveWechatGroupList(WechatGroup wechatGroup)
    {
        return wechatGroupMapper.selectActiveWechatGroupList(wechatGroup);
    }

    /**
     * 新增微信群组
     * 
     * @param wechatGroup 微信群组
     * @return 结果
     */
    @Override
    public int insertWechatGroup(WechatGroup wechatGroup)
    {
        return wechatGroupMapper.insertWechatGroup(wechatGroup);
    }

    /**
     * 修改微信群组
     * 
     * @param wechatGroup 微信群组
     * @return 结果
     */
    @Override
    public int updateWechatGroup(WechatGroup wechatGroup)
    {
        return wechatGroupMapper.updateWechatGroup(wechatGroup);
    }

    /**
     * 更新群组成员数量
     * 
     * @param wechatGroup 微信群组
     * @return 结果
     */
    @Override
    public int updateMemberCount(WechatGroup wechatGroup)
    {
        return wechatGroupMapper.updateMemberCount(wechatGroup);
    }

    /**
     * 批量删除微信群组
     * 
     * @param groupIds 需要删除的微信群组主键
     * @return 结果
     */
    @Override
    public int deleteWechatGroupByGroupIds(String groupIds)
    {
        return wechatGroupMapper.deleteWechatGroupByGroupIds(Convert.toLongArray(groupIds));
    }

    /**
     * 删除微信群组信息
     * 
     * @param groupId 微信群组主键
     * @return 结果
     */
    @Override
    public int deleteWechatGroupByGroupId(Long groupId)
    {
        return wechatGroupMapper.deleteWechatGroupByGroupId(groupId);
    }

    /**
     * 统计微信群组数量
     * 
     * @param wechatGroup 微信群组
     * @return 数量
     */
    @Override
    public int countWechatGroups(WechatGroup wechatGroup)
    {
        return wechatGroupMapper.countWechatGroups(wechatGroup);
    }

    /**
     * 检查微信群组ID是否唯一
     * 
     * @param wechatGroup 微信群组信息
     * @return 结果
     */
    @Override
    public boolean checkWechatGroupIdUnique(WechatGroup wechatGroup)
    {
        Long groupId = StringUtils.isNull(wechatGroup.getGroupId()) ? -1L : wechatGroup.getGroupId();
        WechatGroup info = wechatGroupMapper.selectWechatGroupByWechatGroupId(wechatGroup.getGroupId().toString());
        if (StringUtils.isNotNull(info) && info.getGroupId().longValue() != groupId.longValue())
        {
            return false;
        }
        return true;
    }
}