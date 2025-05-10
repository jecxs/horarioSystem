package com.pontificia.horarioponti.modules.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtResponse {
    private final String token;
    private final UserInfoResponse user;
}
