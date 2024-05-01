package domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {

    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String username;
    private String password;
    private String activationCode = "";
    private LocalDateTime activationDate = LocalDateTime.now();
    private boolean activated = false;

    public RegisterDTO(String firstName, String lastName, String phone, String email, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.username = username;
        this.password = password;
    }
}
