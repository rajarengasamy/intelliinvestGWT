package com.intelliinvest.client.util;

import java.math.BigDecimal;

import com.google.gwt.i18n.client.NumberFormat;

public class ConverterUtils {
	static String[] zeros = {"","0","00","000","0000","00000","000000"};
	public static String NumberToString(Number value){
		return NumberToString(value, 3);
	}
	public static String NumberToString(Number value, Integer multiplier){
		return NumberToString(value, multiplier, false);
	}
	
	public static String NumberToString(Number value, Integer multiplier, boolean up){
		if(null==value){
			return "0";
		}else{
			if(value instanceof Double){
				String valueD = value.toString();
				BigDecimal valueB = new BigDecimal(valueD).setScale(multiplier+3, BigDecimal.ROUND_UP);
				if(up){
					BigDecimal b = valueB.divide(BigDecimal.ONE, 0, BigDecimal.ROUND_DOWN);
					BigDecimal b1 = valueB.remainder(BigDecimal.ONE).setScale(multiplier, BigDecimal.ROUND_UP);
					if(b1.doubleValue()==0D){
						return b.toString();
					}
					return b.add(b1).toString();
				}else{
					BigDecimal b = valueB.divide(BigDecimal.ONE, 0, BigDecimal.ROUND_DOWN);
					BigDecimal b1 = valueB.remainder(BigDecimal.ONE).setScale(multiplier, BigDecimal.ROUND_DOWN);
					if(b1.doubleValue()==0D){
						return b.toString();
					}
					return b.add(b1).toString();
				}
			}else if(value instanceof Float)
				return value.floatValue()+"";
			else if(value instanceof Integer)
				return value.intValue()+"";
			else
				return value.toString();
		}
	}
	
	public static Double calculateMin(Double d1, Double d2, Double d3, Double d4, Double d5){
		return calculateMin(calculateMin(d1, d2, d3,d4), d5);
	}
	
	public static Double calculateMin(Double d1, Double d2, Double d3, Double d4){
		return calculateMin(calculateMin(d1, d2, d3), d4);
	}
	
	public static Double calculateMin(Double d1, Double d2, Double d3){
		return calculateMin(calculateMin(d1, d2), d3);
	}
	public static Double calculateMin(Double d1, Double d2){
		if(d1 < d2){
			return d1;
		}else{
			return d2;
		}
	}
	public static Double calculateMax(Double d1, Double d2){
		if(d1 > d2){
			return d1;
		}else{
			return d2;
		}
	}	
	public static Double getMultiplesOfMinimum(Double value, Double min){
		if (min == 0) {
			return value;
		}
		else {
			Double pledgedValue = new Double((value.longValue() / min.longValue()) * min.longValue());
			return pledgedValue;
		}
	}
	public static String formatCurrency(Object value){
		if (value == null) return null;   
	    try {  
	        NumberFormat nf = NumberFormat.getFormat("#,##0.###"); 
	        double t0FaceVal = new Double(value.toString()).doubleValue();
	        if(t0FaceVal<0.0){
	        	return "<font color='red'>" + nf.format(t0FaceVal) + "</font>";
	        }
	        return nf.format(t0FaceVal)+"";   
	    } catch (Exception e) {   
	        return value.toString();   
	    }  
	}
	public static String getCurrencyValue(Object value){
		if (value == null) return null;   
	    try {  
	    	 return value.toString().replace(",", "").replace("<font color='red'>", "").replace("</font>", "");  
	    } catch (Exception e) {   
	        return value.toString();   
	    }  
	}
	
	public static Double round(Double value, Integer decimalPlace){
		BigDecimal b = new BigDecimal(value.toString()).setScale(decimalPlace+3, BigDecimal.ROUND_UP).setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return b.doubleValue();
	}
	
	public static Double roundUp(Double value, Integer decimalPlace){
		BigDecimal b = new BigDecimal(value.toString()).setScale(decimalPlace+3, BigDecimal.ROUND_UP).setScale(decimalPlace, BigDecimal.ROUND_UP);
		return b.doubleValue();
	}
	public static Double roundDown(Double value, Integer decimalPlace){
		BigDecimal b = new BigDecimal(value.toString()).setScale(decimalPlace+3, BigDecimal.ROUND_UP).setScale(decimalPlace, BigDecimal.ROUND_DOWN);
		return b.doubleValue();
	}
	
}