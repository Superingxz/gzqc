package com.xolo.gzqc.bean.child;

/**
 * Created by Administrator on 2016/10/13.
 */
public class CoMaintainPrograss {
    String in_time;
    String typecode;
    String carno;
    String is_unsettled;
    String set_finished;
    String bf_receive_id;
    public CoMaintainPrograss(String in_time, String typecode, String carno, String is_unsettled, String set_finished) {
        this.in_time = in_time;
        this.typecode = typecode;
        this.carno = carno;
        this.is_unsettled = is_unsettled;
        this.set_finished = set_finished;
    }

    public String getBf_receive_id() {
        return bf_receive_id;
    }

    public void setBf_receive_id(String bf_receive_id) {
        this.bf_receive_id = bf_receive_id;
    }

    public String getIn_time() {
        return in_time;
    }

    public void setIn_time(String in_time) {
        this.in_time = in_time;
    }

    public String getTypecode() {
        return typecode;
    }

    public void setTypecode(String typecode) {
        this.typecode = typecode;
    }

    public String getCarno() {
        return carno;
    }

    public void setCarno(String carno) {
        this.carno = carno;
    }

    public String getIs_unsettled() {
        return is_unsettled;
    }

    public void setIs_unsettled(String is_unsettled) {
        this.is_unsettled = is_unsettled;
    }

    public String getSet_finished() {
        return set_finished;
    }

    public void setSet_finished(String set_finished) {
        this.set_finished = set_finished;
    }
}
