package com.xolo.gzqc.bean.child;

/**
 * 角色
 */
public class Role {


    /**
     * role_id : jxstar089935
     * role_name : 连锁总部
     *
     *
     *
     * 100  维修厂车主
       90    维修厂生产班组
       95    维修厂接车
       99    维修厂采购
     */

    private String role_id;
    private String role_name;
    private String role_no;

    /**
     * user_role_id : 0BAC4418-E9A0-402F-A1ED-9E8FB2FE60C6
     */

    private String user_role_id;
    private boolean is_select;

    public boolean is_select() {
        return is_select;
    }

    public void setIs_select(boolean is_select) {
        this.is_select = is_select;
    }

    /**
     * role_memo : 负责收款的相关业务
     */



    private String role_memo;

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public String getRole_no() {
        return role_no;
    }

    public void setRole_no(String role_no) {
        this.role_no = role_no;
    }

    public String getUser_role_id() {
        return user_role_id;
    }

    public void setUser_role_id(String user_role_id) {
        this.user_role_id = user_role_id;
    }

    public String getRole_memo() {
        return role_memo;
    }

    public void setRole_memo(String role_memo) {
        this.role_memo = role_memo;
    }
}
