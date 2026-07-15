package com.param.pharmacy.op.service.billList;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.billing.global.model.BankMaster;
import com.param.billing.global.model.CardTypeMaster;
import com.param.billing.global.model.DepositMaster;
import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.PaymentMode;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.pharmacy.PharamcyBillPayeeDetails;
import com.param.entity.model.pharmacy.Sale;
import com.param.entity.model.pharmacy.SalePaymentMode;
import com.param.entity.model.pharmacy.SaleType;
import com.param.entity.model.procurement.PurchaseOrderUomMapper;
import com.param.global.billing.dao.IDepositMasterDao;
import com.param.global.billing.dao.IPOSMachineResponseMapperDao;
import com.param.global.billing.dao.IPaymentDepositDetailsDao;
import com.param.global.billing.dto.BillBadDebitListResponse;
import com.param.global.billing.dto.BillListResponse;
import com.param.global.billing.dto.DepositMasterDto;
import com.param.global.billing.dto.ItemDetailsResponse;
import com.param.global.billing.dto.POSMachineResponseMapperDto;
import com.param.global.billing.dto.PaymentDepositDetailsDto;
import com.param.global.billing.dto.PaymentDepositDetailsList;
import com.param.global.billing.dto.PaymentDepositDto;
import com.param.global.billing.dto.SalePayeeDetailsDto;
import com.param.global.billing.service.IDepositMasterService;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.items.UomResponse;
import com.param.global.mail.IMailingService;
import com.param.global.mpesa.Dao.IMpesaMasterDao;
import com.param.global.mpesa.dto.MpesaMasterDto;
import com.param.global.org.common.dao.IPatientLoyaltyCardMapperDao;
import com.param.global.org.common.dto.PatientLoyaltyCardMapperDto;
import com.param.global.pharmacy.dao.IWalkInSalesDao;
import com.param.global.utility.Util;
import com.param.pharmacy.op.dao.billList.IBillListDao;
import com.param.pharmacy.op.dto.billList.BillListDropdownResponse;
import com.param.pharmacy.op.dto.billList.BillListRequest;
import com.param.pharmacy.op.dto.billList.BillSaveRequest;
import com.param.pharmacy.op.dto.billList.PaymentReceiptDetailsDtosList;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;
@SuppressWarnings({ "rawtypes", "unchecked" })
@Service
public class BillListService implements IBillListService ,IBillingCommonConstants,ICommonConstants{

	@Autowired
	IGlobalDao GlobalDao;
	
	@Autowired
	private SessionService sessionService;

	@Autowired
	IBillListDao BillListDao;
	
	@Autowired
	private IMpesaMasterDao iMpesaMasterDao;
	
	@Autowired
	IPatientLoyaltyCardMapperDao iPatientLoyaltyCardMapperDao;
	
	@Autowired
	IDepositMasterService iDepositMasterService;
	
	@Autowired
	private IDepositMasterDao iDepositMasterDao;
	
	@Autowired
	private IGlobalUnitMasterDao iGlobalUnitMasterDao;
	
	@Autowired
	private IPaymentDepositDetailsDao iPaymentDepositDetailsDao;
	
	@Autowired
	IWalkInSalesDao WalkInSalesDao;
	
	@Autowired
	IMailingService iMailingService;

	@Autowired
	IPOSMachineResponseMapperDao iPOSMachineResponseMapperDao;
	
	private Integer screenId = Screen.BillList.getScreen();

	@Override
	@Transactional
	public BillListDropdownResponse getDropdowns() {

		BillListDropdownResponse response = new BillListDropdownResponse();
		response.setStatus(GlobalDao.getScreenStatusBetterPerformance(screenId));
		response.setSalesType(GlobalDao.getSalesTypeListBP());

		return response;
	}

