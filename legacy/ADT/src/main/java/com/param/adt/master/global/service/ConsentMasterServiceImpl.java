package com.param.adt.master.global.service;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.master.global.dao.IConsentMasterDao;
import com.param.adt.master.global.dao.IConsentTypeMasterDao;
import com.param.adt.master.global.dao.IPatientConsentDocumentDetailsDo;
import com.param.adt.master.global.dao.IPatientConsentVideoDetailsDao;
import com.param.adt.master.global.dto.ConsentMasterDto;
import com.param.adt.master.global.dto.ConsentTypeMasterDto;
import com.param.adt.master.global.model.ConsentMaster;
import com.param.adt.master.global.model.PatientConsentVideoDetails;
import com.param.common.dto.CommonResponse;
import com.param.global.common.DocUploadUtility;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.dto.FormsEngineTransactionDto;
import com.param.global.dto.PatientConsentDocumentDetailsDto;
import com.param.global.dto.PatientConsentMapperDto;
import com.param.global.dto.PatientConsentVideoDetailsDto;
import com.param.global.dto.TemplateMasterDto;
import com.param.global.dto.UnitTemplateConfigDto;
import com.param.global.opd.service.IEncounterMasterService;
import com.param.global.org.dao.IGlobalOrganisationMasterDao;
import com.param.global.org.dto.OrganizationMasterDto;
import com.param.global.service.ITemplateMasterService;
import com.param.opd.common.CommonDateUtils;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;
@SuppressWarnings({"rawtypes","unchecked"})
@Service
public class ConsentMasterServiceImpl implements IConsentMasterService,ICommonConstants
{

	@Autowired
	IConsentTypeMasterDao iConsentTypeDao;
	
	@Autowired
	private IConsentMasterDao iConsentMasterDao;
	
	@Autowired
	IEmrService EmrService;
	
	@Autowired
	DocUploadUtility docUploadUtility;
	
	@Autowired
	IPatientConsentDocumentDetailsDo iPatientConsentDocumentDetailsDo;
	
	@Autowired
	IPatientConsentVideoDetailsDao iPatientConsentVideoDetailsDao;
	
	@Autowired
	IGlobalOrganisationMasterDao iGlobalOrganisationMasterDao;
	
	@Autowired
	private ITemplateMasterService iTemplateMasterService;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	IEncounterMasterService iEncounterMasterService;
	
