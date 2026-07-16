package com.param.billing.global.transaction.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.billing.global.transaction.dao.IPaymentRecieptDetailsService;
import com.param.billing.global.transaction.dao.IPaymentRecieptMasterDao;
import com.param.billing.global.transaction.model.PaymentRecieptMaster;
import com.param.global.billing.dto.PaymentReceiptDetailsDto;
import com.param.global.billing.dto.PaymentRecieptMasterDto;
import com.param.global.billing.service.IDepositMasterService;
import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.global.org.common.dao.IPatientLoyaltyCardMapperDao;
import com.param.global.org.common.dto.PatientLoyaltyCardMapperDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class PaymentRecieptMasterServiceImpl implements IPaymentRecieptMasterService,ICommonConstants{
	
	
	@Autowired
	IPaymentRecieptMasterDao iPaymentRecieptMasterDao;
	
	@Autowired
	IPaymentRecieptDetailsService iPaymentRecieptDetailsService;
	
	@Autowired
	IPatientLoyaltyCardMapperDao iPatientLoyaltyCardMapperDao;
	
	@Autowired
	IDepositMasterService iDepositMasterService;

	@Autowired
	private IGlobalUnitMasterDao iGlobalUnitMasterDao;

	@Override
	//@Transactional
	public Response savePaymentReciept(PaymentRecieptMasterDto paymentRecieptMasterDto,Double payeeDueAmount,double depositCounsumtionAmount) throws ApplicationException {
		try{
			Double amountCollected = 0.0;
			Double dueAmount = 0.0;
			List<PatientLoyaltyCardMapperDto>  mailList =  new 	ArrayList<PatientLoyaltyCardMapperDto>();  
			List<PaymentReceiptDetailsDto> forTransferToActiveDepositDetails = new LinkedList<PaymentReceiptDetailsDto>();
			
			paymentRecieptMasterDto.setRecieptNumber(generateRecieptNumber(paymentRecieptMasterDto.getUnitId()));
			
			if(paymentRecieptMasterDto.getAmount() > paymentRecieptMasterDto.getPayeeDueAmount())
			{
				paymentRecieptMasterDto.setDummyAmount(paymentRecieptMasterDto.getAmount());
				paymentRecieptMasterDto.setAmount(paymentRecieptMasterDto.getPayeeDueAmount());
			}else 
			{
				paymentRecieptMasterDto.setDummyAmount(0.0);
				//paymentRecieptMasterDto.setAmount(paymentRecieptMasterDto.getPayeeDueAmount());
			}
			
			PaymentRecieptMaster paymentRecieptDto = new PaymentRecieptMaster();
			
			Response res = iPaymentRecieptMasterDao.savePaymentRecieptMaster(paymentRecieptMasterDto);
			paymentRecieptDto  = (PaymentRecieptMaster) res.getObject();
			Integer paymentRecieptId =  paymentRecieptDto.getPaymentRecieptId();
			paymentRecieptMasterDto.setPaymentRecieptId(paymentRecieptId);
			//------------if we are consuming whole amount from the deposit then there will not be any entry in PaymentReceiptDetails.
			if(paymentRecieptMasterDto.getPaymentReceiptDetailsDtosList()!=null && paymentRecieptMasterDto.getPaymentReceiptDetailsDtosList().size()>0)
			{
				
				if((paymentRecieptMasterDto.getDummyAmount() > paymentRecieptMasterDto.getPayeeDueAmount()))
				{
					Response transferToAvtiveDepositRes = iDepositMasterService.transferToAvtiveDeposit(paymentRecieptMasterDto);
					if(transferToAvtiveDepositRes.getStatus().equals(SUCCESS)){
						paymentRecieptMasterDto.setPaymentReceiptDetailsDtosList(transferToAvtiveDepositRes.getListObject());
					}
				}
				
				Iterator<PaymentReceiptDetailsDto> iterator = paymentRecieptMasterDto.getPaymentReceiptDetailsDtosList().iterator();
				while(iterator.hasNext())
				{
					
					PaymentReceiptDetailsDto paymentReceiptDetailsDto = iterator.next();
						paymentReceiptDetailsDto.setPaymentReceiptMasterId(paymentRecieptId);
						paymentReceiptDetailsDto.setOrgnizationId(paymentRecieptMasterDto.getOrgnizationId());
						paymentReceiptDetailsDto.setUnitId(paymentRecieptMasterDto.getUnitId());
						paymentReceiptDetailsDto.setCreatedBy(paymentRecieptMasterDto.getCreatedBy());
						paymentReceiptDetailsDto.setCreatedDate(paymentRecieptMasterDto.getCreatedDate());
						paymentReceiptDetailsDto.setUpdatedBy(paymentRecieptMasterDto.getUpdatedBy());
						paymentReceiptDetailsDto.setUpdatedDate(paymentRecieptMasterDto.getUpdatedDate());
						paymentReceiptDetailsDto.setStatus('A');
						paymentReceiptDetailsDto.setDateOfTransaction(paymentRecieptMasterDto.getUpdatedDate());
						paymentReceiptDetailsDto.setIsCancle('N');
						paymentReceiptDetailsDto.setStatus('A');
						paymentReceiptDetailsDto.setAmount(paymentReceiptDetailsDto.getAmount()+(paymentReceiptDetailsDto.getAvailableFund()!=null ? paymentReceiptDetailsDto.getAvailableFund() : 0.00));
						paymentReceiptDetailsDto.setAvailableFund(paymentReceiptDetailsDto.getAvailableFund()!=null ? paymentReceiptDetailsDto.getAvailableFund() : 0.00);
						if(paymentReceiptDetailsDto.getChequeDate()!=null)
						{
							paymentReceiptDetailsDto.setChequeDate(paymentReceiptDetailsDto.getChequeDate().concat(" 00:00:00"));
						    paymentReceiptDetailsDto.setHospitalAccountId(paymentReceiptDetailsDto.getHospitalAccountId());
						} 
						if(paymentReceiptDetailsDto.getExpiryDate()!=null)
							paymentReceiptDetailsDto.setExpiryDate(paymentReceiptDetailsDto.getExpiryDate().concat(" 00:00:00"));
						
						if(paymentReceiptDetailsDto.getPaymentModeId()!=null && paymentReceiptDetailsDto.getPaymentModeId().intValue()==6)
						{
							PatientLoyaltyCardMapperDto dto =new PatientLoyaltyCardMapperDto();
							dto.setCardAmount(paymentReceiptDetailsDto.getAmount());
							dto.setCardNo(paymentReceiptDetailsDto.getCardNumber());
							dto.setCreatedBy(paymentRecieptMasterDto.getCreatedBy());
							dto.setCreatedDate(paymentRecieptMasterDto.getCreatedDate());
							dto.setOrganisationId(paymentRecieptMasterDto.getOrgnizationId());
							dto.setUnitId(paymentRecieptMasterDto.getUnitId());
							Response loyalityCardResponse=iPatientLoyaltyCardMapperDao.getPatientDetailsByLoyaltiCardNo(dto);
							if(loyalityCardResponse.getListObject().size() >0 && loyalityCardResponse.getListObject()!=null)
							{
								PatientLoyaltyCardMapperDto patientLoyaltyCardMapperDto=(PatientLoyaltyCardMapperDto)loyalityCardResponse.getListObject().getFirst();
								//checking that front end amount should be less than or equal to card amount
								if(patientLoyaltyCardMapperDto.getCardAmount()>=paymentReceiptDetailsDto.getAmount())
								{
									patientLoyaltyCardMapperDto.setConsumeAmount(paymentReceiptDetailsDto.getAmount());
									iPatientLoyaltyCardMapperDao.updateLoyaltyCardAmountByCard(dto);
									mailList.add(patientLoyaltyCardMapperDto);
									
									paymentReceiptDetailsDto.setLoyaltyCardId(patientLoyaltyCardMapperDto.getLoyaltyCardId());
									
								}else{
									try{
										throw new Exception();
									}catch(Exception e){
										
										return new Response(ERROR, ERROR_CODE, "Amount should be less than or equals to card amount", null, 0.0);
									}
								}
							}
							
						}
					Response res2 = iPaymentRecieptDetailsService.savePaymentRecieptDetails(paymentReceiptDetailsDto);
					if(res2.getStatus().equals(SUCCESS))
					{
						amountCollected += paymentReceiptDetailsDto.getAmount();
						dueAmount = payeeDueAmount - amountCollected;
						
						PaymentReceiptDetailsDto detailsDto = (PaymentReceiptDetailsDto)res2.getObject();
						if(detailsDto.getAvailableFund()!=null && detailsDto.getAvailableFund()>0)
							forTransferToActiveDepositDetails.add(detailsDto);
					}
					
				}
			}else
			{
				dueAmount=payeeDueAmount;
			}
			
			if(depositCounsumtionAmount>0)
			{
				PaymentReceiptDetailsDto paymentReceiptDetailsDto = new PaymentReceiptDetailsDto();
					paymentReceiptDetailsDto.setPaymentReceiptMasterId(paymentRecieptId);
					paymentReceiptDetailsDto.setOrgnizationId(paymentRecieptMasterDto.getOrgnizationId());
					paymentReceiptDetailsDto.setUnitId(paymentRecieptMasterDto.getUnitId());
					paymentReceiptDetailsDto.setCreatedBy(paymentRecieptMasterDto.getCreatedBy());
					paymentReceiptDetailsDto.setCreatedDate(paymentRecieptMasterDto.getCreatedDate());
					paymentReceiptDetailsDto.setUpdatedBy(paymentRecieptMasterDto.getUpdatedBy());
					paymentReceiptDetailsDto.setUpdatedDate(paymentRecieptMasterDto.getUpdatedDate());
					paymentReceiptDetailsDto.setStatus('A');
					paymentReceiptDetailsDto.setDateOfTransaction(paymentRecieptMasterDto.getUpdatedDate());
					paymentReceiptDetailsDto.setIsCancle('N');
					paymentReceiptDetailsDto.setStatus('A');
					paymentReceiptDetailsDto.setAmount(depositCounsumtionAmount);
					paymentReceiptDetailsDto.setPaymentModeId(10);
					paymentReceiptDetailsDto.setAvailableFund(0.00);
				iPaymentRecieptDetailsService.savePaymentRecieptDetails(paymentReceiptDetailsDto);
			}
			
			return new Response<>(SUCCESS,paymentRecieptDto.getRecieptNumber(),paymentRecieptId.toString() , mailList, dueAmount>0 ? dueAmount : 0);
			
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	private String generateRecieptNumber(Integer unitId) {
		try {
			/*Long incrementedId = iPaymentRecieptMasterDao.getNextValRecieptMasterId();
			String billPrefix="RN-18-";
			
			StringBuilder bill=new StringBuilder(billPrefix);
			
				int length = Long.valueOf(incrementedId).toString().length();
				bill = length == 1 ? (bill.append("000000").append(incrementedId))
						: length == 2 ? (bill.append("00000").append(incrementedId))
								: length == 3 ? (bill.append("0000").append(incrementedId))
										: length == 4 ? (bill.append("000").append(incrementedId))
												: length == 5 ? (bill.append("00").append(incrementedId))
														: length == 6 ? (bill.append("0").append(incrementedId))
																: length == 7 ? (bill.append(incrementedId)) : bill.append("");*/
				return iGlobalUnitMasterDao.getGeneratedNo(0, unitId, IBillingCommonConstants.RECIEPT_NUMBER);
			} catch (Exception e) {
				
			}
			
		return "error";
	}
	
}
