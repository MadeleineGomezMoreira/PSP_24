package domain.model;

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

    public DriverCredential(String email, LocalDateTime activationDate, String activationCode) {
        this.email = email;
        this.activationDate = activationDate;
        this.activationCode = activationCode;
    }

    public DriverCredential(int id) {
        this.id = id;
    }

    public DriverCredential(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public DriverCredential(String username) {
        this.username = username;
    }

    public DriverCredential(int id, AccountRole role) {
        this.id = id;
        this.role = role;
    }
}
