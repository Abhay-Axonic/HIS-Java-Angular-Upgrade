package com.param.lis.transaction.controller;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.param.global.common.ServiceException;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.global.dto.TemplateTransactionDto;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ISmsUtilityConstants;
import com.param.global.common.ServletUtils;
import com.param.global.dto.AdmissionDto;
import com.param.global.dto.OrderMasterDto;
import com.param.global.service.IUnitApplicationBillingConfigurationService;
import com.param.global.unit.dto.SmsUtilityConfigInfoDto;
import com.param.global.unit.service.ISmsUtilityConfigService;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.transaction.dto.BioChemParamDto;
import com.param.lis.transaction.dto.LabResultMasterDto;
import com.param.lis.transaction.dto.LabSampleDetailsMasterDto;
import com.param.lis.transaction.dto.LabSampleMasterDto;
import com.param.lis.transaction.dto.SampleAcceptancePendingDto;
import com.param.lis.transaction.service.IBioChemResultEntryService;
import com.param.lis.transaction.service.IBiochemReportReleaseService;
import com.param.lis.transaction.service.ILabTransactionService;
import com.param.utility.TimeZoneComponent;

@RestController
@SuppressWarnings(
{ "rawtypes", "unchecked","null" })
@RequestMapping(value = "/LIS/BioChemistry")
public class BioChemResultEntryController implements ICommonConstants, IError
{
	
	@Autowired
	private IBioChemResultEntryService iBioChemResultEntryService;
	
	@Autowired
	private IBiochemReportReleaseService iBiochemReportReleaseService;
	
	@Autowired
	ILabTransactionService iLabTransactionService;
	
	@Autowired
	private IUnitApplicationBillingConfigurationService unitApplicationBillingConfigurationService;

	final static Logger logger = Logger.getLogger(BioChemResultEntryController.class);
	
	@Autowired
	private ISmsUtilityConfigService smsUtilityService;
	
	@Autowired
	private SessionFactory sessionFactory;

    @Autowired
    IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;
	
