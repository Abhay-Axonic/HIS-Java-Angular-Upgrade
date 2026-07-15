package com.param.global.dao;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.NewBornApgarDto;
import com.param.global.model.NewBornAssessmentApgar;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
@Transactional
public interface INewBornApgarDao extends IGenericDao<NewBornAssessmentApgar, Integer>{

	Response getNewBornApgarList(NewBornApgarDto dto)throws ApplicationException;
	
	Response updateNewBornApgar(NewBornApgarDto dto)throws ApplicationException;
	
	Response saveSingleApgarScore(NewBornApgarDto dto)throws ApplicationException;
	

}
