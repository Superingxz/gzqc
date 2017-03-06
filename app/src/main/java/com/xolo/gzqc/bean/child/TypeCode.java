package com.xolo.gzqc.bean.child;

import java.io.Serializable;

/**
 * 车辆品牌车型
 */
public class TypeCode implements Serializable{

    /**
     * typecode : 本田N-ONE
     * typecode_id : 010777D8-6CE6-4DDC-87F1-4D4819B2B974
     * car_type :
     */

    private String typecode;
    private String typecode_id;
    private String car_type;

    public TypeCode(String typecode, String typecode_id) {
        this.typecode = typecode;
        this.typecode_id = typecode_id;
    }

    public String getTypecode() {
        return typecode;
    }

    public void setTypecode(String typecode) {
        this.typecode = typecode;
    }

    public String getTypecode_id() {
        return typecode_id;
    }

    public void setTypecode_id(String typecode_id) {
        this.typecode_id = typecode_id;
    }

    public String getCar_type() {
        return car_type;
    }

    public void setCar_type(String car_type) {
        this.car_type = car_type;
    }
}
