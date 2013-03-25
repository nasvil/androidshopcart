package com.app.model;

public class ShareItem {
	String itemName;
	String itemKey;
	int imageID;
	
	public ShareItem(String itemName, String itemKey, int imageID) {
		super();
		this.itemName = itemName;
		this.itemKey = itemKey;
		this.imageID = imageID;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemKey() {
		return itemKey;
	}
	public void setItemKey(String itemKey) {
		this.itemKey = itemKey;
	}
	public int getImageID() {
		return imageID;
	}
	public void setImageID(int imageID) {
		this.imageID = imageID;
	}
}
