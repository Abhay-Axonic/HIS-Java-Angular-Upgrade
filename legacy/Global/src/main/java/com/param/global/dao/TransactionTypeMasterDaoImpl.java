package com.param.global.dao;

import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.global.model.TransactionTypeMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.CommonDto;
import com.param.global.dto.TransactionTypeMasterDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class TransactionTypeMasterDaoImpl extends GenericDao<TransactionTypeMaster, Integer> implements ITransactionTypeMasterDao, ICommonConstants{
	final static Logger logger = Logger.getLogger(TransactionTypeMasterDaoImpl.class);


	
	@Autowired
	private DozerBeanMapper mapper;

	@Autowired
	private SessionFactory sessionFactory;
	
	public TransactionTypeMasterDaoImpl() {
		super(TransactionTypeMaster.class);
	}

	@Override
	public Response getTransactionTypes(int orgId, int unitId) throws ApplicationException {
		try{
			List<CommonDto> listCommonDto = sessionFactory.getCurrentSession().getNamedQuery("GET_TRANSACTION_TYPES")
					.setParameter("orgId", orgId)
					//.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listCommonDto, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getTransactionTypeMasterList(TransactionTypeMasterDto transactionTypeMasterDto)
			throws ApplicationException {
		try {
			List<TransactionTypeMasterDto> transactionTypeMasterDtos = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TRANSACTION_TYPE_LIST")
					.setParameter("orgId", transactionTypeMasterDto.getOrgId())
					.setResultTransformer(Transformers.aliasToBean(TransactionTypeMasterDto.class)).list();
			return new Response(SUCCESS, null, null, transactionTypeMasterDtos, null);

		} catch (HibernateException he) {
		logger.error("getTransactionTypeMasterList failed", he);
		} catch (Exception e) {
		logger.error("getTransactionTypeMasterList failed", e);
		} finally {
			 ;
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getTransactionTypeByName(TransactionTypeMasterDto transactionTypeMasterDto)
			throws ApplicationException {
		try {
			List<TransactionTypeMasterDto> transactionTypeMasterDtos = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TRANSACTION_TYPE_LIST_BY_NAME")
					.setParameter("transactionTypeName", transactionTypeMasterDto.getTransactionTypeName().toLowerCase())
					.setParameter("orgId", transactionTypeMasterDto.getOrgId())
					.setResultTransformer(Transformers.aliasToBean(TransactionTypeMasterDto.class)).list();
			return new Response(SUCCESS, null, null, transactionTypeMasterDtos, null);

		} catch (HibernateException he) {
		logger.error("getTransactionTypeByName failed", he);
		} catch (Exception e) {
		logger.error("getTransactionTypeByName failed", e);
		} finally {
			 ;
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response saveTransactionTypeMaster(TransactionTypeMasterDto transactionTypeMasterDto)
			throws ApplicationException {
		try {
			TransactionTypeMaster transactionTypeMaster = mapper.map(transactionTypeMasterDto, TransactionTypeMaster.class,
					"transactionTypeMasterDto_to_transactionTypeMaster");
			transactionTypeMaster.setStatus('A');
			transactionTypeMaster = save(transactionTypeMaster);
			return new Response(SUCCESS, null, null, null, transactionTypeMaster);
		} catch (HibernateException he) {
		logger.error("saveTransactionTypeMaster failed", he);
		} catch (Exception e) {
		logger.error("saveTransactionTypeMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getTransactionTypeByNameNotId(TransactionTypeMasterDto transactionTypeMasterDto)
			throws ApplicationException {
		try {
			List<TransactionTypeMasterDto> transactionTypeMasterDtos = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TRANSACTION_TYPE_LIST_BY_NAME_NOT_ID")
					.setParameter("transactionTypeId", transactionTypeMasterDto.getTransactionTypeId())
					.setParameter("transactionTypeName", transactionTypeMasterDto.getTransactionTypeName().toLowerCase())
					.setParameter("orgId", transactionTypeMasterDto.getOrgId())
					.setResultTransformer(Transformers.aliasToBean(TransactionTypeMasterDto.class)).list();
			return new Response(SUCCESS, null, null, transactionTypeMasterDtos, null);

		} catch (HibernateException he) {
		logger.error("getTransactionTypeByNameNotId failed", he);
		} catch (Exception e) {
		logger.error("getTransactionTypeByNameNotId failed", e);
		} finally {
			 ;
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateTransactionTypeMaster(TransactionTypeMasterDto transactionTypeMasterDto)
			throws ApplicationException {
		try {
			TransactionTypeMaster transactionTypeMaster = findById(transactionTypeMasterDto.getTransactionTypeId());
			transactionTypeMaster.setTransactionTypeName(transactionTypeMasterDto.getTransactionTypeName());
			transactionTypeMaster.setTransactionTypeDesc(transactionTypeMasterDto.getTransactionTypeDesc());
			transactionTypeMaster.setUpdatedBy(transactionTypeMasterDto.getUpdatedBy());
			transactionTypeMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(transactionTypeMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(transactionTypeMaster);
			return new Response<TransactionTypeMasterDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, transactionTypeMaster);

		} catch (HibernateException he) {
		logger.error("updateTransactionTypeMaster failed", he);
			return new Response<TransactionTypeMasterDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
		logger.error("updateTransactionTypeMaster failed", e);
			return new Response<TransactionTypeMasterDto, Integer>(ERROR, null, null, null, null);
		} finally {
			 ;
		}
	}

	@Override
	public Response updateStatusForTransactionType(TransactionTypeMasterDto transactionTypeMasterDto)
			throws ApplicationException {
		try {

			TransactionTypeMaster transactionTypeMaster = findById(transactionTypeMasterDto.getTransactionTypeId());
			transactionTypeMaster.setUpdatedBy(transactionTypeMasterDto.getUpdatedBy());
			transactionTypeMaster.setStatus(transactionTypeMasterDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			transactionTypeMaster
					.setUpdatedDate(GlobalCommonDateUtils.getDate(transactionTypeMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(transactionTypeMaster);
			return new Response<TransactionTypeMasterDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, transactionTypeMaster.getOrgId());

		} catch (HibernateException he) {
		logger.error("updateStatusForTransactionType failed", he);
			return new Response<TransactionTypeMasterDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
		logger.error("updateStatusForTransactionType failed", e);
			return new Response<TransactionTypeMasterDto, Integer>(ERROR, null, null, null, null);
		} finally {
			 ;
		}
	}

}
