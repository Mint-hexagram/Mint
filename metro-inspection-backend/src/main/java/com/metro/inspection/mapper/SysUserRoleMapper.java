package com.metro.inspection.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.metro.inspection.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    @Select("SELECT r.role_name FROM sys_role r " +
            "INNER JOIN sys_user_role ur ON r.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND r.status = 0")
    List<String> getUserRoleNames(Long userId);
} 