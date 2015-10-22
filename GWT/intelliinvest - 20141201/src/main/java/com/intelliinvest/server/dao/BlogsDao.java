package com.intelliinvest.server.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.intelliinvest.client.data.CommentData;
import com.intelliinvest.client.data.PostData;
import com.intelliinvest.server.util.HibernateUtil;

public class BlogsDao {

	private static Logger logger = Logger.getLogger(BlogsDao.class);

	private static BlogsDao blogsDao;

	private static String BLOGS_DATA_QUERY = "SELECT POST_ID as postId, AUTHOR as author, TITLE as title, '' as content, CREATED_TIME as createdTime, UPDATED_TIME as updatedTime FROM POSTS";
	private static String BLOGS_CONTENT_QUERY = "SELECT CONTENT FROM POSTS where POST_ID=:postId";
	private static String INSERT_BLOGS_QUERY = "INSERT INTO POSTS (POST_ID, AUTHOR, TITLE, CONTENT, CREATED_TIME,UPDATED_TIME) "
							+ "values (:postId, :author, :title, :content, :createdTime, :updatedTime)";
	private static String UPDATE_BLOGS_QUERY = "UPDATE POSTS SET TITLE=:title, CONTENT=:content, UPDATED_TIME=:updatedTime where POST_ID=:postId";
	
	private static String COMMENTS_DATA_QUERY = "SELECT COMMENT_ID as commentId, PARENT_ID as parentId, POST_ID as postId, COMMENTER as commenter, "
			+ "CONTENT as content,CREATED_TIME as createdTime FROM COMMENTS where POST_ID=:postId";
	
	private static String INSERT_COMMENTS_QUERY = "INSERT INTO COMMENTS (COMMENT_ID, PARENT_ID, POST_ID, COMMENTER, CONTENT, CREATED_TIME) "
			+ "values (:commentId, :parentId, :postId, :commenter, :content, :createdTime)";
	
//	private static String DELETE_COMMENTS_QUERY = "DELETE FROM  COMMENTS where COMMENT_ID = :commentId";
	
	// private static String BLOGS_DATA_QUERY =
	// "select p.post_id,p.title,p.name,p.content,p.created_at,p.updated_at,c.comment_id,c.body from Posts p left outer join p.comments_list c";

	private BlogsDao() {
		// CREATE Table PAYMENT_DETAILS(NO_OF_STOCKS int(10), NO_OF_MONTHS
		// varchar(10), AMOUNT DOUBLE, LINK varchar(200))
	}

	public static BlogsDao getInstance() {
		if (null == blogsDao) {
			synchronized (BlogsDao.class) {
				if (null == blogsDao) {
					blogsDao = new BlogsDao();
					logger.info("Initialised BlogsDao");
				}
			}
		}
		return blogsDao;
	}

