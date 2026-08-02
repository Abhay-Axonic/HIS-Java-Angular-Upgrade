package com.param.adt.master.global.dao;

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

import com.param.adt.common.ADTCommonDateUtils;
import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.master.global.dto.ReferralTypeMasterDto;
import com.param.adt.master.global.model.ReferralTypeMaster;
import com.param.global.service.VerifiedIdentityService;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class ReferralTypeMasterDaoImpl extends GenericDao<ReferralTypeMaster, Integer> implements IReferralTypeMasterDao,ICommonConstants
{

	public ReferralTypeMasterDaoImpl() {
		super(ReferralTypeMaster.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired 
	private DozerBeanMapper mapper;

	@Autowired
	private VerifiedIdentityService verifiedIdentityService;
	
	@Override
	public Response getReferralTypeByName(ReferralTypeMasterDto referralTypeMasterDto) throws ApplicationException {
		try {
			List<ReferralTypeMasterDto> referralTypeMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_REFFERAL_TYPE_MASTER_LIST_BY_NAME")
					.setParameter("desc", referralTypeMasterDto.getDesc().toLowerCase())
					.setParameter("orgId", referralTypeMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ReferralTypeMasterDto.class)).list();
			return new Response(SUCCESS, null, null, referralTypeMasterDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response addReferralTypeTypeMaster(ReferralTypeMasterDto referralTypeMasterDto) throws ApplicationException {
		try {
			ReferralTypeMaster referralTypeMaster = mapper.map(referralTypeMasterDto, ReferralTypeMaster.class, "ReferralTypeMasterDto_to_ReferralTypeMaster");
			referralTypeMaster.setCreatedDate(TimeZoneComponent.getDateByzone(0));
			referralTypeMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(0));
			referralTypeMaster.setStatus('A');
			referralTypeMaster = save(referralTypeMaster);
			return new Response(SUCCESS, null, null, null, referralTypeMaster);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getReferralTypeMasterList(ReferralTypeMasterDto referralTypeMasterDto) throws ApplicationException {
		try {
			List<ReferralTypeMasterDto> referralTypeMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_REFERRAL_TYPE_MASTER_LIST")
					.setParameter("orgId", referralTypeMasterDto.getOrganizationId())
					.setFirstResult(referralTypeMasterDto.getOffset() != null ? referralTypeMasterDto.getOffset() : 0)
					.setMaxResults(referralTypeMasterDto.getNoOfRecordsPerPage() != null ? referralTypeMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(ReferralTypeMasterDto.class)).list();
			return new Response(SUCCESS, null, null, referralTypeMasterDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getReferralTypeById(ReferralTypeMasterDto referralTypeMasterDto) throws ApplicationException {
		try {
			List<ReferralTypeMasterDto> referralTypeMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_REFFERAL_TYPE_MASTER_LIST_BY_ID")
					.setParameter("id", referralTypeMasterDto.getId())
					.setResultTransformer(Transformers.aliasToBean(ReferralTypeMasterDto.class)).list();

			// BR-144: cross-tenant IDOR fix -- filter the by-id result to the
			// JWT-verified org so a forged id can never read another org's row.
			if (referralTypeMasterDtosList != null && !referralTypeMasterDtosList.isEmpty()) {
				ReferralTypeMaster row = findById(referralTypeMasterDtosList.get(0).getId());
				if (row == null || row.getOrganizationId() == null
						|| !row.getOrganizationId().equals(verifiedIdentityService.getVerifiedOrganizationId())) {
					referralTypeMasterDtosList = new java.util.ArrayList<>();
				}
			}
			return new Response(SUCCESS, null, null, referralTypeMasterDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveReferralTypeList(ReferralTypeMasterDto referralTypeMasterDto) throws ApplicationException {
		try {
			List<ReferralTypeMasterDto> referralTypeMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_REFFERAL_TYPE_MASTER_LIST")
					.setParameter("orgId", referralTypeMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ReferralTypeMasterDto.class)).list();
			return new Response(SUCCESS, null, null, referralTypeMasterDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getReferralTypeByNameNotId(ReferralTypeMasterDto referralTypeMasterDto)
			throws ApplicationException {
		try {
			List<ReferralTypeMasterDto> referralTypeMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_REFFERAL_TYPE_MASTER_LIST_BY_NAME_NOT_ID")
					.setParameter("desc", referralTypeMasterDto.getDesc().toLowerCase())
					.setParameter("id", referralTypeMasterDto.getId())
					.setParameter("orgId", referralTypeMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ReferralTypeMasterDto.class)).list();
			return new Response(SUCCESS, null, null, referralTypeMasterDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response updateReferralTypeMaster(ReferralTypeMasterDto referralTypeMasterDto) throws ApplicationException {
		try {
			ReferralTypeMaster referralTypeMaster = findById(referralTypeMasterDto.getId());

			// BR-144: cross-tenant IDOR fix -- the target row's organization must
			// match the JWT-verified identity, never a client-supplied value.
			// (Same VerifiedIdentityService pattern as BR-143 on the sibling
			// patientSourceMaster DAO.)
			if (referralTypeMaster == null || referralTypeMaster.getOrganizationId() == null
					|| !referralTypeMaster.getOrganizationId()
							.equals(verifiedIdentityService.getVerifiedOrganizationId())) {
				return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
			}

			referralTypeMaster.setDesc(referralTypeMasterDto.getDesc());
			referralTypeMaster.setCode(referralTypeMasterDto.getCode());
			referralTypeMaster.setUpdatedBy(referralTypeMasterDto.getUpdatedBy());
			referralTypeMaster.setUpdatedDate(ADTCommonDateUtils.getDate(referralTypeMasterDto.getUpdatedDate(),"dd-M-yyyy HH:mm:ss"));
			update(referralTypeMaster);
			return new Response<ReferralTypeMaster, Integer>(SUCCESS, null, COMMON_SUCCESS, null, referralTypeMaster);

		} catch (HibernateException he) {
			return new Response<ReferralTypeMaster, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
			
			return new Response<ReferralTypeMaster, Integer>(ERROR, null, null, null, null);
		} finally {
		}
	}


	@Override
	public Response updateStatusForReferralType(ReferralTypeMasterDto referralTypeMasterDto)
			throws ApplicationException {
		try {
			ReferralTypeMaster referralTypeMaster = findById(referralTypeMasterDto.getId());

			// BR-144: cross-tenant IDOR fix -- same ownership check as updateReferralTypeMaster.
			if (referralTypeMaster == null || referralTypeMaster.getOrganizationId() == null
					|| !referralTypeMaster.getOrganizationId()
							.equals(verifiedIdentityService.getVerifiedOrganizationId())) {
				return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
			}

			referralTypeMaster.setUpdatedBy(referralTypeMasterDto.getUpdatedBy());
			referralTypeMaster.setUpdatedDate(ADTCommonDateUtils.getDate(referralTypeMasterDto.getUpdatedDate(),"dd-M-yyyy HH:mm:ss"));
			referralTypeMaster.setStatus(referralTypeMasterDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			update(referralTypeMaster);
			return new Response<ReferralTypeMaster, Integer>(SUCCESS, null, COMMON_SUCCESS, null, referralTypeMaster.getOrganizationId());

		} catch (HibernateException he) {
			return new Response<ReferralTypeMaster, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
			
			return new Response<ReferralTypeMaster, Integer>(ERROR, null, null, null, null);
		} finally {
		}
	}
	
	@Override
	public Response getCount(ReferralTypeMasterDto referralTypeMasterDto)
	{
		try{
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(ReferralTypeMaster.class); 
			c.add(Restrictions.eq("organizationId", referralTypeMasterDto.getOrganizationId()));
			c.setProjection(Projections.rowCount());  
			Long count = (Long)c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		}catch(Exception e)
		{
			
			return new Response(ERROR, null, null, null, null);
		}
	}

	
}
