package com.example.shangting.config;



import com.example.shangting.entity.User;
import com.example.shangting.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // 检查是否已有管理员
        if (userRepository.existsByUsername("admin")) {
            System.out.println("管理员已存在，跳过创建");
            return;
        }

        // 创建默认管理员
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123")); // 可改成更安全的密码
        admin.setEmail("admin@system.com");
        admin.setRole("ADMIN");

        userRepository.save(admin);
        System.out.println("✅ 默认管理员账户已创建: admin / admin123");
    }
}
