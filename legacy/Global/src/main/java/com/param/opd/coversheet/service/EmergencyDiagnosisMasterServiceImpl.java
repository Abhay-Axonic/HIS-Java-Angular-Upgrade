package com.param.opd.coversheet.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.opd.coversheet.dao.IEmergencyDiagnosisMasterDao;
import com.param.opd.coversheet.dto.D2NDiagnisis;
import com.param.opd.coversheet.dto.D2NMedicationDto;
import com.param.opd.coversheet.dto.D2NPatientListDto;
import com.param.opd.coversheet.dto.EmergencyDiagnosisMasterDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({ "rawtypes","unchecked"})
public class EmergencyDiagnosisMasterServiceImpl implements IEmergencyDiagnosisMasterService, ICommonConstants{
	final static Logger logger = Logger.getLogger(EmergencyDiagnosisMasterServiceImpl.class);


	@Autowired
	IEmergencyDiagnosisMasterDao iEmergencyDiagnosisMasterDao;
	
	@Override
	public Response saveEmergencyDiagnosisMaster(EmergencyDiagnosisMasterDto emergencyDiagnosisMasterDto)
			throws ApplicationException {
		try {
		Response response=iEmergencyDiagnosisMasterDao.checkEmergencyDiagnosisMasterAvailable(emergencyDiagnosisMasterDto);
	    if(response.getListObject().size()>0 && response.getListObject()!=null){
			
			return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
		}else{
	
			return	iEmergencyDiagnosisMasterDao.saveEmergencyDiagnosisMaster(emergencyDiagnosisMasterDto);
		}
	} catch (Exception e) {
	logger.error("saveEmergencyDiagnosisMaster failed", e);
	}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);		
	}

	@Override
	public Response getListOfEmergencyDiagnosisMaster(EmergencyDiagnosisMasterDto emergencyDiagnosisMasterDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iEmergencyDiagnosisMasterDao.getListOfEmergencyDiagnosisMaster(emergencyDiagnosisMasterDto);
	}

	@Override
	public Response updateEmergencyDiagnosisMasterStatus(EmergencyDiagnosisMasterDto emergencyDiagnosisMasterDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iEmergencyDiagnosisMasterDao.updateEmergencyDiagnosisMasterStatus(emergencyDiagnosisMasterDto);
	}

	@Override
	public Response getEmergencyDiagnosisMasterById(EmergencyDiagnosisMasterDto emergencyDiagnosisMasterDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iEmergencyDiagnosisMasterDao.getEmergencyDiagnosisMasterById(emergencyDiagnosisMasterDto);
	}

	@Override
	public Response updateEmergencyDiagnosisMaster(EmergencyDiagnosisMasterDto emergencyDiagnosisMasterDto)
			throws ApplicationException {
		
		Response res = iEmergencyDiagnosisMasterDao.checkEmergencyDiagnosisMasterAvailable(emergencyDiagnosisMasterDto);
		try {
			if(res.getListObject()!=null && res.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iEmergencyDiagnosisMasterDao.updateEmergencyDiagnosisMaster(emergencyDiagnosisMasterDto);
			}

		} catch (Exception e) {
		logger.error("updateEmergencyDiagnosisMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getEmergencyDiagnosisMasterCount(EmergencyDiagnosisMasterDto emergencyDiagnosisMasterDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iEmergencyDiagnosisMasterDao.getEmergencyDiagnosisMasterCount(emergencyDiagnosisMasterDto);
	}
	
	@Override
	public Response checkEmergencyDiagnosisMasterAvailable(EmergencyDiagnosisMasterDto emergencyDiagnosisMasterDto) throws ApplicationException{
			try {
				
				return iEmergencyDiagnosisMasterDao.checkEmergencyDiagnosisMasterAvailable(emergencyDiagnosisMasterDto);
		
		} catch (Exception e) {
		logger.error("checkEmergencyDiagnosisMasterAvailable failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveEmergencyDiagnosis(EmergencyDiagnosisMasterDto emergencyDiagnosisMasterDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iEmergencyDiagnosisMasterDao.getActiveEmergencyDiagnosis(emergencyDiagnosisMasterDto);
	}

	@Override
	public Response getDataForDoorToNiddle(EmergencyDiagnosisMasterDto emergencyDiagnosisMasterDto)
			throws ApplicationException 
	{
		try {
			Response res = iEmergencyDiagnosisMasterDao.getDataForDoorToNiddle(emergencyDiagnosisMasterDto);
			if(res!=null && res.getListObject()!=null)
			{
				
				List<D2NDiagnisis> mainList = (List<D2NDiagnisis>)res.getListObject();
				
				List<D2NDiagnisis> finalList = new LinkedList<D2NDiagnisis>();
				int prevId = 0;
				for(D2NDiagnisis dto : mainList)
				{
					if(prevId==0 || dto.getDiagnosisId()!=prevId)
					{
						D2NDiagnisis d2nDiagnisis = new D2NDiagnisis();
							d2nDiagnisis.setDiagnosisId(dto.getDiagnosisId());
							d2nDiagnisis.setDiagnosisName(dto.getDiagnosisName());
						finalList.add(d2nDiagnisis);
						
						prevId = dto.getDiagnosisId();
					}
				}
				
				for(D2NDiagnisis finalListDto : finalList)
				{
					List<D2NPatientListDto> finalPatient = new LinkedList<>();
					int prevPatId = 0;
					for(D2NDiagnisis dto : mainList)
					{
						if(finalListDto.getDiagnosisId().equals(dto.getDiagnosisId()))
						{
							if(prevPatId==0 || dto.getPatientId()!=prevPatId)
							{
								D2NPatientListDto patientDto = new D2NPatientListDto();
									patientDto.setPatientId(dto.getPatientId());
									patientDto.setPatientName(dto.getPatientName());
									patientDto.setAdmissionId(dto.getAdmissionId());
								finalPatient.add(patientDto);
								
								prevPatId = dto.getPatientId();
							}
						}
					}
					finalListDto.setPatientList(finalPatient);
				}
				
				for(D2NDiagnisis finalListDto : finalList)
				{
					for(D2NPatientListDto patDto : finalListDto.getPatientList())
					{
						List<D2NMedicationDto> medicationListFinal = new LinkedList<>();
						for(D2NDiagnisis dto : mainList)
						{
							if(finalListDto.getDiagnosisId().equals(dto.getDiagnosisId()) && patDto.getPatientId().equals(dto.getPatientId()))
							{
								D2NMedicationDto medicationDto = new D2NMedicationDto();
									medicationDto.setItemId(dto.getItemId());
									medicationDto.setItemDesc(dto.getItemDesc());
									medicationDto.setAdministrationTime(dto.getAdministrationTime());
									medicationDto.setAdmissionTime(dto.getAdmissionTime());
								medicationListFinal.add(medicationDto);
							}
						}
						patDto.setMedicationDetails(medicationListFinal);
					}
				}
				
			return new Response<>(SUCCESS, SUCCESS_CODE, null, finalList, null);
			}
			
		}catch (Exception e) {
		logger.error("getDataForDoorToNiddle failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
