package com.intelliinvest.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.intelliinvest.client.data.UserDetailData;
import com.intelliinvest.client.service.IntelliInvestServiceFactory;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.BooleanItem;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.FloatItem;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.form.validator.IsIntegerValidator;
import com.smartgwt.client.widgets.form.validator.LengthRangeValidator;
import com.smartgwt.client.widgets.form.validator.MatchesFieldValidator;
import com.smartgwt.client.widgets.form.validator.RegExpValidator;

public class RegisterWindow {

	static final Window registerWindow = new Window(); 
	
	public static Window getRegisterWindow(){
		return registerWindow;
	}
	
	public static Window initialize(){
		registerWindow.setWidth(350);  
        registerWindow.setHeight(220);  
        registerWindow.setTitle("Register");  
        registerWindow.setShowMinimizeButton(false);  
        registerWindow.setIsModal(true); 
        registerWindow.centerInPage();
        registerWindow.setPadding(5);
//        registerWindow.setLeft("1000");
//		registerWindow.setTop("60");
		registerWindow.setShowCloseButton(true);
        
		final DynamicForm registerForm = new DynamicForm();  
		registerForm.setNumCols(2);
        registerForm.setPadding(5);
        
        final TextItem username = new TextItem("userName", "Display Name");  
        username.setRequired(true);  
        username.setDefaultValue("Username");  
        username.addFocusHandler(Util.getEmptyTextHandler("Username"));
        username.addBlurHandler(Util.getDefaultTextHandler("Username"));
        LengthRangeValidator lengthRangeValidator = new LengthRangeValidator();
        lengthRangeValidator.setMin(5);
        lengthRangeValidator.setMax(25);
        lengthRangeValidator.setErrorMessage("Minimum 5 characters to maximum 25 characters");
        final String USER_NAME_REGEX = "^[a-zA-Z0-9_ ]*$";
        RegExpValidator regExpValidatorUser = new RegExpValidator();
        regExpValidatorUser.setExpression(USER_NAME_REGEX);
        regExpValidatorUser.setErrorMessage("Display name should only be alphanumeric");
        
        username.setValidators(regExpValidatorUser, lengthRangeValidator);
        
        final TextItem emailTextItem = new TextItem("email", "Email");  
        emailTextItem.setRequired(true);  
        emailTextItem.setDefaultValue("user@domain.com");  
        emailTextItem.addFocusHandler(Util.getEmptyTextHandler("user@domain.com"));
        emailTextItem.addBlurHandler(Util.getDefaultTextHandler("user@domain.com"));
        
        final FloatItem phoneTextItem = new FloatItem("phone", "Phone");  
        phoneTextItem.setRequired(true);  
        phoneTextItem.setDefaultValue("987654321");  
        phoneTextItem.addFocusHandler(Util.getEmptyTextHandler("987654321"));
        phoneTextItem.addBlurHandler(Util.getDefaultTextHandler("987654321"));
        phoneTextItem.setValidators(new IsIntegerValidator());
        
        final BooleanItem sendNotificationItem = new BooleanItem("sendNotification", "Send Mail Notification");
        sendNotificationItem.setTitleOrientation(TitleOrientation.LEFT);
        sendNotificationItem.setShowTitle(false);
        sendNotificationItem.setRequired(true);  
        sendNotificationItem.setDefaultValue(true); 
        
        final String EMAIL_VALIDATION_REGEX = "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-+]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        RegExpValidator regExpValidator = new RegExpValidator();
        regExpValidator.setExpression(EMAIL_VALIDATION_REGEX);
        regExpValidator.setErrorMessage("PLease enter a valid email address.");
        emailTextItem.setValidators(regExpValidator);
        emailTextItem.setValidateOnExit(true);

        
        MatchesFieldValidator validator = new MatchesFieldValidator();  
        validator.setOtherField("password2");  
        validator.setErrorMessage("Passwords do not match");  
          
        final PasswordItem password = new PasswordItem("password", "password");  
        password.setRequired(true);  
        password.setValidators(validator);  
        password.setValidators(lengthRangeValidator);
        
        PasswordItem password2 = new PasswordItem("password2", "Re-type Password");  
        password2.setRequired(true);  
        password2.setWrapTitle(false);
        password2.setValidators(lengthRangeValidator);
        
        final ButtonItem createAccount = new ButtonItem("createAccount", "Create Account"); 
        createAccount.setColSpan(2);
        createAccount.setStartRow(false);
        createAccount.setEndRow(true);
        createAccount.setAlign(Alignment.CENTER);
        createAccount.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				 Boolean isValid = registerForm.validate();
				 if(isValid){
					 String password = registerForm.getValue("password").toString();
					 try {
						 password = EncryptUtil.encrypt(password);
					} catch (Exception e) {
						SC.say("Please enter a valid password");
						return;
					} 
					 IntelliInvestServiceFactory.loginService.register(registerForm.getValue("userName").toString(), 
							 registerForm.getValue("email").toString(),
							 registerForm.getValue("phone").toString(),
							 password,
							 Boolean.valueOf(registerForm.getValue("sendNotification").toString()),
							 new AsyncCallback<UserDetailData>() {
								@Override
								public void onSuccess(final UserDetailData userDetailData) {
									if(null==userDetailData){
										SC.say("Registration failed. Please contact administrator");
									}else{
										SC.say(userDetailData.getError());
										registerWindow.animateHide(AnimationEffect.WIPE);
									}
								}
								
								@Override
								public void onFailure(Throwable caught) {
									SC.say(caught.getMessage());
								}
							});
				 }else{
					 SC.say("Please rectify error message before registering");
				 }
				 
			}
		});  
          
        registerForm.setFields(username, emailTextItem, phoneTextItem, sendNotificationItem, password, password2, createAccount);  
        
        registerWindow.addItem(registerForm);
        
        registerWindow.setVisible(false);
        
        return registerWindow;
	}
}
