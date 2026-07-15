package com.param.global.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IRoomModalityMasterDao;
import com.param.global.dto.RoomModalityMasterDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({ "unchecked", "rawtypes" })
public class RoomModalityMasterServiceImpl implements IRoomModalityMasterService, ICommonConstants {
	final static Logger logger = Logger.getLogger(RoomModalityMasterServiceImpl.class);


	@Autowired
	IRoomModalityMasterDao iRoomModalityMasterDao;
	@Override
	public Response saveRoomModalityMasterMaster(RoomModalityMasterDto dto) throws ApplicationException {
		try {
				Response existRes=iRoomModalityMasterDao.checkAlredyExistOrNot(dto);
				if(existRes.getListObject()!=null && existRes.getListObject().size()>0){
					return new Response(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
				}else{
					 return iRoomModalityMasterDao.saveRoomModalityMasterMaster(dto);
				}
              
		} catch (Exception e) {
		logger.error("saveRoomModalityMasterMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getRoomModalityById(int roomModalityId, int orgId, int unitId) throws ApplicationException {
		try {
			return iRoomModalityMasterDao.getRoomModalityById(roomModalityId,orgId,unitId);
		} catch (Exception e) {
		logger.error("getRoomModalityById failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getRoomModalityList(RoomModalityMasterDto dto) throws ApplicationException {
		try {
			return iRoomModalityMasterDao.getRoomModalityList(dto);
		} catch (Exception e) {
		logger.error("getRoomModalityList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateRoomModalityMaster(RoomModalityMasterDto dto) throws ApplicationException {
		try {
			 Response checkRes=iRoomModalityMasterDao.checkNotById(dto);
			 if(checkRes.getListObject()!=null && checkRes.getListObject().size()>0){
				 return new Response(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			 }else{
				 return iRoomModalityMasterDao.updateRoomModalityMaster(dto);
			 }
			
		} catch (Exception e) {
		logger.error("updateRoomModalityMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateRoomModalityMasterStatus(RoomModalityMasterDto dto) throws ApplicationException {
		try {
			return iRoomModalityMasterDao.updateRoomModalityMasterStatus(dto);
		} catch (Exception e) {
		logger.error("updateRoomModalityMasterStatus failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateRoomModalityMasterCount(RoomModalityMasterDto dto) throws ApplicationException {
		try {
			
				 return iRoomModalityMasterDao.updateRoomModalityMasterCount(dto);
			
		} catch (Exception e) {
		logger.error("updateRoomModalityMasterCount failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

}
