package com.xolo.gzqc.bean;

/**
 * Created by sa on 2016/4/27.
 */
public class BaseBean<T> extends DetailBean<T>{
    private String msg;
    private String res;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    @Override
//    {"data":[{"type_id":"jxstar0687731","type_name":"外观","description":"车身右侧有撞击痕迹","photo":""},
//        {"type_id":"jxstar8497732","type_name":"内饰","description":"有明显刮痕","photo":""}]}
    public String toString() {
        return "BaseBean{" +
                "msg='" + getData() + '\'' +
                ", res='" + res + '\'' +
                '}';
    }
}
