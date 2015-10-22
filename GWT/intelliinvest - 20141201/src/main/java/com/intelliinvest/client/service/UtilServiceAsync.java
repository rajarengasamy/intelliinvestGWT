package com.intelliinvest.client.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.intelliinvest.client.data.IntelliInvestData;
import com.intelliinvest.client.data.PaymentData;
import com.intelliinvest.client.data.RiskReturnMatrixSummaryData;

public interface UtilServiceAsync {

	void contactUs(String mailId, String phone, String subject, String message,
			AsyncCallback<String> callback);

	void upload(Date fromDate, Date toDate, String type,
			AsyncCallback<String> callback);

	void getIncremntalRowsForChat(Integer messageId, String userId, AsyncCallback<List<IntelliInvestData>> callback);
	
	void getAdminAvailableForChat(String userId, AsyncCallback<Boolean> callback);

	void getChartData(String chartType, String stockCode, String user,
			AsyncCallback<String> callback);

	void startRefreshStaticData(String mailId, String password,
			AsyncCallback<String> callback);

	void refreshStaticData(String mailId, String password,
			AsyncCallback<String> callback);
	
	void refreshStaticDataIncludingPrice(String mailId, String password,
			AsyncCallback<String> callback);

	void stopRefreshStaticData(String mailId, String password,
			AsyncCallback<String> callback);

	void sendDailyUpdateMail(String mailId, String password,
			AsyncCallback<String> callback);

	void getRiskReturnSummaryData(
			AsyncCallback<Map<String, RiskReturnMatrixSummaryData>> callback);

	void getPaymentData(AsyncCallback<Map<String, PaymentData>> callback);

	void getIntraDayChartData(String chartType, String stockCode, String user,
			AsyncCallback<String> callback);

	void getDayChartData(String chartType, String stockCode, String user,
			AsyncCallback<String> callback);

	void generateSignal(String stockCode, Integer ma, AsyncCallback<String> callback);

	void generateTodaySignal(String stockCode, Integer ma, AsyncCallback<String> callback);

	void generateMagicNumber(String stockCode, Integer ma, AsyncCallback<String> callback);
	
	

}
