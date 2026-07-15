package com.param.global.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.AmbulanceTypeDto;
import com.param.global.dto.BabyStillAssmntDto;
import com.param.global.dto.NewBornAssessmentDto;
import com.param.global.dto.NewBornBabyDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface INewBornBabyDisplayService {
	
	@Transactional
	Response saveStillBirthAssessment(BabyStillAssmntDto babyStillAssmntDto) throws ApplicationException;

	@Transactional
	Response getStillBabyDetails(BabyStillAssmntDto babyStillAssmntDto) throws ApplicationException;

	@Transactional
	Response getSingleStillBaby(BabyStillAssmntDto babyStillAssmntDto) throws ApplicationException;

	@Transactional
	Response updateStillBirthAssessment(BabyStillAssmntDto babyStillAssmntDto) throws ApplicationException;


}

