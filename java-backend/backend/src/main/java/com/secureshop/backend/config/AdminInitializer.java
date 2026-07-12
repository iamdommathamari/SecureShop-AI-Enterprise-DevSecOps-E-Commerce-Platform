package com.secureshop.backend.config;

import com.secureshop.backend.user.Role;
import com.secureshop.backend.user.User;
import com.secureshop.backend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (userRepository.findByEmail("admin@secureshop.com").isEmpty()) {

            User admin = new User();

            admin.setFirstName("System");
            admin.setLastName("Administrator");
            admin.setEmail("admin@secureshop.com");
            admin.setPassword(passwordEncoder.encode("Admin@123"));
            admin.setRole(Role.ROLE_ADMIN);

            userRepository.save(admin);

            System.out.println("Default admin created.");
        }
    }
}