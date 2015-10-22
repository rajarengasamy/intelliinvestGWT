package com.intelliinvest.server;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.intelliinvest.client.data.IntelliInvestData;
import com.intelliinvest.client.data.PaymentData;
import com.intelliinvest.client.data.RiskReturnMatrixSummaryData;
import com.intelliinvest.client.data.UserDetailData;
import com.intelliinvest.client.service.UtilService;
import com.intelliinvest.server.dao.ChatDao;
import com.intelliinvest.server.dao.IntelliInvestDataDao;
import com.intelliinvest.server.dao.PaymentDao;
import com.intelliinvest.server.dao.RiskReturnMatrixDao;
import com.intelliinvest.server.dao.TradingAccountDao;
import com.intelliinvest.server.dao.UserDetailDao;
import com.intelliinvest.server.util.MailUtil;
import com.intelliinvest.server.util.SignalGenerator;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class UtilServlet extends RemoteServiceServlet implements UtilService {
	
	private static Logger logger = Logger.getLogger(UtilServlet.class);
	
	public static Boolean IS_ADMIN_AVAILABLE = false;
	public static Long ADMIN_LAST_AVAILABLE_TIME = 0L;
	
	@Override
	public String contactUs(String mailId, String phone, String subject,
			String message) {
		String messageFull = "Hi Intelli Invest Support,<br>"
						  + "Mail from : " + mailId + "<br>"
						  + "Phone     : " + phone + "<br>"
						  + "Message     : " + message + "<br>";
		Boolean status = MailUtil.sendMail(IntelliInvestStore.properties.getProperty("smtp.host"), IntelliInvestStore.properties.getProperty("mail.from"),
								IntelliInvestStore.properties.getProperty("mail.password")
								, new String[]{IntelliInvestStore.properties.getProperty("mail.from")},
								subject, messageFull);
		if(status){
			return "Succesfully sent to IntelliInvest Support. We will communicate with you related to your query with in 48 hrs.";
		}else{
			return "Error sending information to Support, please try contacting admin through mail";
		}
	}
	
	@Override
	public String upload(Date fromDate, Date toDate, String type) {
		logger.info("Back load for " + type + " from " + fromDate + " to " + toDate);
		Calendar fromCal = Calendar.getInstance();
		fromCal.setTime(fromDate);
		fromCal.set(Calendar.HOUR, 0);
		fromCal.set(Calendar.MINUTE, 0);
		fromCal.set(Calendar.SECOND, 0);
		fromCal.set(Calendar.MILLISECOND, 0);
		fromCal.set(Calendar.AM_PM, Calendar.AM);
		
		Calendar toCal = Calendar.getInstance();
		toCal.setTime(toDate);
		toCal.set(Calendar.HOUR, 0);
		toCal.set(Calendar.MINUTE, 0);
		toCal.set(Calendar.SECOND, 0);
		toCal.set(Calendar.MILLISECOND, 0);
		toCal.set(Calendar.AM_PM, Calendar.AM);
		
		while(toCal.getTime().compareTo(fromCal.getTime())>=0){
//			if(fromCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || fromCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
//				fromCal.add(Calendar.DATE, 1);
//				continue;
//			}
			Boolean status = false;
			if(type.equalsIgnoreCase("Bhav")){
				status = IntelliInvestDataDao.getInstance().backloadBhavData(fromCal.getTime());
			}
			if(status){
				logger.info("Data uploaded successfully for " + fromCal.getTime());
			}else{
				logger.info("Error uploading data for " + fromCal.getTime());
			}
			fromCal.add(Calendar.DATE, 1);
		}
		return "Data upload completed. See logs for details";
	}

	@Override
	public List<IntelliInvestData> getIncremntalRowsForChat(Integer messageId, String userId) {
		return new ArrayList<IntelliInvestData>(ChatDao.getInstance().getChatData(messageId, userId));
	}
	
	@Override
	public String getDayChartData(String chartType, String stockCode, String user) {
		logger.info("Received request for DayChartData : chart type " + chartType + ",  stock code " + stockCode + " and user " + user);
		return ChartRequestServlet.getDayChart(chartType, stockCode);
	}
	
	@Override
	public String getIntraDayChartData(String chartType, String stockCode, String user) {
		logger.info("Received request for IntraDayChartData : chart type " + chartType + ",  stock code " + stockCode + " and user " + user);
		return ChartRequestServlet.getIntraDayChart(chartType, stockCode);
	}
	
	@Override
	public String getChartData(String chartType, String stockCode, String user) {
		logger.info("Received request for chart : chart type " + chartType + ",  stock code " + stockCode + " and user " + user);
		if(chartType.startsWith("eodSignalChart")){
			return ChartRequestServlet.getEODSignalChart(chartType, stockCode);
		}else if(chartType.startsWith("userEODChart")){
			return ChartRequestServlet.getUserEODChart(stockCode, user);
		}else {
			return ChartRequestServlet.getUserEODSignalChart(stockCode, user);
		}
	}
	
	@Override
	public String refreshStaticData(String mailId, String password) {
		UserDetailData userDetailData = UserDetailDao.getInstance().login(mailId, password);
		if(null!=userDetailData && userDetailData.getUserType().equals("Admin")){
			logger.info("User " + mailId + " refreshing static data");
			IntelliInvestStore.refresh();
			return "Refreshed static data for IntelliInvest";
		}else{
			return "Only admin can refresh static data for IntelliInvest";
		}
	}
	
	@Override
	public String refreshStaticDataIncludingPrice(String mailId, String password) {
		UserDetailData userDetailData = UserDetailDao.getInstance().login(mailId, password);
		if(null!=userDetailData && userDetailData.getUserType().equals("Admin")){
			logger.info("User " + mailId + " refreshing static data including price");
			IntelliInvestStore.updateCurrentPrices();
			IntelliInvestStore.updateWorldPrices();
			IntelliInvestStore.refresh();
			return "Refreshed static data including price for IntelliInvest";
		}else{
			return "Only admin can refresh static data including price for IntelliInvest";
		}
	}
	
	@Override
	public String startRefreshStaticData(String mailId, String password) {
		UserDetailData userDetailData = UserDetailDao.getInstance().login(mailId, password);
		if(null!=userDetailData && userDetailData.getUserType().equals("Admin")){
			logger.info("User " + mailId + " start refresh static data");
			IntelliInvestStore.REFRESH_PERIODICALLY=true;
			return "Started static data for IntelliInvest. Stop this service whenever needed.";
		}else{
			return "Only admin can start refresh";
		}
		
	}
	
	@Override
	public String stopRefreshStaticData(String mailId, String password) {
		UserDetailData userDetailData = UserDetailDao.getInstance().login(mailId, password);
		if(null!=userDetailData && userDetailData.getUserType().equals("Admin")){
			logger.info("User " + mailId + " stop refresh static data");
			IntelliInvestStore.REFRESH_PERIODICALLY=false;
			return "Stopped static data for IntelliInvest. Do remember to start again.";
		}else{
			return "Only admin can stop refresh";
		}
	}
	
	@Override
	public String sendDailyUpdateMail(String mailId, String password) {
		IntelliInvestStore.refresh();
		UserDetailData userDetailData = UserDetailDao.getInstance().login(mailId, password);
		if(null!=userDetailData && userDetailData.getUserType().equals("Admin")){
			TradingAccountDao.getInstance().sendDailyTradingAccountUpdateMail();
			return "Sent update mails for all users";
		}else{
			return "Only admin can send daily update mail refresh";
		}
	}
	
	@Override
	public Map<String, RiskReturnMatrixSummaryData> getRiskReturnSummaryData() {
		return RiskReturnMatrixDao.getInstance().getRsikReturnMatrixSummaryData();
	}
	
	@Override
	public Map<String, PaymentData> getPaymentData() {
		return PaymentDao.getInstance().getAllPaymentDetails();
	}
	
	@Override
	public Boolean getAdminAvailableForChat(String userId) {
//		logger.info("Checking for admin availability");
		if(null!=userId && !"".equals(userId)){
			List<UserDetailData> userDetails = UserDetailDao.getInstance().getUserDetails(userId);
			for(UserDetailData userDetailData : userDetails){
				if("Admin".equalsIgnoreCase(userDetailData.getUserType())){
					IS_ADMIN_AVAILABLE = true;
					ADMIN_LAST_AVAILABLE_TIME = System.currentTimeMillis();
//					logger.info("Setting admin availability to true");
				}else{
					if(System.currentTimeMillis() - ADMIN_LAST_AVAILABLE_TIME>60000){
						IS_ADMIN_AVAILABLE = false;
//						logger.info("Setting admin availability to false from user");
					}
				}
			}
		}else{
			if(System.currentTimeMillis() - ADMIN_LAST_AVAILABLE_TIME>60000){
				IS_ADMIN_AVAILABLE = false;
//				logger.info("Setting admin availability to false from empty user");
			}
		}
		return IS_ADMIN_AVAILABLE;
	}
	
	@Override
	public String generateMagicNumber(String stockCode, Integer ma) {
		logger.info("Generationg magic numbers for stock code " + stockCode + "and ma " + ma);
		if(ma==-1){
			SignalGenerator.generateMagicNumber(stockCode);
		}else{
			SignalGenerator.generateMagicNumber(stockCode, ma);
		}
		
		return "Successfully generated magic number for " + stockCode + "";
	}
	
	@Override
	public String generateSignal(String stockCode, Integer ma) {
		logger.info("Generationg Signals for stock code " + stockCode + "and ma " + ma);
		if(ma==-1){
			SignalGenerator.generateSignals(stockCode);
		}else{
			SignalGenerator.generateSignals(stockCode, ma);
		}
		return "Successfully generated signals for stock code " + stockCode + "and ma " + ma;
	}
	
	@Override
	public String generateTodaySignal(String stockCode, Integer ma) {
		logger.info("Generationg TodaySignals for stock code " + stockCode + "and ma " + ma);
		if(ma==-1){
			SignalGenerator.generateTodaySignals(stockCode);
		}else{
			SignalGenerator.generateTodaySignals(stockCode, ma);
		}
		return "Successfully generated TodaySignal for stock code " + stockCode + "and ma " + ma;
	}
}
