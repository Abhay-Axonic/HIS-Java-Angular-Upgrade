package com.param.global.master.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.master.dao.IKeyIngredientsMasterDao;
import com.param.global.master.dto.KeyIngredientsMasterDto;
import com.param.global.master.dto.NutritionalMasterDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class KeyIngredientsMasterServiceImpl implements IKeyIngredientsMasterService, ICommonConstants {

	private static final Logger logger = Logger.getLogger(KeyIngredientsMasterServiceImpl.class);

	@Autowired
	IKeyIngredientsMasterDao iKeyIngredientsMasterDao;

	public Response saveKeyIngredientsMaster(KeyIngredientsMasterDto keyIngredientsMasterDto)
			throws ApplicationException {

		try {
			Response deptResponse = iKeyIngredientsMasterDao.getKeyIngredientsByName(keyIngredientsMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);
			} else {
				return iKeyIngredientsMasterDao.saveKeyIngredientsMaster(keyIngredientsMasterDto);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getUnitMasterDetails(Integer organizationId,Integer unitId) throws ApplicationException {

		try {
			return iKeyIngredientsMasterDao.getUnitMasterDetails(organizationId,unitId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getKeyIngredientsMasterById(KeyIngredientsMasterDto keyIngredientsMasterDto)
			throws ApplicationException {
		try {
			return iKeyIngredientsMasterDao.getKeyIngredientsMasterById(keyIngredientsMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getKeyIngredientsDetailsById(KeyIngredientsMasterDto keyIngredientsMasterDto)
			throws ApplicationException {
		try {
			return iKeyIngredientsMasterDao.getKeyIngredientsDetailsById(keyIngredientsMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getAllKeyIngredientsMaster(Integer organizationId,Integer unitId) throws ApplicationException {
		try {
			Response resp = iKeyIngredientsMasterDao.getAllKeyIngredientsMaster(organizationId,unitId);
			List<KeyIngredientsMasterDto> keyIngredientsMasterDtoList = resp.getListObject();

			if (keyIngredientsMasterDtoList != null) {
				for (KeyIngredientsMasterDto keyIngredientsMasterDto : keyIngredientsMasterDtoList) {

					Response resp2 = iKeyIngredientsMasterDao.getKeyIngredientsDetailsById(keyIngredientsMasterDto);
					List<NutritionalMasterDto> nutriciansList = resp2.getListObject();

					keyIngredientsMasterDto.setNutriciansList(nutriciansList);
				}

			}

			return new Response(SUCCESS, null, null, keyIngredientsMasterDtoList, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response getAllKeyIngredientsMasterWithoutOrgId() throws ApplicationException {
		try {
			Response resp = iKeyIngredientsMasterDao.getAllKeyIngredientsMasterWithoutOrgId();
			List<KeyIngredientsMasterDto> keyIngredientsMasterDtoList = resp.getListObject();

			if (keyIngredientsMasterDtoList != null) {
				for (KeyIngredientsMasterDto keyIngredientsMasterDto : keyIngredientsMasterDtoList) {

					Response resp2 = iKeyIngredientsMasterDao.getKeyIngredientsDetailsById(keyIngredientsMasterDto);
					List<NutritionalMasterDto> nutriciansList = resp2.getListObject();

					keyIngredientsMasterDto.setNutriciansList(nutriciansList);
				}

			}

			return new Response(SUCCESS, null, null, keyIngredientsMasterDtoList, null);
		} catch (Exception e) {
			throw e;
		}
//		return new Response(ERROR, COMMON_ERROR_CODE, null, null, null);
	}

	@Override
	public Response getActiveKeyIngredientsMaster(Integer organizationId,Integer unitId) throws ApplicationException {
		try {
			Response resp = iKeyIngredientsMasterDao.getActiveKeyIngredientsMaster(organizationId,unitId);
			List<KeyIngredientsMasterDto> keyIngredientsMasterDtoList = resp.getListObject();

			if (keyIngredientsMasterDtoList != null) {
				for (KeyIngredientsMasterDto keyIngredientsMasterDto : keyIngredientsMasterDtoList) {

					Response resp2 = iKeyIngredientsMasterDao.getKeyIngredientsDetailsById(keyIngredientsMasterDto);
					List<NutritionalMasterDto> nutriciansList = resp2.getListObject();

					keyIngredientsMasterDto.setNutriciansList(nutriciansList);
				}

			}

			return new Response(SUCCESS, null, null, keyIngredientsMasterDtoList, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateKeyIngredientsMasterStatus(KeyIngredientsMasterDto keyIngredientsMasterDto)
			throws ApplicationException {
		try {
			return iKeyIngredientsMasterDao.updateKeyIngredientsMasterStatus(keyIngredientsMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateKeyIngredientsMaster(KeyIngredientsMasterDto keyIngredientsMasterDto)
			throws ApplicationException {
		try {

			return iKeyIngredientsMasterDao.updateKeyIngredientsMaster(keyIngredientsMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}
