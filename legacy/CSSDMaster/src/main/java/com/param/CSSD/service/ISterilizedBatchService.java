package com.param.CSSD.service;

import com.param.CSSD.dto.SterilizedBatchDto;
import com.param.global.common.Response;

@SuppressWarnings("rawtypes")
public interface ISterilizedBatchService {

	/**
	 * This method is used to transfer a batch to sterilize zone
	 * @param sterilizedBatchDto
	 * @return Response
	 */
	Response transferToSterilizeZone(SterilizedBatchDto sterilizedBatchDto);
	
	/**
	 * This method is used to get the indicators of a batch by batch number.
	 * @param batchNo
	 * @return Response
	 */
	Response getIndicatorsByBatchNo(Integer batchNo);
}
