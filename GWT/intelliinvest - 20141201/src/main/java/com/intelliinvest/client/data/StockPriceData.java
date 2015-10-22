package com.intelliinvest.client.data;


@SuppressWarnings("serial")
public class StockPriceData implements IntelliInvestData{
	String code;
	Double price;
	Double cp;

	public StockPriceData() {
	}
	
	public StockPriceData(String code, Double price, Double cp) {
		this.code = code;
		this.price = price;
		this.cp = cp;
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
	
	public Double getCp() {
		return cp;
	}
	
	public void setCp(Double cp) {
		this.cp = cp;
	}
	
	@Override
	public String toString() {		
		try{
			return 
			"{" 
			+ "\"code\":\"" + code + "\","
			+ "\"price\":" + price + "," 
			+ "\"cp\":" + cp + "," 
			+ "}";			
		}catch (Exception e) {
			return null;
		}
		finally{			
		}
	}
	@Override
	public boolean equals(Object obj) {
		StockPriceData stockPriceData = (StockPriceData) obj;
		if(stockPriceData.code.equals(this.code)){
			return true;
		}else{
			return false;
		}
	}
	
	public StockPriceData clone() {
		StockPriceData stockPriceData = new StockPriceData(code, price, cp);
		return stockPriceData;
	}
}
