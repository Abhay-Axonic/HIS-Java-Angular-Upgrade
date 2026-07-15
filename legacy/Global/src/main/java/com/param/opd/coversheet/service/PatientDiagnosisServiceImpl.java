package com.param.opd.coversheet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.opd.coversheet.dao.IPatientDiagnosisDao;
import com.param.opd.coversheet.dao.IPatientDiagnosisServiceMapperDao;
import com.param.opd.coversheet.dto.DiagnosisPatientAppointmentDetailsDto;
import com.param.opd.coversheet.dto.DiagnosisPatientAppointmentMapperDto;
import com.param.opd.coversheet.dto.PatientDiagnosisServiceMapperDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PatientDiagnosisServiceImpl implements IPatientDiagnosisService, ICommonConstants {
	final static Logger logger = Logger.getLogger(PatientDiagnosisServiceImpl.class);


	@Autowired
	IPatientDiagnosisDao iPatientDiagnosisDao;

	@Autowired
	IPatientDiagnosisServiceMapperDao iPatientDiagnosisServiceMapperDao;  
	
	@Override
	public Response savePatientDiagnosis(DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto)
			throws ApplicationException {
		try {
			boolean flag=false;
			if(diagnosisPatientAppointmentMapperDto.getEncounterId()==null) 
			{
				Response res=iPatientDiagnosisDao.checkPrimaryDaignosisAvailabel(diagnosisPatientAppointmentMapperDto);
			
				 if(res.getListObject()!=null && res.getListObject().size()>0)
				 {
					 for (DiagnosisPatientAppointmentDetailsDto dto : diagnosisPatientAppointmentMapperDto.getListDiagnosisPatientAppointmentDetailsDto()) 
					 {
						 if(dto.getDiagnosisType()==1) 
						 {
							 Response res1=iPatientDiagnosisDao.updateDiagnosisTypeAsSecondary(diagnosisPatientAppointmentMapperDto);
						 }
					 }
				}
			}else{
				Response res=iPatientDiagnosisDao.getPatientDiagnosisByPatientId(diagnosisPatientAppointmentMapperDto);
				if(res.getListObject()!=null && res.getListObject().size()>0)
				{
					 for(int i=0;i<res.getListObject().size();i++) 
					 {
						 DiagnosisPatientAppointmentMapperDto obj=(DiagnosisPatientAppointmentMapperDto)res.getListObject().get(i);
						 if(obj.getDiagnosisType() != null && obj.getDiagnosisType()==1) 
						 {
							 flag=true;
						 }
						 
					 }
					 for (DiagnosisPatientAppointmentDetailsDto dto : diagnosisPatientAppointmentMapperDto.getListDiagnosisPatientAppointmentDetailsDto()) 
					 {
						 if( flag=true) 
						 {
							 dto.setDiagnosisType(2);
						 }
					 }
				}else{
					if(diagnosisPatientAppointmentMapperDto.getListDiagnosisPatientAppointmentDetailsDto().size() == 1) 
					{
						 for (DiagnosisPatientAppointmentDetailsDto dto : diagnosisPatientAppointmentMapperDto.getListDiagnosisPatientAppointmentDetailsDto()) 
						 {
							 dto.setDiagnosisType(1);
						 }
					}
					else 
					{
						for(int i=0;i<diagnosisPatientAppointmentMapperDto.getListDiagnosisPatientAppointmentDetailsDto().size();i++)
						{
							if(i== 0) 
							{
								diagnosisPatientAppointmentMapperDto.getListDiagnosisPatientAppointmentDetailsDto().get(i).setDiagnosisType(1);
							}else 
							{
								diagnosisPatientAppointmentMapperDto.getListDiagnosisPatientAppointmentDetailsDto().get(i).setDiagnosisType(2);
							}
						}
					}
				}
			}
			return iPatientDiagnosisDao.savePatientDiagnosis(diagnosisPatientAppointmentMapperDto);
			
		} catch (Exception e) {
		logger.error("savePatientDiagnosis failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response getPatientDiagnosisByPatientId(
			DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto) throws ApplicationException {		
		try {
			return iPatientDiagnosisDao.getPatientDiagnosisByPatientId(diagnosisPatientAppointmentMapperDto);
		} catch (Exception e) {
		logger.error("getPatientDiagnosisByPatientId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	@Transactional
	public Response getPatientDiagnosisForCaseSheet(int patientId, int visitId, int visitTypeId)
			throws ApplicationException {
		try {
			return iPatientDiagnosisDao.getPatientDiagnosisForCaseSheet(patientId,visitId,visitTypeId);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}


	@Override
	@Transactional
	public Response getPatientDiagnosisByPatientIdForMedicalHistory(
			DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto) throws ApplicationException {
		try {
			return iPatientDiagnosisDao.getPatientDiagnosisByPatientIdForMedicalHistory(diagnosisPatientAppointmentMapperDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}


	@Override
	public Response savePatientCancelledDiagnosis(
			DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto) throws ApplicationException {
		try {
			return iPatientDiagnosisDao.savePatientCancelledDiagnosis(diagnosisPatientAppointmentMapperDto);
		} catch (Exception e) {
		logger.error("savePatientCancelledDiagnosis failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response getListPatientCancelledDiagnosis(DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto) throws ApplicationException {
		try {
			return iPatientDiagnosisDao.getListPatientCancelledDiagnosis(diagnosisPatientAppointmentMapperDto);
		} catch (Exception e) {
		logger.error("getListPatientCancelledDiagnosis failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response getPatientDiagnosisByPatientIdForIcon(
			DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto) throws ApplicationException {		
		try {
			return iPatientDiagnosisDao.getPatientDiagnosisByPatientIdForIcon(diagnosisPatientAppointmentMapperDto);
		} catch (Exception e) {
		logger.error("getPatientDiagnosisByPatientIdForIcon failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response getPatientDiagnosisByPatientIdForBookView(DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto) throws ApplicationException {
		try {
			return iPatientDiagnosisDao.getPatientDiagnosisByPatientIdForBookView(diagnosisPatientAppointmentMapperDto);
		} catch (Exception e) {
		logger.error("getPatientDiagnosisByPatientIdForBookView failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response updatePatientDiagnosisType(DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto) throws ApplicationException {
		try {
			return iPatientDiagnosisDao.updatePatientDiagnosisType(diagnosisPatientAppointmentMapperDto);
		} catch (Exception e) {
		logger.error("updatePatientDiagnosisType failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response checkPrimaryDaignosisAvailabel(DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto) throws ApplicationException {
		try {
			return iPatientDiagnosisDao.checkPrimaryDaignosisAvailabel(diagnosisPatientAppointmentMapperDto);
		} catch (Exception e) {
		logger.error("checkPrimaryDaignosisAvailabel failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response updateDiagnosisTypeAsSecondary(
			DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto) throws ApplicationException {
		try {
			return iPatientDiagnosisDao.updateDiagnosisTypeAsSecondary(diagnosisPatientAppointmentMapperDto);
		} catch (Exception e) {
		logger.error("updateDiagnosisTypeAsSecondary failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response saveResolvedDiagnosisDate(DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto)
			throws ApplicationException {
		try {
			return iPatientDiagnosisDao.saveResolvedDiagnosisDate(diagnosisPatientAppointmentMapperDto);
		} catch (Exception e) {
		logger.error("saveResolvedDiagnosisDate failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response getPatientDiagnosisByEncounterId( DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto) throws ApplicationException {		
		try {
			return iPatientDiagnosisDao.getPatientDiagnosisByEncounterId(diagnosisPatientAppointmentMapperDto);
		} catch (Exception e) {
		logger.error("getPatientDiagnosisByEncounterId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response getPatientDiagnosisByAdmissionId(DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto) throws ApplicationException {		
		try {
			return iPatientDiagnosisDao.getPatientDiagnosisByAdmissionId(diagnosisPatientAppointmentMapperDto);
		} catch (Exception e) {
		logger.error("getPatientDiagnosisByAdmissionId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response getFrequentlyUsedDiagnosisList(DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto) {
		try {
			return iPatientDiagnosisDao.getFrequentlyUsedDiagnosisList(diagnosisPatientAppointmentMapperDto);
		} catch (Exception e) {
		logger.error("getFrequentlyUsedDiagnosisList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response updatePatientDiagnosisTypeOPD(
			DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto) throws ApplicationException {
		try {
			return iPatientDiagnosisDao.updatePatientDiagnosisTypeOPD(diagnosisPatientAppointmentMapperDto);
		} catch (Exception e) {
		logger.error("updatePatientDiagnosisTypeOPD failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response saveFreeTexPatientDiagnosis(
			DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto) throws ApplicationException {
		try {
			Response res=iPatientDiagnosisDao.getdiagnosisNameByName(diagnosisPatientAppointmentMapperDto);
			if(res.getListObject()!=null && res.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
			return iPatientDiagnosisDao.saveFreeTexPatientDiagnosis(diagnosisPatientAppointmentMapperDto);
			}
		} catch (Exception e) {
		logger.error("saveFreeTexPatientDiagnosis failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response serchFreeTexPatientDiagnosis(DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto) {
		try {
			return iPatientDiagnosisDao.serchFreeTexPatientDiagnosis(diagnosisPatientAppointmentMapperDto);
		} catch (Exception e) {
		logger.error("serchFreeTexPatientDiagnosis failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	
	@Override
	public Response savePatientDaignosisServiceMapper(List<PatientDiagnosisServiceMapperDto> list)
			throws ApplicationException {

		try {
			return iPatientDiagnosisServiceMapperDao.savePatientDaignosisServiceMapper(list);
		} catch (Exception e) {
		logger.error("savePatientDaignosisServiceMapper failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response getPatientDaignosisServiceMapper(PatientDiagnosisServiceMapperDto dto) throws ApplicationException {
		try {
			return iPatientDiagnosisServiceMapperDao.getPatientDaignosisServiceMapper(dto);
		} catch (Exception e) {
		logger.error("getPatientDaignosisServiceMapper failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}




}
