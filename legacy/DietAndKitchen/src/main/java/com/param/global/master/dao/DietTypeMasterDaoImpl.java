package com.param.global.master.dao;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.master.dto.DietTypeMasterDto;
import com.param.global.model.CabinMaster;
import com.param.global.unit.dto.CabinMasterDto;
import com.param.opd.coversheet.model.DietTypeMaster;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DietTypeMasterDaoImpl extends GenericDao<DietTypeMaster, Integer>
		implements IDietTypeMasterDao, ICommonConstants {

	private static final Logger logger = Logger.getLogger(DietTypeMasterDaoImpl.class);

	public DietTypeMasterDaoImpl() {
		super(DietTypeMaster.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	DozerBeanMapper mapper;

	@Override
	public Response getDietTypeByName(DietTypeMasterDto dietTypeMasterDto) throws ApplicationException {
		try {

			List<DietTypeMasterDto> dietTypeMasterList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DIET_TYPE_BY_NAME")
					.setParameter("dietName", dietTypeMasterDto.getDietTypeCode().toLowerCase())
					.setParameter("orgId", dietTypeMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(DietTypeMasterDto.class)).list();
			return new Response(SUCCESS, null, null, dietTypeMasterList, null);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response saveDietTypeMaster(DietTypeMasterDto dietTypeMasterDto) throws ApplicationException {
		try {
			DietTypeMaster dietTypeMaster = mapper.map(dietTypeMasterDto, DietTypeMaster.class,
					"DietTypeMasterDto_to_DietTypeMaster");
			dietTypeMaster.setCreatedDate(TimeZoneComponent.getDateByzone(dietTypeMasterDto.getUnitId()!=null ? dietTypeMasterDto.getUnitId() :0));
			dietTypeMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(dietTypeMasterDto.getUnitId()!=null ? dietTypeMasterDto.getUnitId() :0));
			Session session = sessionFactory.getCurrentSession();
			session.save(dietTypeMaster);
			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDietTypeMasterById(DietTypeMasterDto dietTypeMasterDto) throws ApplicationException {
		try {
			List<DietTypeMasterDto> dietTypeMasterListById = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DIET_TYPE_MASTER_BY_ID")
					.setParameter("dietTypeId", dietTypeMasterDto.getDietTypeId())
					.setResultTransformer(Transformers.aliasToBean(DietTypeMasterDto.class)).list();
			return new Response(SUCCESS, null, null, dietTypeMasterListById, null);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateDietTypeMaster(DietTypeMasterDto dietTypeMasterDto) throws ApplicationException {
		try {
			DietTypeMaster dietTypeMaster = findById(dietTypeMasterDto.getDietTypeId());
			dietTypeMaster.setDietTypeCode(dietTypeMasterDto.getDietTypeCode());
			dietTypeMaster.setDietTypeDescription(dietTypeMasterDto.getDietTypeDescription());
			dietTypeMaster.setUpdatedBy(dietTypeMasterDto.getUpdatedBy());
			dietTypeMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(dietTypeMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			Session session = sessionFactory.getCurrentSession();
			session.update(dietTypeMaster);
			return new Response(SUCCESS, null, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateDietTypeMasterStatus(DietTypeMasterDto dietTypeMasterDto) throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			DietTypeMaster dietTypeMaster = (DietTypeMaster) session.load(DietTypeMaster.class,
					dietTypeMasterDto.getDietTypeId());
			dietTypeMaster.setStatus(dietTypeMasterDto.getStatus());
			dietTypeMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(dietTypeMasterDto.getUnitId()!= null ? dietTypeMasterDto.getUnitId() :0));

			session.update(dietTypeMaster);
			if (dietTypeMasterDto.getStatus() == 'A') {
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			} else {
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_DELETE, null, null);
			}
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDietTypeMasterList(DietTypeMasterDto dietTypeMasterDto) throws ApplicationException {
		try {
			List<DietTypeMasterDto> dietTypeMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DIETTYPEMASTER_LIST_BY_ORGANIZATION_ID")
					.setParameter("orgId", dietTypeMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(DietTypeMasterDto.class)).list();
			return new Response(SUCCESS, null, null, dietTypeMasterDtosList, null);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getAllDietTypeMasterList(Integer organizationId,Integer unitId) throws ApplicationException {
		try {
			List<DietTypeMasterDto> dietTypeMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ALL_DIET_TYPE_MASTER_LIST")
					.setParameter("organizationId", organizationId)
					//.setParameter("unitId", unitId) //Dilip
					.setResultTransformer(Transformers.aliasToBean(DietTypeMasterDto.class)).list();
			return new Response(SUCCESS, null, null, dietTypeMasterDtosList, null);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getActiveDietTypeMasterList(Integer organizationId,Integer unitId) throws ApplicationException {
		try {
			List<DietTypeMasterDto> dietTypeMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_DIET_TYPE_MASTER_LIST")
					.setParameter("organizationId", organizationId)
					//.setParameter("unitId", unitId) //dilip
					.setResultTransformer(Transformers.aliasToBean(DietTypeMasterDto.class)).list();
			return new Response(SUCCESS, null, null, dietTypeMasterDtosList, null);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDietTypeMasterPaginationList(DietTypeMasterDto dietTypeMasterDto) throws ApplicationException {
		try {
			List<DietTypeMasterDto> dietTypeMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ALL_DIET_TYPE_MASTER_LIST")
					.setParameter("organizationId", dietTypeMasterDto.getOrganizationId())
					.setFirstResult(dietTypeMasterDto.getOffset() != null ? dietTypeMasterDto.getOffset()  : 0)
					.setMaxResults(dietTypeMasterDto.getNoOfRecordsPerPage() != null ? dietTypeMasterDto.getNoOfRecordsPerPage()  : 10)
					.setResultTransformer(Transformers.aliasToBean(DietTypeMasterDto.class)).list();
			return new Response(SUCCESS, null, null, dietTypeMasterDtosList, null);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDietTypeMasterPaginationListCount(DietTypeMasterDto dietTypeMasterDto)
			throws ApplicationException {
		try {
			
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(DietTypeMaster.class);
			crit.add(Restrictions.eq("organizationId", dietTypeMasterDto.getOrganizationId()));
			crit.add(Restrictions.eq("unitId", dietTypeMasterDto.getUnitId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null, count);

		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}
