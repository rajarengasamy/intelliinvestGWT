package com.intelliinvest.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@RemoteServiceRelativePath("blogs")
public interface BlogsService extends RemoteService{
//	Comment insertComments(Comment comment);
//	PostData createNewBlog(PostData post);
//	Map<Integer, PostData> getAllBlogs();
//	List<Comment> getBlogComments(Integer post_id);
	String getBlogContents(Integer postId);
}
