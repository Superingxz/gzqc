package com.xolo.gzqc.bean.postJson;

import java.io.Serializable;

/**
 * 预约构件，构件清单
 */
public class Part implements Serializable{


    /**
     * parts_name : 过滤器
     * qty : 1
     * costprice : 1300
     * saleprice : 2300
     */

    private String parts_name;
    private String qty;
    private String costprice;
    private String saleprice = "0.00";
    private double totalPrice;
    private String bf_quoted_pricedd_id = "";
    private String source = "";
    private String source_id = "";

    private String qty_unit;

    public String getBf_quoted_pricedd_id() {
        return bf_quoted_pricedd_id;
    }

    public void setBf_quoted_pricedd_id(String bf_quoted_pricedd_id) {
        this.bf_quoted_pricedd_id = bf_quoted_pricedd_id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSource_id() {
        return source_id;
    }

    public void setSource_id(String source_id) {
        this.source_id = source_id;
    }

    public String getQty_unit() {
        return qty_unit;
    }

    public void setQty_unit(String qty_unit) {
        this.qty_unit = qty_unit;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

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


    public Part(String parts_name, String qty, String costprice, String saleprice, double totalPrice,String qty_unit) {
        this.parts_name = parts_name;
        this.qty = qty;
        this.costprice = costprice;
        this.saleprice = saleprice;
        this.totalPrice = totalPrice;
        this.qty_unit = qty_unit;
    }

    public Part() {
    }

    @Override
    public String toString() {
        return "{\"parts_name\":\""+parts_name+"\",\"qty\":\""+qty+"\",\"costprice\":\""+costprice+"\",\"saleprice\":\""+saleprice+"\",\"qty_unit\":\""+qty_unit+"\",\"bf_quoted_pricedd_id\":\""+bf_quoted_pricedd_id+"\",\"source\":\""+source+"\",\"source_id\":\""+source_id+"\"}";
    }

}
