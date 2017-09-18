
package com.azhar.servicereportapp.pojo;

public class HistoryItem {
    String nPCompId, vPName, tCDescrp, tCImage, ttCompTimeStamp;

    public HistoryItem(String nPCompId, String vPName, String tCDescrp, String tCImage, String ttCompTimeStamp) {
        this.nPCompId = nPCompId;
        this.vPName = vPName;
        this.tCDescrp = tCDescrp;
        this.tCImage = tCImage;
        this.ttCompTimeStamp = ttCompTimeStamp;
    }

    public String getnPCompId() {
        return nPCompId;
    }

    public void setnPCompId(String nPCompId) {
        this.nPCompId = nPCompId;
    }

    public String getvPName() {
        return vPName;
    }

    public void setvPName(String vPName) {
        this.vPName = vPName;
    }

    public String gettCDescrp() {
        return tCDescrp;
    }

    public void settCDescrp(String tCDescrp) {
        this.tCDescrp = tCDescrp;
    }

    public String gettCImage() {
        return tCImage;
    }

    public void settCImage(String tCImage) {
        this.tCImage = tCImage;
    }

    public String getTtCompTimeStamp() {
        return ttCompTimeStamp;
    }

    public void setTtCompTimeStamp(String ttCompTimeStamp) {
        this.ttCompTimeStamp = ttCompTimeStamp;
    }
}
