package com.param.global.common;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringJoiner;
import java.util.TimeZone;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.param.utility.TimeZoneComponent;

import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@SessionAttributes("sessionObject")
public class GlobalCommonDateUtils {

	private static final Logger logger = Logger.getLogger(GlobalCommonDateUtils.class.getName());

	private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	private static final Random random = new Random();
	private static final String NUMBERS = "0123456789";
	
	private static SecretKeySpec secretKey;
    private static byte[] key;
    static String secret = ICommonConstants.ENCRYPTIONKEY;
    
    
    public static final DecimalFormat df2 = new DecimalFormat("#.##");
    
    // Define the character sets for the password
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%&*";


    // SecureRandom instance for generating random values
    private static final SecureRandom RANDOM = new SecureRandom();
    
    public static double getAmount(Double amount) {
    	return new Double(df2.format(Math.abs(amount==null?0:amount))).doubleValue();
    }
    
    public static double getAmountCap(Double amount) {
    	return new Double(df2.format(amount==null?0:amount)).doubleValue();
    }
    
    
    public static String getVisitType(Integer visitTypeId) throws ParseException 
    {
    String visitType="";
    switch (visitTypeId) {
	case 1:
		visitType = "OP";
		break;
	case 2:
		visitType = "IP";
		break;
	case 3:
		visitType = "DC";
		break;
	case 4:
		visitType = "ER";
		break;	
	default:
		break;
	  }
    return visitType;
    }

    
	public static String convertToStringJoiner (List<Integer> list)
	{
		StringJoiner stringJoiner = null;
		try {
			if(list.size()>0)
			{
			 stringJoiner  = new StringJoiner(",");
			for (Integer obj : list) {
				stringJoiner.add(obj.toString());
			 }
			return stringJoiner.toString();
			}
			else {
				return "0";
			}
		}
		catch (Exception e)
		{
			logger.log(Level.SEVERE, "convertToStringJoiner failed", e);
		}
		return "0";
	}
    public static String formatDateForClaim(String date, String initDateFormat, String endDateFormat) throws ParseException {

        Date initDate = new SimpleDateFormat(initDateFormat).parse(date);
        SimpleDateFormat formatter = new SimpleDateFormat(endDateFormat);
        String parsedDate = formatter.format(initDate);

        return parsedDate;
    }
	
	/*code for password generation*/
	public static String generatePassword(String firstName,String LastName,Date dob)
	{
		try {
			/*String firstFourChar = null;
			if(firstName!=null&&LastName!=null&&dob!=null)
			{
				SimpleDateFormat df = new SimpleDateFormat("yyyy");
				String year = df.format(dob);
				if(firstName.length()>4)
				{
					 firstFourChar = firstName.substring(0, 4).trim().toUpperCase()+year;
					 return firstFourChar;
				}
				else if(LastName.length()>4)
				{
					int lenght = firstName.length();
					int remainingLen = (4-lenght);
					firstFourChar = firstName.trim().toUpperCase()+LastName.substring(0, remainingLen).trim().toUpperCase()+year;
					return firstFourChar;
				}
			}
			else {
				Random rnd = new Random();
				Integer number = rnd.nextInt(9999);
				return number.toString();
			}*/
			String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*";
			StringBuilder salt = new StringBuilder("");
			Random rnd = new Random();
			while (salt.length() <= 6) {
				int index = (int) (rnd.nextFloat() * SALTCHARS.length());
				salt.append(SALTCHARS.charAt(index));
			}
			return salt.toString();

		} catch (Exception e) {
			logger.log(Level.SEVERE, "generatePassword failed", e);
			return null;
		}

	}

	
	/*code for generate random number*/
	
	public static char[] generateRandomNumber(int len) 
    { 
        //System.out.println("Generating OTP using random() : "); 
  
        // Using numeric values 
        String numbers = "0123456789"; 
  
        // Using random method 
        Random rndm_method = new Random(); 
  
        char[] num = new char[len]; 
  
        for (int i = 0; i < len; i++) 
        { 
            // Use of charAt() method : to get character value 
            // Use of nextInt() as it is scanning the value as int 
        	num[i] = 
             numbers.charAt(rndm_method.nextInt(numbers.length())); 
        } 
        return num;
    }
	/* code for encryption*/
/*
	 public static void setKey(String myKey)
	    {
	        MessageDigest sha = null;
	        try {
	            key = myKey.getBytes("UTF-8");
	            sha = MessageDigest.getInstance("SHA-1");
	            key = sha.digest(key);
	            key = Arrays.copyOf(key, 16);
	            secretKey = new SecretKeySpec(key, "AES");
	        }
	        catch (NoSuchAlgorithmException e) {
	            
	        }
	        catch (UnsupportedEncodingException e) {
	            
	        }
	    }*/
	 
