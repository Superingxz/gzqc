package com.xolo.gzqc.bean.child;

/**
 * Created by Administrator on 2016/10/27.
 */
public class CoSenderCarInfo {
    String brands;
    String typecode;
    String carno;
    String bc_car_sendman_id;

    public CoSenderCarInfo(String brands, String typecode, String carno) {
        this.brands = brands;
        this.typecode = typecode;
        this.carno = carno;
    }

    public String getBrands() {
        return brands;
    }

    public void setBrands(String brands) {
        this.brands = brands;
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

    public String getBc_car_sendman_id() {
        return bc_car_sendman_id;
    }

    public void setBc_car_sendman_id(String bc_car_sendman_id) {
        this.bc_car_sendman_id = bc_car_sendman_id;
    }
}
