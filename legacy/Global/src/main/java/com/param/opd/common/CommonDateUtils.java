package com.param.opd.common;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import com.param.global.common.ServiceException;
import com.param.utility.TimeZoneComponent;

public class CommonDateUtils {
	private static final Logger logger = Logger.getLogger(CommonDateUtils.class);
	/*@Autowired
	private SessionObject sessionObject;*/


	// static String zoneDesc = sessionObject.getZoneDesc();
	static String currentTimezone = "";
	static{
		Calendar cal = Calendar.getInstance();
	      long milliDiff = cal.get(Calendar.ZONE_OFFSET);
	      // Got local offset, now loop through available timezone id(s).
	      String [] ids = TimeZone.getAvailableIDs();
	      for (String id : ids) {
	        TimeZone tz = TimeZone.getTimeZone(id);
	        if (tz.getRawOffset() == milliDiff) {
	          // Found a match.
	        	currentTimezone = id;
	          break;
	        }
	      }
	      //System.out.println(currentTimezone);
	}

	public static Date getDate(String dateString , String pattern) {
		SimpleDateFormat format = null;
		Date date1 = null;
		try {
			format = new SimpleDateFormat(pattern);
			date1 = format.parse(dateString);
			/*//System.out.println("dateeee===="+date1);*/
		} catch (ParseException e) {
			logger.error("getDate failed", e);
		}
		return date1;
	}

	    public static int calculateAge(LocalDate birthDate) {
	            return Period.between(birthDate, LocalDate.now()).getYears();
	    }

	public static String getStringDate(Date dateDate , String pattern) {
		SimpleDateFormat format = null;
		String strDate = null;
		format = new SimpleDateFormat(pattern);
		strDate = format.format(dateDate);
		return strDate;
	}

	public static  Date getDate(Date dateDate , String pattern) {
		SimpleDateFormat format = null;
		Date date2 = null;
		String strDate = null;
		try {
			format = new SimpleDateFormat(pattern);
			strDate = format.format(dateDate);
			date2 = format.parse(strDate);
			////System.out.println(strDate+"__"+date2);
		} catch (ParseException e) {
			logger.error("getDate failed", e);
		}
		return date2;
	}


	public static  String getAvalDate(Date dateDate , String pattern) {
		SimpleDateFormat format = null;
		Date date2 = null;
		String strDate = null;
		try {
			format = new SimpleDateFormat(pattern);
			strDate = format.format(dateDate);
			date2 = format.parse(strDate);
			////System.out.println(strDate+"__"+date2);
		} catch (ParseException e) {
			logger.error("getAvalDate failed", e);
		}
		return strDate;
	}
	public static  Date getTime(Date dateDate , String pattern) {
		SimpleDateFormat format = null;
		Date date2 = null;
		String strDate = null;
		try {
			format = new SimpleDateFormat(pattern);
			strDate = format.format(dateDate);
			date2 = format.parse(strDate);
			//System.out.println(strDate+"__"+date2);
		} catch (ParseException e) {
			logger.error("getTime failed", e);
		}
		return date2;
	}

	/*public static Date getDate(String dateDate, String fromPattern, String toPatteren) {
		SimpleDateFormat format = null;
		Date date = null;
		String strDate = null;
		try {
			format = new SimpleDateFormat(fromPattern);
			date   = format.parse(dateDate);
			//System.out.println(date);

			format = new SimpleDateFormat(toPatteren);
			strDate = format.format(date);
			date   = format.parse(strDate);
			//System.out.println(strDate);


		} catch (Exception e) {

		}
		return date;
	}*/

	//------------------------------------------------------------------------------------------------//

