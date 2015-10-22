package com.intelliinvest.server.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.intelliinvest.client.data.OurOptionSuggestionData;
import com.intelliinvest.client.data.OurSuggestionData;
import com.intelliinvest.client.data.StockDetailStaticHolder;
import com.intelliinvest.client.data.TradingAccountData;
import com.intelliinvest.server.util.HibernateUtil;

public class OurSuggestionDao {

	private static Logger logger = Logger.getLogger(OurSuggestionDao.class);
	
	private static OurSuggestionDao ourSuggestionDao;
	
	private static String OUR_SUGGESTION_DELETE_QUERY = "DELETE from SUGGESTIONS_DATA";
	private static String OUR_SUGGESTION_INSERT_QUERY = "INSERT into SUGGESTIONS_DATA (CODE, SUGGESTION_TYPE, SIGNAL_TYPE, SIGNAL_PRICE) values (:code, :suggestionType, :signalType, :signalPrice)";
	private static String OUR_SUGGESTION_DATA_QUERY = "SELECT CODE as code, SUGGESTION_TYPE as suggestionType FROM SUGGESTIONS_DATA";
//			"SELECT CODE as code, SUGGESTION_TYPE as suggestionType, SIGNAL_TYPE as signalType, SIGNAL_PRICE as signalPrice FROM SUGGESTIONS_DATA";
	
	private static String OUR_OPTION_SUGGESTION_DELETE_QUERY = "DELETE from OPTION_SUGGESTIONS_DATA";
	private static String OUR_OPTION_SUGGESTION_INSERT_QUERY = "INSERT into OPTION_SUGGESTIONS_DATA (CODE, INSTRUMENT, EXPIRY_DATE, STRIKE_PRICE, OPTION_TYPE, OPTION_PRICE)"
																+ " values (:code, :instrument, :expiryDate, :strikePrice, :optionType, optionPrice)";
	private static String OUR_OPTION_SUGGESTION_DATA_QUERY = "SELECT CODE as code, INSTRUMENT as instrument, EXPIRY_DATE as expiryDate, STRIKE_PRICE as strikePrice,"
															+ "OPTION_TYPE as optionType, OPTION_PRICE as optionPrice FROM OPTION_SUGGESTIONS_DATA";
	
	private OurSuggestionDao() {
//		 CREATE Table SUGGESTIONS_DATA( CODE varchar(100), SUGGESTION_TYPE varchar(10), SIGNAL_PRICE DOUBLE, SIGNAL_TYPE varchar(15))
//		 CREATE Table OPTION_SUGGESTIONS_DATA( CODE varchar(100), INSTRUMENT varchar(10), EXPIRY_DATE DATE, STRIKE_PRICE DOUBLE, OPTION_TYPE varchar(10), OPTION_PRICE DOUBLE )
	}
	
	public static OurSuggestionDao getInstance(){
		if(null==ourSuggestionDao){
			synchronized (OurSuggestionDao.class) {
				if(null==ourSuggestionDao){
					ourSuggestionDao = new OurSuggestionDao();
					logger.info("Initialised OurSuggestionDao");
				}
			}
		}
		return ourSuggestionDao;
	}
	
