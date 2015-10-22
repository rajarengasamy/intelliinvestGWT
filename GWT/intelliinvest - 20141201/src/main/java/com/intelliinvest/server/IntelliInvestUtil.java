package com.intelliinvest.server;

public class IntelliInvestUtil {
	public static String getYahooCode(String stockCode){
		if(null==stockCode || "".equals(stockCode)){
			return "";
		}
		String yahooCode = stockCode;
		if(stockCode.length()>9)
		{
			yahooCode = stockCode.substring(0,9);
		}
		yahooCode = yahooCode + ".NS";
		return yahooCode;
	}
}