	public static String getFirstDateOfMonth(Date date) {
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(date);
			c.set(Calendar.DAY_OF_MONTH, 1);

		} catch (Exception e) {
			logger.error("getFirstDateOfMonth failed", e);
		}
		return getStringDate(c.getTime(), "dd-MM-yyyy");
	}

	public static String getLastDateOfMonth(Date date) {
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(date);
			c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));

		} catch (Exception e) {
			logger.error("getLastDateOfMonth failed", e);
		}
		return getStringDate(c.getTime(), "dd-MM-yyyy");
	}

	/*public static String getFirstDateOfMonth(int month, SessionObject sessionObject ) {
		Calendar cal = Calendar.getInstance();
		GregorianCalendar gc = null;
		Date startDate = null;
		Date currDate = null;
		try {
			gc = new GregorianCalendar(cal.get(Calendar.YEAR), month-1, 1);
			startDate = gc.getTime();
			currDate = getDate(getDateByzone(sessionObject.getZoneDesc()), "dd-MM-yyyy");
			//System.out.println("===========>"+currDate);
			if(currDate.compareTo(startDate) >= 0)
				return getStringDate(currDate, "dd-MM-yyyy");
			else if(currDate.compareTo(startDate) < 0)
				return getStringDate(startDate, "dd-MM-yyyy");

		} catch (Exception e) {

		}
		return null;//getStringDate(gc.getTime(), "dd-MM-yyyy");
	}*/

	public static String getLastDateOfMonth(int month) {
		Calendar cal = Calendar.getInstance();
		GregorianCalendar gc = null;
		String lastDate = null;
		try {
			gc = new GregorianCalendar(cal.get(Calendar.YEAR), month-1, 1);
			lastDate = getLastDateOfMonth(gc.getTime());
		} catch (Exception e) {
			logger.error("getLastDateOfMonth failed", e);
		}
		return lastDate;
	}

	public static LocalDate getLocalDate(Date date){
		LocalDate localDate = null;
		try {
			localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		} catch (Exception e) {
			logger.error("getLocalDate failed", e);
		}
		return localDate;
	}

	public static Date getDate(LocalDate date){
		Date dateDate = null;
		try {
			dateDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
		} catch (Exception e) {
			logger.error("getDate failed", e);
		}
		return dateDate;
	}

	public static String getStringDate(LocalDate date, String pattern){
		Date dateDate = null;
		String strDate = null;
		try {
			dateDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
			strDate = getStringDate(dateDate, pattern);
		} catch (Exception e) {
			logger.error("getStringDate failed", e);
		}
		return strDate;
	}

	public static int getDayOfWeek(Date date){
		Calendar cal = Calendar.getInstance();
		int DAY_OF_WEEK = 0;
		try {
			cal.setTime(date);
			DAY_OF_WEEK = cal.get(Calendar.DAY_OF_WEEK);
		} catch (Exception e) {
			logger.error("getDayOfWeek failed", e);
		}
		return DAY_OF_WEEK;
	}

	public static int getWeekOfMonth(Date date){
		Calendar cal = Calendar.getInstance();
		int WEEK_OF_MONTH = 0;
		try {
			cal.setTime(date);
			WEEK_OF_MONTH = cal.get(Calendar.WEEK_OF_MONTH);
		} catch (Exception e) {
			logger.error("getWeekOfMonth failed", e);
		}
		return WEEK_OF_MONTH;
	}

	public static LocalTime getLocalTime(String time) {
		LocalTime localTime = null;
		String hhMM[] = null;
		try {
			hhMM = time.split(":");
			localTime = LocalTime.of(Integer.parseInt(hhMM[0]), Integer.parseInt(hhMM[1]));
		} catch (Exception e) {
			logger.error("getLocalTime failed", e);
		}
		return localTime;
	}
	/*public static void main(String args[]){
	     ////System.out.println("Hello Java");
	     //CommonDateUtils.getDate("13/09/2016", "dd/MM/yyyy" ,"yyyy-MM-dd");
		////System.out.println(Status.CHECKEDIN.ordinal());
		//System.out.println(getFirstDateOfMonth(10));
	}*/

	/*public static String getTodaysDate(SessionObject sessionObject){
		Date date = getDateByzone(sessionObject.getZoneDesc());
		try{
			DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
			String strDate = format.format(date);
			return strDate;
		}catch(Exception e){

		}
		return "";
	}*/


 public static void main(String args[]){
	  		/*String DATE_FORMAT = "dd-M-yyyy hh:mm:ss a";
	         String dateInString = "16-11-2016 12:55:00 PM";
	         LocalDateTime ldt = LocalDateTime.parse(dateInString, DateTimeFormatter.ofPattern(DATE_FORMAT));
	         ZoneId currentZone = ZoneId.of(currentTimezone);
	         //System.out.println("TimeZone : " + currentTimezone);

	         //LocalDateTime + ZoneId = ZonedDateTime
	         ZonedDateTime asiaZonedDateTime = ldt.atZone(currentZone);
	         //System.out.println("Date (India) : " + asiaZonedDateTime);

	         ZoneId newIndiaZone = ZoneId.of("Africa/Abidjan");
	         //System.out.println("TimeZone : " + newIndiaZone);

	         ZonedDateTime nyDateTime = asiaZonedDateTime.withZoneSameInstant(newIndiaZone);
	         //System.out.println("Date (New York) : " + nyDateTime);

	         DateTimeFormatter format = DateTimeFormatter.ofPattern(DATE_FORMAT);
	         //System.out.println("\n---DateTimeFormatter---");

	         //System.out.println("Date (kolkata) : " + format.format(nyDateTime));*/
	 		//getDateByzone("ok");
	 ////System.out.println(getStringDateByzone("Asia/Calcutta", "dd-M-yy HH:mm:ss"));
	 //System.out.println("Date By Zone : " + getStringDateByzone("Asia/Calcutta", "dd-M-yyyy HH:mm:ss"));
	 }

 /***
  * Method to return Date based on timezone in string format
  * @author Nikhil
  * @param zone
  * @return date string
  */
