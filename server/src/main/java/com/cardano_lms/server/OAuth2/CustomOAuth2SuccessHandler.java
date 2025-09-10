package com.cardano_lms.server.OAuth2;

import com.cardano_lms.server.Constant.PredefineLoginMethod;
import com.cardano_lms.server.Constant.PredefinedRole;
import com.cardano_lms.server.DTO.Request.UserCreationRequest;
import com.cardano_lms.server.DTO.Response.AuthenticationResponse;
import com.cardano_lms.server.DTO.Response.UserResponse;
import com.cardano_lms.server.Entity.Role;
import com.cardano_lms.server.Entity.User;
import com.cardano_lms.server.Exception.AppException;
import com.cardano_lms.server.Exception.ErrorCode;
import com.cardano_lms.server.Mapper.UserMapper;
import com.cardano_lms.server.Repository.RoleRepository;
import com.cardano_lms.server.Repository.UserRepository;
import com.cardano_lms.server.Service.AuthenticationService;
import com.cardano_lms.server.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final  UserService userService;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        if (email == null) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        User user = userRepository.findByEmail(email).orElseGet(() -> {
            UserResponse userResponse = userService.createUser(
                    UserCreationRequest.builder()
                            .email(email)
                            .firstName(name)
                            .loginMethod(PredefineLoginMethod.GOOGLE_METHOD)
                            .build()
            );
            return userMapper.toUser(userResponse);
        });

        String token = authenticationService.generateToken(user);

        AuthenticationResponse authResponse = AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        new ObjectMapper().writeValue(response.getWriter(), authResponse);
    }
}
