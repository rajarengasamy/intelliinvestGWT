package com.intelliinvest.server.dao;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.intelliinvest.client.data.ManagePortfolioData;
import com.intelliinvest.client.data.StockDetailStaticHolder;
import com.intelliinvest.client.data.TradingAccountData;
import com.intelliinvest.server.IntelliInvestStore;
import com.intelliinvest.server.util.HibernateUtil;
import com.intelliinvest.server.util.MailUtil;

public class TradingAccountDao {

	private static Logger logger = Logger.getLogger(TradingAccountDao.class);
	
	private static TradingAccountDao tradingAccountDao;
	private static String green = "color:green";
	private static String red = "color:red";
	private static String orange = "color:orange";
	private static String lime = "color:lime";
	
	private static String TRADING_ACCOUNT_RETRIEVE_ALL_QUERY = "select "
			+ "s.CODE as code, s.PREVIOUS_SIGNAL_TYPE as YesterdaySignalType, s.SIGNAL_TYPE as signalType, e.EOD_PRICE as signalPrice, s.SIGNAL_DATE as signalDate "
			+ "from "
			+ "(select a.CODE, a.PREVIOUS_SIGNAL_TYPE, a.SIGNAL_TYPE, a.SIGNAL_DATE from STOCK_SIGNAL_DETAILS a "
			+ "join "
			+ "( select CODE, max(SIGNAL_DATE) as SIGNAL_DATE from STOCK_SIGNAL_DETAILS group by CODE) a1 "
			+ "on a.CODE=a1.CODE and a.SIGNAL_DATE=a1.SIGNAL_DATE )s, "
			+ "EOD_STOCK_PRICE e where s.SIGNAL_DATE = e.EOD_DATE and s.CODE=e.CODE order by s.CODE"; 
			
//			
//			"SELECT i.CODE as code, a.SIGNAL_TYPE as YesterdaySignalType, a.SIGNAL_TYPE as signalType, a.SIGNAL_PRICE as signalPrice, a.SIGNAL_DATE as signalDate "  +
//			"(select e.CODE, s.SIGNAL_TYPE , e.EOD_PRICE as SIGNAL_PRICE, s.MAX_SIGNAL_DATE as SIGNAL_DATE from " +
//					"(select CODE, SIGNAL_TYPE, MAX(SIGNAL_DATE) as MAX_SIGNAL_DATE from STOCK_SIGNAL_DETAILS group by CODE) s, EOD_STOCK_PRICE e " +
//					"where s.MAX_SIGNAL_DATE=e.EOD_DATE and s.CODE=e.CODE " +
//			 ") a " +
//			"where i.CODE = a.CODE order by i.CODE ";
	
	
//	SELECT i.CODE as code, i.YESTERDAY_SIGNAL_TYPE as YesterdaySignalType, i.SIGNAL_TYPE as signalType, a.SIGNAL_PRICE as signalPrice, a.SIGNAL_DATE as signalDate 
//	from INTELLI_INVEST_DATA i, 
//	(select e.CODE, s.SIGNAL_TYPE , e.EOD_PRICE as SIGNAL_PRICE, s.MAX_SIGNAL_DATE as SIGNAL_DATE from 
//	(select CODE, SIGNAL_TYPE, MAX(SIGNAL_DATE) as MAX_SIGNAL_DATE from STOCK_SIGNAL_DETAILS group by CODE) s, EOD_STOCK_PRICE e 
//	 where s.MAX_SIGNAL_DATE=e.EOD_DATE and s.CODE=e.CODE ) a  where i.CODE = a.CODE order by i.CODE
	
//	"SELECT CODE as code, YESTERDAY_SIGNAL_TYPE as YesterdaySignalType, SIGNAL_TYPE as signalType, SIGNAL_PRICE as signalPrice from INTELLI_INVEST_DATA ORDER BY CODE";
	private static String TRADING_ACCOUNT_RETRIEVE_QUERY = "SELECT CODE as code from USER_TRADING_ACCOUNT_DETAILS where USER_ID=:userId";
//			"SELECT a.CODE as code, YESTERDAY_SIGNAL_TYPE as YesterdaySignalType, SIGNAL_TYPE as signalType, SIGNAL_PRICE as signalPrice from INTELLI_INVEST_DATA a, USER_TRADING_ACCOUNT_DETAILS b where USER_ID=:userId and a.CODE=b.CODE ORDER BY CODE";
	private static String TRADING_ACCOUNT_INSERT_QUERY = "INSERT INTO USER_TRADING_ACCOUNT_DETAILS (USER_ID, CODE) values (:userId, :code)";
	private static String TRADING_ACCOUNT_DELETE_QUERY = "DELETE from USER_TRADING_ACCOUNT_DETAILS where CODE=:code and USER_ID=:userId";
//	private static String TRADING_ACCOUNT_SIGNAL_DATE_QUERY = "SELECT CODE, SIGNAL_TYPE, MAX(SIGNAL_DATE) from STOCK_SIGNAL_DETAILS group by CODE, SIGNAL_TYPE";
			
