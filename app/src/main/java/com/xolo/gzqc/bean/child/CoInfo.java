package com.xolo.gzqc.bean.child;

import java.util.List;

/**
 * 车主个人信息
 * Created by Administrator on 2016/9/28.
 */
public class CoInfo {


    /**
     * sex : 0
     * phone : 13058581234
     * street :
     * areaid :
     * phone2 :
     * provinceid :
     * pic : [{"photo_sort":"1","bs_repair_photo_id":"705ADDC7-BF30-4924-9DBB-47E4406C1E6B","photo_path":"http://120.77.3.76/gzqiche/images/bc_car_owner/51F1AA25-4204-4969-A953-880AE890DF1F.jpg"},{"photo_sort":"2","bs_repair_photo_id":"FFC77F39-F2DE-4590-8F5A-5B5834589E93","photo_path":"http://120.77.3.76/gzqiche/images/bc_car_owner/9E3AD652-0934-4FB9-B2CB-F6C29D1D5DB9.jpg"}]
     * factory_name : 顺达汽修一厂
     * photo :
     * city :
     * cityid :
     * nick_name :
     * factory_id : 100400020001
     * area :
     * bc_car_owner_id : 089D0C48-0A27-49A1-8757-54852C6F2399
     * name : 令狐冲
     * province :
     */

    private String sex;
    private String phone;
    private String street;
    private String areaid;
    private String phone2;
    private String provinceid;
    private String factory_name;
    private String photo;
    private String city;
    private String cityid;
    private String nick_name;
    private String factory_id;
    private String area;
    private String bc_car_owner_id;
    private String name;
    private String province;
    private List<PicBean> pic;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAreaid() {
        return areaid;
    }

    public void setAreaid(String areaid) {
        this.areaid = areaid;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getProvinceid() {
        return provinceid;
    }

    public void setProvinceid(String provinceid) {
        this.provinceid = provinceid;
    }

    public String getFactory_name() {
        return factory_name;
    }

    public void setFactory_name(String factory_name) {
        this.factory_name = factory_name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getFactory_id() {
        return factory_id;
    }

    public void setFactory_id(String factory_id) {
        this.factory_id = factory_id;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getBc_car_owner_id() {
        return bc_car_owner_id;
    }

    public void setBc_car_owner_id(String bc_car_owner_id) {
        this.bc_car_owner_id = bc_car_owner_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public List<PicBean> getPic() {
        return pic;
    }

    public void setPic(List<PicBean> pic) {
        this.pic = pic;
    }

    public static class PicBean {
        /**
         * photo_sort : 1
         * bs_repair_photo_id : 705ADDC7-BF30-4924-9DBB-47E4406C1E6B
         * photo_path : http://120.77.3.76/gzqiche/images/bc_car_owner/51F1AA25-4204-4969-A953-880AE890DF1F.jpg
         */

        private String photo_sort;
        private String bs_repair_photo_id;
        private String photo_path;

        public String getPhoto_sort() {
            return photo_sort;
        }

        public void setPhoto_sort(String photo_sort) {
            this.photo_sort = photo_sort;
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
}
