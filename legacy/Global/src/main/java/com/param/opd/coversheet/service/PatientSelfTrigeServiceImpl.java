package com.param.opd.coversheet.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IScheduleLogsDao;
import com.param.global.dto.MyLifeConnectionLinkDto;
import com.param.opd.coversheet.dto.PatientSelfTrigeDto;
import com.param.opd.coversheet.dto.StPatientAllergyDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
public class PatientSelfTrigeServiceImpl implements IPatientSelfTrigeService, ICommonConstants{
	final static Logger logger = Logger.getLogger(PatientSelfTrigeServiceImpl.class);

	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	private Gson gson;
	
	@Autowired
	IScheduleLogsDao iScheduleLogsDao;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Response getListOfPatientSelfTrige(PatientSelfTrigeDto patientSelfTrigeDto)
			throws ApplicationException {
		try {
			List<StPatientAllergyDto> list = new ArrayList<StPatientAllergyDto>();

			String Global_Scheduling_URI = null;
			String MY_LIFE_URI = null;
			URI url = null;
			Response res=iScheduleLogsDao.getConnectionStringForMyLife();
			MyLifeConnectionLinkDto connectionLinkDto=(MyLifeConnectionLinkDto)res.getObject();
			if(connectionLinkDto!=null && connectionLinkDto.getMyLifeLink()!=null && connectionLinkDto.getMyLifeLink()!="" && connectionLinkDto.getGlobalSchedulingLink()!=null && connectionLinkDto.getGlobalSchedulingLink()!="")
			{
				Global_Scheduling_URI = connectionLinkDto.getGlobalSchedulingLink();
				MY_LIFE_URI = connectionLinkDto.getMyLifeLink(); 
				//MY_LIFE_URI = "http://10.10.0.195:9091/MyLifeGlobal/api";
			}
			
			Response tempResponse = new Response<>(ICommonConstants.ERROR, null, null,null,null);
			if(patientSelfTrigeDto.getUuid()!=null && patientSelfTrigeDto.getUuid()!="") 
			{
				switch (patientSelfTrigeDto.getTabName()) {
				case "Vitals":
					url = URI.create(MY_LIFE_URI + "/patient/getSelfTriageVitals/"+patientSelfTrigeDto.getUuid());
					String tempStrResponse1 = restTemplate.getForObject(url, String.class);
					tempResponse = gson.fromJson(tempStrResponse1, Response.class);
					return new Response<>(SUCCESS, null, null, null, tempResponse);
					
				case "ManualVitals":
					url = URI.create(MY_LIFE_URI + "/patient/getManualVitals/"+patientSelfTrigeDto.getUuid());
					String tempStrResponse2 = restTemplate.getForObject(url, String.class);
					tempResponse = gson.fromJson(tempStrResponse2, Response.class);
					return new Response<>(SUCCESS, null, null, null, tempResponse);
					
				case "AIVitals":
					url = URI.create(MY_LIFE_URI + "/patient/selftrige/allergies/"+patientSelfTrigeDto.getUuid());
					String tempStrResponse3 = restTemplate.getForObject(url, String.class);
					tempResponse = gson.fromJson(tempStrResponse3, Response.class);
					return new Response<>(SUCCESS, null, null, null, tempResponse);
					
				case "Allergies":
					url = URI.create(MY_LIFE_URI + "/patient/selftrige/allergies/"+patientSelfTrigeDto.getUuid());
					String tempStrResponse4 = restTemplate.getForObject(url, String.class);
					tempResponse = gson.fromJson(tempStrResponse4, Response.class);
					return new Response<>(SUCCESS, null, null, null, tempResponse);
					
				case "FamilyHistory":
					url = URI.create(MY_LIFE_URI + "/patient/selftrige/familyHistory/"+patientSelfTrigeDto.getUuid());
					String tempStrResponse5 = restTemplate.getForObject(url, String.class);
					tempResponse = gson.fromJson(tempStrResponse5, Response.class);
					return new Response<>(SUCCESS, null, null, null, tempResponse);
					
				case "KnownCondition":
					url = URI.create(MY_LIFE_URI + "/patient/selftrige/knownConditions/"+patientSelfTrigeDto.getUuid());
					String tempStrResponse6 = restTemplate.getForObject(url, String.class);
					tempResponse = gson.fromJson(tempStrResponse6, Response.class);
					return new Response<>(SUCCESS, null, null, null, tempResponse);
					
				case "GeneticDisorder":
					url = URI.create(MY_LIFE_URI + "/patient/selftrige/geneticDisorders/"+patientSelfTrigeDto.getUuid());
					String tempStrResponse7 = restTemplate.getForObject(url, String.class);
					tempResponse = gson.fromJson(tempStrResponse7, Response.class);
					return new Response<>(SUCCESS, null, null, null, tempResponse);
				
				case "Documents":
					url = URI.create(MY_LIFE_URI + "/patient/selftrige/documents/"+patientSelfTrigeDto.getUuid());
					String tempStrResponse8 = restTemplate.getForObject(url, String.class);
					tempResponse = gson.fromJson(tempStrResponse8, Response.class);
					return new Response<>(SUCCESS, null, null, null, tempResponse);
				
				case "UrinAnalyser":
					url = URI.create(MY_LIFE_URI + "/patient/selftrige/urine/vitals/"+patientSelfTrigeDto.getUuid());
					String tempStrResponse9 = restTemplate.getForObject(url, String.class);
					tempResponse = gson.fromJson(tempStrResponse9, Response.class);
					return new Response<>(SUCCESS, null, null, null, tempResponse);
				
				case "FetalDoppler":
					url = URI.create(MY_LIFE_URI + "/patient/selftrige/fetal/"+patientSelfTrigeDto.getUuid());
					String tempStrResponse10 = restTemplate.getForObject(url, String.class);
					tempResponse = gson.fromJson(tempStrResponse10, Response.class);
					return new Response<>(SUCCESS, null, null, null, tempResponse);
					
				default:
					break;
				}
				
			}

	} catch (Exception e) {
	logger.error("getListOfPatientSelfTrige failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

}
