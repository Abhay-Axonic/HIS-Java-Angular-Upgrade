package com.param.lis.transaction.dao;

import com.param.global.common.Response;
import com.param.lis.transaction.dto.PhlebotomistHomeCollectionDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IPhlebotomistHomeCollectionDao {

	Response savePhlebotomistHomeCollection(PhlebotomistHomeCollectionDto dto)throws ApplicationException ;

	Response validateUserAlocation(PhlebotomistHomeCollectionDto dto)throws ApplicationException;

}
