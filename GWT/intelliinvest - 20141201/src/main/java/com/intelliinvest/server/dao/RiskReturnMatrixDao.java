package com.intelliinvest.server.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.intelliinvest.client.data.RiskReturnMatrixData;
import com.intelliinvest.client.data.RiskReturnMatrixSummaryData;
import com.intelliinvest.server.util.HibernateUtil;

public class RiskReturnMatrixDao {

	private static Logger logger = Logger.getLogger(RiskReturnMatrixDao.class);
	
	private static RiskReturnMatrixDao riskReturnMatrixDao;
	
	private static String RISK_RETURN_QUERY ="select a.CODE as code, SIGNAL_TYPE as signalType, TYPE_3M as type3m, TYPE_6M as type6m, TYPE_9M as type9m, TYPE_12M as type12m from RISK_RETURN_MATRIX a, "
			+ "(select CODE, SIGNAL_TYPE, SIGNAL_DATE from STOCK_SIGNAL_DETAILS s1 where "
				+ "SIGNAL_DATE=(select MAX(SIGNAL_DATE) from STOCK_SIGNAL_DETAILS s2 where s1.CODE=s2.CODE)"
				+ ") b where a.CODE=b.CODE";
	
	private static String RISK_RETURN_MATRIX_INSERT_QUERY = "INSERT INTO RISK_RETURN_MATRIX (CODE, TYPE_3M, TYPE_6M, TYPE_9M, TYPE_12M) values (:code, :type3m, :type6m, :type9m, :type12m)";
	
	private static String RISK_RETURN_MATRIX_DELETE_QUERY = "DELETE FROM RISK_RETURN_MATRIX";

	
	private RiskReturnMatrixDao() {
		//		CREATE TABLE RISK_RETURN_MATRIX( CODE varchar(100), TYPE_3M varchar(8), TYPE_6M varchar(8), TYPE_9M varchar(8), TYPE_12M varchar(8));
		}
	
	public static RiskReturnMatrixDao getInstance(){
		if(null==riskReturnMatrixDao){
			synchronized (RiskReturnMatrixDao.class) {
				if(null==riskReturnMatrixDao){
					riskReturnMatrixDao = new RiskReturnMatrixDao();
					logger.info("Initialised riskReturnMatrixDao");
				}
			}
		}
		return riskReturnMatrixDao;
	}
	
