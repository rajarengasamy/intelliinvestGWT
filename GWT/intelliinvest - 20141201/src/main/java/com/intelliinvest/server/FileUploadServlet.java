package com.intelliinvest.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import com.intelliinvest.server.dao.ChartDao;
import com.intelliinvest.server.dao.IntelliInvestDataDao;
import com.intelliinvest.server.dao.OurSuggestionDao;
import com.intelliinvest.server.dao.RiskReturnMatrixDao;
import com.intelliinvest.server.dao.StockDetailsDao;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class FileUploadServlet extends HttpServlet {
	
	private static Logger logger = Logger.getLogger(FileUploadServlet.class);
	
	private boolean isMultipart;
	private int maxFileSize = 50 *1000* 1024;
	private int maxMemSize = 50 *1000* 1024;

	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		String message = "";
		isMultipart = ServletFileUpload.isMultipartContent(request);
		response.setContentType("text/html");
		java.io.PrintWriter out = response.getWriter();
		if (!isMultipart) {
			out.println("<html><head><script>alert('No file uploaded' );</script></head></html>");
			return;
		}
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// maximum size that will be stored in memory
		factory.setSizeThreshold(maxMemSize);
		// Location to save data that is larger than maxMemSize.
		factory.setRepository(new File("c:\\temp"));

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);
		// maximum file size to be uploaded.
		upload.setSizeMax(maxFileSize);

		try {
			// Parse the request to get file items.
			List<FileItem> fileItems = upload.parseRequest(request);

			// Process the uploaded file items
			Iterator<FileItem> i = fileItems.iterator();
			String fileNames = "";
			while (i.hasNext()) {
				FileItem fi = i.next();
				if (!fi.isFormField()) {
					String fileName = fi.getName();
					List<String> lines = new ArrayList<String>();
					int lineCount=0;
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fi.getInputStream()));
					String stockDetail = "";
					while(null!=(stockDetail=bufferedReader.readLine())){
						if(lineCount==0){
							lineCount++;
							continue;
						}
						lines.add(stockDetail);
					}
					if(fileName.startsWith("stockdetails")){
						StockDetailsDao.getInstance().insertStockDetails(lines);
					}else if(fileName.startsWith("suggestions")){
						OurSuggestionDao.getInstance().insertSuggestions(lines);
					}else if(fileName.startsWith("optionsuggestions")){
						OurSuggestionDao.getInstance().insertOptionSuggestions(lines);
					}else if(fileName.startsWith("intelliinvest")){
						IntelliInvestDataDao.getInstance().updateIntelliInvestDataDetails(lines);
					}else if(fileName.startsWith("riskreturn")){
						RiskReturnMatrixDao.getInstance().updateRiskReturnMatrixDetails(lines);
					}else if(fileName.startsWith("signals")){
						ChartDao.getInstance().insertSignals(lines);
					}else{
						message = message + "<br>Not able to recognise given file for upload<br>";
					}
					fileNames = fileNames + "," + fileName;
				}
			}
			IntelliInvestStore.refresh();
			message = message + "Files uploaded succesfully : " + fileNames.substring(1);
		} catch (Exception ex) {
			logger.info("Error in file upload ", ex);
			message =  message + "Error uploading file " + ex.getMessage() + ". Check logs fro details.";
		}
		out.println("<html>");
		out.println("<head>");
		out.println(" <script> alert('" + message +"' );    </script> ");
		out.println("</head>");
		out.println("</html>");
	}
	
}
