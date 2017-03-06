package com.xolo.gzqc.bean.postJson;

import java.io.Serializable;

/**
 * 拆检配件
 */
public class PartVerhaul implements Serializable{

    /**
     * itemt_name : 前杠喷漆
     * parts_name : null
     * itemt_id : 1
     * qty :
     */
    private String team_id;


    private String itemt_name;
    private String parts_name;
    private String itemt_id;
    private String qty;
    /**
     * price : 150
     * carno : 粤A65236
     * bf_query_priced_id : 355FC66D-399C-497E-8908-F5E4E4B8E447
     * operatdate : 2016-09-27 21:21:05
     */


    private String price;
    private String carno;
    private String bf_query_priced_id;
    private String operatdate;
    /**
     * bf_buyd_id : 25CE616F-0E3F-4C92-8BC2-5E41D67ABA7A
     * buy_time : 2016-10-08 09:59:49
     */

    private String bf_buyd_id;
    private String buy_time;
    /**
     * in_time : 2016-09-18 00:00:00
     * workamt : 500.00
     * bc_car_info_id : AB747664-74E6-439D-99B2-21136FF701EF
     */

    private String in_time;
    private String workamt;
    private String bc_car_info_id;
    /**
     * team_name :
     * team_id :
     */

    private String bf_receive_id;

    private String team_name;

    private String split_id = "";

    private String brands_path;

    public String getBrands_path() {
        return brands_path;
    }

    public void setBrands_path(String brands_path) {
        this.brands_path = brands_path;
    }

    public String getBf_receive_id() {
        return bf_receive_id;
    }

    public void setBf_receive_id(String bf_receive_id) {
        this.bf_receive_id = bf_receive_id;
    }

    public String getSplit_id() {
        return split_id;
    }

    public void setSplit_id(String split_id) {
        this.split_id = split_id;
    }

    public String getTeam_id() {
        return team_id;
    }

    public void setTeam_id(String team_id) {
        this.team_id = team_id;
    }

    public String getItemt_name() {
        return itemt_name;
    }

    public void setItemt_name(String itemt_name) {
        this.itemt_name = itemt_name;
    }

    public String getParts_name() {
        return parts_name;
    }

    public void setParts_name(String parts_name) {
        this.parts_name = parts_name;
    }

    public String getItemt_id() {
        return itemt_id;
    }

    public void setItemt_id(String itemt_id) {
        this.itemt_id = itemt_id;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    private String qty_unit;

    public PartVerhaul(String itemt_name, String parts_name, String itemt_id, String qty,String qty_unit) {
        this.itemt_name = itemt_name;
        this.parts_name = parts_name;
        this.itemt_id = itemt_id;
        this.qty = qty;
        this.qty_unit = qty_unit;
    }

    public PartVerhaul(String itemt_name, String parts_name, String itemt_id, String qty,String qty_unit,String team_id,String team_name) {
        this.itemt_name = itemt_name;
        this.parts_name = parts_name;
        this.itemt_id = itemt_id;
        this.qty = qty;
        this.qty_unit = qty_unit;
        this.team_id = team_id;
        this.team_name = team_name;
    }

    @Override
    public String toString() {
        return "{\"itemt_id\":\""+itemt_id+"\",\"itemt_name\":\""+itemt_name+"\",\"parts_name\":\""+parts_name+"\",\"qty\":\""+qty+"\",\"qty_unit\":\""+qty_unit+"\",\"team_id\":\""+team_id+"\",\"team_name\":\""+team_name+"\",\"split_id\":\""+split_id+"\"}";
    }

    public String getQty_unit() {
        return qty_unit;
    }

    public void setQty_unit(String qty_unit) {
        this.qty_unit = qty_unit;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCarno() {
        return carno;
    }

    public void setCarno(String carno) {
        this.carno = carno;
    }

    public String getBf_query_priced_id() {
        return bf_query_priced_id;
    }

    public void setBf_query_priced_id(String bf_query_priced_id) {
        this.bf_query_priced_id = bf_query_priced_id;
    }

    public String getOperatdate() {
        return operatdate;
    }

    public void setOperatdate(String operatdate) {
        this.operatdate = operatdate;
    }

    public String getBf_buyd_id() {
        return bf_buyd_id;
    }

    public void setBf_buyd_id(String bf_buyd_id) {
        this.bf_buyd_id = bf_buyd_id;
    }

    public String getBuy_time() {
        return buy_time;
    }

    public void setBuy_time(String buy_time) {
        this.buy_time = buy_time;
    }

    public String getIn_time() {
        return in_time;
    }

    public void setIn_time(String in_time) {
        this.in_time = in_time;
    }

    public String getWorkamt() {
        return workamt;
    }

    public void setWorkamt(String workamt) {
        this.workamt = workamt;
    }

    public String getBc_car_info_id() {
        return bc_car_info_id;
    }

    public void setBc_car_info_id(String bc_car_info_id) {
        this.bc_car_info_id = bc_car_info_id;
    }

    public String getTeam_name() {
        return team_name;
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }
}
