package com.param.global.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.NewBornApgarDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface INewBornApgarService {
	

	@Transactional
	Response getNewBornApgarList(NewBornApgarDto dto) throws ApplicationException;
	
	@Transactional
	Response updateNewBornApgar(NewBornApgarDto dto) throws ApplicationException;
	
	@Transactional
	Response saveSingleApgarScore(NewBornApgarDto dto) throws ApplicationException;
	
}

