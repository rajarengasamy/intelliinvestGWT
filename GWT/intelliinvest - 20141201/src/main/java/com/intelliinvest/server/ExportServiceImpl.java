package com.intelliinvest.server;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.intelliinvest.client.util.DateUtil;
import com.intelliinvest.server.util.ExceptionUtil;

@SuppressWarnings("serial")
public class ExportServiceImpl extends HttpServlet{
	private static Logger s_log = Logger.getLogger(ExportServiceImpl.class);
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ServletOutputStream op = null;
		DataInputStream in = null;
		BufferedReader r = null;
		try{
			ArrayList<String> titles = new ArrayList<String>();
			ArrayList<String> fields = new ArrayList<String>();
			ArrayList<String> types = new ArrayList<String>();
			HSSFWorkbook wb = new HSSFWorkbook();
	        HSSFSheet sheet = wb.createSheet();
	        int k=0;
			r = new BufferedReader (new InputStreamReader(req.getInputStream(), "UTF8"));
					
			ByteArrayOutputStream o = new ByteArrayOutputStream();
			String str= "";
			int totalColumns=0;
			while((str = r.readLine())!=null){
				if(str.startsWith("-------------------") || str.contains("Content-Disposition:") 
						|| str.equals("")){
					
				}else if(str.contains("fields:")){
					String[] fieldsTmp = str.replace("fields:", "").split("\\^\\^\\^");
					totalColumns=fieldsTmp.length;
					for (int i = 0; i < fieldsTmp.length; i++) {
						String[] fieldsArray =  fieldsTmp[i].split("###");
							titles.add(fieldsArray[0]);
							fields.add(fieldsArray[1]);
							types.add(fieldsArray[2]);
					}
					HSSFRow row = sheet.createRow(k);
					k++;
					for (int i = 0; i < titles.size(); i++) {
						HSSFCell cell = row.createCell(i);
						if(!"\t".equals(titles.get(i)))
							cell.setCellValue(new HSSFRichTextString(titles.get(i)));
					}
				}else{
					if(null!=str){
				        	HSSFRow row = sheet.createRow(k);
				        	k++;
				        	//String pairs[] = str.split(",");
				        	String values[] = str.split("~~~");
				        	
							for (int j = 0; j < values.length; j++) {
								HSSFCell cell = row.createCell(j);
								try{
									if("".equals(types.get(j).trim()) || types.get(j).equals("TEXT") || types.get(j).equals("SEQUENCE")){
										cell.setCellValue(new HSSFRichTextString(values[j]));
									}else if(types.get(j).equals("BOOLEAN")){
										cell.setCellValue(new Boolean(values[j]).booleanValue());
									}else if(types.get(j).equals("INTEGER")){
										cell.setCellValue(new Integer(values[j]).intValue());
									}else if(types.get(j).equals("FLOAT")){
										cell.setCellValue(new Double(values[j]).doubleValue());
									}else if(types.get(j).equals("DATE")){
										cell.setCellValue(DateUtil.getDate(values[j]));
									}else{
										cell.setCellValue(new HSSFRichTextString(values[j]));
									}
								}catch (Exception e) {
									cell.setCellValue(new HSSFRichTextString(values[j]));
								}
							}				       
					}
				}
			}
			for (int i=0;i<totalColumns;i++){
				sheet.autoSizeColumn((short)i);
			}
	        wb.write(o);
			ByteArrayInputStream i = new ByteArrayInputStream(o.toByteArray());
			op = resp.getOutputStream();
            resp.setContentType("application/octet-stream"); 
            resp.setContentLength((int) o.size()); 
            resp.setHeader("Content-Disposition", "attachment; filename= export.xls"); 
            int length = 0; 
            byte[] bbuf = new byte[1024]; 
            in = new DataInputStream(i); 
            while ((in != null) && ((length = in.read(bbuf)) != -1)) { 
                op.write(bbuf, 0, length); 
            } 

		}catch (Exception e) {
			s_log.error(ExceptionUtil.getErrorStack(e));
		}finally{
			if(r!=null){
				r.close();
			}
			if(in!=null){
				in.close(); 
			}
			if(op!=null){
		        op.flush(); 
		        op.close(); 
			}
	        
		}
	}	
	
}
