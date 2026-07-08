package com.huashui.auth.domain.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色菜单关联表
 *
 * @author
 */
@Data
@TableName("sys_role_menu")
public class SysRoleMenu implements Serializable {


    private static final long serialVersionUID = 1L;


    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;


    /**
     * 角色ID
     */
    private Long roleId;


    /**
     * 菜单ID
     */
    private Long menuId;

}