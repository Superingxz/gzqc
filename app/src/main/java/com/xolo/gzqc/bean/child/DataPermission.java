package com.xolo.gzqc.bean.child;

/**
 * Created by Administrator on 2017/2/21.
 */

public class DataPermission {

    /**
     * dtype_id : jxstar882105
     * dtype_name : 维修厂查看车主权限
     * user_data_id : 6165DC71-2C6B-4AD4-9E0A-216B476272C1
     * display : 淘宝汽修厂
     * has_sub : 1
     * dtype_data : 100400070001
     */

    private String dtype_id;
    private String dtype_name;
    private String user_data_id;
    private String display;
    private String has_sub;
    private String dtype_data;
    private boolean is_select;

    public boolean is_select() {
        return is_select;
    }

    public void setIs_select(boolean is_select) {
        this.is_select = is_select;
    }

    public String getDtype_id() {
        return dtype_id;
    }

    public void setDtype_id(String dtype_id) {
        this.dtype_id = dtype_id;
    }

    public String getDtype_name() {
        return dtype_name;
    }

    public void setDtype_name(String dtype_name) {
        this.dtype_name = dtype_name;
    }

    public String getUser_data_id() {
        return user_data_id;
    }

    public void setUser_data_id(String user_data_id) {
        this.user_data_id = user_data_id;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getHas_sub() {
        return has_sub;
    }

    public void setHas_sub(String has_sub) {
        this.has_sub = has_sub;
    }

    public String getDtype_data() {
        return dtype_data;
    }

    public void setDtype_data(String dtype_data) {
        this.dtype_data = dtype_data;
    }
}
