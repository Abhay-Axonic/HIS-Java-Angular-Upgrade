package com.param.lis.transaction.controller;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.param.global.common.DocUploadUtility;
import com.param.global.common.HISFileUploadsPath;
import com.param.global.common.ISmsUtilityConstants;
import com.param.global.common.ServiceException;
import com.param.global.common.ServletUtils;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.global.dto.AdmissionDto;
import com.param.global.dto.OrderMasterDto;
import com.param.global.org.dto.OrganizationMasterDto;
import com.param.global.service.IUnitApplicationBillingConfigurationService;
import com.param.global.unit.dto.LabConfigurationDto;
import com.param.global.unit.dto.SmsUtilityConfigInfoDto;
import com.param.global.unit.service.ILabConfigurationService;
import com.param.global.unit.service.ISmsUtilityConfigService;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.transaction.dto.BioChemParamDto;
import com.param.lis.transaction.dto.LabDashBoardDto;
import com.param.lis.transaction.dto.LabResultMasterDto;
import com.param.lis.transaction.dto.LabSampleDetailsMasterDto;
import com.param.lis.transaction.dto.LabSampleMasterDto;
import com.param.lis.transaction.dto.SampleAcceptancePendingDto;
import com.param.lis.transaction.dto.SearchCommonDto;
import com.param.lis.transaction.service.IBiochemReportReleaseService;
import com.param.lis.transaction.service.ILabTransactionService;
import com.param.utility.TimeZoneComponent;
@RestController
@SuppressWarnings(
{ "rawtypes", "unchecked","null" })
@RequestMapping(value = "/LIS/BioChemistry")
public class BiochemReportReleaseController implements ICommonConstants, IError,HISFileUploadsPath 
{
	@Autowired
	private IBiochemReportReleaseService iBiochemReportReleaseService;
	
	@Autowired
	private ISmsUtilityConfigService smsUtilityService;
	
    @Autowired
    ILabTransactionService iLabTransactionService;
    
	@Autowired
	IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;
	
	@Autowired
	DocUploadUtility docUploadUtility;

	@Autowired
	private IUnitApplicationBillingConfigurationService unitApplicationBillingConfigurationService;
	
	@Autowired
	private ILabConfigurationService iLabConfigurationService;
	
	@Autowired
	SessionFactory sessionFactory;

	final static Logger logger = Logger.getLogger(BiochemValidationWorklistController.class);
	
