package com.intelliinvest.server.dao;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.intelliinvest.client.data.PaymentData;
import com.intelliinvest.server.util.HibernateUtil;

public class PaymentDao {

	private static Logger logger = Logger.getLogger(PaymentDao.class);
	
	private static PaymentDao paymentDao;
	
	private static String PAYMENT_DATA_QUERY = "SELECT NO_OF_STOCKS as noOfStocks, NO_OF_MONTHS as noOfMonths, AMOUNT as amount, LINK as link FROM PAYMENT_DETAILS ORDER BY AMOUNT";
	
	private PaymentDao() {
//		 CREATE Table PAYMENT_DETAILS(NO_OF_STOCKS int(10), NO_OF_MONTHS varchar(10), AMOUNT DOUBLE, LINK varchar(200))
	}
	
	public static PaymentDao getInstance(){
		if(null==paymentDao){
			synchronized (PaymentDao.class) {
				if(null==paymentDao){
					paymentDao = new PaymentDao();
					logger.info("Initialised PaymentDao");
				}
			}
		}
		return paymentDao;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, PaymentData> getAllPaymentDetails(){
		Session session = null;
		Transaction transaction = null;
		List<PaymentData> paymentDatas = new ArrayList<PaymentData>();
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			paymentDatas = session.createSQLQuery(PAYMENT_DATA_QUERY).addEntity(PaymentData.class).list();
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
		Map<String, PaymentData> paymentDataMap = new LinkedHashMap<String, PaymentData>();
		for(PaymentData paymentData : paymentDatas){
			paymentDataMap.put("" + paymentData.getNoOfStocks() + ":" + paymentData.getNoOfMonths() , paymentData);
		}
		return paymentDataMap;
	}
	
}
