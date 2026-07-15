package com.param.lis.serology.transaction.controller;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.param.entity.lis.transaction.LabSampleDetailsMaster;
import com.param.global.common.DocUploadUtility;
import com.param.global.common.HISFileUploadsPath;
import com.param.global.common.ISmsUtilityConstants;
import com.param.global.common.ServletUtils;
import com.param.global.dto.AdmissionDto;
import com.param.global.dto.OrderMasterDto;
import com.param.global.dto.TemplateTransactionDto;
import com.param.global.opd.service.IEncounterMasterService;
import com.param.global.org.dto.OrganizationMasterDto;
import com.param.global.service.IOrderMasterService;
import com.param.global.service.IUnitApplicationBillingConfigurationService;
import com.param.global.unit.dto.LabConfigurationDto;
import com.param.global.unit.dto.SmsUtilityConfigInfoDto;
import com.param.global.unit.service.ILabConfigurationService;
import com.param.global.unit.service.ISmsUtilityConfigService;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.microbiology.transaction.dto.MicrobioParamDto;
import com.param.lis.microbiology.transaction.service.IMicrobiologyService;
import com.param.lis.serology.dto.SerologyResultTestMapperDto;
import com.param.lis.serology.dto.SerologyTestResultDto;
import com.param.lis.serology.transaction.service.ISerologyResultService;
import com.param.lis.transaction.controller.BioChemResultEntryController;
import com.param.lis.transaction.dto.LabResultMasterDto;
import com.param.lis.transaction.dto.LabSampleDetailsMasterDto;
import com.param.lis.transaction.dto.SampleAcceptancePendingDto;
import com.param.lis.transaction.service.IBiochemReportReleaseService;
import com.param.lis.transaction.service.ILabTransactionService;
import com.param.utility.TimeZoneComponent;

@RestController
@SuppressWarnings(
{ "rawtypes", "unchecked" })
@RequestMapping(value = "/LIS/Serology")
public class SerologyResultController implements ICommonConstants, ITransactionConstants, IError,HISFileUploadsPath {

	@Autowired
	private ISerologyResultService iSerologyResultService;
	
	@Autowired
	private IMicrobiologyService iMicrobiologyService;
	
	@Autowired
	IOrderMasterService iOrderMasterService;
	
	
	@Autowired
	IEncounterMasterService iEncounterMasterService;

	@Autowired
	private IBiochemReportReleaseService iBiochemReportReleaseService;
	
	@Autowired
	private ISmsUtilityConfigService smsUtilityService;
	
	@Autowired
    ILabTransactionService iLabTransactionService;
	
	@Autowired
	DocUploadUtility docUploadUtility;

	@Autowired
	private ILabConfigurationService iLabConfigurationService;
	
	@Autowired
	private SessionFactory SessionFactory;
	
	@Autowired
	private IUnitApplicationBillingConfigurationService unitApplicationBillingConfigurationService;

	@Autowired
	SessionFactory sessionFactory;
	
	final static Logger logger = Logger.getLogger(BioChemResultEntryController.class);
	
