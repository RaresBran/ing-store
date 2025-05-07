package com.ing.store;

import com.ing.store.model.Role;
import com.ing.store.model.entity.User;
import com.ing.store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommandLineAppStartupRunner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("admin@admin.com").isEmpty()) {
            userRepository.save(User.builder()
                    .role(Role.ADMIN)
                    .email("admin@admin.com")
                    .password(passwordEncoder.encode("administrator"))
                    .build());
        }
    }
}