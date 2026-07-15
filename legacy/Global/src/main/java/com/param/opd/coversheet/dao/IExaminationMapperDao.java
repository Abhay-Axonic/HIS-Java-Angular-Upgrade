package com.param.opd.coversheet.dao;

import com.param.global.common.Response;
import com.param.opd.coversheet.dto.ExaminationMapperDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IExaminationMapperDao {

	public Response saveExaminationMapper(ExaminationMapperDto examinationMapperDto) throws ApplicationException;

	public Response updateExaminationMapper(ExaminationMapperDto examinationMapperDto) throws ApplicationException;

	public Response getExaminationMapperById(ExaminationMapperDto examinationMapperDto) throws ApplicationException;

	public Response getExaminationMapperList(ExaminationMapperDto examinationMapperDto) throws ApplicationException;

	public Response getMostlyUsedExaminationMapperListFor(ExaminationMapperDto examinationMapperDto) throws ApplicationException;

	public Response removeExaminationMapper(ExaminationMapperDto examinationMapperDto) throws ApplicationException;

	public Response checkAvailableExaminationMapper(ExaminationMapperDto examinationMapperDto)throws ApplicationException;
	

}
