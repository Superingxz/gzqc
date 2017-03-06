package com.xolo.gzqc.bean.child;

/**
 * 配件清单
 * Created by Administrator on 2016/9/27.
 */
public class CoAccessories {
    String qty;
    String saleprice;
    String totalprice;
    String parts_name;

    public CoAccessories(String parts_name, String saleprice ,String qty, String totalprice) {
        this.qty = qty;
        this.saleprice = saleprice;
        this.parts_name = parts_name;
        this.totalprice=totalprice;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getSaleprice() {
        return saleprice;
    }

    public void setSaleprice(String saleprice) {
        this.saleprice = saleprice;
    }

    public String getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }

    public String getParts_name() {
        return parts_name;
    }

    public void setParts_name(String parts_name) {
        this.parts_name = parts_name;
    }
}
