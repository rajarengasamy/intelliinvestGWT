package com.intelliinvest.client.data;

import java.util.Date;


@SuppressWarnings("serial")
public class ChatData implements IntelliInvestData{
	Integer messageId;
	String username;
	Date chatTime;
	String chatMessage;

	public ChatData() {
	}
    
	public ChatData(Integer messageId, String username, Date chatTime, String chatMessage) {
		super();
		this.messageId = messageId;
		this.username = username;
		this.chatTime = chatTime;
		this.chatMessage = chatMessage;
	}
	
	public Integer getMessageId() {
		return messageId;
	}
	
	public void setMessageId(Integer messageId) {
		this.messageId = messageId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getChatTime() {
		return chatTime;
	}

	public void setChatTime(Date chatTime) {
		this.chatTime = chatTime;
	}

	public String getChatMessage() {
		return chatMessage;
	}

	public void setChatMessage(String chatMessage) {
		this.chatMessage = chatMessage;
	}

	@Override
	public String toString() {		
		try{
			return 
			"{" 
			+ "\"messageId\":\"" + messageId + "\","
			+ "\"code\":\"" + username + "\","
			+ "\"chatTime\":\"" + chatTime + "\","
			+ "\"chatMessage\":\"" +  chatMessage + "\""
			+ "}";			
		}catch (Exception e) {
			return null;
		}
		finally{			
		}
	}
	@Override
	public boolean equals(Object obj) {
		ChatData chatData = (ChatData) obj;
		if(chatData.messageId.equals(this.messageId)){
			return true;
		}else{
			return false;
		}
	}
	
	public ChatData clone() {
		ChatData simulationData = new ChatData(messageId, username, chatTime, chatMessage);
		return simulationData;
	}
}
