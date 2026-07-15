package com.param.opd.coversheet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.opd.coversheet.dto.DoctorFavoritesDto;
import com.param.opd.coversheet.service.IDoctorFavoritesService;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Controller
@RequestMapping("api/opd")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DoctorFavoritesController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(DoctorFavoritesController.class);


	
	@Autowired
	IDoctorFavoritesService iDoctorFavoritesService;
	
	@RequestMapping(value="/doctorFavorites", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveDoctorFavorites(@RequestBody DoctorFavoritesDto doctorFavoritesDto) {
		try{
			return iDoctorFavoritesService.saveDoctorFavorites(doctorFavoritesDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/doctorFavoritesList", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response doctorFavoritesListByDocId(@RequestBody DoctorFavoritesDto doctorFavoritesDto) {
		try{
			return iDoctorFavoritesService.doctorFavoritesListByDocId(doctorFavoritesDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/doctorFavoritesServiceListByDocId", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response doctorFavoritesServiceListByDocId(@RequestBody DoctorFavoritesDto doctorFavoritesDto) {
		try{
			return iDoctorFavoritesService.doctorFavoritesServiceListByDocId(doctorFavoritesDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
    @RequestMapping(value="/getMostlyUsedFavoritesServiceList", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response getMostlyUsedFavoritesServiceList(@RequestBody DoctorFavoritesDto doctorFavoritesDto) {
        try{
            return iDoctorFavoritesService.getMostlyUsedFavoritesServiceList(doctorFavoritesDto);
        }catch(Exception e){
        logger.error("method failed", e);
        }
        return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
    }
    

}
