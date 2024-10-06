import java.util.List;

public class Validator {

    public static boolean validateX(float x) {
        float[] checkList = {-2.0f, -1.5f, -1.0f, -0.5f, 0.0f, 0.5f, 1.0f, 1.5f, 2.0f};
        for (float value : checkList) {
            if (value == x) {
                return true; // Если нашли x в массиве
            }
        }
        return false; // Если x не найден
    }

    public static boolean validateY(float y) {
        return -5 <= y && y <= 3;
    }

    public static boolean validateR(int r) {
        int[] checkList = {1, 2, 3, 4, 5};
        for (int value : checkList) {
            if (value == r) {
                return true; // Если нашли r в массиве
            }
        }
        return false; // Если r не найден
    }
}