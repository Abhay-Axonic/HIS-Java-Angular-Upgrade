package com.param.global.otims.dao;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.otims.dto.OTReservationDto;
import com.param.global.otims.dto.ProcedureOrderConsentDto;
import com.param.global.otims.dto.ProcedureOrderServiceDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IProcedureOrderServiceDao {

	Response getServiceDetailsByProcedureOrderId(ProcedureOrderConsentDto dto) throws ApplicationException;

	Response saveProcedureOrderServiceList(List<ProcedureOrderServiceDto> procedureOrderServiceList, int proReqId,
			Integer procedureOrderId)throws ApplicationException;

	Response cancelProcedureOrderServiceList(OTReservationDto oTReservationdto)throws ApplicationException;

}
