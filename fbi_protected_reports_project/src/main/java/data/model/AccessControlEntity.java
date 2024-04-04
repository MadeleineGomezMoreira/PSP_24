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
@NamedQueries({
        @NamedQuery(name = "HQL_GET_ACCESS_CONTROL_BY_USER_AND_REPORT_ID",
                query = "from AccessControlEntity where userId=:userId and reportId=:reportId"),
})
public class AccessControlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int accessId;
    @Column(name = "report_id")
    private int reportId;
    @Column(name = "user_id")
    private int userId;
    @Column(name = "encrypted_key")
    private String encryptedKey;

    public AccessControlEntity(int reportId, int userId) {
        this.reportId = reportId;
        this.userId = userId;
    }
}
