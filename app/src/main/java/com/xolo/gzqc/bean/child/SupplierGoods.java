package com.xolo.gzqc.bean.child;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/12/16.
 */
public class SupplierGoods implements Serializable {
    String total;
    String order_number;
    String emsno;
    String c_clientinfor_id;
    String status;
    String link_name;
    String shipper_code;
    String c_order_m_id;
    String clientname;
    String link_tel;
    String masterid;
    String fee;
    String address;
    String emscompany;
    String out_time;
    String clienttel;
    String closed_time;
    String auditing;
    String operatdate;


    List<SupplierGoodsChild> goods;

    public List<SupplierGoodsChild> getGoods() {
        return goods;
    }

    public void setGoods(List<SupplierGoodsChild> goods) {
        this.goods = goods;
    }

    public String getC_order_m_id() {
        return c_order_m_id;
    }

    public void setC_order_m_id(String c_order_m_id) {
        this.c_order_m_id = c_order_m_id;
    }

    public String getClientname() {
        return clientname;
    }

    public void setClientname(String clientname) {
        this.clientname = clientname;
    }

    public String getLink_tel() {
        return link_tel;
    }

    public void setLink_tel(String link_tel) {
        this.link_tel = link_tel;
    }

    public String getMasterid() {
        return masterid;
    }

    public void setMasterid(String masterid) {
        this.masterid = masterid;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmscompany() {
        return emscompany;
    }

    public void setEmscompany(String emscompany) {
        this.emscompany = emscompany;
    }

    public String getOut_time() {
        return out_time;
    }

    public void setOut_time(String out_time) {
        this.out_time = out_time;
    }

    public String getClienttel() {
        return clienttel;
    }

    public void setClienttel(String clienttel) {
        this.clienttel = clienttel;
    }

    public String getClosed_time() {
        return closed_time;
    }

    public void setClosed_time(String closed_time) {
        this.closed_time = closed_time;
    }

    public String getAuditing() {
        return auditing;
    }

    public void setAuditing(String auditing) {
        this.auditing = auditing;
    }

    public String getOperatdate() {
        return operatdate;
    }

    public void setOperatdate(String operatdate) {
        this.operatdate = operatdate;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getEmsno() {
        return emsno;
    }

    public void setEmsno(String emsno) {
        this.emsno = emsno;
    }

    public String getC_clientinfor_id() {
        return c_clientinfor_id;
    }

    public void setC_clientinfor_id(String c_clientinfor_id) {
        this.c_clientinfor_id = c_clientinfor_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLink_name() {
        return link_name;
    }

    public void setLink_name(String link_name) {
        this.link_name = link_name;
    }

    public String getShipper_code() {
        return shipper_code;
    }

    public void setShipper_code(String shipper_code) {
        this.shipper_code = shipper_code;
    }
}
