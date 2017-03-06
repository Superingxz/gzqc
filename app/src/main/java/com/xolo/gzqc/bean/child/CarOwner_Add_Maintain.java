package com.xolo.gzqc.bean.child;

import android.widget.EditText;

/**
 * Created by Administrator on 2016/9/26.
 */
public class CarOwner_Add_Maintain {
    public CarOwner_Add_Maintain(String name, boolean isImag) {
        this.name = name;
        this.isImag = isImag;
    }
    String name;
    boolean isImag;
    String ettext;



    public String getEttext() {
        return ettext;
    }

    public void setEttext(String ettext) {
        this.ettext = ettext;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isImag() {
        return isImag;
    }

    public void setImag(boolean imag) {
        isImag = imag;
    }
}
