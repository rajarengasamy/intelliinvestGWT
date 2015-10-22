package com.intelliinvest.client.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("serial")
public class IntelliInvestResponse implements Serializable{
	String responseType;
	HashMap<String, String> responseAttributes;
	ArrayList<? extends IntelliInvestData> responseData;
	HashMap<String, HashMap<String, ? extends IntelliInvestData>> screenData;
	
	public IntelliInvestResponse() {
		responseAttributes = new HashMap<String, String>();
		responseData = new ArrayList<IntelliInvestData>();
		screenData = new HashMap<String, HashMap<String, ? extends IntelliInvestData>>();
	}
	
	public IntelliInvestResponse(String responseType) {
		this.responseType = responseType;
		responseAttributes = new HashMap<String, String>();
		responseData = new ArrayList<IntelliInvestData>();
		screenData = new HashMap<String, HashMap<String, ? extends IntelliInvestData>>();
	}

	public IntelliInvestResponse(String responseType, HashMap<String, String> responseAttributes, ArrayList<? extends IntelliInvestData> responseData, HashMap<String, HashMap<String,  ? extends IntelliInvestData>> screenData) {
		super();
		this.responseType = responseType;
		this.responseAttributes = responseAttributes;
		this.responseData = responseData;
		this.screenData = screenData;
	}

	public String getResponseType() {
		return responseType;
	}
	
	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}
	
	public HashMap<String, String> getResponseAttributes() {
		return responseAttributes;
	}

	public void setResponseAttributes(HashMap<String, String> responseAttributes) {
		this.responseAttributes = responseAttributes;
	}

	public ArrayList<? extends IntelliInvestData> getResponseData() {
		return responseData;
	}

	public void setResponseData(ArrayList<? extends IntelliInvestData> responseData) {
		this.responseData = responseData;
	}

	public HashMap<String, HashMap<String, ? extends IntelliInvestData>> getScreenData() {
		return screenData;
	}

	public void setScreenData(
			HashMap<String, HashMap<String, ? extends IntelliInvestData>> screenData) {
		this.screenData = screenData;
	}
	
}
