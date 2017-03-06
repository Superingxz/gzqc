package com.xolo.gzqc.bean.child;

/**
 * Created by Administrator on 2017/1/12.
 */

public class SumByType {
    private String mtype = "0";
    private String count_yy = "0";
    private String count_jc = "0";
    private String count_bj = "0";
    private String count_wxz = "0";
    private String count_wg = "0";
    private String count_js = "0";
    private String money_js = "0";
    private String money_wjs = "0";
    private String money_ss = "0";
    private String count_gj = "0";
    private String count_cg = "0";

    private int res;
    private String title;

    public SumByType(String title, int res) {
        this.title = title;
        this.res = res;
    }

    public String getCount_cg() {
        return count_cg;
    }

    public void setCount_cg(String count_cg) {
        this.count_cg = count_cg;
    }

    public String getCount_gj() {
        return count_gj;
    }

    public void setCount_gj(String count_gj) {
        this.count_gj = count_gj;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public String getMtype() {
        return mtype;
    }

    public void setMtype(String mtype) {
        this.mtype = mtype;
    }

    public String getCount_yy() {
        return count_yy;
    }

    public void setCount_yy(String count_yy) {
        this.count_yy = count_yy;
    }

    public String getCount_jc() {
        return count_jc;
    }

    public void setCount_jc(String count_jc) {
        this.count_jc = count_jc;
    }

    public String getCount_bj() {
        return count_bj;
    }

    public void setCount_bj(String count_bj) {
        this.count_bj = count_bj;
    }

    public String getCount_wxz() {
        return count_wxz;
    }

    public void setCount_wxz(String count_wxz) {
        this.count_wxz = count_wxz;
    }

    public String getCount_wg() {
        return count_wg;
    }

    public void setCount_wg(String count_wg) {
        this.count_wg = count_wg;
    }

    public String getCount_js() {
        return count_js;
    }

    public void setCount_js(String count_js) {
        this.count_js = count_js;
    }

    public String getMoney_js() {
        return money_js;
    }

    public void setMoney_js(String money_js) {
        this.money_js = money_js;
    }

    public String getMoney_wjs() {
        return money_wjs;
    }

    public void setMoney_wjs(String money_wjs) {
        this.money_wjs = money_wjs;
    }

    public String getMoney_ss() {
        return money_ss;
    }

    public void setMoney_ss(String money_ss) {
        this.money_ss = money_ss;
    }
}
