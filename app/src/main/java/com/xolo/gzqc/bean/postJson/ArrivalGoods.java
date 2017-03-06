package com.xolo.gzqc.bean.postJson;

/**
 * 确定构件到货
 */
public class ArrivalGoods {


    /**
     * buyd_id : 43696E13-20A8-4990-AE81-746C8558B67A
     * is_all_arrival : 1
     */

    private String buyd_id;
    private String is_all_arrival;

    public String getBuyd_id() {
        return buyd_id;
    }

    public void setBuyd_id(String buyd_id) {
        this.buyd_id = buyd_id;
    }

    public String getIs_all_arrival() {
        return is_all_arrival;
    }

    public void setIs_all_arrival(String is_all_arrival) {
        this.is_all_arrival = is_all_arrival;
    }

    @Override
    public String toString() {
        return "{\"buyd_id\":\""+buyd_id+"\",\"is_all_arrival\":\""+is_all_arrival+"\"}";
    }
}
