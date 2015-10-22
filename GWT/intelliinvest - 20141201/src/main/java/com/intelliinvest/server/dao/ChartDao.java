package com.intelliinvest.server.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.intelliinvest.client.data.EODChartData;
import com.intelliinvest.client.data.EODSignalChartData;
import com.intelliinvest.client.data.EODVolumeChartData;
import com.intelliinvest.server.IntelliInvestStore;
import com.intelliinvest.server.util.HibernateUtil;

public class ChartDao {

	private static Logger logger = Logger.getLogger(ChartDao.class);
	
	private static ChartDao chartDao;
	
	private static String SIMULATION_CHART_QUERY = "SELECT a.CODE as code, a.EOD_DATE as date, SIGNAL_TYPE as signalType, EOD_Price as price  "
			+ " from EOD_STOCK_PRICE a LEFT OUTER JOIN STOCK_SIGNAL_DETAILS b ON a.CODE=b.CODE and EOD_DATE=SIGNAL_DATE "
			+ " where (a.EOD_DATE >= :date and a.CODE= :code) "
			+ " ORDER BY a.EOD_DATE";
	
	private static String EOD_CHART_QUERY = "SELECT CODE as code, EOD_DATE as date, EOD_PRICE as price from EOD_STOCK_PRICE "
			+ " where (EOD_DATE >= :date and CODE= :code) "
			+ " ORDER BY EOD_DATE";
	
	private static String EOD_VOLUME_CHART_QUERY = "SELECT SYMBOL as code, TIMESTAMP as date, LAST as price, TOTALTRADES as volume from BHAV_DATA "
			+ " where (TIMESTAMP >= :date and SYMBOL= :code) "
			+ " ORDER BY TIMESTAMP";
	
	private static String SIGNAL_INSERT_QUERY = "INSERT INTO STOCK_SIGNAL_DETAILS values (:code, :signalDate, :previousSignalType, :signalType)";
	
	private static String SIGNAL_INSERT_QUERY_ALL = "INSERT INTO STOCK_SIGNAL_DETAILS "
			+ "(select SYMBOL, SIGNAL_DATE, PREVIOUS_SIGNAL_TYPE, SIGNAL_TYPE from SIGNAL_COMPONENTS_#MA# where SIGNAL_PRESENT='Y')";
	
	private static String SIGNAL_INSERT_QUERY_ALL_DATE = "INSERT INTO STOCK_SIGNAL_DETAILS "
			+ "(select SYMBOL, SIGNAL_DATE, PREVIOUS_SIGNAL_TYPE, SIGNAL_TYPE from SIGNAL_COMPONENTS_#MA# where SIGNAL_DATE=:date and SIGNAL_PRESENT='Y')";
	
	private static String SIGNAL_INSERT_QUERY_STOCK = "INSERT INTO STOCK_SIGNAL_DETAILS "
			+ "(select SYMBOL, SIGNAL_DATE, PREVIOUS_SIGNAL_TYPE, SIGNAL_TYPE from SIGNAL_COMPONENTS_#MA# where SYMBOL=:symbol and SIGNAL_PRESENT='Y')";
	
	private static String SIGNAL_INSERT_QUERY_STOCK_DATE = "INSERT INTO STOCK_SIGNAL_DETAILS "
			+ "(select SYMBOL, SIGNAL_DATE, PREVIOUS_SIGNAL_TYPE, SIGNAL_TYPE from SIGNAL_COMPONENTS_#MA# where SYMBOL=:symbol and SIGNAL_DATE=:date and SIGNAL_PRESENT='Y')";
	
	private static String SIGNAL_DELETE_QUERY_ALL = "DELETE FROM STOCK_SIGNAL_DETAILS";
	
	private static String SIGNAL_DELETE_QUERY_ALL_DATE = "DELETE FROM STOCK_SIGNAL_DETAILS where SIGNAL_DATE=:date";
	
	private static String SIGNAL_DELETE_QUERY_STOCK = "DELETE FROM STOCK_SIGNAL_DETAILS where CODE=:symbol";
	
