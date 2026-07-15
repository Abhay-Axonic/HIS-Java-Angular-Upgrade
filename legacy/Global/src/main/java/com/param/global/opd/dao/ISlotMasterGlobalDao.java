package com.param.global.opd.dao;

import com.param.global.common.Response;
import com.param.global.dto.sync.SlotIdReqDto;
import com.param.opd.unit.model.SlotMaster;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

public interface ISlotMasterGlobalDao extends IGenericDao<SlotMaster, Integer>{
	public Response getSlotIdForMyLifeAppSync(SlotIdReqDto dto)throws ApplicationException;

	public Integer getSlotIdByAppointmentId(SlotIdReqDto slotIdReq)throws ApplicationException;

	String getAppDateTimeBySlotId(Integer slotId);
}
