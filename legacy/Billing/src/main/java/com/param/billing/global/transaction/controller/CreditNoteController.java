package com.param.billing.global.transaction.controller;

import com.param.billing.global.transaction.dto.BillListDetailsCNDto;
import com.param.billing.global.transaction.service.IBillPayeeDetailsService;
import com.param.global.billing.dto.BillPayeeDetailsResDto;
import com.param.global.dto.UnitApplicationConfigurationDto;
import com.param.global.service.IUnitApplicationBillingConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.billing.common.ICommonConstants;
import com.param.billing.global.dto.RefundCnSearchDto;
import com.param.billing.global.transaction.dto.CreditNoteDetailsDto;
import com.param.billing.global.transaction.dto.CreditNoteMasterDto;
import com.param.billing.global.transaction.service.ICreditNoteMasterService;
import com.param.billing.global.transaction.service.ITDoctorShareService;
import com.param.global.common.IBillingStatusMasterConstant;
import com.param.global.common.Response;
import com.param.global.dto.OrderMasterDto;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/billing")
@SuppressWarnings({"rawtypes"})
public class CreditNoteController  implements ICommonConstants,IBillingStatusMasterConstant {
	
	@Autowired
	private ICreditNoteMasterService iCreditNoteMasterService;
	
	@Autowired
	ITDoctorShareService iTDoctorShareService;

	@Autowired
	IUnitApplicationBillingConfigurationService iUnitApplicationBillingConfigurationService;

	@Autowired
	private IBillPayeeDetailsService iBillPayeeDetailsService;