	private static String TRADING_ACCOUNT_RETRIEVE_QUERY_MAIL = "SELECT a.USER_ID as userId , CODE as code from USER_TRADING_ACCOUNT_DETAILS a, USER_DETAILS b where a.USER_ID=b.USER_ID and b.SEND_NOTIFICATION=1";
			
	private TradingAccountDao() {
//		 CREATE Table USER_TRADING_ACCOUNT_DETAILS( USER_ID varchar(25), CODE varchar(10))
	}
	
	public static TradingAccountDao getInstance(){
		if(null==tradingAccountDao){
			synchronized (TradingAccountDao.class) {
				if(null==tradingAccountDao){
					tradingAccountDao = new TradingAccountDao();
					logger.info("Initialised TradingAccountDao");
				}
			}
		}
		return tradingAccountDao;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<TradingAccountData> getAllTradingAccountData(){
		Session session = null;
		Transaction transaction = null;
		List<TradingAccountData> tradingAccountDatas = new ArrayList<TradingAccountData>();
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			tradingAccountDatas = session.createSQLQuery(TRADING_ACCOUNT_RETRIEVE_ALL_QUERY).addEntity(TradingAccountData.class).list();
			session.flush();
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			if(null!=session && session.isOpen()){
				session.close();
			}
		} finally {
			if(null!=session && session.isOpen()){
				session.close();
			}
		}
//		List<Object[]> signalDates = HibernateUtil.getSession().createSQLQuery(TRADING_ACCOUNT_SIGNAL_DATE_QUERY).list();
//		signalDatesMap = new HashMap<String, Date>();
//		for(Object[] values : signalDates ){
//			signalDatesMap.put(values[0].toString()+values[1].toString(), (Date)values[2]);
//		}
//		
//		for(TradingAccountData tradingAccountData : tradingAccountDatas){
//			String key = tradingAccountData.getCode() + "Buy";
//			if(tradingAccountData.getSignalType().toUpperCase().contains("SELL")){
//				key = tradingAccountData.getCode() + "Sell";
//			}
//			if(signalDatesMap.containsKey(key)){
//				tradingAccountData.setSignalDate(signalDatesMap.get(key));
//			}
//		}
		return tradingAccountDatas;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public List<TradingAccountData> getTradingAccountData(String userId){
		List<TradingAccountData> tradingAccountDatas = new ArrayList<TradingAccountData>();
		List<String> codes = new ArrayList<String>();
		if(null!=userId){
			Session session = null;
			Transaction transaction = null;
			try {
				session = HibernateUtil.getSession();
				transaction = session.beginTransaction();
				codes = session.createSQLQuery(TRADING_ACCOUNT_RETRIEVE_QUERY).setParameter("userId" , userId).list();
				session.flush();
				transaction.commit();
			} catch (Exception e) {
				transaction.rollback();
				if(null!=session && session.isOpen()){
					session.close();
				}
			} finally {
				if(null!=session && session.isOpen()){
					session.close();
				}
			}
			for(String code : codes){
				if(StockDetailStaticHolder.tradingAccountMap.containsKey(code)){
					tradingAccountDatas.add(StockDetailStaticHolder.tradingAccountMap.get(code));
				}
//				String key = tradingAccountData.getCode() + "Buy";
//				if(tradingAccountData.getSignalType().toUpperCase().contains("SELL")){
//					key = tradingAccountData.getCode() + "Sell";
//				}
//				if(signalDatesMap.containsKey(key)){
//					tradingAccountData.setSignalDate(signalDatesMap.get(key));
//				}
			}
			return tradingAccountDatas;
		}else{
			return new ArrayList<TradingAccountData>();
		}
	}
	
	public TradingAccountData getTradingAccountDataForCode(String code){
		return StockDetailStaticHolder.tradingAccountMap.get(code);
	}
	
	
	public List<TradingAccountData> addTradingAccountData(String userId, List<TradingAccountData> tradingAccountDatas){
		if(null!=userId){
			Session session = null;
			Transaction transaction = null;
			try{
				session = HibernateUtil.getSession();
				transaction = session.beginTransaction();
				int insertRecords = 0;
				for(TradingAccountData tradingAccountData : tradingAccountDatas ){
					if(null!=userId){
						int insertRecord = session.createSQLQuery(TRADING_ACCOUNT_INSERT_QUERY)
						 											.setParameter("userId" , userId)
						 											.setParameter("code" , tradingAccountData.getCode())
						 											.executeUpdate();
						insertRecords = insertRecords + insertRecord;
					}
				}
				logger.info("Inserted " + insertRecords + " successfully");
				session.flush();
				transaction.commit();
			}catch(Exception e){
				transaction.rollback();
				if(null!=session && session.isOpen()){
					session.close();
				}
				logger.info("Insertion failed " + e.getMessage());
				return new ArrayList<TradingAccountData>();
			}finally{
				if(null!=session && session.isOpen()){
					session.close();
				}
			}
		}
		
		if(null==userId){
			return tradingAccountDatas;
		}
		List<TradingAccountData> tradingAccountResult = new ArrayList<TradingAccountData>();
		for(TradingAccountData tradingAccountData : tradingAccountDatas ){
			if(null!=getTradingAccountDataForCode(tradingAccountData.getCode())){
				tradingAccountResult.add(getTradingAccountDataForCode(tradingAccountData.getCode()));
			}else{
				tradingAccountData.setSignalPrice(new Double("-1"));
				tradingAccountData.setSignalType("--");
				tradingAccountResult.add(tradingAccountData); 
			}
			
		}
		return tradingAccountResult;
	}
	
	public List<TradingAccountData> removeTradingAccountData(String userId, List<TradingAccountData> tradingAccountDatas){
		if(null!=userId){
			Session session = null;
			Transaction transaction = null;
			try{
				session = HibernateUtil.getSession();
				transaction = session.beginTransaction();
				int deletedRecords = 0;
				for(TradingAccountData tradingAccountData : tradingAccountDatas ){
					if(null!=userId){
						logger.info("remove " + tradingAccountData);
						int deletedRecord = session.createSQLQuery(TRADING_ACCOUNT_DELETE_QUERY)
							 											.setParameter("code" , tradingAccountData.getCode())
							 											.setParameter("userId" , userId)
							 											.executeUpdate();
						deletedRecords = deletedRecords + deletedRecord;
					}
				}
				logger.info("Deleted " + deletedRecords + " trading accouts successfully");
				session.flush();
				transaction.commit();
			}catch(Exception e){
				transaction.rollback();
				if(null!=session && session.isOpen()){
					session.close();
				}
				logger.info("Updation failed " + e.getMessage());
				return new ArrayList<TradingAccountData>();
			}finally{
				if(null!=session && session.isOpen()){
					session.close();
				}
			}
		}
		return tradingAccountDatas;
	}
	
	public void sendDailyTradingAccountUpdateMail(){
		
		HashMap<String, List<String>> userStocksMap = new HashMap<String, List<String>>();
		Session session = null;
		Transaction transaction = null;
		List<?> userTradingAccountDatas = null;
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			userTradingAccountDatas = session.createSQLQuery(TRADING_ACCOUNT_RETRIEVE_QUERY_MAIL).list();
			session.flush();
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			if(null!=session && session.isOpen()){
				session.close();
			}
		} finally {
			if(null!=session && session.isOpen()){
				session.close();
			}
		}
		for(Object obj : userTradingAccountDatas ){
			if(obj instanceof Object[]){
				Object[] userStock = (Object[])obj;
				String user = userStock[0].toString();
				String stock = userStock[1].toString();
				if(!userStocksMap.containsKey(user)){
					userStocksMap.put(user, new ArrayList<String>());
				}
				userStocksMap.get(user).add(stock);
			}
		}
		
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String date = format.format(IntelliInvestDataDao.getInstance().getBhavDataMaxDate());
		
		for(String user : userStocksMap.keySet()){
			try{
				String message = "<table>";
				
				message = message + "<tr>"
					+ "<td><h2>Daily signal update</h2></td>"
					+ "<td><img src=\"http://intelliinvest.com/data/images/intelliinvest_logo.png\" style=\"display:block;\" width=\"200\" height=\"60\" border=\"0\">"
					+ "</tr>";
				
				message = message + "<tr><br><b>Signals Changed<b><br></tr>";
			
				message = message + "<tr>";
				message = message + getChangedSignals(date, userStocksMap.get(user));
				message = message + "</tr>";
				
				message = message + "<tr><br><b>PNL Information<b><br></tr>";
				
				message = message + "<tr>";
				message = message + getPNLInfo(user);
				message = message + "</tr>";
				
				message = message + "</table><br>Regards,<br>IntelliInvest Team.";
				
				MailUtil.sendMail(IntelliInvestStore.properties.getProperty("smtp.host"), IntelliInvestStore.properties.getProperty("mail.from"),
						IntelliInvestStore.properties.getProperty("mail.password"), 
						new String[]{UserDetailDao.getInstance().getMailIdFromUserId(user)}, "Daily signal update from IntelliInvest for " + date, message);
			}catch(Exception e){
				logger.info("Error sending daily update mail for user " + user);
			}
		}
	}
	
