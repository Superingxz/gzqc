package com.xolo.gzqc.bean.child;

import com.xolo.gzqc.bean.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/13.
 */
public class GoodInfo  extends BaseBean{


    /**
     * comment_count : 5
     * goods_description : 这可是真枪啊
     * total_qty : 5
     * supplier_name : 广州鑫浪信息科技有限公司
     * specification_model : 支
     * first_value_data :
     * goods_name : 手枪
     * c_supplierinfor_id : 9AD7DDDD-0CC2-4173-9251-6F5D5CDF9DB4
     * c_goodsinfor_id : CC7CBB41-92EB-4DEA-8C10-CB6D8A2BF5B0
     * supplier_phone : 13570401327
     * first_type_id : 132
     * second_type : 枪支
     * sales_price : 30000.00
     * topimage : [{"pic_path":"http://120.77.3.76/gzqiche/images/c_pic/D5A479CB-1AE0-4F4C-A4E1-C58F10207945.jpg"}]
     * second_value_data :
     * second_type_id : 222
     * pic_2 : http://120.77.3.76/gzqiche/images/c_goodsinfor/1BBBB352-E913-4D6C-AED7-82E45D2CD248.jpg
     * pic_1 : http://120.77.3.76/gzqiche/images/c_goodsinfor/EC34F349-DEDA-458E-B074-B0F2597098C0.jpg
     * first_type : 武器
     * pic_3 : http://120.77.3.76/gzqiche/images/c_goodsinfor/D8E754A1-BFF4-4AD8-B7EF-A07BF224DD1F.jpg
     * comment : [{"head_portrait":"http://120.77.3.76/gzqiche/resources/images/default.png","comment_name":"","comment_content":"嘿嘿，小孩子玩意","comment_phone":"","c_goodsinfor_id":"CC7CBB41-92EB-4DEA-8C10-CB6D8A2BF5B0","comment_time":"2016-12-13 10:42:23","c_order_comment_id":"382EC2BC-499C-4BAB-B9DB-DB726E131676"},{"head_portrait":"http://120.77.3.76/gzqiche/resources/images/default.png","comment_name":"","comment_content":"妈呀，这什么玩意，假货","comment_phone":"","c_goodsinfor_id":"CC7CBB41-92EB-4DEA-8C10-CB6D8A2BF5B0","comment_time":"2016-12-13 10:42:05","c_order_comment_id":"3F32E839-AD39-48EF-B270-D237A8D9BD19"},{"head_portrait":"http://120.77.3.76/gzqiche/resources/images/default.png","comment_name":"","comment_content":"妈的，还炸膛的，假货","comment_phone":"","c_goodsinfor_id":"CC7CBB41-92EB-4DEA-8C10-CB6D8A2BF5B0","comment_time":"2016-12-13 10:41:42","c_order_comment_id":"54E8043A-5F8D-4B46-8EF6-E19304CADAE4"},{"head_portrait":"http://120.77.3.76/gzqiche/resources/images/default.png","comment_name":"","comment_content":"靠，不能用，假货","comment_phone":"","c_goodsinfor_id":"CC7CBB41-92EB-4DEA-8C10-CB6D8A2BF5B0","comment_time":"2016-12-13 10:41:17","c_order_comment_id":"49E5E1D6-EF2F-4512-8971-885A5F7943AB"},{"head_portrait":"http://120.77.3.76/gzqiche/resources/images/default.png","comment_name":"","comment_content":"能用，呵呵","comment_phone":"","c_goodsinfor_id":"CC7CBB41-92EB-4DEA-8C10-CB6D8A2BF5B0","comment_time":"2016-12-13 10:39:48","c_order_comment_id":"C6E059F4-6DD5-46B3-96DF-37C6AA02E6D1"}]
     */

    private String comment_count;
    private String goods_description;
    private String total_qty;
    private String supplier_name;
    private String specification_model;
    private String first_value_data;
    private String goods_name;
    private String c_supplierinfor_id;
    private String c_goodsinfor_id;
    private String supplier_phone;
    private String first_type_id;
    private String second_type;
    private String sales_price;
    private String second_value_data;
    private String second_type_id;
    private String pic_2;
    private String pic_1;
    private String first_type;
    private String pic_3;
    private String price;
    /**
     * pic_path : http://120.77.3.76/gzqiche/images/c_pic/D5A479CB-1AE0-4F4C-A4E1-C58F10207945.jpg
     */

    private List<TopimageBean> topimage = new ArrayList<>();

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    /**
     * head_portrait : http://120.77.3.76/gzqiche/resources/images/default.png
     * comment_name :
     * comment_content : 嘿嘿，小孩子玩意
     * comment_phone :
     * c_goodsinfor_id : CC7CBB41-92EB-4DEA-8C10-CB6D8A2BF5B0
     * comment_time : 2016-12-13 10:42:23
     * c_order_comment_id : 382EC2BC-499C-4BAB-B9DB-DB726E131676
     */



