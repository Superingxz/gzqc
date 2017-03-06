package com.xolo.gzqc.bean.child;

import java.util.List;

/**
 * Created by Administrator on 2016/9/27.
 */
public class CoHistroyPrice {

    String bf_receive_car_id;
    String times_cnt;
    String owen_id;
    String carno;
    String operatdate;

    List<ChildHistroyPrice> coProjects;


    public List<ChildHistroyPrice> getCoProjects() {
        return coProjects;
    }

    public void setCoProjects(List<ChildHistroyPrice> coProjects) {
        this.coProjects = coProjects;
    }

    public String getBf_receive_car_id() {
        return bf_receive_car_id;
    }

    public void setBf_receive_car_id(String bf_receive_car_id) {
        this.bf_receive_car_id = bf_receive_car_id;
    }

    public String getTimes_cnt() {
        return times_cnt;
    }

    public void setTimes_cnt(String times_cnt) {
        this.times_cnt = times_cnt;
    }

    public String getOwen_id() {
        return owen_id;
    }

    public void setOwen_id(String owen_id) {
        this.owen_id = owen_id;
    }

    public String getCarno() {
        return carno;
    }

    public void setCarno(String carno) {
        this.carno = carno;
    }

    public String getOperatdate() {
        return operatdate;
    }

    public void setOperatdate(String operatdate) {
        this.operatdate = operatdate;
    }


}
