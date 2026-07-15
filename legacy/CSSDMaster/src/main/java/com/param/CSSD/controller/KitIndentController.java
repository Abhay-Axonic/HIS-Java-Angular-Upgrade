package com.param.CSSD.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.CSSD.dto.KitIndentDto;
import com.param.CSSD.dto.KitIndentKitMapperDto;
import com.param.CSSD.service.IKitIndentService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@RestController
@RequestMapping("/cssd")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class KitIndentController implements ICommonConstants {

	final static Logger logger = Logger.getLogger(KitIndentController.class);


	@Autowired
	private IKitIndentService iKitIndentService;

	
	/**
	 * This method is used to create kit indent.
	 * @param kitIndentDto
	 * @return Response
	 */
	@RequestMapping(value = "/saveKitIndent", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveKitIndent(@RequestBody KitIndentDto kitIndentDto) {
		try {
			return iKitIndentService.createKitIndent(kitIndentDto);
		} catch (Exception e) {
			logger.error("saveKitIndent failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * This method is used to update kit indent.
	 * @param kitIndentDto
	 * @return Response
	 */
	@RequestMapping(value = "/updateKitIndent", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateKitIndent(@RequestBody KitIndentDto kitIndentDto) {
		try {
			return iKitIndentService.updateKitIndent(kitIndentDto);
		} catch (Exception e) {
			logger.error("updateKitIndent failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * This method is used to update kit indent status from New to Pending.
	 * @param List<KitIndentDto>
	 * @return Response
	 */
	@RequestMapping(value = "/updateKitIndentStatus", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateKitIndentStatus(@RequestBody List<KitIndentDto> kitIndentDtoList) {
		try {
			return iKitIndentService.updateKitIndentStatus(kitIndentDtoList);
		} catch (Exception e) {
			logger.error("updateKitIndentStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * This method is used to cancel a particular kit indent by Indent No
	 * @param kitIndentDto
	 * @return Response 
	 */
	@RequestMapping(value = "/cancelKitIndent", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response cancelKitIndent(@RequestBody KitIndentDto kitIndentDto ){
		try {
			return iKitIndentService.cancelKitIndent(kitIndentDto);
		} catch (Exception e) {
			logger.error("cancelKitIndent failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * This method is used to get list of kit indents.
	 * @return Response
	 */
	@RequestMapping(value = "/getKitIndentList", method = RequestMethod.GET)
	public @ResponseBody Response getKitIndentList(@RequestParam("organizationId")Integer organizationId,
			@RequestParam("unitId")Integer unitId) {
		try {
			return iKitIndentService.getKitIndentList(organizationId,unitId);
		} catch (Exception e) {
			logger.error("getKitIndentList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * This method is used to get list of kit indents by using multiple filters.
	 * @param fromDate
	 * @param toDate
	 * @param fromStore
	 * @param toStore
	 * @param status
	 * @return Response
	 */
	
	@RequestMapping(value = "/getKitIndentByMultipleFields", method = RequestMethod.GET)
	public @ResponseBody Response getKitIndentByMultipleFields(
			@RequestParam("fromDate")@DateTimeFormat(pattern="dd-MM-yyyy") Date fromDate,
			@RequestParam("toDate")@DateTimeFormat(pattern="dd-MM-yyyy") Date toDate,
			@RequestParam(value="fromStore",required=false) Integer fromStore,
			@RequestParam(value="toStore",required=false) Integer toStore,
			@RequestParam(value="indentNo",required=false) Integer indentNo,
			@RequestParam(value="status",required=false) String status,
			@RequestParam("organizationId")Integer organizationId,
			@RequestParam("unitId")Integer unitId) {
		try {
			return iKitIndentService.getKitIndentByMultipleFields(fromDate,toDate,fromStore,toStore,indentNo,status,organizationId,unitId);
		} catch (Exception e) {
			logger.error("getKitIndentByMultipleFields failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * This method is used to get list of kit indents by using multiple filters for Kit Approval Screen.
	 * @param fromDate
	 * @param toDate
	 * @param fromStore
	 * @param toStore
	 * @param status
	 * @return Response
	 */
	@RequestMapping(value = "/getKitIndentApprovalByMultipleFields", method = RequestMethod.GET)
	public @ResponseBody Response getKitIndentApprovalByMultipleFields(
			@RequestParam("fromDate")@DateTimeFormat(pattern="dd-MM-yyyy") Date fromDate,
			@RequestParam("toDate")@DateTimeFormat(pattern="dd-MM-yyyy") Date toDate,
			@RequestParam(value="fromStore",required=false) Integer fromStore,
			@RequestParam(value="toStore",required=false) Integer toStore,
			@RequestParam(value="indentNo",required=false) Integer indentNo,
			@RequestParam(value="status",required=false) String status,
			@RequestParam("organizationId")Integer organizationId,
			@RequestParam("unitId")Integer unitId
			) {
		try {
			return iKitIndentService.getKitIndentApprovalByMultipleFields(fromDate,toDate,fromStore,toStore,indentNo,status,organizationId,unitId);
		} catch (Exception e) {
			logger.error("getKitIndentApprovalByMultipleFields failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	/**
	 * This method is used to get kit list in the indent by indent number.
	 * @param indentNo
	 * @return Response
	 */
	@RequestMapping(value = "/getKitListByIndentNo", method = RequestMethod.GET)
	public @ResponseBody Response getKitListByIndentNo(@RequestParam("organizationId")Integer organizationId,
			@RequestParam("unitId")Integer unitId,@RequestParam("indentNo") Integer indentNo) {
		try {
			return iKitIndentService.getKitListByIndentNo(organizationId,unitId,indentNo);
		} catch (Exception e) {
			logger.error("getKitListByIndentNo failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * This method is used to get kit indent by indent number.
	 * @param indentNo
	 * @return Response
	 */
	@RequestMapping(value = "/getKitIndentByIndentNo", method = RequestMethod.GET)
	public @ResponseBody Response getKitIndentByIndentNo(@RequestParam("organizationId")Integer organizationId,
			@RequestParam("unitId")Integer unitId,@RequestParam("indentNo") Integer indentNo) {
		try {
			return iKitIndentService.getKitIndentByIndentNo(organizationId,unitId,indentNo);
		} catch (Exception e) {
			logger.error("getKitIndentByIndentNo failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	/**
	 * This method is used to approve kit indent by indent number.
	 * @param indentNo
	 * @return Response
	 */
	@RequestMapping(value = "/approveKit", method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response approveKit(@RequestBody KitIndentKitMapperDto kitMapperDto ) {
		try {
			return iKitIndentService.approveKit(kitMapperDto);
		} catch (Exception e) {
			logger.error("approveKit failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * This method is used to reject kit indent by indent number.
	 * @param indentNo
	 * @param reason
	 * @return Response
	 */
	@RequestMapping(value = "/rejectKit", method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response rejectKit(@RequestBody KitIndentKitMapperDto kitMapperDto) {
		try {
			return iKitIndentService.rejectKit(kitMapperDto);
		} catch (Exception e) {
			logger.error("rejectKit failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * This method is used to get Store List for based on user id.
	 * @param userId
	 * @return Response
	 */
	@RequestMapping(value = "/getKitIndentFromStore", method = RequestMethod.GET)
	public @ResponseBody Response getKitIndentFromStore(@RequestParam("organizationId")Integer organizationId,
			@RequestParam("unitId")Integer unitId,@RequestParam("userId") Integer userId) {
		try {
			return iKitIndentService.getKitIndentFromStore(organizationId,unitId,userId);
		} catch (Exception e) {
			logger.error("getKitIndentFromStore failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getCssdStoreListByUserId", method = RequestMethod.GET)
	public @ResponseBody Response getCssdStoreListByUserId(@RequestParam("organizationId")Integer organizationId,
			@RequestParam("unitId")Integer unitId,@RequestParam("userId") Integer userId) {
		try {
			return iKitIndentService.getCssdStoreListByUserId(organizationId,unitId,userId);
		} catch (Exception e) {
			logger.error("getCssdStoreListByUserId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	/**
	 * This method is used to get Store List whose Store Type is CSSD.
	 * @param userId
	 * @return Response
	 */
	@RequestMapping(value = "/getKitIndentToStore", method = RequestMethod.GET)
	public @ResponseBody Response getKitIndentToStore(@RequestParam("organizationId")Integer organizationId,
			@RequestParam("unitId")Integer unitId) {
		try {
			return iKitIndentService.getKitIndentToStore(organizationId,unitId);
		} catch (Exception e) {
			logger.error("getKitIndentToStore failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * This method is used to get Store List whose Store Type is CSSD.
	 * @param userId
	 * @return Response
	 */
	@RequestMapping(value = "/getIndentPriorityList", method = RequestMethod.GET)
	public @ResponseBody Response getPriorityList(@RequestParam("organizationId")Integer organizationId,
			@RequestParam("unitId")Integer unitId) {
		try {
			return iKitIndentService.getPriorityList(organizationId,unitId);
		} catch (Exception e) {
			logger.error("getPriorityList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
		
}
