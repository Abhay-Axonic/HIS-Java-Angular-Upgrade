package com.param.adt.discharge.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.discharge.dao.IDueForDischargeDao;
import com.param.adt.discharge.dao.IMarkedForDischargeDao;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.dto.AdmissionDto;
import com.param.global.dto.AdmissionsForBatchProcessDto;
import com.param.global.dto.DischargeDto;
import com.param.global.utility.ErrorCorrelationUtil;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class DueForDischargeServiceImpl implements IDueForDischargeService,ICommonConstants {

	final static Logger logger = Logger.getLogger(DueForDischargeServiceImpl.class);

	@Autowired
	IDueForDischargeDao iDueForDischargeDao;
	
	@Autowired
	IMarkedForDischargeDao iMarkedForDischargeDao;
	
	@Autowired
	IMarkedForDischargeService iMarkedForDischargeService;
	
	@Override
	public Response saveDischargeRequest(DischargeDto dischargeDto) throws ApplicationException{
		try {
			dischargeDto.setDischargeTime(dischargeDto.getCreatedDate());
			Response res = iDueForDischargeDao.saveDischargeRequest(dischargeDto);
			if(res.getStatus().equals(SUCCESS) && res.getObject().equals('Y'))
			{
				if(dischargeDto.getVisitTypeId()!=null && dischargeDto.getVisitTypeId()==IP)
				{
					Response admRes=iMarkedForDischargeDao.getAdmissionDetailsByAdmissionId(dischargeDto);
					
					AdmissionsForBatchProcessDto admissionsForBatchProcessDto=(AdmissionsForBatchProcessDto) admRes.getObject();
					admissionsForBatchProcessDto.setDischargeDateTime(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dischargeDto.getUnitId() != null ? dischargeDto.getUnitId():0), "yyyy/MM/dd HH:mm:ss"));
					iMarkedForDischargeService.postRoomRentChargesForDischargeInitiate(admissionsForBatchProcessDto);
				}
			}
			return res;
		} catch (Exception e) {
			String correlationId = ErrorCorrelationUtil.newCorrelationId();
			logger.error(ErrorCorrelationUtil.tag(correlationId, "saveDischargeRequest failed"), e);
			return new Response(ERROR, null, ErrorCorrelationUtil.clientMessage(correlationId), null, null);
		}
	}


	@Override
	public Response getDischagePatientList(AdmissionDto admissionDto) throws ApplicationException
	{
		try {
			return iDueForDischargeDao.getDischagePatientList(admissionDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}
	

	@Override
	public Response cancelDischarge(DischargeDto dischargeDto) {
		try {
			
			dischargeDto.setStatus('A');
			dischargeDto.setIsCancel('N');
			dischargeDto.setIsIcu('N');
			dischargeDto.setIsDeath('N');
			dischargeDto.setIsHandoverMedication('N');
			dischargeDto.setIsShowPatientByBed('N');
			
			Response pddRes = iDueForDischargeDao.updatePDD(dischargeDto);
		     if(pddRes.getStatus().equals(SUCCESS))
		            {
			             
				/*
				 * HttpServletRequest request = ServletUtils.getCurrentRequest(); String BASEURL
				 * = ServletUtils.getBaseUrl(request); CloseableHttpClient httpClient =
				 * HttpClientBuilder.create().build(); HttpPost post = new
				 * HttpPost(BASEURL+Constants.BILLING_CANCEL_DISCHARGE);
				 * 
				 * 
				 * Gson g = new Gson(); ObjectMapper mapper = new ObjectMapper(); //String json
				 * = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dischargeDto);
				 * post.addHeader("Content-Type","application/json");
				 * //System.out.print(g.toJson(dischargeDto)); post.setEntity(new
				 * StringEntity(g.toJson(dischargeDto))); HttpResponse result =
				 * httpClient.execute(post); HttpEntity resEntity =result.getEntity(); String
				 * apiOutput = EntityUtils.toString((org.apache.http.HttpEntity) resEntity);
				 * //System.out.println("apiOutput"+apiOutput); JSONObject response = new
				 * JSONObject(apiOutput); if(response.get("status").equals(SUCCESS)) { return
				 * iDueForDischargeDao.canceDischarge(dischargeDto); } else {
				 * 
				 * throw new Exception();
				 * 
				 * }
				 */
		         }
			  
		} catch (Exception e) {
			
			  return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, e.getMessage());
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updatePDD(DischargeDto dischargeDto) throws ApplicationException {
		try {
			return iDueForDischargeDao.updatePDD(dischargeDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getPatientDischargeOrNot(DischargeDto dischargeDto) {

		try {
			return iDueForDischargeDao.getPatientDischargeOrNot(dischargeDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response markDischargeFromCasesheet(DischargeDto dischargeDto) {
		try {
			 
			Long DischargeCount = 1l;
			Response resCheck = iDueForDischargeDao.checkDischargeOrNot(dischargeDto);
			if(resCheck.getObject().equals(DischargeCount) ) {
				return new Response(ERROR, null, DISCHARGE, null, null);
			}else {
				
				iDueForDischargeDao.updatePDD(dischargeDto);
				
				 dischargeDto.setDischargeTime(dischargeDto.getCreatedDate());
				 Response res = iDueForDischargeDao.saveDischargeRequest(dischargeDto);
				 
				 if(res.getStatus().equals(SUCCESS) && res.getObject().equals('Y'))
				 {
					if(dischargeDto.getVisitTypeId()!=null && dischargeDto.getVisitTypeId()==IP)
					{
						Response admRes=iMarkedForDischargeDao.getAdmissionDetailsByAdmissionId(dischargeDto);
						
						AdmissionsForBatchProcessDto admissionsForBatchProcessDto=(AdmissionsForBatchProcessDto) admRes.getObject();
						admissionsForBatchProcessDto.setDischargeDateTime(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dischargeDto.getUnitId()), "yyyy/MM/dd HH:mm:ss"));
						iMarkedForDischargeService.postRoomRentChargesForDischargeInitiate(admissionsForBatchProcessDto);
					}
				 }
				return res;
			}
			
			 
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}



}
