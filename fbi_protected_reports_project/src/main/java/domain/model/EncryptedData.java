package domain.model;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EncryptedData {
    private String aesKey;
    private String encryptedReport;
}