	@Override
	@Transactional
	public List<BillListResponse> getBillList(BillListRequest billListRequest) {

		billListRequest.setToDate((new DateTime(billListRequest.getToDate())).plusDays(1).toDate());
		
		List<BillListResponse> response = new ArrayList<BillListResponse>();
		List<BillListResponse> sales = BillListDao.searchBillsBP(billListRequest, screenId);
		
		List<BillListResponse>  pharmacyReturnList=new ArrayList<>();
		List<BillListResponse>  issueReturnList=new ArrayList<>();
		List<BillListResponse> invoiceGeneratedList =new ArrayList<>();
		List<BillListResponse> ipdOrOpdBillGeneratedStatusList=new ArrayList<>();
		
		List<Long> saleIds = new ArrayList<>();
		if(sales != null && sales.size()>0) {
			
			saleIds = sales.stream().map(sale -> sale.getId()).collect(Collectors.toList());
			
			pharmacyReturnList=BillListDao.isPharmacyReturn(saleIds);
			issueReturnList=BillListDao.isIssueReturn(saleIds);
			invoiceGeneratedList =BillListDao.isInvoiceGenerated(saleIds,billListRequest.getUnitId());
			ipdOrOpdBillGeneratedStatusList=BillListDao.getIpdOrOpdBillGeneratedStatus(saleIds);
		}
		
		
		
		for(BillListResponse billResp : sales)
		{
			
			BillListResponse bill = new BillListResponse(billResp.getId(), billResp.getBillDate(), billResp.getBillNo(),
					billResp.getSalesType(), billResp.getTotalAmount(), billResp.getNetAmount(), billResp.getPaidAmt(),
					billResp.getBalanceAmt(), billResp.getStatus(), billResp.getOrderBy(), 
					billResp.getRefundAmt(),billResp.getDoctorName());
			bill.setTotalCreditPayable(billResp.getTotalCreditPayable());
			bill.setTotalSelfPayable(billResp.getTotalSelfPayable());
			bill.setTotalDiscountAmount(billResp.getTotalDiscountAmount());
			bill.setConcession(billResp.getConcession());
			bill.setStatusId(billResp.getStatusId());
			bill.setSearchPatient(billResp.getSearchPatient());
        
			BillListResponse ipdOrOpdBillGeneratedStatus = ipdOrOpdBillGeneratedStatusList.stream()
					  .filter(order -> bill.getId().equals(order.getId()))
					  .findAny()
					  .orElse(null);
			
			
			if(ipdOrOpdBillGeneratedStatus!=null){
				 bill.setIpdOpdBillStatus(ipdOrOpdBillGeneratedStatus.getIpdOpdBillStatus());
			}else{
				 bill.setIpdOpdBillStatus(false);
			}
           
            
            BillListResponse invoiceGenerated = invoiceGeneratedList.stream()
					  .filter(order -> bill.getId().equals(order.getId()))
					  .findAny()
					  .orElse(null);
			if(invoiceGenerated!=null){
				 bill.setIsInvoiceGenerated(invoiceGenerated.getIsInvoiceGenerated());
			}else{
				 bill.setIsInvoiceGenerated('N');
			}
            
           
			if(billResp.getPatientId() != null)
			{
				bill.setPatientId(billResp.getPatientId());
				bill.setUhid(billResp.getUhid());
				bill.setImagePath(billResp.getImagePath());
				bill.setPatientName(billResp.getPatientName()!= null ? billResp.getPatientName() : billResp.getTempPatientName());
				bill.setGenderId(billResp.getGenderId());
				bill.setCountryCallingCode(billResp.getCountryCallingCode());
				bill.setGenderCode(billResp.getGenderCode());
				bill.setMobileNumber(billResp.getMobileNumber());
				bill.setAgeString(billResp.getAgeString());
				bill.setDoctorName(billResp.getDoctorName());
				bill.setPatientDetails((billResp.getPatientDetails() != null ? billResp.getPatientDetails():billResp.getTempPatientName()) + " | " + (billResp.getPatientDob() != null ? Util.getAgeByBirthDate(billResp.getPatientDob().toLocalDateTime().toLocalDate()):"0"));
		   
					}
			else if(billResp.getTempPatId()!= null)
			{
				bill.setPatientId(billResp.getTempPatId());
				bill.setPatientName(billResp.getPatientName());
				bill.setGenderId(billResp.getGenderId());
				bill.setGenderCode(billResp.getGenderCode());
				bill.setAgeString(billResp.getAgeString());
				bill.setPatientDetails(billResp.getTempPatientDetails());
				bill.setUhid(billResp.getTempUhid()!=null ? billResp.getTempUhid():null);
				bill.setDoctorName(billResp.getDoctorName());
			}
			bill.setSaleTypeId(billResp.getSaleTypeId());
			
			BillListResponse issueReturn = issueReturnList.stream()
					  .filter(order -> bill.getId().equals(order.getId()))
					  .findAny()
					  .orElse(null);
			
			if(issueReturn !=null){
				bill.setIsIssueReturn(issueReturn.getIsIssueReturn());
			}else{
				bill.setIsIssueReturn(false);
			}
			
			
			
			BillListResponse pharmacyReturn = pharmacyReturnList.stream()
					  .filter(order -> bill.getId().equals(order.getId()))
					  .findAny()
					  .orElse(null);
			if(pharmacyReturn!=null){
				bill.setIsPharmacyReturn(true);
			}else{
				bill.setIsPharmacyReturn(false);
			}
			
			bill.setCancelledReasonId(billResp.getCancelledReasonId());
			bill.setCancelledRemark(billResp.getCancelledRemark());
			response.add(bill);
		}
		return response;

	}

