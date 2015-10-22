package com.intelliinvest.server.util;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class HibernateUtil {
	private static Logger logger = Logger.getLogger(HibernateUtil.class);
	
	private static SessionFactory sessionFactory;
    private static ServiceRegistry serviceRegistry;

	static {
		try {
			logger.info("Initialising hibernate sessionFactory ");
			Configuration configuration = new Configuration();
		    configuration.configure();
		    serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();        
		    sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		    logger.info("Hibernate sessionFactory Created");
		}catch (Throwable ex) {
			logger.error("Initial SessionFactory creation failed." , ex);
			throw new ExceptionInInitializerError(ex);
		}
	}
	
	public static Session getSession() {
		Session session = sessionFactory.openSession();
//		if(!session.isOpen()){
//			session = sessionFactory.openSession();
//		}
		return session;
	}	
}