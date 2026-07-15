package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.dto.ExaminationMasterDto;

import in.param.exception.ApplicationException;

public interface IExaminationMasterDao {

	public Response saveExaminationMaster(ExaminationMasterDto examinationMasterDto) throws ApplicationException;

	public Response updateExaminationMaster(ExaminationMasterDto examinationMasterDto) throws ApplicationException;

	public Response updateExaminationMasterStatus(ExaminationMasterDto examinationMasterDto) throws ApplicationException;

	public Response getExaminationMasterById(ExaminationMasterDto examinationMasterDto) throws ApplicationException;

	public Response getExaminationMasterList(ExaminationMasterDto examinationMasterDto) throws ApplicationException ;

	public Response getExaminationMasterActiveList(ExaminationMasterDto examinationMasterDto) throws ApplicationException;

	public Response getExaminationMasterCount(ExaminationMasterDto examinationMasterDto)throws ApplicationException;

	public Response searchExaminationMasterActiveList(ExaminationMasterDto examinationMasterDto)throws ApplicationException;

}
