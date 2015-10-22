package com.intelliinvest.server.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import com.intelliinvest.client.data.BhavData;
import com.intelliinvest.client.data.SignalComponents;
import com.intelliinvest.server.IntelliInvestStore;

public class SignalComponentsEnhancer{
	
	int ma;
	
	public SignalComponentsEnhancer(int ma) {
		this.ma = ma;
	}

	SignalComponents init9(BhavData bhavData, BhavData bhavData_1){
		SignalComponents signalComponents = new SignalComponents();
		signalComponents.setSymbol(bhavData.getSymbol());
		signalComponents.setSignalDate(bhavData.getTimeStamp());
		
		Double high = bhavData.getHigh();
		Double low = bhavData.getLow();
//		Double close = bhavData.getClose();
		
		Double high_1 = bhavData_1.getHigh();
		Double low_1 = bhavData_1.getLow();
		Double close_1 = bhavData_1.getClose();
		
		Double high_low = high-low;
		Double high_close = high-close_1;
		Double low_close = low-close_1;
		signalComponents.setTR(max(high_low, high_close, low_close));
		
		//plusDM1
		if((high-high_1)> (low_1-low)){
			signalComponents.setPlusDM1(max(high-high_1, 0D));
		}
		
		//minusDM1
		if((low_1-low) > (high-high_1)){
			signalComponents.setMinusDM1(max(low_1 - low, 0D));
		}
		
		signalComponents.setSplitMultiplier(1D);
		signalComponents.setSignalPresent("N");
		
		return signalComponents;
	}
	