    private List<CommentBean> comment = new ArrayList<>();

    public String getComment_count() {
        return comment_count;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    public String getGoods_description() {
        return goods_description;
    }

    public void setGoods_description(String goods_description) {
        this.goods_description = goods_description;
    }

    public String getTotal_qty() {
        return total_qty;
    }

    public void setTotal_qty(String total_qty) {
        this.total_qty = total_qty;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public String getSpecification_model() {
        return specification_model;
    }

    public void setSpecification_model(String specification_model) {
        this.specification_model = specification_model;
    }

    public String getFirst_value_data() {
        return first_value_data;
    }

    public void setFirst_value_data(String first_value_data) {
        this.first_value_data = first_value_data;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getC_supplierinfor_id() {
        return c_supplierinfor_id;
    }

    public void setC_supplierinfor_id(String c_supplierinfor_id) {
        this.c_supplierinfor_id = c_supplierinfor_id;
    }

    public String getC_goodsinfor_id() {
        return c_goodsinfor_id;
    }

    public void setC_goodsinfor_id(String c_goodsinfor_id) {
        this.c_goodsinfor_id = c_goodsinfor_id;
    }

    public String getSupplier_phone() {
        return supplier_phone;
    }

    public void setSupplier_phone(String supplier_phone) {
        this.supplier_phone = supplier_phone;
    }

    public String getFirst_type_id() {
        return first_type_id;
    }

    public void setFirst_type_id(String first_type_id) {
        this.first_type_id = first_type_id;
    }

    public String getSecond_type() {
        return second_type;
    }

    public void setSecond_type(String second_type) {
        this.second_type = second_type;
    }

    public String getSales_price() {
        return sales_price;
    }

    public void setSales_price(String sales_price) {
        this.sales_price = sales_price;
    }

    public String getSecond_value_data() {
        return second_value_data;
    }

    public void setSecond_value_data(String second_value_data) {
        this.second_value_data = second_value_data;
    }

    public String getSecond_type_id() {
        return second_type_id;
    }

    public void setSecond_type_id(String second_type_id) {
        this.second_type_id = second_type_id;
    }

    public String getPic_2() {
        return pic_2;
    }

    public void setPic_2(String pic_2) {
        this.pic_2 = pic_2;
    }

    public String getPic_1() {
        return pic_1;
    }

    public void setPic_1(String pic_1) {
        this.pic_1 = pic_1;
    }

    public String getFirst_type() {
        return first_type;
    }

    public void setFirst_type(String first_type) {
        this.first_type = first_type;
    }

    public String getPic_3() {
        return pic_3;
    }

    public void setPic_3(String pic_3) {
        this.pic_3 = pic_3;
    }

    public List<TopimageBean> getTopimage() {
        return topimage;
    }

    public void setTopimage(List<TopimageBean> topimage) {
        this.topimage = topimage;
    }

    public List<CommentBean> getComment() {
        return comment;
    }

    public void setComment(List<CommentBean> comment) {
        this.comment = comment;
    }

    public static class TopimageBean {
        private String pic_path;

        public String getPic_path() {
            return pic_path;
        }

        public void setPic_path(String pic_path) {
            this.pic_path = pic_path;
        }
    }

    public static class CommentBean {
        private String head_portrait;
        private String comment_name;
        private String comment_content;
        private String comment_phone;
        private String c_goodsinfor_id;
        private String comment_time;
        private String c_order_comment_id;

        public String getHead_portrait() {
            return head_portrait;
        }

        public void setHead_portrait(String head_portrait) {
            this.head_portrait = head_portrait;
        }

        public String getComment_name() {
            return comment_name;
        }

        public void setComment_name(String comment_name) {
            this.comment_name = comment_name;
        }

        public String getComment_content() {
            return comment_content;
        }

        public void setComment_content(String comment_content) {
            this.comment_content = comment_content;
        }

        public String getComment_phone() {
            return comment_phone;
        }

        public void setComment_phone(String comment_phone) {
            this.comment_phone = comment_phone;
        }

        public String getC_goodsinfor_id() {
            return c_goodsinfor_id;
        }

        public void setC_goodsinfor_id(String c_goodsinfor_id) {
            this.c_goodsinfor_id = c_goodsinfor_id;
        }

        public String getComment_time() {
            return comment_time;
        }

        public void setComment_time(String comment_time) {
            this.comment_time = comment_time;
        }

        public String getC_order_comment_id() {
            return c_order_comment_id;
        }

        public void setC_order_comment_id(String c_order_comment_id) {
            this.c_order_comment_id = c_order_comment_id;
        }
    }
}
