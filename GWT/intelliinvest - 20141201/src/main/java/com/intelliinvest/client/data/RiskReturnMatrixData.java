package com.intelliinvest.client.data;



@SuppressWarnings("serial")
public class RiskReturnMatrixData implements IntelliInvestData{
	String code;
	String signalType;
	String type3m;
	String type6m;
	String type9m;
	String type12m;
	
	public RiskReturnMatrixData() {
	}
	
	public RiskReturnMatrixData(String code, String signalType, String type3m,
			String type6m, String type9m, String type12m) {
		super();
		this.code = code;
		this.signalType = signalType;
		this.type3m = type3m;
		this.type6m = type6m;
		this.type9m = type9m;
		this.type12m = type12m;
	}


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSignalType() {
		return signalType;
	}

	public void setSignalType(String signalType) {
		this.signalType = signalType;
	}

	public String getType3m() {
		return type3m;
	}

	public void setType3m(String type3m) {
		this.type3m = type3m;
	}

	public String getType6m() {
		return type6m;
	}

	public void setType6m(String type6m) {
		this.type6m = type6m;
	}

	public String getType9m() {
		return type9m;
	}

	public void setType9m(String type9m) {
		this.type9m = type9m;
	}

	public String getType12m() {
		return type12m;
	}

	public void setType12m(String type12m) {
		this.type12m = type12m;
	}

	@Override
	public String toString() {		
		try{
			return 
			"{" 
			+ "\"code\":\"" + code + "\","
			+ "\"signal\":\"" +  signalType + "," 
			+ "\"type3m\":\"" +  type3m + "," 
			+ "\"type6m\":\"" +  type6m + "," 
			+ "\"type9m\":\"" +  type9m + "," 
			+ "\"type12m\":\"" +  type12m + "" 
			+ "}";			
		}catch (Exception e) {
			return null;
		}
		finally{			
		}
	}
	@Override
	public boolean equals(Object obj) {
		RiskReturnMatrixData riskReturnMatrixData = (RiskReturnMatrixData) obj;
		if(riskReturnMatrixData.code.equals(this.code) && riskReturnMatrixData.code.equals(this.code)){
			return true;
		}else{
			return false;
		}
	}
	
	public RiskReturnMatrixData clone() {
		RiskReturnMatrixData simulationData = new RiskReturnMatrixData(code, signalType, type3m, type6m, type9m, type12m); 
		return simulationData;
	}
}
