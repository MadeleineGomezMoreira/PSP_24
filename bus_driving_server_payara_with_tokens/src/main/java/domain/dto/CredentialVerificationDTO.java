package domain.dto;

import domain.model.AccountRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CredentialVerificationDTO {

    private String username;
    private String role;
}
