package com.xolo.gzqc.bean.child;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/14.
 */
public class Address implements Serializable{

    /**
     * city_id : 128307
     * default_addr : 1
     * area_id : 128355
     * area : 海珠区
     * zip_code : 510000
     * link_name : 钱不多
     * province_id : 128306
     * street : 海港花园
     * province : 广东省
     * c_clientaddr_id : D80E2818-974F-4AB6-A30B-C13F94589D11
     * link_tel : 13868686868
     * city : 广州市
     */

    private String city_id;
    private String default_addr;
    private String area_id;
    private String area;
    private String zip_code;
    private String link_name;
    private String province_id;
    private String street;
    private String province;
    private String c_clientaddr_id;
    private String link_tel;
    private String city;

    public Address(String link_name, String street) {
        this.link_name = link_name;
        this.street = street;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getDefault_addr() {
        return default_addr;
    }

    public void setDefault_addr(String default_addr) {
        this.default_addr = default_addr;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public String getLink_name() {
        return link_name;
    }

    public void setLink_name(String link_name) {
        this.link_name = link_name;
    }

    public String getProvince_id() {
        return province_id;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getC_clientaddr_id() {
        return c_clientaddr_id;
    }

    public void setC_clientaddr_id(String c_clientaddr_id) {
        this.c_clientaddr_id = c_clientaddr_id;
    }

    public String getLink_tel() {
        return link_tel;
    }

    public void setLink_tel(String link_tel) {
        this.link_tel = link_tel;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
