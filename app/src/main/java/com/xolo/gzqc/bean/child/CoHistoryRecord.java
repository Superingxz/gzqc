package com.xolo.gzqc.bean.child;

/**
 * 历史维修记录
 * Created by Administrator on 2016/10/12.
 */
public class CoHistoryRecord {
    String brands;
    String in_time;
    String typecode;
    String carno;
    String bc_car_info_id;
    String bf_receive_id;
    String set_finished;
    String status;
    String totalamount;

    public String getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(String totalamount) {
        this.totalamount = totalamount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBrands() {
        return brands;
    }

    public void setBrands(String brands) {
        this.brands = brands;
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

    public String getBc_car_info_id() {
        return bc_car_info_id;
    }

    public void setBc_car_info_id(String bc_car_info_id) {
        this.bc_car_info_id = bc_car_info_id;
    }

    public String getBf_receive_id() {
        return bf_receive_id;
    }

    public void setBf_receive_id(String bf_receive_id) {
        this.bf_receive_id = bf_receive_id;
    }

    public String getSet_finished() {
        return set_finished;
    }

    public void setSet_finished(String set_finished) {
        this.set_finished = set_finished;
    }
}
