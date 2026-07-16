package com.param.global.dao;

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
import com.param.global.dto.DistrictMasterDto;
import com.param.global.dto.StateMasterDto;
import com.param.global.model.CountryMaster;
import com.param.global.model.DistrictMaster;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Repository
public class DistrictMasterDaoImpl extends GenericDao<DistrictMaster, Integer>
		implements IDistrictMasterDao, ICommonConstants {

	static final Logger logger = Logger.getLogger(DistrictMasterDaoImpl.class);

	public DistrictMasterDaoImpl() {
		super(DistrictMaster.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private DozerBeanMapper mapper;

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Response getDistrictByName(DistrictMasterDto districtMasterDto) throws ApplicationException {
		try {
			List<DistrictMaster> districtMastersList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DISTRICT_LIST_BY_NAME")
					.setParameter("districtName", districtMasterDto.getDistrictName().toLowerCase())
					.setParameter("districtCode", districtMasterDto.getDistrictCode().toLowerCase())
					.setParameter("stateId", districtMasterDto.getStateId())
					.setParameter("countryId", districtMasterDto.getCountryId())
					.setResultTransformer(Transformers.aliasToBean(DistrictMasterDto.class)).list();
			return new Response(SUCCESS, null, null, districtMastersList, null);

		} catch (HibernateException he) {
			logger.error("getDistrictByName failed", he);
		} catch (Exception e) {
			logger.error("getDistrictByName failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response addDistrictMaster(DistrictMasterDto districtMasterDto) throws ApplicationException {
		try {
			DistrictMaster districtMaster = mapper.map(districtMasterDto, DistrictMaster.class,
					"DistrictMasterDto_to_DistrictMaster");
			districtMaster.setStatus('A');
			districtMaster = save(districtMaster);
			return new Response(SUCCESS, null, null, null, districtMaster);
		} catch (HibernateException he) {
			logger.error("addDistrictMaster failed", he);
		} catch (Exception e) {
			logger.error("addDistrictMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getDistrictMasterList(DistrictMasterDto districtMasterDto) throws ApplicationException {
		try {
			List<DistrictMaster> districtMastersList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DISTRICT_LIST")
					.setParameter("orgId", districtMasterDto.getOrganizationId())
					.setFirstResult(districtMasterDto.getOffset() != null ? districtMasterDto.getOffset() : 0)
					.setMaxResults(districtMasterDto.getNoOfRecordsPerPage() != null ? districtMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(DistrictMasterDto.class)).list();
			return new Response(SUCCESS, null, null, districtMastersList, null);

		} catch (HibernateException he) {
			logger.error("getDistrictMasterList failed", he);
		} catch (Exception e) {
			logger.error("getDistrictMasterList failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getDistrictByNameNotId(DistrictMasterDto districtMasterDto) throws ApplicationException {
		try {
			List<DistrictMaster> districtMastersList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DISTRICT_LIST_BY_NAME_NOT_ID")
					.setParameter("districtName", districtMasterDto.getDistrictName().toLowerCase())
					.setParameter("districtId", districtMasterDto.getDistrictId())
					.setParameter("districtCode", districtMasterDto.getDistrictCode().toLowerCase())
					.setParameter("stateId", districtMasterDto.getStateId())
					.setParameter("countryId", districtMasterDto.getCountryId())
					.setParameter("orgId", districtMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(DistrictMasterDto.class)).list();
			return new Response(SUCCESS, null, null, districtMastersList, null);

		} catch (HibernateException he) {
			logger.error("getDistrictByNameNotId failed", he);
		} catch (Exception e) {
			logger.error("getDistrictByNameNotId failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateDistrictMaster(DistrictMasterDto districtMasterDto) throws ApplicationException {
		try {
			DistrictMaster districtMaster = findById(districtMasterDto.getDistrictId());
			districtMaster.setDistrictName(districtMasterDto.getDistrictName());
			districtMaster.setDistrictCode(districtMasterDto.getDistrictCode());
			districtMaster.setCountryId(districtMasterDto.getCountryId());
			districtMaster.setStateId(districtMasterDto.getStateId());
			districtMaster.setUpdatedBy(districtMasterDto.getUpdatedBy());
			districtMaster
					.setUpdatedDate(GlobalCommonDateUtils.getDate(districtMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(districtMaster);
			return new Response<StateMasterDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, districtMaster);

		} catch (HibernateException he) {
			logger.error("updateDistrictMaster failed", he);
			return new Response<StateMasterDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
			logger.error("updateDistrictMaster failed", e);
			return new Response<StateMasterDto, Integer>(ERROR, null, null, null, null);
		} finally {
		}
	}

	@Override
	public Response getDistrictById(DistrictMasterDto districtMasterDto) throws ApplicationException {
		try {
			List<DistrictMaster> districtMastersList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DISTRICT_LIST_BY_ID")
					.setParameter("districtId", districtMasterDto.getDistrictId())
					.setParameter("orgId", districtMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(DistrictMasterDto.class)).list();
			return new Response(SUCCESS, null, null, districtMastersList, null);

		} catch (HibernateException he) {
			logger.error("getDistrictById failed", he);
		} catch (Exception e) {
			logger.error("getDistrictById failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateDistrictStatus(DistrictMasterDto districtMasterDto) throws ApplicationException {
		try {

			DistrictMaster districtMaster = findById(districtMasterDto.getDistrictId());
			districtMaster.setStatus(districtMasterDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			districtMaster.setUpdatedBy(districtMasterDto.getUpdatedBy());
			districtMaster
					.setUpdatedDate(GlobalCommonDateUtils.getDate(districtMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(districtMaster);
			return new Response<StateMasterDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, districtMaster.getStateId());

		} catch (HibernateException he) {
			logger.error("updateDistrictStatus failed", he);
			return new Response<StateMasterDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
			logger.error("updateDistrictStatus failed", e);
			return new Response<StateMasterDto, Integer>(ERROR, null, null, null, null);
		} finally {
		}
	}

	@Override
	public Response getActiveDistrictList(DistrictMasterDto districtMasterDto) throws ApplicationException {
		try {
			List<DistrictMaster> districtMastersList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_DISTRICT_LIST")
					.setParameter("orgId", districtMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(DistrictMasterDto.class)).list();
			return new Response(SUCCESS, null, null, districtMastersList, null);

		} catch (HibernateException he) {
			logger.error("getActiveDistrictList failed", he);
		} catch (Exception e) {
			logger.error("getActiveDistrictList failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveStateListByCountryId(StateMasterDto stateMaster) throws ApplicationException {
		try {
			List<CountryMaster> countryMastersList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_STATE_LIST_BY_COUNTRY_ID").setParameter("countryId", stateMaster.getCountryId())
					.setResultTransformer(Transformers.aliasToBean(StateMasterDto.class)).list();

			return new Response(SUCCESS, null, null, countryMastersList, null);

		} catch (HibernateException he) {
			logger.error("getActiveStateListByCountryId failed", he);
		} catch (Exception e) {
			logger.error("getActiveStateListByCountryId failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getCount(DistrictMasterDto districtMasterDto) throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			Criteria c = session.createCriteria(DistrictMaster.class);
//			c.add(Restrictions.eq("organizationId", districtMasterDto.getOrganizationId()));
			c.add(Restrictions.disjunction()
					.add(Restrictions.eq("organizationId",districtMasterDto.getOrganizationId()))
					.add(Restrictions.isNull("organizationId")));
			c.setProjection(Projections.rowCount());
			Long count = (Long) c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		} catch (Exception e) {
			logger.error("getCount failed", e);
			return new Response(ERROR, null, null, null, null);
		}
	}

	@Override
	public Response getDistrictMasterListByCountryId(DistrictMasterDto districtMasterDto) throws ApplicationException {
		try {
			
			String query = "select mdm.district_id as\"districtId\",mdm.district_name as \"districtName\" "
					  + " from public.m_country_master mcm "
					  + " inner join public.m_state_master msm on msm.country_id  = mcm.country_id and msm.organization_id =:orgId and msm.status ='A' "
					  + " inner join public.m_district_master mdm  on mdm.state_id = msm.state_id  and mdm.organization_id =:orgId and mdm.status ='A' "
					  + " where mcm.organization_id =:orgId and mcm.status ='A' and mcm.country_id = :countryId ";
			
			List<DistrictMasterDto> districtList = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("orgId", districtMasterDto.getOrganizationId())
					.setParameter("countryId", districtMasterDto.getCountryId())
					.setResultTransformer(Transformers.aliasToBean(DistrictMasterDto.class)).list();
			
			return new Response(SUCCESS, null, null, districtList, null);

		} catch (HibernateException he) {
			logger.error("getDistrictMasterListByCountryId failed", he);
		} catch (Exception e) {
			logger.error("getDistrictMasterListByCountryId failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

}
