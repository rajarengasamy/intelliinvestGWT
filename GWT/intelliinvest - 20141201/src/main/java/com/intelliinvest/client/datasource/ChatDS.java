package com.intelliinvest.client.datasource;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.intelliinvest.client.IntelliInvest;
import com.intelliinvest.client.config.Constants;
import com.intelliinvest.client.data.ChatData;
import com.intelliinvest.client.data.IntelliInvestData;
import com.intelliinvest.client.data.IntelliInvestResponse;
import com.intelliinvest.client.service.IntelliInvestServiceFactory;
import com.intelliinvest.client.util.CallbackUtil;
import com.intelliinvest.client.util.DateUtil;
import com.smartgwt.client.data.fields.DataSourceDateField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class ChatDS extends IntelliInvestDS{
	ListGrid grid = null;
	int numberOfRowsretrieved = 0;
	public ChatDS() {
		super(IntelliInvestServiceFactory.intelliInvestDSService, Constants.CHAT);
		DataSourceIntegerField messageId = new DataSourceIntegerField("messageId", "Message Id");
		messageId.setPrimaryKey(true);
		DataSourceTextField usernameField = new DataSourceTextField("username", "Username");
		DataSourceDateField chatTimeField = new DataSourceDateField("chatTime", "Chat Time");
		DataSourceTextField chatMessageField = new DataSourceTextField("chatMessage", "Chat Message");
		setFields(usernameField, chatTimeField, chatMessageField);
	}
	
	public void setGrid(ListGrid grid) {
		this.grid = grid;
	}
	
	public int getNumberOfRowsretrieved() {
		return numberOfRowsretrieved;
	}
	
	public void getIncremntalRowsForChat(final CallbackUtil callbackUtil){
		String lastMessageId = "0";
 		if(grid.getTotalRows()>0){
 			
     		lastMessageId = grid.getRecord(grid.getTotalRows()-1).getAttribute("messageId");
 		}
 		String userId = "";
 		if(null!=IntelliInvest.userDetailData && null!=IntelliInvest.userDetailData.getUserId()){
 			userId = IntelliInvest.userDetailData.getUserId();
 		}
		IntelliInvestServiceFactory.utilService.getIncremntalRowsForChat(new Integer(lastMessageId), userId, new AsyncCallback<List<IntelliInvestData>>() {
			
			@Override
			public void onSuccess(List<IntelliInvestData> result) {
				for(IntelliInvestData data : result){
					addData(copyValues(data));
				}
				executeLocalAdd();
				numberOfRowsretrieved = result.size();
				callbackUtil.finished();
			}
			
			@Override
			public void onFailure(Throwable caught) {
//				SC.say("Error refreshing data for chat");
			}
		});
	}
	
	@Override
	public void postProcessRequest2(IntelliInvestResponse intelliInvestResponse) {
		super.postProcessRequest2(intelliInvestResponse);
		grid.scrollToRow(grid.getTotalRows());
	}
	
	@Override
	public IntelliInvestData copyValues(ListGridRecord record) {
		ChatData chatData = new ChatData();
		chatData.setMessageId(null!=record.getAttribute("messageId")?new Integer(record.getAttribute("messageId")):0);
		chatData.setUsername(record.getAttribute("username"));
		chatData.setChatTime(DateUtil.getDate(record.getAttribute("chatTime")));
		chatData.setChatMessage(record.getAttribute("chatMessage"));
		return chatData;
	}
	
	@Override
	public ListGridRecord copyValues(IntelliInvestData data) {
		ChatData chatData = (ChatData)data;
		ListGridRecord record = new ListGridRecord();
		record.setAttribute("messageId", chatData.getMessageId());
		record.setAttribute("username", chatData.getUsername());
		record.setAttribute("chatTime", chatData.getChatTime());
		record.setAttribute("chatMessage", chatData.getChatMessage());
		return record;
	}
}
