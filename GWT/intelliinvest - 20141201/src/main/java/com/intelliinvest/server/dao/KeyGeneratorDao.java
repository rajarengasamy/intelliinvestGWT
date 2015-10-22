package com.intelliinvest.server.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;

public class KeyGeneratorDao {

	private static Logger logger = Logger.getLogger(KeyGeneratorDao.class);
	
	private static KeyGeneratorDao keyGeneratorDao;
	
	private static String ID_GENERATOR = "SELECT value from KEY_STORE where type=:type";
	private static String ID_INCREMENTOR = "UPDATE KEY_STORE set value=value+1  where type=:type";
	
	
	private KeyGeneratorDao() {
//		 CREATE Table KEY_STORE(type varchar(100), value int(10))
//	insert into KEY_STORE values ('userId', 1000);
//	insert into KEY_STORE values ('managePortfolio', 1000);
//	insert into KEY_STORE values ('chatMessageId', 1000);
	}
	
	public static KeyGeneratorDao getInstance(){
		if(null==keyGeneratorDao){
			synchronized (KeyGeneratorDao.class) {
				if(null==keyGeneratorDao){
					keyGeneratorDao = new KeyGeneratorDao();
					logger.info("Initialised KeyGeneratorDao");
				}
			}
		}
		return keyGeneratorDao;
	}
	
	
	@SuppressWarnings("unchecked")
	public synchronized Integer generateKey(String type, Session session){
		try{
			logger.info("Incrementing user id");
			List<Integer> newKey = session.createSQLQuery(ID_GENERATOR).setParameter("type", type).list();
			session.createSQLQuery(ID_INCREMENTOR).setParameter("type", type).executeUpdate();
			if(newKey.size()>0){
				return newKey.get(0);
			}
		}catch(Exception e){
			logger.info("Insertion failed for Key Generator" + e.getMessage());
			return null;
		}
		return null;
	}

}
