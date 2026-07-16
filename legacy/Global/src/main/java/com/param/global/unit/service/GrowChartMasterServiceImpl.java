package com.param.global.unit.service;

import java.math.BigInteger;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.unit.dao.IGrowthChartDao;
import com.param.global.unit.dto.GrowthChartMasterDto;
import com.param.global.unit.dto.GrowthChartReqDto;
import com.param.global.unit.dto.TGrowthChartDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
@Service
public class GrowChartMasterServiceImpl  implements IGrowChartMasterService, ICommonConstants {

	private static final Logger logger = Logger.getLogger(GrowChartMasterServiceImpl.class);

	@Autowired
	IGrowthChartDao iGrowthChartDao;

	@Override
	@Transactional
	public Response saveGrowthMaster(GrowthChartMasterDto growthChartMasterDto) throws ApplicationException {
		try {

			Date date = TimeZoneComponent.getDateByzone(growthChartMasterDto.getUnitId());
			String dateTime = GlobalCommonDateUtils.getStringDate(date, "dd-M-yyyy HH:mm:ss");
			growthChartMasterDto.setCreatedDate(dateTime);
			growthChartMasterDto.setUpdatedDate(dateTime);
			Response growthChartRes = this.checkAgeExistOrNotAdd(growthChartMasterDto);
			BigInteger count =  (BigInteger) growthChartRes.getObject();
			if(count.intValue()>0 ){
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}
			else{
				return iGrowthChartDao.saveGrowthMaster(growthChartMasterDto);
			}

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getGrowthMaster(int orgId, int unitId, int Offset, int recordPerPage) throws ApplicationException {
		try {
			return iGrowthChartDao.getGrowthMaster(orgId, unitId, Offset, recordPerPage);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getHeightUnit(int orgId, int unitId) throws ApplicationException {
		try {
			return iGrowthChartDao.getHeightUnit(orgId, unitId);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getWeightUnit(int orgId, int unitId) throws ApplicationException {
		try {
			return iGrowthChartDao.getWeightUnit(orgId, unitId);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getCircumferenceUnit(int orgId, int unitId) throws ApplicationException {
		try {
			return iGrowthChartDao.getCircumferenceUnit(orgId, unitId);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getGrowthMasterById(int orgId, int unitId, int growthChartId) throws ApplicationException {
		try {
			return iGrowthChartDao.getGrowthMasterById(orgId, unitId,growthChartId);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response upadateGrowthMaster(GrowthChartMasterDto growthChartMasterDto) throws ApplicationException {
     try {

			Date date = TimeZoneComponent.getDateByzone(growthChartMasterDto.getUnitId());
			String dateTime = GlobalCommonDateUtils.getStringDate(date, "dd-M-yyyy HH:mm:ss");
			growthChartMasterDto.setUpdatedDate(dateTime);

			Response growthChartRes = this.checkAgeExistOrNotUpdate(growthChartMasterDto);
			BigInteger count =  (BigInteger) growthChartRes.getObject();
			if(count.intValue()>0 )
			{
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}
			else{
				return iGrowthChartDao.upadateGrowthMaster(growthChartMasterDto);
			}
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response checkAgeExistOrNotAdd(GrowthChartMasterDto growthChartMasterDto) throws ApplicationException {
		try {
			return iGrowthChartDao.checkAgeExistOrNotAdd(growthChartMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response checkAgeExistOrNotUpdate(GrowthChartMasterDto growthChartMasterDto) throws ApplicationException {
		try {
			return iGrowthChartDao.checkAgeExistOrNotUpdate(growthChartMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response deleteGrowthChartMaster(int orgId, int unitId, int growthChartId) throws ApplicationException {
		try {
			return iGrowthChartDao.deleteGrowthChartMaster(orgId, unitId, growthChartId);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response saveGrowthChartTransaction(List<TGrowthChartDto> listTGrowthChartDto) throws ApplicationException {
		try {
			Date date = TimeZoneComponent.getDateByzone(listTGrowthChartDto.getFirst().getUnitId());
			String dateTime = GlobalCommonDateUtils.getStringDate(date, "dd-M-yyyy HH:mm:ss");
			for (Iterator iterator = listTGrowthChartDto.iterator(); iterator.hasNext();) {
				TGrowthChartDto tGrowthChartDto = (TGrowthChartDto) iterator.next();
				tGrowthChartDto.setCreatedDate(dateTime);
				tGrowthChartDto.setUpdatedDate(dateTime);
				iGrowthChartDao.saveGrowthChartTransaction(tGrowthChartDto);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getGrowthChartTransaction(GrowthChartReqDto growthChartReqDto) throws ApplicationException {
		try {
			return iGrowthChartDao.getGrowthChartTransaction(growthChartReqDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

}
