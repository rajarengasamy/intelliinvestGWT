package com.intelliinvest.client.data;

import java.util.HashMap;
import java.util.HashSet;

public class StockDetailStaticHolder {
	
	public static HashMap<String, StockDetailData> stockDetailsMap = new HashMap<String, StockDetailData>();
//	public static HashMap<String, StockDetailData> intelliStockDetailsMap = new HashMap<String, StockDetailData>();
	public static HashMap<String, StockPriceData> stockCurrentPriceMap = new HashMap<String, StockPriceData>();
	public static HashMap<String, StockPriceData> stockPreviousPriceMap = new HashMap<String, StockPriceData>();
	public static HashMap<String, StockPriceData> stockEODPriceMap = new HashMap<String, StockPriceData>();
//	public static HashMap<String, SimulationData> stockSimulationMap = new HashMap<String, SimulationData>();
	public static HashMap<String, TradingAccountData> tradingAccountMap = new HashMap<String, TradingAccountData>();
	public static HashMap<String, OurSuggestionData> suggestionsMap = new HashMap<String, OurSuggestionData>();
//	public static HashMap<String, OurOptionSuggestionData> optionSuggestionsMap = new HashMap<String, OurOptionSuggestionData>();
	public static HashMap<String, StockPriceData> worldStockPriceMap = new HashMap<String, StockPriceData>();
	public static HashMap<String, String> worldStockDetailsMap = new HashMap<String, String>();
	public static HashMap<String, String> NSEToBSEMap = new HashMap<String, String>();
	public static HashMap<String, String> BSEToNSEMap = new HashMap<String, String>();
	public static HashSet<String> BOMStocksSet = new HashSet<String>();
	public static HashSet<String> BOMStocksSetTemp = new HashSet<String>();
	public static HashMap<String, StockPriceData> NIFTYStockPriceMap = new HashMap<String, StockPriceData>();
	public static HashMap<String, String> magicNumberMap = new HashMap<String, String>();
	
	public static String getName(String code){
		if(stockDetailsMap.containsKey(code)){
			return stockDetailsMap.get(code).getName();
		}else{
			return "N/A";
		}
	}
	
	public static Double getEODPrice(String code){
		
		if(stockEODPriceMap.containsKey(code)){
			return stockEODPriceMap.get(code).getPrice();
		}else{
			return 0D;
		}
	}

	public static Double getCurrentPrice(String code){
		if(stockCurrentPriceMap.containsKey(code)){
			return stockCurrentPriceMap.get(code).getPrice();
		}else{
			return 0D;
		}
	}
	
	public static Double getPreviousPrice(String code){
		if(stockPreviousPriceMap.containsKey(code)){
			return stockPreviousPriceMap.get(code).getPrice();
		}else{
			return 0D;
		}
	}
	
	public static Double getCP(String code){
		if(stockCurrentPriceMap.containsKey(code)){
			return stockCurrentPriceMap.get(code).getCp();
		}else{
			return 0D;
		}
	}
	
//	public static SimulationData getSimulationData(String code){
//		if(stockSimulationMap.containsKey(code)){
//			return stockSimulationMap.get(code);
//		}else{
//			return new SimulationData(code, 0D, 0D,0D, 0D);
//		}
//	}
	
}
