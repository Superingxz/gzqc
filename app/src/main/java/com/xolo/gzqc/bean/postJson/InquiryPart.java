package com.xolo.gzqc.bean.postJson;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/30.
 */
public class InquiryPart implements Serializable{


    /**
     * parts_name : 活塞
     * qty : 5
     * price : 130
     */

    private String parts_name;
    private String qty = "0";
    private String price = "0.00";
    @SerializedName("query_priced_id")
    private String bf_query_priced_id;
    private String costprice = "0.00";
    /**
     * parts_id : 33FBA123-DE87-4975-955F-74A763DE4A3A
     * status : 1
     * bf_buyd_id : 43696E13-20A8-4990-AE81-746C8558B67A
     * is_all_arrival : 1
     */

    private String parts_id;
    private String status;
    private String bf_buyd_id;
    private String is_all_arrival;

    private String qty_unit;
    private String source_id ="";
    private String source="";

    private String itemt_id = "";
    private String itemt_name;

    public String getItemt_id() {
        return itemt_id;
    }

    public void setItemt_id(String itemt_id) {
        this.itemt_id = itemt_id;
    }

    public String getItemt_name() {
        return itemt_name;
    }

    public void setItemt_name(String itemt_name) {
        this.itemt_name = itemt_name;
    }

    public String getSource_id() {
        return source_id;
    }

    public void setSource_id(String source_id) {
        this.source_id = source_id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getQty_unit() {
        return qty_unit;
    }

    public void setQty_unit(String qty_unit) {
        this.qty_unit = qty_unit;
    }

    public String getCostprice() {
        return costprice;
    }

    public void setCostprice(String costprice) {
        this.costprice = costprice;
    }

    public String getBf_query_priced_id() {
        return bf_query_priced_id;
    }

    public void setBf_query_priced_id(String bf_query_priced_id) {
        this.bf_query_priced_id = bf_query_priced_id;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public InquiryPart(String parts_name, String qty, String price, String costprice,String qty_unit) {
        this.parts_name = parts_name;
        this.qty = qty;
        this.price = price;
        this.costprice = costprice;
        this.qty_unit = qty_unit;
    }


    public InquiryPart(String parts_name, String qty, String price, String costprice,String qty_unit,String source,String source_id) {
        this.parts_name = parts_name;
        this.qty = qty;
        this.price = price;
        this.costprice = costprice;
        this.qty_unit = qty_unit;
        this.source = source;
        this.source_id = source_id;
    }


    public InquiryPart() {
    }

    @Override
    public String toString() {
        String  s;
        if (price==null){
            s = costprice;
        }else
             s = price;
        return "{\"query_priced_id\":\""+bf_query_priced_id+"\",\"parts_name\":\""+parts_name+"\",\"qty\":\""+qty+"\",\"price\":\""+s+"\",\"qty_unit\":\""+qty_unit+"\",\"source_id\":\""+source_id+"\",\"source\":\""+source+"\",\"itemt_name\":\""+itemt_name+"\",\"itemt_id\":\""+itemt_id+"\"}";
    }




    public String getParts_id() {
        return parts_id;
    }

    public void setParts_id(String parts_id) {
        this.parts_id = parts_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBf_buyd_id() {
        return bf_buyd_id;
    }

    public void setBf_buyd_id(String bf_buyd_id) {
        this.bf_buyd_id = bf_buyd_id;
    }

    public String getIs_all_arrival() {
        return is_all_arrival;
    }

    public void setIs_all_arrival(String is_all_arrival) {
        this.is_all_arrival = is_all_arrival;
    }
}
