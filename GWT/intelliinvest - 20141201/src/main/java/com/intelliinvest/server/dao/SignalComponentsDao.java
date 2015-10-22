package com.intelliinvest.server.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.intelliinvest.client.data.IdValueData;
import com.intelliinvest.client.data.SignalComponents;
import com.intelliinvest.server.util.HibernateUtil;

public class SignalComponentsDao {

	private static Logger logger = Logger.getLogger(SignalComponentsDao.class);
	
	private static SignalComponentsDao signalComponentsDao;
	
	private static String SIGNAL_COMPONENTS_QUERY = "select SYMBOL as symbol, SIGNAL_DATE as signalDate, TR as TR, PLUS_DM_1 as plusDM1, MINUS_DM_1 as minusDM1, TR_N as TRn, PLUS_DM_N as plusDMn,"
	+ "	MINUS_DM_N as minusDMn, DIFF_DI_N as diffDIn, SUM_DI_N as sumDIn, DX as DX, ADX_N as ADXn, SPLIT_MULTIPLIER as splitMultiplier, "
	+ "	PREVIOUS_SIGNAL_TYPE as previousSignalType, SIGNAL_TYPE as signalType, SIGNAL_PRESENT as signalPresent from SIGNAL_COMPONENTS_#MA# "
	+ " where SYMBOL=:symbol and SIGNAL_DATE=(select MAX(SIGNAL_DATE) from SIGNAL_COMPONENTS_#MA# where SYMBOL=:symbol)";
	
	private static String SIGNAL_COMPONENTS_QUERY_N = "select SYMBOL as symbol, SIGNAL_DATE as signalDate, TR as TR, PLUS_DM_1 as plusDM1, MINUS_DM_1 as minusDM1, TR_N as TRn, PLUS_DM_N as plusDMn,"
			+ "	MINUS_DM_N as minusDMn, DIFF_DI_N as diffDIn, SUM_DI_N as sumDIn, DX as DX, ADX_N as ADXn, SPLIT_MULTIPLIER as splitMultiplier, "
			+ "	PREVIOUS_SIGNAL_TYPE as previousSignalType, SIGNAL_TYPE as signalType, SIGNAL_PRESENT as signalPresent from SIGNAL_COMPONENTS_#MA# "
			+ " where SIGNAL_DATE>=:date ORDER BY SIGNAL_DATE DESC";

	private static String SIGNAL_COMPONENTS_INSERT_QUERY = "INSERT INTO SIGNAL_COMPONENTS_#MA# (SYMBOL, SIGNAL_DATE, TR, PLUS_DM_1, MINUS_DM_1, TR_N, PLUS_DM_N,"
	+ "	MINUS_DM_N, DIFF_DI_N, SUM_DI_N, DX, ADX_N, SPLIT_MULTIPLIER, "
	+ " PREVIOUS_SIGNAL_TYPE, SIGNAL_TYPE, SIGNAL_PRESENT) values (:symbol, :signalDate, :TR, :plusDM1, :minusDM1, :TRn, :plusDMn,"
	+ "	:minusDMn, :diffDIn, :sumDIn, :DX, :ADXn, :splitMultiplier, "
	+ "	:previousSignalType, :signalType, :signalPresent)";
	
	private static String SIGNAL_COMPONENTS_DELETE_QUERY = "DELETE FROM SIGNAL_COMPONENTS_#MA# where SYMBOL=:symbol";
	
	private static String SIGNAL_COMPONENTS_DELETE_DATE_QUERY = "DELETE FROM SIGNAL_COMPONENTS_#MA# where SYMBOL=:symbol and SIGNAL_DATE=:date";
	
	private static String MAGIC_NUMBER_QUERY = "SELECT SYMBOL as id, MAGIC_NUMBER as value from  MAGIC_NUMBERS";
	
	private static String DELETE_MAGIC_NUMBERS = "DELETE FROM MAGIC_NUMBERS WHERE SYMBOL=:symbol";
	
