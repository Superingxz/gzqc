package com.xolo.gzqc.bean.child;

import java.util.List;

/**
 * Created by Administrator on 2016/12/16.
 */
public class Store {

    /**
     * content : [{"pic_path":"","goods_name":"手套","qty":"21","c_goodsinfor_id":"SDFSADFF34534545","sales_price":"100.00"},{"pic_path":"","goods_name":"手枪","qty":"7","c_goodsinfor_id":"CC7CBB41-92EB-4DEA-8C10-CB6D8A2BF5B0","sales_price":"30000.00"}]
     * phone : 13570401327
     * company_name : 广州鑫浪信息科技有限公司
     * c_supplierinfor_id : 9AD7DDDD-0CC2-4173-9251-6F5D5CDF9DB4
     */

    private String phone;
    private String company_name;
    private String c_supplierinfor_id;
    private String announcement;
    /**
     * pic_path :
     * goods_name : 手套
     * qty : 21
     * c_goodsinfor_id : SDFSADFF34534545
     * sales_price : 100.00
     */

    private List<ContentBean> goods;

    public String getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(String announcement) {
        this.announcement = announcement;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getC_supplierinfor_id() {
        return c_supplierinfor_id;
    }

    public void setC_supplierinfor_id(String c_supplierinfor_id) {
        this.c_supplierinfor_id = c_supplierinfor_id;
    }

    public List<ContentBean> getContent() {
        return goods;
    }

    public void setContent(List<ContentBean> content) {
        this.goods = content;
    }

    public static class ContentBean {
        private String pic_path;
        private String goods_name;
        private String qty;
        private String c_goodsinfor_id;
        private String sales_price;

        public String getPic_path() {
            return pic_path;
        }

        public void setPic_path(String pic_path) {
            this.pic_path = pic_path;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getQty() {
            return qty;
        }

        public void setQty(String qty) {
            this.qty = qty;
        }

        public String getC_goodsinfor_id() {
            return c_goodsinfor_id;
        }

        public void setC_goodsinfor_id(String c_goodsinfor_id) {
            this.c_goodsinfor_id = c_goodsinfor_id;
        }

        public String getSales_price() {
            return sales_price;
        }

        public void setSales_price(String sales_price) {
            this.sales_price = sales_price;
        }
    }
}
