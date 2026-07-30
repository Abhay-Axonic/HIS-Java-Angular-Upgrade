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
import com.param.global.service.VerifiedIdentityService;

import in.param.exception.ApplicationException;

/**
 * BR-121 / docs/DECISION_NEEDED_OrganizationIdNotCryptographicallyVerified.md
 * -- live-confirmed cross-tenant IDOR on update, status-toggle, and the
 * nutrition sub-list read (a forged organizationId/unitId in the request was
 * honored outright; the sub-list read endpoint didn't even receive those
 * fields to begin with, so it had no scoping possible at all). Fixed by
 * overwriting organizationId/unitId on every relevant DTO/parameter with
 * VerifiedIdentityService's JWT-derived values here, at the shared service
 * layer, plus (for the id-only sub-list read) an explicit ownership check
 * before returning any data. DAO-level ownership checks on the two mutation
 * paths (update, status-toggle) are a separate, necessary second layer --
 * see KeyIngredientsMasterDaoImpl, since those methods never actually used
 * organizationId/unitId for their own WHERE clause.
 */
@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class KeyIngredientsMasterServiceImpl implements IKeyIngredientsMasterService, ICommonConstants {

	private static final Logger logger = Logger.getLogger(KeyIngredientsMasterServiceImpl.class);

	@Autowired
	IKeyIngredientsMasterDao iKeyIngredientsMasterDao;

	@Autowired
	private VerifiedIdentityService verifiedIdentityService;

	private void applyVerifiedIdentity(KeyIngredientsMasterDto dto) {
		dto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
		dto.setUnitId(verifiedIdentityService.getVerifiedUnitId());
	}

	public Response saveKeyIngredientsMaster(KeyIngredientsMasterDto keyIngredientsMasterDto)
			throws ApplicationException {

		try {
			applyVerifiedIdentity(keyIngredientsMasterDto);
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
			return iKeyIngredientsMasterDao.getUnitMasterDetails(verifiedIdentityService.getVerifiedOrganizationId(),
					verifiedIdentityService.getVerifiedUnitId());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getKeyIngredientsMasterById(KeyIngredientsMasterDto keyIngredientsMasterDto)
			throws ApplicationException {
		try {
			keyIngredientsMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
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
			// BR-121: this endpoint's frontend sends only keyIngredientId, no
			// organizationId/unitId at all -- verify ownership explicitly before
			// returning any nutrition data, since there's no scoped query to
			// piggyback the fix onto here.
			boolean owned = iKeyIngredientsMasterDao.isKeyIngredientOwnedByOrgAndUnit(
					keyIngredientsMasterDto.getKeyIngredientId(),
					verifiedIdentityService.getVerifiedOrganizationId(),
					verifiedIdentityService.getVerifiedUnitId());
			if (!owned) {
				logger.error("getKeyIngredientsDetailsById: refused read of keyIngredientId="
						+ keyIngredientsMasterDto.getKeyIngredientId() + " -- not owned by the verified organization/unit");
				return new Response<>(ERROR, ERROR_CODE, "Not found.", null, null);
			}
			return iKeyIngredientsMasterDao.getKeyIngredientsDetailsById(keyIngredientsMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getAllKeyIngredientsMaster(Integer organizationId,Integer unitId) throws ApplicationException {
		try {
			// BR-121: client-supplied organizationId/unitId parameters ignored
			// in favor of the JWT-verified values.
			Response resp = iKeyIngredientsMasterDao.getAllKeyIngredientsMaster(
					verifiedIdentityService.getVerifiedOrganizationId(), verifiedIdentityService.getVerifiedUnitId());
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
			// BR-121: client-supplied organizationId/unitId parameters ignored
			// in favor of the JWT-verified values.
			Response resp = iKeyIngredientsMasterDao.getActiveKeyIngredientsMaster(
					verifiedIdentityService.getVerifiedOrganizationId(), verifiedIdentityService.getVerifiedUnitId());
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
			applyVerifiedIdentity(keyIngredientsMasterDto);
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
			applyVerifiedIdentity(keyIngredientsMasterDto);
			return iKeyIngredientsMasterDao.updateKeyIngredientsMaster(keyIngredientsMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}
