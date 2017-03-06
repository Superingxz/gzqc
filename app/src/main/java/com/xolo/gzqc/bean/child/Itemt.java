package com.xolo.gzqc.bean.child;

/**
 * Created by Administrator on 2016/11/17.
 */
public class Itemt {

    /**
     * itemt_name : 更换空调压缩机离合器
     * bf_repair_item_id : 05C21EFD-589F-407E-9748-D59FBF4BFCD1
     */

    private String itemt_name;
    private String bf_repair_item_id;

    public Itemt(String itemt_name, String bf_repair_item_id) {
        this.itemt_name = itemt_name;
        this.bf_repair_item_id = bf_repair_item_id;
    }

    public String getItemt_name() {
        return itemt_name;
    }

    public void setItemt_name(String itemt_name) {
        this.itemt_name = itemt_name;
    }

    public String getBf_repair_item_id() {
        return bf_repair_item_id;
    }

    public void setBf_repair_item_id(String bf_repair_item_id) {
        this.bf_repair_item_id = bf_repair_item_id;
    }
}
