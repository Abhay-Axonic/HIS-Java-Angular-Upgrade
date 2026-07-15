package com.param.otims.service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.otims.dto.OTReservationDto;
import com.param.global.otims.dto.ProcedureOrderMasterDto;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings("rawtypes")
public interface IOTReservationService {

	Response saveOTReservation(OTReservationDto oTReservationdto, HttpServletRequest request)throws ApplicationException;

	Response getEquipmentTyoeAndAssetItem(int orgId, int unitId)throws ApplicationException;

	Response updateProReqMasterStatus(Integer proReqId)throws ApplicationException;

	Response searchOTReservation(ProcedureOrderMasterDto procedureOrderMasterDto)throws ApplicationException;

	Response searchReservedProcedureName(ProcedureOrderMasterDto procedureOrderMasterDto) throws ApplicationException;

	Response cancelOTReservation(OTReservationDto oTReservationdto) throws ApplicationException;

	Response getOTReservationListByPatientId(OTReservationDto oTReservationdto) throws ApplicationException;

	Response getOTReservationListByPatientIdForDeposite(OTReservationDto oTReservationdto)throws ApplicationException;

	Response getOTReservationForOtCalender(ProcedureOrderMasterDto procedureOrderMasterDto)throws ApplicationException;

}
