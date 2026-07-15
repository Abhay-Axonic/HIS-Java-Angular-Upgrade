package com.param.procedure.dao;

import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dto.OTManagerDashboardDto;
import com.param.global.otims.dto.ProcedureOrderChecklistDto;
import com.param.global.otims.dto.ProcedureOrderMasterDto;
import com.param.otims.model.ProcedureOrderMaster;
import com.param.procedure.dto.CssdKitVO;
import com.param.procedure.dto.ProcedureOrderListReqDto;
import com.param.procedure.dto.ProcedureOrderServSrchReqDto;
import com.param.procedure.dto.ProcedureRequestDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IProcedureOrderDao extends IGenericDao<ProcedureOrderMaster, Integer>{
	public Response getProcedureOrderById(int orderId)throws ServiceException;
	public Response getProcedureOrderListByStatus(ProcedureOrderListReqDto procedureOrderListReqDto)throws ApplicationException;

	public Response searchProcedureServiceForOrder(ProcedureOrderServSrchReqDto reqDto)throws ApplicationException;

	public Response saveProcedureOrder(ProcedureOrderMasterDto procedureOrderMasterDto)throws ServiceException;
	public Response searchServicesForProcedureOrder(ProcedureOrderServSrchReqDto reqDto)throws ApplicationException;
	public Response getProcReqDetailsById(int id)throws ApplicationException;
	public Response searchProcedureOrder(ProcedureRequestDto procedureRequestDto)throws ApplicationException;
	public Response getRoleListInOTBilling(int orgId,int orgUnitId)throws ApplicationException;
	public Response getProcedureOrderList(int encounterId)throws ApplicationException;
	public Response procedureOrderUpdate(ProcedureOrderMasterDto procedureOrderMasterDto);
	public Response searchProcedureAndPatientDetailsList(OTManagerDashboardDto otManagerDashboardDto)
			throws ApplicationException;
	public Response getProcedureRequestId(int orderId) throws ApplicationException;
	public Response getprocedureOrderbyVisitandDate(ProcedureOrderMasterDto procedureOrderMasterDto);
	public Response getProcedureOrderListByAdmissionId(int admissionId, int encounterId, int proReqId) throws ApplicationException;
    public Response getMedicationList(int proReqId)throws ApplicationException;
    public Response getImplantsList(int proReqId) throws ApplicationException;
    public Response getProcedureByAdmisssion(int admissionId) throws ApplicationException;
	public Response toCheckByAdmitionId(int proReqId)throws ApplicationException;
	public Response getProcedureOrderListByDoctorForEMR(int doctorId, int orgId, int orgUnitId) throws ApplicationException;
	public Response updateAllProcedureOrderDetailsStatus(ProcedureOrderMasterDto procedureOrderMasterDto) throws ApplicationException;
	public Response saveProcedureListFromOPD(ProcedureOrderMasterDto procedureOrderMasterDto) throws ApplicationException;
	public Response getProcedureOrderListByProReqID(Integer proReqId)throws ApplicationException;
	public Response updateProcedureOrderSeconderyToPrimary(Integer procedureOrderId)throws ApplicationException;
	public Response getProcedureOrderListForBookView(ProcedureOrderMasterDto procedureOrderMasterDto)throws ApplicationException;
	public Response searchPlannedProcedure(ProcedureRequestDto procedureRequestDto) throws ApplicationException;
	public Response getProcedureCheckListByProreqId(Integer proReqId/*, List<Integer> roleIds*/)throws ApplicationException;
	public Response getChecklistItemListByMasterId(ProcedureOrderChecklistDto dto/*, List<Integer> roleIds*/) throws ApplicationException;
	public Response getUserOTIMSRoleID(Integer userId)throws ApplicationException;
	public Response getSaveChecklistItemList(ProcedureOrderChecklistDto procedureChecklistDto/*, List<Integer> roleIds*/,
			Integer proReqId, Integer i)throws ApplicationException;
	public Response getScheduleEquipment(Integer integer)throws ApplicationException;
	public Response getNoteByProcedureConfigId(ProcedureOrderMasterDto dto)throws ApplicationException;
	public Response getSterilizedKitBatchList(CssdKitVO d)throws ApplicationException;
	public Response updateProcedureOrder(ProcedureOrderMasterDto order)throws ApplicationException;
	public Response getEmergencyProcedure(ProcedureOrderMasterDto procedureOrderMasterDto)throws ApplicationException;
	public Response getUserOTIMSRoleByUserID(Integer userId)throws ApplicationException;
	public Response updateProcedureSchedulingDetails(ProcedureOrderMasterDto procDto)throws ApplicationException;
	public Response getOTTThetreByServiceId(ProcedureOrderMasterDto order)throws ApplicationException;
	public Response getProcedureOrderListByAdmissionIdAndEncounterId(int admissionId, int encounterId, int i)throws ApplicationException;
	public Response searchProcedureByNameOrCode(ProcedureOrderMasterDto procedureOrderDto)throws ApplicationException;

}