/* public  static String getStringDateByzone(String zone){
     return getStringDate(getDateByzone(zone) , "dd-M-yyyy hh:mm:ss a");
 }*/

 public  static String getStringDateByzone(String zone, String pattern){
     return getStringDate(getDateByzone(zone,pattern) , pattern);
 }

 /***
  * Method to  Date based on timezone
  * @param zone
  * @return Date
  */
 public static Date getDateByzone(String zone){
	 try{
		 String DATE_FORMAT = "dd-M-yyyy hh:mm:ss a";
	     String dateInString = getStringDate(TimeZoneComponent.getDateByzone(0), DATE_FORMAT);
	     LocalDateTime ldt = LocalDateTime.parse(dateInString, DateTimeFormatter.ofPattern(DATE_FORMAT));
	     ZoneId currentZone = ZoneId.of(currentTimezone);
	     //System.out.println("Current TimeZone : " + currentTimezone);

	     //Current zone date time
	     ZonedDateTime cutrrentZonedDateTime = ldt.atZone(currentZone);
	     //System.out.println("Date (AWS) : " + cutrrentZonedDateTime);

	     //In Session date time
	     //ZoneId sessionZone = ZoneId.of("Africa/Abidjan");
	     ZoneId sessionZone = ZoneId.of(zone);
	     //System.out.println("Session TimeZone : " + sessionZone);

	     //comapare to zones
	     ZonedDateTime newDateTime = cutrrentZonedDateTime.withZoneSameInstant(sessionZone);
	     //System.out.println("Date (Session zone) : " + newDateTime);

	     DateTimeFormatter format = DateTimeFormatter.ofPattern(DATE_FORMAT);

	     //System.out.println("Final date time : " + format.format(newDateTime));
	     return getDate(format.format(newDateTime).toString(), DATE_FORMAT);
	 }catch(Exception e){
		 logger.error("getDateByzone failed", e);
	 }
	 return null;
 }

 public static Date getDateByzone(String zone, String pattern){
	 try{
		 String DATE_FORMAT = "dd-M-yyyy hh:mm:ss a";
	     String dateInString = getStringDate(TimeZoneComponent.getDateByzone(0), DATE_FORMAT);
	     LocalDateTime ldt = LocalDateTime.parse(dateInString, DateTimeFormatter.ofPattern(DATE_FORMAT));
	     ZoneId currentZone = ZoneId.of(currentTimezone);
	     ////System.out.println("Current TimeZone : " + currentTimezone);

	     //Current zone date time
	     ZonedDateTime cutrrentZonedDateTime = ldt.atZone(currentZone);
	     ////System.out.println("Date (AWS) : " + cutrrentZonedDateTime);

	     //In Session date time
	     //ZoneId sessionZone = ZoneId.of("Africa/Abidjan");
	     ZoneId sessionZone = ZoneId.of(zone);
	     ////System.out.println("Session TimeZone : " + sessionZone);

	     //comapare to zones
	     ZonedDateTime newDateTime = cutrrentZonedDateTime.withZoneSameInstant(sessionZone);
	     ////System.out.println("Date (Session zone) : " + newDateTime);

	     DateTimeFormatter format = DateTimeFormatter.ofPattern(pattern);

	     ////System.out.println("Final date time : " + format.format(newDateTime));
	     return getDate(format.format(newDateTime).toString(), pattern);
	 }catch(Exception e){
		 logger.error("getDateByzone failed", e);
	 }
	 return null;
 }

 	public static String getStringDateFromLongDate(Long longDate, String pattern) {
 		SimpleDateFormat format = null;
		String strDate = null;
		Date date=null;
		try {
			 date=new Date(longDate);
			format = new SimpleDateFormat(pattern);
			strDate = format.format(date);
			//date = format.parse(strDate);
			////System.out.println(strDate+"__"+date);
		} catch (Exception e) {
			logger.error("getStringDateFromLongDate failed", e);
		}
		return strDate;
 	}

	public static String generateQRCodeLink(String numericValue) {
		String encodedValue = URLEncoder.encode(numericValue, StandardCharsets.UTF_8);
		return "https://api.qrserver.com/v1/create-qr-code/?size=300x300&data=" + encodedValue;
	}
}
