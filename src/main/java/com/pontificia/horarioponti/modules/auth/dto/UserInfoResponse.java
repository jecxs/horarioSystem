package com.pontificia.horarioponti.modules.auth.dto;

import com.pontificia.horarioponti.enums.ERole;
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
    private ERole role;
    private String firstName;
    private String lastName;
    private String documentNumber;

}
