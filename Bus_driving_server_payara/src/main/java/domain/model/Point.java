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
        if (byteArray == null || byteArray.length < 21) {
            throw new IllegalArgumentException("Invalid byte array format");
        }

        // Extract X and Y coordinates from the byte array
        double x = readDoubleLE(byteArray, 5);
        double y = readDoubleLE(byteArray, 13);

        return new Point(x, y);
    }

    // Helper method to read a double from a byte array in little-endian format
    private static double readDoubleLE(byte[] byteArray, int offset) {
        long longValue = ((long) byteArray[offset + 7] << 56)
                | ((long) (byteArray[offset + 6] & 0xFF) << 48)
                | ((long) (byteArray[offset + 5] & 0xFF) << 40)
                | ((long) (byteArray[offset + 4] & 0xFF) << 32)
                | ((long) (byteArray[offset + 3] & 0xFF) << 24)
                | ((long) (byteArray[offset + 2] & 0xFF) << 16)
                | ((long) (byteArray[offset + 1] & 0xFF) << 8)
                | (byteArray[offset] & 0xFF);
        return Double.longBitsToDouble(longValue);
    }

//    public static Point fromByteArray(byte[] byteArray) {
//        if (byteArray == null) {
//            throw new IllegalArgumentException("Invalid byte array format");
//        }
//
//        ByteBuffer buffer = ByteBuffer.wrap(byteArray);
//        double x = buffer.getDouble();
//        double y = buffer.getDouble();
//
//        return new Point(x, y);
//    }
//
//    public byte[] toByteArray() {
//        ByteBuffer buffer = ByteBuffer.allocate(16);
//        buffer.putDouble(x);
//        buffer.putDouble(y);
//
//        return buffer.array();
//    }

    //public static String formatCoordinates(double coordinate) {
    //    // Format the coordinate with 6 decimal places
    //    return String.format("%.6f", coordinate);
    //}

    //String formattedX = formatCoordinates(location.getX());
    //String formattedY = formatCoordinates(location.getY());
    //
    //System.out.println("x: " + formattedX + ", y: " + formattedY);


}
