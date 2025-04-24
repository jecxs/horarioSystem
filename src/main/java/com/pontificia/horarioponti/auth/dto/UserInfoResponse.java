package com.pontificia.horarioponti.auth.dto;

import com.pontificia.horarioponti.auth.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor

public class UserInfoResponse {

    private UUID uuid;
    private String username;
    private Role role;

}
