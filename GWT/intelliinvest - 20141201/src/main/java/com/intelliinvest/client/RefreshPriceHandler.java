package com.intelliinvest.client;

import java.util.HashMap;
import java.util.Map;

public abstract class RefreshPriceHandler {
	private static Map<String, RefreshPriceHandler> refreshHandlerMap = new HashMap<String, RefreshPriceHandler>();
	
	public static void addHandler(String listGridId, RefreshPriceHandler priceHandler){
		refreshHandlerMap.put(listGridId, priceHandler);
	}
	
	public abstract void onRefreshPrice();
	
	public static void refreshScreenDataOnPriceChange(){
		for(RefreshPriceHandler  refreshPriceHandler : refreshHandlerMap.values()){
			refreshPriceHandler.onRefreshPrice();
		}
	}
}
