package com.multicrawler.settings;

public class Rules{
	private DataType dataType;
	private DataLocation dataLocation;
	private String selector;
	private String attrSelector;
		
	public String getSelector() {
		return selector;
	}
	
	public String getAttrSelector() {
		return attrSelector;
	}
	
	public DataType getDataType(){
		return dataType;
	}

	public DataLocation getDataLocation() {
		return dataLocation;
	}
	
	Rules(String selector, DataType dataType){
		this.selector = selector;
		this.dataType = dataType;
		this.dataLocation = DataLocation.insideNode;
	}

	Rules(String selector, DataType dataType, String attrSelector){
		this(selector, dataType);
		this.dataLocation = DataLocation.insideAttribute;
		this.attrSelector = attrSelector;
	}
	
}