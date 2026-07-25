package com.param.CSSDMaster.dao;

import java.util.Date;
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

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.utility.TimeZoneComponent;
import com.param.CSSDMaster.dto.WrapperExpiryMasterDto;
import com.param.CSSDMaster.model.AutoclaveMaster;
import com.param.CSSDMaster.model.WrapperExpiryMaster;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class WrapperExpiryMasterDaoImpl extends GenericDao<WrapperExpiryMaster, Integer>
		implements IWrapperExpiryMasterDao, ICommonConstants {

	final static Logger logger = Logger.getLogger(WrapperExpiryMasterDaoImpl.class);


	public WrapperExpiryMasterDaoImpl() {
		super(WrapperExpiryMaster.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private DozerBeanMapper mapper;

	@Override
	public Response getWrapperExpiryList(Integer organizationId,Integer unitId,Integer offset,Integer noOfRecordsPerPage) throws ApplicationException {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<WrapperExpiryMasterDto> MasterDtoList = statelessSession.getNamedQuery("GET_WRAPPER_EXPIRY_LIST")
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setFirstResult(offset != null ?offset : 0)
					.setMaxResults(noOfRecordsPerPage != null ? noOfRecordsPerPage : 10)
					.setResultTransformer(Transformers.aliasToBean(WrapperExpiryMasterDto.class)).list();
			statelessSession.close();
			return new Response(SUCCESS, null, null, MasterDtoList, null);
		} catch (HibernateException he) {
			logger.error("getWrapperExpiryList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getWrapperExpiryList failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
			
		}
	}
	
	@Override
	public Response getItemList(Integer organizationId,Integer unitId) throws ApplicationException {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<WrapperExpiryMasterDto> MasterDtoList = statelessSession.getNamedQuery("GET_WRAPPER_ITEM_LIST")
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(WrapperExpiryMasterDto.class)).list();
			statelessSession.close();
			return new Response(SUCCESS, null, null, MasterDtoList, null);
		} catch (HibernateException he) {
			logger.error("getItemList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getItemList failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
			
		}
	}
	
	@Override
	public Response getActiveWrapperExpiryList(Integer organizationId,Integer unitId) throws ApplicationException {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<WrapperExpiryMasterDto> MasterDtoList = statelessSession.getNamedQuery("GET_ACTIVE_WRAPPER_EXPIRY_LIST")
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(WrapperExpiryMasterDto.class)).list();
			statelessSession.close();
			return new Response(SUCCESS, null, null, MasterDtoList, null);
		} catch (HibernateException he) {
			logger.error("getActiveWrapperExpiryList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getActiveWrapperExpiryList failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
			
		}
	}
	
	@Override
	public Response getActiveWrapperExpiryByCode(Integer organizationId,Integer unitId,String code) throws ApplicationException {
		try {
			Session statelessSession = sessionFactory.getCurrentSession();

			List<WrapperExpiryMasterDto> MasterDtoList = statelessSession.getNamedQuery("GET_ACTIVE_WRAPPER_EXPIRY_LIST_BY_CODE")
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setParameter("code", "%"+code.toLowerCase()+"%")
					.setResultTransformer(Transformers.aliasToBean(WrapperExpiryMasterDto.class)).list();
			return new Response(SUCCESS, null, null, MasterDtoList, null);
		} catch (HibernateException he) {
			logger.error("getActiveWrapperExpiryByCode failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getActiveWrapperExpiryByCode failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
		}
	}
	
	
	

	@Override
	public Response updateWrapperExpiry(WrapperExpiryMasterDto masterDto) {
		try {
			Session session=sessionFactory.getCurrentSession();
			
			WrapperExpiryMaster wrapperExpiryMaster = (WrapperExpiryMaster)session.get(WrapperExpiryMaster.class, masterDto.getId());
					
			wrapperExpiryMaster.setCode(masterDto.getCode());
			wrapperExpiryMaster.setName(masterDto.getName());
			wrapperExpiryMaster.setItemId(masterDto.getItemId());
			wrapperExpiryMaster.setDuration(masterDto.getDuration());
			wrapperExpiryMaster.setUpdated_date(TimeZoneComponent.getDateByzone(masterDto.getUnitId()!=null ? masterDto.getUnitId() :0));
			wrapperExpiryMaster.setUpdated_by(masterDto.getUpdated_by());
			wrapperExpiryMaster = update(wrapperExpiryMaster);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (Exception e) {
			logger.error("updateWrapperExpiry failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response updateWrapperExpiryStatus(WrapperExpiryMasterDto masterDto) {
		try {
			Session session=sessionFactory.getCurrentSession();
			
			WrapperExpiryMaster wrapperExpiryMaster = (WrapperExpiryMaster)session.get(WrapperExpiryMaster.class, masterDto.getId());
					
			wrapperExpiryMaster.setStatus(masterDto.getStatus());
			wrapperExpiryMaster.setUpdated_date(TimeZoneComponent.getDateByzone(masterDto.getUnitId()!=null ? masterDto.getUnitId() :0));
			wrapperExpiryMaster.setUpdated_by(masterDto.getUpdated_by());
			wrapperExpiryMaster = update(wrapperExpiryMaster);
			if(masterDto.getStatus()=='A')
			{
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			}
			else
			{
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_DELETE, null, null);
			}
			
			
		} catch (Exception e) {
			logger.error("updateWrapperExpiryStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response createWrapperExpiry(WrapperExpiryMasterDto wrapperExpiryMasterDto) throws ApplicationException {
		try {
			
			WrapperExpiryMaster wrapperExpiryMaster = new  WrapperExpiryMaster();
			wrapperExpiryMaster.setCode(wrapperExpiryMasterDto.getCode());
			wrapperExpiryMaster.setName(wrapperExpiryMasterDto.getName());
			wrapperExpiryMaster.setDuration(wrapperExpiryMasterDto.getDuration());
			wrapperExpiryMaster.setOrganizationId(wrapperExpiryMasterDto.getOrganizationId());
			wrapperExpiryMaster.setUnitId(wrapperExpiryMasterDto.getUnitId());
			wrapperExpiryMaster.setIs_active(true);
			wrapperExpiryMaster.setIs_deleted(false);
			wrapperExpiryMaster.setCreated_by(wrapperExpiryMasterDto.getCreated_by());
			wrapperExpiryMaster.setUpdated_by(wrapperExpiryMasterDto.getUpdated_by());
			wrapperExpiryMaster.setCreated_date(TimeZoneComponent.getDateByzone(wrapperExpiryMasterDto.getUnitId()));
			wrapperExpiryMaster.setUpdated_date(TimeZoneComponent.getDateByzone(wrapperExpiryMasterDto.getUnitId()));
			wrapperExpiryMaster.setStatus('A');
			wrapperExpiryMaster = save(wrapperExpiryMaster);
			return new Response(SUCCESS, "201", "Created Successfully", null, null);
		} catch (HibernateException he) {
			logger.error("createWrapperExpiry failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("createWrapperExpiry failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
		}
	}

	@Override
	public Response getWrapperExpiryByNameOrCode(WrapperExpiryMasterDto masterDto) {
		try {
			Integer id = (Integer) sessionFactory.getCurrentSession().getNamedQuery("GET_WRAPPER_EXPIRY_BY_NAME_OR_CODE")
					.setParameter("name", masterDto.getName().toLowerCase())
					.setParameter("code", masterDto.getCode().toLowerCase())
					.setParameter("organizationId", masterDto.getOrganizationId())
					.setParameter("unitId", masterDto.getUnitId())
					.uniqueResult();
			return new Response(SUCCESS, null, null, null, id);
		} catch (HibernateException he) {
			logger.error("getWrapperExpiryByNameOrCode failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getWrapperExpiryByNameOrCode failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getWrapperExpiryByNameOrCodeNotById(WrapperExpiryMasterDto masterDto) {
		try {
			Integer id = (Integer) sessionFactory.getCurrentSession().getNamedQuery("GET_WRAPPER_EXPIRY_BY_NAME_OR_CODE_NOT_ID")
					.setParameter("name", masterDto.getName().toLowerCase())
					.setParameter("code", masterDto.getCode().toLowerCase())
					.setParameter("organizationId", masterDto.getOrganizationId())
					.setParameter("unitId", masterDto.getUnitId())
					.setParameter("id", masterDto.getId())
					.uniqueResult();
			return new Response(SUCCESS, null, null, null, id);
		} catch (HibernateException he) {
			logger.error("getWrapperExpiryByNameOrCodeNotById failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getWrapperExpiryByNameOrCodeNotById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getWrapperListcount(WrapperExpiryMasterDto wrapperExpiryMasterDto) {
		try {
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(WrapperExpiryMaster.class); 
			c.add(Restrictions.eq("organizationId", wrapperExpiryMasterDto.getOrganizationId()));
			c.setProjection(Projections.rowCount());  
			Long count = (Long)c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);

		} catch (HibernateException he) {
			logger.error("getWrapperListcount failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getWrapperListcount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}
