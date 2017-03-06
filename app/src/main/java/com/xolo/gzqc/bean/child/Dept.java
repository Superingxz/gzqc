package com.xolo.gzqc.bean.child;

import java.io.Serializable;

/**
 * 部门
 */

public class Dept implements Serializable {
    String dept_name;
    String dept_id;
    boolean  isChecck;

    public Dept(String dept_name, String dept_id) {
        this.dept_name = dept_name;
        this.dept_id = dept_id;
    }

    public boolean isChecck() {
        return isChecck;
    }

    public void setChecck(boolean checck) {
        isChecck = checck;
    }

    public String getDept_name() {
        return dept_name;
    }

    public void setDept_name(String dept_name) {
        this.dept_name = dept_name;
    }

    public String getDept_id() {
        return dept_id;
    }

    public void setDept_id(String dept_id) {
        this.dept_id = dept_id;
    }
}
