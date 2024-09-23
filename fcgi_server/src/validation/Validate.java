package validation;

import java.util.LinkedList;
import java.util.List;

public class Validate{
    private final List<Float> xRange = new LinkedList<>();
    private final List<Integer> rRange = new LinkedList<>();
    private String log = "all ok";
    public Validate(){
        xRange.add(-2.0f);
        xRange.add(-1.5f);
        xRange.add(-1.0f);
        xRange.add(-0.5f);
        xRange.add(0.0f);
        xRange.add(0.5f);
        xRange.add(1.0f);
        xRange.add(1.5f);
        xRange.add(2.0f);

        rRange.add(1);
        rRange.add(2);
        rRange.add(3);
        rRange.add(4);
        rRange.add(5);
    }
    public boolean check(Float x, Float y, Integer r){
        return checkX(x) && checkY(y) && checkR(r);
    }

    public String getErr(){
        return log;
    }

    public boolean checkX(Float x){
        if (xRange.contains(x)){
            return true;
        }
        log = "X must be selected";
        return false;
    }

    public boolean checkY(Float y){
        if (-5 <= y && y <= 3){
            return true;
        }
        log = "Y value must be -5<=y<=3";
        return false;
    }

    public boolean checkR(int r){
        if (rRange.contains(r)){
            return true;
        }
        log = "R must be selected";
        return false;
    }
}