	private static String SIGNAL_DELETE_QUERY_STOCK_DATE = "DELETE FROM STOCK_SIGNAL_DETAILS where CODE=:symbol and SIGNAL_DATE=:date";
	
	private ChartDao() {
//		CREATE Table EOD_STOCK_PRICE( CODE varchar(100), EOD_DATE DATE, EOD_PRICE DOUBLE )
//		CREATE Table STOCK_SIGNAL_DETAILS( CODE varchar(100), SIGNAL_DATE DATE, PREVIOUS_SIGNAL_TYPE varchar(15), SIGNAL_TYPE varchar(15) )
	}
	
	public static ChartDao getInstance(){
		if(null==chartDao){
			synchronized (ChartDao.class) {
				if(null==chartDao){
					chartDao = new ChartDao();
					logger.info("Initialised chartDao");
				}
			}
		}
		return chartDao;
	}
	
	@SuppressWarnings("unchecked")
	public List<EODSignalChartData> getSimulationChartData(String code, String type){
		Session session = null;
		Transaction transaction = null;
		List<EODSignalChartData> eodSignalChartDatas = new ArrayList<EODSignalChartData>();
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			eodSignalChartDatas = session.createSQLQuery(SIMULATION_CHART_QUERY).addEntity(EODSignalChartData.class)
				 .setParameter("code" , code)
				 .setParameter("date" , getStartDateForSimulation(type))
				 .list();
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
		return eodSignalChartDatas;
	}
	
	@SuppressWarnings("unchecked")
	public List<EODChartData> getEODChartData(String code, String type){
		Session session = null;
		Transaction transaction = null;
		List<EODChartData> eodChartDatas = new ArrayList<EODChartData>();
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			eodChartDatas = session.createSQLQuery(EOD_CHART_QUERY).addEntity(EODChartData.class)
				 .setParameter("code" , code)
				 .setParameter("date" , getStartDateForSimulation(type))
				 .list();
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
		return eodChartDatas;
	}
	
	@SuppressWarnings("unchecked")
	public List<EODVolumeChartData> getEODVolumeChartData(String code, String type){
		Session session = null;
		Transaction transaction = null;
		List<EODVolumeChartData> eodVolumeChartDatas = new ArrayList<EODVolumeChartData>();
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			eodVolumeChartDatas =  session.createSQLQuery(EOD_VOLUME_CHART_QUERY).addEntity(EODVolumeChartData.class)
				 .setParameter("code" , code)
				 .setParameter("date" , getStartDateForSimulation(type))
				 .list();
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
		return eodVolumeChartDatas;
	}
	
	public List<EODVolumeChartData> getVolumeIntraChartData(String code, String type){
		List<EODVolumeChartData> chartData = IntelliInvestStore.getIntraDayPriceVolumeData(code);
		Date lastDate = chartData.get(chartData.size()-1).getDate();
		int i=0;
		while(chartData.size()<240){
			chartData.add(new EODVolumeChartData(code, new Date(lastDate.getTime() + (i*120000)), null, null));
			i++;
		}
		return chartData;
	}
	
	private Date getStartDateForSimulation(String type){
		Calendar cal = Calendar.getInstance();
		
		if(type.toLowerCase().contains("quaterly")){
			cal.add(Calendar.MONTH, -3);
		}else if(type.toLowerCase().contains("halfyearly")){
			cal.add(Calendar.MONTH, -6);
		}else if(type.toLowerCase().contains("yearly")){
			cal.add(Calendar.YEAR, -1);
		}else if(type.toLowerCase().contains("all")){
			cal.add(Calendar.YEAR, -20);
		}
		return cal.getTime();
	}
	
