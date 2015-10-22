package com.intelliinvest.client.util;

import java.util.Date;

@SuppressWarnings("deprecation")
public class DateUtil {
	public static Long MILLI_SEC_DAY = new Long(24*60*60*1000);
	public static Long MILLI_SEC_HOURS = new Long(60*60*1000);
	public static Long MILLI_SEC_MINUTES = new Long(60*1000);
	public static Long MILLI_SEC_SECONDS = new Long(1000);
	
	public static String getMMDDYYYYDate(Date date){
		if(null == date){
			return "";
		}
		return (date.getMonth()+1) + "/" + date.getDate() + "/" + (date.getYear()+1900);
	}
	
	public static String getDDMMYYYYDate(Date date){
		if(null == date){
			return "";
		}
		return date.getDate() + "/" +  (date.getMonth()+1)+ "/" + (date.getYear()+1900);
	}
	
	public static int mmddDateCompare(Date date1, Date date2){
		if(date1.getMonth() > date2.getMonth()){
			return 1;
		}else if(date1.getMonth() < date2.getMonth()){
			return -1;
		}else{
			if(date1.getDate()>date2.getDate()){
				return 1;
			}else if(date1.getDate()<date2.getDate()){
				return -1;
			}else{
				return 0;
			}
		}
	}
	
	public static Date getDateFromMMDD(String mmdd){
		Date d = new Date(getMMDDYYYYDate(new Date()));
		if(mmdd.length()==3){
			d.setMonth(new Integer(mmdd.substring(0,1))-1);
			d.setDate(new Integer(mmdd.substring(1)));
		}else{
			d.setMonth(new Integer(mmdd.substring(0,2))-1);
			d.setDate(new Integer(mmdd.substring(2)));
		}
		return d;
	}
	
	public static Integer ddDateDiff(Date startDate, Date endDate){
		Long diff = endDate.getTime()-startDate.getTime();
		return new Long((diff/MILLI_SEC_DAY)).intValue();
	}

	public static boolean isSameDate(Date date1,int days, Date date2){
		return addDate(date1,days).getTime()==date2.getTime();
	}
	
	public static Date addDate(Date date1, int days){
		return new Date(date1.getTime()+(days*MILLI_SEC_DAY));
	}
	public static Date getDate(Long dateL){
		try{
			if(dateL==-1){
				return new Date(dateL);
			}
			return new Date(dateL);
		}catch (Exception e) {
			return new Date();
		}
	}
	public static Date getDate(String dateStr){
		if(null==dateStr || dateStr.equals("")){
			return null;
		}
		Date date = null;
		try{
			if(dateStr.length()==8 && !dateStr.contains("/")){
				date = new Date(new Integer(dateStr.substring(0,4))-1900,new Integer(dateStr.substring(4,6))-1,new Integer(dateStr.substring(6,8)));
			}else if(dateStr.length()==14){
				date = new Date(new Integer(dateStr.substring(0,4))-1900,
						new Integer(dateStr.substring(4,6))-1,
						new Integer(dateStr.substring(6,8)), new Integer(dateStr.substring(9,11)), new Integer(dateStr.substring(12,14)));
			} else if(dateStr.length()==17){
                date = new Date(new Integer(dateStr.substring(0,4))-1900,
                        new Integer(dateStr.substring(4,6))-1,
                        new Integer(dateStr.substring(6,8)), 
                        new Integer(dateStr.substring(9,11)),
                        new Integer(dateStr.substring(12,14)),
                        new Integer(dateStr.substring(15,17)));
			}  else{
				date = new Date(dateStr);
			}			
		}catch (Exception e) {
//			Log.error(" Error converting date " + dateStr);
		}
		return date;
	}
	
	public static String getYYYYMMDDDate(Date date){
		if(null == date){
			return "";
		}
		String month="",day="";
		if (date.getMonth()+1<10)
			month="0"+(date.getMonth()+1);
		else
			month=""+(date.getMonth()+1);
		if (date.getDate()<10)
			day="0"+(date.getDate());
		else
			day=""+(date.getDate());
		
		return ""+(date.getYear()+1900) + month+day;
	}
	
	public static String getYYYYMMDate(Date date){
		if(null == date){
			return "";
		}
		String month="";
		if (date.getMonth()+1<10)
			month="0"+(date.getMonth()+1);
		else
			month=""+(date.getMonth()+1);
		
		return ""+(date.getYear()+1900) + month;
	}
	
	public static String getYYYYMMDDHHMMSSDate(Date date){
		if(null == date){
			return "";
		}
		String month="",day="", hour="", sec="",min="";
		if (date.getMonth()+1<10)
			month="0"+(date.getMonth()+1);
		else
			month=""+(date.getMonth()+1);
		if (date.getDate()<10)
			day="0"+(date.getDate());
		else
			day=""+(date.getDate());
		if (date.getHours()<10)
			hour="0"+(date.getHours());
		else
			hour=""+(date.getHours());
		if (date.getMinutes()<10)
			min="0"+(date.getMinutes());
		else
			min=""+(date.getMinutes());		
		if (date.getSeconds()<10)
			sec="0"+(date.getSeconds());
		else
			sec=""+(date.getSeconds());
		
		return ""+(date.getYear()+1900) + month+day + " " + hour + ":"+min+":" + sec;
	}
}
