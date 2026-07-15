package com.param.lis.global.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
public class ZonedDate {

	
	public static List<Date> getDatesBetweenUsingJava7(
			  Date startDate, Date endDate) {
			    List<Date> datesInRange = new ArrayList<>();
			    Calendar calendar = new GregorianCalendar();
			    calendar.setTime(startDate);
			     
			    Calendar endCalendar = new GregorianCalendar();
			    endCalendar.setTime(endDate);
			 
			    while (calendar.before(endCalendar)) {
			        Date result = calendar.getTime();
			        datesInRange.add(result);
			        calendar.add(Calendar.DATE, 1);
			    }
			    return datesInRange;
			}

	public static void main(String[] args) {
		
		String a= "{\r\n" + 
				"  \"dischargeId\" : 0,\r\n" + 
				"  \"unitId\" : 1,\r\n" + 
				"  \"organizationId\" : 1,\r\n" + 
				"  \"admissionId\" : 3568,\r\n" + 
				"  \"doctorId\" : null,\r\n" + 
				"  \"dischargeTypeId\" : null,\r\n" + 
				"  \"reasonId\" : 2000,\r\n" + 
				"  \"destinationId\" : null,\r\n" + 
				"  \"dischargeDate\" : null,\r\n" + 
				"  \"dischargeTime\" : null,\r\n" + 
				"  \"isDeath\" : \"N\",\r\n" + 
				"  \"remarkDischarge\" : null,\r\n" + 
				"  \"cancelReasonId\" : null,\r\n" + 
				"  \"noteCancel\" : \"fsd\",\r\n" + 
				"  \"status\" : \"A\",\r\n" + 
				"  \"dischargeStatusId\" : 6,\r\n" + 
				"  \"dischargeDesc\" : null,\r\n" + 
				"  \"createdDate\" : null,\r\n" + 
				"  \"createdBy\" : 0,\r\n" + 
				"  \"updatedBy\" : 229,\r\n" + 
				"  \"updatedDate\" : \"27-06-2019 17:22:48\",\r\n" + 
				"  \"bedId\" : null,\r\n" + 
				"  \"bedStatusId\" : null,\r\n" + 
				"  \"pdd\" : \"28-06-2019 00:00:00\",\r\n" + 
				"  \"visitTypeId\" : null,\r\n" + 
				"  \"erBedTypeAllocation\" : null,\r\n" + 
				"  \"isCancel\" : \"N\",\r\n" + 
				"  \"shortLeaveReasonId\" : null,\r\n" + 
				"  \"patientId\" : null,\r\n" + 
				"  \"tPatientId\" : null,\r\n" + 
				"  \"fromTime\" : {\r\n" + 
				"    \"hour\" : 0,\r\n" + 
				"    \"minute\" : 0,\r\n" + 
				"    \"second\" : 0,\r\n" + 
				"    \"nano\" : 0\r\n" + 
				"  },\r\n" + 
				"  \"toTime\" : {\r\n" + 
				"    \"hour\" : 0,\r\n" + 
				"    \"minute\" : 0,\r\n" + 
				"    \"second\" : 0,\r\n" + 
				"    \"nano\" : 0\r\n" + 
				"  },\r\n" + 
				"  \"stringFromTime\" : null,\r\n" + 
				"  \"stringToTime\" : null,\r\n" + 
				"  \"fromDate\" : null,\r\n" + 
				"  \"toDate\" : null,\r\n" + 
				"  \"note\" : null,\r\n" + 
				"  \"rejectionReasonId\" : null,\r\n" + 
				"  \"rejectionNote\" : null,\r\n" + 
				"  \"authorizedBy\" : null,\r\n" + 
				"  \"housekeepingId\" : null,\r\n" + 
				"  \"nursingStationId\" : null,\r\n" + 
				"  \"floorId\" : null,\r\n" + 
				"  \"wardId\" : null,\r\n" + 
				"  \"wardName\" : null,\r\n" + 
				"  \"wardList\" : null,\r\n" + 
				"  \"roomId\" : null,\r\n" + 
				"  \"bedCategoryId\" : null,\r\n" + 
				"  \"priorityId\" : null,\r\n" + 
				"  \"assignedPersonId\" : null,\r\n" + 
				"  \"housekeepingLogId\" : null,\r\n" + 
				"  \"shortLeaveStatusId\" : null,\r\n" + 
				"  \"shortLeaveStatusIdList\" : null,\r\n" + 
				"  \"isIcu\" : \"N\",\r\n" + 
				"  \"admissionNumber\" : null,\r\n" + 
				"  \"specialityId\" : null,\r\n" + 
				"  \"specialityName\" : null,\r\n" + 
				"  \"uhidNumber\" : null,\r\n" + 
				"  \"genderId\" : null,\r\n" + 
				"  \"genderCode\" : null,\r\n" + 
				"  \"dob\" : null,\r\n" + 
				"  \"doctorName\" : null,\r\n" + 
				"  \"floorName\" : null,\r\n" + 
				"  \"bedNumber\" : null,\r\n" + 
				"  \"patientName\" : null,\r\n" + 
				"  \"shortLeaveId\" : null,\r\n" + 
				"  \"shortLeaveReasonName\" : null,\r\n" + 
				"  \"reasonDesc\" : null,\r\n" + 
				"  \"shortLeaveStatusName\" : null,\r\n" + 
				"  \"doctorsNote\" : null,\r\n" + 
				"  \"billingsNote\" : null,\r\n" + 
				"  \"isHandoverMedication\" : \"N\",\r\n" + 
				"  \"doa\" : null,\r\n" + 
				"  \"dischCount\" : null,\r\n" + 
				"  \"labDeptId\" : null,\r\n" + 
				"  \"radDeptId\" : null,\r\n" + 
				"  \"referalDocId\" : null,\r\n" + 
				"  \"bedList\" : null,\r\n" + 
				"  \"isShowPatientByBed\" : \"N\"\r\n" + 
				"}";
		
		
	}
	
}