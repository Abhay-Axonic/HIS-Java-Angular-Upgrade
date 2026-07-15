package com.param.global.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.NewBornBabyDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
@Transactional
public interface INewBornBabyService {
	
	Response saveNewBornBabyReg(NewBornBabyDto newBornBabyDto) throws ApplicationException;

	Response getBabyDetailsByParentId(NewBornBabyDto newBornBabyDto) throws ApplicationException;

	




}

