package domain.model;

import lombok.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class Point {
    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public static Point fromString(String pointString) {
        // Assuming the POINT format is like 'POINT(x y)'
        Pattern pattern = Pattern.compile("\\(([^\\s]+)\\s([^\\s]+)\\)");
        Matcher matcher = pattern.matcher(pointString);

        if (matcher.find()) {
            double x = Double.parseDouble(matcher.group(1));
            double y = Double.parseDouble(matcher.group(2));
            return new Point(x, y);
        } else {
            throw new IllegalArgumentException("Invalid POINT format");
        }
    }
}
