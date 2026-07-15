package com.param.global.billing.dao;


import com.param.billing.global.model.FranchiseDepositDetails;
import com.param.billing.global.model.PaymentDepositDetails;
import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dto.FranchiseDepositDetailsDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.utility.TimeZoneComponent;
import in.param.common.dao.GenericDao;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentFrDepositDetailsDaoImpl  extends GenericDao<FranchiseDepositDetails, Integer> implements IPaymentFrDepositDetailsDao, ICommonConstants {

    public PaymentFrDepositDetailsDaoImpl() {
        super(FranchiseDepositDetails.class);
    }

    @Override
    public Response savePaymentFrDepositDetails(FranchiseDepositDetailsDto dto) throws BillingException {
        try{
            FranchiseDepositDetails depositDetails = new FranchiseDepositDetails();
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
            depositDetails.setFrDepositId(dto.getFrDepositId());
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
}
