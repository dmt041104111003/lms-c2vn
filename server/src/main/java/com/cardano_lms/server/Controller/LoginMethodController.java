package com.cardano_lms.server.Controller;

import com.cardano_lms.server.DTO.Request.ApiResponse;
import com.cardano_lms.server.DTO.Request.LoginMethodRequest;
import com.cardano_lms.server.DTO.Request.RoleRequest;
import com.cardano_lms.server.DTO.Response.LoginMethodResponse;
import com.cardano_lms.server.DTO.Response.RoleResponse;
import com.cardano_lms.server.Service.LoginMethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/login_methods")
@RequiredArgsConstructor
public class LoginMethodController {
    @Autowired
    LoginMethodService loginMethodService;
    @PostMapping
    ApiResponse<LoginMethodResponse> create(@RequestBody LoginMethodRequest request) {
        return ApiResponse.<LoginMethodResponse>builder()
                .result(loginMethodService.createNewLoginMethod(request))
                .build();
    }

    @DeleteMapping("/{name}")
    ApiResponse<Void> delete(@PathVariable String name) {
        loginMethodService.deleteLoginMethod(name);
        return ApiResponse.<Void>builder().build();
    }
    @GetMapping
    ApiResponse<List<LoginMethodResponse>> getAll() {
        return ApiResponse.<List<LoginMethodResponse>>builder()
                .result(loginMethodService.getAllLoginMethods())
                .build();
    }
}
