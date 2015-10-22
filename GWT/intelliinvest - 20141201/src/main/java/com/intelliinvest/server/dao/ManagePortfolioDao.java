package com.intelliinvest.server.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.intelliinvest.client.data.ManagePortfolioData;
import com.intelliinvest.client.data.StockDetailStaticHolder;
import com.intelliinvest.server.util.HibernateUtil;

public class ManagePortfolioDao {

	private static Logger logger = Logger.getLogger(ManagePortfolioDao.class);
	
	private static ManagePortfolioDao managePortfolioDao;
	private static String MANAGE_PORTFOLIO_RETRIEVE_QUERY = "SELECT ID as id, CODE as code, TRADE_DATE as date, DIRECTION as direction, QUANTITY as quantity, PRICE as price from  MANAGE_PORTFOLIO_DETAILS where USER_ID=:userId ORDER BY CODE, DATE";
	private static String MANAGE_PORTFOLIO_CODE_RETRIEVE_QUERY = "SELECT ID as id, CODE as code, TRADE_DATE as date, DIRECTION as direction, QUANTITY as quantity, PRICE as price from  MANAGE_PORTFOLIO_DETAILS where USER_ID=:userId and CODE=:code ORDER BY CODE, DATE";
	private static String MANAGE_PORTFOLIO_INSERT_QUERY = "INSERT INTO MANAGE_PORTFOLIO_DETAILS (ID, USER_ID, CODE, TRADE_DATE, DIRECTION, QUANTITY, PRICE) values (:id, :userId, :code, :date, :direction, :quantity, :price)";
	private static String MANAGE_PORTFOLIO_UPDATE_QUERY = "UPDATE MANAGE_PORTFOLIO_DETAILS set TRADE_DATE=:date, DIRECTION=:direction, QUANTITY=:quantity, PRICE=:price where ID=:id";

	private ManagePortfolioDao() {
//		 CREATE Table MANAGE_PORTFOLIO_DETAILS( ID varchar(50), USER_ID varchar(25), CODE varchar(10), TRADE_DATE DATE, DIRECTION  varchar(10), QUANTITY DOUBLE, PRICE DOUBLE)
	}
	
	public static ManagePortfolioDao getInstance(){
		if(null==managePortfolioDao){
			synchronized (ManagePortfolioDao.class) {
				if(null==managePortfolioDao){
					managePortfolioDao = new ManagePortfolioDao();
					logger.info("Initialised ManagePortfolioDao");
				}
			}
		}
		return managePortfolioDao;
	}
	
