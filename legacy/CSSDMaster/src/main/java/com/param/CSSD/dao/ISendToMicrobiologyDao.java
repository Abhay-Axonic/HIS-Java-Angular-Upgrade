package com.param.CSSD.dao;

import com.param.CSSD.dto.LoadBatchDto;
import com.param.global.common.Response;

@SuppressWarnings("rawtypes")
public interface ISendToMicrobiologyDao {

	/**
	 * This method is used to send batch to the Microbiology.
	 * @param loadBatchDto
	 * @return Response
	 */
	Response sendToMicrobiology(LoadBatchDto loadBatchDto);
	
	/**
	 * This method is used to get batch list available to send to Microbiology.
	 * @return Response
	 */
	Response getBatchListToSendToMicrobiology();
}
