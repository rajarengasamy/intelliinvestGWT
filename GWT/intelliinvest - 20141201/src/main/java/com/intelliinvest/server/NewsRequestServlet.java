package com.intelliinvest.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.intelliinvest.client.data.StockDetailStaticHolder;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class NewsRequestServlet extends HttpServlet {

	private static Logger logger = Logger.getLogger(NewsRequestServlet.class);
	
	static String YAHOO_NEWS_URL = "http://query.yahooapis.com/v1/public/yql?q=select%20link%2Ctitle%2Cdescription%2CpubDate%20from%20rss%20where%20url%3D%22http%3A%2F%2Ffeeds.finance.yahoo.com%2Frss%2F2.0%2Fheadline%3Fs%3D#CODE#%26region%3DUS%26lang%3Den-US%22&diagnostics=true";
	static String YAHOO_TOP_STORIES_NEWS_URL = "http://query.yahooapis.com/v1/public/yql?q=select%20link%2Ctitle%2Cdescription%2CpubDate%20from%20rss%20where%20url%3D%22http%3A%2F%2Frss.news.yahoo.com%2Frss%2Ftopstories%22&diagnostics=true";
	
	static String GOOGLE_NEWS_URL = "https://www.google.com/finance/company_news?q=#EXCHANGE#:#CODE#&output=rss";
	static String GOOGLE_TOP_STORIES_NEWS_URL ="https://news.google.com/news/feeds?pz=1&cf=all&ned=in&hl=en&output=rss&topic=b";
			
	public static String NEWS_FROM="GOOGLE";
	
	static String TOP_STORIES = "";
	
	static{
		setTopStories();
	}
	
	public static void setTopStories() {
		try{
//			logger.info("Fetching top stories");
			if(NEWS_FROM.equalsIgnoreCase("GOOGLE")){
				TOP_STORIES = HttpUtil.getFromHttpUrlAsString(GOOGLE_TOP_STORIES_NEWS_URL);
			}else{
				TOP_STORIES = HttpUtil.getFromHttpUrlAsString(YAHOO_TOP_STORIES_NEWS_URL);
			}
//			logger.info("Fetched top stories");
			Timer timerForIntelliInvestData = new Timer("refreshTimer");
			timerForIntelliInvestData.schedule(new TimerTask() {
				@Override
				public void run() {
					try{
//						logger.info("Refreshing top stories");
						if(NEWS_FROM.equalsIgnoreCase("GOOGLE")){
							TOP_STORIES = HttpUtil.getFromHttpUrlAsString(GOOGLE_TOP_STORIES_NEWS_URL);
						}else{
							TOP_STORIES = HttpUtil.getFromHttpUrlAsString(YAHOO_TOP_STORIES_NEWS_URL);
						}
//						logger.info("Refreshed top stories");
					}catch(Exception e){
						logger.info("Error refreshing top stories " + e.getMessage());
						TOP_STORIES = "<item><title> Error fetching top stories</title></item>";
					}
				}
			}, new Integer(IntelliInvestStore.properties.getProperty("news.refresh.interval")), new Integer(IntelliInvestStore.properties.getProperty("news.refresh.interval")));
			
		}catch(Exception e){
			logger.info("Error itializing top stories "  + e.getMessage());
			TOP_STORIES = "<item><title> Error fetching top stories</title></item>";
		}
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String stockCode = req.getParameter("stockCode");
		logger.info("Fetching news for " + stockCode + " ");
		if(NEWS_FROM.equalsIgnoreCase("GOOGLE")){
			String news = getNewsFromGoogle(stockCode);
			resp.getOutputStream().write(news.getBytes());
		}else{
			String news = getNewsFromYahoo(stockCode);
			resp.getOutputStream().write(news.getBytes());
		}
		
	}

	private String getNewsFromYahoo(String stockCode)
			throws CharacterCodingException {
		String yahooStockCode = IntelliInvestUtil.getYahooCode(stockCode);
		
		String response = "";
		try{
			if(null!=stockCode && !"".equals(stockCode)){
				String url = YAHOO_NEWS_URL.replaceAll("#CODE#", yahooStockCode.replace("&", "&amp;"));
				response = HttpUtil.getFromHttpUrlAsString(url);
				if(response.contains("<title>Yahoo! Finance: RSS feed not found</title>")){
					response = "<item><title> No news found for stock " + stockCode + "</title></item>";
				}
			}else{
				response = TOP_STORIES; 
			}
		}catch(Exception e){
			logger.info("Error retrieving top stories  " + e.getMessage());
			response = "<item><title> Error fetching news for " + stockCode + "</title></item>";
		}
		
		CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
		decoder.onMalformedInput(CodingErrorAction.REPLACE);
        decoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
        ByteBuffer bb = ByteBuffer.wrap(response.getBytes());
        CharBuffer correctedResponse = decoder.decode(bb);
        logger.info("response received");
		return correctedResponse.toString();
	}
	
	private String getNewsFromGoogle(String stockCode) throws CharacterCodingException {
		String response = "";
		try{
			if(null!=stockCode && !"".equals(stockCode)){
				String exchange = "NSE";
        		if(StockDetailStaticHolder.BOMStocksSet.contains(stockCode)){
        			exchange = "BOM"; 
        			stockCode = StockDetailStaticHolder.NSEToBSEMap.get(stockCode);
        		}
				String url = GOOGLE_NEWS_URL.replace("#CODE#", stockCode.replace("&", "%26")).replace("#EXCHANGE#", exchange);
				response = HttpUtil.getFromHttpUrlAsString(url);
			}else{
				response = TOP_STORIES; 
			}
		}catch(Exception e){
			logger.info("Error retreiving new from google  " + e.getMessage());
			response = "<item><title> Error fetching news for " + stockCode + "</title></item>";
		}
		
		CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
		decoder.onMalformedInput(CodingErrorAction.REPLACE);
        decoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
        ByteBuffer bb = ByteBuffer.wrap(response.getBytes());
        CharBuffer correctedResponse = decoder.decode(bb);
        logger.info("response received");
		return correctedResponse.toString();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
}
