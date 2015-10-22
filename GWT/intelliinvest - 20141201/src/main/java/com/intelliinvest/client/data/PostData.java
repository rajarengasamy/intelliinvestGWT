package com.intelliinvest.client.data;


import java.util.Date;


@SuppressWarnings("serial")
public class PostData implements IntelliInvestData {

	private Integer postId;
	private String author;
	private String content;
	private String title;
	private Date createdTime;
	private Date updatedTime;
	private String error;
	
	public PostData() {
	}
	
	public PostData(Integer postId) {
		this.postId = postId;
	}
	
	public Integer getPostId() {
		return postId;
	}

	public void setPostId(Integer postId) {
		this.postId = postId;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
	@Override
	public boolean equals(Object obj) {
		return this.postId.equals(((PostData)obj).getPostId());
	}
	
	@Override
	public int hashCode() {
		return postId.hashCode();
	}
	
}
