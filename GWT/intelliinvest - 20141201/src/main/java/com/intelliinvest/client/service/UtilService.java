package com.intelliinvest.client.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.intelliinvest.client.data.IntelliInvestData;
import com.intelliinvest.client.data.PaymentData;
import com.intelliinvest.client.data.RiskReturnMatrixSummaryData;

@RemoteServiceRelativePath("util")
public interface UtilService extends RemoteService{
	String contactUs(String mailId, String phone, String subject, String message);
	String upload(Date fromDate, Date toDate, String type);
	List<IntelliInvestData> getIncremntalRowsForChat(Integer messageId, String userId);
	String getChartData(String chartType, String stockCode, String user);
	String getIntraDayChartData(String chartType, String stockCode, String user);
	String refreshStaticData(String mailId, String password);
	String startRefreshStaticData(String mailId, String password);
	String stopRefreshStaticData(String mailId, String password);
	String sendDailyUpdateMail(String mailId, String password);
	Map<String, RiskReturnMatrixSummaryData> getRiskReturnSummaryData();
	Map<String, PaymentData> getPaymentData();
	Boolean getAdminAvailableForChat(String userId);
	String getDayChartData(String chartType, String stockCode, String user);
	String generateMagicNumber(String stockCode, Integer ma);
	String generateTodaySignal(String stockCode, Integer ma);
	String generateSignal(String stockCode, Integer ma);
	String refreshStaticDataIncludingPrice(String mailId, String password);
	
}

