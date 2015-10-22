package com.intelliinvest.server.dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.intelliinvest.client.data.BhavData;
import com.intelliinvest.server.HttpUtil;
import com.intelliinvest.server.util.HibernateUtil;

public class IntelliInvestDataDao {

	private static Logger logger = Logger.getLogger(IntelliInvestDataDao.class);
	
	private static IntelliInvestDataDao intelliInvestDataDao;
	
	
	private static String INTELLI_INVEST_DATA_INSERT_QUERY = "INSERT INTO INTELLI_INVEST_DATA (CODE, EOD_PRICE, SIGNAL_PRICE, YESTERDAY_SIGNAL_TYPE, SIGNAL_TYPE, QUARTERLY, HALF_YEARLY, NINE_MONTHS, YEARLY)"
																					+ "values (:code, :eodPrice, :signalPrice, :yesterdaySignalType, :signalType, :quarterly, :halfYearly, :nineMonths, :yearly)";
	private static String INTELLI_INVEST_DATA_DELETE_QUERY = "DELETE FROM INTELLI_INVEST_DATA";
	
	
	private static String BHAV_DATA_INSERT_QUERY = "INSERT INTO BHAV_DATA (EXCHANGE, SYMBOL,SERIES,OPEN,HIGH,LOW,CLOSE,LAST,PREVCLOSE,TOTTRDQTY,TOTTRDVAL,TIMESTAMP,TOTALTRADES,ISIN)"
			+ "values (:EXCHANGE, :SYMBOL, :SERIES, :OPEN, :HIGH, :LOW, :CLOSE, :LAST, :PREVCLOSE, :TOTTRDQTY, :TOTTRDVAL, :TIMESTAMP, :TOTALTRADES, :ISIN)";
	
	private static String BHAV_DATA_QUERY = "SELECT EXCHANGE as exchange, SYMBOL as symbol,SERIES as series,OPEN as open,HIGH as high,LOW as low,CLOSE as close"
			+ ",LAST as last,PREVCLOSE as prevClose,TOTTRDQTY as totTrdQty,TOTTRDVAL as totTrdVal,TIMESTAMP as timeStamp,TOTALTRADES as totalTrades,ISIN as isin FROM "
			+ "BHAV_DATA where SYMBOL=:symbol order by TIMESTAMP ASC";

	private static String BHAV_DATA_DELETE_QUERY = "DELETE FROM BHAV_DATA where TIMESTAMP=:TIMESTAMP";
	
	private static String BHAV_DATA_QUERY_N = "SELECT EXCHANGE as exchange, SYMBOL as symbol,SERIES as series,OPEN as open,HIGH as high,LOW as low,CLOSE as close"
			+ ",LAST as last,PREVCLOSE as prevClose,TOTTRDQTY as totTrdQty,TOTTRDVAL as totTrdVal,TIMESTAMP as timeStamp,TOTALTRADES as totalTrades,ISIN as isin FROM "
			+ "BHAV_DATA where SYMBOL=:symbol order by TIMESTAMP DESC LIMIT #MA#";
	
	
	private static String BHAV_DATA_QUERY_DATE_N = "SELECT EXCHANGE as exchange, SYMBOL as symbol,SERIES as series,OPEN as open,HIGH as high,LOW as low,CLOSE as close"
			+ ",LAST as last,PREVCLOSE as prevClose,TOTTRDQTY as totTrdQty,TOTTRDVAL as totTrdVal,TIMESTAMP as timeStamp,TOTALTRADES as totalTrades,ISIN as isin FROM "
			+ "BHAV_DATA where TIMESTAMP>=:date order by TIMESTAMP DESC";
	
	
	private static String MAX_DATE_BHAV_DATA = " SELECT MAX(TIMESTAMP) from BHAV_DATA";
	
