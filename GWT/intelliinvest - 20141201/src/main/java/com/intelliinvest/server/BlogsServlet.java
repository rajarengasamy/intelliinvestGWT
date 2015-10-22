package com.intelliinvest.server;

import org.apache.log4j.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.intelliinvest.client.service.BlogsService;
import com.intelliinvest.server.dao.BlogsDao;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class BlogsServlet extends RemoteServiceServlet implements BlogsService {

	private static Logger logger = Logger.getLogger(BlogsServlet.class);

//	@Override
//	public Comment insertComments(Comment comment) {
//		return BlogsDao.getInstance().insertComments(comment);
//	}
//
//	@Override
//	public PostData createNewBlog(PostData post) {
//		return BlogsDao.getInstance().insertBlogDetails(post);
//		
//	}
//	
//	@Override
//	public Map<Integer,PostData> getAllBlogs() {
//		logger.info("Calling Servlet's methods getallblogs...................");
//		List<PostData> returnData = BlogsDao.getInstance().getAllBlogs();
//		Map<Integer,PostData> posts = new HashMap<Integer,PostData>();
//		for(PostData post : returnData){
//			posts.put(post.getPost_id(), post);
//		}
//		logger.info("Count:"+returnData.size());
//		return posts;
//	}
//	
//	@Override
//	public List<Comment> getBlogComments(Integer post_id) {
//		logger.info("Calling Servlet's methods getBlogComments...................");
//		List<Comment> returnData = BlogsDao.getInstance().getBlogComments(post_id);
//		logger.info("Count:"+returnData.size());
//		return returnData;
//	}
//	
	@Override
	public String getBlogContents(Integer post_id) {
		logger.info("Calling Servlet's methods getBlogContents...................");
		String returnData = BlogsDao.getInstance().getBlogContents(post_id);
		return returnData;
	}

}
	