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

import com.param.adt.master.global.model.ReferralMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.AreaMasterDto;
import com.param.global.dto.ReferralMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings({"rawtypes","unchecked"})
@Repository
public class ReferralMasterDaoImpl extends GenericDao<ReferralMaster, Integer> implements IReferralMasterDao,ICommonConstants
{

	private static final Logger logger = Logger.getLogger(ReferralMasterDaoImpl.class);

	public ReferralMasterDaoImpl() {
		super(ReferralMaster.class);
		// TODO Auto-generated constructor stub
	}
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private DozerBeanMapper mapper;

	@Override
	public Response getReferralByName(ReferralMasterDto referralMasterDto) throws ApplicationException {
		try {
			List<ReferralMasterDto> referralMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_REFERRAL_MASTER_LIST_BY_NAME")
					.setParameter("desc", referralMasterDto.getReferralName().toLowerCase())
					.setParameter("orgId", referralMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ReferralMasterDto.class)).list();
			return new Response(SUCCESS, null, null, referralMasterDtosList, null);

		} catch (HibernateException he) {
			logger.error("getReferralByName failed", he);
		} catch (Exception e) {
			logger.error("getReferralByName failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response addReferralTypeTypeMaster(ReferralMasterDto referralMasterDto) throws ApplicationException {
		try {
			ReferralMaster referralMaster = mapper.map(referralMasterDto, ReferralMaster.class, "ReferralMasterDto_to_ReferralMaster");
			referralMaster.setCreatedDate(TimeZoneComponent.getDateByzone(0));
			referralMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(0));
			referralMaster.setId(referralMasterDto.getId());
			referralMaster.setStatus('A');
			referralMaster = save(referralMaster);
			referralMasterDto.setReferralId(referralMaster.getReferralId());
			return new Response(SUCCESS, null, null, null, referralMasterDto);
		} catch (HibernateException he) {
			logger.error("addReferralTypeTypeMaster failed", he);
		} catch (Exception e) {
			logger.error("addReferralTypeTypeMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getReferralMasterList(ReferralMasterDto referralMasterDto) throws ApplicationException {
		try {
			List<ReferralMasterDto> referralMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_REFERRAL_MASTER_LIST")
					.setFirstResult(referralMasterDto.getOffset() != null ? referralMasterDto.getOffset() : 0)
					.setMaxResults(referralMasterDto.getNoOfRecordsPerPage() != null ? referralMasterDto.getNoOfRecordsPerPage() : 10)
					.setParameter("orgId", referralMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ReferralMasterDto.class)).list();
			return new Response(SUCCESS, null, null, referralMasterDtosList, null);

		} catch (HibernateException he) {
			logger.error("getReferralMasterList failed", he);
		} catch (Exception e) {
			logger.error("getReferralMasterList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getReferralById(ReferralMasterDto referralMasterDto) throws ApplicationException {
		try {
			List<ReferralMasterDto> referralMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_REFERRAL_MASTER_LIST_BY_ID")
					.setParameter("referralId", referralMasterDto.getReferralId())
					.setResultTransformer(Transformers.aliasToBean(ReferralMasterDto.class)).list();
			return new Response(SUCCESS, null, null, referralMasterDtosList, null);

		} catch (HibernateException he) {
			logger.error("getReferralById failed", he);
		} catch (Exception e) {
			logger.error("getReferralById failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveReferralList() throws ApplicationException {
		try {
			List<ReferralMasterDto> referralMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_REFERRAL_MASTER_LIST")
					.setResultTransformer(Transformers.aliasToBean(ReferralMasterDto.class)).list();
			return new Response(SUCCESS, null, null, referralMasterDtosList, null);

		} catch (HibernateException he) {
			logger.error("getActiveReferralList failed", he);
		} catch (Exception e) {
			logger.error("getActiveReferralList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getReferralByNameNotId(ReferralMasterDto referralMasterDto) throws ApplicationException {
		try {
			List<ReferralMasterDto> referralMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_REFERRAL_MASTER_LIST_BY_NAME_NOT_ID")
					.setParameter("referralId", referralMasterDto.getReferralId())
					.setParameter("desc",referralMasterDto.getReferralName().toLowerCase())
					.setParameter("orgId", referralMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ReferralMasterDto.class)).list();
			return new Response(SUCCESS, null, null, referralMasterDtosList, null);

		} catch (HibernateException he) {
			logger.error("getReferralByNameNotId failed", he);
		} catch (Exception e) {
			logger.error("getReferralByNameNotId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateReferralMaster(ReferralMasterDto referralMasterDto) throws ApplicationException {
		try {
			ReferralMaster referralMaster = findById(referralMasterDto.getReferralId());

			referralMaster.setReferralName(referralMasterDto.getReferralName());
			referralMaster.setReferralCode(referralMasterDto.getReferralCode());
			referralMaster.setAreaId(referralMasterDto.getAreaId());
			referralMaster.setStateId(referralMasterDto.getStateId());
			referralMaster.setAreaId(referralMasterDto.getAreaId());
			referralMaster.setCityId(referralMasterDto.getCityId());
			referralMaster.setCountryId(referralMasterDto.getCountryId());
			referralMaster.setDistrictId(referralMasterDto.getDistrictId());
			referralMaster.setReferralAddress(referralMasterDto.getReferralAddress());
			referralMaster.setReferralContact(referralMasterDto.getReferralContact());
			referralMaster.setReferralContactPerson(referralMasterDto.getReferralContactPerson());
			referralMaster.setReferralEmail(referralMasterDto.getReferralEmail());
			referralMaster.setUpdatedBy(referralMasterDto.getUpdatedBy());
			update(referralMaster);
			return new Response<ReferralMaster, Integer>(SUCCESS, null, COMMON_SUCCESS, null, null);

		} catch (HibernateException he) {
			logger.error("updateReferralMaster failed", he);
			return new Response<ReferralMaster, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
			logger.error("updateReferralMaster failed", e);
			return new Response<ReferralMaster, Integer>(ERROR, null, null, null, null);
		}
	}

	@Override
	public Response updateReferralMasterStatus(ReferralMasterDto referralMasterDto) throws ApplicationException {
		try {
			ReferralMaster referralMaster = findById(referralMasterDto.getReferralId());
			referralMaster.setUpdatedBy(referralMasterDto.getUpdatedBy());
			referralMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(referralMasterDto.getUpdatedDate(),"dd-MM-yyyy HH:mm:ss"));
			referralMaster.setStatus(referralMasterDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			update(referralMaster);
			return new Response<ReferralMaster, Integer>(SUCCESS, null, COMMON_SUCCESS, null, null);

		} catch (HibernateException he) {
			logger.error("updateReferralMasterStatus failed", he);
			return new Response<ReferralMaster, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
			logger.error("updateReferralMasterStatus failed", e);
			return new Response<ReferralMaster, Integer>(ERROR, null, null, null, null);
		}
	}

	@Override
	public Response getAreaByCityId(AreaMasterDto areaMasterDto) throws ApplicationException {
		try {
			List<AreaMasterDto> areaMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_AREA_BY_CITY_ID")
					.setParameter("cityId", areaMasterDto.getCityId())
					.setResultTransformer(Transformers.aliasToBean(AreaMasterDto.class)).list();
			return new Response(SUCCESS, null, null, areaMasterDtosList, null);

		} catch (HibernateException he) {
			logger.error("getAreaByCityId failed", he);
		} catch (Exception e) {
			logger.error("getAreaByCityId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	public Response getCount(ReferralMasterDto referralMasterDto) throws ApplicationException {
		try{
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(ReferralMaster.class);
			c.add(Restrictions.eq("organizationId", referralMasterDto.getOrganizationId()));
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
	public Response getReferralListByReferralTypeId(ReferralMasterDto referralMasterDto) throws ApplicationException {
		try {
			List<ReferralMasterDto> referralMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_REFERRAL_LIST_BY_REFERRAL_TYPE_ID")
					.setParameter("referralTypeId", referralMasterDto.getReferralTypeId())
					.setParameter("organizationId", referralMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ReferralMasterDto.class)).list();
			return new Response(SUCCESS, null, null, referralMasterDtosList, null);

		} catch (HibernateException he) {
			logger.error("getReferralListByReferralTypeId failed", he);
		} catch (Exception e) {
			logger.error("getReferralListByReferralTypeId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getReferralListByReferralTypeIds(ReferralMasterDto referralMasterDto) {
		try {
			
			String query = "select "
					+" refr.referral_id as \"referralId\", "
					+ "refr.referral_type_id as \"referralTypeId\", "
					+" refr.referral_name as \"desc\" "
					+" from "
					+" adt.m_referral_master refr "
					+" where "
					+" refr.status = 'A' "
					+" and refr.organization_id =:organizationId "
					+" and refr.referral_type_id in (:referralTypeIds)";
			
			
			List<ReferralMasterDto> referralMasterDtosList = sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameterList("referralTypeIds", referralMasterDto.getReferalTypeIds())
					.setParameter("organizationId", referralMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ReferralMasterDto.class)).list();
			return new Response(SUCCESS, null, null, referralMasterDtosList, null);

		} catch (HibernateException he) {
			logger.error("getReferralListByReferralTypeIds failed", he);
		} catch (Exception e) {
			logger.error("getReferralListByReferralTypeIds failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getReferralListForTypeSearch(ReferralMasterDto referralMasterDto) {
		try {
			List<ReferralMasterDto> referralMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_REFERRAL_LIST_FOR_TYPE_SEARCH")
					.setParameter("referralTypeId", referralMasterDto.getReferralTypeId())
					.setParameter("referralName", "%"+referralMasterDto.getDesc().trim().toLowerCase() + "%")
					.setParameter("organizationId", referralMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ReferralMasterDto.class)).list();
			return new Response(SUCCESS, null, null, referralMasterDtosList, null);

		} catch (HibernateException he) {
			logger.error("getReferralListForTypeSearch failed", he);
		} catch (Exception e) {
			logger.error("getReferralListForTypeSearch failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getAreaByCityIdPagination(AreaMasterDto areaMasterDto) throws ApplicationException {
		List<AreaMasterDto> areaMasterDtosList = sessionFactory.getCurrentSession()
				.getNamedQuery("GET_ACTIVE_AREA_BY_CITY_ID")
				.setParameter("cityId", areaMasterDto.getCityId())
				.setFirstResult(areaMasterDto.getOffset() != null ? areaMasterDto.getOffset()  : 0)
				.setMaxResults(areaMasterDto.getNoOfRecordsPerPage() != null ? areaMasterDto.getNoOfRecordsPerPage()  : 10)
				.setResultTransformer(Transformers.aliasToBean(AreaMasterDto.class)).list();

    	return new Response(SUCCESS, SUCCESS_CODE, null, areaMasterDtosList, null);
	}

	@Override
	public Response getReferralListByReferralName(ReferralMasterDto referralMasterDto) throws ApplicationException {
		try {
			List<ReferralMasterDto> referralMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_REFERRAL_LIST_BY_REFERRAL_NAME")
					.setParameter("referralName", "%"+referralMasterDto.getDesc().trim().toLowerCase() + "%")
					.setParameter("organizationId", referralMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ReferralMasterDto.class)).list();
			return new Response(SUCCESS, null, null, referralMasterDtosList, null);

		} catch (HibernateException he) {
			logger.error("getReferralListByReferralName failed", he);
		} catch (Exception e) {
			logger.error("getReferralListByReferralName failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getReferralListByOrgId(ReferralMasterDto referralMasterDto) throws ApplicationException {
		try {
			List<ReferralMasterDto> referralMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_REFERRAL_LIST_BY_ORG_ID")
					.setParameter("organizationId", referralMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ReferralMasterDto.class)).list();
			return new Response(SUCCESS, null, null, referralMasterDtosList, null);

		} catch (HibernateException he) {
           throw he;
		} catch (Exception e) {
			throw e;
		} 
		//return new Response(ERROR, null, null, null, null);
	}

}