	private static String INSERT_MAGIC_NUMBERS = "INSERT INTO MAGIC_NUMBERS (SYMBOL, MAGIC_NUMBER, PNL) values (:symbol, :magicNumber, :pnl)";
	
	private SignalComponentsDao() {
		
//		CREATE TABLE SIGNAL_COMPONENTS_#MA# (SYMBOL varchar(100), SIGNAL_DATE DATE, TR DOUBLE, PLUS_DM_1 DOUBLE, MINUS_DM_1 DOUBLE, TR_N DOUBLE, PLUS_DM_N DOUBLE, MINUS_DM_N DOUBLE, DIFF_DI_N DOUBLE, SUM_DI_N DOUBLE, DX DOUBLE, ADX_N DOUBLE, SPLIT_MULTIPLIER DOUBLE, SIGNAL_TYPE varchar(15), SIGNAL_PRESENT varchar(3))
//		CREATE TABLE SIGNAL_COMPONENTS_10 (SYMBOL varchar(100), SIGNAL_DATE DATE, TR DOUBLE, PLUS_DM_1 DOUBLE, MINUS_DM_1 DOUBLE, TR_N DOUBLE, PLUS_DM_N DOUBLE, MINUS_DM_N DOUBLE, DIFF_DI_N DOUBLE, SUM_DI_N DOUBLE, DX DOUBLE, ADX_N DOUBLE, SPLIT_MULTIPLIER DOUBLE, PREVIOUS_SIGNAL_TYPE varchar(15), SIGNAL_TYPE varchar(15), SIGNAL_PRESENT varchar(3))
//		CREATE TABLE MAGIC_NUMBERS (SYMBOL varchar(100), MAGIC_NUMBER int(10), PNL DOUBLE)
	}
	
	public static SignalComponentsDao getInstance(){
		if(null==signalComponentsDao){
			synchronized (SignalComponentsDao.class) {
				if(null==signalComponentsDao){
					signalComponentsDao = new SignalComponentsDao();
					logger.info("Initialised signalComponentsDao");
				}
			}
		}
		return signalComponentsDao;
	}
	
	@SuppressWarnings("unchecked")
	public List<IdValueData> getMagicNumberMap(){
		Session session = null;
		Transaction transaction = null;
		List<IdValueData> idValueDatas = new ArrayList<IdValueData>();
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			idValueDatas = session.createSQLQuery(MAGIC_NUMBER_QUERY).addEntity(IdValueData.class).list();
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
		return idValueDatas;
	}
	
	public boolean updateSignalComponents(int ma, String symbol, List<SignalComponents> signalComponentsList){
		return updateSignalComponents(true, ma, symbol, signalComponentsList);
	}
	
