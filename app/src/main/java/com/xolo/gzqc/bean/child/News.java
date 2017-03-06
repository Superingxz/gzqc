package com.xolo.gzqc.bean.child;

/**
 * 车主提醒
 */
public class News {

    /**
     * phone : 13812345678
     * name : 张三
     * bf_warn_id : 8240674F-E21F-4F0D-BB8E-974B1B76D759
     * content2 : 100
     * phone2 : 13552367891
     */

    private String phone;
    private String name;
    private String bf_warn_id;
    private String content2;
    private String phone2;
    private boolean isSelect;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBf_warn_id() {
        return bf_warn_id;
    }

    public void setBf_warn_id(String bf_warn_id) {
        this.bf_warn_id = bf_warn_id;
    }

    public String getContent2() {
        return content2;
    }

    public void setContent2(String content2) {
        this.content2 = content2;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
