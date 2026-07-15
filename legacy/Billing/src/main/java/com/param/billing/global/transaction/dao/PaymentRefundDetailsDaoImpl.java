package com.param.billing.global.transaction.dao;

import org.springframework.stereotype.Repository;

import com.param.billing.global.transaction.dto.PaymentRefundDetailsDto;
import com.param.billing.global.transaction.model.PaymentRefundDetails;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;

@Repository
@SuppressWarnings("rawtypes")
public class PaymentRefundDetailsDaoImpl extends GenericDao<PaymentRefundDetails, Integer> implements IPaymentRefundDetailsDao, ICommonConstants{

	public PaymentRefundDetailsDaoImpl() {
		super(PaymentRefundDetails.class);
	}

	@Override
	public Response savePaymentRefundDetails(PaymentRefundDetailsDto dto) throws BillingException {
		try{
			PaymentRefundDetails PaymentrefundDetails = new PaymentRefundDetails();
			PaymentrefundDetails.setPaymentRefundId(dto.getPaymentRefundId());
			PaymentrefundDetails.setPaymentModeId(dto.getPaymentModeId());
			PaymentrefundDetails.setAmount(dto.getAmount());
			PaymentrefundDetails.setBankId(dto.getBankId());
			PaymentrefundDetails.setAccountNumber(dto.getAccountNumber());
			PaymentrefundDetails.setCardNo(dto.getCardNo());
			PaymentrefundDetails.setCardHolderName(dto.getCardHolderName());
			PaymentrefundDetails.setDateOfRefund(dto.getDateOfRefund() != null && !dto.getDateOfRefund().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getDateOfRefund(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			PaymentrefundDetails.setStatus(dto.getStatus());
			PaymentrefundDetails.setOrgnisationId(dto.getOrgnisationId());
			PaymentrefundDetails.setUnitId(dto.getUnitId());
			PaymentrefundDetails.setChequeNumber(dto.getChequeNumber());
			PaymentrefundDetails.setCreatedBy(dto.getCreatedBy());
			PaymentrefundDetails.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			PaymentrefundDetails.setUpdatedBy(dto.getUpdatedBy());
			PaymentrefundDetails.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			PaymentrefundDetails.setHospitalAccountId(dto.getHospitalAccountId());
			PaymentrefundDetails.setTransactionNo(dto.getTransactionNo());
			PaymentrefundDetails.setTransactionTypeId(dto.getTransactionTypeId());
			PaymentrefundDetails.setTransTransactionDate(dto.getTransTransactionDate() != null && !dto.getTransTransactionDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getTransTransactionDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			
			save(PaymentrefundDetails);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

}
