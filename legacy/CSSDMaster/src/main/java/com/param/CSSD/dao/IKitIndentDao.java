package com.param.CSSD.dao;

import java.util.Date;
import java.util.List;

import com.param.CSSD.dto.KitIndentDto;
import com.param.CSSD.dto.KitIndentKitMapperDto;
import com.param.global.common.Response;

@SuppressWarnings("rawtypes")
public interface IKitIndentDao {

	/**
	 * This method is used to create kit indent.
	 * @param kitIndentDto
	 * @return Response
	 */
	Response createKitIndent(KitIndentDto kitIndentDto);
	
	Response updateKitIndent(KitIndentDto kitIndentDto);
	
	Response cancelKitIndent(KitIndentDto kitIndentDto);
	
	Response updateKitIndentStatus(List<KitIndentDto> kitIndentDtoList);
	
	/**
	 * This method is used to get list of kit indents.
	 * @return Response
	 */
	Response getKitIndentList(Integer organizationId,Integer unitId);
	
	/**
	 * This method is used to get kit list in the indent by indent number.
	 * @param indentNo
	 * @return Response
	 */
	Response getKitListByIndentNo(Integer organizationId,Integer unitId,Integer indentNo);
	
	/**
	 * This method is used to get kit indent by indent number.
	 * @param indentNo
	 * @return Response
	 */
	Response getKitIndentByIndentNo(Integer organizationId,Integer unitId,Integer indentNo);
	
	Response getKitIndentByIndentNoForKitIssue(Integer organizationId,Integer unitId,Integer indentNo);
	
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
	
	/**
	 * This method is used to get list of kit indents by using multiple filters.
	 * @param fromDate
	 * @param toDate
	 * @param fromStore
	 * @param toStore
	 * @param status
	 * @return Response
	 */
	/*Response getKitIndentByMultipleFields(String fromDate,String toDate,Integer fromStore,Integer toStore,Integer indentNo,String status);*/
	
	Response getKitIndentByMultipleFields(Date fromDate,Date toDate,Integer fromStore,Integer toStore,Integer indentNo,String status,Integer organizationId,Integer unitId);
	
	Response getKitIndentApprovalByMultipleFields(Date fromDate,Date toDate,Integer fromStore,Integer toStore,Integer indentNo,String status,Integer organizationId,Integer unitId);
	
	Response getKitIndentFromStore(Integer organizationId,Integer unitId,Integer userId);
	
	Response getKitIndentToStore(Integer organizationId,Integer unitId);
	
	Response getPriorityList(Integer organizationId,Integer unitId);
	
	Response getCssdStoreListByUserId(Integer organizationId,Integer unitId,Integer userId);
	
	
	
}
