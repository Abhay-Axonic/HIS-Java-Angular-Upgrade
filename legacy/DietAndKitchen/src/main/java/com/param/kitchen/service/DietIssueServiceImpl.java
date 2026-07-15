package com.param.kitchen.service;

import java.util.List;

import javax.transaction.Transactional;

import com.param.kitchen.dto.DietIssueDto;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.kitchen.dao.IDietIssueDao;
import com.param.kitchen.dto.DietIssueRequestDto;

import in.param.exception.ApplicationException;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Service
public class DietIssueServiceImpl implements IDietIssueService,ICommonConstants {

	private static final Logger logger = Logger.getLogger(DietIssueServiceImpl.class);

	@Autowired
	IDietIssueDao iDietIssueDao;

	
	@Override
	@Transactional
	public Response getDietIssueByMultipleFields(DietIssueRequestDto dietIssueRequestDto,Integer organizationId,Integer unitId)
			throws ApplicationException {
		try {
			
			if(dietIssueRequestDto.getMealId() == null || dietIssueRequestDto.getMealId()==0)
			{
				return iDietIssueDao.getDietIssueByMultipleFieldsWithoutMealId(dietIssueRequestDto,organizationId,unitId);
			}
			else
			{
				return iDietIssueDao.getDietIssueByMultipleFields(dietIssueRequestDto,organizationId,unitId);
			}
			
		} catch (Exception e) {
			throw e;
		}
//		return new Response(com.param.global.common.ICommonConstants.ERROR,com.param.global.common.ICommonConstants.COMMON_ERROR_CODE, null, null, null);
	}
	
	@Override
	@Transactional
	public Response getDietIssueByOnlyDate(DietIssueRequestDto dietIssueRequestDto,Integer organizationId,Integer unitId)
			throws ApplicationException {
		try {
			return iDietIssueDao.getDietIssueByOnlyDate(dietIssueRequestDto,organizationId,unitId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@Override
	@Transactional
	public Response issueDietOrderByDietOrderList(List<Integer> dietOrderIdList)
			throws ApplicationException {
		try {
			return iDietIssueDao.issueDietOrderByDietOrderList(dietOrderIdList);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@Override
	public Response updateStatus(DietIssueDto dietIssueDto) {
		try {
			return iDietIssueDao.updateStatus(dietIssueDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
		
}
