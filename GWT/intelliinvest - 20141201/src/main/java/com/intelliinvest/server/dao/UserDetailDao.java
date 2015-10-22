package com.intelliinvest.server.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.intelliinvest.client.EncryptUtil;
import com.intelliinvest.client.data.UserDetailData;
import com.intelliinvest.server.IntelliInvestStore;
import com.intelliinvest.server.util.HibernateUtil;
import com.intelliinvest.server.util.MailUtil;

public class UserDetailDao {

	private static Logger logger = Logger.getLogger(UserDetailDao.class);
	
	private static UserDetailDao userDao;
	
	private static String MAIL_ID_EXISTS_QUERY = "SELECT MAIL FROM USER_DETAILS WHERE  MAIL=:mail";
	
	private static String MAIL_ID_FROM_USER_QUERY = "SELECT MAIL FROM USER_DETAILS WHERE  USER_ID=:userId";
	
	private static String LOGIN_QUERY = "SELECT USER_ID, USERNAME, PASSWORD, MAIL,PHONE, PLAN, USERTYPE, ACTIVE, ACTIVATION_CODE"
			+ ", CREATION_DATE, RENEWAL_DATE, EXPIRY_DATE, LAST_LOGIN_DATE, SEND_NOTIFICATION "
			+ "FROM USER_DETAILS WHERE MAIL=:mail and PASSWORD=:password";
	
	private static String USER_DETAIL_QUERY = "SELECT USER_ID, USERNAME, PASSWORD, MAIL,PHONE, PLAN, USERTYPE, ACTIVE, ACTIVATION_CODE"
											+ ", CREATION_DATE, RENEWAL_DATE, EXPIRY_DATE, LAST_LOGIN_DATE, SEND_NOTIFICATION  FROM USER_DETAILS WHERE MAIL=:mail";
	
	private static String REGISTER_QUERY = "INSERT INTO USER_DETAILS (USER_ID, USERNAME, MAIL,PHONE, PASSWORD, PLAN, USERTYPE, ACTIVE, ACTIVATION_CODE, CREATION_DATE, RENEWAL_DATE, EXPIRY_DATE, LAST_LOGIN_DATE, SEND_NOTIFICATION)"
											+ " VALUES (:userId, :username, :mail, :phone, :password, :plan, :userType, :active, :activationCode,"
											+ " :creationDate, :renewalDate, :expiryDate, :lastLoginDate, :sendNotification )";
	
	private static String UPDATE_QUERY = "UPDATE USER_DETAILS set PHONE=:phone, PASSWORD=:newPassword, SEND_NOTIFICATION=:sendNotification where MAIL=:mail and PASSWORD=:password";
	
	private static String UPDATE_LAST_LOGIN_DATE_QUERY = "UPDATE USER_DETAILS set LAST_LOGIN_DATE=:lastLoginDate where MAIL=:mail";
	
	private static String UPDATE_EXCEPT_PASSWORD_QUERY = "UPDATE USER_DETAILS set PHONE=:phone, SEND_NOTIFICATION=:sendNotification where MAIL=:mail";
	
	private static String  ACTIVATION_QUERY = "UPDATE USER_DETAILS set ACTIVE='Y' where MAIL=:mail and ACTIVATION_CODE=:activationCode";
	
	private static String ADMIN_SELECT_QUERY = "SELECT USER_ID, USERNAME, PASSWORD, MAIL,PHONE, PLAN, USERTYPE, ACTIVE, ACTIVATION_CODE"
												+ ", CREATION_DATE, RENEWAL_DATE, EXPIRY_DATE, LAST_LOGIN_DATE, SEND_NOTIFICATION "	
												+ " FROM USER_DETAILS";
	
	private static String ADMIN_UPDATE_QUERY = "UPDATE USER_DETAILS set USERNAME=:username, PASSWORD=:password, PHONE=:phone, PLAN=:plan,"
																+ " USERTYPE=:userType, ACTIVE=:active, RENEWAL_DATE=:renewalDate, EXPIRY_DATE=:expiryDate"
																+ " , SEND_NOTIFICATION=:sendNotification where MAIL=:mail";
	
