package com.goodmorningrainbow.model;

import java.io.Serializable;

public class PriceListDT implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
    private String name;
    private String defcost;
    private String fcost;
    private String voice;
    private String letter;
    private String data;
    private String data_unit;
    private String contract;
    private int disCost;
    
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
	public String getDefcost() {
		return defcost;
	}
	public void setDefcost(String defcost) {
		this.defcost = defcost;
	}
	public void setFcost(String fcost) {
		this.fcost = fcost;
	}
	public String getFcost() {
		return fcost;
	}
	public String getVoice() {
		return voice;
	}
	public void setVoice(String voice) {
		this.voice = voice;
	}
	public String getLetter() {
		return letter;
	}
	public void setLetter(String letter) {
		this.letter = letter;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getData_unit() {
		return data_unit;
	}
	public void setData_unit(String data_unit) {
		this.data_unit = data_unit;
	}
	public String getContract() {
		return this.contract;
	}
	public void setContract(String contract) {
		this.contract = contract;
	}
	public void setDisCost(int disCost) {
		this.disCost = disCost;
	}
	public int getDisCost() {
		return disCost;
	}
}
