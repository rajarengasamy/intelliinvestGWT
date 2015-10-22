package com.intelliinvest.client.data;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ComboBoxData implements Serializable, IntelliInvestData{
	public String value;
	public String dispvalue;

	public ComboBoxData() {
	}
	

	public ComboBoxData(String value, String dispvalue) {
		super();
		this.value = value;
		this.dispvalue = dispvalue;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}	
	
	public String getDispvalue() {
		return dispvalue;
	}


	public void setDispvalue(String dispvalue) {
		this.dispvalue = dispvalue;
	}


	@Override
	public String toString() {
		return 
		"{" 
		+ "\"dispvalue\":\"" + dispvalue + "\"," 
		+ "\"value\":\"" + value + "\"" 
		+ "}";
	}
}
