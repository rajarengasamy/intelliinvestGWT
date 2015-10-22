package com.intelliinvest.server.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.intelliinvest.client.data.SimulationData;
import com.intelliinvest.server.util.HibernateUtil;

public class SimulationDao {

	private static Logger logger = Logger.getLogger(SimulationDao.class);
	
	private static SimulationDao simulationDao;
	
	private static String SIMULATION_DATA_RETRIEVE_ALL_QUERY = "SELECT CODE as code, QUARTERLY as quarterly, HALF_YEARLY as halfYearly, NINE_MONTHS as nineMonths, Yearly as yearly from INTELLI_INVEST_DATA ORDER BY CODE";
	private static String SIMULATION_DATA_RETRIEVE_QUERY = "SELECT a.CODE as code, QUARTERLY as quarterly, HALF_YEARLY as halfYearly, NINE_MONTHS as nineMonths, Yearly as yearly from INTELLI_INVEST_DATA a, USER_SIMULATION_DETAILS b where USER_ID=:userId and a.CODE=b.CODE ORDER BY CODE";
	private static String SIMULATION_DATA_INSERT_QUERY = "INSERT INTO USER_SIMULATION_DETAILS (USER_ID, CODE) values (:userId, :code)";
	private static String SIMULATION_DATA_DELETE_QUERY = "DELETE from USER_SIMULATION_DETAILS where CODE=:code and USER_ID=:userId";
	
	private SimulationDao() {
//		 CREATE Table USER_SIMULATION_DETAILS( USER_ID varchar(25), CODE varchar(10))
	}
	
	public static SimulationDao getInstance(){
		if(null==simulationDao){
			synchronized (SimulationDao.class) {
				if(null==simulationDao){
					simulationDao = new SimulationDao();
					logger.info("Initialised SimulationDao");
				}
			}
		}
		return simulationDao;
	}
	
	@SuppressWarnings("unchecked")
	public List<SimulationData> getSimulationData(String userId){
		if(null!=userId){
			Session session = null;
			Transaction transaction = null;
			List<SimulationData> simulationDatas = null;
			try {
				session = HibernateUtil.getSession();
				transaction = session.beginTransaction();
				simulationDatas = session.createSQLQuery(SIMULATION_DATA_RETRIEVE_QUERY).addEntity(SimulationData.class).setParameter("userId" , userId).list();
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
			return simulationDatas;
		}else{
			return new ArrayList<SimulationData>();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<SimulationData> getAllSimulationData(){
		Session session = null;
		Transaction transaction = null;
		List<SimulationData> simulationDatas = new ArrayList<SimulationData>();
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			simulationDatas = session.createSQLQuery(SIMULATION_DATA_RETRIEVE_ALL_QUERY).addEntity(SimulationData.class).list();
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
		return simulationDatas;
	}
	
	public SimulationData getSimulationDataForCode(String code){
//		return StockDetailStaticHolder.getSimulationData(code);
		return null;
	}
	
	public List<SimulationData> addSimulationData(String userId, List<SimulationData> simulationDatas){
		if(null!=userId){
			Session session = null;
			Transaction transaction = null;
			try{
				session = HibernateUtil.getSession();
				transaction = session.beginTransaction();
				int insertRecords = 0;
				for(SimulationData simulationData : simulationDatas ){
					if(null!=userId){
						int insertRecord = session.createSQLQuery(SIMULATION_DATA_INSERT_QUERY)
						 											.setParameter("userId" , userId)
						 											.setParameter("code" , simulationData.getCode())
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
				return new ArrayList<SimulationData>();
			}finally{
				if(null!=session && session.isOpen()){
					session.close();
				}
			}
		}
		
		if(null==userId){
			return simulationDatas;
		}
		List<SimulationData> simulationDataResult = new ArrayList<SimulationData>();
		for(SimulationData simulationData : simulationDatas ){
			simulationDataResult.add(getSimulationDataForCode(simulationData.getCode()));
		}
		return simulationDataResult;
	}
	
	public List<SimulationData> removeSimulationData(String userId, List<SimulationData> simulationDatas){
		if(null!=userId){
			Session session = null;
			Transaction transaction = null;
			try{
				session = HibernateUtil.getSession();
				transaction = session.beginTransaction();
				int deletedRecords = 0;
				for(SimulationData simulationData : simulationDatas ){
					if(null!=userId){
						logger.info("remove " + simulationData);
						int deletedRecord = session.createSQLQuery(SIMULATION_DATA_DELETE_QUERY)
							 											.setParameter("code" , simulationData.getCode())
							 											.setParameter("userId" , userId)
							 											.executeUpdate();
						deletedRecords = deletedRecords + deletedRecord;
					}
				}
				logger.info("Deleted " + deletedRecords + " successfully");
				session.flush();
				transaction.commit();
			}catch(Exception e){
				transaction.rollback();
				if(null!=session && session.isOpen()){
					session.close();
				}
				logger.info("Updation failed " + e.getMessage());
				return new ArrayList<SimulationData>();
			}finally{
				if(null!=session && session.isOpen()){
					session.close();
				}
			}
		}
		return simulationDatas;
	}
	
}
