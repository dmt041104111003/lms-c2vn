package com.cardano_lms.server.Config;


import com.cardano_lms.server.Constant.PredefineLoginMethod;
import com.cardano_lms.server.Constant.PredefinedPaymentMethod;
import com.cardano_lms.server.Constant.PredefinedRole;
import com.cardano_lms.server.Entity.LoginMethod;
import com.cardano_lms.server.Entity.PaymentMethod;
import com.cardano_lms.server.Entity.Role;
import com.cardano_lms.server.Entity.User;
import com.cardano_lms.server.Repository.*;
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
                                        PaymentMethodRepository paymentMethodRepository) {
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

            if(!paymentMethodRepository.existsByName(PredefinedPaymentMethod.CARDANO_WALLET)){
                paymentMethodRepository.save(
                        PaymentMethod.builder()
                                .name(PredefinedPaymentMethod.CARDANO_WALLET)
                                .description("Payment with cardano wallet")
                                .currency("ADA")
                                .build()
                );
            }

            if(!paymentMethodRepository.existsByName(PredefinedPaymentMethod.STRIPE)){
                paymentMethodRepository.save(
                        PaymentMethod.builder()
                                .name(PredefinedPaymentMethod.STRIPE)
                                .description("Payment with stripe")
                                .currency("USD")
                                .build()
                );
            }

            if(!paymentMethodRepository.existsByName(PredefinedPaymentMethod.PAYPAL)){
                paymentMethodRepository.save(
                        PaymentMethod.builder()
                                .name(PredefinedPaymentMethod.PAYPAL)
                                .description("Payment with paypal")
                                .currency("USD")
                                .build()
                );
            }


            log.info("Application initialization completed .....");
        };
    }

}
