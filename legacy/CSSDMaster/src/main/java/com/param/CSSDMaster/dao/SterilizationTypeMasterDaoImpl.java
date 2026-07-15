package com.param.CSSDMaster.dao;

import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.CSSDMaster.dto.SterilizationTypeMasterDto;
import com.param.CSSDMaster.model.SterilizationTypeMaster;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SterilizationTypeMasterDaoImpl extends GenericDao<SterilizationTypeMaster, Integer>
		implements ISterilizationTypeMasterDao, ICommonConstants {

	final static Logger logger = Logger.getLogger(SterilizationTypeMasterDaoImpl.class);


	public SterilizationTypeMasterDaoImpl() {
		super(SterilizationTypeMaster.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private DozerBeanMapper mapper;

	@Override
	public Response getSterilizationTypeList(Integer organizationId,Integer unitId,Integer offset,Integer noOfRecordsPerPage) throws ApplicationException {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<SterilizationTypeMasterDto> MasterDtoList = statelessSession
					.getNamedQuery("GET_STERILIZATION_TYPE_LIST")
					.setParameter("organizationId",organizationId)
					.setParameter("unitId",unitId)
					.setFirstResult(offset != null ?offset : 0)
					.setMaxResults(noOfRecordsPerPage != null ? noOfRecordsPerPage : 10)
					.setResultTransformer(Transformers.aliasToBean(SterilizationTypeMasterDto.class)).list();
			statelessSession.close();
			return new Response(SUCCESS, null, null, MasterDtoList, null);
		} catch (HibernateException he) {
			logger.error("getSterilizationTypeList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getSterilizationTypeList failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
			
		}
	}
	
	@Override
	public Response getActiveSterilizationTypeList(Integer organizationId,Integer unitId) throws ApplicationException {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<SterilizationTypeMasterDto> MasterDtoList = statelessSession
					.getNamedQuery("GET_ACTIVE_STERILIZATION_TYPE_LIST")
					.setParameter("organizationId",organizationId)
					.setParameter("unitId",unitId)
					.setResultTransformer(Transformers.aliasToBean(SterilizationTypeMasterDto.class)).list();
			statelessSession.close();
			return new Response(SUCCESS, null, null, MasterDtoList, null);
		} catch (HibernateException he) {
			logger.error("getActiveSterilizationTypeList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getActiveSterilizationTypeList failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
			
		}
	}

	@Override
	public Response updateSterilizationType(SterilizationTypeMasterDto masterDto) {
		try {
			
			Session session = sessionFactory.getCurrentSession();
			SterilizationTypeMaster sterilizationTypeMaster = (SterilizationTypeMaster)session.get(SterilizationTypeMaster.class, masterDto.getId());
			
			sterilizationTypeMaster.setCode(masterDto.getCode());
			sterilizationTypeMaster.setName(masterDto.getName());
			sterilizationTypeMaster.setUpdated_date(TimeZoneComponent.getDateByzone(masterDto.getUnitId()!=null ? masterDto.getUnitId() :0));
			sterilizationTypeMaster.setUpdated_by(masterDto.getUpdated_by());
			sterilizationTypeMaster = update(sterilizationTypeMaster);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (Exception e) {
			logger.error("updateSterilizationType failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response updateSterilizationTypeStatus(SterilizationTypeMasterDto masterDto) {
		try {
			
			Session session = sessionFactory.getCurrentSession();
			SterilizationTypeMaster sterilizationTypeMaster =(SterilizationTypeMaster) session.get(SterilizationTypeMaster.class, masterDto.getId());
			sterilizationTypeMaster.setUpdated_date(TimeZoneComponent.getDateByzone(masterDto.getUnitId()!=null ? masterDto.getUnitId() :0));
			sterilizationTypeMaster.setUpdated_by(masterDto.getUpdated_by());
			sterilizationTypeMaster.setStatus(masterDto.getStatus());
			
			
			sterilizationTypeMaster = update(sterilizationTypeMaster);
			
			if(masterDto.getStatus()=='A')
			{
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			}
			else
			{
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_DELETE, null, null);
			}
			
			
		} catch (Exception e) {
			logger.error("updateSterilizationTypeStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response createSterilizationType(SterilizationTypeMasterDto sterilizationTypeMasterDto)
			throws ApplicationException {
		try {
			SterilizationTypeMaster sterilizationTypeMaster = mapper.map(sterilizationTypeMasterDto,
					SterilizationTypeMaster.class, "SterilizationTypeMasterDto_to_SterilizationTypeMaster");

			sterilizationTypeMaster.setCreated_date(TimeZoneComponent.getDateByzone(sterilizationTypeMasterDto.getUnitId()!=null ? sterilizationTypeMasterDto.getUnitId() :0));
			sterilizationTypeMaster.setUpdated_date(TimeZoneComponent.getDateByzone(sterilizationTypeMasterDto.getUnitId()!=null ? sterilizationTypeMasterDto.getUnitId() :0));
			//sterilizationTypeMaster.setIs_active(true);
			//sterilizationTypeMaster.setIs_deleted(false);
			sterilizationTypeMaster.setStatus('A');

			sterilizationTypeMaster = save(sterilizationTypeMaster);
			return new Response(SUCCESS, "201", "Created Successfully.", null, null);
		} catch (HibernateException he) {
			logger.error("createSterilizationType failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("createSterilizationType failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
		}
	}

	@Override
	public Response getSterilizationTypesListCount(SterilizationTypeMasterDto sterilizationTypeMasterDto) {
		try {
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(SterilizationTypeMaster.class); 
			c.add(Restrictions.eq("organizationId", sterilizationTypeMasterDto.getOrganizationId()));
			c.setProjection(Projections.rowCount());  
			Long count = (Long)c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);

		} catch (HibernateException he) {
			logger.error("getSterilizationTypesListCount failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getSterilizationTypesListCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	

}