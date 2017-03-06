package com.xolo.gzqc.bean.child;

/**
 * Created by Administrator on 2016/9/26.
 */
public class CarOwner_SendCar_info {
    String name;
    String bc_car_info_id;
    String phone2;
    String phone;
    String sex;
    String drivingno;
    String bc_car_sendman_id;
    public CarOwner_SendCar_info(String phone, String phone2, String name) {
        this.phone = phone;
        this.phone2 = phone2;
        this.name = name;
    }

    public String getBc_car_sendman_id() {
        return bc_car_sendman_id;
    }

    public void setBc_car_sendman_id(String bc_car_sendman_id) {
        this.bc_car_sendman_id = bc_car_sendman_id;
    }

    public String getDrivingno() {
        return drivingno;
    }

    public void setDrivingno(String drivingno) {
        this.drivingno = drivingno;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBc_car_info_id() {
        return bc_car_info_id;
    }

    public void setBc_car_info_id(String bc_car_info_id) {
        this.bc_car_info_id = bc_car_info_id;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
