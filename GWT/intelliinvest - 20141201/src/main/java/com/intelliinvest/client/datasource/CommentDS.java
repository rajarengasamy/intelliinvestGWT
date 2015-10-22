package com.intelliinvest.client.datasource;

import com.intelliinvest.client.config.Constants;
import com.intelliinvest.client.data.CommentData;
import com.intelliinvest.client.data.IntelliInvestData;
import com.intelliinvest.client.data.IntelliInvestRequest;
import com.intelliinvest.client.service.IntelliInvestServiceFactory;
import com.intelliinvest.client.util.DateUtil;
import com.smartgwt.client.data.fields.DataSourceDateField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class CommentDS extends IntelliInvestDS{
	Integer postId = -1;
	
	public CommentDS(Integer postId) {
		super(IntelliInvestServiceFactory.intelliInvestDSService, Constants.COMMENT);
		this.postId = postId;
		DataSourceIntegerField commentIdField = new DataSourceIntegerField("commentId", "Comment Id");
		commentIdField.setRequired(true); 
		commentIdField.setPrimaryKey(true);
		DataSourceIntegerField parentIdField = new DataSourceIntegerField("parentId", "Parent Id");
		parentIdField.setRequired(true);  
		parentIdField.setForeignKey("commentId");  
		parentIdField.setRootValue(postId);
		
		DataSourceTextField commmenterTextField = new DataSourceTextField("commenter", "commenter");
		DataSourceTextField contentTextField = new DataSourceTextField("content", "Content");
		DataSourceDateField createdDateField = new DataSourceDateField("createdTime", "Created Time");
		setFields(commentIdField, commmenterTextField, parentIdField, contentTextField, createdDateField);
	}
	
	public void setPostId(Integer postId) {
		this.postId = postId;
	}
	
	@Override
	public void preProcessRequest(IntelliInvestRequest intelliInvestRequest) {
		super.preProcessRequest(intelliInvestRequest);
		intelliInvestRequest.getRequestAttributes().put("postId", postId.toString());
	}
	
	@Override
	public IntelliInvestData copyValues(ListGridRecord record) {
		CommentData commentData = new CommentData();
		if(null!=record.getAttribute("commentId")){
			commentData.setCommentId(new Integer(record.getAttribute("commentId")));
		}
		commentData.setPostId(new Integer(record.getAttribute("postId")));
		commentData.setParentId(new Integer(record.getAttribute("parentId")));
		commentData.setCommenter(record.getAttribute("commenter"));
		commentData.setContent(record.getAttribute("content"));
		commentData.setCreatedTime(DateUtil.getDate(record.getAttribute("createdTime")));
		return commentData;
	}
	@Override
	public ListGridRecord copyValues(IntelliInvestData data) {
		CommentData commentData = (CommentData)data;
		ListGridRecord record = new ListGridRecord();
		record.setAttribute("commentId", commentData.getCommentId());
		record.setAttribute("parentId", commentData.getParentId());
		record.setAttribute("postId", commentData.getPostId());
		record.setAttribute("commenter", commentData.getCommenter());
		record.setAttribute("content", commentData.getContent());
		record.setAttribute("createdTime", commentData.getCreatedTime());
		return record;
	}	
}
