package com.intelliinvest.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.intelliinvest.client.EncryptUtil;
import com.intelliinvest.client.data.UserDetailData;
import com.intelliinvest.server.dao.UserDetailDao;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class InitializationServlet extends HttpServlet {
	
	private static Logger logger = Logger.getLogger(InitializationServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try{
			String type = req.getParameter("type");
			String mailId = req.getParameter("mailId");
			String password = req.getParameter("password");
			
			password = EncryptUtil.encrypt(password);
		
			logger.info("Received type " + type + " , mailId " + mailId + ", password " + password + " in initializationServlet");
			
			UserDetailData userDetailData = UserDetailDao.getInstance().login(mailId, password);
			if(null!=userDetailData && userDetailData.getUserType().equals("Admin")){
				if(type.equals("staticRefresh")){
					IntelliInvestStore.updateCurrentPrices();
					IntelliInvestStore.updateWorldPrices();
					IntelliInvestStore.refresh();
					resp.getOutputStream().write("<h1>Initialized static data for IntelliInvest<h1>".getBytes());
				}else if(type.equals("stopStaticRefresh")){
					IntelliInvestStore.REFRESH_PERIODICALLY=false;
					resp.getOutputStream().write("<h1>Stopped static data for IntelliInvest. Do remember to start again.<h1>".getBytes());
				}else if(type.equals("startStaticRefresh")){
					IntelliInvestStore.REFRESH_PERIODICALLY=true;
					resp.getOutputStream().write("<h1>Started static data for IntelliInvest. Stop this service whenever needed.<h1>".getBytes());
				}else if(type.equals("chartFrom")){
					String value = req.getParameter("value");
					ChartRequestServlet.CHART_FROM = value;
					resp.getOutputStream().write("<h1>Started static data for IntelliInvest. Stop this service whenever needed.<h1>".getBytes());
				}else if(type.equals("newsFrom")){
					String value = req.getParameter("value");
					NewsRequestServlet.NEWS_FROM = value;
					NewsRequestServlet.setTopStories();
					resp.getOutputStream().write("<h1>Started static data for IntelliInvest. Stop this service whenever needed.<h1>".getBytes());
				}else{
					resp.getOutputStream().write("<h1>Unknown type. Use staticRefresh, stopStaticRefresh, startStaticRefresh, retrieveChartFrom <h1>".getBytes());
				}
			}else{
				resp.getOutputStream().write("<h1>Not Authorised to do this action.<h1>".getBytes());
			}
		}catch(Exception e){
			logger.error("Exception occured " + e.getMessage());
			resp.getOutputStream().write("<h1>Exception occured while executing action. check log for details<h1>".getBytes());
		}
		    
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
	
}