	@Override
	public Response addConsentMaster(ConsentMasterDto consentMasterDto) throws ApplicationException {
		try {

			Response deptResponse = iConsentMasterDao.getConsentByName(consentMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {
				/*consentMasterDto
						.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););
				consentMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				ConsentMaster consentMaster = new ConsentMaster();
				ConsentMasterDto consentMasDto = new ConsentMasterDto();
				
				Response res =iConsentMasterDao.addConsentMaster(consentMasterDto);
				consentMaster =(ConsentMaster) res.getObject();
				
				List<ConsentTypeMasterDto> dto1 = new ArrayList<>();
				ConsentTypeMasterDto dto = new ConsentTypeMasterDto();
				dto.setConsentTypeMasterId(consentMasterDto.getConsentTypeMasterId());
				
				Response ConsentTyperes =  iConsentTypeDao.getConsentTypeById(dto);
				dto1=ConsentTyperes.getListObject();
				
				consentMasDto.setConsentTypeMasterName(dto1.get(0).getConsentTypeMasterName());
				consentMasDto.setConsentMasterId(consentMaster.getConsentMasterId());
				consentMasDto.setConsentMasterName(consentMaster.getConsentMasterName());
				consentMasDto.setConsentTypeMasterId(consentMasterDto.getConsentTypeMasterId());
				consentMasDto.setOrganizationId(consentMasterDto.getOrganizationId());
				consentMasDto.setCreatedBy(consentMasterDto.getCreatedBy());
				consentMasDto.setCreatedDate(consentMasterDto.getCreatedDate());
				consentMasDto.setUpdatedBy(consentMasterDto.getUpdatedBy());
				consentMasDto.setUpdatedDate(consentMasterDto.getUpdatedDate());
				consentMasDto.setStatus('A');
				
				CommonResponse response = new CommonResponse();
				response.setMessage("Consent master Inserted Successfully");
				Request request = new Request();
				Payload payload = new Payload();
				payload.setActionType("create");
				payload.setData(consentMasDto);
				request.setKey(MasterKeyConstants.Consent.toString());
				request.setPayload(payload);
				//iOtimsService.publish(request);
				//EmrService.publish(request);
				
				return new Response(SUCCESS, null, COMMON_SUCCESS, null, consentMaster);
			}
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateConsentMaster(ConsentMasterDto consentMasterDto) throws ApplicationException {
		try {
			Response deptResponse = iConsentMasterDao.getConsentByNameNotId(consentMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {
				/*consentMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				iConsentMasterDao.updateConsentMaster(consentMasterDto);
				
				List<ConsentTypeMasterDto> dto1 = new ArrayList<>();
				ConsentTypeMasterDto dto = new ConsentTypeMasterDto();
				dto.setConsentTypeMasterId(consentMasterDto.getConsentTypeMasterId());
				
				Response ConsentTyperes =  iConsentTypeDao.getConsentTypeById(dto);
				dto1=ConsentTyperes.getListObject();
				
				ConsentMasterDto consentMasDto = new ConsentMasterDto();
				consentMasDto.setConsentTypeMasterName(dto1.get(0).getConsentTypeMasterName());
				consentMasDto.setConsentMasterId(consentMasterDto.getConsentMasterId());
				consentMasDto.setConsentMasterName(consentMasterDto.getConsentMasterName());
				consentMasDto.setConsentTypeMasterId(consentMasterDto.getConsentTypeMasterId());
				consentMasDto.setOrganizationId(consentMasterDto.getOrganizationId());
				consentMasDto.setCreatedBy(consentMasterDto.getUpdatedBy());
				consentMasDto.setCreatedDate(consentMasterDto.getUpdatedDate());
				consentMasDto.setUpdatedBy(consentMasterDto.getUpdatedBy());
				consentMasDto.setUpdatedDate(consentMasterDto.getUpdatedDate());
				consentMasDto.setStatus('A');
				
				CommonResponse response = new CommonResponse();
				response.setMessage("Consent master Inserted Successfully");
				Request request = new Request();
				Payload payload = new Payload();
				payload.setActionType("update");
				payload.setData(consentMasDto);
				request.setKey(MasterKeyConstants.Consent.toString());
				request.setPayload(payload);
				//iOtimsService.publish(request);
				//EmrService.publish(request);
				
				
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);
			}
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateStatusForConsent(ConsentMasterDto consentMasterDto) throws ApplicationException {
		try {
			Response deptResponse = iConsentMasterDao.getConsentById(consentMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				/*consentMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				iConsentMasterDao.updateConsentStatus(consentMasterDto);
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);

			} else {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			}
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getConsentMasterList(ConsentMasterDto consentMasterDto) throws ApplicationException {
		try {
			return iConsentMasterDao.getConsentMasterList(consentMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getConsentById(ConsentMasterDto consentMasterDto) throws ApplicationException {
		try {
			return iConsentMasterDao.getConsentById(consentMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveConsentList(Integer orgId,Integer unitId) throws ApplicationException {
		try {
			return iConsentMasterDao.getActiveConsentList(orgId,unitId);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getConsentCount(ConsentMasterDto consentMasterDto) throws ApplicationException {
		try {
			return iConsentMasterDao.getCount(consentMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getConsentListByConsentTypeId(ConsentMasterDto consentMasterDto) throws ApplicationException {
		try {
			return iConsentMasterDao.getConsentListByConsentTypeId(consentMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response saveDocumentConsentMaster(ConsentMasterDto consentMasterDto) throws ApplicationException {
		try {
			Date date = TimeZoneComponent.getDateByzone(consentMasterDto.getUnitId() != null ? consentMasterDto.getUnitId() : 0);
			String dateTime = GlobalCommonDateUtils.getStringDate(date, "dd-M-yyyy HH:mm:ss");
			
				if(consentMasterDto.getFile().getSize()>0)
			      {
					com.param.global.common.Response orgRes =  iGlobalOrganisationMasterDao.getOrganiazationFolderPath(consentMasterDto.getOrganizationId());
				    OrganizationMasterDto organizationMasterDto = (OrganizationMasterDto) orgRes.getObject();
				 	 String docPath="";
				   
				    	 docPath =  docUploadUtility.updateConsentMasterForFileUploading(consentMasterDto.getFile(),organizationMasterDto.getOrganizationFolder(), consentMasterDto.getConsentMasterId(),consentMasterDto.getOrganizationId(),consentMasterDto.getUnitId());
			       
					consentMasterDto.setFilePath(docPath);
					consentMasterDto.setDocumentName(consentMasterDto.getDocumentName());
			        consentMasterDto.setCreatedDate(dateTime);
			        consentMasterDto.setUpdatedDate(dateTime);
			        iConsentMasterDao.updateConsentMasterForFileUploading(consentMasterDto);
			      }
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getPatientConsent(PatientConsentMapperDto dto) throws ApplicationException {
		try {
			return iConsentMasterDao.getPatientConsent(dto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response savePatientConsent(List<PatientConsentMapperDto> list) throws ApplicationException {
		try {
			
			List<Integer> consentTransactionIdList = new ArrayList<>();
			String dateTime = GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(0), "dd-M-yyyy HH:mm:ss");
			
			UnitTemplateConfigDto reqDto = new UnitTemplateConfigDto();
			reqDto.setUnitId(list.get(0).getUnitId());
			com.param.global.common.Response tempalteConfigRes = iTemplateMasterService.getTemplateConfigByUnit(reqDto);
			reqDto = (UnitTemplateConfigDto) tempalteConfigRes.getObject();
			
			for (PatientConsentMapperDto dto : list) {
				dto.setCreatedDate(dateTime);
				dto.setUpdatedDate(dateTime);
				
				String formUrl = null;
				if(dto.getFormTemplateId()!= null) {
					HttpHeaders headers = new HttpHeaders();
					headers.set("X-Api-Key",reqDto.getApiKey()); 
					headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
					headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
					headers.setContentType(MediaType.APPLICATION_JSON);
					
					org.springframework.http.HttpEntity<String> request = new org.springframework.http.HttpEntity<>(headers);
					 JSONObject obj = null;
						 URI url= UriComponentsBuilder.fromHttpUrl(reqDto.getTemplateUrl()+"generateFormUrl")
						            .queryParam("templateId",dto.getFormTemplateId())
						 .queryParam("age",dto.getAge().toString())
						 .queryParam("gender",dto.getGenderName()).build().toUri();		 
						 
						 ResponseEntity<String> stringResponse = restTemplate.exchange(url,HttpMethod.POST ,request, String.class);
						   obj = new JSONObject( stringResponse.getBody() );
						  formUrl =  obj.getString("formUrl");
						  String conversationId =  obj.getString("conversationId");
						   
						 if(conversationId!=null ){
							 dto.setConversationId(conversationId);
						 }
						 if(formUrl!=null ){
							 dto.setTemplateFormUrl(formUrl);
						 }
				}
				
				Response res = iConsentMasterDao.savePatientConsent(dto);
				Integer  consentTransactionId = (Integer) res.getObject();
				consentTransactionIdList.add(consentTransactionId);
			}
			return new Response(SUCCESS, null, COMMON_SUCCESS, consentTransactionIdList, null);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response uploadpatientConsentDoc(PatientConsentDocumentDetailsDto dto) throws ApplicationException {
		try {
			Date date = TimeZoneComponent.getDateByzone(dto.getUnitId() != null ? dto.getUnitId() :0);
			String dateTime = GlobalCommonDateUtils.getStringDate(date, "dd-M-yyyy HH:mm:ss");
			
				if(dto.getFile().getSize()>0)
			      {
					com.param.global.common.Response orgRes =  iGlobalOrganisationMasterDao.getOrganiazationFolderPath(dto.getOrganizationId());
				    OrganizationMasterDto organizationMasterDto = (OrganizationMasterDto) orgRes.getObject();
				 	 String docPath="";
				   
				    	 docPath =  docUploadUtility.updateConsentMasterForFileUploading(dto.getFile(),organizationMasterDto.getOrganizationFolder(), dto.getConsentTransactionId(),dto.getOrganizationId(),dto.getUnitId());
			       
					dto.setDocumentPath(docPath);
					dto.setDocumentName(dto.getDocumentName());
			        dto.setCreatedDate(dateTime);
			        dto.setUpdatedDate(dateTime);
			        iPatientConsentDocumentDetailsDo.uploadpatientConsentDoc(dto);
			      }
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response uploadpatientConsentVideo(PatientConsentVideoDetailsDto dto) {
		try {
			
			if (dto.getOrganizationId() > 0) 
			{
				com.param.global.common.Response orgRes = iGlobalOrganisationMasterDao.getOrganiazationFolderPath(dto.getOrganizationId());
				OrganizationMasterDto organizationMasterDto = (OrganizationMasterDto) orgRes.getObject();
				String docPath = "";
				PatientConsentMapperDto mpprDto = new PatientConsentMapperDto();
				mpprDto.setOrganizationId(dto.getOrganizationId());
				mpprDto.setUnitId(dto.getUnitId());
				mpprDto.setConsentTransactionId(dto.getConsentTransactionId());
					docPath = docUploadUtility.uploadpatientConsentVideo(dto.getFile(),
							organizationMasterDto.getOrganizationFolder(), mpprDto);
				
					dto.setVideoPath(docPath);
					iPatientConsentVideoDetailsDao.uploadpatientConsentVideo(dto);
			}
			
			
			return new Response<>(SUCCESS, SUCCESS_CODE, "", null, null);
		} catch (Exception e) {
			
			return new Response(ERROR, null, null, null, null);
		}
		
	}

	@Override
	public Response getConsentListByAdmissionAndEncounterId(PatientConsentMapperDto dto)
			throws ApplicationException {
		try {
			return iConsentMasterDao.getConsentListByAdmissionAndEncounterId(dto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response cancelConsentPrint(Integer consentTransactionId) throws ApplicationException {
		try {
			return iConsentMasterDao.cancelConsentPrint(consentTransactionId);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public ByteArrayOutputStream getPatientConsentPrintPdfFile(PatientConsentMapperDto dto)
			throws ApplicationException {
		try{
			return iConsentMasterDao.createConsentPrintPDF(dto);
		} catch (Exception e) {
			
		}
		return null;
	}

	@Override
	public Response getConsentDocsListByMapperId(PatientConsentMapperDto dto)
			throws ApplicationException {
		try {
			return iPatientConsentDocumentDetailsDo.getConsentDocsListByMapperId(dto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getConsentVideoListByMapperId(PatientConsentMapperDto patientConsentMapperDto)
			throws ApplicationException {
		try {
			return iPatientConsentVideoDetailsDao.getConsentDocsListByMapperId(patientConsentMapperDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response deactiveConsentVideo(PatientConsentVideoDetails dto) throws ApplicationException {
		try {
			return iPatientConsentVideoDetailsDao.deactiveConsentVideo(dto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response deactiveConsentDocs(PatientConsentDocumentDetailsDto dto) throws ApplicationException {
		try {
			return iPatientConsentDocumentDetailsDo.deactiveConsentDocs(dto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getConsentHtmlString(Integer consentTransactionId) throws ApplicationException {
		try {
			return iConsentMasterDao.getConsentHtmlString(consentTransactionId);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);	}

	@Override
	public Response replaceConsentAnotations(PatientConsentMapperDto dto) throws ApplicationException {
		try {
			
			
			String body = dto.getHtmlTemplateData()+"<br>";
			body = body.replace("<li>","<li>&bull;   ");
			Map<String,String> qMapResultsHearder = new HashMap<String, String>();
	    	Pattern pattern = Pattern.compile("@\\w+");
			List<String> paramNames = new ArrayList<>();
			Matcher matcher = pattern.matcher(dto.getHtmlTemplateData());
			while (matcher.find())
			{
			    paramNames.add(matcher.group());
			}
			if(paramNames.size()>0)
			{
				Integer count = 0;
				String tempContent = dto.getHtmlTemplateData();
				for(String q : paramNames)
				{
					if (qMapResultsHearder.containsKey(q))
					{
						tempContent = tempContent.replace(q, qMapResultsHearder.get(q));
					}else{
						count++;
					}
				}
				if(count > 0)
				{
					dto.setTemplateDesc(tempContent);
					com.param.global.common.Response  res = iEncounterMasterService.appendCensentData(dto);
					dto = (PatientConsentMapperDto) res.getObject();
					tempContent = dto.getTemplateDesc();
				}
				dto.setHtmlTemplateData(tempContent);
				body = tempContent; 
			}
			
			return new Response<>(SUCCESS, SUCCESS_CODE, "", null, body);
			
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);	
		
	}

}
