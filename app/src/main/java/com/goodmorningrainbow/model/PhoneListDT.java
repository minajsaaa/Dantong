package com.goodmorningrainbow.model;

import java.io.Serializable;

public class PhoneListDT implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String name;    
    private String nameKor;
    private String cost;    
    private String imgName;
    private String netkind;
    private int club;
    private float rateAve;
    private int rateCnt;
    private String imgUrl;
    
    public String getId() {
    	return id;
    }
    public void setId(String id) {
    	this.id = id;
    }
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNameKor() {
		return nameKor;
	}
	public void setNameKor(String nameKor) {
		this.nameKor = nameKor;
	}
	public String getCost() {
		return cost;
	}
	public void setCost(String cost) {
		this.cost = cost;
	}
	public String getImgName() {
		return imgName;
	}
	public void setImgName(String imgName) {
		this.imgName = imgName;
	}
	public String getNetkind() {
		return netkind;
	}
	public void setNetkind(String netkind) {
		this.netkind = netkind;				
	}
	public int getClub() {
		return club;
	}
	public void setClub(int club) {
		this.club = club;
	}
	public float getRateAve() {
		return rateAve;
	}
	public void setRateAve(float rateAve) {
		this.rateAve = rateAve;
	}
	public int getRateCnt() {
		return rateCnt;
	}
	public void setRateCnt(int rateCnt) {
		this.rateCnt = rateCnt;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
}
