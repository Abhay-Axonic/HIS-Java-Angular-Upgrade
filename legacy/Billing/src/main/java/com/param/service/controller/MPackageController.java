package com.param.service.controller;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.param.billing.dto.InvestigationOrderDto;
import com.param.global.dto.*;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.param.billing.common.ICommonConstants;
import com.param.global.billing.service.IContractConsumptionService;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.service.IMPackageMasterService;
/**
 * Controller for package creation
 * @author Ganesh Chaudhari
 *
 */
@RestController
@RequestMapping(value = "/api/packages")
@SuppressWarnings({"rawtypes"})
public class MPackageController implements ICommonConstants {

	@Autowired
	private IMPackageMasterService iMPackageMasterService;
	
	@Autowired
	private IContractConsumptionService iContractConsumptionService;
	
	
	@RequestMapping(value="/service/exists" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response checkServiceAvailableInPackageContract(@RequestBody ContractConsumptionDto contractConsumptionDto)
	{
		try{
			return  iMPackageMasterService.checkServiceAvailableInPackageContract(contractConsumptionDto);
			
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	
	
	/**
	 * THIS IS METHOD FOR SAVE EHC PACKAGE
	 * @param reqDto
	 * @return
	 */

	@RequestMapping(value = "/ehc" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response createEhcPackage(@RequestBody ParentPackageMasterDto parentPackageMasterDto){
		try{
		return iMPackageMasterService.createEhcPackage(parentPackageMasterDto);
		}catch(ServiceException se){
			throw se;
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}	
	}
	
	/**
	 * METHOD TO CREATE ALL INCLUSISVE PACKAGE 
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = "/allInclusive" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveAllInclusivePackage(@RequestBody ParentPackageMasterDto parentPackageMasterDto){
		try{
			return iMPackageMasterService.SaveAllInclusivePackage(parentPackageMasterDto);
		}catch(ServiceException se){
			throw se;
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	/**
	 * METHOD TO CREATE MULTIENCOUNTER PACKAGE
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = "/multiencounter" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response createMultiEncounterPackage(@RequestBody ParentPackageMasterDto packageDto){
		try{
			return iMPackageMasterService.createMultiencouterPackage(packageDto);
		}catch(ServiceException se){
			throw se;
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * THIS IS METHOD FOR CREATE EIGHTER OR PACKAGE CREATION
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = "/eitheror" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response createEitherOrPackage(@RequestBody ParentPackageMasterDto packageDto){
		try{
			
			return iMPackageMasterService.createEitherOrPackage(packageDto);
		}catch(ServiceException se){
			throw se;
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getPackagesByPackageId" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getPackgeByIds(@RequestBody Map<Integer,Integer> packageIds){
		
		
   try{
			
	   PackaageResDto resDto = new PackaageResDto();
	   Response parentPackageListRes =  iMPackageMasterService.getPackgeByIds(packageIds);

	   Set<MultiSelectObj> sBedCatList = new HashSet<>();
	   Set<MultiSelectObj> sVisitTList = new HashSet<>();
	   Set<MultiSelectObj> sPatientTypeList = new HashSet<>();
	   Set<MultiSelectObj> sPayementEList = new HashSet<>();

			List<ParentPackageMasterDto> parentList = parentPackageListRes.getListObject();
			if(parentList!=null && parentList.size()>0)
			{
				
				for (Iterator iterator = parentList.iterator(); iterator.hasNext();) {
					ParentPackageMasterDto parentPackage = (ParentPackageMasterDto) iterator.next();
					resDto.setParentPackageMasterDto(parentPackage);
					Map<Integer, Map<Integer, Map<Integer, List<MPackageMasterDto>>>> map = parentPackage.getPackageMasterDtoList().stream()
			        	    .collect(Collectors.groupingBy(MPackageMasterDto::getPaymentEntitlementTypeId,Collectors.groupingBy(MPackageMasterDto::getPatientTypeId,Collectors.groupingBy(MPackageMasterDto::getBillingBedCategoryId))));
				  
				  List<PackagePaymentEntitlement> packagePaymentEntitlementList = new ArrayList<PackagePaymentEntitlement>();
				    for (Entry<Integer, Map<Integer, Map<Integer, List<MPackageMasterDto>>>> entry : map.entrySet()) //paymentEntitlement
			        {
				    	PackagePaymentEntitlement packagePaymentEntitlement = new PackagePaymentEntitlement();
				    	List<PackagePatientCategory> packagePatientCategoryList = new ArrayList<PackagePatientCategory>();
					    for (Entry<Integer, Map<Integer, List<MPackageMasterDto>>> entry1 :  entry.getValue().entrySet()) //patientType
				        {
					    	
					    	PackagePatientCategory packagePatientCategory = new PackagePatientCategory();
					    	 List<PackageBillingBedCategory> packageBillingBedCategoryList = new ArrayList<PackageBillingBedCategory>();
					    	 for (Entry<Integer, List<MPackageMasterDto>> entry2 :  entry1.getValue().entrySet()) //BillingBedCategory
						        {
					    		  PackageBillingBedCategory packageBillingBedCategory = new PackageBillingBedCategory();
					    		  for (MPackageMasterDto dto: entry2.getValue()) //BillingBedCategory
							        {
					    			 	  packagePaymentEntitlement.setPaymentEntitlementId(dto.getPaymentEntitlementTypeId());
					    			      packagePaymentEntitlement.setPaymentEntitlement(dto.getPaymentEntitlement());
					    			 
					    			      packagePatientCategory.setPatientType(dto.getPatientType());    
					    			      packagePatientCategory.setPatientTypeId(dto.getPatientTypeId());
					    			      
					    			    
					    			      packageBillingBedCategory.setBillingBedCategory(dto.getBillingBedCategory());
					    			      packageBillingBedCategory.setBillingBedCategoryId(dto.getBillingBedCategoryId());
					    			      
						    		      packageBillingBedCategory.setmPackageMasterDto(dto);
						    		      packageBillingBedCategoryList.add(packageBillingBedCategory);

										  sBedCatList.add(new MultiSelectObj(dto.getBillingBedCategoryId(),dto.getBillingBedCategory()));
										  sPatientTypeList.add(new MultiSelectObj(dto.getPatientTypeId(),dto.getPatientType()));
										  sVisitTList.add(new MultiSelectObj(dto.getVisitTypeId(),dto.getVisitTypeName()));
										  sPayementEList.add(new MultiSelectObj(dto.getPaymentEntitlementTypeId(),dto.getPaymentEntitlement()));
							        }
						        }
					    	 packagePatientCategory.setBillingBedCategoryList(packageBillingBedCategoryList);
					    	 packagePatientCategoryList.add(packagePatientCategory);

						}
					    packagePaymentEntitlement.setPatientCategoryList(packagePatientCategoryList);
					    packagePaymentEntitlementList.add(packagePaymentEntitlement);
			        }    
				    resDto.setPaymentEntitlement(packagePaymentEntitlementList);
					resDto.setSelectedBedBillingCategoryList(sBedCatList);
					resDto.setSelectedPaymentEntitlementList(sPayementEList);
					resDto.setSelectedVisitTypeList(sVisitTList);
					resDto.setSelectedPatientTypeList(sPatientTypeList);
				    return  new Response<>(SUCCESS, SUCCESS_CODE, null, null, resDto);
			    }
			  }
			return  new Response<>(ERROR, ERROR_CODE, null, null, null);	
		}catch(ServiceException se){
			throw se;
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	/**
	 * THIS IS METHOD FOR SEARCH PACKAGE SERVICES 
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = "/services/search" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response searchServiceForPackages(@RequestBody ServiceForPackageReqDto reqDto){
		try{
			return iMPackageMasterService.searchServiceForPackage(reqDto);
		}catch(ServiceException se){
			throw se;
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/package/service/search" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response searchServiceForPackageByMultipleCategory(@RequestBody PackageMultyServiceSearchReqDto reqDto){
		try{
			
			PackaageResDto resDto = new PackaageResDto();
			if(reqDto.getServiceForPackageReqList()!=null && reqDto.getServiceForPackageReqList().size()>0)
			{
				  Map<Integer, Map<Integer, Map<Integer, List<ServiceForPackageReqDto>>>> map = reqDto.getServiceForPackageReqList().stream()
			        	    .collect(Collectors.groupingBy(ServiceForPackageReqDto::getPaymentEntitlementId,Collectors.groupingBy(ServiceForPackageReqDto::getPatientTypeId,Collectors.groupingBy(ServiceForPackageReqDto::getBillingBedCategoryId))));
				  
				  List<PackagePaymentEntitlement> packagePaymentEntitlementList = new ArrayList<PackagePaymentEntitlement>();
				    for (Entry<Integer, Map<Integer, Map<Integer, List<ServiceForPackageReqDto>>>> entry : map.entrySet()) //paymentEntitlement
			        {
				    	PackagePaymentEntitlement packagePaymentEntitlement = new PackagePaymentEntitlement();
				    	List<PackagePatientCategory> packagePatientCategoryList = new ArrayList<PackagePatientCategory>();
					    for (Entry<Integer, Map<Integer, List<ServiceForPackageReqDto>>> entry1 :  entry.getValue().entrySet()) //patientType
				        {
					    	
					    	PackagePatientCategory packagePatientCategory = new PackagePatientCategory();
					    	 List<PackageBillingBedCategory> packageBillingBedCategoryList = new ArrayList<PackageBillingBedCategory>();
					    	 for (Entry<Integer, List<ServiceForPackageReqDto>> entry2 :  entry1.getValue().entrySet()) //BillingBedCategory
						        {
					    		  PackageBillingBedCategory packageBillingBedCategory = new PackageBillingBedCategory();
					    		  for (ServiceForPackageReqDto dto: entry2.getValue()) //BillingBedCategory
							        {
					    			 	  packagePaymentEntitlement.setPaymentEntitlementId(dto.getPaymentEntitlementId());
					    			      packagePaymentEntitlement.setPaymentEntitlement(dto.getPaymentEntitlement());
					    			 
					    			      packagePatientCategory.setPatientType(dto.getPatientType());    
					    			      packagePatientCategory.setPatientTypeId(dto.getPatientTypeId());
					    			      
					    			    
					    			      packageBillingBedCategory.setBillingBedCategory(dto.getBillingBedCategory());
					    			      packageBillingBedCategory.setBillingBedCategoryId(dto.getBillingBedCategoryId());
					    			      
						    		      Response packageRes =   iMPackageMasterService.searchServiceForPackageByMultipleCategory(dto, reqDto);
						    		      MPackageMasterDto  packageDto=  (MPackageMasterDto)packageRes.getObject();
						    		      packageBillingBedCategory.setmPackageMasterDto(packageDto);
						    		      packageBillingBedCategoryList.add(packageBillingBedCategory);
							        }
						        }
					    	 packagePatientCategory.setBillingBedCategoryList(packageBillingBedCategoryList);
					    	 packagePatientCategoryList.add(packagePatientCategory);
						}
					    packagePaymentEntitlement.setPatientCategoryList(packagePatientCategoryList);
					    packagePaymentEntitlementList.add(packagePaymentEntitlement);
			        }    
				    resDto.setPaymentEntitlement(packagePaymentEntitlementList);
				    return  new Response<>(SUCCESS, SUCCESS_CODE, null, null, resDto);
			 }
			return  new Response<>(ERROR, ERROR_CODE, null, null, null);	
			     
		}catch(ServiceException se){
			throw se;
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	/**
	 * THIS IS METHOD FOR SEARCH ORDERSET SERVICES 
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = "/services/orderSet/search" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response searchServiceForOrderSet(@RequestBody ServiceForPackageReqDto reqDto){
		try{
			return iMPackageMasterService.searchServiceForOrderSet(reqDto);
		}catch(ServiceException se){
			throw se;
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	

	
	/**
	 * METHOD TO GET EHC PACKAGE LIST
	 * @deprecated
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = "/ehcPackages" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getEHCPackagesList(@RequestBody ServiceForPackageReqDto reqDto){
		try{
			return iMPackageMasterService.getEHCPackagesList(reqDto);
		}catch(ServiceException se){
			throw se;
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * METHOD IS FOR CREATE PACKAGE WITH CAP CREATION
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = "/packagewithcap" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response createPackageWithCap(@RequestBody ParentPackageMasterDto packageDto){
		try{
			return iMPackageMasterService.createPackageWithCap(packageDto);
		}catch(ServiceException se){
			throw se;
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	

	

	/*
	 * @RequestMapping(value = "/getAllInclusive" , method = RequestMethod.POST ,
	 * consumes = MediaType.APPLICATION_JSON_VALUE) public Response
	 * getAllInclusivePackage(@RequestBody ParentPackageMasterDto
	 * parentPackageMasterDto){ try{ return
	 * iMPackageMasterService.getAllInclusivePackage(parentPackageMasterDto);
	 * }catch(ServiceException se){ s throw se; }catch(Exception
	 * e){  throw new ServiceException(e.getMessage()); } }
	 */
	
	
	/**
	 * METHOD TO SEARCH SERVICES TO ADD INTO PACKAGE
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = "/bill/services/search" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response serviceSearchForBilling(@RequestBody ServiceSearchReqDto reqDto){
		try{
			return iMPackageMasterService.searchServicesForBilling(reqDto);
		}catch(ServiceException se){
			throw se;
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
     /**@Author Ganesh : code for searching services for package*/
	@RequestMapping(value = "/searchServicesForPackage" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response searchServicesForPackage(@RequestBody ServiceForPackageReqDto reqDto){
		try{
			return iMPackageMasterService.searchServicesForPackage(reqDto);
		}catch(ServiceException se){
			throw se;
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	
	/**
	 * GET PACKAGE DETAILS BY PACKAGE ID ARRAY
	 * @param packageIds
	 * @return
	 */
	@RequestMapping(value = "/bill/package/{ids}" , method = RequestMethod.GET)
	public Response packageDetailsByIdForBill(@PathVariable("ids") String[] packageIds){
		try{
			List<String> listStrIds = Arrays.asList(packageIds);
			List<Integer> intPackageIds = new LinkedList<>();
			listStrIds.stream().forEach(i -> intPackageIds.add(Integer.parseInt(i)));
			return iMPackageMasterService.getPackageDetailsByIdForBilling(intPackageIds);
		}catch(ServiceException se){
			throw se;
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	 @RequestMapping(value = "/bill/package" , method = RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
		public Response packageDetailsByIdForBill(@RequestBody Map<Integer,Integer> consumtionIdPackageIds){
			try{
				return iMPackageMasterService.getPackageDetailsByIdForBillingTransaction(consumtionIdPackageIds);
			}catch(ServiceException se){
				throw se;
			}catch(Exception e){
				
				throw new ServiceException(e.getMessage());
			}
		}
	
	/**
	 * THIS METHOS IS FOR GET ACTIVE PACKAGES FOR PATIENT BY ENCOUNTER, VISIT TYPE
	 * @param patientId
	 * @param encounterId
	 * @param visitTypeId
	 * @param organisationId
	 * @param unitId
	 * @return
	 */
	@RequestMapping(value="/patientId/{patientId}/encounterId/{encounterId}/visitTypeId/{visitTypeId}",  method = RequestMethod.GET)
	public Response getExistingActivePackageByPatientEncounter(@PathVariable("patientId") Integer patientId,
			@PathVariable("encounterId") Integer encounterId, @PathVariable("visitTypeId") Integer visitTypeId,
			@RequestHeader("organisationId")Integer organisationId, @RequestHeader("unitId") Integer unitId,
			@RequestHeader("packageId")Integer packageId){
		try{
			return iMPackageMasterService.getExistingActivePackageByPatientEncounter(patientId, encounterId, visitTypeId,organisationId, unitId,packageId);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * CHECK SERVICES EXIST IN ACTIVE PACKAGE OR NOT
	 * @param reqDto
	 * @return
	 */
	/*
	 * @RequestMapping(value="/service/exists" , method = RequestMethod.POST ,
	 * consumes = MediaType.APPLICATION_JSON_VALUE) public Response
	 * checkServiceAvailableInPackage(@RequestBody PackageServiceCheckReqDto
	 * reqDto){ try{ return
	 * iMPackageMasterService.checkIfServiceFromPackage(reqDto); }catch(Exception
	 * e){  throw new ServiceException(e.getMessage()); } }
	 */
	
	/**
	 * GET PACKAGE LIST BY USING SEARCH FILTERS
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = "/filter" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getPackageListByFilter(@RequestBody PackageListReqDto reqDto){
		try{
			return iMPackageMasterService.getPackageListByFilter(reqDto);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	 
		@RequestMapping(value = "/getPackageLog/{packageConsumptionServiceDetailId}" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
		public Response getPackageLog(@PathVariable("packageConsumptionServiceDetailId") Integer packageConsumptionServiceDetailId){
			try{
				return iMPackageMasterService.getPackageLog(packageConsumptionServiceDetailId);
			}catch(Exception e){
				
				throw new ServiceException(e.getMessage());
			}
		}
		
		@RequestMapping(value = "/getPackageListForIPDPackageOrder/{patientId}/{admissionId}/{orgId}/{unitId}/{visitTypeId}" , method = RequestMethod.GET)
		public Response getPackageListForIPDPackageOrder(@PathVariable("patientId") Integer patientId,@PathVariable("admissionId") Integer admissionId, @PathVariable("orgId") Integer orgId, @PathVariable("unitId") Integer unitId,@PathVariable("visitTypeId") Integer visitTypeId){
			try{
				return iMPackageMasterService.getPackageListForIPDPackageOrder(patientId,admissionId, orgId, unitId,visitTypeId);
			}catch(Exception e){
				
				throw new ServiceException(e.getMessage());
			}
		}
	/**
	 * common search for getting package details
	 * @param searchDto
	 * @return
	 */
		@RequestMapping(value = "/searchPackageCommonFilter" , method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public Response searchPackageCommonFilter(@RequestBody PackageSearchRequestDto searchDto){
			try{
				return iMPackageMasterService.searchPackageCommonFilter(searchDto);
			}catch(Exception e){
				
				throw new ServiceException(e.getMessage());
			}
		}
		@RequestMapping(value = "/getPackageServicesByPackageId" , method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public Response getPackageServicesByPackageId(@RequestBody PackageSearchRequestDto searchDto){
			try{
				return iMPackageMasterService.getPackageServicesByPackageId(searchDto);
			}catch(Exception e){
				
				throw new ServiceException(e.getMessage());
			}
		}
		@RequestMapping(value = "/bill/package/search" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
		public Response packageSearchForBilling(@RequestBody ServiceSearchReqDto reqDto){
			try{
				return iMPackageMasterService.packageSearchForBilling(reqDto);
			}catch(ServiceException se){
				throw se;
			}catch(Exception e){
				
				throw new ServiceException(e.getMessage());
			}
		}
		
		@RequestMapping(value = "/bill/parentPackage/search" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
		public Response parentPackage(@RequestBody ServiceSearchReqDto reqDto){
			try{
				return iMPackageMasterService.parentPackage(reqDto);
			}catch(ServiceException se){
				throw se;
			}catch(Exception e){
				
				throw new ServiceException(e.getMessage());
			}
		}
		
		@RequestMapping(value = "/getPackageRate" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
		public Response parentPackage(@RequestBody ServiceForPackageReqDto reqDto){
			try{
				return iMPackageMasterService.getPackageRate(reqDto);
			}catch(ServiceException se){
				throw se;
			}catch(Exception e){
				
				throw new ServiceException(e.getMessage());
			}
		}
		
		
		@RequestMapping(value = "/getSerivceRateGlobal" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
		public Response getSerivceRateGlobal(@RequestBody ContractConsumptionDto contrcDto){
			try{
				return iContractConsumptionService.getSerivceRateGlobal(contrcDto, false, false);
			}catch(ServiceException se){
				throw se;
			}catch(Exception e){
				
				throw new ServiceException(e.getMessage());
			}
		}
		@RequestMapping(value = "/forceCancelPackage" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
		public Response forceCancelPackage(@RequestBody PackageConsumptionMasterResDto reqDto){
			try{
				return iMPackageMasterService.forceCancelPackage(reqDto);
			}catch(ServiceException se){
				throw se;
			}catch(Exception e){
				
				throw new ServiceException(e.getMessage());
			}
		}
		
		@RequestMapping(value = "/inActivePackage/parentPkgId/{parentPkgId}/PackageTypeId/{packageTypeId}/activeInactive/{flag}" , method = RequestMethod.GET)
		public Response inActivePackage(@PathVariable("parentPkgId") Integer parentPkgId,@PathVariable("packageTypeId") Integer packageTypeId,@PathVariable("flag") boolean flag){
			try{
			Response res = iMPackageMasterService.inactivePackage(parentPkgId,packageTypeId,flag);
			if(res.getStatus().equals(SUCCESS)) {
				iMPackageMasterService.inactiveParentPackage(parentPkgId,flag);
			}
			return res;
			}catch(ServiceException se){
				throw se;
			}catch(Exception e){
				
				throw new ServiceException(e.getMessage());
			}	
		}
		
		@RequestMapping(value = "/getPackageListForIPDPackageOrderForBookView/{patientId}/{fromDate}/{toDate}" , method = RequestMethod.GET)
		public Response getPackageListForIPDPackageOrderForBookView(@PathVariable("patientId") Integer patientId,
				@PathVariable("fromDate") String fromDate,
				@PathVariable("toDate") String toDate){
			try{
				return iMPackageMasterService.getPackageListForIPDPackageOrderForBookView(patientId,fromDate,toDate);
			}catch(Exception e){
				
				throw new ServiceException(e.getMessage());
			}
		}


	@RequestMapping(value = "/editPackage" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response editPackage(@RequestBody ParentPackageMasterDto packageDto){
		try{
		    Map<Integer,Integer> map = new HashMap();
			map.put(packageDto.getParentPackageId(),packageDto.getPackageTypeId());
			return iMPackageMasterService.editPackage(packageDto,map);
		}catch(ServiceException se){
			throw se;
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value="/getDataByDateFromMpackage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
	public @ResponseBody Response getDataForIPDCoversheetByDate(@RequestBody PackageConsumptionMasterResDto dto) {
		try
		{
			return iMPackageMasterService.getMPackageForIPDCoversheetByDate(dto);
		}catch(Exception e)
		{

		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getUniqueDateFromMPackage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
	public @ResponseBody Response getUniqueDateForIPDCoversheet(@RequestBody PackageConsumptionMasterResDto dto) {
		try
		{
			return iMPackageMasterService.getUniqueDateForIPDCoversheet(dto);
		}catch(Exception e)
		{

		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/getPackageListOnCreditNote/{patientId}/{admissionId}/{orgId}/{unitId}/{visitTypeId}" , method = RequestMethod.GET)
	public Response getPackageListOnCreditNote(@PathVariable("patientId") Integer patientId,@PathVariable("admissionId") Integer admissionId, @PathVariable("orgId") Integer orgId, @PathVariable("unitId") Integer unitId,@PathVariable("visitTypeId") Integer visitTypeId){
		try{
			return iMPackageMasterService.getPackageListOnCreditNote(patientId,admissionId, orgId, unitId,visitTypeId);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/packageListForWeb" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response packageListForWeb(@RequestBody PackageListReqDto reqDto) {
		try {
			return iMPackageMasterService.packageListForWeb(reqDto);
		} catch (Exception e) {

			throw new ServiceException(e.getMessage());
		}

	}

}
