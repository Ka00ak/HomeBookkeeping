package com.kaooak.android.homebookkeeping.data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public class Transaction {

    private long mId;
    private Date mDate;
    private long mAccountId;
    private int mCurrency;
    private BigDecimal mCurrencyValue;
    private int mValue;
    private String mComment;

    public Transaction(long id, long millis, long accountId, int currency, BigDecimal currencyValue, int value, String comment) {
        mId = id;
        mDate = new Date(millis);
        mAccountId = accountId;
        mCurrency = currency;
        mCurrencyValue = currencyValue;
        mValue = value;
        mComment = comment;
    }
    public Transaction(long millis, long accountId, int currency, int value, String comment) {
        mDate = new Date(millis);
        mAccountId = accountId;
        mCurrency = currency;
        mValue = value;
        mComment = comment;
    }

    public long getId() {
        return mId;
    }
    public void setId(long id) {
        mId = id;
    }

    public Date getDate() {
        return mDate;
    }
    public void setDate(Date date) {
        mDate = date;
    }

    public long getAccountId() {
        return mAccountId;
    }
    public void setAccountId(long accountId) {
        mAccountId = accountId;
    }

    public int getCurrency() {
        return mCurrency;
    }
    public void setCurrency(int currency) {
        mCurrency = currency;
    }

    public BigDecimal getCurrencyValue() {
        return mCurrencyValue;
    }
    public void setCurrencyValue(BigDecimal currencyValue) {
        mCurrencyValue = currencyValue;
    }

    public int getValue() {
        return mValue;
    }
    public void setValue(int value) {
        mValue = value;
    }

    public String getComment() {
        return mComment;
    }
    public void setComment(String comment) {
        mComment = comment;
    }
}
