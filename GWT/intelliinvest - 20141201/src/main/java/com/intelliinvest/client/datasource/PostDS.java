package com.intelliinvest.client.datasource;

import com.intelliinvest.client.config.Constants;
import com.intelliinvest.client.data.IntelliInvestData;
import com.intelliinvest.client.data.IntelliInvestRequest;
import com.intelliinvest.client.data.PostData;
import com.intelliinvest.client.service.IntelliInvestServiceFactory;
import com.intelliinvest.client.util.DateUtil;
import com.smartgwt.client.data.fields.DataSourceDateField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class PostDS extends IntelliInvestDS{
	
	public PostDS() {
		super(IntelliInvestServiceFactory.intelliInvestDSService, Constants.POST);
		DataSourceIntegerField messageId = new DataSourceIntegerField("postId", "postId");
		messageId.setPrimaryKey(true);
		DataSourceTextField authorTextField = new DataSourceTextField("author", "Author");
		DataSourceTextField titleTextField = new DataSourceTextField("title", "Title");
		DataSourceTextField contentTextField = new DataSourceTextField("content", "Content");
		DataSourceDateField createdDateField = new DataSourceDateField("createdTime", "Created Time");
		DataSourceDateField updatedDateField = new DataSourceDateField("updatedTime", "Updated Time");
		setFields(messageId, authorTextField, titleTextField, contentTextField, createdDateField, updatedDateField);
	}
	
	@Override
	public void preProcessRequest(IntelliInvestRequest intelliInvestRequest) {
		super.preProcessRequest(intelliInvestRequest);
	}
	
	@Override
	public IntelliInvestData copyValues(ListGridRecord record) {
		PostData postData = new PostData();
		if(null!=record.getAttribute("postId")){
			postData.setPostId(new Integer(record.getAttribute("postId")));
		}
		postData.setAuthor(record.getAttribute("author"));
		postData.setTitle(record.getAttribute("title"));
		postData.setContent(record.getAttribute("content"));
		postData.setCreatedTime(DateUtil.getDate(record.getAttribute("createdTime")));
		postData.setUpdatedTime(DateUtil.getDate(record.getAttribute("updatedTime")));
		return postData;
	}
	@Override
	public ListGridRecord copyValues(IntelliInvestData data) {
		PostData postData = (PostData)data;
		ListGridRecord record = new ListGridRecord();
		record.setAttribute("postId", postData.getPostId());
		record.setAttribute("author", postData.getAuthor());
		record.setAttribute("title", postData.getTitle());
		record.setAttribute("content", postData.getContent());
		record.setAttribute("createdTime", postData.getCreatedTime());
		record.setAttribute("updatedTime", postData.getUpdatedTime());
		return record;
	}	
}
