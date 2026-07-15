package com.param.billing.global.transaction.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.billing.common.ICommonConstants;
import com.param.billing.global.dto.BillPaymentCollectionReqDto;
import com.param.billing.global.transaction.dao.IBillPayeeDetailsDao;
import com.param.billing.global.transaction.dao.IBillingDetailsDao;
import com.param.billing.global.transaction.dao.IBillingMasterDao;
import com.param.billing.global.transaction.dao.ICreditNoteMasterDao;
import com.param.billing.global.transaction.dao.IPaymentDepositConsumptionDao;
import com.param.billing.global.transaction.dto.BillDetailsByBillIdDto;
import com.param.billing.global.transaction.dto.BillListDetailsCNDto;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.Response;
import com.param.global.dao.IOrderDetailsMasterDao;
import com.param.global.dto.BillPayeeDetailsDto;
import com.param.global.dto.OrderDetailsPackageMapperDto;
import com.param.global.dto.items.UomResponse;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class BillPayeeDetailsServiceImpl implements IBillPayeeDetailsService,ICommonConstants{

	@Autowired
	private IBillPayeeDetailsDao iBillPayeeDetailsDao;
	
	@Autowired
	IBillingMasterDao iBillingMasterDao;
	
	@Autowired
	IBillingDetailsDao iBillingDetailsDao;
	
	@Autowired
	IPaymentDepositConsumptionDao iPaymentDepositConsumptionDao ; 
	
	@Autowired
	ICreditNoteMasterDao iCreditNoteMasterDao;
	
	@Autowired
	IOrderDetailsMasterDao iOrderDetailsMasterDao;
	
	
	@Override
	//@Transactional
	public Response saveBillPayeeDetails(BillPayeeDetailsDto billPayeeDetailsDto) throws ApplicationException {
		try {
			return iBillPayeeDetailsDao.saveBillPayeeDetails(billPayeeDetailsDto);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getBillPayeeDetailsByBillId(int billId) throws BillingException {
		try {
			
			
			Response res = iBillingMasterDao.getBillDetailsByBillId(billId);
			
			ListIterator<BillDetailsByBillIdDto> iterator = res.getListObject().listIterator();
			/*while(iterator.hasNext())
			{
				BillDetailsByBillIdDto billDetailsByBillIdDto = iterator.next();
				billDetailsByBillIdDto.setDiscountPercentage((billDetailsByBillIdDto.getDiscount()/(billDetailsByBillIdDto.getGrossAmount()-billDetailsByBillIdDto.getConcession()))*100);
				billDetailsByBillIdDto.setConcessionPercentage(((billDetailsByBillIdDto.getConcession()/billDetailsByBillIdDto.getGrossAmount())*100));
				if (billDetailsByBillIdDto.getTaxPercentage() == null) 
				{
					billDetailsByBillIdDto.setTaxPercentage(billDetailsByBillIdDto.getTaxId() == null
							&& (billDetailsByBillIdDto.getTaxAmount() > 0 || billDetailsByBillIdDto.getTaxAmount() < 0)
									? ((billDetailsByBillIdDto.getTaxAmount() / billDetailsByBillIdDto.getNetAmt())
											* 100)
									: 0);
				}
				
				  //for package
                Response packageMpprRes = iOrderDetailsMasterDao.getOrderDetailsPackageMapperByOrderDetailsId(billDetailsByBillIdDto.getOrderDetailsId());
                List<OrderDetailsPackageMapperDto> mpprList = packageMpprRes.getListObject();
                billDetailsByBillIdDto.setOrderDetailsPackageMapperDtosList(mpprList);
				
			}*/
			
			
			//added By Sneha
			List<BillDetailsByBillIdDto> listServiceMasterDto = null;
			 listServiceMasterDto = res.getListObject();
			List<Integer> orderDetailsIds = listServiceMasterDto.stream().map(x -> x.getOrderDetailsId()).collect(Collectors.toList());
			List<OrderDetailsPackageMapperDto> packageMpprResList = new ArrayList<>();
			
			
			 packageMpprResList = iOrderDetailsMasterDao.getOrderDetailsPackageMapperByOrderDetailsIdOptimise(orderDetailsIds);
			 
				while(iterator.hasNext())
				{
					BillDetailsByBillIdDto billDetailsByBillIdDto = iterator.next();
					billDetailsByBillIdDto.setDiscountPercentage((billDetailsByBillIdDto.getDiscount()/(billDetailsByBillIdDto.getGrossAmount()-billDetailsByBillIdDto.getConcession()))*100);
					billDetailsByBillIdDto.setConcessionPercentage(((billDetailsByBillIdDto.getConcession()/billDetailsByBillIdDto.getGrossAmount())*100));
						
						List<OrderDetailsPackageMapperDto> mpprLists = packageMpprResList.stream().filter(x -> x.getOrderDetailsId().equals(billDetailsByBillIdDto.getOrderDetailsId()))
						.collect(Collectors.toList());
						 billDetailsByBillIdDto.setOrderDetailsPackageMapperDtosList(mpprLists);
			     }
				
			Response res2 = iBillPayeeDetailsDao.getBillPayeeDetailsByBillId(billId);
			
			res.setObject(iPaymentDepositConsumptionDao.getPaymentRecieptDetails(billId));
			
			return new Response(SUCCESS, null, null, res2.getListObject(),res);
			
		}catch (BillingException be) {
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getBillPayeeDetailsByBillIdCr(int billId,int visitTypeId,int admissionId) throws BillingException {
   try {
	   
	     
			
	       if((visitTypeId==2||visitTypeId==3||visitTypeId==4) && admissionId>0)
	       {
	    	   
//	    	    Response refundRes = iCreditNoteMasterDao.checkCreditNotePeningAgainstBill(billId);
//				Integer count = (Integer) refundRes.getObject();
//				if (count.intValue() > 0) {
//					return new Response(ERROR, null, "Please Refund Generated Credit Note.", null, null);
//				}
	    	   
	    	   /*Response ipFinalRes = iCreditNoteMasterDao.checkFinalBillAgaistAdmission(admissionId);
	    	   Integer ipFinalBillCount =(Integer)ipFinalRes.getObject();
	    	   if(ipFinalBillCount.intValue()==0)
		       {
		    	   if(visitTypeId==2)
		    		   return new Response(ERROR, null, "IP Final Bill is Not Generated Or Settled.",null,null);
		    	   else if (visitTypeId==3)
		    		   return new Response(ERROR, null, "Day Care Final Bill is Not Generated Or Settled.",null,null);
		    	   else if(visitTypeId==4)
		    		   return new Response(ERROR, null, "ER Final Bill is Not Generated Or Settled.",null,null);
		       }
	    	   else {*/
				   Double roundOff = iCreditNoteMasterDao.getRoundOffByBillId(billId);
	    		   com.param.billing.common.Response res = iBillingDetailsDao.getBillDetailsByBillId(0, 0, billId);
					Response res2 = iBillPayeeDetailsDao.getBillPayeeDetailsByBillId(billId);
					ListIterator<BillListDetailsCNDto> iterator = res.getListObject().listIterator();
					while(iterator.hasNext())
					{
						BillListDetailsCNDto billListDetailsCNDto = iterator.next();
						Response refundMpprRes = iOrderDetailsMasterDao.getAvailableRefundAmountCreditNote(billListDetailsCNDto.getOrderDetailsId(),billId);
						billListDetailsCNDto.setPayeeRefundMppr(refundMpprRes.getListObject());

						Response CreditApproved = iBillPayeeDetailsDao.getCreditApproveStatus(billListDetailsCNDto.getBillingDetailsId());
						if(CreditApproved.getObject() != null) {
							BillListDetailsCNDto dto = (BillListDetailsCNDto) CreditApproved.getObject();
							billListDetailsCNDto.setCreditApproved(dto.getCreditApproved());
						}
						Character r = 'R';
						if(billListDetailsCNDto.getCreditApproved() != null && billListDetailsCNDto.getCreditApproved().equals(r)) {
							BigDecimal refundA = billListDetailsCNDto.getSelfPayable();
							billListDetailsCNDto.setRefundAmount(0d);
							billListDetailsCNDto.setRemainingRefundAmt(refundA.doubleValue());
							billListDetailsCNDto.setPreviousDiscountPer(100d);
						}else {
							
							Double  refundAmount = (billListDetailsCNDto.getCrTaxToadd()!=null && billListDetailsCNDto.getCancelOrDiscount().equals(2) ?  billListDetailsCNDto.getCrTaxToadd() : 0) + (billListDetailsCNDto.getRefundAmount()!=null ?  billListDetailsCNDto.getRefundAmount() : 0);
							billListDetailsCNDto.setRefundAmount(refundAmount);
							Double  remainingRefundAmt = (billListDetailsCNDto.getRemainingRefundAmt()!=null ?  billListDetailsCNDto.getRemainingRefundAmt() : 0) - (billListDetailsCNDto.getCrTaxToadd()!=null ?  billListDetailsCNDto.getCrTaxToadd() : 0) ;
								if(remainingRefundAmt < 0) {
									billListDetailsCNDto.setRemainingRefundAmt(0d);
								}else {
									billListDetailsCNDto.setRemainingRefundAmt(remainingRefundAmt);
								}
								
								if(refundAmount> 0) {
									Double disPer  = billListDetailsCNDto.getSelfPayable().doubleValue();
									Double discIn100per = (disPer/100);
									Double discInper = (refundAmount/discIn100per);
									if(discInper > 100){
										billListDetailsCNDto.setPreviousDiscountPer(100d);
									}else {
										billListDetailsCNDto.setPreviousDiscountPer(discInper);
									}
								}else {
									billListDetailsCNDto.setPreviousDiscountPer(0d);
								}
						}
						billListDetailsCNDto.setBillDiscountPer(billListDetailsCNDto.getBillDiscount()!=null && billListDetailsCNDto.getBillDiscount()!=0
								&&  (billListDetailsCNDto.getGrossAmount()-billListDetailsCNDto.getConcession())!=0 ?
								(billListDetailsCNDto.getBillDiscount()/(billListDetailsCNDto.getGrossAmount()-billListDetailsCNDto.getConcession()))*100
								:0.00);
					}
					return new Response(SUCCESS, null, null, res2.getListObject(),res.getListObject(),roundOff);
	    	   //}
	       }
	       else if(visitTypeId==1)
	       {
	    	   /*Response refundRes = iCreditNoteMasterDao.checkCreditNotePeningAgainstBill(billId);
				Integer count = (Integer) refundRes.getObject();
				if (count.intValue() > 0) {
					return new Response(ERROR, null, "Please Refund Generated Credit Note.", null, null);
				}
				else {*/
			        Double roundOff = iCreditNoteMasterDao.getRoundOffByBillId(billId);
					com.param.billing.common.Response res = iBillingDetailsDao.getBillDetailsByBillId(0, 0, billId);
					Response res2 = iBillPayeeDetailsDao.getBillPayeeDetailsByBillId(billId);
					ListIterator<BillListDetailsCNDto> iterator = res.getListObject().listIterator();
					while(iterator.hasNext())
					{
						BillListDetailsCNDto billListDetailsCNDto = iterator.next();
						Response refundMpprRes = iOrderDetailsMasterDao.getAvailableRefundAmountCreditNote(billListDetailsCNDto.getOrderDetailsId(),billId);
						billListDetailsCNDto.setPayeeRefundMppr(refundMpprRes.getListObject());

						Response CreditApproved = iBillPayeeDetailsDao.getCreditApproveStatus(billListDetailsCNDto.getBillingDetailsId());
						if(CreditApproved.getObject() != null) {
							BillListDetailsCNDto dto = (BillListDetailsCNDto) CreditApproved.getObject();
							billListDetailsCNDto.setCreditApproved(dto.getCreditApproved());
						}
						Character r = 'R';
						if(billListDetailsCNDto.getCreditApproved() != null && billListDetailsCNDto.getCreditApproved().equals(r)) {
							BigDecimal refundA = billListDetailsCNDto.getSelfPayable();
							billListDetailsCNDto.setRefundAmount(0d);
							billListDetailsCNDto.setRemainingRefundAmt(refundA.doubleValue());
							billListDetailsCNDto.setPreviousDiscountPer(100d);
						}else {
							
							Double  refundAmount = (billListDetailsCNDto.getCrTaxToadd()!=null && billListDetailsCNDto.getCancelOrDiscount().equals(2) ?  billListDetailsCNDto.getCrTaxToadd() : 0) + (billListDetailsCNDto.getRefundAmount()!=null ?  billListDetailsCNDto.getRefundAmount() : 0);
							billListDetailsCNDto.setRefundAmount(refundAmount);
							Double  remainingRefundAmt = (billListDetailsCNDto.getRemainingRefundAmt()!=null ?  billListDetailsCNDto.getRemainingRefundAmt() : 0) - (billListDetailsCNDto.getCrTaxToadd()!=null ?  billListDetailsCNDto.getCrTaxToadd() : 0) ;
							if(remainingRefundAmt < 0) {
								billListDetailsCNDto.setRemainingRefundAmt(0d);
							}else {
								billListDetailsCNDto.setRemainingRefundAmt(remainingRefundAmt);
							}
							if(refundAmount> 0) {
								Double disPer  = billListDetailsCNDto.getSelfPayable().doubleValue();
								Double discIn100per = (disPer/100);
								Double discInper = (refundAmount/discIn100per);
								if(discInper > 100){
									billListDetailsCNDto.setPreviousDiscountPer(100d);
								}else {
									billListDetailsCNDto.setPreviousDiscountPer(discInper);
								}
								
							}else {
								billListDetailsCNDto.setPreviousDiscountPer(0d);
							}

						}
						billListDetailsCNDto.setBillDiscountPer(billListDetailsCNDto.getBillDiscount()!=null && billListDetailsCNDto.getBillDiscount()!=0
								&&  (billListDetailsCNDto.getGrossAmount()-billListDetailsCNDto.getConcession())!=0 ?
								(billListDetailsCNDto.getBillDiscount()/(billListDetailsCNDto.getGrossAmount()-billListDetailsCNDto.getConcession()))*100
								:0.00);
					}
					return new Response(SUCCESS, null, null, res2.getListObject(),res.getListObject(),roundOff);
				//}
	       }

		}catch (BillingException be) {
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
       return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response updateCrAmtByPayeeAndBillId(int billId, int payeeId, double crAmount) throws BillingException {
		try {
			return iBillPayeeDetailsDao.updateCrAmtByPayeeAndBillId(billId, payeeId, crAmount);
		}catch (Exception e) {
			
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response getDebitNoteDetailsByBillMasterId(int billId) throws BillingException {
    try {
			Response res = iBillingMasterDao.getBillDetailsByBillId(billId);
			ListIterator<BillDetailsByBillIdDto> iterator = res.getListObject().listIterator();
			while(iterator.hasNext())
			{
				BillDetailsByBillIdDto billDetailsByBillIdDto = iterator.next();
				Double disPer = (billDetailsByBillIdDto.getDiscount()/(billDetailsByBillIdDto.getGrossAmount()-billDetailsByBillIdDto.getConcession()))*100;
				billDetailsByBillIdDto.setDiscountPercentage(disPer);
				
				  //for package
                Response packageMpprRes = iOrderDetailsMasterDao.getOrderDetailsPackageMapperByOrderDetailsId(billDetailsByBillIdDto.getOrderDetailsId());
                List<OrderDetailsPackageMapperDto> mpprList = packageMpprRes.getListObject();
                billDetailsByBillIdDto.setOrderDetailsPackageMapperDtosList(mpprList);
			}
			
			Response res2 = iBillPayeeDetailsDao.getBillPayeeDetailsByBillId(billId);
			
			return new Response(SUCCESS, null, null, res2.getListObject(),res.getListObject());
			
		}catch (BillingException be) {
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getBillPharmacyPayeeByBillIdCreditNote(int saleId) throws BillingException {
		try {
			return iPaymentDepositConsumptionDao.getBillPharmacyPayeeByBillIdCreditNote(saleId);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response cancelPharmacyReceipt(BillPaymentCollectionReqDto dto) throws BillingException {
		try {
			return iPaymentDepositConsumptionDao.cancelPharmacyReceipt(dto);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

}

