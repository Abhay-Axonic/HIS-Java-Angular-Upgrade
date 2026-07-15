package com.param.bloodBank.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import in.param.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.bloodBank.dao.IBloodBankTypeComponentMasterDao;
import com.param.bloodBank.dto.BloodBankTypeComponentMasterDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;



import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes", "unchecked"})
public class BloodBankTypeComponentMasterServiceImpl implements IBloodBankTypeComponentMasterService, ICommonConstants, IError{

	final static Logger logger = Logger.getLogger(BloodBankTypeComponentMasterServiceImpl.class);


	@Autowired
	IBloodBankTypeComponentMasterDao iBloodBankTypeComponentMasterDao;

	@Override
	public Response saveBloodBankTypeComponentMaster(BloodBankTypeComponentMasterDto bloodBankTypeComponentMasterDto)
			throws ApplicationException {
		try
		{
			Response res = iBloodBankTypeComponentMasterDao.checkActiveBloodBankTypeComponentMasterAvaiable(bloodBankTypeComponentMasterDto);
			if(res.getListObject() != null && res.getListObject().size() > 0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
				
			}else{
			Iterator ite=bloodBankTypeComponentMasterDto.getSelectBloodBankTypeComponentMasterList().iterator(); 
			while (ite.hasNext()) {
				BloodBankTypeComponentMasterDto bloodBankTypeComponentMasterDto1 = new BloodBankTypeComponentMasterDto();
				Integer bloodComponentId = (Integer) ite.next();
//				String date = TimeZoneComponent.getDateByzone(unitId).toString();
				bloodBankTypeComponentMasterDto1.setCreatedDate(bloodBankTypeComponentMasterDto.getCreatedDate());
				bloodBankTypeComponentMasterDto1.setBloodBagTypeId(bloodBankTypeComponentMasterDto.getBloodBagTypeId());
				bloodBankTypeComponentMasterDto1.setBloodComponentId(bloodComponentId);
				bloodBankTypeComponentMasterDto1.setStatus(bloodBankTypeComponentMasterDto.getStatus());
				bloodBankTypeComponentMasterDto1.setOrganizationId(bloodBankTypeComponentMasterDto.getOrganizationId());
				bloodBankTypeComponentMasterDto1.setUnitId(bloodBankTypeComponentMasterDto.getUnitId());
				bloodBankTypeComponentMasterDto1.setCreatedBy(bloodBankTypeComponentMasterDto.getCreatedBy());
				bloodBankTypeComponentMasterDto1.setUpdatedBy(bloodBankTypeComponentMasterDto.getUpdatedBy());
				bloodBankTypeComponentMasterDto1.setUpdatedDate(bloodBankTypeComponentMasterDto.getUpdatedDate());
				bloodBankTypeComponentMasterDto1.setIsDeleted(bloodBankTypeComponentMasterDto.getIsDeleted());

				iBloodBankTypeComponentMasterDao.saveBloodBankTypeComponentMaster(bloodBankTypeComponentMasterDto1);
			}
			}
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (Exception e)
		{
			logger.error("saveBloodBankTypeComponentMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateBloodBankTypeComponentMaster(BloodBankTypeComponentMasterDto bloodBankTypeComponentMasterDto)
			throws ApplicationException {
		try
		{
			List<BloodBankTypeComponentMasterDto> listBloodBankTypeComponentMasterDto = new ArrayList();
			Iterator ite=bloodBankTypeComponentMasterDto.getSelectBloodBankTypeComponentMasterList().iterator(); 
			while (ite.hasNext()) {
				BloodBankTypeComponentMasterDto bloodBankTypeComponentMasterDto1=new BloodBankTypeComponentMasterDto();
				BloodBankTypeComponentMasterDto bloodBankTypeComponentMaster = new BloodBankTypeComponentMasterDto();
				Integer bloodComponentId = (Integer) ite.next();
//				String date = TimeZoneComponent.getDateByzone(unitId).toString();
				bloodBankTypeComponentMasterDto1.setUpdatedDate(bloodBankTypeComponentMasterDto.getUpdatedDate());
				bloodBankTypeComponentMaster.setBloodBagTypeId(bloodBankTypeComponentMasterDto.getBloodBagTypeId());
				bloodBankTypeComponentMasterDto1.setCreatedBy(bloodBankTypeComponentMasterDto.getCreatedBy());
				bloodBankTypeComponentMasterDto1.setOrganizationId(bloodBankTypeComponentMasterDto.getOrganizationId());
				bloodBankTypeComponentMasterDto1.setUnitId(bloodBankTypeComponentMasterDto.getUnitId());
				bloodBankTypeComponentMasterDto1.setBloodBagTypeId(bloodBankTypeComponentMasterDto.getBloodBagTypeId());
				bloodBankTypeComponentMasterDto1.setCreatedDate(bloodBankTypeComponentMasterDto.getCreatedDate());
				bloodBankTypeComponentMasterDto1.setStatus(bloodBankTypeComponentMasterDto.getStatus());
				bloodBankTypeComponentMasterDto1.setIsDeleted(bloodBankTypeComponentMasterDto.getIsDeleted());
				bloodBankTypeComponentMasterDto1.setBloodComponentId(bloodComponentId);
				bloodBankTypeComponentMasterDto1.setOrganizationId(bloodBankTypeComponentMasterDto.getOrganizationId());
				bloodBankTypeComponentMasterDto1.setUpdatedBy(bloodBankTypeComponentMasterDto.getUpdatedBy());
				listBloodBankTypeComponentMasterDto.add(bloodBankTypeComponentMasterDto1);
			}
			if(!listBloodBankTypeComponentMasterDto.isEmpty())
			{
				return iBloodBankTypeComponentMasterDao.updateBloodBankTypeComponentMaster(listBloodBankTypeComponentMasterDto);
			}
			else {
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			}
			} catch (Exception e)
		{
			logger.error("updateBloodBankTypeComponentMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	@Override
	public Response updateBloodBankTypeComponentMasterStatus(Integer bloodBagTypeId, Integer organizationId,
			Character status, Integer unitId) throws ApplicationException {
		try
		{
			return iBloodBankTypeComponentMasterDao.updateBloodBankTypeComponentMasterStatus(bloodBagTypeId, organizationId, status, unitId);
		} catch (Exception e)
		{
			logger.error("updateBloodBankTypeComponentMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBloodBankTypeComponentMasterList(Integer offset, Integer noOfRecordsPerPage)
			throws ApplicationException {
		try
		{
			return iBloodBankTypeComponentMasterDao.getBloodBankTypeComponentMasterList(offset, noOfRecordsPerPage);
		} catch (Exception e)
		{
			logger.error("getBloodBankTypeComponentMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBloodBankTypeComponentMasterById(Integer bloodBagTypeId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try
		{
			return iBloodBankTypeComponentMasterDao.getBloodBankTypeComponentMasterById(bloodBagTypeId, organizationId, unitId);
		} catch (Exception e)
		{
			logger.error("getBloodBankTypeComponentMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	@Override
	public Response getActiveBloodBankTypeComponentMaster(Integer organizationId, Integer unitId) throws ApplicationException {
		try
		{
			return iBloodBankTypeComponentMasterDao.getActiveBloodBankTypeComponentMaster(organizationId, unitId);
		} catch (Exception e)
		{
			logger.error("getActiveBloodBankTypeComponentMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBloodBankTypeComponentMasterCount(Integer organizationId, Integer unitId) throws ApplicationException {
		try
		{
			return iBloodBankTypeComponentMasterDao.getBloodBankTypeComponentMasterCount(organizationId, unitId);
		} catch (Exception e)
		{
			logger.error("getBloodBankTypeComponentMasterCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response checkActiveBloodBankTypeComponentMasterAvaiable(
			BloodBankTypeComponentMasterDto bloodBankTypeComponentMasterDto) throws ApplicationException {
		try
		{
			return iBloodBankTypeComponentMasterDao.checkActiveBloodBankTypeComponentMasterAvaiable(bloodBankTypeComponentMasterDto);
		} catch (Exception e)
		{
			logger.error("checkActiveBloodBankTypeComponentMasterAvaiable failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBloodBagList(Integer organizationId, Integer unitId) throws ApplicationException {
		try
		{
			return iBloodBankTypeComponentMasterDao.getBloodBagList(organizationId, unitId);
		} catch (Exception e)
		{
			logger.error("getBloodBagList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBloodComponentList(Integer organizationId, Integer unitId) throws ApplicationException {
		try
		{
			return iBloodBankTypeComponentMasterDao.getBloodComponentList(organizationId, unitId);
		} catch (Exception e)
		{
			logger.error("getBloodComponentList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDetailedComponentList(Integer bloodBagTypeId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try
		{
			return iBloodBankTypeComponentMasterDao.getDetailedComponentList(bloodBagTypeId, organizationId, unitId);
		} catch (Exception e)
		{
			logger.error("getDetailedComponentList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}