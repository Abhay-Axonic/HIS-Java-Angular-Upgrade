package com.param.CSSD.service;

import java.util.Date;
import java.util.List;

import com.param.CSSD.dto.KitIssueDto;
import com.param.global.common.Response;

@SuppressWarnings("rawtypes")
public interface IKitIssueService {

	/**
	 * This method is used to issue kit.
	 * @param kitIssueDto
	 * @return Response
	 */
	Response createKitIssue(KitIssueDto kitIssueDto);
	
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
	Response getKitIssueByMultipleFields(Date fromDate,Date toDate,Integer fromStore,Integer toStore,Integer indentNo,String status,Integer organizationId,Integer unitId);
	
	Response getKitIssueByMultipleFieldsForAcceptance(Date fromDate,Date toDate,Integer fromStore,Integer toStore,Integer indentNo,String status,Integer organizationId,Integer unitId);
	
	Response getKitIssueByIssueNo(Integer issueNo,Integer organizationId,Integer unitId);
	
	Response updateKitIssue(KitIssueDto kitIssueDto);
	
	Response updateKitIssueStatus(List<KitIssueDto> kitIssueDtoList);
	
	Response cancelKitIssue(KitIssueDto kitIssueDto );
	
	Response acceptRejectKits(KitIssueDto kitIssueDto);
	
	Response acceptKitIssue(Integer organizationId,Integer unitId,Integer issueNo,Integer transitionNumber);
	
	Response rejectKitIssue(Integer organizationId,Integer unitId,Integer issueNo,Integer transitionNumber,Integer rejectReasonId,Integer quantity,String rejectionNote);
	
	Response getKitIndentByMultipleFieldsForKitIssue(Date fromDate,Date toDate,Integer fromStore,Integer toStore,Integer indentNo,Integer organizationId,Integer unitId);
	
	Response getSterilizationRequestByMultipleFieldsForKitIssue(Date fromDate,Date toDate,Integer requestingStore,Integer issueingStore,Integer requestNo,Integer organizationId,Integer unitId);
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * This method is used to get list of issued kits.
	 * @return Response
	 */
	Response getKitIssueList();
	
	
	
	/**
	 * This method is used to get list of kits by issue number.
	 * @param issueNo
	 * @return Response
	 */
	Response getKitListByIssueNo(Integer issueNo);
	
	/**
	 * This method is used to get particular kit issue by issue number.
	 * @param issueNo
	 * @return Response
	 */
	Response getKitIssueByIssueNo(Integer issueNo);
	
	/**
	 * This method is used to get kit list that are available to issue.
	 * @return Response
	 */
	Response getKitListForKitIssue();
	
	/**
	 * This method is used to stop kit issue from Application screen by batch number.
	 * @param batchNo
	 * @return Response
	 */
	Response stopIssue(Integer batchNo);
	
	/**
	 * This method is used to accept kit issue.
	 * @param issueNo
	 * @return Response
	 */
	Response acceptKitIssue(Integer issueNo);
	
	/**
	 * This method is used to reject kit issue.
	 * @param issueNo
	 * @param reason
	 * @return Response
	 */
	Response rejectKitIssue(Integer issueNo,String reason);
}
