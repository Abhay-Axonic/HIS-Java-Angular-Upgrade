package com.param.opd.coversheet.service;

import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.procedure.dao.IProcedureConfigurationDao;
import com.param.opd.coversheet.dao.IAdmissionDAO;
import com.param.opd.coversheet.dao.IAdmissionInstructionPostDAO;
import com.param.opd.coversheet.dto.AdmissionOpdDTO;
import com.param.opd.model.AdmissionModel;
import com.param.opd.model.InstructionAdmissionMapper;

import in.param.exception.ApplicationException;

/*
 * 
 * Created by Tushar
 */

@Service
@SuppressWarnings("rawtypes")
public class AdmissionServiceImpl implements IAdmissionService,ICommonConstants{
	
	@Autowired
	private IAdmissionDAO iAdmissionDAO;
	
	@Autowired
	private IProcedureConfigurationDao iProcedureConfigurationDao;
	
	
	
	@Autowired
	private IAdmissionInstructionPostDAO iAdmissionInstructionPostDAO;

	@Override
	@Transactional
	public Response saveAdmission(AdmissionOpdDTO admissionOpdDTO)
			throws ApplicationException {
		AdmissionModel admissionModel= iAdmissionDAO.saveAdmission(admissionOpdDTO);
		if(admissionModel != null)
		{
			admissionOpdDTO.setAdmission_id(admissionModel.getAdmission_id());
			return iAdmissionInstructionPostDAO.saveInstructionPostAdmission(admissionOpdDTO);			
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
		
	}

	@Override
	@Transactional
	public Response updateAdmission(AdmissionOpdDTO admissionOpdDTO)
			throws ApplicationException {
		AdmissionModel admissionModel= iAdmissionDAO.updateAdmission(admissionOpdDTO);
		if(admissionModel != null){
			return iAdmissionInstructionPostDAO.updateInstructionPostAdmission(admissionOpdDTO);			
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getAdmission(Integer admissionId, Integer instruction_post_id)
			throws ApplicationException {
		AdmissionModel admissionModel =  iAdmissionDAO.getAdmission(admissionId, instruction_post_id);
		if(admissionModel != null){
			InstructionAdmissionMapper  instructionAdmissionMapper = iAdmissionInstructionPostDAO.getInstructionPostAdmission(instruction_post_id);
			if(instructionAdmissionMapper != null){
				AdmissionOpdDTO admissionOpdDTO = new AdmissionOpdDTO();
				admissionOpdDTO.setAdmission_id(admissionModel.getAdmission_id());
				admissionOpdDTO.setBed_category_id(admissionModel.getBed_category_id());
				admissionOpdDTO.setReason_for_admission_id(admissionModel.getReason_for_admission_id());
				admissionOpdDTO.setAdmission_date(admissionModel.getAdmission_date().toString());
				admissionOpdDTO.setPlan_discharge_date(admissionModel.getPlan_discharge_date().toString());
				admissionOpdDTO.setInstruction_post_admission(instructionAdmissionMapper.getInstruction_post_admission());
				admissionOpdDTO.setIs_flexible_status(admissionModel.isIs_flexible_status());
				admissionOpdDTO.setPatient_id(admissionModel.getPatient_id());
				admissionOpdDTO.setCreated_date(admissionModel.getCreated_date());
				admissionOpdDTO.setCreated_by(admissionModel.getCreated_by());
				admissionOpdDTO.setUpdated_date(admissionModel.getUpdated_date());
				admissionOpdDTO.setUpdated_by(admissionModel.getUpdated_by());
				admissionOpdDTO.setEncounter_id(admissionModel.getEncounter_id());
				admissionOpdDTO.setUnit_id(admissionModel.getUnit_id());
				admissionOpdDTO.setOrganisation_id(admissionModel.getOrganisation_id());
				admissionOpdDTO.setInstruction_post_admission_id(instructionAdmissionMapper.getInstruction_admission_mapper_id());
				
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, admissionOpdDTO);
				
				
			}
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getReasonMasters() throws ApplicationException {
		return iAdmissionInstructionPostDAO.getReasonMasters();
	}

	@Override
	@Transactional
	public Response getPatientReturnDetails(AdmissionOpdDTO admissionOpdDTO) throws ApplicationException {
		try {
				return iAdmissionInstructionPostDAO.getPatientReturnDetails(admissionOpdDTO);			
		}catch (Exception e) {
			throw e;
		}
	}

}
