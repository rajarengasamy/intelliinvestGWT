package com.intelliinvest.client.data;



@SuppressWarnings("serial")
public class PaymentData implements IntelliInvestData{
	Integer noOfStocks;
	Integer noOfMonths;
	Double amount;
	String link;
	
	public PaymentData() {
	}
    
    
	public PaymentData(Integer noOfStocks, Integer noOfMonths, Double amount,
			String link) {
		super();
		this.noOfStocks = noOfStocks;
		this.noOfMonths = noOfMonths;
		this.amount = amount;
		this.link = link;
	}


	public Integer getNoOfStocks() {
		return noOfStocks;
	}


	public void setNoOfStocks(Integer noOfStocks) {
		this.noOfStocks = noOfStocks;
	}


	public Integer getNoOfMonths() {
		return noOfMonths;
	}


	public void setNoOfMonths(Integer noOfMonths) {
		this.noOfMonths = noOfMonths;
	}


	public Double getAmount() {
		return amount;
	}


	public void setAmount(Double amount) {
		this.amount = amount;
	}


	public String getLink() {
		return link;
	}


	public void setLink(String link) {
		this.link = link;
	}


	@Override
	public String toString() {		
		try{
			return 
			"{" 
			+ "\"noOfStocks\":\"" + noOfStocks + "\","
			+ "\"noOfMonths\":\"" + noOfMonths + "\","
			+ "\"amount\":\"" +  amount + "," 
			+ "\"link\":" +  link + ","
			+ "}";			
		}catch (Exception e) {
			return null;
		}
		finally{			
		}
	}
	@Override
	public boolean equals(Object obj) {
		PaymentData simulationData = (PaymentData) obj;
		if(simulationData.noOfStocks.equals(this.noOfStocks) && simulationData.noOfMonths.equals(this.noOfMonths)){
			return true;
		}else{
			return false;
		}
	}
	
	public PaymentData clone() {
		PaymentData simulationData = new PaymentData(noOfStocks, noOfMonths, amount, link);
		return simulationData;
	}
}
