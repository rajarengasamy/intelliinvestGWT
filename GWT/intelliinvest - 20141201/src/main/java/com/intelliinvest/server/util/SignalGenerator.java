package com.intelliinvest.server.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.intelliinvest.client.data.BhavData;
import com.intelliinvest.client.data.MaginNumberPNLData;
import com.intelliinvest.client.data.SignalComponents;
import com.intelliinvest.client.data.StockDetailData;
import com.intelliinvest.client.data.StockDetailStaticHolder;
import com.intelliinvest.server.IntelliInvestStore;
import com.intelliinvest.server.dao.ChartDao;
import com.intelliinvest.server.dao.IntelliInvestDataDao;
import com.intelliinvest.server.dao.SignalComponentsDao;

public class SignalGenerator {
	private static Logger logger = Logger.getLogger(SignalGenerator.class);
	
	public static void generateSignals(String symbol) {
		Integer ma = new Integer(IntelliInvestStore.properties.get("ma").toString());
		generateSignals(symbol, ma);
	}
	public static void generateSignals(String symbol, int ma) {
		try{
			if("ALL".equals(symbol)){
				for(StockDetailData stockDetailData : StockDetailStaticHolder.stockDetailsMap.values()){
					try{
						logger.info("Calculating signals for symbol " + stockDetailData.getCode());
						List<SignalComponents> signalComponentsList = SignalGenerator.getSignals(stockDetailData.getCode(), ma);
						SignalComponentsDao.getInstance().updateSignalComponents(ma, stockDetailData.getCode(), signalComponentsList);
					}catch(Exception e){
						logger.info("Error generating signals for " + stockDetailData.getCode() + " with error " +  e.getMessage(), e);
					}
				}
				ChartDao.getInstance().insertSignals(ma, "ALL", null);
			}else{
				logger.info("Calculating signals for symbol " + symbol);
				List<SignalComponents> signalComponentsList = SignalGenerator.getSignals(symbol, ma);
				logger.info("signals generated for " + symbol );
				SignalComponentsDao.getInstance().updateSignalComponents(ma, symbol, signalComponentsList);
				ChartDao.getInstance().insertSignals(ma, symbol, null);
			}
		}catch(Exception e){
			logger.info("Error generating signal for " + symbol + " with error " +  e.getMessage(), e);
		}
		IntelliInvestStore.refresh();
	}
	
	public static List<SignalComponents> getSignals(String stockCode, Integer ma){
		Integer magicNumber = 45;
		String magicNumberStr = IntelliInvestStore.getMagicNumber(ma, stockCode);
		if(null!=magicNumberStr){
			magicNumber = new Integer(magicNumberStr);
		}
		List<BhavData> bhavDatas = IntelliInvestDataDao.getInstance().getBhavData(stockCode);
		BhavData bhavData_1 = new BhavData();
		int count = 0;
		List<SignalComponents> signals = new ArrayList<SignalComponents>();
		SignalComponents prevSignalComponents = null;
		SignalComponentsEnhancer  signalComponentsEnhancer = new SignalComponentsEnhancer(ma);
		for(BhavData bhavData : bhavDatas){
			if(count!=0){
				SignalComponents signalComponents = null;
				if(count <= ma){
					signalComponents = signalComponentsEnhancer.init9(bhavData, bhavData_1);
				}else if(count==(ma+1)){
					signalComponents = signalComponentsEnhancer.init10(magicNumber, bhavDatas.subList(count-ma+1, count), signals);
				}else{
					signalComponents = signalComponentsEnhancer.init(magicNumber, bhavDatas.subList(count-ma+1, count), prevSignalComponents);
				}
				if(null!=signalComponents){
					signals.add(signalComponents);
					prevSignalComponents = signalComponents;
				}
			}
			count++;
			bhavData_1 = bhavData;
		}
		
		return signals;
	}
	
