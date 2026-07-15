package com.param.pharmacy.op.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.common.dto.CommonResponse;
import com.param.common.dto.ErrorResponse;
import com.param.entity.model.pharmacy.SaleDetail;
import com.param.global.billing.dto.CommonBillingItemsDto;
import com.param.global.billing.dto.PharmacyBillCommonDto;
import com.param.global.billing.service.IBillingService;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.BillingMasterDto;
import com.param.global.dto.OrderDetailsPackageMapperDto;
import com.param.global.dto.PatientRegistrationDto;
import com.param.global.dto.PatientSponsorDetailsDto;
import com.param.global.dto.UnitApplicationConfigurationDto;
import com.param.global.dto.items.ItemsDetailsListResponse;
import com.param.global.pharmacy.dto.ItemRequest;
import com.param.global.pharmacy.dto.ParamacyContractReqDto;
import com.param.global.pharmacy.dto.PendingIssuesSave;
import com.param.global.pharmacy.dto.SaleDetailPackageMapperDto;
import com.param.global.pharmacy.dto.WalkInPrescriptionReq;
import com.param.global.pharmacy.dto.WalkInSalesDropdownResponse;
import com.param.global.pharmacy.dto.WalkInSalesRequest;
//import com.param.global.pharmacy.dto.WalkInSalesRequest;
import com.param.global.pharmacy.service.IWalkInSalesService;
import com.param.global.service.IMPackageMasterService;
import com.param.global.service.IPatientSponsorDetailsService;
import com.param.global.service.IUnitApplicationBillingConfigurationService;
//import com.param.pharmacy.ip.dto.pendingIssues.PendingIssuesSave;
//import com.param.pharmacy.op.dto.walkInSales.ItemRequest;
//import com.param.pharmacy.op.dto.walkInSales.ParamacyContractReqDto;
//import com.param.pharmacy.op.dto.walkInSales.WalkInSalesDropdownResponse;
//import com.param.pharmacy.op.dto.walkInSales.WalkInSalesRequest;
import com.param.pharmacy.op.service.walkInSales.CustomException;
import com.param.utility.TimeZoneComponent;

import org.apache.log4j.Logger;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/pharmacy/sales/walkins")
@SuppressWarnings({"rawtypes","unchecked"})
public class WalkInSalesController implements ICommonConstants {

	@Autowired
	IWalkInSalesService WalkInSalesService;
	
	@Autowired
	IMPackageMasterService iMPackageMasterService;
	
	@Autowired
	IPatientSponsorDetailsService iPatientSponsorDetailsService; 
	
	@Autowired
	IBillingService globalBillingService;
	
	@Autowired
	IUnitApplicationBillingConfigurationService iUnitApplicationBillingConfigurationService;

