public class Checker {
    public static boolean hit(float x, float y, int r) {
        return inRect(x, y, r) || inTriangle(x, y, r) || inCircle(x, y, r);
    }

    private static boolean inRect(float x, float y, int r) {
        return (-r <= x && x <= 0) && (0 <= y && y <= r);
    }

    private static boolean inTriangle(float x, float y, int r) {
        return (0 <= x && x <= 1) && (0 <= y && y <= 0.5 * r) && (y + 0.5 * (x - r) <= 0);
    }

    private static boolean inCircle(float x, float y, int r) {
        return (-0.5 <= x && x <= 0) && (-0.5 <= y && y <= 0) && ((Math.pow(x, 2) + Math.pow(y, 2) - Math.pow(0.5 * r, 2)) <= 0);
    }
}