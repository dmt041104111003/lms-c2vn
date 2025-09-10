package com.cardano_lms.server.Config;


import com.cardano_lms.server.Constant.PredefineLoginMethod;
import com.cardano_lms.server.Constant.PredefinedRole;
import com.cardano_lms.server.Entity.LoginMethod;
import com.cardano_lms.server.Entity.Role;
import com.cardano_lms.server.Entity.User;
import com.cardano_lms.server.Repository.LoginMethodRepository;
import com.cardano_lms.server.Repository.RoleRepository;
import com.cardano_lms.server.Repository.SocialLinkRepository;
import com.cardano_lms.server.Repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @NonFinal
    static final String ADMIN_USER_NAME = "admin";

    @NonFinal
    static final String ADMIN_PASSWORD = "admin";

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository,
                                        RoleRepository roleRepository,
                                        LoginMethodRepository loginMethodRepository,
                                        SocialLinkRepository socialLinkRepository) {
        log.info("Initializing application.....");
        return args -> {
            if (userRepository.findByUsername(ADMIN_USER_NAME).isEmpty()) {
                roleRepository.save(Role.builder()
                        .name(PredefinedRole.USER_ROLE)
                        .description("User role")
                        .build());

                roleRepository.save(Role.builder()
                        .name(PredefinedRole.INSTRUCTOR_ROLE)
                        .description("Instructor role")
                        .build());

                Role adminRole = roleRepository.save(Role.builder()
                        .name(PredefinedRole.ADMIN_ROLE)
                        .description("Admin role")
                        .build());

                User user = User.builder()
                        .username(ADMIN_USER_NAME)
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .role(adminRole)
                        .build();

                userRepository.save(user);

                log.warn("admin user has been created with default password: admin, please change it");
            }

            if (!loginMethodRepository.existsByName(PredefineLoginMethod.USERNAME_PASSWORD_METHOD)) {
                loginMethodRepository.save(
                        LoginMethod.builder()
                                .name(PredefineLoginMethod.USERNAME_PASSWORD_METHOD)
                                .description("Login with username & password")
                                .build()
                );
            }

            if (!loginMethodRepository.existsByName(PredefineLoginMethod.GOOGLE_METHOD)) {
                loginMethodRepository.save(
                        LoginMethod.builder()
                                .name(PredefineLoginMethod.GOOGLE_METHOD)
                                .description("Login with Google account")
                                .build()
                );
            }

            if (!loginMethodRepository.existsByName(PredefineLoginMethod.WALLET_METHOD)) {
                loginMethodRepository.save(
                        LoginMethod.builder()
                                .name(PredefineLoginMethod.WALLET_METHOD)
                                .description("Login with blockchain wallet")
                                .build()
                );
            }


            log.info("Application initialization completed .....");
        };
    }

}
