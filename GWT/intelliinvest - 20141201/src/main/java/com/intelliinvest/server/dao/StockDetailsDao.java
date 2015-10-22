package com.intelliinvest.server.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.intelliinvest.client.data.IdValueData;
import com.intelliinvest.client.data.StockDetailData;
import com.intelliinvest.client.data.StockDetailStaticHolder;
import com.intelliinvest.client.data.StockPriceData;
import com.intelliinvest.server.util.HibernateUtil;


public class StockDetailsDao {

	private static Logger logger = Logger.getLogger(StockDetailsDao.class);
	
	private static StockDetailsDao stockDetailsDao;
	
	private static String STOCK_DETAILS_QUERY = "SELECT CODE as code, NAME as name from STOCK_DETAILS ORDER BY CODE"; 
//	private static String INTELLI_STOCK_DETAILS_QUERY = "SELECT a.CODE as code, NAME as name from INTELLI_INVEST_DATA a, STOCK_DETAILS b where a.CODE=b.CODE ORDER BY CODE"; 
	
	private static String STOCK_DETAILS_DELETE_QUERY = "DELETE from STOCK_DETAILS"; 
	private static String STOCK_DETAILS_INSERT_QUERY = "INSERT into STOCK_DETAILS (CODE, NAME) values(:code, :name)";
	
	private static String STOCK_CURRENT_PRICE_QUERY = "SELECT CODE as code, CURRENT_PRICE as price, CP as cp from  CURRENT_STOCK_PRICE ORDER BY CODE";
	private static String STOCK_CURRENT_PRICE_UPDATE_QUERY = "UPDATE CURRENT_STOCK_PRICE set CURRENT_PRICE=:currentPrice, CP=:cp where CODE=:code";
	private static String STOCK_CURRENT_PRICE_INSERT_QUERY = "INSERT into CURRENT_STOCK_PRICE (CODE, CURRENT_PRICE, CP) values(:code, :currentPrice, :cp)";
	
	public static String STOCK_EOD_PRICE_INSERT_QUERY = "INSERT into EOD_STOCK_PRICE (CODE, EOD_DATE, EOD_PRICE) values(:code, :eodDate, :eodPrice)";
	public static String STOCK_EOD_PRICE_DELETE_QUERY = "DELETE from EOD_STOCK_PRICE where EOD_DATE=:eodDate";	
	private static String STOCK_EOD_PRICE_QUERY = "SELECT CODE as code, EOD_PRICE as price, 0 as cp from  EOD_STOCK_PRICE where EOD_DATE=(SELECT MAX(EOD_DATE) from EOD_STOCK_PRICE) ORDER BY CODE";
	
	private static String WORLD_STOCK_PRICE_QUERY = "SELECT CODE as code, PRICE as price, CP as cp from  WORLD_STOCK_PRICE ORDER BY CODE";
	private static String WORLD_STOCK_DETAIL_QUERY = "SELECT CODE as id, NAME as value from  WORLD_STOCK_DETAILS";
	private static String WORLD_STOCK_PRICE_UPDATE_QUERY = "UPDATE WORLD_STOCK_PRICE set PRICE=:price, CP=:cp where CODE=:code";
	private static String WORLD_STOCK_PRICE_INSERT_QUERY = "INSERT into WORLD_STOCK_PRICE (CODE, PRICE, CP) values(:code, :price, :cp)";
	
	private static String NSE_TO_BSE_QUERY = "SELECT NSE_CODE as id, BSE_CODE as value from  NSE_TO_BSE_MAP";
	
//	private static String BOM_STOCKS_QUERY = "SELECT CODE from  BOM_STOCKS";
	
	private static String NIFTY_STOCKS_QUERY = "SELECT CODE from NIFTY_STOCKS";
	
	private StockDetailsDao() {
//		 CREATE Table STOCK_DETAILS( CODE varchar(100), NAME varchar(250))
//		 CREATE Table CURRENT_STOCK_PRICE( CODE varchar(100), CURRENT_PRICE DOUBLE, CP DOUBLE)
//		CREATE Table WORLD_STOCK_PRICE( CODE varchar(100), NAME varchar(100), PRICE DOUBLE, CP DOUBLE )
//		CREATE Table WORLD_STOCK_DETAILS(CODE varchar(100), NAME varchar(100))
		
//		CREATE Table NSE_TO_BSE_MAP( NSE_CODE varchar(100), BSE_CODE varchar(100) )
//		CREATE Table BOM_STOCKS( CODE varchar(100))
//		CREATE Table NIFTY_STOCKS( CODE varchar(100))
	}
	
