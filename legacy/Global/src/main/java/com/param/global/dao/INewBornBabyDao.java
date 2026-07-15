package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.dto.NewBornBabyDto;
import com.param.global.model.NewBornBaby;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface INewBornBabyDao extends IGenericDao<NewBornBaby, Integer>{

	
//	Response getBabyDetailsByName(NewBornBabyDto newBornBabyDto)throws ApplicationException;

	Response saveNewBornBabyReg(NewBornBabyDto newBornBabyDto)throws ApplicationException;

	Response getBabyDetailsByParentId(NewBornBabyDto newBornBabyDto)throws ApplicationException;


}
