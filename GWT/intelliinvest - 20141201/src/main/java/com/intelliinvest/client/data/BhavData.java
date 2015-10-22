package com.intelliinvest.client.data;

import java.util.Date;

@SuppressWarnings("serial")
public class BhavData implements IntelliInvestData{
	String exchange;
	String symbol;
	String series;
	Double open = 0D;
	Double high = 0D;
	Double low = 0D;
	Double close = 0D;
	Double last = 0D;
	Double prevClose = 0D;
	Long totTrdQty = 0L;
	Long totTrdVal = 0L;
	Date timeStamp;
	Long totalTrades;
	String isin;
	
	public BhavData() {
		// TODO Auto-generated constructor stub
	}
	
	public String getExchange() {
		return exchange;
	}
	public void setExchange(String exchange) {
		this.exchange = exchange;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getSeries() {
		return series;
	}
	public void setSeries(String series) {
		this.series = series;
	}
	public Double getOpen() {
		return open;
	}
	public void setOpen(Double open) {
		this.open = open;
	}
	public Double getHigh() {
		return high;
	}
	public void setHigh(Double high) {
		this.high = high;
	}
	public Double getLow() {
		return low;
	}
	public void setLow(Double low) {
		this.low = low;
	}
	public Double getClose() {
		return close;
	}
	public void setClose(Double close) {
		this.close = close;
	}
	public Double getLast() {
		return last;
	}
	public void setLast(Double last) {
		this.last = last;
	}
	public Double getPrevClose() {
		return prevClose;
	}
	public void setPrevClose(Double prevClose) {
		this.prevClose = prevClose;
	}
	public Long getTotTrdQty() {
		return totTrdQty;
	}
	public void setTotTrdQty(Long totTrdQty) {
		this.totTrdQty = totTrdQty;
	}
	public Long getTotTrdVal() {
		return totTrdVal;
	}
	public void setTotTrdVal(Long totTrdVal) {
		this.totTrdVal = totTrdVal;
	}
	public Date getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	public Long getTotalTrades() {
		return totalTrades;
	}
	public void setTotalTrades(Long totalTrades) {
		this.totalTrades = totalTrades;
	}
	public String getIsin() {
		return isin;
	}
	public void setIsin(String isin) {
		this.isin = isin;
	}
	
	
}
