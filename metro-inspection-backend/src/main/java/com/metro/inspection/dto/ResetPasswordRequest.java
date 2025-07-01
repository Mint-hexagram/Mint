package com.metro.inspection.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ResetPasswordRequest {
    
    @NotBlank(message = "验证码不能为空")
    private String verificationCode;
    
    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度在 6 到 20 个字符")
    private String newPassword;
    
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;

    public ResetPasswordRequest() {}

    public ResetPasswordRequest(String verificationCode, String newPassword, String confirmPassword) {
        this.verificationCode = verificationCode;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
} 