package com.example.server.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverCredential {
    private int id;
    private String username;
    private String password;
    private String email;
    private boolean activated;
    private LocalDateTime activationDate;
    private String activationCode;
    private AccountRole role;

    public DriverCredential(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
