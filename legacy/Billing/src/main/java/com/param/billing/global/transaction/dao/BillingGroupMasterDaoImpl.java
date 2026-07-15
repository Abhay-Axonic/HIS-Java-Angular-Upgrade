package com.param.billing.global.transaction.dao;

import java.math.BigInteger;
import java.util.List;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.common.ICommonConstants;
import com.param.billing.common.IError;
import com.param.billing.common.Response;
import com.param.billing.global.dto.BillingGroupMasterDto;
import com.param.billing.global.model.BillingGroupMaster;
import com.param.billing.global.transaction.dto.BillingGroupSpecialityMasterDto;
import com.param.global.common.IBillingCommonConstants;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BillingGroupMasterDaoImpl extends GenericDao<BillingGroupMaster, Integer> implements IBillingGroupMasterDao, ICommonConstants, IBillingCommonConstants,IError{

	public BillingGroupMasterDaoImpl() {
		super(BillingGroupMaster.class);
	}

	@Autowired
	private DozerBeanMapper mapper;
	
	final static Logger logger = Logger.getLogger(BillingGroupMasterDaoImpl.class);
	
	@Override
	public Response saveBillingGroupMaster(BillingGroupSpecialityMasterDto billingGroupSpecialityMasterDto) throws ApplicationException {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("select count(*) from BillingGroupMaster bill where lower(bill.billingGroupDesc) LIKE lower(CONCAT('%',:billingGroupDesc,'%'))");
			
			Long count = (Long) sessionFactory.getCurrentSession().createQuery(sb.toString())
					.setParameter("billingGroupDesc", billingGroupSpecialityMasterDto.getBillingGroupDesc())
					.uniqueResult();
			boolean exist = count > 0;
			if(exist) {
				return new Response<>(ERROR, ALREADY_EXIST_CODE, BILLING_GROUP_DESC_ALREADY_EXIST, null, null);
			}
			else {
			BillingGroupMaster billingGroupMaster = mapper.map(billingGroupSpecialityMasterDto, BillingGroupMaster.class,"BillingGroupSpecialityMasterDto_to_BillingGroupMaster");
				billingGroupMaster.setStatus('A');
			billingGroupMaster = save(billingGroupMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, billingGroupMaster);
			}
		}catch (HibernateException he) {
			logger.error("Exection", he);
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getBillingGroupMasterList(BillingGroupMasterDto billingGroupMasterDto) throws ApplicationException {
		try {
			List<BillingGroupMasterDto> listBillingGroupMasterDto = sessionFactory.getCurrentSession().getNamedQuery("GET_BILLING_GROUP_MASTER_BY_UNIT")
					.setParameter("unitId", billingGroupMasterDto.getUnitId())
					.setParameter("orgId", billingGroupMasterDto.getOrgnisationId())
					.setFirstResult(billingGroupMasterDto.getOffset() == null ? 0 : billingGroupMasterDto.getOffset())
					.setMaxResults(billingGroupMasterDto.getRecordPerPage() == null ? 0 : billingGroupMasterDto.getRecordPerPage())
					.setResultTransformer(Transformers.aliasToBean(BillingGroupMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listBillingGroupMasterDto, null);
		}catch (HibernateException he) {
			logger.error("Exection", he);
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getBillingGroupMasterById(BillingGroupMasterDto billingGroupMasterDto) throws ApplicationException {
		try {
			List<BillingGroupMasterDto> listBillingGroupMasterDto = sessionFactory.getCurrentSession().getNamedQuery("GET_BILLING_GROUP_MASTER_BY_ID")
					.setParameter("billingGroupId", billingGroupMasterDto.getBillingGroupId())
					.setParameter("unitId", billingGroupMasterDto.getUnitId())
					.setParameter("orgId", billingGroupMasterDto.getOrgnisationId())
					.setResultTransformer(Transformers.aliasToBean(BillingGroupSpecialityMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listBillingGroupMasterDto, null);
		}catch (HibernateException he) {
			logger.error("Exection", he);
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response changeBillingGroupMasterStatus(BillingGroupMasterDto billingGroupMasterDto)
			throws ApplicationException {
		try {
			   sessionFactory.getCurrentSession().getNamedQuery("CHANGE_BILLING_GROUP_MASTER_STATUS")
			   .setParameter("status", billingGroupMasterDto.getStatus())
			   .setParameter("billingGroupId", billingGroupMasterDto.getBillingGroupId())
			   .setParameter("unitId", billingGroupMasterDto.getUnitId())
			   .setParameter("orgId", billingGroupMasterDto.getOrgnisationId())
			   .executeUpdate();
			   return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		}catch (HibernateException he) {
			logger.error("Exection", he);
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response updateBillingGroupMaster(BillingGroupSpecialityMasterDto billingGroupSpecialityMasterDto)
			throws ApplicationException {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("select count(*) from BillingGroupMaster bill where lower(bill.billingGroupDesc) LIKE lower(CONCAT('%',:billingGroupDesc,'%')) AND bill.billingGroupId !=:billingGroupId ");
			
			Long count = (Long) sessionFactory.getCurrentSession().createQuery(sb.toString())
					.setParameter("billingGroupDesc", billingGroupSpecialityMasterDto.getBillingGroupDesc())
					.setParameter("billingGroupId", billingGroupSpecialityMasterDto.getBillingGroupId())
					.uniqueResult();
			boolean exist = count > 0;
			if(exist) {
				return new Response<>(ERROR, ALREADY_EXIST_CODE, BILLING_GROUP_DESC_ALREADY_EXIST, null, null);
			}
			else {
				BillingGroupMaster billingGroupMaster = mapper.map(billingGroupSpecialityMasterDto, BillingGroupMaster.class, "BillingGroupSpecialityMasterDto_to_BillingGroupMaster");
				billingGroupMaster.setStatus('A');
				billingGroupMaster = update(billingGroupMaster);
				return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			}
		}catch (HibernateException he) {
			logger.error("Exection", he);
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getBillingGroupMasterTotalCount(BillingGroupMasterDto billingGroupMasterDto)
			throws ApplicationException {
		try {
			Long billingGroupCount =  (Long)sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BILLING_GROUP_MASTER_COUNT")
					.setParameter("orgId", billingGroupMasterDto.getOrgnisationId())
					.setParameter("unitId", billingGroupMasterDto.getUnitId())
					.uniqueResult();
			if (billingGroupCount > 0)
				return new Response(SUCCESS, SUCCESS_CODE, null, null, billingGroupCount);
			 else
				return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, BigInteger.ZERO);
		}catch (HibernateException he) {
			logger.error("Exection", he);
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	
}
