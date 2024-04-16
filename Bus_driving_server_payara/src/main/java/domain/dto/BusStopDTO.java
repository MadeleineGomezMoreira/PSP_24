package domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusStopDTO {

    private int id;
    private String name;
    private String location;

    public BusStopDTO(int id) {
        this.id = id;
    }
}
