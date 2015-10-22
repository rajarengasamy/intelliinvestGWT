package com.intelliinvest.client.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("serial")
public class IntelliInvestRequest implements Serializable{
	String requestType;
	HashMap<String, String> requestAttributes;
	ArrayList<? extends IntelliInvestData> requestData;
	HashMap<String, String> filterAttributes;
	HashMap<String, HashMap<String, ? extends IntelliInvestData>> screenData;
	
	public IntelliInvestRequest() {
		requestAttributes = new HashMap<String, String>();
		requestData = new ArrayList<IntelliInvestData>();
		filterAttributes = new HashMap<String, String>();
		screenData = new HashMap<String, HashMap<String,? extends IntelliInvestData>>();
	}
	public IntelliInvestRequest(String requestType) {
		this.requestType = requestType;
		requestAttributes = new HashMap<String, String>();
		requestData = new ArrayList<IntelliInvestData>();
		filterAttributes = new HashMap<String, String>();
		screenData = new HashMap<String, HashMap<String, ? extends IntelliInvestData>>();
	}

	public IntelliInvestRequest(String requestType, HashMap<String, String> requestAttributes, ArrayList<? extends IntelliInvestData> requestData, HashMap<String, String> filterAttributes, HashMap<String, HashMap<String, ? extends IntelliInvestData>> screenData) {
		super();
		this.requestType = requestType;
		this.requestAttributes = requestAttributes;
		this.requestData = requestData;
		this.filterAttributes = filterAttributes;
		this.screenData = screenData;
	}
	
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	
	public String getRequestType() {
		return requestType;
	}

	public HashMap<String, String> getRequestAttributes() {
		return requestAttributes;
	}

	public void setRequestAttributes(HashMap<String, String> requestAttributes) {
		this.requestAttributes = requestAttributes;
	}

	public ArrayList<? extends IntelliInvestData> getRequestData() {
		return requestData;
	}

	public void setRequestData(ArrayList<? extends IntelliInvestData> requestData) {
		this.requestData = requestData;
	}

	public HashMap<String, String> getFilterAttributes() {
		return filterAttributes;
	}

	public void setFilterAttributes(HashMap<String, String> filterAttributes) {
		this.filterAttributes = filterAttributes;
	}

	public HashMap<String, HashMap<String,? extends IntelliInvestData>> getScreenData() {
		return screenData;
	}

	public void setScreenData(HashMap<String, HashMap<String, ? extends IntelliInvestData>> screenData) {
		this.screenData = screenData;
	}

}
