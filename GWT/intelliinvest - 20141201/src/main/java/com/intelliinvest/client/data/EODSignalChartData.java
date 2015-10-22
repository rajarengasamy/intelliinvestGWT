package com.intelliinvest.client.data;

import java.util.Date;


@SuppressWarnings("serial")
public class EODSignalChartData extends EODChartData implements IntelliInvestData{
	String signalType;

	public EODSignalChartData() {
	}
    
	public EODSignalChartData(String code,  Date date, String signalType, Double price) {
		super(code, date, price);
		this.signalType = signalType;
	}

	public String getSignalType() {
		return signalType;
	}

	public void setSignalType(String signalType) {
		this.signalType = signalType;
	}

	@SuppressWarnings("deprecation")
	@Override
	public String toString() {		
		try{
			return 
					"[new Date(" + (date.getYear()+1900) + "," + date.getMonth() + "," + date.getDate() + "), "
					+ "" + price + ", "
					+ "" + (null!=signalType? price:null) + ", "+ (null==signalType?null:("'" + signalType.charAt(0) + "'")) +"]";
		}catch (Exception e) {
			return null;
		}
		finally{			
		}
	}
	@Override
	public boolean equals(Object obj) {
		EODSignalChartData simulationData = (EODSignalChartData) obj;
		if(simulationData.code.equals(this.code) && simulationData.date.equals(this.date)){
			return true;
		}else{
			return false;
		}
	}
	
	public EODSignalChartData clone() {
		EODSignalChartData simulationData = new EODSignalChartData(code, date, signalType, price); 
		return simulationData;
	}
}
