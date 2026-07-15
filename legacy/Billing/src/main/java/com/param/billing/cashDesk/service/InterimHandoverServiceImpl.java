package com.param.billing.cashDesk.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.billing.cashDesk.dao.IInterimHandoverMasterDao;
import com.param.billing.cashDesk.dto.BillingIdDetailsDto;
import com.param.billing.cashDesk.dto.DenominationMasterDto;
import com.param.billing.cashDesk.dto.InterimHandoverMasterDto;
import com.param.billing.global.model.InterimHandoverMaster;
import com.param.global.billing.dto.PaymentReceiptDetailsDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.DenominationDetailsMasterDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes","unused","unchecked"})
public class InterimHandoverServiceImpl implements IInterimHandoverMasterService,ICommonConstants{

	@Autowired
	private IInterimHandoverMasterDao iInterimHandoverMasterDao;

	@Override
	@Transactional
	public Response saveInterimHandover(InterimHandoverMasterDto interimHandoverMasterDto) {
		try{
			
			Response saveResponse =null;
				 saveResponse = iInterimHandoverMasterDao.saveInterimHandover(interimHandoverMasterDto);
				if(saveResponse.getStatus().equals(SUCCESS)) {
				
					InterimHandoverMaster mstr = (InterimHandoverMaster)saveResponse.getObject();
					for (DenominationDetailsMasterDto dto : interimHandoverMasterDto.getDenominationDetailsMasterList()) {
						dto.setInterimHandoverTransactionId(mstr.getId());
					}
				   iInterimHandoverMasterDao.saveDenominations(interimHandoverMasterDto.getDenominationDetailsMasterList());
				}
			return saveResponse;
		}catch(Exception e){
			
			throw e;
		}
//		return new Response(ERROR, null, null, null,null);
	}

	@Override
	@Transactional
	public Response getListOfDenominations(DenominationMasterDto denominationMasterDto) {
		try{
			return iInterimHandoverMasterDao.getListOfDenominations(denominationMasterDto);
		}catch(Exception e){
			
			throw e;
		}
//		return new Response(ERROR, null, null, null,null);
	}

	@Override
	@Transactional
	public Response saveDenominations(List<DenominationDetailsMasterDto> denominationDetailsMasterDto) {
		try{
			return iInterimHandoverMasterDao.saveDenominations(denominationDetailsMasterDto);
		}catch(Exception e){
			
			throw e;
		}
//		return new Response(ERROR, null, null, null,null);
	}

	@Override
	@Transactional
	public Response getInterimHandoverList(InterimHandoverMasterDto interimHandoverMasterDto) {
		try{
			return iInterimHandoverMasterDao.getInterimHandoverList(interimHandoverMasterDto);
		}catch(Exception e){
			
			throw e;
		}
//		return new Response(ERROR, null, null, null,null);
	}

	/*@Override
	@Transactional
	public Response validateUser1(InterimHandoverMasterDto interimHandoverMasterDto) {
		try{
			return iInterimHandoverMasterDao.validateUser1(interimHandoverMasterDto);
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}
*/
	@Override
	@Transactional
	public Response getICHNumberList(InterimHandoverMasterDto interimHandoverMasterDto) {
		try{
			return iInterimHandoverMasterDao.getICHNumberList(interimHandoverMasterDto);
		}catch(Exception e){
			
			throw e;
//			return new Response(ERROR, null, null, null,null);
		}
	}

	@Override
	@Transactional
	public Response getDenominationDetailsList(DenominationDetailsMasterDto denominationDetailsMasterDto) {
		try{
			return iInterimHandoverMasterDao.getDenominationDetailsList(denominationDetailsMasterDto);
		}catch(Exception e){
			
			throw e;
//			return new Response(ERROR, null, null, null,null);
		}
	}

	@Override
	@Transactional
	public Response getChequeList(PaymentReceiptDetailsDto paymentReceiptDetailsDto) {
		try{
			return iInterimHandoverMasterDao.getChequeList(paymentReceiptDetailsDto);
		}catch(Exception e){
			
			throw e;
//			return new Response(ERROR, null, null, null,null);
		}
	}

	@Override
	@Transactional
	public Response getCardTransactionList(PaymentReceiptDetailsDto paymentDepositDetailsDto) {
		try{
			return iInterimHandoverMasterDao.getCardTransactionList(paymentDepositDetailsDto);
		}catch(Exception e){
			
			throw e;
//			return new Response(ERROR, null, null, null,null);
		}
	}