	@RequestMapping(value = "/ResultEntry/Worklist", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSerologyResultEntryList(
			@RequestBody MicrobioParamDto dto)
	{
		Response<SampleAcceptancePendingDto, Integer> response = null;
		try
		{
			response = iSerologyResultService.getSerologyResultEntryList(dto);
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	@RequestMapping(value = "/ResultEntry/Count", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSerologyResultEntryListCount(@RequestBody MicrobioParamDto dto)
	{
		Response<SampleAcceptancePendingDto, Integer> response = null;
		try
		{
			response = iSerologyResultService.getSerologyResultEntryListCount(dto);
			return response;
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}
	
	
	@RequestMapping(value = "/ResultEntry/Search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response searchSerologyResultEntryList(
			@RequestBody SearchDto dto)
	{
		Response<SampleAcceptancePendingDto, Integer> response = null;
		try
		{
			response = iSerologyResultService.searchSerologyResultEntryList(dto);
			
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	@RequestMapping(value = "/ResultEntry", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveResultEntry(@RequestBody List<SerologyTestResultDto> list)
	{
		try
		{
			 iSerologyResultService.updateAmend(list);
			return iSerologyResultService.saveResultEntry(list);
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}
	

	/**Report Validataion*/
	@RequestMapping(value = "/ReportEntry/Details", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSerologyReportEntryDetails(@RequestBody Map<Integer,Integer> labSampleDtlsId)
	{
		try
		{
			List<Integer> list =  new ArrayList();
			for (Map.Entry<Integer,Integer> entry : labSampleDtlsId.entrySet()) { 
				list.add(entry.getKey());
			} 
			
			return iSerologyResultService.getSerologyReportEntryDetails(list);
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	

	@RequestMapping(value = "/Validation/Worklist", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSerologyValidationList(
			@RequestBody MicrobioParamDto dto)
	{
		Response<SampleAcceptancePendingDto, Integer> response = null;
		try
		{
			response = iSerologyResultService.getSerologyValidationList(dto);
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	@RequestMapping(value = "/Validation/Count", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSerologyValidationListCount(@RequestBody MicrobioParamDto dto)
	{
		Response<SampleAcceptancePendingDto, Integer> response = null;
		try
		{
			response = iSerologyResultService.getSerologyValidationListCount(dto);
			return response;
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}
	
	
	@RequestMapping(value = "/Validation/Search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response searchSerologyValidationList(
			@RequestBody SearchDto dto)
	{
		Response<SampleAcceptancePendingDto, Integer> response = null;
		try
		{
			response = iSerologyResultService.searchSerologyValidationList(dto);
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	@RequestMapping(value = "/ResultEntry/Validation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveValidation(@RequestBody List<SerologyTestResultDto> list)
	{
	
		try
		{
			return iSerologyResultService.saveValidation(list);
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}
	
	
	/**Report Release*/
	@RequestMapping(value = "/ReportValidation/Details", method = RequestMethod.POST)
	public @ResponseBody Response getSerologyReportValidationDetails(@RequestBody Map<Integer,Integer> labSampleDtlsId)
	{
		try
		{
			List<Integer> list =  new ArrayList();
			for (Map.Entry<Integer,Integer> entry : labSampleDtlsId.entrySet()) { 
				list.add(entry.getKey());
			} 
			
			return  iSerologyResultService.getSerologyReportValidationDetails(list);
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	

	@RequestMapping(value = "/ReportRelease/Worklist", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSerologyReportReleaseList(
			@RequestBody MicrobioParamDto dto)
	{
		Response<SampleAcceptancePendingDto, Integer> response = null;
		try
		{
			response = iSerologyResultService.getSerologyReportReleaseList(dto);
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	@RequestMapping(value = "/ReportRelease/Count", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSerologyReportReleaseCount(@RequestBody MicrobioParamDto dto)
	{
		Response<SampleAcceptancePendingDto, Integer> response = null;
		try
		{
			response = iSerologyResultService.getSerologyReportReleaseCount(dto);
			return response;
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}
	
	
	@RequestMapping(value = "/ReportRelease/Search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response searchSerologyReportReleaseList(
			@RequestBody SearchDto dto)
	{
		Response<SampleAcceptancePendingDto, Integer> response = null;
		try
		{
			response = iSerologyResultService.searchSerologyReportReleaseList(dto);
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	@RequestMapping(value = "/ReportRelease", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveReportRelease(@RequestBody List<SerologyTestResultDto> serologyTestResultDto,HttpServletRequest request) throws Exception
	{
		
		try
		{
			com.param.global.common.Response res1 = null;
			String pattern = "yyyy-MM-dd HH:mm:ss";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			String resPublicUrl =  unitApplicationBillingConfigurationService.getPublicUrl(serologyTestResultDto.get(0).getOrgUnitId());
			String date1 = simpleDateFormat.format(TimeZoneComponent.getDateByzone(serologyTestResultDto.get(0).getOrgUnitId()));
			for (SerologyTestResultDto dto : serologyTestResultDto) {
				Response labSampleDetailsMasterRes = null;
				
				Response response = iSerologyResultService.saveReportRelease(dto);
				if(response.getStatus().equals("success")) {
					
							for (Iterator iterator = dto.getSerologyResultTestMapperDtoList().iterator(); iterator.hasNext();) 
							{
								SerologyResultTestMapperDto mapperDto = (SerologyResultTestMapperDto) iterator.next();
								mapperDto.setSerologyTestResultId( dto.getSerologyTestResultId());
								iSerologyResultService.updateReportRelease(mapperDto);
							}
							Date date = TimeZoneComponent.getDateByzone(dto.getOrgUnitId());
							if(response.getObject()!=null && response.getCode().equals(SUCCESS_200))
							{
								LabSampleDetailsMasterDto smpDto = new LabSampleDetailsMasterDto();
								smpDto.setOrgId(dto.getOrgId());
								smpDto.setOrgUnitId(dto.getOrgUnitId());
								smpDto.setAssignUnit(dto.getOrgUnitId());
								smpDto.setPerformedUnit(dto.getOrgUnitId());
								smpDto.setInternalNotes(dto.getInternalNotes());
								smpDto.setCreatedBy(dto.getCreatedBy());
								smpDto.setLabSampleDtlsId(dto.getLabSampleDtlsId());
								smpDto.setIsOnHoldParameter(dto.getIsOnHoldParameter());
								smpDto.setIsReportRecall(dto.getIsReportRecall());
								if(dto.getProReleaseFlag().equals(1)) {
									smpDto.setSampleStatusId(PROVISIONAL_REPORT_RELEASED);
								}else {
									smpDto.setSampleStatusId(REPORT_RELEASED);
								}
								
								smpDto.setCreatedDate(date);
								labSampleDetailsMasterRes =	iMicrobiologyService.updateLabSampleDetailsMaster(smpDto);
								
								if(labSampleDetailsMasterRes!=null && labSampleDetailsMasterRes.getObject()!=null) {
									
									LabSampleDetailsMaster newDto = (LabSampleDetailsMaster)labSampleDetailsMasterRes.getObject();
									if(newDto.getSampleStatusId()==REPORT_RELEASED) {
										
										List<String> orderNos = new ArrayList<>();
						                for (SerologyTestResultDto serologyTestResultNewDto : serologyTestResultDto) {
						                    if(orderNos.size() ==0 ) {
						                        orderNos.add(serologyTestResultNewDto.getOrderNo());
						                    }else {
						                        String paraExits = orderNos.stream()
						                                .filter(p -> p.equals(serologyTestResultNewDto.getOrderNo())).findAny()
						                                .orElse(null);
						                        if(paraExits == null) {
						                            orderNos.add(serologyTestResultNewDto.getOrderNo());
						                        }
						                    }
						                }
						                Integer unitId = dto.getAssignUnitId() != null && dto.getAssignUnitId() > 0 ? dto.getAssignUnitId() : dto.getOrgUnitId();

						                Response  resOrderNo = iBiochemReportReleaseService.getOrderIdByOrderNumber(orderNos, unitId);
						                
						                Integer OrderId = null;
										   if(resOrderNo.getListObject() != null && resOrderNo.getListObject().size()>0) {
							                    List<OrderMasterDto> orderList = new ArrayList<>();
							                    orderList.addAll(resOrderNo.getListObject());
							                    
							                    for (OrderMasterDto orderMasterDto : orderList) {
							                        
							                           if(orderMasterDto.getOrderNo().equals(dto.getOrderNo())) {
									                      	OrderId = orderMasterDto.getOrderId();
									                    }
							                           
							                    }
							                }
										
										   Response  OrderRes = iBiochemReportReleaseService.getCompanyIdByOrderId(OrderId);
					                       OrderMasterDto orderDto= new OrderMasterDto();
					                        if(OrderRes.getObject() != null) {
					                        	
					                        	orderDto = (OrderMasterDto) OrderRes.getObject();
					                        	
					                        }
					                        Boolean SendMail = true;
					                        
						                    if(orderDto.getVisitTypeId() == ICommonConstants.OP) {
						                    	
						                    	 Response  checkDueAmount = iBiochemReportReleaseService.checkDueAmount(orderDto);
						                    	 
						                    	 if(checkDueAmount.getObject()!= null && checkDueAmount.getObject().equals(SendMail)) {
						                    		 SendMail = true;
						                    	 }else {
						                    		 SendMail = false;
						                    	 }
						                    }
										if(SendMail) {
											Response exclusion = iBiochemReportReleaseService.getLabExclusion(newDto.getLabSampleDtlsId(), dto.getOrgUnitId(), dto.getOrgId());

											if (exclusion.getObject() != null) {

												Integer checkCritical = (Integer) exclusion.getObject();
												if (checkCritical == 0) {
													SendMail = true;
												} else {
													SendMail = false;
												}
											} else {
												SendMail = true;
											}
										}
										if(SendMail && (serologyTestResultDto.get(0).getSendMailOnOrderCompletion()!= null && serologyTestResultDto.get(0).getSendMailOnOrderCompletion() ==  false ) &&
												(serologyTestResultDto.get(0).getTriggerReleaseMail()!= null && serologyTestResultDto.get(0).getTriggerReleaseMail() ==  true)) { 
											
											SmsUtilityConfigInfoDto emailinfo = new SmsUtilityConfigInfoDto();
											emailinfo.setLabSampleDtlsId( newDto.getLabSampleDtlsId());
											emailinfo.setPatientId(newDto.getPatientId());
											emailinfo.setConsultingDoctorId(newDto.getDoctorId());
											emailinfo.setAdmitingDoctorId(newDto.getDoctorId());
											emailinfo.setReferringDoctorId(newDto.getDoctorId());
											emailinfo.setTreatingDoctorId(newDto.getDoctorId());
											emailinfo.setUnitId(dto.getOrgUnitId());
											emailinfo.setOrganizationId(dto.getOrgId());
											emailinfo.setTestDesc(dto.getTestDesc());
											emailinfo.setDateTime(date1);
							                if(newDto.getCreatedBy()!=null) {
							                	Integer  empId =iBiochemReportReleaseService.getgetEmpId(dto.getCreatedBy()); 
							                	emailinfo.setEmployeeId(empId);
							                }
							            	if(orderDto.getPaymentEntitlementId() != null && orderDto.getPaymentEntitlementId() == ICommonConstants.INSURANCE) {
					                          	
						                       	   emailinfo.setCompanyId(null);
						                              emailinfo.setInsuranceId(orderDto.getPayeeId()!=null ? orderDto.getPayeeId(): null);
						                              
						                       }else  if(orderDto.getPaymentEntitlementId() != null && orderDto.getPaymentEntitlementId() == ICommonConstants.COMPANY) {
						                       	
						                       	   emailinfo.setCompanyId(orderDto.getPayeeId()!=null ? orderDto.getPayeeId(): null);
						                              emailinfo.setInsuranceId(null);
						                              
						                       }else {
						                       	   emailinfo.setCompanyId(null);
						                              emailinfo.setInsuranceId(null);
						                       }
											emailinfo.setRptFileName("SerologyReport_GroupPrint.rpt");
											res1 = smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.PATHOLOGY_REPORT, emailinfo);
										
										}
										
										 //for feedbackform
							             TemplateTransactionDto templateDto = new TemplateTransactionDto();
							             templateDto.setLabSampleDtlsId(dto.getLabSampleDtlsId());
							             iEncounterMasterService.checkFeedBackFormSubmitted(templateDto);
									}
								}
						}
							iOrderMasterService.assignReviewApplicableDate(dto.getOrderDetailsId());
				}
				
			}
			
			//mail
	        if(serologyTestResultDto.get(0).getSendMailOnOrderCompletion()!= null && serologyTestResultDto.get(0).getSendMailOnOrderCompletion() ==  true) {
	        	
                List<String> orderNos = new ArrayList<>();
                for (SerologyTestResultDto serologyTestResultNewDto : serologyTestResultDto) {
                    if(orderNos.size() ==0 ) {
                        orderNos.add(serologyTestResultNewDto.getOrderNo());
                    }else {
                        String paraExits = orderNos.stream()
                                .filter(p -> p.equals(serologyTestResultNewDto.getOrderNo())).findAny()
                                .orElse(null);
                        if(paraExits == null) {
                            orderNos.add(serologyTestResultNewDto.getOrderNo());
                        }
                    }
                }

				Integer unitId = serologyTestResultDto.get(0).getAssignUnitId() != null && serologyTestResultDto.get(0).getAssignUnitId() > 0 ?
						serologyTestResultDto.get(0).getAssignUnitId() : serologyTestResultDto.get(0).getOrgUnitId();
                Response  response1 = iBiochemReportReleaseService.getOrderIdByOrderNumber(orderNos, unitId);
                
                if(response1.getListObject() != null && response1.getListObject().size()>0) {
                    List<OrderMasterDto> orderList = new ArrayList<>();
                    orderList.addAll(response1.getListObject());
                    
                    for (OrderMasterDto orderMasterDto : orderList) {
                    	SerologyTestResultDto newDto1 = new SerologyTestResultDto();
                        
                        for (SerologyTestResultDto orderMasterDto2 : serologyTestResultDto) {
                            if(orderMasterDto.getOrderNo().equals(orderMasterDto2.getOrderNo())) {
                            	newDto1 = orderMasterDto2;
                            }
                        }
                        
                        Response  OrderRes = iBiochemReportReleaseService.getCompanyIdByOrderId(orderMasterDto.getOrderId());
                        OrderMasterDto orderDto= new OrderMasterDto();
                        if(OrderRes.getObject() != null) {
                        	
                        	orderDto = (OrderMasterDto) OrderRes.getObject();
                        	
                        }
                        
                        Boolean SendMail = true;
                        
	                    if(orderDto.getVisitTypeId() == ICommonConstants.OP) {
	                    	
	                    	 Response  checkDueAmount = iBiochemReportReleaseService.checkDueAmount(orderDto);
	                    	 
	                    	 if(checkDueAmount.getObject()!= null && checkDueAmount.getObject().equals(SendMail)) {
	                    		 SendMail = true;
	                    	 }else {
	                    		 SendMail = false;
	                    	 }
	                    }
	                        
                        
                        if(SendMail && newDto1.getLabSampleDtlsId() != 0) {
                            SearchDto searchDto = new  SearchDto();
                            searchDto.setOrderId(orderMasterDto.getOrderId());
                            searchDto.setOrgId(newDto1.getOrgId());
                            
                            Response labSampleDetailIds = iLabTransactionService.checkForOrderCompletion(searchDto);
                            if(labSampleDetailIds.getListObject()!= null && labSampleDetailIds.getListObject().size()>0) {
                                
                                List<Integer>labSampleDetlsIds = new ArrayList<>();
                                List<SearchDto> LabSampleDetailsMasterList =  new ArrayList<>();
                                
                                LabSampleDetailsMasterList.addAll(labSampleDetailIds.getListObject());
                                String testName = "";
                                for (SearchDto detilsId : LabSampleDetailsMasterList) {
                                    labSampleDetlsIds.add(detilsId.getLabSampleDtlsId());
                                    if(testName == "") {
                                        testName = detilsId.getTestDesc();
                                    }else {
                                        testName = testName +" , "+ detilsId.getTestDesc(); 
                                    }
                                }
                                
                                	Response  res45 =iBiochemReportReleaseService.getDoctorIdByLabSampleDetailsId(newDto1.getLabSampleDtlsId());
                                
                                	 SmsUtilityConfigInfoDto emailinfo = new SmsUtilityConfigInfoDto();
                                if(res45.getObject() != null ) {
                                    AdmissionDto admDto = (AdmissionDto) res45.getObject();
                                    emailinfo.setConsultingDoctorId(admDto.getDoctorId()!= null ?admDto.getDoctorId() : null );
                                    emailinfo.setAdmitingDoctorId(admDto.getDoctorId()!= null ?admDto.getDoctorId() : null);
                                    emailinfo.setReferringDoctorId(admDto.getReferralId()!= null ?admDto.getReferralId() : null);
                                    emailinfo.setTreatingDoctorId(admDto.getDoctorId()!= null ?admDto.getDoctorId() : null);
                                    emailinfo.setPatientId(admDto.getPatientId());
                                }
                                
                               
                                emailinfo.setLabSampleDtlsIdsList(labSampleDetlsIds);
                                
                                emailinfo.setUnitId(newDto1.getOrgUnitId());
                                emailinfo.setOrganizationId(newDto1.getOrgId());
                                emailinfo.setDateTime(date1);
                                if(newDto1.getCreatedBy()!=null) {
				                	
				                	Integer  empId =iBiochemReportReleaseService.getgetEmpId(newDto1.getCreatedBy()); 
				                	emailinfo.setEmployeeId(empId);
                                }
                                emailinfo.setTestDesc(testName);
                                if(orderDto.getPaymentEntitlementId() != null && orderDto.getPaymentEntitlementId() == ICommonConstants.INSURANCE) {
                                	
                                	emailinfo.setCompanyId(null);
                                    emailinfo.setInsuranceId(orderDto.getPayeeId()!=null ? orderDto.getPayeeId(): null);
                                    
                                }else  if( orderDto.getPaymentEntitlementId() != null && orderDto.getPaymentEntitlementId() == ICommonConstants.COMPANY) {
                             	
                                	emailinfo.setCompanyId(orderDto.getPayeeId()!=null ? orderDto.getPayeeId(): null);
                                    emailinfo.setInsuranceId(null);
                                    
                                }else {
                             	   	emailinfo.setCompanyId(null);
                                    emailinfo.setInsuranceId(null);
                                }
                                emailinfo.setRptFileName("Multireportprint.rpt");
                                res1 = smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.PATHOLOGY_REPORT, emailinfo);
                            }
                        } 
                    }
                } 
			}
			  
	        com.param.global.common.Response labres =  iLabConfigurationService.getLabConfigurationList(serologyTestResultDto.get(0).getOrgId(),serologyTestResultDto.get(0).getOrgUnitId());
            
	           if(labres.getListObject() !=null && labres.getListObject().size()>0) {
	        	   
	        	   List<LabConfigurationDto> lablist = labres.getListObject();
	        	   
	        	   if(lablist.get(0).getQrCodeRequired() == 'Y' && lablist.get(0).getQrCodeRequired() !=null) {
	        		   
	        		   for (SerologyTestResultDto labResultMasterDto : serologyTestResultDto) {
	          				
	                       int lowerLimit = 97;
	                       int upperLimit = 122;
	                       Random random = new Random();
	                       StringBuffer r = new StringBuffer(10);
	                       for (int i = 0; i < 10; i++) {
	                           int nextRandomChar = lowerLimit+ (int)(random.nextFloat() * (upperLimit - lowerLimit + 1));
	                           r.append((char)nextRandomChar);
	                       }
	                       
	                       String str= "";
	                       if(resPublicUrl == null) {
	                       	str= ServletUtils.getBaseUrl(request)+"/LIS/BioChemistry/QR/"+labResultMasterDto.getLabSampleDtlsId()+"/"+labResultMasterDto.getOrgUnitId()
	                   		+"/"+labResultMasterDto.getOrgId()+"/"+r.toString();  
	                       }else {
	                       	str= resPublicUrl+"/LIS/BioChemistry/QR/"+labResultMasterDto.getLabSampleDtlsId()+"/"+labResultMasterDto.getOrgUnitId()
	                   		+"/"+labResultMasterDto.getOrgId()+"/"+r.toString(); 
	                       }
	                       
	               		   
	               		
	               		//String path = "C:\\Users\\ASUS\\Downloads\\Quote.png";  
	               		String charset = "UTF-8";  
	               		Map<EncodeHintType, ErrorCorrectionLevel> hashMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();  
	               		
	               		hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);  

	               		com.google.zxing.common.BitMatrix matrix = new MultiFormatWriter().encode(new String(str.getBytes(charset), charset), BarcodeFormat.QR_CODE, 200, 200);  
	               		
	               		//MatrixToImageWriter.writeToFile(matrix, path.substring(path.lastIndexOf('.') + 1), new File(path));
	               		 ByteArrayOutputStream bos = new ByteArrayOutputStream();
	               		 MatrixToImageWriter.writeToStream(matrix, "png", bos);
	               	     bos.close();
	               	     
	               	     com.param.global.common.Response orgRes = iBiochemReportReleaseService.getOrganiazationFolderPath(labResultMasterDto.getOrgId());
	               	     OrganizationMasterDto organizationMasterDto = (OrganizationMasterDto) orgRes.getObject();
	       			 	 String docPath="";
	       			  
	        			 Response bucketRes =iBiochemReportReleaseService.getbucketTypeForUpload(labResultMasterDto.getOrgId(), labResultMasterDto.getOrgUnitId());
	        			 int bucketType =(int) bucketRes.getObject();
	        			 
	       			    	 docPath =  docUploadUtility.updateQRFile(bos,organizationMasterDto.getOrganizationFolder(),r.toString(),labResultMasterDto.getOrgId(),labResultMasterDto.getOrgUnitId());


	       			    	 ByteArrayOutputStream byteOutPutStream = new ByteArrayOutputStream();
	       			    	if(bucketType == 1) {

								Response awsCredResponse = iBiochemReportReleaseService.getAwsCreds();
								OrganizationMasterDto awsCredDto = (OrganizationMasterDto) awsCredResponse.getObject();
								String ACCESS_KEY = awsCredDto != null && awsCredDto.getAwsAccessKey() != null ? awsCredDto.getAwsAccessKey() :"";
								String SECRET_KEY = awsCredDto != null && awsCredDto.getAwsSecretKey() != null ? awsCredDto.getAwsSecretKey() :"";

	       			    	 AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
	       			    	 AmazonS3 s3Client = new AmazonS3Client(awsCredentials);
	       			    	 String path3[]=docPath.split(LAB_REPORTS_BUCKET+"/");
	       			    	 S3Object s3object = s3Client.getObject(BUCKET_NAME + "/" +organizationMasterDto.getOrganizationFolder()+LAB_REPORTS_BUCKET,path3[1]);
	       			    	 S3ObjectInputStream inputStream = s3object.getObjectContent();
	       			    	 
	       			    	 IOUtils.copy(inputStream, byteOutPutStream);
	       			    	}else {
								URL url = new URL(docPath);
								URLConnection connection = url.openConnection();
								InputStream inputStreamNew = connection.getInputStream();
								byte[] buffer = new byte[4096];
								int length;
								while ((length = inputStreamNew.read(buffer)) != -1) {
									byteOutPutStream.write(buffer, 0, length);
								}

								// Get the byte array from the ByteArrayOutputStream
								byte[] contents = byteOutPutStream.toByteArray();

								// Close the input stream and the ByteArrayOutputStream
								// inputStreamNew.close();
								// outputStream.close();
								IOUtils.copy(inputStreamNew, byteOutPutStream);
							}
	       			    	 iBiochemReportReleaseService.updateQAPathInLabsampleDetails(byteOutPutStream,docPath,labResultMasterDto.getLabSampleDtlsId(),str);	 
	                   	
	       			}
	        	   }
	        	   
	           }
			
			return new Response(SUCCESS, SUCCESS_200, REPORT_RELEASE_SUCC, null, (res1!=null && res1.getStatus().equals(SUCCESS) && res1.getMessage()!=null ) 
					? res1.getMessage() : null);
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
		
	}
	
	@RequestMapping(value = "/ResultEntryOrReleaseEntry", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response ResultEntryOrReleaseEntry(@RequestBody List<SerologyTestResultDto> list,HttpServletRequest request) throws Exception
	{
		try
		{
			iSerologyResultService.updateAmend(list);
			
			Response res =  iSerologyResultService.ResultEntryOrReleaseEntry(list);
			String pattern = "yyyy-MM-dd HH:mm:ss";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			String date1 = simpleDateFormat.format(TimeZoneComponent.getDateByzone(list.get(0).getOrgUnitId()));
			//mail
			String resPublicUrl =  unitApplicationBillingConfigurationService.getPublicUrl(list.get(0).getOrgUnitId());
			com.param.global.common.Response res2 = null;
            if(list.get(0).getSendMailOnOrderCompletion()!= null && list.get(0).getSendMailOnOrderCompletion() ==  true) {
                List<String> orderNos = new ArrayList<>();
                for (SerologyTestResultDto serologyTestResultDto : list) {
                    if(orderNos.size() ==0 ) {
                        orderNos.add(serologyTestResultDto.getOrderNo());
                    }else {
                        String paraExits = orderNos.stream()
                                .filter(p -> p.equals(serologyTestResultDto.getOrderNo())).findAny()
                                .orElse(null);
                        if(paraExits == null) {
                            orderNos.add(serologyTestResultDto.getOrderNo());
                        }
                    }
                }
                
                Response  response = iBiochemReportReleaseService.getOrderIdByOrderNumber(orderNos,list.get(0).getOrgUnitId());
                
                if(response.getListObject() != null && response.getListObject().size()>0) {
                    List<OrderMasterDto> orderList = new ArrayList<>();
                    orderList.addAll(response.getListObject());
                    
                    for (OrderMasterDto orderMasterDto : orderList) {
                    	SerologyTestResultDto dto = new SerologyTestResultDto();
                        
                        for (SerologyTestResultDto orderMasterDto2 : list) {
                            if(orderMasterDto.getOrderNo().equals(orderMasterDto2.getOrderNo())) {
                                dto = orderMasterDto2;
                            }
                        }
                        
                        
                        Response  OrderRes = iBiochemReportReleaseService.getCompanyIdByOrderId(orderMasterDto.getOrderId());
                        OrderMasterDto orderDto= new OrderMasterDto();
                        if(OrderRes.getObject() != null) {
                        	
                        	orderDto = (OrderMasterDto) OrderRes.getObject();
                        	
                        }
                        
                        Boolean SendMail = true;
                        
	                    if(orderDto.getVisitTypeId() == ICommonConstants.OP) {
	                    	
	                    	 Response  checkDueAmount = iBiochemReportReleaseService.checkDueAmount(orderDto);
	                    	 
	                    	 if(checkDueAmount.getObject()!= null && checkDueAmount.getObject().equals(SendMail)) {
	                    		 SendMail = true;
	                    	 }else {
	                    		 SendMail = false;
	                    	 }
	                    }
                        
                        if(SendMail && dto.getLabSampleDtlsId() != 0) {
                            SearchDto searchDto = new  SearchDto();
                            searchDto.setOrderId(orderMasterDto.getOrderId());
                            searchDto.setOrgId(dto.getOrgId());
                            
                            Response labSampleDetailIds = iLabTransactionService.checkForOrderCompletion(searchDto);
                            if(labSampleDetailIds.getListObject()!= null && labSampleDetailIds.getListObject().size()>0) {
                                
                                List<Integer>labSampleDetlsIds = new ArrayList<>();
                                List<SearchDto> LabSampleDetailsMasterList =  new ArrayList<>();
                                
                                LabSampleDetailsMasterList.addAll(labSampleDetailIds.getListObject());
                                String testName = "";
                                for (SearchDto detilsId : LabSampleDetailsMasterList) {
                                    labSampleDetlsIds.add(detilsId.getLabSampleDtlsId());
                                    if(testName == "") {
                                        testName = detilsId.getTestDesc();
                                    }else {
                                        testName = testName +" , "+ detilsId.getTestDesc(); 
                                    }
                                }
                                
                                SmsUtilityConfigInfoDto emailinfo = new SmsUtilityConfigInfoDto();
                                if(res!=null && res.getListObject()!=null && res.getListObject().size()>0) {
                                	List<LabSampleDetailsMasterDto> newList = res.getListObject();
            						emailinfo.setConsultingDoctorId(newList.get(0).getDoctorId());
         							emailinfo.setAdmitingDoctorId(newList.get(0).getDoctorId());
         							emailinfo.setReferringDoctorId(newList.get(0).getDoctorId());
         							emailinfo.setTreatingDoctorId(newList.get(0).getDoctorId());
                                    emailinfo.setLabSampleDtlsIdsList(labSampleDetlsIds);
                                    emailinfo.setPatientId(newList.get(0).getPatientId());
                                }
                                
                                emailinfo.setUnitId(dto.getOrgUnitId());
                                emailinfo.setOrganizationId(dto.getOrgId());
                                emailinfo.setDateTime(date1);
                                if(dto.getCreatedBy()!=null) {
				                	
				                	Integer  empId =iBiochemReportReleaseService.getgetEmpId(dto.getCreatedBy()); 
				                	emailinfo.setEmployeeId(empId);
                                }
                                emailinfo.setTestDesc(testName);
                                if(orderDto.getPaymentEntitlementId() != null && orderDto.getPaymentEntitlementId() == ICommonConstants.INSURANCE) {
                                	
                             	   	emailinfo.setCompanyId(null);
                                    emailinfo.setInsuranceId(orderDto.getPayeeId()!=null ? orderDto.getPayeeId(): null);
                                    
                                }else  if( orderDto.getPaymentEntitlementId() != null && orderDto.getPaymentEntitlementId() == ICommonConstants.COMPANY) {
                             	
                             	   	emailinfo.setCompanyId(orderDto.getPayeeId()!=null ? orderDto.getPayeeId(): null);
                                    emailinfo.setInsuranceId(null);
                                    
                                }else {
                             	   	emailinfo.setCompanyId(null);
                                    emailinfo.setInsuranceId(null);
                                }
                                emailinfo.setRptFileName("Multireportprint.rpt");
                                res2 = smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.PATHOLOGY_REPORT, emailinfo);
                            }
                        } 
                    }
                } 
			}else if(list.get(0).getTriggerReleaseMail()!= null && list.get(0).getTriggerReleaseMail() ==  true) { 

					if(res!=null && res.getListObject()!=null && res.getListObject().size()>0) {
						List<LabSampleDetailsMasterDto> newList = res.getListObject();
						
						
						 List<String> orderNos = new ArrayList<>();
			                for (SerologyTestResultDto labResultMasDto : list) {
			                    if(orderNos.size() ==0 ) {
			                        orderNos.add(labResultMasDto.getOrderNo());
			                    }else {
			                        String paraExits = orderNos.stream()
			                                .filter(p -> p.equals(labResultMasDto.getOrderNo())).findAny()
			                                .orElse(null);
			                        if(paraExits == null) {
			                            orderNos.add(labResultMasDto.getOrderNo());
			                        }
			                    }
			                }
						
			                Response  resOrderNo = iBiochemReportReleaseService.getOrderIdByOrderNumber(orderNos,list.get(0).getOrgUnitId());
						
						
						for (LabSampleDetailsMasterDto newDto : newList) {
							
							
							Integer OrderId = null;
							   if(resOrderNo.getListObject() != null && resOrderNo.getListObject().size()>0) {
				                    List<OrderMasterDto> orderList = new ArrayList<>();
				                    orderList.addAll(resOrderNo.getListObject());
				                    
				                    for (OrderMasterDto orderMasterDto : orderList) {
				                        
				                    	   for (SerologyTestResultDto orderMasterDto2 : list) {
				                    		   
				                               if(orderMasterDto2.getLabSampleDtlsId() == newDto.getLabSampleDtlsId()) {
				                            	   
				                            	   if(orderMasterDto.getOrderNo().equals(orderMasterDto2.getOrderNo())) {
						                            	OrderId = orderMasterDto.getOrderId();
						                            }
				                               }
				                           }
				                    }
				                }
							
							   Response  OrderRes = iBiochemReportReleaseService.getCompanyIdByOrderId(OrderId);
		                       OrderMasterDto orderDto= new OrderMasterDto();
		                        if(OrderRes.getObject() != null) {
		                        	
		                        	orderDto = (OrderMasterDto) OrderRes.getObject();
		                        	
		                        }
		                        
		                        Boolean SendMail = true;
		                        
			                    if(orderDto.getVisitTypeId() == ICommonConstants.OP) {
			                    	
			                    	 Response  checkDueAmount = iBiochemReportReleaseService.checkDueAmount(orderDto);
			                    	 
			                    	 if(checkDueAmount.getObject()!= null && checkDueAmount.getObject().equals(SendMail)) {
			                    		 SendMail = true;
			                    	 }else {
			                    		 SendMail = false;
			                    	 }
			                    }
							if(SendMail) {
								Response exclusion = iBiochemReportReleaseService.getLabExclusion(newDto.getLabSampleDtlsId(), list.get(0).getOrgUnitId(), list.get(0).getOrgId());

								if (exclusion.getObject() != null) {

									Integer checkCritical = (Integer) exclusion.getObject();
									if (checkCritical == 0) {
										SendMail = true;
									} else {
										SendMail = false;
									}
								} else {
									SendMail = true;
								}
							}
							if(SendMail) {
								
								SmsUtilityConfigInfoDto emailinfo = new SmsUtilityConfigInfoDto();
								emailinfo.setLabSampleDtlsId( newDto.getLabSampleDtlsId());
							 	emailinfo.setPatientId(newDto.getPatientId());
							 	emailinfo.setConsultingDoctorId(newDto.getDoctorId());
								emailinfo.setAdmitingDoctorId(newDto.getDoctorId());
								emailinfo.setReferringDoctorId(newDto.getDoctorId());
								emailinfo.setTreatingDoctorId(newDto.getDoctorId());
								emailinfo.setUnitId(list.get(0).getOrgUnitId());
								emailinfo.setOrganizationId(list.get(0).getOrgId());
								emailinfo.setDateTime(date1);
		                        if(list.get(0).getCreatedBy()!=null) {
		                        	Integer  empId =iBiochemReportReleaseService.getgetEmpId(list.get(0).getCreatedBy()); 
				                	emailinfo.setEmployeeId(empId);
		                        }
								emailinfo.setTestDesc(newDto.getTestDesc());
								 if(orderDto.getPaymentEntitlementId() != null && orderDto.getPaymentEntitlementId() == ICommonConstants.INSURANCE) {
	                             	
	                          	   	emailinfo.setCompanyId(null);
	                                 emailinfo.setInsuranceId(orderDto.getPayeeId()!=null ? orderDto.getPayeeId(): null);
	                                 
	                             }else  if( orderDto.getPaymentEntitlementId() != null && orderDto.getPaymentEntitlementId() == ICommonConstants.COMPANY) {
	                          	
	                          	   	emailinfo.setCompanyId(orderDto.getPayeeId()!=null ? orderDto.getPayeeId(): null);
	                                 emailinfo.setInsuranceId(null);
	                                 
	                             }else {
	                          	   	emailinfo.setCompanyId(null);
	                                 emailinfo.setInsuranceId(null);
	                             }
								emailinfo.setRptFileName("SerologyReport_GroupPrint.rpt");
								res2 = smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.PATHOLOGY_REPORT, emailinfo);
							}
							
						}
						
					}
			
			}
            
            
            com.param.global.common.Response labres =  iLabConfigurationService.getLabConfigurationList(list.get(0).getOrgId(),list.get(0).getOrgUnitId());
            
	           if(labres.getListObject() !=null && labres.getListObject().size()>0) {
	        	   
	        	   List<LabConfigurationDto> lablist = labres.getListObject();
	        	   
	        	   if(lablist.get(0).getQrCodeRequired() == 'Y' && lablist.get(0).getQrCodeRequired() !=null) {
	        		   
	        		   for (SerologyTestResultDto labResultMasterDto : list) {
	          				
	                       int lowerLimit = 97;
	                       int upperLimit = 122;
	                       Random random = new Random();
	                       StringBuffer r = new StringBuffer(10);
	                       for (int i = 0; i < 10; i++) {
	                           int nextRandomChar = lowerLimit+ (int)(random.nextFloat() * (upperLimit - lowerLimit + 1));
	                           r.append((char)nextRandomChar);
	                       }
	                       
	                       String str= "";
	                       if(resPublicUrl == null) {
	                       	str= ServletUtils.getBaseUrl(request)+"/LIS/BioChemistry/QR/"+labResultMasterDto.getLabSampleDtlsId()+"/"+labResultMasterDto.getOrgUnitId()
	                   		+"/"+labResultMasterDto.getOrgId()+"/"+r.toString();  
	                       }else {
	                       	str= resPublicUrl+"/LIS/BioChemistry/QR/"+labResultMasterDto.getLabSampleDtlsId()+"/"+labResultMasterDto.getOrgUnitId()
	                   		+"/"+labResultMasterDto.getOrgId()+"/"+r.toString(); 
	                       }
	                       
	               		   
	               		
	               		//String path = "C:\\Users\\ASUS\\Downloads\\Quote.png";  
	               		String charset = "UTF-8";  
	               		Map<EncodeHintType, ErrorCorrectionLevel> hashMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();  
	               		
	               		hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);  

	               		com.google.zxing.common.BitMatrix matrix = new MultiFormatWriter().encode(new String(str.getBytes(charset), charset), BarcodeFormat.QR_CODE, 200, 200);  
	               		
	               		//MatrixToImageWriter.writeToFile(matrix, path.substring(path.lastIndexOf('.') + 1), new File(path));
	               		 ByteArrayOutputStream bos = new ByteArrayOutputStream();
	               		 MatrixToImageWriter.writeToStream(matrix, "png", bos);
	               	     bos.close();
	               	     
	               	     com.param.global.common.Response orgRes = iBiochemReportReleaseService.getOrganiazationFolderPath(labResultMasterDto.getOrgId());
	               	     OrganizationMasterDto organizationMasterDto = (OrganizationMasterDto) orgRes.getObject();
	       			 	 String docPath="";
	       			   
	       			 	Response bucketRes =iBiochemReportReleaseService.getbucketTypeForUpload(labResultMasterDto.getOrgId(),labResultMasterDto.getOrgUnitId());
	       			    int bucketType =(int) bucketRes.getObject();
	       			 	 
	       			    	 docPath =  docUploadUtility.updateQRFile(bos,organizationMasterDto.getOrganizationFolder(),r.toString(),labResultMasterDto.getOrgId(),labResultMasterDto.getOrgUnitId());

						   Response awsCredResponse = iBiochemReportReleaseService.getAwsCreds();
						   OrganizationMasterDto awsCredDto = (OrganizationMasterDto) awsCredResponse.getObject();
						   String ACCESS_KEY = awsCredDto != null && awsCredDto.getAwsAccessKey() != null ? awsCredDto.getAwsAccessKey() :"";
						   String SECRET_KEY = awsCredDto != null && awsCredDto.getAwsSecretKey() != null ? awsCredDto.getAwsSecretKey() :"";

	       			    	 ByteArrayOutputStream byteOutPutStream = new ByteArrayOutputStream();
	       			    	if(bucketType == 1) {
	       			    	 AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
	       			    	 AmazonS3 s3Client = new AmazonS3Client(awsCredentials);
	       			    	 String path3[]=docPath.split(LAB_REPORTS_BUCKET+"/");
	       			    	 S3Object s3object = s3Client.getObject(BUCKET_NAME + "/" +organizationMasterDto.getOrganizationFolder()+LAB_REPORTS_BUCKET,path3[1]);
	       			    	 S3ObjectInputStream inputStream = s3object.getObjectContent();
	       			    	 
	       			    	 IOUtils.copy(inputStream, byteOutPutStream);
	        		   } else {
							URL url = new URL(docPath);
							URLConnection connection = url.openConnection();
							InputStream inputStreamNew = connection.getInputStream();
							byte[] buffer = new byte[4096];
							int length;
							while ((length = inputStreamNew.read(buffer)) != -1) {
								byteOutPutStream.write(buffer, 0, length);
							}

							// Get the byte array from the ByteArrayOutputStream
							byte[] contents = byteOutPutStream.toByteArray();

							// Close the input stream and the ByteArrayOutputStream
							// inputStreamNew.close();
							// outputStream.close();
							IOUtils.copy(inputStreamNew, byteOutPutStream);
						}
	       			    	
	       			    	 iBiochemReportReleaseService.updateQAPathInLabsampleDetails(byteOutPutStream,docPath,labResultMasterDto.getLabSampleDtlsId(),str);	 
	                   	
	       			}
	        	   }
	        	   
	           }
            
			if(res2!=null && res2.getStatus().equals(SUCCESS) && res2.getMessage()!=null) {
				res.setObject(res2.getMessage());
			}
			return res;
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}
	
	@RequestMapping(value = "/updateResultEntry", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateResultEntry(@RequestBody List<SerologyTestResultDto> list)
	{
		try
		{
			return iSerologyResultService.updateResultEntry(list);
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}
	
	@RequestMapping(value = "/recollectSampleForSerlogy", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response recollectSampleForSerlogy(@RequestBody SearchDto dto)
	{
		try
		{
			return iSerologyResultService.recollectSampleForSerlogy(dto);
			 
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	
	@RequestMapping(value = "/retestSampleForSerlogy", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response retestSampleForSerlogy(@RequestBody SearchDto dto)
	{
		try
		{
			return iSerologyResultService.retestSampleForSerlogy(dto);
			 
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	
	
}
