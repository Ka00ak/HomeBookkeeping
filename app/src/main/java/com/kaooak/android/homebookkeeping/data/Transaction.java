package com.kaooak.android.homebookkeeping.data;

import java.util.Date;
import java.util.UUID;

public class Transaction {
    private UUID mUUID;
    private int mType;
    private Date mDate;
    private UUID mAccountOneUUID;
    private UUID mAccountTwoUUID;
    private double mValue;
    private int mCurrency;
    private double mRate;
    private String mComment;

    public Transaction(String uuid, int type, long date, String accountOneUuid, String accountTwoUuid, double value, int currency, double rate, String comment) {
        mUUID = UUID.fromString(uuid);
        mType = type;
        mDate = new Date(date);
        mAccountOneUUID = UUID.fromString(accountOneUuid);
        mAccountTwoUUID = UUID.fromString(accountTwoUuid);
        mValue = value;
        mCurrency = currency;
        mRate = rate;
        mComment = comment;
    }
    public Transaction(int type, long date, String accountOneUuid, String accountTwoUuid, double value, int currency, double rate, String comment) {
        mUUID = UUID.randomUUID();
        mType = type;
        mDate = new Date(date);
        mAccountOneUUID = UUID.fromString(accountOneUuid);
        mAccountTwoUUID = UUID.fromString(accountTwoUuid);
        mValue = value;
        mCurrency = currency;
        mRate = rate;
        mComment = comment;
    }

    public String getUUID() {
        return mUUID.toString();
    }
    public void setUUID(String uuid) {
        mUUID = UUID.fromString(uuid);
    }

    public int getType() {
        return mType;
    }
    public void setType(int type) {
        mType = type;
    }

    public long getDate() {
        return mDate.getTime();
    }
    public void setDate(long date) {
        mDate = new Date(date);
    }

    public String getAccountOneUUID() {
        return mAccountOneUUID.toString();
    }
    public void setAccountOneUUID(String accountOneUUID) {
        mAccountOneUUID = UUID.fromString(accountOneUUID);
    }

    public String getAccountTwoUUID() {
        return mAccountTwoUUID.toString();
    }
    public void setAccountTwoUUID(String accountTwoUUID) {
        mAccountTwoUUID = UUID.fromString(accountTwoUUID);
    }

    public double getValue() {
        return mValue;
    }
    public void setValue(double value) {
        mValue = value;
    }

    public int getCurrency() {
        return mCurrency;
    }
    public void setCurrency(int currency) {
        mCurrency = currency;
    }

    public double getRate() {
        return mRate;
    }
    public void setRate(double rate) {
        mRate = rate;
    }

    public String getComment() {
        return mComment;
    }
    public void setComment(String comment) {
        mComment = comment;
    }
}
