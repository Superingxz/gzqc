package com.xolo.gzqc.bean.postJson;

/**
 * Created by Administrator on 2016/10/10.
 */
public class Repairlist {

    private String parts_name;
    private String qty;
    private String costprice;
    private String saleprice;



    public String getParts_name() {
        return parts_name;
    }

    public void setParts_name(String parts_name) {
        this.parts_name = parts_name;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getCostprice() {
        return costprice;
    }

    public void setCostprice(String costprice) {
        this.costprice = costprice;
    }

    public String getSaleprice() {
        return saleprice;
    }

    public void setSaleprice(String saleprice) {
        this.saleprice = saleprice;
    }

    public Repairlist(String parts_name, String qty, String costprice, String saleprice) {
        this.parts_name = parts_name;
        this.qty = qty;
        this.costprice = costprice;
        this.saleprice = saleprice;
    }

    @Override
    public String toString() {
        return "{\"parts_name\": \""+parts_name+"\",\"qty\": \""+qty+"\",\"costprice\": \""+costprice+"\",\"saleprice\": \""+saleprice+"\"}";
    }

}
