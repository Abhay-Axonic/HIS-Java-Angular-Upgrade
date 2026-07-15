package com.param.utility;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class TimeZoneComponent {

	private static final Logger logger = Logger.getLogger(TimeZoneComponent.class.getName());

	private static Map<Integer,String> timeZoneMap = new HashMap<Integer,String>();
	private static Map<String,List<Integer>> timeZoneAndUnitsMap = new HashMap<String,List<Integer>>(); 
	
	@Autowired
	ITimeZoneService iTimeZoneService;
	
	@PostConstruct
	public void init() {
		timeZoneAndUnitsMap = new HashMap<String,List<Integer>>(); 
		List<UnitMasterDto> unitMasterDtoList = iTimeZoneService.getUnitTimeZones();
		for(UnitMasterDto dto : unitMasterDtoList)
		{
			timeZoneMap.put(dto.getUnitId(), (dto.getUnitTimeZone()!=null && dto.getUnitTimeZone().length()>0) ? dto.getUnitTimeZone() : "Asia/Calcutta");
	
			List<Integer> unitIds = new ArrayList<Integer>();
			unitIds.add(dto.getUnitId());
			
			if(timeZoneAndUnitsMap.containsKey((dto.getUnitTimeZone()!=null && dto.getUnitTimeZone().length()>0) ? dto.getUnitTimeZone() : "Asia/Calcutta")) 
			{
				timeZoneAndUnitsMap.get((dto.getUnitTimeZone()!=null && dto.getUnitTimeZone().length()>0) ? dto.getUnitTimeZone() : "Asia/Calcutta").add(dto.getUnitId());
				unitIds = new ArrayList<Integer>();
			}else {
				timeZoneAndUnitsMap.put((dto.getUnitTimeZone()!=null && dto.getUnitTimeZone().length()>0) ? dto.getUnitTimeZone() : "Asia/Calcutta", unitIds);
			}
		}
	}

	public static Date getDateByzone(Integer unitId){
		try{
			 String zone = (unitId != null && unitId >0) ? TimeZoneComponent.timeZoneMap.get(unitId) : "Asia/Calcutta";
			 String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";
		     String dateInString = getStringDate(new Date(), DATE_FORMAT);
		     LocalDateTime ldt = LocalDateTime.parse(dateInString, DateTimeFormatter.ofPattern(DATE_FORMAT));
		     ZoneId currentZone = ZoneId.of("Asia/Calcutta");
		     ZonedDateTime cutrrentZonedDateTime = ldt.atZone(currentZone);
		     ZoneId sessionZone = ZoneId.of(zone);
		     ZonedDateTime newDateTime = cutrrentZonedDateTime.withZoneSameInstant(sessionZone);
		     DateTimeFormatter format = DateTimeFormatter.ofPattern(DATE_FORMAT);
		     return getDate(format.format(newDateTime).toString(), DATE_FORMAT);
		 }catch(Exception e){
			 logger.log(Level.SEVERE, "getDateByzone failed", e);
		 }
		 return null;
	 }
	
	public static Long getDateByzoneLong(Integer unitId){
		try{
			 String zone = (unitId != null && unitId >0) ? TimeZoneComponent.timeZoneMap.get(unitId) : "Asia/Calcutta";
			 String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";
		     String dateInString = getStringDate(new Date(), DATE_FORMAT);
		     LocalDateTime ldt = LocalDateTime.parse(dateInString, DateTimeFormatter.ofPattern(DATE_FORMAT));
		     ZoneId currentZone = ZoneId.of("Asia/Calcutta");
		     ZonedDateTime cutrrentZonedDateTime = ldt.atZone(currentZone);
		     ZoneId sessionZone = ZoneId.of(zone);
		     ZonedDateTime newDateTime = cutrrentZonedDateTime.withZoneSameInstant(sessionZone);
		     DateTimeFormatter format = DateTimeFormatter.ofPattern(DATE_FORMAT);
		     return getDateLong(format.format(newDateTime).toString(), DATE_FORMAT);
		 }catch(Exception e){
			 logger.log(Level.SEVERE, "getDateByzoneLong failed", e);
		 }
		 return null;
	 }
	
	private static String getStringDate(Date dateDate , String pattern) 
	{
		SimpleDateFormat format = null;
		String strDate = null;
		format = new SimpleDateFormat(pattern);
		strDate = format.format(dateDate);
		return strDate;
	}
	
	private static Date getDate(String dateString , String pattern) 
	{
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
		} catch (ParseException e) {
			logger.log(Level.SEVERE, "getDate failed", e);
		}
		return date1;
	}
	
	private static Long getDateLong(String dateString , String pattern) 
	{
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
		} catch (ParseException e) {
			logger.log(Level.SEVERE, "getDateLong failed", e);
		}
		return date1.getTime();
	}
	
	public static void updateTimezoneMap(Integer unitId, String timezone)
	{
		TimeZoneComponent.timeZoneMap.put(unitId, timezone);
		
		List<Integer> unitIds = new ArrayList<Integer>();
		unitIds.add(unitId);
		
		for (Map.Entry<String,List<Integer>> timeZone : timeZoneAndUnitsMap.entrySet())
		{
			List<Integer> itrUnitIds = timeZone.getValue();
			
			if(itrUnitIds.contains(unitId))
				itrUnitIds.remove(unitId);
			 
			timeZone.setValue(itrUnitIds);
		}
		
		if(TimeZoneComponent.timeZoneAndUnitsMap.containsKey((timezone!=null && timezone.length()>0) ? timezone : "Asia/Calcutta"))
			timeZoneAndUnitsMap.get((timezone!=null && timezone.length()>0) ? timezone : "Asia/Calcutta").add(unitId);
		else
			timeZoneAndUnitsMap.put((timezone!=null && timezone.length()>0) ? timezone : "Asia/Calcutta", unitIds);
		
		
	}

	public static Map<String,List<Integer>> getUnitTimeZoneList(){
		return timeZoneAndUnitsMap;
	}
	
	public static String getTimeZoneByUnitId(Integer unitId){
		return timeZoneMap.get(unitId);
		}


	public static LocalDate convertUtilDateToLocalDate(Integer unitId,Date utilDate) {
		String zone = (unitId != null && unitId >0) ? TimeZoneComponent.timeZoneMap.get(unitId) : "Asia/Calcutta";
		ZoneId sessionZone = ZoneId.of(zone);
		return utilDate.toInstant().atZone(sessionZone).toLocalDate();
	}
	public static Date convertToLocalDateToDate(Integer unitId,LocalDate localDate) {
		String zone = (unitId != null && unitId >0) ? TimeZoneComponent.timeZoneMap.get(unitId) : "Asia/Calcutta";
		ZoneId sessionZone = ZoneId.of(zone);
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
	public static LocalDate getLocalDateByZone(Integer unitId){
		try{
			String zone = (unitId != null && unitId >0) ? TimeZoneComponent.timeZoneMap.get(unitId) : "Asia/Calcutta";
			return LocalDate.now(ZoneId.of(zone));
		}catch(Exception e){
			logger.log(Level.SEVERE, "getLocalDateByZone failed", e);
		}
		return null;
	}

	public static Date getDateByzone(String date,Integer unitId){
		try{
			String zone = (unitId != null && unitId >0) ? TimeZoneComponent.timeZoneMap.get(unitId) : "Asia/Calcutta";
			String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";
			String dateInString = date;
			LocalDateTime ldt = LocalDateTime.parse(dateInString, DateTimeFormatter.ofPattern(DATE_FORMAT));
			ZoneId currentZone = ZoneId.of("Asia/Calcutta");
			ZonedDateTime cutrrentZonedDateTime = ldt.atZone(currentZone);
			ZoneId sessionZone = ZoneId.of(zone);
			ZonedDateTime newDateTime = cutrrentZonedDateTime.withZoneSameInstant(sessionZone);
			DateTimeFormatter format = DateTimeFormatter.ofPattern(DATE_FORMAT);
			return getDate(format.format(newDateTime).toString(), DATE_FORMAT);
		}catch(Exception e){
			logger.log(Level.SEVERE, "getDateByzone failed", e);
		}
		return null;
	}


	public static Date getDateBywithoutZone(String date) {
		try {
			DateTimeFormatter formatter =
					DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

			LocalDateTime ldt = LocalDateTime.parse(date, formatter);

			// DB already stores local timestamps → NO conversion
			return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());

		} catch (Exception e) {
			logger.log(Level.SEVERE, "getDateBywithoutZone failed", e);
		}
		return null;
	}


	public static Date getDateByzoneWithDayEndTime(Integer unitId) {
		try {
			LocalDate today = LocalDate.now();
			LocalDateTime endOfDay = today.atTime(23, 59, 59);
			return Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
		} catch (Exception e) {
			logger.log(Level.SEVERE, "getDateByzoneWithDayEndTime failed", e);
		}
		return null;
	}

	public void refreshTimeZoneMap(){
		init();
	}

	private static final Pattern SAFE_TEXT_PATTERN = Pattern.compile(
			"^(?!.*(<script|</script>|javascript:|onerror=|onload=|<|>)).*$",
			Pattern.CASE_INSENSITIVE
	);

	public static boolean isValidText(String input) {

		if (input == null || input.trim().isEmpty()) {
			return true;
		}

		return SAFE_TEXT_PATTERN.matcher(input).matches();
	}
}