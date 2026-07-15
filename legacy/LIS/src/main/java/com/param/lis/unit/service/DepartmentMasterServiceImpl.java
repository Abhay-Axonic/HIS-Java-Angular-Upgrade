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
import com.param.lis.unit.dao.IDepartmentMasterDao;
import com.param.lis.unit.dto.DepartmentMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings(
{ "rawtypes", "unchecked" })
public class DepartmentMasterServiceImpl implements IDepartmentMasterService, ICommonConstants, IError
{

	@Autowired
	IDepartmentMasterDao iDepartmentMasterDao;

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unused")
	@Override
	@Transactional
	public Response addDepartmentMaster(DepartmentMasterDto departmentMasterDto) throws ApplicationException
	{
		try
		{
			GlobalCommonDto departmentMat = (GlobalCommonDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DEPARTMENT_BY_CODE")
					.setParameter("code", departmentMasterDto.getcode().trim().toLowerCase())
					.setParameter("orgId", departmentMasterDto.getOrgId())
					.setParameter("unitId", departmentMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).uniqueResult();
			if (departmentMat == null)
			{
				departmentMasterDto.setCreatedDate(TimeZoneComponent.getDateByzone(departmentMasterDto.getUnitId()).getTime());
				Response response = iDepartmentMasterDao.addDepartmentMaster(departmentMasterDto);
				return response;
			} else
			{
				return new Response(ERROR, ERR_500, DEPARTMENT_CODE_EXISIS, null, null);
			}

		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getDepartmentMasterById(Integer orgId,Integer unitId, Integer departmentId) throws ApplicationException
	{
		try
		{
			return iDepartmentMasterDao.getDepartmentMasterById(orgId,unitId,departmentId);
		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response updateDepartmentMaster(DepartmentMasterDto departmentMasterDto) throws ApplicationException
	{
		
		try
		{
			GlobalCommonDto departmentMat = (GlobalCommonDto) sessionFactory.getCurrentSession()
					.getNamedQuery("UPDATE_GET_DEPARTMENT_BY_CODE")
					.setParameter("code", departmentMasterDto.getcode().trim().toLowerCase())
					.setParameter("orgId", departmentMasterDto.getOrgId())
					.setParameter("unitId", departmentMasterDto.getUnitId())
					.setParameter("departmentId", departmentMasterDto.getDepartmentId())
					.setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).uniqueResult();
			if (departmentMat == null)
			{
				departmentMasterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(departmentMasterDto.getUnitId()).getTime());
				return iDepartmentMasterDao.updateDepartmentMaster(departmentMasterDto);
			} else
			{
				return new Response(ERROR, ERR_500, DEPARTMENT_CODE_EXISIS, null, null);
			}

		} catch (Exception e)
		{
			
			throw e;
		}
	
	}

	@Override
	@Transactional
	public Response ActivateInactivateDepartmentMaster(Integer orgId, Integer sampleId, Character sampleStatus)
			throws ApplicationException
	{
		try
		{
			return iDepartmentMasterDao.ActivateInactivateDepartmentMaster(orgId, sampleId, sampleStatus);
		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response listDepartmentMaster(Integer orgId, Integer offset, Integer recordPerPage) throws ApplicationException
	{
		try
		{
			return iDepartmentMasterDao.listDepartmentMaster(orgId, offset, recordPerPage);
		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getToTalDepartmentMasterRecord(Integer orgId) throws ApplicationException
	{
		try
		{
			return iDepartmentMasterDao.getToTalDepartmentMasterRecord(orgId);
		} catch (Exception e)
		{
			
			throw e;
		}
	}

}
