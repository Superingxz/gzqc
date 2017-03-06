package com.xolo.gzqc.bean.child;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/7.
 */
public class WordBeachDealed {

    /**
     * brands : 捷达
     * typecode : 7160（AT）
     * carno : 粤A8J113
     * deallist : [{"in_time":"2016-12-05 16:28:38","status":"维修中","carno":"粤A8J113","bc_car_info_id":"44847485-EA33-46FF-9627-1C48DBD5DA5A","is_unsettled":"0","bf_receive_id":"A03B7F28-7445-4D05-B613-E5DA94B1298F","type":"处理派工","deal_time":"2016-12-05 16:39:49"},{"in_time":"2016-12-05 11:10:32","status":"已交车","carno":"粤A8J113","bc_car_info_id":"44847485-EA33-46FF-9627-1C48DBD5DA5A","is_unsettled":"0","bf_receive_id":"2FC3A5C0-B325-4D46-BE9B-AC26290DDA7E","type":"处理派工","deal_time":"2016-12-05 11:44:29"}]
     */

    private String brands;
    private String typecode;
    private String carno;
    private String brands_path;

    public String getBrands_path() {
        return brands_path;
    }

    public void setBrands_path(String brands_path) {
        this.brands_path = brands_path;
    }
    /**
     * in_time : 2016-12-05 16:28:38
     * status : 维修中
     * carno : 粤A8J113
     * bc_car_info_id : 44847485-EA33-46FF-9627-1C48DBD5DA5A
     * is_unsettled : 0
     * bf_receive_id : A03B7F28-7445-4D05-B613-E5DA94B1298F
     * type : 处理派工
     * deal_time : 2016-12-05 16:39:49
     */

    private List<DeallistBean> deallist = new ArrayList<>();

    public String getBrands() {
        return brands;
    }

    public void setBrands(String brands) {
        this.brands = brands;
    }

    public String getTypecode() {
        return typecode;
    }

    public void setTypecode(String typecode) {
        this.typecode = typecode;
    }

    public String getCarno() {
        return carno;
    }

    public void setCarno(String carno) {
        this.carno = carno;
    }

    public List<DeallistBean> getDeallist() {
        return deallist;
    }

    public void setDeallist(List<DeallistBean> deallist) {
        this.deallist = deallist;
    }

    public static class DeallistBean {
        private String in_time;
        private String status;
        private String carno;
        private String bc_car_info_id;
        private String is_unsettled;
        private String bf_receive_id;
        private String type;
        private String deal_time;

        public String getIn_time() {
            return in_time;
        }

        public void setIn_time(String in_time) {
            this.in_time = in_time;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCarno() {
            return carno;
        }

        public void setCarno(String carno) {
            this.carno = carno;
        }

        public String getBc_car_info_id() {
            return bc_car_info_id;
        }

        public void setBc_car_info_id(String bc_car_info_id) {
            this.bc_car_info_id = bc_car_info_id;
        }

        public String getIs_unsettled() {
            return is_unsettled;
        }

        public void setIs_unsettled(String is_unsettled) {
            this.is_unsettled = is_unsettled;
        }

        public String getBf_receive_id() {
            return bf_receive_id;
        }

        public void setBf_receive_id(String bf_receive_id) {
            this.bf_receive_id = bf_receive_id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDeal_time() {
            return deal_time;
        }

        public void setDeal_time(String deal_time) {
            this.deal_time = deal_time;
        }
    }
}
