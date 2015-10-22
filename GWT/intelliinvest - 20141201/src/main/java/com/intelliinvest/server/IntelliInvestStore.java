package com.intelliinvest.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.intelliinvest.client.data.EODVolumeChartData;
import com.intelliinvest.client.data.IdValueData;
import com.intelliinvest.client.data.OurOptionSuggestionData;
import com.intelliinvest.client.data.OurSuggestionData;
import com.intelliinvest.client.data.SimulationData;
import com.intelliinvest.client.data.StockDetailData;
import com.intelliinvest.client.data.StockDetailStaticHolder;
import com.intelliinvest.client.data.StockPriceData;
import com.intelliinvest.client.data.TradingAccountData;
import com.intelliinvest.server.dao.OurSuggestionDao;
import com.intelliinvest.server.dao.SignalComponentsDao;
//import com.intelliinvest.server.dao.SimulationDao;
import com.intelliinvest.server.dao.StockDetailsDao;
import com.intelliinvest.server.dao.TradingAccountDao;

public class IntelliInvestStore {
	
	private static Logger logger = Logger.getLogger(IntelliInvestStore.class);
	
	final static String GOOGLE_QUOTE_URL = "https://www.google.com/finance/info?q=#CODE#";
	
	final static String GOOGLE_REALTIME_QUOTE_URL = "http://www.google.com/finance/getprices?q=#CODE#&x=#EXCHANGE#&i=120&p=1d&f=d,c,v&df=cpct";
	
	static Boolean REFRESH_PERIODICALLY = false;
	
	public static Properties properties = null;
	
	static{
		loadProperties();
		logger.info("Setting REFRESH_PERIODICALLY to " + new Boolean(properties.getProperty("refresh.periodically")));
		REFRESH_PERIODICALLY = new Boolean(properties.getProperty("refresh.periodically"));
		initializeTimers();
		initialize();
	}
	
	private static void initializeTimers(){
		
		Timer timerForCurrentPrice = new Timer("refreshTimerCurrentPrice");
		timerForCurrentPrice.schedule(new TimerTask() {
			@Override
			public void run() {
				if(REFRESH_PERIODICALLY){
					logger.info("refreshing current price data for stocks and world indexes");
					try{
						updateCurrentPrices();
						updateWorldPrices();
					}catch(Exception e){
						logger.info("Error refreshing current price data for stocks and world indexes " + e.getMessage());
					}
				}else{
					logger.info("refreshing of all price data for IntelliInvest is disabled");
				}
			}
		}, new Integer(properties.getProperty("price.refresh.interval")), new Integer(properties.getProperty("price.refresh.interval")));
		
		logger.info("added timer for refreshing prices");
		Timer timerForIntelliInvestData = new Timer("refreshTimer");
		timerForIntelliInvestData.schedule(new TimerTask() {
			@Override
			public void run() {
				if(REFRESH_PERIODICALLY){
					logger.info("refreshing IntelliInvest data");
					try{
						refresh();
					}catch(Exception e){
						logger.info("Error refreshing IntelliInvest data " + e.getMessage());
					}
				}else{
					logger.info("refreshing of all data for IntelliInvest is disabled");
				}
			}
		}, new Integer(properties.getProperty("static.refresh.interval")), new Integer(properties.getProperty("static.refresh.interval")));
		logger.info("added timer for refreshing static data");
		
		Long secondsInOneDay = 86400000L;
		
		Timer timerToStartRefresh = new Timer("timerToStartRefresh");
		Calendar startCal = Calendar.getInstance();
		startCal.set(Calendar.HOUR_OF_DAY, 9);
		startCal.set(Calendar.MINUTE, 0);
		startCal.set(Calendar.SECOND, 0);
		startCal.set(Calendar.MILLISECOND, 0);
		
		timerToStartRefresh.schedule(new TimerTask() {
			
			@Override
			public void run() {
				Calendar cal = Calendar.getInstance();
				REFRESH_PERIODICALLY = true;
				if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
					logger.info("Setting REFRESH_PERIODICALLY to false from timer because of saturday or sunday");
					REFRESH_PERIODICALLY = false;
				}else{
					logger.info("Setting REFRESH_PERIODICALLY to true from timer");
				}
			}
		}, startCal.getTime(), secondsInOneDay);
		
