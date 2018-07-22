package com.kaooak.android.homebookkeeping.data;

import java.math.BigDecimal;
import java.util.UUID;

public class Account {
    private UUID mUUID;
    private String mName;
    private int mCurrency;
    private double mValue;
    private double mStartValue;

    public Account(String uuid, String name, int currency, double value, double startValue) {
        mUUID = UUID.fromString(uuid);
        mName = name;
        mCurrency = currency;
        mValue = value;
        mStartValue = startValue;
    }

    public Account(String name, int currency, double value, double startValue) {
        mUUID = UUID.randomUUID();
        mName = name;
        mCurrency = currency;
        mValue = value;
        mStartValue = startValue;
    }

    public String getUUID() {
        return mUUID.toString();
    }

    public String getName() {
        return mName;
    }
    public void setName(String name) {
        mName = name;
    }

    public int getCurrency() {
        return mCurrency;
    }
    public void setCurrency(int currency) {
        mCurrency = currency;
    }

    public double getValue() {
        return mValue;
    }
    public void setValue(double value) {
        mValue = value;
    }

    public double getStartValue() {
        return mStartValue;
    }
    public void setStartValue(double startValue) {
        mStartValue = startValue;
    }

    @Override
    public String toString() {
        return mName;
    }
}
