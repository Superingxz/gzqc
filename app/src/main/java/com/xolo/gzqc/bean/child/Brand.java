package com.xolo.gzqc.bean.child;

import java.io.Serializable;

/**
 * 车品牌
 */
public class Brand implements Serializable{

    /**
     * brands : 奥迪
     * brands_path : http://120.77.3.76/gzqiche/resources/images/brands/aodi.png
     * brands_id : AF83E026-DBB3-4A6D-8840-80EBE453B034
     * pinyin : A
     */

    private String brands;
    private String brands_path;
    private String brands_id;
    private String pinyin;

    public Brand(String brands, String brands_id) {
        this.brands = brands;
        this.brands_id = brands_id;
    }

    public String getBrands() {
        return brands;
    }

    public void setBrands(String brands) {
        this.brands = brands;
    }

    public String getBrands_path() {
        return brands_path;
    }

    public void setBrands_path(String brands_path) {
        this.brands_path = brands_path;
    }

    public String getBrands_id() {
        return brands_id;
    }

    public void setBrands_id(String brands_id) {
        this.brands_id = brands_id;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }
}
