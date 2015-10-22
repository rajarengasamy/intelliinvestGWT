package com.intelliinvest.client.data;

import java.util.ArrayList;
import java.util.List;



@SuppressWarnings("serial")
public class RiskReturnMatrixSummaryData implements IntelliInvestData{
	String summaryType;
	List<String> codes;
	Integer buyCount;
	Integer sellCount;
	
	public RiskReturnMatrixSummaryData() {
	}
	
	public RiskReturnMatrixSummaryData(String summaryType) {
		super();
		this.summaryType = summaryType;
		codes = new ArrayList<String>();
		buyCount = 0;
		sellCount = 0;
	}

	public RiskReturnMatrixSummaryData(String summaryType, List<String> codes,
			Integer buyCount, Integer sellCount) {
		super();
		this.summaryType = summaryType;
		this.codes = codes;
		this.buyCount = buyCount;
		this.sellCount = sellCount;
	}

	public String getSummaryType() {
		return summaryType;
	}

	public void setSummaryType(String summaryType) {
		this.summaryType = summaryType;
	}

	public List<String> getCodes() {
		return codes;
	}

	public void setCodes(List<String> codes) {
		this.codes = codes;
	}

	public Integer getBuyCount() {
		return buyCount;
	}

	public void setBuyCount(Integer buyCount) {
		this.buyCount = buyCount;
	}

	public Integer getSellCount() {
		return sellCount;
	}

	public void setSellCount(Integer sellCount) {
		this.sellCount = sellCount;
	}

	@Override
	public String toString() {		
		try{
			return 
			"{" 
			+ "\"summaryType\":\"" + summaryType + "\","
			+ "\"codes\":\"" +  codes + "," 
			+ "\"buyCount\":\"" +  buyCount + "," 
			+ "\"sellCount\":\"" +  sellCount + "," 
			+ "}";			
		}catch (Exception e) {
			return null;
		}
		finally{			
		}
	}
	@Override
	public boolean equals(Object obj) {
		RiskReturnMatrixSummaryData riskReturnMatrixSummaryData = (RiskReturnMatrixSummaryData) obj;
		if(riskReturnMatrixSummaryData.summaryType.equals(this.summaryType) && riskReturnMatrixSummaryData.summaryType.equals(this.summaryType)){
			return true;
		}else{
			return false;
		}
	}
	
	public RiskReturnMatrixSummaryData clone() {
		RiskReturnMatrixSummaryData simulationData = new RiskReturnMatrixSummaryData(summaryType, codes, buyCount, sellCount); 
		return simulationData;
	}
}
