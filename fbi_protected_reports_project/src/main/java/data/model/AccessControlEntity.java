package data.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "access_control")
public class AccessControlEntity {

    //TODO: set relationships here

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int accessId;
    @Column(name = "report_id")
    private int reportId;
    @Column(name = "user_id")
    private int userId;
    @Column(name = "encrypted_key")
    private String encryptedKey;

}
