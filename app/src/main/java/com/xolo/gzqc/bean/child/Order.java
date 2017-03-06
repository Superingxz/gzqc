package com.xolo.gzqc.bean.child;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/15.
 */
public class Order implements Parcelable {
    /**
     * order_number : O2016121514471701
     * total : 30240.00
     * phone : 13570401327
     * emsno :
     * link_name : 钱少
     * goods : [{"goodsname":"手枪","goodsid":"CC7CBB41-92EB-4DEA-8C10-CB6D8A2BF5B0","pic_path":"","did":"401FF553-D69C-4494-A1D2-B2365E0EC7A1","qty":"1","mid":"06B7DDA2-DCFA-4685-BEC5-FD90FD08A339","factprice":"30000.00"},{"goodsname":"手套","goodsid":"SDFSADFF34534545","pic_path":"","did":"4D6A3980-ECE6-46BF-874E-51E77FA3F7A5","qty":"3","mid":"06B7DDA2-DCFA-4685-BEC5-FD90FD08A339","factprice":"80.00"}]
     * supperid : 9AD7DDDD-0CC2-4173-9251-6F5D5CDF9DB4
     * c_order_m_id : 06B7DDA2-DCFA-4685-BEC5-FD90FD08A339
     * clientname : 钱多多
     * link_tel : 13570401327
     * fee : 0.00
     * emscompany :
     * company_name : 广州鑫浪信息科技有限公司
     * address : 广东省广州市海珠区新港东路世港国际A栋805室
     * clienttel : 13570401327
     * operatdate : 2016-12-15 14:47:17
     */

    private String order_number;
    private String total;
    private String phone;
    private String emsno;
    private String link_name;
    private String supperid;
    private String c_order_m_id;
    private String clientname;
    private String link_tel;
    private String fee;
    private String emscompany;
    private String company_name;
    private String address;
    private String clienttel;
    private String operatdate;
    private int qty_total;
    private double price_total;
    private String status;
    /**
     * goodsname : 手枪
     * goodsid : CC7CBB41-92EB-4DEA-8C10-CB6D8A2BF5B0
     * pic_path :
     * did : 401FF553-D69C-4494-A1D2-B2365E0EC7A1
     * qty : 1
     * mid : 06B7DDA2-DCFA-4685-BEC5-FD90FD08A339
     * factprice : 30000.00
     */

    private List<GoodsBean> goods;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmsno() {
        return emsno;
    }

    public void setEmsno(String emsno) {
        this.emsno = emsno;
    }

    public String getLink_name() {
        return link_name;
    }

    public void setLink_name(String link_name) {
        this.link_name = link_name;
    }

    public String getSupperid() {
        return supperid;
    }

    public void setSupperid(String supperid) {
        this.supperid = supperid;
    }

    public String getC_order_m_id() {
        return c_order_m_id;
    }

    public void setC_order_m_id(String c_order_m_id) {
        this.c_order_m_id = c_order_m_id;
    }

    public String getClientname() {
        return clientname;
    }

    public void setClientname(String clientname) {
        this.clientname = clientname;
    }

    public String getLink_tel() {
        return link_tel;
    }

    public void setLink_tel(String link_tel) {
        this.link_tel = link_tel;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getEmscompany() {
        return emscompany;
    }

    public void setEmscompany(String emscompany) {
        this.emscompany = emscompany;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getClienttel() {
        return clienttel;
    }

    public void setClienttel(String clienttel) {
        this.clienttel = clienttel;
    }

    public String getOperatdate() {
        return operatdate;
    }

    public void setOperatdate(String operatdate) {
        this.operatdate = operatdate;
    }

    public List<GoodsBean> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsBean> goods) {
        this.goods = goods;
    }

    public static class GoodsBean implements Parcelable {
        private String goodsname;
        private String goodsid;
        private String pic_path;
        private String did;
        private String qty;
        private String mid;
        private String factprice;
        private String comment;

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getGoodsname() {
            return goodsname;
        }

        public void setGoodsname(String goodsname) {
            this.goodsname = goodsname;
        }

        public String getGoodsid() {
            return goodsid;
        }

        public void setGoodsid(String goodsid) {
            this.goodsid = goodsid;
        }

        public String getPic_path() {
            return pic_path;
        }

        public void setPic_path(String pic_path) {
            this.pic_path = pic_path;
        }

        public String getDid() {
            return did;
        }

        public void setDid(String did) {
            this.did = did;
        }

        public String getQty() {
            return qty;
        }

        public void setQty(String qty) {
            this.qty = qty;
        }

        public String getMid() {
            return mid;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }

        public String getFactprice() {
            return factprice;
        }

        public void setFactprice(String factprice) {
            this.factprice = factprice;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.goodsname);
            dest.writeString(this.goodsid);
            dest.writeString(this.pic_path);
            dest.writeString(this.did);
            dest.writeString(this.qty);
            dest.writeString(this.mid);
            dest.writeString(this.factprice);
        }

        public GoodsBean() {
        }

        protected GoodsBean(Parcel in) {
            this.goodsname = in.readString();
            this.goodsid = in.readString();
            this.pic_path = in.readString();
            this.did = in.readString();
            this.qty = in.readString();
            this.mid = in.readString();
            this.factprice = in.readString();
        }

        public static final Creator<GoodsBean> CREATOR = new Creator<GoodsBean>() {
            @Override
            public GoodsBean createFromParcel(Parcel source) {
                return new GoodsBean(source);
            }

            @Override
            public GoodsBean[] newArray(int size) {
                return new GoodsBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.order_number);
        dest.writeString(this.total);
        dest.writeString(this.phone);
        dest.writeString(this.emsno);
        dest.writeString(this.link_name);
        dest.writeString(this.supperid);
        dest.writeString(this.c_order_m_id);
        dest.writeString(this.clientname);
        dest.writeString(this.link_tel);
        dest.writeString(this.fee);
        dest.writeString(this.emscompany);
        dest.writeString(this.company_name);
        dest.writeString(this.address);
        dest.writeString(this.clienttel);
        dest.writeString(this.operatdate);
        dest.writeInt(this.qty_total);
        dest.writeDouble(this.price_total);
        dest.writeString(this.status);
        dest.writeList(this.goods);
    }

    public Order() {
    }

    protected Order(Parcel in) {
        this.order_number = in.readString();
        this.total = in.readString();
        this.phone = in.readString();
        this.emsno = in.readString();
        this.link_name = in.readString();
        this.supperid = in.readString();
        this.c_order_m_id = in.readString();
        this.clientname = in.readString();
        this.link_tel = in.readString();
        this.fee = in.readString();
        this.emscompany = in.readString();
        this.company_name = in.readString();
        this.address = in.readString();
        this.clienttel = in.readString();
        this.operatdate = in.readString();
        this.qty_total = in.readInt();
        this.price_total = in.readDouble();
        this.status = in.readString();
        this.goods = new ArrayList<GoodsBean>();
        in.readList(this.goods, GoodsBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<Order> CREATOR = new Parcelable.Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel source) {
            return new Order(source);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
}
