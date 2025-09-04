package com.cardano_lms.server.Controller;

import com.cardano_lms.server.DTO.Request.ApiResponse;
import com.cardano_lms.server.DTO.Request.NonceCreationRequest;
import com.cardano_lms.server.DTO.Request.RoleRequest;
import com.cardano_lms.server.DTO.Response.NonceCreationResponse;
import com.cardano_lms.server.DTO.Response.RoleResponse;
import com.cardano_lms.server.Entity.Nonce;
import com.cardano_lms.server.Mapper.NonceMapper;
import com.cardano_lms.server.Repository.NonceRepository;
import com.cardano_lms.server.Service.NonceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/nonce")
@RequiredArgsConstructor
@Slf4j
public class NonceController {
    @Autowired
    NonceService nonceService;
    @Autowired
    NonceMapper nonceMapper;

    @PostMapping
    public ApiResponse<NonceCreationResponse> generateNonce(@RequestBody NonceCreationRequest nonceCreationRequest) {
        NonceCreationResponse nonce = nonceService.generateNonce(nonceMapper.toNonce(nonceCreationRequest).getAddress());
        return ApiResponse.<NonceCreationResponse>builder()
                .result(nonce)
                .build();
    }

}
