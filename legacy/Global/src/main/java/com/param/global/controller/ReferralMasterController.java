package com.param.global.controller;

import com.param.global.dto.PrefixMasterDto;
import com.param.global.utility.UserComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.AreaMasterDto;
import com.param.global.dto.ReferralMasterDto;
import com.param.global.service.IReferralMasterService;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Controller
@RequestMapping("/adt")
@SuppressWarnings({"rawtypes","unchecked"})
public class ReferralMasterController implements ICommonConstants 
{
	final static Logger logger = Logger.getLogger(ReferralMasterController.class);

	@Autowired
	private IReferralMasterService iReferralMasterService;
	
	@RequestMapping(value = "/saveReferralMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveReferralMaster(@RequestBody ReferralMasterDto referralMasterDto) {
		try {

			return iReferralMasterService.saveReferralMaster(referralMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getReferralMasterList",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getReferralMasterList(@RequestBody ReferralMasterDto referralMasterDto) {
		try {
			return iReferralMasterService.getReferralMasterList(referralMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/updateReferralMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateReferralMaster(@RequestBody ReferralMasterDto referralMasterDto) {
		try {
			return iReferralMasterService.updateReferralMaster(referralMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getReferralById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getReferralById(@RequestBody ReferralMasterDto referralMasterDto) {
		try {

			return iReferralMasterService.getReferralById(referralMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/updateStatusForReferral", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateStatusForReferral(@RequestBody ReferralMasterDto referralMasterDto) {
		try {

			return iReferralMasterService.updateStatusForReferral(referralMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getActiveReferralList", method = RequestMethod.GET)
	public @ResponseBody Response getActiveReferralList() {
		try {
			return iReferralMasterService.getActiveReferralList();
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getAreaByCityId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAreaByCityId(@RequestBody AreaMasterDto areaMasterDto) {
		try {

			return  iReferralMasterService.getAreaByCityId(areaMasterDto);

		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getReferralCount",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getReferralCount(@RequestBody ReferralMasterDto referralMasterDto) {
		try {
			return iReferralMasterService.getReferralCount(referralMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getReferralListByReferralTypeId",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getReferralListByReferralTypeId(@RequestBody ReferralMasterDto referralMasterDto) {
		try {
			return iReferralMasterService.getReferralListByReferralTypeId(referralMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getReferralListByReferralTypeIds",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getReferralListByReferralTypeIds(@RequestBody ReferralMasterDto referralMasterDto) {
		try {
			return iReferralMasterService.getReferralListByReferralTypeIds(referralMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getReferralListByReferralTypeId/forTypeSearch",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getReferralListForTypeSearch(@RequestBody ReferralMasterDto referralMasterDto) {
		try {
			return iReferralMasterService.getReferralListForTypeSearch(referralMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getAreaByCityIdPagination", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAreaByCityIdPagination(@RequestBody AreaMasterDto areaMasterDto) {
		try {

			return iReferralMasterService.getAreaByCityIdPagination(areaMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/search/getReferralListByReferralName",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getReferralListByReferralName(@RequestBody ReferralMasterDto referralMasterDto) {
		try {
			return iReferralMasterService.getReferralListByReferralName(referralMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	
	@RequestMapping(value = "/getReferralListByOrgId",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getReferralListByOrgId(@RequestBody ReferralMasterDto referralMasterDto) {
		try {
			return iReferralMasterService.getReferralListByOrgId(referralMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
}
