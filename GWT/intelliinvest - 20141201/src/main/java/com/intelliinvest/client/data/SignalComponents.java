package com.intelliinvest.client.data;

import java.util.Date;

@SuppressWarnings("serial")
public class SignalComponents implements IntelliInvestData{
	
	String symbol;
	Double TR;
	Double plusDM1 = 0D;
	Double minusDM1 = 0D;
	Double TRn = 0D;
	Double plusDMn = 0D;
	Double minusDMn = 0D;
	Double plusDIn = 0D;
	Double minusDIn = 0D;
	Double diffDIn = 0D;
	Double sumDIn = 0D;
	Double DX = 0D;
	Double ADXn = 0D;
	Double splitMultiplier = 0D;
	String previousSignalType = "";
	String signalType = "Wait";
	Date signalDate;
	String signalPresent;
	
	public SignalComponents() {
	}
	
	public String getSymbol() {
		return symbol;
	}
	
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	public Double getTR() {
		return TR;
	}

	public void setTR(Double tR) {
		TR = tR;
	}

	public Double getPlusDM1() {
		return plusDM1;
	}

	public void setPlusDM1(Double plusDM1) {
		this.plusDM1 = plusDM1;
	}

	public Double getMinusDM1() {
		return minusDM1;
	}

	public void setMinusDM1(Double minusDM1) {
		this.minusDM1 = minusDM1;
	}

	public Double getTRn() {
		return TRn;
	}

	public void setTRn(Double tRn) {
		TRn = tRn;
	}

	public Double getPlusDMn() {
		return plusDMn;
	}

	public void setPlusDMn(Double plusDMn) {
		this.plusDMn = plusDMn;
	}

	public Double getMinusDMn() {
		return minusDMn;
	}

	public void setMinusDMn(Double minusDMn) {
		this.minusDMn = minusDMn;
	}

	public Double getPlusDIn() {
		return plusDIn;
	}

	public void setPlusDIn(Double plusDIn) {
		this.plusDIn = plusDIn;
	}

	public Double getMinusDIn() {
		return minusDIn;
	}

	public void setMinusDIn(Double minusDIn) {
		this.minusDIn = minusDIn;
	}

	public Double getDiffDIn() {
		return diffDIn;
	}

	public void setDiffDIn(Double diffDIn) {
		this.diffDIn = diffDIn;
	}

	public Double getSumDIn() {
		return sumDIn;
	}

	public void setSumDIn(Double sumDIn) {
		this.sumDIn = sumDIn;
	}

	public Double getDX() {
		return DX;
	}

	public void setDX(Double dX) {
		DX = dX;
	}

	public Double getADXn() {
		return ADXn;
	}

	public void setADXn(Double aDXn) {
		ADXn = aDXn;
	}
	
	public String getPreviousSignalType() {
		return previousSignalType;
	}
	
	public void setPreviousSignalType(String previousSignalType) {
		this.previousSignalType = previousSignalType;
	}
	
	public String getSignalType() {
		return signalType;
	}
	
	public void setSignalType(String signalType) {
		this.signalType = signalType;
	}
	
	public String getSignalPresent() {
		return signalPresent;
	}
	
	public void setSignalPresent(String signalPresent) {
		this.signalPresent = signalPresent;
	}
	
	public Date getSignalDate() {
		return signalDate;
	}
	public void setSignalDate(Date signalDate) {
		this.signalDate = signalDate;
	}
	
	public Double getSplitMultiplier() {
		return splitMultiplier;
	}
	
	public void setSplitMultiplier(Double splitMultiplier) {
		this.splitMultiplier = splitMultiplier;
	}
	
	@Override
	public String toString() {
		String s = 
		" ***************** " +
		" " + TR +
		" " + plusDM1 +
		" " + minusDM1 +
		" " + TRn +
		" " + plusDMn +
		" " + minusDMn +
		" " + plusDIn +
		" " + minusDIn +
		" " + diffDIn +
		" " + sumDIn +
		" " + DX +
		" " + ADXn +
		" " + splitMultiplier +
		" " + previousSignalType +
		" " + signalType +
		" " +  signalDate +
		" " +  signalPresent;
		return s;
	}
	
}
