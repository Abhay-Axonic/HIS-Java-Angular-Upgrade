package com.param.opd.coversheet.dao;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

import com.param.global.common.Response;
import com.param.opd.coversheet.dto.AdmissionOpdDTO;
import com.param.opd.model.InstructionAdmissionMapper;

/*
 * 
 * Created by Tushar
 */

public interface IAdmissionInstructionPostDAO extends IGenericDao<InstructionAdmissionMapper, Integer>{
	
	public Response saveInstructionPostAdmission(AdmissionOpdDTO admissionOpdDTO)throws ApplicationException;
	
	public Response updateInstructionPostAdmission(AdmissionOpdDTO admissionOpdDTO)throws ApplicationException;
	
	public InstructionAdmissionMapper getInstructionPostAdmission(Integer instruction_post_id)throws ApplicationException;

	public Response getReasonMasters() throws ApplicationException;

	public Response getPatientReturnDetails(AdmissionOpdDTO admissionOpdDTO) throws ApplicationException;

}