	SignalComponents init10(Integer magicNumber, List<BhavData> bhavDatas, List<SignalComponents> signals){
		BhavData bhavData = bhavDatas.get(bhavDatas.size()-1);
		BhavData bhavData_1 = bhavDatas.get(bhavDatas.size()-2);
		SignalComponents prevSignalComponents = signals.get(signals.size()-1);
		SignalComponents signalComponents = new SignalComponents();
		signalComponents.setSymbol(bhavData.getSymbol());
		signalComponents.setSignalDate(bhavData.getTimeStamp());
		Double TRnTmp = 0D;
		Double plusDMnTmp = 0D;
		Double minusDMnTmp = 0D;
		
		for(SignalComponents signalComponentsTmp: signals){
			TRnTmp = TRnTmp + signalComponentsTmp.getTR();
			plusDMnTmp = plusDMnTmp + signalComponentsTmp.getPlusDM1();
			minusDMnTmp = minusDMnTmp + signalComponentsTmp.getMinusDM1();
		}
		
		Double high_1 = bhavData_1.getHigh() * prevSignalComponents.getSplitMultiplier();
		Double low_1 = bhavData_1.getLow() * prevSignalComponents.getSplitMultiplier();
		Double close_1 = bhavData_1.getClose() * prevSignalComponents.getSplitMultiplier();
		
		Double open = bhavData.getOpen()* prevSignalComponents.getSplitMultiplier();
		
		if(open<close_1){
			Double multiplier = close_1/open;
			BigDecimal splitMultiplier = new BigDecimal(multiplier).setScale(0, RoundingMode.HALF_UP);
			if(splitMultiplier.intValue()>=2){
				signalComponents.setSplitMultiplier(splitMultiplier.doubleValue() * prevSignalComponents.getSplitMultiplier());
			}else{
				signalComponents.setSplitMultiplier(prevSignalComponents.getSplitMultiplier());
			}
//			System.out.println(" calculated split multiplier for " + close_1 + "  " + open + "  " + splitMultiplier + " is " + signalComponents.getSplitMultiplier());
		}else{
			Double multiplier = open/close_1;
			BigDecimal splitMultiplier = new BigDecimal(multiplier).setScale(0, RoundingMode.HALF_UP);
			if(splitMultiplier.intValue()>=2){
				signalComponents.setSplitMultiplier( prevSignalComponents.getSplitMultiplier()/splitMultiplier.doubleValue());
			}else{
				signalComponents.setSplitMultiplier(prevSignalComponents.getSplitMultiplier());
			}
//			System.out.println(" calculated split multiplier for " + close_1 + "  " + open + "  " + splitMultiplier + " is " + signalComponents.getSplitMultiplier());
		}
		
		Double high = bhavData.getHigh() * signalComponents.getSplitMultiplier();
		Double low = bhavData.getLow() * signalComponents.getSplitMultiplier();
//		Double close = bhavData.getClose() * signalComponents.getSplitMultiplier();

		Double high_low = high-low;
		Double high_close = Math.abs(high-close_1);
		Double low_close = Math.abs(low-close_1);
		
		//TR
		signalComponents.setTR(max(high_low, high_close, low_close));
		
		//plusDM1
		if((high-high_1)> (low_1-low)){
			signalComponents.setPlusDM1(max(high-high_1, 0D));
		}
		
		//minusDM1
		if((low_1-low) > (high-high_1)){
			signalComponents.setMinusDM1(max(low_1 - low, 0D));
		}
			
		signalComponents.setTRn(TRnTmp);
			
			//plusDMn 
		signalComponents.setPlusDMn(plusDMnTmp);
			
			//minusDMn
		signalComponents.setMinusDMn(minusDMnTmp);
		
			//plusDIn 
		signalComponents.setPlusDIn(100 * (plusDMnTmp/TRnTmp));
			
			//minusDIn 
		signalComponents.setMinusDIn(100 * (minusDMnTmp/TRnTmp));
			
			//diffDIn
		signalComponents.setDiffDIn(signalComponents.getPlusDIn() - signalComponents.getMinusDIn());
			
			//sumDIn
		signalComponents.setSumDIn(signalComponents.getPlusDIn() + signalComponents.getMinusDIn());
			
			//DX
		signalComponents.setDX(100 * (signalComponents.getDiffDIn()/signalComponents.getSumDIn()) );
		
		Double previousVolumeAverage = 0D;
		for(int i=0; i<bhavDatas.size()-1; i++){
			previousVolumeAverage += bhavDatas.get(i).getTotTrdQty();
		}
		
		previousVolumeAverage = previousVolumeAverage/(bhavDatas.size()-1D);
		
		Double volumeWeightage = bhavData.getTotTrdQty()/previousVolumeAverage;
		if(volumeWeightage>0D && volumeWeightage<1D){
			volumeWeightage = 1D;
		}else if(volumeWeightage>1D && volumeWeightage<2D){
			volumeWeightage = 1.1D;
		}else if(volumeWeightage>2D && volumeWeightage<4D){
			volumeWeightage = 1.2D;
		}else{
			volumeWeightage = 1.3D;
		}
		
		//ADX
		signalComponents.setADXn(signalComponents.getDX());
		
		Double delta = new Double(IntelliInvestStore.properties.getProperty("delta"));
		
		//signal
		String signal = "";
		if(signalComponents.getADXn()>magicNumber && (signalComponents.getPlusDIn()-signalComponents.getMinusDIn())>delta){
			signal = "Buy";
		}else if(signalComponents.getADXn()>magicNumber && (signalComponents.getPlusDIn()-signalComponents.getMinusDIn())<(-1* delta)){
			signal = "Sell";
		}else{
			if(prevSignalComponents.getSignalType().equals("Buy") || prevSignalComponents.getSignalType().equals("Hold")){
				signal = "Hold";
			}else{
				signal = "Wait";
			}
		}
		signalComponents.setSignalType(signal);
		
		String signalPresent = "N";
//		if(signal.equals("Buy") && (prevSignalComponents.getSignal().equals("Buy") || prevSignalComponents.getSignal().equals("Hold"))){
//			signalPresent = "N";
//		}else if(signal.equals("Hold") && (prevSignalComponents.getSignal().equals("Buy") || prevSignalComponents.getSignal().equals("Hold"))){
//			signalPresent = "N";
//		}else if(signal.equals("Sell") && (prevSignalComponents.getSignal().equals("Sell") || prevSignalComponents.getSignal().equals("Wait"))){
//			signalPresent = "N";
//		}else if(signal.equals("Wait") && (prevSignalComponents.getSignal().equals("Sell") || prevSignalComponents.getSignal().equals("Wait"))){
//			signalPresent = "N";
//		}
		
		signalComponents.setSignalPresent(signalPresent);
		
//		System.out.println(signalComponents);
		return signalComponents;
	}
	
