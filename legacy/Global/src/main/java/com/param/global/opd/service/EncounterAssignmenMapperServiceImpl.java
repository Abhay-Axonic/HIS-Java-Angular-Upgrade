package com.param.global.opd.service;



import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.global.common.DocUploadUtility;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IDoctorIPConsultationServiceMapperDao;
import com.param.global.dao.ITPackageConsumptionMasterDao;
import com.param.global.dto.SlotMasterDto;
import com.param.global.opd.dao.IAppointmentSchedulingDao;
import com.param.global.opd.dao.IEncounterAssignmenMapperDao;
import com.param.global.opd.dao.IEncounterMasterDao;
import com.param.global.org.dao.IGlobalOrganisationMasterDao;
import com.param.global.org.dto.OrganizationMasterDto;
import com.param.global.unit.dao.IDoctorCabinMapperDao;
import com.param.opd.encounter.dto.EncounterAssignmenMapperDto;
import com.param.opd.encounter.dto.EncounterMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class EncounterAssignmenMapperServiceImpl implements IEncounterAssignmenMapperService,ICommonConstants {
	final static Logger logger = Logger.getLogger(EncounterAssignmenMapperServiceImpl.class);


	
	@Autowired
	private IEncounterAssignmenMapperDao iEncounterAssignmenMapperDao;
	
	@Autowired
	@Qualifier("applicationEventMulticaster")
	private ApplicationEventPublisher applicationEventPublisher;
	
	@Autowired
	IAppointmentSchedulingService iAppointmentSchedulingService; 
	
	@Autowired
	IEncounterMasterService iEncounterMasterService;
	
	@Autowired
	IDoctorCabinMapperDao iDoctorCabinMapperDao;
	
	@Autowired
	private ITPackageConsumptionMasterDao iTPackageConsumptionMasterDao;
	
	
	@Autowired
	IDoctorIPConsultationServiceMapperDao iDoctorIPConsultationServiceMapperDao;
	
	@Autowired
	IEncounterMasterDao iEncounterMasterDao;
	
	@Autowired
	IGlobalOrganisationMasterDao iGlobalOrganisationMasterDao;
	
	@Autowired 
	DocUploadUtility docUploadUtility;
	
	@Autowired
	IAppointmentSchedulingDao iAppointmentSchedulingDao;
	
	@Override
	@Transactional
	public Response saveEncounterAssignment(EncounterAssignmenMapperDto encounterAssignmenMapperDto)
			throws ApplicationException {
		try{
			
			Response enDtls = iEncounterAssignmenMapperDao.saveEncounterAssignment(encounterAssignmenMapperDto);
			
			if(encounterAssignmenMapperDto.getIsAssignmentCompStatus()!=null && encounterAssignmenMapperDto.getIsAssignmentCompStatus()=='Y') {
			if(encounterAssignmenMapperDto.getEncounterMasterDto()!=null && encounterAssignmenMapperDto.getEncounterMasterDto().getVisitTypeId()!=null && encounterAssignmenMapperDto.getEncounterMasterDto().getVisitTypeId().intValue()!=1) {
			iTPackageConsumptionMasterDao.expireMEPkgByVisit(encounterAssignmenMapperDto.getAdmissionId(),encounterAssignmenMapperDto.getEncounterMasterDto().getVisitTypeId());
			}else if(encounterAssignmenMapperDto.getEncounterId()!=null && encounterAssignmenMapperDto.getEncounterId()>0) {
				iTPackageConsumptionMasterDao.expireMEPkgByVisit(encounterAssignmenMapperDto.getEncounterId(),1);
			 }

			}
			return new Response<>(SUCCESS,SUCCESS_CODE,"Consultation completed !!",null,null);
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getOldListEncounterAssignment(
			EncounterAssignmenMapperDto encounterAssignmenMapperDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iEncounterAssignmenMapperDao.getOldListEncounterAssignment(encounterAssignmenMapperDto);
	}
	
	
	@Override
	@Transactional
	public Response updateEncounterAssignment(EncounterAssignmenMapperDto encounterAssignmenMapperDto)throws ApplicationException {
		try
		{
			Response res = iEncounterAssignmenMapperDao.updateEncounterAssignment(encounterAssignmenMapperDto);
			if(res.getStatus().equals(SUCCESS) && encounterAssignmenMapperDto.getIsFromCoverSheet()!=null && encounterAssignmenMapperDto.getEncounterId()!=null)
			{
				
				if(encounterAssignmenMapperDto.getAppointmentId()>0)
				{
					SlotMasterDto slotMasterDto=new SlotMasterDto();
						slotMasterDto.setAppointmentId(encounterAssignmenMapperDto.getAppointmentId());
						slotMasterDto.setUpdatedBy(encounterAssignmenMapperDto.getUpdatedBy());
						slotMasterDto.setUpdatedDate(encounterAssignmenMapperDto.getConsultationStopTime());
						slotMasterDto.setAppointmentStatusId(APPOINTMENTSTATUSID6);
					iAppointmentSchedulingService.updateAppointmentStatus(slotMasterDto);
					
					/*
					 * try{ syncAppointmentForMyLife(slotMasterDto); }catch(Exception e){
					 *  }
					 */
				}
				
				EncounterMasterDto encounterMasterDto = new EncounterMasterDto();
					encounterMasterDto.setEncounterId(encounterAssignmenMapperDto.getEncounterId());
					encounterMasterDto.setCurrentDate(encounterAssignmenMapperDto.getConsultationStopTime());
					encounterMasterDto.setStatusId(6);
					encounterMasterDto.setUpdatedBy(encounterAssignmenMapperDto.getUpdatedBy());
				iEncounterMasterService.updateEncounterStatus(encounterMasterDto);
				
				/*iTPackageConsumptionMasterDao.expireMEPkgByVisit(encounterAssignmenMapperDto.getEncounterId(),1);//1 is visit type op
*/				// updating ehc encounter status
				encounterMasterDto.setChildEncounterId(encounterAssignmenMapperDto.getChildEncounterId());
				iEncounterMasterDao.updateEncounterServiceMpperStatus(encounterMasterDto);
				
				//multilingual coversheet call
			/*	Response orgRes = iGlobalOrganisationMasterDao.getOrganiazationFolderPath(encounterAssignmenMapperDto.getOrganizationId());
				OrganizationMasterDto organizationMasterDto = (OrganizationMasterDto) orgRes.getObject();
				
				encounterMasterDto.setOrganizationId(encounterAssignmenMapperDto.getOrganizationId());
				encounterMasterDto.setUnitId(encounterAssignmenMapperDto.getUnitId());
				encounterMasterDto.setPatientId(encounterAssignmenMapperDto.getPatientId());
				encounterMasterDto.setAppointmentId(encounterAssignmenMapperDto.getAppointmentId());
				String coversheetMlPath=null;
			    if(organizationMasterDto.getOrganizationFolder()!=null && encounterAssignmenMapperDto.getAppointmentId()>0) {
					coversheetMlPath= docUploadUtility.uploadMultilingualCoversheet(organizationMasterDto.getOrganizationFolder(),encounterMasterDto);
					if(coversheetMlPath!=null)
					iAppointmentSchedulingDao.updateMultiLingCoverPath(coversheetMlPath,encounterAssignmenMapperDto.getAppointmentId());
				}*/
			}
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch (HibernateException he) {
		throw new ApplicationException(he.getMessage(), he);
	} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		

	}

	@Override
	public Response updatePatientCritical(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try
		{
			Response res = iEncounterMasterService.updatePatientCritical(encounterMasterDto);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch (HibernateException he) {
		logger.error("updatePatientCritical failed", he);
		} catch (Exception e) {
		logger.error("updatePatientCritical failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);

	}

	@Override
	@Transactional
	public Response updateDoctorIdForEncouner(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try
		{
			Response res2 =null;
			/*Response res = iDoctorCabinMapperDao.getCabinByDocId(encounterMasterDto.getDoctorId());
			if(res.getObject()!=null){
			DoctorCabinMapperDto dto = (DoctorCabinMapperDto)res.getObject();
			if(dto.getCabinId()!=null){
				encounterMasterDto.setCabinId(dto.getCabinId());
				res2 = iEncounterMasterService.updateDoctorIdForEncouner(encounterMasterDto);
				res2.setObject(dto.getCabinId());
				}else{
					return new Response<>(ERROR, null, "Cabin is not mapped to doctor..!!", null, null);
				}
			}else{
				return new Response<>(ERROR, null, "Cabin is not mapped to doctor..!!", null, null);
			}*/
			res2 = iEncounterMasterService.updateDoctorIdForEncouner(encounterMasterDto);
			res2.setObject(encounterMasterDto.getCabinId());
			return res2;
		}catch (HibernateException he) {
		throw new ApplicationException(he.getMessage(), he);
	} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getCabinListByDoctorId(Integer doctorId,Integer unitId) throws ApplicationException {
		return iEncounterAssignmenMapperDao.getCabinListByDoctorId(doctorId,unitId);
	}
	
	/*public void syncAppointmentForMyLife(SlotMasterDto slotMasterDto) {
		ScheduleLogsDto logDto = new ScheduleLogsDto(this);
		// insert into log table
		 String dateTime = GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss");
		 logDto.setAction(ScheduleActions.INSERT);
		 logDto.setAddedBy(slotMasterDto.getUpdatedBy());
		 logDto.setAddedDate(dateTime);
		 logDto.setDateTime(dateTime);
		 logDto.setApplication(ScheduleActions.HIS);
		 logDto.setErrorCount(0);
		 logDto.setPriority(ScheduleActions.PRIORITY_NORM);
		 logDto.setRecordId(slotMasterDto.getAppointmentId());
		 logDto.setStatus(ScheduleActions.STATUS_PENDING);
		 logDto.setTableName(ScheduleTables.APPOINTMENT_MASTER);
		 logDto.setOrgId(slotMasterDto.getOrganizationId());
		 applicationEventPublisher.publishEvent(logDto);
	}*/
	
	@Override
	public Response saveEncounterAssignmentIfAssessignmentIsFalse(EncounterMasterDto encounterMasterDto)
			throws ApplicationException {
		try{
			EncounterAssignmenMapperDto encounterAssignmenMapperDto = new EncounterAssignmenMapperDto();
				encounterAssignmenMapperDto.setIsAssignmentCompStatus('N');
				encounterAssignmenMapperDto.setAssignmentStartRoleId(3);
				encounterAssignmenMapperDto.setAssignmentStartTime(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()), "yyyy-mm-dd HH:mm:ss"));
				encounterAssignmenMapperDto.setAssignmentStopTime(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()), "yyyy-mm-dd HH:mm:ss"));
				encounterAssignmenMapperDto.setEncounterId(encounterMasterDto.getEncounterId());
				encounterAssignmenMapperDto.setPatientId(encounterMasterDto.getPatientId());
				encounterAssignmenMapperDto.setUnitId(encounterMasterDto.getUnitId());
				encounterAssignmenMapperDto.setOrganizationId(encounterMasterDto.getOrganizationId());
				encounterAssignmenMapperDto.setIsConsultationStatus('N');
				encounterAssignmenMapperDto.setIsNursingAssessmentSkipped('Y');
			return iEncounterAssignmenMapperDao.saveEncounterAssignment(encounterAssignmenMapperDto);
		}catch(Exception e){
		logger.error("saveEncounterAssignmentIfAssessignmentIsFalse failed", e);
		}
		return new Response<>(ERROR	,ERROR_CODE,COMMON_ERROR_MESSAGE,null,null);
	}

	@Override
	@Transactional
	public Response getCabinListByRMOMappedDoctorList(EncounterMasterDto encounterMasterDto)
			throws ApplicationException {
		return iEncounterAssignmenMapperDao.getCabinListByRMOMappedDoctorList(encounterMasterDto);
	}

	@Override
	@Transactional
	public Response updateEncounterDetailsMultilingual(EncounterAssignmenMapperDto encounterAssignmenMapperDto)
			throws ApplicationException {

		try{
			// TODO Auto-generated method stub
			Response orgRes = iGlobalOrganisationMasterDao.getOrganiazationFolderPath(encounterAssignmenMapperDto.getOrganizationId());
			OrganizationMasterDto organizationMasterDto = (OrganizationMasterDto) orgRes.getObject();
			EncounterMasterDto encounterMasterDto = new EncounterMasterDto();
			encounterMasterDto.setEncounterId(encounterAssignmenMapperDto.getEncounterId());
			encounterMasterDto.setCurrentDate(encounterAssignmenMapperDto.getConsultationStopTime());
			encounterMasterDto.setStatusId(6);
			encounterMasterDto.setUpdatedBy(encounterAssignmenMapperDto.getUpdatedBy());
			encounterMasterDto.setOrganizationId(encounterAssignmenMapperDto.getOrganizationId());
			encounterMasterDto.setUnitId(encounterAssignmenMapperDto.getUnitId());
			encounterMasterDto.setPatientId(encounterAssignmenMapperDto.getPatientId());
			encounterMasterDto.setAppointmentId(encounterAssignmenMapperDto.getAppointmentId());
			String coversheetMlPath=null;
		    if(organizationMasterDto != null && organizationMasterDto.getOrganizationFolder()!=null && encounterAssignmenMapperDto.getAppointmentId()>0) {
				coversheetMlPath= docUploadUtility.uploadMultilingualCoversheet(organizationMasterDto.getOrganizationFolder(),encounterMasterDto);
				if(coversheetMlPath!=null)
				iAppointmentSchedulingDao.updateMultiLingCoverPath(coversheetMlPath,encounterAssignmenMapperDto.getAppointmentId());
			}
	        return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch(Exception e){
			throw e;
		}
		//return new Response<>(ERROR	,ERROR_CODE,COMMON_ERROR_MESSAGE,null,null);
	}

}
