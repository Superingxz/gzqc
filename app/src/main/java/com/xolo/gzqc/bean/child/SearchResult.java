package com.xolo.gzqc.bean.child;

/**
 * Created by Administrator on 2016/12/12.
 */
public class SearchResult {
    /**
     * goods_name : 手套
     * pic_path : http://120.77.3.76/gzqiche/images/c_goodsinfor/D5A479CB-1AE0-4F4C-A4E1-C58F10207945.jpg
     * qty : 0
     * c_goodsinfor_id : SDFSADFF34534545
     * sales_price : 100.00
     */

    private String goods_name;
    private String pic_path;
    private String qty;
    private String c_goodsinfor_id;
    private String sales_price;
    /**
     * company_name : 广州鑫浪信息科技有限公司
     * c_supplierinfor_id : 9AD7DDDD-0CC2-4173-9251-6F5D5CDF9DB4
     */

    private String company_name;
    private String c_supplierinfor_id;


    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getPic_path() {
        return pic_path;
    }

    public void setPic_path(String pic_path) {
        this.pic_path = pic_path;
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
}