	@Override
	@Transactional
	public Response saveChequeInterimTransaction(InterimHandoverMasterDto interimHandoverMasterDto) {
		try{
			
			Response validateRes = iInterimHandoverMasterDao.validateUser1(interimHandoverMasterDto);
			Response res =null;
			if(validateRes.getStatus().equals(SUCCESS)) {
				
				Response saveResponse = iInterimHandoverMasterDao.saveChequeInterimTransaction(interimHandoverMasterDto);
				if(saveResponse.getStatus().equals(SUCCESS)) {
				
					List<PaymentReceiptDetailsDto> list = new ArrayList<>();
					
					for(BillingIdDetailsDto dto : interimHandoverMasterDto.getListOfIds())
						{
							PaymentReceiptDetailsDto paydto = new PaymentReceiptDetailsDto();
								paydto.setPaymentRecieptDetailsId(dto.getPaymentReceiptDetailsId());
								paydto.setOrgnizationId(interimHandoverMasterDto.getOrganizationId());
								paydto.setUnitId(interimHandoverMasterDto.getUnitId());
								paydto.setInterimCardStatus("D");
								list.add(paydto);
						}
							if(list.size()>0  || list!= null) {
								res= iInterimHandoverMasterDao.updateInterimChequeStatus(list);
							}	
				}
				
			}else {
				return new Response<>(ERROR, COMMON_ERROR_CODE, "Invalid Security Code", null, null);
			}
			
			return res;
			
		}catch(Exception e){
			
			throw e;
//			return new Response(ERROR, null, null, null,null);
		}
	}

	@Override
	@Transactional
	public Response saveCardInterimTransaction(InterimHandoverMasterDto interimHandoverMasterDto) {
		try{
			
			
			Response validateRes = iInterimHandoverMasterDao.validateUser1(interimHandoverMasterDto);
			Response res =null;
			if(validateRes.getStatus().equals(SUCCESS)) {
				
				Response saveResponse = iInterimHandoverMasterDao.saveCardInterimTransaction(interimHandoverMasterDto);
				if(saveResponse.getStatus().equals(SUCCESS)) {
				
					List<PaymentReceiptDetailsDto> list = new ArrayList<>();
					
					for(BillingIdDetailsDto dto : interimHandoverMasterDto.getListOfIds())
					{
						PaymentReceiptDetailsDto paydto = new PaymentReceiptDetailsDto();
						paydto.setPaymentRecieptDetailsId(dto.getPaymentReceiptDetailsId());
						paydto.setOrgnizationId(interimHandoverMasterDto.getOrganizationId());
						paydto.setUnitId(interimHandoverMasterDto.getUnitId());
						paydto.setInterimCardStatus("D");
						list.add(paydto);
					}
					if(list.size()>0  || list!= null) {
						 res = iInterimHandoverMasterDao.updateInterimCardStatus(list);
					}	
				}
				
			}else {
				return new Response<>(ERROR, COMMON_ERROR_CODE, "Invalid Security Code", null, null);
			}
			
			return res;
			
		}catch(Exception e){
			
			throw e;
//			return new Response(ERROR, null, null, null,null);
		}
	}

	@Override
	@Transactional
	public Response updateInterimChequeStatus(List<PaymentReceiptDetailsDto> paymentDepositDetailsDto) {
		try{
			return iInterimHandoverMasterDao.updateInterimChequeStatus(paymentDepositDetailsDto);
		}catch(Exception e){
			
			throw e;
//			return new Response(ERROR, null, null, null,null);
		}
	}

	@Override
	@Transactional
	public Response updateInterimCardStatus(List<PaymentReceiptDetailsDto> paymentDepositDetailsDto) {
		try{
			return iInterimHandoverMasterDao.updateInterimCardStatus(paymentDepositDetailsDto);
		}catch(Exception e){
			
			throw e;
//			return new Response(ERROR, null, null, null,null);
		}
	}

	@Override
	@Transactional
	public Response updateInterimStatusToComplete(InterimHandoverMasterDto interimHandoverMasterDto)
			throws ApplicationException {
		try {
			Response validateRes = iInterimHandoverMasterDao.validateUser1(interimHandoverMasterDto);
			Response res =null;
			if(validateRes.getStatus().equals(SUCCESS)) {
				res =  iInterimHandoverMasterDao.updateInterimStatusToComplete(interimHandoverMasterDto);
			}else {
				return new Response<>(ERROR, COMMON_ERROR_CODE, "Invalid Security Code", null, null);
			}
			return res;
		} catch (Exception e) {
			
			throw e;
//			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}	
	}

	
	
}
