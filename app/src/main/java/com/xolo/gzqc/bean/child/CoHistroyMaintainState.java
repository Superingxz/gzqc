package com.xolo.gzqc.bean.child;

import com.xolo.gzqc.bean.BaseBean;

/**
 * 购件进度-配件采购状态
 * Created by Administrator on 2016/10/12.
 */
public class CoHistroyMaintainState extends BaseBean{
    String parts_id;
    String status;
    String totalprice;
    String costprice;
    String parts_name;
    String qty;
    String saleprice;
    String totalsaleprice;


    public String getSaleprice() {
        return saleprice;
    }

    public void setSaleprice(String saleprice) {
        this.saleprice = saleprice;
    }

    public String getTotalsaleprice() {
        return totalsaleprice;
    }

    public void setTotalsaleprice(String totalsaleprice) {
        this.totalsaleprice = totalsaleprice;
    }

    public CoHistroyMaintainState(String status, String totalprice, String costprice, String parts_name, String qty) {
        this.status = status;
        this.totalprice = totalprice;
        this.costprice = costprice;
        this.parts_name = parts_name;
        this.qty = qty;
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

    public String getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }

    public String getCostprice() {
        return costprice;
    }

    public void setCostprice(String costprice) {
        this.costprice = costprice;
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
}
