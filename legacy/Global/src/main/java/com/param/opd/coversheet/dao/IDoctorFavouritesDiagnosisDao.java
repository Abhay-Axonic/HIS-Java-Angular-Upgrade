package com.param.opd.coversheet.dao;

import com.param.global.common.Response;
import com.param.opd.coversheet.dto.DoctorFavouritesDiagnosisDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IDoctorFavouritesDiagnosisDao {

	public Response isDoctorFavoritesDiagnosisExist(DoctorFavouritesDiagnosisDto doctorFavouritesDiagnosisDto) throws ApplicationException;

	public Response saveDoctorFavouriteDiagnosis(DoctorFavouritesDiagnosisDto doctorFavouritesDiagnosisDto) throws ApplicationException;

	public Response updateStatusForDoctorFavouriteDiagnosis(DoctorFavouritesDiagnosisDto doctorFavouritesDiagnosisDto)throws ApplicationException;

	public Response getDoctorFavouriteDiagnosisListByDocId(DoctorFavouritesDiagnosisDto doctorFavouritesDiagnosisDto)throws ApplicationException;


}
