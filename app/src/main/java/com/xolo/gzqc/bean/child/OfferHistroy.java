package com.xolo.gzqc.bean.child;

import com.xolo.gzqc.bean.postJson.Offer;

import java.util.ArrayList;
import java.util.List;

/**
 * 历史报价
 */
public class OfferHistroy {

    /**
     * bf_receive_car_id : 1FE33E31-68E7-4BCE-B115-660BB3E4D375
     * times_cnt : 1
     * owen_id : 978691E5-E3AC-4A26-96F7-EFDF28910712
     * carno : 粤A65236
     * operatdate : 2016-09-30 00:00:00
     */

    private String bf_receive_car_id;
    private String times_cnt;
    private String owen_id;
    private String carno;
    private String operatdate;
    private List<Offer>  offerList = new ArrayList<>();
    private boolean  isShow;
    private String totalsaleprice;
    private String totalprice;
    private String totalworkamt;

    public String getTotalsaleprice() {
        return totalsaleprice;
    }

    public void setTotalsaleprice(String totalsaleprice) {
        this.totalsaleprice = totalsaleprice;
    }

    public String getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }

    public String getTotalworkamt() {
        return totalworkamt;
    }

    public void setTotalworkamt(String totalworkamt) {
        this.totalworkamt = totalworkamt;
    }

    public String getBf_receive_car_id() {
        return bf_receive_car_id;
    }

    public void setBf_receive_car_id(String bf_receive_car_id) {
        this.bf_receive_car_id = bf_receive_car_id;
    }

    public String getTimes_cnt() {
        return times_cnt;
    }

    public void setTimes_cnt(String times_cnt) {
        this.times_cnt = times_cnt;
    }

    public String getOwen_id() {
        return owen_id;
    }

    public void setOwen_id(String owen_id) {
        this.owen_id = owen_id;
    }

    public String getCarno() {
        return carno;
    }

    public void setCarno(String carno) {
        this.carno = carno;
    }

    public String getOperatdate() {
        return operatdate;
    }

    public void setOperatdate(String operatdate) {
        this.operatdate = operatdate;
    }

    public List<Offer> getOfferList() {
        return offerList;
    }

    public void setOfferList(List<Offer> offerList) {
        this.offerList = offerList;
    }


    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }
}
