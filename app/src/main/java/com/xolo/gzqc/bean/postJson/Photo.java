package com.xolo.gzqc.bean.postJson;

/**
 * 图片
 */
public class Photo {

    private  String photo;
    /**
     * bs_repair_photo_id : 741FB207-979A-4206-A2FE-9B54F43B7321
     * photo_path : http://120.24.208.159/gzqiche/images/bf_assign_work/58384B20-40FF-48DD-B786-C8D29F52A5CF.jpg
     */

    private String bs_repair_photo_id;
    private String photo_path;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Photo(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "{\"photo\":\""+photo+"\"}";
    }

    public String getBs_repair_photo_id() {
        return bs_repair_photo_id;
    }

    public void setBs_repair_photo_id(String bs_repair_photo_id) {
        this.bs_repair_photo_id = bs_repair_photo_id;
    }

    public String getPhoto_path() {
        return photo_path;
    }

    public void setPhoto_path(String photo_path) {
        this.photo_path = photo_path;
    }
}
