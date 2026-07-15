package com.param.billing.global.transaction.controller;

import java.util.Map;

import com.param.global.dto.UnitApplicationConfigurationDto;
import com.param.global.service.IUnitApplicationBillingConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.billing.global.dto.RefundCnSearchDto;
import com.param.billing.global.dto.RefundCollectionReqDto;
import com.param.billing.global.transaction.dto.RefundCNMasterDto;
import com.param.billing.global.transaction.service.IPaymentRefundMasterService;
import com.param.billing.global.transaction.service.IRefundCNMasterService;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.Response;

import static com.param.global.common.ICommonConstants.ERROR;
import static com.param.global.common.ICommonConstants.ERROR_CODE;

@RestController
@RequestMapping("api/billing")
@SuppressWarnings({"rawtypes"})
public class RefundController {

	@Autowired
	private IRefundCNMasterService iRefundCNMasterService;
	
	@Autowired
	private IPaymentRefundMasterService iPaymentRefundMasterService;

	@Autowired
	IUnitApplicationBillingConfigurationService iUnitApplicationBillingConfigurationService;

	
	@RequestMapping(value = "/refund/creditnote" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveRefundCreditNote(@RequestBody RefundCNMasterDto refundCNMasterDto){
		try{
			return iRefundCNMasterService.saveRefundCreditNote(refundCNMasterDto);
		}catch(Exception e){
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/refund/{depositId}/{tRefundCnId}" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveRefundAgainstDeposit(@RequestBody RefundCollectionReqDto refundCollectionReqDto,@PathVariable Map<String,String> params){
		try{
			int depositId = Integer.parseInt(params.get("depositId").trim());
			int tRefundCnId = Integer.parseInt(params.get("tRefundCnId").trim());
			return iPaymentRefundMasterService.saveRefundAgainstDeposit(refundCollectionReqDto, depositId, tRefundCnId);
		}catch(Exception e){
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/refund/cr/{billMasterId}/{billPayeeId}" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveRefundAgainstCreditNote(@RequestBody RefundCollectionReqDto refundCollectionReqDto,@PathVariable Map<String,String> params){
		try{
			int billMasterId = Integer.parseInt(params.get("billMasterId").trim());
			int billPayeeId = Integer.parseInt(params.get("billPayeeId").trim());
			return iPaymentRefundMasterService.saveRefundAgainstCreditNote(refundCollectionReqDto, billMasterId, billPayeeId);
		}catch(Exception e){
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/refund/creditnote/search" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response searchRefundCn(@RequestBody RefundCnSearchDto reqDto){
		try{
			return iRefundCNMasterService.searchRefundCn(reqDto);
		}catch(Exception e){
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/refund/search" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response searchRefundMaster(@RequestBody RefundCnSearchDto reqDto){
		try{
			return iPaymentRefundMasterService.searchRefundMaster(reqDto);
		}catch(Exception e){
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/refund/details/{orgId}/{unitId}/{paymentRefundVoucherId}" , method = RequestMethod.GET)
	public Response getRefundDetailsByRefundId(@PathVariable Map<String,String> params){
		try{
			int orgId = Integer.parseInt(params.get("orgId").trim());
			int unitId = Integer.parseInt(params.get("unitId").trim());
			int paymentRefundVoucherId = Integer.parseInt(params.get("paymentRefundVoucherId").trim());
			return iPaymentRefundMasterService.getRefundDetailsByRefundId(orgId,unitId,paymentRefundVoucherId);
		}catch(Exception e){
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/refund/bankDetails/{orgId}/{unitId}/{tRefundCnId}" , method = RequestMethod.GET)
	public Response getRefundBankDetails(@PathVariable Map<String,String> params){
		try{
			int orgId = Integer.parseInt(params.get("orgId").trim());
			int unitId = Integer.parseInt(params.get("unitId").trim());
			int tRefundCnId = Integer.parseInt(params.get("tRefundCnId").trim());
			return iPaymentRefundMasterService.getRefundBankDetails(orgId,unitId,tRefundCnId);
		}catch(Exception e){
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}

	@RequestMapping(value = "/refund/cr/search" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response searchRefundedCreditNote(@RequestBody RefundCnSearchDto reqDto){
		try{
			return iPaymentRefundMasterService.searchRefundedCreditNote(reqDto);
		}catch(Exception e){
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/refund/cr/details/{orgId}/{unitId}/{paymentRefundVoucherId}" , method = RequestMethod.GET)
	public Response searchRefundedCreditNoteDetails(@PathVariable Map<String,String> params){
		try{
			int orgId = Integer.parseInt(params.get("orgId").trim());
			int unitId = Integer.parseInt(params.get("unitId").trim());
			int paymentRefundVoucherId = Integer.parseInt(params.get("paymentRefundVoucherId").trim());
			return iPaymentRefundMasterService.searchRefundedCreditNoteDetails(orgId,unitId,paymentRefundVoucherId);
		}catch(Exception e){
			
			throw e;
		//	throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/refund/pharmacy" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveRefundAgainstPharamacy(@RequestBody RefundCollectionReqDto refundCollectionReqDto){
		try{
			return iPaymentRefundMasterService.saveRefundAgainstPharamacy(refundCollectionReqDto);
		}catch(Exception e){
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}

	@RequestMapping(value = "/refund/creditNoteForWeb/{billMasterId}" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveRefundAgainstCreditNoteForWeb(@RequestBody RefundCollectionReqDto refundCollectionReqDto,@PathVariable Map<String,String> params){
		try{

			UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
			unitApplicationConfigurationDto.setOrganizationId(refundCollectionReqDto.getPaymentRefundMasterDto().getOrgnisationId());
			unitApplicationConfigurationDto.setUnitId(refundCollectionReqDto.getPaymentRefundMasterDto().getUnitId());
			Response resUnit = iUnitApplicationBillingConfigurationService.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
			if(resUnit.getObject()!= null){
				unitApplicationConfigurationDto = (UnitApplicationConfigurationDto) resUnit.getObject();
			}else{
				return new Response<>(ERROR, ERROR_CODE," Application Configuration is not.", null, null);
			}
			int billMasterId = Integer.parseInt(params.get("billMasterId").trim());
			int billPayeeId = unitApplicationConfigurationDto.getSelfPayeeId();
			refundCollectionReqDto.getPaymentRefundMasterDto().setIsCnOrDeposit(1);
			refundCollectionReqDto.getPaymentRefundMasterDto().setStatus('A');
			refundCollectionReqDto.getPaymentRefundMasterDto().setCounterId(0);
			refundCollectionReqDto.getPaymentRefundMasterDto().setCancelOrDiscount(2);
			refundCollectionReqDto.getPaymentRefundMasterDto().setCreatedBy(unitApplicationConfigurationDto.getWebAppUserId());
			refundCollectionReqDto.getListPaymentRefundDetails().get(0).setPaymentModeId(8);
			refundCollectionReqDto.getListPaymentRefundDetails().get(0).setCreatedBy(unitApplicationConfigurationDto.getWebAppUserId());

			return iPaymentRefundMasterService.saveRefundAgainstCreditNote(refundCollectionReqDto, billMasterId, billPayeeId);
		}catch(Exception e){

			throw e;
			//throw new BillingException(e.getMessage());
		}
	}


}
