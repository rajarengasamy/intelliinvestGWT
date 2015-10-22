package com.intelliinvest.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.intelliinvest.client.data.UserDetailData;
import com.intelliinvest.client.service.LoginService;
import com.intelliinvest.server.dao.UserDetailDao;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class LoginServlet extends RemoteServiceServlet implements LoginService {
	
	@Override
	public UserDetailData login(String mail, String password) {
		return UserDetailDao.getInstance().login(mail, password);
	}
	
	@Override
	public UserDetailData register(String userName, String mail, String phone, String password, Boolean sendNotification) {
		return UserDetailDao.getInstance().register(userName, mail, phone, password, sendNotification);
	}
	
	@Override
	public String forgotPassword(String mail) {
		return UserDetailDao.getInstance().forgotPassword(mail);
	}
	
	@Override
	public UserDetailData save(String userName, String mail, String phone, String oldPassword, String newPassword, Boolean sendNotification) {
		return UserDetailDao.getInstance().saveProfile(userName, mail, phone, oldPassword, newPassword, sendNotification);
	}
	@Override
	public Boolean loginExists(String mailId) {
		return UserDetailDao.getInstance().mailIdExists(mailId);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String mail = req.getParameter("mailId");
		String activationCode = req.getParameter("activationCode");
		if(null!=mail && null!=activationCode){
			Boolean activated = UserDetailDao.getInstance().activateAccount(mail, activationCode);
			if(activated){
				resp.getOutputStream().write("Account activated Successfully".getBytes());
			}else{
				resp.getOutputStream().write("Account activation failed. Please contact administrator for further details.".getBytes());
			}
		}
	}
	
}
