package com.param.actionmaster.service;

import in.param.exception.ApplicationException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.actionmaster.dao.IUserActionFavouritesDao;
import com.param.data.dto.UserActionFavouritesDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class UserActionFavouritesServiceImpl implements IUserActionFavouritesService,ICommonConstants {
	final static Logger logger = Logger.getLogger(UserActionFavouritesServiceImpl.class);


	@Autowired
	IUserActionFavouritesDao iUserActionFavouritesDao; 
	
	@Override
	@Transactional
	public Response saveUserActionFavourites(UserActionFavouritesDto userActionFavouritesDto) {
		try{
			return iUserActionFavouritesDao.saveUserActionFavourites(userActionFavouritesDto);
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response remove(Integer id) {
		try{
			return iUserActionFavouritesDao.remove(id);
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}
	
	@Override
	@Transactional
	public Response get(Integer userId,Integer unitId) {
		try{
			return iUserActionFavouritesDao.get(userId,unitId);
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

}
