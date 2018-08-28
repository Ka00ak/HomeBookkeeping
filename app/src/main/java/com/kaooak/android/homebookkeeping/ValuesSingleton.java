package com.kaooak.android.homebookkeeping;

public class ValuesSingleton {

    private static double mValueRUB = 1;
    private static double mValueUSD = 1;
    private static double mValueEUR = 1;

    public static double getmValueRUB() {
        return mValueRUB;
    }
    public static void setmValueRUB(double mValueRUB) {
        ValuesSingleton.mValueRUB = mValueRUB;
    }

    public static double getmValueUSD() {
        return mValueUSD;
    }
    public static void setmValueUSD(double mValueUSD) {
        ValuesSingleton.mValueUSD = mValueUSD;
    }

    public static double getmValueEUR() {
        return mValueEUR;
    }
    public static void setmValueEUR(double mValueEUR) {
        ValuesSingleton.mValueEUR = mValueEUR;
    }
}
