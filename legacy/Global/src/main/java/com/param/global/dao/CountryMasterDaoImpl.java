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

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.CountryMasterDto;
import com.param.global.model.CountryMaster;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CountryMasterDaoImpl extends GenericDao<CountryMaster, Integer>
		implements ICountryMasterDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(CountryMasterDaoImpl.class);


	@Autowired
	private DozerBeanMapper mapper;

	@Autowired
	private SessionFactory sessionFactory;

	public CountryMasterDaoImpl() {
		super(CountryMaster.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Response getCountryByName(CountryMasterDto countryMasterDto) throws ApplicationException {
		try {
			List<CountryMasterDto> nationalityMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_COUNTRY_LIST_BY_NAME")
					.setParameter("countryName", countryMasterDto.getCountryName().toLowerCase())
					.setParameter("orgId", countryMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(CountryMasterDto.class)).list();
			return new Response(SUCCESS, null, null, nationalityMasterDtoList, null);

		} catch (HibernateException he) {
		logger.error("getCountryByName failed", he);
		} catch (Exception e) {
		logger.error("getCountryByName failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response addCountryMaster(CountryMasterDto countryMasterDto) throws ApplicationException {
		try {
			CountryMaster countryMaster = mapper.map(countryMasterDto, CountryMaster.class,
					"countryMasterDto_to_countryMaster");
			countryMaster.setStatus('A');
			countryMaster = save(countryMaster);
			return new Response(SUCCESS, null, null, null, countryMaster);
		} catch (HibernateException he) {
		logger.error("addCountryMaster failed", he);
		} catch (Exception e) {
		logger.error("addCountryMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getCountryMasterList(CountryMasterDto countryMasterDto) throws ApplicationException {
		try {
			List<CountryMasterDto> nationalityMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_COUNTRY_LIST")
					.setParameter("orgId", countryMasterDto.getOrganizationId())
					.setFirstResult(countryMasterDto.getOffset() != null ? countryMasterDto.getOffset() : 0)
					.setMaxResults(countryMasterDto.getNoOfRecordsPerPage() != null ? countryMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(CountryMasterDto.class)).list();
			return new Response(SUCCESS, null, null, nationalityMasterDtoList, null);

		} catch (HibernateException he) {
		logger.error("getCountryMasterList failed", he);
		} catch (Exception e) {
		logger.error("getCountryMasterList failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getCountryByID(CountryMasterDto countryMasterDto) throws ApplicationException {
		try {
			List<CountryMasterDto> nationalityMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_COUNTRY_LIST_BY_ID")
					.setParameter("countryId", countryMasterDto.getCountryId())
					.setParameter("orgId", countryMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(CountryMasterDto.class)).list();
			return new Response(SUCCESS, null, null, nationalityMasterDtoList, null);

		} catch (HibernateException he) {
		logger.error("getCountryByID failed", he);
		} catch (Exception e) {
		logger.error("getCountryByID failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateCountryStatus(CountryMasterDto countryMasterDto) throws ApplicationException {
		try {

			CountryMaster countryMaster = findById(countryMasterDto.getCountryId());
			countryMaster.setUpdatedBy(countryMasterDto.getUpdatedBy());
			countryMaster.setStatus(countryMasterDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			countryMaster
					.setUpdatedDate(GlobalCommonDateUtils.getDate(countryMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(countryMaster);
			return new Response<CountryMasterDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, countryMaster.getOrganizationId());

		} catch (HibernateException he) {
		logger.error("updateCountryStatus failed", he);
			return new Response<CountryMasterDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
		logger.error("updateCountryStatus failed", e);
			return new Response<CountryMasterDto, Integer>(ERROR, null, null, null, null);
		} finally {
		}
	}

	@Override
	public Response updateCountryMaster(CountryMasterDto countryMasterDto) throws ApplicationException {
		try {
			CountryMaster countryMaster = findById(countryMasterDto.getCountryId());
			countryMaster.setCountryName(countryMasterDto.getCountryName());
			countryMaster.setCountryCode(countryMasterDto.getCountryCode());
			countryMaster.setCountryCallingCode(countryMasterDto.getCountryCallingCode());
			countryMaster.setUpdatedBy(countryMasterDto.getUpdatedBy());
			countryMaster.setCountryInitial(countryMasterDto.getCountryInitial());
			countryMaster.setContactDigit(countryMasterDto.getContactDigit());
			countryMaster
					.setUpdatedDate(GlobalCommonDateUtils.getDate(countryMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(countryMaster);
			return new Response<CountryMasterDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, countryMaster);

		} catch (HibernateException he) {
		logger.error("updateCountryMaster failed", he);
			return new Response<CountryMasterDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
		logger.error("updateCountryMaster failed", e);
			return new Response<CountryMasterDto, Integer>(ERROR, null, null, null, null);
		} finally {
		}
	}

	@Override
	public Response getCountryByNameNotId(CountryMasterDto countryMasterDto) throws ApplicationException {
		try {
			List<CountryMasterDto> nationalityMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_COUNTRY_LIST_BY_NAME_NOT_ID")
					.setParameter("countryId", countryMasterDto.getCountryId())
					.setParameter("countryName", countryMasterDto.getCountryName().toLowerCase())
					.setParameter("orgId", countryMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(CountryMasterDto.class)).list();
			return new Response(SUCCESS, null, null, nationalityMasterDtoList, null);

		} catch (HibernateException he) {
		logger.error("getCountryByNameNotId failed", he);
		} catch (Exception e) {
		logger.error("getCountryByNameNotId failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveCountryList(Integer orgId) throws ApplicationException {
		try {
			List<CountryMasterDto> nationalityMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_COUNTRY_LIST")
					.setParameter("orgId", orgId)
					.setResultTransformer(Transformers.aliasToBean(CountryMasterDto.class)).list();
			return new Response(SUCCESS, null, null, nationalityMasterDtoList, null);

		} catch (HibernateException he) {
		logger.error("getActiveCountryList failed", he);
		} catch (Exception e) {
		logger.error("getActiveCountryList failed", e);
		} finally {

		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getCount(CountryMasterDto countryMasterDto)
	{
		try{
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(CountryMaster.class); 
			c.add(Restrictions.disjunction()
					.add(Restrictions.eq("organizationId",countryMasterDto.getOrganizationId()))
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
	public Response getCountryMasterList() throws ApplicationException {
		try{
			List<CountryMasterDto> countryList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_COUNTRY_MASTER_LIST")
					.setResultTransformer(Transformers.aliasToBean(CountryMasterDto.class)).list();
			return new Response(SUCCESS, null, null, countryList, null);
			
			
		}catch(HibernateException he){
		logger.error("getCountryMasterList failed", he);
		}
		return null;
	}
}
