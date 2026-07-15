package com.param.global.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IAssociatedCompanyMasterDao;
import com.param.global.dto.AssociatedCompanyMasterDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings("rawtypes")
public class AssociatedCompanyMasterServiceImpl implements IAssociatedCompanyMasterService, ICommonConstants{
	final static Logger logger = Logger.getLogger(AssociatedCompanyMasterServiceImpl.class);


	@Autowired
	private IAssociatedCompanyMasterDao iAssociatedCompanyMasterDao;
	
	@Override
	@Transactional
	public Response getAssociatedCompanyMaster(AssociatedCompanyMasterDto associatedCompanyMasterDto)
			throws ApplicationException {
		try {
				return iAssociatedCompanyMasterDao.getAssociatedCompanyMaster(associatedCompanyMasterDto);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

}