	public static void generateTodaySignals(String symbol) {
		Integer ma = new Integer(IntelliInvestStore.properties.get("ma").toString());
		generateTodaySignals(symbol, ma);
	}
	public static void generateTodaySignals(String symbol, int ma) {
		try{
			Date businessDate = IntelliInvestDataDao.getInstance().getBhavDataMaxDate();
			if("ALL".equals(symbol)){
				Map<String, List<BhavData>> bhavDatas = IntelliInvestDataDao.getInstance().getBhavData(businessDate, ma-1);
				Map<String, SignalComponents> signalComponents = SignalComponentsDao.getInstance().getSignalComponent(businessDate, ma);
				for(StockDetailData stockDetailData : StockDetailStaticHolder.stockDetailsMap.values()){
					try{
						if(bhavDatas.containsKey(stockDetailData.getCode())  && signalComponents.containsKey(stockDetailData.getCode())){
							logger.info("Calculating signals for today for symbol " + stockDetailData.getCode());
							List<SignalComponents> signalComponentsList = 
									SignalGenerator.getTodaysSignals(stockDetailData.getCode(), ma, bhavDatas.get(stockDetailData.getCode()), signalComponents.get(stockDetailData.getCode()));
							SignalComponentsDao.getInstance().updateSignalComponents(false, ma, stockDetailData.getCode(), signalComponentsList);
						}
					}catch(Exception e){
						logger.info("Error generating signal for " + stockDetailData.getCode() + " with error " +  e.getMessage(), e);
					}
				}
				ChartDao.getInstance().insertSignals(ma, "ALL", businessDate);
			}else{
				List<SignalComponents> prevSignalComponents = SignalComponentsDao.getInstance().getSignalComponent(symbol, ma);
				if(prevSignalComponents.size()>0){
					logger.info("Calculating signals for today for symbol " + symbol);
					List<BhavData> bhavData = IntelliInvestDataDao.getInstance().getBhavData(symbol, ma-1);
					List<SignalComponents> signalComponentsList = SignalGenerator.getTodaysSignals(symbol, ma, bhavData, prevSignalComponents.get(0));
					SignalComponentsDao.getInstance().updateSignalComponents(false, ma, symbol, signalComponentsList);
				}
				ChartDao.getInstance().insertSignals(ma, symbol, businessDate);
			}
		}catch(Exception e){
			logger.info("Error generating signal for " + symbol + " with error " + e.getMessage() , e);
		}
		IntelliInvestStore.refresh();
	}
	
	public static List<SignalComponents> getTodaysSignals(String symbol, Integer ma, List<BhavData> bhavDatasTmp, SignalComponents prevSignalComponents){
//		List<BhavData> bhavDatasTmp = IntelliInvestDataDao.getInstance().getBhavData(symbol, ma-1);
		List<BhavData> bhavDatas = new ArrayList<BhavData>();
		for(int i=(bhavDatasTmp.size()-1); i>=0; i--){
			bhavDatas.add(bhavDatasTmp.get(i));
		}
//		List<SignalComponents> prevSignalComponents = SignalComponentsDao.getInstance().getSignalComponent(symbol, ma);
		ArrayList<SignalComponents> signalComponentsList = new ArrayList<SignalComponents>();
		Integer magicNumber = 45;
		String magicNumberStr = IntelliInvestStore.getMagicNumber(ma, symbol);
		if(null!=magicNumberStr){
			magicNumber = new Integer(magicNumberStr);
		}
		SignalComponentsEnhancer signalComponentsEnhancer = new SignalComponentsEnhancer(ma);
		SignalComponents signalComponents = signalComponentsEnhancer.init(magicNumber, bhavDatas, prevSignalComponents);
		logger.info("Todays signal for " + signalComponents.getSymbol() + " Signal type " + signalComponents.getSignalType() + " signal present " + signalComponents.getSignalPresent());
		signalComponentsList.add(signalComponents);
		return signalComponentsList;
	}
	
	public static void generateMagicNumber(String symbol) {
		Integer ma = new Integer(IntelliInvestStore.properties.get("ma").toString());
		generateMagicNumber(symbol, ma);
	}
	public static void generateMagicNumber(String symbol, int ma) {
		
		try{
			if("ALL".equals(symbol)){
				for(StockDetailData stockDetailData : StockDetailStaticHolder.stockDetailsMap.values()){
					try{
						MaginNumberPNLData maginNumberPNLData = SignalGenerator.getMagicNumber(stockDetailData.getCode(), ma);
						SignalComponentsDao.getInstance().updateMagicNumbers(ma, stockDetailData.getCode(), maginNumberPNLData.getMagicNumber(), maginNumberPNLData.getPNL());
					}catch(Exception e){
						logger.info("Error generating magic number for " + stockDetailData.getCode() );
					}
				}
				IntelliInvestStore.refresh();
			}else{
				try{
					MaginNumberPNLData maginNumberPNLData = SignalGenerator.getMagicNumber(symbol, ma);
					SignalComponentsDao.getInstance().updateMagicNumbers(ma, symbol, maginNumberPNLData.getMagicNumber(), maginNumberPNLData.getPNL());
				}catch(Exception e){
					logger.info("Error generating magic number for " + symbol  + " with error " + e.getMessage() , e);
				}
			}
			
		}catch(Exception e){
			logger.info("Error generating magic number for " + symbol + " with error " + e.getMessage() , e);
		}
		IntelliInvestStore.refresh();
	}
	
