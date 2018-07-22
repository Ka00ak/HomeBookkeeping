package com.kaooak.android.homebookkeeping.data.JSON;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Valute {

    @SerializedName("USD")
    @Expose
    private ConcreteValute uSD;
    @SerializedName("EUR")
    @Expose
    private ConcreteValute eUR;

    public ConcreteValute getUSD() {
        return uSD;
    }

    public void setUSD(ConcreteValute uSD) {
        this.uSD = uSD;
    }

    public ConcreteValute getEUR() {
        return eUR;
    }

    public void setEUR(ConcreteValute eUR) {
        this.eUR = eUR;
    }
}
