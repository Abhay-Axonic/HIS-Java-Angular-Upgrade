package com.param.ipd.service;

import javax.transaction.Transactional;

import com.param.entity.model.ipd.ComplaintAdmission;
import com.param.global.common.Response;
import com.param.ipd.dto.ComplaintAdmissionDto;

import com.param.ipd.dto.SmartDocumentSummary;
import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings("rawtypes")
public interface IComplaintAdmissionService {
	
	public Response getComplaintAdmission(ComplaintAdmissionDto complaintAdmissionDto) throws ApplicationException;

	public Response saveComplaintAdmission(ComplaintAdmissionDto complaintAdmissionDto) throws ApplicationException;

	public Response editComplaintAppointment(ComplaintAdmissionDto complaintAdmissionDto) throws ApplicationException;

	public Response updateComplaintAdmission(ComplaintAdmissionDto complaintAdmissionDto) throws ApplicationException;
	
	public Response updateSaveNurseReviewedFlag(ComplaintAdmission complaintAdmission) throws ApplicationException;


	public Response updateComplaintAdmissionStatusToRemoveComplaint(ComplaintAdmissionDto complaintAdmissionDto) throws ApplicationException;

//	public Response getComplaintAppointmentByPatientId(ComplaintAdmissionDto complaintAdmissionDto)throws ApplicationException;

	public Response saveResolvedComplaintAdmission(ComplaintAdmissionDto complaintAdmissionDto)throws ApplicationException;

    Response getActiveComplaintsForIPDCoversheetByDate(ComplaintAdmissionDto dto) throws ApplicationException;

	Response getUniqueDateForIPDCoversheet(ComplaintAdmissionDto dto) throws ApplicationException;

	Response generateSummaryDocument(ComplaintAdmissionDto dto) throws ApplicationException;

    Response saveMyLatestMedicalSummaryCallBack(SmartDocumentSummary dto) throws ApplicationException;

    Response getAISummaryData(ComplaintAdmissionDto dto) throws ApplicationException;;
}
