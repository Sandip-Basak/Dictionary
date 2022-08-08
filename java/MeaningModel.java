package com.example.dictionary;

public class MeaningModel {
    private String POS="";
    private String Mean="";
    public MeaningModel(String POS, String mean) {
        this.POS = POS;
        this.Mean = mean;
    }

    public String getPOS() {
        return POS;
    }

    public void setPOS(String POS) {
        this.POS = POS;
    }

    public String getMean() {
        return Mean;
    }

    public void setMean(String mean) {
        Mean = mean;
    }
}
