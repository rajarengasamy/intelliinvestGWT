package com.intelliinvest.client.data;

import java.io.Serializable;

@SuppressWarnings("serial")
public class IdValueData implements Serializable, IntelliInvestData{
	public String id;
	public String value;

	public IdValueData() {
	}
	
	public IdValueData(String name, String value) {
		super();
		this.id = name;
		this.value = value;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return 
		"{" 
		+ "\"id\":\"" + id + "\"," 
		+ "\"value\":\"" + value + "\"" 
		+ "}";
	}
}
