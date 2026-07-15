package com.param.opd.coversheet.service;

import org.springframework.transaction.annotation.Transactional;

import com.param.global.common.Response;
import com.param.opd.coversheet.dto.DoctorFavouritesDiagnosisDto;

import in.param.exception.ApplicationException;
@SuppressWarnings({"rawtypes"})
@Transactional
public interface IDoctorFavouritesDiagnosisService {

	public Response saveDoctorFavouriteDiagnosis(DoctorFavouritesDiagnosisDto doctorFavouritesDiagnosisDto) throws ApplicationException;

	public Response getDoctorFavouriteDiagnosisListByDocId(DoctorFavouritesDiagnosisDto doctorFavouritesDiagnosisDto)throws ApplicationException;

}
