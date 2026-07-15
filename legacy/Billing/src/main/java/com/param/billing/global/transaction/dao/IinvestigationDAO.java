package com.param.billing.global.transaction.dao;

import java.util.Date;
import java.util.List;

import com.param.billing.dto.InvestigationDTO;
import com.param.billing.dto.InvestigationOrderDto;
import com.param.global.common.Response;
import com.param.opd.model.InvestigationModel;

/*
 * 
 * Created by TUSHAR
 */
import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

public interface IinvestigationDAO extends IGenericDao<InvestigationModel, Integer>{
	
	public Response saveInvestigation(List<InvestigationDTO> investigationDTO)throws ApplicationException;
	
	public Response getCurrentInvestigations(Integer patient_id, boolean flag)throws ApplicationException;
	
	public Response getInvestigations(Date startDate,Date endDate,Integer service_id)throws ApplicationException;
	
	public Response updateInvestigation(InvestigationDTO investigationDTO)throws ApplicationException;
	
	public Response deleteInvestigation(Integer investigation_id)throws ApplicationException;
	
	public Response getServiceTypes()throws ApplicationException;
	
	public Response getHistoryServices(Integer orgId)throws ApplicationException;

	Response getServices(String serviceCode, String serviceName, Integer orgId)
			throws ApplicationException;

	Response getServicesByTest(Integer serviceId) throws ApplicationException;
	
	public Response getCurrentInvestigationDetails(Integer panelId,Integer orderDetailsId)throws ApplicationException;

	public Response getCurrentInvestigationsByPatientId(Integer patient_id) throws ApplicationException;

	public Response getInvestigationsListForBookView(Integer patientId, Integer organizationId,String fromDate, String toDate)throws ApplicationException;

	public Response getInvestigationsListForCoversheet(int patient_id, boolean flag, Integer unitId)throws ApplicationException;

	public Response getInvestigationsListForAnesthesia(int patient_id, boolean flag, Integer unitId)throws ApplicationException;

	public Response getCurrentInvestigationsByProcedureIdAndVisitId(Integer visitId, Integer procedureId, boolean flag)throws ApplicationException;

	public Response getInvestigationsByPatientDetails(InvestigationOrderDto investigationOrderDto)throws ApplicationException;

	public Response updateDiagnosticsRemarks(InvestigationOrderDto investigationOrderDto)throws ApplicationException;

	public Response cancelLabOrderForNewEMR(InvestigationOrderDto investigationOrderDto)throws ApplicationException;

}