	private static final Logger logger = Logger.getLogger(WalkInSalesController.class);

	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = WalkInSalesDropdownResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getDropdownList(@PathVariable Integer orgId,@PathVariable Integer unitId) throws Exception {
		try {
			return new ResponseEntity<Object>(WalkInSalesService.getDropdowns(orgId,unitId), HttpStatus.OK);
		} 
		catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = { "/items/{screenId}/{unitId}/{orgId}/{storeId}/{userId}/{itemId}/{tempBillId}",
			"/items/{screenId}/{unitId}/{orgId}/{storeId}/{userId}/{itemId}/{tempBillId}/{allocaltionId}" }, method = RequestMethod.PUT)
	@ApiOperation(value = "Allocate item quantity", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public synchronized ResponseEntity<Object> allocate(@PathVariable Map<String, String> pathVariables,
			@RequestBody ItemRequest itemRequest) throws Exception {
		try {
			WalkInSalesRequest request = null;
			List<Integer> allocationIds = new ArrayList<Integer>();
			if (pathVariables.containsKey("allocaltionId") && pathVariables.get("allocaltionId")!=null) {
				
				List<String> listStrIds = Arrays.asList(pathVariables.get("allocaltionId").split(","));
				listStrIds.stream().forEach(i -> allocationIds.add(Integer.parseInt(i)));
				
			}
			if (pathVariables.containsKey("tempBillId")) {
				request = new WalkInSalesRequest(Integer.parseInt(pathVariables.get("screenId")),
						Long.parseLong(pathVariables.get("tempBillId")), itemRequest);
			}
			request.setOrganizationId(Integer.parseInt(pathVariables.get("orgId")));
			request.setUnitId(Integer.parseInt(pathVariables.get("unitId")));
			request.setStoreId(Integer.parseInt(pathVariables.get("storeId")));
			request.setUserId(Integer.parseInt(pathVariables.get("userId")));
			request.setIsFromOt(itemRequest.getIsFromOt()!=null ? itemRequest.getIsFromOt() : false);
			request.getItem().setItemId(Integer.parseInt(pathVariables.get("itemId")));
			request.getItem().setAllocationIds(itemRequest.getAllocationIds()!=null ? itemRequest.getAllocationIds() : allocationIds);
			
			return new ResponseEntity<Object>(WalkInSalesService.allocateItems(request), HttpStatus.OK);
		} 
		catch(CustomException e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = { "/items/{screenId}/{tempBillId}",
			"/items/{screenId}/{unitId}/{orgId}/{storeId}/{userId}/{tempBillId}/{allocaltionId}" }, method = RequestMethod.DELETE)
	@ApiOperation(value = "Delete allocated item quantity", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> deleteAllocation(@PathVariable Map<String, String> pathVariables) throws Exception {
		try {
			Long tempBillId = null;
			List<Long> allocationIds = new LinkedList<>();
			Integer screenId = null;
			Integer unitId = null;
			Integer storeId = null;
			Integer userId = null;
			if (pathVariables.containsKey("allocaltionId") && pathVariables.get("allocaltionId")!=null) {
				List<String> listStrIds = Arrays.asList(pathVariables.get("allocaltionId").split(","));
				
				for(String s : listStrIds)
				{
					//Long.parseLong(s)
					Long id=Long.parseLong(s);
					allocationIds.add(id);	
				}
				
				//listStrIds.stream().forEach(i -> allocationIds.add(Integer.parseInt(i)));
			}

			if (pathVariables.containsKey("tempBillId")) {
				tempBillId = Long.parseLong(pathVariables.get("tempBillId"));
			}

			if (pathVariables.containsKey("screenId")) {
				screenId = Integer.parseInt(pathVariables.get("screenId"));
			}
			if (pathVariables.containsKey("unitId")) {
				unitId = Integer.parseInt(pathVariables.get("unitId"));
			}
			if (pathVariables.containsKey("storeId")) {
				storeId = Integer.parseInt(pathVariables.get("storeId"));
			}
			if (pathVariables.containsKey("userId")) {
				userId = Integer.parseInt(pathVariables.get("userId"));
			}
			return new ResponseEntity<Object>(WalkInSalesService.deleteAllocationNew(screenId, tempBillId, allocationIds, unitId, storeId, userId),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	@ApiOperation(value = "Save walk-in-sale bill", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public synchronized ResponseEntity<Object> saveBill(@RequestBody WalkInSalesRequest request) throws Exception {
		try {
			
			if(request.getPrescriptionId() != null) {
				
				CommonResponse test = WalkInSalesService.getStatus(request.getPrescriptionId());
				Integer Ids = test.getId().intValue();
				if(Ids == com.param.common.constants.Status.Completed.getStatus() || Ids == com.param.common.constants.Status.Closed.getStatus()) {
					CommonResponse response = new CommonResponse();
					response.setMessage("This transaction already processed by other user");
					return new ResponseEntity<Object>(response,HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
			
			
			
			CommonResponse walkinResponse = WalkInSalesService.saveBillComplete(request);
			if(request.isAddToBill()==true && walkinResponse.getId()!=null && walkinResponse.getIds()!=null && walkinResponse.getIds().size()>0)
				globalBillingService.postServicesIntoExistingBill(walkinResponse.getIds());

			CommonResponse finalResponse = new CommonResponse<>();
				finalResponse.setId(walkinResponse.getId());
				finalResponse.setListObject(walkinResponse.getIds());
				finalResponse.setMessage(walkinResponse.getMessage());
			return new ResponseEntity<Object>(finalResponse, HttpStatus.OK);
		} 
		catch(com.param.common.dto.CustomException ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
		/*catch (IllegalArgumentException ex) {
			return new ResponseEntity<Object>(new ErrorResponse("HIS-1004", ex.getMessage(), ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}*/
		catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/save/prescription", method = RequestMethod.POST)
	@ApiOperation(value = "Save walk-in-sale prescription", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> saveWalkInPrescription(@RequestBody WalkInPrescriptionReq request) throws Exception {
		try {
			return new ResponseEntity<Object>(WalkInSalesService.saveWalkInPrescription(request), HttpStatus.OK);
		} 
		catch (IllegalArgumentException ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
		catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/batch/allocate/{screenId}/{unitId}/{orgId}/{storeId}/{tempBillId}", method = RequestMethod.PUT)
	@ApiOperation(value = "Allocate batch and get item details", response = ItemsDetailsListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> batchAllocate(@PathVariable Map<String, String> pathVariables,
			@RequestBody ItemRequest itemRequest) throws Exception {
		try {
			
			WalkInSalesRequest request = new WalkInSalesRequest(Integer.parseInt(pathVariables.get("screenId")),
					Long.parseLong(pathVariables.get("tempBillId")), itemRequest);
			request.setOrganizationId(Integer.parseInt(pathVariables.get("orgId")));
			request.setUnitId(Integer.parseInt(pathVariables.get("unitId")));
			request.setStoreId(Integer.parseInt(pathVariables.get("storeId")));
			return new ResponseEntity<Object>(WalkInSalesService.allocateBatch(request), HttpStatus.OK);
		}
		catch (CustomException ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
		catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}


	@RequestMapping(value = "/batch/allocate/{screenId}/{unitId}/{orgId}/{userId}/{storeId}/{entitlementId}/{tempBillId}", method = RequestMethod.PUT)
	@ApiOperation(value = "Allocate batch and get item details", response = ItemsDetailsListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> batchAllocateWithTariff(@PathVariable Map<String, String> pathVariables,
			@RequestBody ItemRequest itemRequest) throws Exception {
		try {

			WalkInSalesRequest request = new WalkInSalesRequest(Integer.parseInt(pathVariables.get("screenId")),
					Long.parseLong(pathVariables.get("tempBillId")), itemRequest);
			request.setEntitlementId(Integer.parseInt(pathVariables.get("entitlementId")));
			request.setOrganizationId(Integer.parseInt(pathVariables.get("orgId")));
			request.setUserId(Integer.parseInt(pathVariables.get("userId")));
			request.setUnitId(Integer.parseInt(pathVariables.get("unitId")));
			request.setStoreId(Integer.parseInt(pathVariables.get("storeId")));
			request.setBarcodeScanned(itemRequest.getBarcodeScanned());
			return new ResponseEntity<Object>(WalkInSalesService.allocateBatchWithTariff(request), HttpStatus.OK);
		} catch (CustomException ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	/** @author Ganesh **/
	@RequestMapping(value = "/saveAndValidateContract", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveAndValidateContract(@RequestBody ParamacyContractReqDto dto) {
		try {
			return WalkInSalesService.consumeItemFromContract(dto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	/** @author Ganesh **/
	@RequestMapping(value = "/validatePendingIssue", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public synchronized Response validatePendingIssue(@RequestBody PendingIssuesSave pendingIssuesSave) {
		try {
			return WalkInSalesService.consumeItemFromContractBillablePendingIssue(pendingIssuesSave,
					pendingIssuesSave.getAdmissionId(), pendingIssuesSave.getEncounterId());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/removeContractItems", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response removeContractItems(@RequestBody CommonBillingItemsDto itemRequest) {
		try {
			return WalkInSalesService.removeContractItems(itemRequest);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/savePatintAndSponsor", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response savePatintAndSponsor(@RequestBody ParamacyContractReqDto dto) {
		try {
			WalkInSalesRequest request = dto.getRequest();
			if (request.getPatientId() == null)// for walkin corporate/contract
			{
				Response res = WalkInSalesService.savePatient(request);
				if (res.getStatus().equals(SUCCESS)) {
					PatientRegistrationDto prDto = (PatientRegistrationDto) res.getObject();
					dto.setPatientId(prDto.getPatientId());

					if (dto.getPatientSponsorDetailsDtosList() != null && dto.getPatientSponsorDetailsDtosList().size() > 0) 
					{
						dto.getPatientSponsorDetailsDtosList().forEach(psd -> psd.setPatientId(prDto.getPatientId()));
						
						for (PatientSponsorDetailsDto pspdto : dto.getPatientSponsorDetailsDtosList()) 
						{
							pspdto.setPatientId(prDto.getPatientId());
							pspdto.setEncounterId(pspdto.getEncounterId()!=null && pspdto.getEncounterId()>0?pspdto.getEncounterId():null);
							pspdto.setApplicableDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-MM-yyyy HH:mm:ss").substring(0,10).concat(" 00:00:00"));
						}
						iPatientSponsorDetailsService
								.savePatientSponsorDetailsPharamcy(dto.getPatientSponsorDetailsDtosList(), null, null,dto.getSelfPayeeId());
					}
					/*
					 * Response consumeItemRes = WalkInSalesService.consumeItemFromContract(dto);
					 * if(consumeItemRes.getStatus().equals(SUCCESS)) {
					 * consumeItemRes.setObject(prDto); return consumeItemRes; }
					 */
					return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, prDto);
				} else {
					return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
				}

			} else {
				if (dto.getPatientSponsorDetailsDtosList() != null
						&& dto.getPatientSponsorDetailsDtosList().size() > 0) {
					Integer admissionId = dto.getAdmissionId();
					Integer encounterId = dto.getEncounterId();
					
					dto.getPatientSponsorDetailsDtosList().forEach(psd -> psd.setApplicableDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-MM-yyyy HH:mm:ss").substring(0,10).concat(" 00:00:00")));
					
					iPatientSponsorDetailsService.savePatientSponsorDetailsPharamcy(
							dto.getPatientSponsorDetailsDtosList(), encounterId, admissionId,dto.getSelfPayeeId());
				}
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, dto);
//				return WalkInSalesService.consumeItemFromContract(dto);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = { "/revertQty/{unitId}/{saleId}/{screenId}/{cancelledRemark}/{cancelledReasonId}/{userId}" }, method = RequestMethod.PUT)
	@ApiOperation(value = "Allocate item quantity", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> revertAllocatedQty(@PathVariable Integer unitId,@PathVariable("saleId") Long saleId,
			@PathVariable("screenId") Integer screenId,@PathVariable("cancelledRemark") String cancelledRemark,@PathVariable("cancelledReasonId") Integer cancelledReasonId
			,@PathVariable("userId") Integer userId) throws Exception {
		try {
			WalkInSalesService.revertAllocatedQty(saleId, screenId,unitId,cancelledRemark,cancelledReasonId,userId);
			return new ResponseEntity<Object>(HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = { "/items/alldata/{unitId}/{orgId}/{itemid}/{storeId}" }, method = RequestMethod.GET)
	@ApiOperation(value = "Delete allocated item quantity", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> allDataList( @PathVariable Integer itemid , @PathVariable Integer storeId,@PathVariable Integer unitId ,@PathVariable Integer orgId  ) throws Exception {
		try {
			return new ResponseEntity<Object>(WalkInSalesService.allDataList( itemid , storeId,orgId,unitId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = { "/items/allexpiredstockdata/{unitId}/{itemid}/{storeId}" }, method = RequestMethod.GET)
	@ApiOperation(value = "Delete allocated item quantity", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> allExpiredDataList( @PathVariable Integer itemid , @PathVariable Integer storeId ,@PathVariable Integer unitId) throws Exception {
		try {
			return new ResponseEntity<Object>(WalkInSalesService.allExpiredDataList( itemid , storeId,unitId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = { "/items/availableqty/{unitId}/{storeid}/{itemid}/{batchNo}/{uontypeid}" }, method = RequestMethod.GET)
	@ApiOperation(value = "Delete allocated item quantity", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> availableQty(@PathVariable Integer unitId, @PathVariable Integer storeid , @PathVariable Integer itemid ,  @PathVariable Integer batchNo , @PathVariable Integer uontypeid) throws Exception {
		try {
			return new ResponseEntity<Object>(WalkInSalesService.availableQty(storeid, itemid, batchNo , uontypeid,unitId),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	/** @author Ganesh **/
	@RequestMapping(value = "/consumeItemPackageContract", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response consumeItemPackageContract(@RequestBody PharmacyBillCommonDto dto) {
		try {
			
			Response res = iMPackageMasterService.consumeItemFromPackageContract(dto,0);
			
			if(res.getListObject()!= null && res.getListObject().size()>0) {
				
				List<CommonBillingItemsDto> items = res.getListObject();
				Map<Integer, Double> packageIdsMap = new HashMap<>();
				for (CommonBillingItemsDto commonBillingItemsDto : items) {
					
					if(commonBillingItemsDto.getSaleDetailPackageMapperDtoList()!= null) {
						for (SaleDetailPackageMapperDto SaleDetailPackageDto : commonBillingItemsDto.getSaleDetailPackageMapperDtoList()) {
						
							if(SaleDetailPackageDto.getPackageTypeId()!= null && SaleDetailPackageDto.getPackageTypeId() == PACKAGE4) {
								
								if(packageIdsMap.containsKey(SaleDetailPackageDto.getPackageId())) {
									
									Double amount = packageIdsMap.get(SaleDetailPackageDto.getPackageId());
									amount = amount + SaleDetailPackageDto.getPackageAmount();
									packageIdsMap.put(SaleDetailPackageDto.getPackageId(), amount);
									
								}else {
									packageIdsMap.put(SaleDetailPackageDto.getPackageId(), SaleDetailPackageDto.getPackageAmount());
								}
							
							}
						}
					}
				}
				
				UnitApplicationConfigurationDto configDto = new UnitApplicationConfigurationDto();
	            configDto.setOrganizationId(dto.getOrganizationId());
	            configDto.setUnitId(dto.getUnitId());
	            com.param.global.common.Response configRes = iUnitApplicationBillingConfigurationService.getUnitApplicationBillingConfigByOrgUnit(configDto);
	            if (configRes.getObject() != null) {
	                configDto = (UnitApplicationConfigurationDto) configRes.getObject();
	            }
	            
	            for (Map.Entry<Integer, Double> entry : packageIdsMap.entrySet()) {
	            	
	            	 BillingMasterDto bdto = new BillingMasterDto();
					  bdto.setAdmissionId(dto.getAdmissionId()!= null ?dto.getAdmissionId() :null ); 
					  bdto.setEncounterId(dto.getEncounterId()!= null ?dto.getEncounterId():null );
					  bdto.setPackageId( entry.getKey());
					  bdto.setPharmacyServiceId(configDto.getPharmacyServiceId());
					  Response respackageAmount =   iMPackageMasterService.getAllInclusivePackageAmount(bdto);
					  
					  if(respackageAmount.getObject()!= null) {
						  Double amount = entry.getValue();
						  OrderDetailsPackageMapperDto respackageAmountDto = (OrderDetailsPackageMapperDto) respackageAmount.getObject();
						  Response respackageConsumeAmount = iMPackageMasterService.getAllInclusivePackageConsumeAmount(bdto);
						  
						  if(respackageConsumeAmount.getObject()!= null) {
							  OrderDetailsPackageMapperDto respackageConsumeAmountDto = (OrderDetailsPackageMapperDto) respackageConsumeAmount.getObject();
							  amount = amount + respackageConsumeAmountDto.getPackageAmount().doubleValue();
						  }
						  
						  Double packServiceAmount = respackageAmountDto.getServiceAmount().doubleValue();
						  
						  if(amount>packServiceAmount) {
							  Double diffAmount = amount-packServiceAmount;
							  res.setResObject(diffAmount);
						  }
					  }
					 
	        	}
				 
			}
			
		
			return res;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}


	@RequestMapping(value = "/terrifData", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getTerrifData(@RequestBody PharmacyBillCommonDto dto) {
		try {

			return WalkInSalesService.getTerrifData(dto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = { "/prefix/{storeid}/{unitId}/{screenId}" }, method = RequestMethod.GET)
	@ApiOperation(value = "Delete allocated item quantity", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getDefaultPrefix( @PathVariable Integer storeid , @PathVariable Integer unitId, 
			@PathVariable Integer screenId) throws Exception {
		try {
			return new ResponseEntity<Object>(WalkInSalesService.getPrefixNumber(storeid, unitId, screenId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	

	@RequestMapping(value = "/revertstock/{orgId}/{unitId}", method = RequestMethod.GET)
	@ApiOperation(value = " ", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> revertStock(@PathVariable Integer orgId , @PathVariable Integer unitId) throws Exception {
		try {
			
			 
				
			return new ResponseEntity<Object>(WalkInSalesService.revertStock(orgId,unitId), HttpStatus.OK);
		} 
		 
		catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateSaleId", method = RequestMethod.POST)
	@ApiOperation(value = "Save walk-in-sale bill", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public synchronized ResponseEntity<Object> updateBill(@RequestBody WalkInSalesRequest request) throws Exception {
		try {
			CommonResponse walkinResponse = WalkInSalesService.updateBill(request);
			
			return new ResponseEntity<Object>(walkinResponse, HttpStatus.OK);
		} 
		
		catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
}
