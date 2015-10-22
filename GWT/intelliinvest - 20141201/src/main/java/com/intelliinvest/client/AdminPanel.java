
package com.intelliinvest.client;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.intelliinvest.client.datasource.UserDetailsDS;
import com.intelliinvest.client.service.IntelliInvestServiceFactory;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DateDisplayFormat;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.types.FormMethod;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.BooleanItem;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.RadioGroupItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.UploadItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.RemoveRecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RemoveRecordClickHandler;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.VStack;

public class AdminPanel implements IntelliInvestPanel{
	final Layout adminLayout;
	final HStack userDetailsStack;
	final ListGrid userDetailGrid;
	final EnhancedGrid enhancedUserAdminGrid;
	Boolean resize = false;
	
	public AdminPanel() {
		this.adminLayout = new VLayout();
		userDetailsStack = new HStack();
		userDetailGrid = new ListGrid();
		enhancedUserAdminGrid = new EnhancedGrid();
		initialize();
	}

	public Layout getAdminLayout() {
		return adminLayout;
	}

	@Override
	public boolean isVisible() {
		return adminLayout.isVisible();
	}
	
	@Override
	public void markForResize() {
		this.resize = true;
	}
	
	@Override
	public void resize() {
		if(!resize){
			return;
		}
		resize = false;
		adminLayout.setWidth(IntelliInvest.WIDTH - 245 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
		adminLayout.setHeight(IntelliInvest.HEIGHT-165); 
		userDetailGrid.setWidth(IntelliInvest.WIDTH - 500 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));   
        userDetailGrid.setHeight(IntelliInvest.HEIGHT-230); 
        enhancedUserAdminGrid.resize();
	}
	public void initialize() {
		adminLayout.setShowEdges(false);   
		adminLayout.setWidth(IntelliInvest.WIDTH - 245 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
		adminLayout.setHeight(IntelliInvest.HEIGHT-165); 
		adminLayout.setMembersMargin(2);   
		adminLayout.setPadding(2);
        
		final DynamicForm form = new DynamicForm();
		form.setNumCols(6);
		final RadioGroupItem radioGroupItem = new RadioGroupItem();
		radioGroupItem.setTitle("Select");
		radioGroupItem.setValueMap("Upload file", "User administration", "Log activity", "Backload", "Admin utility");
		radioGroupItem.setShowTitle(false);
		radioGroupItem.setVertical(false);
		radioGroupItem.setWrap(false);
		radioGroupItem.setDefaultValue("Upload file");
		
		final Layout uploadFileLayout = getUploadFileLayout();
		final Layout userDetailsLayout = getUserDetailsLayout();
		final Layout backLoadLayout = getBackLoadDataLayout();
		final Layout logActivityLayout = getLogActivityLayout();
		final Layout adminUtilityLayout = getAdminUtilityLayout();
		
		final HStack adminContentStack = new HStack();
		adminContentStack.addMember(uploadFileLayout);
		adminContentStack.addMember(userDetailsLayout);
		adminContentStack.addMember(logActivityLayout);
		adminContentStack.addMember(backLoadLayout);
		adminContentStack.addMember(adminUtilityLayout);
		
		uploadFileLayout.show();
		userDetailsLayout.hide();
		logActivityLayout.hide();
		backLoadLayout.hide();
		adminUtilityLayout.hide();
		
		radioGroupItem.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				String selectedValue = radioGroupItem.getValue().toString();
				if(selectedValue.equals("Upload file")){
					uploadFileLayout.show();
					userDetailsLayout.hide();
					logActivityLayout.hide();
					backLoadLayout.hide();
					adminUtilityLayout.hide();
				}else if(selectedValue.equals("User administration")){
					uploadFileLayout.hide();
					userDetailsLayout.show();
					logActivityLayout.hide();
					backLoadLayout.hide();
					adminUtilityLayout.hide();
				}else if(selectedValue.equals("Backload")){
					uploadFileLayout.hide();
					userDetailsLayout.hide();
					logActivityLayout.hide();
					backLoadLayout.show();
					adminUtilityLayout.hide();
				}else if(selectedValue.equals("Log activity")){
					uploadFileLayout.hide();
					userDetailsLayout.hide();
					logActivityLayout.show();
					backLoadLayout.hide();
					adminUtilityLayout.hide();
				}else{
					uploadFileLayout.hide();
					userDetailsLayout.hide();
					logActivityLayout.hide();
					backLoadLayout.hide();
					adminUtilityLayout.show();
				}
			}
		});
		form.setFields(radioGroupItem);
		adminLayout.addMember(form);
		adminLayout.addMember(adminContentStack);
	}

	public Layout getBackLoadDataLayout(){
		VStack backLoadDataLayout = new VStack();
		
		final DynamicForm backLoadDataForm = new DynamicForm();
		
		backLoadDataForm.setNumCols(2);
		backLoadDataForm.setGroupTitle("Backload");
		backLoadDataForm.setWrapItemTitles(false);
        
		final SelectItem backLoadTypeItem = new SelectItem("backLoadType", "Back Load Type");
        backLoadTypeItem.setValueMap("Bhav");
        backLoadTypeItem.setDefaultToFirstOption(true);
        backLoadTypeItem.setRequired(true);
        backLoadTypeItem.setWidth(100);
        
		final DateItem fromDateItem = new DateItem("fromDate", "From Date");
        fromDateItem.setRequired(true);
        fromDateItem.setUseTextField(true);
        fromDateItem.setWidth(150);
        fromDateItem.setDefaultValue(new Date());
        fromDateItem.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATE);
        
        final DateItem toDateItem = new DateItem("toDate", "To Date");
        toDateItem.setRequired(true);
        toDateItem.setUseTextField(true);
        toDateItem.setWidth(150);
        toDateItem.setDefaultValue(new Date());
        toDateItem.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATE);
        
        backLoadDataForm.setFields(backLoadTypeItem, fromDateItem, toDateItem);
        
        backLoadDataLayout.addMember(backLoadDataForm);
        
        final DynamicForm dummyForm = new DynamicForm();  
        dummyForm.setHeight(5);
        backLoadDataLayout.addMember(dummyForm);
        
        IButton loadButton = new IButton("Load");  
        loadButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				IntelliInvestServiceFactory.utilService.upload(fromDateItem.getValueAsDate(), toDateItem.getValueAsDate(), backLoadTypeItem.getValueAsString(), new AsyncCallback<String>() {
					
					@Override
					public void onSuccess(String result) {
						SC.say(result);
					}
					
					@Override
					public void onFailure(Throwable caught) {
						SC.say(caught.getMessage());
					}
				});
			}
        }); 
        backLoadDataLayout.addMember(loadButton);
        
        final DynamicForm form = new DynamicForm();  
        form.setPadding(10);
        form.setIsGroup(true);  
        form.setWidth("100%");
        form.setNumCols(20);
        
        final TextItem stockItem = new TextItem("StockCode", "Stock");
        stockItem.setValue("ALL");
        final TextItem maItem = new TextItem("ma", "MA");
        maItem.setValue("-1");
        
        ButtonItem generateMagicNumber = new ButtonItem("magicNumber", "Generate Magic Number");
        
        generateMagicNumber.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				IntelliInvestServiceFactory.utilService.generateMagicNumber(stockItem.getValueAsString().toUpperCase(), new Integer(maItem.getValueAsString()), 
						new AsyncCallback<String>() {
							
							@Override
							public void onSuccess(String result) {
								SC.say("Successfully generated magic number for " + stockItem.getValueAsString().toUpperCase() + " stock");
							}
							
							@Override
							public void onFailure(Throwable caught) {
								SC.say("Error generating magic number for " + stockItem.getValueAsString().toUpperCase() + " stock");
							}
						});
				
			}
		});
        
        ButtonItem generateAllSignals = new ButtonItem("allSignals", "Generate All Signals");
        
        generateAllSignals.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				IntelliInvestServiceFactory.utilService.generateSignal(stockItem.getValueAsString().toUpperCase(), new Integer(maItem.getValueAsString()), 
						new AsyncCallback<String>() {
							
							@Override
							public void onSuccess(String result) {
								SC.say("Successfully generated signals for " + stockItem.getValueAsString().toUpperCase() + " stock");
							}
							
							@Override
							public void onFailure(Throwable caught) {
								SC.say("Error generating signal for " + stockItem.getValueAsString().toUpperCase() + " stock");
							}
						});
				
			}
		});
        
        
        ButtonItem generateTodaysSignal = new ButtonItem("todaysSignal", "Generate Todays Signal");
        generateTodaysSignal.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				IntelliInvestServiceFactory.utilService.generateTodaySignal(stockItem.getValueAsString().toUpperCase(), new Integer(maItem.getValueAsString()), 
						new AsyncCallback<String>() {
							
							@Override
							public void onSuccess(String result) {
								SC.say("Successfully generated TodaySignal for " + stockItem.getValueAsString().toUpperCase() + " stock");
							}
							
							@Override
							public void onFailure(Throwable caught) {
								SC.say("Error generating TodaySignal for " + stockItem.getValueAsString().toUpperCase() + " stock");
							}
						});
				
			}
		});
        
        form.setFields(stockItem, maItem, generateTodaysSignal, generateMagicNumber, generateAllSignals);
        
        
        backLoadDataLayout.addMember(form);
		return backLoadDataLayout;
	}
	public Layout getUploadFileLayout() {
		VStack uploadLayout = new VStack();
		
		final DynamicForm uploadForm = new DynamicForm();

		uploadForm.setNumCols(10);
		uploadForm.setEncoding(Encoding.MULTIPART);
		uploadForm.setMethod(FormMethod.POST);

		uploadForm.setAction(GWT.getModuleBaseURL() + "FileUpload.up");
		uploadForm.setTarget("uploadTarget");
		
		IButton uploadButton = new IButton("Upload");
		uploadButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				uploadForm.submitForm();
			}
		});

		UploadItem uploadItem = new UploadItem("filename");
		uploadItem.setTitle("Choose File");
		uploadItem.setEndRow(false);
		
		uploadForm.setFields(uploadItem);
		uploadForm.setWrapItemTitles(false);
		Label label = new Label("<br><br>File name for uploading Stock Details : stockdetails.csv{CODE, NAME}"
								+ "<br>File name for uploading Intelliinvest Data : intelliinvest.csv{CODE, EOD_PRICE, SIGNAL_PRICE, YESTERDAY_SIGNAL_TYPE, SIGNAL_TYPE, QUARTERLY, HALF_YEARLY, NINE_MONTHS, YEARLY}"
								+ "<br>File name for uploading Suggestions : suggestions.csv{CODE, SUGGESTION_TYPE, SIGNAL_TYPE, SIGNAL_PRICE}"
								+ "<br>File name for uploading Option Suggestions : optionsuggestions.csv{CODE, INSTRUMENT, EXPIRY_DATE, STRIKE_PRICE, OPTION_TYPE, OPTION_PRICE}"
								+ "<br>File name for uploading signals : signals.csv{CODE,SIGNAL_DATE,SIGNAL_TYPE}");
		label.setWrap(false);
		label.setHeight(150);
		uploadLayout.addMember(label);
		uploadLayout.addMember(uploadForm);
		uploadLayout.addMember(uploadButton);
		return uploadLayout;
	}

	public Layout getUserDetailsLayout() {
	   
	   userDetailsStack.setShowEdges(false);   
       userDetailsStack.setWidth(IntelliInvest.WIDTH - 245 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
       userDetailsStack.setHeight(IntelliInvest.HEIGHT-210); 
       userDetailsStack.setMembersMargin(2);   
       userDetailsStack.setPadding(2);
        
        final UserDetailsDS userDetailsDS = new UserDetailsDS();
        
        userDetailGrid.addRemoveRecordClickHandler(new RemoveRecordClickHandler() {
			@Override
			public void onRemoveRecordClick(RemoveRecordClickEvent event) {
				userDetailsDS.removeData(userDetailGrid.getRecord(event.getRowNum()));
			}
		});
        
        userDetailGrid.setWidth(IntelliInvest.WIDTH - 500 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));   
        userDetailGrid.setHeight(IntelliInvest.HEIGHT-230);   
        
        ListGridField userIdField = new ListGridField("userId", "User Id", 120);
        userIdField.setCanEdit(false);
        ListGridField userNameField = new ListGridField("username", "User Name", 120);
        ListGridField passwordField = new ListGridField("password", "Password", 80);
        passwordField.setCanEdit(false);
        ListGridField userTypeField = new ListGridField("userType", "User Type", 50);
        ListGridField mailIdField = new ListGridField("mail", "Mail Id",190); 
        mailIdField.setCanEdit(false);
        ListGridField phoneField = new ListGridField("phone", "Phone", 100); 
        ListGridField planField = new ListGridField("plan", "Plan", 60);
        ListGridField activeField = new ListGridField("active", "Active", 60);
        
        ListGridField creationDateField = new ListGridField("creationDate", "Creation Date", 80);
        creationDateField.setCanEdit(false);
        creationDateField.setType(ListGridFieldType.DATE);
        creationDateField.setAlign(Alignment.CENTER);
        creationDateField.setFilterEditorProperties(new TextItem());
        creationDateField.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATE);
        
        ListGridField renewalDateField = new ListGridField("renewalDate", "Renewal Date", 80);
        renewalDateField.setType(ListGridFieldType.DATE);
        renewalDateField.setAlign(Alignment.CENTER);
        renewalDateField.setFilterEditorProperties(new TextItem());
        renewalDateField.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATE);
        
        ListGridField expiryDateField = new ListGridField("expiryDate", "Expiry Date", 80);
        expiryDateField.setType(ListGridFieldType.DATE);
        expiryDateField.setAlign(Alignment.CENTER);
        expiryDateField.setFilterEditorProperties(new TextItem());
        expiryDateField.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATE);
      
        ListGridField lastLoginDateField = new ListGridField("lastLoginDate", "Last Login Date", 80);
        lastLoginDateField.setType(ListGridFieldType.DATE);
        lastLoginDateField.setAlign(Alignment.CENTER);
        lastLoginDateField.setFilterEditorProperties(new TextItem());
        lastLoginDateField.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
        
        ListGridField sendNotificationField = new ListGridField("sendNotification", "Send Notification", 80);
        sendNotificationField.setType(ListGridFieldType.BOOLEAN);
        sendNotificationField.setAlign(Alignment.CENTER);
        
        userDetailGrid.setFields(userIdField, userNameField, sendNotificationField, lastLoginDateField, passwordField, userTypeField, mailIdField, phoneField, planField, activeField
        		,creationDateField, renewalDateField, expiryDateField);
     
        userDetailGrid.setDataSource(userDetailsDS);
        userDetailGrid.setShowAllRecords(true); 
        userDetailGrid.setShowHeaderMenuButton(false);
        userDetailGrid.setShowFilterEditor(true);  
        userDetailGrid.setCanReorderFields(false);   
        userDetailGrid.setAutoFetchData(true);
        userDetailGrid.setCanEdit(false);
        userDetailGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
        userDetailGrid.setAutoSaveEdits(false);
        userDetailGrid.setCanRemoveRecords(true);
        userDetailGrid.setShowHeaderMenuButton(false);
        userDetailGrid.setShowHeaderContextMenu(false);
        
        userDetailsStack.addMember(enhancedUserAdminGrid.enhanceGrid("Admin", userDetailGrid, true));
        enhancedUserAdminGrid.getSaveButton().setVisible(false);