	    public static String encrypt(String strToEncrypt)
	    {
	        try
	        {
	            setKey(secret);
	            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
	            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
	            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
	        }
	        catch (Exception e)
	        {
	            logger.log(Level.SEVERE, "encrypt failed", e);
	        }
	        return null;
	    }
	 
	    public static String decrypt(String strToDecrypt)
	    {
	        try
	        {
	            setKey(secret);
	            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
	            cipher.init(Cipher.DECRYPT_MODE, secretKey);
	            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
	        }
	        catch (Exception e)
	        {
	            logger.log(Level.SEVERE, "decrypt failed", e);
	        }
	        return null;
	    }
	 
	 /*public static String getMD5EncryptedValue(String password) {
	        final byte[] defaultBytes = password.getBytes();
	        try {
	            final MessageDigest md5MsgDigest = MessageDigest.getInstance("MD5");
	            md5MsgDigest.reset();
	            md5MsgDigest.update(defaultBytes);
	            final byte messageDigest[] = md5MsgDigest.digest();
	            final StringBuffer hexString = new StringBuffer();
	            for (final byte element : messageDigest) {
	                final String hex = Integer.toHexString(0xFF & element);
	                if (hex.length() == 1) {
	                    hexString.append('0');
	                }
	                hexString.append(hex);
	            }
	            password = hexString + "";
	        } catch (final NoSuchAlgorithmException nsae) {
	            nsa
	        }
	        return password;
	    }
	 */  
	
	
	
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
			
			String  preDate = null;
			
			if(dateString.length()<12) {
				
				 preDate = dateString+" 00:00:00";
				 
			}else {
				
				preDate = dateString;
			}
			
