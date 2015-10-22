package com.intelliinvest.client.data;

import java.util.Date;


@SuppressWarnings("serial")
public class UserEODSignalChartData extends EODSignalChartData{
	Double avgBuyPrice;
	Double avgSellPrice;

	public UserEODSignalChartData() {
	}
    
	public UserEODSignalChartData(EODSignalChartData data) {
		super(data.getCode(), data.getDate(), data.getSignalType(), data.getPrice());
	}
	
	public UserEODSignalChartData(String code,  Date date, String signalType, Double price) {
		super();
		this.code = code;
		this.signalType = signalType;
		this.price = price;
		this.date = date;
	}


	public Double getAvgBuyPrice() {
		return avgBuyPrice;
	}

	public void setAvgBuyPrice(Double avgBuyPrice) {
		this.avgBuyPrice = avgBuyPrice;
	}

	public Double getAvgSellPrice() {
		return avgSellPrice;
	}

	public void setAvgSellPrice(Double avgSellPrice) {
		this.avgSellPrice = avgSellPrice;
	}

	@SuppressWarnings("deprecation")
	@Override
	public String toString() {		
		try{
			return 
					"[new Date(" + (date.getYear()+1900) + "," + date.getMonth() + "," + date.getDate() + "), "
					+ "" + price + ", "
					+ "" + (null!=signalType? price:null) + ", "+ (null==signalType?null:("'" + signalType.charAt(0) + "'")) + ","
					+ "" + avgBuyPrice + "," //+ (null!=avgBuyPrice? "Buy":null) + "'," 
					+ "" + avgSellPrice //+ ",'" + (null!=avgSellPrice? "Sell":null) + "'"
					+"]";
		}catch (Exception e) {
			return null;
		}
		finally{			
		}
	}
	@Override
	public boolean equals(Object obj) {
		UserEODSignalChartData simulationData = (UserEODSignalChartData) obj;
		if(simulationData.code.equals(this.code) && simulationData.date.equals(this.date)){
			return true;
		}else{
			return false;
		}
	}

}
