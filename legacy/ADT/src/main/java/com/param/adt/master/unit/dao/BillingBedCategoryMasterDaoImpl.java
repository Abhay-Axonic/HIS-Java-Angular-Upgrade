package com.param.adt.master.unit.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.common.Response;
import com.param.adt.master.global.dto.VisitTypeMasterDto;
import com.param.adt.master.unit.dto.BillingBedCategoryMasterDto;
import com.param.adt.master.unit.model.BillingBedCategoryMaster;
import com.param.adt.master.unit.model.BillingBedVisitTypeMapper;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.service.VerifiedIdentityService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.ServiceException;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BillingBedCategoryMasterDaoImpl extends GenericDao<BillingBedCategoryMaster, Integer> implements IBillingBedCategoryMasterDao,ICommonConstants{

	@Autowired
	private VerifiedIdentityService verifiedIdentityService;

	final static Logger logger = Logger.getLogger(BillingBedCategoryMasterDaoImpl.class);

	public BillingBedCategoryMasterDaoImpl() {
		super(BillingBedCategoryMaster.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response getActiveBillingBedCategoryList(BillingBedCategoryMasterDto bedCategoryMasterDto) {
		try {
			List<BillingBedCategoryMasterDto> billingBedCategoryMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_BILLING_BED_CATEGORY_LIST")
					.setParameter("orgId", bedCategoryMasterDto.getOrganizationId())
					.setParameter("unitId", bedCategoryMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(BillingBedCategoryMasterDto.class)).list();
			return new Response(SUCCESS, null, null, billingBedCategoryMasterDtosList, null);

		} catch (HibernateException he) {
			logger.error("getActiveBillingBedCategoryList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getActiveBillingBedCategoryList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getActiveBillingBedCategoryByBedCat(BillingBedCategoryMasterDto bedCategoryMasterDto)
			throws ApplicationException {
		try {
			List<BillingBedCategoryMasterDto> billingBedCategoryMasterDtos = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BILLING_BED_LIST_BY_BED_CAT")
					.setParameter("organizationId", bedCategoryMasterDto.getOrganizationId())
					.setParameter("bedCategoryId", bedCategoryMasterDto.getBedCategoryId())
					.setResultTransformer(Transformers.aliasToBean(BillingBedCategoryMasterDto.class)).list();
			return new Response(SUCCESS, null, null, billingBedCategoryMasterDtos, null);

		} catch (HibernateException he) {
			logger.error("getActiveBillingBedCategoryByBedCat failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getActiveBillingBedCategoryByBedCat failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response saveBillingBedCategoryMaster(BillingBedCategoryMasterDto billingBedCategoryMasterDto)
			throws ApplicationException {
		try {
			BillingBedCategoryMaster billingBedCategoryMaster=new BillingBedCategoryMaster();
				billingBedCategoryMaster.setBillingBedCode(billingBedCategoryMasterDto.getBillingBedCode());
				billingBedCategoryMaster.setBillingBedDesc(billingBedCategoryMasterDto.getBillingBedDesc());
				billingBedCategoryMaster.setHierarchyId(billingBedCategoryMasterDto.getHierarchyId());
				billingBedCategoryMaster.setCreatedBy(billingBedCategoryMasterDto.getCreatedBy());
				billingBedCategoryMaster.setUpdatedBy(billingBedCategoryMasterDto.getUpdatedBy());
				billingBedCategoryMaster.setCreatedDate(GlobalCommonDateUtils.getDate(billingBedCategoryMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
				billingBedCategoryMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(billingBedCategoryMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
				billingBedCategoryMaster.setStatus(billingBedCategoryMasterDto.getStatus());
				billingBedCategoryMaster.setOrganizationId(billingBedCategoryMasterDto.getOrganizationId());
				billingBedCategoryMaster.setUnitId(billingBedCategoryMasterDto.getUnitId());
				billingBedCategoryMaster.setIsGstApplicable(billingBedCategoryMasterDto.getIsGstApplicable());
				billingBedCategoryMaster.setGstAmountRange(billingBedCategoryMasterDto.getGstAmountRange());
				billingBedCategoryMaster.setGstPercentage(billingBedCategoryMasterDto.getGstPercentage());
				billingBedCategoryMaster.setIsPeriodicBilling(billingBedCategoryMasterDto.getIsPeriodicBilling());
			billingBedCategoryMaster=save(billingBedCategoryMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, billingBedCategoryMaster.getBillingBedCategoryId());

		} catch (HibernateException he) {
			logger.error("saveBillingBedCategoryMaster failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("saveBillingBedCategoryMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBillingBedCategoryById(int billingBedId, int orgId, int unitId) throws ApplicationException {
		try {
			List<BillingBedCategoryMasterDto> billingBedCategoryMasterDtosList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BILLING_BED_CATEGORY_BY_ID")
					.setParameter("orgId",orgId)
					.setParameter("unitId", unitId)
					.setParameter("billingBedId", billingBedId)
					.setResultTransformer(Transformers.aliasToBean(BillingBedCategoryMasterDto.class)).list();		
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, billingBedCategoryMasterDtosList, null);

		} catch (HibernateException he) {
			logger.error("getBillingBedCategoryById failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getBillingBedCategoryById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBillingBedCategoryList(BillingBedCategoryMasterDto billingBedCategoryMasterDto)
			throws ApplicationException {
		try {
			List<BillingBedCategoryMasterDto> billingBedCategoryMasterDtosList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BILLING_BED_CATEGORY_LIST")
					.setParameter("orgId",billingBedCategoryMasterDto.getOrganizationId())
					.setParameter("unitId", billingBedCategoryMasterDto.getUnitId())
					.setFirstResult(billingBedCategoryMasterDto.getOffset() != null ? billingBedCategoryMasterDto.getOffset() : 0)
					.setMaxResults(
							billingBedCategoryMasterDto.getNoOfRecordsPerPage() != null ? billingBedCategoryMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(BillingBedCategoryMasterDto.class)).list();		
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, billingBedCategoryMasterDtosList, null);

		} catch (HibernateException he) {
			logger.error("getBillingBedCategoryList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getBillingBedCategoryList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<VisitTypeMasterDto> getBillingBedCategoryVistList(BillingBedCategoryMasterDto billingBedCategoryMasterDto)
			throws ApplicationException {
		try {
			List<VisitTypeMasterDto> billingBedCategoryVistList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BILLING_BED_CATEGORY_VISIT_LIST")
					.setParameter("orgId",billingBedCategoryMasterDto.getOrganizationId())
					//.setParameter("unitId", billingBedCategoryMasterDto.getUnitId())
					.setParameter("billingBedCategoryId", billingBedCategoryMasterDto.getBillingBedCategoryId())
					.setResultTransformer(Transformers.aliasToBean(VisitTypeMasterDto.class)).list();	
			
			return billingBedCategoryVistList;

		} catch (HibernateException he) {
			logger.error("getBillingBedCategoryVistList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getBillingBedCategoryVistList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateBillingBedCategoryMaster(BillingBedCategoryMasterDto billingBedCategoryMasterDto)
			throws ApplicationException {
		try {
			BillingBedCategoryMaster billingBedCategoryMaster=findById(billingBedCategoryMasterDto.getBillingBedCategoryId());

			// BR-146: cross-tenant IDOR fix -- the target row's organization must
			// match the JWT-verified identity, never a client-supplied value.
			// (Same VerifiedIdentityService pattern as BR-143/BR-144/BR-145.)
			if (billingBedCategoryMaster == null || billingBedCategoryMaster.getOrganizationId() == null
					|| !billingBedCategoryMaster.getOrganizationId()
							.equals(verifiedIdentityService.getVerifiedOrganizationId())) {
				return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
			}

			billingBedCategoryMaster.setBillingBedCode(billingBedCategoryMasterDto.getBillingBedCode());
			billingBedCategoryMaster.setBillingBedDesc(billingBedCategoryMasterDto.getBillingBedDesc());
			billingBedCategoryMaster.setIsPeriodicBilling(billingBedCategoryMasterDto.getIsPeriodicBilling());
			billingBedCategoryMaster.setHierarchyId(billingBedCategoryMasterDto.getHierarchyId());
			billingBedCategoryMaster.setUpdatedBy(billingBedCategoryMasterDto.getUpdatedBy());
			billingBedCategoryMaster.setIsGstApplicable(billingBedCategoryMasterDto.getIsGstApplicable());
			billingBedCategoryMaster.setGstAmountRange(billingBedCategoryMasterDto.getGstAmountRange());
			billingBedCategoryMaster.setGstPercentage(billingBedCategoryMasterDto.getGstPercentage());
			billingBedCategoryMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(billingBedCategoryMasterDto.getUpdatedDate(), "dd-MM-yyyy HH:mm:ss"));
			update(billingBedCategoryMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, billingBedCategoryMaster);
		} catch (HibernateException he) {
			logger.error("updateBillingBedCategoryMaster failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateBillingBedCategoryMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateBillingBedCategoryMasterStatus(BillingBedCategoryMasterDto billingBedCategoryMasterDto)
			throws ApplicationException {
		try {
			BillingBedCategoryMaster billingBedCategoryMaster=findById(billingBedCategoryMasterDto.getBillingBedCategoryId());

			// BR-146: cross-tenant IDOR fix -- same ownership check as updateBillingBedCategoryMaster.
			if (billingBedCategoryMaster == null || billingBedCategoryMaster.getOrganizationId() == null
					|| !billingBedCategoryMaster.getOrganizationId()
							.equals(verifiedIdentityService.getVerifiedOrganizationId())) {
				return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
			}

			billingBedCategoryMaster.setStatus(billingBedCategoryMasterDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			billingBedCategoryMaster.setUpdatedBy(billingBedCategoryMasterDto.getUpdatedBy());
			billingBedCategoryMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(billingBedCategoryMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(billingBedCategoryMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, billingBedCategoryMaster.getUnitId());

		} catch (HibernateException he) {
			logger.error("updateBillingBedCategoryMasterStatus failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateBillingBedCategoryMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBillingBedCategoryMasterCount(BillingBedCategoryMasterDto billingBedCategoryMasterDto)
			throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			Criteria c = session.createCriteria(BillingBedCategoryMaster.class);
			c.add(Restrictions.eq("organizationId", billingBedCategoryMasterDto.getOrganizationId()));
			c.add(Restrictions.eq("unitId", billingBedCategoryMasterDto.getUnitId()));
			c.setProjection(Projections.rowCount());
			Long count = (Long) c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		} catch (HibernateException he) {
			logger.error("getBillingBedCategoryMasterCount failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getBillingBedCategoryMasterCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBillingBedCategoryByName(BillingBedCategoryMasterDto billingBedCategoryMasterDto)
			throws ApplicationException {
		try {
			List<BillingBedCategoryMasterDto> billingBedCategoryMasterDtosList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BILLING_BED_CATEGORY_BY_NAME")
					.setParameter("orgId",billingBedCategoryMasterDto.getOrganizationId())
					.setParameter("unitId",billingBedCategoryMasterDto.getUnitId())
					.setParameter("desc", billingBedCategoryMasterDto.getBillingBedDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(BillingBedCategoryMasterDto.class)).list();		
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, billingBedCategoryMasterDtosList, null);

		} catch (HibernateException he) {
			logger.error("getBillingBedCategoryByName failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getBillingBedCategoryByName failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBillingBedCategoryByNameNotById(BillingBedCategoryMasterDto billingBedCategoryMasterDto)
			throws ApplicationException {
		try {

			List<BillingBedCategoryMasterDto> billingBedCategoryMasterDtosList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BILLING_BED_CATEGORY_BY_NAME_NOT_BY_ID")
					.setParameter("billingBedId",billingBedCategoryMasterDto.getBillingBedCategoryId())
					.setParameter("orgId",billingBedCategoryMasterDto.getOrganizationId())
					.setParameter("unitId",billingBedCategoryMasterDto.getUnitId())
					.setParameter("desc", billingBedCategoryMasterDto.getBillingBedDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(BillingBedCategoryMasterDto.class)).list();		
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, billingBedCategoryMasterDtosList, null);
		} catch (HibernateException he) {
			logger.error("getBillingBedCategoryByNameNotById failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getBillingBedCategoryByNameNotById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Integer saveBillingBedVisitTypeMapper(BillingBedVisitTypeMapper billingBedVisitTypeMapper)
			throws ApplicationException {
		try {
				Integer i = (Integer) sessionFactory.getCurrentSession().save(billingBedVisitTypeMapper);
			
			return i;

		} catch (HibernateException he) {
			logger.error("saveBillingBedVisitTypeMapper failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("saveBillingBedVisitTypeMapper failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updatePreviousVisitTypeList(BillingBedVisitTypeMapper bedVisitTypeMapper)
			throws ApplicationException {
		try {
			
			sessionFactory.getCurrentSession().createQuery("update BillingBedVisitTypeMapper set status='I', "
					+ "updatedBy=:updatedBy, "
					+ "updatedDate=:updatedDate "
					+ "WHERE billingBedCategoryId=:billingBedCategoryId "
					+ "AND visitTypeId=:visitTypeId ")
				.setParameter("updatedBy", bedVisitTypeMapper.getUpdatedBy())
				.setParameter("billingBedCategoryId", bedVisitTypeMapper.getBillingBedCategoryId())
				.setParameter("visitTypeId", bedVisitTypeMapper.getVisitTypeId())
				.setParameter("updatedDate",bedVisitTypeMapper.getUpdatedDate())
			.executeUpdate();
			
			return new Response<>(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {
			logger.error("updatePreviousVisitTypeList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updatePreviousVisitTypeList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBillingBedCategoryByVistTypeId(int visitTypeId, int orgId, int unitId)
			throws ApplicationException {
		try {

			List<BillingBedCategoryMasterDto> billingBedCategoryMasterDtosList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BILLING_BED_CATEGORY_BY_VIST_TYPE_ID")
					.setParameter("visitTypeId",visitTypeId)
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(BillingBedCategoryMasterDto.class)).list();		
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, billingBedCategoryMasterDtosList, null);
		} catch (HibernateException he) {
			logger.error("getBillingBedCategoryByVistTypeId failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getBillingBedCategoryByVistTypeId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBillingBedCategoryByVistTypeIdMultiple(BillingBedCategoryMasterDto billingBedCategoryMasterDto)
			throws ApplicationException {
		try {

			List<BillingBedCategoryMasterDto> billingBedCategoryMasterDtosList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BILLING_BED_CATEGORY_BY_VIST_TYPE_ID_MULTIPLE")
					.setParameterList("visitTypeIds",billingBedCategoryMasterDto.getVisitTypeListIds())
					.setParameter("orgId",billingBedCategoryMasterDto.getOrganizationId())
					.setParameter("unitId", billingBedCategoryMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(BillingBedCategoryMasterDto.class)).list();		
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, billingBedCategoryMasterDtosList, null);
		} catch (HibernateException he) {
			logger.error("getBillingBedCategoryByVistTypeIdMultiple failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getBillingBedCategoryByVistTypeIdMultiple failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	//@Override
	/*public Response getActiveBillingBedCategoryMasterList(Integer orgId) throws ApplicationException {
		try {
			List<BillingBedCategoryMasterDto> billingBedCategoryMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_BILLING_BED_CATEGORY_MASTER_LIST")
					.setParameter("orgId", orgId)
					.setResultTransformer(Transformers.aliasToBean(BillingBedCategoryMasterDto.class)).list();
			return new Response(SUCCESS, null, null, billingBedCategoryMasterDtosList, null);

		} catch (HibernateException he) {
			h

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}*/

}
