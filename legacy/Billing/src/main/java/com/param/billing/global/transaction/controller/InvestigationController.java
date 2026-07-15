 
package com.param.billing.global.transaction.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.billing.dto.InvestigationDTO;
import com.param.billing.dto.InvestigationOrderDto;
import com.param.billing.global.transaction.service.IBillingMasterService;
import com.param.billing.global.transaction.service.IinvestigationService;
import com.param.global.billing.service.IBillingService;
import com.param.global.billing.service.IContractConsumptionService;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.BillingMasterDto;
import com.param.global.dto.ContractConsumptionDto;
import com.param.global.dto.FreeTextOrderedServicesDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.OrderMasterDto;
import com.param.global.dto.ServiceSearchResDto;
import com.param.global.pharmacy.service.IWalkInSalesService;
import com.param.global.service.IMPackageMasterService;
import com.param.global.service.IOrderDetailsMasterService;
import com.param.global.service.IOrderMasterService;
import com.param.service.global.SessionService;

/*FUNCTIONALITY : Investigation
DATE:16/04/2018
CREATED BY: TUSHAR*/
@RestController
@RequestMapping("api/opd")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class InvestigationController implements ICommonConstants{
	
	@Autowired 
	private IinvestigationService inIinvestigationService;
	
	@Autowired 
	private IBillingMasterService iBillingMasterService;
	
	@Autowired 
	private IWalkInSalesService iWalkInSalesService;
	
	@Autowired 
	private IContractConsumptionService iContractConsumptionService;
	
	@Autowired 
	private IMPackageMasterService iMPackageMasterService;
	
	@Autowired 
	private IOrderMasterService iOrderMasterService;
	
	@Autowired 
	private IOrderDetailsMasterService iOrderDetailsMasterService;
	
	@Autowired
	SessionService sessionService;
	
	@Autowired
	IBillingService globalBillingService;
	
	@RequestMapping(value="/saveInvestigation", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveInvestigation(@RequestBody OrderMasterDto orderMasterDto) throws Exception {
		try
		{
			
			boolean updatePackageFlag =false;
			Response res = inIinvestigationService.saveOrdersFromInvestigation(orderMasterDto);
			
			
			if(res.getResObject()!=null)
			{
			    Response orderRes = (Response) res.getResObject();
			    BillingMasterDto billingMasterDto = null;
			    Character isPreOrPostBilling = null;
			    for (OrderMasterDto masterDto : orderMasterDto.getListOrderMasterDto()) 
			    {
			    	
			    	for (OrderDetailsMasterDto orderDetailsDto : masterDto.getListOrderDetailsMasterDto()) 
				    {
				    	 if(orderDetailsDto.isPackage())
							{
				    		    	//SAVE PACKAGE CONSUMPTION 
									billingMasterDto  = new BillingMasterDto();
									billingMasterDto.setOrganisationId(masterDto.getOrgId());
									billingMasterDto.setUnitId(masterDto.getOrgUnitId());
									billingMasterDto.setCreatedBy(masterDto.getCreatedBy());
									billingMasterDto.setUpdatedBy(masterDto.getCreatedBy());
									billingMasterDto.setPatientId(masterDto.getPatientId());
									billingMasterDto.setVisitTypeId(masterDto.getVisitTypeId());
									billingMasterDto.setEncounterId(masterDto.getEncounterId());
									billingMasterDto.setAdmissionId(masterDto.getAdmissionId());
									billingMasterDto.setPaymentEntitlementId(masterDto.getPaymentEntitlementTypeId());
									billingMasterDto.setPatientTypeId(masterDto.getPatientTypeId());
									billingMasterDto.setBillingBedCategoryId(masterDto.getBillingBedCategoryId());
									billingMasterDto.setContractId(masterDto.getContractId());
									billingMasterDto.setTariffId(masterDto.getTariffId()!=null && masterDto.getTariffId()>0?masterDto.getTariffId():masterDto.getDefaultSelfTariffId());
									billingMasterDto.setPharmacyServiceId(masterDto.getPharmacyServiceId());
									billingMasterDto.setSelfPayeeId((Integer)res.getObject());
									iBillingMasterService.packageConsumption(billingMasterDto, masterDto.getListOrderDetailsMasterDto(), orderRes);
									
									//package update
									updatePackageFlag = true;
									isPreOrPostBilling = masterDto.getIsPreOrPostBilling();
								
							}
					}
				}
			    if(updatePackageFlag && isPreOrPostBilling!=null)
			    {
			    	List<OrderDetailsMasterDto> listToBeUpdated = new ArrayList<>();
			    	List<OrderDetailsMasterDto> listToBeAdded = new ArrayList<>();
					List<OrderDetailsMasterDto> orderDetailsList = new ArrayList<>();
					List<Integer> orderDetailsIds = new LinkedList();
					
			    	Response updatePayeeRes = iBillingMasterService.updatePackageCoverSheet(billingMasterDto,isPreOrPostBilling,true,false);
					if (updatePayeeRes.getStatus().equals(SUCCESS)) 
					{
						listToBeUpdated = updatePayeeRes.getListObject();
						if(listToBeUpdated.size()>0)
						{
							for(OrderDetailsMasterDto detailsMasterDto : listToBeUpdated)
							{
			                    if(detailsMasterDto.getServiceId().intValue() == billingMasterDto.getPharmacyServiceId())
								{
			                    	List<OrderDetailsMasterDto> pharamcyList = new ArrayList<>();
			                    	pharamcyList.add(detailsMasterDto);
			                    	if(detailsMasterDto.getOrderQty()>0)
			                    	{
			                    		detailsMasterDto.setConcessionPer(detailsMasterDto.getPatientClassPercentage().doubleValue());
			    						iWalkInSalesService.updatePayeePharamcy(pharamcyList, billingMasterDto);
			                    	}else{
			                    		
			                    		if(detailsMasterDto.getOldNetPay().doubleValue()==0.00)
			                    		{
			                    			iWalkInSalesService.updatePayeeRetunService(pharamcyList, billingMasterDto);
			                    		}
			                    		else {
			                    			iWalkInSalesService.applyDiscountOnPharamcyReturn(pharamcyList, billingMasterDto);
			                    		}
			                    		
			                    	}
			                    	if(detailsMasterDto.getDiscountCategoryId() != null){
			                    		iWalkInSalesService.updatediscountCategory(detailsMasterDto);
			                    	}
			                    	
								}else if (detailsMasterDto.getIsPackageService()=='Y'){
									orderDetailsList.add(detailsMasterDto);
								}else{
									ContractConsumptionDto consumptionDto;
									if(detailsMasterDto.isPackage())
									{
										ServiceSearchResDto searchResDto = new ServiceSearchResDto();
											searchResDto.setAdmissionId(billingMasterDto.getAdmissionId());
											searchResDto.setEncounterId(billingMasterDto.getEncounterId());
											searchResDto.setContractId(billingMasterDto.getContractId());
											searchResDto.setServiceMasterId(detailsMasterDto.getServiceId());
											searchResDto.setOrderDate(GlobalCommonDateUtils.getStringDate(detailsMasterDto.getOrderDate(), "yyyy-MM-dd HH:mm:ss"));
											searchResDto.setOrganizationId(billingMasterDto.getOrganisationId());
											searchResDto.setUnitId(billingMasterDto.getUnitId());
											searchResDto.setVisitTypeId(billingMasterDto.getVisitTypeId());
											searchResDto.setBillingBedCategoryId(billingMasterDto.getBillingBedCategoryId());
											searchResDto.setPatientCategoryId(billingMasterDto.getPatientTypeId());
											searchResDto.setDefaultSelfTariffId(billingMasterDto.getTariffId());
											searchResDto.setPackageMasterId(detailsMasterDto.getPackageId());
											searchResDto.setChangedPrice(detailsMasterDto.getServiceAmt().doubleValue());
											searchResDto.setOrdDiscAmt(detailsMasterDto.getServiceAmt().doubleValue()>0 ? detailsMasterDto.getOrdDiscount().doubleValue() : 0.00);
											searchResDto.setOrdDiscPer(detailsMasterDto.getServiceAmt().doubleValue()>0 ? detailsMasterDto.getOrdDiscountPer().doubleValue():0.00);
											searchResDto.setBasePrice(detailsMasterDto.getServiceAmt());
											searchResDto.setApplyAuth(billingMasterDto.isAddPreAuth());
											searchResDto.setPatientClassPer(detailsMasterDto.getPatientClassPercentage()!=null && detailsMasterDto.getServiceAmt().doubleValue()>0 ? detailsMasterDto.getPatientClassPercentage().doubleValue() : 0.00);
										res =iContractConsumptionService.consumePackageFromContract(searchResDto);	
										consumptionDto = (ContractConsumptionDto) res.getObject();
									}else{
										consumptionDto=new ContractConsumptionDto();
											consumptionDto.setOrganizationId(billingMasterDto.getOrganisationId());
											consumptionDto.setUnitId(billingMasterDto.getUnitId());
											consumptionDto.setVisitTypeId(billingMasterDto.getVisitTypeId());
											consumptionDto.setPatientTypeId(billingMasterDto.getPatientTypeId());
											consumptionDto.setBillingBedCategoryId(billingMasterDto.getBillingBedCategoryId());
											consumptionDto.setPaymentEntitlementId(1);
											consumptionDto.setServiceId(detailsMasterDto.getServiceId());
											
											if(detailsMasterDto.getOrdRenderDatetime()!= null ) {
												
												consumptionDto.setCurrentDate(GlobalCommonDateUtils.getStringDate(detailsMasterDto.getOrdRenderDatetime(), "yyyy-MM-dd HH:mm:ss"));
												
											}else {
												
												consumptionDto.setCurrentDate(GlobalCommonDateUtils.getStringDate(detailsMasterDto.getOrderDate(), "yyyy-MM-dd HH:mm:ss"));
											}
											
											consumptionDto.setContractId(billingMasterDto.getContractId());
											consumptionDto.setEncounterId(billingMasterDto.getEncounterId());
											consumptionDto.setAdmissionId(billingMasterDto.getAdmissionId());
											consumptionDto.setDefaultSelfTariffId(billingMasterDto.getTariffId());
											consumptionDto.setPatientId(billingMasterDto.getPatientId());
											consumptionDto.setSelfPayeeId(billingMasterDto.getSelfPayeeId());
										    consumptionDto.setPatientClassId(billingMasterDto.getPatientClassId());	
										    consumptionDto.setServiceRate(detailsMasterDto.getServiceAmt().doubleValue());
										    consumptionDto.setChangedPrice(detailsMasterDto.getServiceAmt().doubleValue());
										    consumptionDto.setSpecialityId(detailsMasterDto.getDepartmentId());
										    consumptionDto.setSubSpecialityId(detailsMasterDto.getSubDepartmentId());
										    consumptionDto.setOrdDiscountPer(detailsMasterDto.getServiceAmt().doubleValue()>0 ? detailsMasterDto.getOrdDiscountPer().doubleValue():0.00);
										    consumptionDto.setPatientClassPercentage(detailsMasterDto.getServiceAmt().doubleValue()>0 ? detailsMasterDto.getPatientClassPercentage().doubleValue():0.00);
										    consumptionDto.setQuantity(detailsMasterDto.getOrderQty().doubleValue());
										    consumptionDto.setDocSplId(detailsMasterDto.getOrdDocSplId());
										if(billingMasterDto.isPackageExist() && detailsMasterDto.getForceAddRemoved()!=(-1))		
											res = iMPackageMasterService.checkServiceAvailableInPackageContract(consumptionDto);
										else
											res =iContractConsumptionService.consumeServiceFromContract(consumptionDto);
										
										consumptionDto=(ContractConsumptionDto)res.getObject();
									}
									if(res!=null || res.getObject()!=null)
									{
										detailsMasterDto.setPatientClassPercentage(new BigDecimal(consumptionDto.getPatientClassPercentage()!=null ? consumptionDto.getPatientClassPercentage():0.00));
										detailsMasterDto.setServiceAmt(new BigDecimal(consumptionDto.getServiceRate()!=null ? consumptionDto.getServiceRate() : 0.00));
										detailsMasterDto.setDummyServiceAmt(new BigDecimal(consumptionDto.getServiceRate()!=null ? consumptionDto.getServiceRate():0.00));
										detailsMasterDto.setTariffId(detailsMasterDto.getTariffId());
										detailsMasterDto.setOrdTotalAmt(new BigDecimal((consumptionDto.getServiceRate()!=null?consumptionDto.getServiceRate():0.00)*(detailsMasterDto.getOrderQty()!=null ? detailsMasterDto.getOrderQty():1)));
										detailsMasterDto.setOrdConcession(new BigDecimal(consumptionDto.getConcession()!=null ? consumptionDto.getConcession():0.00));
										detailsMasterDto.setOrdDiscount(new BigDecimal(consumptionDto.getOrdDiscount()));
										detailsMasterDto.setTaxId(consumptionDto.getTaxId());
										detailsMasterDto.setTaxAmount(new BigDecimal(consumptionDto.getTaxAmt()!=null ? consumptionDto.getTaxAmt():0.00));
										Boolean checkIn = false;
										if(consumptionDto.getPackageConsumptionAmt() != null && consumptionDto.getPackageConsumptionAmt().compareTo(BigDecimal.ZERO) > 0 ){
											
											if((consumptionDto.getFinalPrice() - consumptionDto.getTaxAmt()) == consumptionDto.getPackageConsumptionAmt().doubleValue()){
											
													detailsMasterDto.setNetAmount(consumptionDto.getPackageConsumptionAmt()!=null ? consumptionDto.getPackageConsumptionAmt():new BigDecimal(consumptionDto.getFinalPrice()!=null ? consumptionDto.getFinalPrice():0.00));
													detailsMasterDto.setCreditPayable(new BigDecimal(0.00));
													detailsMasterDto.setIsPackageService('Y');
													checkIn = true;
												
											}else if(consumptionDto.getServiceRate() > consumptionDto.getPackageConsumptionAmt().doubleValue()) {
												detailsMasterDto.setNetAmount(new BigDecimal(consumptionDto.getFinalPrice()!=null ? consumptionDto.getFinalPrice():0.00));
											}else{
												detailsMasterDto.setNetAmount(new BigDecimal(consumptionDto.getFinalPrice()!=null ? consumptionDto.getFinalPrice():0.00));
											}
											
										}else{
											detailsMasterDto.setNetAmount(new BigDecimal(consumptionDto.getFinalPrice()!=null ? consumptionDto.getFinalPrice():0.00));
										}
										
										
										detailsMasterDto.setTaxPer(BigDecimal.valueOf(consumptionDto.getTaxPercentage()!=null ? consumptionDto.getTaxPercentage():0.00));
										detailsMasterDto.setSelfPayable(new BigDecimal(consumptionDto.getSelfPayable()!=null?consumptionDto.getSelfPayable():0.00));
										detailsMasterDto.setPayeeId(consumptionDto.getPayeeId());
										if(checkIn == false){
											detailsMasterDto.setCreditPayable(new BigDecimal(consumptionDto.getCreaditPayable()!=null?consumptionDto.getCreaditPayable():0.00));
										}
										detailsMasterDto.setContractId(consumptionDto.getContractId());
										detailsMasterDto.setPackageConsumptionAmt(consumptionDto.getPackageConsumptionAmt()!=null ? consumptionDto.getPackageConsumptionAmt():BigDecimal.ZERO);
										detailsMasterDto.setCoPayPer(new BigDecimal(consumptionDto.getCoShare()!=null?consumptionDto.getCoShare():0.00));
										detailsMasterDto.setOrderDetailsPayeeMapperDtosList(consumptionDto.getOrderDetailsPayeeMapperDtosList());
										if(consumptionDto.getOrderDetailsPackageMapperDtosList()!=null && consumptionDto.getOrderDetailsPackageMapperDtosList().size()>0)
											detailsMasterDto.setOrderDetailsPackageMapperDtosList(consumptionDto.getOrderDetailsPackageMapperDtosList());
										else
											detailsMasterDto.setOrderDetailsPackageMapperDtosList(null);
									}
				                    if(detailsMasterDto.getPatientCouponTemplateMapperId() != null ){
				                    	iOrderMasterService.updateCouponConsumptionId(detailsMasterDto.getTariffId(),detailsMasterDto.getPatientCouponTemplateMapperId(),detailsMasterDto.getOrderDetailsId());
									}
				                    orderDetailsList.add(detailsMasterDto);
								}
			                    orderDetailsIds.add(detailsMasterDto.getOrderDetailsId());
							}
						}
					}
					if(orderDetailsList.size()>0 && listToBeAdded.size()>0)
					{
						orderMasterDto.setListOrderDetailsMasterDto(orderDetailsList);
						Response savedRes = iOrderMasterService.saveOrderMasterWithDiscount(orderMasterDto,null);
						orderDetailsIds = savedRes.getListObject();
					}
					if(orderDetailsList.size()>0 && listToBeUpdated.size()>0)
					{
						orderMasterDto.setListOrderDetailsMasterDto(orderDetailsList);
						iOrderDetailsMasterService.updateOrderDetails(orderDetailsList, null,(billingMasterDto.getLineLevelDiscountOrderList()!=null)?billingMasterDto.getLineLevelDiscountOrderList().size():null, orderMasterDto);
				
					
					}
					
			    }
			    if(res.getListObject2()!=null && res.getListObject2().size()>0 && orderMasterDto.isAddToBill()==true)
			    {
			    	globalBillingService.postServicesIntoExistingBill(res.getListObject2());
			    }
				return res;
			}
				return res;
			
		}catch(Exception e)
		{
			
			throw e;
		}
		//return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getCurrentInvestigations", method=RequestMethod.GET)
	public @ResponseBody Response getInvestigations(@RequestHeader("patientId") Integer patient_id, @RequestHeader("flag") boolean flag) {
		try
		{
			return inIinvestigationService.getCurrentInvestigations(patient_id,flag);
		}catch(Exception e)
		{
			
			throw e;
		}
		//return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value="/currentInvestigationDetails", method=RequestMethod.GET)
	public @ResponseBody Response getInvestigations1(@RequestHeader("panelId") Integer panelId, @RequestHeader("orderDetailsId") Integer orderDetailsId) {
		try
		{
			return inIinvestigationService.getCurrentInvestigationDetails(panelId,orderDetailsId);
		}catch(Exception e)
		{
			
			throw e;
		}
		//return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getInvestigations", method=RequestMethod.GET)
	public @ResponseBody Response getInvestigations(@RequestParam("start") String start_date,@RequestParam("end") String end_date,@RequestParam("service_id") Integer service_id) {
		try
		{
			return inIinvestigationService.getInvestigations(start_date,end_date,service_id);
		}catch(Exception e)
		{
			
			throw e;
		}
		//return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/updateInvestigation", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateInvestigation(@RequestBody InvestigationDTO investigationDTO) {
		try
		{
			return inIinvestigationService.updateInvestigation(investigationDTO);
		}catch(Exception e)
		{
			
			throw e;
		}
		//return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/deleteInvestigation", method=RequestMethod.DELETE)
	public @ResponseBody Response deleteInvestigation(@RequestParam("investigation_id") Integer investigation_id) {
		try
		{
			return inIinvestigationService.deleteInvestigation(investigation_id);
		}catch(Exception e)
		{
			
			throw e;
		}
		//return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getServices", method=RequestMethod.GET)
	public @ResponseBody Response getServices(@RequestParam(value = "code", required = false) String serviceCode, @RequestParam(value = "name", required = false) String serviceName, @RequestParam("orgId") Integer orgId) {
		try
		{
			return inIinvestigationService.getServices(serviceCode, serviceName,orgId);
		}catch(Exception e)
		{
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getHistoryServices", method=RequestMethod.GET)
	public @ResponseBody Response getHistoryServices(@RequestParam("orgId") Integer orgId) {
		try
		{
			return inIinvestigationService.getHistoryServices(orgId);
		}catch(Exception e)
		{
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getServiceTypes", method=RequestMethod.GET)
	public @ResponseBody Response getServiceTypes() {
		try
		{
			return inIinvestigationService.getServiceTypes();
		}catch(Exception e)
		{
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getServicesByTest", method=RequestMethod.GET)
	public @ResponseBody Response getServicesByTest(@RequestParam(value = "serviceId") Integer serviceId) {
		try
		{
			return inIinvestigationService.getServicesByTest(serviceId);
		}catch(Exception e)
		{
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/previousInvestigation/{orgId}/{orgUnitId}/{patientId}/{visitTypeId}", method=RequestMethod.GET)
	public @ResponseBody Response getPreviousInvestigation(@PathVariable Map<String,String> params) {
		try
		{
			int orgId =Integer.parseInt(params.get("orgId").trim());
			int orgUnitId =Integer.parseInt(params.get("orgUnitId").trim());
			int patientId=Integer.parseInt(params.get("patientId").trim());
			int visitTypeId=Integer.parseInt(params.get("visitTypeId").trim());
			return inIinvestigationService.getPreviousInvestigation(orgId,orgUnitId,patientId,visitTypeId);
		}catch(Exception e)
		{
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/previousInvestigationDetails/{orgId}/{orgUnitId}/{orderId}", method=RequestMethod.GET)
	public @ResponseBody Response getPreviousInvestigationDetails(@PathVariable Map<String,String> params) {
		try
		{
			int orgId =Integer.parseInt(params.get("orgId").trim());
			int orgUnitId =Integer.parseInt(params.get("orgUnitId").trim());
			int orderId=Integer.parseInt(params.get("orderId").trim());
			return inIinvestigationService.getPreviousInvestigationDetails(orgId,orgUnitId,orderId);
		}catch(Exception e)
		{
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value="/getCurrentInvestigationsByPatientId", method=RequestMethod.GET)
	public @ResponseBody Response getCurrentInvestigationsByPatientId(@RequestHeader("patientId") Integer patient_id) {
		try
		{
			return inIinvestigationService.getCurrentInvestigationsByPatientId(patient_id);
		}catch(Exception e)
		{
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getInvestigationsListForBookView/{patientId}/{orgId}/{fromDate}/{toDate}", method=RequestMethod.GET)
	public @ResponseBody Response getInvestigationsListForBookView(@PathVariable("patientId") int patientId,
			  @PathVariable("orgId") Integer organizationId,
			  @PathVariable("fromDate") String fromDate,
			  @PathVariable("toDate") String toDate) {
		try
		{
			return inIinvestigationService.getInvestigationsListForBookView(patientId, organizationId, fromDate, toDate);
		}catch(Exception e)
		{
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getInvestigationsListForCoversheet", method=RequestMethod.GET)
	public @ResponseBody Response getInvestigationsListForCoversheet(@RequestHeader("patientId") Integer patient_id, @RequestHeader("flag") boolean flag, @RequestHeader("unitId") Integer unitId) {
		try
		{
			return inIinvestigationService.getInvestigationsListForCoversheet(patient_id,flag,unitId);
		}catch(Exception e)
		{
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getInvestigationsListForAnesthesia", method=RequestMethod.GET)
	public @ResponseBody Response getInvestigationsListForAnesthesia(@RequestHeader("patientId") Integer patient_id, @RequestHeader("flag") boolean flag, @RequestHeader("unitId") Integer unitId) {
		try
		{
			return inIinvestigationService.getInvestigationsListForAnesthesia(patient_id,flag,unitId);
		}catch(Exception e)
		{
			throw e;
		}
//		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getCurrentInvestigationsByProcedureIdAndVisitId/", method=RequestMethod.GET)
	public @ResponseBody Response getCurrentInvestigationsByProcedureIdAndVisitId(@RequestHeader("visitId") Integer visitId,
			@RequestHeader("procedureId") Integer procedureId,@RequestHeader("flag") boolean flag) {
		try
		{
			return inIinvestigationService.getCurrentInvestigationsByProcedureIdAndVisitId(visitId,procedureId,flag);
		}catch(Exception e)
		{
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getInvestigationsByPatientDetails",  method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getInvestigationsByPatientDetails(@RequestBody InvestigationOrderDto investigationOrderDto) {
		try
		{
			return inIinvestigationService.getInvestigationsByPatientDetails(investigationOrderDto);
		}catch(Exception e)
		{
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/updateDiagnosticsRemarks",  method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateDiagnosticsRemarks(@RequestBody InvestigationOrderDto investigationOrderDto) {
		try
		{
			return inIinvestigationService.updateDiagnosticsRemarks(investigationOrderDto);
		}catch(Exception e)
		{
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/cancelLabOrderForNewEMR",  method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response cancelLabOrderForNewEMR(@RequestBody InvestigationOrderDto investigationOrderDto) {
		try
		{
			return inIinvestigationService.cancelLabOrderForNewEMR(investigationOrderDto);
		}catch(Exception e)
		{
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getInvestigationsListForNewEMR", method=RequestMethod.GET)
	public @ResponseBody Response getInvestigationsListForNewEMR(@RequestParam("patientId") Integer patient_id, @RequestParam("flag") boolean flag, @RequestParam("unitId") Integer unitId) {
		try
		{
			return inIinvestigationService.getInvestigationsListForCoversheet(patient_id,flag,unitId);
		}catch(Exception e)
		{
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/saveFreeTextInvestigations",  method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveFreeTextInvestigations(@RequestBody FreeTextOrderedServicesDto freeTextOrderedServicesDto) {
		try
		{
			return inIinvestigationService.saveFreeTextInvestigations(freeTextOrderedServicesDto);
		}catch(Exception e)
		{
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getFreeTextInvestigations",  method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getFreeTextInvestigations(@RequestBody FreeTextOrderedServicesDto freeTextOrderedServicesDto) {
		try
		{
			return inIinvestigationService.getFreeTextInvestigations(freeTextOrderedServicesDto);
		}catch(Exception e)
		{
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/searchFreeTextInvestigations",  method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response searchFreeTextInvestigations(@RequestBody FreeTextOrderedServicesDto freeTextOrderedServicesDto) {
		try
		{
			return inIinvestigationService.searchFreeTextInvestigations(freeTextOrderedServicesDto);
		}catch(Exception e)
		{
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/cancelFreeTextInvestigations",  method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response cancelFreeTextInvestigations(@RequestBody FreeTextOrderedServicesDto freeTextOrderedServicesDto) {
		try
		{
			return inIinvestigationService.cancelFreeTextInvestigations(freeTextOrderedServicesDto);
		}catch(Exception e)
		{
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/saveInvestigationForAppointmentTemplate", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveInvestigationForAppointmentTemplate(@RequestBody OrderMasterDto orderMasterDto) throws Exception {
		try
		{
			
			return inIinvestigationService.saveInvestigationForAppointmentTemplate(orderMasterDto);
			
		}catch(Exception e)
		{
			
			throw e;
		}
	}

	@RequestMapping(value="/getDataByDateFromInvestigation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
	public @ResponseBody Response getDataForIPDCoversheetByDate(@RequestBody InvestigationOrderDto dto) {
		try
		{
			return inIinvestigationService.getInvestigationForIPDCoversheetByDate(dto);
		}catch(Exception e)
		{

		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getInvestigationsListForCoversheet/getUniqueDateFromInvestigation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
	public @ResponseBody Response getUniqueDateForIPDCoversheet(@RequestBody InvestigationOrderDto dto) {
		try
		{
			return inIinvestigationService.getUniqueDateForIPDCoversheet(dto);
		}catch(Exception e)
		{

		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	
}
