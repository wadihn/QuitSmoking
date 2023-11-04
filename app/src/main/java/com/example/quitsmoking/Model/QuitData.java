package com.example.quitsmoking.Model;

public class QuitData {
    private String date;
    private String numOfCigar;
    private String priceOfCigar;
    private String time;

    public QuitData() {
    }

    public QuitData(String date, String numOfCigar, String priceOfCigar, String time) {
        this.date = date;
        this.numOfCigar = numOfCigar;
        this.priceOfCigar = priceOfCigar;
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNumOfCigar() {
        return numOfCigar;
    }

    public void setNumOfCigar(String numOfCigar) {
        this.numOfCigar = numOfCigar;
    }

    public String getPriceOfCigar() {
        return priceOfCigar;
    }

    public void setPriceOfCigar(String priceOfCigar) {
        this.priceOfCigar = priceOfCigar;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
