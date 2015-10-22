package com.intelliinvest.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.intelliinvest.client.data.UserDetailData;
import com.intelliinvest.client.service.IntelliInvestServiceFactory;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.types.DateDisplayFormat;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.BooleanItem;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.IntegerItem;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.form.validator.IsIntegerValidator;
import com.smartgwt.client.widgets.form.validator.MatchesFieldValidator;

public class MyProfileWindow {

	static final Window myProfileWindow = new Window(); 
	static final DynamicForm myProfileForm = new DynamicForm();  
	public static Window getMyProfileWindow(){
		return myProfileWindow;
	}
	
	public static Window initialize(){
		myProfileWindow.setWidth(340);  
        myProfileWindow.setHeight(370);  
        myProfileWindow.setTitle("My Profile");  
        myProfileWindow.setShowMinimizeButton(false);  
        myProfileWindow.setIsModal(true);  
        myProfileWindow.setPadding(5);
        myProfileWindow.centerInPage();
        myProfileWindow.setShowCloseButton(true);
        
		myProfileForm.setNumCols(2);
        myProfileForm.setPadding(5);
        
        final StaticTextItem username = new StaticTextItem("username", "Username");  
        username.setRequired(true); 
        username.setValue("");
        
        final StaticTextItem emailTextItem = new StaticTextItem("email", "E-mail");  
        emailTextItem.setRequired(true); 
        emailTextItem.setValue("");
        

        final StaticTextItem planTextItem = new StaticTextItem("plan", "Plan");  
        planTextItem.setRequired(true); 
        planTextItem.setValue("");
        
        final IntegerItem phoneTextItem = new IntegerItem("phone", "Phone");  
        phoneTextItem.setRequired(true);  
        phoneTextItem.setDefaultValue(""); 
        phoneTextItem.setValidators(new IsIntegerValidator());
        
        final BooleanItem sendNotificationItem = new BooleanItem("sendNotification", "Send Mail Notification");
        sendNotificationItem.setTitleOrientation(TitleOrientation.LEFT);
        sendNotificationItem.setShowTitle(false);
        sendNotificationItem.setRequired(true);  
        sendNotificationItem.setDefaultValue(true); 
        
        DateItem creationDateItem = new DateItem("creationDate", "User Since");
        creationDateItem.setRequired(true);  
        creationDateItem.setDisabled(true);
        creationDateItem.setShowPickerIcon(false);
        creationDateItem.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATE);

