package check;

import java.text.DecimalFormat;

public class Checker {

    public boolean hit(float x, float y, int r) {
        return inRect(x, y, r) || inTriangle(x, y, r) || inCircle(x, y, r);
    }

    private boolean inRect(float x, float y, int r) {
        return x <= 0 && y >= 0 && x >= -r && y <= r;
    }

    private boolean inTriangle(float x, float y, int r) {
        // Координаты вершин треугольника
        float x1 = 0, y1 = 0;
        float x2 = 0, y2 = 0.5f;
        float x3 = 1, y3 = 0;

        // Площадь треугольника, масштабированная на r
        float S = 0.5f * Math.abs((x2 - x1) * (y3 - y1) - (x3 - x1) * (y2 - y1)) * r * r;

        x1 = x1 * r; y1 = y1 * r;
        x2 = x2 * r; y2 = y2 * r;
        x3 = x3 * r; y3 = y3 * r;

        // Площадь треугольника 1
        float S1 = 0.5f * Math.abs((x2 - x1) * (y - y1) - (x - x1) * (y2 - y1));

        // Площадь треугольника 2
        float S2 = 0.5f * Math.abs((x2 - x) * (y3 - y) - (x3 - x) * (y2 - y));

        // Площадь треугольника 3
        float S3 = 0.5f * Math.abs((x - x1) * (y3 - y1) - (x3 - x1) * (y - y1));

        // Точка внутри треугольника, если сумма площадей равна исходной площади

        return Math.abs(S - (S1 + S2 + S3)) < 0.0001;
    }

    private boolean inCircle(float x, float y, int r) {
        return (x * x + y * y) <= (r * r)  && x <= 0 && y <= 0;
    }
}
