package com.xolo.gzqc.bean.child;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * 车况
 */
public class CarState implements Parcelable {


    /**
     * description : 车身右侧有撞击痕迹
     * type_name : 外观
     * photo :
     * bf_car_status_id : 204B7C3E-5DEA-41C5-9655-DE227BE2DEC0
     * type_id : jxstar0687731
     */

    private String description;
    private String type_name;
    private String photo = "";
    private String bf_car_status_id;
    private String type_id;
//    private Bitmap bitmap;
    private String path;
    private  String photolist;

    public CarState() {
    }

    public CarState(String description, String type_name, String photo, String bf_car_status_id, String type_id) {
        this.description = description;
        this.type_name = type_name;
        this.photo = photo;
        this.bf_car_status_id = bf_car_status_id;
        this.type_id = type_id;
    }

    public String getPhotolist() {
        return photolist;
    }

    public void setPhotolist(String photolist) {
        this.photolist = photolist;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getBf_car_status_id() {
        return bf_car_status_id;
    }

    public void setBf_car_status_id(String bf_car_status_id) {
        this.bf_car_status_id = bf_car_status_id;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }


    @Override
//    "{\"data\":"+          +"}"
    public String toString() {
        return "{\"type_id\":\""+type_id+"\",\"type_name\":\""+type_name+"\",\"description\":\""+description+"\"}";
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.description);
        dest.writeString(this.type_name);
        dest.writeString(this.photo);
        dest.writeString(this.bf_car_status_id);
        dest.writeString(this.type_id);
        dest.writeString(this.path);
        dest.writeString(this.photolist);
    }

    protected CarState(Parcel in) {
        this.description = in.readString();
        this.type_name = in.readString();
        this.photo = in.readString();
        this.bf_car_status_id = in.readString();
        this.type_id = in.readString();
        this.path = in.readString();
        this.photolist = in.readString();
    }

    public static final Creator<CarState> CREATOR = new Creator<CarState>() {
        @Override
        public CarState createFromParcel(Parcel source) {
            return new CarState(source);
        }

        @Override
        public CarState[] newArray(int size) {
            return new CarState[size];
        }
    };
}
