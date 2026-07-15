package com.param.lis.histopathology.transaction.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.histopathology.transaction.dao.IRestainRequestDao;
import com.param.lis.histopathology.transaction.dto.RestainReqDto;
import com.param.lis.histopathology.transaction.dto.TRestainingRequestMasterDto;
import com.param.lis.histopathology.transaction.dto.TSubSpecimanMasterDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class RestainRequestServiceImpl
		implements IRestainRequestService, ICommonConstants, ITransactionConstants, IError {

	final Logger logger = Logger.getLogger(RestainRequestServiceImpl.class);

	@Autowired
	private IRestainRequestDao iRestainRequestDao;

	@Override
	@Transactional
	public Response getSlidesDetails(TSubSpecimanMasterDto tSubSpecimanMasterDto) throws ApplicationException {
		try {
			return iRestainRequestDao.getSlidesDetails(tSubSpecimanMasterDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response saveRestainRequest(TRestainingRequestMasterDto tRestainingRequestMasterDto)
			throws ApplicationException {
		try {
			return iRestainRequestDao.saveRestainRequest(tRestainingRequestMasterDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getRestainRequestWorkList( SearchDto searchDto) throws ApplicationException {
		try {
			return iRestainRequestDao.getRestainRequestWorkList(searchDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getRestainRequestWorkListCount(SearchDto searchDto) throws ApplicationException {
		try {
			return iRestainRequestDao.getRestainRequestWorkListCount(searchDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getRestainRequestWorkListDetails(Integer tRestainingReqId, Integer orgId, Integer orgUnitId)
			throws ApplicationException {
		try {
			return iRestainRequestDao.getRestainRequestWorkListDetails(tRestainingReqId, orgId, orgUnitId);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getRestainRequestWorkListSlides(RestainReqDto dto) throws ApplicationException {
		try {
			return iRestainRequestDao.getRestainRequestWorkListSlides(dto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getMaxSlideNumber(RestainReqDto dto) throws ApplicationException {
		try {
			return iRestainRequestDao.getMaxSlideNumber(dto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

}
