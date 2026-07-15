package com.param.billing.global.transaction.service;

import java.util.Date;
import java.util.ListIterator;

import com.param.global.billing.dto.RequestDiscountDto;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.billing.global.dto.RefundCnSearchDto;
import com.param.billing.global.model.DepositMaster;
import com.param.billing.global.service.IRequestDiscountService;
import com.param.billing.global.transaction.dao.IRefundCNMasterDao;
import com.param.billing.global.transaction.dto.RefundCNMasterDto;
import com.param.billing.global.transaction.model.RefundCNMaster;
import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dao.IDepositMasterDao;
import com.param.global.billing.dto.DepositMasterDto;
import com.param.global.billing.dto.PaymentDepositDetailsDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.utility.TimeZoneComponent;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class RefundCNMasterServiceImpl implements IRefundCNMasterService, ICommonConstants,IBillingCommonConstants{
	
	@Autowired
	private IRefundCNMasterDao iRefundCNMasterDao;
	
	/*@Autowired
	private SerialNoUtility serialNoUtility;*/
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private IDepositMasterDao iDepositMasterDao;
	
	@Autowired
	private IGlobalUnitMasterDao iGlobalUnitMasterDao;
	
	@Autowired
	private IRequestDiscountService iRequestDiscountService;
	

	@Override
	@Transactional
	public Response saveRefundCreditNote(RefundCNMasterDto refundCNMasterDto) throws BillingException 
	{
		try{
			 RefundCNMaster refundCNMaster  = null;
			Date date = TimeZoneComponent.getDateByzone(refundCNMasterDto.getUnitId());
			String dateTime = GlobalCommonDateUtils.getStringDate(date, "dd-M-yyyy HH:mm:ss");
			
			//BigInteger depositId = iRefundCNMasterDao.generateRefundCreditNoteNo();
			
			String refundCnNumber = iGlobalUnitMasterDao.getGeneratedNo(0, refundCNMasterDto.getUnitId(),refundAgainstDepositScreen);
			if(refundCnNumber==null)
			{
				 return new Response(ERROR, ERROR_CODE, "Please Configure Refund Number.", null, refundCNMaster);
			}
		    refundCNMasterDto.setRefundCnNo(refundCnNumber);
			refundCNMasterDto.setRefundCnDate(dateTime);
		    refundCNMasterDto.setCreatedDate(dateTime);
			 Response refundCreditNoteRes = iRefundCNMasterDao.saveRefundCreditNote(refundCNMasterDto);
			 if(refundCreditNoteRes.getCode().equalsIgnoreCase(SUCCESS_CODE) && refundCreditNoteRes.getObject() != null)
			 {
				 
				     refundCNMaster = (RefundCNMaster)refundCreditNoteRes.getObject();
				    DepositMaster despsitMaster = (DepositMaster) sessionFactory.getCurrentSession().get(DepositMaster.class, refundCNMaster.getDepositId());
					
					
					
					double depositAmount = despsitMaster.getAvailableDeposit();
					double depositRefundAmount = refundCNMasterDto.getRefundAmount().doubleValue();
					double availableDeposit = (depositAmount - depositRefundAmount);
					
					DepositMasterDto depositMasterDto = new DepositMasterDto();
					depositMasterDto.setRefundAmount(despsitMaster.getRefundAmount()+depositRefundAmount);
					depositMasterDto.setDepositMasterId(refundCNMaster.getDepositId());
					depositMasterDto.setAvailableDeposit(availableDeposit);
					depositMasterDto.setUpdatedBy(refundCNMasterDto.getCreatedBy());
					depositMasterDto.setUpdatedDate(dateTime);
					Response depositRes = iDepositMasterDao.updateDepositMasterRefundAmount(depositMasterDto);
					
					
					
					if(depositRes.getCode().equalsIgnoreCase(SUCCESS_CODE))
					{
						double remainingAmount = refundCNMaster.getRefundAmount().doubleValue();
						Response depositDetails = iDepositMasterDao.getDepositDetailsByDepositId(refundCNMaster.getDepositId());
						if (depositDetails.getCode().equalsIgnoreCase(SUCCESS_CODE) && depositDetails.getListObject() != null&& depositDetails.getListObject().size()>0) 
						{
							double amount  = 0.00; 
							double balanceAmount = 0.00;
							double refundAmout = 0.00;
							
							ListIterator<PaymentDepositDetailsDto> paymentItr = depositDetails.getListObject().listIterator();
							PaymentDepositDetailsDto detailsDto = null;
							while(paymentItr.hasNext())
							{
								 detailsDto = paymentItr.next();
								 if(remainingAmount>0.00)
								 {	 
								 amount = detailsDto.getBalanceAmount()==null?0.00:detailsDto.getBalanceAmount();
								 if(remainingAmount>amount)
								 {
									 balanceAmount = 0.00;
									 refundAmout = remainingAmount-amount;
									 remainingAmount = refundAmout;
								 }
								 else if(remainingAmount==amount)
								 {
									 balanceAmount = 0.00;
									 refundAmout = remainingAmount;
									 remainingAmount = 0.00;
								 }
								 else
								 {
									 balanceAmount =amount-remainingAmount;
									 refundAmout = remainingAmount;
									 remainingAmount = balanceAmount;
								 }
								
									detailsDto.setRefundAmount(detailsDto.getRefundAmount()+refundAmout);
									detailsDto.setUpdatedBy(refundCNMasterDto.getCreatedBy());
									detailsDto.setUpdatedDate(dateTime);
									detailsDto.setBalanceAmount(balanceAmount);
									iDepositMasterDao.updateDepositDetailsRefundAmount(detailsDto);
								 }
								 
							}
			           }
					}
					
					if(refundCNMasterDto.getIsApproved()!=null && refundCNMasterDto.getIsApproved()=='N')
					{
						RequestDiscountDto dto = new RequestDiscountDto();
						dto.setReqStatus(1);
						dto.setCreatedBy(refundCNMasterDto.getCreatedBy());
						dto.setCreatedDate(dateTime);
						dto.setUpdatedDate(dateTime);
						dto.setUpdatedBy(refundCNMasterDto.getUpdatedBy());
						dto.setOrganizationId(refundCNMasterDto.getOrganizationId());
						dto.setUnitId(refundCNMasterDto.getUnitId());
						//dto.setRequestType(5); //5 for credit Note
						dto.setRequestType(12); //12 for Deposit
						dto.settRefundCnId(refundCNMaster.gettRefundCnId());
						dto.setPatientId(refundCNMasterDto.getPatientId());
						dto.setRequestSentBy(refundCNMasterDto.getCreatedBy());
						dto.setRequestSentTo(refundCNMasterDto.getRequestSentTo());
						dto.setRequestDate(dateTime);
						iRequestDiscountService.requestDiscount(dto);
					}
			 }
			 return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, refundCNMaster);
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response searchRefundCn(RefundCnSearchDto reqDto) throws BillingException {
		try{
			return iRefundCNMasterDao.searchRefundCn(reqDto);
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	

}