	@SuppressWarnings("unchecked")
	public List<ManagePortfolioData> getManagePortfolioData(String userId){
		List<ManagePortfolioData> managePortfolioDatas = new ArrayList<ManagePortfolioData>();
		if(null!=userId){
			Session session = null;
			Transaction transaction = null;
			try {
				session = HibernateUtil.getSession();
				transaction = session.beginTransaction();
				managePortfolioDatas = session.createSQLQuery(MANAGE_PORTFOLIO_RETRIEVE_QUERY).addEntity(ManagePortfolioData.class).setParameter("userId" , userId).list();
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
			populatePnl(managePortfolioDatas);
		}
		logger.info(managePortfolioDatas.size() + " records sent for user " + userId + " for manage portfolio");
		return managePortfolioDatas;
	}
	
	@SuppressWarnings("unchecked")
	public List<ManagePortfolioData> getManagePortfolioDataForCode(String userId, String code){
		Session session = null;
		Transaction transaction = null;
		List<ManagePortfolioData> managePortfolioDatas = new ArrayList<ManagePortfolioData>();
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			managePortfolioDatas = session.createSQLQuery(MANAGE_PORTFOLIO_CODE_RETRIEVE_QUERY).addEntity(ManagePortfolioData.class)
				.setParameter("userId" , userId)
				.setParameter("code", code).list();
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
		return managePortfolioDatas;
	}
	
	public List<ManagePortfolioData> getManagePortfolioData(String userId, List<ManagePortfolioData> addManagePortfolioData){
		List<ManagePortfolioData> managePortfolioDatas = new ArrayList<ManagePortfolioData>();
		if(null!=userId){
			for(ManagePortfolioData managePortfolioData : addManagePortfolioData){
				managePortfolioDatas.addAll(getManagePortfolioDataForCode(userId, managePortfolioData.getCode()));
			}
			populatePnl(managePortfolioDatas);
		}
		logger.info(managePortfolioDatas.size() + " records sent for user " + userId + " for manage portfolio in add.");
		return managePortfolioDatas;
	}
	
	public void populatePnl(List<ManagePortfolioData> managePortfolioDatas){
		HashMap<String, List<ManagePortfolioData>> rawSummaryData = new HashMap<String, List<ManagePortfolioData>>();
		for(ManagePortfolioData managePortfolioData : managePortfolioDatas){
			 String code = managePortfolioData.getCode();
			 if(!rawSummaryData.containsKey(code)){
				 rawSummaryData.put(code, new ArrayList<ManagePortfolioData>());
			 }
			 rawSummaryData.get(code).add(managePortfolioData);
		 }
		for(String code : rawSummaryData.keySet()){
			 List<ManagePortfolioData> managePortfolioDataL = rawSummaryData.get(code);
			 Collections.sort(managePortfolioDataL, new Comparator<ManagePortfolioData>() {
				 @Override
				public int compare(ManagePortfolioData managePortfolioData1, ManagePortfolioData managePortfolioData2) {
					return managePortfolioData1.getDate().compareTo(managePortfolioData2.getDate());
				}
			});
			 
			List<ManagePortfolioData> buyList = new ArrayList<ManagePortfolioData>();
			List<ManagePortfolioData> sellList = new ArrayList<ManagePortfolioData>();
			for(ManagePortfolioData managePortfolioDataTmp : managePortfolioDataL){
				ManagePortfolioData managePortfolioData = managePortfolioDataTmp.clone();
				if(managePortfolioData.getDirection().equalsIgnoreCase("Sell")){
					sellList.add(managePortfolioData);
				}else{
					buyList.add(managePortfolioData);
				}
			}
			
			int buyIndex = 0;
			int sellIndex = 0;
			while(buyIndex<buyList.size() && sellIndex<sellList.size()){
				ManagePortfolioData buyManagePortfolioData = buyList.get(buyIndex);
				ManagePortfolioData sellManagePortfolioData = sellList.get(sellIndex);
				Integer buyQuantity = buyManagePortfolioData.getQuantity();
				Integer sellQuantity = sellManagePortfolioData.getQuantity();
				Double buyPrice = buyManagePortfolioData.getPrice();
				Double sellPrice = sellManagePortfolioData.getPrice();
				 
				if(sellQuantity==buyQuantity){
					Double pnl = (buyQuantity*(sellPrice-buyPrice));
					if(null==sellManagePortfolioData.getRealisedPnl()){
						sellManagePortfolioData.setRealisedPnl(pnl);
					}else{
						sellManagePortfolioData.setRealisedPnl(sellManagePortfolioData.getRealisedPnl()+pnl);
					}
					buyManagePortfolioData.setQuantity(buyQuantity-sellQuantity);
					sellManagePortfolioData.setQuantity(buyQuantity-sellQuantity);
					buyIndex++;
					sellIndex++;
				}else if(sellQuantity>buyQuantity){
					Double pnl = (buyQuantity*(sellPrice-buyPrice));
					if(null==sellManagePortfolioData.getRealisedPnl()){
						sellManagePortfolioData.setRealisedPnl(pnl);
					}else{
						sellManagePortfolioData.setRealisedPnl(sellManagePortfolioData.getRealisedPnl()+pnl);
					}
					sellManagePortfolioData.setQuantity(sellQuantity-buyQuantity);
					buyManagePortfolioData.setQuantity(0);
					buyIndex++;
				}else{
					Double pnl = (sellQuantity*(sellPrice-buyPrice));
					if(null==sellManagePortfolioData.getRealisedPnl()){
						sellManagePortfolioData.setRealisedPnl(pnl);
					}else{
						sellManagePortfolioData.setRealisedPnl(sellManagePortfolioData.getRealisedPnl()+pnl);
					}
					sellManagePortfolioData.setQuantity(0);
					buyManagePortfolioData.setQuantity(buyQuantity-sellQuantity);
					sellIndex++;
				}
			}
			
			for(ManagePortfolioData managePortfolioData : managePortfolioDataL){
				if(buyList.contains(managePortfolioData)){
					managePortfolioData.setRealisedPnl(buyList.get(buyList.indexOf(managePortfolioData)).getRealisedPnl());
					Integer remainingQuantity = buyList.get(buyList.indexOf(managePortfolioData)).getQuantity();
					managePortfolioData.setRemainingQuantity(remainingQuantity);
				}else if(sellList.contains(managePortfolioData)){
					managePortfolioData.setRealisedPnl(sellList.get(sellList.indexOf(managePortfolioData)).getRealisedPnl());
					Integer remainingQuantity = sellList.get(sellList.indexOf(managePortfolioData)).getQuantity();
					managePortfolioData.setRemainingQuantity(remainingQuantity);
				}
				Double currentPrice = StockDetailStaticHolder.getCurrentPrice(managePortfolioData.getCode());
				Double eodPrice = StockDetailStaticHolder.getEODPrice(managePortfolioData.getCode());
				Double cp = StockDetailStaticHolder.getCP(managePortfolioData.getCode());
				managePortfolioData.setCp(StockDetailStaticHolder.getCP(managePortfolioData.getCode()));
				managePortfolioData.setCurrentPrice(currentPrice);
				managePortfolioData.setAmount(managePortfolioData.getRemainingQuantity() * currentPrice);
				managePortfolioData.setTotalAmount(managePortfolioData.getQuantity() * managePortfolioData.getPrice());
				managePortfolioData.setUnrealisedPnl(managePortfolioData.getRemainingQuantity() * (currentPrice - managePortfolioData.getPrice()));
				managePortfolioData.setTodaysPnl((managePortfolioData.getRemainingQuantity() * eodPrice * cp)/100);
			}
		 }
	}
	
	public HashMap<String, ManagePortfolioData> getManagePortfolioDetailsData(List<ManagePortfolioData> managePortfolioDatas){
		HashMap<String, ManagePortfolioData> returnData = new HashMap<String, ManagePortfolioData>();
		for(ManagePortfolioData managePortfolioData : managePortfolioDatas){
			returnData.put(managePortfolioData.getId(), managePortfolioData);
		}
		return returnData;
	}

	public HashMap<String, ManagePortfolioData> getManagePortfolioSummaryData(List<ManagePortfolioData> managePortfolioDatas){
		HashMap<String, ManagePortfolioData> summaryDatas = new HashMap<String, ManagePortfolioData>();
		HashMap<String, List<ManagePortfolioData>> rawSummaryData = new HashMap<String, List<ManagePortfolioData>>();
		for(ManagePortfolioData managePortfolioData : managePortfolioDatas){
			 String code = managePortfolioData.getCode();
			 if(!rawSummaryData.containsKey(code)){
				 rawSummaryData.put(code, new ArrayList<ManagePortfolioData>());
			 }
			 rawSummaryData.get(code).add(managePortfolioData);
		 }
		 for(String code  : rawSummaryData.keySet()){
			 ManagePortfolioData summaryData = new ManagePortfolioData();
			 summaryData.setId(code);
			 summaryData.setCode(code);
			 int buyQuantity = 0;
			 int sellQuantity = 0;
			 int totalBuyQuantity = 0;
			 Double buyAmount = 0D;
			 Double sellAmount = 0D;
			 Double realisedPnl = 0D;
			 List<ManagePortfolioData> managePortfolioDataL = rawSummaryData.get(code);
			 for(ManagePortfolioData managePortfolioData : managePortfolioDataL){
				 if(managePortfolioData.getDirection().equalsIgnoreCase("Buy")){
					 buyQuantity = buyQuantity + managePortfolioData.getRemainingQuantity();
					 buyAmount = buyAmount + (managePortfolioData.getRemainingQuantity() * managePortfolioData.getPrice());
					 totalBuyQuantity = totalBuyQuantity + managePortfolioData.getQuantity();
				 }else{
					 sellQuantity = sellQuantity + managePortfolioData.getRemainingQuantity();
					 sellAmount = sellAmount + (managePortfolioData.getRemainingQuantity() * managePortfolioData.getPrice());
					 realisedPnl = realisedPnl + managePortfolioData.getRealisedPnl();
				 }
			 }
			 if((buyQuantity - sellQuantity) == 0){
				 summaryData.setRemainingQuantity(0);
				 summaryData.setQuantity(totalBuyQuantity);
				 summaryData.setPrice(0D);
				 summaryData.setDirection("Flat");
			 }else{
				 summaryData.setRemainingQuantity(buyQuantity - sellQuantity);
				 summaryData.setQuantity(totalBuyQuantity);
				 summaryData.setPrice((buyAmount-sellAmount)/(buyQuantity - sellQuantity));
				 summaryData.setDirection(buyQuantity>sellQuantity?"Long":"Short");
			 }
			 summaryData.setRealisedPnl(realisedPnl);
			 Double currentPrice = StockDetailStaticHolder.getCurrentPrice(summaryData.getCode());
			Double eodPrice = StockDetailStaticHolder.getEODPrice(summaryData.getCode());
			Double cp = StockDetailStaticHolder.getCP(summaryData.getCode());
			summaryData.setCp(StockDetailStaticHolder.getCP(summaryData.getCode()));
			summaryData.setCurrentPrice(currentPrice);
			summaryData.setAmount(summaryData.getRemainingQuantity() * currentPrice);
			summaryData.setTotalAmount(summaryData.getQuantity() * summaryData.getPrice());
			summaryData.setUnrealisedPnl(summaryData.getRemainingQuantity() * (currentPrice - summaryData.getPrice()));
			summaryData.setTodaysPnl((summaryData.getRemainingQuantity() * eodPrice * cp)/100);
			summaryDatas.put(code, summaryData);
		 }
		 return summaryDatas;
	}
	
	public List<ManagePortfolioData> addManagePortfolioData(String userId, List<ManagePortfolioData> managePortfolioDatas){
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			int insertRecords = 0;
			for(ManagePortfolioData managePortfolioData : managePortfolioDatas ){
				managePortfolioData.setId(""+KeyGeneratorDao.getInstance().generateKey("managePortfolio", session) );
				logger.info("Inserting " + managePortfolioData);
				if(null!=userId){
					int insertRecord = session.createSQLQuery(MANAGE_PORTFOLIO_INSERT_QUERY)
																.setParameter("id" , managePortfolioData.getId())
					 											.setParameter("userId" , userId)
					 											.setParameter("code" , managePortfolioData.getCode())
					 											.setParameter("date" , managePortfolioData.getDate())
					 											.setParameter("direction" , managePortfolioData.getDirection())
					 											.setParameter("quantity" , managePortfolioData.getQuantity())
					 											.setParameter("price" , managePortfolioData.getPrice())
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
			return new ArrayList<ManagePortfolioData>();
		}finally{
			if(null!=session && session.isOpen()){
				session.close();
			}
		}
		return managePortfolioDatas;
	}
	
	public List<ManagePortfolioData> updateManagePortfolioData(String userId, List<ManagePortfolioData> managePortfolioDatas){
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			int updatedRecords = 0;
			for(ManagePortfolioData managePortfolioData : managePortfolioDatas ){
				if(null!=userId){
					logger.info("Update " + managePortfolioData);
					int updatedRecord = session.createSQLQuery(MANAGE_PORTFOLIO_UPDATE_QUERY)
																	.setParameter("date" , managePortfolioData.getDate())
						 											.setParameter("direction" , managePortfolioData.getDirection())
						 											.setParameter("quantity" , managePortfolioData.getQuantity())
						 											.setParameter("price" , managePortfolioData.getPrice())
						 											.setParameter("id" , managePortfolioData.getId())
						 											.executeUpdate();
					updatedRecords = updatedRecords + updatedRecord;
				}
			}
			logger.info("Updated " + updatedRecords + " successfully");
			session.flush();
			transaction.commit();
		}catch(Exception e){
			transaction.rollback();
			if(null!=session && session.isOpen()){
				session.close();
			}
			logger.info("Updation failed " + e.getMessage());
			return new ArrayList<ManagePortfolioData>();
		}finally{
			if(null!=session && session.isOpen()){
				session.close();
			}
		}
		return managePortfolioDatas;
	}
	
}