	public boolean updateRiskReturnMatrixDetails(List<String> riskReturnMatrix){
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			logger.info("Deleting records in RISK_RETURN_MATRIX");
			int deletedCount = session.createSQLQuery(RISK_RETURN_MATRIX_DELETE_QUERY).executeUpdate();
			logger.info("Deleted " + deletedCount + "records in RISK_RETURN_MATRIX");
			int insertRecords = 0;
			for(String riskReturn : riskReturnMatrix ){
				String[] riskReturnValues = riskReturn.split(",");
				int insertRecord = session.createSQLQuery(RISK_RETURN_MATRIX_INSERT_QUERY)
							.setParameter("code" , riskReturnValues[0])
							.setParameter("type3m" , riskReturnValues[1])
							.setParameter("type6m" , riskReturnValues[2])
							.setParameter("type9m" , riskReturnValues[3])
							.setParameter("type12m" , riskReturnValues[4])
				 			.executeUpdate();
				insertRecords = insertRecords + insertRecord;
			}
			logger.info("Inserted " + insertRecords + " successfully in RISK_RETURN_MATRIX");
			session.flush();
			transaction.commit();
		}catch(Exception e){
			transaction.rollback();
			if(null!=session && session.isOpen()){
				session.close();
			}
			logger.info("Insertion failed for RISK_RETURN_MATRIX" + e.getMessage());
			return false;
		}finally{
			if(null!=session && session.isOpen()){
				session.close();
			}
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public List<RiskReturnMatrixData> getAllRsikReturnMatrixData(){
		Session session = null;
		Transaction transaction = null;
		List<RiskReturnMatrixData> riskReturnMatrixDatas = new ArrayList<RiskReturnMatrixData>();
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			riskReturnMatrixDatas = session.createSQLQuery(RISK_RETURN_QUERY).addEntity(RiskReturnMatrixData.class).list();
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
		return riskReturnMatrixDatas;
	}
	
	public Map<String, RiskReturnMatrixSummaryData> getRsikReturnMatrixSummaryData(){
		
		List<RiskReturnMatrixData> riskReturnMatrixDatas = getAllRsikReturnMatrixData();
		Map<String, RiskReturnMatrixSummaryData> riskReturnMatrixSummaryDatas = new HashMap<String, RiskReturnMatrixSummaryData>();
		for(RiskReturnMatrixData riskReturnMatrixData : riskReturnMatrixDatas){
			String key3m = "3M" + riskReturnMatrixData.getType3m();
			String key6m = "6M" + riskReturnMatrixData.getType6m();
			String key9m = "9M" + riskReturnMatrixData.getType9m();
			String key12m = "12M" + riskReturnMatrixData.getType12m();
			if(!riskReturnMatrixSummaryDatas.containsKey(key3m)){
				riskReturnMatrixSummaryDatas.put(key3m, new RiskReturnMatrixSummaryData(key3m));
			}
			if(!riskReturnMatrixSummaryDatas.containsKey(key6m)){
				riskReturnMatrixSummaryDatas.put(key6m, new RiskReturnMatrixSummaryData(key3m));
			}
			if(!riskReturnMatrixSummaryDatas.containsKey(key9m)){
				riskReturnMatrixSummaryDatas.put(key9m, new RiskReturnMatrixSummaryData(key3m));
			}
			if(!riskReturnMatrixSummaryDatas.containsKey(key12m)){
				riskReturnMatrixSummaryDatas.put(key12m, new RiskReturnMatrixSummaryData(key3m));
			}
			
			RiskReturnMatrixSummaryData summaryData = riskReturnMatrixSummaryDatas.get(key3m);
			summaryData.getCodes().add(riskReturnMatrixData.getCode());
			if(riskReturnMatrixData.getSignalType().toUpperCase().contains("WAIT") || riskReturnMatrixData.getSignalType().toUpperCase().contains("SELL")){
				summaryData.setSellCount(summaryData.getSellCount()+1);
			}else{
				summaryData.setBuyCount(summaryData.getBuyCount()+1);
			}
			
			summaryData = riskReturnMatrixSummaryDatas.get(key6m);
			summaryData.getCodes().add(riskReturnMatrixData.getCode());
			if(riskReturnMatrixData.getSignalType().toUpperCase().contains("WAIT") || riskReturnMatrixData.getSignalType().toUpperCase().contains("SELL")){
				summaryData.setSellCount(summaryData.getSellCount()+1);
			}else{
				summaryData.setBuyCount(summaryData.getBuyCount()+1);
			}
			
			summaryData = riskReturnMatrixSummaryDatas.get(key9m);
			summaryData.getCodes().add(riskReturnMatrixData.getCode());
			if(riskReturnMatrixData.getSignalType().toUpperCase().contains("WAIT") || riskReturnMatrixData.getSignalType().toUpperCase().contains("SELL")){
				summaryData.setSellCount(summaryData.getSellCount()+1);
			}else{
				summaryData.setBuyCount(summaryData.getBuyCount()+1);
			}
			
			summaryData = riskReturnMatrixSummaryDatas.get(key12m);
			summaryData.getCodes().add(riskReturnMatrixData.getCode());
			if(riskReturnMatrixData.getSignalType().toUpperCase().contains("WAIT") || riskReturnMatrixData.getSignalType().toUpperCase().contains("SELL")){
				summaryData.setSellCount(summaryData.getSellCount()+1);
			}else{
				summaryData.setBuyCount(summaryData.getBuyCount()+1);
			}
			
		}
		return riskReturnMatrixSummaryDatas;
	}
	
	
}