	@RequestMapping(value = "/ResultEntry", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getResultEntryList(
			@RequestBody BioChemParamDto bioChemParamDto)
	{
		Response<SampleAcceptancePendingDto, Integer> response = null;
		try
		{
			response = iBioChemResultEntryService.getResultEntryList(bioChemParamDto);
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	@RequestMapping(value = "/ResultEntry/Count", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getResultEntryListCount(@RequestBody BioChemParamDto bioChemParamDto)
	{
		Response<SampleAcceptancePendingDto, Integer> response = null;
		try
		{
			response = iBioChemResultEntryService.getResultEntryListCount(bioChemParamDto);
			return response;
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}
	
	@RequestMapping(value = "/ResultEntry/Details", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getResultEntryDetails(@RequestBody List<BioChemParamDto> bioChemParamDtoList)
	{
		Response<LabSampleDetailsMasterDto, Integer> response = null;
		try
		{
			response = iBioChemResultEntryService.getResultEntryDetails(bioChemParamDtoList);
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	
	@RequestMapping(value = "/ResultEntry/EntryDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveEntryDetails(@RequestBody List<LabResultMasterDto> listLabResultMasterDto)
	{
		Response<LabResultMasterDto, Integer> response = null;
		try
		{
			response = iBioChemResultEntryService.saveEntryDetails(listLabResultMasterDto);
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	
	@RequestMapping(value = "/machineResult", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getTestMachineResult(@RequestBody Map<String,Integer> sampleIds)
	{
		try
		{
			return iBioChemResultEntryService.getTestMachineResult(sampleIds);
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	
	 @RequestMapping(value = "/resultEntry/Search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	  public @ResponseBody Response labResultEntrySearch(@RequestBody SearchDto searchDto)
	  {
	      Response<LabSampleMasterDto, Integer> response = null;
	      try
	      {
	          response = iBioChemResultEntryService.labResultEntrySearch(searchDto);
	          return response;
	      } catch (Exception e)
	      {
	          logger.error("Exception", e);
	          throw e;
	      }
	  }
	 
	 
	 
	 @RequestMapping(value = "/releaseEntry/EntryDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response saveReleaseEntryDetails(@RequestBody List<LabResultMasterDto> listLabResultMasterDto,HttpServletRequest request) throws Exception
		{
			Response<LabResultMasterDto, Integer> response = null;
			com.param.global.common.Response res2 = null;
			try
			{
				response = iBioChemResultEntryService.saveReleaseEntryDetails(listLabResultMasterDto);
				 if(response.getCode().equals(SUCCESS_200)) {
					 
					 List<LabResultMasterDto> list = new ArrayList<>();
					 list.addAll(response.getListObject());
					// iBioChemResultEntryService.sendEmail(list);
					 String pattern = "yyyy-MM-dd HH:mm:ss";
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
						String date1 = simpleDateFormat.format(TimeZoneComponent.getDateByzone(listLabResultMasterDto!=null && listLabResultMasterDto.size()>0 
								? listLabResultMasterDto.getFirst().getOrgUnitId():null));
						
						String resPublicUrl =  unitApplicationBillingConfigurationService.getPublicUrl(listLabResultMasterDto.getFirst().getAssignUnit());
						
						if(listLabResultMasterDto.getFirst().getSendMailOnOrderCompletion()!= null && listLabResultMasterDto.getFirst().getSendMailOnOrderCompletion() ==  true) {
							
							List<String> orderNos = new ArrayList<>();
							for (LabResultMasterDto labResultMasDto : listLabResultMasterDto) {
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
							
							Response  res = iBiochemReportReleaseService.getOrderIdByOrderNumber(orderNos,listLabResultMasterDto.getFirst().getAssignUnit());
							
							if(res.getListObject() != null && res.getListObject().size()>0) {
								List<OrderMasterDto> orderList = new ArrayList<>();
								orderList.addAll(res.getListObject());
								
								for (OrderMasterDto orderMasterDto : orderList) {
									LabResultMasterDto dto = new LabResultMasterDto();
									
									for (LabResultMasterDto orderMasterDto2 : listLabResultMasterDto) {
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
				                        
				                    if(orderDto.getVisitTypeId() == OP) {
				                    	
				                    	 Response  checkDueAmount = iBiochemReportReleaseService.checkDueAmount(orderDto);
				                    	 
				                    	 if(checkDueAmount.getObject()!= null && checkDueAmount.getObject().equals(SendMail)) {
				                    		 SendMail = true;
				                    	 }else {
				                    		 SendMail = false;
				                    	 }
				                    }
				                        
									
									if(SendMail && dto.getLabSampleDtlsId() != null) {
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
											
											Response  res1 =iBiochemReportReleaseService.getDoctorIdByLabSampleDetailsId(dto.getLabSampleDtlsId());
											
											if(res1.getObject() != null ) {
												AdmissionDto admDto = (AdmissionDto) res1.getObject();
												emailinfo.setConsultingDoctorId(admDto.getDoctorId()!= null ?admDto.getDoctorId() : null );
												emailinfo.setAdmitingDoctorId(admDto.getDoctorId()!= null ?admDto.getDoctorId() : null);
												emailinfo.setReferringDoctorId(admDto.getReferralId()!= null ?admDto.getReferralId() : null);
												emailinfo.setTreatingDoctorId(admDto.getDoctorId()!= null ?admDto.getDoctorId() : null);
											}
											
											emailinfo.setLabSampleDtlsIdsList(labSampleDetlsIds);
											
											 Response  checkIsCritical =iBiochemReportReleaseService.getCheckIsParameterCritical(labSampleDetlsIds);
											 if(checkIsCritical.getObject()!= null) {
												 
												 Integer checkCritical = (Integer)checkIsCritical.getObject();
												 if(checkCritical >0){
													 emailinfo.setCriticalReport('Y');
												 }else {
													 emailinfo.setCriticalReport('N');
												 }
											 }else {
												 emailinfo.setCriticalReport('N');
											 }
											   String criticalUrl= "";
						                        if(resPublicUrl == null) {
						                        	criticalUrl= ServletUtils.getBaseUrl(request)+"/LIS/BioChemistry/pathology/report/"+dto.getLabSampleDtlsId()+"/"+listLabResultMasterDto.getFirst().getAssignUnit()
						                            		+"/"+listLabResultMasterDto.getFirst().getOrgId();  
						                        }else {
						                        	criticalUrl= resPublicUrl+"/LIS/BioChemistry/pathology/report/"+dto.getLabSampleDtlsId()+"/"+listLabResultMasterDto.getFirst().getAssignUnit()
						                            		+"/"+listLabResultMasterDto.getFirst().getOrgId(); 
						                        }
						                    emailinfo.setLabReportUrl(criticalUrl);
											
										 	emailinfo.setPatientId(dto.getPatientId());
											emailinfo.setUnitId(listLabResultMasterDto.getFirst().getAssignUnit());
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
			                                       
			                                }else  if(orderDto.getPaymentEntitlementId() != null && orderDto.getPaymentEntitlementId() == ICommonConstants.COMPANY) {
			                                	
			                                	   emailinfo.setCompanyId(orderDto.getPayeeId()!=null ? orderDto.getPayeeId(): null);
			                                       emailinfo.setInsuranceId(null);
			                                       
			                                }else {
			                                	   emailinfo.setCompanyId(null);
			                                       emailinfo.setInsuranceId(null);
			                                }
											  
											emailinfo.setRptFileName("Multireportprint.rpt");
											emailinfo.setSrfId(dto.getSrfId()!=null ? dto.getSrfId():null);
											res2 = smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.PATHOLOGY_REPORT, emailinfo);
											
											
											//send sms
			                                
			                                //for getting report URL
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
			                                	 str= ServletUtils.getBaseUrl(request)+"/LIS/BioChemistry/QR/"+dto.getLabSampleDtlsId()+"/"+listLabResultMasterDto.getFirst().getAssignUnit()
					                        		+"/"+dto.getOrgId()+"/"+r.toString(); 
			                                }else {
			                                	 str= resPublicUrl+"/LIS/BioChemistry/QR/"+dto.getLabSampleDtlsId()+"/"+listLabResultMasterDto.getFirst().getAssignUnit()
					                        		+"/"+dto.getOrgId()+"/"+r.toString(); 
			                                }
			            			    	
			                        		emailinfo.setLabReportUrl(str);
			        						smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.PATHOLOGY_REPORT, emailinfo);
										}
										
									}
								}
							}
							
							
						}else 	 if(listLabResultMasterDto.getFirst().getTriggerReleaseMail()!= null && listLabResultMasterDto.getFirst().getTriggerReleaseMail() ==  true) {
						
							List<String> orderNos = new ArrayList<>();
			                for (LabResultMasterDto labResultMasDto : listLabResultMasterDto) {
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
						
			                Response  resOrderNo = iBiochemReportReleaseService.getOrderIdByOrderNumber(orderNos,listLabResultMasterDto.getFirst().getAssignUnit());
							
						 for (LabResultMasterDto labResultMasterDto : list) {
							 
							 Integer OrderId = null;
							   if(resOrderNo.getListObject() != null && resOrderNo.getListObject().size()>0) {
				                    List<OrderMasterDto> orderList = new ArrayList<>();
				                    orderList.addAll(resOrderNo.getListObject());
				                    
				                    for (OrderMasterDto orderMasterDto : orderList) {
				                        
				                    	   for (LabResultMasterDto orderMasterDto2 : listLabResultMasterDto) {
				                    		   
				                               if(orderMasterDto2.getLabSampleDtlsId().equals(labResultMasterDto.getLabSampleDtlsId())) {
				                            	   
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
		                        
			                    if(orderDto.getVisitTypeId() == OP) {
			                    	
			                    	 Response  checkDueAmount = iBiochemReportReleaseService.checkDueAmount(orderDto);
			                    	 
			                    	 if(checkDueAmount.getObject()!= null && checkDueAmount.getObject().equals(SendMail)) {
			                    		 SendMail = true;
			                    	 }else {
			                    		 SendMail = false;
			                    	 }
			                    }
							 if(SendMail) {
								 Response exclusion = iBiochemReportReleaseService.getLabExclusion(labResultMasterDto.getLabSampleDtlsId(), listLabResultMasterDto.getFirst().getAssignUnit(), listLabResultMasterDto.getFirst().getOrgId());

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
									
									Response  res =iBiochemReportReleaseService.getDoctorIdByLabSampleDetailsId(labResultMasterDto.getLabSampleDtlsId());
									
									if(res.getObject() != null ) {
										AdmissionDto admDto = (AdmissionDto) res.getObject();
										emailinfo.setConsultingDoctorId(admDto.getDoctorId()!= null ?admDto.getDoctorId() : null );
										emailinfo.setAdmitingDoctorId(admDto.getDoctorId()!= null ?admDto.getDoctorId() : null);
										emailinfo.setReferringDoctorId(admDto.getReferralId()!= null ?admDto.getReferralId() : null);
										emailinfo.setTreatingDoctorId(admDto.getDoctorId()!= null ?admDto.getDoctorId() : null);
									}
									
									 List<Integer>labSampleDetlsIds = new ArrayList<>();
									 labSampleDetlsIds.add(labResultMasterDto.getLabSampleDtlsId());
									 Response  checkIsCritical =iBiochemReportReleaseService.getCheckIsParameterCritical(labSampleDetlsIds);
									 if(checkIsCritical.getObject()!= null) {
										 
										 Integer checkCritical = (Integer)checkIsCritical.getObject();
										 if(checkCritical >0){
											 emailinfo.setCriticalReport('Y');
										 }else {
											 emailinfo.setCriticalReport('N');
										 }
									 }else {
										 emailinfo.setCriticalReport('N');
									 }
									   String criticalUrl= "";
				                        if(resPublicUrl == null) {
				                        	criticalUrl= ServletUtils.getBaseUrl(request)+"/LIS/BioChemistry/pathology/report/"+labResultMasterDto.getLabSampleDtlsId()+"/"+listLabResultMasterDto.getFirst().getAssignUnit()
				                            		+"/"+listLabResultMasterDto.getFirst().getOrgId();  
				                        }else {
				                        	criticalUrl= resPublicUrl+"/LIS/BioChemistry/pathology/report/"+labResultMasterDto.getLabSampleDtlsId()+"/"+listLabResultMasterDto.getFirst().getAssignUnit()
				                            		+"/"+listLabResultMasterDto.getFirst().getOrgId(); 
				                        }
				                    emailinfo.setLabReportUrl(criticalUrl);
									
								 	emailinfo.setLabSampleDtlsId( labResultMasterDto.getLabSampleDtlsId());
								 	emailinfo.setPatientId(labResultMasterDto.getPatientId());
									emailinfo.setUnitId(listLabResultMasterDto.getFirst().getAssignUnit());
									emailinfo.setOrganizationId(labResultMasterDto.getOrgId());
									emailinfo.setDateTime(date1);
	                                if(labResultMasterDto.getCreatedBy()!=null) {
	            	                	
	            	                	Integer  empId =iBiochemReportReleaseService.getgetEmpId(labResultMasterDto.getCreatedBy()); 
					                	emailinfo.setEmployeeId(empId);
	                                }
									emailinfo.setTestDesc(labResultMasterDto.getTestDesc());
									
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
									  
									
									emailinfo.setRptFileName("pathologyReport_GroupPrint.rpt");
									emailinfo.setSrfId(listLabResultMasterDto.getFirst().getSrfId()!=null ? listLabResultMasterDto.getFirst().getSrfId():null);
									res2 = smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.PATHOLOGY_REPORT, emailinfo);
									
									//send sms
			                        
			                        //for getting report URL
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
	                                	
	                                	str= ServletUtils.getBaseUrl(request)+"/LIS/BioChemistry/QR/"+labResultMasterDto.getLabSampleDtlsId()+"/"+listLabResultMasterDto.getFirst().getAssignUnit()
	    		                		+"/"+labResultMasterDto.getOrgId()+"/"+r.toString(); 
	                                	
	                                }else {
	                                	
	                                	str= resPublicUrl+"/LIS/BioChemistry/QR/"+labResultMasterDto.getLabSampleDtlsId()+"/"+listLabResultMasterDto.getFirst().getAssignUnit()
	    		                		+"/"+labResultMasterDto.getOrgId()+"/"+r.toString(); 
	                                }
	                        		
			                        
			                		  
			    			    	
			                		emailinfo.setLabReportUrl(str);
									smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.PATHOLOGY_REPORT, emailinfo);
		                        }
							 
							
							 }
						
					}	
					
					
				}
				 LabResultMasterDto dto = new LabResultMasterDto();
				 if(res2!=null && res2.getStatus().equals(SUCCESS)  && res2.getMessage()!=null) {
					 dto.setRptResponse(res2.getMessage());
						response.setObject(dto);
					}
				return response;
			} catch (Exception e)
			{
				logger.error("Exception", e);
				throw e;
			}
		}
	 
	 
	 
	 @RequestMapping(value = "consolidated/resultEntry/Search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	  public @ResponseBody Response consolidatedResultEntrySearch(@RequestBody SearchDto searchDto)
	  {
	      Response<LabSampleMasterDto, Integer> response = null;
	      try
	      {
	          response = iBioChemResultEntryService.consolidatedResultEntrySearch(searchDto);
	          return response;
	      } catch (Exception e)
	      {
	          logger.error("Exception", e);
	          throw e;
	      }
	  }
	 
	 @RequestMapping(value = "consolidated/resultEntry/Type/Search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	  public @ResponseBody Response consolidatedResultEntryTypeSearch(@RequestBody SearchDto searchDto)
	  {
	      Response<LabSampleMasterDto, Integer> response = null;
	      try
	      {
	          response = iBioChemResultEntryService.consolidatedResultEntryTypeSearch(searchDto);
	          return response;
	      } catch (Exception e)
	      {
	          logger.error("Exception", e);
	          throw e;
	      }
	  }
	 
	 @RequestMapping(value = "/resultEntry/Type/Search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	  public @ResponseBody Response labResultEntryTypeSearch(@RequestBody SearchDto searchDto)
	  {
	      Response<LabSampleMasterDto, Integer> response = null;
	      try
	      {
	          response = iBioChemResultEntryService.labResultEntryTypeSearch(searchDto);
	          return response;
	      } catch (Exception e)
	      {
	          logger.error("Exception", e);
	          throw e;
	      }
	  }
	 

		@RequestMapping(value = "/getParameterValueByMachineId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response getParameterValueByMachineId(@RequestBody BioChemParamDto bioChemParamDtoList)
		{
			Response<LabSampleDetailsMasterDto, Integer> response = null;
			try
			{
				response = iBioChemResultEntryService.getParameterValueByMachineId(bioChemParamDtoList);
				return response;
			} catch (Exception e)
			{
				logger.error("Exception", e);
				throw e;
			}
		}

        @RequestMapping(value = "/ResultEntry/updateEntryDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
        public @ResponseBody Response updateEntryDetails(@RequestBody List<LabResultMasterDto> listLabResultMasterDto)
        {
            Response<LabResultMasterDto, Integer> response = null;
            try
            {
                response = iBioChemResultEntryService.updateEntryDetails(listLabResultMasterDto);
                return response;
            } catch (Exception e)
            {
                logger.error("Exception", e);
                throw e;
            }
        }
        @RequestMapping(value = "barcodeScannerList/Search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  	  public @ResponseBody Response barcodeScannerListSearch(@RequestBody SearchDto searchDto)
  	  {
  	      Response<LabSampleMasterDto, Integer> response = null;
  	      try
  	      {
  	          response = iBioChemResultEntryService.barcodeScannerListSearch(searchDto);
  	          return response;
  	      } catch (Exception e)
  	      {
  	          logger.error("Exception", e);
  	          throw e;
  	      }
  	  }
        @RequestMapping(value = "barcodeScannerList/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    	  public @ResponseBody Response barcodeScannerListSave(@RequestBody List<LabResultMasterDto> listLabResultMasterDto)
    	  {
        	Response<LabResultMasterDto, Integer> response = null;
    	      try
    	      {
    	          response = iBioChemResultEntryService.barcodeScannerListSave(listLabResultMasterDto);
    	          return response;
    	      } catch (Exception e)
    	      {
    	          logger.error("Exception", e);
    	          throw e;
    	      }
    	  }
        @RequestMapping(value = "consolidated/resultEntry/sampleRecollection", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  	  public @ResponseBody Response getSampleRecollection(@RequestBody SearchDto searchDto)
  	  {
  	      Response<LabSampleMasterDto, Integer> response = null;
  	      try
  	      {
  	          response = iBioChemResultEntryService.getSampleRecollection(searchDto);
  	          return response;
  	      } catch (Exception e)
  	      {
  	          logger.error("Exception", e);
  	          throw e;
  	      }
  	  }
        
    	@RequestMapping(value = "/saveInterfaceResult", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    	public @ResponseBody Response saveInterfaceResult(@RequestBody LabResultMasterDto labResultMasterDto)
    	{
    		Response<LabResultMasterDto, Integer> response = null;
    		try
    		{
    			response = iBioChemResultEntryService.saveInterfaceResult(labResultMasterDto);
    			return response;
    		} catch (Exception e)
    		{
    			logger.error("Exception", e);
    			throw e;
    		}
    	}

    @RequestMapping(value = "/getLabReportsForWebUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response getLabReportsForWebUser(@RequestBody LabResultMasterDto labResultMasterDto, HttpServletResponse servletResponse) {
        try{

            List<Integer> labSampleDetailsIds = new ArrayList<>();
            List<TemplateTransactionDto> templateTrList = new ArrayList<>();
            if((labResultMasterDto.getPatientId()!= null && labResultMasterDto.getPatientId() > 0) && (labResultMasterDto.getMobileNo() == null || labResultMasterDto.getMobileNo().trim().isEmpty())){
                List<Integer> patientIds = new ArrayList<>();
                patientIds.add(labResultMasterDto.getPatientId());
                labSampleDetailsIds = iBioChemResultEntryService.getLabSampleDetailIdsByPatientId(labResultMasterDto, patientIds);
//                templateTrList = iBioChemResultEntryService.getRISReportsByPatientId(labResultMasterDto, patientIds);
            }
            else if((labResultMasterDto.getMobileNo() != null && !labResultMasterDto.getMobileNo().trim().isEmpty()) && (labResultMasterDto.getPatientId() == null || labResultMasterDto.getPatientId() <= 0)){
                Response res = iBioChemResultEntryService.getLabSampleDetailIdsByMobileNo(labResultMasterDto);
                labSampleDetailsIds = res.getListObject();
//                templateTrList = res.getListObject1();
            }

            if(labSampleDetailsIds != null && !labSampleDetailsIds.isEmpty()) {

                String sampleDetailIds = labSampleDetailsIds.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(","));

                String reportParam = "{\"appName\": \"HIS\"," +
                        " \"appSecretKey\": \"f306b6eccd9d5069d2dc71afe139271a\"," +
                        "\"reportDesignFilePath\": \"Multireportprint_listview.rpt\"," +
                        "\"reportFormat\": \"PDF\"," +
                        "\"exportFileName\": \"pathologyReport\"," +
                        "\"reportParams\": [" +
                        "{" +
                        " \"paramName\": \"lab_sample_dtls_id\"," +
                        " \"paramType\": \"String\"," +
                        " \"paramValue\": \""+ sampleDetailIds +"\"" +
                        " }," +
                        "{" +
                        " \"paramName\": \"isOriginal\"," +
                        " \"paramType\": \"Integer\"," +
                        " \"paramValue\": 1" +
                        " }," +
                        "{" +
                        " \"paramName\": \"isHeader\"," +
                        " \"paramType\": \"Integer\"," +
                        " \"paramValue\":" + 0 +
                        " }" +
                        "]}";

                String API_URL = iUnitApplicationBillingConfigurationDao.getURLForPdfGeneration(labResultMasterDto.getOrgId(), labResultMasterDto.getOrgUnitId());
                ByteArrayOutputStream byteOutPutStream = new ByteArrayOutputStream();

                HttpPost pst = new HttpPost(API_URL);
                pst.addHeader("Content-Type", "application/json");
                pst.setEntity(new StringEntity(reportParam));
                CloseableHttpClient httpClient = HttpClientBuilder.create().build();
                HttpResponse result = httpClient.execute(pst);

                HttpEntity resEntity = result.getEntity();
                resEntity.writeTo(byteOutPutStream);

                return new Response(SUCCESS, SUCCESS_200, SUCCESS, null, byteOutPutStream.toByteArray());
//                return new Response(SUCCESS, SUCCESS_200, SUCCESS, templateTrList, byteOutPutStream.toByteArray());
            }
//            else if(templateTrList != null && !templateTrList.isEmpty()) {
//                return new Response(SUCCESS, SUCCESS_200, SUCCESS, templateTrList, null);
//            }
            else {
                return new Response(SUCCESS, SUCCESS_200, "No Reports Found..", null, null);
            }

        }catch(Exception e){
            logger.error("Exception", e);
            throw new ServiceException(e.getMessage());
        }
    }

}
