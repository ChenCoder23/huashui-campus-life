package com.huashui.dormitory.domain.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 楼栋-宿管关联表
 */
@Data
@TableName("dorm_building_manager")
public class DormBuildingManager {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 楼栋ID
     */
    private Long buildingId;

    /**
     * 宿管用户ID
     */
    private Long userId;

    /**
     * 状态：0-停用，1-启用
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}