package com.metro.inspection.service;

import com.metro.inspection.dto.LoginRequest;
import com.metro.inspection.dto.LoginResponse;
import com.metro.inspection.dto.RegisterRequest;
import com.metro.inspection.dto.ForgotPasswordRequest;
import com.metro.inspection.dto.ResetPasswordRequest;

public interface AuthService {
    /**
     * 用户登录
     */
    LoginResponse login(LoginRequest loginRequest);
    
    /**
     * 用户注册
     */
    void register(RegisterRequest registerRequest);
    
    /**
     * 忘记密码 - 发送验证码
     */
    void forgotPassword(ForgotPasswordRequest forgotPasswordRequest);
    
    /**
     * 重置密码
     */
    void resetPassword(ResetPasswordRequest resetPasswordRequest);
    
    /**
     * 用户登出
     */
    void logout(String token);
    
    /**
     * 刷新令牌
     */
    String refreshToken(String token);
} 