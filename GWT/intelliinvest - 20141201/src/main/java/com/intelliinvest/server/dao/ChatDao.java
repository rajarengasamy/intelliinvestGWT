package com.intelliinvest.server.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.intelliinvest.client.data.ChatData;
import com.intelliinvest.server.util.HibernateUtil;

public class ChatDao {

	private static Logger logger = Logger.getLogger(ChatDao.class);
	
	private static ChatDao chatDao;
	
	private static String CHAT_DATA_INSERT_QUERY = "INSERT into CHAT_DATA (MESSAGE_ID, USERNAME, CHAT_TIME, CHAT_MESSAGE) values (:messageId, :username, :chatTime, :chatMessage)";
	private static String CHAT_DATA_QUERY = "SELECT MESSAGE_ID as messageId, USERNAME as username, CHAT_TIME as chatTime, CHAT_MESSAGE as chatMessage FROM CHAT_DATA where CHAT_TIME > :startTime ORDER BY MESSAGE_ID";
	private static String CHAT_DATA_DIFF_QUERY = "SELECT MESSAGE_ID as messageId, USERNAME as username, CHAT_TIME as chatTime, CHAT_MESSAGE as chatMessage FROM CHAT_DATA where MESSAGE_ID > :messageId ORDER BY MESSAGE_ID";
	
	private ChatDao() {
//		 CREATE Table CHAT_DATA( MESSAGE_ID INT(10), USERNAME varchar(25), CHAT_TIME DATETIME, CHAT_MESSAGE varchar(500))
	}
	
	public static ChatDao getInstance(){
		if(null==chatDao){
			synchronized (ChatDao.class) {
				if(null==chatDao){
					chatDao = new ChatDao();
					logger.info("Initialised ChatDao");
				}
			}
		}
		return chatDao;
	}
	
	@SuppressWarnings("unchecked")
	public List<ChatData> getChatData(){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -5);
		Session session = null;
		Transaction transaction = null;
		List<ChatData> chatDatas = new ArrayList<ChatData>();
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			chatDatas = session.createSQLQuery(CHAT_DATA_QUERY).addEntity(ChatData.class)
										.setParameter("startTime", cal.getTime()).list();
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
		return chatDatas;
	}
	
	@SuppressWarnings("unchecked")
	public List<ChatData> getChatData(Integer messageId, String userId){
		Session session = null;
		Transaction transaction = null;
		List<ChatData> chatDatas = new ArrayList<ChatData>();
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			chatDatas = session.createSQLQuery(CHAT_DATA_DIFF_QUERY).addEntity(ChatData.class)
										.setParameter("messageId", messageId).list();
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
		return chatDatas;
	}
	
	public List<ChatData> addChatData(String userId, List<ChatData> newChatMessages){
		ChatData chatData = null;
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			if(null!=userId && newChatMessages.size()==1 ){
				ChatData newChatMessage = newChatMessages.get(0);
				chatData = new ChatData(KeyGeneratorDao.getInstance().generateKey("chatMessageId", session),
						newChatMessage.getUsername(), new Date(), newChatMessage.getChatMessage());
				session.createSQLQuery(CHAT_DATA_INSERT_QUERY)
						.setParameter("messageId" , chatData.getMessageId())
						.setParameter("username" , chatData.getUsername())
						.setParameter("chatTime" , chatData.getChatTime())
						.setParameter("chatMessage" , chatData.getChatMessage())
						.executeUpdate();
				logger.info("Inserted chat data for " + userId + " successfully");
			}
			session.flush();
			transaction.commit();
		}catch(Exception e){
			transaction.rollback();
			if(null!=session && session.isOpen()){
				session.close();
			}
			logger.info("Insertion failed " + e.getMessage());
		}finally{
			if(null!=session && session.isOpen()){
				session.close();
			}
		}
		return java.util.Collections.singletonList(chatData);
	}

}
