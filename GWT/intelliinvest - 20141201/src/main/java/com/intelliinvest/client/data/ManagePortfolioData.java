package com.intelliinvest.client.data;

import java.util.Date;

@SuppressWarnings("serial")
public class ManagePortfolioData implements IntelliInvestData{
	String id;
	String code;
	Double price;
	Integer quantity;
	Integer remainingQuantity;
	String direction;
	Date date;
	Double realisedPnl = 0D;
	Double cp = 0D;
	Double currentPrice = 0D;
	Double amount = 0D;
	Double totalAmount = 0D;
	Double unrealisedPnl = 0D;
	Double todaysPnl = 0D;
	
	public ManagePortfolioData() {
	}

	
	public ManagePortfolioData(String id, String code, Double price,
			Integer quantity, Integer remainingQuantity, String direction,
			Date date, Double realisedPnl, Double cp, Double currentPrice,
			Double amount, Double totalAmount, Double unrealisedPnl,
			Double todaysPnl) {
		super();
		this.id = id;
		this.code = code;
		this.price = price;
		this.quantity = quantity;
		this.remainingQuantity = remainingQuantity;
		this.direction = direction;
		this.date = date;
		this.realisedPnl = realisedPnl;
		this.cp = cp;
		this.currentPrice = currentPrice;
		this.amount = amount;
		this.totalAmount = totalAmount;
		this.unrealisedPnl = unrealisedPnl;
		this.todaysPnl = todaysPnl;
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
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

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public Date getDate() {
		return date;
	}
	
	public Double getRealisedPnl() {
		return realisedPnl;
	}
	
	public void setRealisedPnl(Double pnl) {
		this.realisedPnl = pnl;
	}
	
	public Integer getRemainingQuantity() {
		return remainingQuantity;
	}
	
	public void setRemainingQuantity(Integer remainingQuantity) {
		this.remainingQuantity = remainingQuantity;
	}
	
	public Double getCp() {
		return cp;
	}


	public void setCp(Double cp) {
		this.cp = cp;
	}


	public Double getCurrentPrice() {
		return currentPrice;
	}


	public void setCurrentPrice(Double currentPrice) {
		this.currentPrice = currentPrice;
	}


	public Double getAmount() {
		return amount;
	}


	public void setAmount(Double amount) {
		this.amount = amount;
	}


	public Double getTotalAmount() {
		return totalAmount;
	}


	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}


	public Double getUnrealisedPnl() {
		return unrealisedPnl;
	}


	public void setUnrealisedPnl(Double unrealisedPnl) {
		this.unrealisedPnl = unrealisedPnl;
	}


	public Double getTodaysPnl() {
		return todaysPnl;
	}


	public void setTodaysPnl(Double todaysPnl) {
		this.todaysPnl = todaysPnl;
	}
	@Override
	public String toString() {		
		try{
			return 
			"{" 
			+ "\"managePortfolioId\":\"" + id + "\","
			+ "\"code\":\"" + code + "\","
			+ "\"price\":" + price + "," 
			+ "\"date\":\"" + date + "\","
			+ "\"direction\":\"" + direction + "\","
			+ "\"realisedPnl\":" + realisedPnl + "," 
			+ "\"quantity\":" +  quantity + "," 
			+ "\"remainingQuantity\":" +  remainingQuantity + "," 
			+ "\"cp\":" +  cp + "," 
			+ "\"currentPrice\":" +  currentPrice + "," 
			+ "\"amount\":" +  amount + "," 
			+ "\"totalAmount\":" +  totalAmount + "," 
			+ "\"unrealisedPnl\":" +  unrealisedPnl + "," 
			+ "\"todaysPnl\":" +  todaysPnl + "" 
			+ "}";			
		}catch (Exception e) {
			return null;
		}
		finally{			
		}
	}
	@Override
	public boolean equals(Object obj) {
		ManagePortfolioData managePortfolioData = (ManagePortfolioData) obj;
		if(managePortfolioData.id.equals(this.id)){
			return true;
		}else{
			return false;
		}
	}
	
	public ManagePortfolioData clone() {
		ManagePortfolioData managePortfolioData = new ManagePortfolioData(id, code, price, quantity, remainingQuantity, direction, date, realisedPnl, cp, currentPrice, amount, totalAmount, unrealisedPnl, todaysPnl);
		return managePortfolioData;
	}
}