	@RequestMapping(value = "/creditnote/service" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveCreditNoteAgainstService(@RequestBody CreditNoteMasterDto creditNoteMasterDto){
		try{
			return iCreditNoteMasterService.saveCreditNoteAgainstService(creditNoteMasterDto);
		}catch(Exception e){
			
			throw e;
			//new BillingException(e.getMessage());
		}
	}
	@RequestMapping(value = "/creditnote/deposit" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveCreditNoteAgainstDeposit(@RequestBody CreditNoteMasterDto creditNoteMasterDto){
		try{
			return iCreditNoteMasterService.saveCreditNoteAgainstDeposit(creditNoteMasterDto);
		}catch(Exception e){
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}

	@RequestMapping(value = "/creditnote" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveCreditNote(@RequestBody CreditNoteMasterDto creditNoteMasterDto){
		try{
			Response res = iCreditNoteMasterService.saveCreditNote(creditNoteMasterDto);
			
			/*if(creditNoteMasterDto.getIsApproved()!=null && creditNoteMasterDto.getIsApproved()=='Y')
			{
				iTDoctorShareService.updateDocShareAfterCreditNote(creditNoteMasterDto);
				
				//iTDoctorShareService.getProcessDocShare(creditNoteMasterDto.getUnitId());
			}*/
			return res;
		}catch(Exception e){
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/creditnote/discount/details/{billingDetailsId}" , method = RequestMethod.GET)
	public Response creditNoteDiscountDetails(@PathVariable("billingDetailsId")int billingDetailsId ){
		try{
			return iCreditNoteMasterService.creditNoteDiscountDetails(billingDetailsId);
		}catch(Exception e){
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/creditnote/refund" , method = RequestMethod.POST)
	public Response getCreditNoteListForRefund(@RequestBody RefundCnSearchDto reqDto){
		try{
			return iCreditNoteMasterService.getCreditNoteListForRefund(reqDto);
		}catch(Exception e){
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/creditnotes/addendum/{billId}" , method = RequestMethod.GET)
	public Response getCreditNotesForAddendum(@PathVariable("billId") int billId){
		try{
			return iCreditNoteMasterService.getCreditNotesByBillForAddendum(billId);
		}catch(Exception e){
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, e.getMessage(), null, null);
		}
	}

	@RequestMapping(value = "/creditnote/cancel/{creditNoteId}" , method = RequestMethod.GET)
	public Response cancelCreditNote(@PathVariable("creditNoteId") int creditNoteId){
		try{
			Response res = iCreditNoteMasterService.cancelCreditNote(creditNoteId);
			
			//CreditNoteMasterDto creditNoteMasterDto = (CreditNoteMasterDto)res.getObject();
			
			//iTDoctorShareService.updateDocShareAfterCreditNoteCancellation(creditNoteMasterDto);
			
			//iTDoctorShareService.getProcessDocShare(creditNoteMasterDto.getUnitId());
			
			return res;
		}catch(Exception e){
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, e.getMessage(), null, null);
		}
	}

	@RequestMapping(value = "/patient/outstanding/{patientId}" , method = RequestMethod.GET)
	public Response checkPatientOutstanding(@PathVariable("patientId") Integer patientId){
		try{
			return iCreditNoteMasterService.checkPatientOutstanding(patientId);
		}catch(Exception e){
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	
	
	@RequestMapping(value = "/getEncounterAndAdmissionDetails" , method = RequestMethod.POST)
	public Response getEncounterAndAdmissionDetails(@RequestBody OrderMasterDto reqDto){
		try{
			return iCreditNoteMasterService.getEncounterAndAdmissionDetails(reqDto);
		}catch(Exception e){
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/getCreditNoteDetailsByCreditNoteId" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getCreditNoteDetailsByCreditNoteId(@RequestBody CreditNoteDetailsDto creditNoteDetailsDto){
		try{
			return iCreditNoteMasterService.getCreditNoteDetailsByCreditNoteId(creditNoteDetailsDto);
		}catch(Exception e){
			
			throw e;
			//new BillingException(e.getMessage());
		}
	}

	@RequestMapping(value = "/creditnoteFromWeb" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response creditnoteFromWeb(@RequestBody CreditNoteMasterDto creditNoteMasterDto){
		try{

			UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
			unitApplicationConfigurationDto.setOrganizationId(creditNoteMasterDto.getOrgnisationId());
			unitApplicationConfigurationDto.setUnitId(creditNoteMasterDto.getUnitId());
			Response resUnit = iUnitApplicationBillingConfigurationService.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
			if(resUnit.getObject()!= null){
				unitApplicationConfigurationDto = (UnitApplicationConfigurationDto) resUnit.getObject();
			}else{
				return new Response<>(ERROR, ERROR_CODE," Application Configuration is not.", null, null);
			}
			Response resServiceDeatils  =  iBillPayeeDetailsService.getBillPayeeDetailsByBillIdCr(creditNoteMasterDto.getBillId(), 1, creditNoteMasterDto.getEncounterId());

			if(resServiceDeatils.getObject() != null){

				List<BillPayeeDetailsResDto> listBillPayeeDetailsResDto = resServiceDeatils.getListObject();
				List<BillListDetailsCNDto> listBillListDetailsCNDto = (List<BillListDetailsCNDto>) resServiceDeatils.getObject();

				creditNoteMasterDto.setBillPayeeId(unitApplicationConfigurationDto.getSelfPayeeId());
				creditNoteMasterDto.setBillPayeeDetailsId(listBillPayeeDetailsResDto.getFirst().getBillPayeeDetailsId());
				creditNoteMasterDto.setStatus('A');
				creditNoteMasterDto.setCreatedBy(unitApplicationConfigurationDto.getWebAppUserId());
				creditNoteMasterDto.setUpdatedBy(unitApplicationConfigurationDto.getWebAppUserId());
				creditNoteMasterDto.setIsCreditNoteApproval(false);
				creditNoteMasterDto.setIsApproved('N');
				creditNoteMasterDto.setCancelOrDiscount(2);
				creditNoteMasterDto.setRequestSentTo(unitApplicationConfigurationDto.getWebAppUserId());
				creditNoteMasterDto.setCounterId(0);
				creditNoteMasterDto.setDiscountRoundoff(0);
				creditNoteMasterDto.setSignRoundOff(1);
				creditNoteMasterDto.setCrNetAmount(creditNoteMasterDto.getCrTotalAmount());
				creditNoteMasterDto.setCrDeuAmount(creditNoteMasterDto.getCrTotalAmount());
				creditNoteMasterDto.setSelfPayable(new BigDecimal(0));
				creditNoteMasterDto.setCreditPayable(new BigDecimal(0));
				creditNoteMasterDto.setPackageConsumptionAmt(new BigDecimal(0));
				creditNoteMasterDto.setVisitTypeId(1);

				 List<CreditNoteDetailsDto> listCreditNoteDetails = new ArrayList<>();

				 List<Integer> orderIds = new ArrayList<>();
				orderIds.add(creditNoteMasterDto.getEncounterId());
				for(BillListDetailsCNDto dto :listBillListDetailsCNDto){
					CreditNoteDetailsDto obj = new CreditNoteDetailsDto();
					obj.setCrDiscountAmount(0);
					obj.setCrServiceRateAmount(dto.getBasePrice().doubleValue());
					obj.setCrAdditionalDiscount(0);
					obj.setCrTaxAmount(0);
					obj.setCrNetAmount(dto.getBasePrice().doubleValue());
					obj.setBillingDetailsId(dto.getBillingDetailsId());
					obj.setOrganisationId(creditNoteMasterDto.getOrgnisationId());
					obj.setUnitId(creditNoteMasterDto.getUnitId());
					obj.setCreatedBy(unitApplicationConfigurationDto.getWebAppUserId());
					obj.setUpdatedBy(unitApplicationConfigurationDto.getWebAppUserId());
					obj.setOrderId(dto.getOrderId());
					obj.setOrderDetailsId(dto.getOrderDetailsId());
					obj.setCancelOrDiscount(1);
					obj.setSelfPayable(dto.getBasePrice());
					obj.setCreditPayable(new BigDecimal(0));
					obj.setPackageConsumptionAmt(new BigDecimal(0));
					obj.setIsPackage(dto.isPackage());
					obj.setPackageId(dto.getPackageId());
					obj.setListCreditNoteDiscountDetailsDto(new ArrayList<>());
					obj.setPackageTypeId(dto.getPackageTypeId());

					listCreditNoteDetails.add(obj);
				}

				if(creditNoteMasterDto.getListCreditNoteDetails()== null ){
					creditNoteMasterDto.setListCreditNoteDetails(new ArrayList<>());
					creditNoteMasterDto.getListCreditNoteDetails().addAll(listCreditNoteDetails);
				}

				Response res = iCreditNoteMasterService.saveCreditNote(creditNoteMasterDto);
//				Code commented by Avishkar :-
//				if(orderIds.size()>0){
//					iCreditNoteMasterService.inActiveOrderDetails(orderIds);
//				}
				if(creditNoteMasterDto.getEncounterId() != null && creditNoteMasterDto.getEncounterId()>0){
					iCreditNoteMasterService.inActiveOrderDetailsForWeb(creditNoteMasterDto.getEncounterId());
				}
			}



			return resServiceDeatils;
		}catch(Exception e){

			throw e;
		}
	}

}
