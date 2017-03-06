package com.xolo.gzqc.bean.child;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/16.
 */
public class SupplierGoodsChild implements Serializable{
    String goodsname;
    String goodsid;
    String pic_path;
    String supperid;
    String qty;
    String factprice;
    String cid;
    String c_order_m_id;

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public String getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(String goodsid) {
        this.goodsid = goodsid;
    }

    public String getPic_path() {
        return pic_path;
    }

    public void setPic_path(String pic_path) {
        this.pic_path = pic_path;
    }

    public String getSupperid() {
        return supperid;
    }

    public void setSupperid(String supperid) {
        this.supperid = supperid;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getFactprice() {
        return factprice;
    }

    public void setFactprice(String factprice) {
        this.factprice = factprice;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getC_order_m_id() {
        return c_order_m_id;
    }

    public void setC_order_m_id(String c_order_m_id) {
        this.c_order_m_id = c_order_m_id;
    }
}
