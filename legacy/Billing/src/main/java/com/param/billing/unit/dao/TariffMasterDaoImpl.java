package com.param.billing.unit.dao;

import java.util.List;
import java.util.ListIterator;

import com.param.billing.global.model.BankMaster;
import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.global.transaction.model.TariffMaster;
import com.param.billing.global.transaction.model.TariffPatientTypeMapper;
import com.param.billing.global.transaction.model.TariffPaymentEntitlementMapper;
import com.param.billing.global.transaction.model.TariffVisitTypeMapper;
import com.param.billing.global.transaction.model.TariffBillingBedCategoryMapper;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.TariffMasterDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import com.param.global.service.VerifiedIdentityService;


@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TariffMasterDaoImpl extends GenericDao<TariffMaster, Integer> implements ITariffMasterDao,ICommonConstants{

	@Autowired
	private VerifiedIdentityService verifiedIdentityService;

	final static Logger logger = Logger.getLogger(TariffMasterDaoImpl.class);

	@Autowired
	DozerBeanMapper mapper;
	
	@Autowired
	SessionFactory sessionFactory;
	
	public TariffMasterDaoImpl() {
		super(TariffMaster.class);
	}

	@Override
	public Response saveTariffMaster(TariffMasterDto tariffMasterDto) {
		try {

			TariffMaster tariffMaster = mapper.map(tariffMasterDto, TariffMaster.class,
					"TariffMasterDto_to_TariffMaster");
			tariffMaster = save(tariffMaster);
			
			tariffMasterDto.setTariffId(tariffMaster.getTariffId());
			return new Response(SUCCESS, null, COMMON_SUCCESS, null, tariffMasterDto);

		} catch (HibernateException he) {
			logger.error("saveTariffMaster failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("saveTariffMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	public Response saveTariffPaymentEntitlementMapper(TariffMasterDto tariffMasterDto) {
		try {
			Session session=sessionFactory.getCurrentSession();
			int saveCount=0;
			
			if (tariffMasterDto.getPaymentEntitlementIdList() != null
					&& tariffMasterDto.getPaymentEntitlementIdList().size() > 0) {

				ListIterator<TariffMasterDto> itr = tariffMasterDto.getPaymentEntitlementIdList().listIterator();
				while (itr.hasNext()) 
				{
					TariffPaymentEntitlementMapper tariffPaymentEntitlementMapper=new TariffPaymentEntitlementMapper();
					TariffMasterDto dto=itr.next();
						tariffPaymentEntitlementMapper.setTariffId(tariffMasterDto.getTariffId());
						tariffPaymentEntitlementMapper.setPaymentEntitlementId(dto.getId());
						tariffPaymentEntitlementMapper.setCreatedBy(tariffMasterDto.getUpdatedBy());
						tariffPaymentEntitlementMapper.setCreatedDate(GlobalCommonDateUtils.getDate(tariffMasterDto.getUpdatedDate(),"dd-M-yyyy HH:mm:ss"));
						tariffPaymentEntitlementMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(tariffMasterDto.getUpdatedDate(),"dd-M-yyyy HH:mm:ss"));
						tariffPaymentEntitlementMapper.setUpdatedBy(tariffMasterDto.getUpdatedBy());
						tariffPaymentEntitlementMapper.setOrganizationId(tariffMasterDto.getOrganizationId());
						tariffPaymentEntitlementMapper.setUnitId(tariffMasterDto.getUnitId());
						tariffPaymentEntitlementMapper.setStatus('A');
						int count=(int) session.save(tariffPaymentEntitlementMapper);
					
					if (count>0)
						saveCount++;
				}
			}
			if(tariffMasterDto.getPaymentEntitlementIdList().size()==saveCount)
				return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
			else
				return new Response(ERROR, null, COMMON_ERROR, null, null);

		} catch (HibernateException he) {
			logger.error("saveTariffPaymentEntitlementMapper failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("saveTariffPaymentEntitlementMapper failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response saveVisitTypeMapper(TariffMasterDto tariffMasterDto) throws ApplicationException {
		try {
			Session session=sessionFactory.getCurrentSession();
			int saveCount=0;
			
			if (tariffMasterDto.getVisitTypeList() != null
					&& tariffMasterDto.getVisitTypeList().size() > 0) {

				ListIterator<TariffMasterDto> itr = tariffMasterDto.getVisitTypeList().listIterator();
				while (itr.hasNext()) 
				{
					TariffVisitTypeMapper tariffVisitTypeMapper=new TariffVisitTypeMapper();
					TariffMasterDto dto=itr.next();
					tariffVisitTypeMapper.setTariffId(tariffMasterDto.getTariffId());
					tariffVisitTypeMapper.setVisitTypeId(dto.getId());
					tariffVisitTypeMapper.setCreatedBy(tariffMasterDto.getUpdatedBy());
					tariffVisitTypeMapper.setCreatedDate(GlobalCommonDateUtils.getDate(tariffMasterDto.getUpdatedDate(),"dd-M-yyyy HH:mm:ss"));
					tariffVisitTypeMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(tariffMasterDto.getUpdatedDate(),"dd-M-yyyy HH:mm:ss"));
					tariffVisitTypeMapper.setUpdatedBy(tariffMasterDto.getUpdatedBy());
					tariffVisitTypeMapper.setOrganizationId(tariffMasterDto.getOrganizationId());
					tariffVisitTypeMapper.setUnitId(tariffMasterDto.getUnitId());
					tariffVisitTypeMapper.setStatus('A');
					int count=(int) session.save(tariffVisitTypeMapper);
					
					if (count>0)
						saveCount++;
				}
			}
			if(tariffMasterDto.getVisitTypeList().size()==saveCount)
				return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
			else
				return new Response(ERROR, null, COMMON_ERROR, null, null);

		} catch (HibernateException he) {
			logger.error("saveVisitTypeMapper failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("saveVisitTypeMapper failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	@Override
	public Response saveTariffPatientTypeMapper(TariffMasterDto tariffMasterDto) throws ApplicationException {
		try {
			Session session=sessionFactory.getCurrentSession();
			int saveCount=0;
			
			if (tariffMasterDto.getPatientTypeList() != null
					&& tariffMasterDto.getPatientTypeList().size() > 0) {

				ListIterator<TariffMasterDto> itr = tariffMasterDto.getPatientTypeList().listIterator();
				while (itr.hasNext()) 
				{
					TariffPatientTypeMapper tariffPatientTypeMapper=new TariffPatientTypeMapper();
					TariffMasterDto dto=itr.next();
					tariffPatientTypeMapper.setTariffId(tariffMasterDto.getTariffId());
					tariffPatientTypeMapper.setPatientTypeId(dto.getId());
					tariffPatientTypeMapper.setCreatedBy(tariffMasterDto.getUpdatedBy());
					tariffPatientTypeMapper.setCreatedDate(GlobalCommonDateUtils.getDate(tariffMasterDto.getUpdatedDate(),"dd-M-yyyy HH:mm:ss"));
					tariffPatientTypeMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(tariffMasterDto.getUpdatedDate(),"dd-M-yyyy HH:mm:ss"));
					tariffPatientTypeMapper.setUpdatedBy(tariffMasterDto.getUpdatedBy());
					tariffPatientTypeMapper.setOrganizationId(tariffMasterDto.getOrganizationId());
					tariffPatientTypeMapper.setUnitId(tariffMasterDto.getUnitId());
					tariffPatientTypeMapper.setStatus('A');
					int count=(int) session.save(tariffPatientTypeMapper);
					
					if (count>0)
						saveCount++;
				}
			}
			if(tariffMasterDto.getPatientTypeList().size()==saveCount)
				return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
			else
				return new Response(ERROR, null, COMMON_ERROR, null, null);

		} catch (HibernateException he) {
			logger.error("saveTariffPatientTypeMapper failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("saveTariffPatientTypeMapper failed", e);
			throw new ServiceException(e.getMessage());
		}

	}

	/*@Override
	public Response saveTariffBillingBedCategoryMapper(TariffMasterDto tariffMasterDto) throws ApplicationException {
		try {
			Session session=sessionFactory.getCurrentSession();
			int saveCount=0;
			
			if (tariffMasterDto.getBillingBedList() != null
					&& tariffMasterDto.getBillingBedList().size() > 0) {

				ListIterator<TariffMasterDto> itr = tariffMasterDto.getBillingBedList().listIterator();
				while (itr.hasNext()) 
				{
					TariffBillingBedCategoryMapper tariffBillingBedCategoryMapper=new TariffBillingBedCategoryMapper();
					TariffMasterDto dto=itr.next();
					tariffBillingBedCategoryMapper.setTariffId(tariffMasterDto.getTariffId());
					tariffBillingBedCategoryMapper.setBillingBedCategoryId(dto.getId());
					tariffBillingBedCategoryMapper.setCreatedBy(tariffMasterDto.getUpdatedBy());
					tariffBillingBedCategoryMapper.setCreatedDate(GlobalCommonDateUtils.getDate(tariffMasterDto.getUpdatedDate(),"dd-M-yyyy HH:mm:ss"));
					tariffBillingBedCategoryMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(tariffMasterDto.getUpdatedDate(),"dd-M-yyyy HH:mm:ss"));
					tariffBillingBedCategoryMapper.setUpdatedBy(tariffMasterDto.getUpdatedBy());
					tariffBillingBedCategoryMapper.setOrganizationId(tariffMasterDto.getOrganizationId());
					tariffBillingBedCategoryMapper.setUnitId(tariffMasterDto.getUnitId());
					tariffBillingBedCategoryMapper.setStatus('A');
					int count=(int) session.save(tariffBillingBedCategoryMapper);
					
					if (count>0)
						saveCount++;
				}
			}
			if(tariffMasterDto.getBillingBedList().size()==saveCount)
				return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
			else
				return new Response(ERROR, null, COMMON_ERROR, null, null);
			
		} catch (HibernateException he) {
			h
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}*/
	@Override
	public Response getTariffListByName(TariffMasterDto tariffMasterDto) throws ApplicationException {
		try {
			List<TariffMasterDto> tariffMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TARIFF_MASTER_LIST_BY_NAME")
					.setParameter("orgId", tariffMasterDto.getOrganizationId())
					.setParameter("unitId", tariffMasterDto.getUnitId())
					.setParameter("tariffDesc",tariffMasterDto.getTariffDesc())
					.setResultTransformer(Transformers.aliasToBean(TariffMasterDto.class)).list();
			return new Response(SUCCESS, null, null, tariffMasterDtosList, null);

		} catch (HibernateException he) {
			logger.error("getTariffListByName failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getTariffListByName failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getTariffMasterList(TariffMasterDto tariffMasterDto) throws ApplicationException {
		try {
			List<TariffMasterDto> tariffMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TARIFF_MASTER_LIST")
					.setParameter("orgId", tariffMasterDto.getOrganizationId())
					.setParameter("unitId", tariffMasterDto.getUnitId())
					.setFirstResult(tariffMasterDto.getOffset() != null ? tariffMasterDto.getOffset() : 0)
					.setMaxResults(tariffMasterDto.getNoOfRecordsPerPage() != null ? tariffMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(TariffMasterDto.class)).list();
			return new Response(SUCCESS, null, null, tariffMasterDtosList, null);

		} catch (HibernateException he) {
			logger.error("getTariffMasterList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getTariffMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPaymentEntitlementListByTariffid(TariffMasterDto tariffMasterDto) throws ApplicationException {
		try {
			List<TariffMasterDto> tariffMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PAYMENT_ENTITLEMENT_LIST_BY_TARIFF_ID")
					.setParameter("tariffId", tariffMasterDto.getTariffId())
					.setResultTransformer(Transformers.aliasToBean(TariffMasterDto.class)).list();
			return new Response(SUCCESS, null, null, tariffMasterDtosList, null);

		} catch (HibernateException he) {
			logger.error("getPaymentEntitlementListByTariffid failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getPaymentEntitlementListByTariffid failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getTariffListById(TariffMasterDto tariffMasterDto) throws ApplicationException {
		try {
			TariffMasterDto tariffMasterDtoo = (TariffMasterDto)sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TARIFF_MASTER_LIST_BY_ID")
					.setParameter("tariffId", tariffMasterDto.getTariffId())
					.setResultTransformer(Transformers.aliasToBean(TariffMasterDto.class)).uniqueResult();
			
			List<TariffMasterDto> tariffMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PAYMENT_ENTITLEMENT_LIST_BY_TARIFF_ID")
					.setParameter("tariffId", tariffMasterDto.getTariffId())
					.setResultTransformer(Transformers.aliasToBean(TariffMasterDto.class)).list();

			List<TariffMasterDto> visitTypeList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_VISIT_TYPE_LIST_BY_TARIFF_ID")
					.setParameter("tariffId", tariffMasterDto.getTariffId())
					.setResultTransformer(Transformers.aliasToBean(TariffMasterDto.class)).list();
			
			List<TariffMasterDto> patientTypeList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PATIENT_TYPE_LIST_BY_TARIFF_ID")
					.setParameter("tariffId", tariffMasterDto.getTariffId())
					.setResultTransformer(Transformers.aliasToBean(TariffMasterDto.class)).list();
			
			
			tariffMasterDtoo.setApplicablePaymentEntitlementIdList2(tariffMasterDtosList);
			tariffMasterDtoo.setVisitTypeList(visitTypeList);
			tariffMasterDtoo.setPatientTypeList(patientTypeList);
			
			return new Response(SUCCESS, null, null, null, tariffMasterDtoo);

		} catch (HibernateException he) {
			logger.error("getTariffListById failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getTariffListById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getTariffListByNameNotId(TariffMasterDto tariffMasterDto) throws ApplicationException {
		try {
			List<TariffMasterDto> tariffMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TARIFF_MASTER_LIST_BY_NAME_NOT_ID")
					.setParameter("orgId", tariffMasterDto.getOrganizationId())
					.setParameter("tariffId", tariffMasterDto.getTariffId())
					.setParameter("unitId", tariffMasterDto.getUnitId())
					.setParameter("tariffDesc",tariffMasterDto.getTariffDesc())
					.setResultTransformer(Transformers.aliasToBean(TariffMasterDto.class)).list();
			return new Response(SUCCESS, null, null, tariffMasterDtosList, null);

		} catch (HibernateException he) {
			logger.error("getTariffListByNameNotId failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getTariffListByNameNotId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateTariffMaster(TariffMasterDto tariffMasterDto) throws ApplicationException {
		try {
			TariffMaster tarriffMaster = findById(tariffMasterDto.getTariffId());
			// BATCH-5 org-trust fix (write-by-ID class): bulk UPDATE by-ID only, zero org check.
			if (tarriffMaster == null || tarriffMaster.getOrganizationId() == null
					|| !tarriffMaster.getOrganizationId().equals(verifiedIdentityService.getVerifiedOrganizationId())) {
				return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
			}
			sessionFactory.getCurrentSession().createQuery("UPDATE TariffMaster tariff SET tariff.tariffCode =:tariffCode , "
					+ "tariff.tariffDesc =:tariffDesc , "
					+ "tariff.validFrom =:validFrom , "
					+ "tariff.validTo =:validTo , "
					+ "tariff.updatedBy =:updatedBy , "
					+ "tariff.updatedDate =:updatedDate "
					+ "WHERE tariff.tariffId =:tariffId ")
					.setParameter("tariffId", tariffMasterDto.getTariffId())
					.setParameter("tariffCode", tariffMasterDto.getTariffCode())
					.setParameter("tariffDesc", tariffMasterDto.getTariffDesc())
					.setParameter("validFrom", GlobalCommonDateUtils.getDate(tariffMasterDto.getValidFrom(), "dd-M-yyyy HH:mm:ss"))
					.setParameter("validTo", GlobalCommonDateUtils.getDate(tariffMasterDto.getValidTo(), "dd-M-yyyy HH:mm:ss"))
					.setParameter("updatedBy", tariffMasterDto.getUpdatedBy())
					.setParameter("updatedDate", GlobalCommonDateUtils.getDate(tariffMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"))
			.executeUpdate();
			
			return new Response(SUCCESS, null, COMMON_UPDATE, null, tarriffMaster);

		} catch (HibernateException he) {
			logger.error("updateTariffMaster failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateTariffMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response inactiveOldPaymentEntitlements(TariffMasterDto tariffMasterDto) throws ApplicationException {
		try {
			sessionFactory.getCurrentSession().createQuery("update TariffPaymentEntitlementMapper tpem "
					+ "SET tpem.status='I', "
					+ "tpem.updatedBy =:updatedBy , "
					+ "tpem.updatedDate =:updatedDate "
					+ "WHERE tpem.tariffId =:tariffId ")
					.setParameter("tariffId", tariffMasterDto.getTariffId())
					.setParameter("updatedBy", tariffMasterDto.getUpdatedBy())
					.setParameter("updatedDate", GlobalCommonDateUtils.getDate(tariffMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"))
			.executeUpdate();

			return new Response(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {
			logger.error("inactiveOldPaymentEntitlements failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("inactiveOldPaymentEntitlements failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateStatusForTariff(TariffMasterDto tariffMasterDto) throws ApplicationException {
		try {
			Integer count=0;
			if(tariffMasterDto.getStatus().equals('I'))
			{
			String sql=" SELECT cast(COUNT(*) as int) From (select tod.tariff_id from t_order_details tod WHERE tod.status='A' and tod.tariff_id =:tariffId  "
					+ " UNION "
					+ " select tod.tariff_id from service.m_company_contract_master tod WHERE tod.status='A' and tod.tariff_id =:tariffId ) as count ";
			SQLQuery query=sessionFactory.getCurrentSession().createSQLQuery(sql);
			 count=(Integer) query
					 .setParameter("tariffId", tariffMasterDto.getTariffId())
					 .uniqueResult();
			}
			if(count<1)
			{
				TariffMaster tarrifMaster = findById(tariffMasterDto.getTariffId());
				// BATCH-5 org-trust fix (write-by-ID class): the bulk status UPDATE was by-ID only,
				// zero org check — a forged tariffId flipped any org's tariff (live-proven: forged
				// org-1 session flipped org-999 victim TARIFF-999-X to 'I' before this guard). Verify.
				if (tarrifMaster == null || tarrifMaster.getOrganizationId() == null
						|| !tarrifMaster.getOrganizationId().equals(verifiedIdentityService.getVerifiedOrganizationId())) {
					return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
				}
			sessionFactory.getCurrentSession().createQuery("update TariffMaster tariff "
					+ "SET tariff.status =:status , "
					+ "tariff.updatedBy =:updatedBy , "
					+ "tariff.updatedDate =:updatedDate "
					+ "WHERE tariff.tariffId =:tariffId ")
					.setParameter("tariffId", tariffMasterDto.getTariffId())
					.setParameter("updatedBy", tariffMasterDto.getUpdatedBy())
					.setParameter("updatedDate", GlobalCommonDateUtils.getDate(tariffMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"))
					.setParameter("status", tariffMasterDto.getStatus())
			.executeUpdate();
			
			return new Response(SUCCESS, null, null, null, tarrifMaster.getUnitId());
			}
			else
			{
				return new Response(SUCCESS, null, "Processing Transaction", null, null);
			}

		} catch (HibernateException he) {
			logger.error("updateStatusForTariff failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateStatusForTariff failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response updateStatusForTariffPaymentMapper(TariffMasterDto tariffMasterDto) throws ApplicationException {
		try {
			// BATCH-5 org-trust fix (write-by-ID class): the bulk mapper-status UPDATE was
			// by mapper-ID only, zero org check — a forged mapper ID flipped any org's row.
			TariffPaymentEntitlementMapper mapper = (TariffPaymentEntitlementMapper) sessionFactory.getCurrentSession().get(TariffPaymentEntitlementMapper.class, tariffMasterDto.getTariffPaymentEntitlementMapperId());
			if (mapper == null || mapper.getOrganizationId() == null
					|| !mapper.getOrganizationId().equals(verifiedIdentityService.getVerifiedOrganizationId())) {
				return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
			}
			sessionFactory.getCurrentSession().createQuery("update TariffPaymentEntitlementMapper tpem "
					+ "SET tpem.status =:status , "
					+ "tpem.updatedBy =:updatedBy , "
					+ "tpem.updatedDate =:updatedDate "
					+ "WHERE tpem.tariffPaymentEntitlementMapperId =:tariffPaymentEntitlementMapperId ")
					.setParameter("tariffPaymentEntitlementMapperId", tariffMasterDto.getTariffPaymentEntitlementMapperId())
					.setParameter("updatedBy", tariffMasterDto.getUpdatedBy())
					.setParameter("updatedDate", GlobalCommonDateUtils.getDate(tariffMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"))
					.setParameter("status", tariffMasterDto.getStatus())
			.executeUpdate();

			return new Response(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {
			logger.error("updateStatusForTariffPaymentMapper failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateStatusForTariffPaymentMapper failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getActiveTariffMasterList(TariffMasterDto tariffMasterDto) throws ApplicationException {
		try {
			List<TariffMasterDto> tariffMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_TARIFF_MASTER_LIST")
					.setParameter("orgId", tariffMasterDto.getOrganizationId())
					.setParameter("unitId", tariffMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(TariffMasterDto.class)).list();
			return new Response(SUCCESS, null, null, tariffMasterDtosList, null);

		} catch (HibernateException he) {
			logger.error("getActiveTariffMasterList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getActiveTariffMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPaymentEntitlementListByTariffIdList(TariffMasterDto tariffMasterDto)
			throws ApplicationException {
		try {
			List<TariffMasterDto> tariffMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PAYMENT_ENTITLEMENT_LIST_BY_TARIFF_ID_LIST")
					.setParameterList("tariffIdList", tariffMasterDto.getTariffIdList())
					.setParameter("orgId", tariffMasterDto.getOrganizationId())
					.setParameter("orgUnitId", tariffMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(TariffMasterDto.class)).list();
			return new Response(SUCCESS, null, null, tariffMasterDtosList, null);

		} catch (HibernateException he) {
			logger.error("getPaymentEntitlementListByTariffIdList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getPaymentEntitlementListByTariffIdList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getVisitTypeListByTariffIdList(TariffMasterDto tariffMasterDto) throws ApplicationException {
		try {
			List<TariffMasterDto> tariffMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_VISIT_TYPE_LIST_BY_TARIFF_ID_LIST")
					.setParameterList("tariffIdList", tariffMasterDto.getTariffIdList())
					.setParameter("orgId", tariffMasterDto.getOrganizationId())
					.setParameter("orgUnitId", tariffMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(TariffMasterDto.class)).list();
			return new Response(SUCCESS, null, null, tariffMasterDtosList, null);

		} catch (HibernateException he) {
			logger.error("getVisitTypeListByTariffIdList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getVisitTypeListByTariffIdList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPatientTypeListByTariffIdList(TariffMasterDto tariffMasterDto) throws ApplicationException {
		try {
			List<TariffMasterDto> tariffMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PATIENT_TYPE_LIST_BY_TARIFF_ID_LIST")
					.setParameterList("tariffIdList", tariffMasterDto.getTariffIdList())
					.setParameter("orgId", tariffMasterDto.getOrganizationId())
					.setParameter("orgUnitId", tariffMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(TariffMasterDto.class)).list();
			return new Response(SUCCESS, null, null, tariffMasterDtosList, null);

		} catch (HibernateException he) {
			logger.error("getPatientTypeListByTariffIdList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getPatientTypeListByTariffIdList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBillingBedCategoryByTariffIdList(TariffMasterDto tariffMasterDto) throws ApplicationException {
		try {
			List<TariffMasterDto> tariffMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BILLING_BED_CATEGORY_BY_TARIFFID_LIST")
					.setParameterList("tariffIdList", tariffMasterDto.getTariffIdList())
					.setParameter("orgId", tariffMasterDto.getOrganizationId())
					.setParameter("orgUnitId", tariffMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(TariffMasterDto.class)).list();
			return new Response(SUCCESS, null, null, tariffMasterDtosList, null);

		} catch (HibernateException he) {
			logger.error("getBillingBedCategoryByTariffIdList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getBillingBedCategoryByTariffIdList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateStatusForTariffBillingBedCategoryMapper(TariffMasterDto tariffMasterDto)
			throws ApplicationException {
		try {
			// BATCH-5 org-trust fix (write-by-ID class): the bulk mapper-status UPDATE was
			// by mapper-ID only, zero org check — a forged mapper ID flipped any org's row.
			TariffBillingBedCategoryMapper mapper = (TariffBillingBedCategoryMapper) sessionFactory.getCurrentSession().get(TariffBillingBedCategoryMapper.class, tariffMasterDto.getTariffBillingBedCategoryMapperId());
			if (mapper == null || mapper.getOrganizationId() == null
					|| !mapper.getOrganizationId().equals(verifiedIdentityService.getVerifiedOrganizationId())) {
				return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
			}
			sessionFactory.getCurrentSession().createQuery("update TariffBillingBedCategoryMapper tpem "
					+ "SET tpem.status =:status , "
					+ "tpem.updatedBy =:updatedBy , "
					+ "tpem.updatedDate =:updatedDate "
					+ "WHERE tpem.tariffBillingBedCategoryMapperId =:tariffBillingBedCategoryMapperId ")
					.setParameter("tariffBillingBedCategoryMapperId", tariffMasterDto.getTariffBillingBedCategoryMapperId())
					.setParameter("updatedBy", tariffMasterDto.getUpdatedBy())
					.setParameter("updatedDate", GlobalCommonDateUtils.getDate(tariffMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"))
					.setParameter("status", tariffMasterDto.getStatus())
			.executeUpdate();

			return new Response(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {
			logger.error("updateStatusForTariffBillingBedCategoryMapper failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateStatusForTariffBillingBedCategoryMapper failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBillingBedCategoryListByTariffId(TariffMasterDto tariffMasterDto) throws ApplicationException {
		try {
			List<TariffMasterDto> tariffMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BILLING_BED_CATEGORY_LIST_BY_TARIFF_ID")
					.setParameter("tariffId", tariffMasterDto.getTariffId())
					.setParameter("orgId", tariffMasterDto.getOrganizationId())
					.setParameter("orgUnitId", tariffMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(TariffMasterDto.class)).list();
			return new Response(SUCCESS, null, null, tariffMasterDtosList, null);

		} catch (HibernateException he) {
			logger.error("getBillingBedCategoryListByTariffId failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getBillingBedCategoryListByTariffId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateStatusForTariffPatientTypeMapper(TariffMasterDto tariffMasterDto)
			throws ApplicationException {
		try {
			// BATCH-5 org-trust fix (write-by-ID class): the bulk mapper-status UPDATE was
			// by mapper-ID only, zero org check — a forged mapper ID flipped any org's row.
			TariffPatientTypeMapper mapper = (TariffPatientTypeMapper) sessionFactory.getCurrentSession().get(TariffPatientTypeMapper.class, tariffMasterDto.getTariffPatientTypeMapperId());
			if (mapper == null || mapper.getOrganizationId() == null
					|| !mapper.getOrganizationId().equals(verifiedIdentityService.getVerifiedOrganizationId())) {
				return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
			}
			sessionFactory.getCurrentSession().createQuery("update TariffPatientTypeMapper tpem "
					+ "SET tpem.status =:status , "
					+ "tpem.updatedBy =:updatedBy , "
					+ "tpem.updatedDate =:updatedDate "
					+ "WHERE tpem.tariffPatientTypeMapperId =:tariffPatientTypeMapperId ")
					.setParameter("tariffPatientTypeMapperId", tariffMasterDto.getTariffPatientTypeMapperId())
					.setParameter("updatedBy", tariffMasterDto.getUpdatedBy())
					.setParameter("updatedDate", GlobalCommonDateUtils.getDate(tariffMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"))
					.setParameter("status", tariffMasterDto.getStatus())
			.executeUpdate();

			return new Response(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {
			logger.error("updateStatusForTariffPatientTypeMapper failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateStatusForTariffPatientTypeMapper failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPatientTypeListByTariffId(TariffMasterDto tariffMasterDto) throws ApplicationException {
		try {
			List<TariffMasterDto> tariffMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PATIENT_TYPE_LIST_BY_TARIFF_ID")
					.setParameter("tariffId", tariffMasterDto.getTariffId())
					.setParameter("orgId", tariffMasterDto.getOrganizationId())
					.setParameter("orgUnitId", tariffMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(TariffMasterDto.class)).list();
			return new Response(SUCCESS, null, null, tariffMasterDtosList, null);

		} catch (HibernateException he) {
			logger.error("getPatientTypeListByTariffId failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getPatientTypeListByTariffId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateStatusForTariffVisitTypeMapper(TariffMasterDto tariffMasterDto) throws ApplicationException {
		try {
			// BATCH-5 org-trust fix (write-by-ID class): the bulk mapper-status UPDATE was
			// by mapper-ID only, zero org check — a forged mapper ID flipped any org's row.
			TariffVisitTypeMapper mapper = (TariffVisitTypeMapper) sessionFactory.getCurrentSession().get(TariffVisitTypeMapper.class, tariffMasterDto.getTariffVisitTypeMapperId());
			if (mapper == null || mapper.getOrganizationId() == null
					|| !mapper.getOrganizationId().equals(verifiedIdentityService.getVerifiedOrganizationId())) {
				return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
			}
			sessionFactory.getCurrentSession().createQuery("update TariffVisitTypeMapper tpem "
					+ "SET tpem.status =:status , "
					+ "tpem.updatedBy =:updatedBy , "
					+ "tpem.updatedDate =:updatedDate "
					+ "WHERE tpem.tariffVisitTypeMapperId =:tariffVisitTypeMapperId ")
					.setParameter("updatedBy", tariffMasterDto.getUpdatedBy())
					.setParameter("updatedDate", GlobalCommonDateUtils.getDate(tariffMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"))
					.setParameter("status", tariffMasterDto.getStatus())
					.setParameter("tariffVisitTypeMapperId", tariffMasterDto.getTariffVisitTypeMapperId())
			.executeUpdate();

			return new Response(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {
			logger.error("updateStatusForTariffVisitTypeMapper failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateStatusForTariffVisitTypeMapper failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getVisitTypeListByTariffId(TariffMasterDto tariffMasterDto) throws ApplicationException {
		try {
			List<TariffMasterDto> tariffMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_VISIT_TYPE_LIST_BY_TARIFF_ID")
					.setParameter("tariffId", tariffMasterDto.getTariffId())
					.setParameter("orgId", tariffMasterDto.getOrganizationId())
					.setParameter("orgUnitId", tariffMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(TariffMasterDto.class)).list();
			return new Response(SUCCESS, null, null, tariffMasterDtosList, null);

		} catch (HibernateException he) {
			logger.error("getVisitTypeListByTariffId failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getVisitTypeListByTariffId failed", e);
			throw new ServiceException(e.getMessage());
		}

}

	@Override
	public Response tariffMasterCount(TariffMasterDto tariffMasterDto) throws ApplicationException {
		try {
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(TariffMaster.class); 
			c.add(Restrictions.eq("organizationId", tariffMasterDto.getOrganizationId()));
			c.add(Restrictions.eq("unitId", tariffMasterDto.getUnitId()));
			c.setProjection(Projections.rowCount());  
			Long count = (Long)c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		} catch (HibernateException he) {
			logger.error("tariffMasterCount failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("tariffMasterCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response tariffPaymentEntitlementMapper(TariffMasterDto tariffMasterDto) throws ApplicationException {
		try {
			sessionFactory.getCurrentSession().createQuery("update TariffPaymentEntitlementMapper tpem "
					+ "SET tpem.status='I', "
					+ "tpem.updatedBy =:updatedBy , "
					+ "tpem.updatedDate =:updatedDate "
					+ "WHERE tpem.tariffId =:tariffId ")
					.setParameter("updatedBy", tariffMasterDto.getUpdatedBy())
					.setParameter("updatedDate", GlobalCommonDateUtils.getDate(tariffMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"))
					.setParameter("tariffId", tariffMasterDto.getTariffId())
			.executeUpdate();

			return new Response(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {
			logger.error("tariffPaymentEntitlementMapper failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("tariffPaymentEntitlementMapper failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response tariffVisitTypeMapper(TariffMasterDto tariffMasterDto) throws ApplicationException {
		try {
			sessionFactory.getCurrentSession().createQuery("update TariffVisitTypeMapper tpem "
					+ "SET tpem.status='I', "
					+ "tpem.updatedBy =:updatedBy , "
					+ "tpem.updatedDate =:updatedDate "
					+ "WHERE tpem.tariffId =:tariffId ")
					.setParameter("updatedBy", tariffMasterDto.getUpdatedBy())
					.setParameter("updatedDate", GlobalCommonDateUtils.getDate(tariffMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"))
					.setParameter("tariffId", tariffMasterDto.getTariffId())
			.executeUpdate();

			return new Response(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {
			logger.error("tariffVisitTypeMapper failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("tariffVisitTypeMapper failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response tariffTariffPatientTypeMapper(TariffMasterDto tariffMasterDto) throws ApplicationException {
		try {
			sessionFactory.getCurrentSession().createQuery("update TariffPatientTypeMapper tpem "
					+ "SET tpem.status='I', "
					+ "tpem.updatedBy =:updatedBy , "
					+ "tpem.updatedDate =:updatedDate "
					+ "WHERE tpem.tariffId =:tariffId ")
					.setParameter("updatedBy", tariffMasterDto.getUpdatedBy())
					.setParameter("updatedDate", GlobalCommonDateUtils.getDate(tariffMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"))
					.setParameter("tariffId", tariffMasterDto.getTariffId())
			.executeUpdate();

			return new Response(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {
			logger.error("tariffTariffPatientTypeMapper failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("tariffTariffPatientTypeMapper failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getTariffMasterListOpdBilling(TariffMasterDto tariffMasterDto) {
		try {
			List<TariffMasterDto> tariffMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TARIFF_MASTER_LIST_OPD_BILLING")
					.setParameter("orgId", tariffMasterDto.getOrganizationId())
					.setParameter("unitId", tariffMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(TariffMasterDto.class)).list();
			return new Response(SUCCESS, null, null, tariffMasterDtosList, null);

		} catch (HibernateException he) {
			logger.error("getTariffMasterListOpdBilling failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getTariffMasterListOpdBilling failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}
