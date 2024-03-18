package domain.error;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppError {
    private String message;
    private LocalDateTime localDateTime;

    public AppError(String message) {
        this.message = message;
        this.localDateTime = LocalDateTime.now();
    }
}
