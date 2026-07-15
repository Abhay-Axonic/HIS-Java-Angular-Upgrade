package com.param.global.billing.dao;

import org.springframework.stereotype.Repository;

import com.param.billing.global.model.PaymentDepositDetails;
import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dto.PaymentDepositDetailsDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;

@Repository
@SuppressWarnings("rawtypes")
public class PaymentDepositDetailsDaoImpl extends GenericDao<PaymentDepositDetails, Integer> implements IPaymentDepositDetailsDao, ICommonConstants{

	public PaymentDepositDetailsDaoImpl() {
		super(PaymentDepositDetails.class);
	}

	@Override
	public Response savePaymentDepositDetails(PaymentDepositDetailsDto dto) throws BillingException {
		try{
			PaymentDepositDetails depositDetails = new PaymentDepositDetails();
				depositDetails.setAccountNumber(dto.getAccountNumber());
				depositDetails.setAmount(dto.getAmount());
				depositDetails.setBalanceAmount(dto.getBalanceAmount());
				depositDetails.setRefundAmount(0.00);
				depositDetails.setConsumedAmount(0.00);
				depositDetails.setBankId(dto.getBankId());
				depositDetails.setCardHolderName(dto.getCardHolderName());
				depositDetails.setCardNo(dto.getCardNo());
				depositDetails.setCardTypeId(dto.getCardTypeId());
				depositDetails.setChequeDate(dto.getChequeDate() != null && !dto.getChequeDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getChequeDate(), "dd-M-yyyy") : null);
				depositDetails.setChequeNumber(dto.getChequeNumber());
				depositDetails.setCreatedBy(dto.getCreatedBy());
				depositDetails.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
				depositDetails.setDepositId(dto.getDepositId());
				depositDetails.setExpiryDate(dto.getExpiryDate() != null && !dto.getExpiryDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getExpiryDate(), "dd-M-yyyy") : null);
				depositDetails.setPaymentModeId(dto.getPaymentModeId());
				depositDetails.setUpdatedBy(dto.getUpdatedBy());
				depositDetails.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
				depositDetails.setHospitalAccountId(dto.getHospitalAccountId());
				depositDetails.setPaymentRecieptDetailsId(dto.getPaymentRecieptDetailsId());
				depositDetails.setTransactionNo(dto.getTransactionNo());
				depositDetails.setTransactionTypeId(dto.getTransactionTypeId());
				depositDetails.setTransTransactionDate(dto.getTransTransactionDate() != null && !dto.getTransTransactionDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getTransTransactionDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
				depositDetails.setSkipMpesaValidation(dto.getSkipMpesaValidation() != null ? dto.getSkipMpesaValidation() : false);
				depositDetails.setDeviceName(dto.getDeviceName()!= null ? dto.getDeviceName() : null);
				depositDetails.setTransactionDetails(dto.getTransactionDetails()!= null ? dto.getTransactionDetails() : null);
			save(depositDetails);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response updateDepositDetails(PaymentDepositDetailsDto paymentDepositDetailsDto) throws BillingException {
		try{
			sessionFactory.getCurrentSession().createQuery("UPDATE PaymentDepositDetails depo set "
					+ "depo.balanceAmount=:balanceAmount, "
					+ "depo.consumedAmount=:consumedAmount, "
					+ "depo.updatedBy=:updatedBy, "
					+ "depo.updatedDate=:updatedDate "
					+ "WHERE depo.depositMasterId=:depositMasterId ")
				.setParameter("balanceAmount", paymentDepositDetailsDto.getBalanceAmount())
				.setParameter("consumedAmount", paymentDepositDetailsDto.getConsumedAmount())
				.setParameter("updatedBy", paymentDepositDetailsDto.getUpdatedBy())
				.setParameter("updatedDate", GlobalCommonDateUtils.getDate(paymentDepositDetailsDto.getUpdatedDate(), "yyyy-mm-dd HH:mm:ss"))
			.executeUpdate();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(Exception e){
			
			throw e;
		}
	}
	
}
