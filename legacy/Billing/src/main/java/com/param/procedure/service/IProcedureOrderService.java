package com.param.procedure.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dto.OTManagerDashboardDto;
import com.param.global.otims.dto.PreOperativeDetailsDto;
import com.param.global.otims.dto.ProcedureOrderConsentDto;
import com.param.global.otims.dto.ProcedureOrderMasterDto;
import com.param.procedure.dto.ProcedureOrderListReqDto;
import com.param.procedure.dto.ProcedureOrderReqDto;
import com.param.procedure.dto.ProcedureOrderServSrchReqDto;
import com.param.procedure.dto.ProcedureRequestDto;
import com.param.procedure.dto.VOProcedureOrderMasterDto;

import in.param.exception.ApplicationException;
@Transactional
@SuppressWarnings("rawtypes")
public interface IProcedureOrderService {
	public Response getProcedureOrderById(int orderId, int tariffId, int storeId,HttpServletRequest request)throws ServiceException, Exception;
	public Response getProcedureOrderListByStatus(ProcedureOrderListReqDto procedureOrderListReqDto)throws ServiceException;

	public Response searchProcedureServiceForOrder(ProcedureOrderServSrchReqDto reqDto)throws ApplicationException;

	public Response saveProcedureList(List<ProcedureOrderMasterDto> procedureOrderMasterDtoList)throws ServiceException;
	public Response searchServicesForProcedureOrder(ProcedureOrderServSrchReqDto reqDto)throws ApplicationException;
	public Response orderProcedures(ProcedureOrderReqDto reqDto)throws ApplicationException,ServiceException,Exception;
	public Response getProcReqDetailsById(int id) throws ApplicationException;
	public Response searchProcedureOrder(ProcedureRequestDto procedureRequestDto)throws ApplicationException;
	public Response getRoleListInOTBilling(int orgId,int orgUnitId)throws ApplicationException;
	public Response getProcedureOrderList(int encounterId)throws ApplicationException;
	public Response searchProcedureAndPatientDetailsList(OTManagerDashboardDto otManagerDashboardDto)throws ApplicationException;
	public Response procedureOrderUpdate(ProcedureOrderMasterDto procedureOrderMasterDto);
	public Response getProcedureRequestId(int orderId) throws ApplicationException;
	public Response getprocedureOrderbyVisitandDate(ProcedureOrderMasterDto procedureOrderMasterDto)throws ApplicationException;
	public Response getProcedureOrderListByAdmissionId(int admissionId, int encounterId, int proReqId) throws ApplicationException;
	public Response getProcedureByAdmisssion(int admissionId, int encounterId) throws ApplicationException;
	public Response toCheckByAdmitionId(int proReqId)throws ApplicationException;
	public Response getProcedureOrderListByDoctorForEMR(int doctorId, int orgId,int orgUnitId)throws ApplicationException;
	public Response getConsentDetailsByProcedureOrderId(ProcedureOrderConsentDto dto) throws ApplicationException;
	/*public Response getChecklistDetailsByProcedureOrderId(ProcedureOrderChecklistDto dto) throws ApplicationException;
	public Response getStaffDetailsByProcedureOrderId(ProcedureOrderStaffDto dto) throws ApplicationException;
	public Response getServiceDetailsByProcedureOrderId(ProcedureOrderServiceDto dto) throws ApplicationException;
	public Response getPatientEducationDetailsByProcedureOrderId(ProcedureOrderPatientEducationDto dto) throws ApplicationException;
	public Response getEquipmentDetailsByProcedureOrderId(ProcedureOrderEquipmentDto dto)  throws ApplicationException;*/
	public Response saveProcedureListFromOPD(List<ProcedureOrderMasterDto> procedureOrderMasterDtoList) throws ApplicationException;
	public Response getProcedureOrderListByProReqID(Integer proReqId) throws ApplicationException;
	public Response getProcedureOrderListForDischargeSummary(Integer admissionId, Integer organizationId,
			Integer unitId)throws ApplicationException;
	public Response getProcedureOrderListForBookView(ProcedureOrderMasterDto procedureOrderMasterDto)throws ApplicationException;
	public Response procedureOrderEquipment(List<VOProcedureOrderMasterDto> vOProcedureOrderMasterDto)throws ApplicationException;
	public Response procedureOrderStaff(List<VOProcedureOrderMasterDto> vOProcedureOrderMasterDto)throws ApplicationException;
	public Response searchPlannedProcedure(ProcedureRequestDto procedureRequestDto) throws ApplicationException;
	public Response procedureOrderChecklist(Integer proReqId, Integer userId)throws ApplicationException;
	public Response getProcedureServiceRate(List<ProcedureOrderMasterDto> procedureOrderList)throws ApplicationException;
	public Response getEquipmentRateFromContract(ProcedureOrderMasterDto procedureOrderDto)throws ApplicationException;
	public Response getMedicationsAndImplantsByPeoReqId(PreOperativeDetailsDto preOperativeDetailsDto)throws ApplicationException;
	public Response searchProcedureByNameOrCode(ProcedureOrderMasterDto procedureOrderDto)throws ApplicationException;


    Response getDataByDateFromProcedureOrder(ProcedureOrderMasterDto dto) throws ApplicationException;

	Response getUniqueDateFromProcedureOrder(ProcedureOrderMasterDto dto) throws ApplicationException;
}
