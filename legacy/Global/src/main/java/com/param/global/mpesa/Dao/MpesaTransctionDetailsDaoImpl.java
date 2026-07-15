package com.param.global.mpesa.Dao;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.entity.model.mpesa.MpesaTransactionDetails;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.mpesa.dto.MpesaTransctionDetailsDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class MpesaTransctionDetailsDaoImpl extends GenericDao<MpesaTransactionDetails, Integer> implements IMpesaTransctionDetailsDao, ICommonConstants{

	final static Logger logger = Logger.getLogger(MpesaTransctionDetailsDaoImpl.class);

	public MpesaTransctionDetailsDaoImpl() {
		super(MpesaTransactionDetails.class);
	}

	@Override
	public Response saveSTKPushPayment(MpesaTransctionDetailsDto mpesaTransctionDetailsDto)throws ApplicationException  {
		try {
			
			MpesaTransactionDetails mpesaTransactionDetails = new MpesaTransactionDetails();
			
			mpesaTransactionDetails.setAmount(mpesaTransctionDetailsDto.getAmount());
			mpesaTransactionDetails.setCheckoutRequestId(mpesaTransctionDetailsDto.getCheckoutRequestId());
			mpesaTransactionDetails.setCreatedBy(mpesaTransctionDetailsDto.getCreatedBy());
			mpesaTransactionDetails.setCreatedDate(GlobalCommonDateUtils.getDate(mpesaTransctionDetailsDto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
			mpesaTransactionDetails.setCustomerMessage(mpesaTransctionDetailsDto.getCustomerMessage());
			mpesaTransactionDetails.setMerchantRequestId(mpesaTransctionDetailsDto.getMerchantRequestId());
			mpesaTransactionDetails.setOrganizationId(mpesaTransctionDetailsDto.getOrganizationId());
			mpesaTransactionDetails.setPartyA(mpesaTransctionDetailsDto.getPartyA());
			mpesaTransactionDetails.setPhoneNumber(mpesaTransctionDetailsDto.getPhoneNumber()); 
			mpesaTransactionDetails.setResponseCode(mpesaTransctionDetailsDto.getResponseCode());
			mpesaTransactionDetails.setResponseDescription(mpesaTransctionDetailsDto.getResponseDescription());
			//mpesaTransactionDetails.setResultCode(mpesaTransctionDetailsDto.getResultCode());
			//mpesaTransactionDetails.setResultDesc(mpesaTransctionDetailsDto.getResultDesc());
			mpesaTransactionDetails.setStatus('A');
			mpesaTransactionDetails.setUhidNumber(mpesaTransctionDetailsDto.getUhidNumber()!= null ? mpesaTransctionDetailsDto.getUhidNumber() : null);
			mpesaTransactionDetails.setUnitId(mpesaTransctionDetailsDto.getUnitId());
			mpesaTransactionDetails.setUpdatedBy(mpesaTransctionDetailsDto.getUpdatedBy());
			mpesaTransactionDetails.setUpdatedDate(GlobalCommonDateUtils.getDate(mpesaTransctionDetailsDto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
			mpesaTransactionDetails.setUsed(false);
		
			mpesaTransactionDetails= save(mpesaTransactionDetails);
			
			return new Response<>(SUCCESS, SUCCESS_CODE, mpesaTransctionDetailsDto.getCustomerMessage(), null, mpesaTransactionDetails);

	}catch(Exception e) {
			logger.error("saveSTKPushPayment failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response validateSTKPushPayment(MpesaTransctionDetailsDto mpesaTransctionDetailsDto)
			throws ApplicationException {
		try{
			MpesaTransctionDetailsDto dto =(MpesaTransctionDetailsDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_MPESA_DETAILS_BY_UHID")
					.setParameter("organizationId", mpesaTransctionDetailsDto.getOrganizationId())
					.setParameter("unitId", mpesaTransctionDetailsDto.getUnitId())
					.setParameter("uhid", mpesaTransctionDetailsDto.getUhidNumber()!=  null ?  mpesaTransctionDetailsDto.getUhidNumber(): "" )
					.setParameter("checkoutRequestId", mpesaTransctionDetailsDto.getCheckoutRequestId() !=  null ? mpesaTransctionDetailsDto.getCheckoutRequestId()  : "" )
					.setMaxResults(1)
					.setResultTransformer(Transformers.aliasToBean(MpesaTransctionDetailsDto.class)).uniqueResult();
			
				return new Response<>(SUCCESS, SUCCESS_CODE,SUCCESS_MESSAGE, null, dto);

		}catch(HibernateException he){
			logger.error("validateSTKPushPayment failed", he);
			throw new ServiceException(he.getMessage());
		}catch(Exception e){
			logger.error("validateSTKPushPayment failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateSTKPushSimulation(MpesaTransctionDetailsDto dto)
			throws ApplicationException {
		try{
			
			MpesaTransactionDetails mstr = findById(dto.getMpesaTransactionId());
		
			mstr.setResponseCode(dto.getResponseCode());
			mstr.setResponseDescription(dto.getResponseDescription());
			mstr.setResultCode(dto.getResultCode());
			mstr.setResultDesc(dto.getResultDesc());
			mstr.setAmount(dto.getAmount());
			mstr=update(mstr);
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, mstr);
			}catch(HibernateException he){
				logger.error("updateSTKPushSimulation failed", he);
				throw new ServiceException(he.getMessage());
			}catch(Exception e){
				logger.error("updateSTKPushSimulation failed", e);
				throw new ServiceException(e.getMessage());
			}
	}



}
