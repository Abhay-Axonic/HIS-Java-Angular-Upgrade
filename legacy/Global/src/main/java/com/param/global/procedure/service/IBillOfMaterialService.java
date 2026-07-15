package com.param.global.procedure.service;

import java.util.List;

import javax.transaction.Transactional;

import in.param.exception.ApplicationException;

import com.param.global.common.Response;
import com.param.global.otims.dto.OTReservationDto;
import com.param.global.procedure.dto.BillOfMaterialDto;

@Transactional
@SuppressWarnings("rawtypes")
public interface IBillOfMaterialService {

	Response saveBillOfMaterial(BillOfMaterialDto billOfMaterialDto)throws ApplicationException;

	Response getBillOfMaterialServices(BillOfMaterialDto billOfMaterialDto)throws ApplicationException;

	Response getIsProcedure(BillOfMaterialDto billOfMaterialDto)throws ApplicationException;

	Response getBillOfMaterialListByProcedureId(BillOfMaterialDto billOfMaterialDto) throws ApplicationException;

	Response getBillOfMaterialServiceAmount(List<BillOfMaterialDto> billOfMaterialDto)throws ApplicationException;

	Response updateReservationAgainstAdmissionEncounter(OTReservationDto oTReservationDto)throws ApplicationException;

	Response reAssignOtReservationProcedureAdmissionEncounter(OTReservationDto oTReservationDto)throws ApplicationException;

	Response convertOtdepositeToActiveDeposite(OTReservationDto oTReservationDto)throws ApplicationException;

	Response getCongServicesByProReqId(OTReservationDto oTReservationDto)throws ApplicationException;

	Response getBillOfMaterialListByProcedureList(BillOfMaterialDto billOfMaterialDto)throws ApplicationException;

	Response updateStatusProcedure(BillOfMaterialDto billOfMaterialDto)throws ApplicationException;

	Response getBillOfMaterialListByProcedureIdAll(BillOfMaterialDto billOfMaterialDto)throws ApplicationException;

	
	
}