	public boolean updateSignalComponents(boolean isDeleteAll, int ma, String symbol, List<SignalComponents> signalComponentsList){
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			if(isDeleteAll){
				logger.info("Deleting records in " + " SIGNAL_COMPONENTS_#MA#".replace("#MA#", ""+ma) + " for " + symbol);
				int deletedCount = session.createSQLQuery(SIGNAL_COMPONENTS_DELETE_QUERY.replace("#MA#", ""+ma)).setParameter("symbol", symbol).executeUpdate();
				logger.info("Deleted " + deletedCount + " records in " + "SIGNAL_COMPONENTS_#MA#".replace("#MA#", ""+ma) + " for " + symbol);
			}else{
				logger.info("Deleting records in " + " SIGNAL_COMPONENTS_#MA#".replace("#MA#", ""+ma) + " for " + symbol);
				int deletedCount = session.createSQLQuery(SIGNAL_COMPONENTS_DELETE_DATE_QUERY.replace("#MA#", ""+ma)).setParameter("symbol", symbol).setParameter("date", signalComponentsList.get(0).getSignalDate()).executeUpdate();
				logger.info("Deleted " + deletedCount + " records in " + "SIGNAL_COMPONENTS_#MA#".replace("#MA#", ""+ma) + " for " + symbol);
			}
			int insertRecords = 0;
			for(SignalComponents signalComponents : signalComponentsList ){
				if(!isValidSignal(signalComponents)){
					continue;
				}
				int insertRecord = session.createSQLQuery(SIGNAL_COMPONENTS_INSERT_QUERY.replace("#MA#", ""+ma))
						.setParameter("symbol", signalComponents.getSymbol())
						.setParameter("signalDate", signalComponents.getSignalDate()) 
						.setParameter("TR", signalComponents.getTR()) 
						.setParameter("plusDM1", signalComponents.getPlusDM1()) 
						.setParameter("minusDM1", signalComponents.getMinusDM1()) 
						.setParameter("TRn", signalComponents.getTRn()) 
						.setParameter("plusDMn", signalComponents.getPlusDMn())
						.setParameter("minusDMn", signalComponents.getMinusDMn()) 
						.setParameter("diffDIn", signalComponents.getDiffDIn()) 
						.setParameter("sumDIn", signalComponents.getSumDIn()) 
						.setParameter("DX", signalComponents.getDX()) 
						.setParameter("ADXn", signalComponents.getADXn()) 
						.setParameter("splitMultiplier", signalComponents.getSplitMultiplier())
						.setParameter("previousSignalType", signalComponents.getPreviousSignalType()) 
						.setParameter("signalType", signalComponents.getSignalType()) 
						.setParameter("signalPresent", signalComponents.getSignalPresent())
				 		.executeUpdate();
				insertRecords = insertRecords + insertRecord;
			}
			logger.info("Inserted " + insertRecords + " successfully in " + "SIGNAL_COMPONENTS_#MA#".replace("#MA#", ""+ma) + " for " + symbol);
			session.flush();
			transaction.commit();
		}catch(Exception e){
			transaction.rollback();
			if(null!=session && session.isOpen()){
				session.close();
			}
			logger.info("Insertion failed for SIGNAL_COMPONENTS_#MA#".replace("#MA#", ""+ma) + e.getMessage() + " for " + symbol);
			return false;
		}finally{
			if(null!=session && session.isOpen()){
				session.close();
			}
		}
		return true;
	}
	
	public boolean updateMagicNumbers(int ma, String symbol, Integer magicNumber, Double PNL){
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			logger.info("Deleting records in MAGIC_NUMBERS" + " for " + symbol+ "_" + ma);
			int deletedCount = session.createSQLQuery(DELETE_MAGIC_NUMBERS).setParameter("symbol", symbol+ "_" + ma).executeUpdate();
			logger.info("Deleted " + deletedCount + " records in MAGIC_NUMBERS" + " for " + symbol+ "_" + ma);
			Integer insertCount = session.createSQLQuery(INSERT_MAGIC_NUMBERS)
					.setParameter("symbol", symbol + "_" + ma)
					.setParameter("magicNumber", magicNumber) 
					.setParameter("pnl", PNL) 
			 		.executeUpdate();
			session.flush();
			transaction.commit();
			logger.info("Insertion " + insertCount + " records in MAGIC_NUMBERS" + " for " + symbol+ "_" + ma);
		}catch(Exception e){
			transaction.rollback();
			if(null!=session && session.isOpen()){
				session.close();
			}
			logger.info("Insertion failed for MAGIC_NUMBERS" + e.getMessage() + " for " + symbol+ "_" + ma);
			return false;
		}finally{
			if(null!=session && session.isOpen()){
				session.close();
			}
		}
		return true;
	}
	
	private static boolean isValidSignal(SignalComponents signalComponents){
		return !(Double.isNaN(signalComponents.getADXn()) || 
				Double.isNaN(signalComponents.getDiffDIn()) || 
				Double.isNaN(signalComponents.getDX()) || 
				Double.isNaN(signalComponents.getMinusDIn()) || 
				Double.isNaN(signalComponents.getMinusDM1()) || 
				Double.isNaN(signalComponents.getMinusDMn()) || 
				Double.isNaN(signalComponents.getPlusDIn()) || 
				Double.isNaN(signalComponents.getPlusDM1()) || 
				Double.isNaN(signalComponents.getPlusDMn()) || 
				Double.isNaN(signalComponents.getSplitMultiplier()) || 
				Double.isNaN(signalComponents.getSumDIn()) ||
				Double.isNaN(signalComponents.getTR()) ||
				Double.isNaN(signalComponents.getTRn()) || 
				Double.isInfinite(signalComponents.getADXn()) || 
				Double.isInfinite(signalComponents.getDiffDIn()) || 
				Double.isInfinite(signalComponents.getDX()) || 
				Double.isInfinite(signalComponents.getMinusDIn()) || 
				Double.isInfinite(signalComponents.getMinusDM1()) || 
				Double.isInfinite(signalComponents.getMinusDMn()) || 
				Double.isInfinite(signalComponents.getPlusDIn()) || 
				Double.isInfinite(signalComponents.getPlusDM1()) || 
				Double.isInfinite(signalComponents.getPlusDMn()) || 
				Double.isInfinite(signalComponents.getSplitMultiplier()) || 
				Double.isInfinite(signalComponents.getSumDIn()) ||
				Double.isInfinite(signalComponents.getTR()) ||
				Double.isInfinite(signalComponents.getTRn())
				);
		
	}
	
	@SuppressWarnings("unchecked")
	public List<SignalComponents> getSignalComponent(String symbol, int ma){
		logger.info("getSignalComponent for symbol " + symbol + " and ma " + ma);
		Session session = null;
		Transaction transaction = null;
		List<SignalComponents> signalComponentsList = new ArrayList<SignalComponents>();
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			signalComponentsList = session.createSQLQuery(SIGNAL_COMPONENTS_QUERY.replace("#MA#", ""+ma)).addEntity(SignalComponents.class).setParameter("symbol", symbol).list();
			session.flush();
			transaction.commit();
		} catch (Exception e) {
			logger.error("Error in getSignalComponent " + e.getMessage());
			transaction.rollback();
			if(null!=session && session.isOpen()){
				session.close();
			}
		} finally {
			if(null!=session && session.isOpen()){
				session.close();
			}
		}
		return signalComponentsList;
	}	
	
	@SuppressWarnings("unchecked")
	public Map<String,SignalComponents> getSignalComponent(Date date, int ma){
		logger.info("getSignalComponent for date " + date + " and ma " + ma);
		Calendar fromCal = Calendar.getInstance();
		fromCal.setTime(date);
		fromCal.set(Calendar.HOUR, 0);
		fromCal.set(Calendar.MINUTE, 0);
		fromCal.set(Calendar.SECOND, 0);
		fromCal.set(Calendar.MILLISECOND, 0);
		fromCal.set(Calendar.AM_PM, Calendar.AM);
		fromCal.add(Calendar.DATE, -7);
		Map<String, SignalComponents> signalComponents = new HashMap<String, SignalComponents>();
		Session session = null;
		Transaction transaction = null;
		List<SignalComponents> signalComponentsList = new ArrayList<SignalComponents>();
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			signalComponentsList = session.createSQLQuery(SIGNAL_COMPONENTS_QUERY_N.replace("#MA#", ""+ma)).addEntity(SignalComponents.class).setParameter("date", fromCal.getTime()).list();
			session.flush();
			transaction.commit();
		} catch (Exception e) {
			logger.error("Error in getSignalComponent " + e.getMessage());
			transaction.rollback();
			if(null!=session && session.isOpen()){
				session.close();
			}
		} finally {
			if(null!=session && session.isOpen()){
				session.close();
			}
		}
		for(SignalComponents signalComponent :signalComponentsList){
			String symbol = signalComponent.getSymbol();
			if(!signalComponents.containsKey(symbol)){
				signalComponents.put(symbol, signalComponent);
			}
		}
		return signalComponents;
	}	
	
}
