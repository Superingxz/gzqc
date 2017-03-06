package com.xolo.gzqc.bean.child;

/**
 * 车辆信息
 * Created by Administrator on 2016/9/26.
 */
public class CarOwner_CarInfo {
    String img;
    String car_code;
    String car_type;
    String car_name;
    String typecode;//型号
    String carno;//车牌
    String enginecode;//引擎编号
    String operation_type;//操作类型
    String phone;
    String reg_date;
    String brands;//品牌
    String fuel_id;
    String productyear;
    String vincode;
    String bc_car_info_id;
    String drive_type;
    String fuel;
    String output;

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

    public String getEnginecode() {
        return enginecode;
    }

    public void setEnginecode(String enginecode) {
        this.enginecode = enginecode;
    }

    public String getOperation_type() {
        return operation_type;
    }

    public void setOperation_type(String operation_type) {
        this.operation_type = operation_type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public String getBrands() {
        return brands;
    }

    public void setBrands(String brands) {
        this.brands = brands;
    }

    public String getFuel_id() {
        return fuel_id;
    }

    public void setFuel_id(String fuel_id) {
        this.fuel_id = fuel_id;
    }

    public String getProductyear() {
        return productyear;
    }

    public void setProductyear(String productyear) {
        this.productyear = productyear;
    }

    public String getVincode() {
        return vincode;
    }

    public void setVincode(String vincode) {
        this.vincode = vincode;
    }

    public String getBc_car_info_id() {
        return bc_car_info_id;
    }

    public void setBc_car_info_id(String bc_car_info_id) {
        this.bc_car_info_id = bc_car_info_id;
    }

    public String getDrive_type() {
        return drive_type;
    }

    public void setDrive_type(String drive_type) {
        this.drive_type = drive_type;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public CarOwner_CarInfo(String car_name, String car_code, String car_type, String img) {
        this.car_name = car_name;
        this.car_code = car_code;
        this.car_type = car_type;
        this.img = img;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCar_code() {
        return car_code;
    }

    public void setCar_code(String car_code) {
        this.car_code = car_code;
    }

    public String getCar_type() {
        return car_type;
    }

    public void setCar_type(String car_type) {
        this.car_type = car_type;
    }

    public String getCar_name() {
        return car_name;
    }

    public void setCar_name(String car_name) {
        this.car_name = car_name;
    }
}
