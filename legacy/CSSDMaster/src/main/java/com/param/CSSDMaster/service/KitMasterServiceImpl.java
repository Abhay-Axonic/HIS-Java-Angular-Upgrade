package com.param.CSSDMaster.service;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.Response;
import com.param.CSSDMaster.dao.IKitMasterDao;
import com.param.CSSD.dao.IPreSterilizationProcessDao;
import com.param.CSSD.dao.IPreSterilizationProcessInstrumentDao;
import com.param.CSSD.model.PreSterilizationProcess;
import com.param.CSSD.model.PreSterilizationProcessInstrument;
import com.param.CSSDMaster.dao.IKitEquipmentMapperDao;
import com.param.CSSDMaster.dto.KitInstrumentMapperDto;
import com.param.CSSDMaster.dto.KitMasterDto;
import com.param.CSSDMaster.model.KitInstrumentMapper;
import com.param.CSSDMaster.model.KitMaster;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@Service
@SuppressWarnings({ "unchecked", "rawtypes" })
public class KitMasterServiceImpl implements IKitMasterService {

	final static Logger logger = Logger.getLogger(KitMasterServiceImpl.class);


	@Autowired
	private IKitMasterDao iMasterDao;

	@Autowired
	private IKitEquipmentMapperDao iMapperDao;
	
	@Autowired
	private IPreSterilizationProcessDao iPreSterilizationProcessDao;
	
	@Autowired
	private IPreSterilizationProcessInstrumentDao iPreSterilizationProcessEquipmentDao;

	@Override
	@Transactional
	public Response getAllMasterList(Integer organizationId,Integer unitId,Integer offset,Integer noOfRecordsPerPage) {
		try {
			return iMasterDao.getAllKitList(organizationId,unitId,offset,noOfRecordsPerPage);
		} catch (Exception e) {
			logger.error("getAllMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getAllSurgeonList(Integer organizationId,Integer unitId) {
		try {
			return iMasterDao.getAllSurgeonList(organizationId,unitId);
		} catch (Exception e) {
			logger.error("getAllSurgeonList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getActiveMasterList(Integer organizationId,Integer unitId,Integer storeId) {
		try {
			return iMasterDao.getActiveKitList(organizationId,unitId,storeId);
		} catch (Exception e) {
			logger.error("getActiveMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getKitWithEquipments(Integer organizationId,Integer unitId,Integer storeId,Integer kitId) {
		try {
			return iMasterDao.getKitWithEquipments(organizationId,unitId,storeId,kitId);
		} catch (Exception e) {
			logger.error("getKitWithEquipments failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response searchKitByKitName(Integer organizationId,Integer unitId,String name) {
		try {
			return iMasterDao.searchKitByKitName(organizationId,unitId,name);
		} catch (Exception e) {
			logger.error("searchKitByKitName failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response searchInstrumentByKitName(Integer organizationId,Integer unitId,Integer storeId,String name) {
		try {
			return iMasterDao.searchInstrumentByKitName(organizationId,unitId,storeId,name);
		} catch (Exception e) {
			logger.error("searchInstrumentByKitName failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@Override
	@Transactional
	public Response searchInstrumentAsKit(Integer organizationId,Integer unitId,String name) {
		try {
			return iMasterDao.searchInstrumentAsKit(organizationId,unitId,name);
		} catch (Exception e) {
			logger.error("searchInstrumentAsKit failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response searchInstrumentByName(Integer organizationId,Integer unitId,Integer storeId,String name) {
		try {
			return iMasterDao.searchInstrumentByName(organizationId,unitId,storeId,name);
		} catch (Exception e) {
			logger.error("searchInstrumentByName failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response searchInstrumentByMultipleFields(String code,String name,Integer instrumentNo,Integer organizationId,Integer unitId,Integer storeId) {
		try {
			return iMasterDao.searchInstrumentByMultipleFields(code,name,instrumentNo,organizationId,unitId,storeId);
		} catch (Exception e) {
			logger.error("searchInstrumentByMultipleFields failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	

	@Override
	@Transactional
	public Response createMaster(KitMasterDto masterDto) {
		try {
			Response resp = iMasterDao.createKit(masterDto);
			for (KitInstrumentMapperDto kitEquipmentMapperDto : masterDto.getKitInstrumentList()) {
				kitEquipmentMapperDto.setKitId(((Integer) resp.getObject()));
				kitEquipmentMapperDto.setCreated_by(masterDto.getCreated_by());
				kitEquipmentMapperDto.setUpdated_by(masterDto.getCreated_by());
				Response mapperResponse=iMapperDao.saveMapper(kitEquipmentMapperDto);
			}
			return new Response("success", "201", "Created Successfully", null, resp.getObject());
		} catch (Exception e) {
			logger.error("createMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response updateMaster(KitMasterDto masterDto) {
		try {
			Response resp = iMasterDao.updateKit(masterDto);
			if (!masterDto.getKitInstrumentList().isEmpty()) {
				for (KitInstrumentMapperDto kitEquipmentMapperDto : masterDto.getKitInstrumentList()) {
					if (kitEquipmentMapperDto.getKitInstrumentId() != null) {
						iMapperDao.updateMapper(kitEquipmentMapperDto);
					} else {
						kitEquipmentMapperDto.setKitId(masterDto.getKitId());
						iMapperDao.saveMapper(kitEquipmentMapperDto);
					}

				}
			}

			return resp;

		} catch (Exception e) {
			logger.error("updateMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response updateKitStatus(KitMasterDto masterDto) {
		try {
			return iMasterDao.updateKitStatus(masterDto);
		} catch (Exception e) {
			logger.error("updateKitStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getAllInstrumentList(Integer organizationId,Integer unitId) {
		try {
			return iMasterDao.getAllInstrumentList(organizationId,unitId);
		} catch (Exception e) {
			logger.error("getAllInstrumentList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getKitListcount(KitMasterDto kitMasterDto) {
		try {
			return iMasterDao.getKitListcount(kitMasterDto);
		} catch (Exception e) {
			logger.error("getKitListcount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getEquipmentList(KitMasterDto kitMasterDto) {
		try {
			return iMasterDao.getEquipmentList(kitMasterDto);
		} catch (Exception e) {
			logger.error("getEquipmentList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	
	
}