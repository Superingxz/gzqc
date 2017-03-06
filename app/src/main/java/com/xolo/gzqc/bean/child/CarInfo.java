package com.xolo.gzqc.bean.child;

import java.io.Serializable;

/**
 * 车辆详细信息
 */
public class CarInfo implements Serializable{


    /**
     * output_id : jxstar7527632
     * operation_id : jxstar3607591
     * typecode : X65
     * carno : 粤A65236
     * enginecode : DASFGR25642589635
     * operation_type : 自动
     * reg_date : 2016-09-23 00:00:00
     * brands : 绅宝
     * fuel_id : jxstar0797581
     * productyear : 2015
     * vincode : DASFGR25642589635
     * bc_car_owner_id : 978691E5-E3AC-4A26-96F7-EFDF28910712
     * fuel : 汽油
     * bc_car_info_id : AB747664-74E6-439D-99B2-21136FF701EF
     * drive_id : jxstar9277596
     * drive_type : 二驱前置
     * output : 2.0L
     */

    private String output_id;
    private String operation_id;
    private String typecode;
    private String carno;
    private String enginecode;
    private String operation_type;
    private String reg_date;
    private String brands;
    private String fuel_id;
    private String productyear;
    private String vincode;
    private String bc_car_owner_id;
    private String fuel;
    private String bc_car_info_id;
    private String drive_id;
    private String drive_type;
    private String output;
    private String brands_path;
    private String operatoname;
    private String old_carno;

    /**
     * in_time : 2016-09-18 13:20:18
     */
    private String in_time;
    /**
     * team_name :
     * team_id :
     */

    private String bf_query_pricem_id;

    public String getBf_query_pricem_id() {
        return bf_query_pricem_id;
    }

    public void setBf_query_pricem_id(String bf_query_pricem_id) {
        this.bf_query_pricem_id = bf_query_pricem_id;
    }

    private String team_name;
    private String team_id;
    /**
     * status : 7
     * display_data : 派工
     */

    private String status;
    private String display_data;
    /**
     * bf_finished_id : TEST201610091423
     * bf_receive_id : 1FE33E31-68E7-4BCE-B115-660BB3E4D375
     */

    private String bf_finished_id;

    /**
     * is_unsettled : 0
     */

    private String is_unsettled;

    /**
     * bf_quoted_pricem_id : B162E741-A23E-45F9-A5A2-5F8D6D46E446
     */

    private String bf_quoted_pricem_id;
    /**
     * bf_receiver_id : 1FE33E31-68E7-4BCE-B115-660BB3E4D375
     */

    /**
     * 只能说后台太厉害了
     */
    private String bf_receiver_id;

    private String bf_receive_id;

    private String bf_receive_car_id;
    /**
     * name : 测试1021
     */

    private String name;
    /**
     * operator :
     */

    private String operator;

    private String buy_time;

    private String fault_description;
    /**
     * phone :
     */

    private String phone;

    private String operatdate;

    private String is_urgent;

    private String qr_code;

    private String type;

    private String deal_time;

    private String give_date;

    private String bf_buym_id;


    private String typecode_id;

    private String brands_id;

    public String getOld_carno() {
        return old_carno;
    }

    public void setOld_carno(String old_carno) {
        this.old_carno = old_carno;
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

    public String getTypecode_id() {
        return typecode_id;
    }

    public void setTypecode_id(String typecode_id) {
        this.typecode_id = typecode_id;
    }

    public String getBrands_id() {
        return brands_id;
    }

    public void setBrands_id(String brand_id) {
        this.brands_id = brand_id;
    }

    public String getBf_buym_id() {
        return bf_buym_id;
    }

    public void setBf_buym_id(String bf_buym_id) {
        this.bf_buym_id = bf_buym_id;
    }

    public String getGive_date() {
        return give_date;
    }

    public void setGive_date(String give_date) {
        this.give_date = give_date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDeal_time() {
        return deal_time;
    }

    public void setDeal_time(String deal_time) {
        this.deal_time = deal_time;
    }

    public String getQr_code() {
        return qr_code;
    }

    public void setQr_code(String qr_code) {
        this.qr_code = qr_code;
    }

    public String getIs_urgent() {
        return is_urgent;
    }

    public void setIs_urgent(String is_urgent) {
        this.is_urgent = is_urgent;
    }

    public String getOperatdate() {
        return operatdate;
    }

    public void setOperatdate(String operatdate) {
        this.operatdate = operatdate;
    }

    public String getBf_receive_car_id() {
        return bf_receive_car_id;
    }

    public String getBuy_time() {
        return buy_time;
    }

    public void setBuy_time(String buy_time) {
        this.buy_time = buy_time;
    }

    public void setBf_receive_car_id(String bf_receive_car_id) {
        this.bf_receive_car_id = bf_receive_car_id;
    }

    public String getOutput_id() {
        return output_id;
    }

    public void setOutput_id(String output_id) {
        this.output_id = output_id;
    }

    public String getOperation_id() {
        return operation_id;
    }

    public void setOperation_id(String operation_id) {
        this.operation_id = operation_id;
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

    public String getBc_car_owner_id() {
        return bc_car_owner_id;
    }

    public void setBc_car_owner_id(String bc_car_owner_id) {
        this.bc_car_owner_id = bc_car_owner_id;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public String getBc_car_info_id() {
        return bc_car_info_id;
    }

    public void setBc_car_info_id(String bc_car_info_id) {
        this.bc_car_info_id = bc_car_info_id;
    }

    public String getDrive_id() {
        return drive_id;
    }

    public void setDrive_id(String drive_id) {
        this.drive_id = drive_id;
    }

    public String getDrive_type() {
        return drive_type;
    }

    public void setDrive_type(String drive_type) {
        this.drive_type = drive_type;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getIn_time() {
        return in_time;
    }

    public void setIn_time(String in_time) {
        this.in_time = in_time;
    }

    public String getTeam_name() {
        return team_name;
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

    public String getTeam_id() {
        return team_id;
    }

    public void setTeam_id(String team_id) {
        this.team_id = team_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDisplay_data() {
        return display_data;
    }

    public void setDisplay_data(String display_data) {
        this.display_data = display_data;
    }

    public String getBf_finished_id() {
        return bf_finished_id;
    }

    public void setBf_finished_id(String bf_finished_id) {
        this.bf_finished_id = bf_finished_id;
    }

    public String getBf_receive_id() {
        return bf_receive_id;
    }

    public void setBf_receive_id(String bf_receive_id) {
        this.bf_receive_id = bf_receive_id;
    }

    public String getBf_receiver_id() {
        return bf_receiver_id;
    }

    public void setBf_receiver_id(String bf_receiver_id) {
        this.bf_receiver_id = bf_receiver_id;
    }

    public String getIs_unsettled() {
        return is_unsettled;
    }

    public void setIs_unsettled(String is_unsettled) {
        this.is_unsettled = is_unsettled;
    }

    public String getBf_quoted_pricem_id() {
        return bf_quoted_pricem_id;
    }

    public void setBf_quoted_pricem_id(String bf_quoted_pricem_id) {
        this.bf_quoted_pricem_id = bf_quoted_pricem_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getFault_description() {
        return fault_description;
    }

    public void setFault_description(String fault_description) {
        this.fault_description = fault_description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
