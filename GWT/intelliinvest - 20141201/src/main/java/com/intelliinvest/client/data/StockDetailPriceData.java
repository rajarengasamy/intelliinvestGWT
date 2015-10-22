package com.intelliinvest.client.data;


@SuppressWarnings("serial")
public class StockDetailPriceData implements IntelliInvestData{
	String code;
	String name;
	Double price;

	public StockDetailPriceData() {
	}
	
	public StockDetailPriceData(String code, String name, Double price) {
		this.code = code;
		this.name = name;
		this.price = price;
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

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Override
	public String toString() {		
		try{
			return 
			"{" 
			+ "\"code\":\"" + code + "\","
			+ "\"name\":\"" + name + "\"," 
			+ "\"price\":" + price + ""
			+ "}";			
		}catch (Exception e) {
			return null;
		}
		finally{			
		}
	}
	@Override
	public boolean equals(Object obj) {
		StockDetailPriceData stockDetailPriceData = (StockDetailPriceData) obj;
		if(stockDetailPriceData.code.equals(this.code)){
			return true;
		}else{
			return false;
		}
	}
	
	public StockDetailPriceData clone() {
		StockDetailPriceData stockDetailPriceData = new StockDetailPriceData(code, name, price);
		return stockDetailPriceData;
	}
}
