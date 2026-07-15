package com.param.opd.coversheet.service;

import java.util.List;
import java.util.ListIterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.opd.coversheet.dao.IDoctorFavouritesDiagnosisDao;
import com.param.opd.coversheet.dto.DoctorFavoritesDto;
import com.param.opd.coversheet.dto.DoctorFavouritesDiagnosisDto;
import com.param.opd.coversheet.dto.PrescriptionDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DoctorFavouritesDiagnosisServiceImpl implements IDoctorFavouritesDiagnosisService, ICommonConstants, IError{
	final static Logger logger = Logger.getLogger(DoctorFavouritesDiagnosisServiceImpl.class);


	@Autowired 
	IDoctorFavouritesDiagnosisDao iDoctorFavouritesDiagnosisDao;
	
	@Override
	public Response saveDoctorFavouriteDiagnosis(DoctorFavouritesDiagnosisDto doctorFavouritesDiagnosisDto)
			throws ApplicationException {
		try{
			if(doctorFavouritesDiagnosisDto.getStatus().equals('A'))
			{
				Response res = iDoctorFavouritesDiagnosisDao.isDoctorFavoritesDiagnosisExist(doctorFavouritesDiagnosisDto);
				
				if(res.getListObject().size()>0 && res.getListObject()!=null){
					
					return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
					
				}else{
					return iDoctorFavouritesDiagnosisDao.saveDoctorFavouriteDiagnosis(doctorFavouritesDiagnosisDto);
				}
			}else
			{
				return iDoctorFavouritesDiagnosisDao.updateStatusForDoctorFavouriteDiagnosis(doctorFavouritesDiagnosisDto);
			}
			
		}catch(Exception e){
		logger.error("saveDoctorFavouriteDiagnosis failed", e);
		}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDoctorFavouriteDiagnosisListByDocId(DoctorFavouritesDiagnosisDto doctorFavouritesDiagnosisDto)
			throws ApplicationException {
		try{
			return iDoctorFavouritesDiagnosisDao.getDoctorFavouriteDiagnosisListByDocId(doctorFavouritesDiagnosisDto);
			
		}catch(Exception e){
		logger.error("getDoctorFavouriteDiagnosisListByDocId failed", e);
		}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


}
