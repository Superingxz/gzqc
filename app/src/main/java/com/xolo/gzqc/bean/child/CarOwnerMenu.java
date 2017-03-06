package com.xolo.gzqc.bean.child;

public class CarOwnerMenu {
	String title;
	Integer img;
	Integer defaultimg;
	Boolean isOnclick;
	String messageNum;

	public String getMessageNum() {
		return messageNum;
	}

	public void setMessageNum(String messageNum) {
		this.messageNum = messageNum;
	}

	public CarOwnerMenu(String title, Integer img, Integer defaultimg, Boolean isOnclick) {
		super();
		this.title = title;
		this.img = img;
		this.defaultimg = defaultimg;
		this.isOnclick = isOnclick;
	}

	public Integer getDefaultimg() {
		return defaultimg;
	}

	public void setDefaultimg(Integer defaultimg) {
		this.defaultimg = defaultimg;
	}

	public Boolean getIsOnclick() {
		return isOnclick;
	}

	public void setIsOnclick(Boolean isOnclick) {
		this.isOnclick = isOnclick;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getImg() {
		return img;
	}

	public void setImg(Integer img) {
		this.img = img;
	}

}
