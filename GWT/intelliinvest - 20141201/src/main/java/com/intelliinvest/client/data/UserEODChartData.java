package com.intelliinvest.client.data;

import java.util.Date;


@SuppressWarnings("serial")
public class UserEODChartData extends EODChartData{
	Double avgBuyPrice;
	Double avgSellPrice;

	public UserEODChartData() {
	}
    
	public UserEODChartData(EODChartData data) {
		super(data.getCode(), data.getDate() , data.getPrice());
	}
	
	public UserEODChartData(String code,  Date date, Double price) {
		super();
		this.code = code;
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
		UserEODChartData simulationData = (UserEODChartData) obj;
		if(simulationData.code.equals(this.code) && simulationData.date.equals(this.date)){
			return true;
		}else{
			return false;
		}
	}

}