//        enhancedUserAdminGrid.getPaddingForm().setWidth(50);
        enhancedUserAdminGrid.getRefreshButton().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				userDetailsDS.setFetch(true);
				userDetailGrid.invalidateCache();
				userDetailGrid.fetchData();
			}
		});
        
        final DynamicForm dummyForm2 = new DynamicForm();  
        dummyForm2.setWidth(5);
        userDetailsStack.addMember(dummyForm2);
        
        VStack vStack = new VStack();
        vStack.setWidth(200);
        
        final DynamicForm dummyForm1 = new DynamicForm();  
        dummyForm1.setHeight(30);
        vStack.addMember(dummyForm1);
        
        final DynamicForm form = new DynamicForm();  
        form.setPadding(10);
        form.setIsGroup(true);  
        form.setWidth(200);
        
        TextItem userIdItem = new TextItem("userId", "User Id");
        userIdItem.setDisabled(true);
        userIdItem.setRequired(true); 
        TextItem userNameItem = new TextItem("username", "User Name");
        userNameItem.setRequired(true); 
        TextItem passwordItem = new TextItem("password", "Password");
        passwordItem.setRequired(true); 
        TextItem userTypeItem = new TextItem("userType", "User Type");
        userTypeItem.setRequired(true); 
        TextItem mailItem = new TextItem("mail", "E-Mail");
        mailItem.setRequired(true); 
        mailItem.setDisabled(true);
        TextItem phoneItem = new TextItem("phone", "Phone");
        phoneItem.setRequired(true); 
        TextItem planItem = new TextItem("plan", "Plan");
        planItem.setRequired(true); 
        
        DateItem creationDateItem = new DateItem("creationDate", "Creation Date");
        creationDateItem.setRequired(true);
        creationDateItem.setUseTextField(true);
        creationDateItem.setWidth(90);
        creationDateItem.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATE);
        creationDateItem.setCanEdit(false);

        DateItem renewalDateItem = new DateItem("renewalDate", "Renewal Date");
        renewalDateItem.setRequired(true);
        renewalDateItem.setUseTextField(true);
        renewalDateItem.setWidth(90);
        renewalDateItem.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATE);
        
        DateItem expiryDateItem = new DateItem("expiryDate", "Expiry Date");
        expiryDateItem.setRequired(true);
        expiryDateItem.setUseTextField(true);
        expiryDateItem.setWidth(90);
        expiryDateItem.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATE);
        
        final BooleanItem sendNotificationItem = new BooleanItem("sendNotification", "Send Notification");
        sendNotificationItem.setTitleOrientation(TitleOrientation.LEFT);
        sendNotificationItem.setShowTitle(false);
        sendNotificationItem.setRequired(true);  
        sendNotificationItem.setDefaultValue(true); 
        
        form.setGroupTitle("Edit");
        form.setWrapItemTitles(false);
        form.setNumCols(2);  
        form.setDataSource(userDetailsDS); 
        form.setFields(userIdItem, mailItem, userNameItem, sendNotificationItem, passwordItem, userTypeItem, phoneItem, planItem, creationDateItem, renewalDateItem, expiryDateItem);
        
        userDetailGrid.addRecordClickHandler(new RecordClickHandler() {
            public void onRecordClick(RecordClickEvent event) {  
                form.reset();  
                form.editSelectedData(userDetailGrid);  
            }  
        });  
        
        vStack.addMember(form);
        
        final DynamicForm dummyForm = new DynamicForm();  
        dummyForm.setHeight(5);
        vStack.addMember(dummyForm);
        
        IButton addButton = new IButton("Save");  
        addButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {  
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {  
                Record record = form.getValuesAsRecord();   
                userDetailsDS.updateData(record);
                userDetailsDS.executeUpdate();
                userDetailGrid.redraw();
            }  
        }); 
        vStack.addMember(addButton);
        userDetailsStack.addMember(vStack);
        
       return userDetailsStack;
	}
	
	public Layout getLogActivityLayout(){
		HStack logActivityLayout = new HStack();
		logActivityLayout.setContents("Coming soon...");
		return logActivityLayout;
		
	}
	
	public Layout getAdminUtilityLayout(){
		VStack utilityStack = new VStack();
		DynamicForm refreshForm = new DynamicForm();
		refreshForm.setNumCols(6);
		refreshForm.setTitle("Refresh");
		refreshForm.setWrapItemTitles(false);
		refreshForm.setPadding(10);
		
		ButtonItem refreshButton = new ButtonItem("refreshStaticData", "Refresh Static Data");
		refreshButton.setShowTitle(false);
		refreshButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				IntelliInvestServiceFactory.utilService.refreshStaticData(IntelliInvest.userDetailData.getMail(), IntelliInvest.userDetailData.getPassword(), 
						new AsyncCallback<String>() {
							
							@Override
							public void onSuccess(String result) {
								SC.say(result);
							}
							
							@Override
							public void onFailure(Throwable caught) {
								SC.say("Error refreshing Static Data " + caught.getMessage());
							}
						});
			}
		});
		
		ButtonItem refreshIncludingPriceButton = new ButtonItem("refreshStaticDataIncludingPrice", "Refresh Static Data Including Price");
		refreshIncludingPriceButton.setShowTitle(false);
		refreshIncludingPriceButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				IntelliInvestServiceFactory.utilService.refreshStaticDataIncludingPrice(IntelliInvest.userDetailData.getMail(), IntelliInvest.userDetailData.getPassword(), 
						new AsyncCallback<String>() {
							
							@Override
							public void onSuccess(String result) {
								SC.say(result);
							}
							
							@Override
							public void onFailure(Throwable caught) {
								SC.say("Error refreshing Static Data Including Price " + caught.getMessage());
							}
						});
			}
		});
		
		ButtonItem startRefreshButton = new ButtonItem("startRefreshStaticData", "Start Static Data Refresh");
		startRefreshButton.setShowTitle(false);
		startRefreshButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				IntelliInvestServiceFactory.utilService.startRefreshStaticData(IntelliInvest.userDetailData.getMail(), IntelliInvest.userDetailData.getPassword(), 
						new AsyncCallback<String>() {
							
							@Override
							public void onSuccess(String result) {
								SC.say(result);
							}
							
							@Override
							public void onFailure(Throwable caught) {
								SC.say("Error start refreshing " + caught.getMessage());
							}
						});
			}
		});
		
		ButtonItem stopRefreshButton = new ButtonItem("stopRefreshStaticData", "Stop Static Data Refresh");
		stopRefreshButton.setShowTitle(false);
		stopRefreshButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				IntelliInvestServiceFactory.utilService.stopRefreshStaticData(IntelliInvest.userDetailData.getMail(), IntelliInvest.userDetailData.getPassword(), 
						new AsyncCallback<String>() {
							
							@Override
							public void onSuccess(String result) {
								SC.say(result);
							}
							
							@Override
							public void onFailure(Throwable caught) {
								SC.say("Error stop refreshing " + caught.getMessage());
							}
						});
			}
		});
		
		ButtonItem sendDailyUpdateMailButton = new ButtonItem("sendDailyUpdateMail", "Send Daily Update Mail");
		sendDailyUpdateMailButton.setShowTitle(false);
		sendDailyUpdateMailButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				IntelliInvestServiceFactory.utilService.sendDailyUpdateMail(IntelliInvest.userDetailData.getMail(), IntelliInvest.userDetailData.getPassword(), 
						new AsyncCallback<String>() {
							
							@Override
							public void onSuccess(String result) {
								SC.say(result);
							}
							
							@Override
							public void onFailure(Throwable caught) {
								SC.say("Error stop refreshing " + caught.getMessage());
							}
						});
			}
		});
		
		refreshForm.setItems(refreshButton, startRefreshButton, stopRefreshButton, sendDailyUpdateMailButton);
		utilityStack.addMember(refreshForm);
		return utilityStack;
		
	}
}
