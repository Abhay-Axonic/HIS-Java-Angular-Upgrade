package com.param.global.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.ExaminationMasterDto;

import in.param.exception.ApplicationException;
@Transactional
public interface IExaminationMasterService {
	
	public Response saveExaminationMaster(ExaminationMasterDto examinationMasterDto) throws ApplicationException;
	
	public Response updateExaminationMaster(ExaminationMasterDto examinationMasterDto) throws ApplicationException;
	
	public Response updateExaminationMasterStatus(ExaminationMasterDto examinationMasterDto) throws ApplicationException;
	
	public Response getExaminationMasterById(ExaminationMasterDto examinationMasterDto) throws ApplicationException;

	public Response getExaminationMasterList(ExaminationMasterDto examinationMasterDto)throws ApplicationException;
	
	public Response getExaminationMasterActiveList(ExaminationMasterDto examinationMasterDto) throws ApplicationException;

	public Response getExaminationMasterCount(ExaminationMasterDto examinationMasterDto)throws ApplicationException;

	public Response searchExaminationMasterActiveList(ExaminationMasterDto examinationMasterDto)throws ApplicationException;

	

	
	



	

}
