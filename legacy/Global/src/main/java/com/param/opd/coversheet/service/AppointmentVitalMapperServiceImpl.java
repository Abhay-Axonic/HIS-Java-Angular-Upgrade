package com.param.opd.coversheet.service;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.param.exception.ApplicationException;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IScheduleLogsDao;
import com.param.opd.coversheet.dao.IAppointmentVitalMapperDao;
import com.param.opd.coversheet.dao.IVitalAppointmentQualifierMapperDao;
import com.param.opd.coversheet.dto.AppointmentVitalMapperDto;
import com.param.opd.coversheet.dto.ListAppointmentVitalMapperDto;
import com.param.opd.coversheet.model.AppointmentVitalMapper;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"rawtypes", "unchecked"})
@Service
public class AppointmentVitalMapperServiceImpl implements IAppointmentVitalMapperService, ICommonConstants {
	final static Logger logger = Logger.getLogger(AppointmentVitalMapperServiceImpl.class);


	@Autowired
	private IAppointmentVitalMapperDao iAppointmentVitalMapperDao;
	@Autowired
	private IScheduleLogsDao iScheduleLogsDao;
	@Autowired
	private IVitalAppointmentQualifierMapperDao iVitalAppointmentQualifierMapperDao;
	
	@Override
	@Transactional
	public Response saveAppointmentVitalMapper(AppointmentVitalMapperDto appointmentVitalMapperDto)throws ApplicationException {
			try {
				if(appointmentVitalMapperDto.getListAppointmentVitalMapperDtos().size()>0) {
					
					
				
				Response res = iAppointmentVitalMapperDao.saveAppointmentVitalMapper(appointmentVitalMapperDto);
				if(res.getListObject()!=null) {
					List<ListAppointmentVitalMapperDto> newList = res.getListObject();
					
				//appointmentVitalMapperDto = (AppointmentVitalMapperDto) res.getListObject();
				if(newList.size()!=0) {
					
					for (ListAppointmentVitalMapperDto vitalMethoDto : newList) {
						if(vitalMethoDto.getVitalLocationList()!=null && vitalMethoDto.getVitalLocationList().size()>0 ) {
						for(AppointmentVitalMapperDto vitalDto : vitalMethoDto.getVitalLocationList()) {
							vitalMethoDto.setVitalId(vitalDto.getVitalId());
							vitalMethoDto.setVitalQualifireId(vitalDto.getVitalQualifireId());
							vitalDto.setPatientId(appointmentVitalMapperDto.getPatientId());
							vitalDto.setAppointmentVitalMapperId(vitalMethoDto.getAppointmentVitalMapperId());
							vitalDto.setAdmissionId(appointmentVitalMapperDto.getAdmissionId());
							vitalDto.setCreatedBy(appointmentVitalMapperDto.getCreatedBy());
							vitalDto.setCreatedDate(appointmentVitalMapperDto.getCreatedDate());
							vitalDto.setCreatedTime(appointmentVitalMapperDto.getCreatedTime());
							vitalDto.setEncounterId(appointmentVitalMapperDto.getEncounterId());
							vitalDto.setOrganizationId(appointmentVitalMapperDto.getOrganizationId());
							vitalDto.setUnitId(appointmentVitalMapperDto.getUnitId());
							vitalDto.setUpdatedBy(appointmentVitalMapperDto.getUpdatedBy());
							vitalDto.setUpdatedDate(appointmentVitalMapperDto.getUpdatedDate());
							vitalDto.setStatus(appointmentVitalMapperDto.getStatus());
						//	vitalDto.setVitalQualifireId(vitalMethoDto.getVitalQualifireId());
							iVitalAppointmentQualifierMapperDao.saveVitalAppointmentQualifierMapper(vitalDto);
						}
						}
						
						if(vitalMethoDto.getVitalPositionList()!=null && vitalMethoDto.getVitalPositionList().size()>0 ) {
							for(AppointmentVitalMapperDto vitalDto : vitalMethoDto.getVitalPositionList()) {
								vitalMethoDto.setVitalId(vitalDto.getVitalId());
								vitalMethoDto.setVitalQualifireId(vitalDto.getVitalQualifireId());
								vitalDto.setPatientId(appointmentVitalMapperDto.getPatientId());
								vitalDto.setAppointmentVitalMapperId(vitalMethoDto.getAppointmentVitalMapperId());
								vitalDto.setAdmissionId(appointmentVitalMapperDto.getAdmissionId());
								vitalDto.setCreatedBy(appointmentVitalMapperDto.getCreatedBy());
								vitalDto.setCreatedDate(appointmentVitalMapperDto.getCreatedDate());
								vitalDto.setCreatedTime(appointmentVitalMapperDto.getCreatedTime());
								vitalDto.setEncounterId(appointmentVitalMapperDto.getEncounterId());
								vitalDto.setOrganizationId(appointmentVitalMapperDto.getOrganizationId());
								vitalDto.setUnitId(appointmentVitalMapperDto.getUnitId());
								vitalDto.setUpdatedBy(appointmentVitalMapperDto.getUpdatedBy());
								vitalDto.setUpdatedDate(appointmentVitalMapperDto.getUpdatedDate());
								vitalDto.setStatus(appointmentVitalMapperDto.getStatus());
							//	vitalDto.setVitalQualifireId(vitalMethoDto.getVitalQualifireId());
								iVitalAppointmentQualifierMapperDao.saveVitalAppointmentQualifierMapper(vitalDto);
							}
							}
						
						if(vitalMethoDto.getVitalMethodList()!=null && vitalMethoDto.getVitalMethodList().size()>0 ) {
							for(AppointmentVitalMapperDto vitalDto : vitalMethoDto.getVitalMethodList()) {
								vitalMethoDto.setVitalId(vitalDto.getVitalId());
								vitalMethoDto.setVitalQualifireId(vitalDto.getVitalQualifireId());
								vitalDto.setPatientId(appointmentVitalMapperDto.getPatientId());
								vitalDto.setAppointmentVitalMapperId(vitalMethoDto.getAppointmentVitalMapperId());
								vitalDto.setAdmissionId(appointmentVitalMapperDto.getAdmissionId());
								vitalDto.setCreatedBy(appointmentVitalMapperDto.getCreatedBy());
								vitalDto.setCreatedDate(appointmentVitalMapperDto.getCreatedDate());
								vitalDto.setCreatedTime(appointmentVitalMapperDto.getCreatedTime());
								vitalDto.setEncounterId(appointmentVitalMapperDto.getEncounterId());
								vitalDto.setOrganizationId(appointmentVitalMapperDto.getOrganizationId());
								vitalDto.setUnitId(appointmentVitalMapperDto.getUnitId());
								vitalDto.setUpdatedBy(appointmentVitalMapperDto.getUpdatedBy());
								vitalDto.setUpdatedDate(appointmentVitalMapperDto.getUpdatedDate());
								vitalDto.setStatus(appointmentVitalMapperDto.getStatus());
							//	vitalDto.setVitalQualifireId(vitalMethoDto.getVitalQualifireId());
								iVitalAppointmentQualifierMapperDao.saveVitalAppointmentQualifierMapper(vitalDto);
							}
							}
						
						if(vitalMethoDto.getVitalQualityList()!=null && vitalMethoDto.getVitalQualityList().size()>0 ) {
							for(AppointmentVitalMapperDto vitalDto : vitalMethoDto.getVitalQualityList()) {
								vitalMethoDto.setVitalId(vitalDto.getVitalId());
								vitalMethoDto.setVitalQualifireId(vitalDto.getVitalQualifireId());
								vitalDto.setPatientId(appointmentVitalMapperDto.getPatientId());
								vitalDto.setAppointmentVitalMapperId(vitalMethoDto.getAppointmentVitalMapperId());
								vitalDto.setAdmissionId(appointmentVitalMapperDto.getAdmissionId());
								vitalDto.setCreatedBy(appointmentVitalMapperDto.getCreatedBy());
								vitalDto.setCreatedDate(appointmentVitalMapperDto.getCreatedDate());
								vitalDto.setCreatedTime(appointmentVitalMapperDto.getCreatedTime());
								vitalDto.setEncounterId(appointmentVitalMapperDto.getEncounterId());
								vitalDto.setOrganizationId(appointmentVitalMapperDto.getOrganizationId());
								vitalDto.setUnitId(appointmentVitalMapperDto.getUnitId());
								vitalDto.setUpdatedBy(appointmentVitalMapperDto.getUpdatedBy());
								vitalDto.setUpdatedDate(appointmentVitalMapperDto.getUpdatedDate());
								vitalDto.setStatus(appointmentVitalMapperDto.getStatus());
							//	vitalDto.setVitalQualifireId(vitalMethoDto.getVitalQualifireId());
								iVitalAppointmentQualifierMapperDao.saveVitalAppointmentQualifierMapper(vitalDto);
							}
							}
						
						if(vitalMethoDto.getVitalSiteList()!=null && vitalMethoDto.getVitalSiteList().size()>0 ) {
							for(AppointmentVitalMapperDto vitalDto : vitalMethoDto.getVitalSiteList()) {
								vitalMethoDto.setVitalId(vitalDto.getVitalId());
								vitalMethoDto.setVitalQualifireId(vitalDto.getVitalQualifireId());
								vitalDto.setPatientId(appointmentVitalMapperDto.getPatientId());
								vitalDto.setAppointmentVitalMapperId(vitalMethoDto.getAppointmentVitalMapperId());
								vitalDto.setAdmissionId(appointmentVitalMapperDto.getAdmissionId());
								vitalDto.setCreatedBy(appointmentVitalMapperDto.getCreatedBy());
								vitalDto.setCreatedDate(appointmentVitalMapperDto.getCreatedDate());
								vitalDto.setCreatedTime(appointmentVitalMapperDto.getCreatedTime());
								vitalDto.setEncounterId(appointmentVitalMapperDto.getEncounterId());
								vitalDto.setOrganizationId(appointmentVitalMapperDto.getOrganizationId());
								vitalDto.setUnitId(appointmentVitalMapperDto.getUnitId());
								vitalDto.setUpdatedBy(appointmentVitalMapperDto.getUpdatedBy());
								vitalDto.setUpdatedDate(appointmentVitalMapperDto.getUpdatedDate());
								vitalDto.setStatus(appointmentVitalMapperDto.getStatus());
							//	vitalDto.setVitalQualifireId(vitalMethoDto.getVitalQualifireId());
								iVitalAppointmentQualifierMapperDao.saveVitalAppointmentQualifierMapper(vitalDto);
							}
							}
						
						if(vitalMethoDto.getVitalCuffList()!=null && vitalMethoDto.getVitalCuffList().size()>0 ) {
							for(AppointmentVitalMapperDto vitalDto : vitalMethoDto.getVitalCuffList()) {
								vitalMethoDto.setVitalId(vitalDto.getVitalId());
								vitalMethoDto.setVitalQualifireId(vitalDto.getVitalQualifireId());
								vitalDto.setPatientId(appointmentVitalMapperDto.getPatientId());
								vitalDto.setAppointmentVitalMapperId(vitalMethoDto.getAppointmentVitalMapperId());
								vitalDto.setAdmissionId(appointmentVitalMapperDto.getAdmissionId());
								vitalDto.setCreatedBy(appointmentVitalMapperDto.getCreatedBy());
								vitalDto.setCreatedDate(appointmentVitalMapperDto.getCreatedDate());
								vitalDto.setCreatedTime(appointmentVitalMapperDto.getCreatedTime());
								vitalDto.setEncounterId(appointmentVitalMapperDto.getEncounterId());
								vitalDto.setOrganizationId(appointmentVitalMapperDto.getOrganizationId());
								vitalDto.setUnitId(appointmentVitalMapperDto.getUnitId());
								vitalDto.setUpdatedBy(appointmentVitalMapperDto.getUpdatedBy());
								vitalDto.setUpdatedDate(appointmentVitalMapperDto.getUpdatedDate());
								vitalDto.setStatus(appointmentVitalMapperDto.getStatus());
							//	vitalDto.setVitalQualifireId(vitalMethoDto.getVitalQualifireId());
								iVitalAppointmentQualifierMapperDao.saveVitalAppointmentQualifierMapper(vitalDto);
							}
							}
					}
					
				}
					
				}
				}
		return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
	} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	
	}

