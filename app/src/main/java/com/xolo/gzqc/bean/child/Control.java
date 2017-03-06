package com.xolo.gzqc.bean.child;

/**
 *根据类别获取对应的数据字典接口。1为燃料，2为操控，3为驱动，4为排量 ,5为油量
 */
public class Control {


    public Control(String control_id, String display_data) {
        this.control_id = control_id;
        this.display_data = display_data;
    }

    /**
     * control_id : jxstar0797581
     * display_data : 汽油
     */


    private String control_id;
    private String display_data;

    public String getControl_id() {
        return control_id;
    }

    public void setControl_id(String control_id) {
        this.control_id = control_id;
    }

    public String getDisplay_data() {
        return display_data;
    }

    public void setDisplay_data(String display_data) {
        this.display_data = display_data;
    }
}
