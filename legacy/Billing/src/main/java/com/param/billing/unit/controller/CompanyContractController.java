package com.param.billing.unit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.billing.unit.dto.ChargeConversionDto;
import com.param.billing.unit.service.ICompanyContractService;
import com.param.global.billing.dto.TContractServiceDetailsDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.CompanyMasterDto;
import com.param.global.dto.ItemsForContractDto;
import com.param.service.dto.CompanyContractReqDto;
import com.param.service.dto.ContractServiceCheckReqDto;
import com.param.service.dto.ServiceForCompnayContarctReqDto;
import com.param.service.dto.TContractGroupDetailsDto;


@RestController
@RequestMapping(value="unit/CompanyContract")
@SuppressWarnings({"rawtypes","unchecked"})
public class CompanyContractController  implements ICommonConstants{
	@Autowired
	private ICompanyContractService iCompanyContractService;
	
	/**
	 * @author Suraj 
	 * get Company Master List
	 * @param companyMasterDto
	 * @return listCompanyMasterList
	 */
	@RequestMapping(value="/getCompanyMasterList" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getCompanyMasterList(@RequestBody CompanyMasterDto companyMasterDto){
		Response<CompanyMasterDto, Integer> response = null;
		try{
			 response= iCompanyContractService.getCompanyMasterList(companyMasterDto);
			return response;
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	
	/**
	 * @author Suraj
	 *  Get Associate Company Master List
	 * @param companyMasterDto
	 * @return listAssociateCompanyMasterList
	 */
	
	@RequestMapping(value="/getAssociateCompanyMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAssociateCompanyMaster(@RequestBody CompanyMasterDto companyMasterDto){
		try{
			return iCompanyContractService.getAssociateCompanyMaster(companyMasterDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	/**
	 * @author Suraj 
	 * Get Visit Type List
	 * @param companyMasterDto
	 * @return listVisitType
	 */
	@RequestMapping(value="/getVisitTypeList" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getVisitTypeList(@RequestBody CompanyMasterDto companyMasterDto){
		try{
			return iCompanyContractService.getVisitTypeList(companyMasterDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	/**
	 * TO GET GRADE LIST BY ASSOCIATE COMPANY
	 * @param companyMasterDto
	 * @return
	 */
	@RequestMapping(value="/getGradeListByAssociateCompanyId" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getGradeListByAssociateCompanyId(@RequestBody CompanyMasterDto companyMasterDto){
		try{
			return iCompanyContractService.getGradeListByAssociateCompanyId(companyMasterDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	/**
	 * @author Suraj
	 * Create  Company Contract 
	 * @param reqDto
	 * @return mCompanyContractMaster
	 */
	@RequestMapping(value = "/createCompanyContarct" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response createCompanyContarct(@RequestBody CompanyContractReqDto reqDto){
		try{
			
			return iCompanyContractService.createCompanyContarct(reqDto);
		}catch(ServiceException se){
			throw se;
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	/**
	 * @author Suraj
	 * Search Company Contract 
	 * @param reqDto
	 * @return listServiceForCompnayContarctResDto
	 */
	@RequestMapping(value="/searchCompanyContract" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response searchCompanyContract(@RequestBody ServiceForCompnayContarctReqDto reqDto){
		try{
			return iCompanyContractService.searchCompanyContract(reqDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	
	@RequestMapping(value="/CompanyContractListSearch" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response CompanyContractListSearch(@RequestBody ServiceForCompnayContarctReqDto reqDto){
		try{
			return iCompanyContractService.CompanyContractListSearch(reqDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	/**
	 * @author Suraj
	 * Get Contract By Company Id
	 * @param reqDto
	 * @return contractList
	 */
	@RequestMapping(value="/getContractByCompanyId" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getContractByCompanyId(@RequestBody CompanyMasterDto reqDto){
		try{
			return iCompanyContractService.getContractByCompanyId(reqDto);
		}catch(Exception e){
			 
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	
/**
 * GET SERVICES PRICE BY COMPANY ID 
 * @param reqDto
 * @return
 */
	@RequestMapping(value="/getServiceRateByCompanyId" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getServiceRateByCompanyId(@RequestBody CompanyMasterDto reqDto){
		try{
			return iCompanyContractService.getServiceRateByCompanyId(reqDto);
		}catch(Exception e){
			 
		}
		return new Response(ERROR, null, null, null,null);
	}
	
/**
 * TO CHECK IF SERVICE EXIST IN CONTRACT OR NOT
 * @param reqDto
 * @return
 */
	@RequestMapping(value="/checkIfServiceFromContarct" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response checkServiceAvailableInPackage(@RequestBody ContractServiceCheckReqDto reqDto){
		try{
			return iCompanyContractService.checkIfServiceFromContarct(reqDto);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * @author Suraj
	 * Get Charge Conversion List By CurrentPayer
	 * @param reqDto
	 * @return List
	 */
	@RequestMapping(value="/getChargeConversionListByCurrentPayer" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getChargeConversionListByCurrentPayer(@RequestBody ChargeConversionDto reqDto){
		try{
			return iCompanyContractService.getChargeConversionListByCurrentPayer(reqDto);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * @author Suraj
	 * Get Charge Conversion List By ConvertPayer
	 * @param reqDto
	 * @return List
	 */
	@RequestMapping(value="/getChargeConversionListByConvertPayer" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getChargeConversionListByConvertPayer(@RequestBody ChargeConversionDto reqDto){
		try{
			return iCompanyContractService.getChargeConversionListByConvertPayer(reqDto);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value="/getItemListByAssetAndProductCategory" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getItemListByAssetAndProductCategory(@RequestBody ItemsForContractDto itemsForContractDto){
		try{
			return iCompanyContractService.getItemListByAssetAndProductCategory(itemsForContractDto);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/getProductAssetInclusionList" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getProductAssetInclusionList(@RequestBody ItemsForContractDto itemsForContractDto){
		try{
			return iCompanyContractService.getProductAssetInclusionList(itemsForContractDto);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/getItemListByProductCategoryForEquipment" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getItemListByProductCategoryForEquipment(@RequestBody ItemsForContractDto itemsForContractDto){
		try{
			return iCompanyContractService.getItemListByProductCategoryForEquipment(itemsForContractDto);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value="/getCompanyContractDetailByContractId" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getCompanyContractDetailByContractId(@RequestBody CompanyContractReqDto companyContractReqDto){
		try{
			return iCompanyContractService.getCompanyContractDetailByContractId(companyContractReqDto);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contract/status/{status}/{contractId}" , method = RequestMethod.GET)
	public @ResponseBody Response updateStatus(@PathVariable("contractId") Integer contractId,@PathVariable("status") Character status){
		try{
			return iCompanyContractService.updateStatus(contractId,status);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/searchCompanyContractForVersion" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response searchCompanyContractForVersion(@RequestBody ServiceForCompnayContarctReqDto reqDto){
		try{
			return iCompanyContractService.searchCompanyContractForVersion(reqDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getContractDetailsBySubDept" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getContractDetailsBySubDept(@RequestBody TContractGroupDetailsDto reqDto){
		try{
			return iCompanyContractService.getContractDetailsBySubDept(reqDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/addServicesInContract" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response addServicesInContract(@RequestBody List<TContractServiceDetailsDto> masterDtoList){
		try{
			return iCompanyContractService.addServicesInContract(masterDtoList);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getAssociateCompanyMasterByMultipleCompanyIds" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAssociateCompanyMasterByMultipleCompanyIds(@RequestBody CompanyMasterDto companyMasterDto){
		try{
			return iCompanyContractService.getAssociateCompanyMasterByMultipleCompanyIds(companyMasterDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
}
