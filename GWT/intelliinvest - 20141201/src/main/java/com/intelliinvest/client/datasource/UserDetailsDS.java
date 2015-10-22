package com.intelliinvest.client.datasource;

import com.intelliinvest.client.config.Constants;
import com.intelliinvest.client.data.IntelliInvestData;
import com.intelliinvest.client.data.UserDetailData;
import com.intelliinvest.client.service.IntelliInvestServiceFactory;
import com.intelliinvest.client.util.DateUtil;
import com.smartgwt.client.data.fields.DataSourceBooleanField;
import com.smartgwt.client.data.fields.DataSourceDateField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class UserDetailsDS extends IntelliInvestDS {
	
	public UserDetailsDS() {
		super(IntelliInvestServiceFactory.intelliInvestDSService, Constants.USER_DETAIL);
		DataSourceTextField userIdDSField = new DataSourceTextField("userId", "User Id", 50);  
        DataSourceTextField userNameDSField = new DataSourceTextField("username", "User Name", 50);  
        DataSourceTextField passwordDSField = new DataSourceTextField("password", "Password", 50);  
        DataSourceTextField roleDSField = new DataSourceTextField("userType", "User Type", 50);
        DataSourceTextField mailIdDSField = new DataSourceTextField("mail", "E-Mail",150);
        mailIdDSField.setPrimaryKey(true);
        DataSourceTextField phoneDSField = new DataSourceTextField("phone", "Phone", 50); 
        DataSourceTextField PlanTypeDSField = new DataSourceTextField("plan", "Plan",70); 
        DataSourceTextField activeDSField = new DataSourceTextField("active", "Active",70); 
        DataSourceDateField creationDateField = new DataSourceDateField("creationDate", "Creation Date");
        DataSourceDateField renewalDateField = new DataSourceDateField("renewalDate", "Renewal Date");
        DataSourceDateField expiryDateField = new DataSourceDateField("expiryDate", "Expiry Date");
        DataSourceDateField lastLoginDateField = new DataSourceDateField("lastLoginDate", "Last Login Date");
        DataSourceBooleanField sendNotificationField = new DataSourceBooleanField("sendNotification", "Send Notification");
        setFields(userIdDSField, userNameDSField, passwordDSField, roleDSField, mailIdDSField, phoneDSField, PlanTypeDSField, activeDSField
        		, creationDateField, renewalDateField, expiryDateField, lastLoginDateField, sendNotificationField);
	}
	
	@Override
	public IntelliInvestData copyValues(ListGridRecord record) {
		UserDetailData userDetailData = new UserDetailData();
		userDetailData.setUserId(record.getAttribute("userId"));
		userDetailData.setUsername(record.getAttribute("username"));
		userDetailData.setPassword(record.getAttribute("password"));
		userDetailData.setUserType(record.getAttribute("userType"));
		userDetailData.setMail(record.getAttribute("mail"));
		userDetailData.setPhone(record.getAttribute("phone"));
		userDetailData.setPlan(record.getAttribute("plan"));
		userDetailData.setActive(record.getAttribute("active"));
		userDetailData.setCreationDate(DateUtil.getDate(record.getAttribute("creationDate")));
		userDetailData.setRenewalDate(DateUtil.getDate(record.getAttribute("renewalDate")));
		userDetailData.setExpiryDate(DateUtil.getDate(record.getAttribute("expiryDate")));
		userDetailData.setLastLoginDate(DateUtil.getDate(record.getAttribute("lastLoginDate")));
		userDetailData.setSendNotification(Boolean.valueOf((record.getAttribute("sendNotification"))));
		return userDetailData;
	}
	@Override
	public ListGridRecord copyValues(IntelliInvestData data) {
		UserDetailData userDetailData = (UserDetailData)data;
		ListGridRecord record = new ListGridRecord();
		record.setAttribute("userId", userDetailData.getUserId());
		record.setAttribute("username", userDetailData.getUsername());
		record.setAttribute("password", userDetailData.getPassword());
		record.setAttribute("userType", userDetailData.getUserType());
		record.setAttribute("mail", userDetailData.getMail());
		record.setAttribute("phone", userDetailData.getPhone());
		record.setAttribute("plan", userDetailData.getPlan());
		record.setAttribute("active", userDetailData.getActive());
		record.setAttribute("creationDate", userDetailData.getCreationDate());
		record.setAttribute("renewalDate", userDetailData.getRenewalDate());
		record.setAttribute("expiryDate", userDetailData.getExpiryDate());
		record.setAttribute("lastLoginDate", userDetailData.getLastLoginDate());
		record.setAttribute("sendNotification", userDetailData.getSendNotification());
		return record;
	}
}
