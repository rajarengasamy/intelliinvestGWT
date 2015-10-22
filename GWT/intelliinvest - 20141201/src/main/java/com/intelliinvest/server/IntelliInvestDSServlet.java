package com.intelliinvest.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.intelliinvest.client.config.Constants;
import com.intelliinvest.client.data.ChatData;
import com.intelliinvest.client.data.CommentData;
import com.intelliinvest.client.data.IntelliInvestData;
import com.intelliinvest.client.data.IntelliInvestRequest;
import com.intelliinvest.client.data.IntelliInvestResponse;
import com.intelliinvest.client.data.ManagePortfolioData;
import com.intelliinvest.client.data.PostData;
import com.intelliinvest.client.data.SimulationData;
import com.intelliinvest.client.data.TradingAccountData;
import com.intelliinvest.client.data.UserDetailData;
import com.intelliinvest.client.service.IntelliInvestService;
import com.intelliinvest.server.dao.BlogsDao;
import com.intelliinvest.server.dao.ChatDao;
import com.intelliinvest.server.dao.ManagePortfolioDao;
import com.intelliinvest.server.dao.OurSuggestionDao;
import com.intelliinvest.server.dao.SimulationDao;
import com.intelliinvest.server.dao.TradingAccountDao;
import com.intelliinvest.server.dao.UserDetailDao;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class IntelliInvestDSServlet extends RemoteServiceServlet implements IntelliInvestService {
	
	private static Logger logger = Logger.getLogger(IntelliInvestDSServlet.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public IntelliInvestResponse fetch(IntelliInvestRequest intelliInvestRequest) {
		String user = intelliInvestRequest.getRequestAttributes().get("userId");
		IntelliInvestResponse intelliInvestResponse = new IntelliInvestResponse();
		List< ? extends IntelliInvestData> responseData = new ArrayList<IntelliInvestData>();
		logger.info("Inside IntelliInvestDSServlet ..." + intelliInvestRequest.getRequestType() + " for user " + user + " with request size " + intelliInvestRequest.getRequestData().size());
		if(intelliInvestRequest.getRequestType().equals(Constants.MANAGE_PORTFOLIO + Constants.FETCH)){
			intelliInvestResponse.setResponseType(Constants.MANAGE_PORTFOLIO + Constants.FETCH);
			responseData = ManagePortfolioDao.getInstance().getManagePortfolioData(user);
			intelliInvestResponse.getScreenData().put("SUMMARY", ManagePortfolioDao.getInstance().getManagePortfolioSummaryData((List<ManagePortfolioData>)responseData));
		}else if(intelliInvestRequest.getRequestType().equals(Constants.NEW_MANAGE_PORTFOLIO + Constants.FETCH)){
			intelliInvestResponse.setResponseType(Constants.NEW_MANAGE_PORTFOLIO + Constants.FETCH);
			String code = intelliInvestRequest.getRequestAttributes().get("code");
			if(code.equalsIgnoreCase("SUMMARY")){
				List< ? extends IntelliInvestData> responseDetailData = ManagePortfolioDao.getInstance().getManagePortfolioData(user);
				responseData = new ArrayList<ManagePortfolioData>(ManagePortfolioDao.getInstance().getManagePortfolioSummaryData((List<ManagePortfolioData>)responseDetailData).values());
			}else{
				List<ManagePortfolioData> managePortfolioDatas = ManagePortfolioDao.getInstance().getManagePortfolioDataForCode(user, code);
				ManagePortfolioDao.getInstance().populatePnl(managePortfolioDatas);
				responseData = managePortfolioDatas;
			}
		}else if(intelliInvestRequest.getRequestType().equals(Constants.TRADING_ACCOUNT + Constants.FETCH)){
			intelliInvestResponse.setResponseType(Constants.TRADING_ACCOUNT + Constants.FETCH);
			responseData = TradingAccountDao.getInstance().getTradingAccountData(user);
		}else if(intelliInvestRequest.getRequestType().equals(Constants.SIMULATION + Constants.FETCH)){
			intelliInvestResponse.setResponseType(Constants.SIMULATION + Constants.FETCH);
			responseData = SimulationDao.getInstance().getSimulationData(user);
		}else if(intelliInvestRequest.getRequestType().equals(Constants.OUR_SUGGESTION + Constants.FETCH)){
			intelliInvestResponse.setResponseType(Constants.OUR_SUGGESTION + Constants.FETCH);
			String suggestionType = intelliInvestRequest.getRequestAttributes().get("suggestionType");
			responseData = OurSuggestionDao.getInstance().getOurSuggestionsData(user, suggestionType);
		}
//		else if(intelliInvestRequest.getRequestType().equals(Constants.OUR_OPTION_SUGGESTION + Constants.FETCH)){
//			intelliInvestResponse.setResponseType(Constants.OUR_OPTION_SUGGESTION + Constants.FETCH);
//			responseData = OurSuggestionDao.getInstance().getOurOptionSuggestionsData(user);
//		}
		else if(intelliInvestRequest.getRequestType().equals(Constants.USER_DETAIL + Constants.FETCH)){
			intelliInvestResponse.setResponseType(Constants.USER_DETAIL + Constants.FETCH);
			responseData = UserDetailDao.getInstance().getUserDetails(user);
		}else if(intelliInvestRequest.getRequestType().equals(Constants.CHAT + Constants.FETCH)){
			intelliInvestResponse.setResponseType(Constants.CHAT + Constants.FETCH);
			responseData = ChatDao.getInstance().getChatData();
		}else if(intelliInvestRequest.getRequestType().equals(Constants.POST + Constants.FETCH)){
			intelliInvestResponse.setResponseType(Constants.POST + Constants.FETCH);
			responseData = BlogsDao.getInstance().getAllBlogsData();
		}else if(intelliInvestRequest.getRequestType().equals(Constants.COMMENT + Constants.FETCH)){
			String postId = intelliInvestRequest.getRequestAttributes().get("postId");
			intelliInvestResponse.setResponseType(Constants.COMMENT + Constants.FETCH);
			responseData = BlogsDao.getInstance().getBlogComments(new Integer(postId));
		}
		intelliInvestResponse.setResponseData(new ArrayList<IntelliInvestData>(responseData));
		logger.info(intelliInvestRequest.getRequestType() + " for user " + user +" returned result of size " + intelliInvestResponse.getResponseData().size());
		return intelliInvestResponse;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public IntelliInvestResponse add(IntelliInvestRequest intelliInvestRequest) {
		 String user = intelliInvestRequest.getRequestAttributes().get("userId");
		 IntelliInvestResponse intelliInvestResponse = new IntelliInvestResponse();
		 List<? extends IntelliInvestData> responseData = new ArrayList<IntelliInvestData>();
		 logger.info("Inside IntelliInvestDSServlet ..." + intelliInvestRequest.getRequestType() + " for user " + user + " with request size " + intelliInvestRequest.getRequestData().size());
		 if(intelliInvestRequest.getRequestType().equals(Constants.MANAGE_PORTFOLIO + Constants.ADD)){
			intelliInvestResponse.setResponseType(Constants.MANAGE_PORTFOLIO + Constants.ADD);
			responseData = ManagePortfolioDao.getInstance().addManagePortfolioData(user, (ArrayList<ManagePortfolioData>)intelliInvestRequest.getRequestData());
			List<ManagePortfolioData> changedData = ManagePortfolioDao.getInstance().getManagePortfolioData(user, (ArrayList<ManagePortfolioData>)responseData);
			HashMap<String, ManagePortfolioData> changedResponseData = new HashMap<String, ManagePortfolioData>();
			for(ManagePortfolioData managePortfolioData : changedData){
				changedResponseData.put(managePortfolioData.getId(), managePortfolioData);
			}
			intelliInvestResponse.getScreenData().put("CHANGED", changedResponseData);
			intelliInvestResponse.getScreenData().put("SUMMARY", ManagePortfolioDao.getInstance().getManagePortfolioSummaryData(changedData));
		}else if(intelliInvestRequest.getRequestType().equals(Constants.NEW_MANAGE_PORTFOLIO + Constants.ADD)){
			intelliInvestResponse.setResponseType(Constants.NEW_MANAGE_PORTFOLIO + Constants.ADD);
			List<ManagePortfolioData> managePortfolioDatas = ManagePortfolioDao.getInstance().addManagePortfolioData(user, (ArrayList<ManagePortfolioData>)intelliInvestRequest.getRequestData());
			List<ManagePortfolioData> changedData = ManagePortfolioDao.getInstance().getManagePortfolioData(user, managePortfolioDatas);
			responseData = new ArrayList<ManagePortfolioData>(ManagePortfolioDao.getInstance().getManagePortfolioSummaryData(changedData).values());
		}else if(intelliInvestRequest.getRequestType().equals(Constants.SIMULATION + Constants.ADD)){
			intelliInvestResponse.setResponseType(Constants.SIMULATION + Constants.ADD);
			responseData = SimulationDao.getInstance().addSimulationData(user, (ArrayList<SimulationData>)intelliInvestRequest.getRequestData());
		}else if(intelliInvestRequest.getRequestType().equals(Constants.TRADING_ACCOUNT + Constants.ADD)){
			intelliInvestResponse.setResponseType(Constants.TRADING_ACCOUNT + Constants.ADD);
			responseData = TradingAccountDao.getInstance().addTradingAccountData(user, (ArrayList<TradingAccountData>)intelliInvestRequest.getRequestData());
		}else if(intelliInvestRequest.getRequestType().equals(Constants.USER_DETAIL + Constants.ADD)){
			intelliInvestResponse.setResponseType(Constants.USER_DETAIL + Constants.ADD);
			responseData = UserDetailDao.getInstance().addUserDetail(user, (ArrayList<UserDetailData>)intelliInvestRequest.getRequestData());
		}else if(intelliInvestRequest.getRequestType().equals(Constants.CHAT + Constants.ADD)){
			intelliInvestResponse.setResponseType(Constants.CHAT + Constants.ADD);
			responseData = ChatDao.getInstance().addChatData(user, (ArrayList<ChatData>)intelliInvestRequest.getRequestData());
		}else if(intelliInvestRequest.getRequestType().equals(Constants.POST + Constants.ADD)){
			intelliInvestResponse.setResponseType(Constants.POST + Constants.ADD);
			responseData = BlogsDao.getInstance().insertBlogDetails(user, (ArrayList<PostData>)intelliInvestRequest.getRequestData());
		}else if(intelliInvestRequest.getRequestType().equals(Constants.COMMENT + Constants.ADD)){
			intelliInvestResponse.setResponseType(Constants.COMMENT + Constants.ADD);
			responseData = BlogsDao.getInstance().insertComments(user, (ArrayList<CommentData>)intelliInvestRequest.getRequestData());
		}
		intelliInvestResponse.setResponseData(new ArrayList<IntelliInvestData>(responseData));
		logger.info(intelliInvestRequest.getRequestType() + " for user " + user +" updated result of size " + intelliInvestResponse.getResponseData().size());
		return intelliInvestResponse;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public IntelliInvestResponse remove(IntelliInvestRequest intelliInvestRequest) {
		 String user = intelliInvestRequest.getRequestAttributes().get("userId");
		 IntelliInvestResponse intelliInvestResponse = new IntelliInvestResponse();
		 List<? extends IntelliInvestData> responseData = new ArrayList<IntelliInvestData>();
		 logger.info("Inside IntelliInvestDSServlet ..." + intelliInvestRequest.getRequestType() + " for user " + user + " with request size " + intelliInvestRequest.getRequestData().size());
		 if(intelliInvestRequest.getRequestType().equals(Constants.SIMULATION + Constants.REMOVE)){
			intelliInvestResponse.setResponseType(Constants.SIMULATION + Constants.REMOVE);
			responseData = SimulationDao.getInstance().removeSimulationData(user, (ArrayList<SimulationData>)intelliInvestRequest.getRequestData());
		 }else if(intelliInvestRequest.getRequestType().equals(Constants.TRADING_ACCOUNT + Constants.REMOVE)){
			intelliInvestResponse.setResponseType(Constants.TRADING_ACCOUNT + Constants.REMOVE);
			responseData = TradingAccountDao.getInstance().removeTradingAccountData(user, (ArrayList<TradingAccountData>)intelliInvestRequest.getRequestData());
		 }else if(intelliInvestRequest.getRequestType().equals(Constants.USER_DETAIL + Constants.REMOVE)){
			intelliInvestResponse.setResponseType(Constants.USER_DETAIL + Constants.REMOVE);
			responseData = UserDetailDao.getInstance().removeUserDetail(user, (ArrayList<UserDetailData>)intelliInvestRequest.getRequestData());
		 }
		intelliInvestResponse.setResponseData(new ArrayList<IntelliInvestData>(responseData));
		logger.info(intelliInvestRequest.getRequestType() + " for user " + user +" updated result of size " + intelliInvestResponse.getResponseData().size());
		return intelliInvestResponse;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public IntelliInvestResponse update(IntelliInvestRequest intelliInvestRequest) {
		 String user = intelliInvestRequest.getRequestAttributes().get("userId");
		 IntelliInvestResponse intelliInvestResponse = new IntelliInvestResponse();
		 List<? extends IntelliInvestData> responseData = new ArrayList<IntelliInvestData>();
		 logger.info("Inside IntelliInvestDSServlet ..." + intelliInvestRequest.getRequestType() + " for user " + user + " with request size " + intelliInvestRequest.getRequestData().size());
		 if(intelliInvestRequest.getRequestType().equals(Constants.MANAGE_PORTFOLIO + Constants.UPDATE)){
			intelliInvestResponse.setResponseType(Constants.MANAGE_PORTFOLIO + Constants.UPDATE);
			responseData = ManagePortfolioDao.getInstance().updateManagePortfolioData(user, (ArrayList<ManagePortfolioData>)intelliInvestRequest.getRequestData());
			List<ManagePortfolioData> changedData = ManagePortfolioDao.getInstance().getManagePortfolioData(user, (ArrayList<ManagePortfolioData>)responseData);
			HashMap<String, IntelliInvestData> changedResponseData = new HashMap<String, IntelliInvestData>();
			for(IntelliInvestData intelliInvestData : changedData){
				ManagePortfolioData managePortfolioData = (ManagePortfolioData)intelliInvestData;
				changedResponseData.put(managePortfolioData.getId(), managePortfolioData);
			}
			intelliInvestResponse.getScreenData().put("CHANGED", changedResponseData);
			intelliInvestResponse.getScreenData().put("SUMMARY", ManagePortfolioDao.getInstance().getManagePortfolioSummaryData(changedData));
		}else if(intelliInvestRequest.getRequestType().equals(Constants.NEW_MANAGE_PORTFOLIO + Constants.UPDATE)){
			intelliInvestResponse.setResponseType(Constants.NEW_MANAGE_PORTFOLIO + Constants.UPDATE);
			String code = intelliInvestRequest.getRequestAttributes().get("code");
			if(code.equalsIgnoreCase("SUMMARY")){
				List<ManagePortfolioData> managePortfolioDatas = ManagePortfolioDao.getInstance().addManagePortfolioData(user, (ArrayList<ManagePortfolioData>)intelliInvestRequest.getRequestData());
				List<ManagePortfolioData> changedData = ManagePortfolioDao.getInstance().getManagePortfolioData(user, managePortfolioDatas);
				responseData = new ArrayList<ManagePortfolioData>(ManagePortfolioDao.getInstance().getManagePortfolioSummaryData(changedData).values());
			}else{
				List<ManagePortfolioData> managePortfolioDatas = ManagePortfolioDao.getInstance().updateManagePortfolioData(user, (ArrayList<ManagePortfolioData>)intelliInvestRequest.getRequestData());
				ManagePortfolioDao.getInstance().populatePnl(managePortfolioDatas);
				responseData = managePortfolioDatas;
				
				List<ManagePortfolioData> changedData = ManagePortfolioDao.getInstance().getManagePortfolioData(user, (ArrayList<ManagePortfolioData>)responseData);
				intelliInvestResponse.getScreenData().put("SUMMARY", ManagePortfolioDao.getInstance().getManagePortfolioSummaryData(changedData));
			}
		}else if(intelliInvestRequest.getRequestType().equals(Constants.USER_DETAIL + Constants.UPDATE)){
			intelliInvestResponse.setResponseType(Constants.USER_DETAIL + Constants.UPDATE);
			responseData = UserDetailDao.getInstance().updateUserDetail(user, (ArrayList<UserDetailData>)intelliInvestRequest.getRequestData());
		}else if(intelliInvestRequest.getRequestType().equals(Constants.POST + Constants.UPDATE)){
			intelliInvestResponse.setResponseType(Constants.POST + Constants.UPDATE);
			responseData = BlogsDao.getInstance().updateBlogsData(user, (ArrayList<PostData>)intelliInvestRequest.getRequestData());
		}
		intelliInvestResponse.setResponseData(new ArrayList<IntelliInvestData>(responseData));
		logger.info(intelliInvestRequest.getRequestType() + " for user " + user +" updated result of size " + intelliInvestResponse.getResponseData().size());
		return intelliInvestResponse;
	}
	
}
