package com.param.global.dao;

import java.util.List;

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

import org.apache.log4j.Logger;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.CityMasterDto;
import com.param.global.dto.DistrictMasterDto;
import com.param.global.dto.StateMasterDto;
import com.param.global.model.CityMaster;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings({"rawtypes","unchecked"})
@Repository
public class CityMasterDaoImpl extends GenericDao<CityMaster, Integer> implements ICityMasterDao,ICommonConstants
{
	private static final Logger logger = Logger.getLogger(CityMasterDaoImpl.class);

	@Autowired
	private DozerBeanMapper mapper;

	@Autowired
	private SessionFactory sessionFactory;


	public CityMasterDaoImpl() {
		super(CityMaster.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Response getCityByName(CityMasterDto cityMasterDto) throws ApplicationException {
		try {
			List<CityMasterDto> cityMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CITY_LIST_BY_NAME")
					.setParameter("cityName", cityMasterDto.getCityName().toLowerCase())
					.setParameter("countryId", cityMasterDto.getCountryId())
					.setParameter("stateId", cityMasterDto.getStateId())
					.setParameter("districtId", cityMasterDto.getDistrictId())
					.setParameter("orgId", cityMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(CityMasterDto.class)).list();
			return new Response(SUCCESS, null, null, cityMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error("getCityByName failed", he);
		} catch (Exception e) {
			logger.error("getCityByName failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getCityByNameNotId(CityMasterDto cityMasterDto) throws ApplicationException {
		try {
			List<CityMasterDto> cityMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CITY_LIST_BY_NAME_NOT_ID")
					.setParameter("cityId", cityMasterDto.getCityId())
					.setParameter("cityName", cityMasterDto.getCityName().toLowerCase())
					.setParameter("countryId", cityMasterDto.getCountryId())
					.setParameter("stateId", cityMasterDto.getStateId())
					.setParameter("districtId", cityMasterDto.getDistrictId())
					.setParameter("orgId", cityMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(CityMasterDto.class)).list();
			return new Response(SUCCESS, null, null, cityMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error("getCityByNameNotId failed", he);
		} catch (Exception e) {
			logger.error("getCityByNameNotId failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getCityByID(CityMasterDto cityMasterDto) throws ApplicationException {
		try {
			List<CityMasterDto> cityMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CITY_LIST_BY_ID")
					.setParameter("cityId", cityMasterDto.getCityId())
					.setResultTransformer(Transformers.aliasToBean(CityMasterDto.class)).list();
			return new Response(SUCCESS, null, null, cityMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error("getCityByID failed", he);
		} catch (Exception e) {
			logger.error("getCityByID failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getCityMasterList(CityMasterDto cityMasterDto) throws ApplicationException {
		try {
			List<CityMasterDto> cityMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CITY_LIST")
					.setParameter("orgId", cityMasterDto.getOrganizationId())
					.setFirstResult(cityMasterDto.getOffset() != null ? cityMasterDto.getOffset() : 0)
					.setMaxResults(cityMasterDto.getNoOfRecordsPerPage() != null ? cityMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(CityMasterDto.class)).list();
			return new Response(SUCCESS, null, null, cityMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error("getCityMasterList failed", he);
		} catch (Exception e) {
			logger.error("getCityMasterList failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveCityList() throws ApplicationException {
		try {
			List<CityMasterDto> cityMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_CITY_LIST")
					.setResultTransformer(Transformers.aliasToBean(CityMasterDto.class)).list();
			return new Response(SUCCESS, null, null, cityMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error("getActiveCityList failed", he);
		} catch (Exception e) {
			logger.error("getActiveCityList failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveDistrictListByStateId(DistrictMasterDto districtMasterDto) throws ApplicationException {
		try {
			List<StateMasterDto> stateMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_DISTRICT_LIST_BY_STATE_ID")
					.setParameter("stateId", districtMasterDto.getStateId())
					.setResultTransformer(Transformers.aliasToBean(DistrictMasterDto.class)).list();
			return new Response(SUCCESS, null, null, stateMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error("getActiveDistrictListByStateId failed", he);
		} catch (Exception e) {
			logger.error("getActiveDistrictListByStateId failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateCityStatus(CityMasterDto cityMasterDto) throws ApplicationException {
		try {

			CityMaster cityMaster = findById(cityMasterDto.getCityId());
			cityMaster.setStatus(cityMasterDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			cityMaster.setUpdatedBy(cityMasterDto.getUpdatedBy());
			cityMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(cityMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(cityMaster);
			return new Response<CityMasterDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, null);

		} catch (HibernateException he) {
			logger.error("updateCityStatus failed", he);
			return new Response<CityMasterDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
			logger.error("updateCityStatus failed", e);
			return new Response<CityMasterDto, Integer>(ERROR, null, null, null, null);
		} finally {
		}
	}

	@Override
	public Response addCityMaster(CityMasterDto cityMasterDto) throws ApplicationException {
		try {
			CityMaster cityMaster = mapper.map(cityMasterDto, CityMaster.class, "CityMasterDto_to_CityMaster");
			cityMaster.setStatus('A');
			cityMaster = save(cityMaster);
			return new Response(SUCCESS, null, null, null, cityMaster);
		} catch (HibernateException he) {
			logger.error("addCityMaster failed", he);
		} catch (Exception e) {
			logger.error("addCityMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateCityMaster(CityMasterDto cityMasterDto) throws ApplicationException {
		try {
			CityMaster cityMaster = findById(cityMasterDto.getCityId());
			cityMaster.setCityCode(cityMasterDto.getCityCode());
			cityMaster.setCityName(cityMasterDto.getCityName());
			cityMaster.setCountryId(cityMasterDto.getCountryId());
			cityMaster.setDistrictId(cityMasterDto.getDistrictId());
			cityMaster.setStateId(cityMasterDto.getStateId());
			cityMaster.setUpdatedBy(cityMasterDto.getUpdatedBy());
			cityMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(cityMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(cityMaster);
			return new Response<CityMasterDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, cityMaster);

		} catch (HibernateException he) {
			logger.error("updateCityMaster failed", he);
			return new Response<CityMasterDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
			logger.error("updateCityMaster failed", e);
			return new Response<CityMasterDto, Integer>(ERROR, null, null, null, null);
		} finally {
		}
	}

	@Override
	public Response getCount(CityMasterDto cityMasterDto)
	{
		try{
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(CityMaster.class);
//			c.add(Restrictions.eq("organizationId", cityMasterDto.getOrganizationId()));
			c.add(Restrictions.disjunction()
					.add(Restrictions.eq("organizationId",cityMasterDto.getOrganizationId()))
					.add(Restrictions.isNull("organizationId")));
			c.setProjection(Projections.rowCount());
			Long count = (Long)c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		}catch(Exception e)
		{
			logger.error("getCount failed", e);
			return new Response(ERROR, null, null, null, null);
		}
	}

	@Override
	public Response getCityByStateId(CityMasterDto cityMasterDto)throws ApplicationException {
		try {
			List<CityMasterDto> cityMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CITY_LIST_BY_STATE_ID")
					.setParameter("stateId", cityMasterDto.getStateId())
					.setResultTransformer(Transformers.aliasToBean(CityMasterDto.class)).list();
			return new Response(SUCCESS, null, null, cityMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error("getCityByStateId failed", he);
		} catch (Exception e) {
			logger.error("getCityByStateId failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}
}
