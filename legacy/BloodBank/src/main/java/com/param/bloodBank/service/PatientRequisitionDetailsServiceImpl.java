package com.param.bloodBank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.bloodBank.dao.IBloodComponentOrderMasterDao;
import com.param.bloodBank.dao.IPatientRequisitionDetailsDao;
import com.param.bloodBank.dto.BloodComponentOrderMasterDto;
import com.param.bloodBank.dto.CrossMatchRegisterDto;
import com.param.bloodBank.dto.PatientRequisitionDetailsDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes"})
public class PatientRequisitionDetailsServiceImpl implements IPatientRequisitionDetailsService, ICommonConstants{

	final static Logger logger = Logger.getLogger(PatientRequisitionDetailsServiceImpl.class);


	@Autowired
	IPatientRequisitionDetailsDao iPatientRequisitionDetailsDao;
	
	@Autowired
	IBloodComponentOrderMasterDao iBloodComponentOrderMasterDao;

	@Override
	public Response getComponentListByPatientId(PatientRequisitionDetailsDto dto) throws ApplicationException {
		try {
			return  iPatientRequisitionDetailsDao.getComponentListByPatientId(dto);
			 
		} catch (Exception e) {
			logger.error("getComponentListByPatientId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response savePatientRequisitionDetails(PatientRequisitionDetailsDto dto) throws ApplicationException {
		try {
			Response res =  iPatientRequisitionDetailsDao.savePatientRequisitionDetails(dto);
			
			if(res.getObject()!=null)
			{
				BloodComponentOrderMasterDto orderDto = new BloodComponentOrderMasterDto();
				orderDto.setBloodComponentOrderId(dto.getBloodComponentOrderId());
				orderDto.setUpdatedBy(dto.getUpdatedBy());
				orderDto.setUpdatedDate(dto.getUpdatedDate());
				orderDto.setIsExported(true);
				iBloodComponentOrderMasterDao.updateIsExportedStatus(orderDto);
			//	return res;
			}
			 
			return res;
		} catch (Exception e) {
			logger.error("savePatientRequisitionDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPatientRequisitionDetailsOnCrossMatch(PatientRequisitionDetailsDto dto)
			throws ApplicationException {
		try {
			return  iPatientRequisitionDetailsDao.getPatientRequisitionDetailsOnCrossMatch(dto);
			 
		} catch (Exception e) {
			logger.error("getPatientRequisitionDetailsOnCrossMatch failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateExportedToCrossMatchStatus(PatientRequisitionDetailsDto dto) throws ApplicationException {
		try {
			return  iPatientRequisitionDetailsDao.updateExportedToCrossMatchStatus(dto);
			 
		} catch (Exception e) {
			logger.error("updateExportedToCrossMatchStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updatePatientRequisitionDetails(CrossMatchRegisterDto crossMatchRegisterDto)
			throws ApplicationException {
		try {
			return  iPatientRequisitionDetailsDao.updatePatientRequisitionDetails(crossMatchRegisterDto);
			 
		} catch (Exception e) {
			logger.error("updatePatientRequisitionDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}
