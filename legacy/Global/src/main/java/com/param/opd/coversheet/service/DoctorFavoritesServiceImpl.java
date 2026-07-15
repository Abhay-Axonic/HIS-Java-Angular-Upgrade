package com.param.opd.coversheet.service;

import java.util.ListIterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.opd.coversheet.dao.IDoctorFavoritesDao;
import com.param.opd.coversheet.dto.DoctorFavoritesDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DoctorFavoritesServiceImpl implements IDoctorFavoritesService,ICommonConstants{
	final static Logger logger = Logger.getLogger(DoctorFavoritesServiceImpl.class);


	@Autowired
	IDoctorFavoritesDao iDoctorFavoritesDao; 
	
	@Override
	public Response saveDoctorFavorites(DoctorFavoritesDto doctorFavoritesDto) throws ApplicationException {
		try{
			if(doctorFavoritesDto.getStatus().equals('A'))
			{
				Response res = iDoctorFavoritesDao.isDoctorFavoritesExist(doctorFavoritesDto);
				if(res.getListObject().size()>0){
					ListIterator<DoctorFavoritesDto> iterator= res.getListObject().listIterator();
					while(iterator.hasNext())
					{
						DoctorFavoritesDto favoritesDto = iterator.next();
						
						/*if(favoritesDto.getRoaId().equals(doctorFavoritesDto.getRoaId()) && 
								favoritesDto.getFrequencyId().equals(doctorFavoritesDto.getFrequencyId())
										&& favoritesDto.getDoseUnitId().equals(doctorFavoritesDto.getDoseUnitId())
											&& favoritesDto.getDose().equals(doctorFavoritesDto.getDose()))
						{*/
							return new Response(ERROR, COMMON_ERROR_CODE, ALREADY_EXIST, null, null);
//						}
						/*else{
							doctorFavoritesDto.setDoctorFavoriteId(favoritesDto.getDoctorFavoriteId());
							iDoctorFavoritesDao.saveDoctorFavorites(doctorFavoritesDto); //it will update
						}*/
					}
				}else{
					return iDoctorFavoritesDao.saveDoctorFavorites(doctorFavoritesDto);
				}
			}else
			{
				return iDoctorFavoritesDao.updateStatusForDoctorFavorites(doctorFavoritesDto);
			}
			
		}catch(Exception e){
		logger.error("saveDoctorFavorites failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response doctorFavoritesListByDocId(DoctorFavoritesDto doctorFavoritesDto) throws ApplicationException {
		try{
			return iDoctorFavoritesDao.getDoctorFavoritesListByDoctorId(doctorFavoritesDto);
		}catch(Exception e){
		logger.error("doctorFavoritesListByDocId failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response doctorFavoritesServiceListByDocId(DoctorFavoritesDto doctorFavoritesDto)
			throws ApplicationException {
		try{
			return iDoctorFavoritesDao.doctorFavoritesServiceListByDocId(doctorFavoritesDto);
		}catch(Exception e){
		logger.error("doctorFavoritesServiceListByDocId failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
    @Override
    public Response getMostlyUsedFavoritesServiceList(DoctorFavoritesDto doctorFavoritesDto)
            throws ApplicationException {
        try{
            return iDoctorFavoritesDao.getMostlyUsedFavoritesServiceList(doctorFavoritesDto);
        }catch(Exception e){
        logger.error("getMostlyUsedFavoritesServiceList failed", e);
        }
        return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
    }



}
