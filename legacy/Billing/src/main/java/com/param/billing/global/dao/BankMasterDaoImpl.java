package com.param.billing.global.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.param.billing.common.ICommonConstants;
import com.param.billing.common.Response;
import com.param.billing.global.dto.BankMasterDto;
import com.param.billing.global.model.BankMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.dto.CommonDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "unchecked", "rawtypes" })
public class BankMasterDaoImpl extends GenericDao<BankMaster, Integer> implements IBankMasterDao, ICommonConstants {

	@Autowired
	SessionFactory sessionFactory;
	public BankMasterDaoImpl() {
		super(BankMaster.class);
	}

	@Override
	public Response getActiveBankMaster(BankMasterDto bankMasterDto) throws ApplicationException {
		try {
			List bankMasterList = sessionFactory.getCurrentSession().getNamedQuery("GET_ACTIVE_BANK_MASTER")
					//.setInteger("unitId", bankMasterDto.getUnitId())
					.setParameter("orgId", bankMasterDto.getOrgnisationId())
					.setResultTransformer(Transformers.aliasToBean(BankMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, bankMasterList, null);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getBankMasterCount(BankMasterDto bankMasterDto) throws ApplicationException {
		try {
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(BankMaster.class); 
			c.add(Restrictions.eq("orgnisationId", bankMasterDto.getOrgnisationId()));
			c.setProjection(Projections.rowCount());  
			Long count = (Long)c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);

		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateBankMasterStatus(BankMasterDto bankMasterDto) throws ApplicationException {
		try {
			BankMaster bankMaster=findById(bankMasterDto.getBankId());
			bankMaster.setStatus(bankMasterDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			bankMaster.setUpdatedBy(bankMasterDto.getUpdatedBy());
			bankMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(bankMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(bankMaster);
			return new Response<>(SUCCESS, COMMON_UPDATE, COMMON_UPDATE, null, bankMaster.getOrgnisationId());

		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateBankMaster(BankMasterDto bankMasterDto) throws ApplicationException {
		try {
			
			BankMaster bankMaster=findById(bankMasterDto.getBankId());
			
			bankMaster.setBankAddress(bankMasterDto.getBankAddress());
			bankMaster.setBankCode(bankMasterDto.getBankCode());
			bankMaster.setBranchId(bankMasterDto.getBranchId());
			bankMaster.setBankName(bankMasterDto.getBankName());
			bankMaster.setOrgnisationId(bankMasterDto.getOrgnisationId());
			bankMaster.setUpdatedBy(bankMasterDto.getUpdatedBy());
			bankMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(bankMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			bankMaster.setUnitId(bankMasterDto.getUnitId());
			update(bankMaster);

            BankMasterDto responseDto = new BankMasterDto();
            responseDto.setBankAddress(bankMaster.getBankAddress());
            responseDto.setBankId(bankMaster.getBankId());
            responseDto.setBankCode(bankMaster.getBankCode());
            responseDto.setBranchId(bankMaster.getBranchId());
            responseDto.setBankName(bankMaster.getBankName());
            responseDto.setOrgnisationId(bankMaster.getOrgnisationId());
            responseDto.setUpdatedBy(bankMaster.getUpdatedBy());
            responseDto.setUpdatedDate(bankMaster.getUpdatedDate().toString());
            responseDto.setUnitId(bankMaster.getUnitId());
			return new Response<>(SUCCESS,SUCCESS_CODE , COMMON_UPDATE, null, responseDto);

		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getBankMasterList(BankMasterDto bankMasterDto) throws ApplicationException {
		try {
			List bankMasterList = sessionFactory.getCurrentSession().getNamedQuery("GET_BANK_MASTER_LIST")
					.setParameter("orgId", bankMasterDto.getOrgnisationId())
					.setFirstResult(bankMasterDto.getOffset() != null ? bankMasterDto.getOffset() : 0)
					.setMaxResults(bankMasterDto.getNoOfRecordsPerPage() != null ? bankMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(BankMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, bankMasterList, null);

		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getBankMasterById(int bankId, int orgId) throws ApplicationException {
		try {
			List bankMasterList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BANK_MASTER_LIST_BY_ID")
					.setParameter("orgId", orgId)
					.setParameter("bankId", bankId)
					.setResultTransformer(Transformers.aliasToBean(BankMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, bankMasterList, null);

		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveBankMaster(BankMasterDto bankMasterDto) throws ApplicationException {
		try {
			BankMaster bankMaster=new BankMaster();
			bankMaster.setBankAddress(bankMasterDto.getBankAddress());
			bankMaster.setBankCode(bankMasterDto.getBankCode());
			bankMaster.setBankName(bankMasterDto.getBankName());
			bankMaster.setBranchId(bankMasterDto.getBranchId());
			bankMaster.setCreatedBy(bankMasterDto.getCreatedBy());
			bankMaster.setCreatedDate(GlobalCommonDateUtils.getDate(bankMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			bankMaster.setOrgnisationId(bankMasterDto.getOrgnisationId());
			bankMaster.setStatus(bankMasterDto.getStatus());
			bankMaster.setUpdatedBy(bankMasterDto.getUpdatedBy());
			bankMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(bankMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			bankMaster.setUnitId(bankMasterDto.getUnitId());
			save(bankMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, bankMaster);

		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getBankMasterByNameNotId(BankMasterDto bankMasterDto) throws ApplicationException {
		try {
			
			List bankMasterList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BANKMASTER_BY_NAME_NOT_ID")
					.setParameter("orgId", bankMasterDto.getOrgnisationId())//addded by minesh
					.setParameter("bankId", bankMasterDto.getBankId())
					.setParameter("bankName", bankMasterDto.getBankName().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(BankMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, bankMasterList, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getBankMasterByName(BankMasterDto bankMasterDto) throws ApplicationException {
		try {
			List bankMasterList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BANK_MASTER_BY_NAME")
					.setParameter("orgId", bankMasterDto.getOrgnisationId())//addded by m	inesh
					.setParameter("bankName", bankMasterDto.getBankName().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(BankMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, bankMasterList, null);

		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getHospitalBank(Integer orgId, Integer unitId) throws ApplicationException {
		try {
			List list = sessionFactory.getCurrentSession().getNamedQuery("GET_HOSPITAL_BANK")
					.setParameter("unitId", unitId)
					.setParameter("orgId", orgId)
					.setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
