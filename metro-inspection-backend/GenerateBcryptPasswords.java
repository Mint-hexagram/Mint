import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerateBcryptPasswords {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // 生成BCrypt哈希值
        String adminPassword = "aaaaaa";
        String zhangsanPassword = "bbbbb";
        String lisiPassword = "cccccc";
        
        System.out.println("-- 更新现有用户密码为BCrypt加密版本");
        System.out.println("-- admin用户的密码：" + adminPassword);
        System.out.println("UPDATE sys_user SET password = '" + encoder.encode(adminPassword) + "' WHERE username = 'admin';");
        System.out.println();
        
        System.out.println("-- zhangsan用户的密码：" + zhangsanPassword);
        System.out.println("UPDATE sys_user SET password = '" + encoder.encode(zhangsanPassword) + "' WHERE username = 'zhangsan';");
        System.out.println();
        
        System.out.println("-- lisi用户的密码：" + lisiPassword);
        System.out.println("UPDATE sys_user SET password = '" + encoder.encode(lisiPassword) + "' WHERE username = 'lisi';");
        System.out.println();
        
        System.out.println("-- 验证更新结果");
        System.out.println("SELECT user_id, username, nickname, LEFT(password, 10) as password_preview, status, create_time, remark");
        System.out.println("FROM sys_user WHERE username IN ('admin', 'zhangsan', 'lisi');");
    }
} 