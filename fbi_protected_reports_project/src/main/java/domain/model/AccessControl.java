package domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessControl {

    private int accessControlId;
    private int reportId;
    private int userId;
    private String encryptedKey;

}
