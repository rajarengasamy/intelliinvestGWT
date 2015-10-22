package com.intelliinvest.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.intelliinvest.client.data.EODChartData;
import com.intelliinvest.client.data.EODSignalChartData;
import com.intelliinvest.client.data.EODVolumeChartData;
import com.intelliinvest.client.data.ManagePortfolioData;
import com.intelliinvest.client.data.StockDetailStaticHolder;
import com.intelliinvest.client.data.UserEODChartData;
import com.intelliinvest.client.data.UserEODSignalChartData;
import com.intelliinvest.server.dao.ChartDao;
import com.intelliinvest.server.dao.ManagePortfolioDao;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class ChartRequestServlet extends HttpServlet {
	
	static final String YAHOO_SMALL_1_DAY ="http://ichart.yahoo.com/t?s=#CODE#";
	static final String YAHOO_SMALL_5_DAY ="http://ichart.yahoo.com/v?s=#CODE#";
	static final String YAHOO_BIG_1_DAY ="http://ichart.finance.yahoo.com/b?s=#CODE#";
	static final String YAHOO_BIG_5_DAY ="http://ichart.finance.yahoo.com/w?s=#CODE#";
	static final String YAHOO_BIG_1_MONTH ="http://chart.finance.yahoo.com/c/1m/#CODE#";
	static final String YAHOO_BIG_3_MONTHS ="http://chart.finance.yahoo.com/c/3m/#CODE#";
	static final String YAHOO_BIG_6_MONTHS ="http://chart.finance.yahoo.com/c/6m/#CODE#";
	static final String YAHOO_BIG_1_YEAR ="http://chart.finance.yahoo.com/c/1y/#CODE#";
	static final String YAHOO_BIG_2_YEARS ="http://chart.finance.yahoo.com/c/2y/#CODE#";
	static final String YAHOO_BIG_5_YEARS ="http://chart.finance.yahoo.com/c/5y/#CODE#";
	static final String YAHOO_BIG_MAX_YEARS ="http://chart.finance.yahoo.com/c/my/#CODE#";
	static final String GOOGLE_SMALL_1_DAY ="https://www.google.com/finance/chart?q=#EXCHANGE#:#CODE#&cht=o";
	static final String GOOGLE_SMALL_5_DAY ="https://www.google.com/finance/chart?q=#EXCHANGE#:#CODE#&cht=o&p=5d";
	static final String GOOGLE_BIG_1_DAY ="https://www.google.com/finance/getchart?q=#CODE#&x=#EXCHANGE#&p=1d&i=60";
	static final String GOOGLE_BIG_5_DAY ="https://www.google.com/finance/getchart?q=#CODE#&x=#EXCHANGE#&p=5d&i=60";
	static final String GOOGLE_BIG_1_MONTH ="https://www.google.com/finance/getchart?q=#CODE#&x=#EXCHANGE#&p=1M&i=86400";
	static final String GOOGLE_BIG_3_MONTHS ="https://www.google.com/finance/getchart?q=#CODE#&x=#EXCHANGE#&p=3M&i=86400";
	static final String GOOGLE_BIG_6_MONTHS ="https://www.google.com/finance/getchart?q=#CODE#&x=#EXCHANGE#&p=6M&i=86400";
	static final String GOOGLE_BIG_1_YEAR ="https://www.google.com/finance/getchart?q=#CODE#&x=#EXCHANGE#&p=1Y&i=86400";
	static final String GOOGLE_BIG_2_YEARS ="https://www.google.com/finance/getchart?q=#CODE#&x=#EXCHANGE#&p=2Y&i=86400";
	static final String GOOGLE_BIG_5_YEARS ="https://www.google.com/finance/getchart?q=#CODE#&x=#EXCHANGE#&p=5Y&i=86400";
	static final String GOOGLE_BIG_MAX_YEARS ="https://www.google.com/finance/getchart?q=#CODE#&x=#EXCHANGE#&p=10Y&i=86400";
	
	private static Logger logger = Logger.getLogger(ChartRequestServlet.class);
	
	public static String CHART_FROM="GOOGLE";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String chartType = req.getParameter("chartType");
		String stockCode = req.getParameter("stockCode");
		String user = req.getParameter("userId");
		logger.info("Received request for chart : chart type " + chartType + ",  stock code " + stockCode + " and user " + user);
		if(CHART_FROM.equalsIgnoreCase("GOOGLE")){
			byte[] response = getChartFromGoogle(chartType, stockCode);
			resp.getOutputStream().write(response);
		}else{
			byte[] response = getChartFromYahoo(chartType, stockCode);
			resp.getOutputStream().write(response);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
	
	public byte[] getChartFromYahoo(String chartType, String stockCode) throws IOException{
		stockCode = IntelliInvestUtil.getYahooCode(stockCode);
		
		if(chartType.equals("default") || null==stockCode || "".equals(stockCode) || null==chartType || "".equals(chartType)){
			InputStream inputStream = new FileInputStream(new File(getServletContext().getRealPath("data/images/defaultChart.jpg")));
			return IOUtils.toByteArray(inputStream);
		}else{
	        try {
	        	String url = YAHOO_SMALL_1_DAY;
        		if(chartType.equals("1DS")){
        			url = YAHOO_SMALL_1_DAY;
        		}if(chartType.equals("5DS")){
        			url = YAHOO_SMALL_5_DAY;
        		}else if(chartType.equals("1D")){ 
	        		url = YAHOO_BIG_1_DAY;
	        	}else if(chartType.equals("5D")){ 
	        		url = YAHOO_BIG_5_DAY;
	        	}else if(chartType.equals("1M")){ 
	        		url = YAHOO_BIG_1_MONTH;
	        	}else if(chartType.equals("3M")){ 
	        		url = YAHOO_BIG_3_MONTHS;
	        	}else if(chartType.equals("6M")){ 
	        		url = YAHOO_BIG_6_MONTHS;
	        	}else if(chartType.equals("1Y")){ 
	        		url = YAHOO_BIG_1_YEAR;
	        	}else if(chartType.equals("Max")){ 
	        		url = YAHOO_BIG_MAX_YEARS;
	        	}
        		byte[] response = HttpUtil.getFromHttpUrlAsBytes(url.replace("#CODE#", stockCode.replace("&", "&amp;")));
        		return response;
	        }finally {
	        }
		}
	}
	
	public byte[] getChartFromGoogle(String chartType, String stockCode) throws IOException{
		if(chartType.equals("default") || null==stockCode || "".equals(stockCode) || null==chartType || "".equals(chartType)){
			InputStream inputStream = new FileInputStream(new File(getServletContext().getRealPath("data/images/defaultChart.jpg")));
			return IOUtils.toByteArray(inputStream);
		}else{
	        try {
	        	String url = GOOGLE_SMALL_1_DAY;
        		if(chartType.equals("1DS")){
        			url = GOOGLE_SMALL_1_DAY;
        		}if(chartType.equals("5DS")){
        			url = GOOGLE_SMALL_5_DAY;
        		}else if(chartType.equals("1D")){ 
	        		url = GOOGLE_BIG_1_DAY;
	        	}else if(chartType.equals("5D")){ 
	        		url = GOOGLE_BIG_5_DAY;
	        	}else if(chartType.equals("1M")){ 
	        		url = GOOGLE_BIG_1_MONTH;
	        	}else if(chartType.equals("3M")){ 
	        		url = GOOGLE_BIG_3_MONTHS;
	        	}else if(chartType.equals("6M")){ 
	        		url = GOOGLE_BIG_6_MONTHS;
	        	}else if(chartType.equals("1Y")){ 
	        		url = GOOGLE_BIG_1_YEAR;
	        	}else if(chartType.equals("Max")){ 
	        		url = GOOGLE_BIG_MAX_YEARS;
	        	}
        		String exchange = "NSE";
        		if(StockDetailStaticHolder.BOMStocksSet.contains(stockCode)){
        			exchange = "BOM"; 
        			stockCode = StockDetailStaticHolder.NSEToBSEMap.get(stockCode);
        		}
        		byte[] response = HttpUtil.getFromHttpUrlAsBytes(url.replace("#CODE#", stockCode.replace("&", "%26")).replace("#EXCHANGE#", exchange));
        		return response;
	        }catch(Exception e){
	        	throw new RuntimeException("Error fetching chart " + e.getMessage());
	        }
		}
	}
	
	public static String getDayChart(String chartType, String stockCode){
		List<EODVolumeChartData> chartDatas = ChartDao.getInstance().getEODVolumeChartData(stockCode, chartType);
		StringBuffer stringBuffer = new StringBuffer();
		for(EODChartData chartData : chartDatas){
			if(stringBuffer.length()==0){
				stringBuffer.append(chartData.toString());
			}else{
				stringBuffer.append(",").append(chartData.toString());	
			}
			
		}
		return "[" + stringBuffer.toString() + "]";
	}
	
	public static String getIntraDayChart(String chartType, String stockCode){
		List<EODVolumeChartData> chartDatas = ChartDao.getInstance().getVolumeIntraChartData(stockCode, chartType);
		StringBuffer stringBuffer = new StringBuffer();
		for(EODChartData chartData : chartDatas){
			if(stringBuffer.length()==0){
				stringBuffer.append(chartData.toString());
			}else{
				stringBuffer.append(",").append(chartData.toString());	
			}
			
		}
		return "[" + stringBuffer.toString() + "]";
	}

	public static String getEODSignalChart(String chartType, String stockCode) {
		List<EODSignalChartData> chartDatas = ChartDao.getInstance().getSimulationChartData(stockCode, chartType);
		return chartDatas.toString();
	}
	
	public static String getUserEODSignalChart(String stockCode, String user) {
		List<EODSignalChartData> chartDatas = ChartDao.getInstance().getSimulationChartData(stockCode, "eodSignalChartYearly");
		List<ManagePortfolioData> managePortfolioDatas = ManagePortfolioDao.getInstance().getManagePortfolioDataForCode(user, stockCode);
		Map<Date, Integer> dateBuyQuantityMap = new HashMap<Date, Integer>();
		Map<Date, Integer> dateSellQuantityMap = new HashMap<Date, Integer>();
		Map<Date, Double> dateBuyAmountMap = new HashMap<Date, Double>();
		Map<Date, Double> dateSellAmountMap = new HashMap<Date, Double>();
		for(ManagePortfolioData managePortfolioData : managePortfolioDatas){
			if(managePortfolioData.getDirection().equals("Buy")){
				if(!dateBuyQuantityMap.containsKey(managePortfolioData.getDate())){
					dateBuyQuantityMap.put(managePortfolioData.getDate(), 0);
					dateBuyAmountMap.put(managePortfolioData.getDate(), 0D);
					dateSellQuantityMap.put(managePortfolioData.getDate(), 0);
					dateSellAmountMap.put(managePortfolioData.getDate(), 0D);
				}
				dateBuyQuantityMap.put(managePortfolioData.getDate(), dateBuyQuantityMap.get(managePortfolioData.getDate()) + managePortfolioData.getQuantity());
				dateBuyAmountMap.put(managePortfolioData.getDate(), dateBuyAmountMap.get(managePortfolioData.getDate()) + (managePortfolioData.getPrice() * managePortfolioData.getQuantity()));
			}else{
				if(!dateBuyQuantityMap.containsKey(managePortfolioData.getDate())){
					dateBuyQuantityMap.put(managePortfolioData.getDate(), 0);
					dateBuyAmountMap.put(managePortfolioData.getDate(), 0D);
					dateSellQuantityMap.put(managePortfolioData.getDate(), 0);
					dateSellAmountMap.put(managePortfolioData.getDate(), 0D);
				}
				dateSellQuantityMap.put(managePortfolioData.getDate(), dateSellQuantityMap.get(managePortfolioData.getDate()) + managePortfolioData.getQuantity());
				dateSellAmountMap.put(managePortfolioData.getDate(), dateSellAmountMap.get(managePortfolioData.getDate()) + (managePortfolioData.getPrice() * managePortfolioData.getQuantity()));
			}
		}
		
		List<UserEODSignalChartData> userChartDatas = new ArrayList<UserEODSignalChartData>();
		for(EODSignalChartData chartData : chartDatas){
			UserEODSignalChartData userChartData = new UserEODSignalChartData(chartData);
			if(dateBuyQuantityMap.containsKey(chartData.getDate()) && dateBuyQuantityMap.get(chartData.getDate())!=0){
				userChartData.setAvgBuyPrice(dateBuyAmountMap.get(chartData.getDate())/dateBuyQuantityMap.get(chartData.getDate()) );
			}
			if(dateSellQuantityMap.containsKey(chartData.getDate()) && dateSellQuantityMap.get(chartData.getDate())!=0){
				userChartData.setAvgSellPrice(dateSellAmountMap.get(chartData.getDate())/dateSellQuantityMap.get(chartData.getDate()) );
			}
			userChartDatas.add(userChartData);
		}
		return userChartDatas.toString();
	}

	public static String getUserEODChart(String stockCode, String user) {
		List<EODChartData> chartDatas = ChartDao.getInstance().getEODChartData(stockCode, "eodSignalChartYearly");
		List<ManagePortfolioData> managePortfolioDatas = ManagePortfolioDao.getInstance().getManagePortfolioDataForCode(user, stockCode);
		Map<Date, Integer> dateBuyQuantityMap = new HashMap<Date, Integer>();
		Map<Date, Integer> dateSellQuantityMap = new HashMap<Date, Integer>();
		Map<Date, Double> dateBuyAmountMap = new HashMap<Date, Double>();
		Map<Date, Double> dateSellAmountMap = new HashMap<Date, Double>();
		for(ManagePortfolioData managePortfolioData : managePortfolioDatas){
			if(managePortfolioData.getDirection().equals("Buy")){
				if(!dateBuyQuantityMap.containsKey(managePortfolioData.getDate())){
					dateBuyQuantityMap.put(managePortfolioData.getDate(), 0);
					dateBuyAmountMap.put(managePortfolioData.getDate(), 0D);
					dateSellQuantityMap.put(managePortfolioData.getDate(), 0);
					dateSellAmountMap.put(managePortfolioData.getDate(), 0D);
				}
				dateBuyQuantityMap.put(managePortfolioData.getDate(), dateBuyQuantityMap.get(managePortfolioData.getDate()) + managePortfolioData.getQuantity());
				dateBuyAmountMap.put(managePortfolioData.getDate(), dateBuyAmountMap.get(managePortfolioData.getDate()) + (managePortfolioData.getPrice() * managePortfolioData.getQuantity()));
			}else{
				if(!dateBuyQuantityMap.containsKey(managePortfolioData.getDate())){
					dateBuyQuantityMap.put(managePortfolioData.getDate(), 0);
					dateBuyAmountMap.put(managePortfolioData.getDate(), 0D);
					dateSellQuantityMap.put(managePortfolioData.getDate(), 0);
					dateSellAmountMap.put(managePortfolioData.getDate(), 0D);
				}
				dateSellQuantityMap.put(managePortfolioData.getDate(), dateSellQuantityMap.get(managePortfolioData.getDate()) + managePortfolioData.getQuantity());
				dateSellAmountMap.put(managePortfolioData.getDate(), dateSellAmountMap.get(managePortfolioData.getDate()) + (managePortfolioData.getPrice() * managePortfolioData.getQuantity()));
			}
		}
		
		List<UserEODChartData> userChartDatas = new ArrayList<UserEODChartData>();
		for(EODChartData chartData : chartDatas){
			UserEODChartData userChartData = new UserEODChartData(chartData);
			if(dateBuyQuantityMap.containsKey(chartData.getDate()) && dateBuyQuantityMap.get(chartData.getDate())!=0){
				userChartData.setAvgBuyPrice(dateBuyAmountMap.get(chartData.getDate())/dateBuyQuantityMap.get(chartData.getDate()) );
			}
			if(dateSellQuantityMap.containsKey(chartData.getDate()) && dateSellQuantityMap.get(chartData.getDate())!=0){
				userChartData.setAvgSellPrice(dateSellAmountMap.get(chartData.getDate())/dateSellQuantityMap.get(chartData.getDate()) );
			}
			userChartDatas.add(userChartData);
		}
		return userChartDatas.toString();
	}

}
