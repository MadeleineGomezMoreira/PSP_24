package domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusStop {
    private int id;
    private String name;
    private Point location;

    public BusStop(int id) {
        this.id = id;
    }
}
