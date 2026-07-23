package com.param.global.unit.service;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.transaction.Transactional;

import com.param.global.razorpay.AxiosConfig;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.param.global.common.GenaratePDFFromHtml;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.ISmsUtilityConstants;
import com.param.global.common.Response;
import com.param.global.common.SmsUtility;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.global.dao.IPatientRegistrationDao;
import com.param.global.dao.ITemplateTransactionDao;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.global.dao.IUserMasterDao;
import com.param.global.dto.MailDto;
import com.param.global.dto.MessageQueueDto;
import com.param.global.dto.PatientDetailsDto;
import com.param.global.org.dto.UnitMasterDto;
import com.param.global.unit.dao.ISmsUtilityConfigDao;
import com.param.global.unit.dto.BulkEmailDto;
import com.param.global.unit.dto.SmsUtilityConfigInfoDto;
import com.param.global.unit.dto.SmsUtilityConfigurationDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class SmsUtilityConfigServiceImpl implements ISmsUtilityConfigService, ICommonConstants,ISmsUtilityConstants {
	final static Logger logger = Logger.getLogger(SmsUtilityConfigServiceImpl.class);

	@Autowired
	ISmsUtilityConfigDao iSmsUtilityConfigDao;

	@Autowired
	SmsUtility smsUtility;
	
	@Autowired
	IUserMasterDao iUserMasterDao;
	
	@Autowired
	IPatientRegistrationDao iPatientRegistrationDao;
	
	@Autowired
	IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;
	
	@Autowired 
	GenaratePDFFromHtml genaratePDFFromHtml;
	
	@Autowired
	ITemplateTransactionDao iTemplateTransactionDao;
	
	
	@Autowired
	IGlobalUnitMasterDao iGlobalUnitMasterDao;

	@Autowired
	AxiosConfig axiosConfig;

	
	
	@Override
	public Response saveSmsUtilityConfig(SmsUtilityConfigurationDto smsUtilityConfigurationDto) {
		// TODO Auto-generated method stub
		
		return iSmsUtilityConfigDao.saveSmsUtilityConfig(smsUtilityConfigurationDto);
	}

	
	@Override
	public Response getTriggerMaster() throws ApplicationException {

		return iSmsUtilityConfigDao.getTriggerMaster();
	}

	@Override
	public Response getCategoryMaster() throws ApplicationException {

		return iSmsUtilityConfigDao.getCategoryMaster();
	}

	@Override
	public Response getSmsUtilityConfigDetails(Integer organizationId, Integer unitId) throws ApplicationException {

		return iSmsUtilityConfigDao.getSmsUtilityConfigDetails(organizationId, unitId);
	}


	@Override
	public Response getSmsUtilityByTriggerId(Integer triggerId, SmsUtilityConfigInfoDto smsInfo) {
		 String fileName = "";
		 Response unitRes =  iGlobalUnitMasterDao.getUnitFolderPath(smsInfo.getUnitId());
		  UnitMasterDto unitMasterDto = (UnitMasterDto) unitRes.getObject();
		Response res=iSmsUtilityConfigDao.getSmsUtilityByTriggerId(triggerId, smsInfo);
	    List<SmsUtilityConfigInfoDto> triggerList=res.getListObject();
	    List<SmsUtilityConfigInfoDto> configInfoList=new LinkedList<>();
		String rptString = "";
		for (SmsUtilityConfigInfoDto queryData : triggerList) {
			
			String uniqueId = null;
			Integer skipSaveMessageQueue = 1;
			
			if( queryData.getTriggerId() == 9) 
			{
				uniqueId = smsInfo.getBillId().toString();

				if(queryData.getRptReport()!=null)
				{
					rptString = queryData.getRptReport();
					rptString = rptString.replace("#billId",smsInfo.getBillId() != null ? smsInfo.getBillId().toString() : "0");
					rptString = rptString.replace("#recieptId",smsInfo.getRecieptId() != null ? smsInfo.getRecieptId().toString() :"0");
					rptString = rptString.replace("#depositeConsumptionId",(smsInfo.getDepositeConsumptionId()!=null && smsInfo.getDepositeConsumptionId()>0)?smsInfo.getDepositeConsumptionId().toString(): "0");
					uniqueId = smsInfo.getBillId().toString();
					String  StringFileNameDate = GlobalCommonDateUtils.getStringDate(new Date().getTime(),"_dd_MM_yyyy_HH_mm_ss");
					fileName = smsInfo.getBillId()+StringFileNameDate+".pdf";

				}
			}
			if( queryData.getTriggerId() == 11) {
				uniqueId = smsInfo.getBillId().toString();
				if(queryData.getRptReport()!=null)
				{
					rptString = queryData.getRptReport();
					rptString = rptString.replace("#billId",smsInfo.getBillId() != null ? smsInfo.getBillId().toString() : "0");
					rptString = rptString.replace("#patientId",smsInfo.getPatientId() != null ? smsInfo.getPatientId().toString():"0");
					uniqueId = smsInfo.getBillId().toString();
					String  StringFileNameDate = GlobalCommonDateUtils.getStringDate(new Date().getTime(),"_dd_MM_yyyy_HH_mm_ss");
					fileName = smsInfo.getBillId()+StringFileNameDate+".pdf";
				}
			}
			
			if( queryData.getTriggerId() == 10) {
				uniqueId = smsInfo.getBillId().toString();
				if(queryData.getRptReport()!=null)
				{
					rptString = queryData.getRptReport();
					rptString = rptString.replace("#billId",smsInfo.getBillId() != null ? smsInfo.getBillId().toString() : "0");
					rptString = rptString.replace("#patientId",smsInfo.getPatientId() != null ? smsInfo.getPatientId().toString():"0");
					uniqueId = smsInfo.getBillId().toString();
					String  StringFileNameDate = GlobalCommonDateUtils.getStringDate(new Date().getTime(),"_dd_MM_yyyy_HH_mm_ss");
					fileName = smsInfo.getBillId()+StringFileNameDate+".pdf";
				}
			}
			
			if( queryData.getTriggerId() == 8) {
				uniqueId = smsInfo.getDepositId().toString();
				if(queryData.getRptReport()!=null)
				{
					rptString = queryData.getRptReport();
					rptString = rptString.replace("#depositMasterId",smsInfo.getDepositId() != null ? smsInfo.getDepositId().toString() : "0");
					uniqueId = smsInfo.getDepositId().toString();
					String  StringFileNameDate = GlobalCommonDateUtils.getStringDate(new Date().getTime(),"_dd_MM_yyyy_HH_mm_ss");
					fileName = smsInfo.getBillId()+StringFileNameDate+".pdf";
				}
			}

			if( queryData.getTriggerId() == 2 ) {
				uniqueId = smsInfo.getAppointmentId().toString();
			}

		// Encounter Creation
		if (queryData.getTriggerId() == 27) {
			uniqueId = smsInfo.getEncounterId().toString();
			if(queryData.getRptReport()!=null && (smsInfo.getEncounterTypeId() == 1 || smsInfo.getEncounterTypeId() == 3))
			{
				rptString = queryData.getRptReport();
				rptString = rptString.replace("#encounterId", smsInfo.getEncounterId() != null ? smsInfo.getEncounterId().toString() : "0");
				uniqueId = smsInfo.getEncounterId().toString();
				String StringFileNameDate = GlobalCommonDateUtils.getStringDate(new Date().getTime(),"_dd_MM_yyyy_HH_mm_ss");
				fileName = smsInfo.getEncounterId() + StringFileNameDate + ".pdf";
			}
		}

		// Admission
		if (queryData.getTriggerId() == 5) {
			uniqueId = smsInfo.getAdmissionId().toString();
			if(queryData.getRptReport()!=null)
			{
				rptString = queryData.getRptReport();
				rptString = rptString.replace("#admissionId", smsInfo.getAdmissionId() != null ? smsInfo.getAdmissionId().toString() : "0");
				uniqueId = smsInfo.getAdmissionId().toString();
				String StringFileNameDate = GlobalCommonDateUtils.getStringDate(new Date().getTime(),"_dd_MM_yyyy_HH_mm_ss");
				fileName = smsInfo.getAdmissionId() + StringFileNameDate + ".pdf";
			}
		}

		if (queryData.getTriggerId() == 30 && queryData.getRptReport() != null && smsInfo.getRptFileName() != null) {
			rptString = queryData.getRptReport();

			rptString = rptString.replace("#rptFileName", smsInfo.getRptFileName() != null ? smsInfo.getRptFileName().toString() : "");

			if(smsInfo.getLabSampleDtlsId()!=null) {
				rptString = rptString.replace("#labSampleDetaillsId", smsInfo.getLabSampleDtlsId() != null ? smsInfo.getLabSampleDtlsId().toString() : "0");
				uniqueId = smsInfo.getLabSampleDtlsId().toString();
			}
			if(smsInfo.getLabSampleDtlsIdsList()!=null) {
				rptString = rptString.replace("#labSampleDetaillsId", smsInfo.getLabSampleDtlsIdsList().toString().replaceAll("\\[","").replaceAll("\\]",""));
				uniqueId = smsInfo.getLabSampleDtlsIdsList().toString().replaceAll("\\[","").replaceAll("\\]","");
			}

			String StringFileNameDate = GlobalCommonDateUtils.getStringDate(new Date().getTime(),
					"_dd_MM_yyyy_HH_mm_ss");
			if(smsInfo.getLabSampleDtlsIdsList()!=null) {
				fileName = smsInfo.getPatientId() + StringFileNameDate + ".pdf";
			}else {
				fileName = smsInfo.getLabSampleDtlsId() + StringFileNameDate + ".pdf";
			}

		}

		// BillList OP OR IP
		if (queryData.getTriggerId() == 31 || queryData.getTriggerId() == 32) {
			uniqueId = smsInfo.getBillId().toString();
			if(queryData.getRptReport()!=null)
			{
				rptString = queryData.getRptReport();
				rptString = rptString.replace("#billId",smsInfo.getBillId() != null ? smsInfo.getBillId().toString() : "0");
				uniqueId = smsInfo.getBillId().toString();
				String StringFileNameDate = GlobalCommonDateUtils.getStringDate(new Date().getTime(),
						"_dd_MM_yyyy_HH_mm_ss");
				fileName = smsInfo.getBillId() + StringFileNameDate + ".pdf";
			}
		}
		if( queryData.getTriggerId() == 33) {
			uniqueId = smsInfo.getEncounterId().toString();
			if(queryData.getRptReport()!=null)
			{
				rptString = queryData.getRptReport();
				rptString = rptString.replace("#encounterId", smsInfo.getEncounterId() != null ? smsInfo.getEncounterId().toString() : "0");
				rptString = rptString.replace("#patientId",smsInfo.getPatientId() != null ? smsInfo.getPatientId().toString():"0");
				rptString = rptString.replace("#unitId",smsInfo.getUnitId() != null ? smsInfo.getUnitId().toString() : "0");
				String  StringFileNameDate = GlobalCommonDateUtils.getStringDate(new Date().getTime(),"_dd_MM_yyyy_HH_mm_ss");
				fileName = smsInfo.getBillId()+StringFileNameDate+".pdf";
				uniqueId = smsInfo.getEncounterId().toString();
			}
		}

		if (queryData.getTriggerId() == 34 && queryData.getRptReport() != null) {
			if (smsInfo.getRptOrHtml() == 1) {
				rptString = queryData.getRptReport();
				rptString = rptString.replace("#templateTransactionId", smsInfo.getTemplateTransactionId() != null ? smsInfo.getTemplateTransactionId().toString() : "0");
				String StringFileNameDate = GlobalCommonDateUtils.getStringDate(new Date().getTime(), "_dd_MM_yyyy_HH_mm_ss");
				fileName = smsInfo.getTemplateTransactionId() + StringFileNameDate + "RediologyReport.pdf";
			}
		}

		if( queryData.getTriggerId() == 37) {
			uniqueId = smsInfo.getEncounterId().toString();
			if(queryData.getRptReport()!=null)
			{
				rptString = queryData.getRptReport();
				rptString = rptString.replace("#encounterId", smsInfo.getEncounterId() != null ? smsInfo.getEncounterId().toString() : "0");
				rptString = rptString.replace("#patientId",smsInfo.getPatientId() != null ? smsInfo.getPatientId().toString():"0");
				rptString = rptString.replace("#unitId",smsInfo.getUnitId() != null ? smsInfo.getUnitId().toString() : "0");
				rptString = rptString.replace("#organizationId",smsInfo.getOrganizationId() != null ? smsInfo.getOrganizationId().toString() : "0");
				String  StringFileNameDate = GlobalCommonDateUtils.getStringDate(new Date().getTime(),"_dd_MM_yyyy_HH_mm_ss");
				fileName = smsInfo.getPatientId()+StringFileNameDate+".pdf";
				uniqueId = smsInfo.getEncounterId().toString();
			}
		}
		
		if( queryData.getTriggerId() == 38) {
			uniqueId = smsInfo.getEncounterId().toString();
			if(queryData.getRptReport()!=null)
			{
				rptString = queryData.getRptReport();
				rptString = rptString.replace("#admissionId", smsInfo.getAdmissionId() != null ? smsInfo.getAdmissionId().toString() : "0");
				rptString = rptString.replace("#patientId",smsInfo.getPatientId() != null ? smsInfo.getPatientId().toString():"0");
				rptString = rptString.replace("#unitId",smsInfo.getUnitId() != null ? smsInfo.getUnitId().toString() : "0");
				String  StringFileNameDate = GlobalCommonDateUtils.getStringDate(new Date().getTime(),"_dd_MM_yyyy_HH_mm_ss");
				fileName = smsInfo.getPatientId()+StringFileNameDate+".pdf";
				uniqueId = smsInfo.getEncounterId().toString();
			}
		}
		if( queryData.getTriggerId() == 41) {
			uniqueId = smsInfo.getUniqueId().toString();
		}

		if( queryData.getTriggerId() == 42 && queryData.getRptReport()!=null) {

			if (smsInfo.getTemplateType() != null && smsInfo.getTemplateType() == 1) {

				rptString = queryData.getRptReport();
				rptString = rptString.replace("#consentTransactionId", smsInfo.getConsentTransactionId() != null ? smsInfo.getConsentTransactionId().toString() : "0");
				String StringFileNameDate = GlobalCommonDateUtils.getStringDate(new Date().getTime(), "_dd_MM_yyyy_HH_mm_ss");
				fileName = smsInfo.getPatientId() + StringFileNameDate + ".pdf";

			}
		}
			
		if( queryData.getTriggerId() == 44 ) {
			uniqueId = smsInfo.getUniqueId().toString();
		}
		
		if( queryData.getTriggerId() == 60 ) {
			uniqueId = smsInfo.getCostEstimateId().toString();
			if(queryData.getRptReport()!=null)
			{
				rptString = queryData.getRptReport();
				rptString = rptString.replace("#Id",smsInfo.getCostEstimateId() != null ? smsInfo.getCostEstimateId().toString() : "0");
				String  StringFileNameDate = GlobalCommonDateUtils.getStringDate(new Date().getTime(),"_dd_MM_yyyy_HH_mm_ss");
				fileName = smsInfo.getPatientId()+StringFileNameDate;
			}
		}

		if( queryData.getTriggerId() == 49 && queryData.getReportUrl()!=null) {

			rptString = queryData.getRptReport();
			rptString = rptString.replace("#admissionId", smsInfo.getAdmissionId() != null ? smsInfo.getAdmissionId().toString() : "0");
			rptString = rptString.replace("#patientId",smsInfo.getPatientId() != null ? smsInfo.getPatientId().toString():"0");
			rptString = rptString.replace("#unitId",smsInfo.getUnitId() != null ? smsInfo.getUnitId().toString() : "0");
			rptString = rptString.replace("#link",smsInfo.getReportUrl() != null ? smsInfo.getReportUrl() : "");
			String  StringFileNameDate = GlobalCommonDateUtils.getStringDate(new Date().getTime(),"_dd_MM_yyyy_HH_mm_ss");
			fileName = smsInfo.getPatientId()+StringFileNameDate;

		}
		if( queryData.getTriggerId() == 50 ) {
			skipSaveMessageQueue = 2;
		}
		if( queryData.getTriggerId() == 51 ) {
			skipSaveMessageQueue = 2;
		}
		if( queryData.getTriggerId() == 52 ) {
			skipSaveMessageQueue = 2;
		}
		if( queryData.getTriggerId() == 53 ) {
			skipSaveMessageQueue = 2;
		}

			Integer categoryId = queryData.getCategoryId();
			SmsUtilityConfigInfoDto dto=new SmsUtilityConfigInfoDto();
			dto.setHospitalName(queryData.getHospitalName());
			dto.setPatientName(queryData.getPatientName());
			dto.setTreatingDoctorName(queryData.getTreatingDoctorName());
			dto.setAdmitingDoctorName(queryData.getAdmitingDoctorName());
			dto.setConsultingDoctorName(queryData.getConsultingDoctorName());
			dto.setAdmissionNo(smsInfo.getAdmissionNo()!=null ? smsInfo.getAdmissionNo():null);
			dto.setTreatingDoctorName(queryData.getTreatingDoctorName());
			dto.setReportString(rptString);
			dto.setReportUrl(smsInfo.getReportMsgUrl()!=null ? smsInfo.getReportMsgUrl() :"");
			dto.setReferringDoctorName(queryData.getReferringDoctorName());
			dto.setDepositNo(smsInfo.getDepositNo());
			dto.setSenderId(queryData.getSenderId());
			dto.setPatientUhidNo(queryData.getPatientUhidNo());
			dto.setDateTime(smsInfo.getDateTime());
			dto.setBedDetails(smsInfo.getBedDetails());
			dto.setBillNo(smsInfo.getBillNo()!=null ? smsInfo.getBillNo(): null);
			dto.setRecieptNo(smsInfo.getRecieptNo()!=null ? smsInfo.getRecieptNo() :null);
			dto.setBillAmount(smsInfo.getBillAmount());
			dto.setSelfAmount(smsInfo.getSelfAmount());
			dto.setCreditAmount(smsInfo.getCreditAmount());
			dto.setOutstandingAmount(smsInfo.getOutstandingAmount());
			dto.setTriggerId(triggerId);
			dto.setUniqueId(uniqueId);
			dto.setSkipSaveMessageQueue(skipSaveMessageQueue);
			dto.setOrganizationId(smsInfo.getOrganizationId());
			dto.setUnitId(smsInfo.getUnitId());
			dto.setUniqueId(uniqueId);
			dto.setLabReportUrl(smsInfo.getLabReportUrl()!=null ? smsInfo.getLabReportUrl():null);
			dto.setTestDesc(smsInfo.getTestDesc()!=null ? smsInfo.getTestDesc(): null);
			dto.setSrfId(smsInfo.getSrfId()!=null ? smsInfo.getSrfId():null);
			dto.setCyclicOrderDate(smsInfo.getCyclicOrderDate());
			dto.setVaccineName(smsInfo.getVaccineName()!=null ? smsInfo.getVaccineName():null);
			dto.setSeriesName(smsInfo.getSeriesName()!=null ? smsInfo.getSeriesName():null);
			dto.setSiteName(smsInfo.getSiteName()!=null ? smsInfo.getSiteName() : null);
			dto.setWhatsAppOption(queryData.getWhatsAppOption());
			dto.setTotalDepositAmount(smsInfo.getTotalDepositAmount());
			dto.setTotalPendingDuaAmount(smsInfo.getTotalPendingDuaAmount());
			dto.setAppointmentUniqueId(smsInfo.getAppointmentUniqueId()!= null ? smsInfo.getAppointmentUniqueId() : null);
			dto.setPassNumber(smsInfo.getPassNumber());
			dto.setWhatsAppTemplateMasterId(queryData.getWhatsAppTemplateMasterId() != null ? queryData.getWhatsAppTemplateMasterId() : null);
			dto.setWhatsAppTemplateName(queryData.getWhatsAppTemplateName() != null ? queryData.getWhatsAppTemplateName() : null);
			dto.setReportMsgUrl(smsInfo.getReportMsgUrl() != null ? smsInfo.getReportMsgUrl() : smsInfo.getLabReportUrl());
			dto.setRptFileName(fileName != null ? fileName:"");
			dto.setPatientId(smsInfo.getPatientId() != null ? smsInfo.getPatientId() : 0);


			if( queryData.getTriggerId() == 57 || queryData.getTriggerId() == 58 || queryData.getTriggerId() == 59  ) {
				dto.setProceduceName(smsInfo.getProceduceName() != null ? smsInfo.getProceduceName():null);
			}
			if(smsInfo.getOutstandingAmount()!=null && smsInfo.getBillAmount()!=null)
				dto.setPaidAmount(smsInfo.getBillAmount().subtract(smsInfo.getOutstandingAmount()).setScale(2, RoundingMode.HALF_EVEN));

			dto.setCriticalUrl(smsInfo.getCriticalUrl() != null ? smsInfo.getCriticalUrl() : null);

			switch (categoryId) {
			case ISmsUtilityConstants.PATIENT:
				if(queryData.getPatientMobileNo()!=null) {
					dto.setSmsTemplateMessage(queryData.getTemplateDescription());
					dto.setMobileNo(queryData.getPatientMobileNo());
					dto.setIsSMSSend(queryData.getIsSMSSend());
					if(queryData.getIsWhatsappSend()!=null && queryData.getIsWhatsappSend()=='Y') {
						dto.setIsWhatsappSend(queryData.getIsWhatsappSend());
						dto.setToMobileNumber(smsInfo.getToMobileNumber() != null && !smsInfo.getToMobileNumber().trim().isEmpty() ? smsInfo.getToMobileNumber() : queryData.getPatientWhatsAppNo() != null ? queryData.getPatientWhatsAppNo() :null);
						dto.setUnitFolder(unitMasterDto.getUnitFolder());
						if(queryData.getPatientWhatsAppNo() != null && queryData.getPatientMobileNo() != null ){
							dto.setToMobileNumber(smsInfo.getToMobileNumber() != null && !smsInfo.getToMobileNumber().trim().isEmpty() ? smsInfo.getToMobileNumber() : "91"+queryData.getPatientMobileNo());
						}
					}
					dto.setUserId(smsInfo.getPatientId());
					if(smsInfo.getVideoCallLink() != null) {
					dto.setVideoCallLink(smsInfo.getVideoCallLink());
					}
					dto.setCategoryId(ISmsUtilityConstants.PATIENT);
					dto.setCategoryName("PATIENT");
					configInfoList.add(dto);
				}
				break;

			case ISmsUtilityConstants.CONSULTING_DOCTOR:
				if (queryData.getConsultingDoctorMobileNo() != null) {
					dto.setSmsTemplateMessage(queryData.getTemplateDescription());
					dto.setMobileNo(queryData.getConsultingDoctorMobileNo());
					dto.setIsSMSSend(queryData.getIsSMSSend());
					if(queryData.getIsWhatsappSend()!=null && queryData.getIsWhatsappSend()=='Y') {
						dto.setIsWhatsappSend(queryData.getIsWhatsappSend());
					  dto.setToMobileNumber(queryData.getConsultingDoctorMobileNo() != null ? queryData.getConsultingDoctorMobileNo() : null);
						dto.setUnitFolder(unitMasterDto.getUnitFolder());
					}
					dto.setUserId(smsInfo.getConsultingDoctorId());
					
					if(smsInfo.getVideoCallLink() != null) {
						dto.setVideoCallLink(smsInfo.getVideoCallLink());
						}
					dto.setCategoryId(ISmsUtilityConstants.CONSULTING_DOCTOR);
					dto.setCategoryName("CONSULTING DOCTOR");
					configInfoList.add(dto);
				}
				break;

			case ISmsUtilityConstants.TREATING_DOCTOR:
				if (queryData.getTreatingDoctorMobileNo() != null) {
					dto.setSmsTemplateMessage(queryData.getTemplateDescription());
					dto.setMobileNo(queryData.getTreatingDoctorMobileNo());
					dto.setIsSMSSend(queryData.getIsSMSSend());
					if(queryData.getIsWhatsappSend()!=null && queryData.getIsWhatsappSend()=='Y') {
						dto.setIsWhatsappSend(queryData.getIsWhatsappSend());
					  dto.setToMobileNumber(queryData.getTreatingDoctorMobileNo() != null ? queryData.getTreatingDoctorMobileNo() :null );
						dto.setUnitFolder(unitMasterDto.getUnitFolder());
					}
					
					dto.setUserId(smsInfo.getTreatingDoctorId());
					dto.setCategoryId(ISmsUtilityConstants.TREATING_DOCTOR);
					dto.setCategoryName("TREATING DOCTOR");
					configInfoList.add(dto);
				}
				break;	
				
			case ISmsUtilityConstants.ADMITTING_DOCTOR:
				if (queryData.getAdmitingDoctorMobileNo() != null) {
					dto.setSmsTemplateMessage(queryData.getTemplateDescription());
					dto.setMobileNo(queryData.getAdmitingDoctorMobileNo());
					dto.setIsSMSSend(queryData.getIsSMSSend());
					if(queryData.getIsWhatsappSend()!=null && queryData.getIsWhatsappSend()=='Y') {
						dto.setIsWhatsappSend(queryData.getIsWhatsappSend());
					 dto.setToMobileNumber(queryData.getAdmitingDoctorMobileNo() != null ? queryData.getAdmitingDoctorMobileNo() :null);
						dto.setUnitFolder(unitMasterDto.getUnitFolder());
					}
					dto.setUserId(smsInfo.getAdmitingDoctorId());
					dto.setCategoryId(ISmsUtilityConstants.ADMITTING_DOCTOR);
					dto.setCategoryName("ADMITTING DOCTOR");
					configInfoList.add(dto);
				}
				break;		
				
			case ISmsUtilityConstants.REFERRING_DOCTOR:
				if (queryData.getReferringDoctorMobileNo() != null) {
					dto.setSmsTemplateMessage(queryData.getTemplateDescription());
					dto.setMobileNo(queryData.getReferringDoctorMobileNo());
					dto.setIsSMSSend(queryData.getIsSMSSend());
					if(queryData.getIsWhatsappSend()!=null && queryData.getIsWhatsappSend()=='Y') {
						dto.setIsWhatsappSend(queryData.getIsWhatsappSend());
					 dto.setToMobileNumber(queryData.getReferringDoctorMobileNo() != null ? queryData.getReferringDoctorMobileNo() :null);
						dto.setUnitFolder(unitMasterDto.getUnitFolder());
					}
					dto.setUserId(smsInfo.getReferringDoctorId());
					dto.setCategoryId(ISmsUtilityConstants.REFERRING_DOCTOR);
					dto.setCategoryName("REFERRING DOCTOR");
					configInfoList.add(dto);
				}
				break;
				
				
			  case ISmsUtilityConstants.USER_DEFINED_MAIL:
				  
					 dto.setUserDefinedMail((queryData.getUserDefinedMail() !=null && queryData.getUserDefinedMail() != "") ? queryData.getUserDefinedMail() : null);
				  	 dto.setSmsTemplateMessage(queryData.getTemplateDescription());
					 dto.setCategoryId(ISmsUtilityConstants.USER_DEFINED_MAIL);
					 dto.setCategoryName("USER DEFINED MAIL"); 
					 configInfoList.add(dto);
					 
					 
				break;

			case ISmsUtilityConstants.ADMIN:
				
				List<SmsUtilityConfigInfoDto> list = iUserMasterDao.getAdminDetaisForSmsUtility(smsInfo.getUnitId());
				if(list!=null && list.size()>0)
				{
					ListIterator<SmsUtilityConfigInfoDto> iterator = list.listIterator();
					while(iterator.hasNext())
					{
						SmsUtilityConfigInfoDto smsDto=new SmsUtilityConfigInfoDto();
						smsDto.setHospitalName(queryData.getHospitalName());
						smsDto.setPatientName(queryData.getPatientName());
						smsDto.setTreatingDoctorName(queryData.getTreatingDoctorName());
						smsDto.setAdmitingDoctorName(queryData.getAdmitingDoctorName());
						smsDto.setConsultingDoctorName(queryData.getConsultingDoctorName());
						smsDto.setTreatingDoctorName(queryData.getTreatingDoctorName());
						smsDto.setReferringDoctorName(queryData.getReferringDoctorName());
						smsDto.setDepositNo(smsInfo.getDepositNo());
						smsDto.setSenderId(queryData.getSenderId());
						smsDto.setReportUrl(smsInfo.getReportMsgUrl()!=null ? smsInfo.getReportMsgUrl() :"");
						smsDto.setPatientUhidNo(queryData.getPatientUhidNo());
						smsDto.setDateTime(smsInfo.getDateTime());
						smsDto.setBedDetails(smsInfo.getBedDetails());
						smsDto.setBillAmount(smsInfo.getBillAmount());
						smsDto.setSelfAmount(smsInfo.getSelfAmount());
						smsDto.setCreditAmount(smsInfo.getCreditAmount());
						smsDto.setOutstandingAmount(smsInfo.getOutstandingAmount());
						smsDto.setAdmissionNo(smsInfo.getAdmissionNo()!=null ? smsInfo.getAdmissionNo():null);
						smsDto.setBillNo(smsInfo.getBillNo()!=null ? smsInfo.getBillNo(): null);
						smsDto.setRecieptNo(smsInfo.getRecieptNo()!=null ? smsInfo.getRecieptNo() :null);
						smsDto.setUserId(smsInfo.getEmployeeId());
						smsDto.setCategoryId(ISmsUtilityConstants.ADMIN);
						smsDto.setCategoryName("ADMIN");
						smsDto.setTriggerId(triggerId);
						smsDto.setUniqueId(uniqueId);
						smsDto.setOrganizationId(smsInfo.getOrganizationId());
						smsDto.setUnitId(smsInfo.getUnitId());
						smsDto.setCyclicOrderDate(smsInfo.getCyclicOrderDate());
						smsDto.setLabReportUrl(smsInfo.getLabReportUrl()!=null ? smsInfo.getLabReportUrl():null);
						smsDto.setTestDesc(smsInfo.getTestDesc()!=null ? smsInfo.getTestDesc(): null);
						smsDto.setSrfId(smsInfo.getSrfId()!=null ? smsInfo.getSrfId():null);
						smsDto.setUserDefinedMail((queryData.getUserDefinedMail() !=null && queryData.getUserDefinedMail() != "") ? queryData.getUserDefinedMail() : null);
						smsDto.setVaccineName(smsInfo.getVaccineName()!=null ? smsInfo.getVaccineName():null);
						smsDto.setSeriesName(smsInfo.getSeriesName()!=null ? smsInfo.getSeriesName():null);
						smsDto.setSiteName(smsInfo.getSiteName()!=null ? smsInfo.getSiteName() : null);
						smsDto.setAppointmentUniqueId(smsInfo.getAppointmentUniqueId()!= null ? smsInfo.getAppointmentUniqueId() : null);
						smsDto.setTotalDepositAmount(smsInfo.getTotalDepositAmount());
						smsDto.setTotalPendingDuaAmount(smsInfo.getTotalPendingDuaAmount());
						if(smsInfo.getOutstandingAmount()!=null && smsInfo.getBillAmount()!=null)
							smsDto.setPaidAmount(smsInfo.getBillAmount().subtract(smsInfo.getOutstandingAmount()).setScale(2, RoundingMode.HALF_EVEN));

						smsDto.setCriticalUrl(smsInfo.getCriticalUrl() != null ? smsInfo.getCriticalUrl() : null);
						smsDto.setWhatsAppTemplateMasterId(queryData.getWhatsAppTemplateMasterId() != null ? queryData.getWhatsAppTemplateMasterId() : null);
						smsDto.setWhatsAppTemplateName(queryData.getWhatsAppTemplateName() != null ? queryData.getWhatsAppTemplateName() : null);
						smsDto.setRptFileName(fileName != null ? fileName : "");

						SmsUtilityConfigInfoDto configInfoDto = iterator.next();
						if (configInfoDto.getMobileNo() != null) 
						{
							smsDto.setSmsTemplateMessage(queryData.getTemplateDescription());
							smsDto.setMobileNo(configInfoDto.getMobileNo());
							configInfoList.add(smsDto);
						}
					}
				}
				break;

			}
			if(triggerId!=null && triggerId.equals(ISmsUtilityConstants.SUBSCRIPTION_REMINDER)){
					if(queryData.getIsWhatsappSend()!=null && queryData.getIsWhatsappSend()=='Y') {
						dto.setIsWhatsappSend(queryData.getIsWhatsappSend());
						dto.setToMobileNumber(smsInfo.getMobileNo() != null ? smsInfo.getMobileNo() :null);
					}
				    dto.setSenderId(axiosConfig.getAxiosSenderId());
					dto.setUnitId(smsInfo.getUnitId());
					dto.setOrganizationId(smsInfo.getOrganizationId());
					dto.setSmsTemplateMessage(queryData.getTemplateDescription());
					dto.setMobileNo(smsInfo.getMobileNo());
					dto.setCategoryId(categoryId);
					dto.setCategoryName(queryData.getCategoryName());
					dto.setIsSMSSend(queryData.getIsSMSSend());
					dto.setSubscriptionEndDate(smsInfo.getSubscriptionEndDate());
					configInfoList.add(dto);

			}
	    }
		smsUtility.sendSmsConfig(configInfoList);
		return res;
	}


	@Override
	public Response getRequiredDataList(Integer triggerId) throws ApplicationException {
		return iSmsUtilityConfigDao.getRequiredDataList(triggerId);
	}


	@Override
	@Transactional
	public void sendBatchSMS(List<Integer> unitIds) throws ApplicationException {
		try{
			Response smsTemplateRes= iSmsUtilityConfigDao.getSmsTemplateDetails(unitIds);	
			
			
			  if(smsTemplateRes.getListObject()!=null && smsTemplateRes.getListObject().size()>0){
				  List<SmsUtilityConfigInfoDto> configInfoList=new LinkedList<>();
				  List<SmsUtilityConfigInfoDto> triggerList=smsTemplateRes.getListObject();
				  for (SmsUtilityConfigInfoDto queryData : triggerList) {
						SmsUtilityConfigInfoDto dto=new SmsUtilityConfigInfoDto();
						dto.setHospitalName(queryData.getHospitalName());
						dto.setSenderId(queryData.getSenderId());				  
						Integer categoryId=queryData.getCategoryId();
						  switch (categoryId) {
							  case ISmsUtilityConstants.COLLECTION_SMS:
								  /**
								    Calling getCollectionSMSDetails() for getting the details for SMS and we are the these details to 
								    SmsUtilityConfigInfoDto dto.
								    */
								  Response collectionSMSRes= iSmsUtilityConfigDao.getCollectionSMSDetails(queryData.getOrganizationId(),queryData.getUnitId());
								  if(collectionSMSRes.getListObject()!=null && collectionSMSRes.getListObject().size()>0){
									  /**
									    Calling getListOfMobileNoForScheduleSmsUtility() for getting the Mobile no for sending sms 
									    */
									  SmsUtilityConfigInfoDto smsUtilityInfoDto=(SmsUtilityConfigInfoDto)collectionSMSRes.getListObject().getFirst();
									  dto.setCompanyAdmissionPatientCount(smsUtilityInfoDto.getCompanyAdmissionPatientCount());
									  dto.setCompanyDischargePatientCount(smsUtilityInfoDto.getCompanyDischargePatientCount());
									  dto.setSelfAdmissionPatientCount(smsUtilityInfoDto.getSelfAdmissionPatientCount());
									  dto.setSelfDischargePatientCount(smsUtilityInfoDto.getSelfDischargePatientCount());
									  dto.setDeathCount(smsUtilityInfoDto.getDeathCount());
									  dto.setOpdFollowUpCount(smsUtilityInfoDto.getOpdFollowUpCount());
									  dto.setOpdNewCount(smsUtilityInfoDto.getOpdNewCount());
									  dto.setDepositeAmount(smsUtilityInfoDto.getDepositeAmount());
									  dto.setRefundAmount(smsUtilityInfoDto.getRefundAmount());
									  dto.setCollectionAmount(smsUtilityInfoDto.getCollectionAmount());
									  dto.setUnitId(queryData.getUnitId());
									  dto.setOrganizationId(queryData.getOrganizationId());
									  dto.setTriggerId(queryData.getTriggerId());
									  dto.setCategoryId(categoryId);
									  dto.setCategoryName("COLLECTION SMS");
									  List<SmsUtilityConfigInfoDto> list = iSmsUtilityConfigDao.getListOfMobileNoForScheduleSmsUtility(queryData.getOrganizationId(),queryData.getUnitId(),queryData.getTriggerId());
								  						   
										if(list!=null && list.size()>0)
										{
											ListIterator<SmsUtilityConfigInfoDto> iterator = list.listIterator();
											while(iterator.hasNext())
											{
												//creating new object for sending MSg to multiple mobile no
												SmsUtilityConfigInfoDto smsDto=new SmsUtilityConfigInfoDto();
												smsDto.setCompanyAdmissionPatientCount(smsUtilityInfoDto.getCompanyAdmissionPatientCount());
												smsDto.setCompanyDischargePatientCount(smsUtilityInfoDto.getCompanyDischargePatientCount());
												smsDto.setSelfAdmissionPatientCount(smsUtilityInfoDto.getSelfAdmissionPatientCount());
												smsDto.setSelfDischargePatientCount(smsUtilityInfoDto.getSelfDischargePatientCount());
												smsDto.setDeathCount(smsUtilityInfoDto.getDeathCount());
												smsDto.setOpdFollowUpCount(smsUtilityInfoDto.getOpdFollowUpCount());
												smsDto.setOpdNewCount(smsUtilityInfoDto.getOpdNewCount());
												smsDto.setDepositeAmount(smsUtilityInfoDto.getDepositeAmount());
												smsDto.setRefundAmount(smsUtilityInfoDto.getRefundAmount());
												smsDto.setCollectionAmount(smsUtilityInfoDto.getCollectionAmount());
												smsDto.setHospitalName(queryData.getHospitalName());
												smsDto.setSenderId(queryData.getSenderId());
												smsDto.setUnitId(queryData.getUnitId());
												smsDto.setOrganizationId(queryData.getOrganizationId());
												smsDto.setTriggerId(queryData.getTriggerId());
												smsDto.setCategoryId(categoryId);
												smsDto.setCategoryName("COLLECTION SMS");
												SmsUtilityConfigInfoDto configInfoDto = iterator.next();
												if (configInfoDto.getMobileNo() != null) 
												{
													smsDto.setSmsTemplateMessage(queryData.getTemplateDescription());
													smsDto.setMobileNo(configInfoDto.getMobileNo());
													//System.out.println(smsDto.getMobileNo());
													configInfoList.add(smsDto);
												}
											}
											
										}	
								   }
							  
							  break;
							  case ISmsUtilityConstants.BIRTHDAY:
								  List<PatientDetailsDto> list = iPatientRegistrationDao.getPatientDataForBirthdaySMS(queryData.getUnitId());
									if(list!=null && list.size()>0)
									{
										ListIterator<PatientDetailsDto> iterator = list.listIterator();
										while(iterator.hasNext())
										{
											PatientDetailsDto patientInfo = iterator.next();
											if (patientInfo.getMobile() != null) 
											{
												SmsUtilityConfigInfoDto smsDto=new SmsUtilityConfigInfoDto();
													smsDto.setHospitalName(queryData.getHospitalName());
													smsDto.setTriggerId(queryData.getTriggerId());
													smsDto.setPatientName(patientInfo.getFirstName());
													smsDto.setSenderId(queryData.getSenderId());
													smsDto.setDateTime(patientInfo.getDob());
													smsDto.setAge(patientInfo.getAge());
													smsDto.setSmsTemplateMessage(queryData.getTemplateDescription());
													smsDto.setMobileNo(patientInfo.getMobile());
													smsDto.setUnitId(queryData.getUnitId());
													smsDto.setCategoryId(categoryId);
													smsDto.setOrganizationId(queryData.getOrganizationId());
													smsDto.setCategoryName("BIRTHDAY");
												configInfoList.add(smsDto);
											}
										}
							  }break;		
							  
						  }		  
				  
				  }

				  smsUtility.sendSmsConfig(configInfoList);
			  }
			
		}catch(Exception e){
			throw e;
		}
		
	}

	@Override
	public List<SmsUtilityConfigInfoDto> getMobileList(Integer organizationId, Integer unitId, Integer triggerId)
			throws ApplicationException {
		try{
			return iSmsUtilityConfigDao.getListOfMobileNoForScheduleSmsUtility(organizationId,unitId,triggerId);
		}catch(Exception e){
		logger.error("getMobileList failed", e);
		}
		return null;
	}


	@Override
	public Response updateStatus(SmsUtilityConfigurationDto smsUtilityConfigurationDto) throws ApplicationException {
		try {
				return iSmsUtilityConfigDao.updateStatus(smsUtilityConfigurationDto);
		} catch (Exception e) {
		logger.error("updateStatus failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response getEmailUtilityByTriggerId(Integer triggerId, SmsUtilityConfigInfoDto smsInfo)
			throws ApplicationException {
		
		List<SmsUtilityConfigInfoDto> triggerList=null;
		  List<SmsUtilityConfigInfoDto> configInfoList=new LinkedList<>();
		  ByteArrayOutputStream byteOutPutStream = new ByteArrayOutputStream();
		  String fileName = "";
		 // List<Integer> statusCodeList = new ArrayList<Integer>();
		  
		  Response unitRes =  iGlobalUnitMasterDao.getUnitFolderPath(smsInfo.getUnitId());
		  UnitMasterDto unitMasterDto = (UnitMasterDto) unitRes.getObject();
		   
		Response res=iSmsUtilityConfigDao.getEmailUtilityByTriggerId(triggerId, smsInfo);
		MailDto mailDto = new MailDto();
		if(res.getListObject()!=null && res.getListObject().size()>0) 
		{
			 	triggerList=res.getListObject();
		
				for (SmsUtilityConfigInfoDto queryData : triggerList) 
				{
					String uniqueId = null;
					String rptString = "";
					//String API_URL = queryData.getPdfGenerationUrl();
					if(mailDto!=null)
					{
						mailDto.setId(queryData.getMailId()!=null ? queryData.getMailId() : "info@axonichealth.co.in");
						mailDto.setPassword(queryData.getMailPassword()!=null ? queryData.getMailPassword() : System.getenv("GLOBAL_SMTP_PASSWORD"));
						mailDto.setHost(queryData.getSmtpHost()!=null ? queryData.getSmtpHost() : "smtp.gmail.com");
						mailDto.setPort(queryData.getSmtpPort()!=null ? queryData.getSmtpPort() : "465");
					}
					
					if( queryData.getTriggerId() == 9 && queryData.getRptReport()!=null) 
					{
						rptString = queryData.getRptReport();
						rptString = rptString.replace("#billId",smsInfo.getBillId().toString());
						rptString = rptString.replace("#recieptId",smsInfo.getRecieptId().toString());
						rptString = rptString.replace("#depositeConsumptionId",(smsInfo.getDepositeConsumptionId()!=null && smsInfo.getDepositeConsumptionId()>0)?smsInfo.getDepositeConsumptionId().toString(): "0");
						uniqueId = smsInfo.getBillId().toString();
						String  StringFileNameDate = GlobalCommonDateUtils.getStringDate(new Date().getTime(),"_dd_MM_yyyy_HH_mm_ss");
						fileName = smsInfo.getBillId()+StringFileNameDate+".pdf";
					
					}
					
					
					if( queryData.getTriggerId() == 11 && queryData.getRptReport()!=null) {
						rptString = queryData.getRptReport();
						rptString = rptString.replace("#billId",smsInfo.getBillId().toString());
						rptString = rptString.replace("#patientId",smsInfo.getPatientId().toString());
						uniqueId = smsInfo.getBillId().toString();
						String  StringFileNameDate = GlobalCommonDateUtils.getStringDate(new Date().getTime(),"_dd_MM_yyyy_HH_mm_ss");
						fileName = smsInfo.getBillId()+StringFileNameDate+".pdf";
						
					}
					
					
					
					if( queryData.getTriggerId() == 10 && queryData.getRptReport()!=null) {
						rptString = queryData.getRptReport();
						rptString = rptString.replace("#billId",smsInfo.getBillId().toString());
						rptString = rptString.replace("#patientId",smsInfo.getPatientId().toString());
						uniqueId = smsInfo.getBillId().toString();
						String  StringFileNameDate = GlobalCommonDateUtils.getStringDate(new Date().getTime(),"_dd_MM_yyyy_HH_mm_ss");
						fileName = smsInfo.getBillId()+StringFileNameDate+".pdf";
					
					}
					
					if( queryData.getTriggerId() == 8 && queryData.getRptReport()!=null) {
						rptString = queryData.getRptReport();
						rptString = rptString.replace("#depositMasterId",smsInfo.getDepositId().toString());
						uniqueId = smsInfo.getDepositId().toString();
						String  StringFileNameDate = GlobalCommonDateUtils.getStringDate(new Date().getTime(),"_dd_MM_yyyy_HH_mm_ss");
						fileName = smsInfo.getBillId()+StringFileNameDate+".pdf";
					
					}
					
					
					if( queryData.getTriggerId() == 2 && queryData.getRptReport()!=null) {
						rptString = queryData.getRptReport();
						rptString = rptString.replace("#appointmentId",smsInfo.getAppointmentId().toString());
						uniqueId = smsInfo.getAppointmentId().toString();
						String  StringFileNameDate = GlobalCommonDateUtils.getStringDate(new Date().getTime(),"_dd_MM_yyyy_HH_mm_ss");
						fileName = uniqueId+StringFileNameDate+".pdf";
					
						
					}
					
				// Encounter Creation
				if (queryData.getTriggerId() == 27 && queryData.getRptReport() != null
						&& (smsInfo.getEncounterTypeId() == 1 || smsInfo.getEncounterTypeId() == 3)) {
					rptString = queryData.getRptReport();
					rptString = rptString.replace("#encounterId", smsInfo.getEncounterId().toString());
					uniqueId = smsInfo.getEncounterId().toString();
					String StringFileNameDate = GlobalCommonDateUtils.getStringDate(new Date().getTime(),"_dd_MM_yyyy_HH_mm_ss");
					fileName = smsInfo.getEncounterId() + StringFileNameDate + ".pdf";
				
				}

				// Admission
				if (queryData.getTriggerId() == 5 && queryData.getRptReport() != null) {
					rptString = queryData.getRptReport();
					rptString = rptString.replace("#admissionId", smsInfo.getAdmissionId().toString());
					uniqueId = smsInfo.getAdmissionId().toString();
					String StringFileNameDate = GlobalCommonDateUtils.getStringDate(new Date().getTime(),"_dd_MM_yyyy_HH_mm_ss");
					fileName = smsInfo.getAdmissionId() + StringFileNameDate + ".pdf";
			
				}
				
				// PATHOLOGY_REPORT
				if (queryData.getTriggerId() == 30 && queryData.getRptReport() != null && smsInfo.getRptFileName() != null) {
					rptString = queryData.getRptReport();
					
					rptString = rptString.replace("#rptFileName", smsInfo.getRptFileName().toString());
					
					if(smsInfo.getLabSampleDtlsId()!=null) {
						rptString = rptString.replace("#labSampleDetaillsId", smsInfo.getLabSampleDtlsId().toString());
						uniqueId = smsInfo.getLabSampleDtlsId().toString();
					}
					if(smsInfo.getLabSampleDtlsIdsList()!=null) {
						rptString = rptString.replace("#labSampleDetaillsId", smsInfo.getLabSampleDtlsIdsList().toString().replaceAll("\\[","").replaceAll("\\]",""));
						uniqueId = smsInfo.getLabSampleDtlsIdsList().toString().replaceAll("\\[","").replaceAll("\\]","");
					}
					
					String StringFileNameDate = GlobalCommonDateUtils.getStringDate(new Date().getTime(),
							"_dd_MM_yyyy_HH_mm_ss");
						if(smsInfo.getLabSampleDtlsIdsList()!=null) {
							fileName = smsInfo.getPatientId() + StringFileNameDate + ".pdf";
						}else {
							fileName = smsInfo.getLabSampleDtlsId() + StringFileNameDate + ".pdf";
						}

				

				}
				
				
				// BillList OP OR IP
				if ((queryData.getTriggerId() == 31 || queryData.getTriggerId() == 32) && queryData.getRptReport() != null) {
					rptString = queryData.getRptReport();
					rptString = rptString.replace("#billId", smsInfo.getBillId().toString());
					uniqueId = smsInfo.getBillId().toString();
					String StringFileNameDate = GlobalCommonDateUtils.getStringDate(new Date().getTime(),
							"_dd_MM_yyyy_HH_mm_ss");
					fileName = smsInfo.getBillId() + StringFileNameDate + ".pdf";
				

				}
				
				
				if( queryData.getTriggerId() == 33 && queryData.getRptReport()!=null) {
					rptString = queryData.getRptReport();
					rptString = rptString.replace("#encounterId",smsInfo.getEncounterId().toString());
					rptString = rptString.replace("#patientId",smsInfo.getPatientId().toString());
					rptString = rptString.replace("#unitId",smsInfo.getUnitId().toString());
					String  StringFileNameDate = GlobalCommonDateUtils.getStringDate(new Date().getTime(),"_dd_MM_yyyy_HH_mm_ss");
					fileName = smsInfo.getBillId()+StringFileNameDate+".pdf";
					uniqueId = smsInfo.getEncounterId().toString();
			
				}
				
				if (queryData.getTriggerId() == 34 && queryData.getRptReport() != null) {
					if(smsInfo.getRptOrHtml() == 1){
						rptString = queryData.getRptReport();
						rptString = rptString.replace("#templateTransactionId", smsInfo.getTemplateTransactionId().toString());
						String  StringFileNameDate = GlobalCommonDateUtils.getStringDate(new Date().getTime(),"_dd_MM_yyyy_HH_mm_ss");
						fileName = smsInfo.getTemplateTransactionId() + StringFileNameDate + "RediologyReport.pdf";
					}
					
					
					/*String risTemplate = smsInfo.getRisHeader() +""+smsInfo.getRisBody() +""+smsInfo.getRisFooter();
					Response respass = iTemplateTransactionDao.getPasswordAndUserId(smsInfo.getUnitId(),smsInfo.getOrganizationId());
					if(respass.getObject()!= null) {
						UnitMasterDto unitDto = (UnitMasterDto)respass.getObject();
						ResponseEntity<byte[]>  resTemp = genaratePDFFromHtml.convert(risTemplate,unitDto.getPdfUserId(),unitDto.getPdfPassword());
						
						if(resTemp.getStatusCode().equals(HttpStatus.OK)) {
							byteOutPutStream.write(resTemp.getBody(), 0, resTemp.getBody().length);
							fileName = queryData.getPatientName()+" RediologyReport.pdf";
							try {
								iTemplateTransactionDao.updatePdfByte(resTemp.getBody(),smsInfo.getTemplateTransactionId());
							} catch (org.omg.CORBA.portable.ApplicationException e) {
								
							}
						}
					
					}*/
					
					
					}
				
				if( queryData.getTriggerId() == 37 && queryData.getRptReport()!=null) {
					rptString = queryData.getRptReport();
					rptString = rptString.replace("#encounterId",smsInfo.getEncounterId().toString());
					rptString = rptString.replace("#patientId",smsInfo.getPatientId().toString());
					rptString = rptString.replace("#unitId",smsInfo.getUnitId().toString());
					rptString = rptString.replace("#organizationId",smsInfo.getOrganizationId().toString());
					String  StringFileNameDate = GlobalCommonDateUtils.getStringDate(new Date().getTime(),"_dd_MM_yyyy_HH_mm_ss");
					fileName = smsInfo.getPatientId()+StringFileNameDate+".pdf";
					uniqueId = smsInfo.getEncounterId().toString();
				
				}
				
				if( queryData.getTriggerId() == 38 && queryData.getRptReport()!=null) {
					rptString = queryData.getRptReport();
					rptString = rptString.replace("#admissionId",smsInfo.getAdmissionId().toString());
					rptString = rptString.replace("#patientId",smsInfo.getPatientId().toString());
					rptString = rptString.replace("#unitId",smsInfo.getUnitId().toString());
					String  StringFileNameDate = GlobalCommonDateUtils.getStringDate(new Date().getTime(),"_dd_MM_yyyy_HH_mm_ss");
					fileName = smsInfo.getPatientId()+StringFileNameDate+".pdf";
					uniqueId = smsInfo.getEncounterId().toString();
						
			
				}
				if( queryData.getTriggerId() == 49 && queryData.getReportUrl()!=null) {
					rptString = queryData.getRptReport();
					rptString = rptString.replace("#admissionId",smsInfo.getAdmissionId().toString());
					rptString = rptString.replace("#patientId",smsInfo.getPatientId().toString());
					rptString = rptString.replace("#unitId",smsInfo.getUnitId().toString());
					rptString = rptString.replace("#link",smsInfo.getReportUrl());
					String  StringFileNameDate = GlobalCommonDateUtils.getStringDate(new Date().getTime(),"_dd_MM_yyyy_HH_mm_ss");
					fileName = smsInfo.getPatientId()+StringFileNameDate;
				
						
			
				}
				
				
				if( queryData.getTriggerId() == 42 && queryData.getRptReport()!=null) {
					
					if(smsInfo.getTemplateType() == 1) {
						
						rptString = queryData.getRptReport();
						rptString = rptString.replace("#consentTransactionId",smsInfo.getConsentTransactionId().toString());
						String  StringFileNameDate = GlobalCommonDateUtils.getStringDate(new Date().getTime(),"_dd_MM_yyyy_HH_mm_ss");
						fileName = smsInfo.getPatientId()+StringFileNameDate+".pdf";
						
					}else if(smsInfo.getTemplateType() == 2) {
						
						String templateDescription = queryData.getTemplateDescription() + "<a href='"+smsInfo.getDocumentPath()+ "'> "+smsInfo.getDocumentPath()+"</a>" ;
						queryData.setTemplateDescription(templateDescription);
						
					}else if(smsInfo.getTemplateType() == 3) {
						
						String templateDescription = queryData.getTemplateDescription() + "<a href='"+smsInfo.getHtmlTemplateData()+"&formType=consent'> "+smsInfo.getHtmlTemplateData()+"</a>" ;
						queryData.setTemplateDescription(templateDescription);
					} 
					
					uniqueId = smsInfo.getConsentTransactionId().toString();
						
			
				}
				
				if( queryData.getTriggerId() == 60 && queryData.getRptReport()!=null) {
					rptString = queryData.getRptReport();
					rptString = rptString.replace("#Id",smsInfo.getCostEstimateId().toString());
					String  StringFileNameDate = GlobalCommonDateUtils.getStringDate(new Date().getTime(),"_dd_MM_yyyy_HH_mm_ss");
					fileName = smsInfo.getPatientId()+StringFileNameDate;


				
						
			
				}
					
				if( queryData.getTriggerId() == 44 ) {
					uniqueId = smsInfo.getUniqueId().toString();
				}

				if(triggerId == 6 || triggerId == 33){
					String StringFileNameDate = GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(smsInfo.getUnitId()).getTime(),
							"_dd_MM_yyyy_HH_mm_ss");
					fileName = smsInfo.getPatientId() + StringFileNameDate + ".pdf";
				}
				
					Integer categoryId = queryData.getCategoryId();

					SmsUtilityConfigInfoDto dto=new SmsUtilityConfigInfoDto();
					dto.setHospitalName(queryData.getHospitalName());
					dto.setPatientName(queryData.getPatientName());
					dto.setRecieptNo(smsInfo.getRecieptNo()!= null ? smsInfo.getRecieptNo() : null);
					dto.setCyclicOrderDate(smsInfo.getCyclicOrderDate()!= null ? smsInfo.getCyclicOrderDate() :null );
					dto.setTriggerId(triggerId);
					dto.setAdmissionNo(smsInfo.getAdmissionNo());
					dto.setReportString(rptString);
					dto.setUniqueId(uniqueId);
					dto.setReportUrl(smsInfo.getReportUrl()!=null ? smsInfo.getReportUrl() :"");
					dto.setTreatingDoctorName(queryData.getTreatingDoctorName());
					dto.setBillNo(smsInfo.getBillNo());
					dto.setAdmitingDoctorName(queryData.getAdmitingDoctorName());
					dto.setConsultingDoctorName(queryData.getConsultingDoctorName());
					dto.setTreatingDoctorName(queryData.getTreatingDoctorName());
					dto.setReferringDoctorName(queryData.getReferringDoctorName());
					dto.setInsuranceName(queryData.getInsuranceName());
					dto.setCompanyName(queryData.getCompanyName());
					dto.setDepositNo(smsInfo.getDepositNo());
					dto.setSenderId(queryData.getSenderId());
					dto.setPatientUhidNo(queryData.getPatientUhidNo());
					dto.setDateTime(smsInfo.getDateTime());
					dto.setBedDetails(smsInfo.getBedDetails());
					dto.setBillAmount(smsInfo.getBillAmount());
					dto.setSelfAmount(smsInfo.getSelfAmount());
					dto.setCreditAmount(smsInfo.getCreditAmount());
					dto.setOutstandingAmount(smsInfo.getOutstandingAmount());
					dto.setMailSubject(queryData.getMailSubject()!=null ? queryData.getMailSubject() : "");
					dto.setTestDesc(smsInfo.getTestDesc()!=null ? smsInfo.getTestDesc() : "");
					dto.setUserName(smsInfo.getUserName()!=null ? smsInfo.getUserName(): "");
					dto.setPassword(smsInfo.getPassword()!=null ? smsInfo.getPassword(): "");
					dto.setApplicationLink(smsInfo.getApplicationLink()!=null ? smsInfo.getApplicationLink(): "");
					dto.setEmployeeName(queryData.getEmployeeName()!=null ? queryData.getEmployeeName() : null);
					dto.setEmployeeEmail(queryData.getEmployeeEmail()!=null ? queryData.getEmployeeEmail() : null);
					dto.setUnitId(smsInfo.getUnitId());
					dto.setOrganizationId(smsInfo.getOrganizationId());
					dto.setSrfId(smsInfo.getSrfId()!=null ? smsInfo.getSrfId():null);
					dto.setConsentTransactionId(smsInfo.getConsentTransactionId());
					dto.setConsentMasterName(smsInfo.getConsentMasterName());
					dto.setConsentTypeMasterName(smsInfo.getConsentTypeMasterName());
					dto.setTemplateType(smsInfo.getTemplateType());
					dto.setFormTemplateId(smsInfo.getFormTemplateId());
					dto.setDocumentName(smsInfo.getDocumentName());
					dto.setDocumentPath(smsInfo.getDocumentPath());
					dto.setVisitNumber(smsInfo.getVisitNumber());
					dto.setAge(smsInfo.getAge());
					dto.setGender(smsInfo.getGender());
					dto.setMobileNo(smsInfo.getMobileNo());
					dto.setPassNumber(smsInfo.getPassNumber());
					if(smsInfo.getCriticalReport()!= null && smsInfo.getCriticalReport() == 'Y' && queryData.getCriticalReport()!= null && queryData.getCriticalReport() == 'Y') {
						dto.setCriticalReport('Y');
					}else {
						dto.setCriticalReport('N');
					}
					dto.setLabReportUrl(smsInfo.getLabReportUrl()!= null ? smsInfo.getLabReportUrl(): "");
					dto.setVaccineName(smsInfo.getVaccineName()!=null ? smsInfo.getVaccineName():null);
					dto.setSeriesName(smsInfo.getSeriesName()!=null ? smsInfo.getSeriesName():null);
					dto.setSiteName(smsInfo.getSiteName()!=null ? smsInfo.getSiteName() : null);
					dto.setAppointmentUniqueId(smsInfo.getAppointmentUniqueId()!= null ? smsInfo.getAppointmentUniqueId() : null);
					
					if( queryData.getTriggerId() == 57 || queryData.getTriggerId() == 58 || queryData.getTriggerId() == 59  ) {
						dto.setProceduceName(smsInfo.getProceduceName() != null ? smsInfo.getProceduceName():null);
					}
					
					if(smsInfo.getOutstandingAmount()!=null && smsInfo.getBillAmount()!=null)
						dto.setPaidAmount(smsInfo.getBillAmount().subtract(smsInfo.getOutstandingAmount()).setScale(2, RoundingMode.HALF_EVEN));

					dto.setCriticalUrl(smsInfo.getCriticalUrl() != null ? smsInfo.getCriticalUrl() : null);

					switch (categoryId) {
					case ISmsUtilityConstants.PATIENT:
						if(queryData.getPatientEmail()!=null) {
							dto.setSmsTemplateMessage(queryData.getTemplateDescription());
							dto.setToEmailId(queryData.getPatientEmail());
							if(queryData.getIsWhatsappSend()!=null && queryData.getIsWhatsappSend()=='Y') {
							dto.setIsWhatsappSend(queryData.getIsWhatsappSend());
							dto.setToMobileNumber(queryData.getPatientWhatsAppNo() != null ? queryData.getPatientWhatsAppNo() :null);
							dto.setUnitFolder(unitMasterDto.getUnitFolder());
							}
							
							dto.setUserId(smsInfo.getPatientId());
							if(smsInfo.getVideoCallLink() != null) {
								dto.setVideoCallLink(smsInfo.getVideoCallLink());
							}
							dto.setCategoryId(ISmsUtilityConstants.PATIENT);
							dto.setCategoryName("PATIENT");
							configInfoList.add(dto);
						}
						break;

					case ISmsUtilityConstants.CONSULTING_DOCTOR:
						if (queryData.getConsultingEmail() != null) {
							dto.setSmsTemplateMessage(queryData.getTemplateDescription());
							dto.setToEmailId(queryData.getConsultingEmail());
							if(queryData.getIsWhatsappSend()!=null && queryData.getIsWhatsappSend()=='Y') {
								dto.setIsWhatsappSend(queryData.getIsWhatsappSend());
							  dto.setToMobileNumber(queryData.getConsultingDoctorMobileNo() != null ? queryData.getConsultingDoctorMobileNo() : null);
								dto.setUnitFolder(unitMasterDto.getUnitFolder());
							}
							dto.setUserId(smsInfo.getConsultingDoctorId());
							if(smsInfo.getVideoCallLink() != null) {
								dto.setVideoCallLink(smsInfo.getVideoCallLink());
							}
							dto.setCategoryId(ISmsUtilityConstants.CONSULTING_DOCTOR);
							dto.setCategoryName("CONSULTING DOCTOR");
							configInfoList.add(dto);
						}
						break;

					case ISmsUtilityConstants.TREATING_DOCTOR:
						if (queryData.getTreatingEmail() != null) {
							dto.setSmsTemplateMessage(queryData.getTemplateDescription());
							dto.setToEmailId(queryData.getTreatingEmail());
							if(queryData.getIsWhatsappSend()!=null && queryData.getIsWhatsappSend()=='Y') {
								dto.setIsWhatsappSend(queryData.getIsWhatsappSend());
							  dto.setToMobileNumber(queryData.getTreatingDoctorMobileNo() != null ? queryData.getTreatingDoctorMobileNo() :null );
								dto.setUnitFolder(unitMasterDto.getUnitFolder());
							}
							dto.setUserId(smsInfo.getTreatingDoctorId());
							dto.setCategoryId(ISmsUtilityConstants.TREATING_DOCTOR);
							dto.setCategoryName("TREATING DOCTOR");
							configInfoList.add(dto);
						}
						break;	
						
					case ISmsUtilityConstants.ADMITTING_DOCTOR:
						if (queryData.getAdmitingEmail() != null) {
							dto.setSmsTemplateMessage(queryData.getTemplateDescription());
							dto.setToEmailId(queryData.getAdmitingEmail());
							if(queryData.getIsWhatsappSend()!=null && queryData.getIsWhatsappSend()=='Y') {
								dto.setIsWhatsappSend(queryData.getIsWhatsappSend());
							 dto.setToMobileNumber(queryData.getAdmitingDoctorMobileNo() != null ? queryData.getAdmitingDoctorMobileNo() :null);
								dto.setUnitFolder(unitMasterDto.getUnitFolder());
							}
							dto.setUserId(smsInfo.getAdmitingDoctorId());
							dto.setCategoryId(ISmsUtilityConstants.ADMITTING_DOCTOR);
							dto.setCategoryName("ADMITTING DOCTOR");
							configInfoList.add(dto);
						}
						break;		
						
					case ISmsUtilityConstants.DOCTOR:
						if (queryData.getAdmitingEmail() != null) {
							dto.setSmsTemplateMessage(queryData.getTemplateDescription());
							dto.setToEmailId(queryData.getAdmitingEmail());
							if(queryData.getIsWhatsappSend()!=null && queryData.getIsWhatsappSend()=='Y') {
								dto.setIsWhatsappSend(queryData.getIsWhatsappSend());
							   dto.setToMobileNumber(queryData.getAdmitingDoctorMobileNo() != null ? queryData.getAdmitingDoctorMobileNo() :null);
								dto.setUnitFolder(unitMasterDto.getUnitFolder());
							}
							dto.setUserId(smsInfo.getAdmitingDoctorId());
							dto.setCategoryId(ISmsUtilityConstants.DOCTOR);
							dto.setCategoryName("DOCTOR");
							configInfoList.add(dto);
						}
						break;	
						
					case ISmsUtilityConstants.EMPLOYEE:
						if (queryData.getEmployeeEmail() != null) {
							dto.setSmsTemplateMessage(queryData.getTemplateDescription());
							dto.setToEmailId(queryData.getEmployeeEmail());
							if(queryData.getIsWhatsappSend()!=null && queryData.getIsWhatsappSend()=='Y') {
								dto.setIsWhatsappSend(queryData.getIsWhatsappSend());
							    dto.setToMobileNumber(queryData.getEmployeeMobileNo() != null ? queryData.getEmployeeMobileNo() : null);
								dto.setUnitFolder(unitMasterDto.getUnitFolder());
							}
							dto.setUserId(smsInfo.getEmployeeId());
							dto.setCategoryId(ISmsUtilityConstants.EMPLOYEE);
							dto.setCategoryName("EMPLOYEE");
							configInfoList.add(dto);
						}
						break;	
						
					case ISmsUtilityConstants.REFERRING_DOCTOR:
						if (queryData.getReferringEmail()!= null) {
							dto.setSmsTemplateMessage(queryData.getTemplateDescription());
							dto.setToEmailId(queryData.getReferringEmail());
							dto.setUserId(smsInfo.getReferringDoctorId());
							dto.setCategoryId(ISmsUtilityConstants.REFERRING_DOCTOR);
							dto.setCategoryName("REFERRING DOCTOR");
							configInfoList.add(dto);
							
						}
						break;
						
					case ISmsUtilityConstants.COMPANY:
						if (queryData.getCompanyEmail()!= null) {
							dto.setSmsTemplateMessage(queryData.getTemplateDescription());
							dto.setToEmailId(queryData.getCompanyEmail());
							if(queryData.getIsWhatsappSend()!=null && queryData.getIsWhatsappSend()=='Y') {
								dto.setIsWhatsappSend(queryData.getIsWhatsappSend());
							   dto.setToMobileNumber(queryData.getCompanyMobileNo() != null ? queryData.getCompanyMobileNo() :null);
								dto.setUnitFolder(unitMasterDto.getUnitFolder());
							}
							dto.setUserId(smsInfo.getCompanyId());
							dto.setCategoryId(ISmsUtilityConstants.COMPANY);
							dto.setCategoryName("COMPANY");
							configInfoList.add(dto);
						}
						break;
						
						
					case ISmsUtilityConstants.INSURANCE:
						if (queryData.getInsuranceEmail()!= null) {
							dto.setSmsTemplateMessage(queryData.getTemplateDescription());
							dto.setToEmailId(queryData.getInsuranceEmail());
							if(queryData.getIsWhatsappSend()!=null && queryData.getIsWhatsappSend()=='Y') {
								dto.setIsWhatsappSend(queryData.getIsWhatsappSend());
							    dto.setToMobileNumber(queryData.getInsuranceMobileNo() != null ? queryData.getInsuranceMobileNo() :null);
							    dto.setUnitId(smsInfo.getUnitId());
							}
							dto.setUserId(smsInfo.getInsuranceId());
							dto.setCategoryId(ISmsUtilityConstants.INSURANCE);
							dto.setCategoryName("INSURANCE");
							configInfoList.add(dto);
						}
						break;
						
					 case ISmsUtilityConstants.USER_DEFINED_MAIL:
						 dto.setUserDefinedMail((queryData.getUserDefinedMail() !=null && queryData.getUserDefinedMail() != "") ? queryData.getUserDefinedMail() : null);
						 dto.setSmsTemplateMessage(queryData.getTemplateDescription());
						 dto.setCategoryId(ISmsUtilityConstants.USER_DEFINED_MAIL);
						 dto.setCategoryName("USER DEFINED MAIL"); 
						 configInfoList.add(dto);
						 
						 break;
						
					case ISmsUtilityConstants.ADMIN:
						
						List<SmsUtilityConfigInfoDto> list = iUserMasterDao.getAdminDetaisForSmsUtility(smsInfo.getUnitId());
						if(list!=null && list.size()>0)
						{
							ListIterator<SmsUtilityConfigInfoDto> iterator = list.listIterator();
							while(iterator.hasNext())
							{
								SmsUtilityConfigInfoDto smsDto=new SmsUtilityConfigInfoDto();
								smsDto.setHospitalName(queryData.getHospitalName());
								smsDto.setPatientName(queryData.getPatientName());
								smsDto.setRecieptNo(smsInfo.getRecieptNo()!= null ? smsInfo.getRecieptNo() : null);
								smsDto.setTreatingDoctorName(queryData.getTreatingDoctorName());
								smsDto.setAdmissionNo(smsInfo.getAdmissionNo());
								smsDto.setAdmitingDoctorName(queryData.getAdmitingDoctorName());
								smsDto.setConsultingDoctorName(queryData.getConsultingDoctorName());
								smsDto.setTreatingDoctorName(queryData.getTreatingDoctorName());
								smsDto.setReferringDoctorName(queryData.getReferringDoctorName());
								smsDto.setReportUrl(smsInfo.getReportUrl()!=null ? smsInfo.getReportUrl() :"");
								smsDto.setDepositNo(smsInfo.getDepositNo());
								smsDto.setSenderId(queryData.getSenderId());
								smsDto.setPatientUhidNo(queryData.getPatientUhidNo());
								smsDto.setDateTime(smsInfo.getDateTime());
								smsDto.setBedDetails(smsInfo.getBedDetails());
								smsDto.setBillAmount(smsInfo.getBillAmount());
								smsDto.setBillNo(smsInfo.getBillNo());
								smsDto.setSelfAmount(smsInfo.getSelfAmount());
								smsDto.setCreditAmount(smsInfo.getCreditAmount());
								smsDto.setOutstandingAmount(smsInfo.getOutstandingAmount());
								smsDto.setUserId(smsInfo.getEmployeeId());
								smsDto.setCategoryId(ISmsUtilityConstants.ADMIN);
								smsDto.setCategoryName("ADMIN");
								smsDto.setTriggerId(triggerId);
								smsDto.setReportString(rptString);
								smsDto.setUniqueId(uniqueId);
								smsDto.setMailSubject(queryData.getMailSubject()!=null ? queryData.getMailSubject() : "");
								smsDto.setTestDesc(smsInfo.getTestDesc()!=null ? smsInfo.getTestDesc() : "");
								smsDto.setUserName(smsInfo.getUserName()!=null ? smsInfo.getUserName(): "");
								smsDto.setPassword(smsInfo.getPassword()!=null ? smsInfo.getPassword(): "");
								smsDto.setApplicationLink(smsInfo.getApplicationLink()!=null ? smsInfo.getApplicationLink(): "");
								smsDto.setEmployeeName(queryData.getEmployeeName()!=null ? queryData.getEmployeeName() : null);
								smsDto.setEmployeeEmail(queryData.getEmployeeEmail()!=null ? queryData.getEmployeeEmail() : null);
								smsDto.setCyclicOrderDate(smsInfo.getCyclicOrderDate());
								smsDto.setUnitId(smsInfo.getUnitId());
								smsDto.setOrganizationId(smsInfo.getOrganizationId());
								smsDto.setSrfId(smsInfo.getSrfId()!=null ? smsInfo.getSrfId():null);
								smsDto.setConsentTransactionId(smsInfo.getConsentTransactionId());
								smsDto.setConsentMasterName(smsInfo.getConsentMasterName());
								smsDto.setConsentTypeMasterName(smsInfo.getConsentTypeMasterName());
								smsDto.setTemplateType(smsInfo.getTemplateType());
								smsDto.setFormTemplateId(smsInfo.getFormTemplateId());
								smsDto.setDocumentName(smsInfo.getDocumentName());
								smsDto.setDocumentPath(smsInfo.getDocumentPath());
								smsDto.setVisitNumber(smsInfo.getVisitNumber());
								smsDto.setAge(smsInfo.getAge());
								smsDto.setGender(smsInfo.getGender());
								smsDto.setMobileNo(smsInfo.getMobileNo());
								if(smsInfo.getCriticalReport()!= null && smsInfo.getCriticalReport() == 'Y' && queryData.getCriticalReport()!= null && queryData.getCriticalReport() == 'Y') {
									smsDto.setCriticalReport('Y');
								}else {
									smsDto.setCriticalReport('N');
								}
								smsDto.setLabReportUrl(smsInfo.getLabReportUrl()!= null ? smsInfo.getLabReportUrl(): "");
								smsDto.setUserDefinedMail((queryData.getUserDefinedMail() !=null && queryData.getUserDefinedMail() != "") ? queryData.getUserDefinedMail() : null);
								smsDto.setVaccineName(smsInfo.getVaccineName()!=null ? smsInfo.getVaccineName():null);
								smsDto.setSeriesName(smsInfo.getSeriesName()!=null ? smsInfo.getSeriesName():null);
								smsDto.setSiteName(smsInfo.getSiteName()!=null ? smsInfo.getSiteName() : null);
								smsDto.setAppointmentUniqueId(smsInfo.getAppointmentUniqueId()!= null ? smsInfo.getAppointmentUniqueId() : null);
								if(smsInfo.getOutstandingAmount()!=null && smsInfo.getBillAmount()!=null)
									smsDto.setPaidAmount(smsInfo.getBillAmount().subtract(smsInfo.getOutstandingAmount()).setScale(2, RoundingMode.HALF_EVEN));


								smsDto.setCriticalUrl(smsInfo.getCriticalUrl() != null ? smsInfo.getCriticalUrl() : null);

								SmsUtilityConfigInfoDto configInfoDto = iterator.next();
								if (configInfoDto.getToEmailId() != null) 
								{
									smsDto.setSmsTemplateMessage(queryData.getTemplateDescription());
									smsDto.setToEmailId(configInfoDto.getToEmailId());
									configInfoList.add(smsDto);
								}
							}
							
						}
						break;
					}
					if(triggerId!=null && triggerId.equals(ISmsUtilityConstants.SUBSCRIPTION_REMINDER)){

						mailDto.setId(axiosConfig.getAxiosMailId());
						mailDto.setPassword(axiosConfig.getAxiosMailPassword());
						mailDto.setHost(axiosConfig.getAxiosSmtpHost());
						mailDto.setPort(axiosConfig.getAxiosSmtpPort());

						dto.setSmsTemplateMessage(queryData.getTemplateDescription());
						dto.setToEmailId(smsInfo.getToEmailId());
						dto.setUnitId(smsInfo.getUnitId());
						dto.setOrganizationId(smsInfo.getOrganizationId());
						dto.setIsSMSSend(queryData.getIsSMSSend());
						dto.setCategoryId(categoryId);
						dto.setCategoryName(queryData.getCategoryName());
						dto.setSubscriptionEndDate(smsInfo.getSubscriptionEndDate());
						dto.setMailSubject(queryData.getMailSubject());
						configInfoList.add(dto);
					}
					
			    }
		
		}
	
		if(configInfoList !=null && configInfoList.size()>0) {
			smsUtility.sendEmalConfig(configInfoList,fileName,byteOutPutStream,mailDto); //optional for other reports
		}else {
			res.setMessage("Failed To Send Mail");
		}
		// Strip the raw query result (may carry smtp_user_pwd/sender_id and other config-level fields)
		// before returning -- this Response is forwarded verbatim into several callers' own HTTP responses.
		res.setListObject(null);
		res.setObject(null);
		return res;
	}


	@Override
	public Response getBulkEmailUtilityByTriggerId(Integer triggerId, SmsUtilityConfigInfoDto smsInfo,Integer categoryId)
			throws ApplicationException, FileNotFoundException, IOException {
		
		List<SmsUtilityConfigInfoDto> triggerList=null;
		  List<SmsUtilityConfigInfoDto> configInfoList=new LinkedList<>();
		  //List<BulkEmailDto> statusCodeList = new ArrayList<>();
		Response res=iSmsUtilityConfigDao.getEmailBulkUtilityByTriggerId(triggerId, smsInfo,categoryId);
		
		MailDto mailDto = new MailDto();
		if(res.getListObject()!=null && res.getListObject().size()>0) 
		{
			 	triggerList=res.getListObject();
		
				for (SmsUtilityConfigInfoDto queryData : triggerList) 
				{
					
					String rptString = "";
					//String API_URL = queryData.getPdfGenerationUrl();
					if(mailDto!=null)
					{
						mailDto.setId(queryData.getMailId()!=null ? queryData.getMailId() : "info@axonichealth.co.in");
						mailDto.setPassword(queryData.getMailPassword()!=null ? queryData.getMailPassword() : System.getenv("GLOBAL_SMTP_PASSWORD"));
						mailDto.setHost(queryData.getSmtpHost()!=null ? queryData.getSmtpHost() : "smtp.gmail.com");
						mailDto.setPort(queryData.getSmtpPort()!=null ? queryData.getSmtpPort() : "465");
					}
				
				// PATHOLOGY_REPORT
				if (queryData.getTriggerId() == 40 && queryData.getRptReport() != null) {
					
					if(smsInfo.getPatientList()!=null && smsInfo.getPatientList().size()>0) {
						
						
						for (BulkEmailDto bulkEmailDto : smsInfo.getPatientList()) {
							  String fileName = "";
							  
							rptString = queryData.getRptReport();
							rptString = rptString.replace("#rptFileName", smsInfo.getRptFileName().toString());
							String StringFileNameDate = GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(smsInfo.getUnitId()).getTime(),
									"_dd_MM_yyyy_HH_mm_ss");
								fileName = bulkEmailDto.getUhid() + StringFileNameDate + ".pdf";
								bulkEmailDto.setAttachmentFileName(fileName);
								rptString = rptString.replace("#labSampleDetaillsId", bulkEmailDto.getLabSampleDtlsList().toString().replaceAll("\\[","").replaceAll("\\]",""));
								bulkEmailDto.setUniqueId(bulkEmailDto.getLabSampleDtlsList().toString().replaceAll("\\[","").replaceAll("\\]",""));
								bulkEmailDto.setReportString(rptString);
						
						
						}
					}
				}
					
					SmsUtilityConfigInfoDto dto=new SmsUtilityConfigInfoDto();
					dto.setHospitalName(queryData.getHospitalName());
					dto.setPatientName(queryData.getPatientName());
					dto.setTriggerId(triggerId);
					dto.setReportString(rptString);
					//dto.setUniqueId(uniqueId);
					dto.setInsuranceName(queryData.getInsuranceName());
					dto.setCompanyName(queryData.getCompanyName());
					dto.setPatientUhidNo(queryData.getPatientUhidNo());
					dto.setDateTime(smsInfo.getDateTime());
					dto.setMailSubject(queryData.getMailSubject()!=null ? queryData.getMailSubject() : "");
					dto.setTestDesc(smsInfo.getTestDesc()!=null ? smsInfo.getTestDesc() : "");
					dto.setOrganizationId(smsInfo.getOrganizationId());
					dto.setUnitId(smsInfo.getUnitId());
					switch (categoryId) {
					case ISmsUtilityConstants.COMPANY:
						if (queryData.getCompanyEmail()!= null) {
							dto.setSmsTemplateMessage(queryData.getTemplateDescription());
							dto.setToEmailId(queryData.getCompanyEmail());
							dto.setUserId(smsInfo.getCompanyId());
							dto.setCategoryId(ISmsUtilityConstants.COMPANY);
							dto.setCategoryName("COMPANY");
							configInfoList.add(dto);
						}
						break;
						
						
					case ISmsUtilityConstants.INSURANCE:
						if (queryData.getInsuranceEmail()!= null) {
							dto.setSmsTemplateMessage(queryData.getTemplateDescription());
							dto.setToEmailId(queryData.getInsuranceEmail());
							dto.setUserId(smsInfo.getInsuranceId());
							dto.setCategoryId(ISmsUtilityConstants.INSURANCE);
							dto.setCategoryName("INSURANCE");
							configInfoList.add(dto);
						}
						break;
					}
			    }
		}
		
		
		
		
		smsUtility.sendBulkEmailConfig(configInfoList,smsInfo,mailDto);

		// Strip the raw query result (may carry smtp_user_pwd/sender_id and other config-level fields)
		// before returning -- this Response is forwarded verbatim into several callers' own HTTP responses.
		res.setListObject(null);
		res.setObject(null);
		return res;
	}


		@Override
	public void triggerScheduledMails(String execution,List<Integer> unitIds) {
		
		String fileName = "";
		String message="";
		for (Integer unitId : unitIds) {
			Response res=iSmsUtilityConfigDao.getMailUtilityByUnitId(execution,unitId);
			 List<SmsUtilityConfigInfoDto> triggerList=null;
			 triggerList=res.getListObject();
			 
			 if(triggerList != null && triggerList.size()>0) {
				 for (SmsUtilityConfigInfoDto triggerType : triggerList) {
					 String rptString = "";
						    String pattern = "yyyy-MM-dd";
							SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
							String date = simpleDateFormat.format(TimeZoneComponent.getDateByzone(unitId));
							Date getDate = TimeZoneComponent.getDateByzone(unitId);
							getDate = DateUtils.addDays(getDate, -1);
							String date1 = simpleDateFormat.format(getDate);
							fileName="";
							if(ISmsUtilityConstants.AUTO_CORPORATE_MAIL==triggerType.getTriggerId())
							{
									if( triggerType.getRptReport()!=null) {
										rptString = triggerType.getRptReport();
										rptString = rptString.replace("@date",date1.toString());
										rptString = rptString.replace("@unitId",unitId.toString());
										rptString = rptString.replace("@type",execution.toString());
										String  StringFileNameDate = GlobalCommonDateUtils.getStringDate(new Date().getTime(),"_dd_MM_yyyy_HH_mm_ss");
										fileName = unitId+StringFileNameDate+".pdf";
								 }
								 message=triggerType.getTemplateDescription();
								 if(triggerType.getUserDefinedMail()!= null)
								 {
								 List<String> elephantList = Arrays.asList(triggerType.getUserDefinedMail().split(","));
								 for (String UserMail : elephantList) {
									 MessageQueueDto dto = new MessageQueueDto();
									 dto.setCategoryId(triggerType.getCategoryId());
									 dto.setCategoryName(triggerType.getCategoryName());
									 dto.setMailContent(message);
									 dto.setMailFileName(fileName);
									 dto.setMailSubject(triggerType.getMailSubject());
									 dto.setOrganizationId(triggerType.getOrganizationId());
									 dto.setReportString(rptString);
									 dto.setToMailId(UserMail);
									 dto.setTriggerId(triggerType.getTriggerId());
									 dto.setUniqueId(unitId.toString());
									 dto.setUnitId(unitId);
									 smsUtility.getSmsUtilityByTriggerIdForMinStockQty(dto);
								}
							  }
							}
							else
							{
						 if( triggerType.getRptReport()!=null) {
								rptString = triggerType.getRptReport();
								rptString = rptString.replace("@date",date);
								rptString = rptString.replace("@unitId",unitId.toString());
								String  StringFileNameDate = GlobalCommonDateUtils.getStringDate(new Date().getTime(),"_dd_MM_yyyy_HH_mm_ss");
								fileName = unitId+StringFileNameDate+".pdf";
								
						 }
						 message=triggerType.getTemplateDescription();
						 if(triggerType.getUserDefinedMail()!= null)
						 {
						 List<String> elephantList = Arrays.asList(triggerType.getUserDefinedMail().split(","));
						 for (String UserMail : elephantList) {
							 MessageQueueDto dto = new MessageQueueDto();
							 dto.setCategoryId(triggerType.getCategoryId());
							 dto.setCategoryName(triggerType.getCategoryName());
							 dto.setMailContent(message);
							 dto.setMailFileName(fileName);
							 dto.setMailSubject(triggerType.getMailSubject());
							 dto.setOrganizationId(triggerType.getOrganizationId());
							 dto.setReportString(rptString);
							 dto.setToMailId(UserMail);
							 dto.setTriggerId(triggerType.getTriggerId());
							 dto.setUniqueId(unitId.toString());
							 dto.setUnitId(unitId);
							 smsUtility.getSmsUtilityByTriggerIdForMinStockQty(dto);
						}
							}
							}
					 
				} 
			 }
			

		}
				
	}

	@Override
	@Transactional
	public Response getWhatsAppTemplateMaster(Integer organizationId, Integer unitId) throws ApplicationException {
		return iSmsUtilityConfigDao.getWhatsAppTemplateMaster(organizationId, unitId);
	}

}
