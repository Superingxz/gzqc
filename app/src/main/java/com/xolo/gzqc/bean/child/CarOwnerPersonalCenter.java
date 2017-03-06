package com.xolo.gzqc.bean.child;

/**
 * Created by Administrator on 2016/9/24.
 */
public class CarOwnerPersonalCenter {
    String[] mItems;
    String Text;
    String content;
    CoInfo coInfo;
    public CarOwnerPersonalCenter(String text, String content) {
        Text = text;
        this.content = content;
    }

    public String[] getmItems() {
        return mItems;
    }

    public void setmItems(String[] mItems) {
        this.mItems = mItems;
    }

    public CoInfo getCoInfo() {
        return coInfo;
    }

    public void setCoInfo(CoInfo coInfo) {
        this.coInfo = coInfo;
    }
    public String getText() {
        return Text;
    }

    public String getContent() {
        return content;
    }
    public void setText(String text) {
        Text = text;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