			date1 = format.parse(preDate);
			//System.out.println("dateeee===="+date1);
		} catch (ParseException e) {
			logger.log(Level.SEVERE, "getDate failed", e);
		}
		return date1;
	}
	
	public static String getStringMongoDate(String dateString) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
			DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String mongoDate=null;
		try {
			Date procedureStartTime = dateFormat.parse(dateString);
			mongoDate = formatter.format(procedureStartTime);
			//System.out.println("dateeee===="+mongoDate);
		} catch (ParseException e) {
			logger.log(Level.SEVERE, "getStringMongoDate failed", e);
		}
		return mongoDate;
	}

	public static String getStringOnlyDate(String dateString) {
		String preDate = null;
		if(dateString.length()<12) {
			
			 preDate = dateString+" 00:00:00";
			 
		}else {
			
			preDate = dateString;
		}
		
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String mongoDate=null;
	try {
		Date procedureStartTime = dateFormat.parse(preDate);
		mongoDate = formatter.format(procedureStartTime);
		//System.out.println("dateeee===="+mongoDate);
	} catch (ParseException e) {
		logger.log(Level.SEVERE, "getStringOnlyDate failed", e);
	}
	return mongoDate;
}
	
	
	public static String getStringDate(Date dateDate , String pattern) {
		SimpleDateFormat format = null;
		String strDate = null;
		format = new SimpleDateFormat(pattern);
		strDate = format.format(dateDate);
		return strDate;
	}
	
	
	public static Integer getDateDifference(Date fromDate , Date toDate) 
	{
		long diff =  fromDate.getTime() - toDate.getTime();
		int diffDays = (int) (diff / (24 * 60 * 60 * 1000));
		return diffDays;
	}
	
	
	public static String getStringDate(Long longDate , String pattern) {
		
	    Date date=new Date(longDate);
	    SimpleDateFormat df2 = new SimpleDateFormat(pattern);
	    String dateText = df2.format(date);
		
		return dateText;
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
			logger.log(Level.SEVERE, "getDate failed", e);
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
			logger.log(Level.SEVERE, "getAvalDate failed", e);
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
			logger.log(Level.SEVERE, "getTime failed", e);
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
			logger.log(Level.SEVERE, "getFirstDateOfMonth failed", e);
		}
		return getStringDate(c.getTime(), "dd-MM-yyyy");
	}
	
	public static String getLastDateOfMonth(Date date) {
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(date);
			c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));

		} catch (Exception e) {
			logger.log(Level.SEVERE, "getLastDateOfMonth failed", e);
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
			logger.log(Level.SEVERE, "getLastDateOfMonth failed", e);
		}
		return lastDate;
	}
	
	public static LocalDate getLocalDate(Date date){
		LocalDate localDate = null;
		try {
			localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "getLocalDate failed", e);
		}
		return localDate;
	}
	
	public static Date getDate(LocalDate date){
		Date dateDate = null;
		try {
			dateDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
		} catch (Exception e) {
			logger.log(Level.SEVERE, "getDate failed", e);
		}
		return dateDate;
	}
	
	public static Date getDateWithEndTime(LocalDate date){
		Date dateDate = null;
		try {
			dateDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
			dateDate.setHours(23);
			dateDate.setMinutes(59);
			dateDate.setSeconds(59);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "getDateWithEndTime failed", e);
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
			logger.log(Level.SEVERE, "getStringDate failed", e);
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
			logger.log(Level.SEVERE, "getDayOfWeek failed", e);
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
			logger.log(Level.SEVERE, "getWeekOfMonth failed", e);
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
			logger.log(Level.SEVERE, "getLocalTime failed", e);
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
	
	

 public static void main(String args[]) throws ParseException{  
	  	//	//System.out.println("hisfds "+encrypt("dinesh"));
	  		////System.out.println(decrypt("HZz0HNs0+trNwUpijqb5Jg=="));
	  		
	  
	        
	       /* String time1 = "24:00:00";
	        String time2 = "07-02-2020";
	        
	     
	        Date  d =  getDate(time2,"dd-M-yyyy HH:mm:ss");*/
	        
	        
	 String sDate1= "2022-07-27";  
	    Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(sDate1);  
	    System.out.println(sDate1+"\t"+date1);  
			
	        /*SimpleDateFormat formatter = new SimpleDateFormat("h:mm:ss");
	        Date d1 = formatter.parse(time1);
	        Date d2 = formatter.parse(time2);
	        long timeDiff = d2.getTime() - d1.getTime();
	        //System.out.println((timeDiff / 3600000) + " hour/s " + (timeDiff % 3600000) / 60000 + " minutes");
	        
	        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	        final Date date = format.parse("2019-02-28");
	        final Calendar calendar = Calendar.getInstance();
	        calendar.setTime(date);
	        calendar.add(Calendar.DAY_OF_YEAR, 1);
	        //System.out.println(format.format(calendar.getTime()) + " next date");
	        
	        
	        String string2 = "14:49:00";
	        Date time3 = new SimpleDateFormat("HH:mm:ss").parse(string2);
	        Calendar calendar2 = Calendar.getInstance();
	        calendar2.setTime(time3);
	        calendar2.add(Calendar.DATE, 1);

	        String someRandomTime = "12:00:00";
	        Date d = new SimpleDateFormat("HH:mm:ss").parse(someRandomTime);
	        Calendar calendar3 = Calendar.getInstance();
	        calendar3.setTime(d);
	        calendar3.add(Calendar.DATE, 1);

	        Date x = calendar3.getTime();
	        if (x.before(calendar2.getTime())) {
	           
	            //System.out.println(true);
	        }*/
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
	     String dateInString = getStringDate(new Date(), DATE_FORMAT);
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
		 logger.log(Level.SEVERE, "getDateByzone failed", e);
	 }
	 return null;
 }

 public static Date getDateByzone(String zone, String pattern){
	 try{
		 String DATE_FORMAT = "dd-M-yyyy hh:mm:ss a";
	     String dateInString = getStringDate(new Date(), DATE_FORMAT);
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
		 logger.log(Level.SEVERE, "getDateByzone failed", e);
	 }
	 return null;
 }


