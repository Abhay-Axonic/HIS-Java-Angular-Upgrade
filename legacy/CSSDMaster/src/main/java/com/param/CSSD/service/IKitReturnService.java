package com.param.CSSD.service;

import java.util.Date;
import java.util.List;

import com.param.CSSD.dto.KitReturnDto;
import com.param.global.common.Response;

@SuppressWarnings("rawtypes")
public interface IKitReturnService {

	
	/**
	 * This method is used to get kit list available to return.
	 * @param fromStore
	 * @param toStore
	 * @return
	 */
	Response getKitListForKitReturn(Integer organizationId,Integer unitId,Integer fromStoreId,Integer toStoreId,String name);
	
	/**
	 * This method is used to create kit return.
	 * @param kitReturnDto
	 * @return Response
	 */
	Response createKitReturn(KitReturnDto kitReturnDto);
	
	/**
	 * This method is used to get kit return list by multiple filters.
	 * @param fromStore
	 * @param toStore
	 * @param returnNo
	 * @param status
	 * @return Response
	 */
	Response getKitReturnByMultipleFields(Date fromDate,Date toDate,Integer fromStore,Integer toStore,Integer returnNo,String status,Integer organizationId,Integer unitId);
	
	/**
	 * This method is used to get particular kit return by return number.
	 * @param returnNo
	 * @return Response
	 */
	Response getKitReturnByReturnNo(Integer ReturnNo,Integer organizationId,Integer unitId);
	
	/**
	 * This method is used to update kit return.
	 * @param kitReturnDtoList
	 * @return Response														
	 */
	Response updateKitReturn(KitReturnDto kitReturnDto);
	
	/**
	 * This method is used to update status for kit return.
	 * @param kitReturnDtoList
	 * @return Response														
	 */
	Response updateKitReturnStatus(List<KitReturnDto> kitReturnDtoList );
	
	/**
	 * This method is used to cancel kit return.
	 * @param kitReturnDto
	 * @return Response														
	 */
	Response cancelKitReturn(KitReturnDto kitReturnDto );
	
	
	
	
	
	
	
	
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
