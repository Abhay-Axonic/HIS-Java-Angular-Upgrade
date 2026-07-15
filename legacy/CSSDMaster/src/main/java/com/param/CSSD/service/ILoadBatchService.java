package com.param.CSSD.service;

import java.util.Date;

import com.param.CSSD.dto.LoadBatchDto;
import com.param.global.common.Response;

@SuppressWarnings("rawtypes")
public interface ILoadBatchService {

	
	Response searchKitsToLoadBatch(Integer organizationId,Integer unitId,Integer storeId,String name);
	
	Response getRecentBioIndiDate(Integer organizationId,Integer unitId,Integer storeId,Integer autoclaveId);
	
	Response loadBatch(LoadBatchDto loadBatchDto);
	
	Response getLoadBatchListByMultipleFields(Integer organizationId,Integer unitId,Integer storeId,Date from,Date to,Integer batchNo,Boolean biologicalIndicator,String status);
	
	Response savePendingIndicators(LoadBatchDto loadBatchDto);
	
	Response getBatchDetailsByBatchNumber(Integer organizationId,Integer unitId,Integer storeId,Integer batchNo);
	
	Response getLoadBatchListToSendToMicrobiologyByMultipleFields(Integer organizationId,Integer unitId,Integer storeId,Date from,Date to,Integer batchNo,String status);
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * This method is used to load a batch into machine.
	 * @param loadBatchDto
	 * @return Response
	 */
	/*Response loadBatch(LoadBatchDto loadBatchDto);*/
	
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
	
	/**
	 * This method is used to get kit list by batch number.
	 * @param batchNo
	 * @return Response
	 */
	Response getKitListByBatchNumber(Integer batchNo);
	
	/**
	 * This method is used to get loaded batch list
	 * @return Response
	 */
	Response getLoadBatchList();
	
	/**
	 * This method is used to get loaded batch list by using multiple filters.
	 * @param from
	 * @param to
	 * @param batchNo
	 * @param status
	 * @return Response
	 */
	/*Response getLoadBatchListByMultipleFields(Date from,Date to,Integer batchNo,String status);*/
	
	/**
	 * This method is used to get the list of batch numbers for Control indicator.
	 * @return Response
	 */
	Response getBatchNoListForControl();
	
	/**
	 * This method is used to get the list of batch numbers for Test indicator.
	 * @return Response
	 */
	Response getBatchNoListForTest();
	
	/**
	 * This method is used to get batch list for Application Screen.
	 * @param batchNo
	 * @param fromDate
	 * @param toDate
	 * @param autoclaveNo
	 * @param status
	 * @return Response
	 */
	Response getBatchListForApplication(Integer batchNo,Date fromDate,Date toDate,Integer autoclaveNo,String status);
}