	private IntelliInvestDataDao() {
//		 CREATE Table INTELLI_INVEST_DATA ( CODE varchar(100), EOD_PRICE DOUBLE, SIGNAL_PRICE DOUBLE, YESTERDAY_SIGNAL_TYPE varchar(15), SIGNAL_TYPE varchar(15), QUARTERLY DOUBLE, HALF_YEARLY DOUBLE, NINE_MONTHS DOUBLE, YEARLY DOUBLE)
//		CREATE Table BHAV_DATA ( EXCHANGE varchar(8), SYMBOL varchar(20), SERIES varchar(4), OPEN DOUBLE, HIGH DOUBLE, LOW DOUBLE, CLOSE DOUBLE, LAST DOUBLE, PREVCLOSE DOUBLE, TOTTRDQTY BIGINT, TOTTRDVAL DOUBLE, TIMESTAMP DATE, TOTALTRADES  BIGINT, ISIN varchar(15))

	}
	
	public static IntelliInvestDataDao getInstance(){
		if(null==intelliInvestDataDao){
			synchronized (IntelliInvestDataDao.class) {
				if(null==intelliInvestDataDao){
					intelliInvestDataDao = new IntelliInvestDataDao();
					logger.info("Initialised IntelliInvestDataDao");
				}
			}
		}
		return intelliInvestDataDao;
	}
	
	private String getBhavDataFromNSE(Date date) throws Exception{
		ZipInputStream zis = null;
		ByteArrayOutputStream bos = null;
		try{
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy");
			SimpleDateFormat format2 = new SimpleDateFormat("MMM");
			SimpleDateFormat format3 = new SimpleDateFormat("ddMMMyyyy");
			String url = "http://nseindia.com/content/historical/EQUITIES/"
								+ format1.format(date).toUpperCase() + "/"
								+ format2.format(date).toUpperCase() + "/"
								+ "cm" + format3.format(date).toUpperCase() + "bhav.csv.zip";
			
			logger.info("Processing data for Bhav for date " + date + "from url " + url);
			
			byte[] bytes = HttpUtil.getFromUrlAsBytes(url);
			zis = new ZipInputStream(new ByteArrayInputStream(bytes));
			ZipEntry ze = zis.getNextEntry();
			byte[] buffer = new byte[1024];
	    	while(ze!=null){
	    	   String fileName = ze.getName();
	    	   if(fileName.equalsIgnoreCase("cm" + format3.format(date) + "bhav.csv")){
	    		   bos = new ByteArrayOutputStream();             
	    		   int len;
	    		   while ((len = zis.read(buffer)) > 0) {
	    			   bos.write(buffer, 0, len);
	    		   }
	    		   String fileContent = bos.toString();
	    		   return fileContent;
	    	   }
	    	ze = zis.getNextEntry();
	    	}
		}catch(Exception e){
			logger.info("Error while fetching Bhav data for " + date + " Error " + e.getMessage());
			return "";
		}
    	finally{
    		if(null!=zis){
    			zis.closeEntry();
    			zis.close();
    		}
    		if(null!=bos){
    			bos.close();
    		}
    	}
		logger.info("Not able to fetch Bhav data for " + date);
		return "";
	}
	
