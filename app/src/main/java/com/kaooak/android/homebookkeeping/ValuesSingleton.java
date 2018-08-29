package com.kaooak.android.homebookkeeping;

import java.math.BigDecimal;

public class ValuesSingleton {

    private static BigDecimal mValueRUB = new BigDecimal(1);
    private static BigDecimal mValueUSD = new BigDecimal(1);
    private static BigDecimal mValueEUR = new BigDecimal(1);

    public static BigDecimal getmValueRUB() {
        return mValueRUB;
    }
    public static void setmValueRUB(BigDecimal mValueRUB) {
        ValuesSingleton.mValueRUB = mValueRUB;
    }

    public static BigDecimal getmValueUSD() {
        return mValueUSD;
    }
    public static void setmValueUSD(BigDecimal mValueUSD) {
        ValuesSingleton.mValueUSD = mValueUSD;
    }

    public static BigDecimal getmValueEUR() {
        return mValueEUR;
    }
    public static void setmValueEUR(BigDecimal mValueEUR) {
        ValuesSingleton.mValueEUR = mValueEUR;
    }
}
