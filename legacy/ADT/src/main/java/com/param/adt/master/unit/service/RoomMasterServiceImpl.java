package com.param.adt.master.unit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.adt.master.unit.dao.IRoomMasterDao;
import com.param.adt.master.unit.dto.RoomMasterDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;
import com.param.global.service.VerifiedIdentityService;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class RoomMasterServiceImpl implements IRoomMasterService, ICommonConstants {

	@Autowired
	private VerifiedIdentityService verifiedIdentityService;

	@Autowired
	IRoomMasterDao iRoomMasterDao;

	@Override
	public Response saveRoomMasterMaster(RoomMasterDto roomMasterDto) throws ApplicationException {
		// BATCH-5 org-trust fix: bind verified tenant
		roomMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
		roomMasterDto.setUnitId(verifiedIdentityService.getVerifiedUnitId());
		Response response = iRoomMasterDao.getRoomMasterByName(roomMasterDto);

		try {
			// BATCH-5 fix: null-check ordering was reversed (size() before null check → NPE when the
			// dup-check query errors/returns null)
			if (response.getListObject() != null && response.getListObject().size() > 0) {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);
			} else {
				return iRoomMasterDao.saveRoomMasterMaster(roomMasterDto);
			}
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getRoomMasterById(int roomId, int orgId, int unitId) throws ApplicationException {
		// BATCH-5 org-trust fix: bind verified tenant (client path org/unit were trusted)
		orgId = verifiedIdentityService.getVerifiedOrganizationId();
		unitId = verifiedIdentityService.getVerifiedUnitId();
		try {
			return iRoomMasterDao.getRoomMasterById(roomId, orgId, unitId);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getRoomMasterList(RoomMasterDto roomMasterDto) throws ApplicationException {
		// BATCH-5 org-trust fix: bind verified tenant
		roomMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
		roomMasterDto.setUnitId(verifiedIdentityService.getVerifiedUnitId());
		try {
			return iRoomMasterDao.getRoomMasterList(roomMasterDto);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateRoomMaster(RoomMasterDto roomMasterDto) throws ApplicationException {
		// BATCH-5 org-trust fix: bind verified tenant
		roomMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
		roomMasterDto.setUnitId(verifiedIdentityService.getVerifiedUnitId());
		Response response = iRoomMasterDao.getRoomMasterByNameNotById(roomMasterDto);

		try {
			// BATCH-5 fix: null-check ordering was reversed (size() before null check → NPE when the
			// dup-check query errors/returns null)
			if (response.getListObject() != null && response.getListObject().size() > 0) {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);
			} else {
				return iRoomMasterDao.updateRoomMaster(roomMasterDto);
			}
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateRoomMasterStatus(RoomMasterDto roomMasterDto) throws ApplicationException {
		// BATCH-5 org-trust fix: bind verified tenant
		roomMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
		roomMasterDto.setUnitId(verifiedIdentityService.getVerifiedUnitId());
		try {
			return iRoomMasterDao.updateRoomMasterStatus(roomMasterDto);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getRoomMasterCount(RoomMasterDto roomMasterDto) throws ApplicationException {
		// BATCH-5 org-trust fix: bind verified tenant
		roomMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
		roomMasterDto.setUnitId(verifiedIdentityService.getVerifiedUnitId());
		try {
			return iRoomMasterDao.getRoomMasterCount(roomMasterDto);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getRoomByWardId(RoomMasterDto roomMasterDto) throws ApplicationException {
		// BATCH-5 org-trust fix: bind verified tenant
		roomMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
		roomMasterDto.setUnitId(verifiedIdentityService.getVerifiedUnitId());
		try {
			return iRoomMasterDao.getRoomByWardId(roomMasterDto);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveModalityRoom(RoomMasterDto roomMasterDto) throws ApplicationException {
		// BATCH-5 org-trust fix: bind verified tenant
		roomMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
		roomMasterDto.setUnitId(verifiedIdentityService.getVerifiedUnitId());
		try {
			return iRoomMasterDao.getActiveModalityRoom(roomMasterDto);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
