package com.param.opd.coversheet.service;

import in.param.exception.ApplicationException;

import com.param.global.common.Response;
import com.param.opd.coversheet.dto.AdmissionOpdDTO;

/*
 * cREATED BY TUSHAR
 */
public interface IAdmissionService {
	
	public Response saveAdmission(AdmissionOpdDTO admissionOpdDTO)throws ApplicationException;
	
	public Response updateAdmission(AdmissionOpdDTO admissionOpdDTO)throws ApplicationException;
	
	public Response getAdmission(Integer admissionId,Integer instruction_post_id)throws ApplicationException;

	public Response getReasonMasters() throws ApplicationException;

	public Response getPatientReturnDetails(AdmissionOpdDTO admissionOpdDTO)throws ApplicationException;
	
}
