package com.param.global.unit.service;

import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.unit.dao.GrowthDevelopmentalMilestonesTransationDao;
import com.param.global.unit.dto.GrowthDevelopmentalMilestonesDto;
import com.param.global.unit.dto.GrowthDevelopmentalMilestonesReqDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings("rawtypes")
@Service
public class GrowthDevelopmentalMilestoneServiceImpl  implements GrowthDevelopmentalMilestoneService, ICommonConstants {
	final static Logger logger = Logger.getLogger(GrowthDevelopmentalMilestoneServiceImpl.class);

	
	@Autowired
	GrowthDevelopmentalMilestonesTransationDao growthDevelopmentalMilestonesTransationDao;

	@Override
	@Transactional
	public Response saveGrowthDevelopmentalMilestonesTransaction(
			List<GrowthDevelopmentalMilestonesDto> listGrowthDevelopmentalMilestonesDto) throws ApplicationException {
		try {
			for (Iterator iterator = listGrowthDevelopmentalMilestonesDto.iterator(); iterator.hasNext();) {
				GrowthDevelopmentalMilestonesDto transaction = (GrowthDevelopmentalMilestonesDto) iterator.next();
				if (transaction.getGrowthDevelopmentalMilestonesTransactionId() == null && transaction.getAchievedDate() != null) {
					growthDevelopmentalMilestonesTransationDao.saveGrowthDevelopmentalMilestonesTransation(transaction);
				}
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getGrowthDevelopmentalMilestonesTransaction(
			GrowthDevelopmentalMilestonesReqDto growthDevelopmentalMilestonesReqDto) throws ApplicationException {
		try {
			return growthDevelopmentalMilestonesTransationDao.getGrowthDevelopmentalMilestonesTransation(growthDevelopmentalMilestonesReqDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

}
