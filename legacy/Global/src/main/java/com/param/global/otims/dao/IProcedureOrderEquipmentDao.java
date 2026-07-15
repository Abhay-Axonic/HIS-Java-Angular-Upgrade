package com.param.global.otims.dao;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.otims.dto.OTReservationDto;
import com.param.global.otims.dto.ProcedureOrderConsentDto;
import com.param.global.otims.dto.ProcedureOrderEquipmentDto;

import in.param.exception.ApplicationException;

@SuppressWarnings({ "rawtypes"})
public interface IProcedureOrderEquipmentDao {

	Response saveProcedureOrderEquipment(List<ProcedureOrderEquipmentDto> procedureOrderEquipmentList,Integer proReqId, Integer procedureOrderId) throws ApplicationException;

	Response getEquipmentDetailsByProcedureOrderId(ProcedureOrderConsentDto dto)throws ApplicationException;

	Response cancelProcedureOrderEquipment(OTReservationDto oTReservationdto)throws ApplicationException;

	Response getserviceIdByAssetCat(Integer equipmentTypeId, Integer orgId, Integer unitId,Integer visitTypeId, Integer serviceId)throws ApplicationException;

}
