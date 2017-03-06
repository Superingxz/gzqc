package com.xolo.gzqc.bean.child;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/12.
 */
public class GoodMore {

    /**
     * control_id : jxstar0868985
     * goods : [{"pic_path":"http://120.77.3.76/gzqiche/images/c_goodsinfor/D5A479CB-1AE0-4F4C-A4E1-C58F10207945.jpg","goods_name":"手套","qty":"0","c_goodsinfor_id":"SDFSADFF34534545","sales_price":"100.00"}]
     * display_data : 汽车配件
     * value_data : f_qcyp_pj
     */

    private String control_id;
    private String display_data;
    private String value_data;
    /**
     * pic_path : http://120.77.3.76/gzqiche/images/c_goodsinfor/D5A479CB-1AE0-4F4C-A4E1-C58F10207945.jpg
     * goods_name : 手套
     * qty : 0
     * c_goodsinfor_id : SDFSADFF34534545
     * sales_price : 100.00
     */

    private List<GoodsBean> goods  = new ArrayList<>();

    public String getControl_id() {
        return control_id;
    }

    public void setControl_id(String control_id) {
        this.control_id = control_id;
    }

    public String getDisplay_data() {
        return display_data;
    }

    public void setDisplay_data(String display_data) {
        this.display_data = display_data;
    }

    public String getValue_data() {
        return value_data;
    }

    public void setValue_data(String value_data) {
        this.value_data = value_data;
    }

    public List<GoodsBean> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsBean> goods) {
        this.goods = goods;
    }

    public static class GoodsBean {
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
