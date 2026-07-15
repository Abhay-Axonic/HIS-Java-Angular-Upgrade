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

import com.param.CSSD.dto.KitIssueDto;
import com.param.CSSD.service.IKitIssueService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.JsonCheck;
import com.param.global.common.Response;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@RestController
@RequestMapping("/cssd")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class KitIssueController implements ICommonConstants {

	final static Logger logger = Logger.getLogger(KitIssueController.class);


	@Autowired
	private IKitIssueService iKitIssueService;

	/**
	 * This method is used to issue kit.
	 * @param kitIssueDto
	 * @return Response
	 */
	
	@RequestMapping(value = "/saveKitIssue", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveKitIndent(@RequestBody KitIssueDto kitIssueDto) {
		try {
			return iKitIssueService.createKitIssue(kitIssueDto);
		} catch (Exception e) {
			logger.error("saveKitIndent failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	 

	
	/**
	 * This method is used to get issued kits by using multiple filters.
	 * @param fromDate
	 * @param toDate
	 * @param fromStore
	 * @param toStore
	 * @param indentNo
	 * @param status
	 * @return Response
	 */
	@RequestMapping(value = "/getKitIssueByMultipleFields", method = RequestMethod.GET)
	public @ResponseBody Response getKitIssueByMultipleFields(
			@RequestParam("fromDate")@DateTimeFormat(pattern="dd-MM-yyyy") Date fromDate,
			@RequestParam("toDate")@DateTimeFormat(pattern="dd-MM-yyyy") Date toDate,
			@RequestParam(value="fromStore",required=false) Integer fromStore,
			@RequestParam(value="toStore",required=false) Integer toStore,
			@RequestParam(value="issueNo",required=false) Integer issueNo,
			@RequestParam(value="status",required=false) String status,
			@RequestParam("organizationId")Integer organizationId,
			@RequestParam("unitId")Integer unitId) {
		try {
			return iKitIssueService.getKitIssueByMultipleFields(fromDate,toDate,fromStore,toStore,issueNo,status,organizationId,unitId);
		} catch (Exception e) {
			logger.error("getKitIssueByMultipleFields failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/getKitIssueByMultipleFieldsForAcceptance", method = RequestMethod.GET)
	public @ResponseBody Response getKitIssueByMultipleFieldsForAcceptance(
			@RequestParam("fromDate")@DateTimeFormat(pattern="dd-MM-yyyy") Date fromDate,
			@RequestParam("toDate")@DateTimeFormat(pattern="dd-MM-yyyy") Date toDate,
			@RequestParam(value="fromStore",required=false) Integer fromStore,
			@RequestParam(value="toStore",required=false) Integer toStore,
			@RequestParam(value="issueNo",required=false) Integer issueNo,
			@RequestParam(value="status",required=false) String status,
			@RequestParam("organizationId")Integer organizationId,
			@RequestParam("unitId")Integer unitId) {
		try {
			return iKitIssueService.getKitIssueByMultipleFieldsForAcceptance(fromDate,toDate,fromStore,toStore,issueNo,status,organizationId,unitId);
		} catch (Exception e) {
			logger.error("getKitIssueByMultipleFieldsForAcceptance failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getKitIssueByIssueNo", method = RequestMethod.GET)
	public @ResponseBody Response getKitIssueByIssueNo(
			@RequestParam("issueNo") Integer issueNo,
			@RequestParam("organizationId")Integer organizationId,
			@RequestParam("unitId")Integer unitId) {
		try {
			return iKitIssueService.getKitIssueByIssueNo(issueNo,organizationId,unitId);
		} catch (Exception e) {
			logger.error("getKitIssueByIssueNo failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateKitIssue", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateKitIndent(@RequestBody KitIssueDto kitIssueDto) {
		try {
			return iKitIssueService.updateKitIssue(kitIssueDto);
		} catch (Exception e) {
			logger.error("updateKitIndent failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/acceptRejectKits", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response acceptRejectKits(@RequestBody KitIssueDto kitIssueDto) {
		try {
			return iKitIssueService.acceptRejectKits(kitIssueDto);
		} catch (Exception e) {
			logger.error("acceptRejectKits failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateKitIssueStatus", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateKitIssueStatus(@RequestBody List<KitIssueDto> kitIssueDtoList) {
		try {
			return iKitIssueService.updateKitIssueStatus(kitIssueDtoList);
		} catch (Exception e) {
			logger.error("updateKitIssueStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/cancelKitIssue", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response cancelKitIssue(@RequestBody KitIssueDto kitIssueDto ){
		try {
			return iKitIssueService.cancelKitIssue(kitIssueDto);
		} catch (Exception e) {
			logger.error("cancelKitIssue failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/acceptKitIssue", method = RequestMethod.PUT)
	public @ResponseBody Response acceptKitIssue(@RequestParam("organizationId")Integer organizationId,
			@RequestParam("unitId")Integer unitId,
			@RequestParam("issueNo")Integer issueNo,
			@RequestParam("transitionNumber")Integer transitionNumber) {
		try {
			return iKitIssueService.acceptKitIssue(organizationId,unitId,issueNo,transitionNumber);
		} catch (Exception e) {
			logger.error("acceptKitIssue failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/rejectKitIssue", method = RequestMethod.PUT)
	public Response rejectKitIssue(@RequestParam("organizationId")Integer organizationId,
			@RequestParam("unitId")Integer unitId,
			@RequestParam("issueNo")Integer issueNo,
			@RequestParam("transitionNumber")Integer transitionNumber,
			@RequestParam("rejectReasonId")Integer rejectReasonId,
			@RequestParam("quantity")Integer quantity,
			@RequestParam(value="rejectionNote",required=false)String rejectionNote) {
		try {
			return iKitIssueService.rejectKitIssue(organizationId,unitId,issueNo,transitionNumber,rejectReasonId,quantity,rejectionNote);
		} catch (Exception e) {
			logger.error("rejectKitIssue failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	@RequestMapping(value = "/getKitIndentByMultipleFieldsForKitIssue", method = RequestMethod.GET)
	public @ResponseBody Response getKitIndentByMultipleFieldsForKitIssue(
			@RequestParam("fromDate")@DateTimeFormat(pattern="dd-MM-yyyy") Date fromDate,
			@RequestParam("toDate")@DateTimeFormat(pattern="dd-MM-yyyy") Date toDate,
			@RequestParam(value="fromStore",required=false) Integer fromStore,
			@RequestParam(value="toStore",required=false) Integer toStore,
			@RequestParam(value="indentNo",required=false) Integer indentNo,
			@RequestParam("organizationId")Integer organizationId,
			@RequestParam("unitId")Integer unitId) {
		try {
			return iKitIssueService.getKitIndentByMultipleFieldsForKitIssue(fromDate,toDate,fromStore,toStore,indentNo,organizationId,unitId);
		} catch (Exception e) {
			logger.error("getKitIndentByMultipleFieldsForKitIssue failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/getSterilizationRequestByMultipleFieldsForKitIssue", method = RequestMethod.GET)
	public @ResponseBody Response getSterilizationRequestByMultipleFieldsForKitIssue(
			@RequestParam("fromDate")@DateTimeFormat(pattern="dd-MM-yyyy") Date fromDate,
			@RequestParam("toDate")@DateTimeFormat(pattern="dd-MM-yyyy") Date toDate,
			@RequestParam(value="requestingStore",required=false) Integer requestingStore,
			@RequestParam(value="issueingStore",required=false) Integer issueingStore,
			@RequestParam(value="requestNo",required=false) Integer requestNo,
			@RequestParam("organizationId")Integer organizationId,
			@RequestParam("unitId")Integer unitId) {
		try {
			return iKitIssueService.getSterilizationRequestByMultipleFieldsForKitIssue(fromDate,toDate,requestingStore,issueingStore,requestNo,organizationId,unitId);
		} catch (Exception e) {
			logger.error("getSterilizationRequestByMultipleFieldsForKitIssue failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * This method is used to get list of issued kits.
	 * @return Response
	 */
	@RequestMapping(value = "/getKitIssueList", method = RequestMethod.GET)
	public @ResponseBody Response getKitIssueList() {
		try {
			return iKitIssueService.getKitIssueList();
		} catch (Exception e) {
			logger.error("getKitIssueList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	/**
	 * This method is used to get list of kits by issue number.
	 * @param issueNo
	 * @return Response
	 */
	@RequestMapping(value = "/getKitListByIssueNo", method = RequestMethod.GET)
	public @ResponseBody Response getKitListByIssueNo(@RequestParam("issueNo") Integer issueNo) {
		try {
			return iKitIssueService.getKitListByIssueNo(issueNo);
		} catch (Exception e) {
			logger.error("getKitListByIssueNo failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	/**
	 * This method is used to get particular kit issue by issue number.
	 * @param issueNo
	 * @return Response
	 */
	@RequestMapping(value = "/getKitIssueByKitIssueNo", method = RequestMethod.GET)
	public @ResponseBody Response getKitIssueByIssueNo(@RequestParam("issueNo") Integer issueNo) {
		try {
			return iKitIssueService.getKitIssueByIssueNo(issueNo);
		} catch (Exception e) {
			logger.error("getKitIssueByIssueNo failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * This method is used to get kit list that are available to issue.
	 * @return Response
	 */
	@RequestMapping(value = "/getKitListForKitIssue", method = RequestMethod.GET)
	public @ResponseBody Response getKitListForKitIssue() {
		try {
			return iKitIssueService.getKitListForKitIssue();
		} catch (Exception e) {
			logger.error("getKitListForKitIssue failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * This method is used to stop kit issue from Application screen by batch number.
	 * @param batchNo
	 * @return Response
	 */
	@RequestMapping(value = "/stopIssue", method = RequestMethod.PUT)
	public @ResponseBody Response stopIssue(@RequestParam("batchNo") Integer batchNo) {
		try {
			return iKitIssueService.stopIssue(batchNo);
		} catch (Exception e) {
			logger.error("stopIssue failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * This method is used to accept kit issue.
	 * @param issueNo
	 * @return Response
	 */
	@RequestMapping(value = "/acceptKitIssueOld", method = RequestMethod.PUT)
	public @ResponseBody Response approveKitIndent(@RequestParam("issueNo") Integer issueNo) {
		try {
			return iKitIssueService.acceptKitIssue(issueNo);
		} catch (Exception e) {
			logger.error("approveKitIndent failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * This method is used to reject kit issue.
	 * @param issueNo
	 * @param reason
	 * @return Response
	 */
	@RequestMapping(value = "/rejectKitIssueOld", method = RequestMethod.PUT)
	public @ResponseBody Response rejectKitIssue(@RequestParam("issueNo") Integer issueNo,@RequestParam(value="reason",required=false ) String reason) {
		try {
			return iKitIssueService.rejectKitIssue(issueNo,reason);
		} catch (Exception e) {
			logger.error("rejectKitIssue failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
		
}
