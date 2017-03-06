package com.xolo.gzqc.bean.child;

/**
 * 预约进度查询
 * Created by Administrator on 2016/9/26.
 */
public class Maintain_Record {
    String name;
    String phone;
    String plan_com_time;//计划来厂时间
    String bf_orderm_id;
    String carno;//车牌号
    String bc_car_info_id;
    String operatdate;//预约时间
    String fault_description;

    public String getFault_description() {
        return fault_description;
    }

    public void setFault_description(String fault_description) {
        this.fault_description = fault_description;
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

    public String getOperatdate() {
        return operatdate;
    }

    public void setOperatdate(String operatdate) {
        this.operatdate = operatdate;
    }

    public String getBf_orderm_id() {
        return bf_orderm_id;
    }

    public void setBf_orderm_id(String bf_orderm_id) {
        this.bf_orderm_id = bf_orderm_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPlan_com_time() {
        return plan_com_time;
    }

    public void setPlan_com_time(String plan_com_time) {
        this.plan_com_time = plan_com_time;
    }
}
