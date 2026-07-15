package com.param.CSSD.service;

import java.util.Date;
import java.util.List;

import com.param.CSSD.dto.KitIndentDto;
import com.param.CSSD.dto.KitIndentKitMapperDto;
import com.param.global.common.Response;

@SuppressWarnings("rawtypes")
public interface IKitIndentService {

	/**
	 * This method is used to create kit indent.
	 * @param kitIndentDto
	 * @return Response
	 */
	Response createKitIndent(KitIndentDto kitIndentDto);
	
	/**
	 * This method is used to update kit indent.
	 * @param kitIndentDto
	 * @return Response
	 */
	Response updateKitIndent(KitIndentDto kitIndentDto);

	/**
	 * This method is used to update kit indent status from New to Pending.
	 * @param List<KitIndentDto>
	 * @return Response
	 */
	Response updateKitIndentStatus(List<KitIndentDto> kitIndentDtoList);
	
	/**
	 * This method is used to cancel a particular kit indent by Indent No
	 * @param kitIndentDto
	 * @return Response 
	 */
	Response cancelKitIndent(KitIndentDto kitIndentDto);
	/**
	 * This method is used to get list of kit indents.
	 * @return Response
	 */
	Response getKitIndentList(Integer orgnizationId,Integer unitId);
	
	/**
	 * This method is used to get list of kit indents by using multiple filters.
	 * @param fromDate
	 * @param toDate
	 * @param fromStore
	 * @param toStore
	 * @param status
	 * @return Response
	 */
	Response getKitIndentByMultipleFields(Date fromDate,Date toDate,Integer fromStore,Integer toStore,Integer indentNo,String status,Integer orgnizationId,Integer unitId);
	
	/**
	 * This method is used to get list of kit indents by using multiple filters for Kit Approval Screen.
	 * @param fromDate
	 * @param toDate
	 * @param fromStore
	 * @param toStore
	 * @param status
	 * @return Response
	 */
	Response getKitIndentApprovalByMultipleFields(Date fromDate,Date toDate,Integer fromStore,Integer toStore,Integer indentNo,String status,Integer orgnizationId,Integer unitId);
	
	/**
	 * This method is used to get kit list in the indent by indent number.
	 * @param indentNo
	 * @return Response
	 */
	Response getKitListByIndentNo(Integer orgnizationId,Integer unitId,Integer indentNo);
	
	/**
	 * This method is used to get kit indent by indent number.
	 * @param indentNo
	 * @return Response
	 */
	Response getKitIndentByIndentNo(Integer orgnizationId,Integer unitId,Integer indentNo);
	
	/**
	 * This method is used to approve kit indent by indent number.
	 * @param indentNo
	 * @return Response
	 */
	Response approveKit(KitIndentKitMapperDto kitMapperDto);
	
	/**
	 * This method is used to reject kit indent by indent number.
	 * @param indentNo
	 * @param reason
	 * @return Response
	 */
	Response rejectKit(KitIndentKitMapperDto kitMapperDto);
	
	Response getKitIndentFromStore(Integer orgnizationId,Integer unitId,Integer userId);
	
	Response getKitIndentToStore(Integer orgnizationId,Integer unitId);
	
	Response getPriorityList(Integer orgnizationId,Integer unitId);
	
	Response getCssdStoreListByUserId(Integer orgnizationId,Integer unitId,Integer userId);
}
