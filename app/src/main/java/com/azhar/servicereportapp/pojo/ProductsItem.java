
package com.azhar.servicereportapp.pojo;

import com.google.gson.annotations.SerializedName;

public class ProductsItem {

    @SerializedName("nPPid")
    private String nPPid;
    @SerializedName("nPName")
    private String nPName;
    @SerializedName("nFCatId")
    private String nFCatId;
    @SerializedName("tPDescrp")
    private String tPDescrp;
    @SerializedName("tPImage")
    private String tPImage;
    @SerializedName("bPStatus")
    private String bPStatus;

    public ProductsItem(String nPPid, String nPName, String nFCatId, String tPDescrp, String tPImage, String bPStatus) {
        this.nPPid = nPPid;
        this.nPName = nPName;
        this.nFCatId = nFCatId;
        this.tPDescrp = tPDescrp;
        this.tPImage = tPImage;
        this.bPStatus = bPStatus;
    }

    public String getNPPid() {
        return nPPid;
    }

    public void setNPPid(String nPPid) {
        this.nPPid = nPPid;
    }

    public String getNPName() {
        return nPName;
    }

    public void setNPName(String nPName) {
        this.nPName = nPName;
    }

    public String getNFCatId() {
        return nFCatId;
    }

    public void setNFCatId(String nFCatId) {
        this.nFCatId = nFCatId;
    }

    public String getTPDescrp() {
        return tPDescrp;
    }

    public void setTPDescrp(String tPDescrp) {
        this.tPDescrp = tPDescrp;
    }

    public String getTPImage() {
        return tPImage;
    }

    public void setTPImage(String tPImage) {
        this.tPImage = tPImage;
    }

    public String getBPStatus() {
        return bPStatus;
    }

    public void setBPStatus(String bPStatus) {
        this.bPStatus = bPStatus;
    }

}