/* public static Date getDateByzone(String zone, String pattern){
	 try{
		 String DATE_FORMAT = "dd-M-yyyy hh:mm:ss a";
	     String dateInString = getStringDate(new Date(), DATE_FORMAT);
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
	     
	     DateTimeFormatter format = DateTimeFormatter.ofPattern(pattern);
	     
	     //System.out.println("Final date time : " + format.format(newDateTime));
	     return getDate(format.format(newDateTime).toString(), pattern);
	 }catch(Exception e){
		 
	 }
	 return null;
 }*/
 
public static Long getLongDateFromStrigDate(String string_date) {
 SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	 try {
	     Date d = f.parse(string_date);
	     long milliseconds = d.getTime();
	     return milliseconds;
	 } catch (ParseException e) {
	     logger.log(Level.SEVERE, "getLongDateFromStrigDate failed", e);
	 }
	 return null;
	}


public static Long getLongDateFromStrigDateWithPattern(String string_date,String pattern) {
	 SimpleDateFormat f = new SimpleDateFormat(pattern);
		 try {
		     Date d = f.parse(string_date);
		     long milliseconds = d.getTime();
		     return milliseconds;
		 } catch (ParseException e) {
		     logger.log(Level.SEVERE, "getLongDateFromStrigDateWithPattern failed", e);
		 }
		 return null;
		}


