package com.param.CSSD.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.CSSD.dao.IPreSterilizationProcessDao;
import com.param.CSSD.dto.PreSterilizationProcessDto;
import com.param.CSSD.dto.PreSterilizationProcessInstrumentDto;
import com.param.CSSDMaster.dao.IKitEquipmentMapperDao;
import com.param.CSSDMaster.dao.IKitMasterDao;
import com.param.CSSDMaster.dto.CssdKitDto;
import com.param.CSSDMaster.dto.KitInstrumentMapperDto;
import com.param.CSSDMaster.dto.KitMasterDto;
import com.param.CSSDMaster.service.IKitMasterService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PreSterilizationProcessServiceImpl implements IPreSterilizationProcessService,ICommonConstants {

	final static Logger logger = Logger.getLogger(PreSterilizationProcessServiceImpl.class);


	@Autowired
	private IPreSterilizationProcessDao iPreSterilizationProcessDao;
	
	@Autowired
	private IKitMasterService iKitMasterService;

	@Autowired
	private IKitMasterDao iMasterDao;

	@Autowired
	private IKitEquipmentMapperDao iMapperDao;

	@Override
	@Transactional
	public Response getCleanedWorklistByMultipleFields(Date from,Date to,String name,String status,Integer organizationId,Integer unitId,Integer storeId) {
		try {
			return iPreSterilizationProcessDao.getCleanedWorklistByMultipleFields(from,to,name,status,organizationId,unitId,storeId);
		} catch (Exception e) {
			logger.error("getCleanedWorklistByMultipleFields failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getPendingForCleaningWorklistByMultipleFields(Date from,Date to,String name,Integer organizationId,Integer unitId,Integer storeId) {
		try {
			return iPreSterilizationProcessDao.getPendingForCleaningWorklistByMultipleFields(from,to,name,organizationId,unitId,storeId);
		} catch (Exception e) {
			logger.error("getPendingForCleaningWorklistByMultipleFields failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getKitInstrumentId(Integer kitId, Integer instrumentId) {
		try {
			return iPreSterilizationProcessDao.getKitInstrumentId(kitId,instrumentId);
		} catch (Exception e) {
			logger.error("getKitInstrumentId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@Override
	@Transactional
	public Response getInspectedWorklistByMultipleFields(Date from,Date to,String name,String status,Integer organizationId,Integer unitId,Integer storeId) {
		try {
			return iPreSterilizationProcessDao.getInspectedWorklistByMultipleFields(from,to,name,status,organizationId,unitId,storeId);
		} catch (Exception e) {
			logger.error("getInspectedWorklistByMultipleFields failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getDisinfectedWorklistByMultipleFields(Date from,Date to,String name,String status,Integer organizationId,Integer unitId,Integer storeId) {
		try {
			return iPreSterilizationProcessDao.getDisinfectedWorklistByMultipleFields(from,to,name,status,organizationId,unitId,storeId);
		} catch (Exception e) {
			logger.error("getDisinfectedWorklistByMultipleFields failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getPreSterilizationProcessByTransitionNumber(Integer organizationId,Integer unitId,Integer transitionNumber) {
		try {
			return iPreSterilizationProcessDao.getPreSterilizationProcessByTransitionNumber(organizationId,unitId,transitionNumber);
		} catch (Exception e) {
			logger.error("getPreSterilizationProcessByTransitionNumber failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response saveInstrumentAsKit(PreSterilizationProcessDto preSterilizationProcessDto) {
		try {
			
				KitMasterDto tempDto =(KitMasterDto) iMasterDao.getKitByNameAndCode(preSterilizationProcessDto.getName(),preSterilizationProcessDto.getCode(),
						preSterilizationProcessDto.getOrganizationId(),preSterilizationProcessDto.getUnitId(),preSterilizationProcessDto.getStoreId()).getObject() ;
			
				if(tempDto == null) {
					
					KitMasterDto kitMasterDto = new KitMasterDto();
					kitMasterDto.setName(preSterilizationProcessDto.getName());
					kitMasterDto.setCode(preSterilizationProcessDto.getCode());
					kitMasterDto.setCreated_by(preSterilizationProcessDto.getCreated_by());
					kitMasterDto.setUpdated_by(preSterilizationProcessDto.getUpdated_by());
					kitMasterDto.setOrganizationId(preSterilizationProcessDto.getOrganizationId());
					kitMasterDto.setUnitId(preSterilizationProcessDto.getUnitId());
					kitMasterDto.setStoreId(preSterilizationProcessDto.getStoreId());
					kitMasterDto.setIsInstrument(preSterilizationProcessDto.getIsInstrument());
					
					List<KitInstrumentMapperDto> instList = new ArrayList<>();
					for (PreSterilizationProcessInstrumentDto instrument : preSterilizationProcessDto.getKitInstrumentList()) {
						KitInstrumentMapperDto mapperDto = new KitInstrumentMapperDto();
						mapperDto.setInstrumentId(instrument.getInstrumentId());
						mapperDto.setQuantity(instrument.getQuantity());
						mapperDto.setCreated_by(preSterilizationProcessDto.getCreated_by());
						mapperDto.setUpdated_by(preSterilizationProcessDto.getUpdated_by());
						
						instList.add(mapperDto);
					}
					
					kitMasterDto.setKitInstrumentList(instList);
					
					Response resp = iMasterDao.createKit(kitMasterDto);
					for (KitInstrumentMapperDto kitEquipmentMapperDto : kitMasterDto.getKitInstrumentList()) {
						kitEquipmentMapperDto.setKitId(((Integer) resp.getObject()));
						kitEquipmentMapperDto.setCreated_by(kitMasterDto.getCreated_by());
						kitEquipmentMapperDto.setUpdated_by(kitMasterDto.getCreated_by());
						Response mapperResponse=iMapperDao.saveMapper(kitEquipmentMapperDto);
					}
					
					preSterilizationProcessDto.setKitId(((Integer) resp.getObject()));
				}else {
					preSterilizationProcessDto.setKitId(tempDto.getKitId());
				}
				return new Response("Success", null, null, null,preSterilizationProcessDto );
			
			
		} catch (Exception e) {
			logger.error("saveInstrumentAsKit failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@Override
	@Transactional
	public Response saveCleanedWorklist(PreSterilizationProcessDto preSterilizationProcessDto) {
		try {
			return iPreSterilizationProcessDao.saveCleanedWorklist(preSterilizationProcessDto);
		} catch (Exception e) {
			logger.error("saveCleanedWorklist failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response formKit(PreSterilizationProcessDto preSterilizationProcessDto) {
		try {
			return iPreSterilizationProcessDao.formKit(preSterilizationProcessDto);
		} catch (Exception e) {
			logger.error("formKit failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getKitFormationWorklistByMultipleFields(Date from,Date to,String name,String status,Integer organizationId,Integer unitId,Integer storeId) {
		try {
			return iPreSterilizationProcessDao.getKitFormationWorklistByMultipleFields(from,to,name,status,organizationId,unitId,storeId);
		} catch (Exception e) {
			logger.error("getKitFormationWorklistByMultipleFields failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getKitFormationWorklistByTransitionNumber(Integer organizationId,Integer unitId,Integer storeId,Integer transitionNumber) {
		try {
			return iPreSterilizationProcessDao.getKitFormationWorklistByTransitionNumber(organizationId,unitId,storeId,transitionNumber);
		} catch (Exception e) {
			logger.error("getKitFormationWorklistByTransitionNumber failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@Override
	@Transactional
	public Response replaceInstrument(Integer toBeReplaced,Integer replaceWith) {
		try {
			return iPreSterilizationProcessDao.replaceInstrument(toBeReplaced,replaceWith);
		} catch (Exception e) {
			logger.error("replaceInstrument failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@Override
	@Transactional
	public Response getSterilizedKitsByKitId(Integer organizationId,Integer unitId,Integer storeId,Integer kitId,Integer limit) {
		try {
			return iPreSterilizationProcessDao.getSterilizedKitsByKitId(organizationId,unitId,storeId,kitId,limit);
		} catch (Exception e) {
			logger.error("getSterilizedKitsByKitId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	@Transactional
	public Response updatePreSterilizationProcess(PreSterilizationProcessDto preSterilizationProcessDto) {
		try {
			return iPreSterilizationProcessDao.updatePreSterilizationProcess(preSterilizationProcessDto);
		} catch (Exception e) {
			logger.error("updatePreSterilizationProcess failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	

	@Override
	@Transactional
	public Response getPreSterilizationProcessList() {
		try {
			return iPreSterilizationProcessDao.getPreSterilizationProcessList();
		} catch (Exception e) {
			logger.error("getPreSterilizationProcessList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response searchPreSterilizationProcessList(String name) {
		try {
			return iPreSterilizationProcessDao.searchPreSterilizationProcessList(name);
		} catch (Exception e) {
			logger.error("searchPreSterilizationProcessList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@Override
	@Transactional
	public Response getKitListPendingForInspection() {
		try {
			return iPreSterilizationProcessDao.getKitListPendingForInspection();
		} catch (Exception e) {
			logger.error("getKitListPendingForInspection failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getKitListPendingForDisInfection() {
		try {
			return iPreSterilizationProcessDao.getKitListPendingForDisInfection();
		} catch (Exception e) {
			logger.error("getKitListPendingForDisInfection failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getKitListPendingForKitFormation() {
		try {
			return iPreSterilizationProcessDao.getKitListPendingForKitFormation();
		} catch (Exception e) {
			logger.error("getKitListPendingForKitFormation failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getFormedKitList() {
		try {
			return iPreSterilizationProcessDao.getFormedKitList();
		} catch (Exception e) {
			logger.error("getFormedKitList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getPreSterilizationProcessKitById(Integer kitId) {
		try {
			return iPreSterilizationProcessDao.getPreSterilizationProcessKitById(kitId);
		} catch (Exception e) {
			logger.error("getPreSterilizationProcessKitById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getCssdStoreList(PreSterilizationProcessDto preSterilizationProcessDto) {
		try {
			return iPreSterilizationProcessDao.getCssdStoreList(preSterilizationProcessDto);
		} catch (Exception e) {
			logger.error("getCssdStoreList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response checkStockAvaiability(PreSterilizationProcessDto preSterilizationProcessDto) {
		try {
			return iPreSterilizationProcessDao.checkStoreStockByItemId(preSterilizationProcessDto);
		} catch (Exception e) {
			logger.error("checkStockAvaiability failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response updateInTransitStock(PreSterilizationProcessDto preSterilizationProcessDto) {
		try {
			return iPreSterilizationProcessDao.updateInTransitStock(preSterilizationProcessDto);
		}catch (Exception e) {
			logger.error("updateInTransitStock failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response updateSterilizationStock(List<Integer> unitIds) {
		try {
			return iPreSterilizationProcessDao.updateSterilizationStock(unitIds);
		}catch (Exception e) {
			logger.error("updateSterilizationStock failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getSterilizedKitByKitName(CssdKitDto kitDto) {
		try {
			return iPreSterilizationProcessDao.getSterilizedKitByKitName(kitDto);
		}catch (Exception e) {
			logger.error("getSterilizedKitByKitName failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getSterilizedKitBatchList(CssdKitDto kitDto) {
		try {
			Response res=  iPreSterilizationProcessDao.getSterilizedKitBatchList(kitDto);
			
			CssdKitDto dto = new CssdKitDto();
			
			List<CssdKitDto> list = new ArrayList<>();
			if(res.getListObject().size()>0) {
				list.addAll(res.getListObject());
				dto.setKitName(list.get(0).getKitName());
				dto.setKitCode(list.get(0).getKitCode());
				dto.setBatchList(list);
			}
			 return new Response(SUCCESS, SUCCESS_CODE, null, null,dto);	
		}catch (Exception e) {
			logger.error("getSterilizedKitBatchList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
		
}
