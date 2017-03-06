package com.xolo.gzqc.bean.child;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 车况读取
 */
public class CarStateRead implements Parcelable {


    /**
     * photos : [{"photo":"http://120.77.3.76/gzqiche/images/bf_car_status/2A398236-28FC-4F7C-9ACE-8080E426F087.jpg"}]
     * description : I have
     * type_name : 内饰
     * bf_car_status_id : AA6E1A1D-C723-4C1C-A3FB-F5C7C9A9F038
     * type_id : jxstar8497732
     */

    private String description;
    private String type_name;
    private String bf_car_status_id;
    private String type_id;
    /**
     * photo : http://120.77.3.76/gzqiche/images/bf_car_status/2A398236-28FC-4F7C-9ACE-8080E426F087.jpg
     */

    private List<PhotosBean> photos = new ArrayList<>();

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

    public List<PhotosBean> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotosBean> photos) {
        this.photos = photos;
    }

    public static class PhotosBean implements Parcelable {

        private String photo;

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.photo);
        }

        public PhotosBean() {
        }

        protected PhotosBean(Parcel in) {
            this.photo = in.readString();
        }

        public static final Creator<PhotosBean> CREATOR = new Creator<PhotosBean>() {
            @Override
            public PhotosBean createFromParcel(Parcel source) {
                return new PhotosBean(source);
            }

            @Override
            public PhotosBean[] newArray(int size) {
                return new PhotosBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.description);
        dest.writeString(this.type_name);
        dest.writeString(this.bf_car_status_id);
        dest.writeString(this.type_id);
        dest.writeList(this.photos);
    }

    public CarStateRead() {
    }

    protected CarStateRead(Parcel in) {
        this.description = in.readString();
        this.type_name = in.readString();
        this.bf_car_status_id = in.readString();
        this.type_id = in.readString();
        this.photos = new ArrayList<PhotosBean>();
        in.readList(this.photos, PhotosBean.class.getClassLoader());
    }

    public static final Creator<CarStateRead> CREATOR = new Creator<CarStateRead>() {
        @Override
        public CarStateRead createFromParcel(Parcel source) {
            return new CarStateRead(source);
        }

        @Override
        public CarStateRead[] newArray(int size) {
            return new CarStateRead[size];
        }
    };
}
