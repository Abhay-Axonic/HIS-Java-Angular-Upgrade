package com.param.opd.coversheet.dao;

import com.param.global.common.Response;
import com.param.opd.coversheet.dto.DoctorFavoritesDto;
import com.param.opd.coversheet.model.DoctorFavorites;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IDoctorFavoritesDao extends IGenericDao<DoctorFavorites, Integer>{

	Response saveDoctorFavorites(DoctorFavoritesDto doctorFavoritesDto)throws ApplicationException;

	Response isDoctorFavoritesExist(DoctorFavoritesDto doctorFavoritesDto)throws ApplicationException;

	Response updateStatusForDoctorFavorites(DoctorFavoritesDto doctorFavoritesDto)throws ApplicationException;

	Response getDoctorFavoritesListByDoctorId(DoctorFavoritesDto doctorFavoritesDto) throws ApplicationException;

	Response doctorFavoritesServiceListByDocId(DoctorFavoritesDto doctorFavoritesDto) throws ApplicationException;

	Response getMostlyUsedFavoritesServiceList(DoctorFavoritesDto doctorFavoritesDto) throws ApplicationException;

}
