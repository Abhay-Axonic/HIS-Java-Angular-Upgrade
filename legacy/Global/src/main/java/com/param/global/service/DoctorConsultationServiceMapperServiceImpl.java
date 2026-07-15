package com.param.global.service;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ScheduleActions;
import com.param.global.common.ScheduleTables;
import com.param.global.common.ServiceException;
import com.param.global.dao.IDoctorConsultationServiceMapperDao;
import com.param.global.dao.IScheduleLogsDao;
import com.param.global.dto.DoctorConsChargesForMyLife;
import com.param.global.dto.DoctorConsultationServiceMapperDto;
import com.param.global.dto.ScheduleLogsDto;
import com.param.global.model.DoctorConsultationServiceMapper;
import com.param.opd.encounter.dto.EncounterMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DoctorConsultationServiceMapperServiceImpl
		implements IDoctorConsultationServiceMapperService, ICommonConstants {

	private static final Logger logger = Logger.getLogger(DoctorConsultationServiceMapperServiceImpl.class);

	@Autowired
	IDoctorConsultationServiceMapperDao iDoctorConsultationServiceMapperDao;

	@Autowired
	@Qualifier("applicationEventMulticaster")
	private ApplicationEventPublisher applicationEventPublisher; 
	
	@Autowired
	private IScheduleLogsDao isheduleDao;
	
	@Override
	public Response saveDoctorConsultationServiceMapperService(
			DoctorConsultationServiceMapperDto doctorConsultationServiceMapperDto) throws ApplicationException {
		try {
			
			ListIterator<DoctorConsultationServiceMapperDto> dtoListwc = doctorConsultationServiceMapperDto.getDocConsultationMapperList().listIterator();
			while(dtoListwc.hasNext()) {
				DoctorConsultationServiceMapperDto dto = dtoListwc.next();
				dto.setUnitId(doctorConsultationServiceMapperDto.getUnitId());
				dto.setOrganizationId(doctorConsultationServiceMapperDto.getOrganizationId());
				dto.setStatus(doctorConsultationServiceMapperDto.getStatus());
				dto.setCreatedBy(doctorConsultationServiceMapperDto.getCreatedBy());
				dto.setUpdatedBy(doctorConsultationServiceMapperDto.getUpdatedBy());
				Integer id = iDoctorConsultationServiceMapperDao.checkDoctorConsultationExistOrNot(dto);
                   if (id != null) {
					
					// if consultation for doctor is already exist then
					iDoctorConsultationServiceMapperDao.inactivePreviousEntry(id);
                   }
			   }
			
			
			ListIterator<DoctorConsultationServiceMapperDto> dtoList=doctorConsultationServiceMapperDto.getDocConsultationMapperList().listIterator();
			while(dtoList.hasNext()) {
				
				DoctorConsultationServiceMapperDto dto = dtoList.next();
				dto.setUnitId(doctorConsultationServiceMapperDto.getUnitId());
				dto.setOrganizationId(doctorConsultationServiceMapperDto.getOrganizationId());
				dto.setStatus(doctorConsultationServiceMapperDto.getStatus());
				dto.setCreatedBy(doctorConsultationServiceMapperDto.getCreatedBy());
				dto.setUpdatedBy(doctorConsultationServiceMapperDto.getUpdatedBy());
//				Integer id = iDoctorConsultationServiceMapperDao.checkDoctorConsultationExistOrNot(dto);
			/*	if (id != null) {
					
					// if consultation for doctor is already exist then
//					iDoctorConsultationServiceMapperDao.inactivePreviousEntry(id);
				
					// adding new entry after in-activating previous
					Response res=iDoctorConsultationServiceMapperDao.saveDoctorConsultationServiceMapperService(dto);
					if(dto.getDoctorId()!=null && dto.getPaymentEntitlementId()==1 && res.getCode().equals(SUCCESS_CODE) && res.getObject() != null)
					{					    
						try{					    		
							DoctorConsultationServiceMapper doctorConsultationServiceMapper = (DoctorConsultationServiceMapper)res.getObject();		
							syncDoctorChargesToMylife(doctorConsultationServiceMapper);
							}catch(Exception e){					    		
													    
								}				    	
						}
				} else {*/

					Response res= iDoctorConsultationServiceMapperDao.saveDoctorConsultationServiceMapperService(dto);
					
					if(dto.getDoctorId()!=null && dto.getPaymentEntitlementId()==SELF && res.getCode().equals(SUCCESS_CODE) && res.getObject() != null){
						try{
							DoctorConsultationServiceMapper doctorConsultationServiceMapper = (DoctorConsultationServiceMapper)res.getObject();
							syncDoctorChargesToMylife(doctorConsultationServiceMapper);
							}catch(Exception e){
								logger.error(e.getMessage(), e);
								}
						}
//				}

			}
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

		} catch (Exception e) {
			logger.error("saveDoctorConsultationServiceMapperService failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	private void syncDoctorChargesToMylife(DoctorConsultationServiceMapper doctorConsultationServiceMapper) {
		ScheduleLogsDto logDto = new ScheduleLogsDto(this);					
		// insert into log table					    		
		String dateTime = GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(doctorConsultationServiceMapper.getUnitId()), "dd-M-yyyy HH:mm:ss");
		logDto.setAction(ScheduleActions.INSERT);					    		
		logDto.setAddedBy(doctorConsultationServiceMapper.getCreatedBy());			
		logDto.setAddedDate(dateTime);					    		
		logDto.setDateTime(dateTime);					    		
		logDto.setApplication(ScheduleActions.HIS);					
		logDto.setErrorCount(0);					    		 
		logDto.setPriority(ScheduleActions.PRIORITY_NORM);			
		logDto.setRecordId(doctorConsultationServiceMapper.getDoctorId());					   
		logDto.setStatus(ScheduleActions.STATUS_PENDING);				
		logDto.setTableName(ScheduleTables.DOCTOR_CHARGES);		
		logDto.setOrgId(doctorConsultationServiceMapper.getOrganizationId());
		//applicationEventPublisher.publishEvent(logDto);	
		isheduleDao.saveScheduleLogs(logDto);
	}

	@Override
	public Response getDoctorConsultationServiceMapperList(
			DoctorConsultationServiceMapperDto doctorConsultationServiceMapperDto) throws ApplicationException {
		try {
			return iDoctorConsultationServiceMapperDao
					.getDoctorConsultationServiceMapperList(doctorConsultationServiceMapperDto);
		} catch (Exception e) {
		logger.error("getDoctorConsultationServiceMapperList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateDoctorConsultationServiceMapper(
			DoctorConsultationServiceMapperDto doctorConsultationServiceMapperDto) throws ApplicationException {
		try {
			Response res=iDoctorConsultationServiceMapperDao
					.updateDoctorConsultationServiceMapper(doctorConsultationServiceMapperDto);
			
			if(res.getObject() != null){					    
				try{					    		
					DoctorConsultationServiceMapper doctorConsultationServiceMapper = (DoctorConsultationServiceMapper)res.getObject();		
					syncDoctorChargesToMylife(doctorConsultationServiceMapper);
					}catch(Exception e){
					logger.error("updateDoctorConsultationServiceMapper failed", e);
					}
				}
			return res;

		} catch (Exception e) {
		logger.error("updateDoctorConsultationServiceMapper failed", e);
		}
		return new Response(ERROR, null, null, null, null);

	}

	@Override
	public Response getDoctorConsultationServiceMapperById(
			DoctorConsultationServiceMapperDto doctorConsultationServiceMapperDto) {
		try {
			return iDoctorConsultationServiceMapperDao
					.getDoctorConsultationServiceMapperById(doctorConsultationServiceMapperDto);
		} catch (Exception e) {
		logger.error("getDoctorConsultationServiceMapperById failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getDoctorConsultationServiceBySpecialityAndDoctorId(
			DoctorConsultationServiceMapperDto doctorConsultationServiceMapperDto) {
		try {
			return iDoctorConsultationServiceMapperDao.getDoctorConsultationServiceBySpecialityAndDoctorId(doctorConsultationServiceMapperDto);
		} catch (Exception e) {
		logger.error("getDoctorConsultationServiceBySpecialityAndDoctorId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@Override
	public Response getDoctorConsultationServiceBySpecialityAndDoctorIdTransactional(
			DoctorConsultationServiceMapperDto doctorConsultationServiceMapperDto) {
		try {
			return iDoctorConsultationServiceMapperDao.getDoctorConsultationServiceBySpecialityAndDoctorId(doctorConsultationServiceMapperDto);
		} catch (Exception e) {
		logger.error("getDoctorConsultationServiceBySpecialityAndDoctorIdTransactional failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateDoctorConsultationServiceMapperStatus(
			DoctorConsultationServiceMapperDto doctorConsultationServiceMapperDto) throws ApplicationException {
		try {
			return iDoctorConsultationServiceMapperDao.updateDoctorConsultationServiceMapperStatus(doctorConsultationServiceMapperDto);
		} catch (Exception e) {
		logger.error("updateDoctorConsultationServiceMapperStatus failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getDoctorConsultationServiceMapperListCount(DoctorConsultationServiceMapperDto doctorConsultationServiceMapperDto) throws ApplicationException {
		try {
			return iDoctorConsultationServiceMapperDao.getDoctorConsultationServiceMapperListCount(doctorConsultationServiceMapperDto);
		} catch (Exception e) {
		logger.error("getDoctorConsultationServiceMapperListCount failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response doctorConCheckAlreadyExistOrNot(DoctorConsultationServiceMapperDto doctorConsultationServiceMapperDto) throws ApplicationException {
		try {
			ListIterator<DoctorConsultationServiceMapperDto> dtoList=doctorConsultationServiceMapperDto.getDocConsultationMapperList().listIterator();
			
			List<DoctorConsultationServiceMapperDto> list = new ArrayList<>();
			while(dtoList.hasNext()) {
				
				DoctorConsultationServiceMapperDto dto = dtoList.next();
				dto.setUnitId(doctorConsultationServiceMapperDto.getUnitId());
				dto.setOrganizationId(doctorConsultationServiceMapperDto.getOrganizationId());
				dto.setStatus(doctorConsultationServiceMapperDto.getStatus());
				dto.setCreatedBy(doctorConsultationServiceMapperDto.getCreatedBy());
				dto.setUpdatedBy(doctorConsultationServiceMapperDto.getUpdatedBy());
				Integer id = iDoctorConsultationServiceMapperDao.checkDoctorConsultationExistOrNot(dto);
				if (id != null) {
					dto.setDoctorConsultationServiceMapperId(id);
					list.add(dto);
				}
			}
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);

		} catch (Exception e) {
		logger.error("doctorConCheckAlreadyExistOrNot failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getDoctorCharges(DoctorConsChargesForMyLife doctorConsultationServiceMapperDto)
			throws ApplicationException {
		try {
			return iDoctorConsultationServiceMapperDao.getDoctorCharges(doctorConsultationServiceMapperDto);
		} catch (Exception e) {
		logger.error("getDoctorCharges failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public DoctorConsultationServiceMapperDto getDoctorConsultationServiceBySpecialityAndDoctorIdForUpdatePackage(
			EncounterMasterDto doctorConsultationServiceMapperDto) throws ApplicationException {
		// TODO Auto-generated method stub
		return iDoctorConsultationServiceMapperDao.getDoctorConsultationServiceBySpecialityAndDoctorIdForUpdatePackage(doctorConsultationServiceMapperDto);
	}

	
	
}