	@SuppressWarnings("unchecked")
	public List<PostData> getAllBlogsData() {
		logger.info("in DAO getallblogs.............");
		Session session = null;
		Transaction transaction = null;
		List<PostData> postDatas = new ArrayList<PostData>();
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			postDatas = session.createSQLQuery(BLOGS_DATA_QUERY).addEntity(PostData.class).list();
			session.flush();
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			if(null!=session && session.isOpen()){
				session.close();
			}
		} finally {
			if(null!=session && session.isOpen()){
				session.close();
			}
		}
		return postDatas;
	}
	
	@SuppressWarnings("rawtypes")
	public String getBlogContents(Integer postId) {
		String content = "";
		logger.info("in DAO getBlogContents.............");
		List contents = new ArrayList();
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			contents = session.createSQLQuery(BLOGS_CONTENT_QUERY).setParameter("postId", postId).list();
			session.flush();
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			if(null!=session && session.isOpen()){
				session.close();
			}
		} finally {
			if(null!=session && session.isOpen()){
				session.close();
			}
		}
		if(null!=contents && contents.size()>0){
			content = contents.get(0).toString();
		}
		return content;
	}
	
	@SuppressWarnings("unchecked")
	public List<CommentData> getBlogComments(Integer postId) {
		logger.info("in DAO getBlogComments.............");
		Session session = null;
		Transaction transaction = null;
		List<CommentData> comments = new ArrayList<CommentData>();
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			comments = session.createSQLQuery(COMMENTS_DATA_QUERY).addEntity(CommentData.class).setParameter("postId", postId).list();
			session.flush();
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			if(null!=session && session.isOpen()){
				session.close();
			}
		} finally {
			if(null!=session && session.isOpen()){
				session.close();
			}
		}
		return comments;
	}

	public ArrayList<PostData> insertBlogDetails(String userId, ArrayList<PostData> postDatas) {
		logger.info("Calling insert blog method.....");
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			for(PostData postData : postDatas){
				postData.setContent(postData.getContent());
				postData.setPostId(KeyGeneratorDao.getInstance().generateKey("post", session));
				postData.setCreatedTime(new Timestamp(System.currentTimeMillis()));
				postData.setUpdatedTime(new Timestamp(System.currentTimeMillis()));
				int insertRecords = 0;
				if(null!=userId){
					int insertRecord = session.createSQLQuery(INSERT_BLOGS_QUERY)
					 											.setParameter("postId" , postData.getPostId())
					 											.setParameter("author" , postData.getAuthor())
					 											.setParameter("title" , postData.getTitle())
					 											.setParameter("content" , postData.getContent())
					 											.setParameter("createdTime" , postData.getCreatedTime())
					 											.setParameter("updatedTime" , postData.getUpdatedTime())
					 											.executeUpdate();
					insertRecords = insertRecords + insertRecord;
				}
				logger.info("Inserted " + insertRecords + " successfully");
				session.flush();
				transaction.commit();
			}
		} catch (Exception e) {
			transaction.rollback();
			if(null!=session && session.isOpen()){
				session.close();
			}
		} finally {
			if(null!=session && session.isOpen()){
				session.close();
			}
		}
		return postDatas;
	}
	
	public List<PostData> updateBlogsData(String userId, List<PostData> postDatas){
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			int updatedRecords = 0;
			for(PostData postData : postDatas ){
				if(null!=userId){
					postData.setUpdatedTime(new Timestamp(System.currentTimeMillis()));
					int updatedRecord = session.createSQLQuery(UPDATE_BLOGS_QUERY)
																	.setParameter("title" , postData.getTitle())
						 											.setParameter("content" , postData.getContent())
						 											.setParameter("updatedTime" , postData.getUpdatedTime())
						 											.setParameter("postId" , postData.getPostId())
						 											.executeUpdate();
					updatedRecords = updatedRecords + updatedRecord;
				}
			}
			logger.info("Updated " + updatedRecords + " successfully");
			session.flush();
			transaction.commit();
		}catch(Exception e){
			transaction.rollback();
			if(null!=session && session.isOpen()){
				session.close();
			}
			logger.info("Updation failed " + e.getMessage());
			return new ArrayList<PostData>();
		}finally{
			if(null!=session && session.isOpen()){
				session.close();
			}
		}
		return postDatas;
	}
	
	public ArrayList<CommentData> insertComments(String userId, ArrayList<CommentData> comments) {
		logger.info("Calling insert comments method.....");
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			for(CommentData commentData : comments){
				commentData.setContent(commentData.getContent());
				commentData.setCommentId(KeyGeneratorDao.getInstance().generateKey("comment", session));
				commentData.setCreatedTime(new Timestamp(System.currentTimeMillis()));
				int insertRecords = 0;
				if(null!=userId){
					int insertRecord = session.createSQLQuery(INSERT_COMMENTS_QUERY)
					 											.setParameter("commentId" , commentData.getCommentId())
					 											.setParameter("parentId" , commentData.getParentId())
					 											.setParameter("postId" , commentData.getPostId())
					 											.setParameter("commenter" , commentData.getCommenter())
					 											.setParameter("content" , commentData.getContent())
					 											.setParameter("createdTime" , commentData.getCreatedTime())
					 											.executeUpdate();
					insertRecords = insertRecords + insertRecord;
				}
				logger.info("Inserted " + insertRecords + " successfully");
				session.flush();
				transaction.commit();
			}
		} catch (Exception e) {
			transaction.rollback();
			if(null!=session && session.isOpen()){
				session.close();
			}
		} finally {
			if(null!=session && session.isOpen()){
				session.close();
			}
		}
		return comments;
	}
}
