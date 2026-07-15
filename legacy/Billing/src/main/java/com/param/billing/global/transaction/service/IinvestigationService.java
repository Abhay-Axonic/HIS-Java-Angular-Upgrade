package com.param.billing.global.transaction.service;


import com.param.billing.dto.InvestigationDTO;
import com.param.billing.dto.InvestigationOrderDto;
import com.param.global.common.Response;
import com.param.global.dto.FreeTextOrderedServicesDto;
import com.param.global.dto.OrderMasterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IinvestigationService {


	public Response getCurrentInvestigations(Integer patient_id, boolean flag) throws ApplicationException;
	
	public Response getInvestigations(String start_date, String end_date,
			Integer service_id) throws ApplicationException;

	public Response updateInvestigation(InvestigationDTO investigationDTO)
			throws ApplicationException;

	public Response deleteInvestigation(Integer investigation_id)
			throws ApplicationException;

	public Response getServiceTypes() throws ApplicationException;

	public Response getHistoryServices(Integer orgId)
			throws ApplicationException;

	Response getServices(String serviceCode, String serviceName, Integer orgId)
			throws ApplicationException;
	
	Response getServicesByTest(Integer serviceId) throws ApplicationException;

	public Response saveOrdersFromInvestigation(OrderMasterDto orderMasterDto)throws ApplicationException;
	
	public Response getPreviousInvestigation(int orgId,int orgUnitId,int patientId, int visitTypeId)throws ApplicationException;
	
	public Response getPreviousInvestigationDetails(int orgId,int orgUnitId,int orderId)throws ApplicationException;
	
	public Response getCurrentInvestigationDetails(Integer panelId,Integer orderDetailsId)throws ApplicationException;

	public Response getCurrentInvestigationsByPatientId(Integer patient_id)throws ApplicationException;

	public Response getInvestigationsListForBookView(Integer patientId, Integer organizationId, String fromDate, String toDate)throws ApplicationException;

	public Response getInvestigationsListForCoversheet(int patient_id, boolean flag, Integer unitId)throws ApplicationException;

	public Response getInvestigationsListForAnesthesia(int patient_id, boolean flag, Integer unitId)throws ApplicationException;

	public Response getCurrentInvestigationsByProcedureIdAndVisitId(Integer visitId, Integer procedureId, boolean flag)throws ApplicationException;

	public Response getInvestigationsByPatientDetails(InvestigationOrderDto investigationOrderDto)throws ApplicationException;

	public Response updateDiagnosticsRemarks(InvestigationOrderDto investigationOrderDto)throws ApplicationException;

	public Response cancelLabOrderForNewEMR(InvestigationOrderDto investigationOrderDto)throws ApplicationException;

	public Response saveFreeTextInvestigations(FreeTextOrderedServicesDto freeTextOrderedServicesDto)throws ApplicationException;

	public Response getFreeTextInvestigations(FreeTextOrderedServicesDto freeTextOrderedServicesDto)throws ApplicationException;

	public Response searchFreeTextInvestigations(FreeTextOrderedServicesDto freeTextOrderedServicesDto)throws ApplicationException;

	public Response cancelFreeTextInvestigations(FreeTextOrderedServicesDto freeTextOrderedServicesDto)throws ApplicationException;

	public Response saveInvestigationForAppointmentTemplate(OrderMasterDto orderMasterDto)throws ApplicationException;

    Response getInvestigationForIPDCoversheetByDate(InvestigationOrderDto dto) throws ApplicationException;

	Response getUniqueDateForIPDCoversheet(InvestigationOrderDto dto) throws ApplicationException;
}
