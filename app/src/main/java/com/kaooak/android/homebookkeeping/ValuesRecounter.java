package com.kaooak.android.homebookkeeping;

import com.kaooak.android.homebookkeeping.data.Currencies;

public class ValuesRecounter {

    public static double recount(double value, double curValue, double curValue2) {
        double result = (value * curValue) / curValue2;
        return result;
    }
}
