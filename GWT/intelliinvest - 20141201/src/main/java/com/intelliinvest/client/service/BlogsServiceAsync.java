package com.intelliinvest.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BlogsServiceAsync {
//	void insertComments(Comment comment, AsyncCallback<Comment> callback);
//	void createNewBlog(PostData post, AsyncCallback<PostData> callback);
//	void getAllBlogs(AsyncCallback<Map<Integer,PostData>> callback);
//	void getBlogComments(Integer post_id, AsyncCallback<List<Comment>> callback);
	void getBlogContents(Integer postId, AsyncCallback<String> callback);
}