	@Override
	@Transactional
	public BillListResponse getBillDetails(Long id,Integer selfPayeeId, Integer unitId) {
		
		BillListResponse billResp = BillListDao.getBillDetailsBP(id,selfPayeeId,unitId);
		//BillListResponse billResp = new BillListResponse();	
		
		if(billResp.getPatientId() != null)
		{
			BillListResponse creditDeu = BillListDao.getCreditDuaAmount(id,selfPayeeId);
			
			List<Long> saleIds = new ArrayList<>();
			saleIds.add(id);
			List<BillListResponse>  issueReturnList=BillListDao.isIssueReturn(saleIds);
			billResp.setIsIssueReturn(issueReturnList != null && issueReturnList.size()>0 && issueReturnList.get(0).getIsIssueReturn()? issueReturnList.get(0).getIsIssueReturn() : false);
			billResp.setCreditDueAmount(creditDeu.getCreditDueAmount());
			billResp.setPatientId(billResp.getPatientId());
			billResp.setUhid(billResp.getUhid());
			Timestamp now = new Timestamp((new DateTime()).getMillis());
			billResp.setPatientDetails((billResp.getPatientDetails()!= null ? billResp.getPatientDetails():"") +(billResp.getPatientDob()!=null? Period.between(billResp.getPatientDob().toLocalDateTime().toLocalDate(),
					now.toLocalDateTime().toLocalDate()).getYears():""));
			
			billResp.setGenderId(billResp.getGenderId());
			billResp.setImagePath(billResp.getImagePath());
		}
		else if(billResp.getTempPatId()!= null)
		{
			billResp.setPatientId(billResp.getTempPatId());
			billResp.setPatientDetails(billResp.getTempPatientDetails());
			billResp.setGenderId(billResp.getTempGenderId());
		}
		List<ItemDetailsResponse> itemResponse=BillListDao.getSaleDetailsBP(billResp.getId());
		for (ItemDetailsResponse itemDetailsResponse : itemResponse) {
				Integer getOrgId=BillListDao.getOrgId(itemDetailsResponse.getId());
				Integer leaseQuantity = Util.GetActualIssuedQuantity(itemDetailsResponse.getQuantityPrescribed()!=null ? itemDetailsResponse.getQuantityPrescribed():0, getUom(itemDetailsResponse.getItemId(),getOrgId),
						new UomType(itemDetailsResponse.getUomTypeId()));
				itemDetailsResponse.setQuantityPrescribed(leaseQuantity);
		}
		billResp.setItems(itemResponse);
		return billResp;
		
	}
	
	
	@Override
	@Transactional
	public Object saveBillDetails(BillSaveRequest request) throws Exception
	{
		
		if(request.getPaymentReceiptDetailsDtosList() != null) {
			
			for (PaymentReceiptDetailsDtosList dto : request.getPaymentReceiptDetailsDtosList()) {
				
				if(dto.getPaymentModeId() == 12 && dto.getPaymentGatewayProviderId() == 2 ) {
					Response resCcavenue = iDepositMasterService.ccAvenueCheckvalidation(dto.getTransactionNo());
					if(resCcavenue.getObject() == null) {
						return new Response<>(ERROR, ERROR_CODE, dto.getTransactionNo()+" This already used in other trancation", null, null);
					}
				}
				 if(dto.getPaymentModeId() == 3) 
	                {
						Date date = TimeZoneComponent.getDateByzone(request.getUnitId());
						String dateTimePOS= GlobalCommonDateUtils.getStringDate(date, "dd-M-yyyy HH:mm:ss");
	                	POSMachineResponseMapperDto pOSMachineResponseMapperDto=new POSMachineResponseMapperDto();
	                	pOSMachineResponseMapperDto.setCreatedDate(dateTimePOS);
	                	pOSMachineResponseMapperDto.setUpdatedDate(dateTimePOS);
	                	pOSMachineResponseMapperDto.setUnitId(request.getUnitId());
	                	pOSMachineResponseMapperDto.setOrganizationId(request.getOrgnizationId());
	                	pOSMachineResponseMapperDto.setUpdatedBy(request.getCreatedBy());
	                	pOSMachineResponseMapperDto.setIsConsumed(true);
	                	pOSMachineResponseMapperDto.setRequestedUniqueId(dto.getTransactionNo());
	                	
	                	iPOSMachineResponseMapperDao.updatePOSMachineResponseMapperFlag(pOSMachineResponseMapperDto);
	                }
			}
		}
		Integer depositMasterIdToUpdateUsingSalePaymentMode =null;
		Sale sale = BillListDao.getBillDetails(request.getBillingMasterId());
		sale.setReceivedFrom(request.getPaymentReceiptDetailsDtosList()!=null && request.getPaymentReceiptDetailsDtosList().size()!=0 ? request.getPaymentReceiptDetailsDtosList().get(0).getReceivedFrom():(request.getReceivedFrom()!=null ? request.getReceivedFrom():""));
		if(sale.getSaleType().getId() ==1 ) {
			
			sale.setSaleType(new SaleType(3));
		}
		Double paidAmount = request.getAmountData();
		//paidAmount = paidAmount + request.getAmountData();
		Double dueAmount = 0d;
		if(sale.getOutstandingAmount()==null)
		{
			 dueAmount = 0d;
		}
		else
		{
			 dueAmount = sale.getOutstandingAmount();
		}
		
		Integer depositConsumptionId = null;
		double remainingAmt = 0.0;
		Double finalConsumption = 0.0;
		sale.setPaidAmount(sale.getPaidAmount() + request.getAmountData());
		Integer checkBalance = 0;
		if(request.getBalanceAmount()!=null && request.getBalanceAmount().equals(sale.getTotalSelfPayable()) && request.getPaymentDepositDetailsList().size()==0)
		{
			request.setBalanceAmount(null);
		}
		if(request.getBalanceAmount()==null)
		{
			checkBalance = 1;
			request.setBalanceAmount(sale.getTotalSelfPayable());
		}
        String getIsInvoiceGeneratied=BillListDao.getIsInvoiceGeneratied(sale.getId(),request.getSelfPayeeId());
        if(getIsInvoiceGeneratied==null)
        {
        	getIsInvoiceGeneratied="N";
        }
        if(getIsInvoiceGeneratied.equalsIgnoreCase("Y") && paidAmount >request.getBalanceAmount())
        {
        	sale.setOutstandingAmount(0.00);
        }
        else if(getIsInvoiceGeneratied.equalsIgnoreCase("Y") && paidAmount <request.getBalanceAmount())
        {
        	sale.setOutstandingAmount(sale.getOutstandingAmount()-paidAmount);
        }
        else if(getIsInvoiceGeneratied.equalsIgnoreCase("N") && paidAmount >request.getBalanceAmount())
        {
        	sale.setOutstandingAmount(sale.getOutstandingAmount()-(request.getBalanceAmount()));
        }
        else if(getIsInvoiceGeneratied.equalsIgnoreCase("N") && paidAmount <request.getBalanceAmount())
        {
        	sale.setOutstandingAmount(sale.getOutstandingAmount()-(paidAmount));
        }
        else
        {
        	if(checkBalance==1) {
        		sale.setOutstandingAmount(sale.getOutstandingAmount()-(paidAmount));
        	}
        	else
        	{
        		sale.setOutstandingAmount(sale.getOutstandingAmount()-(paidAmount));
        	}
        }
		if(paidAmount >request.getBalanceAmount() && (paidAmount - request.getBalanceAmount())>0 && request.getPaymentReceiptDetailsDtosList() != null && request.getPaymentReceiptDetailsDtosList().size()>0) 
		{
			if(request.getPaymentDepositDetailsList()!= null && request.getPaymentDepositDetailsList().size()>0)
			{
				ListIterator<PaymentDepositDetailsList> iterator1 = request.getPaymentDepositDetailsList().listIterator();
				while(iterator1.hasNext())
				{
					PaymentDepositDetailsList depositDtoDetails = iterator1.next();
					remainingAmt =  paidAmount - (request.getBalanceAmount()+depositDtoDetails.getConsumedAmount()); 
				}
			}
			else
			{
				remainingAmt =  paidAmount - request.getBalanceAmount(); 
			}
			Date date = TimeZoneComponent.getDateByzone(sale.getUnitId());
			String dateTime = GlobalCommonDateUtils.getStringDate(date, "dd-M-yyyy HH:mm:ss");
			DepositMaster depositMaster =null;
			//save deposit master 
			DepositMasterDto deposit = new DepositMasterDto();
				deposit.setAvailableDeposit(remainingAmt);
				deposit.setDepositAmount(remainingAmt);
				deposit.setPatientId(request.getPatientId());
				deposit.setOrgId(request.getOrgnizationId());
				deposit.setUnitId(request.getUnitId());
				deposit.setConsumedDeposit(0.00);
				deposit.setCreatedDate(dateTime);
				deposit.setCreatedBy(request.getCreatedBy());
				deposit.setDepositDate(dateTime);
				deposit.setDepositTypeId(1);
				deposit.setStatus(ACTIVE);
				deposit.setUpdatedBy(deposit.getCreatedBy());
				deposit.setUpdatedDate(dateTime);
				String depositNumber = iGlobalUnitMasterDao.getGeneratedNo(0, request.getUnitId(), depositScreenId);
				if(depositNumber==null)
					return new Response<>(ERROR, ERROR_CODE, "Please Configure Deposit Number.", null, null);
				deposit.setDepositNo(depositNumber);
			Response depositRes = iDepositMasterDao.saveDeposit(deposit);
			if(depositRes.getCode().equalsIgnoreCase(SUCCESS_CODE) && depositRes.getObject() != null)
			{
			  depositMaster = (DepositMaster)depositRes.getObject();
			  depositMasterIdToUpdateUsingSalePaymentMode=depositMaster.getDepositMasterId();
			  PaymentDepositDetailsDto detailsDto = new PaymentDepositDetailsDto();
				detailsDto.setCreatedDate(dateTime);
				detailsDto.setDepositId(depositMaster.getDepositMasterId());
				detailsDto.setPaymentModeId(7);
				detailsDto.setAmount(remainingAmt);
				detailsDto.setUpdatedBy(deposit.getCreatedBy());
				detailsDto.setUpdatedDate(dateTime);
				detailsDto.setBalanceAmount(remainingAmt);
				detailsDto.setRefundAmount(0.00);
				detailsDto.setUnitId(request.getUnitId());
				detailsDto.setOrganizationId(request.getOrgnizationId());
				iPaymentDepositDetailsDao.savePaymentDepositDetails(detailsDto);
			}
			if(getIsInvoiceGeneratied.equalsIgnoreCase("N") && sale.getTotalCreditPayable()>0)
			{
				sale.setStatus(new Status(com.param.common.constants.Status.PartiallyCompleted.getStatus()));
			}
			else
			{
				sale.setStatus(new Status(com.param.common.constants.Status.Completed.getStatus()));
			}
		}else if(((new BigDecimal(paidAmount).setScale(2, RoundingMode.HALF_EVEN)).equals( new BigDecimal(dueAmount).setScale(2, RoundingMode.HALF_EVEN)))) {
			sale.setStatus(new Status(com.param.common.constants.Status.Completed.getStatus()));
		}
		else if(dueAmount>0) {
			sale.setStatus(new Status(com.param.common.constants.Status.PartiallyCompleted.getStatus()));
		}
		//consume deposit master
		if(request.getPaymentDepositDetailsList()!= null && request.getPaymentDepositDetailsList().size()>0)
		{
			if(paidAmount >request.getBalanceAmount() && (paidAmount - request.getBalanceAmount())>0 && request.getPaymentReceiptDetailsDtosList() != null && request.getPaymentReceiptDetailsDtosList().size()>0) 
			{
					Double totalAmount=0.0;
					Double totalConsumed=0.0;
					ListIterator<PaymentDepositDetailsList> iterator1 = request.getPaymentDepositDetailsList().listIterator();
					while(iterator1.hasNext())
					{
						PaymentDepositDetailsList depositDtoDetails = iterator1.next();
						totalConsumed=totalConsumed+depositDtoDetails.getConsumedAmount();
					}
					for (PaymentReceiptDetailsDtosList dto : request.getPaymentReceiptDetailsDtosList()) {
						totalAmount=totalAmount+dto.getAmount();
					}
					
						sale.setOutstandingAmount((request.getBalanceAmount())-(totalConsumed+totalAmount));
			}
			else
			{
				Double totalConsumed=0.0;
				Double totalAmount=0.0;
				ListIterator<PaymentDepositDetailsList> iterator1 = request.getPaymentDepositDetailsList().listIterator();
				while(iterator1.hasNext())
				{
					PaymentDepositDetailsList depositDtoDetails = iterator1.next();
					totalConsumed=totalConsumed+depositDtoDetails.getConsumedAmount();
				}
				for (PaymentReceiptDetailsDtosList dto : request.getPaymentReceiptDetailsDtosList()) {
					totalAmount=totalAmount+dto.getAmount();
				}
//				if(request.getBalanceAmount().equals(totalConsumed))
//				{
//					sale.setOutstandingAmount(0.00);
//				}
			}
			Double totaldepositAmount = 0.0;
			ListIterator<PaymentDepositDetailsList> iterator1 = request.getPaymentDepositDetailsList().listIterator();
			while(iterator1.hasNext())
			{
				PaymentDepositDetailsList depositDtoDetails = iterator1.next();
				totaldepositAmount = totaldepositAmount + depositDtoDetails.getConsumedAmount();
			}
			//request.setTotalDepositConsumption(totaldepositAmount.longValue());
			request.setDepositConsumption(totaldepositAmount);
			request.setDepositeConsumptionNumber(iGlobalUnitMasterDao.getGeneratedNo(0, request.getUnitId(), IBillingCommonConstants.DEPOSIT_CONSUMPTION_NUMBER));
			request.setSaleId(sale.getId().intValue());
			Response res = BillListDao.saveDepositeConsumption(request);
			
			depositConsumptionId = (Integer) res.getObject();
			
			ListIterator<PaymentDepositDetailsList> iterator = request.getPaymentDepositDetailsList().listIterator();
			while(iterator.hasNext())
			{
				PaymentDepositDetailsList depositDto = iterator.next();
				
				if(depositDto.getBillId() != null)
					depositDto.setBillId(request.getBillingMasterId().intValue()); 
				if(depositDto.getBillPayeeDetailsId() != null)
					depositDto.setBillPayeeDetailsId(request.getBillPayeeDetailsId().intValue()); 
				
				PaymentDepositDto depositDto1 = new PaymentDepositDto();
				
				if(depositDto.getBillId() != null)
					depositDto1.setBillId(depositDto.getBillId()); 
				if(depositDto.getBillPayeeDetailsId() != null)
					depositDto1.setBillPayeeDetailsId(depositDto.getBillPayeeDetailsId()); 
				Double consumedAmount = depositDto.getConsumedAmount();
				depositDto1.setConsumedAmount(consumedAmount);
				depositDto1.setAvailableDeposit(depositDto.getAvailableDeposit());
				depositDto1.setCreatedBy(request.getCreatedBy());
				depositDto1.setDepConsumptionDate(TimeZoneComponent.getDateByzone(sale.getUnitId()).toString());
				depositDto1.setDepositMasterId(depositDto.getDepositMasterId());
				//update call to deposit master
				iDepositMasterService.updateDepositMaster(depositDto1);
				
				//save call to deposit consumption details
				PaymentDepositDetailsDto paymentDepositDetailsDtoConsumption = new PaymentDepositDetailsDto();	
					paymentDepositDetailsDtoConsumption.setOrganizationId(depositDto.getOrganizationId());
					paymentDepositDetailsDtoConsumption.setUnitId(depositDto.getUnitId());
					paymentDepositDetailsDtoConsumption.setDepositId(depositDto.getDepositMasterId());
					paymentDepositDetailsDtoConsumption.setDepConsumptionId(depositConsumptionId);
					paymentDepositDetailsDtoConsumption.setDepositConsumed(consumedAmount);
					paymentDepositDetailsDtoConsumption.setUpdatedBy(request.getUpdatedBy());
					paymentDepositDetailsDtoConsumption.setRemark(depositDto.getRemark());
					paymentDepositDetailsDtoConsumption.setUpdatedDate(TimeZoneComponent.getDateByzone(sale.getUnitId()).toString());
					BillListDao.addDepositConsumptionDetails(paymentDepositDetailsDtoConsumption);
				Response res3 = iDepositMasterService.getDepositDetailsByDepositId(depositDto.getDepositMasterId());
				
				List<PaymentDepositDetailsDto> detailsByDepositIdDtosList = res3.getListObject();
				ListIterator<PaymentDepositDetailsDto> listIterator = detailsByDepositIdDtosList.listIterator();
				if(consumedAmount>0)
				{
					while(listIterator.hasNext())
					{
						PaymentDepositDetailsDto paymentDepositDetailsDto = listIterator.next();
						
						paymentDepositDetailsDto.setDepConsumptionId(depositConsumptionId);
						
						Double detailsAmount = Math.round(paymentDepositDetailsDto.getBalanceAmount() * 100.0)/100.0;
						
							if(consumedAmount>=detailsAmount){
								paymentDepositDetailsDto.setConsumedAmount(detailsAmount);
							}else{
								paymentDepositDetailsDto.setConsumedAmount(consumedAmount);
							}
							paymentDepositDetailsDto.setBalanceAmount(detailsAmount-paymentDepositDetailsDto.getConsumedAmount());
							
							//update call to deposit details
								paymentDepositDetailsDto.setCreatedBy(depositDto.getCreatedBy());
								paymentDepositDetailsDto.setUpdatedDate(depositDto.getDepConsumptionDate());
							iDepositMasterService.updateDepositDetails(paymentDepositDetailsDto);
							
							consumedAmount=consumedAmount-paymentDepositDetailsDto.getConsumedAmount();
							finalConsumption=finalConsumption+paymentDepositDetailsDto.getConsumedAmount();
						
						}
					}
			}
			
		}
		
		List<SalePaymentMode> salePaymentModes = buildSalePaymentModes(request, sale);
		
		if(salePaymentModes !=null ) {
		sale.setSalePaymentModes(salePaymentModes);
		
		BillListDao.saveBill(sale);
		
		//Ganesh
		GlobalDao.updateDueAmountByBillBilPayeeId(sale.getId().intValue(), sessionService.getSelfPayeeId(), paidAmount);
		
		CommonResponse response = new CommonResponse();
		response.setId(sale.getId().longValue());


		if(depositMasterIdToUpdateUsingSalePaymentMode!=null){
			for (SalePaymentMode salePaymentMode : salePaymentModes) {
				WalkInSalesDao.updateSalePaymentModeIdInDepositMaster(depositMasterIdToUpdateUsingSalePaymentMode,salePaymentMode.getId());
			}
		}

		WalkInSalesDao.inactiveOrderDetailsBySaleId(sale.getId().intValue(), null);
		response.setMessage("Sale Details saved successfully");
		return response;
		
		}else {
			
			CommonResponse response = new CommonResponse();
			response.setMessage("Invalid Transaction ID ");
			
			return response;
		}
	}

