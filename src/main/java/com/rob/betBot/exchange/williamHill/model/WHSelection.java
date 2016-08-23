package com.rob.betBot.exchange.williamHill.model;

public class WHSelection {

    private String ev_oc_id; // eg. "878349638"
    private String ev_mkt_id; // eg."262608610"
    private String name; // eg. "Guingamp"

    // Odds are fractions eg. "29/20"
    private String lp_num; // Numerator eg. "29"
    private String lp_den; // Denumerator eg. "20"

    public String getEv_oc_id() {
        return ev_oc_id;
    }

    public void setEv_oc_id(String ev_oc_id) {
        this.ev_oc_id = ev_oc_id;
    }

    public String getEv_mkt_id() {
        return ev_mkt_id;
    }

    public void setEv_mkt_id(String ev_mkt_id) {
        this.ev_mkt_id = ev_mkt_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLp_num() {
        return lp_num;
    }

    public void setLp_num(String lp_num) {
        this.lp_num = lp_num;
    }

    public String getLp_den() {
        return lp_den;
    }

    public void setLp_den(String lp_den) {
        this.lp_den = lp_den;
    }
}
