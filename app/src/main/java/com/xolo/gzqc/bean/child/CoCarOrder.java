package com.xolo.gzqc.bean.child;

/**
 * Created by Administrator on 2016/10/14.
 */
public class CoCarOrder {
    String is_check;
    String operatoname;
    String description;
    String type_name;
    String photo;
    String bf_car_status_id;
    String type_id;


    public CoCarOrder(String type_name, String description,String photo) {
        this.type_name = type_name;
        this.description = description;
        this.photo=photo;
    }

    public String getIs_check() {
        return is_check;
    }

    public void setIs_check(String is_check) {
        this.is_check = is_check;
    }

    public String getOperatoname() {
        return operatoname;
    }

    public void setOperatoname(String operatoname) {
        this.operatoname = operatoname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getBf_car_status_id() {
        return bf_car_status_id;
    }

    public void setBf_car_status_id(String bf_car_status_id) {
        this.bf_car_status_id = bf_car_status_id;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }
}
