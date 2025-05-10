package com.pontificia.horarioponti.modules.auth.dto;

import com.pontificia.horarioponti.enums.Role;
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
    private String firstName;
    private String lastName;
    private String documentNumber;

}