	public boolean insertSignals(Integer ma, String symbol, Date date){
		Boolean success= false;
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			if(null==date && symbol.equals("ALL")){
				logger.info("Deleting All records in " + " STOCK_SIGNAL_DETAILS");
				int deletedCount = session.createSQLQuery(SIGNAL_DELETE_QUERY_ALL).executeUpdate();
				logger.info("Deleted All " + deletedCount + " records in " + " STOCK_SIGNAL_DETAILS");
				int insertRecord = session.createSQLQuery(SIGNAL_INSERT_QUERY_ALL.replace("#MA#", ""+ma)).executeUpdate();
				logger.info("Inserted ALL " + insertRecord + " successfully in " + " STOCK_SIGNAL_DETAILS");
			}else if(null==date && !symbol.equals("ALL")){
				logger.info("Deleting records in " + " STOCK_SIGNAL_DETAILS for symbol " + symbol);
				int deletedCount = session.createSQLQuery(SIGNAL_DELETE_QUERY_STOCK).setParameter("symbol", symbol).executeUpdate();
				logger.info("Deleted All " + deletedCount + " records in " + " STOCK_SIGNAL_DETAILS for symbol " + symbol);
				int insertRecord = session.createSQLQuery(SIGNAL_INSERT_QUERY_STOCK.replace("#MA#", ""+ma)).setParameter("symbol", symbol).executeUpdate();
				logger.info("Inserted " + insertRecord + " successfully in " + " STOCK_SIGNAL_DETAILS  for symbol " + symbol);
			}else if(null!=date && symbol.equals("ALL")){
				logger.info("Deleting All records in " + " STOCK_SIGNAL_DETAILS for date " + date);
				int deletedCount = session.createSQLQuery(SIGNAL_DELETE_QUERY_ALL_DATE).setParameter("date", date).executeUpdate();
				logger.info("Deleted All " + deletedCount + " records in " + " STOCK_SIGNAL_DETAILS for date " + date);
				int insertRecord = session.createSQLQuery(SIGNAL_INSERT_QUERY_ALL_DATE.replace("#MA#", ""+ma)).setParameter("date", date).executeUpdate();
				logger.info("Inserted " + insertRecord + " successfully in " + " STOCK_SIGNAL_DETAILS  for date " + date);
			}else if(null!=date && !symbol.equals("ALL")){
				logger.info("Deleting records in " + " STOCK_SIGNAL_DETAILS  for symbol " + symbol + " and date " + date);
				int deletedCount = session.createSQLQuery(SIGNAL_DELETE_QUERY_STOCK_DATE).setParameter("date", date).setParameter("symbol", symbol).executeUpdate();
				logger.info("Deleted " + deletedCount + " records in " + " STOCK_SIGNAL_DETAILS for symbol " + symbol + " and date " + date);
				int insertRecord = session.createSQLQuery(SIGNAL_INSERT_QUERY_STOCK_DATE.replace("#MA#", ""+ma)).setParameter("date", date).setParameter("symbol", symbol).executeUpdate();
				logger.info("Inserted " + insertRecord + " successfully in " + " STOCK_SIGNAL_DETAILS  for " + symbol + " and date " + date);
			}
			
			session.flush();
			transaction.commit();
		}catch(Exception e){
			transaction.rollback();
			if(null!=session && session.isOpen()){
				session.close();
			}
			logger.info("Insertion failed for STOCK_SIGNAL_DETAILS " + e.getMessage());
			success = false;
		}finally{
			if(null!=session && session.isOpen()){
				session.close();
			}
		}
			
		return success;
	}
	

	public boolean insertSignals(List<String> signals){
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			int insertRecords = 0;
			for(String signal : signals ){
				String[] signalValues = signal.split(",");
				int insertRecord = session.createSQLQuery(SIGNAL_INSERT_QUERY)
				 											.setParameter("code" , signalValues[0])
				 											.setParameter("signalDate" , new SimpleDateFormat("yyyy-MM-dd").parse(signalValues[1]))
				 											.setParameter("previousSignalType" , signalValues[2])
				 											.setParameter("signalType" , signalValues[3])
				 											.executeUpdate();
				insertRecords = insertRecords + insertRecord;
			}
			logger.info("Inserted " + insertRecords + " successfully in STOCK_SIGNAL_DETAILS");
			session.flush();
			transaction.commit();
		}catch(Exception e){
			transaction.rollback();
			if(null!=session && session.isOpen()){
				session.close();
			}
			logger.info("Insertion failed for STOCK_SIGNAL_DETAILS " + e.getMessage());
			return false;
		}finally{
			if(null!=session && session.isOpen()){
				session.close();
			}
		}
		return true;
	}
}
