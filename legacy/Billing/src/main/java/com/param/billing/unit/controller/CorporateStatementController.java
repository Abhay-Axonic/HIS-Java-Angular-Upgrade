package com.param.billing.unit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.billing.common.ICommonConstants;
import com.param.billing.common.Response;
import com.param.billing.unit.dao.ICorporateStatementDao;
import com.param.billing.unit.service.ICorporateStatementService;
import com.param.global.dto.CorporateStatementDto;
import com.param.global.unit.dto.CabinMasterDto;

@RestController
@RequestMapping(value="/unit/CompanyStatment")
@SuppressWarnings({"rawtypes","unchecked"})
public class CorporateStatementController  implements ICommonConstants{
	
	@Autowired
	ICorporateStatementService iCorporateStatementService;
	
	@RequestMapping(value = "/saveCorporateStatementMasterDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveCorporateStatementMasterDetails(@RequestBody CorporateStatementDto corporateStatementDto) {
		try {
			return iCorporateStatementService.saveCorporateStatementMasterDetails(corporateStatementDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	@RequestMapping(value = "/updateCorporateStatementMasterDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateCorporateStatementMasterDetails(@RequestBody CorporateStatementDto corporateStatementDto) {
		try {
			return iCorporateStatementService.updateCorporateStatementMasterDetails(corporateStatementDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getCorporateStatementMasterById/{billId}/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getCabinMasterById(@PathVariable ("billId") Integer billId,
									@PathVariable ("organizationId") Integer organizationId,
									@PathVariable ("unitId") Integer unitId){
		
		try {
			return iCorporateStatementService.getCorporateStatementMasterById(billId, organizationId, unitId);
			
		} catch (Exception e) {
			
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/checkAlreadyExit", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response checkAlreadyExit(@RequestBody CorporateStatementDto corporateStatementDto){

		try {
			return iCorporateStatementService.checkAlreadyExit(corporateStatementDto);

		} catch (Exception e) {
			
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

}
