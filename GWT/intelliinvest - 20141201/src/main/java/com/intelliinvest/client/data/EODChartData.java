package com.intelliinvest.client.data;

import java.util.Date;


@SuppressWarnings("serial")
public class EODChartData implements IntelliInvestData{
	String code;
	Date date;
	Double price;

	public EODChartData() {
	}
    
	public EODChartData(String code,  Date date, Double price) {
		super();
		this.code = code;
		this.price = price;
		this.date = date;
	}


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@SuppressWarnings("deprecation")
	@Override
	public String toString() {		
		try{
			return 
					"[new Date(" + (date.getYear()+1900) + "," + date.getMonth() + "," + date.getDate() + "), "
					+ "" + price +"]";
		}catch (Exception e) {
			return null;
		}
		finally{			
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		EODChartData simulationData = (EODChartData) obj;
		if(simulationData.code.equals(this.code) && simulationData.date.equals(this.date)){
			return true;
		}else{
			return false;
		}
	}
	
	public EODChartData clone() {
		EODChartData simulationData = new EODChartData(code, date, price); 
		return simulationData;
	}
}
