package com.param.global.otims.dao;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.otims.dto.OTReservationDto;
import com.param.global.otims.dto.ProcedureOrderConsentDto;
import com.param.global.otims.dto.ProcedureOrderStaffDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IProcedureOrderStaffDao {
	
	Response getStaffDetailsByProcedureOrderId(ProcedureOrderConsentDto dto) throws ApplicationException;

	Response saveProcedureOrderStaffList(List<ProcedureOrderStaffDto> procedureOrderStaffList, int proReqId,
			Integer procedureOrderId)throws ApplicationException;

	Response cancelProcedureOrderStaffList(OTReservationDto oTReservationdto) throws ApplicationException;

}
