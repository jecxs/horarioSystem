package com.pontificia.horarioponti.auth.dto;

import com.pontificia.horarioponti.repository.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor

public class UserInfoResponse {

    private UUID id;
    private String username;
    private Role role;

}
