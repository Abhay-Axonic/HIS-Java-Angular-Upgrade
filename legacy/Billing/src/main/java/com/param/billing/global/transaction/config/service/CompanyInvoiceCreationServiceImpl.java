package com.param.billing.global.transaction.config.service;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.stream.Collectors;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.billing.dto.CompanyReceiptMasterDto;
import com.param.billing.global.dto.BillPaymentCollectionReqDto;
import com.param.billing.global.dto.BillingPaymentDetailsReqDto;
import com.param.billing.global.model.CompanyReceiptDetail;
import com.param.billing.global.model.CompanyReceiptMaster;
import com.param.billing.global.transaction.config.dto.CompanyInvoiceCreationReqDto;
import com.param.billing.global.transaction.config.dto.CompanyInvoiceReminderMasterDto;
import com.param.billing.global.transaction.config.dto.MCompanyInvoiceMasterDto;
import com.param.billing.global.transaction.config.dto.TCompanyInvoiceCreationReqDto;
import com.param.billing.global.transaction.config.dto.TCompanyInvoiceDetailsDto;
import com.param.billing.global.transaction.config.dto.TCompanyInvoiceServiceDetailsDto;
import com.param.billing.global.transaction.dao.IBillPayeeDetailsDao;
import com.param.billing.global.transaction.dao.IBillStatusMapperDao;
import com.param.billing.global.transaction.dao.IBillingDetailsDao;
import com.param.billing.global.transaction.dao.IBillingMasterDao;
import com.param.billing.global.transaction.dao.ICompanyInvoiceCreationDao;
import com.param.billing.global.transaction.dao.ICompanyInvoiceCreationMasterDao;
import com.param.billing.global.transaction.dao.ICompanyReceiptDetailDao;
import com.param.billing.global.transaction.dao.ICompanyReceiptMasterDao;
import com.param.billing.global.transaction.dao.ICompanyReceiptServiceDetailDao;
import com.param.billing.global.transaction.dao.ICompanyRemainderDao;
import com.param.billing.global.transaction.dao.IPaymentDepositConsumptionDao;
import com.param.billing.global.transaction.dao.IPaymentRecieptDetailsDao;
import com.param.billing.global.transaction.dao.IPaymentRecieptMasterDao;
import com.param.billing.global.transaction.dao.ITCompanyInvoiceCreationDao;
import com.param.billing.global.transaction.dao.ITCompanyInvoiceServiceDetailsDao;
import com.param.billing.global.transaction.dto.CompanyInvoiceSettlementReqDto;
import com.param.billing.global.transaction.dto.CompanyInvoiceSettlementSearchReqDto;
import com.param.billing.global.transaction.dto.CompanyReceiptDetailDto;
import com.param.billing.global.transaction.dto.CompanyReceiptServiceDetailsDto;
import com.param.billing.global.transaction.model.BillingStatusMapperId;
import com.param.billing.global.transaction.model.MCompanyInvoiceMaster;
import com.param.billing.global.transaction.model.PaymentRecieptMaster;
import com.param.billing.global.transaction.model.TCompanyInvoiceDetails;
import com.param.billing.global.transaction.service.IBillingMasterService;
import com.param.dozer.DozerFactory;
import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dto.BillingDetailsDto;
import com.param.global.billing.dto.BillingStatusMapperDto;
import com.param.global.billing.dto.PaymentDepositDetailsDto;
import com.param.global.billing.dto.PaymentDepositDetailsList;
import com.param.global.billing.dto.PaymentReceiptDetailsDto;
import com.param.global.billing.dto.PaymentRecieptMasterDto;
import com.param.global.billing.dto.SaleDetailsDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.BillPayeeDetailsDto;
import com.param.global.mpesa.Dao.IMpesaMasterDao;
import com.param.global.mpesa.dto.MpesaMasterDto;
import com.param.global.pharmacy.dao.IWalkInSalesDao;
import com.param.global.service.global.IGlobalService;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

import static com.param.global.common.IBillingCommonConstants.COMPANY_RECEIPTS;

