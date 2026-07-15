package com.param.otims.dao;

import com.param.global.common.Response;
import com.param.global.otims.dto.OTReservationDto;
import com.param.global.otims.dto.ProcedureOrderMasterDto;
import com.param.global.otims.dto.ProcedureReqMasterDto;
import com.param.global.procedure.VO.AssetVO;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IOTReservationDao {

	Response saveOTReservation(OTReservationDto oTReservationdto)throws ApplicationException;

	Response getEquipmentTyoeAndAssetItem(int orgId, int unitId)throws ApplicationException;

	Response getAssetAndBatch(AssetVO asstItem)throws ApplicationException;

	Response saveProcedureReq(ProcedureReqMasterDto procedureReqMasterDto)throws ApplicationException;

	Response saveProcedureOrder(ProcedureOrderMasterDto procedureOrderMasterDto)throws ApplicationException;

	Response updateProReqMasterStatus(Integer proReqId)throws ApplicationException;

	Response searchOTReservation(ProcedureOrderMasterDto procedureOrderMasterDto)throws ApplicationException;

	Response searchReservedProcedureName(ProcedureOrderMasterDto procedureOrderMasterDto)throws ApplicationException;

	Response cancelProcedureOrder(OTReservationDto oTReservationdto)throws ApplicationException;

	Response cancelOTReservation(OTReservationDto oTReservationdto)throws ApplicationException;

	Response getOTReservationListByPatientId(OTReservationDto oTReservationdto)throws ApplicationException;

	Response getVisitTypeListByPatientId(OTReservationDto dto)throws ApplicationException;

	Response getOTReservationListByPatientIdForDeposite(OTReservationDto oTReservationdto)throws ApplicationException;

	Response updateOTReservation(OTReservationDto oTReservationdto)throws ApplicationException;

	Response updateAllProcedureOrderDetailsStatus(ProcedureOrderMasterDto procedureOrderMasterDto)throws ApplicationException;

	Response getOTReservationForOtCalender(ProcedureOrderMasterDto procedureOrderMasterDto)throws ApplicationException;

}
