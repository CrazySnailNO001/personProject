package com.xzh.personalproject.commons.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;

public class NumberUtil {
    public static String getPercentValue(Double double1, Double double2) {
        return getPercentValueWithSymbol(double1, double2, true);
    }

    public static String getPercentValueWithSymbol(Double double1, Double double2, boolean withSymbol) {
        String result = "0.00";
        if (double2 != 0 && double2 > 0) {
            result = formatDouble(double1 / double2 * 100.0);
        }
        if (withSymbol) {
            result += "%";
        }
        return result;
    }

    public static String formatDouble(double d) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);
        // 如果不需要四舍五入，可以使用RoundingMode.DOWN
        nf.setRoundingMode(RoundingMode.HALF_UP);
        nf.setGroupingUsed(false);
        return nf.format(d);
    }

    public static String formatDouble(double d, int digits) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(digits);
        // 如果不需要四舍五入，可以使用RoundingMode.DOWN
        nf.setRoundingMode(RoundingMode.HALF_UP);
        nf.setGroupingUsed(false);
        return nf.format(d);
    }

    public static double twoDigit(double d) {
        return withDigit(d, 2);
    }

    public static double withDigit(double d, int digits) {
        return new BigDecimal(d).setScale(digits, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
