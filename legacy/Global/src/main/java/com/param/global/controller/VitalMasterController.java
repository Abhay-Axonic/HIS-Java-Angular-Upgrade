package com.param.global.controller;

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

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.SurgeryMasterDto;
import com.param.global.dto.VitalMasterDto;
import com.param.global.dto.VitalSearchDto;
import com.param.global.service.IVitalMasterService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
/*FUNCTIONALITY : VITAL MASTER
DATE:16/04/2018
CREATED BY: JYOTI*/

@RestController
@RequestMapping("api/global")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class VitalMasterController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(VitalMasterController.class);


	@Autowired
	private IVitalMasterService iVitalMasterService;
	
	
	@RequestMapping(value="/getVitalMasterList", method=RequestMethod.GET)
	public @ResponseBody Response listOfVitalMasterList(@RequestHeader(value="organization_id") int organizationId) {
		try
		{
			VitalMasterDto vitalMasterDto= new VitalMasterDto();
//			vitalMasterDto.setUnitId(unitId);
			vitalMasterDto.setOrganizationId(organizationId);
			return iVitalMasterService.getVitalMasterList(vitalMasterDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value = "/vitals/{orgId}/{unitId}" , method = RequestMethod.GET)
	public Response getVitalsForNursing(@PathVariable Map<String,String> params){
		try{
			int orgId = Integer.parseInt(params.get("orgId").trim());
			int unitId = Integer.parseInt(params.get("unitId").trim());
			return iVitalMasterService.getVitalsForNursing(orgId,unitId);
		}catch(Exception e){
		logger.error("getVitalsForNursing failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	
}
