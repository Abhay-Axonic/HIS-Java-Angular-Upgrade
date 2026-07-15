package com.param.bloodBank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.bloodBank.dao.IBloodRedCellDao;
import com.param.bloodBank.dto.BloodRedCellDto;
import com.param.bloodBank.dto.TransfusionTransmittedDiseasesDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes"})
public class BloodRedCellServiceImpl implements IBloodRedCellService,ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodRedCellServiceImpl.class);


	@Autowired 
	IBloodRedCellDao  iBloodRedCellDao;
	@Override
	public Response saveOrUpdateRedCell(BloodRedCellDto dto) throws ApplicationException {
		try {
			if(dto.getRedCellId() == null) {
				
				return iBloodRedCellDao.saveOrUpdateRedCell(dto);
				
			}else {
				if(dto.getGroupingStatus()!= null && dto.getGroupingStatus() == 'Y') {
					
					return iBloodRedCellDao.updateRedCellGrouping(dto);
					
				}else if(dto.getSerumStatus()!= null && dto.getSerumStatus() == 'Y') {
					
					return iBloodRedCellDao.updateRedCellSerum(dto);
					
				}else {
					
					return iBloodRedCellDao.updateBloodConfirmed(dto);
				}
			}
			
			

		} catch (Exception e) {
			logger.error("saveOrUpdateRedCell failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	@Override
	public Response getRedCellDetails(BloodRedCellDto dto) throws ApplicationException {
		try {
					return iBloodRedCellDao.getRedCellDetails(dto);

		} catch (Exception e) {
			logger.error("getRedCellDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	@Override
	public Response getRedCellDetailsOnComponentSeparation(TransfusionTransmittedDiseasesDto dto) throws ApplicationException {
		try {
			return iBloodRedCellDao.getRedCellDetailsOnComponentSeparation(dto);

		} catch (Exception e) {
			logger.error("getRedCellDetailsOnComponentSeparation failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
