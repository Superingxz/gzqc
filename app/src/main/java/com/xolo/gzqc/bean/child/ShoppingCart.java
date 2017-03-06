package com.xolo.gzqc.bean.child;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/12.
 */
public class ShoppingCart implements Parcelable {

    /**
     * phone : 13570401327
     * company_name : 广州鑫浪信息科技有限公司
     * c_salecar_m_id : 9714DC12-6EAF-4DA2-A085-67CC9F8D65A1
     * salecar_goods : [{"unit":"双","pic_path":"http://120.77.3.76/gzqiche/images/c_goodsinfor/D5A479CB-1AE0-4F4C-A4E1-C58F10207945.jpg","goods_name":"手套","qty":"3","c_goodsinfor_id":"SDFSADFF34534545","sales_price":"80.00"}]
     * c_supplierinfor_id : 9AD7DDDD-0CC2-4173-9251-6F5D5CDF9DB4
     */

    private String phone;
    private String company_name;
    private String c_salecar_m_id = "";
    private String c_supplierinfor_id;
    private String specification_model;
    private String price;
    private int qty_total;
    private double price_total;
    /**
     * unit : 双
     * pic_path : http://120.77.3.76/gzqiche/images/c_goodsinfor/D5A479CB-1AE0-4F4C-A4E1-C58F10207945.jpg
     * goods_name : 手套
     * qty : 3
     * c_goodsinfor_id : SDFSADFF34534545
     * sales_price : 80.00
     */

    private List<SalecarGoodsBean> salecar_goods = new ArrayList<>();

    public ShoppingCart(String company_name, String c_supplierinfor_id, List<SalecarGoodsBean> salecar_goods) {
        this.company_name = company_name;
        this.c_supplierinfor_id = c_supplierinfor_id;
        this.salecar_goods = salecar_goods;
    }

    public String getSpecification_model() {
        return specification_model;
    }

    public void setSpecification_model(String specification_model) {
        this.specification_model = specification_model;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQty_total() {
        return qty_total;
    }

    public void setQty_total(int qty_total) {
        this.qty_total = qty_total;
    }

    public double getPrice_total() {
        return price_total;
    }

    public void setPrice_total(double price_total) {
        this.price_total = price_total;
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

    public String getC_salecar_m_id() {
        return c_salecar_m_id;
    }

    public void setC_salecar_m_id(String c_salecar_m_id) {
        this.c_salecar_m_id = c_salecar_m_id;
    }

    public String getC_supplierinfor_id() {
        return c_supplierinfor_id;
    }

    public void setC_supplierinfor_id(String c_supplierinfor_id) {
        this.c_supplierinfor_id = c_supplierinfor_id;
    }

    public List<SalecarGoodsBean> getSalecar_goods() {
        return salecar_goods;
    }

    public void setSalecar_goods(List<SalecarGoodsBean> salecar_goods) {
        this.salecar_goods = salecar_goods;
    }

    public static class SalecarGoodsBean implements Parcelable {
        private String unit;
        private String pic_path;
        private String goods_name;
        private String qty;
        private String c_goodsinfor_id;
        private String sales_price;
        /**
         * price : 100.00
         * specification_model : 3-2
         */
        private String price;
        private String specification_model;



        public SalecarGoodsBean(String unit, String goods_name, String qty, String sales_price, String price, String specification_model, String pic_path, String c_goodsinfor_id) {
            this.unit = unit;
            this.goods_name = goods_name;
            this.qty = qty;
            this.sales_price = sales_price;
            this.price = price;
            this.specification_model = specification_model;
            this.pic_path = pic_path;
            this.c_goodsinfor_id = c_goodsinfor_id;
        }

        @Override
        public String toString() {
            return "{\"c_goodsinfor_id\":\""+c_goodsinfor_id+"\",\"goods_name\":\""+goods_name+"\",\"specification_model\":\""+specification_model+"\",\"unit\":\""+unit+"\",\"price\":\""+price+"\",\"sales_price\":\""+sales_price+"\",\"qty\":\""+qty+"\"}";
        }



        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getSpecification_model() {
            return specification_model;
        }

        public void setSpecification_model(String specification_model) {
            this.specification_model = specification_model;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.unit);
            dest.writeString(this.pic_path);
            dest.writeString(this.goods_name);
            dest.writeString(this.qty);
            dest.writeString(this.c_goodsinfor_id);
            dest.writeString(this.sales_price);
            dest.writeString(this.price);
            dest.writeString(this.specification_model);
        }

        protected SalecarGoodsBean(Parcel in) {
            this.unit = in.readString();
            this.pic_path = in.readString();
            this.goods_name = in.readString();
            this.qty = in.readString();
            this.c_goodsinfor_id = in.readString();
            this.sales_price = in.readString();
            this.price = in.readString();
            this.specification_model = in.readString();
        }

        public static final Creator<SalecarGoodsBean> CREATOR = new Creator<SalecarGoodsBean>() {
            @Override
            public SalecarGoodsBean createFromParcel(Parcel source) {
                return new SalecarGoodsBean(source);
            }

            @Override
            public SalecarGoodsBean[] newArray(int size) {
                return new SalecarGoodsBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.phone);
        dest.writeString(this.company_name);
        dest.writeString(this.c_salecar_m_id);
        dest.writeString(this.c_supplierinfor_id);
        dest.writeList(this.salecar_goods);
    }

    public ShoppingCart() {
    }

    protected ShoppingCart(Parcel in) {
        this.phone = in.readString();
        this.company_name = in.readString();
        this.c_salecar_m_id = in.readString();
        this.c_supplierinfor_id = in.readString();
        this.salecar_goods = new ArrayList<SalecarGoodsBean>();
        in.readList(this.salecar_goods, SalecarGoodsBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<ShoppingCart> CREATOR = new Parcelable.Creator<ShoppingCart>() {
        @Override
        public ShoppingCart createFromParcel(Parcel source) {
            return new ShoppingCart(source);
        }

        @Override
        public ShoppingCart[] newArray(int size) {
            return new ShoppingCart[size];
        }
    };


    /**
     * {"data":[{"c_supplierinfor_id":"9AD7DDDD-0CC2-4173-9251-6F5D5CDF9DB4","goods":[{"c_goodsinfor_id":"SDFSADFF34534545","goods_name":"手套","specification_model":"3-2","unit":"双","price":"50.00","sales_price":"80.00","qty":"3"},{"c_goodsinfor_id":"CC7CBB41-92EB-4DEA-8C10-CB6D8A2BF5B0","goods_name":"手枪",
     * "specification_model":"支","unit":"支","price":"30000.00","sales_price":"30000.00","qty":"1"}]}]}
     * @return
     */
    @Override
    public String toString() {
        return "{\"c_supplierinfor_id\":\""+c_supplierinfor_id+"\",\"goods\":"+salecar_goods.toString()+"}";
    }

}