	public static StockDetailsDao getInstance(){
		if(null==stockDetailsDao){
			synchronized (StockDetailsDao.class) {
				if(null==stockDetailsDao){
					stockDetailsDao = new StockDetailsDao();
					logger.info("Initialised StockDetailsDao");
				}
			}
		}
		return stockDetailsDao;
	}

//	@SuppressWarnings("unchecked")
//	public List<StockDetailData> getIntelliStockDetails(){
//		 return HibernateUtil.getSession().createSQLQuery(INTELLI_STOCK_DETAILS_QUERY).addEntity(StockDetailData.class).list();
//	}
//	
//	@SuppressWarnings("unchecked")
//	public List<String> getBOMStocks(){
//		 return HibernateUtil.getSession().createSQLQuery(BOM_STOCKS_QUERY).list();
//	}
	
	@SuppressWarnings("unchecked")
	public HashSet<String> getNiftyStocks(){
		Session session = null;
		Transaction transaction = null;
		HashSet<String> niftyStocks = new HashSet<String>();
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			niftyStocks = new HashSet<String>(session.createSQLQuery(NIFTY_STOCKS_QUERY).list());
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
		return niftyStocks;
	}
	
	@SuppressWarnings("unchecked")
	public List<StockDetailData> getStockDetails(){
		Session session = null;
		Transaction transaction = null;
		List<StockDetailData> stockDetailDatas = new ArrayList<StockDetailData>();
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			stockDetailDatas = session.createSQLQuery(STOCK_DETAILS_QUERY).addEntity(StockDetailData.class).list();
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
		return stockDetailDatas;
	}
	
	public List<StockDetailData> updateStockDetails(Map<String, String> stockDetailsMap){
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			int deletedRecords = session.createSQLQuery(STOCK_DETAILS_DELETE_QUERY).executeUpdate();
			logger.info("Deleted " + deletedRecords + " stock details successfully");
			
			int insertedRecords = 0;
			logger.info("Updating current prices for all stocks ");
			for(Entry<String, String> entry : stockDetailsMap.entrySet()){
				int insertedRecord = session.createSQLQuery(STOCK_DETAILS_INSERT_QUERY)
 											.setParameter("code" , entry.getKey())
 											.setParameter("name" , entry.getValue())
 											.executeUpdate();
				insertedRecords = insertedRecords + insertedRecord;
			}
			logger.info("Inserted " + insertedRecords + " stock details successfully");
			session.flush();
			transaction.commit();
		}catch(Exception e){
			transaction.rollback();
			if(null!=session && session.isOpen()){
				session.close();
			}
			logger.info("Error updating stock details for all stocks " + e.getMessage());
			return null;
		}finally{
			if(null!=session && session.isOpen()){
				session.close();
			}
		}
		
		return getStockDetails();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<StockPriceData> getCurrentStockPrices(){
		Session session = null;
		Transaction transaction = null;
		List<StockPriceData> stockPriceDatas = new ArrayList<StockPriceData>();
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			stockPriceDatas = session.createSQLQuery(STOCK_CURRENT_PRICE_QUERY).addEntity(StockPriceData.class).list();
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
		return stockPriceDatas;
	}
	
	public List<StockPriceData> updateCurrentStockPrices(List<StockPriceData> currentPrices){
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			int updatedRecords = 0;
			int insertedRecords = 0;
			logger.info("Updating current prices for all stocks ");
			for( StockPriceData currentPriceData : currentPrices){
				if(StockDetailStaticHolder.stockCurrentPriceMap.containsKey(currentPriceData.getCode())){
					int updatedRecord = session.createSQLQuery(STOCK_CURRENT_PRICE_UPDATE_QUERY)
								.setParameter("code" , currentPriceData.getCode())
								.setParameter("currentPrice" , currentPriceData.getPrice())
								.setParameter("cp" , currentPriceData.getCp())
								.executeUpdate();
					updatedRecords = updatedRecords + updatedRecord;
				}else{
					int insertedRecord = session.createSQLQuery(STOCK_CURRENT_PRICE_INSERT_QUERY)
							.setParameter("code" , currentPriceData.getCode())
							.setParameter("currentPrice" , currentPriceData.getPrice())
							.setParameter("cp" , currentPriceData.getCp())
							.executeUpdate();
					insertedRecords = insertedRecords + insertedRecord;
				}
				
			}
			logger.info("Updated " + updatedRecords + " current prices for stocks successfully");
			logger.info("Inserted " + insertedRecords + " current prices for stocks successfully");
			session.flush();
			transaction.commit();
		}catch(Exception e){
			transaction.rollback();
			if(null!=session && session.isOpen()){
				session.close();
			}
			logger.info("Error updating current prices for all stocks " + e.getMessage());
			return null;
		}finally{
			if(null!=session && session.isOpen()){
				session.close();
			}
		}
		
