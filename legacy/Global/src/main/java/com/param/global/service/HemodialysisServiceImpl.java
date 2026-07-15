package com.param.global.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.DocUploadUtility;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IHemodialysisDao;
import com.param.global.dao.ITemplateTransactionDao;
import com.param.global.dto.PatientDialysisRecordDto;
import com.param.global.dto.SlotMasterDto;
import com.param.global.opd.dao.IAppointmentSchedulingDao;
import com.param.global.org.dao.IGlobalOrganisationMasterDao;
import com.param.global.org.dto.OrganizationMasterDto;
import com.param.opd.appointment.dto.HemodialysisAppointmentMapperDto;
import com.param.opd.appointment.model.AppointmentMaster;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class HemodialysisServiceImpl implements IHemodialysisService,ICommonConstants{
	final static Logger logger = Logger.getLogger(HemodialysisServiceImpl.class);


		@Autowired
		IAppointmentSchedulingDao iAppointmentSchedulingDao;

		@Autowired
		IHemodialysisDao iHemodialysisDao;

		@Autowired
		private ITemplateTransactionDao iTemplateTransactionDao;
		
		@Autowired
		private IGlobalOrganisationMasterDao iGlobalOrganisationMasterDao;
		
		@Autowired
		private DocUploadUtility docUploadUtility;
		
		@Autowired
		IOrderMasterService iOrderMasterService;
		
		@Override
		public Response scheduleDialysisAppointment(SlotMasterDto slotMasterDto) {
			try{
				boolean res = iAppointmentSchedulingDao.isAppointmentValid(slotMasterDto);
				if(res==false){
					return new Response(ERROR, COMMON_ERROR_CODE, "Slot is already booked !!", null, null);
				}else{
					String appDateStr[]=(slotMasterDto.getAppointmentDate()).split("-");
					HemodialysisAppointmentMapperDto hemodialysisAppointmentMapperDto=new HemodialysisAppointmentMapperDto();
					hemodialysisAppointmentMapperDto.setOrderDetailsId(slotMasterDto.getOrderDetailsId());
					hemodialysisAppointmentMapperDto.setChairId(slotMasterDto.getBedId());//here bed id is chair id
					hemodialysisAppointmentMapperDto.setOrganizationId(slotMasterDto.getOrganizationId());
					hemodialysisAppointmentMapperDto.setUnitId(slotMasterDto.getUnitId());
					hemodialysisAppointmentMapperDto.setUpdatedBy(slotMasterDto.getUpdatedBy());
					hemodialysisAppointmentMapperDto.setUpdatedDate(slotMasterDto.getUpdatedDate());
					hemodialysisAppointmentMapperDto.setAppointmentDate(appDateStr[2]+"-"+appDateStr[1]+"-"+appDateStr[0]);
					//if(slotMasterDto.getSlotId()!=null && slotMasterDto.getAppointmentId()!=null ) {
					if(slotMasterDto.getAppointmentId()!=null ) {
						hemodialysisAppointmentMapperDto.setAppointmentId(slotMasterDto.getAppointmentId());
						Response res1=iHemodialysisDao.assignDialysisChair(slotMasterDto);
						iHemodialysisDao.saveDialysisRequest(hemodialysisAppointmentMapperDto);
						return res1;
					}else {
						slotMasterDto.setAppointmentStatusId(APPOINTMENTSTATUSID5);//make check in
					Response res2 = iAppointmentSchedulingDao.scheduleAppointment(slotMasterDto);
					 AppointmentMaster appMaster=(AppointmentMaster)res2.getObject();
					 hemodialysisAppointmentMapperDto.setAppointmentId(appMaster.getAppointmentId());
					iHemodialysisDao.saveDialysisRequest(hemodialysisAppointmentMapperDto);
					return res2;
					}
				}
			}catch(Exception e){
			logger.error("scheduleDialysisAppointment failed", e);
			}
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

		@Override
		public Response saveDialysisRequest(HemodialysisAppointmentMapperDto hemodialysisAppointmentMapperDto) {
			try{
				return iHemodialysisDao.saveDialysisRequest(hemodialysisAppointmentMapperDto);
			}catch(Exception e){
			logger.error("saveDialysisRequest failed", e);
			}
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

		@Override
		public Response getPatientListForDialysisWorkload(HemodialysisAppointmentMapperDto hemodialysisAppointmentMapperDto) {
			try{
				return iHemodialysisDao.getPatientListForDialysisWorkload(hemodialysisAppointmentMapperDto);
			}catch(Exception e){
			logger.error("getPatientListForDialysisWorkload failed", e);
			}
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

		@Override
		public Response getDialysisMachines(HemodialysisAppointmentMapperDto hemodialysisAppointmentMapperDto) {
			try{
				return iHemodialysisDao.getDialysisMachines(hemodialysisAppointmentMapperDto);
			}catch(Exception e){
			logger.error("getDialysisMachines failed", e);
			}
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

		@Override
		public Response savePatientDialysisRecord(PatientDialysisRecordDto patientDialysisRecordDto) {
			try{
				Integer templateTransactionId=null;
				if(patientDialysisRecordDto.getTemplateObject()!=null) {
				Response templateSaveRes=iTemplateTransactionDao.saveTemplateTransaction(patientDialysisRecordDto.getTemplateObject()); 
				 templateTransactionId=(Integer)templateSaveRes.getObject();
				}
				if(templateTransactionId!=null) {
					patientDialysisRecordDto.setTemplateTransactionId(templateTransactionId);
				}
				
				HemodialysisAppointmentMapperDto dto=new HemodialysisAppointmentMapperDto();
				dto.setDialysisAppMapperId(patientDialysisRecordDto.getDialysisAppMapperId());
				dto.setDialysisStatusId(patientDialysisRecordDto.getDialysisStatusId());
				Response dialysisAppMapperRes=iHemodialysisDao.saveDialysisRequest(dto);
				
				if(patientDialysisRecordDto.getDialysisStatusId()!=null && patientDialysisRecordDto.getDialysisStatusId()==4)
					iOrderMasterService.assignReviewApplicableDate(patientDialysisRecordDto.getOrderDetailsId());
				return iHemodialysisDao.savePatientDialysisRecord(patientDialysisRecordDto);
			}catch(Exception e){
			logger.error("savePatientDialysisRecord failed", e);
			}
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

		@Override
		public Response uploadDialysisDocument(PatientDialysisRecordDto patientDialysisRecordDto) {
			try {
					Response orgRes = iGlobalOrganisationMasterDao
							.getOrganiazationFolderPath(patientDialysisRecordDto.getOrganizationId());
					OrganizationMasterDto organizationMasterDto = (OrganizationMasterDto) orgRes.getObject();
					patientDialysisRecordDto.setOrgFolderPath(organizationMasterDto.getOrganizationFolder());
					String docPath = docUploadUtility.uploadPatientDialysisDocument(patientDialysisRecordDto.getFile(),
								 patientDialysisRecordDto);
						patientDialysisRecordDto.setDocumentPath(docPath);
					return iHemodialysisDao.uploadDialysisDocument(patientDialysisRecordDto);
			} catch (Exception e) {
			logger.error("uploadDialysisDocument failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

		@Override
		public Response getPatientDialysisRecordDetails(
				HemodialysisAppointmentMapperDto hemodialysisAppointmentMapperDto) {
			try{
				return iHemodialysisDao.getPatientDialysisRecordDetails(hemodialysisAppointmentMapperDto);
			}catch(Exception e){
			logger.error("getPatientDialysisRecordDetails failed", e);
			}
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

		@Override
		public Response searchPatientForDialysis(HemodialysisAppointmentMapperDto hemodialysisAppointmentMapperDto) {
			try{
				return iHemodialysisDao.searchPatientForDialysis(hemodialysisAppointmentMapperDto);
			}catch(Exception e){
			logger.error("searchPatientForDialysis failed", e);
			}
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

		@Override
		public Response updateDialysisStatus(PatientDialysisRecordDto patientDialysisRecordDto) {
			try{
				HemodialysisAppointmentMapperDto dto=new HemodialysisAppointmentMapperDto();
				dto.setDialysisAppMapperId(patientDialysisRecordDto.getDialysisAppMapperId());
				dto.setDialysisStatusId(patientDialysisRecordDto.getDialysisStatusId());
				Response dialysisAppMapperRes=iHemodialysisDao.saveDialysisRequest(dto);
				
				if(patientDialysisRecordDto.getDialysisStatusId()!=null && patientDialysisRecordDto.getDialysisStatusId()==4)
					iOrderMasterService.assignReviewApplicableDate(patientDialysisRecordDto.getOrderDetailsId());
				return iHemodialysisDao.updatePatientDialysisRecord(patientDialysisRecordDto);
			}catch(Exception e){
			logger.error("updateDialysisStatus failed", e);
			}
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

		
}
