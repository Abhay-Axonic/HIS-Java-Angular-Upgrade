package com.param.billing.global.transaction.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.billing.global.dto.RefundCnSearchDto;
import com.param.billing.global.dto.RefundCollectionReqDto;
import com.param.billing.global.model.DepositMaster;
import com.param.billing.global.transaction.dao.IBillStatusMapperDao;
import com.param.billing.global.transaction.dao.IBillingDetailsDao;
import com.param.billing.global.transaction.dao.IBillingMasterDao;
import com.param.billing.global.transaction.dao.IDoctorShareDao;
import com.param.billing.global.transaction.dao.IPaymentRefundDetailsDao;
import com.param.billing.global.transaction.dao.IPaymentRefundMasterDao;
import com.param.billing.global.transaction.dao.IRefundCNMasterDao;
import com.param.billing.global.transaction.dto.CreditNoteDetailsDto;
import com.param.billing.global.transaction.dto.PaymentRefundDetailsDto;
import com.param.billing.global.transaction.dto.PaymentRefundMasterDto;
import com.param.billing.global.transaction.dto.RefundCNMasterDto;
import com.param.billing.global.transaction.dto.TDoctorShareRespDto;
import com.param.billing.global.transaction.model.PaymentRefundMaster;
import com.param.billing.global.transaction.model.RefundCNMaster;
import com.param.billing.global.transaction.model.TDoctorShare;
import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dao.IDepositMasterDao;
import com.param.global.billing.dao.IPaymentDepositDetailsDao;
import com.param.global.billing.dto.DepositMasterDto;
import com.param.global.billing.dto.PaymentDepositDetailsDto;
import com.param.global.billing.service.IDepositMasterService;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.global.mail.IMailingService;
import com.param.global.org.common.dao.IPatientLoyaltyCardMapperDao;
import com.param.global.org.common.dto.PatientLoyaltyCardMapperDto;
import com.param.global.service.IOrderMasterService;
import com.param.global.service.OrderMasterServiceImpl;
import com.param.global.utility.SerialNoUtility;
import com.param.utility.TimeZoneComponent;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PaymentRefundDetailsServiceImpl implements IPaymentRefundMasterService,IBillingCommonConstants, ICommonConstants {

	@Autowired
	private IPaymentRefundMasterDao iPaymentRefundMasterDao;

	@Autowired
	private IPaymentRefundDetailsDao iPaymentRefundDetailsDao;
	
	@Autowired
	private IRefundCNMasterDao iRefundCNMasterDao;
	

	@Autowired
	private SerialNoUtility serialNoUtility;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private IBillPayeeDetailsService iBillPayeeDetailsService;
	
	@Autowired
	private ICreditNoteMasterService iCreditNoteMasterService;
	
	@Autowired
	private IGlobalUnitMasterDao iGlobalUnitMasterDao;
	
	@Autowired
	IBillingDetailsDao iBillingDetailsDao; 
	
	@Autowired
	IBillingMasterDao iBillingMasterDao; 
	
	@Autowired
	IBillStatusMapperDao iBillStatusMapperDao;
	
	@Autowired
	IDepositMasterService iDepositMasterService;
	
	@Autowired
	private IDepositMasterDao iDepositMasterDao;
	
	@Autowired
	private IPaymentDepositDetailsDao iPaymentDepositDetailsDao;
	
	@Autowired
	private IPatientLoyaltyCardMapperDao iPatientLoyaltyCardMapperDao;
	
	@Autowired
	private IDoctorShareDao iDoctorShareDao;
	
	@Autowired
	private IOrderMasterService iOrderMasterService;
	

	@Autowired
	IMailingService iMailingService;

	@Override
	@Transactional
	public Response saveRefundAgainstDeposit(RefundCollectionReqDto refundCollectionReqDto,int depositId,int tRefundCnId) throws BillingException {
		try {
			Response existRes = iPaymentRefundMasterDao.checkExistingRefundApproval(refundCollectionReqDto, depositId, tRefundCnId);
			if(existRes.getStatus().equals(SUCCESS) && existRes.getListObject() != null && !existRes.getListObject().isEmpty()){
				return new Response(ALREADY_APPROVED, SUCCESS_CODE, "Refund already completed for selected refund voucher..!", null, null);
			}

			Double loyaltyDepositamount = 0.0;
			Double activeDepositamount = 0.0;
			PaymentRefundMaster paymentRefundMaster = null;
			Date date = TimeZoneComponent.getDateByzone(refundCollectionReqDto.getListPaymentRefundDetails().getFirst().getUnitId());
			String dateTime = GlobalCommonDateUtils.getStringDate(date, "dd-M-yyyy HH:mm:ss");
			List<PatientLoyaltyCardMapperDto>  mailList =  new 	ArrayList<PatientLoyaltyCardMapperDto>();  
			PaymentRefundMasterDto dto = refundCollectionReqDto.getPaymentRefundMasterDto();
			//BigInteger refundVoucherNo = iPaymentRefundMasterDao.generateRefundVoucherNo();
			dto.settRefundCnId(tRefundCnId);
			dto.setPaymentRefundVoucherNo(iGlobalUnitMasterDao.getGeneratedNo(0, dto.getUnitId(),refundScreenId));
			dto.setDateOfRefund(dateTime);
			dto.setCreatedDate(dateTime);
			
			List<PaymentRefundDetailsDto> listDeposit = new ArrayList<PaymentRefundDetailsDto>();
			List<PaymentRefundDetailsDto> listLoyaltyCard  = new ArrayList<PaymentRefundDetailsDto>();
			
			Response refundApi = iPaymentRefundMasterDao.savePaymentRefundMasterDto(dto);
			if (refundApi.getCode().equalsIgnoreCase(SUCCESS_CODE) && refundApi.getObject() != null) {
				 paymentRefundMaster = (PaymentRefundMaster) refundApi.getObject();

				// save payment details
				ListIterator<PaymentRefundDetailsDto> paymentItr = refundCollectionReqDto.getListPaymentRefundDetails().listIterator();
				PaymentRefundDetailsDto detailsDto = null;
			
				while (paymentItr.hasNext()) 
				{
					detailsDto = paymentItr.next();
					detailsDto.setPaymentRefundId(paymentRefundMaster.getPaymentRefundVoucherId());
					detailsDto.setCreatedDate(dateTime);
					detailsDto.setUpdatedBy(dto.getCreatedBy());
					detailsDto.setUpdatedDate(dateTime);
					detailsDto.setOrgnisationId(paymentRefundMaster.getOrgnisationId());
					detailsDto.setUnitId(paymentRefundMaster.getUnitId());
					
					if(detailsDto.getPaymentModeId()!=null && detailsDto.getPaymentModeId()==6)
					{
						loyaltyDepositamount += detailsDto.getAmount();
						listLoyaltyCard.add(detailsDto);
					}
					if(detailsDto.getPaymentModeId()!=null && detailsDto.getPaymentModeId()==7)
					{
						activeDepositamount+= detailsDto.getAmount();
						listDeposit.add(detailsDto);	
					}
					iPaymentRefundDetailsDao.savePaymentRefundDetails(detailsDto);
					
					
				}
			}
			
					
					RefundCNMaster refundCNMaster = (RefundCNMaster) sessionFactory.getCurrentSession().get(RefundCNMaster.class, tRefundCnId);
					RefundCNMasterDto refundCNMasterDto = new RefundCNMasterDto();
					refundCNMasterDto.settRefundCnId(tRefundCnId);	
					if(refundCNMaster.getRefundedAmount().compareTo(BigDecimal.ZERO)==1)
					{
						refundCNMasterDto.setRefundedAmount(refundCNMaster.getRefundedAmount().add(BigDecimal.valueOf(dto.getAmountRefunded())));
					}
					else 
					{
						refundCNMasterDto.setRefundedAmount(BigDecimal.valueOf(dto.getAmountRefunded()));
					}
					refundCNMasterDto.setBalanceAmount(refundCNMaster.getBalanceAmount().subtract(BigDecimal.valueOf(dto.getAmountRefunded())));
					refundCNMasterDto.setUpdatedBy(dto.getCreatedBy());
					refundCNMasterDto.setUpdatedDate(dateTime);
					iRefundCNMasterDao.updateRefundCreditNote(refundCNMasterDto);
					
					if(listLoyaltyCard.size()>0)
					{
						
						this.saveDepositDetailsAgainstRefund(listLoyaltyCard,dto,loyaltyDepositamount,4,6);
					}
					if(listDeposit.size()>0)
					{
					
						this.saveDepositDetailsAgainstRefund(listDeposit,dto,activeDepositamount,1,7);
					}
					
					return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, paymentRefundMaster.getPaymentRefundVoucherId());
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}

	}

	private Response saveDepositDetailsAgainstRefund(List<PaymentRefundDetailsDto> listLoyaltyCard,PaymentRefundMasterDto dto, Double loyaltyDepositamount, int depositTypeId,int paymentMode) {
		
		try{
			List<PatientLoyaltyCardMapperDto>  mailList =  new 	ArrayList<PatientLoyaltyCardMapperDto>();  
			Date date = TimeZoneComponent.getDateByzone(dto.getUnitId());
			String dateTime = GlobalCommonDateUtils.getStringDate(date, "dd-M-yyyy HH:mm:ss");
			DepositMaster depositMaster =null;
			//save deposit master 
			DepositMasterDto deposit = new DepositMasterDto();
			deposit.setAvailableDeposit(loyaltyDepositamount);
			deposit.setDepositAmount(loyaltyDepositamount);
			deposit.setPatientId(dto.getPatientId());
			deposit.setOrgId(dto.getOrgnisationId());
			deposit.setUnitId(dto.getUnitId());
			deposit.setConsumedDeposit(0.00);
			deposit.setCreatedDate(dateTime);
			deposit.setCreatedBy(dto.getCreatedBy());
			deposit.setDepositDate(dateTime);
			deposit.setDepositTypeId(depositTypeId);
			if(deposit.getSignetureImage()!=null && !deposit.getSignetureImage().isEmpty())
			{
				 byte[]  signature = Base64.getDecoder().decode(new String(deposit.getSignetureImage().split(",")[1]).getBytes("UTF-8"));
				 
				deposit.setSignature(signature);
			}
			
			String depositNumber = iGlobalUnitMasterDao.getGeneratedNo(0, deposit.getUnitId(), depositScreenId);
			if(depositNumber==null)
			{
				return new Response<>(ERROR, ERROR_CODE, "Please Configure Deposit Number.", null, null);
			}
			deposit.setDepositNo(depositNumber);
			deposit.setStatus(ACTIVE);
			deposit.setUpdatedBy(deposit.getCreatedBy());
			deposit.setUpdatedDate(dateTime);
			Response depositRes = iDepositMasterDao.saveDeposit(deposit);
			if(depositRes.getCode().equalsIgnoreCase(SUCCESS_CODE) && depositRes.getObject() != null){
			  depositMaster = (DepositMaster)depositRes.getObject();
				
				//save payment details
				ListIterator<PaymentRefundDetailsDto> paymentItr = listLoyaltyCard.listIterator();
				PaymentRefundDetailsDto reuDto = null;
				while(paymentItr.hasNext())
				{
					PaymentDepositDetailsDto detailsDto = new PaymentDepositDetailsDto();
					reuDto = paymentItr.next();
					detailsDto.setCreatedDate(dateTime);
					detailsDto.setCreatedBy(dto.getCreatedBy());
					detailsDto.setDepositId(depositMaster.getDepositMasterId());
					detailsDto.setPaymentModeId(paymentMode);
					detailsDto.setAmount(reuDto.getAmount());
					if(paymentMode == 6) {
						detailsDto.setCardNo(reuDto.getCardNo());
					}
					detailsDto.setUpdatedBy(deposit.getCreatedBy());
					detailsDto.setUpdatedDate(dateTime);
					detailsDto.setBalanceAmount(reuDto.getAmount());
					detailsDto.setRefundAmount(0.00);
					detailsDto.setUnitId(dto.getUnitId());
					detailsDto.setOrganizationId(dto.getOrgnisationId());
					iPaymentDepositDetailsDao.savePaymentDepositDetails(detailsDto);
					
					if(paymentMode == 6) {
						PatientLoyaltyCardMapperDto LoyaltyCarddto =new PatientLoyaltyCardMapperDto();
						LoyaltyCarddto.setCardNo(reuDto.getCardNo());
						LoyaltyCarddto.setCreatedBy(reuDto.getCreatedBy());
						LoyaltyCarddto.setCreatedDate(dateTime);
						LoyaltyCarddto.setOrganisationId(dto.getOrgnisationId());
						LoyaltyCarddto.setUnitId(dto.getUnitId());
						Response loyalityCardResponse=iPatientLoyaltyCardMapperDao.getPatientDetailsByLoyaltiCardNo(LoyaltyCarddto);
						if(loyalityCardResponse.getListObject().size() >0 && loyalityCardResponse.getListObject()!=null){
							PatientLoyaltyCardMapperDto patientLoyaltyCardMapperDto=(PatientLoyaltyCardMapperDto)loyalityCardResponse.getListObject().getFirst();
								Double totalLoylty  = reuDto.getAmount()+patientLoyaltyCardMapperDto.getCardAmount();
								LoyaltyCarddto.setCardAmount(totalLoylty);
								iPatientLoyaltyCardMapperDao.addUpdatedLoyaltyCardAmount(LoyaltyCarddto);
								
								patientLoyaltyCardMapperDto.setConsumeAmount(reuDto.getAmount());
								mailList.add(patientLoyaltyCardMapperDto);
						}
					
					}
				}
			}
			for (PatientLoyaltyCardMapperDto patientLoyaltyCardMapperDto : mailList) {
				iMailingService.addLoyaltyAmount(patientLoyaltyCardMapperDto);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
			
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response searchRefundMaster(RefundCnSearchDto reqDto) throws BillingException 
	{
		try{
			return iPaymentRefundMasterDao.searchRefundMaster(reqDto);
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getRefundDetailsByRefundId(int orgId, int unitId, int paymentRefundVoucherId)
			throws BillingException 
	{
		try{
			return iPaymentRefundMasterDao.getRefundDetailsByRefundId(orgId, unitId, paymentRefundVoucherId);
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getRefundBankDetails(int orgId, int unitId, int tRefundCnId) throws BillingException {
		try{
			return iPaymentRefundMasterDao.getRefundBankDetails(orgId, unitId, tRefundCnId);
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response saveRefundAgainstCreditNote(RefundCollectionReqDto refundCollectionReqDto, int billMasterId,int billPayeeId) throws BillingException {
		try{
			Double loyaltyDepositamount = 0.0;
			Double activeDepositamount = 0.0;
			PaymentRefundMaster paymentRefundMaster = null;
			Date date = TimeZoneComponent.getDateByzone(refundCollectionReqDto.getListPaymentRefundDetails().getFirst().getUnitId());
			String dateTime = GlobalCommonDateUtils.getStringDate(date, "dd-M-yyyy HH:mm:ss");
			
			
			PaymentRefundMasterDto dto = refundCollectionReqDto.getPaymentRefundMasterDto();
			//BigInteger refundVoucherNo = iPaymentRefundMasterDao.generateRefundVoucherNo();
			dto.setPaymentRefundVoucherNo(iGlobalUnitMasterDao.getGeneratedNo(0, dto.getUnitId(),refundScreenId));
			dto.setDateOfRefund(dateTime);
			dto.setCreatedDate(dateTime);
			
			List<PaymentRefundDetailsDto> listDeposit = new ArrayList<PaymentRefundDetailsDto>();
			List<PaymentRefundDetailsDto> listLoyaltyCard  = new ArrayList<PaymentRefundDetailsDto>();
			
			Response refundApi = iPaymentRefundMasterDao.savePaymentRefundMasterDto(dto);
			if (refundApi.getCode().equalsIgnoreCase(SUCCESS_CODE) && refundApi.getObject() != null) {
				 paymentRefundMaster = (PaymentRefundMaster) refundApi.getObject();

				// save payment details
				ListIterator<PaymentRefundDetailsDto> paymentItr = refundCollectionReqDto.getListPaymentRefundDetails().listIterator();
				PaymentRefundDetailsDto detailsDto = null;
				while (paymentItr.hasNext()) {
					detailsDto = paymentItr.next();
					detailsDto.setPaymentRefundId(paymentRefundMaster.getPaymentRefundVoucherId());
					detailsDto.setCreatedDate(dateTime);
					detailsDto.setUpdatedBy(dto.getCreatedBy());
					detailsDto.setUpdatedDate(dateTime);
					detailsDto.setOrgnisationId(paymentRefundMaster.getOrgnisationId());
					detailsDto.setUnitId(paymentRefundMaster.getUnitId());
					
					if(detailsDto.getPaymentModeId()!=null && detailsDto.getPaymentModeId()==6)
					{
						loyaltyDepositamount += detailsDto.getAmount();
						listLoyaltyCard.add(detailsDto);
					}
					if(detailsDto.getPaymentModeId()!=null && detailsDto.getPaymentModeId()==7)
					{
						activeDepositamount+= detailsDto.getAmount();
						listDeposit.add(detailsDto);	
					}
					iPaymentRefundDetailsDao.savePaymentRefundDetails(detailsDto);
				}
			}
			
			Response  billPayeeDetails = iBillPayeeDetailsService.updateCrAmtByPayeeAndBillId(billMasterId, billPayeeId, dto.getAmountRefunded());
			
			Response  creditNoteRes = iCreditNoteMasterService.updateDueAmountByCreditNoteId(dto.getCreditNoteId(), dto.getAmountRefunded(),dto.getReceivedFrom());
			
			if(listLoyaltyCard.size()>0)
			{
				
				 this.saveDepositDetailsAgainstRefund(listLoyaltyCard,dto,loyaltyDepositamount,4,6);
			}
			if(listDeposit.size()>0)
			{
			
				this.saveDepositDetailsAgainstRefund(listDeposit,dto,activeDepositamount,1,7);
			}
			
			/*//check if doctor share is processed
			Response res = iPaymentRefundMasterDao.checkIsDoctroSharePosted(refundCollectionReqDto.getPaymentRefundMasterDto().getCreditNoteId());
			if(res.getListObject()!=null && res.getListObject().size()>0)
			{
				List<CreditNoteDetailsDto> resList = res.getListObject();
				if(resList.size()>0)
				{
					for(CreditNoteDetailsDto creditNoteDetailsDto : resList)
					{
						com.param.billing.common.Response resByOrdDtlRes = iDoctorShareDao.getDoctorShareDataByOrderDetailsId(creditNoteDetailsDto.getOrderDetailId());
						List<TDoctorShareRespDto> listTDoctorShareRespDto = resByOrdDtlRes.getListObject();
						if(listTDoctorShareRespDto.size()>0)
						{
							if(creditNoteDetailsDto.getCancelOrDiscount()==1)//if service is cancelled
							{
								for(TDoctorShareRespDto doctorShareDto : listTDoctorShareRespDto)
								{
									if(doctorShareDto.getDueShareAmt()>0)
									{
										iDoctorShareDao.inactiveDocShare(doctorShareDto.getDoctorShareId());
									}else{
										
										com.param.billing.common.Response doShareRes = iDoctorShareDao.docSharefindById(doctorShareDto.getDoctorShareId());
										TDoctorShareRespDto doctorShare = (TDoctorShareRespDto)doShareRes.getObject();
											doctorShare.setDueShareAmt(doctorShare.getProcessedShareAmt()!=null ? new Double(OrderMasterServiceImpl.df2.format(-doctorShare.getProcessedShareAmt())):0.00);
											doctorShare.setProcessedShareAmt(0.00);
											doctorShare.setHospShare(new Double(OrderMasterServiceImpl.df2.format(-doctorShare.getHospShare())));
											doctorShare.setDocShare(new Double(OrderMasterServiceImpl.df2.format(-doctorShare.getDocShare())));
											doctorShare.setAgainstCrId(refundCollectionReqDto.getPaymentRefundMasterDto().getCreditNoteId());
										iDoctorShareDao.saveDoctorShareData(doctorShare);
									}
									
								}
							}else{
								for(TDoctorShareRespDto doctorShareDto : listTDoctorShareRespDto)
								{
									com.param.billing.common.Response doShareRes = iDoctorShareDao.docSharefindById(doctorShareDto.getDoctorShareId());
									if(doctorShareDto.getDueShareAmt()>0)
									{
										TDoctorShare doctorShare = (TDoctorShare)doShareRes.getObject2();
										
										Double negatAmt =((doctorShare.getSharePer()/100)*creditNoteDetailsDto.getCrAdditionalDiscount());
										Double hospitalSharePer = 100 - doctorShare.getSharePer();
										Double hospitalShare = ((hospitalSharePer/100)*creditNoteDetailsDto.getCrAdditionalDiscount());
											doctorShare.setDueShareAmt(new Double(OrderMasterServiceImpl.df2.format(doctorShare.getDueShareAmt() - negatAmt)));
											doctorShare.setHospShare(new Double(OrderMasterServiceImpl.df2.format(doctorShare.getHospShare() - hospitalShare)));
											doctorShare.setDocShare(new Double(OrderMasterServiceImpl.df2.format(doctorShare.getDocShare() - negatAmt)));
											doctorShare.setTotalDocShare(new Double(OrderMasterServiceImpl.df2.format(doctorShare.getTotalDocShare() - negatAmt)));
											doctorShare.setAgainstCrId(refundCollectionReqDto.getPaymentRefundMasterDto().getCreditNoteId());
										iDoctorShareDao.saveDoShareObject(doctorShare);
									}else
									{
										TDoctorShareRespDto doctorShare = (TDoctorShareRespDto)doShareRes.getObject();
										
										Double negatAmt =((doctorShare.getSharePer()/100)*creditNoteDetailsDto.getCrAdditionalDiscount());
										Double hospitalSharePer = 100 - doctorShare.getSharePer();
										Double hospitalShare = ((hospitalSharePer/100)*creditNoteDetailsDto.getCrAdditionalDiscount());
											doctorShare.setDueShareAmt(new Double(OrderMasterServiceImpl.df2.format(-negatAmt)));
											doctorShare.setHospShare(new Double(OrderMasterServiceImpl.df2.format(-hospitalShare)));
											doctorShare.setDocShare(new Double(OrderMasterServiceImpl.df2.format(-negatAmt)));
											doctorShare.setAgainstCrId(refundCollectionReqDto.getPaymentRefundMasterDto().getCreditNoteId());
										iDoctorShareDao.saveDoctorShareData(doctorShare);
									}
									
								}
							}
						}
						
					}
				}
				
			}*/
			
			
			if (billPayeeDetails.getCode().equalsIgnoreCase(SUCCESS_CODE)&&creditNoteRes.getCode().equalsIgnoreCase(SUCCESS_CODE)) {
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, paymentRefundMaster.getPaymentRefundVoucherId());
			}
			return new Response(ERROR, ERROR_CODE, COMMON_ERROR_CODE, null, null);
			
			
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response searchRefundedCreditNote(RefundCnSearchDto reqDto) throws BillingException {
		try{
			return iPaymentRefundMasterDao.searchRefundedCreditNote(reqDto);
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response searchRefundedCreditNoteDetails(int orgId, int unitId, int paymentRefundVoucherId)
			throws BillingException {
		try{
			return iPaymentRefundMasterDao.searchRefundedCreditNoteDetails(orgId, unitId, paymentRefundVoucherId);
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response saveRefundAgainstPharamacy(RefundCollectionReqDto refundCollectionReqDto) throws BillingException 
	{
		try{
		Date date = TimeZoneComponent.getDateByzone(refundCollectionReqDto.getListPaymentRefundDetails().getFirst().getUnitId());
		String dateTime = GlobalCommonDateUtils.getStringDate(date, "dd-M-yyyy HH:mm:ss");
		Double loyaltyDepositamount = 0.0;
		Double activeDepositamount = 0.0;
		List<PaymentRefundDetailsDto> listLoyaltyCard  = new ArrayList<PaymentRefundDetailsDto>();
		PaymentRefundMasterDto dto = refundCollectionReqDto.getPaymentRefundMasterDto();
		//BigInteger refundVoucherNo = iPaymentRefundMasterDao.generateRefundVoucherNo();
		dto.setPaymentRefundVoucherNo(iGlobalUnitMasterDao.getGeneratedNo(0, dto.getUnitId(),refundScreenId));
		dto.setDateOfRefund(dateTime);
		dto.setCreatedDate(dateTime);
		List<PaymentRefundDetailsDto> listDeposit = new ArrayList<PaymentRefundDetailsDto>();

		Response refundApi = iPaymentRefundMasterDao.savePaymentRefundMasterDto(dto);
		if (refundApi.getCode().equalsIgnoreCase(SUCCESS_CODE) && refundApi.getObject() != null) 
		{
			PaymentRefundMaster paymentRefundMaster = (PaymentRefundMaster) refundApi.getObject();

			// save payment details
			ListIterator<PaymentRefundDetailsDto> paymentItr = refundCollectionReqDto.getListPaymentRefundDetails().listIterator();
			PaymentRefundDetailsDto detailsDto = null;
			while (paymentItr.hasNext()) {
				detailsDto = paymentItr.next();
				detailsDto.setPaymentRefundId(paymentRefundMaster.getPaymentRefundVoucherId());
				detailsDto.setCreatedDate(dateTime);
				detailsDto.setUpdatedBy(dto.getCreatedBy());
				detailsDto.setUpdatedDate(dateTime);
				detailsDto.setOrgnisationId(paymentRefundMaster.getOrgnisationId());
				detailsDto.setUnitId(paymentRefundMaster.getUnitId());
				if(detailsDto.getPaymentModeId()!=null && detailsDto.getPaymentModeId()==6)
				{
					loyaltyDepositamount += detailsDto.getAmount();
					listLoyaltyCard.add(detailsDto);
				}
				if(detailsDto.getPaymentModeId()!=null && detailsDto.getPaymentModeId()==9)
				{
					activeDepositamount += detailsDto.getAmount();
					listDeposit.add(detailsDto);	
				}
				iPaymentRefundDetailsDao.savePaymentRefundDetails(detailsDto);
			}
			
			
			if(listLoyaltyCard.size()>0)
			{
				this.saveDepositDetailsAgainstRefund(listLoyaltyCard,dto,loyaltyDepositamount,4,6);
			}
			
			if(listDeposit.size()>0)
			{
			
				this.saveDepositDetailsAgainstRefund(listDeposit,dto,activeDepositamount,1,9);
			}
			
		}
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
	}catch(BillingException be){
		throw be;
	}catch(Exception e){
		
		throw new BillingException(e.getMessage());
	}
 }
}
