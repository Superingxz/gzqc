package com.xolo.gzqc.bean.child;

/**
 * 保养提醒
 */
public class Maintainwarn {

    /**
     * content : 40
     * buillkeyid : 1
     * bf_warn_id : 8240674F-E21F-4F0D-BB8E-974B1B76D759
     * content2 : 100
     * sourcetype : 10
     * operatdate : 2016-10-18 00:00:00
     */

    private String content;
    private String buillkeyid;
    private String bf_warn_id;
    private String content2;
    private String sourcetype;
    private String operatdate;
    /**
     * type : 1
     */

    private String mileage;

    private String type;

    private  String mileage_add;
    /**
     * owner_status : 0
     * insurance_date : 2017-10-09 00:00:00
     * annual_check_date : 2017-10-09 00:00:00
     */

    private String owner_status;
    private String insurance_date;
    private String annual_check_date;

    public String getMileage_add() {
        return mileage_add;
    }

    public void setMileage_add(String mileage_add) {
        this.mileage_add = mileage_add;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBuillkeyid() {
        return buillkeyid;
    }

    public void setBuillkeyid(String buillkeyid) {
        this.buillkeyid = buillkeyid;
    }

    public String getBf_warn_id() {
        return bf_warn_id;
    }

    public void setBf_warn_id(String bf_warn_id) {
        this.bf_warn_id = bf_warn_id;
    }

    public String getContent2() {
        return content2;
    }

    public void setContent2(String content2) {
        this.content2 = content2;
    }

    public String getSourcetype() {
        return sourcetype;
    }

    public void setSourcetype(String sourcetype) {
        this.sourcetype = sourcetype;
    }

    public String getOperatdate() {
        return operatdate;
    }

    public void setOperatdate(String operatdate) {
        this.operatdate = operatdate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOwner_status() {
        return owner_status;
    }

    public void setOwner_status(String owner_status) {
        this.owner_status = owner_status;
    }

    public String getInsurance_date() {
        return insurance_date;
    }

    public void setInsurance_date(String insurance_date) {
        this.insurance_date = insurance_date;
    }

    public String getAnnual_check_date() {
        return annual_check_date;
    }

    public void setAnnual_check_date(String annual_check_date) {
        this.annual_check_date = annual_check_date;
    }
}