	@Override
	
	public Response getListOfAppointmentVitalMapper(AppointmentVitalMapperDto appointmentVitalMapperDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iAppointmentVitalMapperDao.getListOfAppointmentVitalMapper(appointmentVitalMapperDto);
	}

	@Override
	public Response getListOfAppointmentVitalMapperByPatientId(
			AppointmentVitalMapperDto appointmentVitalMapperDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iAppointmentVitalMapperDao.getListOfAppointmentVitalMapperByPatientId(appointmentVitalMapperDto);
	}

	@Override
	@Transactional
	public Response getOldAppointmentVitalMapper(
			AppointmentVitalMapperDto appointmentVitalMapperDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iAppointmentVitalMapperDao.getOldAppointmentVitalMapper(appointmentVitalMapperDto);
	}

	@Override
	@Transactional
	public Response getCurrentAppointmentVitalMapper(
			AppointmentVitalMapperDto appointmentVitalMapperDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iAppointmentVitalMapperDao.getCurrentAppointmentVitalMapper(appointmentVitalMapperDto);
	}

	

	@Override
	@Transactional
	public Response updateSaveNurseReviewedFlag(
			AppointmentVitalMapper appointmentVitalMapper)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iAppointmentVitalMapperDao.updateSaveNurseReviewedFlag(appointmentVitalMapper);
	}

	@Override
	@Transactional
	public Response updateAppointmentVitalMapper(
			AppointmentVitalMapperDto appointmentVitalMapperDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iAppointmentVitalMapperDao.updateAppointmentVitalMapperDto(appointmentVitalMapperDto);
	}

	@Override
	@Transactional
	public Response saveAppointmentVitalMapperFromAndroid(List<AppointmentVitalMapperDto> appointmentVitalMapperDto)
			throws ApplicationException {
		/*AppointmentVitalMapperDto dto=new AppointmentVitalMapperDto();
		dto.setEncounterId(appointmentVitalMapperDto.get(0).getEncounterId());
		dto.setPatientId(appointmentVitalMapperDto.get(0).getPatientId());
		dto.setVitalId(appointmentVitalMapperDto.get(0).getVitalId());
		dto.setRoleId(appointmentVitalMapperDto.get(0).getRoleId());
		Response appointmentVitalMapperResponse = iAppointmentVitalMapperDao.updateAppointmentVitalMapper(dto);*/
		return iAppointmentVitalMapperDao.saveAppointmentVitalMapperFromAndroid(appointmentVitalMapperDto);
	}

	@Override
	@Transactional
	public Response getConnectionStringForMyLife() throws ApplicationException {
		// TODO Auto-generated method stub
		return iScheduleLogsDao.getConnectionStringForMyLife();
	}

	@Override
	@Transactional
	public Response getIsCriticalByPatientId(AppointmentVitalMapperDto appointmentVitalMapperDto)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return iAppointmentVitalMapperDao.getIsCriticalByPatientId(appointmentVitalMapperDto);
		}

	@Override
	public Response updateVitalForEnteredInError(AppointmentVitalMapperDto appointmentVitalMapperDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iAppointmentVitalMapperDao.updateVitalForEnteredInError(appointmentVitalMapperDto);
	}

	@Override
	public Response getListOfVitalsByPatientIdForCoversheet(AppointmentVitalMapperDto appointmentVitalMapperDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iAppointmentVitalMapperDao.getListOfVitalsByPatientIdForCoversheet(appointmentVitalMapperDto);
	}

	@Override
	public Response getListOfEnteredInErrorVitals(AppointmentVitalMapperDto appointmentVitalMapperDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iAppointmentVitalMapperDao.getListOfEnteredInErrorVitals(appointmentVitalMapperDto);
	}

	@Override
	public Response getListOfVitalsByPatientIdForBookView(AppointmentVitalMapperDto appointmentVitalMapperDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iAppointmentVitalMapperDao.getListOfVitalsByPatientIdForBookView(appointmentVitalMapperDto);	
		}

	@Override
	public Response getQualifiersListForPopUp(AppointmentVitalMapperDto appointmentVitalMapperDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iAppointmentVitalMapperDao.getQualifiersListForPopUp(appointmentVitalMapperDto);	
	}

	@Override
	@Transactional
	public Response getListOfVitalsByPatientIdForDoctorApp(AppointmentVitalMapperDto appointmentVitalMapperDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iAppointmentVitalMapperDao.getListOfVitalsByPatientIdForDoctorApp(appointmentVitalMapperDto);	
	
	}

	
}