		return getCurrentStockPrices();
	}
	
	@SuppressWarnings("unchecked")
	public List<StockPriceData> getEODStockPrices(){
		Session session = null;
		Transaction transaction = null;
		List<StockPriceData> stockPriceDatas = new ArrayList<StockPriceData>();
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			stockPriceDatas = session.createSQLQuery(STOCK_EOD_PRICE_QUERY).addEntity(StockPriceData.class).list();
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
		return stockPriceDatas;
	}
	
	public List<StockDetailData> insertStockDetails(List<String> stockDetails){
		Map<String, String> stockDetailsMap = new HashMap<String, String>();
		for(String stockDetail : stockDetails){
			String[] stockDetailValues = stockDetail.split(",");
			stockDetailsMap.put(stockDetailValues[0], stockDetailValues[1]);
		}
		return updateStockDetails(stockDetailsMap);
	}
	
	@SuppressWarnings("unchecked")
	public List<StockPriceData> getWorldStockPrices(){
		Session session = null;
		Transaction transaction = null;
		List<StockPriceData> stockPriceDatas = new ArrayList<StockPriceData>();
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			stockPriceDatas = session.createSQLQuery(WORLD_STOCK_PRICE_QUERY).addEntity(StockPriceData.class).list();
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
		return stockPriceDatas;
	}
	
	@SuppressWarnings("unchecked")
	public List<IdValueData> getWorldStockDetails(){
		Session session = null;
		Transaction transaction = null;
		List<IdValueData> idValueDatas = new ArrayList<IdValueData>();
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			idValueDatas = session.createSQLQuery(WORLD_STOCK_DETAIL_QUERY).addEntity(IdValueData.class).list();
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
	
	public List<StockPriceData> updateWorldStockPrices(List<StockPriceData> worldPrices){
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			int updatedRecords = 0;
			int insertedRecords = 0;
			logger.info("Updating world prices for all stocks ");
			for( StockPriceData worldPriceData : worldPrices){
				if(StockDetailStaticHolder.worldStockPriceMap.containsKey(worldPriceData.getCode())){
					int updatedRecord = session.createSQLQuery(WORLD_STOCK_PRICE_UPDATE_QUERY)
								.setParameter("code" , worldPriceData.getCode())
								.setParameter("price" , worldPriceData.getPrice())
								.setParameter("cp" , worldPriceData.getCp())
								.executeUpdate();
					updatedRecords = updatedRecords + updatedRecord;
				}else{
					int insertedRecord = session.createSQLQuery(WORLD_STOCK_PRICE_INSERT_QUERY)
							.setParameter("code" , worldPriceData.getCode())
							.setParameter("price" , worldPriceData.getPrice())
							.setParameter("cp" , worldPriceData.getCp())
							.executeUpdate();
					insertedRecords = insertedRecords + insertedRecord;
				}
				
			}
			logger.info("Updated " + updatedRecords + " world prices for stocks successfully");
			logger.info("Inserted " + insertedRecords + " world prices for stocks successfully");
			session.flush();
			transaction.commit();
		}catch(Exception e){
			transaction.rollback();
			if(null!=session && session.isOpen()){
				session.close();
			}
			logger.info("Error updating world prices for all stocks " + e.getMessage());
			return null;
		}finally{
			if(null!=session && session.isOpen()){
				session.close();
			}
		}
		return getWorldStockPrices();
	}
	
	@SuppressWarnings("unchecked")
	public List<IdValueData> getNSEToBSEMap(){
		Session session = null;
		Transaction transaction = null;
		List<IdValueData> idValueDatas = new ArrayList<IdValueData>();
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			idValueDatas = session.createSQLQuery(NSE_TO_BSE_QUERY).addEntity(IdValueData.class).list();
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
}
