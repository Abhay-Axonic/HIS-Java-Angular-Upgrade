package com.param.opd.coversheet.dao;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.dto.CoversheetTemplateMasterDto;
import com.param.opd.coversheet.dto.ConsultDoctorDto;
import com.param.opd.model.ConsultDoctor;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IConsultDoctorDao extends IGenericDao<ConsultDoctor, Integer>{

	Response saveConsultDoctor(ConsultDoctorDto consultDoctorDto)throws ApplicationException;

	Response consultDoctorListByEncounter(Integer encounterId)throws ApplicationException;

	Response inactiveConsultDoctor(ConsultDoctorDto consultDoctorDto)throws ApplicationException;

	Response getConsultationPatientListForDocDesk(ConsultDoctorDto ConsultDoctorDto)throws ApplicationException;

	Response consultDoctorListByAdmission(ConsultDoctorDto ConsultDoctorDto)throws ApplicationException;

	Response updateActionMasterStatus(ConsultDoctorDto consultDoctorDto)throws ApplicationException;

	Response consultDoctorListForBookView(ConsultDoctorDto consultDoctorDto)throws ApplicationException;


	Response updateConsultDoctorNotes(ConsultDoctorDto consultDoctorDto)throws ApplicationException;

	Response savePatientConsultDoctor(List<ConsultDoctorDto> list, CoversheetTemplateMasterDto resDto)throws ApplicationException;

	Response updateCovTempPatientConsultDoctor(List<ConsultDoctorDto> list, CoversheetTemplateMasterDto coversheetTemplateMasterDto);



}
