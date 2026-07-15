package com.param.batchjobs.service;

import java.sql.SQLException;
import java.util.List;

import com.param.data.dto.ExceptionDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IBatchDao {

	boolean isBatchExecuted(String timeZone, List<Integer> unitIds) throws Exception;
	
	boolean addBathLog(String timeZone, List<Integer> unitIds) throws Exception;

	void clainSentToSmartCard();

	boolean isBatchExecutedRoomRent(String timeZone, List<Integer> unitIds) throws Exception;

	boolean addBathLogForRoomRent(String timeZone, List<Integer> unitIds)throws SQLException;

	Response batchJobToUpdateAppointmentStatus() throws ApplicationException;

	Response addExceptionLog(ExceptionDto exceptionDto) throws ApplicationException, SQLException;

	Response checkBatchJobExecutedOrNotForpostRoomRent(String timeZone, List<Integer> unitIds)throws ApplicationException;

	Response checkBatchJobExecutedOrNotFormidnightBatch(String timeZone, List<Integer> unitId)throws ApplicationException;

	Response saveLog(String timeZone, List<Integer> unitId, String func)throws ApplicationException, Exception;
	
}