public static Long getLongDateFromStrigDate(String string_date,String dateFormat) {
	 SimpleDateFormat f = new SimpleDateFormat(dateFormat);
		 try {
		     Date d = f.parse(string_date);
		     long milliseconds = d.getTime();
		     return milliseconds;
		 } catch (ParseException e) {
		     logger.log(Level.SEVERE, "getLongDateFromStrigDate failed", e);
		 }
		 return null;
		}
	public static List<String> getDatesOfSpecificDayInSingleYear(String year,Integer dayId) {
		try{
		List<String> dateList = new LinkedList<String>();
		org.joda.time.LocalDate startDate = new org.joda.time.LocalDate(year + "-01-01");
		org.joda.time.LocalDate endDate = new org.joda.time.LocalDate(year + "-12-31");
		org.joda.time.LocalDate day = startDate.withDayOfWeek(dayId);
		if (startDate.isAfter(day)) {
			startDate = day.plusWeeks(1);
		} else {
			startDate = day;
		}
		while (startDate.isBefore(endDate)) {
			dateList.add(startDate.toString());
			startDate = startDate.plusWeeks(1);
		}
		return dateList;
		}catch(Exception e)
		{
			logger.log(Level.SEVERE, "getDatesOfSpecificDayInSingleYear failed", e);
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
			logger.log(Level.SEVERE, "getStringDateFromLongDate failed", e);
		}
		return strDate;
 	}
	
	   public static Date getNextDateInDate(Long date) {
	     Date toDate = null;
	        try {
	          Calendar cal = Calendar.getInstance();
	          cal.setTime(new Date(date));
	          cal.add(Calendar.DATE, 1);
	           toDate = cal.getTime();
	        } catch (Exception e) {
	            logger.log(Level.SEVERE, "getNextDateInDate failed", e);
	        }
	        return toDate;
	    }

	   public static String generateRandomString(){
			try{
				StringBuilder salt = new StringBuilder();
		        while (salt.length() < 9) { // length of the random string.
		            int index = (int) (random.nextFloat() * CHARS.length());
		            salt.append(CHARS.charAt(index));
		        }
		        String saltStr = salt.toString();
		        return saltStr;
			}catch(Exception e){
				logger.log(Level.SEVERE, "generateRandomString failed", e);
			}
			return null;
		}
	   
	   public static char[] generateOTP(int len) 
	    { 
	        //System.out.println("Generating OTP using random() : "); 
	  
	        // Using numeric values 
	        String numbers = "0123456789"; 
	  
	        // Using random method 
	        Random rndm_method = new Random(); 
	  
	        char[] otp = new char[len]; 
	  
	        for (int i = 0; i < len; i++) 
	        { 
	            // Use of charAt() method : to get character value 
	            // Use of nextInt() as it is scanning the value as int 
	            otp[i] = 
	             numbers.charAt(rndm_method.nextInt(numbers.length())); 
	        } 
	        return otp; 
	    } 
	   
		public static void setKey(String myKey){
	        MessageDigest sha = null;
	        try {
	            key = myKey.getBytes("UTF-8");
	            sha = MessageDigest.getInstance("SHA-1");
	            key = sha.digest(key);
	            key = Arrays.copyOf(key, 16); // use only first 128 bit
	          //  //System.out.println(key.length);
	            ////System.out.println(new String(key,"UTF-8"));
	            secretKey = new SecretKeySpec(key, "AES");
	            
	        } catch (NoSuchAlgorithmException e) {
	            // TODO Auto-generated catch block
	            logger.log(Level.SEVERE, "setKey failed (NoSuchAlgorithmException)", e);
	        } catch (UnsupportedEncodingException e) {
	            // TODO Auto-generated catch block
	            logger.log(Level.SEVERE, "setKey failed (UnsupportedEncodingException)", e);
	        }
		}
					
		public static Map<String,Integer> getMonthValue(String str_date,String end_date,List<Integer> dayId) {
			
				 List<Date> dates = new ArrayList<Date>();
				 Map<String,Integer> dateList = new HashMap<String,Integer>();
				 Integer IntDay= 0;
				 DateFormat formatter ; 
				 formatter = new SimpleDateFormat("dd-MM-yyyy");
				 try {
					 Date  startDate = (Date)formatter.parse(str_date); 
					 Date  endDate = (Date)formatter.parse(end_date);
					 long interval = 24*1000*60*60; // 1 hour in millis
					 long endTime =endDate.getTime() ; // create your endtime here, possibly using Calendar or Date
					 long curTime = startDate.getTime();
					 while (curTime <= endTime) {
					     dates.add(new Date(curTime));
					     curTime += interval;
					 }
					 for(int i=0;i<dates.size();i++){
						 
					     Date lDate =(Date)dates.get(i);
					     LocalDate localDate = lDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
						 //System.out.println("localDate"+localDate);
					     int year  = localDate.getYear();
					     int month = localDate.getMonthValue();
					     int day   = localDate.getDayOfMonth();
						   IntDay = getDaye(year,month,day);
						 //  //System.out.println("str"+IntDay);
						   for (Integer ids : dayId) {
							   
							 if(ids == IntDay) {
								 String stringDate  = localDate.toString();
								 dateList.put(stringDate,IntDay);
							 }  
						}
					 }
				 }
				 catch (Exception e) {
					 //System.out.println("e"+e);
				 logger.log(Level.SEVERE, "getMonthValue failed", e);
				 }
				   return dateList;
		}
			public static Integer getDaye(int y , int m , int dd) {
				
				
				LocalDate localDate = LocalDate.of(y, m, dd); 
			
			    DayOfWeek dayOfWeek = DayOfWeek.from(localDate); 
			
			    int val  = dayOfWeek.get(ChronoField.DAY_OF_WEEK); 
				
				return val;
			}
			
			 public static String generatePassword(int length) {
			        if (length < 4) {
			            throw new IllegalArgumentException("Password length should be at least 4");
			        }

			        // Combine all character sets
			        String allCharacters = UPPERCASE + LOWERCASE + DIGITS + SPECIAL_CHARACTERS;

			        // StringBuilder to build the password
			        StringBuilder password = new StringBuilder(length);

			        // Ensure the password contains at least one character from each set
			        password.append(getRandomCharacter(UPPERCASE));
			        password.append(getRandomCharacter(LOWERCASE));
			        password.append(getRandomCharacter(DIGITS));
			        password.append(getRandomCharacter(SPECIAL_CHARACTERS));

			        // Fill the rest of the password length with random characters
			        for (int i = 4; i < length; i++) {
			            password.append(getRandomCharacter(allCharacters));
			        }

			        // Shuffle the password to ensure randomness
			        return shuffleString(password.toString());
			    }

			    private static char getRandomCharacter(String characterSet) {
			        int randomIndex = RANDOM.nextInt(characterSet.length());
			        return characterSet.charAt(randomIndex);
			    }

			    private static String shuffleString(String input) {
			        char[] characters = input.toCharArray();
			        for (int i = characters.length - 1; i > 0; i--) {
			            int index = RANDOM.nextInt(i + 1);
			            char temp = characters[index];
			            characters[index] = characters[i];
			            characters[i] = temp;
			        }
			        return new String(characters);
			    }
			
			
}