	public boolean backloadBhavData(Date date){
		Session session = null;
		Transaction transaction = null;
		try{
			String bhavDataContent = getBhavDataFromNSE(date);
			String[] bhavDatas = bhavDataContent.split("\n");
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			logger.info("Deleting records in BHAV_DATA for date " + date);
			int deletedCount = session.createSQLQuery(BHAV_DATA_DELETE_QUERY).setParameter("TIMESTAMP", date).executeUpdate();
			logger.info("Deleted " + deletedCount + "records in INTELLI_INVEST_DATA");
			deletedCount = session.createSQLQuery(StockDetailsDao.STOCK_EOD_PRICE_DELETE_QUERY)
					.setParameter("eodDate", date)
					.executeUpdate();
			logger.info("Deleted " + deletedCount + "records in EOD_STOCK_PRICE");
			int insertRecords = 0;
			int insertRecordsForEOD = 0;
			for(String bhavData : bhavDatas ){
				String[] bhavDataValues = bhavData.split(",");
				if(bhavDataValues.length>=13 && (bhavDataValues[1].equalsIgnoreCase("EQ") || bhavDataValues[1].equalsIgnoreCase("BE"))){
					int insertRecord = session.createSQLQuery(BHAV_DATA_INSERT_QUERY)
							.setParameter("EXCHANGE", "NSE")
							.setParameter("SYMBOL", bhavDataValues[0]) 
							.setParameter("SERIES", bhavDataValues[1]) 
							.setParameter("OPEN", new Double(bhavDataValues[2]))
							.setParameter("HIGH", new Double(bhavDataValues[3])) 
							.setParameter("LOW", new Double(bhavDataValues[4])) 
							.setParameter("CLOSE", new Double(bhavDataValues[5])) 
							.setParameter("LAST", new Double(bhavDataValues[6])) 
							.setParameter("PREVCLOSE", new Double(bhavDataValues[7])) 
							.setParameter("TOTTRDQTY", new Long(bhavDataValues[8]))
							.setParameter("TOTTRDVAL", new Double(bhavDataValues[9]))
							.setParameter("TIMESTAMP", new SimpleDateFormat("dd-MMM-yyyy").parse(bhavDataValues[10]))
							.setParameter("TOTALTRADES", new Long(bhavDataValues[11]))
							.setParameter("ISIN", bhavDataValues[12]) 
					 		.executeUpdate();
					insertRecords = insertRecords + insertRecord;
					insertRecord = session.createSQLQuery(StockDetailsDao.STOCK_EOD_PRICE_INSERT_QUERY)
							.setParameter("code",  bhavDataValues[0])
							.setParameter("eodDate", new SimpleDateFormat("dd-MMM-yyyy").parse(bhavDataValues[10]))
							.setParameter("eodPrice", new Double(bhavDataValues[6])) 
							.executeUpdate();
					insertRecordsForEOD = insertRecordsForEOD + insertRecord;
				}
			}
			logger.info("Inserted " + insertRecords + " successfully in BHAV_DATA");
			logger.info("Inserted " + insertRecordsForEOD + " successfully in EOD_STOCK_PRICE");
			session.flush();
			transaction.commit();
		}catch(Exception e){
			transaction.rollback();
			if(null!=session && session.isOpen()){
				session.close();
			}
			logger.info("Insertion failed for BHAV_DATA " + e.getMessage());
			return false;
		}finally{
			if(null!=session && session.isOpen()){
				session.close();
			}
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public List<BhavData> getBhavData(String symbol){
		logger.info("getBhavData for symbaol " + symbol);
		List<BhavData> bhavDatas = new ArrayList<BhavData>();
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			bhavDatas = session.createSQLQuery(BHAV_DATA_QUERY).addEntity(BhavData.class).setParameter("symbol", symbol).list();
			session.flush();
			transaction.commit();
		}catch(Exception e){
			logger.error("Error in getBhavData " + e.getMessage());
			transaction.rollback();
			if(null!=session && session.isOpen()){
				session.close();
			}
			logger.info("Insertion failed " + e.getMessage());
		}finally{
			if(null!=session && session.isOpen()){
				session.close();
			}
		}
		return bhavDatas;
	}
	
	@SuppressWarnings("unchecked")
	public List<BhavData> getBhavData(String symbol, Integer ma){
		logger.info("getBhavData for symbol " + symbol + " and ma " + ma);
		Session session = null;
		Transaction transaction = null;
		List<BhavData> bhavDatas = new ArrayList<BhavData>();
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			bhavDatas = session.createSQLQuery(BHAV_DATA_QUERY_N.replace("#MA#", ""+ma)).addEntity(BhavData.class).setParameter("symbol", symbol).list();
			session.flush();
			transaction.commit();
		} catch (Exception e) {
			logger.error("Error in getBhavData " + e.getMessage());
			transaction.rollback();
			if(null!=session && session.isOpen()){
				session.close();
			}
		} finally {
			if(null!=session && session.isOpen()){
				session.close();
			}
		}
		return bhavDatas;
	}
	