	private List<SalePaymentMode> buildSalePaymentModes(BillSaveRequest request, Sale sale) 
	{
		//System.out.println("In buildSalePaymentModes");
		List<SalePaymentMode> salePaymentModes = new ArrayList<SalePaymentMode>();
		List<PaymentReceiptDetailsDtosList> paymentReceiptDetailsDtosList = request.getPaymentReceiptDetailsDtosList();
		List<PatientLoyaltyCardMapperDto>  mailList =  new 	ArrayList<PatientLoyaltyCardMapperDto>();  
		//System.out.println("Check payment detail lists are provided or not");
		if(paymentReceiptDetailsDtosList != null && !paymentReceiptDetailsDtosList.isEmpty()) 
		{
			//System.out.println("payment detail lists are Found. Processing");
			for(PaymentReceiptDetailsDtosList paymentDetail : paymentReceiptDetailsDtosList) {
				
				SalePaymentMode s = new SalePaymentMode();
				
				s.setSale(sale);
				
				//System.out.println("Setting amount : "+paymentDetail.getAmount());
				s.setAmount(paymentDetail.getAmount());
				
				if(paymentDetail.getAccountNumber() != null  && !paymentDetail.getAccountNumber().isEmpty()) {
					//System.out.println("Setting account number : "+paymentDetail.getAccountNumber());
					s.setAccountNumber(paymentDetail.getAccountNumber());
				}
				
				if(paymentDetail.getPaymentModeId() != null) {
					//System.out.println("Setting payment mode. ID in request : "+paymentDetail.getPaymentModeId());
					//PaymentMode paymentMode = BillListDao.getPaymentModeById(paymentDetail.getPaymentModeId());
					PaymentMode paymentMode = new PaymentMode(paymentDetail.getPaymentModeId());
					s.setPaymentMode(paymentMode);
				}
				MpesaMasterDto masterDto = new MpesaMasterDto();
				if(paymentDetail.getPaymentModeId() != null && paymentDetail.getPaymentModeId() == 5) {
					//System.out.println("Payment mode is Mpesa");
					
					if((paymentDetail.getSkipMpesaValidation()!=null && paymentDetail.getSkipMpesaValidation())) {
						masterDto.setMpesaTxcode(paymentDetail.getTransactionNo());	
						masterDto.setUnitId(request.getUnitId() != null ?request.getUnitId() : sessionService.getUnitId());
						masterDto.setOrganizationId(request.getOrgnizationId() != null ?request.getOrgnizationId() : sessionService.getOrganizationId());
						masterDto.setUpdatedBy(request.getCreatedBy());
						masterDto.setMpesaAmount(paymentDetail.getAmount());
						iMpesaMasterDao.validateMpesaTrancastion(masterDto);
						
					}
					if(paymentDetail.getPaymentModeId() == 3 && paymentDetail.getSkipMpesaValidation() != null && paymentDetail.getSkipMpesaValidation() == true) 
	                {
	                	POSMachineResponseMapperDto pOSMachineResponseMapperDto=new POSMachineResponseMapperDto();
//	                	pOSMachineResponseMapperDto.setCreatedDate(sessionService.getCreatedDate());
//	                	pOSMachineResponseMapperDto.setUpdatedDate(paymentDetail.getCreatedDate());
	                	pOSMachineResponseMapperDto.setUnitId(request.getUnitId() != null ?request.getUnitId() : sessionService.getUnitId());
	                	pOSMachineResponseMapperDto.setOrganizationId(request.getOrgnizationId() != null ?request.getOrgnizationId() : sessionService.getOrganizationId());
	                	pOSMachineResponseMapperDto.setUpdatedBy(paymentDetail.getCreatedBy());
	                	pOSMachineResponseMapperDto.setIsConsumed(true);
	                	pOSMachineResponseMapperDto.setRequestedUniqueId(paymentDetail.getTransactionNo());
	                	
	                	iPOSMachineResponseMapperDao.updatePOSMachineResponseMapperFlag(pOSMachineResponseMapperDto);
	                }
					s.setSkipMpesaValidation(paymentDetail.getSkipMpesaValidation() != null ? paymentDetail.getSkipMpesaValidation() : false);
					//System.out.println("Setting transactionNo : "+paymentDetail.getTransactionNo());
					s.setTransactionNo(paymentDetail.getTransactionNo());
					
					if(paymentDetail.getBankId() != null) {
						//System.out.println("Setting bank master. ID : "+paymentDetail.getBankId());
						//BankMaster bankMaster = BillListDao.getBankMasterById(paymentDetail.getBankId());
						BankMaster bankMaster = new BankMaster();
						bankMaster.setBankId(paymentDetail.getBankId());
						s.setBankMaster(bankMaster);
					}
				}
				
				if(paymentDetail.getPaymentModeId() != null && paymentDetail.getPaymentModeId() == 12) {
					
					if(paymentDetail.getPaymentGatewayProviderId() == 1) {
						
						iMpesaMasterDao.updateRazorPay(paymentDetail.getTransactionNo());
						
					}else if(paymentDetail.getPaymentGatewayProviderId() == 2){
						
						iMpesaMasterDao.updateCCAvenue(paymentDetail.getTransactionNo(),request.getBillingMasterId().intValue());
					}
					
				}
				
				
				if(paymentDetail.getPaymentModeId() != null && paymentDetail.getPaymentModeId() == 6) {
					
					Date date = TimeZoneComponent.getDateByzone(sale.getUnitId());
					String dateTime = GlobalCommonDateUtils.getStringDate(date, "dd-M-yyyy HH:mm:ss");
					PatientLoyaltyCardMapperDto dto =new PatientLoyaltyCardMapperDto();
					dto.setCardAmount(paymentDetail.getAmount());
					dto.setCardNo(paymentDetail.getCardNumber());
					dto.setCreatedBy(paymentDetail.getCreatedBy());
					dto.setCreatedDate(dateTime);
					dto.setOrganisationId(request.getOrgnizationId() != null ?request.getOrgnizationId() : sessionService.getOrganizationId());
					dto.setUnitId(request.getUnitId() != null ?request.getUnitId() : sessionService.getUnitId());
					Response loyalityCardResponse=iPatientLoyaltyCardMapperDao.getPatientDetailsByLoyaltiCardNo(dto);
					if(loyalityCardResponse.getListObject().size() >0 && loyalityCardResponse.getListObject()!=null){
						PatientLoyaltyCardMapperDto patientLoyaltyCardMapperDto=(PatientLoyaltyCardMapperDto)loyalityCardResponse.getListObject().get(0);
						//checking that front end amount should be less than or equal to card amount
						if(patientLoyaltyCardMapperDto.getCardAmount()>=paymentDetail.getAmount())
							{
							patientLoyaltyCardMapperDto.setConsumeAmount(paymentDetail.getAmount());
							iPatientLoyaltyCardMapperDao.updateLoyaltyCardAmountByCard(dto);
							mailList.add(patientLoyaltyCardMapperDto);
						}else{
							return null;
						}
					}
					
					
					
				}
				
				if(paymentDetail.getPaymentModeId() != null && paymentDetail.getPaymentModeId() == 2){
					//System.out.println("Payment mode is CHEQUE.");
					
					if(paymentDetail.getBankId() != null) {
						//System.out.println("Setting bank master. ID : "+paymentDetail.getBankId());
						//BankMaster bankMaster = BillListDao.getBankMasterById(paymentDetail.getBankId());
						BankMaster bankMaster = new BankMaster();
						bankMaster.setBankId(paymentDetail.getBankId());
						s.setBankMaster(bankMaster);
					}
					
					if(paymentDetail.getChequeNumber()!=null&&!paymentDetail.getChequeNumber().isEmpty()) {
						//System.out.println("Setting check number : "+paymentDetail.getChequeNumber());
						s.setCheckNumber(paymentDetail.getChequeNumber());
					}
					
					if(paymentDetail.getChequeDate() != null) {
						//System.out.println("Setting check date : "+paymentDetail.getChequeDate());
						Date d = GlobalCommonDateUtils.getDate(paymentDetail.getChequeDate(), "dd-M-yyyy HH:mm:ss");
						s.setChequeDate(new Timestamp(d.getTime()));
					}
				}
					
				
				if(paymentDetail.getPaymentModeId() != null && (paymentDetail.getPaymentModeId() == 3
						|| paymentDetail.getPaymentModeId() == 4 || paymentDetail.getPaymentModeId() == 6))
				{
					//System.out.println("Payment mode is card");
					
					if(paymentDetail.getCardHolderName()!=null&&!paymentDetail.getCardHolderName().isEmpty()) {
						//System.out.println("Setting card holder name : "+paymentDetail.getCardHolderName());
						s.setCardHolderName(paymentDetail.getCardHolderName());
					}
					
					if(paymentDetail.getCardNumber()!=null&&!paymentDetail.getCardNumber().isEmpty()) {
						//System.out.println("Setting card number : "+paymentDetail.getCardNumber());
						s.setCardNumber(paymentDetail.getCardNumber());
					}
					
					if(paymentDetail.getCardTypeId() != null) {
						//System.out.println("Setting card type id : "+paymentDetail.getCardTypeId());
						//CardTypeMaster cardTypeMaster = BillListDao.getCardTypeyId(paymentDetail.getCardTypeId());
						CardTypeMaster cardTypeMaster = new CardTypeMaster();
						cardTypeMaster.setCardTypeId(paymentDetail.getCardTypeId());
						s.setCardTypeMaster(cardTypeMaster);
					}
					
					if(paymentDetail.getExpiryDate() != null) {
						//System.out.println("Setting expiry date : "+paymentDetail.getExpiryDate());
						Date d = GlobalCommonDateUtils.getDate(paymentDetail.getExpiryDate(), "dd-M-yyyy HH:mm:ss");
						s.setExpiryDate(new Timestamp(d.getTime()));
					}
					if( paymentDetail.getPaymentModeId() == 3) {
						s.setSkipMpesaValidation(paymentDetail.getSkipMpesaValidation()!=null ?paymentDetail.getSkipMpesaValidation() : false);
					}
					if(paymentDetail.getTransactionNo() != "" && paymentDetail.getPaymentModeId() == 3) {
						s.setTransactionNo(paymentDetail.getTransactionNo());
					}
					
					if(paymentDetail.getDeviceName() != null) {
						s.setDeviceName(paymentDetail.getDeviceName());
					}
					if(paymentDetail.getTransactionDetails() != null) {
						s.setTransactionDetails(paymentDetail.getTransactionDetails());
					}
				}
				
				if(paymentDetail.getPaymentModeId() != null && paymentDetail.getPaymentModeId() == 8) {
					//System.out.println("Payment mode is E-Transfer");
				
					if(paymentDetail.getTransactionNo()!= null) {
					//System.out.println("Setting transactionNo : "+paymentDetail.getTransactionNo());
					s.setTransactionNo(paymentDetail.getTransactionNo());
					}
					if(paymentDetail.getTransTransactionDate() != null) {
						//System.out.println("Setting TransTransactionDate : "+paymentDetail.getTransTransactionDate());
						Date d = GlobalCommonDateUtils.getDate(paymentDetail.getTransTransactionDate(), "dd-M-yyyy HH:mm:ss");
						s.setTransTransactionDate(new Timestamp(d.getTime()));
					}
					
					if(paymentDetail.getTransactionTypeId() != null) {
						//System.out.println("Setting TransactionTypeId : "+paymentDetail.getTransactionTypeId());
						s.setTransactionTypeId(paymentDetail.getTransactionTypeId());
					}
					if(paymentDetail.getBankId() != null) {
						//System.out.println("Setting bank master. ID : "+paymentDetail.getBankId());
						BankMaster bankMaster = new BankMaster();
						bankMaster.setBankId(paymentDetail.getBankId());
						s.setBankMaster(bankMaster);
					}
				}
				
				
				
				

				//System.out.println("Adding to list");
				s.setCreateDefaults(request.getOrgnizationId()!=null ? request.getOrgnizationId() : sessionService.getOrganizationId() 
						, request.getUnitId() !=null ?request.getUnitId() : sessionService.getUnitId()
						, request.getCreatedBy()!=null ? request.getCreatedBy() : sessionService.getUserId());
				salePaymentModes.add(s);
			}
		}
		else
			//System.out.println("payment detail lists are NULL");
		
		//System.out.println("Returning list. size : "+salePaymentModes.size());
		for (PatientLoyaltyCardMapperDto patientLoyaltyCardMapperDto : mailList) {
			iMailingService.consumeLoyaltyAmount(patientLoyaltyCardMapperDto);
		}
		return salePaymentModes;
	}
	
	
	@Override
	@Transactional
	public List<BillBadDebitListResponse> badDebtBillsearch(BillListRequest billListRequest) {

		billListRequest.setToDate((new DateTime(billListRequest.getToDate())).plusDays(1).toDate());
		
		List<BillBadDebitListResponse> response = new ArrayList<BillBadDebitListResponse>();
		List<BillBadDebitListResponse> sales = BillListDao.searchBillsBadDebitlist(billListRequest, screenId);
		for(BillBadDebitListResponse billResp : sales)
		{
			BillBadDebitListResponse bill = new BillBadDebitListResponse(billResp.getId(), billResp.getBillDate(), billResp.getBillNo(),
					billResp.getSalesType(), billResp.getTotalAmount(), billResp.getNetAmount(), billResp.getPaidAmt(),
					billResp.getBalanceAmt(), billResp.getStatus(), billResp.getOrderBy(), 
					billResp.getRefundAmt());
			if(billResp.getPatientId() != null)
			{
				bill.setPatientId(billResp.getPatientId());
				bill.setUhid(billResp.getUhid());
				bill.setPatientDetails(billResp.getPatientDetails());
			}
			else if(billResp.getTempPatId()!= null)
			{
				bill.setPatientId(billResp.getTempPatId());
				bill.setPatientDetails(billResp.getTempPatientDetails());
			}
			response.add(bill);
		}
		return response;

	}

