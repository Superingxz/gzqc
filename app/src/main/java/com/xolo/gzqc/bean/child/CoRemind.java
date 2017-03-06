package com.xolo.gzqc.bean.child;

/**
 * Created by Administrator on 2016/10/9.
 */
public class CoRemind {
    String is_know;
    String bf_warn_owner_id;
    String source_id;
    String is_pay;
    String content2;
    String warn_time;
    String warn_type;
    String operate_status;//增加返回参数 操作状态 operate_status 。操作状态如果为1，则为已操作，为0，则为未操作。如果是空，则不是报价提醒的

    public String getOperate_status() {
        return operate_status;
    }

    public void setOperate_status(String operate_status) {
        this.operate_status = operate_status;
    }

    public String getIs_know() {
        return is_know;
    }

    public void setIs_know(String is_know) {
        this.is_know = is_know;
    }

    public String getBf_warn_owner_id() {
        return bf_warn_owner_id;
    }

    public void setBf_warn_owner_id(String bf_warn_owner_id) {
        this.bf_warn_owner_id = bf_warn_owner_id;
    }

    public String getSource_id() {
        return source_id;
    }

    public void setSource_id(String source_id) {
        this.source_id = source_id;
    }

    public String getIs_pay() {
        return is_pay;
    }

    public void setIs_pay(String is_pay) {
        this.is_pay = is_pay;
    }

    public String getContent2() {
        return content2;
    }

    public void setContent2(String content2) {
        this.content2 = content2;
    }

    public String getWarn_time() {
        return warn_time;
    }

    public void setWarn_time(String warn_time) {
        this.warn_time = warn_time;
    }

    public String getWarn_type() {
        return warn_type;
    }

    public void setWarn_type(String warn_type) {
        this.warn_type = warn_type;
    }
}
