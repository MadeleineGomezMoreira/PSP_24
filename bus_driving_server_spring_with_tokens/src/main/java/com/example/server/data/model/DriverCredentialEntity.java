package com.example.server.data.model;

import com.example.server.domain.model.AccountRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "driver_credentials")
public class DriverCredentialEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "credential_id", nullable = false)
    private int id;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "email")
    private String email;
    @Column(name = "activated")
    private boolean activated;
    @Column(name = "activation_date")
    private LocalDateTime activationDate;
    @Column(name = "activation_code")
    private String activationCode;
    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    private AccountRoleEntity role;

}
