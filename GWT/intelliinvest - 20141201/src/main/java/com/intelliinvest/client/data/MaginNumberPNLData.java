package com.intelliinvest.client.data;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MaginNumberPNLData implements Serializable, IntelliInvestData{
	public Integer magicNumber;
	public Double PNL;

	public MaginNumberPNLData() {
	}
	
	
	public MaginNumberPNLData(Integer magicNumber, Double pNL) {
		super();
		this.magicNumber = magicNumber;
		PNL = pNL;
	}

	public Integer getMagicNumber() {
		return magicNumber;
	}


	public void setMagicNumber(Integer magicNumber) {
		this.magicNumber = magicNumber;
	}


	public Double getPNL() {
		return PNL;
	}


	public void setPNL(Double pNL) {
		PNL = pNL;
	}


	@Override
	public String toString() {
		return 
		"{" 
		+ "\"magicNumber\":\"" + magicNumber + "\"," 
		+ "\"PNL\":\"" + PNL + "\"" 
		+ "}";
	}
}