        DateItem renewalDateItem = new DateItem("renewalDate", "Last Renewal Date");
        renewalDateItem.setRequired(true); 
        renewalDateItem.setDisabled(true);
        renewalDateItem.setShowPickerIcon(false);
        renewalDateItem.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATE);
        
        DateItem expiryDateItem = new DateItem("expiryDate", "Expiry Date");
        expiryDateItem.setRequired(true); 
        expiryDateItem.setDisabled(true);
        expiryDateItem.setShowPickerIcon(false);
        expiryDateItem.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATE);
        
        final BooleanItem changePassword = new BooleanItem("changePasswordCheck", "Change Password");
        changePassword.setTitleOrientation(TitleOrientation.LEFT);
        changePassword.setShowTitle(false);
        
        final PasswordItem oldPassword = new PasswordItem("password", "Old Password");  
        
        MatchesFieldValidator newValidator = new MatchesFieldValidator();  
        newValidator.setOtherField("newPassword2");  
        newValidator.setErrorMessage("New passwords do not match");  
        
        final PasswordItem newPassword = new PasswordItem("newPassword", "New Password");  
        newPassword.setWrapTitle(false);
        newPassword.setValidators(newValidator);  
        
        final PasswordItem newPassword2 = new PasswordItem("newPassword2", "Re-type New Password");  
        newPassword2.setWrapTitle(false);
        
        oldPassword.setRequired(false); 
		oldPassword.setDisabled(true);
		newPassword.setRequired(false); 
		newPassword.setDisabled(true);
		newPassword2.setRequired(false);  
		newPassword2.setDisabled(true);
		
        changePassword.addChangedHandler(new  ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				if(null!=event.getValue() && Boolean.TRUE.equals(Boolean.valueOf(event.getValue().toString()))){
					oldPassword.setRequired(true); 
					oldPassword.setDisabled(false);
					newPassword.setRequired(true); 
					newPassword.setDisabled(false);
					newPassword2.setRequired(true);  
					newPassword2.setDisabled(false);
					
				}else{
					oldPassword.setRequired(false); 
					oldPassword.setDisabled(true);
					oldPassword.setValue("");
					newPassword.setRequired(false); 
					newPassword.setDisabled(true);
					newPassword.setValue("");
					newPassword2.setRequired(false);  
					newPassword2.setDisabled(true);
					newPassword2.setValue("");
				}
			}
		});
		
        final ButtonItem save = new ButtonItem("save", "Update Details"); 
        save.setColSpan(2);
        save.setStartRow(true);
        save.setEndRow(true);
        save.setAlign(Alignment.CENTER);
        save.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				 Boolean isValid = myProfileForm.validate();
				 if(isValid){
					 String password = "";
					 if(null!=myProfileForm.getValue("password") && !"".equals(myProfileForm.getValue("password").toString()) && !myProfileForm.getItem("password").isDisabled()){
						 try {
							 password = EncryptUtil.encrypt(myProfileForm.getValue("password").toString());
						} catch (Exception e) {
							SC.say("Please enter a valid password");
							return;
						} 
						if(!password.equals(IntelliInvest.userDetailData.getPassword())){
							 SC.say("Old password entered does not match existing password " + IntelliInvest.userDetailData.getPassword() + " " + password);
							 return;
						}
					 }
					 String newPassword = "";
					 if(null!=myProfileForm.getValue("newPassword") && !"".equals(myProfileForm.getValue("newPassword").toString()) && !myProfileForm.getItem("newPassword").isDisabled()){
						 try {
							 newPassword = EncryptUtil.encrypt(myProfileForm.getValue("newPassword").toString());
						} catch (Exception e) {
							SC.say("Please enter a valid new password");
							return;
						} 
					 }
					 IntelliInvestServiceFactory.loginService.save(
							 myProfileForm.getValue("username").toString(), 
							 myProfileForm.getValue("email").toString(),
							 myProfileForm.getValue("phone").toString(),
							 password,
							 newPassword,
							 Boolean.valueOf(myProfileForm.getValue("sendNotification").toString()),
							 new AsyncCallback<UserDetailData>() {
								@Override
								public void onSuccess(UserDetailData userDetailData) {
									if(null!=userDetailData){
										IntelliInvest.userDetailData = userDetailData;
										SC.say("Details updated successfully", new BooleanCallback() {
											@Override
											public void execute(Boolean value) {
												myProfileWindow.animateHide(AnimationEffect.WIPE);
											}
										});
									}else{
										SC.say("Error updating details user");
									}
								}
								
								@Override
								public void onFailure(Throwable caught) {
									SC.say(caught.getMessage());
								}
							});
				 	}else{
				 		SC.say("Please correct details before Updating details");
				 	}
					 
				}
		});  
          
        myProfileForm.setFields(username, emailTextItem, planTextItem, phoneTextItem, sendNotificationItem, creationDateItem, renewalDateItem, expiryDateItem, changePassword, oldPassword, newPassword, newPassword2, save);  
        myProfileWindow.addItem(myProfileForm);
        myProfileWindow.setVisible(false);
        
        return myProfileWindow;
	}
	
	public static void setValues(){
		myProfileForm.getField("username").setValue(IntelliInvest.userDetailData.getUsername());
		myProfileForm.getField("email").setValue(IntelliInvest.userDetailData.getMail());
		myProfileForm.getField("plan").setValue(IntelliInvest.userDetailData.getPlan());
		myProfileForm.getField("phone").setValue(IntelliInvest.userDetailData.getPhone()); 
		myProfileForm.getField("creationDate").setValue(IntelliInvest.userDetailData.getCreationDate()); 
		myProfileForm.getField("renewalDate").setValue(IntelliInvest.userDetailData.getRenewalDate()); 
		myProfileForm.getField("expiryDate").setValue(IntelliInvest.userDetailData.getExpiryDate()); 
		myProfileForm.getField("sendNotification").setValue(IntelliInvest.userDetailData.getSendNotification()); 
	}
}
