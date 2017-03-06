package com.xolo.gzqc.bean.child;

/**
 * Created by Administrator on 2016/12/17.
 */
public class CreatOrder {

    /**
     * order_number : O2016121716202401
     * total_qty : 1
     * total_money : 100.00
     * c_order_m_id : A614BF9A-312B-4B25-A942-8696162AEBE1
     */

    private String order_number;
    private String total_qty;
    private String total_money;
    private String c_order_m_id;

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getTotal_qty() {
        return total_qty;
    }

    public void setTotal_qty(String total_qty) {
        this.total_qty = total_qty;
    }

    public String getTotal_money() {
        return total_money;
    }

    public void setTotal_money(String total_money) {
        this.total_money = total_money;
    }

    public String getC_order_m_id() {
        return c_order_m_id;
    }

    public void setC_order_m_id(String c_order_m_id) {
        this.c_order_m_id = c_order_m_id;
    }
}
