package com.param.CSSD.dao;

import java.util.Date;
import java.util.List;

import com.param.CSSD.dto.KitReturnDto;
import com.param.CSSD.dto.SterilizationRequestDto;
import com.param.CSSD.dto.SterilizationRequestTransitionMapperDto;
import com.param.global.common.Response;

@SuppressWarnings("rawtypes")
public interface ISterilizationRequestDao {

	
	/**
	 * This method is used to get kit list available to request sterilization.
	 * @param requestingStore
	 * @param issueingStore
	 * @return
	 */
	Response getKitListForSterilizationRequest(Integer organizationId,Integer unitId,Integer requestingStoreId,Integer issueingStoreId,String name);
	
	/**
	 * This method is used to create sterilization request.
	 * @param sterilizationRequestDto
	 * @return Response
	 */
	Response createSterilizationRequest(SterilizationRequestDto sterilizationRequestDto );
	
	/**
	 * This method is used to get sterilization request list by multiple filters.
	 * @param reuqestingStore
	 * @param issueingStore
	 * @param requestNo
	 * @param status
	 * @return Response
	 */
	Response getSterilizationRequestByMultipleFields(Date fromDate,Date toDate,Integer requestingStoreId,Integer issueingStoreId,Integer requestNo,String status,Integer organizationId,Integer unitId);
	
	/**
	 * This method is used to get sterilization request list by multiple filters.
	 * @param reuqestingStore
	 * @param issueingStore
	 * @param requestNo
	 * @param status
	 * @return Response
	 */
	Response getSterilizationRequestForAcceptanceByMultipleFields(Date fromDate,Date toDate,Integer requestingStoreId,Integer issueingStoreId,Integer requestNo,String status,Integer organizationId,Integer unitId);
	
	/**
	 * This method is used to get particular sterilization request by request number.
	 * @param requestNo
	 * @return Response
	 */
	Response getSterilizationRequestByRequestNo(Integer requestNo,Integer organizationId,Integer unitId);
	
	/**
	 * This method is used to update sterilization request.
	 * @param sterilizationRequestDto
	 * @return Response														
	 */
	Response updateSterilizationRequest(SterilizationRequestDto sterilizationRequestDto );
	
	/**
	 * This method is used to update status for sterilization request.
	 * @param sterilizationRequestDtoList
	 * @return Response														
	 */
	Response updateSterilizationRequestStatus(List<SterilizationRequestDto> sterilizationRequestDtoList);
	
	/**
	 * This method is used to cancel sterilization request.
	 * @param kitReturnDto
	 * @return Response														
	 */
	Response cancelSterilizationRequest(SterilizationRequestDto sterilizationRequestDto );
	
	/**
	 * This method is used to accept sterilization request.
	 * @param kitReturnDto
	 * @return Response														
	 */
	Response acceptSterilizationRequest(SterilizationRequestDto sterilizationRequestDto);
	
	
	
	
	
	/*-----------------------------------------------------------------------------------------------------------------------------------------------------------------*/
	
	
	

	
	
	
	/**
	 * This method is used to get kit return list.
	 * @return Response
	 */
	Response getKitReturnList();
	
	/**
	 * This method is used to get the list of kits in a kit return by return number.
	 * @param ReturnNo
	 * @return
	 */
	Response getKitListByReturnNo(Integer ReturnNo);
}
