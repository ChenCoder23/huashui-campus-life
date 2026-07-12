package com.huashui.dormitory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huashui.dormitory.domain.pojo.DormBuildingManager;
import org.apache.ibatis.annotations.Mapper;

/**
 * 楼栋-宿管关联Mapper
 */
@Mapper
public interface DormBuildingManagerMapper extends BaseMapper<DormBuildingManager> {
}