	public static void main(String[] args) {
		Double close_1 =1250D;
		Double open = 240D;
		if(open<close_1){
			Double multiplier = close_1/open;
			BigDecimal splitMultiplier = new BigDecimal(multiplier).setScale(0, RoundingMode.HALF_UP);
			if(splitMultiplier.intValue()>=2){
				System.out.println(" calculated split multiplier for " + close_1 + "  " + open + "  " + splitMultiplier + " is " + (splitMultiplier.doubleValue() * 1D));
			}
		}else{
			Double multiplier = open/close_1;
			BigDecimal splitMultiplier = new BigDecimal(multiplier).setScale(0, RoundingMode.HALF_UP);
			if(splitMultiplier.intValue()>=2){
				System.out.println(" calculated split multiplier for " + close_1 + "  " + open + "  " + splitMultiplier + " is " + (1D/splitMultiplier.doubleValue()));
			}
		}
	}
	
	SignalComponents init(Integer magicNumber, List<BhavData> bhavDatas, SignalComponents prevSignalComponents){
		BhavData bhavData = bhavDatas.get(bhavDatas.size()-1);
		BhavData bhavData_1 = bhavDatas.get(bhavDatas.size()-2);
		SignalComponents signalComponents = new SignalComponents();
		signalComponents.setSymbol(bhavData.getSymbol());
		signalComponents.setSignalDate(bhavData.getTimeStamp());
		
		Double high_1 = bhavData_1.getHigh() * prevSignalComponents.getSplitMultiplier();
		Double low_1 = bhavData_1.getLow() * prevSignalComponents.getSplitMultiplier();
		Double close_1 = bhavData_1.getClose() * prevSignalComponents.getSplitMultiplier();
		
		Double open = bhavData.getOpen()* prevSignalComponents.getSplitMultiplier();
		
		if(open<close_1){
			Double multiplier = close_1/open;
			BigDecimal splitMultiplier = new BigDecimal(multiplier).setScale(0, RoundingMode.HALF_UP);
			if(splitMultiplier.intValue()>=2){
				signalComponents.setSplitMultiplier(splitMultiplier.doubleValue() * prevSignalComponents.getSplitMultiplier());
			}else{
				signalComponents.setSplitMultiplier(prevSignalComponents.getSplitMultiplier());
			}
//			System.out.println(" calculated split multiplier for " + close_1 + "  " + open + "  " + splitMultiplier + " is " + signalComponents.getSplitMultiplier());
		}else{
			Double multiplier = open/close_1;
			BigDecimal splitMultiplier = new BigDecimal(multiplier).setScale(0, RoundingMode.HALF_UP);
			if(splitMultiplier.intValue()>=2){
				signalComponents.setSplitMultiplier( prevSignalComponents.getSplitMultiplier()/splitMultiplier.doubleValue());
			}else{
				signalComponents.setSplitMultiplier(prevSignalComponents.getSplitMultiplier());
			}
//			System.out.println(" calculated split multiplier for " + close_1 + "  " + open + "  " + splitMultiplier + " is " + signalComponents.getSplitMultiplier());
		}
		
		Double high = bhavData.getHigh() * signalComponents.getSplitMultiplier();
		Double low = bhavData.getLow() * signalComponents.getSplitMultiplier();
//		Double close = bhavData.getClose() * signalComponents.getSplitMultiplier();
		
		Double high_low = high-low;
		Double high_close = Math.abs(high-close_1);
		Double low_close = Math.abs(low-close_1);
		
		//TR
		signalComponents.setTR(max(high_low, high_close, low_close));
		
		//plusDM1
		if((high-high_1)> (low_1-low)){
			signalComponents.setPlusDM1(max(high-high_1, 0D));
		}
		
		//minusDM1
		if((low_1-low) > (high-high_1)){
			signalComponents.setMinusDM1(max(low_1 - low, 0D));
		}
		
		//TRn
		signalComponents.setTRn((prevSignalComponents.getTRn() * (ma-1)/ma) + signalComponents.getTR());
		
		//plusDMn 
		signalComponents.setPlusDMn((prevSignalComponents.getPlusDMn() * (ma-1)/ma) + signalComponents.getPlusDM1());
		
		//minusDMn
		signalComponents.setMinusDMn((prevSignalComponents.getMinusDMn() * (ma-1)/ma) + signalComponents.getMinusDM1());
		
		//plusDIn 
		signalComponents.setPlusDIn(100 * (signalComponents.getPlusDMn()/signalComponents.getTRn()));
		
		//minusDIn 
		signalComponents.setMinusDIn(100 * (signalComponents.getMinusDMn()/signalComponents.getTRn()));
		
		//diffDIn
		signalComponents.setDiffDIn(Math.abs(signalComponents.getPlusDIn() - signalComponents.getMinusDIn()));
		
		//sumDIn
		signalComponents.setSumDIn(signalComponents.getPlusDIn() + signalComponents.getMinusDIn());
		
		//DX
		signalComponents.setDX(100 * (signalComponents.getDiffDIn()/signalComponents.getSumDIn()));
	
		Double previousVolumeAverage = 0D;
		for(int i=0; i<bhavDatas.size()-1; i++){
			previousVolumeAverage += bhavDatas.get(i).getTotTrdQty();
		}
		
		previousVolumeAverage = previousVolumeAverage/(bhavDatas.size()-1D);
		
		Double volumeWeightage = bhavData.getTotTrdQty()/previousVolumeAverage;
		
		if(volumeWeightage>0D && volumeWeightage<1D){
			volumeWeightage = 1D;
		}else if(volumeWeightage>1D && volumeWeightage<2D){
			volumeWeightage = 1.1D;
		}else if(volumeWeightage>2D && volumeWeightage<4D){
			volumeWeightage = 1.2D;
		}else{
			volumeWeightage = 1.3D;
		}
		
		//ADX
		signalComponents.setADXn( ( ( prevSignalComponents.getADXn() * ( (ma-1D)/ma) ) + ( signalComponents.getDX()*(1D/ma) ) ) * volumeWeightage );
		
		Double delta = new Double(IntelliInvestStore.properties.getProperty("delta"));
		//signal
		String signal = "";
		if(signalComponents.getADXn()>magicNumber && (signalComponents.getPlusDIn()-signalComponents.getMinusDIn())>delta){
			signal = "Buy";
		}else if(signalComponents.getADXn()>magicNumber && (signalComponents.getPlusDIn()-signalComponents.getMinusDIn())<(-1* delta)){
			signal = "Sell";
		}else{
			if(prevSignalComponents.getSignalType().equals("Buy") || prevSignalComponents.getSignalType().equals("Hold")){
				signal = "Hold";
			}else{
				signal = "Wait";
			}
		}
		
		signalComponents.setSignalType(signal);
		String signalPresent = "Y";
		if(signal.equals("Buy") && (prevSignalComponents.getSignalType().equals("Buy") || prevSignalComponents.getSignalType().equals("Hold"))){
			signalPresent = "N";
		}else if(signal.equals("Hold") && (prevSignalComponents.getSignalType().equals("Buy") || prevSignalComponents.getSignalType().equals("Hold"))){
			signalPresent = "N";
		}else if(signal.equals("Sell") && (prevSignalComponents.getSignalType().equals("Sell") || prevSignalComponents.getSignalType().equals("Wait"))){
			signalPresent = "N";
		}else if(signal.equals("Wait") && (prevSignalComponents.getSignalType().equals("Sell") || prevSignalComponents.getSignalType().equals("Wait"))){
			signalPresent = "N";
		}
		
		signalComponents.setPreviousSignalType(prevSignalComponents.getSignalType());
		signalComponents.setSignalPresent(signalPresent);
		
//		System.out.println(signalComponents);
		
		return signalComponents;
	}
	
	Double max(Double value1, Double value2){
		if(value1>value2){
			return value1;
		}
		return value2;
		
	}
	Double max(Double value1, Double value2, Double value3){
		Double max;
		if(value1>value2){
			if(value1 > value3){
				max = value1;
			}else{
				max = value3;
			}
		}else{
			if(value2 > value3){
				max = value2;
			}else{
				max = value3;
			}
		}
		return max;
	}
	
}
