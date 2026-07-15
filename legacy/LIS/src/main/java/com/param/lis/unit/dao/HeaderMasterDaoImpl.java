package com.param.lis.unit.dao;

import java.math.BigInteger;
import java.util.List;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.data.dto.HeaderParameterMapperDto;
import com.param.entity.lis.unit.HeaderMaster;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.global.dto.GlobalCommonDto;
import com.param.lis.unit.dto.HeaderMasterDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings(
{ "rawtypes", "unchecked" })
public class HeaderMasterDaoImpl extends GenericDao<HeaderMaster, Integer>
		implements IHeaderMasterDao, ICommonConstants, IError
{

	public HeaderMasterDaoImpl()
	{
		super(HeaderMaster.class);
	}

	/*
	 * @Autowired private SessionFactory sessionFactory;
	 */

	@Autowired
	private DozerBeanMapper mapper;

	final static Logger logger = Logger.getLogger(HeaderMaster.class);

	@Override
	public Response addHeaderMaster(HeaderMasterDto departmentMasterDto) throws ApplicationException
	{
		try
		{
			HeaderMaster headerMaster = mapper.map(departmentMasterDto, HeaderMaster.class,
					"HeaderMasterDtoTOHeaderMaster");
			
			headerMaster.setStatus('A');
			
			HeaderMaster headerMst = save(headerMaster);
			if (headerMst != null)
			{
				return new Response(SUCCESS, SUCCESS_200, HEADER_ADD_SUCC, null, headerMst);
			} else
			{
				return new Response(SUCCESS, SUCCESS_200, HEADER_ADD_FAIL, null, null);
			}
		}catch(HibernateException exception){
			logger.error("Exection", exception);
		} catch (Exception e)
		{
			logger.error("Exection", e);
		}
		return new Response(ERROR, ERR_500,HEADER_ADD_FAIL, null, null);
	}

	@Override
	public Response getHeaderMasterById(Integer orgId,Integer unitId,Integer headerId) throws ApplicationException
	{
		
		try
		{
			GlobalCommonDto departmentMaster = (GlobalCommonDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_HEADER_BY_HEADR_ID").setParameter("orgId", orgId)
					.setParameter("headerId", headerId)
					.setParameter("unitId", unitId)
                    .setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class))					
					.uniqueResult();
			
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, departmentMaster);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			return new Response(ERROR, ERR_500, HEADER_NOT_FOUND, null, null);
		}
		
	}

	@Override
	public Response updateHeaderMaster(HeaderMasterDto departmentMasterDto) throws ApplicationException
	{
		try
		{
			HeaderMaster departmentMaster = mapper.map(departmentMasterDto, HeaderMaster.class,
					"HeaderMasterDtoTOHeaderMaster");
			
			departmentMaster.setStatus('A');
			HeaderMaster techniqueMat = update(departmentMaster);
			if (techniqueMat != null)
			{
				return new Response(SUCCESS, SUCCESS_200, HEADER_UPDATE_SUCC, null, null);
			} else
			{
				return new Response(SUCCESS, SUCCESS_200, HEADER_UPDATE_FAIL, null, null);
			}
		} catch (Exception e)
		{
			logger.error("Exection", e);
			return new Response(ERROR, ERR_500, HEADER_ADD_FAIL, null, null);
		}
	}


	
	@Override
	public Response ActivateInactivateHeaderMaster(Integer orgId, Integer headerId, Character headerStatus)
			throws ApplicationException
	{
		try
		{
			HeaderMaster headerMaster = findById(headerId);
			if (headerMaster.getHeaderId() != 0)
			{
				headerMaster.setStatus(headerStatus);
				HeaderMaster departmentMst = update(headerMaster);
				if (departmentMst.getStatus() == ACTIVE)
				{
					return new Response(SUCCESS, SUCCESS_200, HEADER_ACTIVATE_SUCC, null, null);
				} else if (departmentMst.getStatus() == INACTIVE)
				{
					return new Response(SUCCESS, SUCCESS_200, HEADER_INACTIVATE_SUCC, null, null);
				} else
				{
					if (headerStatus == ACTIVE)
					{
						return new Response(SUCCESS, SUCCESS_200, HEADER_ACTIVATE_FAIL, null, null);
					} else
					{
						return new Response(SUCCESS, SUCCESS_200, HEADER_INACTIVATE_FAIL, null, null);
					}
				}
			} else
			{
				return new Response(SUCCESS, SUCCESS_200, HEADER_NOT_FOUND, null, null);
			}

		} catch (Exception e)
		{
			logger.error("Exection", e);
			return new Response(ERROR, ERR_500, HEADER_NOT_FOUND, null, null);
		}
	}

	@Override
	public Response listHeaderMaster(Integer orgId,Integer unitId, Integer offset, Integer recordPerPage) throws ApplicationException
	{
		try
		{
			List<GlobalCommonDto> listTechniqueMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PAGINATED_HEADER_MASTER_LIST")
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setFirstResult(offset != null ? offset : 0)
					.setMaxResults(recordPerPage != null ? recordPerPage : 10)
					.setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).list();
			
			if(listTechniqueMasterDto.isEmpty())
			{
				return new Response(ERROR, ERR_500, HEADER_RECORDS_NOT_FOUND, null, null);
			}
			else{
				return new Response(SUCCESS, SUCCESS_200, null, listTechniqueMasterDto, null);
			}
			
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getToTalHeaderMasterRecord(Integer orgId,Integer unitId) throws ApplicationException
	{
		try
		{
			BigInteger HeaderMasterTotalRecordCount = (BigInteger) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TOTAL_HEADER_RECORD")
					.setParameter("unitId", unitId)
					.setParameter("orgId", orgId).uniqueResult();
			
			if (HeaderMasterTotalRecordCount.compareTo(BigInteger.ZERO) == 1)
			{
				return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, HeaderMasterTotalRecordCount);
			} else
			{
				return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, BigInteger.ZERO);
			}

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response checkForUniqueHeaderMasterDesc(HeaderMasterDto departmentMasterDto) throws ApplicationException {
		try
		{
			List<GlobalCommonDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_HEADER_BY_DESCRIPTION")
					.setParameter("desc", departmentMasterDto.getDesc().trim().toLowerCase())
					.setParameter("orgId", departmentMasterDto.getOrgId())
					.setParameter("unitId", departmentMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).list();
			
				return new Response(ERROR, ERR_500, ERR_500_MESSAGE, list, null);
			
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
		
	}

	@Override
	public Response updatecheckForUniqueHeaderMasterDesc(HeaderMasterDto headerMasterDto) throws ApplicationException {
		try
		{
			List<GlobalCommonDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("UPDATE_GET_HEADER_BY_DESCRIPTION")
					.setParameter("desc", headerMasterDto.getDesc().trim().toLowerCase())
					.setParameter("orgId", headerMasterDto.getOrgId())
					.setParameter("unitId", headerMasterDto.getUnitId())
					.setParameter("headerId", headerMasterDto.getHeaderId())
					.setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).list();
			
				return new Response(ERROR, ERR_500, ERR_500_MESSAGE, list, null);
			
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getHeaderParameterList(Integer orgId, Integer unitId, Integer headerId)
			throws ApplicationException {
		try
		{
			List<HeaderParameterMapperDto> list =  sessionFactory.getCurrentSession()
					.createSQLQuery("select thpm.header_id  as \"headerId\", " + 
							"  thpm.header_parameter_mapper_id  as \"headerParameterMapperId\" , " + 
							"  thpm.para_sequence  as \"paraSequence\", " + 
							"  thpm.parameter_id  as \"parameterId\", " + 
							"  thpm.status as \"status\","
							+ " header_mstr.header_desc as \"headerName\","
							+ " mpm.parameter_name  as \"parameterName\" " + 
							"  from lab.t_header_parameter_mapper thpm  " + 
							"  inner join  lab.m_parameter_master mpm  on thpm.parameter_id = mpm.parameter_id " + 
							"  inner join lab.m_header_master header_mstr on header_mstr.header_id = thpm.header_id "+
							"  where thpm.header_id = :headerId  and thpm.unit_id = :orgUnitId and thpm.organization_id = :orgId and thpm.status = 'A' ")
					.setParameter("orgId", orgId)
					.setParameter("orgUnitId", unitId)
					.setParameter("headerId", headerId)
	                .setResultTransformer(Transformers.aliasToBean(HeaderParameterMapperDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, list, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

}
