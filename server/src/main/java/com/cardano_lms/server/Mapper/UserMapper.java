package com.cardano_lms.server.Mapper;

import com.cardano_lms.server.DTO.Request.UserCreationRequest;
import com.cardano_lms.server.DTO.Request.UserUpdateRequest;
import com.cardano_lms.server.DTO.Response.UserResponse;
import com.cardano_lms.server.Entity.LoginMethod;
import com.cardano_lms.server.Entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "loginMethod", source = "loginMethod")
    User toUser(UserCreationRequest request);

    default LoginMethod mapLoginMethod(String loginMethodName) {
        if (loginMethodName == null) return null;
        LoginMethod lm = new LoginMethod();
        lm.setName(loginMethodName);
        return lm;
    }

    User toUser(UserResponse userResponse);

    UserResponse toUserResponse(User user);

    @Mapping(target = "role", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
