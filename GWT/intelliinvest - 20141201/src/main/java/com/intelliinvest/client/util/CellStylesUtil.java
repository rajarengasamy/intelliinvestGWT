package com.intelliinvest.client.util;

import com.google.gwt.i18n.client.NumberFormat;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class CellStylesUtil {
	public static CellFormatter NUMBER_CELL_FORMATTER = new CellFormatter() {
		@Override
		public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
			if(null==value || "".equals(value.toString()) ){
				return "0.00";
			}
			NumberFormat nf = NumberFormat.getFormat("##,##,##,##,##,##0.00");
			try {
				if(((Number) value).doubleValue()==0D){
					return "0";
				}
				return nf.format(((Number) value).doubleValue());
			} catch (Exception e) {
				return value.toString();
			}
		}
	};
	
	public static CellFormatter NUMBER_CELL_FORMATTER_4 = new CellFormatter() {
		@Override
		public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
			if(null==value || "".equals(value.toString()) ){
				return "0.0000";
			}
			NumberFormat nf = NumberFormat.getFormat("##,##,##,##,##,##0.0000");
			try {
				if(((Number) value).doubleValue()==0D){
					return "0";
				}
				return nf.format(((Number) value).doubleValue());
			} catch (Exception e) {
				return value.toString();
			}
		}
	};
	
	public static CellFormatter QUANTITY_CELL_FORMATTER = new CellFormatter() {
		@Override
		public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
			if(null==value || "".equals(value.toString()) ){
				return "0";
			}
			NumberFormat nf = NumberFormat.getFormat("##,##,##,##,##,##0");
			try {
				return nf.format(((Number) value).doubleValue());
			} catch (Exception e) {
				return value.toString();
			}
		}
	};
	
	
	public static CellFormatter PERCENTAGE_CELL_FORMATTER = new CellFormatter() {
		@Override
		public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
			if(null==value || "".equals(value.toString()) ){
				return "0.00%";
			}
			NumberFormat nf = NumberFormat.getFormat("##0.00%");
			try {
				return nf.format(((Number) value).doubleValue());
			} catch (Exception e) {
				return value.toString();
			}
		}
	};
	
	public static String getPositiveNegativeCSSFormatter(Record record, String fieldName) {
		String fieldValue = record.getAttribute(fieldName);
		if(null==fieldValue || "".equals(fieldValue)){
			System.out.println("taking from null " + fieldName);
			return "font-weight:bold; color:green";   
		}else{
			try {
				if (new Double(record.getAttribute(fieldName)).doubleValue() < 0D) {  
		            return "font-weight:bold; color:red";  
		        } else {  
		        	return "font-weight:bold; color:green";  
		        } 
			}catch (Exception e) {
				return "";
			}
		}
	}
	
	public static String getPositiveNegativeWithChartCSSFormatter(Record record, String fieldName) {
		String fieldValue = record.getAttribute(fieldName);
		if(null==fieldValue || "".equals(fieldValue)){
			return "font-weight:bold; color:green;cursor:hand;";   
		}else{
			try {
				if (new Double(record.getAttribute(fieldName)).doubleValue() < 0D) {  
		            return "font-weight:bold; color:red;cursor:hand;";  
		        } else {  
		        	return "font-weight:bold; color:green;cursor:hand;";  
		        } 
			}catch (Exception e) {
				return "";
			}
		}
	}
	
	public static String getBuySellCSSFormatter(Record record, String fieldName) {
		String fieldValue = record.getAttribute(fieldName);
		if(null==fieldValue || "".equals(fieldValue)){
			return "font-weight:bold; color:green";   
		}else{
			if (record.getAttribute(fieldName).toUpperCase().contains("WAIT")) { 
				return "font-weight:bold; color:orange"; 
	        }else if (record.getAttribute(fieldName).toUpperCase().contains("BUY")) {  
	            return "font-weight:bold;  color:green";  
	        } else if (record.getAttribute(fieldName).toUpperCase().contains("HOLD")) {  
	        	return "font-weight:bold; color:lime";
	        }if (record.getAttribute(fieldName).toUpperCase().contains("SELL")) {  
	            return "font-weight:bold; color:red";  
	        }else {  
	        	return "font-weight:bold;"; 
	        } 
		}
         
	}
}
