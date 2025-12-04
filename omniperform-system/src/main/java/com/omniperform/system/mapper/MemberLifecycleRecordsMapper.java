package com.omniperform.system.mapper;

import java.util.List;
import com.omniperform.system.domain.MemberLifecycleRecords;

/**
 * 会员生命周期记录Mapper接口
 * 
 * @author omniperform
 * @date 2025-01-09
 */
public interface MemberLifecycleRecordsMapper 
{
    /**
     * 查询会员生命周期记录
     * 
     * @param id 会员生命周期记录主键
     * @return 会员生命周期记录
     */
    public MemberLifecycleRecords selectMemberLifecycleRecordsById(Long id);

    /**
     * 查询会员生命周期记录列表
     * 
     * @param memberLifecycleRecords 会员生命周期记录
     * @return 会员生命周期记录集合
     */
    public List<MemberLifecycleRecords> selectMemberLifecycleRecordsList(MemberLifecycleRecords memberLifecycleRecords);

    /**
     * 新增会员生命周期记录
     * 
     * @param memberLifecycleRecords 会员生命周期记录
     * @return 结果
     */
    public int insertMemberLifecycleRecords(MemberLifecycleRecords memberLifecycleRecords);

    /**
     * 修改会员生命周期记录
     * 
     * @param memberLifecycleRecords 会员生命周期记录
     * @return 结果
     */
    public int updateMemberLifecycleRecords(MemberLifecycleRecords memberLifecycleRecords);

    /**
     * 删除会员生命周期记录
     * 
     * @param id 会员生命周期记录主键
     * @return 结果
     */
    public int deleteMemberLifecycleRecordsById(Long id);

    /**
     * 批量删除会员生命周期记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMemberLifecycleRecordsByIds(Long[] ids);
}
