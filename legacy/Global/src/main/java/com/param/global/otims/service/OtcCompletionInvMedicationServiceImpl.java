package com.param.global.otims.service;

import java.util.List;
import java.util.ListIterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.dao.IOtcCompletionInvMedicationDao;
import com.param.global.otims.dto.OtcCompletionInvMedicationDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class OtcCompletionInvMedicationServiceImpl implements IOtcCompletionInvMedicationService, ICommonConstants{
	final static Logger logger = Logger.getLogger(OtcCompletionInvMedicationServiceImpl.class);


	@Autowired
	private IOtcCompletionInvMedicationDao iOtcCompletionInvMedicationDao;
	
	@Override
	@Transactional
	public Response saveInvMedications(OtcCompletionInvMedicationDto otcCompletionInvMedicationDto)
			throws ApplicationException {
		try{
			return iOtcCompletionInvMedicationDao.saveInvMedications(otcCompletionInvMedicationDto);
			
		} catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
	}

}