	@RequestMapping(value = "/ReportReleaseWorkList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getReportReleaseWorkList(
			@RequestBody BioChemParamDto bioChemParamDto)
	{
		Response<SampleAcceptancePendingDto, Integer> response = null;
		try
		{
			response = iBiochemReportReleaseService.getReportReleaseWorkList(bioChemParamDto);
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	@RequestMapping(value = "/ReportReleaseWorkList/Count", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getReportReleaseWorkListCount(@RequestBody BioChemParamDto bioChemParamDto)
	{
		Response<SampleAcceptancePendingDto, Integer> response = null;
		try
		{
			response = iBiochemReportReleaseService.getReportReleaseWorkListCount(bioChemParamDto);
			return response;
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}
	
	@RequestMapping(value = "/ReportReleaseWorkList/Details", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getReportReleaseWorkListDetails(@RequestBody List<BioChemParamDto> bioChemParamDtoList)
	{
		Response<LabSampleDetailsMasterDto, Integer> response = null;
		try
		{
			response = iBiochemReportReleaseService.getReportReleaseWorkListDetails(bioChemParamDtoList);
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	
	@RequestMapping(value = "/ReportReleaseWorkList/EntryDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveReportReleaseWorkList(@RequestBody List<LabResultMasterDto> listLabResultMasterDto,HttpServletRequest request) throws Exception
	{
		Response<LabResultMasterDto, Integer> response = null;
		com.param.global.common.Response res2 = null;
		try
		{
		    String pattern = "yyyy-MM-dd HH:mm:ss";
		    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		    String date1=simpleDateFormat.format(TimeZoneComponent.getDateByzone(listLabResultMasterDto.get(0).getOrgUnitId()));

			response = iBiochemReportReleaseService.saveReportReleaseWorkList(listLabResultMasterDto,"");
			
			String resPublicUrl =  unitApplicationBillingConfigurationService.getPublicUrl(listLabResultMasterDto.get(0).getAssignUnit());
			
            if(listLabResultMasterDto.get(0).getSendMailOnOrderCompletion()!= null && listLabResultMasterDto.get(0).getSendMailOnOrderCompletion() ==  true) {
                
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
                
                Response  res = iBiochemReportReleaseService.getOrderIdByOrderNumber(orderNos,listLabResultMasterDto.get(0).getAssingnedFrom() != null ?
						listLabResultMasterDto.get(0).getAssingnedFrom() : listLabResultMasterDto.get(0).getAssignUnit());
                
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
			                        	criticalUrl= ServletUtils.getBaseUrl(request)+"/LIS/BioChemistry/pathology/report/"+dto.getLabSampleDtlsId()+"/"+listLabResultMasterDto.get(0).getAssignUnit()
			                            		+"/"+listLabResultMasterDto.get(0).getOrgId();  
			                        }else {
			                        	criticalUrl= resPublicUrl+"/LIS/BioChemistry/pathology/report/"+dto.getLabSampleDtlsId()+"/"+listLabResultMasterDto.get(0).getAssignUnit()
			                            		+"/"+listLabResultMasterDto.get(0).getOrgId(); 
			                        }
			                    emailinfo.setLabReportUrl(criticalUrl);
			                    
                                
                                 emailinfo.setPatientId(dto.getPatientId());
                                emailinfo.setUnitId(listLabResultMasterDto.get(0).getAssingnedFrom());
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
                                emailinfo.setSrfId(dto.getSrfId()!=null ? dto.getSrfId():null);
                                emailinfo.setRptFileName("Multireportprint.rpt");
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
                                	 str= ServletUtils.getBaseUrl(request)+"/LIS/BioChemistry/QR/"+dto.getLabSampleDtlsId()+"/"+dto.getOrgUnitId()
                             		+"/"+dto.getOrgId()+"/"+r.toString();  
                                }else {
                                	 str= resPublicUrl+"/LIS/BioChemistry/QR/"+dto.getLabSampleDtlsId()+"/"+dto.getOrgUnitId()
                             		+"/"+dto.getOrgId()+"/"+r.toString();  
                                }
                        		
            			    	
                        		emailinfo.setLabReportUrl(str);
        						smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.PATHOLOGY_REPORT, emailinfo);
                                
                            }
                            
                        }
                    }
                }
            }else if(listLabResultMasterDto.get(0).getTriggerReleaseMail()!= null && listLabResultMasterDto.get(0).getTriggerReleaseMail() ==  true) {
			
				if(response.getListObject()!=null && response.getListObject().size()>0) {
					List<LabResultMasterDto> mstrList = response.getListObject();
					
					
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
					
		                Response  resOrderNo = iBiochemReportReleaseService.getOrderIdByOrderNumber(orderNos,listLabResultMasterDto.get(0).getAssingnedFrom() != null ?
								listLabResultMasterDto.get(0).getAssingnedFrom() : listLabResultMasterDto.get(0).getAssignUnit());
		                
					for (LabResultMasterDto dto : mstrList ) {
						
						Integer OrderId = null;
						   if(resOrderNo.getListObject() != null && resOrderNo.getListObject().size()>0) {
			                    List<OrderMasterDto> orderList = new ArrayList<>();
			                    orderList.addAll(resOrderNo.getListObject());
			                    
			                    for (OrderMasterDto orderMasterDto : orderList) {
			                        
			                    	   for (LabResultMasterDto orderMasterDto2 : listLabResultMasterDto) {
			                    		   
			                               if(orderMasterDto2.getLabSampleDtlsId().equals(dto.getLabSampleDtlsId())) {
			                            	   
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
							Response exclusion = iBiochemReportReleaseService.getLabExclusion(dto.getLabSampleDtlsId(), listLabResultMasterDto.get(0).getAssignUnit(), listLabResultMasterDto.get(0).getOrgId());

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
		                	 
		                	//mail 
								SmsUtilityConfigInfoDto emailinfo = new SmsUtilityConfigInfoDto();
								
								Response  res =iBiochemReportReleaseService.getDoctorIdByLabSampleDetailsId(dto.getLabSampleDtlsId());
								
							    List<Integer>labSampleDetlsIds = new ArrayList<>();
							    labSampleDetlsIds.add(dto.getLabSampleDtlsId());
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
			                        	criticalUrl= ServletUtils.getBaseUrl(request)+"/LIS/BioChemistry/pathology/report/"+dto.getLabSampleDtlsId()+"/"+listLabResultMasterDto.get(0).getAssignUnit()
			                            		+"/"+listLabResultMasterDto.get(0).getOrgId();  
			                        }else {
			                        	criticalUrl= resPublicUrl+"/LIS/BioChemistry/pathology/report/"+dto.getLabSampleDtlsId()+"/"+listLabResultMasterDto.get(0).getAssignUnit()
			                            		+"/"+listLabResultMasterDto.get(0).getOrgId(); 
			                        }
			                    emailinfo.setLabReportUrl(criticalUrl);
			                    
			                    
								emailinfo.setLabSampleDtlsId( dto.getLabSampleDtlsId());
								emailinfo.setPatientId(dto.getPatientId());
								if(res.getObject() != null ) {
									AdmissionDto admDto = (AdmissionDto) res.getObject();
									emailinfo.setConsultingDoctorId(admDto.getDoctorId()!= null ?admDto.getDoctorId() : null );
									emailinfo.setAdmitingDoctorId(admDto.getDoctorId()!= null ?admDto.getDoctorId() : null);
									emailinfo.setReferringDoctorId(admDto.getReferralId()!= null ?admDto.getReferralId() : null);
									emailinfo.setTreatingDoctorId(admDto.getDoctorId()!= null ?admDto.getDoctorId() : null);
								}
								
								emailinfo.setUnitId(listLabResultMasterDto.get(0).getAssignUnit());
								emailinfo.setOrganizationId(listLabResultMasterDto.get(0).getOrgId());
								emailinfo.setDateTime(date1);
		                        if(listLabResultMasterDto.get(0).getCreatedBy()!=null) {
		                        	
				                	Integer  empId =iBiochemReportReleaseService.getgetEmpId(listLabResultMasterDto.get(0).getCreatedBy()); 
				                	emailinfo.setEmployeeId(empId);
		                        }
								emailinfo.setTestDesc(dto.getTestDesc());
								
								
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
								emailinfo.setSrfId(listLabResultMasterDto.get(0).getSrfId()!=null ? listLabResultMasterDto.get(0).getSrfId():null);
								emailinfo.setRptFileName("pathologyReport_GroupPrint.rpt");
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
		                        	str= ServletUtils.getBaseUrl(request)+"/LIS/BioChemistry/QR/"+dto.getLabSampleDtlsId()+"/"+listLabResultMasterDto.get(0).getAssignUnit()
		                            		+"/"+listLabResultMasterDto.get(0).getOrgId();  
		                        }else {
		                        	str= resPublicUrl+"/LIS/BioChemistry/QR/"+dto.getLabSampleDtlsId()+"/"+listLabResultMasterDto.get(0).getAssignUnit()
		                            		+"/"+listLabResultMasterDto.get(0).getOrgId(); 
		                        }
		                        
		                		  
		    			    	
		                		emailinfo.setLabReportUrl(str);
								smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.PATHOLOGY_REPORT, emailinfo);
		                	 
		                 }       
	                        
						
					}
				}
			}
            
           com.param.global.common.Response labres =  iLabConfigurationService.getLabConfigurationList(listLabResultMasterDto.get(0).getOrgId(),listLabResultMasterDto.get(0).getAssignUnit());
            
           if(labres.getListObject() !=null && labres.getListObject().size()>0) {
        	   
        	   List<LabConfigurationDto> lablist = labres.getListObject();
        	   
        	   if(lablist.get(0).getQrCodeRequired() == 'Y' && lablist.get(0).getQrCodeRequired() !=null) {
        		   
        		   for (LabResultMasterDto labResultMasterDto : listLabResultMasterDto) {
          				
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
                       	str= ServletUtils.getBaseUrl(request)+"/LIS/BioChemistry/QR/"+labResultMasterDto.getLabSampleDtlsId()+"/"+listLabResultMasterDto.get(0).getAssignUnit()
                   		+"/"+labResultMasterDto.getOrgId()+"/"+r.toString();  
                       }else {
                       	str= resPublicUrl+"/LIS/BioChemistry/QR/"+labResultMasterDto.getLabSampleDtlsId()+"/"+listLabResultMasterDto.get(0).getAssignUnit()
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
       			   
       			 	 
       			 	Response bucketRes =iBiochemReportReleaseService.getbucketTypeForUpload(labResultMasterDto.getOrgId(),labResultMasterDto.getAssignUnit());
       			    int bucketType =(int) bucketRes.getObject();
       			 
					 docPath =  docUploadUtility.updateQRFile(bos,organizationMasterDto.getOrganizationFolder(),r.toString(),labResultMasterDto.getOrgId(),listLabResultMasterDto.get(0).getAssignUnit());

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
							// ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
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
           
			LabResultMasterDto dto = new LabResultMasterDto();
			if(res2!=null && res2.getStatus().equals(SUCCESS) && res2.getMessage()!=null) {
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
	
	   @RequestMapping(value = "/bulk/Release", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	    public @ResponseBody Response releaseNormanReports(@RequestBody List<LabResultMasterDto> listLabResultMasterDto)
	    {
	        Response<LabResultMasterDto, Integer> response = null;
	        try
	        {
	            response = iBiochemReportReleaseService.releaseNormanReports(listLabResultMasterDto);
	            return response;
	        } catch (Exception e)
	        {
	            logger.error("Exception", e);
	            throw e;
	        }
	    }
	
      @RequestMapping(value = "/patient/Search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
      public @ResponseBody Response searchPatient(@RequestBody SearchCommonDto searchCommonDto)
      {
          Response<LabDashBoardDto, Integer> response = null;
          try
          {
              response = iBiochemReportReleaseService.searchPatient(searchCommonDto);
              return response;
          } catch (Exception e)
          {
              logger.error("Exception", e);
              throw e;
          }
      }
      
      @RequestMapping(value = "/ReportReleaseWorkList/Search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
      public @ResponseBody Response getFilteredReportReleaseWorkList(@RequestBody SearchDto searchDto)
      {
          Response<LabSampleMasterDto, Integer> response = null;
          try
          {
              response = iBiochemReportReleaseService.getFilteredReportReleaseWorkList(searchDto);
              return response;
          } catch (Exception e)
          {
              logger.error("Exception", e);
              throw e;
          }
      }
      
      
      @RequestMapping(value = "/ConsolidatedReleaseWorkList/Search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
      public @ResponseBody Response ConsolidatedReleaseWorkList(@RequestBody SearchDto searchDto)
      {
          Response<LabSampleMasterDto, Integer> response = null;
          try
          {
              response = iBiochemReportReleaseService.ConsolidatedReleaseWorkList(searchDto);
              return response;
          } catch (Exception e)
          {
              logger.error("Exception", e);
              throw e;
          }
      }
      
      @RequestMapping(value = "/ConsolidatedReleaseWorkList/type/Search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
      public @ResponseBody Response ConsolidatedReleaseWorkTypeSearchList(@RequestBody SearchDto searchDto)
      {
          Response<LabSampleMasterDto, Integer> response = null;
          try
          {
              response = iBiochemReportReleaseService.ConsolidatedReleaseWorkTypeSearchList(searchDto);
              return response;
          } catch (Exception e)
          {
              logger.error("Exception", e);
              throw e;
          }
      }
      
      
      @RequestMapping(value = "/ReportReleaseWorkList/Type/Search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
      public @ResponseBody Response getFilteredReportReleaseTypeSearchWorkList(@RequestBody SearchDto searchDto)
      {
          Response<LabSampleMasterDto, Integer> response = null;
          try
          {
              response = iBiochemReportReleaseService.getFilteredReportReleaseTypeSearchWorkList(searchDto);
              return response;
          } catch (Exception e)
          {
              logger.error("Exception", e);
              throw e;
          }
      }
      
      
      
	@RequestMapping(value = "/releaseMailOnCreditNote", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  	public @ResponseBody Response releaseMailOnCreditNote(@RequestBody OrderMasterDto orderMasterDto) throws Exception
  	{
  		Response<LabResultMasterDto, Integer> response = null;
  		com.param.global.common.Response res2 = null;
  		try
  		{
  			String pattern = "yyyy-MM-dd HH:mm:ss";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			String date1 = simpleDateFormat.format(TimeZoneComponent.getDateByzone(orderMasterDto.getOrgUnitId()));
                      
                LabResultMasterDto dto = new LabResultMasterDto();
                          
                Response  OrderRes = iBiochemReportReleaseService.getCompanyIdByOrderId(orderMasterDto.getOrderId());
                OrderMasterDto orderDto= new OrderMasterDto();
                if(OrderRes.getObject() != null) {
                          	
                orderDto = (OrderMasterDto) OrderRes.getObject();
                SearchDto searchDto = new  SearchDto();
                searchDto.setOrderId(orderMasterDto.getOrderId());
                searchDto.setOrgId(orderMasterDto.getOrgId());
                

                Boolean SendMail = true;
                
	            if(orderDto.getVisitTypeId() == OP) {
	            	
	            	 Response  checkDueAmount = iBiochemReportReleaseService.checkDueAmount(orderDto);
	            	 
	            	 if(checkDueAmount.getObject()!= null && checkDueAmount.getObject().equals(SendMail)) {
	            		 SendMail = true;
	            	 }else {
	            		 SendMail = false;
	            	 }
	            }
                
                
                Response labSampleDetailIds = iLabTransactionService.checkForOrderCompletion(searchDto);
                if(SendMail && labSampleDetailIds.getListObject()!= null && labSampleDetailIds.getListObject().size()>0) {
                    
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
                    
                    Response  res1 =iBiochemReportReleaseService.getDoctorIdByLabSampleDetailsId(LabSampleDetailsMasterList.get(0).getLabSampleDtlsId());
                    
                    if(res1.getObject() != null ) {
                        AdmissionDto admDto = (AdmissionDto) res1.getObject();
                        emailinfo.setConsultingDoctorId(admDto.getDoctorId()!= null ?admDto.getDoctorId() : null );
                        emailinfo.setAdmitingDoctorId(admDto.getDoctorId()!= null ?admDto.getDoctorId() : null);
                        emailinfo.setReferringDoctorId(admDto.getReferralId()!= null ?admDto.getReferralId() : null);
                        emailinfo.setTreatingDoctorId(admDto.getDoctorId()!= null ?admDto.getDoctorId() : null);
                    }
                    emailinfo.setLabSampleDtlsIdsList(labSampleDetlsIds);
                     emailinfo.setPatientId(orderMasterDto.getPatientId());
                    emailinfo.setUnitId(orderMasterDto.getOrgUnitId());
                    emailinfo.setOrganizationId(orderMasterDto.getOrgId());
                    emailinfo.setDateTime(date1);
                    if(dto.getCreatedBy()!=null) {
                    	Integer  empId =iBiochemReportReleaseService.getgetEmpId(orderMasterDto.getCreatedBy()); 
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


                LabResultMasterDto dto1 = new LabResultMasterDto();
                	if(res2!=null && res2.getStatus().equals(SUCCESS) && res2.getMessage()!=null) {
                		dto1.setRptResponse(res2.getMessage());
                		response.setObject(dto1);
                	}     	
                }
                          
                            
  			return response;
  		} catch (Exception e)
  		{
  			logger.error("Exception", e);
  			throw e;
  		}
  	}
  	
	
	 
    @RequestMapping(value = "/getLabSampleDetailsIdByOrderId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response getLabSampleDetailsIdByOrderId(@RequestBody List<SearchDto> searchDto)
    {
        Response<LabSampleMasterDto, Integer> response = null;
        try
        {
            response = iBiochemReportReleaseService.getLabSampleDetailsIdByOrderId(searchDto);
            return response;
        } catch (Exception e)
        {
            logger.error("Exception", e);
            throw e;
        }
    }
    
	@RequestMapping(value = "/QR/{LabDetailsId}/{unitId}/{orgId}/{randomNumber}")
	public void getReportByQRCode(@PathVariable("LabDetailsId")Integer LabDetailsId,@PathVariable("unitId")Integer unitId,@PathVariable("orgId")int orgId,@PathVariable("randomNumber")String randomNumber,HttpServletRequest request,HttpServletResponse response){
		try{
			

			String rptString = "{\"appName\": \"HIS\"," + 
					" \"appSecretKey\": \"f306b6eccd9d5069d2dc71afe139271a\"," + 
					"\"reportDesignFilePath\": \"Multireportprint.rpt\"," + 
					"\"reportFormat\": \"PDF\"," + 
					"\"exportFileName\": \"pathologyReport\"," + 
					"\"reportParams\": [" + 
					"{" + 
					" \"paramName\": \"lab_sample_dtls_id\"," + 
					" \"paramType\": \"String\"," + 
					" \"paramValue\":"+LabDetailsId.toString()+ 
					" }," + 
					" {" + 
					" \"paramName\": \"isOriginal\"," + 
					" \"paramType\": \"Integer\"," + 
					" \"paramValue\": 1" + 
					" }," + 
					" {" + 
					" \"paramName\": \"isHeader\"," + 
					"\"paramType\": \"Integer\"," + 
					"\"paramValue\": 0" + 
					"}" + 
					"]}";
			String API_URL = iUnitApplicationBillingConfigurationDao.getURLForPdfGeneration(orgId, unitId);
			ByteArrayOutputStream byteOutPutStream = new ByteArrayOutputStream();
			HttpPost pst = new HttpPost(API_URL);
			pst.addHeader("Content-Type", "application/json");
			pst.setEntity(new StringEntity(rptString));
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			HttpResponse result = httpClient.execute(pst);
			HttpEntity resEntity = result.getEntity();
			resEntity.writeTo(byteOutPutStream);
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "inline; filename=randomNumber.pdf");
			ServletOutputStream output = response.getOutputStream();
			output.write(byteOutPutStream.toByteArray());
			output.close();
		}catch(Exception e){
			logger.error("Exception", e);
			throw new ServiceException(e.getMessage());
		}
	}


		@RequestMapping(value = "/pathology/report/{LabDetailsId}/{unitId}/{orgId}")
		public void getReportByQRCode(@PathVariable("LabDetailsId")Integer LabDetailsId,@PathVariable("unitId")Integer unitId,@PathVariable("orgId")int orgId,HttpServletRequest request,HttpServletResponse response){
			try{
				

				String rptString = "{\"appName\": \"HIS\"," + 
						" \"appSecretKey\": \"f306b6eccd9d5069d2dc71afe139271a\"," + 
						"\"reportDesignFilePath\": \"Multireportprint.rpt\"," + 
						"\"reportFormat\": \"PDF\"," + 
						"\"exportFileName\": \"pathologyReport\"," + 
						"\"reportParams\": [" + 
						"{" + 
						" \"paramName\": \"lab_sample_dtls_id\"," + 
						" \"paramType\": \"String\"," + 
						" \"paramValue\":"+LabDetailsId.toString()+ 
						" }," + 
						" {" + 
						" \"paramName\": \"isOriginal\"," + 
						" \"paramType\": \"Integer\"," + 
						" \"paramValue\": 1" + 
						" }," + 
						" {" + 
						" \"paramName\": \"isHeader\"," + 
						"\"paramType\": \"Integer\"," + 
						"\"paramValue\": 1" + 
						"}" + 
						"]}";
				String API_URL = iUnitApplicationBillingConfigurationDao.getURLForPdfGeneration(orgId, unitId);
				ByteArrayOutputStream byteOutPutStream = new ByteArrayOutputStream();
				HttpPost pst = new HttpPost(API_URL);
				pst.addHeader("Content-Type", "application/json");
				pst.setEntity(new StringEntity(rptString));
				CloseableHttpClient httpClient = HttpClientBuilder.create().build();
				HttpResponse result = httpClient.execute(pst);
				HttpEntity resEntity = result.getEntity();
				resEntity.writeTo(byteOutPutStream);
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline; filename=randomNumber.pdf");
				ServletOutputStream output = response.getOutputStream();
				output.write(byteOutPutStream.toByteArray());
				output.close();
			}catch(Exception e){
				logger.error("Exception", e);
				throw new ServiceException(e.getMessage());
			}
		}

	@RequestMapping(value = "/ReportReleaseWorkList/sendWhatsAppManually", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response sendWhatsAppManually(@RequestBody LabResultMasterDto labResultMasterDto, HttpServletRequest request,HttpServletResponse ServletResponse){

		Response<LabResultMasterDto, Integer> response = null;
		com.param.global.common.Response res2 = null;
		try{
			String pattern = "yyyy-MM-dd HH:mm:ss";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			String date1=simpleDateFormat.format(TimeZoneComponent.getDateByzone(labResultMasterDto.getOrgUnitId()));

			String resPublicUrl =  unitApplicationBillingConfigurationService.getPublicUrl(labResultMasterDto.getAssignUnit());

			List<String> orderNos = new ArrayList<>();

			if(orderNos.size() ==0 ) {
				orderNos.add(labResultMasterDto.getOrderNo());
			}

			Response  res = iBiochemReportReleaseService.getOrderIdByOrderNumber(orderNos,labResultMasterDto.getAssingnedFrom() != null ?
					labResultMasterDto.getAssingnedFrom() : labResultMasterDto.getAssignUnit());

			if(res.getListObject() != null && res.getListObject().size()>0) {
				List<OrderMasterDto> orderList = new ArrayList<>();
				orderList.addAll(res.getListObject());

				for (OrderMasterDto orderMasterDto : orderList) {
					LabResultMasterDto dto = new LabResultMasterDto();

					if(orderMasterDto.getOrderNo().equals(labResultMasterDto.getOrderNo())) {
						dto = labResultMasterDto;
					}

					Response  OrderRes = iBiochemReportReleaseService.getCompanyIdByOrderId(orderMasterDto.getOrderId());
					OrderMasterDto orderDto= new OrderMasterDto();
					if(OrderRes.getObject() != null) {

						orderDto = (OrderMasterDto) OrderRes.getObject();

					}

					Boolean SendMail = true;

					/*if(orderDto.getVisitTypeId() == OP) {

						Response  checkDueAmount = iBiochemReportReleaseService.checkDueAmount(orderDto);

						if(checkDueAmount.getObject()!= null && checkDueAmount.getObject().equals(SendMail)) {
							SendMail = true;
						}else {
							SendMail = false;
						}
					}*/

					if(SendMail && dto.getLabSampleDtlsId() != null) {
						SearchDto searchDto = new  SearchDto();
						searchDto.setOrderId(orderMasterDto.getOrderId());
						searchDto.setOrgId(dto.getOrgId());

						List<Integer>labSampleDetlsIds = new ArrayList<>();

						String testName = "";
						labSampleDetlsIds.add(dto.getLabSampleDtlsId());

						if(testName == "") {
							testName = dto.getTestDesc();
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

						emailinfo.setPatientId(dto.getPatientId());
						emailinfo.setUnitId(labResultMasterDto.getAssingnedFrom());
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

						}else if( orderDto.getPaymentEntitlementId() != null && orderDto.getPaymentEntitlementId() == ICommonConstants.COMPANY) {

							emailinfo.setCompanyId(orderDto.getPayeeId()!=null ? orderDto.getPayeeId(): null);
							emailinfo.setInsuranceId(null);

						}else {
							emailinfo.setCompanyId(null);
							emailinfo.setInsuranceId(null);
						}
						emailinfo.setSrfId(dto.getSrfId()!=null ? dto.getSrfId():null);
						emailinfo.setRptFileName("Multireportprint.rpt");

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
							str= ServletUtils.getBaseUrl(request)+"/LIS/BioChemistry/QR/"+dto.getLabSampleDtlsId()+"/"+dto.getOrgUnitId()
									+"/"+dto.getOrgId()+"/"+r.toString();
						}else {
							str= resPublicUrl+"/LIS/BioChemistry/QR/"+dto.getLabSampleDtlsId()+"/"+dto.getOrgUnitId()
									+"/"+dto.getOrgId()+"/"+r.toString();
						}
						emailinfo.setLabReportUrl(str);
						emailinfo.setToMobileNumber(dto.getNewWhatsAppNo());
						smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.PATHOLOGY_REPORT, emailinfo);
						return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, null);

					}
					return new Response(ERROR, null, "Due amount pending", null, null);
				}
			}
			return new Response(ERROR, null, "Order Id not found", null, null);
		}catch(Exception e){
			logger.error("Exception", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