		logger.info("added timerToStartRefresh for start refreshing prices after 9 am");
		
		Timer timerToStopRefresh = new Timer("timerToStopRefresh");
		Calendar endCal = Calendar.getInstance();
		endCal.set(Calendar.HOUR_OF_DAY, 16);
		endCal.set(Calendar.MINUTE, 0);
		endCal.set(Calendar.SECOND, 0);
		endCal.set(Calendar.MILLISECOND, 0);
		
		timerToStopRefresh.schedule(new TimerTask() {
			
			@Override
			public void run() {
				logger.info("Setting REFRESH_PERIODICALLY to false from timer");
				REFRESH_PERIODICALLY = false;
			}
		}, endCal.getTime(), secondsInOneDay);
		
		logger.info("added timerToStopRefresh for stop refreshing prices after 4 pm");
	}
	
	private static void initializeNewTimers(){
		Integer refreshInterval = new Integer(properties.getProperty("refresh.interval"));
		Timer refreshTimer = new Timer("refreshTimer");
		refreshTimer.schedule(
			new TimerTask() {
				Integer lastHourlyRun = -1;
				@Override
				public void run() {
					Calendar calendar = Calendar.getInstance();
					if(REFRESH_PERIODICALLY){
						if(calendar.get(Calendar.HOUR_OF_DAY)>=8 || calendar.get(Calendar.HOUR_OF_DAY)<=6){
							if(lastHourlyRun!=calendar.get(Calendar.HOUR_OF_DAY)){
								logger.info("refreshing IntelliInvest data");
								try{
									refresh();
								}catch(Exception e){
									logger.info("Error refreshing IntelliInvest data " + e.getMessage());
								}
								lastHourlyRun = calendar.get(Calendar.HOUR_OF_DAY);
							}
						}
						if(calendar.get(Calendar.HOUR_OF_DAY)>=9 || calendar.get(Calendar.HOUR_OF_DAY)<=4){
							logger.info("refreshing current price data for stocks and world indexes");
							try{
								updateCurrentPrices();
								updateWorldPrices();
							}catch(Exception e){
								logger.info("Error refreshing current price data for stocks and world indexes " + e.getMessage());
							}
						}
					}else{
						logger.info("refreshing of all price data for IntelliInvest is disabled");
					}
				}
			}, 0, refreshInterval);
		logger.info("added timer for refresh");
	}
	
	public static void initialize(){
		refresh();
		logger.info("Initialised IntelliInvestStore");
	}
	
	private static void loadProperties() {
		try{
			logger.info("Loading property file for intelliinvest");
			properties = new Properties();
			properties.load(IntelliInvestStore.class.getResourceAsStream("/intelliinvest.properties"));
			logger.info("Loaded property file " + properties);
		}catch(Exception e){
			logger.info("Error loading properties  " + e.getMessage());
			throw new RuntimeException(e);
		}
		
	}
	
	public static void refresh(){
		StockDetailStaticHolder.stockDetailsMap = getStockDetailMapFromList(StockDetailsDao.getInstance().getStockDetails());
//		StockDetailStaticHolder.intelliStockDetailsMap = getStockDetailMapFromList(StockDetailsDao.getInstance().getIntelliStockDetails());
		StockDetailStaticHolder.stockCurrentPriceMap = getStockPriceMapFromList(StockDetailsDao.getInstance().getCurrentStockPrices());
		StockDetailStaticHolder.stockEODPriceMap = getStockPriceMapFromList(StockDetailsDao.getInstance().getEODStockPrices());
//		StockDetailStaticHolder.stockSimulationMap = getSimulationMapFromList(SimulationDao.getInstance().getAllSimulationData());
		StockDetailStaticHolder.tradingAccountMap = getTradingAccountMapFromList(TradingAccountDao.getInstance().getAllTradingAccountData());
		StockDetailStaticHolder.suggestionsMap = getOurSuggestionMapFromList(OurSuggestionDao.getInstance().getAllSuggestionsData());
//		StockDetailStaticHolder.optionSuggestionsMap = getOurOptionSuggestionMapFromList(OurSuggestionDao.getInstance().getAllOptionSuggestionsData());
		StockDetailStaticHolder.worldStockPriceMap = getStockPriceMapFromList(StockDetailsDao.getInstance().getWorldStockPrices()); 
		StockDetailStaticHolder.worldStockDetailsMap = getMapFromList(StockDetailsDao.getInstance().getWorldStockDetails());
		StockDetailStaticHolder.NSEToBSEMap = getMapFromList(StockDetailsDao.getInstance().getNSEToBSEMap());
		StockDetailStaticHolder.BSEToNSEMap = getMapFromListReverse(StockDetailsDao.getInstance().getNSEToBSEMap());
		StockDetailStaticHolder.NIFTYStockPriceMap = getStockPriceMapForNifty(StockDetailsDao.getInstance().getNiftyStocks());
		StockDetailStaticHolder.magicNumberMap = getMapFromList(SignalComponentsDao.getInstance().getMagicNumberMap());
		logger.info("refreshed IntelliInvestStore data");
	}
	
	public static String getMagicNumber(int ma, String symbol){
		return StockDetailStaticHolder.magicNumberMap.get(symbol + "_" + ma);
	}
	public static void updateCurrentPrices(){
		List<StockPriceData> currentPrices = getCurrentPrices();
		currentPrices = StockDetailsDao.getInstance().updateCurrentStockPrices(currentPrices);
		StockDetailStaticHolder.stockCurrentPriceMap = getStockPriceMapFromList(currentPrices);
	}
	
	public static void updateWorldPrices(){
		List<StockPriceData> worldPrices = getWorldStockPrices();
		worldPrices = StockDetailsDao.getInstance().updateWorldStockPrices(worldPrices);
		StockDetailStaticHolder.worldStockPriceMap = getStockPriceMapFromList(worldPrices);
	}
	
	private static HashMap<String, StockPriceData> getStockPriceMapForNifty(HashSet<String> datas){
		HashMap<String, StockPriceData> dataMap = new HashMap<String, StockPriceData>();
		for(String data : datas){
				dataMap.put(data, StockDetailStaticHolder.stockCurrentPriceMap.get(data));
		}
		return dataMap;
	}
	
	private static HashMap<String, String> getMapFromList(List<IdValueData> datas){
		HashMap<String, String> map = new HashMap<String, String>();
		for(IdValueData data : datas){
			map.put(data.getId(), data.getValue());
		}
		return map;
	}
	private static HashMap<String, String> getMapFromListReverse(List<IdValueData> datas){
		HashMap<String, String> map = new HashMap<String, String>();
		for(IdValueData data : datas){
			map.put(data.getValue(), data.getId());
		}
		return map;
	}
	
	private static HashMap<String, StockDetailData> getStockDetailMapFromList(List<StockDetailData> datas){
		HashMap<String, StockDetailData> dataMap = new HashMap<String, StockDetailData>();
		for(StockDetailData data : datas){
				dataMap.put(data.getCode(), data);
		}
		return dataMap;
	}
	
	private static HashMap<String, StockPriceData> getStockPriceMapFromList(List<StockPriceData> datas){
		HashMap<String, StockPriceData> dataMap = new HashMap<String, StockPriceData>();
		for(StockPriceData data : datas){
				dataMap.put(data.getCode(), data);
		}
		return dataMap;
	}
	
	private static HashMap<String, SimulationData> getSimulationMapFromList(List<SimulationData> datas){
		HashMap<String, SimulationData> dataMap = new HashMap<String, SimulationData>();
		for(SimulationData data : datas){
				dataMap.put(data.getCode(), data);
		}
		return dataMap;
	}
	
	private static HashMap<String, TradingAccountData> getTradingAccountMapFromList(List<TradingAccountData> datas){
		HashMap<String, TradingAccountData> dataMap = new HashMap<String, TradingAccountData>();
		for(TradingAccountData data : datas){
				dataMap.put(data.getCode(), data);
		}
		return dataMap;
	}
	
	private static HashMap<String, OurSuggestionData> getOurSuggestionMapFromList(List<OurSuggestionData> datas){
		HashMap<String, OurSuggestionData> dataMap = new HashMap<String, OurSuggestionData>();
		for(OurSuggestionData data : datas){
				dataMap.put(data.getCode() + data.getSuggestionType(), data);
		}
		return dataMap;
	}
	
	private static HashMap<String, OurOptionSuggestionData> getOurOptionSuggestionMapFromList(List<OurOptionSuggestionData> datas){
		HashMap<String, OurOptionSuggestionData> dataMap = new HashMap<String, OurOptionSuggestionData>();
		for(OurOptionSuggestionData data : datas){
				dataMap.put(data.getCode() + data.getInstrument() + data.getExpiryDate() + data.getOptionType(), data);
		}
		return dataMap;
	}
		
	public static ArrayList<StockPriceData> getCurrentPrices(){
		StockDetailStaticHolder.BOMStocksSetTemp = new HashSet<String>();
		List<StockDetailData> stockDetails = StockDetailsDao.getInstance().getStockDetails();
		ArrayList<StockPriceData> stockCurrentPriceList = new ArrayList<StockPriceData>();  
		int start = -10;
		int end = 0;
		while(end<stockDetails.size()){
			start = start + 10;
			end = end + 10;
			if(end>stockDetails.size()){
				end = stockDetails.size();
			}
			stockCurrentPriceList.addAll(getStockPrice(stockDetails.subList(start, end)));
		}
		StockDetailStaticHolder.BOMStocksSet = StockDetailStaticHolder.BOMStocksSetTemp;
		logger.info("Stocks added for BOMStocksSet " + StockDetailStaticHolder.BOMStocksSet + " ");

		return stockCurrentPriceList;
	}
	
	public static List<StockPriceData> getStockPrice(List<StockDetailData> stockDetailDatas) {
		List<StockPriceData> stockCurrentPriceList = new ArrayList<StockPriceData>(); 
		String codes = "";
		try{
			for(StockDetailData stockDetailData :stockDetailDatas){
				String code = stockDetailData.getCode();
				code = "NSE:"+ stockDetailData.getCode();
				codes = codes + code  + ",";
			}
			String response = HttpUtil.getFromHttpUrlAsString(GOOGLE_QUOTE_URL.replace("#CODE#",  codes.replace("&", "%26")));
			stockCurrentPriceList.addAll(getPriceFromJSON("NSE", codes, response));
			return stockCurrentPriceList;
		}catch(Exception e){
			logger.info("Error fetching stock price in getStockPrice " + codes);
			return new ArrayList<StockPriceData>();
		}
	}
	
	private static List<StockPriceData> getPriceFromJSON(String exchange, String codes, String response){
		List<StockPriceData> stockCurrentPriceList = new ArrayList<StockPriceData>(); 
		SimpleDateFormat format = new SimpleDateFormat("MMM dd, hh:mmaa z");
		JSONArray jsonArray = JSONArray.fromObject( response.replaceFirst("//", "").trim() );  
		try{
			Calendar currentCal = Calendar.getInstance();
			currentCal.setTime(format.parse(format.format(new Date())));
			currentCal.add(Calendar.MONTH, -1);
			for(int i=0; i<jsonArray.size();i++){
				JSONObject stockObject = (JSONObject)jsonArray.get(i);
				String code = stockObject.getString("t").replace("\\x26", "&");
				try{
					Double price =  new Double(stockObject.getString("l_fix").replaceAll(",", ""));
					Double cp = new Double(stockObject.getString("cp").replaceAll(",", ""));
					String lt = stockObject.getString("lt");
					Date ltDate = format.parse(lt);
					if(currentCal.getTime().compareTo(ltDate)>0){
						throw new RuntimeException("Stale details for " + code);
					}
					if(!exchange.equals("NSE")){
						code = StockDetailStaticHolder.BSEToNSEMap.get(code);
						StockDetailStaticHolder.BOMStocksSetTemp.add(code);
					}
					stockCurrentPriceList.add(new StockPriceData(code, price, cp));
				}catch(Exception e1){
					if(exchange.equals("NSE")){
						if(StockDetailStaticHolder.NSEToBSEMap.containsKey(code)){
							String bseCode = StockDetailStaticHolder.NSEToBSEMap.get(code);
//							logger.info("Error fetching stock price from NSE. Trying to get from BOM for code " + code + " ->  " + bseCode );
							response = HttpUtil.getFromHttpUrlAsString(GOOGLE_QUOTE_URL.replace("#CODE#",  "BOM:"+ bseCode.replace("&", "%26")));
							stockCurrentPriceList.addAll(getPriceFromJSON("BOM", code, response));
						}
					}else{
						logger.info("Error fetching data from " + exchange + " for code " + code);
					}
				}
			}
		}catch(Exception e){
			logger.info("Error fetching stock price for " + codes);
		}
		return stockCurrentPriceList;
	}
	
	public static List<StockPriceData> getWorldStockPrices() {
		List<StockPriceData> stockCurrentPriceList = new ArrayList<StockPriceData>(); 
		try{
			for(String key : StockDetailStaticHolder.worldStockDetailsMap.keySet()){
				String stockCode = key;
				String stockName = StockDetailStaticHolder.worldStockDetailsMap.get(key);
				String response = HttpUtil.getFromHttpUrlAsString(GOOGLE_QUOTE_URL.replace("#CODE#", stockCode.replace("&", "%26")));
				JSONArray jsonArray = JSONArray.fromObject( response.replaceFirst("//", "").trim() );  
				for(int j=0; j<jsonArray.size();j++){
					try{
						JSONObject stockObject = (JSONObject)jsonArray.get(j);
						Double price = new Double(stockObject.getString("l_fix").replaceAll(",", ""));
						Double cp = new Double(stockObject.getString("cp").replaceAll(",", ""));
						stockCurrentPriceList.add(new StockPriceData(stockName, price, cp));
					}catch(Exception e){
						logger.info("Error fetching stock price for " + key);
					}
				}
			}
			return stockCurrentPriceList;
		}catch(Exception e){
			logger.info("Error fetching World stock prices");
			return new ArrayList<StockPriceData>();
		}
	}
	
	public static List<EODVolumeChartData> getIntraDayPriceVolumeData(String code) {
		List<EODVolumeChartData> volumeChartDataList = getIntraDayPriceVolumeData(code, "NSE");
		if(volumeChartDataList.size()==0){
			if(StockDetailStaticHolder.NSEToBSEMap.containsKey(code)){
				String bseCode = StockDetailStaticHolder.NSEToBSEMap.get(code);
				getIntraDayPriceVolumeData(bseCode, "BOM");
			}
		}
		return volumeChartDataList;
	}
	
	public static List<EODVolumeChartData> getIntraDayPriceVolumeData(String code, String exchange) {
		List<EODVolumeChartData> volumeChartDataList = new ArrayList<EODVolumeChartData>(); 
		try{
			String response = HttpUtil.getFromHttpUrlAsString(GOOGLE_REALTIME_QUOTE_URL.replace("#CODE#", code.replace("&", "%26")).replace("#EXCHANGE#", exchange));
			String[] values = response.split("\n");
			Date baseDate = null;
			Integer interval = 120;
			for(String value : values){
				Date date = null;
				if(value.startsWith("COLUMNS")){
					continue;
				}else if(value.split(",").length==3){
					String[] datas = value.split(",");
					if(datas[0].startsWith("a")){
						baseDate = new Date(new Long(datas[0].replace("a", ""))*1000L);
						date = baseDate;
					}else{
						date = new Date(baseDate.getTime() + (new Long(datas[0])*interval*1000L));
					}
					Double price = new Double(datas[1]);
					Long volume = new Long(datas[2]);
					volumeChartDataList.add(new EODVolumeChartData(code, date, price, volume));
				}else if(value.startsWith("INTERVAL")){
					interval = new Integer(value.replace("INTERVAL=", ""));
				}
			}
			return volumeChartDataList;
		}catch(Exception e){
			logger.info("Error fetching Intra Day Price Volume Data");
			return new ArrayList<EODVolumeChartData>();
		}
	}
	
}
