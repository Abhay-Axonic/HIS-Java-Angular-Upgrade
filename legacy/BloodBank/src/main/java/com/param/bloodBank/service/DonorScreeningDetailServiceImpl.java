package com.param.bloodBank.service;


import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.bloodBank.dao.IDonorRegistrationDao;
import com.param.bloodBank.dao.IDonorScreeningDetailDao;
import com.param.bloodBank.dto.DonorScreeningDetailDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class DonorScreeningDetailServiceImpl implements IDonorScreeningDetailService,ICommonConstants {

	final static Logger logger = Logger.getLogger(DonorScreeningDetailServiceImpl.class);


	@Autowired
	private IDonorScreeningDetailDao donorScreeningDetailDao;
	
	@Autowired
	IDonorRegistrationDao iDonorRegistrationDao;
	
	@Override
	@Transactional
	public Response saveDonorScreeningDetail(DonorScreeningDetailDto donorScreeningDetailDto)
			throws ApplicationException {
		try {
			Response res=donorScreeningDetailDao.saveDonorScreeningDetail(donorScreeningDetailDto);
			if(res.getObject()!=null ) {
				donorScreeningDetailDto=(DonorScreeningDetailDto)res.getObject();
				Response response= donorScreeningDetailDao.saveDonorScreeningMapper(donorScreeningDetailDto);
				
				if(response.getStatus().equals(SUCCESS) ) {
					return donorScreeningDetailDao.updateScreeningFlag(donorScreeningDetailDto);
				}
			}
			
		} catch (Exception e) {
			logger.error("saveDonorScreeningDetail failed", e);
			throw new ServiceException(e.getMessage());
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response updateDonorScreeningDeferralStatus(DonorScreeningDetailDto donorScreeningDetailDto)
			throws ApplicationException {
		try {
			return donorScreeningDetailDao.updateDonorScreeningDeferralStatus(donorScreeningDetailDto);
			
		} catch (Exception e) {
			logger.error("updateDonorScreeningDeferralStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getDonorScreeningDetailByDonorId(Integer donorId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			return donorScreeningDetailDao.getDonorScreeningDetailByDonorId(donorId,organizationId,unitId);
			
		} catch (Exception e) {
			logger.error("getDonorScreeningDetailByDonorId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response searchDonor(DonorScreeningDetailDto donorScreeningDetailDto) throws ApplicationException {
		try {
			Response res =  donorScreeningDetailDao.searchDonor(donorScreeningDetailDto);
			List<DonorScreeningDetailDto> finalList = new ArrayList();
			Response res1 = donorScreeningDetailDao.registreredDonorSearch(donorScreeningDetailDto);
			if(res.getListObject()!=null && res.getListObject().size()>0)
			{
			 finalList.addAll(res.getListObject());
			}
			if(res1.getListObject()!=null && res1.getListObject().size()>0)
			{
				 finalList.addAll(res1.getListObject());
			}
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, finalList, null);
		} catch (Exception e) {
			logger.error("searchDonor failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getDonorRegisttrationAndScreeningDetails(DonorScreeningDetailDto donorScreeningDetailDto)
			throws ApplicationException {
		try {
			Response res =  iDonorRegistrationDao.getDonorRegistrationDetails(donorScreeningDetailDto);
			
			List<DonorScreeningDetailDto> finalList = new ArrayList();
			
			Response res1 = donorScreeningDetailDao.searchDonor(donorScreeningDetailDto);
			if(res.getListObject()!=null && res.getListObject().size()>0)
			{
			 finalList.addAll(res.getListObject());
			}
			if(res1.getListObject()!=null && res1.getListObject().size()>0)
			{
				 finalList.addAll(res1.getListObject());
			}
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, finalList, null);
		} catch (Exception e) {
			logger.error("getDonorRegisttrationAndScreeningDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
