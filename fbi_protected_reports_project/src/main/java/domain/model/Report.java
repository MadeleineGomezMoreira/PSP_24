package domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Report {
    private int reportId;
    private String title;
    private String content;
    private int lastModifiedById;
    private LocalDateTime lastModifiedDate;
}
