package com.param.global.procedure.dao;

import com.param.global.common.Response;
import com.param.global.otims.dto.OTReservationDto;
import com.param.global.procedure.dto.BillOfMaterialDto;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IBillOfMaterialDao {

	Response saveBillOfMaterial(BillOfMaterialDto billOfMaterialDto)throws ApplicationException;

	Response getBillOfMaterialServices(BillOfMaterialDto billOfMaterialDto)throws ApplicationException;

	Response getIsProcedure(BillOfMaterialDto billOfMaterialDto)throws ApplicationException;

	Response getBillOfMaterialListByProcedureId(BillOfMaterialDto billOfMaterialDto) throws ApplicationException;

	Response getVisitTypeByBillingBedCat(Integer billingBedCatId, Integer unitId) throws ApplicationException;

	Response getBillOfMaterialServiceAmount(BillOfMaterialDto billOfMaterialDto)throws ApplicationException;

	Response updateReservationAgainstAdmissionEncounter(OTReservationDto oTReservationDto)throws ApplicationException;

	Response reAssignOtReservationProcedureAdmissionEncounter(OTReservationDto oTReservationDto)throws ApplicationException;

	Response convertOtdepositeToActiveDeposite(OTReservationDto oTReservationDto)throws ApplicationException;

	Response getCongServicesByProReqId(OTReservationDto oTReservationDto)throws ApplicationException;

	Response getAdmissionsById(Integer admissionId)throws ApplicationException;

	Response getBillOfMaterialListByProcedureList(BillOfMaterialDto billOfMaterialDto)throws ApplicationException;

	Response updateStatusProcedure(BillOfMaterialDto billOfMaterialDto)throws ApplicationException;

	Response getBillOfMaterialListByProcedureIdAll(BillOfMaterialDto billOfMaterialDto)throws ApplicationException;

}
