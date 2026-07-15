package com.param.global.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.IPharmanDao;
import com.param.global.dto.PharmanDrugDto;

@Service
@SuppressWarnings({"rawtypes"})

public class PharmanServiceImpl implements IPharmanService, ICommonConstants{

	private static final Logger logger = Logger.getLogger(PharmanServiceImpl.class);

	@Autowired
	IPharmanDao iPharmanDao;

	@Override
	public Response getPharmanDrugList(PharmanDrugDto pharmanDrugDto) {
		try {
				return iPharmanDao.getPharmanDrugList(pharmanDrugDto);

		} catch (Exception e) {
			logger.error("getPharmanDrugList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getPharmanDrugFormByName(PharmanDrugDto pharmanDrugDto) {
		try {
			return iPharmanDao.getPharmanDrugFormByName(pharmanDrugDto);

		} catch (Exception e) {
			logger.error("getPharmanDrugFormByName failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getPopUpData(PharmanDrugDto pharmanDrugDto) {
		try {
			return iPharmanDao.getPopUpData(pharmanDrugDto);

		} catch (Exception e) {
			logger.error("getPopUpData failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getPopUpDataWithId(PharmanDrugDto pharmanDrugDto) {
		try {
			return iPharmanDao.getPopUpDataWithId(pharmanDrugDto);

		} catch (Exception e) {
			logger.error("getPopUpDataWithId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
