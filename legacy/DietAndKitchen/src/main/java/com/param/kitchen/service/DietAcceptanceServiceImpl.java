package com.param.kitchen.service;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.master.dto.DishDto;
import com.param.kitchen.dao.IDietAcceptanceDao;
import com.param.kitchen.dto.DietAcceptanceRequestDto;
import com.param.kitchen.dto.PatientDetailsDto;
import com.param.global.common.ICommonConstants;
import in.param.exception.ApplicationException;

@Service
public class DietAcceptanceServiceImpl implements IDietAcceptanceService,ICommonConstants {

	private static final Logger logger = Logger.getLogger(DietAcceptanceServiceImpl.class);

	@Autowired
	IDietAcceptanceDao iDietAcceptanceDao;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional
	public Response acceptDietOrderByDietOrderId(List<PatientDetailsDto> patientDetailsList)
			throws ApplicationException {
		try {
			return iDietAcceptanceDao.acceptDietOrderByDietOrderId(patientDetailsList);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional
	public Response getDietAcceptanceByMultipleFields(DietAcceptanceRequestDto dietAcceptanceRequestDto,Integer organizationId,Integer unitId )
			throws ApplicationException {
		try {
			
			if(dietAcceptanceRequestDto.getDietDate() != null )
			{
				if(dietAcceptanceRequestDto.getMealId() != null && dietAcceptanceRequestDto.getMealId()!=0)
				{
					if(dietAcceptanceRequestDto.getTransition() != null)
					{
						return iDietAcceptanceDao.getDietAcceptanceByMultipleFields(dietAcceptanceRequestDto,organizationId,unitId);
					}
					else
					{
						return iDietAcceptanceDao.getDietAcceptanceByMealIdWithouTransition(dietAcceptanceRequestDto,organizationId,unitId);
					}
				}
				else if(dietAcceptanceRequestDto.getTransition() != null)
				{
					return iDietAcceptanceDao.getDietAcceptanceByTransitionWithouMealId(dietAcceptanceRequestDto,organizationId,unitId);
				}
				else
				{
					return iDietAcceptanceDao.getDietAcceptanceByOnlyDate(dietAcceptanceRequestDto,organizationId,unitId);
				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return new Response(com.param.global.common.ICommonConstants.ERROR,com.param.global.common.ICommonConstants.COMMON_ERROR_CODE, null, null, null);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional
	public Response getDietAcceptanceByOnlyDate(DietAcceptanceRequestDto dietAcceptanceRequestDto,Integer organizationId,Integer unitId)
			throws ApplicationException {
		try {
			return iDietAcceptanceDao.getDietAcceptanceByOnlyDate(dietAcceptanceRequestDto,organizationId,unitId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional
	public Response rejectDietOrderByDietOrderId(List<DishDto> orderList, String remarks)
			throws ApplicationException {
		try {
			return iDietAcceptanceDao.rejectDietOrderByDietOrderId(orderList,remarks);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}
