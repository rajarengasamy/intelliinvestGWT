package com.intelliinvest.client.data;

//import com.allen_sauer.gwt.log.client.Log;

@SuppressWarnings("serial")
public class StockDetailData implements IntelliInvestData{
	String code;
	String name;

	public StockDetailData() {
	}
	
	public StockDetailData(String code, String name) {
		super();
		this.code = code;
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {		
		try{
			return 
			"{" 
			+ "\"code\":\"" + code + "\"," 
			+ "\"name\":\"" + name + "\""
			+ "}";			
		}catch (Exception e) {
			return null;
		}
		finally{			
		}
	}
	@Override
	public boolean equals(Object obj) {
		StockDetailData stockDetailsData = (StockDetailData) obj;
		if(stockDetailsData.code.equals(this.code)){
			return true;
		}else{
			return false;
		}
	}
	
	public StockDetailData clone() {
		StockDetailData stockDetailsData = new StockDetailData(code, name);
		return stockDetailsData;
	}
}
