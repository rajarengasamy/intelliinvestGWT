package com.intelliinvest.client.data;

import java.util.Date;


@SuppressWarnings("serial")
public class OurOptionSuggestionData implements IntelliInvestData{
	String code;
	String instrument;
	Date expiryDate;
	Double strikePrice;
	String optionType;
	Double optionPrice;

	public OurOptionSuggestionData() {
	}
    
	public OurOptionSuggestionData(String code, String instrument,
			Date expiryDate, Double strikePrice, String optionType,
			Double optionPrice) {
		super();
		this.code = code;
		this.instrument = instrument;
		this.expiryDate = expiryDate;
		this.strikePrice = strikePrice;
		this.optionType = optionType;
		this.optionPrice = optionPrice;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getInstrument() {
		return instrument;
	}

	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Double getStrikePrice() {
		return strikePrice;
	}

	public void setStrikePrice(Double strikePrice) {
		this.strikePrice = strikePrice;
	}

	public String getOptionType() {
		return optionType;
	}

	public void setOptionType(String optionType) {
		this.optionType = optionType;
	}

	public Double getOptionPrice() {
		return optionPrice;
	}

	public void setOptionPrice(Double optionPrice) {
		this.optionPrice = optionPrice;
	}

	@Override
	public String toString() {		
		try{
			return 
			"{" 
			+ "\"code\":\"" + code + "\","
			+ "\"instrument\":\"" + instrument + "\","
			+ "\"expiryDate\":\"" +  expiryDate + "," 
			+ "\"strikePrice\":" +  strikePrice + ","
			+ "\"optionType\":\"" + optionType + "\","
			+ "\"optionPrice\":" +  optionPrice + ","
			+ "}";			
		}catch (Exception e) {
			return null;
		}
		finally{			
		}
	}
	@Override
	public boolean equals(Object obj) {
		OurOptionSuggestionData simulationData = (OurOptionSuggestionData) obj;
		if(simulationData.code.equals(this.code) && simulationData.instrument.equals(this.instrument) 
				&&  simulationData.expiryDate.equals(this.expiryDate) &&  simulationData.strikePrice.equals(this.strikePrice)){
			return true;
		}else{
			return false;
		}
	}
	
	public OurOptionSuggestionData clone() {
		OurOptionSuggestionData simulationData = new OurOptionSuggestionData(code, instrument, expiryDate, strikePrice, optionType, optionPrice);
		return simulationData;
	}
}
