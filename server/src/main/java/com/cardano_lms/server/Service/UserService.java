package com.cardano_lms.server.Service;

import com.cardano_lms.server.Constant.PredefineLoginMethod;
import com.cardano_lms.server.Constant.PredefinedRole;
import com.cardano_lms.server.DTO.Request.UserCreationRequest;
import com.cardano_lms.server.DTO.Request.UserUpdateRequest;
import com.cardano_lms.server.DTO.Response.UserResponse;
import com.cardano_lms.server.Entity.Role;
import com.cardano_lms.server.Entity.User;
import com.cardano_lms.server.Exception.AppException;
import com.cardano_lms.server.Exception.ErrorCode;
import com.cardano_lms.server.Mapper.UserMapper;
import com.cardano_lms.server.Repository.RoleRepository;
import com.cardano_lms.server.Repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserCreationRequest request) {

        String loginMethod = request.getLoginMethod();
        if (loginMethod == null || loginMethod.isBlank()) {
            throw new AppException(ErrorCode.LOGIN_METHOD_IS_REQUIRED);
        }

        if (!loginMethod.equals(PredefineLoginMethod.USERNAME_PASSWORD_METHOD) &&
                !loginMethod.equals(PredefineLoginMethod.WALLET_METHOD) &&
                !loginMethod.equals(PredefineLoginMethod.GOOGLE_METHOD)) {
            throw new AppException(ErrorCode.LOGIN_METHOD_IS_REQUIRED);
        }

        User user = userMapper.toUser(request);

        switch (loginMethod) {
            case PredefineLoginMethod.USERNAME_PASSWORD_METHOD:
                if (request.getPassword() == null || request.getPassword().isBlank()) {
                    throw new AppException(ErrorCode.PASSWORD_REQUIRED);
                }
                user.setPassword(passwordEncoder.encode(request.getPassword()));
                break;

            case PredefineLoginMethod.WALLET_METHOD:
                user.setPassword(UUID.randomUUID().toString());
                break;

            case PredefineLoginMethod.GOOGLE_METHOD:
                user.setPassword(UUID.randomUUID().toString());
                break;

            default:
                throw new AppException(ErrorCode.LOGIN_METHOD_IS_REQUIRED);
        }

        roleRepository.findById(PredefinedRole.USER_ROLE)
                .ifPresent(user::setRole);

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        return userMapper.toUserResponse(user);
    }



    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateUser(user, request);

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getRole() != null) {
            Role role = roleRepository.findById(request.getRole())
                    .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
            user.setRole(role);
        }

        return userMapper.toUserResponse(userRepository.save(user));
    }


    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        log.info("In method get Users");
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUser(String id) {
        return userMapper.toUserResponse(
                userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }
}
