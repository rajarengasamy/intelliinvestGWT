package com.intelliinvest.client.data;

import java.util.Date;


@SuppressWarnings("serial")
public class OurSuggestionData implements IntelliInvestData{
	String code;
	String signalType;
	Double signalPrice;
	String suggestionType;
	Date signalDate;
	
	public OurSuggestionData() {
	}
    
    
	public OurSuggestionData(String code, String suggestionType, String signalType,
			Double signalPrice, Date signalDate) {
		super();
		this.code = code;
		this.suggestionType = suggestionType;
		this.signalType = signalType;
		this.signalPrice = signalPrice;
		this.signalDate = signalDate;
	}

	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}

	public String getSuggestionType() {
		return suggestionType;
	}
	
	public void setSuggestionType(String suggestionType) {
		this.suggestionType = suggestionType;
	}
	
	public String getSignalType() {
		return signalType;
	}


	public void setSignalType(String signalType) {
		this.signalType = signalType;
	}


	public Double getSignalPrice() {
		return signalPrice;
	}


	public void setSignalPrice(Double signalPrice) {
		this.signalPrice = signalPrice;
	}


	public Date getSignalDate() {
		return signalDate;
	}
	
	public void setSignalDate(Date signalDate) {
		this.signalDate = signalDate;
	}
	
	@Override
	public String toString() {		
		try{
			return 
			"{" 
			+ "\"code\":\"" + code + "\","
			+ "\"suggestionType\":\"" + suggestionType + "\","
			+ "\"signal\":\"" +  signalType + "," 
			+ "\"signalPrice\":" +  signalPrice + ","
			+ "\"signalDate\":" +  signalDate + ","
			+ "}";			
		}catch (Exception e) {
			return null;
		}
		finally{			
		}
	}
	@Override
	public boolean equals(Object obj) {
		OurSuggestionData simulationData = (OurSuggestionData) obj;
		if(simulationData.code.equals(this.code) && simulationData.suggestionType.equals(this.suggestionType)){
			return true;
		}else{
			return false;
		}
	}
	
	public OurSuggestionData clone() {
		OurSuggestionData simulationData = new OurSuggestionData(code, suggestionType, signalType, signalPrice, signalDate); 
		return simulationData;
	}
}