	@Override
	@Transactional
	public BillListResponse badDebtBillDetials(Long id) {
		BillListResponse billResp = BillListDao.getbadDebtBillDetialsBP(id);
		//BillListResponse billResp = new BillListResponse();	
		
		if(billResp.getPatientId() != null)
		{
			billResp.setPatientId(billResp.getPatientId());
			billResp.setUhid(billResp.getUhid());
			Timestamp now = new Timestamp((new DateTime()).getMillis());
			billResp.setPatientDetails(billResp.getPatientDetails() + Period.between(billResp.getPatientDob().toLocalDateTime().toLocalDate(), 
					now.toLocalDateTime().toLocalDate()).getYears());
		}
		else if(billResp.getTempPatId()!= null)
		{
			billResp.setPatientId(billResp.getTempPatId());
			billResp.setPatientDetails(billResp.getTempPatientDetails());
		}
		
		billResp.setItems(BillListDao.getSaleDetailsBP(billResp.getId()));
		return billResp;
		
	}

	@Override
	@Transactional
	public List<SalePayeeDetailsDto> badDebtBillPayeeDetials(Long id) {
		return BillListDao.badDebtBillPayeeDetials(id);
	}

	@Override
	@Transactional
	public List<BillListResponse> pharmacyReceiptCancellationFlag(BillListRequest billListRequest) {
		
		return BillListDao.pharmacyReceiptCancellationFlag(billListRequest);
	}
	
	public List<PurchaseOrderUomMapper> getUom(Integer itemId,Integer orgId) {

		List<UomResponse> uomResponse = GlobalDao.getUnitOfMeasurementListByItem(itemId,orgId);
		List<PurchaseOrderUomMapper> prUomResponse = new ArrayList<PurchaseOrderUomMapper>();
		for (UomResponse uom : uomResponse) {
			PurchaseOrderUomMapper prUom = new PurchaseOrderUomMapper(new UomType(uom.getUomTypeId()),
					new UomUnit(uom.getUomUnitId()), uom.getConversion(), uom.getIpUom(), uom.getOpUom(),
					uom.getStoreUom());
			prUomResponse.add(prUom);
		}
		return prUomResponse;
	}

	@Override
	@Transactional
	public PharamcyBillPayeeDetails checkSelfDueAmount(BillSaveRequest request) {
		return BillListDao.checkSelfDueAmount(request);
	}



}
