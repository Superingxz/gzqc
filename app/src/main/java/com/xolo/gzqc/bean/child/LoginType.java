package com.xolo.gzqc.bean.child;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2017/1/5.
 */
public class LoginType implements Parcelable{

    /**
     * user_name : 机械一
     * phone : 13803333331
     * type : [{"user_type":"1"}]
     */

    private String user_name;
    private String phone;
    /**
     * user_type : 1
     */

    private List<TypeBean> type;

    protected LoginType(Parcel in) {
        user_name = in.readString();
        phone = in.readString();
        type = in.createTypedArrayList(TypeBean.CREATOR);
    }

    public static final Creator<LoginType> CREATOR = new Creator<LoginType>() {
        @Override
        public LoginType createFromParcel(Parcel in) {
            return new LoginType(in);
        }

        @Override
        public LoginType[] newArray(int size) {
            return new LoginType[size];
        }
    };

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<TypeBean> getType() {
        return type;
    }

    public void setType(List<TypeBean> type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_name);
        dest.writeString(phone);
        dest.writeTypedList(type);
    }

    public static class TypeBean implements Parcelable {

        private String user_type;

        public String getUser_type() {
            return user_type;
        }

        public void setUser_type(String user_type) {
            this.user_type = user_type;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.user_type);
        }

        public TypeBean() {
        }

        protected TypeBean(Parcel in) {
            this.user_type = in.readString();
        }

        public static final Creator<TypeBean> CREATOR = new Creator<TypeBean>() {
            @Override
            public TypeBean createFromParcel(Parcel source) {
                return new TypeBean(source);
            }

            @Override
            public TypeBean[] newArray(int size) {
                return new TypeBean[size];
            }
        };
    }
}
