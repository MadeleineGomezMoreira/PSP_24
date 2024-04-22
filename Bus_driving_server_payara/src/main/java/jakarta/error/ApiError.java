package jakarta.error;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ApiError {

    private String message;
    private LocalDateTime date;

    public ApiError(String message) {
        this.message = message;
        this.date = LocalDateTime.now();
    }
}