	private static String ADMIN_DELETE_QUERY = "DELETE FROM USER_DETAILS where MAIL=:mail";
	
	private UserDetailDao() {
//		CREATE Table USER_DETAILS (USER_ID varchar(25), USERNAME varchar(25), MAIL varchar(50), PHONE varchar(20), PASSWORD varchar(100) 
//		, PLAN varchar(20), USERTYPE varchar(10), ACTIVE varchar(2), ACTIVATION_CODE varchar(20), CREATION_DATE DATE, RENEWAL_DATE DATE, EXPIRY_DATE DATE
//		)
	}
	
	public static UserDetailDao getInstance(){
		if(null==userDao){
			synchronized (UserDetailDao.class) {
				if(null==userDao){
					userDao = new UserDetailDao();
					logger.info("Initialised UserDao");
				}
			}
		}
		return userDao;
	}
	
	@SuppressWarnings("unchecked")
	public String getMailIdFromUserId(String userId) {
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			List<Object> userDetails = session.createSQLQuery(MAIL_ID_FROM_USER_QUERY)
														.setParameter("userId" , userId)
														.list();
			session.flush();
			transaction.commit();
			if(userDetails.size()>=1){
				return userDetails.get(0).toString();
			}else{
				throw new RuntimeException("Mail id cannot be retreived from user id for " + userId);
			}
			
		}catch(Exception e){
			transaction.rollback();
			if(null!=session && session.isOpen()){
				session.close();
			}
			throw new RuntimeException("Mail id cannot be retreived from user id for " + userId);
		}finally{
			if(null!=session && session.isOpen()){
				session.close();
			}
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public Boolean mailIdExists(String mail) {
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			List<Object> userDetails = session.createSQLQuery(MAIL_ID_EXISTS_QUERY)
														.setParameter("mail" , mail)
														.list();
			session.flush();
			transaction.commit();
			if(userDetails.size()>=1){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			transaction.rollback();
			if(null!=session && session.isOpen()){
				session.close();
			}
			logger.info("Login exists for user " + mail + " failed " + e.getMessage());
			return false;
		}finally{
			if(null!=session && session.isOpen()){
				session.close();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public UserDetailData login(String mail, String password) {
		UserDetailData userDetailData = new UserDetailData();
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			List<UserDetailData> userDetails = session.createSQLQuery(LOGIN_QUERY)
														.addEntity(UserDetailData.class)
														.setParameter("mail" , mail)
														.setParameter("password" , password)
														.list();
			session.flush();
			transaction.commit();
			if(userDetails.size()==1){
				if(userDetails.get(0).getActive().equals("Y")){
					logger.info("Login for user " + userDetails.get(0).getUsername() + " successful" );
					updateLastLoginDate(mail);
					return userDetails.get(0);
				}else{
					userDetailData.setError("Please activate account before logging in. Check mail for more details.");
					return userDetailData;
				}
			}else{
				logger.info("Login for user " + mail + " failed" );
				userDetailData.setError("Login for user " + mail + " failed. Please check user name and/or password entered");
				return userDetailData;
			}
		}catch(Exception e){
			transaction.rollback();
			if(null!=session && session.isOpen()){
				session.close();
			}
			logger.info("Login for user " + mail + " failed " + e.getMessage());
			userDetailData.setError("Login for user " + mail + " failed due to system failure. It has been notified to Admin. Please try after some time.");
			return userDetailData;
		}finally{
			if(null!=session && session.isOpen()){
				session.close();
			}
		}
	}
	
	public void updateLastLoginDate(String mail){
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			session.createSQLQuery(UPDATE_LAST_LOGIN_DATE_QUERY)
						.setParameter("mail" , mail)
						.setParameter("lastLoginDate" , new Date())
						.executeUpdate();
			logger.info("Last Login Date updated for user with mail id " + mail + " successful");
			session.flush();
			transaction.commit();
		}catch(Exception e){
			transaction.rollback();
			if(null!=session && session.isOpen()){
				session.close();
			}
			logger.info("Error updating Last Login Date details for user with mail id " + mail + " successful " + e.getMessage());
		}finally{
			if(null!=session && session.isOpen()){
				session.close();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private UserDetailData getUserDetailFromMail(String mail){
		Session session = null;
		Transaction transaction = null;
		UserDetailData userDetailData = null;
		try{
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			List<UserDetailData> userDetails = session.createSQLQuery(USER_DETAIL_QUERY)
																	.addEntity(UserDetailData.class)
																	.setParameter("mail" , mail)
																	.list();
			session.flush();
			transaction.commit();
			if(userDetails.size()==1){
				userDetailData = userDetails.get(0);
			}
		}catch(Exception e){
			logger.info("Error while getting details for user from mailId " + e.getMessage());
			transaction.rollback();
			if(null!=session && session.isOpen()){
				session.close();
			}
		}finally{
			if(null!=session && session.isOpen()){
				session.close();
			}
		}
		return userDetailData;
	}
	
	public String forgotPassword(String mail) {
		try{
			UserDetailData userDetailData = getUserDetailFromMail(mail);
			if(null!=userDetailData){
				Long time = System.currentTimeMillis();
				String randomText = "INI" + time.toString().substring(time.toString().length()-5);
				saveProfile(userDetailData.getUsername(), userDetailData.getMail(), userDetailData.getPhone(), 
								userDetailData.getPassword(), EncryptUtil.encrypt(randomText), userDetailData.getSendNotification());
				logger.info("Mail sent to your maild id registered with us");
				if(MailUtil.sendMail(IntelliInvestStore.properties.getProperty("smtp.host"), IntelliInvestStore.properties.getProperty("mail.from"),
						IntelliInvestStore.properties.getProperty("mail.password"), 
						new String[]{mail}, "Password reset from IntelliInvest", "Hi,\n Your password has been reset to " + randomText)){
					logger.info("Mail sent to user with mail id " + mail + "");
					return "New Password sent to your registered maild id";
				}else{
					logger.error("Error sending to user with mail id " + mail + "");
					return "Problem sending activation mail to your account. Please contact admin for further support";
				}
			}else{
				logger.info("Mail not sent to user with mail id " + mail + " because Username does not exists");
				return "Username does not exists";
			}
		}catch(Exception e){
			logger.info("Error while sending mail for forgot password failed " + e.getMessage());
			return "Error while sending mail for forgot password failed";
		}
	}
	
	public UserDetailData register(String userName, String mail, String phone, String password, Boolean sendNotification){
		UserDetailData userDetail = new UserDetailData();
		if(mailIdExists(mail)){
			userDetail.setError("Mail Id already exists");
			return userDetail;
		}
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			Long time = System.currentTimeMillis();
			String randomText = "ACT" + time.toString().substring(time.toString().length()-5);
			String userId = "USER" + KeyGeneratorDao.getInstance().generateKey("userId", session);
			Date currentDate = getCurrentDate();
			Date expiryDate = addDaysToDate(currentDate, new Integer(IntelliInvestStore.properties.get("trail.period").toString()));
			
			session.createSQLQuery(REGISTER_QUERY)
						.setParameter("userId" , userId)
						.setParameter("username" , userName)
						.setParameter("mail" , mail)
						.setParameter("phone" , phone)
						.setParameter("password" , password)
						.setParameter("plan" , "Default_10")
						.setParameter("userType", "User")
						.setParameter("active", "N")
						.setParameter("activationCode", randomText)
						.setParameter("creationDate", currentDate)
						.setParameter("renewalDate", currentDate)
						.setParameter("expiryDate", expiryDate)
						.setParameter("lastLoginDate", new Date())
						.setParameter("sendNotification", sendNotification)
						.executeUpdate();
			logger.info("Registration for user " + userName + " with mail id " + mail + " successful");
			logger.info("Sending activation mail for user " + mail);
			MailUtil.sendMail(IntelliInvestStore.properties.getProperty("smtp.host"), IntelliInvestStore.properties.getProperty("mail.from"),
							IntelliInvestStore.properties.getProperty("mail.password"), new String[]{mail}, "Activation of IntelliInvest Account", 
							"Hi " + userName + ",<br>To activate your account please click below link<br>http://"+ IntelliInvestStore.properties.getProperty("context.url") +"/intelliinvest/login?mailId=" 
							+ mail + "&activationCode=" +randomText +"<br>Regards,<br>IntelliInvest Team.");
			session.flush();
			transaction.commit();
		}catch(Exception e){
			transaction.rollback();
			if(null!=session && session.isOpen()){
				session.close();
			}
			userDetail.setError("Registration failed for user " + userName + " with mail id " + mail + " " + e.getMessage());
			return userDetail;
		}finally{
			if(null!=session && session.isOpen()){
				session.close();
			}
		}
		userDetail.setError("Registration for user " + userName + " with mail id " + mail + " successful. \n Please activate your account by clicking link in your activation mail.");
		return userDetail;
	}
	
	private Date getCurrentDate(){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.AM_PM, Calendar.AM);
		return cal.getTime();
	}
	
	private Date addDaysToDate(Date date, int days){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);
		return cal.getTime();
	}
	
	public Boolean activateAccount(String mail, String activationCode){
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			int updatedRows = session.createSQLQuery(ACTIVATION_QUERY)
						.setParameter("mail" , mail)
						.setParameter("activationCode" , activationCode)
						.executeUpdate();
			session.flush();
			transaction.commit();
			if(updatedRows==1){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			transaction.rollback();
			if(null!=session && session.isOpen()){
				session.close();
			}
			logger.info("Error activating for user " + mail + " with activation code " + activationCode + " " + e.getMessage());
			return false;
		}finally{
			if(null!=session && session.isOpen()){
				session.close();
			}
		}
	}
	
	public UserDetailData saveProfileExceptPassword(String userName, String mail, String phone, String password, Boolean sendNotification){
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			session.createSQLQuery(UPDATE_EXCEPT_PASSWORD_QUERY)
						.setParameter("mail" , mail)
						.setParameter("phone" , phone)
						.setParameter("sendNotification" , sendNotification)
						.executeUpdate();
			logger.info("Updated details for user " + userName + " with mail id " + mail + " successful");
			session.flush();
			transaction.commit();
		}catch(Exception e){
			transaction.rollback();
			if(null!=session && session.isOpen()){
				session.close();
			}
			logger.info("Error updating details for user " + userName + " with mail id " + mail + " successful " + e.getMessage());
			return null;
		}finally{
			if(null!=session && session.isOpen()){
				session.close();
			}
		}
		return getUserDetailFromMail(mail);
	}
	
	public UserDetailData saveProfile(String userName, String mail, String phone, String oldPassword, String newPassword, Boolean sendNotification){
		logger.info("Updating details for user " + userName + " with mail id " + mail + " Phone " + phone + " oldPassword " + oldPassword + " new password " + newPassword);
		if(null!=newPassword && "".equals(newPassword.trim())){
			return saveProfileExceptPassword(userName, mail, phone, oldPassword, sendNotification);
		}
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			session.createSQLQuery(UPDATE_QUERY)
						.setParameter("mail" , mail)
						.setParameter("phone" , phone)
						.setParameter("password" , oldPassword)
						.setParameter("newPassword" , newPassword)
						.setParameter("sendNotification" , sendNotification)
						.executeUpdate();
			logger.info("Updated details for user " + userName + " with mail id " + mail + " successful");
			session.flush();
			transaction.commit();
		}catch(Exception e){
			transaction.rollback();
			if(null!=session && session.isOpen()){
				session.close();
			}
			logger.info("Error updating details for user " + userName + " with mail id " + mail + " successful " + e.getMessage());
			return null;
		}finally{
			if(null!=session && session.isOpen()){
				session.close();
			}
		}
		return getUserDetailFromMail(mail);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<UserDetailData> getUserDetails(String user){
		if(null!=user){ 
			Session session = null;
			Transaction transaction = null;
			List<UserDetailData> userDetailDatas = new ArrayList<UserDetailData>();
			try {
				session = HibernateUtil.getSession();
				transaction = session.beginTransaction();
				userDetailDatas = session.createSQLQuery(ADMIN_SELECT_QUERY).addEntity(UserDetailData.class).list();
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
			return userDetailDatas;
		}else{
			return new ArrayList<UserDetailData>();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<UserDetailData> addUserDetail(String user, List<UserDetailData> userDetailDatas){
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			for(UserDetailData userDetailData : userDetailDatas ){
				logger.info("Inserting " + userDetailData);
				if(null!=user){
					register(userDetailData.getUsername(), userDetailData.getMail(), userDetailData.getPhone(), userDetailData.getPassword(), userDetailData.getSendNotification());
					List<UserDetailData> userDetails = session.createSQLQuery(USER_DETAIL_QUERY)
							.addEntity(UserDetailData.class)
							.setParameter("mail" , userDetailData.getMail())
							.list();
					if(userDetails.size()==1){
						userDetailData = userDetails.get(0);
					}
				}
					
			}
			session.flush();
			transaction.commit();
		}catch(Exception e){
			transaction.rollback();
			if(null!=session && session.isOpen()){
				session.close();
			}
			logger.info("Insertion failed " + e.getMessage());
			return new ArrayList<UserDetailData>();
		}finally{
			if(null!=session && session.isOpen()){
				session.close();
			}
		}
		return userDetailDatas;
	}

	
	public List<UserDetailData> updateUserDetail(String user, List<UserDetailData> userDetailsData){
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			int updatedRecords = 0;
			for(UserDetailData userDetailData : userDetailsData ){
				if(null!=user){
					logger.info("Update " + userDetailData);
					int updatedRecord = session.createSQLQuery(ADMIN_UPDATE_QUERY)
						 											.setParameter("username" , userDetailData.getUsername())
						 											.setParameter("password" , userDetailData.getPassword())
						 											.setParameter("phone" , userDetailData.getPhone())
						 											.setParameter("plan" , userDetailData.getPlan())
						 											.setParameter("userType" , userDetailData.getUserType())
						 											.setParameter("active" , userDetailData.getActive())
						 											.setParameter("mail" , userDetailData.getMail())
						 											.setParameter("renewalDate" , userDetailData.getRenewalDate())
						 											.setParameter("expiryDate" , userDetailData.getExpiryDate())
						 											.setParameter("sendNotification" , userDetailData.getSendNotification())
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
			return new ArrayList<UserDetailData>();
		}finally{
			if(null!=session && session.isOpen()){
				session.close();
			}
		}
		return userDetailsData;
	}
	
	public List<UserDetailData> removeUserDetail(String user, List<UserDetailData> userDetailDatas){
		if(null!=user){
			Session session = null;
			Transaction transaction = null;
			try{
				session = HibernateUtil.getSession();
				transaction = session.beginTransaction();
				int deletedRecords = 0;
				for(UserDetailData userDetailData : userDetailDatas ){
					if(null!=user){
						logger.info("remove " + userDetailData);
						int deletedRecord = session.createSQLQuery(ADMIN_DELETE_QUERY)
							 											.setParameter("mail" , userDetailData.getMail())
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
				return new ArrayList<UserDetailData>();
			}finally{
				if(null!=session && session.isOpen()){
					session.close();
				}
			}
		}
		return userDetailDatas;
	}
	
}
