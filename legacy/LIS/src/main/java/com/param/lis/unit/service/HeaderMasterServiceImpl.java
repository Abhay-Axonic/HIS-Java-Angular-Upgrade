package com.param.lis.unit.service;

import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.data.dto.HeaderParameterMapperDto;
import com.param.entity.lis.unit.HeaderMaster;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.global.dto.GlobalCommonDto;
import com.param.lis.unit.dao.IHeaderMasterDao;
import com.param.lis.unit.dao.IHeaderParameterMapperDao;
import com.param.lis.unit.dto.HeaderMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings(
{ "rawtypes", "unchecked" })
public class HeaderMasterServiceImpl implements IHeaderMasterService, ICommonConstants, IError
{

	@Autowired
	IHeaderMasterDao iHeaderMasterDao;

	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired 
	IHeaderParameterMapperDao  iHeaderParameterMapperDao;

	@Override
	@Transactional
	public Response addHeaderMaster(HeaderMasterDto departmentMasterDto) throws ApplicationException
	{
		try
		{
			GlobalCommonDto departmentMat = (GlobalCommonDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_HEADER_BY_CODE")
					.setParameter("code", departmentMasterDto.getCode().trim().toLowerCase())
					.setParameter("orgId", departmentMasterDto.getOrgId())
					.setParameter("unitId", departmentMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).uniqueResult();
			
			Response res = iHeaderMasterDao.checkForUniqueHeaderMasterDesc(departmentMasterDto);
			if(departmentMat!=null) {
				return new Response(ERROR, ERR_500, HEADER_CODE_EXISIS, null, null);
			}else if(res.getListObject()!=null && res.getListObject().size() > 0) {
				return new Response(ERROR, ERR_500, HEADER_NAME_EXISIS, null, null);
			}else 
			{
				departmentMasterDto.setCreatedDate(TimeZoneComponent.getDateByzone(departmentMasterDto.getUnitId()).getTime());
				Response response = iHeaderMasterDao.addHeaderMaster(departmentMasterDto);
				if(response.getObject()!= null) {
					HeaderMaster mster = (HeaderMaster) response.getObject();
					
					if(departmentMasterDto.getHeaderParameterMapperDtoList() !=null && departmentMasterDto.getHeaderParameterMapperDtoList().size()>0  ) {
						for (HeaderParameterMapperDto dto : departmentMasterDto.getHeaderParameterMapperDtoList()) {
							dto.setHeaderId(mster.getHeaderId());
							iHeaderParameterMapperDao.saveHeaderParameterMapper(dto);
						}
					}
					
				}
				return response;
			}
		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getHeaderMasterById(Integer orgId,Integer unitId, Integer headerId) throws ApplicationException
	{
		try
		{
			return iHeaderMasterDao.getHeaderMasterById(orgId,unitId,headerId);
		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response updateHeaderMaster(HeaderMasterDto headerMasterDto) throws ApplicationException
	{
		
		try
		{
			GlobalCommonDto departmentMat = (GlobalCommonDto) sessionFactory.getCurrentSession()
					.getNamedQuery("UPDATE_GET_HEADER_BY_CODE")
					.setParameter("code", headerMasterDto.getCode().trim().toLowerCase())
					.setParameter("orgId", headerMasterDto.getOrgId())
					.setParameter("unitId", headerMasterDto.getUnitId())
					.setParameter("headerId", headerMasterDto.getHeaderId())
					.setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).uniqueResult();
			
			Response res = iHeaderMasterDao.updatecheckForUniqueHeaderMasterDesc(headerMasterDto);
			if (departmentMat != null)
			{
				return new Response(ERROR, ERR_500, HEADER_CODE_EXISIS, null, null);
			}else if(res.getListObject()!=null && res.getListObject().size()>0) {
				return new Response(ERROR, ERR_500, HEADER_NAME_EXISIS, null, null);
			}else
			{
				 headerMasterDto.setUpdatedDate(TimeZoneComponent.getDateByzone( headerMasterDto.getUnitId()).getTime());
				 Response res1 =  iHeaderMasterDao.updateHeaderMaster(headerMasterDto);
				 
				 if(res1.getStatus().equals(SUCCESS)) {
					 
					 if( headerMasterDto.getHeaderParameterMapperDtoList()  != null &&  headerMasterDto.getHeaderParameterMapperDtoList().size()>0) {
						 for (HeaderParameterMapperDto dto : headerMasterDto.getHeaderParameterMapperDtoList()) {
							 iHeaderParameterMapperDao.saveHeaderParameterMapper(dto);
						}
					 }
					 
				 }
				 
				 return res1;
			}

		} catch (Exception e)
		{
			
			throw e;
		}
	
	}

	@Override
	@Transactional
	public Response ActivateInactivateHeaderMaster(Integer orgId, Integer headerId, Character headerStatus)
			throws ApplicationException
	{
		try
		{
			return iHeaderMasterDao.ActivateInactivateHeaderMaster(orgId, headerId, headerStatus);
		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response listHeaderMaster(Integer orgId,Integer unitId, Integer offset, Integer recordPerPage) throws ApplicationException
	{
		try
		{
			return iHeaderMasterDao.listHeaderMaster(orgId, unitId,offset, recordPerPage);
		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getToTalHeaderMasterRecord(Integer orgId,Integer unitId) throws ApplicationException
	{
		try
		{
			return iHeaderMasterDao.getToTalHeaderMasterRecord(orgId,unitId);
		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getHeaderParameterList(Integer orgId, Integer unitId, Integer headerId)
			throws ApplicationException {
		try
		{
			return iHeaderMasterDao.getHeaderParameterList(orgId,unitId,headerId);
		} catch (Exception e)
		{
			
			throw e;
		}
	}

}
