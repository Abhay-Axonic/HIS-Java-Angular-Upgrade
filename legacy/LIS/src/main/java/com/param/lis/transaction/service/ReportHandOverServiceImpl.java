package com.param.lis.transaction.service;



import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import com.param.global.service.IUnitApplicationBillingConfigurationService;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.DocUploadUtility;
import com.param.global.common.ISmsUtilityConstants;
import com.param.global.common.ServletUtils;
import com.param.global.org.dao.IGlobalOrganisationMasterDao;
import com.param.global.org.dto.OrganizationMasterDto;
import com.param.global.unit.dto.SmsUtilityConfigInfoDto;
import com.param.global.unit.service.ISmsUtilityConfigService;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.transaction.dao.IReportHandOverDao;
import com.param.lis.transaction.dto.BioChemParamDto;
import com.param.lis.transaction.dto.LabResultMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ReportHandOverServiceImpl
		implements IReportHandOverService, ICommonConstants, ITransactionConstants, IError {

	@Autowired
	IReportHandOverDao iReportHandOver;
	
	@Autowired
	IGlobalOrganisationMasterDao iGlobalOrganisationMasterDao;
	
	@Autowired
	DocUploadUtility docUploadUtility;
	
	@Autowired
	private ISmsUtilityConfigService smsUtilityService;
	
	@Autowired
	private IUnitApplicationBillingConfigurationService unitApplicationBillingConfigurationService;
	
	@Autowired
	private SessionFactory SessionFactory;

	final static Logger logger = Logger.getLogger(ReportHandOverServiceImpl.class);

	@Override
	@Transactional
	public Response getReportHandList(BioChemParamDto  bioChemParamDto) throws ApplicationException {
		try {
			return iReportHandOver.getReportHandList(bioChemParamDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getReportHandListCount(BioChemParamDto bioChemParamDto) throws ApplicationException {
		try {
			return iReportHandOver.getReportHandListCount(bioChemParamDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response reportHandOverListSearch(SearchDto searchDto) throws ApplicationException {
		try {
			return iReportHandOver.reportHandOverListSearch(searchDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response updateLabsampDetails(List<LabResultMasterDto> list) throws ApplicationException {
		try {
			return iReportHandOver.updateLabsampDetails(list);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response saveReportHandOverImage(LabResultMasterDto dto) throws ApplicationException {
		try {
			
			if (dto.getOrgUnitId() > 0) 
			{
				com.param.global.common.Response orgRes  = iGlobalOrganisationMasterDao.getOrganiazationFolderPath(dto.getOrgId());
				OrganizationMasterDto organizationMasterDto = (OrganizationMasterDto) orgRes.getObject();
				String docPath = "";
				
					docPath = docUploadUtility.uploadLabReportHandOverImage(dto.getFile(),
							organizationMasterDto.getOrganizationFolder(), dto.getOrgId(),dto.getOrgUnitId());
				
					dto.setImagePath(docPath);
					iReportHandOver.saveReportHandOverImage(dto);
			}
			
			
			return new Response<>(SUCCESS, null, null, null, null);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response sendReportOnMail(List<LabResultMasterDto> list, HttpServletRequest request) throws Exception {
		try {
			String resPublicUrl = null;
			if(list != null && list.size() >0) {
			 resPublicUrl =  unitApplicationBillingConfigurationService.getPublicUrl(list.get(0).getOrgUnitId());
			}
			//iMailingService.sentRegistrationMailToPatient(patientRegistrationDto);
			String pattern = "yyyy-MM-dd HH:mm:ss";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			String date1 = simpleDateFormat.format(TimeZoneComponent.getDateByzone(list!=null && list.size()>0 ? list.get(0).getOrgUnitId():null));
			com.param.global.common.Response res1 = null;
			for (LabResultMasterDto dto : list) {
				SmsUtilityConfigInfoDto emailinfo = new SmsUtilityConfigInfoDto();
				emailinfo.setUnitId(dto.getOrgUnitId());
				emailinfo.setOrganizationId(dto.getOrgId());
				emailinfo.setPatientId(dto.getPatientId());
				if(dto.getCreatedBy()!=null) {
					
					Integer employeeId = (Integer) SessionFactory.getCurrentSession().createSQLQuery(
								"select em.employee_id  as \"employeeId\" from public.m_employee_master em where em.user_id =:userId")
								.setParameter("userId", dto.getCreatedBy()).uniqueResult();
					emailinfo.setEmployeeId(employeeId);
				}
				emailinfo.setLabSampleDtlsIdsList(dto.getLabSampleDtlsIdsList());
				if(dto.getDoctorId()!=null) {
					emailinfo.setConsultingDoctorId(dto.getDoctorId());
					emailinfo.setAdmitingDoctorId(dto.getDoctorId());
					emailinfo.setReferringDoctorId(dto.getDoctorId());
					emailinfo.setTreatingDoctorId(dto.getDoctorId());
				}
				emailinfo.setDateTime(date1);
				emailinfo.setTestDesc(dto.getTestDesc());
				emailinfo.setCompanyId(dto.getCompanyId()!=null ? dto.getCompanyId(): null);
				emailinfo.setInsuranceId(dto.getInsuranceId()!=null ? dto.getInsuranceId(): null);
				String urlStringMsg="";
				String urlString="";
				Response res2= iReportHandOver.getReportHandOverUrl(emailinfo.getLabSampleDtlsIdsList(), dto.getOrgUnitId(), dto.getOrgId());
				if(res2.getListObject() != null && res2.getListObject().size() > 0) {
					
						List<SmsUtilityConfigInfoDto> litUrls=(List<SmsUtilityConfigInfoDto>)res2.getListObject();	
						for (SmsUtilityConfigInfoDto listDto : litUrls) {
							Integer index=litUrls.indexOf(listDto);
							if(listDto.getLabReportUrl() != null) {
							String s="<a href='"+listDto.getLabReportUrl()+"'>"+listDto.getLabReportUrl()+" <a></br>";
//							 String newline = System.lineSeparator();
//							urlString +=urlString+" "+listDto.getLabReportUrl()+" "+newline;
							 urlString +=s;
							String sms=listDto.getLabReportUrl();
							urlStringMsg =sms;
							
							emailinfo.setReportMsgUrl(urlStringMsg);
							if(emailinfo.getReportMsgUrl()!= null) {
								smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.PATHOLOGY_REPORT,emailinfo);
							}
							
						}else {
							String criticalUrl= "";
			                if(resPublicUrl == null) {
			                	criticalUrl= ServletUtils.getBaseUrl(request)+"/LIS/BioChemistry/pathology/report/"+dto.getLabSampleDtlsIdsList().get(index)+"/"+dto.getOrgUnitId()
			                    		+"/"+dto.getOrgId();  
			                }else {
			                	criticalUrl= resPublicUrl+"/LIS/BioChemistry/pathology/report/"+dto.getLabSampleDtlsIdsList().get(index)+"/"+dto.getOrgUnitId()
			                    		+"/"+dto.getOrgId(); 
			                }
			                String s="<a href='"+criticalUrl+"'>"+criticalUrl+" <a></br>";
							 urlString +=s;
							 listDto.setLabReportUrl(criticalUrl);
							String sms=listDto.getLabReportUrl();
							urlStringMsg =sms;
							
							emailinfo.setReportMsgUrl(urlStringMsg);
							if(emailinfo.getReportMsgUrl()!= null) {
								smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.PATHOLOGY_REPORT,emailinfo);
							}
						}
					}
					
				
				
				 
				emailinfo.setReportUrl(urlString);
			
//				smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.PATHOLOGY_REPORT,emailinfo);
			
				}else {
					emailinfo.setRptFileName("Multireportprint.rpt");
				}
				if(emailinfo.getReportUrl()!= null) {
					res1 = smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.PATHOLOGY_REPORT, emailinfo);
				}
				
			}
			return new Response<>(SUCCESS, null, (res1!=null && res1.getStatus().equals(SUCCESS) && res1.getMessage()!=null)?res1.getMessage(): null, null, null);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response reportHandOverListTypeSearch(SearchDto searchDto)
			throws ApplicationException {
		try {
			return iReportHandOver.reportHandOverListTypeSearch(searchDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response bulkEmailWorklistPatientSearch(SearchDto searchDto)
			throws ApplicationException {
		try {
			return iReportHandOver.bulkEmailWorklistPatientSearch(searchDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response sendBulkEmailToCompanyOrInsurance(List<SmsUtilityConfigInfoDto> dtoList) throws Exception {
		try {
			com.param.global.common.Response res1 = null;
			Integer categoryId = null;
			String pattern = "yyyy-MM-dd";
			String resMsg = "Mail Sent successfully";
			String resFailedMsg = "";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			String date = simpleDateFormat.format(TimeZoneComponent.getDateByzone(dtoList!=null && dtoList.size()>0 ? dtoList.get(0).getUnitId(): null));
			for (SmsUtilityConfigInfoDto dto : dtoList) {
				SmsUtilityConfigInfoDto emailinfo = new SmsUtilityConfigInfoDto();
				emailinfo.setUnitId(dto.getUnitId());
				emailinfo.setOrganizationId(dto.getOrgId());
				emailinfo.setDateTime(date);
				if(dto.getPaymentEntitlementId()!=null && dto.getPaymentEntitlementId() == INSURANCE) {
					emailinfo.setInsuranceId(dto.getPayeeId());
					emailinfo.setCompanyId(null);
					categoryId = 14;
				}else if(dto.getPaymentEntitlementId()!=null && dto.getPaymentEntitlementId() == COMPANY) {
					emailinfo.setCompanyId(dto.getPayeeId());
					emailinfo.setInsuranceId(null);
					categoryId = 13;
				}
				emailinfo.setPatientList(dto.getPatientList());
				emailinfo.setRptFileName("Multireportprint.rpt");
				
				 res1 = smsUtilityService.getBulkEmailUtilityByTriggerId(ISmsUtilityConstants.BULK_EMAIL_WORKLIST, emailinfo,categoryId);
				 if(res1.getCode().equals("500")) {
					 if(resFailedMsg == "") {
						 resFailedMsg = "Failed To Send Mail To "+resFailedMsg + res1.getMessage();
					 }else {
						 resFailedMsg = resFailedMsg +","+ res1.getMessage();
					 }
				 }
			}
			if(resFailedMsg != "") {
				return new Response(ERROR,"500" , resFailedMsg, null, null);
			}else {
				return new Response(SUCCESS,"200" , resMsg, null, null);
			}
			
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	

}
