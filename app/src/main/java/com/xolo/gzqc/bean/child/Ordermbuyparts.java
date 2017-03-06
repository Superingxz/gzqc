package com.xolo.gzqc.bean.child;

import java.io.Serializable;

/**
 * 查询数据
 */
public class Ordermbuyparts implements Serializable{


    /**
     * phone : 13812345678
     * bf_orderm_id : 00BB2920-2727-4E18-ADDF-3EB5138995B1
     * plan_com_time : 2016-09-22 00:00:00
     * name : 张三
     * carno : 粤A65236
     * bc_car_info_id : AB747664-74E6-439D-99B2-21136FF701EF
     * operatdate : 2016-09-27 19:52:32
     */

    private String phone;
    private String bf_orderm_id;
    private String plan_com_time;
    private String name;
    private String carno;
    private String bc_car_info_id;
    private String operatdate;
    private String fault_description;
    private String source;

    /**
     * bc_car_owner_id : 978691E5-E3AC-4A26-96F7-EFDF28910712
     * is_pay : 1
     */

    private String bc_car_owner_id;
    private String is_pay;
    private String accept_userid;

    /**
     * brands : 绅宝
     * in_time : 2016-09-18 13:20:18
     * bf_receive_car_id : 7CB3465A-3111-4E47-B809-1C5490EBF351
     * typecode : X65
     * bf_quoted_pricem_id : jxstar79355
     * is_rework : 0
     */
    private String brands;
    private String in_time;
    private String bf_receive_car_id;
    private String typecode;
    private String bf_quoted_pricem_id;
    private String is_rework;
    /**
     * status : 询价中
     * buy_id : 7ED69C18-D91A-40A0-BA54-EA0F05C600EB
     * buy_time : 2016-09-27 21:09:00
     */

    private String status;
    private String buy_id;
    private String buy_time;

    private String bf_receive_id;

    private String brands_path;
    private String operatoname;

    public Ordermbuyparts(String carno, String name, String phone, String plan_com_time, String fault_description,String bc_car_owner_id,String bf_receive_id) {
        this.carno = carno;
        this.name = name;
        this.phone = phone;
        this.plan_com_time = plan_com_time;
        this.fault_description = fault_description;
        this.bc_car_owner_id = bc_car_owner_id;
        this.bf_orderm_id = bf_receive_id;
    }

    public String getOperatoname() {
        return operatoname;
    }

    public void setOperatoname(String operatoname) {
        this.operatoname = operatoname;
    }

    public String getBrands_path() {
        return brands_path;
    }

    public void setBrands_path(String brands_path) {
        this.brands_path = brands_path;
    }

    public String getAccept_userid() {
        return accept_userid;
    }

    public void setAccept_userid(String accept_userid) {
        this.accept_userid = accept_userid;
    }

    public String getBf_receive_id() {
        return bf_receive_id;
    }

    public void setBf_receive_id(String bf_receive_id) {
        this.bf_receive_id = bf_receive_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBf_orderm_id() {
        return bf_orderm_id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setBf_orderm_id(String bf_orderm_id) {
        this.bf_orderm_id = bf_orderm_id;
    }

    public String getPlan_com_time() {
        return plan_com_time;
    }

    public void setPlan_com_time(String plan_com_time) {
        this.plan_com_time = plan_com_time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getBc_car_owner_id() {
        return bc_car_owner_id;
    }

    public void setBc_car_owner_id(String bc_car_owner_id) {
        this.bc_car_owner_id = bc_car_owner_id;
    }

    public String getIs_pay() {
        return is_pay;
    }

    public void setIs_pay(String is_pay) {
        this.is_pay = is_pay;
    }

    public String getFault_description() {
        return fault_description;
    }

    public void setFault_description(String fault_description) {
        this.fault_description = fault_description;
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

    public String getBf_receive_car_id() {
        return bf_receive_car_id;
    }

    public void setBf_receive_car_id(String bf_receive_car_id) {
        this.bf_receive_car_id = bf_receive_car_id;
    }

    public String getTypecode() {
        return typecode;
    }

    public void setTypecode(String typecode) {
        this.typecode = typecode;
    }

    public String getBf_quoted_pricem_id() {
        return bf_quoted_pricem_id;
    }

    public void setBf_quoted_pricem_id(String bf_quoted_pricem_id) {
        this.bf_quoted_pricem_id = bf_quoted_pricem_id;
    }

    public String getIs_rework() {
        return is_rework;
    }

    public void setIs_rework(String is_rework) {
        this.is_rework = is_rework;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBuy_id() {
        return buy_id;
    }

    public void setBuy_id(String buy_id) {
        this.buy_id = buy_id;
    }

    public String getBuy_time() {
        return buy_time;
    }

    public void setBuy_time(String buy_time) {
        this.buy_time = buy_time;
    }
}
