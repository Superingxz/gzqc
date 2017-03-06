package com.xolo.gzqc.bean.child;

import java.util.List;

/**
 * Created by Administrator on 2016/9/27.
 */
public class ChildHistroyPrice {

    String itemt_name;
    String times_cnt;
    String workamt;
    String bf_quoted_priced_id;
    String itemt_id;

    List<CoAccessories> coAccessories;


    public List<CoAccessories> getCoAccessories() {
        return coAccessories;
    }

    public void setCoAccessories(List<CoAccessories> coAccessories) {
        this.coAccessories = coAccessories;
    }

    public String getItemt_name() {
        return itemt_name;
    }

    public void setItemt_name(String itemt_name) {
        this.itemt_name = itemt_name;
    }

    public String getTimes_cnt() {
        return times_cnt;
    }

    public void setTimes_cnt(String times_cnt) {
        this.times_cnt = times_cnt;
    }

    public String getWorkamt() {
        return workamt;
    }

    public void setWorkamt(String workamt) {
        this.workamt = workamt;
    }

    public String getBf_quoted_priced_id() {
        return bf_quoted_priced_id;
    }

    public void setBf_quoted_priced_id(String bf_quoted_priced_id) {
        this.bf_quoted_priced_id = bf_quoted_priced_id;
    }

    public String getItemt_id() {
        return itemt_id;
    }

    public void setItemt_id(String itemt_id) {
        this.itemt_id = itemt_id;
    }
}
