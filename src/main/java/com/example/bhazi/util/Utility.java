package com.example.bhazi.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Collection;

public class Utility {
    public static double scaleNumber(double number, int scale) {
        DecimalFormat decimalFormat = new DecimalFormat("#.000");
        decimalFormat.format(number);
        return new BigDecimal(number).setScale(scale, RoundingMode.HALF_UP).doubleValue();
    }

    public static float getSumOfList(Collection<Float> list) {
        float sum = 0.0f;
        for (float value : list) {
            sum += value;
        }
        return sum;
    }
}
