package com.param.global.billing.dao;

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

import com.param.billing.global.model.BankBranchMaster;
import com.param.global.billing.dto.BankBranchMasterDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BankBranchMasterDaoImpl extends GenericDao<BankBranchMaster, Integer>
		implements IBankBranchMasterDao, ICommonConstants {

	final static Logger logger = Logger.getLogger(BankBranchMasterDaoImpl.class);

	@Autowired
	SessionFactory sessionFactory;

	public BankBranchMasterDaoImpl() {
		super(BankBranchMaster.class);
	}

	@Override
	public Response saveBankBranchMaster(BankBranchMasterDto bankBranchMasterDto) throws ApplicationException {
		try {
			BankBranchMaster bankBranchMaster = new BankBranchMaster();
			bankBranchMaster.setBranchDesc(bankBranchMasterDto.getBranchDesc());
			bankBranchMaster.setBranchAddress(bankBranchMasterDto.getBranchAddress());
			bankBranchMaster.setBranchCode(bankBranchMasterDto.getBranchCode());
			bankBranchMaster.setBankId(bankBranchMasterDto.getBankId());
			bankBranchMaster.setUnitId(bankBranchMasterDto.getUnitId());
			bankBranchMaster.setCreatedBy(bankBranchMasterDto.getCreatedBy());
			bankBranchMaster.setUpdatedBy(bankBranchMasterDto.getUpdatedBy());
			bankBranchMaster.setCreatedDate(GlobalCommonDateUtils.getDate(bankBranchMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			bankBranchMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(bankBranchMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			bankBranchMaster.setStatus(bankBranchMasterDto.getStatus());
			bankBranchMaster.setOrgnisationId(bankBranchMasterDto.getOrgnisationId());
			save(bankBranchMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
			logger.error("saveBankBranchMaster failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("saveBankBranchMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBankBranchMasterById(int branchId, int orgnisationId) throws ApplicationException {
		try {

			List<BankBranchMasterDto> BankBranchMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BANK_BRANCH_MASTER_BY_ID")
					.setParameter("orgnisationId", orgnisationId)
					.setParameter("branchId", branchId)
					.setResultTransformer(Transformers.aliasToBean(BankBranchMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, BankBranchMasterDtoList, null);
		} catch (HibernateException he) {
			logger.error("getBankBranchMasterById failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getBankBranchMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBankBranchMasterList(BankBranchMasterDto bankBranchMasterDto) throws ApplicationException {
		try {
			List<BankBranchMasterDto> BankBranchMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BANK_BRANCH_MASTER_LIST")
					.setParameter("orgnisationId", bankBranchMasterDto.getOrgnisationId())
					//.setParameter("unitId", bankBranchMasterDto.getUnitId())
					.setFirstResult(bankBranchMasterDto.getOffset() != null ? bankBranchMasterDto.getOffset() : 0)
					.setMaxResults(bankBranchMasterDto.getNoOfRecordsPerPage() != null ? bankBranchMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(BankBranchMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, BankBranchMasterDtoList, null);
		} catch (HibernateException he) {
			logger.error("getBankBranchMasterList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getBankBranchMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateBankBranchMaster(BankBranchMasterDto bankBranchMasterDto) throws ApplicationException {
		
		BankBranchMaster bankBranchMaster = findById(bankBranchMasterDto.getBranchId());
		
		bankBranchMaster.setBranchDesc(bankBranchMasterDto.getBranchDesc());
		bankBranchMaster.setBranchAddress(bankBranchMasterDto.getBranchAddress());
		bankBranchMaster.setBranchCode(bankBranchMasterDto.getBranchCode());
		bankBranchMaster.setBankId(bankBranchMasterDto.getBankId());
		bankBranchMaster.setUpdatedBy(bankBranchMasterDto.getUpdatedBy());
		bankBranchMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(bankBranchMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
		
		update(bankBranchMaster);
		
		return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
	}

	@Override
	public Response updateBankBranchMasterStatus(BankBranchMasterDto bankBranchMasterDto) throws ApplicationException {
		try {

			BankBranchMaster bankBranchMaster = findById(bankBranchMasterDto.getBranchId());
			bankBranchMaster.setStatus(bankBranchMasterDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			bankBranchMaster.setUpdatedBy(bankBranchMasterDto.getUpdatedBy());
			bankBranchMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(bankBranchMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(bankBranchMaster);
			return new Response<>(SUCCESS, COMMON_UPDATE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
			logger.error("updateBankBranchMasterStatus failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateBankBranchMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBankBranchMasterCount(BankBranchMasterDto bankBranchMasterDto) throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			Criteria c = session.createCriteria(BankBranchMaster.class);
			c.add(Restrictions.eq("orgnisationId", bankBranchMasterDto.getOrgnisationId()));
			//c.add(Restrictions.eq("unitId", bankBranchMasterDto.getUnitId()));
			c.setProjection(Projections.rowCount());
			Long count = (Long) c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		} catch (HibernateException he) {
			logger.error("getBankBranchMasterCount failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getBankBranchMasterCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBankBranchMasterByNameNotId(BankBranchMasterDto bankBranchMasterDto) throws ApplicationException {
		try {

			List<BankBranchMasterDto> BankBranchMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BANK_BRANCH_MASTER_BY_NAME_NOT_ID")
					.setParameter("orgnisationId", bankBranchMasterDto.getOrgnisationId())//added by minesh
					.setParameter("branchId", bankBranchMasterDto.getBranchId())
					.setParameter("branchDesc", bankBranchMasterDto.getBranchDesc())
					.setResultTransformer(Transformers.aliasToBean(BankBranchMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, BankBranchMasterDtoList, null);
		} catch (HibernateException he) {
			logger.error("getBankBranchMasterByNameNotId failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getBankBranchMasterByNameNotId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBankBranchMasterByName(BankBranchMasterDto bankBranchMasterDto) throws ApplicationException {
		try {
			List<BankBranchMasterDto> BankBranchMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BANK_BRANCH_MASTER_BY_NAME")
					.setParameter("orgnisationId", bankBranchMasterDto.getOrgnisationId())
					.setParameter("branchDesc", bankBranchMasterDto.getBranchDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(BankBranchMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, BankBranchMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error("getBankBranchMasterByName failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getBankBranchMasterByName failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getActiveBankBranchMasterList(BankBranchMasterDto bankBranchMasterDto) throws ApplicationException {
		try {
			List<BankBranchMasterDto> BankBranchMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_BANK_BRANCH_MASTER_LIST")
					.setParameter("orgnisationId", bankBranchMasterDto.getOrgnisationId())					
					.setResultTransformer(Transformers.aliasToBean(BankBranchMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, BankBranchMasterDtoList, null);
		} catch (HibernateException he) {
			logger.error("getActiveBankBranchMasterList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getActiveBankBranchMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}