	@SuppressWarnings("rawtypes")
	public Date getBhavDataMaxDate(){
		Session session = null;
		Transaction transaction = null;
		Date maxDate = new Date();
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			List maxDateL = session.createSQLQuery(MAX_DATE_BHAV_DATA).list();
			if(maxDateL.size()>0){
				maxDate = (Date)maxDateL.get(0);
			}
			session.flush();
			transaction.commit();
		} catch (Exception e) {
			logger.error("Error in getBhavDataMaxDate " + e.getMessage());
			transaction.rollback();
			if(null!=session && session.isOpen()){
				session.close();
			}
		} finally {
			if(null!=session && session.isOpen()){
				session.close();
			}
		}
		logger.info("getBhavDataMaxDate returned " + maxDate);
		return maxDate;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,List<BhavData>> getBhavData(Date date, Integer ma){
		logger.info("getBhavData for " + date + " and ma " + ma);
		Map<String,List<BhavData>> bhavDatas = new HashMap<String, List<BhavData>>();
		Calendar fromCal = Calendar.getInstance();
		fromCal.setTime(date);
		fromCal.set(Calendar.HOUR, 0);
		fromCal.set(Calendar.MINUTE, 0);
		fromCal.set(Calendar.SECOND, 0);
		fromCal.set(Calendar.MILLISECOND, 0);
		fromCal.set(Calendar.AM_PM, Calendar.AM);
		fromCal.add(Calendar.DATE, -20);
		Session session = null;
		Transaction transaction = null;
		List<BhavData> allBhavDatas = new ArrayList<BhavData>();
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			allBhavDatas = session.createSQLQuery(BHAV_DATA_QUERY_DATE_N.replace("#MA#", ""+ma)).addEntity(BhavData.class).setParameter("date", fromCal.getTime()).list();
			session.flush();
			transaction.commit();
		} catch (Exception e) {
			logger.error("Error in getBhavData " + e.getMessage());
			transaction.rollback();
			if(null!=session && session.isOpen()){
				session.close();
			}
		} finally {
			if(null!=session && session.isOpen()){
				session.close();
			}
		}
		for(BhavData bhavData : allBhavDatas){
			String symbol = bhavData.getSymbol();
			if(!bhavDatas.containsKey(symbol)){
				bhavDatas.put(symbol, new ArrayList<BhavData>());
			}
			List<BhavData> bhavDataSymbol = bhavDatas.get(symbol);
			if(bhavDataSymbol.size()<10){
				bhavDataSymbol.add(bhavData);
				bhavDatas.put(symbol, bhavDataSymbol);
			}
		}
		return bhavDatas;
	}
	
	public boolean updateIntelliInvestDataDetails(List<String> stockDetails){
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			logger.info("Deleting records in INTELLI_INVEST_DATA");
			int deletedCount = session.createSQLQuery(INTELLI_INVEST_DATA_DELETE_QUERY).executeUpdate();
			logger.info("Deleted " + deletedCount + "records in INTELLI_INVEST_DATA");
			int insertRecords = 0;
			for(String stockDetail : stockDetails ){
				String[] stockDetailValues = stockDetail.split(",");
				int insertRecord = session.createSQLQuery(INTELLI_INVEST_DATA_INSERT_QUERY)
							.setParameter("code" , stockDetailValues[0])
							.setParameter("eodPrice" , new Double(stockDetailValues[1]))
							.setParameter("signalPrice" , new Double(stockDetailValues[2]))
							.setParameter("yesterdaySignalType" , stockDetailValues[3])
							.setParameter("signalType" , stockDetailValues[4])
							.setParameter("quarterly" , stockDetailValues[5])
							.setParameter("halfYearly" , stockDetailValues[6])
							.setParameter("nineMonths" , stockDetailValues[7])
							.setParameter("yearly" , stockDetailValues[8])
				 			.executeUpdate();
				insertRecords = insertRecords + insertRecord;
			}
			logger.info("Inserted " + insertRecords + " successfully in INTELLI_INVEST_DATA");
			session.flush();
			transaction.commit();
		}catch(Exception e){
			transaction.rollback();
			if(null!=session && session.isOpen()){
				session.close();
			}
			logger.info("Insertion failed for INTELLI_INVEST_DATA" + e.getMessage());
			return false;
		}finally{
			if(null!=session && session.isOpen()){
				session.close();
			}
		}
		return true;
	}
	
}
