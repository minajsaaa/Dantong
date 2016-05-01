package com.goodmorningrainbow.model;

import java.io.Serializable;
import java.util.ArrayList;

public class CostDT implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String name;
	private ArrayList<String> costArray;
	
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
	public ArrayList<String> getCostArray() {
		return costArray;
	}
	public void setCostArray(ArrayList<String> costArray) {
		this.costArray = costArray;
	}
}
