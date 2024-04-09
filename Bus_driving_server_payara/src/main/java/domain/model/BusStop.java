package domain.model;

import lombok.Data;

@Data
public class BusStop {
    private int id;
    private String name;
    private Point location;

}