	public static MaginNumberPNLData getMagicNumber(String stockCode, Integer ma){
		Integer magicNumber = 45;
		Double PNL = 0D;
		try{
			List<BhavData> bhavDatas = IntelliInvestDataDao.getInstance().getBhavData(stockCode);
			Map<Integer, Double> maPnlMap = new HashMap<Integer, Double>();
			for(int i=20; i<=60;i++){
				BhavData bhavData_1 = new BhavData();
				int count = 0;
				List<SignalComponents> signals = new ArrayList<SignalComponents>();
				SignalComponents prevSignalComponents = null;
				Map<Date, BhavData> priceMap = new HashMap<Date, BhavData>();
				List<SignalComponents> signalPresentList = new ArrayList<SignalComponents>();
				SignalComponentsEnhancer  signalComponentsEnhancer = new SignalComponentsEnhancer(ma);
				for(BhavData bhavData : bhavDatas){
					if(count!=0){
						SignalComponents signalComponents = null;
						if(count <= ma){
							signalComponents = signalComponentsEnhancer.init9(bhavData, bhavData_1);
						}else if(count==ma+1){
							signalComponents = signalComponentsEnhancer.init10(i, bhavDatas.subList(count-ma+1, count), signals);
						}else{
							signalComponents = signalComponentsEnhancer.init(i, bhavDatas.subList(count-ma+1, count), prevSignalComponents);
						}
						if(null!=signalComponents){
							signals.add(signalComponents);
							prevSignalComponents = signalComponents;
							if(signalComponents.getSignalPresent().equalsIgnoreCase("Y")){
								priceMap.put(bhavDatas.get(count-1).getTimeStamp(), bhavDatas.get(count-1));
								signalPresentList.add(signalComponents);
							}
						}
						
					}
					count++;
					bhavData_1 = bhavData;
				}
				Double pnl = getPnl(priceMap, signalPresentList, signals.get(signals.size()-1), bhavDatas.get(bhavDatas.size()-1));
				maPnlMap.put(i, pnl);
			}
			magicNumber = getMaxPnl(maPnlMap);
			PNL =  maPnlMap.get(magicNumber);
			logger.info("Magic number for " + stockCode + " is " + magicNumber + " with pnl " + maPnlMap.get(magicNumber));
		}catch(Exception e){
			logger.info("Error generating Magic number for " + stockCode );
		}
		return new MaginNumberPNLData(magicNumber, PNL);
	}
	
	private static Integer getMaxPnl(Map<Integer, Double> maPnlMap){
		Double maxPnl = 0D;
		Integer magicNumberWithMaxPNL = -1;
		boolean isFirst = true;
		for(Integer magicNumber : maPnlMap.keySet()){
			if(isFirst){
				maxPnl = maPnlMap.get(magicNumber);
				magicNumberWithMaxPNL = magicNumber;
				isFirst = false;
			}
			if(maPnlMap.get(magicNumber)>maxPnl){
				maxPnl = maPnlMap.get(magicNumber);
				magicNumberWithMaxPNL = magicNumber;
			}
		}
		return magicNumberWithMaxPNL;
	}
	private static Double getPnl(Map<Date, BhavData> priceMap, List<SignalComponents> signalPresentList, SignalComponents signalComponent, BhavData lastBhav){
		Double pnl = 0D;
		SignalComponents prevSignalComponents = null;
		for(SignalComponents signalComponents : signalPresentList){
			if(!signalComponents.getSignalType().equalsIgnoreCase("Buy") && null==prevSignalComponents){
				
			}else if(signalComponents.getSignalType().equalsIgnoreCase("Buy")){
				prevSignalComponents = signalComponents;
				Double price = priceMap.get(signalComponents.getSignalDate()).getClose() * signalComponents.getSplitMultiplier();
				pnl = pnl - price;
			}else if(!signalComponents.getSignalType().equalsIgnoreCase("Buy")){
				prevSignalComponents = signalComponents;
				Double price = priceMap.get(signalComponents.getSignalDate()).getClose() * signalComponents.getSplitMultiplier();
				pnl = pnl + price;
			}
		}
		
		if(null!=prevSignalComponents && prevSignalComponents.getSignalType().equalsIgnoreCase("Buy")){
			Double price = lastBhav.getClose() * signalComponent.getSplitMultiplier();
			pnl = pnl + price;
		}
		return pnl;
	}
	
}


