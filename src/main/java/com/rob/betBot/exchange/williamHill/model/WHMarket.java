package com.rob.betBot.exchange.williamHill.model;


public class WHMarket {

    private String ev_mkt_id; //eg. "262608613"
    private String status; // eg. "A"
    private String mkt_name; // eg. "Correct Score"

    public String getEv_mkt_id() {
        return ev_mkt_id;
    }

    public void setEv_mkt_id(String ev_mkt_id) {
        this.ev_mkt_id = ev_mkt_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMkt_name() {
        return mkt_name;
    }

    public void setMkt_name(String mkt_name) {
        this.mkt_name = mkt_name;
    }
}
