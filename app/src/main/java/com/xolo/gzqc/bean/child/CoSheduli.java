package com.xolo.gzqc.bean.child;

/**
 * Created by Administrator on 2016/9/27.
 */
public class CoSheduli {
    String name;
    String num;
    String util_price;
    String totole_price;
    String state;

    public CoSheduli(String state, String name, String num, String util_price, String totole_price) {
        this.state = state;
        this.name = name;
        this.num = num;
        this.util_price = util_price;
        this.totole_price = totole_price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getUtil_price() {
        return util_price;
    }

    public void setUtil_price(String util_price) {
        this.util_price = util_price;
    }

    public String getTotole_price() {
        return totole_price;
    }

    public void setTotole_price(String totole_price) {
        this.totole_price = totole_price;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