	@SuppressWarnings("unchecked")
	public List<OurSuggestionData> getAllSuggestionsData(){
		Session session = null;
		Transaction transaction = null;
		List<OurSuggestionData> ourSuggestionDatas = new ArrayList<OurSuggestionData>();
		try{
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			ourSuggestionDatas = session.createSQLQuery(OUR_SUGGESTION_DATA_QUERY).addEntity(OurSuggestionData.class).list();
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
		for(OurSuggestionData ourSuggestionData : ourSuggestionDatas){
			if(StockDetailStaticHolder.tradingAccountMap.containsKey(ourSuggestionData.getCode())){
				TradingAccountData tradingAccountData = StockDetailStaticHolder.tradingAccountMap.get(ourSuggestionData.getCode());
				ourSuggestionData.setSignalPrice(tradingAccountData.getSignalPrice());
				ourSuggestionData.setSignalType(tradingAccountData.getSignalType());
				ourSuggestionData.setSignalDate(tradingAccountData.getSignalDate());
			}
		}
		return ourSuggestionDatas;
	}
	
	@SuppressWarnings("unchecked")
	public List<OurOptionSuggestionData> getAllOptionSuggestionsData(){
		Session session = null;
		Transaction transaction = null;
		List<OurOptionSuggestionData> optionSuggestionDatas = new ArrayList<OurOptionSuggestionData>();
		try{
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			optionSuggestionDatas = session.createSQLQuery(OUR_OPTION_SUGGESTION_DATA_QUERY).addEntity(OurOptionSuggestionData.class).list();
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
		return optionSuggestionDatas;
		
	}
	
	public List<OurSuggestionData> getOurSuggestionsData(String user, String suggestionType){
		 ArrayList<OurSuggestionData> suggestionDatas = new ArrayList<OurSuggestionData>();
		 for(OurSuggestionData ourSuggestionData : StockDetailStaticHolder.suggestionsMap.values()){
			 if(ourSuggestionData.getSuggestionType().equals(suggestionType)){
				 if(null!=user){ 
					 suggestionDatas.add(ourSuggestionData);
				 }else{
					 if(suggestionDatas.size()<2){
						 suggestionDatas.add(ourSuggestionData);
					 }
				 }
			 }
		 }
		 return suggestionDatas;
	}
	
//	public List<OurOptionSuggestionData> getOurOptionSuggestionsData(String user){
//		 ArrayList<OurOptionSuggestionData> suggestionDatas = new ArrayList<OurOptionSuggestionData>();
//		 for(OurOptionSuggestionData ourSuggestionData : StockDetailStaticHolder.optionSuggestionsMap.values()){
//			 if(null!=user){ 
//				 suggestionDatas.add(ourSuggestionData);
//			 }else{
//				 if(suggestionDatas.size()<2){
//					 suggestionDatas.add(ourSuggestionData);
//				 }
//			 }
//		 }
//		 return suggestionDatas;
//	}
	
	public boolean insertSuggestions(List<String> suggestions){
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			logger.info("Deleting records in STOCK_DETAILS");
			int deletedCount = session.createSQLQuery(OUR_SUGGESTION_DELETE_QUERY).executeUpdate();
			logger.info("Deleted " + deletedCount + "records in SUGGESTIONS_DATA");
			int insertRecords = 0;
			for(String suggestion : suggestions ){
				String[] suggestionsValues = suggestion.split(",");
				int insertRecord = session.createSQLQuery(OUR_SUGGESTION_INSERT_QUERY)
				 											.setParameter("code" , suggestionsValues[0])
				 											.setParameter("suggestionType" , suggestionsValues[1])
				 											.setParameter("signalType" , suggestionsValues[2])
				 											.setParameter("signalPrice" , new Double(suggestionsValues[3]))
				 											.executeUpdate();
				insertRecords = insertRecords + insertRecord;
			}
			logger.info("Inserted " + insertRecords + " successfully in SUGGESTIONS_DATA");
			session.flush();
			transaction.commit();
		}catch(Exception e){
			transaction.rollback();
			if(null!=session && session.isOpen()){
				session.close();
			}
			logger.info("Insertion failed for SUGGESTIONS_DATA " + e.getMessage());
			return false;
		}finally{
			if(null!=session && session.isOpen()){
				session.close();
			}
		}
		return true;
	}
	
	public boolean insertOptionSuggestions(List<String> suggestions){
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			logger.info("Deleting records in STOCK_DETAILS");
			int deletedCount = session.createSQLQuery(OUR_OPTION_SUGGESTION_DELETE_QUERY).executeUpdate();
			logger.info("Deleted " + deletedCount + "records in OPTION_SUGGESTIONS_DATA");
			int insertRecords = 0;
			for(String suggestion : suggestions ){
				String[] suggestionsValues = suggestion.split(",");
				int insertRecord = session.createSQLQuery(OUR_OPTION_SUGGESTION_INSERT_QUERY)
				 											.setParameter("code" , suggestionsValues[0])
				 											.setParameter("instrument" , suggestionsValues[1])
				 											.setParameter("expiryDate" , new SimpleDateFormat("dd-MMM-yyyy").parse(suggestionsValues[2]))
				 											.setParameter("strikePrice" , new Double(suggestionsValues[3]))
				 											.setParameter("optionType" , suggestionsValues[4])
				 											.setParameter("optionPrice" , new Double(suggestionsValues[5]))
				 											.executeUpdate();
				insertRecords = insertRecords + insertRecord;
			}
			logger.info("Inserted " + insertRecords + " successfully in OPTION_SUGGESTIONS_DATA");
			session.flush();
			transaction.commit();
		}catch(Exception e){
			transaction.rollback();
			if(null!=session && session.isOpen()){
				session.close();
			}
			logger.info("Insertion failed for OPTION_SUGGESTIONS_DATA " + e.getMessage());
			return false;
		}finally{
			if(null!=session && session.isOpen()){
				session.close();
			}
		}
		return true;
	}
	
}
