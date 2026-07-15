package com.param.CSSD.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.Response;
import com.param.CSSD.dao.IPreSterilizationProcessInstrumentDao;
import com.param.CSSD.dto.PreSterilizationProcessInstrumentDto;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PreSterilizationProcessInstrumentServiceImpl implements IPreSterilizationProcessInstrumentService {

	final static Logger logger = Logger.getLogger(PreSterilizationProcessInstrumentServiceImpl.class);


	@Autowired
	private IPreSterilizationProcessInstrumentDao iPreSterilizationProcessEquipmentDao;

	@Override
	@Transactional
	public Response updatePreSterilizationProcessEquipment(PreSterilizationProcessInstrumentDto preSterilizationProcessEquipmentDto) {
		try {
			return iPreSterilizationProcessEquipmentDao.updatePreSterilizationProcessEquipment(preSterilizationProcessEquipmentDto);
		} catch (Exception e) {
			logger.error("updatePreSterilizationProcessEquipment failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getPreSterilizationProcessEquipmentList(Integer kitId) {
		try {
			return iPreSterilizationProcessEquipmentDao.getPreSterilizationProcessEquipmentList(kitId);
		} catch (Exception e) {
			logger.error("getPreSterilizationProcessEquipmentList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response cleanEquipment(List<PreSterilizationProcessInstrumentDto> preSterilizationProcessEquipmentDtoList) {
		try {
			return iPreSterilizationProcessEquipmentDao.cleanEquipment(preSterilizationProcessEquipmentDtoList);
		} catch (Exception e) {
			logger.error("cleanEquipment failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response inspectEquipment(List<PreSterilizationProcessInstrumentDto> preSterilizationProcessEquipmentDtoList) {
		try {
			return iPreSterilizationProcessEquipmentDao.inspectEquipment(preSterilizationProcessEquipmentDtoList);
		} catch (Exception e) {
			logger.error("inspectEquipment failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@Override
	@Transactional
	public Response disInfectEquipment(List<PreSterilizationProcessInstrumentDto> preSterilizationProcessEquipmentDtoList) {
		try {
			return iPreSterilizationProcessEquipmentDao.disInfectEquipment(preSterilizationProcessEquipmentDtoList);
		} catch (Exception e) {
			logger.error("disInfectEquipment failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getEquipmentListPendingForInspection(Integer kitId) {
		try {
			return iPreSterilizationProcessEquipmentDao.getEquipmentListPendingForInspection(kitId);
		} catch (Exception e) {
			logger.error("getEquipmentListPendingForInspection failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getEquipmentListPendingForDisInfection(Integer kitId) {
		try {
			return iPreSterilizationProcessEquipmentDao.getEquipmentListPendingForDisInfection(kitId);
		} catch (Exception e) {
			logger.error("getEquipmentListPendingForDisInfection failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getEquipmentListPendingForKitFormation(Integer kitId) {
		try {
			return iPreSterilizationProcessEquipmentDao.getEquipmentListPendingForKitFormation(kitId);
		} catch (Exception e) {
			logger.error("getEquipmentListPendingForKitFormation failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	

}
