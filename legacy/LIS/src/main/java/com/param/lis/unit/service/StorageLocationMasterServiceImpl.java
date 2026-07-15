package com.param.lis.unit.service;

import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.global.dto.GlobalCommonDto;
import com.param.lis.unit.dao.IStorageLocationMasterDao;
import com.param.lis.unit.dto.StorageLocationMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings(
{ "rawtypes", "unchecked" })
public class StorageLocationMasterServiceImpl implements IStorageLocationMasterService, ICommonConstants, IError
{

	@Autowired
	IStorageLocationMasterDao iStorageLocationMasterDao;

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	@Transactional
	public Response addStorageLocationMaster(StorageLocationMasterDto storageLocationMasterDto) throws ApplicationException
	{
		try
		{
			GlobalCommonDto departmentMat = (GlobalCommonDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_STORAGE_LOCATION_MASTER_BY_CODE")
					.setParameter("code", storageLocationMasterDto.getcode().trim().toLowerCase())
					.setParameter("orgId", storageLocationMasterDto.getOrgId())
					.setParameter("unitId", storageLocationMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).uniqueResult();
			if (departmentMat == null)
			{
				storageLocationMasterDto.setCreatedDate(TimeZoneComponent.getDateByzone(storageLocationMasterDto.getUnitId()).getTime());
				Response response = iStorageLocationMasterDao.addStorageLocationMaster(storageLocationMasterDto);
				return response;
			} else
			{
				return new Response(ERROR, ERR_500, STORAGE_LOCATION_CODE_EXISIS, null, null);
			}

		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getStorageLocationMasterById(Integer orgId,Integer unitId, Integer storageLocationId) throws ApplicationException
	{
		try
		{
			return iStorageLocationMasterDao.getStorageLocationMasterById(orgId,unitId,storageLocationId);
		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response updateStorageLocationMaster(StorageLocationMasterDto storageLocationMasterDto) throws ApplicationException
	{
		
		try
		{
			GlobalCommonDto storageLocationMat = (GlobalCommonDto) sessionFactory.getCurrentSession()
					.getNamedQuery("UPDATE_GET_STORAGE_LOCATION_MASTER_BY_CODE")
					.setParameter("code", storageLocationMasterDto.getcode().trim().toLowerCase())
					.setParameter("orgId", storageLocationMasterDto.getOrgId())
					.setParameter("unitId", storageLocationMasterDto.getUnitId())
					.setParameter("storageLocationId", storageLocationMasterDto.getStorageLocationId())
					.setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).uniqueResult();
			if (storageLocationMat == null)
			{
				storageLocationMasterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(storageLocationMasterDto.getUnitId()).getTime());
				return  iStorageLocationMasterDao.updateStorageLocationMaster(storageLocationMasterDto);
			} else
			{
				return new Response(ERROR, ERR_500, DEPARTMENT_CODE_EXISIS, null, null);
			}

		} catch (Exception e)
		{
			
			throw e;
		}
		/*try
		{
			storageLocationMasterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId).getTime());
			return iStorageLocationMasterDao.updateStorageLocationMaster(storageLocationMasterDto);
		} catch (Exception e)
		{
			
			throw e;
		}*/
	}

	@Override
	@Transactional
	public Response ActivateInactivateStorageLocationMaster(Integer orgId, Integer storageLocationId, Character storageLocationStatus)
			throws ApplicationException
	{
		try
		{
			return iStorageLocationMasterDao.ActivateInactivateStorageLocationMaster(orgId, storageLocationId, storageLocationStatus);
		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response listStorageLocationMaster(Integer orgId, Integer offset, Integer recordPerPage) throws ApplicationException
	{
		try
		{
			return iStorageLocationMasterDao.listStorageLocationMaster(orgId, offset, recordPerPage);
		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getToTalStorageLocationMasterRecord(Integer orgId) throws ApplicationException
	{
		try
		{
			return iStorageLocationMasterDao.getToTalStorageLocationMasterRecord(orgId);
		} catch (Exception e)
		{
			
			throw e;
		}
	}

}
