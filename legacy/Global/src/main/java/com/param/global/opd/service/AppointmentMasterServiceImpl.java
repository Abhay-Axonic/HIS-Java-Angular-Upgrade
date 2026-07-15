package com.param.global.opd.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.sync.AppointmentDetailsRealTimeDto;
import com.param.global.dto.sync.AppointmentDoctorCoversheetMapperDto;
import com.param.global.opd.dao.IAppointmentMasterDao;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class AppointmentMasterServiceImpl implements IAppointmentMasterService, ICommonConstants{

	@Autowired
	private IAppointmentMasterDao iAppointmentMasterDao;
	
	@Override
	@Transactional
	public Response getAppointmentDetailsToSyncById(int appointmentId) throws ApplicationException {
		try{
			//get appointment basic details
			Response res=iAppointmentMasterDao.geAppointmentStatus(appointmentId);
			if(res.getCode().equalsIgnoreCase(SUCCESS_CODE)) {
			Response response = iAppointmentMasterDao.getBasicAppointmentDetaistoSync(appointmentId);
			if(response.getCode().equalsIgnoreCase(SUCCESS_CODE) && response.getObject() != null ){
				AppointmentDetailsRealTimeDto realTimeDto = (AppointmentDetailsRealTimeDto)response.getObject();
				Response medRes = iAppointmentMasterDao.getMedicationDetailsToSync(appointmentId);
				Response servRes = iAppointmentMasterDao.getAppointmentServiceDtlsToSync(appointmentId);
				Response vitalRes = iAppointmentMasterDao.getAppointmentVitalsToSync(appointmentId);
				Response diagnRes = iAppointmentMasterDao.getDiagnosisDtlsToSync(appointmentId);
				Response appointDetail = iAppointmentMasterDao.getAppointmentDetails(appointmentId);
				AppointmentDetailsRealTimeDto dto=(AppointmentDetailsRealTimeDto)appointDetail.getObject();
				
				realTimeDto.setMedicationList(medRes.getListObject() != null ? medRes.getListObject() : null);
				realTimeDto.setServiceAppoMapperList(servRes.getListObject() != null ? servRes.getListObject() : null);
				realTimeDto.setVitalMapperList(vitalRes.getListObject() != null ? vitalRes.getListObject() : null);
				realTimeDto.setDiagnosisAppoMapperList(diagnRes.getListObject() != null ? diagnRes.getListObject() : null);
				realTimeDto.setCoversheetMapper(dto.getCoversheetMapper());
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, realTimeDto);
			}
			return new Response<>(ERROR, NOT_FOUND_CODE, NOT_FOUND, null, null);
		}
		else {
			return new Response<>(ERROR, NOT_FOUND_CODE, NOT_FOUND, null, null);
			}
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response syncPatientPathalogyReport() {
		return iAppointmentMasterDao.syncPatientPathalogyReport();
	}
	
	@Override
	@Transactional
	public Response syncPatientRadiologyReport(Integer id) {
		return iAppointmentMasterDao.syncPatientRadiologyReport(id);
	}
}
