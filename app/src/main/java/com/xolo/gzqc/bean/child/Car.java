package com.xolo.gzqc.bean.child;

/**
 * 车辆列表
 */
public class Car {


    /**
     * brands : 绅宝
     * typecode : X65
     * carno : 粤A65236
     * bc_car_info_id : 1CDF5589-6CA9-441C-83B9-C59439D7FCB6
     */

    private String brands;
    private String typecode;
    private String carno;
    private String bc_car_info_id;
    private String qr_code;

    public String getQr_code() {
        return qr_code;
    }

    public void setQr_code(String qr_code) {
        this.qr_code = qr_code;
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

    public String getBc_car_info_id() {
        return bc_car_info_id;
    }

    public void setBc_car_info_id(String bc_car_info_id) {
        this.bc_car_info_id = bc_car_info_id;
    }
}
