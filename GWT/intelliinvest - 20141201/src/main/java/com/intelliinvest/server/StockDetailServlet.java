package com.intelliinvest.server;

import java.util.HashMap;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.intelliinvest.client.config.Constants;
import com.intelliinvest.client.data.IntelliInvestData;
import com.intelliinvest.client.data.IntelliInvestRequest;
import com.intelliinvest.client.data.IntelliInvestResponse;
import com.intelliinvest.client.data.StockDetailStaticHolder;
import com.intelliinvest.client.service.IntelliInvestService;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class StockDetailServlet extends RemoteServiceServlet implements IntelliInvestService {
	
	private static Logger logger = Logger.getLogger(StockDetailServlet.class);
	
	@Override
	public IntelliInvestResponse fetch(IntelliInvestRequest intelliInvestRequest) {
		String requestType = intelliInvestRequest.getRequestType();
		IntelliInvestResponse intelliInvestResponse = new IntelliInvestResponse();
		if(Constants.INTELLI_INVEST_STATIC_DATA_REQUEST.equals(requestType)){
			intelliInvestResponse.setResponseType(Constants.INTELLI_INVEST_STATIC_DATA_REQUEST);
			HashMap<String, HashMap<String, ? extends IntelliInvestData>> screenData = new HashMap<String, HashMap<String,? extends IntelliInvestData>>();
			screenData.put(Constants.STOCK_DETAILS, StockDetailStaticHolder.stockDetailsMap);
//			screenData.put(Constants.INTELLI_STOCK_DETAILS, StockDetailStaticHolder.intelliStockDetailsMap);
			screenData.put(Constants.STOCK_CURRENT_PRICES, StockDetailStaticHolder.stockCurrentPriceMap);
			screenData.put(Constants.STOCK_EOD_PRICES, StockDetailStaticHolder.stockEODPriceMap);
			screenData.put(Constants.WORLD_STOCK_PRICES, StockDetailStaticHolder.worldStockPriceMap);
			screenData.put(Constants.NIFTY_STOCK_PRICES, StockDetailStaticHolder.NIFTYStockPriceMap);
			intelliInvestResponse.setScreenData(screenData);
			return intelliInvestResponse;
		}else if(Constants.INTELLI_INVEST_CURRENT_PRICE_DATA_REQUEST.equals(requestType)){
			intelliInvestResponse.setResponseType(Constants.INTELLI_INVEST_CURRENT_PRICE_DATA_REQUEST);
			HashMap<String, HashMap<String,? extends IntelliInvestData>> screenData = new HashMap<String, HashMap<String,? extends IntelliInvestData>>();
			screenData.put(Constants.STOCK_CURRENT_PRICES, StockDetailStaticHolder.stockCurrentPriceMap);
			screenData.put(Constants.NIFTY_STOCK_PRICES, StockDetailStaticHolder.NIFTYStockPriceMap);
			screenData.put(Constants.WORLD_STOCK_PRICES, StockDetailStaticHolder.worldStockPriceMap);
			intelliInvestResponse.setScreenData(screenData);
			return intelliInvestResponse;
		}
		throw new RuntimeException("Unexpected request type " + requestType + ". Please send this error to admin to help resolve this problem.");
	}

	@Override
	public IntelliInvestResponse add(IntelliInvestRequest intelliInvestRequest) {
		return null;
	}
	
	
	
	@Override
	public IntelliInvestResponse remove(IntelliInvestRequest intelliInvestRequest) {
		return null;
	}
	
	
	
	@Override
	public IntelliInvestResponse update(IntelliInvestRequest intelliInvestRequest) {
		return null;
	}
	
	public static void main(String[] args) {
		String response ="[ { \"id\": \"675530\" ,\"t\" : \"INFY\" ,\"e\" : \"NSE\" ,\"l\" : \"3,353.00\" ,\"l_fix\" : \"3353.00\" ,\"l_cur\" : \"Rs.3,353.00\" ,\"s\": \"0\" ,\"ltt\":\"3:29PM GMT+5:30\" ,\"lt\" : \"Nov 21, 3:29PM GMT+5:30\" ,\"c\" : \"-49.20\" ,\"c_fix\" : \"-49.20\" ,\"cp\" : \"-1.45\" ,\"cp_fix\" : \"-1.45\" ,\"ccol\" : \"chr\" } ]";
		JSONArray jsonArray = JSONArray.fromObject( response );  
		String price = ((JSONObject)jsonArray.get(0)).get("l").toString();
		logger.info(price);
		logger.info(((JSONObject)jsonArray.get(0)).getDouble("l_cur"));
		logger.info(((JSONObject)jsonArray.get(0)).getDouble("l_fix"));
		String test="a,b";
		logger.info(test.split(",").length);
	}
	
	@Override
	public void init() throws ServletException {
		super.init();
		IntelliInvestStore.initialize();
	}
	
}
