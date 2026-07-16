package com.param.procedure.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.dto.OTManagerDashboardDto;
import com.param.global.otims.dto.PreOperativeDetailsDto;
import com.param.global.otims.dto.ProcedureOrderConsentDto;
import com.param.global.otims.dto.ProcedureOrderMasterDto;
import com.param.procedure.dto.ProcedureOrderListReqDto;
import com.param.procedure.dto.ProcedureOrderReqDto;
import com.param.procedure.dto.ProcedureOrderServSrchReqDto;
import com.param.procedure.dto.ProcedureRequestDto;
import com.param.procedure.dto.VOProcedureOrderMasterDto;
import com.param.procedure.service.IProcedureOrderService;

@RestController
@RequestMapping(value = "/api/procedure")
@SuppressWarnings("rawtypes")
public class ProcedureOrderController implements ICommonConstants{
	
	@Autowired
	private IProcedureOrderService iProcedureOrderService;
	
	/**
	 * GET PROCEDURE ORDER BY ORDER ID
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value = "/order/{orderId}/{tariffId}/{storeId}")
	public Response getProcedureOrderById(@PathVariable("orderId")int orderId,@PathVariable("tariffId")int tariffId,@PathVariable("storeId")int storeId,HttpServletRequest request){
		try{
			
			return iProcedureOrderService.getProcedureOrderById(orderId,tariffId,storeId,request);
		}catch(Exception e){
			
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	/**
	 * GET PROCEDURE ORDER BY ORDER STATUS
	 * @param procedureOrderListReqDto
	 * @return
	 */
	@RequestMapping(value = "/order/status", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getProcedureOrderById(@RequestBody ProcedureOrderListReqDto procedureOrderListReqDto){
		try{
//			procedureOrderListReqDto.setOrderStatusId(orderStatusId);
			return iProcedureOrderService.getProcedureOrderListByStatus(procedureOrderListReqDto);
		}catch(Exception e){
			
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}	
			
	/**
	 * GET SERVICES FOR PROCEDURE ORDER WHERE IS PROCEDURE ORDER FLAG IS TRUE
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = "/order/service/search" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response searchProcedureServiceForOrder(@RequestBody ProcedureOrderServSrchReqDto reqDto){
		try{
			return iProcedureOrderService.searchProcedureServiceForOrder(reqDto);
		}catch(Exception e){
			
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	/**
	 * METHOD TO ORDER PROCEDURE FROM CCOVERSHEET
	 * @param procedureOrderMasterDtoList
	 * @return
	 */
	@RequestMapping(value = "/procedureOrder" ,method=RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveProcedureList(@RequestBody List<ProcedureOrderMasterDto> procedureOrderMasterDtoList){
		try{
			
			Response res1 = null;
			if(procedureOrderMasterDtoList.getFirst().getProReqId() != 0 &&  procedureOrderMasterDtoList.getFirst().getIsPrimarySecondary() == 'Y' && procedureOrderMasterDtoList.getFirst().getProcedureReqMasterDto()!=null) {
				Response res = iProcedureOrderService.getProcedureOrderListByProReqID(procedureOrderMasterDtoList.getFirst().getProReqId());
				if(res.getStatus().equals(SUCCESS)) {
					 res1 = iProcedureOrderService.saveProcedureList(procedureOrderMasterDtoList);
				}
			}else {
				return iProcedureOrderService.saveProcedureList(procedureOrderMasterDtoList);
			}
		return res1;
		}catch(Exception e){
			
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	/**
	 * TO SEARCH AND ADD REGULER SERVICE TO PROCEDURE ORDER
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = "/order/services/regular/search" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response searchServicesForProcedureOrder(@RequestBody ProcedureOrderServSrchReqDto reqDto){
		try{
			return iProcedureOrderService.searchServicesForProcedureOrder(reqDto);
		}catch(Exception e){
			
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	/**
	 * METHOD TO SAVE ACTUAL ORDER WITH DETAILS
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = "/order" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public synchronized Response saveProcedureOrder(@RequestBody ProcedureOrderReqDto reqDto){
		try{
			return iProcedureOrderService.orderProcedures(reqDto);
	   
		}catch(Exception e){
			
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	/*@RequestMapping(value = "/order", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response addSample(@RequestBody String sampleMasterDto) {
		
		
		Response<ProcedureOrderReqDto, Integer> response = null;
		try {
			
			JsonCheck.mapJsonToObject(sampleMasterDto, ProcedureOrderReqDto.class);
			
			//  response = iSampleMasterService.addSampleMaster(sampleMasterDto); return
			 // response;
			 
			return null;
		} catch (Exception e) {
			
			return new Response(ERROR, ERROR_CODE, null, null, null);
		}
	}*/

	
	/**
	 * METHOD TO GET PROCEDURE REQUEST DETAILS BY REQUEST ID
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/order/req/{id}")
	public Response getProcedureReqDetailsById(@PathVariable("id") int id){
		try{
			return iProcedureOrderService.getProcReqDetailsById(id);
		}catch(Exception e){
			
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	/**
	 * METHOD TO SEARCH ORDERS BY FILTER
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = "/order/search" , method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response searchProcedureOrder(@RequestBody ProcedureRequestDto procedureRequestDto){
		try{
			return iProcedureOrderService.searchProcedureOrder(procedureRequestDto);
		}catch(Exception e){
			
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	/**
	 * TO GET PROCEDURE ROLE LIST FOR DOCTOR DETAILS ADDITION
	 * @param orgId
	 * @param orgUnitId
	 * @return
	 */
	@RequestMapping(value = "/getRoleList/{orgId}/{orgUnitId}", method = RequestMethod.GET)
	public Response getRoleListInOTBilling(@PathVariable("orgId") int orgId,@PathVariable("orgUnitId") int orgUnitId){
		try{
			return iProcedureOrderService.getRoleListInOTBilling(orgId, orgUnitId);
		}catch(Exception e){
			
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	@RequestMapping(value = "/getProcedureOrderList/{encounterId}", method = RequestMethod.GET)
	public Response getProcedureOrderList(@PathVariable("encounterId") int encounterId){
		try{
			return iProcedureOrderService.getProcedureOrderList(encounterId);
		}catch(Exception e){
			
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}

	@RequestMapping(value = "/searchProcedureAndPatientDetailsList", method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response searchProcedureAndPatientDetailsList(@RequestBody OTManagerDashboardDto otManagerDashboardDto){
		try{
			return iProcedureOrderService.searchProcedureAndPatientDetailsList(otManagerDashboardDto);
		}catch(Exception e){
			
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	@RequestMapping(value = "/procedureOrderUpdate" ,method=RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response procedureOrderUpdate(@RequestBody ProcedureOrderMasterDto procedureOrderMasterDto){
		try{
			return iProcedureOrderService.procedureOrderUpdate(procedureOrderMasterDto);
		}catch(Exception e){
			
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	//get Procedure Request Id by Procedure Order ID
	@RequestMapping(value = "/getProcedureRequestId/{orderId}", method = RequestMethod.GET)
	public Response getProcedureRequestId(@PathVariable("orderId") int orderId){
		try{
			return iProcedureOrderService.getProcedureRequestId(orderId);
		}catch(Exception e){
			
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	@RequestMapping(value = "/getprocedureOrderbyVisitandDate" ,method=RequestMethod.POST)
	public Response getprocedureOrderbyVisitandDate(@RequestBody ProcedureOrderMasterDto procedureOrderMasterDto){
		try{
			return iProcedureOrderService.getprocedureOrderbyVisitandDate(procedureOrderMasterDto);
		}catch(Exception e){
			
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	@RequestMapping(value = "/getProcedureOrderListByAdmissionId/{admissionId}/{encounterId}/{proReqId}", method = RequestMethod.GET)
	public Response getProcedureOrderListByAdmissionId(@PathVariable("admissionId") int admissionId,@PathVariable("encounterId") int encounterId , @PathVariable("proReqId") int proReqId){
		try{
			return iProcedureOrderService.getProcedureOrderListByAdmissionId(admissionId,encounterId,proReqId);
		}catch(Exception e){
			
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	@RequestMapping(value = "/procedures/{admissionId}/{encounterId}", method = RequestMethod.GET)
	public Response getProcedureByAdmisssion(@PathVariable("admissionId") int admissionId,@PathVariable("encounterId") int encounterId){
		try{
			return iProcedureOrderService.getProcedureByAdmisssion(admissionId,encounterId);
		}catch(Exception e){
			
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	@RequestMapping(value = "/toCheckByAdmitionId/{proReqId}", method = RequestMethod.GET)
	public Response toCheckByAdmitionId(@PathVariable("proReqId") int proReqId){
		try{
			return iProcedureOrderService.toCheckByAdmitionId(proReqId);
		}catch(Exception e){
			
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	@RequestMapping(value = "/getProcedureOrderList/doctorId/{doctorId}/{orgId}/{orgUnitId}", method = RequestMethod.GET)
	public Response getProcedureOrderListByDoctorForEMR(@PathVariable("doctorId") int doctorId, @PathVariable("orgId") int orgId, @PathVariable("orgUnitId") int orgUnitId){
		try{
			return iProcedureOrderService.getProcedureOrderListByDoctorForEMR(doctorId, orgId, orgUnitId);
		}catch(Exception e){
			
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	@RequestMapping(value = "/procedureOrder/getConsentDetailsByProcedureOrderId", method = RequestMethod.POST)
	public Response getConsentDetailsByProcedureOrderId(@RequestBody ProcedureOrderConsentDto dto){
		try{
			return iProcedureOrderService.getConsentDetailsByProcedureOrderId(dto);
		}catch(Exception e){
			
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	
	/**
	 * METHOD TO ORDER PROCEDURE FROM OPD CCOVERSHEET
	 * @param procedureOrderMasterDtoList
	 * @return
	 */
	@RequestMapping(value = "/procedureOrderForOPD" ,method=RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveProcedureListFromOPD(@RequestBody List<ProcedureOrderMasterDto> procedureOrderMasterDtoList){
		try{
			return iProcedureOrderService.saveProcedureListFromOPD(procedureOrderMasterDtoList);
		}catch(Exception e){
			
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	@RequestMapping(value = "/getProcedureOrderListForDischargeSummary/{admissionId}/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getProcedureOrderListForDischargeSummary(@PathVariable("admissionId") Integer admissionId,@PathVariable("organizationId") Integer organizationId,@PathVariable("unitId") Integer unitId){
		try{
			return iProcedureOrderService.getProcedureOrderListForDischargeSummary(admissionId,organizationId,unitId);
		}catch(Exception e){
			
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	/*@RequestMapping(value = "/procedureOrder/getChecklistDetailsByProcedureOrderId", method = RequestMethod.POST)
	public Response getChecklistDetailsByProcedureOrderId(@RequestBody ProcedureOrderChecklistDto dto){
		try{
			return iProcedureOrderService.getChecklistDetailsByProcedureOrderId(dto);
		}catch(Exception e){
			
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	@RequestMapping(value = "/procedureOrder/getStaffDetailsByProcedureOrderId", method = RequestMethod.POST)
	public Response getStaffDetailsByProcedureOrderId(@RequestBody ProcedureOrderStaffDto dto){
		try{
			return iProcedureOrderService.getStaffDetailsByProcedureOrderId(dto);
		}catch(Exception e){
			
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	@RequestMapping(value = "/procedureOrder/getServiceDetailsByProcedureOrderId", method = RequestMethod.POST)
	public Response getServiceDetailsByProcedureOrderId(@RequestBody ProcedureOrderServiceDto dto){
		try{
			return iProcedureOrderService.getServiceDetailsByProcedureOrderId(dto);
		}catch(Exception e){
			
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	@RequestMapping(value = "/procedureOrder/getPatientEducationDetailsByProcedureOrderId", method = RequestMethod.POST)
	public Response getPatientEducationDetailsByProcedureOrderId(@RequestBody ProcedureOrderPatientEducationDto dto){
		try{
			return iProcedureOrderService.getPatientEducationDetailsByProcedureOrderId(dto);
		}catch(Exception e){
			
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	@RequestMapping(value = "/procedureOrder/getEquipmentDetailsByProcedureOrderId", method = RequestMethod.POST)
	public Response getEquipmentDetailsByProcedureOrderId(@RequestBody ProcedureOrderEquipmentDto dto){
		try{
			return iProcedureOrderService.getEquipmentDetailsByProcedureOrderId(dto);
		}catch(Exception e){
			
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}*/
	
	@RequestMapping(value = "/getProcedureOrderListForBookView", method = RequestMethod.GET)
	public Response getProcedureOrderListForBookView(@RequestParam(value="patientId") int patientId,
			@RequestParam(value="fromDate") String fromDate,@RequestParam(value="toDate") String toDate,
			@RequestParam(value="organization_id") int organizationId){
		try{
			ProcedureOrderMasterDto procedureOrderMasterDto= new ProcedureOrderMasterDto();
			procedureOrderMasterDto.setPatientId(patientId);
			procedureOrderMasterDto.setOrganizationId(organizationId);
			procedureOrderMasterDto.setFromDate(fromDate);
			procedureOrderMasterDto.setToDate(toDate);
			
			return iProcedureOrderService.getProcedureOrderListForBookView(procedureOrderMasterDto);
		}catch(Exception e){
			
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	@RequestMapping(value = "/procedureOrderEquipment" ,method=RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response procedureOrderEquipment(@RequestBody List<VOProcedureOrderMasterDto> VOProcedureOrderMasterDto){
		try{
			return iProcedureOrderService.procedureOrderEquipment(VOProcedureOrderMasterDto);
		}catch(Exception e){
			
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	@RequestMapping(value = "/procedureOrderStaff" ,method=RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response procedureOrderStaff(@RequestBody List<VOProcedureOrderMasterDto> VOProcedureOrderMasterDto){
		try{
			return iProcedureOrderService.procedureOrderStaff(VOProcedureOrderMasterDto);
		}catch(Exception e){
			
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	@RequestMapping(value = "/plannedProcedure/search" , method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response searchPlannedProcedure(@RequestBody ProcedureRequestDto procedureRequestDto){
		try{
			return iProcedureOrderService.searchPlannedProcedure(procedureRequestDto);
		}catch(Exception e){
			
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	@RequestMapping(value = "/procedureOrderChecklist/{proReqId}/{userId}" ,method=RequestMethod.GET)
	public Response procedureOrderChecklist(@PathVariable("proReqId") Integer proReqId ,@PathVariable("userId") Integer userId){
		try{
			return iProcedureOrderService.procedureOrderChecklist(proReqId,userId);
		}catch(Exception e){
			
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	
	@RequestMapping(value = "/getProcedureServiceRate" , method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getProcedureServiceRate(@RequestBody List<ProcedureOrderMasterDto> procedureOrderList){
		try{
			return iProcedureOrderService.getProcedureServiceRate(procedureOrderList);
		}catch(Exception e){
			
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	
	@RequestMapping(value = "/getEquipmentRate/fromContract" , method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getEquipmentRateFromContract(@RequestBody ProcedureOrderMasterDto procedureOrderDto){
		try{
			return iProcedureOrderService.getEquipmentRateFromContract(procedureOrderDto);
		}catch(Exception e){
			
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	@RequestMapping(value = "/getMedicationsAndImplantsByPeoReqId" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getMedicationsAndImplantsByPeoReqId(@RequestBody PreOperativeDetailsDto preOperativeDetailsDto){
		try{
			return  iProcedureOrderService.getMedicationsAndImplantsByPeoReqId(preOperativeDetailsDto);
		}catch(Exception e){
			
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/searchProcedureByNameOrCode" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response searchProcedureByNameOrCode(@RequestBody ProcedureOrderMasterDto procedureOrderDto) {
		try {
			return iProcedureOrderService.searchProcedureByNameOrCode(procedureOrderDto);
		}catch (Exception e) {
			
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getDataByDateFromProcedureOrder", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
	public @ResponseBody Response getDataByDateFromProcedureOrder(@RequestBody ProcedureOrderMasterDto dto) {
		try
		{
			return iProcedureOrderService.getDataByDateFromProcedureOrder(dto);
		}catch(Exception e)
		{

		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getProcedureOrderListForCoversheet/getUniqueDateFromProcedureOrder", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
	public @ResponseBody Response getUniqueDateFromProcedureOrder(@RequestBody ProcedureOrderMasterDto dto) {
		try
		{
			return iProcedureOrderService.getUniqueDateFromProcedureOrder(dto);
		}catch(Exception e)
		{

		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


}
