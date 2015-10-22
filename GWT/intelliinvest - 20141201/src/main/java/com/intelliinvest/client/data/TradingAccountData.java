package com.intelliinvest.client.data;

import java.util.Date;


@SuppressWarnings("serial")
public class TradingAccountData implements IntelliInvestData{
	String code;
	String yesterdaySignalType;
	String signalType;
	Double signalPrice;
	Date signalDate;

	public TradingAccountData() {
	}
    
    
	public TradingAccountData(String code, String yesterdaySignalType, String signalType,
			Double signalPrice, Date signalDate) {
		super();
		this.code = code;
		this.yesterdaySignalType = yesterdaySignalType;
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

	public String getYesterdaySignalType() {
		return yesterdaySignalType;
	}
	
	public void setYesterdaySignalType(String yesterdaySignalType) {
		this.yesterdaySignalType = yesterdaySignalType;
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
			+ "\"yesterdaySignalType\":\"" +  yesterdaySignalType + "," 
			+ "\"signalType\":\"" +  signalType + "," 
			+ "\"signalPrice\":" +  signalPrice + ","
			+ "\"signalDate\":" +  signalDate + ""
			+ "}";			
		}catch (Exception e) {
			return null;
		}
		finally{			
		}
	}
	@Override
	public boolean equals(Object obj) {
		TradingAccountData simulationData = (TradingAccountData) obj;
		if(simulationData.code.equals(this.code)){
			return true;
		}else{
			return false;
		}
	}
	
	public TradingAccountData clone() {
		TradingAccountData simulationData = new TradingAccountData(code, yesterdaySignalType, signalType, signalPrice, signalDate); 
		return simulationData;
	}
}
