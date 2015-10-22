package com.intelliinvest.client.data;

import java.util.Date;



@SuppressWarnings("serial")
public class UserDetailData implements IntelliInvestData{
	String userId;
	String username;
	String mail;
	String phone;
	String password;
	String plan;
	String userType;
	String active;
	String activationCode;
	String error;
	Date creationDate;
	Date renewalDate;
	Date expiryDate;
	Date lastLoginDate;
	Boolean sendNotification;
	public UserDetailData() {
	}
	
	public UserDetailData(String userId, String username, String mail, String phone,
			String password, String plan, String userType, String active,
			String activationCode, Date creationDate, Date renewalDate, Date expiryDate,
			Date lastLoginDate, Boolean sendNotification) {
		super();
		this.userId = userId;
		this.username = username;
		this.mail = mail;
		this.phone = phone;
		this.password = password;
		this.plan = plan;
		this.userType = userType;
		this.active = active;
		this.activationCode = activationCode;
		this.creationDate = creationDate;
		this.renewalDate = renewalDate;
		this.expiryDate = expiryDate;
		this.lastLoginDate = lastLoginDate;
		this.sendNotification = sendNotification;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public void setError(String error) {
		this.error = error;
	}
	
	public String getError() {
		return error;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPlan() {
		return plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getActivationCode() {
		return activationCode;
	}

	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getRenewalDate() {
		return renewalDate;
	}

	public void setRenewalDate(Date renewalDate) {
		this.renewalDate = renewalDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	
	public Date getLastLoginDate() {
		return lastLoginDate;
	}
	
	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}
	
	public Boolean getSendNotification() {
		return sendNotification;
	}
	
	public void setSendNotification(Boolean sendNotification) {
		this.sendNotification = sendNotification;
	}

	@Override
	public String toString() {		
		try{
			return 
			"{" 
			+ "\"userId\":\"" + userId + "\","
			+ "\"username\":\"" + username + "\","
			+ "\"mail\":\"" + mail + "\","
			+ "\"phone\":\"" + phone + "\","
			+ "\"password\":\"" + password + "\","
			+ "\"plan\":\"" + plan + "\","
			+ "\"userType\":\"" + userType + "\","
			+ "\"active\":\"" + active + "\","
			+ "\"creationDate\":\"" + creationDate + "\","
			+ "\"renewalDate\":\"" + renewalDate + "\","
			+ "\"expiryDate\":\"" + expiryDate + "\","
			+ "\"lastLoginDate\":\"" + lastLoginDate + "\","
			+ "\"sendNotification\":\"" + sendNotification + "\","
			+ "\"activationCode\":\"" + activationCode + "\""
			+ "}";			
		}catch (Exception e) {
			return null;
		}
		finally{			
		}
	}
	@Override
	public boolean equals(Object obj) {
		UserDetailData userDetailData = (UserDetailData) obj;
		if(userDetailData.mail.equals(this.mail)){
			return true;
		}else{
			return false;
		}
	}
	
	public UserDetailData clone() {
		UserDetailData userDetailData = new UserDetailData(userId, username, mail, phone, password, plan, userType, active, activationCode, creationDate, 
				renewalDate, expiryDate, lastLoginDate, sendNotification);
		return userDetailData;
	}
	
}
