package com.param.global.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.itextpdf.html2pdf.HtmlConverter;
import com.param.data.dto.MailReqDto;
import com.param.entity.lis.histo.TTemplateResult;
import com.param.entity.lis.transaction.LabSampleDetailsMaster;
import com.param.global.common.DocUploadUtility;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.common.SmsUtility;
import com.param.global.dao.ITemplateMasterDao;
import com.param.global.dao.ITemplateTransactionDao;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.global.dao.ITemplateQuetionaryPrintMapperDao;
import com.param.global.dto.AdmissionDto;
import com.param.global.dto.FormsEngineTransactionDto;
import com.param.global.dto.PatientDetailsDto;
import com.param.global.dto.RadiologyOrdersListDto;
import com.param.global.dto.SpecialityMasterDto;
import com.param.global.dto.TemplateMasterDto;
import com.param.global.dto.TemplateReqDto;
import com.param.global.dto.TemplateResponseDto;
import com.param.global.dto.TemplateTransactionDto;
import com.param.global.dto.UnitApplicationConfigurationDto;
import com.param.global.dto.UnitTemplateConfigDto;
import com.param.global.dto.global.TTemplateResultDto;
import com.param.global.mail.IMailingService;
import com.param.global.mail.MailingServiceImpl;
import com.param.global.opd.service.IEncounterMasterService;
import com.param.global.org.dao.IGlobalOrganisationMasterDao;
import com.param.global.org.dto.OrganizationMasterDto;
import com.param.global.org.dto.UnitMasterDto;
import com.param.global.unit.dto.MedallRequestResponseDto;
import com.param.opd.common.CommonDateUtils;
import com.param.utility.TimeZoneComponent;
import com.param.workbench.service.IRISWorkbenchService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.select.Elements;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class TemplateTransactionServiceImpl  implements ITemplateTransactionService,IError,ICommonConstants{

	private static final Logger logger = Logger.getLogger(TemplateTransactionServiceImpl.class);

	@Autowired
	private ITemplateTransactionDao iTemplateTransactionDao;
	
	@Autowired
	private ITemplateMasterService iTemplateMasterService;
	
	@Autowired
	IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;
	
	@Autowired
	SmsUtility smsUtility;
	
	@Autowired
	IOrderMasterService iOrderMasterService;
	
	
	@Autowired
	IRISWorkbenchService iRISWorkbenchService;
	
	
	@Autowired
	ITemplateMasterDao iTemplateMasterDao;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	IMailingService iMailingService;
	
	@Autowired
	private IPatientRegistrationService iPatientRegistrationService;
	
	@Autowired
	IEncounterMasterService iEncounterMasterService;
	
	@Autowired
	private ITemplateQuetionaryPrintMapperDao iTemplateQuetionaryPrintMapperDao;
	
	@Autowired
	IGlobalOrganisationMasterDao iGlobalOrganisationMasterDao;
	
	@Autowired
	DocUploadUtility docUploadUtility;
	
	@Override
	@Transactional
	public Response saveTemplateTransaction(TemplateTransactionDto dto) throws ApplicationException {
		try{
			dto.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			dto.setStatus(ACTIVE);
			Response res =  iTemplateTransactionDao.saveTemplateTransaction(dto);
			
			if(res.getStatus()==SUCCESS && dto.getIsFinalRelease()!=null && dto.getIsFinalRelease()=='Y' && dto.getVisitTypeId()==OP){
				String senderId = null;
				UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
				unitApplicationConfigurationDto.setOrganizationId(dto.getOrgId());
				unitApplicationConfigurationDto.setUnitId(dto.getUnitId());
				com.param.global.common.Response  appConfigRes = iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
			    UnitApplicationConfigurationDto appDto = (UnitApplicationConfigurationDto)appConfigRes.getObject();
				if(appDto != null){
					senderId = appDto.getSenderId();
				}
				else{
					return new Response(ERROR, null, "Unit application configuraion not yet defined.!!", null, null);
				}
				
			}
			// for review appointment applicable
			if(res.getStatus()==SUCCESS && dto.getIsFinalRelease()!=null && dto.getIsFinalRelease()=='Y') {
				  iOrderMasterService.assignReviewApplicableDate(dto.getOrderDetailsId());
			}
			return res;
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response updateTemplateTransaction(TemplateTransactionDto dto) throws ApplicationException {
		try{
			dto.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			dto.setStatus(ACTIVE);
			if(dto.getTemplateTransactionId() == 0 ){
				Response res1= iTemplateTransactionDao.checkTemplateTransactionExits(dto);
				if(res1.getObject() != null ){
					return new Response(ERROR, null, "This transaction already process. ", null, null);
				}
			}
			
			
			Response res= iTemplateTransactionDao.updateTemplateTransaction(dto);
			if(res.getStatus()==SUCCESS && dto.getIsFinalRelease()!=null && dto.getIsFinalRelease()=='Y' && dto.getVisitTypeId()==OP){
				String senderId = null;
				UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
				unitApplicationConfigurationDto.setOrganizationId(dto.getOrgId());
				unitApplicationConfigurationDto.setUnitId(dto.getUnitId());
				com.param.global.common.Response  appConfigRes = iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
			    UnitApplicationConfigurationDto appDto = (UnitApplicationConfigurationDto)appConfigRes.getObject();
				if(appDto != null){
					senderId = appDto.getSenderId();
				}
				else{
					return new Response(ERROR, null, "Unit application configuraion not yet defined.!!", null, null);
				}
				
			}
			// for review appointment applicable
			if(res.getStatus()==SUCCESS && dto.getIsFinalRelease()!=null && dto.getIsFinalRelease()=='Y') {
				  iOrderMasterService.assignReviewApplicableDate(dto.getOrderDetailsId());
			}
			return res;
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getTemplateByPatient(TemplateReqDto dto) throws ApplicationException {
		try{
			return iTemplateTransactionDao.getTemplateByPatient(dto);
		}catch(Exception e){
			
			throw e;
		}
	}

	
	@Override
	@Transactional
	public Response getTemplateByPatientInOtims(TemplateReqDto templateReqDto) throws ApplicationException {
		try{
			return iTemplateTransactionDao.getTemplateByPatientInOtims(templateReqDto);
		}catch(Exception e){
			
			throw e;
		}
	}

	
	@Override
	@Transactional
	public Response getQuestionaaiireDetails(TemplateReqDto dto) throws ApplicationException {
		try{
			return iTemplateTransactionDao.getQuestionaaiireDetails(dto);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getTemplateQuestionaaire(TemplateReqDto dto) throws ApplicationException {
		try{
			return iTemplateMasterService.getTemplateByDoctorOrSpeciality(dto);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@Override
	@Transactional
	public Response getTemplateQuestionaaireRadiology(TemplateReqDto dto) throws ApplicationException {
		try{
			return iTemplateMasterService.getTemplateQuestionaaireRadiology(dto);
		}catch(Exception e){
			
			throw e;
		}
	}
	

	@Override
	@Transactional
	public Response saveAdmissionConcent(AdmissionDto dto) throws ApplicationException {
		try{
			return iTemplateTransactionDao.saveAdmissionConcent(dto);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response updateTemplateAsInactive(TemplateTransactionDto dto) throws ApplicationException {
		try{
			return iTemplateTransactionDao.updateTemplateAsInactive(dto);
		}catch(Exception e){
			
			throw e;
		}
	}
	public ByteArrayOutputStream getRediologyReport(int transactionId, int orgId, Integer unitId) {
		
		 try {
			 
			  String json = "{\r\n" + 
			  		"	                 \"appName\": \"HIS\",\r\n" + 
			  		"	                 \"appSecretKey\": \"f306b6eccd9d5069d2dc71afe139271a\",\r\n" + 
			  		"	                 \"reportDesignFilePath\": \"RadiologyReport.rpt\",\r\n" + 
			  		"	                 \"reportFormat\": \"PDF\",\r\n" + 
			  		"	                 \"exportFileName\": \"RadiologyReport\",\r\n" + 
			  		"	                 \"reportParams\": [\r\n" + 
			  		"	                   {\r\n" + 
			  		"	                   \"paramName\": \"template_transaction_id\",\r\n" + 
			  		"	                   \"paramType\": \"Integer\",\r\n" + 
			  		"	                   \"paramValue\": "+transactionId+"\r\n" + 
			  		"	                   }\r\n"+
			  		"	                 ]\r\n" + 
			  		"}";
			 ByteArrayOutputStream byteOutPutStream = new ByteArrayOutputStream();
				CloseableHttpClient httpClient = HttpClientBuilder.create().build();
				String API_URL = iUnitApplicationBillingConfigurationDao.getURLForPdfGeneration(orgId, unitId);

				if (API_URL == null)
					return null;

				HttpPost post = new HttpPost(API_URL);
				post.addHeader("Content-Type", "application/json");
				post.setEntity(new StringEntity(json));

				HttpResponse result = httpClient.execute(post);
				HttpEntity resEntity = result.getEntity();
				resEntity.writeTo(byteOutPutStream);
				return byteOutPutStream;
		  } catch (Exception e) {
		      logger.error("getRediologyReport failed", e);
		      return null;
		  }


	}

	@Override
	@Transactional
	public Response getSavedTemplateCount(TemplateTransactionDto templateTransactionDto) throws ApplicationException {
		try{
			return iTemplateTransactionDao.getSavedTemplateCount(templateTransactionDto);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response updateTemplateTransactionRIS(TemplateTransactionDto dto) throws ApplicationException {

		try{
			dto.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			dto.setStatus(ACTIVE);
			if(dto.getTemplateTransactionId() == 0 ){
				Response res1= iTemplateTransactionDao.checkTemplateTransactionExits(dto);
				if(res1.getObject() != null ){
					return new Response(ERROR, null, "This transaction already process. ", null, null);
				}
			}
			
			Response res= iTemplateTransactionDao.updateTemplateTransaction(dto);
			if(res.getStatus()==SUCCESS && dto.getIsFinalRelease()!=null && dto.getIsFinalRelease()=='Y' && dto.getVisitTypeId()==OP){
				String senderId = null;
				UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
				unitApplicationConfigurationDto.setOrganizationId(dto.getOrgId());
				unitApplicationConfigurationDto.setUnitId(dto.getUnitId());
				com.param.global.common.Response  appConfigRes = iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
			    UnitApplicationConfigurationDto appDto = (UnitApplicationConfigurationDto)appConfigRes.getObject();
				if(appDto != null){
					senderId = appDto.getSenderId();
				}
				else{
					return new Response(ERROR, null, "Unit application configuraion not yet defined.!!", null, null);
				}
				/*if(dto.getMobileNumber()!=null && senderId!=null){
					smsUtility.sendRadiologyReportReleaseMessage(dto.getPatientName(), dto.getMobileNumber(), senderId);
				}*/
				/*if(dto.getPatientMail()!=null && dto.getPatientMail()!=""){
					
					HttpServletResponse servletResponse = null;
					ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
					RadiologyOrdersListDto dto1 = new RadiologyOrdersListDto();
					dto1.setTemplateContent(dto.getTemplateObject());
					dto1.setPatientName(dto.getPatientName());
					dto1.setUnitId(dto.getUnitId());
					dto1.setOrganizationId(dto.getOrgId());
					dto1.setOrderDate(dto.getOrderDate());
					dto1.setUhidNumber(dto.getUhidNumber());
					dto1.setAgeString(dto.getAgeString());
					dto1.setGenderCode(dto.getGenderCode());
					dto1.setReferralName(dto.getReferralName());
					dto1.setIsForMail(dto.getIsForMail()!=null ? dto.getIsForMail(): null);
					
					byteStream = iRISWorkbenchService.printRadiologyReport(dto1);
					
					
				    String  StringFileNameDate = GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId).getTime(),"_dd_MM_yyyy_HH_mm_ss");
	                String fileName = dto.getUhidNumber().toUpperCase().trim()+StringFileNameDate+".pdf";
					new MailingServiceImpl().sendMail(dto.getPatientMail(), fileName, byteStream,appDto);
				}*/
				
			}
			// for review appointment applicable
			if(res.getStatus()==SUCCESS && dto.getIsFinalRelease()!=null && dto.getIsFinalRelease()=='Y') {
				  iOrderMasterService.assignReviewApplicableDate(dto.getOrderDetailsId());
				  iEncounterMasterService.checkFeedBackFormSubmitted(dto);
			}
			return res;
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@org.springframework.transaction.annotation.Transactional
	public Response saveTemplateSummery(TemplateResponseDto dto) throws ApplicationException {
			try{
				Response templateRes = iTemplateMasterDao.getTemplateByConversationId(dto);
				Date date = null;//TimeZoneComponent.getDateByzone(dto);
				Response response=iTemplateQuetionaryPrintMapperDao.updateTemplateTransactionData(dto);
				TemplateTransactionDto dto1  = new TemplateTransactionDto();
				Response templateResultRes =null;
				if(templateRes.getObject()!=null)
				{
					FormsEngineTransactionDto fTDto = (FormsEngineTransactionDto)templateRes.getObject();
					 date = TimeZoneComponent.getDateByzone(fTDto.getUnitId());
					 String credatedDate = GlobalCommonDateUtils.getStringDate(date, "dd-MM-yyyy HH:mm:ss");
					if(fTDto.getTemplateTypeId()==9)//lab
					{
						//TTemplateResultDto templateResDto = new TTemplateResultDto();
						TTemplateResultDto templateTraDto = new TTemplateResultDto();
						templateTraDto.setTemplateResId(fTDto.getTemplateResId());
						templateTraDto.setOrgId(fTDto.getOrgId());
						templateTraDto.setOrgUnitId(fTDto.getUnitId());
						templateTraDto.settSubSpecimanId(fTDto.gettSubSpecimanId());
						templateTraDto.setLabTemplateId(fTDto.getTemplateId());
						templateTraDto.setReportNo(fTDto.getReportNo());
						templateTraDto.setStainingId(fTDto.getStainingId());
						templateTraDto.setLabSampleDtlsId(fTDto.getLabSampleDtlsId());
						templateTraDto.setCreatedBy(fTDto.getCreatedBy());
						templateTraDto.setCreatedDate(date.getTime());
						templateTraDto.setSampleStatusId(fTDto.getSampleStatusId());
						templateTraDto.setOrderDetailsId(fTDto.getOrderDetailsId());
						templateTraDto.setTemplateResult(dto.getSummery());
						templateTraDto.setConversationId(fTDto.getConversationId());
						templateTraDto.setPostbackURL(dto.getPostbackURL());
						templateTraDto.setCreatedBy(fTDto.getCreatedBy());
						templateTraDto.setUpdatedBy(fTDto.getCreatedBy());
						templateTraDto.setFormData(dto.getFormData());
						JSONObject json = new JSONObject(dto.getFormData());  
						templateTraDto.setTemplateUrl(json.get("formUrl").toString());
						templateResultRes = this.saveTemplateResult(templateTraDto);
						return templateResultRes;
					}
					else {
						TemplateTransactionDto tTraDto = new TemplateTransactionDto();
						tTraDto.setTemplateTransactionId(fTDto.getTemplateTransactionId());
						tTraDto.setTemplateId(fTDto.getTemplateId());
						tTraDto.setSpecialityId(fTDto.getSpecialityId());
						tTraDto.setTitle(fTDto.getTitle());
						tTraDto.setSubSpecialityId(fTDto.getSampleStatusId());
						tTraDto.setAdmissionId(fTDto.getAdmissionId());
						tTraDto.setEncounterId(fTDto.getEncounterId());
						tTraDto.setDoctorId(fTDto.getDoctorId());
						tTraDto.setDiagnosisId(fTDto.getDiagnosisId());
						tTraDto.setPatientId(fTDto.getPatientId());
						tTraDto.setSignedBy(fTDto.getCreatedBy());
						tTraDto.setSignedDate(fTDto.getCreatedDate());
						tTraDto.setJsonObject(dto.getSummery());
						tTraDto.setOrgId(fTDto.getOrgId());
						tTraDto.setUnitId(fTDto.getUnitId());
						tTraDto.setCreatedBy(fTDto.getCreatedBy());
						tTraDto.setCreatedDate(credatedDate);
						tTraDto.setUpdatedBy(fTDto.getCreatedBy());
						tTraDto.setOrderDetailsId(fTDto.getOrderDetailsId());
						tTraDto.setUpdatedDate(fTDto.getCreatedDate());
						tTraDto.setmIcpcSystemId(fTDto.getmIcpcSystemId());
						tTraDto.setmIcpcId(fTDto.getmIcpcId());
						tTraDto.setIsProvisionalRelease(fTDto.getIsProvisionalRelease());
						tTraDto.setIsFinalRelease(fTDto.getIsFinalRelease());
						tTraDto.setIsQuestionary(fTDto.getIsQuestionary());
						tTraDto.setConversationId(fTDto.getConversationId());
						tTraDto.setTemplateTransactionId(fTDto.getTemplateTransactionId());
						tTraDto.setTemplateObject(dto.getSummery());
						tTraDto.setTemplateHtml(dto.getHtml());
						tTraDto.setPostbackURL(dto.getPostbackURL());
						tTraDto.setAppointmentId(fTDto.getAppointmentId());
						tTraDto.setAdmNoteId(fTDto.getAdmNoteId());
						tTraDto.setCreatedBy(fTDto.getCreatedBy());
						tTraDto.setUpdatedBy(fTDto.getCreatedBy());
						tTraDto.setFormData(dto.getFormData());
						tTraDto.setTemplateTypeId(fTDto.getTemplateTypeId());
						JSONObject json = new JSONObject(dto.getFormData());  
						tTraDto.setTemplateUrl(json.get("formUrl").toString());
						JSONArray arrayList = (JSONArray) json.get("json");
						
						
						
						if(fTDto.getTemplateTransactionId()!=null && fTDto.getTemplateTransactionId()>0)
							templateResultRes = this.updateTemplateTransaction(tTraDto);
//							templateResultRes = updateTemplateTrasactionData(tTraDto);
						else	
							templateResultRes = this.saveTemplateTransaction(tTraDto);
						
						Integer tempTranId=(Integer) templateResultRes.getObject();
						fTDto.setTemplateTransactionId(tempTranId);
//						if(tempTranId != null && tempTranId !=0) {
//							Response rest1=iTemplateQuetionaryPrintMapperDao.updateTemplateQuetionaryPrintData(tempTranId);
//						}
						if (arrayList.length() > 0) {
							for (int i = 0; i < arrayList.length(); i++) {
								JSONObject listObj = arrayList.getJSONObject(i);
								TemplateQuetionaryPrintDto saveDto=new TemplateQuetionaryPrintDto();
								saveDto.setQueTitle(listObj.get("queTitle") != null ? listObj.get("queTitle").toString() : "");
								saveDto.setQueType(listObj.get("queType") != null ? listObj.get("queType").toString() :"");
								saveDto.setQueuId(listObj.get("queUId") != null ? listObj.get("queUId").toString(): "");
								saveDto.setSectionTitle(listObj.get("sectionTitle") != null ? listObj.get("sectionTitle").toString() : "");
								saveDto.setSlugValue(listObj.get("slugValue") != null ? listObj.get("slugValue").toString():"");
								saveDto.setSlugValueNew(listObj.get("slugValueNew") != null ? listObj.get("slugValueNew").toString(): "");
								saveDto.setAllowToPrint(Boolean.parseBoolean(listObj.get("allowToPrint").toString()));
								saveDto.setAnswer(listObj.get("answer") != null ? listObj.get("answer").toString() : "");
								saveDto.setSummaryForPrint(listObj.get("SummaryForPrint") != null ? listObj.get("SummaryForPrint").toString() : "");
								saveDto.setTemplateTransactionId(fTDto.getTemplateTransactionId());
								saveDto.setStatus('A');
								saveDto.setUnitId(fTDto.getUnitId());
								saveDto.setOrganizationId(fTDto.getOrgId());
								saveDto.setAddedBy(fTDto.getCreatedBy());
								saveDto.setAddedDate(fTDto.getCreatedDate());
								saveDto.setUpdatedBy(fTDto.getCreatedBy());
								saveDto.setUpdatedDate(fTDto.getCreatedDate());
								Response rest1=iTemplateQuetionaryPrintMapperDao.saveTemplateQuetionaryPrintData(saveDto);
							}
							
						}
						
						if(fTDto.getTemplateTypeId()!=null && fTDto.getTemplateTypeId()==23 && fTDto.getUnitEmailId()!=null) //feedbackForm
						{
							  MailReqDto mailDto =new MailReqDto();
							  mailDto.setPatientName(fTDto.getPatientName());
							  mailDto.setUnitEmailId(fTDto.getUnitEmailId());
							  mailDto.setFormTemplate(dto.getSummery());
							  mailDto.setAdmissionId(dto.getAdmissionId());
							  mailDto.setEncounterId(dto.getEncounterId());
							  iMailingService.sendTemplateFormToDoctor(mailDto);
							  iEncounterMasterService.updateBackFormSubmitted(tTraDto);
							  
						}
						
						Integer templateTypeId = iTemplateMasterDao.getTemplateTypeId(fTDto.getTemplateId());
						
						if(templateTypeId != null && (templateTypeId == 22 || templateTypeId == 29 )) {
							
							dto1.setUnitId(fTDto.getUnitId());
							dto1.setOrgId(fTDto.getOrgId());
							dto1.setTemplateTransactionId(tempTranId);
							dto1.setPatientId(fTDto.getPatientId());
							dto1.setTemplateId(fTDto.getTemplateId());
							dto1.setConversationId(fTDto.getConversationId());
						}
						
						/*
						
						if(templateTypeId != null && (templateTypeId == 22 || templateTypeId == 29 )) {
									
								TemplateTransactionDto dto1  = new TemplateTransactionDto();
								dto1.setUnitId(fTDto.getUnitId());
								dto1.setOrgId(fTDto.getOrgId());
								dto1.setTemplateTransactionId(tempTranId);
								
								System.out.println("befour Template");
								Response res = iTemplateTransactionDao.printHTMLTemplate(dto1);
								System.out.println("after Template");
								Response res1 = iTemplateMasterDao.getOrgAndUnitName(fTDto.getUnitId());
								System.out.println("after orgUnit Template" + fTDto.getUnitId());
								if(res.getObject() != null) {
									
									System.out.println("In Template");
									TemplateTransactionDto templateTransactionDto = (TemplateTransactionDto) res.getObject(); 
								
								
									String completeTemplateContent = templateTransactionDto.getHeaderPart() + templateTransactionDto.getTemplateHtml();
		
								        Document doc = Jsoup.parse(completeTemplateContent);
				
								        // Handle input elements
								        Elements inputs = doc.select("input");
								        for (Element input : inputs) {
								            if (input.attr("type").equals("text")) {
								                input.replaceWith(new Element("label").text(input.val()));
								            }
								        }
				
								        Elements textareas = doc.select("textarea");
								        for (Element textarea : textareas) {
								            textarea.replaceWith(new Element("label").text(textarea.val()));
								        }
				
								        Elements spans = doc.select("span");
								        for (Element span : spans) {
								            if (span.hasClass("ant-select-arrow") ||
								                span.hasClass("anticon") ||
								                span.hasClass("ant-select-clear")) {
								                span.replaceWith(new Element("label").text(""));
								            }
								        }
				
								        Elements divs = doc.select("div");
								        for (Element div : divs) {
								            if (div.text().equals("Select Answer")) {
								                div.remove();
								            } else if (div.hasClass("ant-select-selector")) {
								                div.replaceWith(new Element("label").text(div.text()));
								            }
								        }
				
								        String resultHtml = doc.html();
								        resultHtml = resultHtml.replaceAll("checked=\"\"", "checked");
										
								        String fileName = "";
								        
								        if(templateTransactionDto.getPatientId() == null || templateTransactionDto.getPatientId() == 0){
								        	Response spec = iTemplateMasterDao.getSpecialityName(fTDto.getConversationId());
								        	
								        	if(spec.getObject()!= null ) {
								        		SpecialityMasterDto MasterDto = (SpecialityMasterDto)spec.getObject();
								        		fileName = MasterDto.getSpecialityName();
								        	}
								        }else {
								        	fileName = templateTransactionDto.getPatientName();
								        }
								        
								    	byte[] pdfBytes = convertHtmlToPdf(resultHtml);
		
										ByteArrayOutputStream outputStream = new ByteArrayOutputStream(pdfBytes.length);
										outputStream.write(pdfBytes, 0, pdfBytes.length);
										
										UnitMasterDto unit = (UnitMasterDto)res1.getObject();
										
										String docPath = docUploadUtility.updateNABHTemplate(outputStream, fileName,unit.getOrganizationName(), unit.getUnitName(),tTraDto.getTitle());
										iTemplateMasterDao.getUpdatePath(docPath,fTDto.getTemplateTransactionId());
								        
								}
								
							}*/
						dto1.setTemplateTypeId(dto1.getTemplateTypeId()==null && tTraDto.getTemplateTypeId()!=null ?  tTraDto.getTemplateTypeId():dto1.getTemplateTypeId());
						templateResultRes.setResObject(dto1);
						return templateResultRes;
					}
					
				}
				
				Response res = 	iTemplateTransactionDao.checkPateintConsent(dto.getConversationId(),dto.getHtml());
				if(res.getObject() !=null)
				{
					res.setResObject(dto1);
					return res;
				}

				
			}catch(Exception e){
				logger.error("saveTemplateSummery failed", e);
			}
			return new Response(ERROR, null, "Failed to save template.", null, null);
	}

	

	private static String cleanHtml(String htmlContent) {
		Document document = Jsoup.parse(htmlContent);
		document.outputSettings(new OutputSettings().syntax(OutputSettings.Syntax.xml));
		document.outputSettings().escapeMode(Entities.EscapeMode.xhtml);
		return document.html();
	}

	 public  byte[] convertHtmlToPdf(String htmlContent) throws Exception {
		 try {
			 String cleanedHtmlContent = cleanHtml(htmlContent);
			 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             InputStream htmlInputStream = new ByteArrayInputStream(cleanedHtmlContent.getBytes());
			 HtmlConverter.convertToPdf(htmlInputStream, outputStream);
			
			 byte[] pdfBytes = outputStream.toByteArray();
			 return pdfBytes;
		 }
		 catch (Exception e) {
			 System.err.println("Error during HTML to PDF conversion: " + e.getMessage());
			 e.printStackTrace();
			 throw new IOException("Failed to convert HTML to PDF", e);
		 }
	 }
	
	@Override
	@Transactional
	public Response saveTemplateResult(TTemplateResultDto tTemplateResultDto) throws ApplicationException {
	    try {
	        Response<LabSampleDetailsMaster, Integer> sampleStatusRes = null;
	        Response<TTemplateResult, Integer> tTemplateReslt =
	        		iTemplateTransactionDao.saveTemplateResult(tTemplateResultDto);
	        if (tTemplateReslt.getCode().equals(SUCCESS_200)) {

	          if (tTemplateResultDto.getSampleStatusId()!= 0) {

	            LabSampleDetailsMaster master =
	                new LabSampleDetailsMaster();
	            master.setOrgId(tTemplateResultDto.getOrgId());
	            master.setOrgUnitId(tTemplateResultDto.getOrgUnitId());
	            master.setCreatedBy(tTemplateResultDto.getCreatedBy());
	            master.setSampleStatusId(tTemplateResultDto.getSampleStatusId());
	            master.setLabSampleDtlsId(tTemplateResultDto.getLabSampleDtlsId());
	            master.setAssignUnit(tTemplateResultDto.getOrgUnitId());
	            master.setPerformedUnit(tTemplateResultDto.getOrgUnitId());
	            sampleStatusRes =
	            		iTemplateTransactionDao.updateLabSampleDetailsMaster(master);
	            
	            iOrderMasterService.assignReviewApplicableDate(tTemplateResultDto.getOrderDetailsId());
	            if (sampleStatusRes.getCode().equals(SUCCESS_200)) {
	              return new Response(SUCCESS, SUCCESS_200, "Template Result Saved Successfully.",
	                  null, null);
	            }

	          } else {
	            return new Response(SUCCESS, SUCCESS_200, "Template Result Saved Successfully.", null,
	                null);
	          }
	          return new Response(ERROR, ERR_500, "Failed to Save Template Result.", null, null);
	        } else {

	          return new Response(ERROR, ERR_500, "Failed to Save Template Result", null, null);
	        }

	      } catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}

	}

	@Override
	public Response sendTemplateURLS(TemplateTransactionDto dto) throws Exception {
		try{
			UnitTemplateConfigDto reqDto = new UnitTemplateConfigDto();
			reqDto.setUnitId(dto.getUnitId());
			Response tempalteConfigRes = iTemplateMasterService.getTemplateConfigByUnit(reqDto);
			reqDto = (UnitTemplateConfigDto) tempalteConfigRes.getObject();
			FormsEngineTransactionDto formsEngineTransactionDto = new FormsEngineTransactionDto();      
			if(tempalteConfigRes.getObject()!=null) {
			Response getPatientRes = iPatientRegistrationService.getPatientDetailsById(dto.getPatientId());
			PatientDetailsDto patientDetailsDto = (PatientDetailsDto) getPatientRes.getObject();
			
			List<TemplateMasterDto> templateList= null;
			Response templateMasterRes = null;
			if(dto.getTemplateTypeId()!=null && dto.getTemplateTypeId()==10 && dto.getSpecialityId()!= null )//1.Questionnaire form
			{
				TemplateReqDto tempalteReqDto = new TemplateReqDto();
				tempalteReqDto.setOrgId(dto.getOrgId());
				tempalteReqDto.setUnitId(dto.getUnitId());
				tempalteReqDto.setTemplateTypeId(dto.getTemplateTypeId());
				tempalteReqDto.setSpecialityId(dto.getSpecialityId()!= null ? dto.getSpecialityId() : null);
				templateMasterRes = iTemplateMasterService.getTemplatesByTypeAndSpeciality(tempalteReqDto);
				templateList = templateMasterRes.getListObject();
			}
			else if(dto.getTemplateTypeId()!=null && dto.getTemplateTypeId()==23)//2.feedback form
			{
				TemplateReqDto tempalteReqDto = new TemplateReqDto();
				tempalteReqDto.setOrgId(dto.getOrgId());
				tempalteReqDto.setUnitId(dto.getUnitId());
				tempalteReqDto.setTemplateTypeId(dto.getTemplateTypeId());
				templateMasterRes = iTemplateMasterService.getTemplatesByType(tempalteReqDto);
				templateList = templateMasterRes.getListObject();
			}
			else {
				if(dto.getTemplateId() != null){
				Response templateRes = iTemplateMasterService.getTemplateById(dto.getTemplateId() != null ? dto.getTemplateId() : 0);
				TemplateMasterDto templateDto = (TemplateMasterDto) templateRes.getObject();
				templateList = Arrays.asList(templateDto);
				}
			}
			
			Integer age = 0;
			if(patientDetailsDto != null ){
			 age = CommonDateUtils.calculateAge(CommonDateUtils.getLocalDate(CommonDateUtils.getDate(patientDetailsDto.getDob(), "dd-MM-yyyy")));
			}
			Integer srNo = 1;
			String formUrl = null;
			String SMS=""; String mail=""; 
			if(templateList!=null) {
			for (Iterator iterator = templateList.iterator(); iterator.hasNext();) {
				
				TemplateMasterDto templateDto = (TemplateMasterDto) iterator.next();
				if(templateDto.getFormTemplateId()!=null) {
				HttpHeaders headers = new HttpHeaders();
				headers.set("Content-Type", "application/json");
				headers.set("X-Api-Key",reqDto.getApiKey()); 
				headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
				headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
				headers.setContentType(MediaType.APPLICATION_JSON);
				

				 	JSONObject obj = null;
					JSONObject jsonObject = new JSONObject();

					JSONArray queryParams = new JSONArray();
					String doctorName = dto.getDoctorName();

					if(!doctorName.contains("Dr.")){
						doctorName = "Dr."+doctorName;
					}

					queryParams.put(new JSONObject().put("patientName", dto.getPatientName()));
					queryParams.put(new JSONObject().put("doctorName", doctorName));
					queryParams.put(new JSONObject().put("patientuhId", dto.getUhidNumber()));
					queryParams.put(new JSONObject().put("patientDob", dto.getDob()));
					queryParams.put(new JSONObject().put("patientAge", dto.getAgeString()));
					queryParams.put(new JSONObject().put("patientGender", dto.getGender()));
					queryParams.put(new JSONObject().put("patientAdmissionNo", dto.getAdmissionNumber()));
					queryParams.put(new JSONObject().put("patientAdmissionDate", dto.getDoa()));
					queryParams.put(new JSONObject().put("patientDischargeDate", dto.getPdd()));

					jsonObject.put("queryParams", queryParams);

					String jsonString = jsonObject.toString();

					org.springframework.http.HttpEntity<String> request = new org.springframework.http.HttpEntity<>(jsonString,headers);

					 URI url= UriComponentsBuilder.fromHttpUrl(reqDto.getTemplateUrl()+"generateFormUrl")
					            .queryParam("templateId",templateDto.getFormTemplateId())
					 .queryParam("age",age != 0 ? age.toString() :0)
					 .queryParam("gender",patientDetailsDto != null ? patientDetailsDto.getGenderName() :'O').build().toUri();		 
					 
					 ResponseEntity<String> stringResponse = restTemplate.exchange(url,HttpMethod.POST ,request, String.class);
					   obj = new JSONObject( stringResponse.getBody() );
					  formUrl =  obj.getString("formUrl");
					  String conversationId =  obj.getString("conversationId");
					   
					 if(conversationId!=null ){
						 formsEngineTransactionDto.setConversationId(conversationId);
					 }
					 if(formUrl!=null ){
						 formsEngineTransactionDto.setFormUrl(formUrl);
						 SMS+="\n"+(srNo)+". "+formUrl;
						  mail+="<br>"+(srNo)+". <a  href="+formUrl+">"+templateDto.getTitle()+"</a>";
//						  System.out.println("mail ==>"+mail);
					 }
					 
					 formsEngineTransactionDto.setTemplateTransactionId(dto.getTemplateTransactionId());
					 formsEngineTransactionDto.setTemplateId(templateDto.getTemplateId());
					 formsEngineTransactionDto.setSpecialityId(templateDto.getSpecialityId()!= null ? templateDto.getSpecialityId() : null);
					 formsEngineTransactionDto.setSubSpecialityId(templateDto.getSubSpecialityId());
					 formsEngineTransactionDto.setEncounterId(dto.getEncounterId());
				     formsEngineTransactionDto.setAdmissionId(dto.getAdmissionId());
				     formsEngineTransactionDto.setDoctorId(dto.getDoctorId());
				     formsEngineTransactionDto.setPatientId(dto.getPatientId());
				     formsEngineTransactionDto.setTemplateTypeId(templateDto.getTemplateTypeId());
				     formsEngineTransactionDto.setDiagnosisId(dto.getDiagnosisId());
				     formsEngineTransactionDto.setCreatedBy(dto.getCreatedBy());
				     formsEngineTransactionDto.setOrgId(dto.getOrgId());
				     formsEngineTransactionDto.setUnitId(dto.getUnitId());
				     formsEngineTransactionDto.setmIcpcId(dto.getmIcpcId());
				     formsEngineTransactionDto.setmIcpcSystemId(dto.getmIcpcSystemId());
				     formsEngineTransactionDto.setIsProvisionalRelease(dto.getIsProvisionalRelease());
				     formsEngineTransactionDto.setIsFinalRelease(dto.getIsFinalRelease());
				     formsEngineTransactionDto.setIsReportRelease(dto.getIsReportRelease());
				     formsEngineTransactionDto.setIsQuestionary(dto.getIsQuestionary());
				     formsEngineTransactionDto.setConversationId(conversationId);
					 formsEngineTransactionDto.setFormTemplateId(templateDto.getFormTemplateId());
					 formsEngineTransactionDto.setOrderDetailsId(dto.getOrderDetailsId());	
					 formsEngineTransactionDto.setAppointmentId(dto.getAppointmentId());
					 formsEngineTransactionDto.setAdmNoteId(dto.getAdmNoteId());
					 
					 
	                 //Lab				 
					 formsEngineTransactionDto.settSubSpecimanId(dto.gettSubSpecimanId());
					 formsEngineTransactionDto.setReportNo(dto.getReportNo());
					 formsEngineTransactionDto.setStainingId(dto.getStainingId());
					 formsEngineTransactionDto.setLabSampleDtlsId(dto.getLabSampleDtlsId());
					 formsEngineTransactionDto.setTemplateResId(dto.getTemplateResId());
					 formsEngineTransactionDto.setTemplateTransactionId(dto.getTemplateTransactionId());
					 formsEngineTransactionDto.setSampleStatusId(dto.getSampleStatusId());
					 
					 
						stringResponse=null;
						iTemplateMasterService.saveFormsEngineTransactionDetails(formsEngineTransactionDto);
						srNo++;
			       }
			
			if(patientDetailsDto != null  && patientDetailsDto.getEmail()!=null) {
			if(dto.getTemplateTypeId()!=null && dto.getTemplateTypeId()==10)//1.Questionaire form
			{
				  MailReqDto mailDto =new MailReqDto();
				  mailDto.setPatientName(patientDetailsDto.getPatientName());
				  mailDto.setDoctorName(dto.getDoctorName());
				  mailDto.setAppointmentDate(dto.getAppointmentDate());
				  mailDto.setTimeSlot(dto.getTimeSlot());
				  mailDto.setpEmailId(patientDetailsDto.getEmail());
				  mailDto.setDoctorId(dto.getDoctorId()); 
				  mailDto.setFormTemplate(mail);
				  mailDto.setAdmissionId(dto.getAdmissionId());
				  mailDto.setEncounterId(dto.getEncounterId());
				  mailDto.setSpecialityName(dto.getSpecialityName());
				 // iMailingService.sendTemplateMailtoPatient(mailDto);
				 		
			}
			else if(dto.getTemplateTypeId()!=null && dto.getTemplateTypeId()==23)//2.feedback form
			{
				  MailReqDto mailDto =new MailReqDto();
				  mailDto.setPatientName(patientDetailsDto.getPatientName());
				  mailDto.setDoctorName(dto.getDoctorName());
				  mailDto.setAppointmentDate(dto.getAppointmentDate());
				  mailDto.setTimeSlot(dto.getTimeSlot());
				  mailDto.setpEmailId(patientDetailsDto.getEmail());
				  mailDto.setDoctorId(dto.getDoctorId()); 
				  mailDto.setFormTemplate(mail);
				  mailDto.setAdmissionId(dto.getAdmissionId());
				  mailDto.setEncounterId(dto.getEncounterId());
				  if(patientDetailsDto.getEmail()!=null && patientDetailsDto.getEmail().length()>0)
				  iMailingService.sendFeedBackTemplateMailtoPatient(mailDto);
			   }
			  }
			 }
			}
			 return new Response(SUCCESS,SUCCESS_CODE,SUCCESS_MESSAGE,null,formUrl,formsEngineTransactionDto);
		  }
		}catch(Exception e){
			throw e;
		}
		return new Response(ERROR,ERROR_CODE,COMMON_ERROR_MESSAGE,null,null);

	}

	@Transactional
	@Override
	public Response printSavedTemplate(TemplateTransactionDto dto) throws ApplicationException {
		try{
			return iTemplateTransactionDao.printSavedTemplate(dto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response printHTMLTemplate(TemplateTransactionDto dto) throws ApplicationException {
		try{
			return iTemplateTransactionDao.printHTMLTemplate(dto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	public Response getTemplateTransactionForIPDCoversheetByDate(TemplateTransactionDto dto) throws in.param.exception.ApplicationException {
		try{
			List<TemplateTransactionDto> resDtoList = new ArrayList<>();

			for(String date : dto.getDates()){
				List<TemplateTransactionDto> certificateListForCaseSheet = new ArrayList<>();
				List<TemplateTransactionDto> lettersListForCaseSheet = new ArrayList<>();
				TemplateTransactionDto resDto = new TemplateTransactionDto();

				resDto.setAddedDate(date);
				if(dto.getCertificateListForCaseSheet() != null && dto.getCertificateListForCaseSheet().size()>0){
					for (TemplateTransactionDto item : dto.getCertificateListForCaseSheet()) {

						if (date.equals(item.getCreatedDate1())) {
							item.setAddedDate(date);
							certificateListForCaseSheet.add(item);
						}
					}
					resDto.setCertificateListForCaseSheet(certificateListForCaseSheet);
				}

				if(dto.getLettersListForCaseSheet() != null && dto.getLettersListForCaseSheet().size()>0){
					for (TemplateTransactionDto item : dto.getLettersListForCaseSheet()) {

						if (date.equals(item.getCreatedDate1())) {
							item.setAddedDate(date);
							lettersListForCaseSheet.add(item);
						}
					}
					resDto.setLettersListForCaseSheet(lettersListForCaseSheet);
				}
				resDtoList.add(resDto);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, resDtoList, null);
		}
		catch (Exception e){
			logger.error("getTemplateTransactionForIPDCoversheetByDate failed", e);
		}

		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUniqueDateForIPDCoversheet(TemplateTransactionDto dto) throws in.param.exception.ApplicationException {
		try{
			List<String>distinctDates = new ArrayList<>();
			TemplateTransactionDto resDto = dto;

			if(dto.getCertificateListForCaseSheet() != null && dto.getCertificateListForCaseSheet().size()>0){
				for (TemplateTransactionDto item : dto.getCertificateListForCaseSheet()) {

					if (!distinctDates.contains(item.getCreatedDate1())) {
						distinctDates.add(item.getCreatedDate1());
					}
				}

			}
			if(dto.getLettersListForCaseSheet() != null && dto.getLettersListForCaseSheet().size()>0) {
				for (TemplateTransactionDto item : dto.getLettersListForCaseSheet()) {

					if (!distinctDates.contains(item.getCreatedDate1())) {
						distinctDates.add(item.getCreatedDate1());
					}
				}
			}
			if(distinctDates != null && distinctDates.size()>0){
				Collections.sort(distinctDates, Comparator.reverseOrder());
				resDto.setDates(distinctDates);
				return getTemplateTransactionForIPDCoversheetByDate(resDto);
			}
			else{
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS, null, null);
			}
		}
		catch (Exception e){
			logger.error("getUniqueDateForIPDCoversheet failed", e);
		}

		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response updateNABH(TemplateTransactionDto fTDto) throws ApplicationException {

		try{
			
			
			Integer templateTypeId = iTemplateMasterDao.getTemplateTypeId(fTDto.getTemplateId());
			
			if(templateTypeId != null && (templateTypeId == 22 || templateTypeId == 29 )) {
						
					TemplateTransactionDto dto1  = new TemplateTransactionDto();
					dto1.setUnitId(fTDto.getUnitId());
					dto1.setOrgId(fTDto.getOrgId());
					dto1.setTemplateTransactionId(fTDto.getTemplateTransactionId());
					Response res = iTemplateTransactionDao.printHTMLTemplate(dto1);
					
					Response res1 = iTemplateMasterDao.getOrgAndUnitName(fTDto.getUnitId());
					if(res.getObject() != null) {
						TemplateTransactionDto templateTransactionDto = (TemplateTransactionDto) res.getObject(); 
					
					
						String completeTemplateContent = templateTransactionDto.getHeaderPart() + templateTransactionDto.getTemplateHtml();

					        Document doc = Jsoup.parse(completeTemplateContent);
	
					        // Handle input elements
					        Elements inputs = doc.select("input");
					        for (Element input : inputs) {
					            if (input.attr("type").equals("text")) {
					                input.replaceWith(new Element("label").text(input.val()));
					            }
					        }
	
					        Elements textareas = doc.select("textarea");
					        for (Element textarea : textareas) {
					            textarea.replaceWith(new Element("label").text(textarea.val()));
					        }
	
					        Elements spans = doc.select("span");
					        for (Element span : spans) {
					            if (span.hasClass("ant-select-arrow") ||
					                span.hasClass("anticon") ||
					                span.hasClass("ant-select-clear")) {
					                span.replaceWith(new Element("label").text(""));
					            }
					        }
	
					        Elements divs = doc.select("div");
					        for (Element div : divs) {
					            if (div.text().equals("Select Answer")) {
					                div.remove();
					            } else if (div.hasClass("ant-select-selector")) {
					                div.replaceWith(new Element("label").text(div.text()));
					            }
					        }
	
					        String resultHtml = doc.html();
					        resultHtml = resultHtml.replaceAll("checked=\"\"", "checked");
							
					        String fileName = "";
					        
					        if(templateTransactionDto.getPatientId() == null || templateTransactionDto.getPatientId() == 0){
					        	Response spec = iTemplateMasterDao.getSpecialityName(fTDto.getConversationId());
					        	
					        	if(spec.getObject()!= null ) {
					        		SpecialityMasterDto MasterDto = (SpecialityMasterDto)spec.getObject();
					        		fileName = MasterDto.getSpecialityName();
					        	}
					        }else {
					        	fileName = templateTransactionDto.getPatientName();
					        }
					        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
					        fileName = fileName+"-"+timeStamp;
					        
					    	byte[] pdfBytes = convertHtmlToPdf(resultHtml);

 							ByteArrayOutputStream outputStream = new ByteArrayOutputStream(pdfBytes.length);
							outputStream.write(pdfBytes, 0, pdfBytes.length);
							
							UnitMasterDto unit = (UnitMasterDto)res1.getObject();
							
							String docPath = docUploadUtility.updateNABHTemplate(outputStream, fileName,unit.getOrganizationName(), unit.getUnitName(),templateTransactionDto.getTitle());
							iTemplateMasterDao.getUpdatePath(docPath,fTDto.getTemplateTransactionId());
					        
					}
					
				}
			
		}
		catch (Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}

		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}

	
	
}


