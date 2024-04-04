package data.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reports")
public class ReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reportId;
    @Column(name = "title")
    private String title;
    @Column(name = "content")
    private String content;
    @Column(name = "last_modified_by_id")
    private int lastModifiedById;
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    public ReportEntity(int reportId) {
        this.reportId = reportId;
    }
}
