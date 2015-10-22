package com.intelliinvest.client.data;


@SuppressWarnings("serial")
public class SimulationData implements IntelliInvestData{
	String code;
	Double quarterly;
	Double halfYearly;
	Double nineMonths;
	Double yearly;

	public SimulationData() {
	}
    
    
	public SimulationData(String code, Double quarterly, Double halfYearly, Double nineMonths, Double yearly) {
		super();
		this.code = code;
		this.quarterly = quarterly;
		this.halfYearly = halfYearly;
		this.nineMonths = nineMonths;
		this.yearly = yearly;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public Double getQuarterly() {
		return quarterly;
	}


	public void setQuarterly(Double quarterly) {
		this.quarterly = quarterly;
	}


	public Double getHalfYearly() {
		return halfYearly;
	}


	public void setHalfYearly(Double halfYearly) {
		this.halfYearly = halfYearly;
	}
	
	public Double getNineMonths() {
		return nineMonths;
	}
	
	public void setNineMonths(Double nineMonths) {
		this.nineMonths = nineMonths;
	}

	public Double getYearly() {
		return yearly;
	}


	public void setYearly(Double yearly) {
		this.yearly = yearly;
	}


	@Override
	public String toString() {		
		try{
			return 
			"{" 
			+ "\"code\":\"" + code + "\","
			+ "\"quarterly\":" + quarterly + "," 
			+ "\"halfYearly\":" +  halfYearly + ","
			+ "\"nineMonths\":" +  nineMonths + ","
			+ "\"yearly\":" +  yearly + ""
			+ "}";			
		}catch (Exception e) {
			return null;
		}
		finally{			
		}
	}
	@Override
	public boolean equals(Object obj) {
		SimulationData simulationData = (SimulationData) obj;
		if(simulationData.code.equals(this.code)){
			return true;
		}else{
			return false;
		}
	}
	
	public SimulationData clone() {
		SimulationData simulationData = new SimulationData(code, quarterly, halfYearly, nineMonths, yearly); 
		return simulationData;
	}
}
