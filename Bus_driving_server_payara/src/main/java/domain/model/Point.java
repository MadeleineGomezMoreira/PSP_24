package domain.model;

import lombok.Data;

import java.nio.ByteBuffer;

@Data
public class Point {
    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Point fromByteArray(byte[] byteArray) {
        if (byteArray == null) {
            throw new IllegalArgumentException("Invalid byte array format");
        }

        ByteBuffer buffer = ByteBuffer.wrap(byteArray);
        double x = buffer.getDouble();
        double y = buffer.getDouble();

        return new Point(x, y);
    }

    public byte[] toByteArray() {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.putDouble(x);
        buffer.putDouble(y);

        return buffer.array();
    }

    //public static String formatCoordinates(double coordinate) {
    //    // Format the coordinate with 6 decimal places
    //    return String.format("%.6f", coordinate);
    //}

    //String formattedX = formatCoordinates(location.getX());
    //String formattedY = formatCoordinates(location.getY());
    //
    //System.out.println("x: " + formattedX + ", y: " + formattedY);


}
