package com.metro.inspection.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.metro.inspection.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    IPage<SysUser> selectUserPage(IPage<SysUser> page, @Param(Constants.WRAPPER) Wrapper<SysUser> queryWrapper);

    IPage<SysUser> findByConditions(@Param("username") String username, @Param("nickname") String nickname, @Param("phone") String phone, @Param("deptId") Long deptId, @Param("status") Integer status, Page<SysUser> page);
    
    Page<SysUser> findUserPage(Page<SysUser> page, @Param("user") SysUser user);
} 