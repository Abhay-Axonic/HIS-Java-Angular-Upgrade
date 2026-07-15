package com.param.assetManagement.maintenance.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.assetManagement.maintenance.dao.IPreventiveMaintenanceDao;
import com.param.assetManagement.maintenance.dto.AssetScheduleChecklistDto;
import com.param.assetManagement.maintenance.dto.PreventiveMaintenanceDto;
import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class PreventiveMaintenanceServiceImpl  implements IPreventiveMaintenanceService,ICommonConstants,IBillingCommonConstants{

	private static final Logger logger = Logger.getLogger(PreventiveMaintenanceServiceImpl.class);

	@Autowired
	private IPreventiveMaintenanceDao iPreventiveMaintenanceDao;

	
	@Override
	public Response saveAssetSchedule(List<PreventiveMaintenanceDto> preventiveMaintenanceDto)
			throws ApplicationException {
		try {
			
			for (PreventiveMaintenanceDto dto : preventiveMaintenanceDto) {
				int assetScheduleId = iPreventiveMaintenanceDao.saveAssetSchedule(dto);
				if(assetScheduleId != 0 && dto.getGetChecklistData()!=null) {
					for(AssetScheduleChecklistDto assetScheduleDto : dto.getGetChecklistData()){
						try {
							if(assetScheduleDto.getChecklistNameId()!=null && assetScheduleDto.getChecklistNameId()!=0){
								assetScheduleDto.setAssetScheduleId(assetScheduleId);
								assetScheduleDto.setChecklistNameId(assetScheduleDto.getChecklistNameId());
								assetScheduleDto.setChecklistName(assetScheduleDto.getChecklistName());
								assetScheduleDto.setChecklistCode(assetScheduleDto.getChecklistCode());
								//assetScheduleDto.setStatus(assetScheduleDto.getStatus());
								assetScheduleDto.setCreatedBy(dto.getCreatedBy());
								assetScheduleDto.setCreatedDate(dto.getCreatedDate());
								assetScheduleDto.setUnitId(dto.getUnitId());
								assetScheduleDto.setOrganizationId(dto.getOrganizationId());
								
								iPreventiveMaintenanceDao.saveChecklists(assetScheduleDto);
							}
						}catch (Exception e) {
							logger.error("Error saving asset schedule checklist", e);
							throw new ServiceException(e.getMessage());
						}
					}
				}
			}

			new Response(SUCCESS, null, COMMON_SUCCESS, null, null);

		} catch (Exception e) {
			logger.error("Error saving asset schedule", e);
			throw new ServiceException(e.getMessage());
		}
		return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
	}

	@Override
	public Response getActiveAssestList(PreventiveMaintenanceDto preventiveMaintenanceDto)
			throws ApplicationException {
		try {
			return iPreventiveMaintenanceDao.getActiveAssestList(preventiveMaintenanceDto);
		} catch (Exception e) {
			logger.error("Error fetching active asset list", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response updateAssetSchedule(PreventiveMaintenanceDto preventiveMaintenanceDto)
			throws ApplicationException {
		try {
			iPreventiveMaintenanceDao.updateAssetSchedule(preventiveMaintenanceDto);
			return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
		} catch (Exception e) {
			logger.error("Error updating asset schedule", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response fetchAssetSchedule(PreventiveMaintenanceDto preventiveMaintenanceDto)
			throws ApplicationException {
		try {
			return iPreventiveMaintenanceDao.fetchAssetSchedule(preventiveMaintenanceDto);
		} catch (Exception e) {
			logger.error("Error fetching asset schedule", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response fetchCheckListByAssetScheduleId(PreventiveMaintenanceDto preventiveMaintenanceDto)
			throws ApplicationException {
		try {
			return iPreventiveMaintenanceDao.fetchCheckListByAssetScheduleId(preventiveMaintenanceDto);
		} catch (Exception e) {
			logger.error("Error fetching checklist by asset schedule id", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response addAndUpdateChecklist(PreventiveMaintenanceDto preventiveMaintenanceDto) throws ApplicationException{
		try {
			return iPreventiveMaintenanceDao.addAndUpdateChecklist(preventiveMaintenanceDto);
		} catch (Exception e) {
			logger.error("Error adding/updating checklist", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response deleteChecklist(PreventiveMaintenanceDto preventiveMaintenanceDto) throws ApplicationException{
		try {
			return iPreventiveMaintenanceDao.deleteChecklist(preventiveMaintenanceDto);
		} catch (Exception e) {
			logger.error("Error deleting checklist", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@Override
	public Response getAssetDataToView(PreventiveMaintenanceDto preventiveMaintenanceDto)
			throws ApplicationException {
		try {
			return iPreventiveMaintenanceDao.getAssetDataToView(preventiveMaintenanceDto);
		} catch (Exception e) {
			logger.error("Error fetching asset data to view", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getAssetDetailsByCriteria(PreventiveMaintenanceDto preventiveMaintenanceDto)
			throws ApplicationException {
		try {
			return iPreventiveMaintenanceDao.getAssetDetailsByCriteria(preventiveMaintenanceDto);

		} catch (Exception e) {
			logger.error("Error fetching asset details by criteria", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response saveChangedSchedule(PreventiveMaintenanceDto preventiveMaintenanceDto)
			throws ApplicationException {
		try {
			return iPreventiveMaintenanceDao.saveChangedSchedule(preventiveMaintenanceDto);

		} catch (Exception e) {
			logger.error("Error saving changed schedule", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response saveCompletionDetails(PreventiveMaintenanceDto preventiveMaintenanceDto)
			throws ApplicationException {
		try {
			return iPreventiveMaintenanceDao.saveCompletionDetails(preventiveMaintenanceDto);

		} catch (Exception e) {
			logger.error("Error saving completion details", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response fetchScheduledAndCompletionData(PreventiveMaintenanceDto preventiveMaintenanceDto)
			throws ApplicationException {
		try {
			return iPreventiveMaintenanceDao.fetchScheduledAndCompletionData(preventiveMaintenanceDto);

		} catch (Exception e) {
			logger.error("Error fetching scheduled and completion data", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response saveAssignDetails(PreventiveMaintenanceDto preventiveMaintenanceDto) throws ApplicationException {
		try {
			return iPreventiveMaintenanceDao.saveAssignDetails(preventiveMaintenanceDto);
		} catch (Exception e) {
			logger.error("Error saving assign details", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response saveOrUpdatePMSDetails(PreventiveMaintenanceDto dto)
			throws ApplicationException {
		try {
			return iPreventiveMaintenanceDao.saveOrUpdatePMSDetails(dto);
		} catch (Exception e) {
			logger.error("Error saving/updating PMS details", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response fetchPMSByPMSId(PreventiveMaintenanceDto dto)
			throws ApplicationException {
		try {
			return iPreventiveMaintenanceDao.fetchPMSByPMSId(dto);
		} catch (Exception e) {
			logger.error("Error fetching PMS by PMS id", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response deleteSpareById(PreventiveMaintenanceDto preventiveMaintenanceDto) throws ApplicationException{
		try {
			return iPreventiveMaintenanceDao.deleteSpareById(preventiveMaintenanceDto);
		} catch (Exception e) {
			logger.error("Error deleting spare", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
//	public Response saveOrUpdateSpareConsumption(SpareConsumptionDto dto) {
//		try {
//			return iPreventiveMaintenanceDao.saveOrUpdateSpareConsumption(dto);
//		} catch (Exception e) {
//			
//		}
//		return new Response(ERROR, null, null, null, null);
//	}
	
}
