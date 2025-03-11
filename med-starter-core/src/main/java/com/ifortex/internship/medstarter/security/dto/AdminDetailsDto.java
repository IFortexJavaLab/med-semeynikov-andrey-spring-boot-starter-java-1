package com.ifortex.internship.medstarter.security.dto;

import com.ifortex.internship.medstarter.security.model.constant.UserRole;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminDetailsDto {
    UUID accountId;
    String email;
    UserRole role;
    boolean isSuperAdmin;
}