	public String getBuySellColor(String bs){
		if (bs.toUpperCase().contains("WAIT")) { 
			return orange; 
        }else if (bs.toUpperCase().contains("HOLD")) {  
        	return lime;
        }else if (bs.toUpperCase().contains("BUY")) {  
            return green;  
        }else if (bs.toUpperCase().contains("SELL")) {  
            return red;  
        }else {  
        	return ""; 
        } 
	}
	public String getChangedSignals(String date, List<String> stocks){
		
        
		boolean signalChanged = false;
		String message = "<table cellpadding=\"5\" cellspacing=\"3\" style=\"border:1px solid black;border-collapse:collapse;text-align:center;\">";
		message = message + "<tr>"
				+ "<th cellpadding=\"5\" cellspacing=\"3\" style=\"background:lightgray;border:1px solid black;border-collapse:collapse;text-align:center;\">Stock</th>"
				+ "<th cellpadding=\"5\" cellspacing=\"3\" style=\"background:lightgray;border:1px solid black;border-collapse:collapse;text-align:center;\">Previous Signal</th>"
				+ "<th cellpadding=\"5\" cellspacing=\"3\" style=\"background:lightgray;border:1px solid black;border-collapse:collapse;text-align:center;\">Signal</th>"
				+ "</tr>";
		for(String stock : stocks){
			TradingAccountData tradingAccountData = StockDetailStaticHolder.tradingAccountMap.get(stock);
			if(null==tradingAccountData || null==tradingAccountData.getSignalDate()){
				continue;
			}
			String signalDate = new SimpleDateFormat("yyyyMMdd").format(tradingAccountData.getSignalDate());
			if(null!=tradingAccountData && signalDate.equals(date) && !tradingAccountData.getYesterdaySignalType().equalsIgnoreCase(tradingAccountData.getSignalType())){
				signalChanged = true;
				message = message + "<tr>"
						+ "<td cellpadding=\"5\" cellspacing=\"3\" style=\"border:1px solid black;border-collapse:collapse;text-align:center;\">" + stock + "</td>"
						+ "<td cellpadding=\"5\" cellspacing=\"3\" style=\"border:1px solid black;border-collapse:collapse;text-align:center;" + getBuySellColor(tradingAccountData.getYesterdaySignalType()) + "\">" + tradingAccountData.getYesterdaySignalType() + "</td>"
						+ "<td cellpadding=\"5\" cellspacing=\"3\" style=\"border:1px solid black;border-collapse:collapse;text-align:center;" + getBuySellColor(tradingAccountData.getSignalType()) + "\">" + tradingAccountData.getSignalType() + "</td>"
						+ "</tr>";
			}
		}
		message = message + "</table>";
		if(!signalChanged){
			message = "<table><tr>No signals Changed for today from your Watch List</tr></table>";
		}
		return message;
	}
	
	
	public String getPNLInfo(String userId){
		HashMap<String, ManagePortfolioData> portfolioDataMap = ManagePortfolioDao.getInstance().getManagePortfolioSummaryData(ManagePortfolioDao.getInstance().getManagePortfolioData(userId));
		Double totalPnl = 0D;
		Double totalTodaysPnl = 0D;
		String message = "<table cellpadding=\"5\" cellspacing=\"3\" style=\"border:1px solid black;border-collapse:collapse;text-align:center;\">";
		message = message + "<tr>"
				+ "<th cellpadding=\"5\" cellspacing=\"3\" style=\"background:lightgray;border:1px solid black;border-collapse:collapse;text-align:center;\">Stock</th>"
				+ "<th cellpadding=\"5\" cellspacing=\"3\" style=\"background:lightgray;border:1px solid black;border-collapse:collapse;text-align:center;\">Balance</th>"
				+ "<th cellpadding=\"5\" cellspacing=\"3\" style=\"background:lightgray;border:1px solid black;border-collapse:collapse;text-align:center;\">Average Price</th>"
				+ "<th cellpadding=\"5\" cellspacing=\"3\" style=\"background:lightgray;border:1px solid black;border-collapse:collapse;text-align:center;\">EOD Price</th>"
				+ "<th cellpadding=\"5\" cellspacing=\"3\" style=\"background:lightgray;border:1px solid black;border-collapse:collapse;text-align:center;\">Todays PNL</th>"
				+ "<th cellpadding=\"5\" cellspacing=\"3\" style=\"background:lightgray;border:1px solid black;border-collapse:collapse;text-align:center;\">Overall PNL</th>"
				+ "</tr>";
		DecimalFormat nf = new DecimalFormat("##,##,##,##,##,##0.00");
		for(String stock : portfolioDataMap.keySet()){
			Double pnl = 0D;
			Double todaysPnl = 0D;
			ManagePortfolioData managePortfolioData = portfolioDataMap.get(stock);
			Double eodPrice = StockDetailStaticHolder.getEODPrice(stock);
			Double cp = StockDetailStaticHolder.getCP(stock);
			Double currentPrice = StockDetailStaticHolder.getCurrentPrice(stock);
			todaysPnl = (managePortfolioData.getRemainingQuantity() * eodPrice * cp)/100;
			pnl = managePortfolioData.getRemainingQuantity() * (currentPrice - managePortfolioData.getPrice());
			
			totalPnl = totalPnl + pnl;
			totalTodaysPnl = totalTodaysPnl + todaysPnl;
			
			message = message + "<tr>"
					+ "<td cellpadding=\"5\" cellspacing=\"3\" style=\"border:1px solid black;border-collapse:collapse;text-align:center;" + ((pnl>=0)?green:red) + "\">" + stock + "</td>"
					+ "<td cellpadding=\"5\" cellspacing=\"3\" style=\"border:1px solid black;border-collapse:collapse;text-align:center;\">" + managePortfolioData.getRemainingQuantity() + "</td>"
					+ "<td cellpadding=\"5\" cellspacing=\"3\" style=\"border:1px solid black;border-collapse:collapse;text-align:center;\">" + nf.format(managePortfolioData.getPrice()) + "</td>"
					+ "<td cellpadding=\"5\" cellspacing=\"3\" style=\"border:1px solid black;border-collapse:collapse;text-align:center;\">" + nf.format(eodPrice) + "</td>"
					+ "<td cellpadding=\"5\" cellspacing=\"3\" style=\"border:1px solid black;border-collapse:collapse;text-align:center;" + ((todaysPnl>=0)?green:red) + "\">" + nf.format(todaysPnl) + "</td>"
					+ "<td cellpadding=\"5\" cellspacing=\"3\" style=\"border:1px solid black;border-collapse:collapse;text-align:center;" + ((pnl>=0)?green:red) + "\">" + nf.format(pnl) + "</td>"
					+ "</tr>";
		}
		
		message = message + "<tr>"
				+ "<td cellpadding=\"5\" cellspacing=\"3\" style=\"font-weight:bold;border:1px solid black;border-collapse:collapse;text-align:center;" + ((totalPnl>=0)?green:red) + "\">" + "Total" + "</td>"
				+ "<td cellpadding=\"5\" cellspacing=\"3\" style=\"font-weight:bold;border:1px solid black;border-collapse:collapse;text-align:center;\">" + "-" + "</td>"
				+ "<td cellpadding=\"5\" cellspacing=\"3\" style=\"font-weight:bold;border:1px solid black;border-collapse:collapse;text-align:center;\">" + "-" + "</td>"
				+ "<td cellpadding=\"5\" cellspacing=\"3\" style=\"font-weight:bold;border:1px solid black;border-collapse:collapse;text-align:center;\">" + "-" + "</td>"
				+ "<td cellpadding=\"5\" cellspacing=\"3\" style=\"font-weight:bold;border:1px solid black;border-collapse:collapse;text-align:center;" + ((totalTodaysPnl>=0)?green:red) + "\">" + nf.format(totalTodaysPnl) + "</td>"
				+ "<td cellpadding=\"5\" cellspacing=\"3\" style=\"font-weight:bold;border:1px solid black;border-collapse:collapse;text-align:center;" + ((totalPnl>=0)?green:red) + "\">" + nf.format(totalPnl) + "</td>"
				+ "</tr>";
		
		message = message + "</table>";
		if(portfolioDataMap.size()==0){
			message = "<table><tr>Please add information in your Manage portfolio tab for receiving update of your PNL information.</tr></table>";
		}
		return message;
	}
	
	
}
