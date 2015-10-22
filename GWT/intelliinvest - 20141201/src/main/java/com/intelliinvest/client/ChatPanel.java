package com.intelliinvest.client;

import com.google.gwt.user.client.Timer;
import com.intelliinvest.client.datasource.ChatDS;
import com.intelliinvest.client.util.CallbackUtil;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DateDisplayFormat;
import com.smartgwt.client.types.KeyNames;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyUpEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyUpHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;

public class ChatPanel implements IntelliInvestPanel{
	final Layout chatPanelLayout;
	final ChatDS chatDS;
	final ListGrid chatGrid;
	final DynamicForm messageForm;
	final EnhancedGrid chatEnhancedGrid;
	Boolean resize = false;
	static Timer liveGridTimer;
	
	public ChatPanel() {
		this.chatPanelLayout = new VLayout();
		chatDS = new ChatDS();
		chatGrid = getChatGrid(chatDS);
		chatDS.setGrid(chatGrid);
		messageForm = new DynamicForm();
		chatEnhancedGrid = new EnhancedGrid();
		initialize();
		if(null==liveGridTimer){
			liveGridTimer = new Timer() {
				@Override
				public void run() {
	         		chatDS.getIncremntalRowsForChat(new CallbackUtil() {
						@Override
						public void finished() {
							if(chatDS.getNumberOfRowsretrieved()>0){
								if(!MiddlePanel.middlePanelTabSet.getSelectedTab().getID().equals("Chat")){
									MiddlePanel.chatImg.setSrc("/data/images/message.gif");
								}
							}
						}
					});
				}
			};
			System.out.println("adding chat time schedule");
			liveGridTimer.scheduleRepeating(15000);
		}
		
	}
	
	@Override
	public boolean isVisible() {
		return chatPanelLayout.isVisible();
	}
	
	public Layout getChatPanelLayout() {
		return chatPanelLayout;
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
		
		messageForm.setWidth(IntelliInvest.WIDTH - 230 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
		
		chatGrid.setWidth(IntelliInvest.WIDTH - 230 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
		messageForm.getField("chatTextEditor").setWidth(IntelliInvest.WIDTH - 315 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
		chatGrid.setHeight(IntelliInvest.HEIGHT-270); 
		chatEnhancedGrid.resize();
		
		chatPanelLayout.setWidth(IntelliInvest.WIDTH - 225 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
		chatPanelLayout.setHeight(IntelliInvest.HEIGHT-165);
		
	}
	
	public void initialize(){
		chatPanelLayout.setWidth(IntelliInvest.WIDTH-225-(IntelliInvest.LEFT_PANEL.isVisible()?200:0)); 
		chatPanelLayout.setHeight(IntelliInvest.HEIGHT-165);
        
        chatPanelLayout.addMember(chatEnhancedGrid.enhanceGrid("IntelliInvest Chat", chatGrid, false));
        
        messageForm.setID("homeChatForm");
        messageForm.setNumCols(4);
        messageForm.setHeight(50);
        messageForm.setWidth(IntelliInvest.WIDTH - 230 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
        
        final TextAreaItem textEditor = new TextAreaItem("chatTextEditor");
        textEditor.setStartRow(true);
        textEditor.setEndRow(false);
        textEditor.setHeight(50);
        textEditor.setWidth(IntelliInvest.WIDTH - 315 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
        textEditor.setShowTitle(false);
        
        textEditor.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if(event.getKeyName().equals(KeyNames.ENTER)){
					if(null==IntelliInvest.userDetailData || null==IntelliInvest.userDetailData.getUserId()){
	             		SC.say("Only logged in user can participate in chat");
	             	}else{
	             		chatDS.getIncremntalRowsForChat(new CallbackUtil() {
							@Override
							public void finished() {
								 ListGridRecord record = new ListGridRecord();  
			 	                 record.setAttribute("username", IntelliInvest.userDetailData.getUsername());
			 	                 record.setAttribute("chatMessage", textEditor.getValue());
			 	                 chatDS.addData(record);
			 	                 chatDS.executeAdd();
				 	             textEditor.setValue("");
							}
						});
	             	}
				}
			}
		});
        
       
        ButtonItem addButton = new ButtonItem("Add");
        addButton.setStartRow(false);
        addButton.setEndRow(true);
        addButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(null==IntelliInvest.userDetailData || null==IntelliInvest.userDetailData.getUserId()){
             		SC.say("Only logged in user can participate in chat");
             	}else{
             		chatDS.getIncremntalRowsForChat(new CallbackUtil() {
						@Override
						public void finished() {
							 ListGridRecord record = new ListGridRecord();  
		 	                 record.setAttribute("username", IntelliInvest.userDetailData.getUsername());
		 	                 record.setAttribute("chatMessage", textEditor.getValue());
		 	                 chatDS.addData(record);
		 	                 chatDS.executeAdd();
			 	             textEditor.setValue("");
						}
					});
             	}
			}
		});
        addButton.setWidth(85);
        addButton.setHeight(50);
        addButton.setAlign(Alignment.CENTER);
        
        messageForm.setFields(textEditor, addButton);
        chatPanelLayout.addMember(messageForm);
        
	}
	
	private ListGrid getChatGrid(final ChatDS chatDS){
		final ListGrid chatGrid = new ListGrid();
        chatGrid.setWidth(IntelliInvest.WIDTH - 230 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));  
        chatGrid.setHeight(IntelliInvest.HEIGHT-270);  
        ListGridField messageIdField = new ListGridField("messageId", "Message Id", 100); 
        messageIdField.setHidden(true);
        ListGridField usernameField = new ListGridField("username", "User", 100); 
        ListGridField chatTimeField = new ListGridField("chatTime", "Time", 100); 
        chatTimeField.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
        ListGridField chatMessageField = new ListGridField("chatMessage", "Message",675); 
        chatGrid.setFixedRecordHeights(false);  
        chatGrid.setDataSource(chatDS);  
        chatGrid.setWrapCells(true);
        chatGrid.setCanHover(true);
        chatGrid.setAutoFetchData(true);
        chatGrid.setShowHeader(true);
        chatGrid.setCanSort(false);
        chatGrid.setShowHeaderMenuButton(false);
        chatGrid.setShowHeaderContextMenu(false);
        chatGrid.setDefaultFields(new ListGridField[]{messageIdField, usernameField, chatTimeField, chatMessageField});
        
        usernameField.setWidth("14%");
        chatTimeField.setWidth("12%");
        chatMessageField.setWidth("74%");

//		chatGrid.addDataArrivedHandler(new DataArrivedHandler() {
//			@Override
//			public void onDataArrived(DataArrivedEvent event) {
//				liveGridTimer.scheduleRepeating(15000);
//			}
//		});
		
        return chatGrid;
	}

}