@Transactional
@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class CompanyInvoiceCreationServiceImpl implements ICompanyInvoiceCreationService, ICommonConstants{

	@Autowired
	private ICompanyInvoiceCreationDao iCompanyInvoiceCreationDao;
	@Autowired
	private ICompanyInvoiceCreationMasterDao iCompanyInvoiceCreationMasterDao;
	@Autowired
	private ITCompanyInvoiceCreationDao iTCompanyInvoiceCreationDao;
	@Autowired
	IBillingMasterDao iBillingMasterDao;
	@Autowired
	private ICompanyReceiptMasterDao iCompanyReceiptMasterDao;
	@Autowired
	ICompanyRemainderDao iCompanyRemainderDao;
	@Autowired
	private ICompanyReceiptDetailDao iCompanyReceiptDetailDao;
	@Autowired
	private IPaymentRecieptMasterDao iPaymentRecieptMasterDao;
	@Autowired
	private DozerFactory factory;
	@Autowired
	private IPaymentRecieptDetailsDao iPaymentRecieptDetailsDao;
	@Autowired
	private IBillPayeeDetailsDao iBillPayeeDetailsDao;
	@Autowired
	private IBillStatusMapperDao iBillStatusMapperDao;
	@Autowired
	private IBillingMasterDao billingMasterDao;
	@Autowired
	private ITCompanyInvoiceServiceDetailsDao iTCompanyInvoiceServiceDetailsDao;
	@Autowired
	private IBillingDetailsDao iBillingDetailsDao;
	@Autowired
	private ICompanyReceiptServiceDetailDao iCompanyReceiptServiceDetailDao;
	@Autowired
	IMpesaMasterDao iMpesaMasterDao;
	@Autowired
	private IGlobalDao iGlobalDao;
	@Autowired
	private IGlobalService iGlobalService;
	@Autowired
	IBillingMasterService iBillingMasterService;
	@Autowired
	private IGlobalUnitMasterDao iGlobalUnitMasterDao;
	@Autowired
	IWalkInSalesDao iWalkInSalesDao;
	@Autowired
	IPaymentDepositConsumptionDao iPaymentDepositConsumptionDao;
	@Autowired
	private SessionFactory SessionFactory;
	
	@Override
	@Transactional
	public com.param.global.common.Response getCompanyInvoiceCreationList(
			CompanyInvoiceCreationReqDto companyInvoiceCreationReqDto) throws BillingException {
		try {
			return iCompanyInvoiceCreationDao.getCompanyInvoiceCreationList(companyInvoiceCreationReqDto);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public com.param.global.common.Response getDetailsCompanyInvoiceCreationList(
			CompanyInvoiceCreationReqDto companyInvoiceCreationReqDto) throws BillingException {
		try {
			return iCompanyInvoiceCreationDao.getDetailsCompanyInvoiceCreationList(companyInvoiceCreationReqDto);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response saveCompanyInvoiceCreationList(TCompanyInvoiceCreationReqDto reqDto) throws ApplicationException {
		try {
			BillingDetailsDto billingDetailsDto = null;
			Integer companyInvoiceId = 0;
			MCompanyInvoiceMasterDto mCompanyInvoiceMasterDto = reqDto.getmCompanyInvoiceMasterDto();
			
			
			if(mCompanyInvoiceMasterDto != null){
				//to solve concurrency issue
				Response respsp = iBillingMasterDao.checkInvoiceCreatedOrNotByBillId(reqDto);
				 if(respsp.getListObject() != null && respsp.getListObject().size() > 0) {
					 return new Response<>(ERROR, ERROR_CODE, "This transaction already processed by other user", null, null);
				 }
					 
			}
				
			if(mCompanyInvoiceMasterDto != null){
				mCompanyInvoiceMasterDto.setOrgId(reqDto.getOrgId());
				mCompanyInvoiceMasterDto.setUnitId(reqDto.getUnitId());
				if(mCompanyInvoiceMasterDto.getBillNumber() == null) {
					mCompanyInvoiceMasterDto.setInvoiceNumber(iGlobalUnitMasterDao.getGeneratedNo(0, reqDto.getUnitId(), IBillingCommonConstants.COMPANY_INVOICE_NUMBER));
					mCompanyInvoiceMasterDto.setInvoiceNumberSystemGen('N');
				}else {
					mCompanyInvoiceMasterDto.setInvoiceNumber(mCompanyInvoiceMasterDto.getBillNumber());
					mCompanyInvoiceMasterDto.setInvoiceNumberSystemGen('Y');
				}
				Response mCompanyInvoiceMasterRes = iCompanyInvoiceCreationMasterDao.saveCompanyInvoiceCreationMasterList(mCompanyInvoiceMasterDto);
				if(mCompanyInvoiceMasterRes.getCode().equalsIgnoreCase(SUCCESS_CODE) && mCompanyInvoiceMasterRes.getObject() != null){
					MCompanyInvoiceMaster mCompanyInvoiceMaster = (MCompanyInvoiceMaster)mCompanyInvoiceMasterRes.getObject();
					companyInvoiceId = mCompanyInvoiceMaster.getCompanyInvoiceId();
					ListIterator<TCompanyInvoiceDetailsDto> servicDtlsItr = reqDto.getListTCompanyInvoiceDetailsDto().listIterator();
					TCompanyInvoiceDetailsDto tCompanyInvoiceDetailsDto = null;
					while(servicDtlsItr.hasNext())
					{
						tCompanyInvoiceDetailsDto = servicDtlsItr.next();
						tCompanyInvoiceDetailsDto.setCompanyInvoiceId(companyInvoiceId);
						tCompanyInvoiceDetailsDto.setOrgId(reqDto.getOrgId());
						tCompanyInvoiceDetailsDto.setUnitId(reqDto.getUnitId());
						Response mCompanyInvoiceDtlsRes = iTCompanyInvoiceCreationDao.saveCompanyInvoiceCreationMasterList(tCompanyInvoiceDetailsDto);
						if(mCompanyInvoiceDtlsRes.getCode().equalsIgnoreCase(SUCCESS_CODE) && mCompanyInvoiceDtlsRes.getObject() != null){
							
							TCompanyInvoiceDetails responnseInvoiceDetailsDto = (TCompanyInvoiceDetails)mCompanyInvoiceDtlsRes.getObject();
							Integer companyInvoiceDetailId = responnseInvoiceDetailsDto.getCompanyInvoiceDetailId();
						
							//Ganesh bill && pharamacy
							if(tCompanyInvoiceDetailsDto.getBillId()!=null && tCompanyInvoiceDetailsDto.getBillId()>0)
							{
								iBillingMasterDao.updateBillMasterIsInvoiceCreationByBillId(tCompanyInvoiceDetailsDto.getBillId(),mCompanyInvoiceMasterDto.getPayeeId(),mCompanyInvoiceMasterDto.getAssociateCompanyId());
								//get service order details against billId
								billingDetailsDto = new BillingDetailsDto();
								billingDetailsDto.setBillingMasterId(tCompanyInvoiceDetailsDto.getBillId());
								billingDetailsDto.setPayeeId(mCompanyInvoiceMasterDto.getPayeeId());
								billingDetailsDto.setAssociateCompanyId(mCompanyInvoiceMasterDto.getAssociateCompanyId());
								com.param.billing.common.Response billDetailsRes = iBillingDetailsDao.getBillingDetailsByBillId(billingDetailsDto);
								if(billDetailsRes.getListObject() != null && billDetailsRes.getListObject().size() > 0){
									
									List<BillingDetailsDto> listBillingDetails = billDetailsRes.getListObject();
									
									for(BillingDetailsDto dto : listBillingDetails){
										
										//save company invoice service details
										
										TCompanyInvoiceServiceDetailsDto invoiceServiceDetailsDto = new TCompanyInvoiceServiceDetailsDto();
											invoiceServiceDetailsDto.setInvoiceId(companyInvoiceId);
											invoiceServiceDetailsDto.setInvoiceDetailId(companyInvoiceDetailId);
											invoiceServiceDetailsDto.setOrgId(reqDto.getOrgId());
											invoiceServiceDetailsDto.setOrgUnitId(reqDto.getUnitId());
											invoiceServiceDetailsDto.setBillDetailId(dto.getBillingDetailsId());
											invoiceServiceDetailsDto.setServiceId(dto.getServiceId());
											invoiceServiceDetailsDto.setCreditPayableAmount(dto.getCreditPayable());
											invoiceServiceDetailsDto.setCrNetAmount(dto.getCrNetAmount());
											invoiceServiceDetailsDto.setDueAmount(dto.getCreditPayable()-dto.getCrNetAmount());
											invoiceServiceDetailsDto.setReceivedAmount(0.0);
											invoiceServiceDetailsDto.setDisallowedAmount(0.0);
										iTCompanyInvoiceServiceDetailsDao.saveCompanyInvoiceServiceDetails(invoiceServiceDetailsDto);
									}
								}
							}
							else if(tCompanyInvoiceDetailsDto.gettSaleId()!=null && tCompanyInvoiceDetailsDto.gettSaleId()>0)
							{
								iGlobalDao.updateSaleMasterIsInvoiceCreationBySaleId(tCompanyInvoiceDetailsDto.gettSaleId(),mCompanyInvoiceMasterDto.getPayeeId());
								List<SaleDetailsDto> saleDetails = iGlobalDao.getBillingDetailsBySaleId(tCompanyInvoiceDetailsDto.gettSaleId(), mCompanyInvoiceMasterDto.getPayeeId());
								if(saleDetails!= null && saleDetails.size() > 0){
									for(SaleDetailsDto dto : saleDetails){
										//save company invoice items
										TCompanyInvoiceServiceDetailsDto invoiceServiceDetailsDto = new TCompanyInvoiceServiceDetailsDto();
											invoiceServiceDetailsDto.setInvoiceId(companyInvoiceId);
											invoiceServiceDetailsDto.setInvoiceDetailId(companyInvoiceDetailId);
											invoiceServiceDetailsDto.setOrgId(reqDto.getOrgId());
											invoiceServiceDetailsDto.setOrgUnitId(reqDto.getUnitId());
											invoiceServiceDetailsDto.settSaleDetailsId(dto.getId().intValue());
											invoiceServiceDetailsDto.setItemId(dto.getItemId());
											invoiceServiceDetailsDto.setCreditPayableAmount(dto.getCreditPayable());
											invoiceServiceDetailsDto.setDueAmount(dto.getCreditPayable());
											invoiceServiceDetailsDto.setReceivedAmount(0.0);
											invoiceServiceDetailsDto.setDisallowedAmount(0.0);
										iTCompanyInvoiceServiceDetailsDao.saveCompanyInvoiceServiceDetails(invoiceServiceDetailsDto);
									}
								}
								if(reqDto.getIsConsolidatedBill()!=null && (reqDto.getIsConsolidatedBill()=='Y' || reqDto.getIsConsolidatedBill()=='N') )
								{
									iWalkInSalesDao.inactiveOrderDetailsBySaleId(tCompanyInvoiceDetailsDto.gettSaleId().intValue(), null);
								}
							}
				
						}
							
					}
				}
				
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, companyInvoiceId);
			}else {
				return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
		
	} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	
	}

	@Override
	@Transactional
	public Response getcompanyRemainderList(CompanyInvoiceCreationReqDto companyInvoiceCreationReqDto)
			throws BillingException {
		try {
			return iCompanyInvoiceCreationMasterDao.getcompanyRemainderList(companyInvoiceCreationReqDto);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response saveCompanyReminder(CompanyInvoiceReminderMasterDto reqDto) throws ApplicationException {
		try {
			return iCompanyRemainderDao.saveCompanyRemainder(reqDto);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response updateCompanyReminderRemark(CompanyInvoiceReminderMasterDto reqDto) throws ApplicationException {
		try {
			return iCompanyRemainderDao.updateCompanyRemainderByCompanyInvoiceReminderId(reqDto);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getCompInvoiceForSettlement(CompanyInvoiceSettlementSearchReqDto reqDto) throws BillingException {
		try {
			return iCompanyInvoiceCreationMasterDao.getCompInvoiceForSettlement(reqDto);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response settleCompanyInvoice(CompanyInvoiceSettlementReqDto reqDto) throws BillingException {
		try {
			List<Integer> companyReceiptIds = new LinkedList<Integer>();
			CompanyReceiptDetail companyReceiptServiceDetails = null;
			int orgId = 0;
			int unitId = 0;
			Integer	depositConsumptionId=0;
			Double totalConsumption = 0.00;
			//solve concurrancy issue
			
			Response resCheck =iPaymentDepositConsumptionDao.checkInvoiceSettleOrNotByBillId(reqDto);
			 if(resCheck.getListObject() != null && resCheck.getListObject().size() > 0) {
				 return new Response<>(ERROR, ERROR_CODE, "This transaction already processed by other user,Please refresh the page", null, null);
			 }
			
			//get company invoices
			if(reqDto.getCompanyReceipt() != null)
			{
					//For Company Receipt Comsumption
					if(reqDto.getPaymentDepositDetailsList() != null && reqDto.getPaymentDepositDetailsList().size() > 0)
					{
						totalConsumption = reqDto.getPaymentDepositDetailsList().stream().collect(Collectors.summingDouble(PaymentDepositDetailsList::getConsumedAmount));
						if(totalConsumption>0)
						{
							BillPaymentCollectionReqDto billPaymentCollectionReqDto = new BillPaymentCollectionReqDto();
								billPaymentCollectionReqDto.setOrgnizationId(orgId);
								billPaymentCollectionReqDto.setUnitId(unitId);
								billPaymentCollectionReqDto.setUpdatedBy(reqDto.getCompanyReceipt().getCreatedBy());
								billPaymentCollectionReqDto.setCreatedBy(reqDto.getCompanyReceipt().getCreatedBy());
								billPaymentCollectionReqDto.setConsumedAmount(totalConsumption);
								billPaymentCollectionReqDto.setDepositeConsumptionNumber(iGlobalUnitMasterDao.getGeneratedNo(0, unitId, IBillingCommonConstants.DEPOSIT_CONSUMPTION_NUMBER));
								billPaymentCollectionReqDto.setStatus('A');
							Response res =iPaymentDepositConsumptionDao.addDepositConsumption(billPaymentCollectionReqDto);
							depositConsumptionId = (Integer) res.getObject();
						
							for(PaymentDepositDetailsList paymentDepositDetailsLists : reqDto.getPaymentDepositDetailsList())
							{
								iCompanyInvoiceCreationMasterDao.updateCompanyInvoiceDueAmount(paymentDepositDetailsLists.getAvailableDeposit(),paymentDepositDetailsLists.getCompanyReceiptsExpenseId());
								
								PaymentDepositDetailsDto paymentDepositDetailsDtoConsumption = new PaymentDepositDetailsDto();	
									paymentDepositDetailsDtoConsumption.setOrganizationId(orgId);
									paymentDepositDetailsDtoConsumption.setUnitId(unitId);
									paymentDepositDetailsDtoConsumption.setDepConsumptionId(depositConsumptionId);
									paymentDepositDetailsDtoConsumption.setDepositConsumed(paymentDepositDetailsLists.getConsumedAmount());
									paymentDepositDetailsDtoConsumption.setUpdatedBy(reqDto.getCompanyReceipt().getCreatedBy());
									paymentDepositDetailsDtoConsumption.setCompanyReceiptsExpenseId(paymentDepositDetailsLists.getCompanyReceiptsExpenseId());
								iPaymentDepositConsumptionDao.addDepositConsumptionDetails(paymentDepositDetailsDtoConsumption);
							}
						}
					}
					//End Company Receipt Consumption 
				
					CompanyReceiptMasterDto companyReceiptDto = reqDto.getCompanyReceipt();
//						companyReceiptDto.setCompanyReceiptNumber(GlobalCommonDateUtils.generateRandomString());
						companyReceiptDto.setCompanyReceiptNumber(iGlobalUnitMasterDao.getGeneratedGlobalNo(0, companyReceiptDto.getOrgId(),companyReceiptDto.getUnitId(), COMPANY_RECEIPTS));
						if(depositConsumptionId!=null && depositConsumptionId > 0)
							companyReceiptDto.setDepositConsumptionId(depositConsumptionId);
					Response compRecpRes = iCompanyReceiptMasterDao.saveCompanyReceiptMaster(companyReceiptDto);
					
					companyReceiptDto.setAssociateCompanyId(companyReceiptDto.getAssociateCompanyId()!=null ? companyReceiptDto.getAssociateCompanyId() : 0);
					if(compRecpRes.getCode().equals(SUCCESS_CODE) && compRecpRes.getObject() != null)
					{
						CompanyReceiptMaster receiptMaster = (CompanyReceiptMaster)compRecpRes.getObject();
						companyReceiptIds.add(receiptMaster.getCompanyReceiptId());
						if(companyReceiptDto.getReceiptDetails() != null && companyReceiptDto.getReceiptDetails().size() > 0)
						{
							//get receipt details
							for(CompanyReceiptDetailDto receiptDetailsDto : companyReceiptDto.getReceiptDetails())
							{
								orgId = receiptDetailsDto.getOrgId();
								unitId = receiptDetailsDto.getUnitId();
								Double  totalAmount = (receiptDetailsDto.getNetReceiptAmount().add(receiptDetailsDto.getTdsAmount()).doubleValue()) + (receiptDetailsDto.getDisallowedAmount().add(receiptDetailsDto.getOtherDeduction()).doubleValue()) ;
								iTCompanyInvoiceCreationDao.updateDueAmount(receiptDetailsDto.getCompanyInvoiceDetailId(), totalAmount);
								receiptDetailsDto.setCompanyReceiptId(receiptMaster.getCompanyReceiptId());
								Response companyReceipDtlRes = iCompanyReceiptDetailDao.saveCompanyReceiptDetail(receiptDetailsDto);
								if(companyReceipDtlRes.getObject() != null ){
									companyReceiptServiceDetails = (CompanyReceiptDetail)companyReceipDtlRes.getObject(); 
								}
								
								if(receiptDetailsDto.getBillId()!=null && receiptDetailsDto.getBillId()>0)
								{
									//update invoice details due amount
									
									//update bill payee details
									iBillPayeeDetailsDao.updateBillPayeeDueAmountForCompanyInvoice(receiptDetailsDto.getBillId(), companyReceiptDto.getPayeeId(),companyReceiptDto.getAssociateCompanyId(), (receiptDetailsDto.getNetReceiptAmount().add(receiptDetailsDto.getTdsAmount())).doubleValue(),receiptDetailsDto.getDisallowedAmount().doubleValue(),receiptDetailsDto.getOtherDeduction().doubleValue());
									
							
									 Double amt = receiptDetailsDto.getOutstandingAmt().subtract(receiptDetailsDto.getNetReceiptAmount().add(receiptDetailsDto.getTdsAmount())).doubleValue(); 
								
									
									//update bill master amount
									iBillingMasterDao.updateOutStandingByBillIdForCompanyInvoiceSett(receiptDetailsDto.getBillId(),amt,receiptDetailsDto.getCreatedBy());
									
									//check if bill outstanding is zero to update bill status
									boolean result = iBillingMasterDao.checkOutstandingIsClearedForCompanyInvoice(receiptDetailsDto.getBillId(),receiptDetailsDto.getNetReceiptAmount().doubleValue());
									
									if(receiptDetailsDto.getOutstandingAmt().doubleValue()>receiptDetailsDto.getNetReceiptAmount().add(receiptDetailsDto.getTdsAmount()).doubleValue()) {
										result = false;
									}
									
									if(result)
									{
										//mark bill as settled
										BillingStatusMapperDto mapperDto = new BillingStatusMapperDto();
										BillingStatusMapperId mapperId = new BillingStatusMapperId();
											mapperId.setBillId(receiptDetailsDto.getBillId());
											mapperId.setBillStatusId(3);//settled
											mapperDto.setBillingStatusMapperId(mapperId);
											mapperDto.setCreatedBy(receiptDetailsDto.getCreatedBy());
											mapperDto.setOrgnisationId(receiptDetailsDto.getOrgId());
											mapperDto.setUnitId(receiptDetailsDto.getUnitId());
											mapperDto.setStatus(ACTIVE);
										iBillStatusMapperDao.saveBillStatus(mapperDto);
										
										//check if bill outstanding is zero to update bill status on Bill Master
										billingMasterDao.updateBillStatusByBillId(3,receiptDetailsDto.getBillId());
									}else{
										
										BillingStatusMapperDto mapperDto = new BillingStatusMapperDto();
										BillingStatusMapperId mapperId = new BillingStatusMapperId();
											mapperId.setBillId(receiptDetailsDto.getBillId());
											mapperId.setBillStatusId(2);//onhold
											mapperDto.setBillingStatusMapperId(mapperId);
											mapperDto.setCreatedBy(receiptDetailsDto.getCreatedBy());
											mapperDto.setOrgnisationId(receiptDetailsDto.getOrgId());
											mapperDto.setUnitId(receiptDetailsDto.getUnitId());
											mapperDto.setStatus(ACTIVE);
										iBillStatusMapperDao.saveBillStatus(mapperDto);
										
										billingMasterDao.updateBillStatusByBillId(2,receiptDetailsDto.getBillId());
									}
									
									//here we need to create debit note if the bill contains any disallowed amount.
									if((receiptDetailsDto.getOtherDeduction()!=null && receiptDetailsDto.getOtherDeduction().doubleValue()>0) 
											|| (receiptDetailsDto.getDisallowedAmount()!=null && receiptDetailsDto.getDisallowedAmount().doubleValue()>0))
									{
										receiptDetailsDto.setPayeeId(companyReceiptDto.getPayeeId());
										receiptDetailsDto.setAssociateCompanyId(companyReceiptDto.getAssociateCompanyId());
										List<TCompanyInvoiceServiceDetailsDto> serviceDetilsList = new LinkedList<>();
										if(reqDto.getRecvAndDisallAmountDetails() != null && reqDto.getRecvAndDisallAmountDetails().size()>0)
										{
											for(TCompanyInvoiceServiceDetailsDto dto : reqDto.getRecvAndDisallAmountDetails())
											{
												if(dto.getBillingMasterId().equals(receiptDetailsDto.getBillId()))
												{
													serviceDetilsList.add(dto);
												}
											}
											receiptDetailsDto.setServiceDetilsList(serviceDetilsList);
										}
										iBillingMasterDao.updateDisallowedAmountToSelfAmount(receiptDetailsDto,reqDto.getSelfPayeeId());
									 }
									
									
								}
								else if(receiptDetailsDto.getSaleId()!=null && receiptDetailsDto.getSaleId()>0)
								{
									
									Integer status = 0;
									 //Double amt = 0.0;
								/*
								 * if (totalConsumption >0) { amt = (
								 * receiptDetailsDto.getDisallowedAmount().add(receiptDetailsDto.
								 * getOtherDeduction())).doubleValue(); }else { amt =
								 * receiptDetailsDto.getOutstandingAmt().subtract(receiptDetailsDto.
								 * getTotalAmount()).add(receiptDetailsDto.getDisallowedAmount().add(
								 * receiptDetailsDto.getOtherDeduction())).doubleValue(); }
								 */
                                  
									 Double amt = receiptDetailsDto.getOutstandingAmt().subtract((receiptDetailsDto.getNetReceiptAmount().add(receiptDetailsDto.getTdsAmount()))).doubleValue();
									 Double selfDueAmount =iBillPayeeDetailsDao.getSelfDueAmount(receiptDetailsDto.getSaleId(), companyReceiptDto.getPayeeId());
									 amt=amt+selfDueAmount;
									//Double amt = receiptDetailsDto.getOutstandingAmt().subtract(receiptDetailsDto.getNetReceiptAmount()).doubleValue(); 
									if(amt.equals(0.00))
									{
										//iGlobalDao.updatePharamcyBillStataus(receiptDetailsDto.getSaleId(), com.param.common.constants.Status.Completed.getStatus());
										status = com.param.common.constants.Status.Completed.getStatus();
									}else {
										//iGlobalDao.updatePharamcyBillStataus(receiptDetailsDto.getSaleId(), com.param.common.constants.Status.PartiallyCompleted.getStatus());
										status = com.param.common.constants.Status.PartiallyCompleted.getStatus();
									}
							             
									iBillPayeeDetailsDao.updatePharamcyBillPayeeDueAmountForCompanyInvoice(receiptDetailsDto.getSaleId(), companyReceiptDto.getPayeeId(),companyReceiptDto.getAssociateCompanyId(), receiptDetailsDto.getTotalAmount().doubleValue(),receiptDetailsDto.getDisallowedAmount().doubleValue(),receiptDetailsDto.getOtherDeduction().doubleValue());
									//update bill master amount
									
									//iBillingMasterDao.updatePharmacyOutStandingByBillIdForCompanyInvoiceSett(receiptDetailsDto.getSaleId(), receiptDetailsDto.getOutstandingAmt().subtract(receiptDetailsDto.getTotalAmount()).doubleValue(),receiptDetailsDto.getCreatedBy());
									iGlobalService.updatePharamcyDetailsPaidAmount(receiptDetailsDto.getSaleId(), (receiptDetailsDto.getNetReceiptAmount().add(receiptDetailsDto.getTdsAmount())).doubleValue(),status,companyReceiptDto.getPaymentEntitlementId(),amt);
									
									
									if((receiptDetailsDto.getOtherDeduction()!=null && receiptDetailsDto.getOtherDeduction().doubleValue()>0) || (receiptDetailsDto.getDisallowedAmount()!=null && receiptDetailsDto.getDisallowedAmount().doubleValue()>0))
									{
										receiptDetailsDto.setPayeeId(companyReceiptDto.getPayeeId());
										List<TCompanyInvoiceServiceDetailsDto> serviceDetilsList = new LinkedList<>();
										if(reqDto.getRecvAndDisallAmountDetails() != null && reqDto.getRecvAndDisallAmountDetails().size()>0)
										{
											for(TCompanyInvoiceServiceDetailsDto dto : reqDto.getRecvAndDisallAmountDetails())
											{
												if(dto.getSaleId().equals(receiptDetailsDto.getSaleId()))
												{
													serviceDetilsList.add(dto);
												}
											}
											receiptDetailsDto.setServiceDetilsList(serviceDetilsList);
										}
										iBillingMasterDao.updatePharmacyDisallowedAmountToSelfAmount(receiptDetailsDto,reqDto.getSelfPayeeId());
									 }
									
								}
								
							}
						}
						
						String dateTime = GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(companyReceiptDto.getUnitId()), "dd-M-yyyy HH:mm:ss");
						//insert payment details
						if((reqDto.getPaymentDetails() != null && reqDto.getPaymentDetails().size() > 0) || (reqDto.getPaymentDepositDetailsList() != null && reqDto.getPaymentDepositDetailsList().size() > 0))
						{
							PaymentRecieptMasterDto paymentRcptMst = new PaymentRecieptMasterDto();
								paymentRcptMst.setOrgnizationId(companyReceiptDto.getOrgId());
								paymentRcptMst.setUnitId(companyReceiptDto.getUnitId());
								paymentRcptMst.setAmount(companyReceiptDto.getNetReceiptAmount());
								paymentRcptMst.setRecieptNumber(iGlobalUnitMasterDao.getGeneratedNo(0, unitId, IBillingCommonConstants.RECIEPT_NUMBER));
								paymentRcptMst.setDateOfReciept(dateTime);
								paymentRcptMst.setPayeeId(companyReceiptDto.getPayeeId());
								paymentRcptMst.setCreatedDate(dateTime);
								paymentRcptMst.setCreatedBy(companyReceiptDto.getCreatedBy());
								paymentRcptMst.setCompanyInvoiceId(receiptMaster.getCompanyReceiptId());
							Response paymentRecptRes = iPaymentRecieptMasterDao.savePaymentRecieptMaster(paymentRcptMst);
							if(paymentRecptRes.getCode().equals(SUCCESS_CODE) && paymentRecptRes.getObject() != null)
							{
								PaymentRecieptMaster paymentReceiptId = (PaymentRecieptMaster)paymentRecptRes.getObject();
								
								//merge payment receipt id to company receipt
								receiptMaster.setReceiptId(paymentReceiptId.getPaymentRecieptId());
								iCompanyReceiptMasterDao.mergeCompanyReceipt(receiptMaster);
								
								//save payment receipt details
								if(reqDto.getPaymentDetails()!=null && reqDto.getPaymentDetails().size()>0)
								{
									MpesaMasterDto masterDto = new MpesaMasterDto();
									for(BillingPaymentDetailsReqDto paymentDetails : reqDto.getPaymentDetails())
									{
										if(paymentDetails.getPaymentModeId() == 5 && (paymentDetails.getSkipMpesaValidation()!=null && paymentDetails.getSkipMpesaValidation())) 
										{
												masterDto.setMpesaTxcode(paymentDetails.getCardNo());	
												masterDto.setUnitId(unitId);
												masterDto.setOrganizationId(orgId);
												masterDto.setUpdatedBy(paymentDetails.getCreatedBy());
												masterDto.setMpesaAmount(paymentDetails.getAmount());
											    iMpesaMasterDao.validateMpesaTrancastion(masterDto);
											
										}
										PaymentReceiptDetailsDto paymentReceiptDtls = factory.map(paymentDetails, PaymentReceiptDetailsDto.class, null);
										paymentReceiptDtls.setPaymentReceiptMasterId(paymentReceiptId.getPaymentRecieptId());
										if(paymentDetails.getChequeDate() != null && !paymentDetails.getChequeDate().isEmpty())
										{
											paymentReceiptDtls.setChequeDate(paymentDetails.getChequeDate().concat(" 00:00:00"));
											paymentReceiptDtls.setHospitalAccountId(paymentDetails.getHospitalAccountId());
										}
										else
										{
											paymentReceiptDtls.setChequeDate(null);
											paymentReceiptDtls.setHospitalAccountId(null);
										}
										//paymentReceiptDtls.setChequeDate(paymentDetails.getChequeDate() != null && !paymentDetails.getChequeDate().isEmpty() ? (paymentDetails.getChequeDate().concat(" 00:00:00")) : null);
											paymentReceiptDtls.setCardNumber(paymentDetails.getCardNo());
											paymentReceiptDtls.setDateOfTransaction(dateTime);
											paymentReceiptDtls.setOrgnizationId(companyReceiptDto.getOrgId());
											paymentReceiptDtls.setStatus(ACTIVE);
											paymentReceiptDtls.setUnitId(companyReceiptDto.getUnitId());
											paymentReceiptDtls.setCreatedDate(dateTime);
											paymentReceiptDtls.setTransTransactionDate(paymentDetails.getTransTransactionDate() != null && !paymentDetails.getTransTransactionDate().isEmpty() ? (paymentDetails.getTransTransactionDate().concat(" 00:00:00")) : null);
										iPaymentRecieptDetailsDao.savePaymentRecieptDetails(paymentReceiptDtls);
										
									}
								}
								if(reqDto.getPaymentDepositDetailsList() != null && reqDto.getPaymentDepositDetailsList().size() > 0)
								{
									PaymentReceiptDetailsDto paymentReceiptDetailsDto = new PaymentReceiptDetailsDto();
										paymentReceiptDetailsDto.setPaymentReceiptMasterId(paymentReceiptId.getPaymentRecieptId());
										paymentReceiptDetailsDto.setOrgnizationId(companyReceiptDto.getOrgId());
										paymentReceiptDetailsDto.setUnitId(companyReceiptDto.getUnitId());
										paymentReceiptDetailsDto.setCreatedBy(companyReceiptDto.getCreatedBy());
										paymentReceiptDetailsDto.setUpdatedBy(companyReceiptDto.getCreatedBy());
										paymentReceiptDetailsDto.setStatus('A');
										paymentReceiptDetailsDto.setIsCancle('N');
										paymentReceiptDetailsDto.setStatus('A');
										paymentReceiptDetailsDto.setAmount(totalConsumption);
										paymentReceiptDetailsDto.setPaymentModeId(10);
										paymentReceiptDetailsDto.setAvailableFund(0.00);
									iPaymentRecieptDetailsDao.savePaymentRecieptDetails(paymentReceiptDetailsDto);
								}
								
							}
							
						}
						
						//update company invoice service details
						if(reqDto.getRecvAndDisallAmountDetails() != null && reqDto.getRecvAndDisallAmountDetails().size() >0)
						{
							double recAmt;
							double disallAmt;
							double dueAmout;
							double otherDeduction;
							for(TCompanyInvoiceServiceDetailsDto invoiceServiceDto : reqDto.getRecvAndDisallAmountDetails())
							{
									recAmt = invoiceServiceDto.getReceivedAmount() != null ? invoiceServiceDto.getReceivedAmount() : 0;
									disallAmt = invoiceServiceDto.getDisallowedAmount() != null ? invoiceServiceDto.getDisallowedAmount() : 0;
									dueAmout = invoiceServiceDto.getDueAmount() != null ? invoiceServiceDto.getDueAmount() : 0;
									otherDeduction = invoiceServiceDto.getOtherDeduction() != null ? invoiceServiceDto.getOtherDeduction() : 0;
								
									invoiceServiceDto.setReceivedAmount(recAmt);
									invoiceServiceDto.setDisallowedAmount(disallAmt);
									invoiceServiceDto.setDueAmount(dueAmout);
									invoiceServiceDto.setOtherDeduction(otherDeduction);
								Response serviceRes = iTCompanyInvoiceServiceDetailsDao.updateRecvAndDisallAmtByInvDtls(invoiceServiceDto);
								if(serviceRes.getStatus().equalsIgnoreCase(SUCCESS))
								{
									CompanyReceiptServiceDetailsDto dto = new CompanyReceiptServiceDetailsDto();
										dto.setServiceId(invoiceServiceDto.getServiceId());
										dto.setItemId(invoiceServiceDto.getItemId());
										dto.setReceivedAmount(recAmt);
										dto.setDisallowedAmount(disallAmt);
										dto.setOtherDeduction(otherDeduction);
										dto.setComReceiptDtlId(companyReceiptServiceDetails.getCompanyReceiptDetailId());
										dto.setOrgId(orgId);
										dto.setOrgUnitId(unitId);
										dto.setTdsAmount(invoiceServiceDto.getTdsAmount());
									iCompanyReceiptServiceDetailDao.saveCompanyReceiptServiceDetail(dto);
								}
							}
						}
						
					}
					//update invoice master due amount
					Map<Integer,Double> receivedAmounts = reqDto.getCompanyRecptRecAmount();
					if(receivedAmounts != null && receivedAmounts.size() > 0)
					{
						for(Map.Entry<Integer, Double> recAmt : receivedAmounts.entrySet()) {
							iCompanyInvoiceCreationMasterDao.updateCompanyInvoiceDueAmount(recAmt.getKey(), recAmt.getValue());
						    iCompanyInvoiceCreationMasterDao.updateCompanyInvoiceSettlementDate(recAmt.getKey(), recAmt.getValue());
						}
					}
				
				return new Response<>(SUCCESS, SUCCESS_CODE, INVOICE_SETTLED, null, null);
			}
			return new Response<>(ERROR, NOT_FOUND_CODE, NOT_FOUND, null, null);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response updateBillStatus(CompanyInvoiceSettlementReqDto dto) throws BillingException {
		try{
			if(dto.getCompanyReceipt() != null){
				CompanyReceiptMasterDto companyReceiptDto = dto.getCompanyReceipt();
				if(companyReceiptDto.getReceiptDetails() != null && companyReceiptDto.getReceiptDetails().size() > 0){
					//get receipt details
					ListIterator<CompanyReceiptDetailDto> receiptDtlsItr = companyReceiptDto.getReceiptDetails().listIterator();
					
					while(receiptDtlsItr.hasNext()){
						CompanyReceiptDetailDto receiptDetailsDto = receiptDtlsItr.next();
						
						//check if bill outstanding is zero to update bill status
						boolean result = iBillingMasterDao.checkOutstandingIsClearedForCompanyInvoice1(receiptDetailsDto.getBillId());
						if(result){
							//mark bill as settled
							BillingStatusMapperDto mapperDto = new BillingStatusMapperDto();
							BillingStatusMapperId mapperId = new BillingStatusMapperId();
							mapperId.setBillId(receiptDetailsDto.getBillId());
							mapperId.setBillStatusId(3);//settled
							mapperDto.setBillingStatusMapperId(mapperId);
							mapperDto.setCreatedBy(receiptDetailsDto.getCreatedBy());
							mapperDto.setOrgnisationId(receiptDetailsDto.getOrgId());
							mapperDto.setUnitId(receiptDetailsDto.getUnitId());
							mapperDto.setStatus(ACTIVE);
							iBillStatusMapperDao.saveBillStatus(mapperDto);
							
							//check if bill outstanding is zero to update bill status on Bill Master
							billingMasterDao.updateBillStatusByBillId(3,receiptDetailsDto.getBillId());
						}
					}
				}
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getCompanyInvoiceSettlementDetailsByCompanyInvoiceId(CompanyInvoiceSettlementSearchReqDto reqDto)
			throws BillingException {
		try{
			return iCompanyInvoiceCreationDao.getCompanyInvoiceSettlementDetailsByCompanyInvoiceId(reqDto);
		}catch(Exception e){
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getCompInvoiceForSettlementDetails(CompanyInvoiceSettlementSearchReqDto reqDto)
			throws BillingException {
		try{
			return iCompanyInvoiceCreationDao.getCompInvoiceForSettlementDetails(reqDto);
		}catch(Exception e){
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response companyInvoiceCreationListByBillId(CompanyInvoiceCreationReqDto companyInvoiceCreationReqDto)
			throws BillingException {
		try {
			return iCompanyInvoiceCreationDao.companyInvoiceCreationListByBillId(companyInvoiceCreationReqDto);
		} catch (Exception e) {
			
			throw e;
		}
	}

	

}
