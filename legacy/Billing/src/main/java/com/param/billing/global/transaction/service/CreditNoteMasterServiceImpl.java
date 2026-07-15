package com.param.billing.global.transaction.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.billing.global.dto.RefundCnSearchDto;
import com.param.billing.global.model.CompanyReceiptsExpense;
import com.param.billing.global.service.IRequestDiscountService;
import com.param.billing.global.transaction.config.dao.IRequestDiscountDao;
import com.param.billing.global.transaction.config.dto.CompanyReceiptsDto;
import com.param.billing.global.transaction.config.dto.CompanyRecieptsReceiptDetailsDto;
import com.param.billing.global.transaction.dao.IBillPayeeDetailsDao;
import com.param.billing.global.transaction.dao.IBillingMasterDao;
import com.param.billing.global.transaction.dao.ICompanyReceiptsDao;
import com.param.billing.global.transaction.dao.ICreditNoteDetailsDao;
import com.param.billing.global.transaction.dao.ICreditNoteDiscountDetailsDao;
import com.param.billing.global.transaction.dao.ICreditNoteMasterDao;
import com.param.billing.global.transaction.dao.ITCompanyInvoiceCreationDao;
import com.param.billing.global.transaction.dto.CreditNoteDetailsDto;
import com.param.billing.global.transaction.dto.CreditNoteDiscountDetailsDto;
import com.param.billing.global.transaction.dto.CreditNoteMasterDto;
import com.param.billing.global.transaction.model.BillPayeeDetails;
import com.param.billing.global.transaction.model.BillingMaster;
import com.param.billing.global.transaction.model.CreditNoteDetails;
import com.param.billing.global.transaction.model.CreditNoteMaster;
import com.param.billing.global.transaction.model.MCompanyInvoiceMaster;
import com.param.billing.global.transaction.model.TCompanyInvoiceDetails;
import com.param.billing.global.transaction.model.TCompanyInvoiceServiceDetails;
import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dao.IpdChargesDao;
import com.param.global.billing.dto.AdmissionListIpdChargesResDto;
import com.param.global.billing.dto.RequestDiscountDto;
import com.param.global.billing.service.IContractConsumptionService;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.global.dao.IMPackageMasterDao;
import com.param.global.dao.IOrderDetailsMasterDao;
import com.param.global.dao.ITPackageConsumptionMasterDao;
import com.param.global.dto.ContractConsumptionDto;
import com.param.global.dto.OrderDetailsByEncounterIdDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.OrderDetailsPackageMapperDto;
import com.param.global.dto.OrderDetailsPayeeMapperDto;
import com.param.global.dto.OrderMasterDto;
import com.param.global.model.OrderDetailsMaster;
import com.param.global.opd.service.IEncounterMasterService;
import com.param.global.service.IOrderDetailsMasterService;
import com.param.global.service.IOrderMasterService;
import com.param.global.utility.SerialNoUtility;
import com.param.opd.encounter.dto.EncounterMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class CreditNoteMasterServiceImpl implements ICreditNoteMasterService, ICommonConstants,IBillingCommonConstants {
	
	
	@Autowired
	ICreditNoteMasterDao iCreditNoteMasterDao;
	@Autowired
	IRequestDiscountService iRequestDiscountService;
	@Autowired
	IRequestDiscountDao RequestDiscountDao;
	@Autowired
	ICreditNoteDetailsDao iCreditNoteDetailsDao;
	@Autowired
	ICreditNoteDiscountDetailsDao iCreditNoteDiscountDetailsDao;
	@Autowired
	SerialNoUtility serialNoUtility;
	@Autowired
	IOrderMasterService iOrderMasterService;
	@Autowired
	IOrderDetailsMasterService iOrderDetailsMasterService;
	@Autowired
	private IGlobalUnitMasterDao iGlobalUnitMasterDao;
	@Autowired
	private ITPackageConsumptionMasterDao iTPackageConsumptionMasterDao;
	@Autowired
	IMPackageMasterDao iMPackageMasterDao;
	@Autowired
	IOrderDetailsMasterDao iOrderDetailsMasterDao;
	@Autowired
	IContractConsumptionService iContractConsumptionService;
	@Autowired
	ICompanyReceiptsService iCompanyReceiptsService;
	@Autowired
	ICompanyReceiptsDao iCompanyReceiptsDao;
	@Autowired
	ITCompanyInvoiceCreationDao iTCompanyInvoiceCreationDao;
	@Autowired
	IBillPayeeDetailsDao iBillPayeeDetailsDao;
	@Autowired
	IRequestDiscountDao iRequestDiscountDao;
	
	@Autowired 
	IpdChargesDao ipdChargesDao;
	
	@Autowired
	IEncounterMasterService iEncounterMasterService;
	
	@Autowired
	private IBillingMasterDao billingMasterDao;
	
	@Override
	@Transactional
	public Response saveCreditNoteAgainstService(CreditNoteMasterDto creditNoteMasterDto) throws ApplicationException {
		try{
			Date date = TimeZoneComponent.getDateByzone(creditNoteMasterDto.getUnitId());
			String dateTime = GlobalCommonDateUtils.getStringDate(date, "dd-M-yyyy HH:mm:ss");
			CreditNoteMaster creditNoteMaster = null;
			
			String creditNoteNumber = iGlobalUnitMasterDao.getGeneratedNo(0, creditNoteMasterDto.getUnitId(),creditNoteScreen);
			if(creditNoteNumber==null)
			{
				return new Response(ERROR, ERROR_CODE, "Please Configure Credit Note Number.", null, null);
			}
			creditNoteMasterDto.setCreditNoteNo(creditNoteNumber);
			creditNoteMasterDto.setCreditNoteDate(dateTime);
		    creditNoteMasterDto.setCreatedDate(dateTime);
			Response creditNoteRes =  iCreditNoteMasterDao.saveCreditNote(creditNoteMasterDto);
			
			OrderMasterDto orderMasterDto = new OrderMasterDto();
			orderMasterDto.setOrderId(creditNoteMasterDto.getListCreditNoteDetails().get(0).getOrderId());
			orderMasterDto.setOrgId(creditNoteMasterDto.getOrgnisationId());
			orderMasterDto.setOrgUnitId(creditNoteMasterDto.getUnitId());
			orderMasterDto.setOrderDate(date.getTime());
			orderMasterDto.setCreatedBy(creditNoteMasterDto.getCreatedBy());
			orderMasterDto.setCreatedDate(date.getTime());
			orderMasterDto.setUpdatedBy(creditNoteMasterDto.getUpdatedBy());
			orderMasterDto.setUpdatedDate(date.getTime());
			
			Response orderMstRes =  iOrderMasterService.creditNoteCancelSerivces(orderMasterDto);
			Integer orderId = (Integer)orderMstRes.getObject();
			
			if (creditNoteRes.getCode().equalsIgnoreCase(SUCCESS_CODE)  &&
				orderMstRes.getCode().equalsIgnoreCase(SUCCESS_CODE) && 
				creditNoteRes.getObject() != null ) 
			 {
			    creditNoteMaster = (CreditNoteMaster) creditNoteRes.getObject();
				ListIterator<CreditNoteDetailsDto> creditNoteDetailsItr = creditNoteMasterDto.getListCreditNoteDetails().listIterator();
				CreditNoteDetailsDto creditNoteDetailsDto = null;
			
				
				while (creditNoteDetailsItr.hasNext()) 
				{
					creditNoteDetailsDto = creditNoteDetailsItr.next();
					
					OrderDetailsMasterDto orderDetailsMasterDto = new OrderDetailsMasterDto();
					orderDetailsMasterDto.setOrderDetailsId(creditNoteDetailsDto.getOrderDetailsId());
					orderDetailsMasterDto.setOrderId(orderId);
					orderDetailsMasterDto.setCreatedBy(creditNoteMasterDto.getCreatedBy());
					orderDetailsMasterDto.setUpdatedBy(creditNoteMasterDto.getUpdatedBy());
					orderDetailsMasterDto.setCreatedDate(date.getTime());
					orderDetailsMasterDto.setUpdatedDate(date.getTime());
					orderDetailsMasterDto.setOrderDate(date.getTime());
					orderDetailsMasterDto.setOrderSchDate(date.getTime());
					orderDetailsMasterDto.setOrdCancelDatetime(date.getTime());
					orderDetailsMasterDto.setOrdCancelBy(creditNoteMasterDto.getCreatedBy());
					orderDetailsMasterDto.setPackageId(creditNoteDetailsDto.getPackageId());
					orderDetailsMasterDto.setIsPackage(creditNoteDetailsDto.getIsPackage());
					orderMasterDto.setOrgId(creditNoteMasterDto.getOrgnisationId());
					orderMasterDto.setOrgUnitId(creditNoteMasterDto.getUnitId());
					if(creditNoteDetailsDto.getIsPackage()==true) {
						 iTPackageConsumptionMasterDao.cancelPackageConsumption(creditNoteDetailsDto.getOrderDetailsId());
						 iMPackageMasterDao.cancelEhcPackageServiceCR(orderDetailsMasterDto);
						
						 // cancel package services
						 Response res=null;
						 //iTPackageConsumptionMasterDao.getPackageServicesByOrderDetailsId(creditNoteDetailsDto.getOrderDetailsId());
						 if(res.getListObject()!=null && res.getListObject().size()>0) {
							List<OrderDetailsByEncounterIdDto> orderDetailsIdList = res.getListObject();
							
								for(OrderDetailsByEncounterIdDto orderDetailsByEncounterIdDto : orderDetailsIdList)
								{	
									Response res2 = iOrderDetailsMasterDao.getOrderPayeeDetailsByOrderDetailsId(orderDetailsByEncounterIdDto.getOrderDetailsId(),null);
									List<OrderDetailsPayeeMapperDto> list = res2.getListObject();
									orderDetailsByEncounterIdDto.setOrderDetailsPayeeMapperDtosList(res2.getListObject());								

									// for package
									Response packageMpprRes = iOrderDetailsMasterDao.getOrderDetailsPackageMapperByOrderDetailsId(orderDetailsByEncounterIdDto.getOrderDetailsId());
									List<OrderDetailsPackageMapperDto> mpprList = packageMpprRes.getListObject();
									orderDetailsByEncounterIdDto.setOrderDetailsPackageMapperDtosList(mpprList);
									
									ContractConsumptionDto contractDtoClone = new ContractConsumptionDto();
									contractDtoClone.setOrderDetailsPayeeMapperDtosList(orderDetailsByEncounterIdDto.getOrderDetailsPayeeMapperDtosList());
									contractDtoClone.setOrderDetailsPackageMapperDtosList(orderDetailsByEncounterIdDto.getOrderDetailsPackageMapperDtosList());
									contractDtoClone.setSpecialityId(orderDetailsByEncounterIdDto.getSpecialityId());
									contractDtoClone.setEncounterId(orderDetailsByEncounterIdDto.getEncounterId());
									contractDtoClone.setAdmissionId(orderDetailsByEncounterIdDto.getAdmissionId());
									contractDtoClone.setContractId(orderDetailsByEncounterIdDto.getContractId());
									contractDtoClone.setServiceId(orderDetailsByEncounterIdDto.getServiceMasterId());
									contractDtoClone.setOrderDetailsId(orderDetailsByEncounterIdDto.getOrderDetailsId());
									contractDtoClone.setCreatedBy(creditNoteMasterDto.getCreatedBy());
									iContractConsumptionService.addCapAmount(contractDtoClone);

								}
						 }
					}
					
					Response orderCancleRes = iOrderDetailsMasterService.cancelServices(orderDetailsMasterDto);
					
					
					
					
					if (orderCancleRes.getCode().equalsIgnoreCase(SUCCESS_CODE) && orderCancleRes.getObject() != null)
					{
						creditNoteDetailsDto.setOrderDetailId(creditNoteDetailsDto.getOrderDetailsId());
						creditNoteDetailsDto.setOrderDtlIdOld(creditNoteDetailsDto.getOrderDetailsId());
						creditNoteDetailsDto.setOrderDetailIdNew((Integer)orderCancleRes.getObject());
						creditNoteDetailsDto.setCreditNoteId(creditNoteMaster.getCreditNoteId());
						creditNoteDetailsDto.setCreatedDate(dateTime);
						creditNoteDetailsDto.setUpdatedBy(creditNoteMasterDto.getCreatedBy());
						creditNoteDetailsDto.setUpdatedDate(dateTime);
						creditNoteDetailsDto.setOrganisationId(creditNoteMaster.getOrgnisationId());
						creditNoteDetailsDto.setUnitId(creditNoteMaster.getUnitId());
						creditNoteDetailsDto.setCreatedDate(dateTime);
						iCreditNoteDetailsDao.saveCreditNoteDetails(creditNoteDetailsDto);
					}	
			   }
			}
			
			if(creditNoteMasterDto.getIsCreditNoteApproval().booleanValue()==false)
			{
				RequestDiscountDto dto = new RequestDiscountDto();
				dto.setReqStatus(1);
				dto.setCreatedBy(creditNoteMasterDto.getCreatedBy());
				dto.setCreatedDate(dateTime);
				dto.setUpdatedDate(dateTime);
				dto.setUpdatedBy(creditNoteMasterDto.getUpdatedBy());
				dto.setOrganizationId(creditNoteMasterDto.getOrgnisationId());
				dto.setUnitId(creditNoteMasterDto.getUnitId());
				dto.setRequestType(3);
				dto.setPatientId(creditNoteMasterDto.getPatientId());
				dto.setCreditNoteId(creditNoteMaster.getCreditNoteId());
				dto.setRequestSentBy(creditNoteMasterDto.getCreatedBy());
				dto.setRequestSentTo(creditNoteMasterDto.getRequestSentTo());
				dto.setRequestDate(dateTime);
				dto.setEncounterId(creditNoteMasterDto.getEncounterId());
				dto.setAdmissionId(creditNoteMasterDto.getAdmissionId());
				dto.setVisitTypeId(creditNoteMasterDto.getVisitTypeId());
				iRequestDiscountService.requestDiscount(dto);
			}
			
			
			
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, creditNoteMaster.getCreditNoteId());
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}
	
	

	@Override
	@Transactional
	public Response saveCreditNoteAgainstDeposit(CreditNoteMasterDto creditNoteMasterDto) throws ApplicationException {
		try{
			Date date = TimeZoneComponent.getDateByzone(creditNoteMasterDto.getUnitId());
			String dateTime = GlobalCommonDateUtils.getStringDate(date, "dd-M-yyyy HH:mm:ss");
			CreditNoteMaster creditNoteMaster = null;
			
			
			String creditNoteNumber = iGlobalUnitMasterDao.getGeneratedNo(0, creditNoteMasterDto.getUnitId(),creditNoteScreen);
			if(creditNoteNumber==null)
			{
				return new Response(ERROR, ERROR_CODE, "Please Configure Credit Note Number.", null, null);
			}
			creditNoteMasterDto.setCreditNoteNo(creditNoteNumber);
			creditNoteMasterDto.setCreditNoteDate(dateTime);
		    creditNoteMasterDto.setCreatedDate(dateTime);
			Response creditNoteRes =  iCreditNoteMasterDao.saveCreditNote(creditNoteMasterDto);
			
			if (creditNoteRes.getCode().equalsIgnoreCase(SUCCESS_CODE)  &&
				creditNoteRes.getObject() != null ) 
			 {
			    creditNoteMaster = (CreditNoteMaster) creditNoteRes.getObject();

				ListIterator<CreditNoteDetailsDto> creditNoteDetailsItr = creditNoteMasterDto.getListCreditNoteDetails().listIterator();
				CreditNoteDetailsDto creditNoteDetailsDto = null;
			
				
				while (creditNoteDetailsItr.hasNext()) 
				  {
					creditNoteDetailsDto = creditNoteDetailsItr.next();
					
						creditNoteDetailsDto.setOrderDetailId(creditNoteDetailsDto.getOrderDetailsId());
						creditNoteDetailsDto.setOrderDtlIdOld(creditNoteDetailsDto.getOrderDetailsId());
						creditNoteDetailsDto.setCreditNoteId(creditNoteMaster.getCreditNoteId());
						creditNoteDetailsDto.setCreatedDate(dateTime);
						creditNoteDetailsDto.setUpdatedBy(creditNoteMasterDto.getCreatedBy());
						creditNoteDetailsDto.setUpdatedDate(dateTime);
						creditNoteDetailsDto.setOrganisationId(creditNoteMaster.getOrgnisationId());
						creditNoteDetailsDto.setUnitId(creditNoteMaster.getUnitId());
						creditNoteDetailsDto.setCreatedDate(dateTime);
						Response creditNoteDetailsRes = iCreditNoteDetailsDao.saveCreditNoteDetails(creditNoteDetailsDto);
						if(creditNoteDetailsRes.getCode().equalsIgnoreCase(SUCCESS_CODE)  &&
								creditNoteDetailsRes.getObject() != null )
						{

							CreditNoteDetails creditNoteDetails = (CreditNoteDetails) creditNoteDetailsRes.getObject();

							ListIterator<CreditNoteDiscountDetailsDto> CreditNoteDiscountDetailsDtoItr = creditNoteDetailsDto.getListCreditNoteDiscountDetailsDto().listIterator();
							CreditNoteDiscountDetailsDto creditNoteDiscountDetailsDto = null;
							
							while (CreditNoteDiscountDetailsDtoItr.hasNext()) 
							  {
								creditNoteDiscountDetailsDto = CreditNoteDiscountDetailsDtoItr.next();
								creditNoteDiscountDetailsDto.setCreditNoteDetailId(creditNoteDetails.getCreditNoteDetailId());
								creditNoteDiscountDetailsDto.setBillingDetailsId(creditNoteDetails.getBillingDetailsId());
								creditNoteDiscountDetailsDto.setCreatedDate(dateTime);
								creditNoteDiscountDetailsDto.setUpdatedBy(creditNoteMasterDto.getCreatedBy());
								creditNoteDiscountDetailsDto.setUpdatedDate(dateTime);
								creditNoteDiscountDetailsDto.setOrgnisationId(creditNoteMaster.getOrgnisationId());
								creditNoteDiscountDetailsDto.setUnitId(creditNoteMaster.getUnitId());
								creditNoteDiscountDetailsDto.setCreatedDate(dateTime);
								iCreditNoteDiscountDetailsDao.saveCreditNoteDiscountDetails(creditNoteDiscountDetailsDto);
									
								}	
						}
					}	
			}
			if(creditNoteMasterDto.getIsCreditNoteApproval().booleanValue()==false)
			{
				RequestDiscountDto dto = new RequestDiscountDto();
				dto.setReqStatus(1);
				dto.setCreatedBy(creditNoteMasterDto.getCreatedBy());
				dto.setCreatedDate(dateTime);
				dto.setUpdatedDate(dateTime);
				dto.setUpdatedBy(creditNoteMasterDto.getUpdatedBy());
				dto.setOrganizationId(creditNoteMasterDto.getOrgnisationId());
				dto.setUnitId(creditNoteMasterDto.getUnitId());
				dto.setRequestType(3);
				dto.setPatientId(creditNoteMasterDto.getPatientId());
				dto.setCreditNoteId(creditNoteMaster.getCreditNoteId());
				dto.setRequestSentBy(creditNoteMasterDto.getCreatedBy());
				dto.setRequestSentTo(creditNoteMasterDto.getRequestSentTo());
				dto.setRequestDate(dateTime);
				iRequestDiscountService.requestDiscount(dto);
			}
			if(creditNoteMasterDto.getReqDiscId()!=null && creditNoteMasterDto.getReqDiscId()>0)
			{
				RequestDiscountDao.approveAndApply(creditNoteMasterDto.getReqDiscId());
			}
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, creditNoteMaster.getCreditNoteId());
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response saveCreditNote(CreditNoteMasterDto creditNoteMasterDto) throws ApplicationException {
		try{
			Date date = TimeZoneComponent.getDateByzone(creditNoteMasterDto.getUnitId());
			String dateTime = GlobalCommonDateUtils.getStringDate(date, "dd-M-yyyy HH:mm:ss");
			CreditNoteMaster creditNoteMaster = null;
			
			BillingMaster billingMaster = billingMasterDao.getBillById(creditNoteMasterDto.getBillId());
			
			if(creditNoteMasterDto.getCrDeuAmount() > 0 ){
				Integer payemnt = 1;
				for (BillPayeeDetails billPayeeDto : billingMaster.getBillPayeeDetailsList()) {
					if(billPayeeDto.getBillPayeeDetailsId() == creditNoteMasterDto.getBillPayeeDetailsId()){
						payemnt  = billPayeeDto.getPaymentEntitlementId();
					}
				}
				if(billingMaster.getOutstanding() >= (creditNoteMasterDto.getCrTotalAmount() +creditNoteMasterDto.getCrTaxAmount()) ){
					creditNoteMasterDto.setCrDeuAmount(0.00);
				}else if(billingMaster.getOutstanding() < ( creditNoteMasterDto.getCrTotalAmount() +creditNoteMasterDto.getCrTaxAmount() )  && payemnt != 1 ){
					creditNoteMasterDto.setCrDeuAmount( ( creditNoteMasterDto.getCrTotalAmount() +creditNoteMasterDto.getCrTaxAmount() ) - billingMaster.getOutstanding());
				}else  if(billingMaster.getOutstanding() < ( creditNoteMasterDto.getCrNetAmount()) && payemnt == 1 ){ 
					creditNoteMasterDto.setCrDeuAmount( ( creditNoteMasterDto.getCrNetAmount()) - billingMaster.getOutstanding());
				}
				
			}
			Boolean checkPackage = false;
			List<Integer> packIds  = new ArrayList<>();
			for (CreditNoteDetailsDto dto : creditNoteMasterDto.getListCreditNoteDetails()) {
			
				if(dto.getIsPackage() != null && dto.getIsPackage() == true && dto.getCrAdditionalDiscount() == 0){
					checkPackage = true;
					packIds.add(dto.getPackageId());
				}
				
			}
			
			if(billingMaster.getBillStatusId() == 1 && checkPackage == true){
				
				iOrderDetailsMasterDao.updateOrderDetailsStatus(packIds,billingMaster.getBillingMasterId());
			}
			

			String creditNoteNumber = iGlobalUnitMasterDao.getGeneratedNo(0, creditNoteMasterDto.getUnitId(),creditNoteScreen);
			if(creditNoteNumber==null)
			{
				return new Response(ERROR, ERROR_CODE, "Please Configure Credit Note Number.", null, null);
			}
			creditNoteMasterDto.setCreditNoteNo(creditNoteNumber);
			creditNoteMasterDto.setCreditNoteDate(dateTime);
			creditNoteMasterDto.setCreatedDate(dateTime);
			Response creditNoteRes =  iCreditNoteMasterDao.saveCreditNote(creditNoteMasterDto);

			if (creditNoteRes.getCode().equalsIgnoreCase(SUCCESS_CODE) && creditNoteRes.getObject() != null )
			{
				creditNoteMaster = (CreditNoteMaster) creditNoteRes.getObject();
				int size = creditNoteMasterDto.getListCreditNoteDetails().size();
				int i = 0;
				List<CreditNoteDetails> creditNoteDetailsList = new LinkedList<>();
				for (CreditNoteDetailsDto creditNoteDetailsDto : creditNoteMasterDto.getListCreditNoteDetails())
				{
					i++;
					if(size==i && creditNoteMaster.getDiscountRoundoff()!=0)
					{
						creditNoteDetailsDto.setCrAdditionalDiscount(creditNoteDetailsDto.getCrAdditionalDiscount()+creditNoteMaster.getDiscountRoundoff());
						creditNoteDetailsDto.setCrNetAmount(creditNoteDetailsDto.getCrNetAmount()+creditNoteMaster.getDiscountRoundoff());
					}
					creditNoteDetailsDto.setOrderDetailId(creditNoteDetailsDto.getOrderDetailsId());
					creditNoteDetailsDto.setOrderDtlIdOld(creditNoteDetailsDto.getOrderDetailsId());
					creditNoteDetailsDto.setCreditNoteId(creditNoteMaster.getCreditNoteId());
					creditNoteDetailsDto.setCreatedDate(dateTime);
					creditNoteDetailsDto.setUpdatedBy(creditNoteMasterDto.getCreatedBy());
					creditNoteDetailsDto.setUpdatedDate(dateTime);
					creditNoteDetailsDto.setOrganisationId(creditNoteMaster.getOrgnisationId());
					creditNoteDetailsDto.setUnitId(creditNoteMaster.getUnitId());
					creditNoteDetailsDto.setCreatedDate(dateTime);
					
					if(creditNoteDetailsDto.getPackageId()!=null 
						&& creditNoteDetailsDto.getPackageTypeId()!=null
							&& creditNoteDetailsDto.getPackageId()>0 
								&& creditNoteDetailsDto.getPackageTypeId()==1 
									&& creditNoteDetailsDto.getCancelOrDiscount()==1)
					{
						Response res=iTPackageConsumptionMasterDao.getPackageServicesByOrderDetailsId(creditNoteDetailsDto.getPackageId(),creditNoteMaster.getBillId());
						if(res.getListObject()!=null && res.getListObject().size()>0) 
						{
							List<CreditNoteDetails> ehcPackageList = res.getListObject();
							for(CreditNoteDetails crdtNtDtlsEhcService:ehcPackageList)
							{
								CreditNoteDetailsDto creditNoteDetailsEhcServices = new CreditNoteDetailsDto();
									creditNoteDetailsEhcServices.setCreditNoteId(creditNoteMaster.getCreditNoteId());
									creditNoteDetailsEhcServices.setOrderDtlIdOld(crdtNtDtlsEhcService.getOrderDetailId());
									creditNoteDetailsEhcServices.setOrderDetailId(crdtNtDtlsEhcService.getOrderDetailId());
									creditNoteDetailsEhcServices.setBillingDetailsId(crdtNtDtlsEhcService.getBillingDetailsId());
									creditNoteDetailsEhcServices.setCrServiceRateAmount(crdtNtDtlsEhcService.getCrServiceRateAmount());
									creditNoteDetailsEhcServices.setPackageConsumptionAmt(crdtNtDtlsEhcService.getPackageConsumptionAmt());
									creditNoteDetailsEhcServices.setCrAdditionalDiscount(0.00);
									creditNoteDetailsEhcServices.setCrTaxAmount(0.00);
									creditNoteDetailsEhcServices.setCrNetAmount(0.00);
									creditNoteDetailsEhcServices.setCrDiscountAmount(0.00);
									creditNoteDetailsEhcServices.setSelfPayable(BigDecimal.ZERO);
									creditNoteDetailsEhcServices.setCreditPayable(BigDecimal.ZERO);
									creditNoteDetailsEhcServices.setOrganisationId(creditNoteDetailsDto.getOrganisationId());
									creditNoteDetailsEhcServices.setUnitId(creditNoteDetailsDto.getUnitId());
									creditNoteDetailsEhcServices.setCreatedBy(creditNoteDetailsDto.getCreatedBy());
									creditNoteDetailsEhcServices.setUpdatedBy(creditNoteDetailsDto.getUpdatedBy());
									creditNoteDetailsEhcServices.setCancelOrDiscount(1);
									creditNoteDetailsEhcServices.setStatus('A');
								iCreditNoteDetailsDao.saveCreditNoteDetails(creditNoteDetailsEhcServices);
							}
						
						}
					}
					Response creditNoteDetailsRes = iCreditNoteDetailsDao.saveCreditNoteDetails(creditNoteDetailsDto);
					if(creditNoteDetailsRes.getCode().equalsIgnoreCase(SUCCESS_CODE) &&
							creditNoteDetailsRes.getObject() != null )
					{
						CreditNoteDetails creditNoteDetails = (CreditNoteDetails) creditNoteDetailsRes.getObject();
						creditNoteDetailsList.add(creditNoteDetails);
						ListIterator<CreditNoteDiscountDetailsDto> CreditNoteDiscountDetailsDtoItr = creditNoteDetailsDto.getListCreditNoteDiscountDetailsDto().listIterator();
						CreditNoteDiscountDetailsDto creditNoteDiscountDetailsDto = null;

						while (CreditNoteDiscountDetailsDtoItr.hasNext())
						{
							creditNoteDiscountDetailsDto = CreditNoteDiscountDetailsDtoItr.next();
							if(size==i && creditNoteMaster.getDiscountRoundoff()!=0)
							{
								creditNoteDiscountDetailsDto.setDiscountAmount(creditNoteDiscountDetailsDto.getDiscountAmount()+creditNoteMaster.getDiscountRoundoff());
							}
							creditNoteDiscountDetailsDto.setCreditNoteDetailId(creditNoteDetails.getCreditNoteDetailId());
							creditNoteDiscountDetailsDto.setBillingDetailsId(creditNoteDetails.getBillingDetailsId());
							creditNoteDiscountDetailsDto.setCreatedDate(dateTime);
							creditNoteDiscountDetailsDto.setUpdatedBy(creditNoteMasterDto.getCreatedBy());
							creditNoteDiscountDetailsDto.setUpdatedDate(dateTime);
							creditNoteDiscountDetailsDto.setOrgnisationId(creditNoteMaster.getOrgnisationId());
							creditNoteDiscountDetailsDto.setUnitId(creditNoteMaster.getUnitId());
							creditNoteDiscountDetailsDto.setCreatedDate(dateTime);
							iCreditNoteDiscountDetailsDao.saveCreditNoteDiscountDetails(creditNoteDiscountDetailsDto);
						}
					}
				
					//Start doctor share
					OrderDetailsMaster toBeUpdated = new OrderDetailsMaster();
					
					toBeUpdated.setHospitalShare(creditNoteDetailsDto.getHospitalShare()!= null ? new BigDecimal(creditNoteDetailsDto.getHospitalShare()) : new BigDecimal(0));
					toBeUpdated.setDocSharePer(creditNoteDetailsDto.getDocSharePer() != null ? new BigDecimal(creditNoteDetailsDto.getDocSharePer()) : new BigDecimal(0));
					toBeUpdated.setRefDocSharePer(creditNoteDetailsDto.getRefDocSharePer() != null ? new BigDecimal(creditNoteDetailsDto.getRefDocSharePer()) : new BigDecimal(0));
					toBeUpdated.setDiscountOwnership(creditNoteDetailsDto.getDiscountOwnership() != null ? creditNoteDetailsDto.getDiscountOwnership() : 0);
					toBeUpdated.setOrderDetailsId(creditNoteDetailsDto.getOrderDetailId());
					
					iOrderDetailsMasterDao.updateOrderDetailsForDoctorShare(toBeUpdated);
					
					Integer doctorShareVac = 0;
					
					Integer referShareVac = 0;
					
					doctorShareVac = iOrderDetailsMasterDao.getDoctorShareVac(creditNoteDetailsDto.getOrderDetailsId());
					
					referShareVac = iOrderDetailsMasterDao.getReferShareVac(creditNoteDetailsDto.getOrderDetailsId());
					
					if(doctorShareVac == 0){
						Integer doctorShare = iOrderDetailsMasterDao.getDoctorShare(creditNoteDetailsDto.getOrderDetailsId());
						
						if(doctorShare == 1){
							 iOrderDetailsMasterDao.updateDoctorShare(creditNoteDetailsDto.getOrderDetailsId(),toBeUpdated);
						}
						
					}
					
					if(referShareVac == 0){
						Integer referShare = iOrderDetailsMasterDao.getReferShare(creditNoteDetailsDto.getOrderDetailsId());
						
						if(referShare == 1){
							iOrderDetailsMasterDao.updateReferShare(creditNoteDetailsDto.getOrderDetailsId(),toBeUpdated);
						}
					}
					//end doctorShare
					
				}
				creditNoteMaster.setListCreditNoteDetails(creditNoteDetailsList);
			}
			if(creditNoteMasterDto.getIsCreditNoteApproval().booleanValue()==false)
			{
				RequestDiscountDto dto = new RequestDiscountDto();
					dto.setReqStatus(1);
					dto.setCreatedBy(creditNoteMasterDto.getCreatedBy());
					dto.setCreatedDate(dateTime);
					dto.setUpdatedDate(dateTime);
					dto.setUpdatedBy(creditNoteMasterDto.getUpdatedBy());
					dto.setOrganizationId(creditNoteMasterDto.getOrgnisationId());
					dto.setUnitId(creditNoteMasterDto.getUnitId());
					dto.setRequestType(3);
					dto.setPatientId(creditNoteMasterDto.getPatientId());
					dto.setCreditNoteId(creditNoteMaster.getCreditNoteId());
					dto.setRequestSentBy(creditNoteMasterDto.getCreatedBy());
				dto.setRequestSentTo(creditNoteMasterDto.getRequestSentTo());

				//dto.setRequestSentTo(creditNoteMasterDto.getCreatedBy());//userId
					dto.setRequestDate(dateTime);
					dto.setEncounterId(creditNoteMasterDto.getEncounterId());
					dto.setAdmissionId(creditNoteMasterDto.getAdmissionId());
					dto.setVisitTypeId(creditNoteMasterDto.getVisitTypeId());
				iRequestDiscountService.requestDiscount(dto);
			}
			if(creditNoteMasterDto.getReqDiscId()!=null && creditNoteMasterDto.getReqDiscId()>0)
			{
				RequestDiscountDao.approveAndApply(creditNoteMasterDto.getReqDiscId());
			}
			if(creditNoteMasterDto.getIsApproved()!=null && creditNoteMasterDto.getIsApproved()=='Y')
			{
				applyCreditNoteOnBill(creditNoteMaster,creditNoteMasterDto.getSelfPayeeId());
			}
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, creditNoteMaster.getCreditNoteId());
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public void applyCreditNoteOnBill(CreditNoteMaster creditNoteMaster,Integer selfPayeeId) throws ApplicationException {
		try{

			BillPayeeDetails billPayeeDetails = iCreditNoteMasterDao.getBillDetailsObjByBillPayeeDetailsId(creditNoteMaster.getBillPayeeDetailsId());
			Double amountToBeAddedInCompanyExpense = 0.00;
			Double amountToBeSubtractedFromBillOutstanding = 0.00;
			Double amountToBeSubtractedFromBillOutstandingNew = 0.00;
			if(billPayeeDetails.getDueAmount()==0 || billPayeeDetails.getDueAmount()<=creditNoteMaster.getCrNetAmount())
			{
				amountToBeAddedInCompanyExpense = creditNoteMaster.getCrNetAmount()-billPayeeDetails.getDueAmount();
				amountToBeSubtractedFromBillOutstanding = billPayeeDetails.getDueAmount();
				billPayeeDetails.setDueAmount(0.00);
			}else{
				
				//amountToBeSubtractedFromBillOutstanding = (creditNoteMaster.getCrNetAmount()+ (billPayeeDetails.getCrNetAmt()!= null ? billPayeeDetails.getCrNetAmt()  : 0.00));
				amountToBeSubtractedFromBillOutstanding = (creditNoteMaster.getCrNetAmount());
				
				//if((billPayeeDetails.getDueAmount() + amountToBeSubtractedFromBillOutstanding ) >billPayeeDetails.getBillAmount() ){
					billPayeeDetails.setDueAmount(billPayeeDetails.getDueAmount()-creditNoteMaster.getCrNetAmount());
				//}
				
				
			}
			billPayeeDetails.setCrNetAmt(billPayeeDetails.getCrNetAmt()!=null ? billPayeeDetails.getCrNetAmt()+creditNoteMaster.getCrNetAmount():creditNoteMaster.getCrNetAmount());
			
			boolean isSelfDueClear = (billPayeeDetails.getPayeeId().equals(selfPayeeId) && billPayeeDetails.getDueAmount()==0) ? true : false;
			
			iBillPayeeDetailsDao.updatePayeeDetailsObject(billPayeeDetails);
			iCreditNoteMasterDao.updateCrPayeeDetails(creditNoteMaster.getBillId(),creditNoteMaster.getBillPayeeDetailsId(),amountToBeSubtractedFromBillOutstanding,true,isSelfDueClear,creditNoteMaster);
			
			if(!creditNoteMaster.getBillPayeeId().equals(selfPayeeId))
			{
				Response companyInvoiceRes = iTCompanyInvoiceCreationDao.getCompanyInvoiceDetailsByBillDetails(billPayeeDetails.getBillId(), creditNoteMaster.getBillPayeeDetailsId());
				if(companyInvoiceRes!=null && companyInvoiceRes.getObject()!=null && companyInvoiceRes.getListObject()!=null)
				{
					if(amountToBeAddedInCompanyExpense>0)
					{
						amountToBeAddedInCompanyExpense = amountToBeAddedInCompanyExpense > 0 ? amountToBeAddedInCompanyExpense : creditNoteMaster.getCrNetAmount();
						
						CompanyReceiptsDto expense = new CompanyReceiptsDto();
							expense.setAmount(amountToBeAddedInCompanyExpense);
							expense.setBalanceAmount(amountToBeAddedInCompanyExpense);
							expense.setBillDateTime(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(creditNoteMaster.getUnitId()), "yyyy-MM-dd HH:mm:ss"));
							//expense.setBillNumber(companyReceiptsDto.getBillNumber());
							expense.setBillStatus(2);
							expense.setCompanyTypeId((billPayeeDetails.getAssociateCompanyId()!=null && billPayeeDetails.getAssociateCompanyId()>0) ? 2:3);
							expense.setCompanyId(billPayeeDetails.getPayeeId());
							expense.setInsuranceCompanyId(billPayeeDetails.getAssociateCompanyId());
							expense.setName("Generated agianst credit note "+creditNoteMaster.getCreditNoteNo());
							expense.setCreatedBy(creditNoteMaster.getCreatedBy());
							expense.setUpdatedBy(creditNoteMaster.getCreatedBy());
							expense.setOrgId(creditNoteMaster.getOrgnisationId());
							expense.setUnitId(creditNoteMaster.getUnitId());
							expense.setRemark("Generated agianst credit note "+creditNoteMaster.getCreditNoteNo());
							expense.setStatus('A');
							expense.setCreditNoteId(creditNoteMaster.getCreditNoteId());
						List<CompanyRecieptsReceiptDetailsDto> companyRecieptsReceiptDetailsDtosList =new LinkedList<>();	
						CompanyRecieptsReceiptDetailsDto companyRecieptsReceiptDetailsDto = new CompanyRecieptsReceiptDetailsDto();
							companyRecieptsReceiptDetailsDto.setAmount(amountToBeAddedInCompanyExpense);
							companyRecieptsReceiptDetailsDto.setUnitId(creditNoteMaster.getUnitId());
							companyRecieptsReceiptDetailsDto.setOrgId(creditNoteMaster.getOrgnisationId());
							companyRecieptsReceiptDetailsDto.setPaymentModeId(7);
							companyRecieptsReceiptDetailsDto.setCreatedBy(creditNoteMaster.getCreatedBy());
							companyRecieptsReceiptDetailsDto.setUpdatedBy(creditNoteMaster.getCreatedBy());
						companyRecieptsReceiptDetailsDtosList.add(companyRecieptsReceiptDetailsDto);
						expense.setCompanyRecieptsReceiptDetailsDto(companyRecieptsReceiptDetailsDtosList);
						
						iCompanyReceiptsService.saveCompanyReceipts(expense);
						
					}else{
						
						TCompanyInvoiceDetails companyInvoiceDetails = (TCompanyInvoiceDetails) companyInvoiceRes.getObject();
						MCompanyInvoiceMaster companyInvoiceMaster = (MCompanyInvoiceMaster) companyInvoiceRes.getResObject();
							
						companyInvoiceDetails.setCrNetAmount(companyInvoiceDetails.getCrNetAmount()+creditNoteMaster.getCrNetAmount());
						companyInvoiceDetails.setDueAmount(companyInvoiceDetails.getDueAmount()-creditNoteMaster.getCrNetAmount()<=0 ? 0 : companyInvoiceDetails.getDueAmount()-creditNoteMaster.getCrNetAmount());
						
						companyInvoiceMaster.setDueAmount(companyInvoiceMaster.getDueAmount()-creditNoteMaster.getCrNetAmount()<=0 ? 0 : companyInvoiceMaster.getDueAmount()-creditNoteMaster.getCrNetAmount());
						companyInvoiceMaster.setCrNetAmount(companyInvoiceMaster.getCrNetAmount()+creditNoteMaster.getCrNetAmount());
					
						companyInvoiceDetails.setmCompanyInvoiceMaster(companyInvoiceMaster);	
							
						iTCompanyInvoiceCreationDao.updateTCompanyInvoiceDetails(companyInvoiceDetails);	
							
						List<TCompanyInvoiceServiceDetails> companyInvoiceServiceDetailsList = companyInvoiceRes.getListObject();
						
						for(TCompanyInvoiceServiceDetails details : companyInvoiceServiceDetailsList)
						{
							CreditNoteDetails detailsDto = creditNoteMaster.getListCreditNoteDetails().stream()
																.filter(crd -> crd.getBillingDetailsId().equals(details.getBillDetailId()))
																.findAny().orElse(null);

							double crNet = detailsDto!=null ? detailsDto.getCrNetAmount()+detailsDto.getCrTaxAmount() :0;
							details.setDueAmount(details.getDueAmount()-crNet <=0 ? 0 : details.getDueAmount()-crNet);
							details.setCrNetAmount(details.getCrNetAmount()+crNet);
							
							iTCompanyInvoiceCreationDao.updateTCompanyInvoiceServiceDetails(details);
						}
					}
				}
			}
		}catch(Exception e){
			
			throw e;
		}
	}
	
	
	@Override
	@Transactional
	public Response creditNoteDiscountDetails(int billingDetailsId) throws ApplicationException {
		try{
			return iCreditNoteDiscountDetailsDao.creditNoteDiscountDetails(billingDetailsId);
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}


	@Override
	@Transactional
	public Response getCreditNoteListForRefund(RefundCnSearchDto reqDto) throws ApplicationException {
		try{
			return iCreditNoteMasterDao.getCreditNoteListForRefund(reqDto);
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}


	@Override
	@Transactional
	public Response updateDueAmountByCreditNoteId(int creditNoteId, double crDeuAmount,String receivedFrom) {
		try{
			return iCreditNoteMasterDao.updateDueAmountByCreditNoteId(creditNoteId, crDeuAmount,receivedFrom);
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}


	@Override
	@Transactional
	public Response getCreditNotesByBillForAddendum(int billId) throws ApplicationException {
		try{
			return iCreditNoteMasterDao.getCreditNotesByBillForAddendum(billId);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response cancelCreditNote(Integer creditNoteId) throws ApplicationException {
		try{
			
			Response res = iCreditNoteMasterDao.cancelCreditNote(creditNoteId,null);
			CreditNoteMaster creditNoteMaster = (CreditNoteMaster) res.getObject();
			
			iRequestDiscountDao.cancelDiscountRequestOnCreditNoteCancellation(creditNoteId);
			
			RequestDiscountDto requestDiscountDto = new RequestDiscountDto();
			requestDiscountDto.setCreditNoteId(creditNoteId);
			if(requestDiscountDto.getCreditNoteId()!=null && requestDiscountDto.getCreditNoteId()>0)
			{
				Response orderDetailsRes =  iRequestDiscountDao.getOrderDetailsIdByCreditNoteId(requestDiscountDto);
				if(orderDetailsRes.getListObject()!= null && orderDetailsRes.getListObject().size()>0){
					List<RequestDiscountDto> creditDetailsObj = orderDetailsRes.getListObject();
					
					for (RequestDiscountDto dto : creditDetailsObj) {
						
						iOrderDetailsMasterService.opdateOrderDetailsAndDoctorShare(dto);
					}
					
				}
			}
			
			
			
			List<CompanyReceiptsExpense> companyReceiptMastersList = iCompanyReceiptsDao.getCompanyReceiptsExpenseByCreditNoteId(creditNoteId);
			if(companyReceiptMastersList!=null && companyReceiptMastersList.size()>0)
			{
				for(CompanyReceiptsExpense companyReceiptsExpense : companyReceiptMastersList)
				{
					if(companyReceiptsExpense.getAmount().equals(companyReceiptsExpense.getBalanceAmount()))
					{
						iCompanyReceiptsDao.inactiveComapnyReciept(companyReceiptsExpense.getCompanyReceiptsExpenseId());
					}
				}
			}
			
			Response companyInvoiceRes = iTCompanyInvoiceCreationDao.getCompanyInvoiceDetailsByBillDetails(creditNoteMaster.getBillId(), creditNoteMaster.getBillPayeeDetailsId());
			
			if(companyInvoiceRes!=null && companyInvoiceRes.getObject()!=null && companyInvoiceRes.getListObject()!=null)
			{
				TCompanyInvoiceDetails companyInvoiceDetails = (TCompanyInvoiceDetails) companyInvoiceRes.getObject();
				MCompanyInvoiceMaster companyInvoiceMaster = (MCompanyInvoiceMaster) companyInvoiceRes.getResObject();
					
				companyInvoiceDetails.setDueAmount(companyInvoiceDetails.getDueAmount()+creditNoteMaster.getCrNetAmount());
				companyInvoiceDetails.setCrNetAmount(companyInvoiceDetails.getCrNetAmount()-creditNoteMaster.getCrNetAmount()<0 ? 0 : companyInvoiceDetails.getCrNetAmount()-creditNoteMaster.getCrNetAmount());
				
				companyInvoiceMaster.setDueAmount(companyInvoiceMaster.getDueAmount()+creditNoteMaster.getCrNetAmount());
				companyInvoiceMaster.setCrNetAmount(companyInvoiceMaster.getCrNetAmount()-creditNoteMaster.getCrNetAmount()<0 ? 0 : companyInvoiceMaster.getCrNetAmount()-creditNoteMaster.getCrNetAmount());
			
				companyInvoiceDetails.setmCompanyInvoiceMaster(companyInvoiceMaster);	
					
				iTCompanyInvoiceCreationDao.updateTCompanyInvoiceDetails(companyInvoiceDetails);	
					
				List<TCompanyInvoiceServiceDetails> companyInvoiceServiceDetailsList = companyInvoiceRes.getListObject();
				
				for(TCompanyInvoiceServiceDetails details : companyInvoiceServiceDetailsList)
				{
					CreditNoteDetails detailsDto = creditNoteMaster.getListCreditNoteDetails().stream()
														.filter(crd -> crd.getBillingDetailsId().equals(details.getBillDetailId()))
														.findAny().orElse(null);

					double crTaxAmt = detailsDto!=null ? detailsDto.getCrTaxAmount() :0 ;
					double crNet = detailsDto!=null ? detailsDto.getCrNetAmount()+crTaxAmt :0;
					details.setDueAmount(details.getDueAmount()+crNet);
					details.setCrNetAmount(details.getCrNetAmount()-crNet);
					iTCompanyInvoiceCreationDao.updateTCompanyInvoiceServiceDetails(details);
				}
			}
			
			CreditNoteMasterDto creditNoteMasterDto = new CreditNoteMasterDto();
				creditNoteMasterDto.setCreditNoteId(creditNoteMaster.getCreditNoteId());
				creditNoteMasterDto.setUnitId(creditNoteMaster.getUnitId());
			List<CreditNoteDetailsDto> creditNoteDetailsDtosList = new LinkedList<CreditNoteDetailsDto>();
			for(CreditNoteDetails creditNoteDetails : creditNoteMaster.getListCreditNoteDetails())
			{
				CreditNoteDetailsDto creditNoteDetailsDto = new CreditNoteDetailsDto();
					creditNoteDetailsDto.setOrderDetailsId(creditNoteDetails.getOrderDetailId());
					creditNoteDetailsDto.setCancelOrDiscount(creditNoteDetails.getCancelOrDiscount());
				creditNoteDetailsDtosList.add(creditNoteDetailsDto);	
			}
			creditNoteMasterDto.setListCreditNoteDetails(creditNoteDetailsDtosList);
			res.setObject(creditNoteMasterDto);
		
			return res;
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response checkPatientOutstanding(Integer patientId) throws ApplicationException {
		try{
			return iCreditNoteMasterDao.checkPatientOutstanding(patientId);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response updateCrPayeeDetails(CreditNoteMaster master,boolean addSubtract) throws ApplicationException {
		try{
			return iCreditNoteMasterDao.updateCrPayeeDetails(master.getBillId(),master.getBillPayeeDetailsId(),master.getCrNetAmount(),addSubtract,false,master);
		}catch(Exception e){
			
			throw e;
		}
	}



	@Override
	@Transactional
	public Response getEncounterAndAdmissionDetails(OrderMasterDto reqDto) throws ApplicationException {
		
		try{
			
			if(reqDto.getAdmissionId() != null ) {
				
				Response admissionDto = ipdChargesDao.getAdmissionsById(reqDto.getAdmissionId());
				AdmissionListIpdChargesResDto adminDto = (AdmissionListIpdChargesResDto) admissionDto.getObject();
				if(adminDto == null) {
					
					return new Response<>(ERROR, ERROR_CODE, "Patient may be pending for Nursing acceptance or discharged...!! ", null, null);

				}else {
					
					return new Response<>(SUCCESS, SUCCESS_CODE, null, null, adminDto);
					
				}
				 				
			}
			
			if(reqDto.getEncounterId() != null) {
				
				Response encounterRes = iEncounterMasterService.getEncounterDetailsById(reqDto.getEncounterId());
				EncounterMasterDto encounterMasterDto = (EncounterMasterDto) encounterRes.getObject();
				
				if(encounterMasterDto == null) {
					
					return new Response<>(ERROR, ERROR_CODE, "Patient is not yet checked in ...!! ", null, null);

				}else{
					return new Response<>(SUCCESS, SUCCESS_CODE, null, null, encounterMasterDto);
				}
				
			}
			
		}catch(Exception e){
			
			throw e;
		}
		return null;
		
	}



	@Override
	@Transactional
	public Response getCreditNoteDetailsByCreditNoteId(CreditNoteDetailsDto creditNoteDetailsDto)
			throws ApplicationException {
		try{
			return iCreditNoteMasterDao.getCreditNoteDetailsByCreditNoteId(creditNoteDetailsDto);
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public void inActiveOrderDetails(List<Integer> orderIds) throws ApplicationException {
		try{
			 iCreditNoteMasterDao.inActiveOrderDetails(orderIds);
		}catch(Exception e){

			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public void inActiveOrderDetailsForWeb(Integer orderIds) throws ApplicationException {
		try{
			 iCreditNoteMasterDao.inActiveOrderDetailsForWeb(orderIds);
		}catch(Exception e){

			throw new BillingException(e.getMessage());
		}
	}

}
