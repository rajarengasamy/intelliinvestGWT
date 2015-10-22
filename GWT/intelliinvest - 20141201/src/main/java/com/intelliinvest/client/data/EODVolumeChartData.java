package com.intelliinvest.client.data;

import java.util.Date;


@SuppressWarnings("serial")
public class EODVolumeChartData extends EODChartData implements IntelliInvestData{
	Long volume;

	public EODVolumeChartData() {
	}
    
	public EODVolumeChartData(String code,  Date date, Double price, Long volume) {
		super(code, date, price);
		this.volume = volume;
	}

	public Long getVolume() {
		return volume;
	}
	
	public void setVolume(Long volume) {
		this.volume = volume;
	}

	@SuppressWarnings("deprecation")
	@Override
	public String toString() {		
		try{
			return 
					"{"
						+ "date:new Date(" + (date.getYear()+1900) + "," + date.getMonth() + "," + date.getDate() + "," + date.getHours() + "," + date.getMinutes() + "," + date.getSeconds() + ")"
						+ (null!=price?(",value:" + price):"") 
						+ (null!=volume?(", volume:" + volume):"")
					+"}";
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
	
	public EODVolumeChartData clone() {
		EODVolumeChartData volumeChartData = new EODVolumeChartData(code, date, price, volume); 
		return volumeChartData;
	}
}
