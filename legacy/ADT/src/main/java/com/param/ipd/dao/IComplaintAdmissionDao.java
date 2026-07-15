package com.param.ipd.dao;

import com.param.entity.model.ipd.ComplaintAdmission;
import com.param.global.common.Response;
import com.param.ipd.dto.ComplaintAdmissionDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IComplaintAdmissionDao {
	
	public Response getComplaintAdmission(ComplaintAdmissionDto complaintAdmissionDto) throws ApplicationException;

	public Response saveComplaintAdmission(ComplaintAdmissionDto complaintAdmissionDto) throws ApplicationException;

	public Response editComplaintAppointment(ComplaintAdmissionDto complaintAdmissionDto) throws ApplicationException;

	public Response updateComplaintAdmission(ComplaintAdmissionDto complaintAdmissionDto) throws ApplicationException;

	public Response updateSaveNurseReviewedFlag(ComplaintAdmission complaintAdmission) throws ApplicationException;

	public Response updateComplaintAdmissionStatusToRemoveComplaint(ComplaintAdmissionDto complaintAdmissionDto)throws ApplicationException;

//	public Response getComplaintAppointmentByPatientId(ComplaintAdmissionDto complaintAdmissionDto)throws ApplicationException;

	public Response saveResolvedComplaintAdmission(ComplaintAdmissionDto complaintAdmissionDto)throws ApplicationException;

	public Response getAISummaryData(ComplaintAdmissionDto dto) throws ApplicationException;
}
