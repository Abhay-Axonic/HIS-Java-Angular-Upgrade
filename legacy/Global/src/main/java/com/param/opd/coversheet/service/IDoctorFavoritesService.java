package com.param.opd.coversheet.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.opd.coversheet.dto.DoctorFavoritesDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IDoctorFavoritesService {

	@Transactional
	Response saveDoctorFavorites(DoctorFavoritesDto doctorFavoritesDto)throws ApplicationException;

	@Transactional
	Response doctorFavoritesListByDocId(DoctorFavoritesDto doctorFavoritesDto)throws ApplicationException;

	@Transactional
	Response doctorFavoritesServiceListByDocId(DoctorFavoritesDto doctorFavoritesDto)throws ApplicationException;

	@Transactional
	Response getMostlyUsedFavoritesServiceList(DoctorFavoritesDto doctorFavoritesDto)throws ApplicationException;

}
