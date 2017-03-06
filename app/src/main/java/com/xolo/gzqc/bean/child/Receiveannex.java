package com.xolo.gzqc.bean.child;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * 随车附件
 */
public class Receiveannex implements Parcelable{

    /**
     * is_exist : 0
     * annex_name : 三角牌
     * bf_receive_annex_id : 1AF5B2B9-E2F2-4C4F-BF0A-7BD4EFB96497
     * annex_code : 6001
     */

    private String is_exist;
    private String annex_name;
    private String bf_receive_annex_id;
    private String annex_code;


    public Receiveannex(String annex_code, String annex_name, String  is_exist) {
        this.is_exist = is_exist;
        this.annex_name = annex_name;
        this.annex_code = annex_code;
    }

    protected Receiveannex(Parcel in) {
        is_exist = in.readString();
        annex_name = in.readString();
        bf_receive_annex_id = in.readString();
        annex_code = in.readString();
    }

    public static final Creator<Receiveannex> CREATOR = new Creator<Receiveannex>() {
        @Override
        public Receiveannex createFromParcel(Parcel in) {
            return new Receiveannex(in);
        }

        @Override
        public Receiveannex[] newArray(int size) {
            return new Receiveannex[size];
        }
    };

    public String getIs_exist() {
        return is_exist;
    }

    public void setIs_exist(String is_exist) {
        this.is_exist = is_exist;
    }

    public String getAnnex_name() {
        return annex_name;
    }

    public void setAnnex_name(String annex_name) {
        this.annex_name = annex_name;
    }

    public String getBf_receive_annex_id() {
        return bf_receive_annex_id;
    }

    public void setBf_receive_annex_id(String bf_receive_annex_id) {
        this.bf_receive_annex_id = bf_receive_annex_id;
    }

    public String getAnnex_code() {
        return annex_code;
    }

    public void setAnnex_code(String annex_code) {
        this.annex_code = annex_code;
    }


    @Override
//    {\"annex_code\":\"1001\",\"annex_name\":\"行驶证\",\"is_exist\":\"1\"}
    public String toString() {
        return "{\"annex_code\":\""+annex_code+"\",\"annex_name\":\""+annex_name+"\",\"is_exist\":\""+is_exist+"\"}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(is_exist);
        dest.writeString(annex_name);
        dest.writeString(bf_receive_annex_id);
        dest.writeString(annex_code);
    }

}
