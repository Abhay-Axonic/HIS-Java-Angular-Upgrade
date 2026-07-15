package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.dto.AmbulanceTarrifDto;
import com.param.global.dto.AmbulanceTypeDto;
import com.param.global.dto.BabyStillAssmntDto;
import com.param.global.dto.NewBornAssessmentDto;
import com.param.global.dto.NewBornBabyDto;
import com.param.global.model.AmbulanceType;
import com.param.global.model.BabyStillAssessment;
import com.param.global.model.NewBornBaby;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface INewBornBabyDisplayDao extends IGenericDao<BabyStillAssessment, Integer>{

	
	Response saveStillBirthAssessment(BabyStillAssmntDto babyStillAssmntDto)throws ApplicationException;

	Response getStillBabyDetails(BabyStillAssmntDto babyStillAssmntDto)throws ApplicationException;

	Response getSingleStillBaby(BabyStillAssmntDto babyStillAssmntDto)throws ApplicationException;

	Response updateStillBirthAssessment(BabyStillAssmntDto babyStillAssmntDto)throws ApplicationException;
	

}
