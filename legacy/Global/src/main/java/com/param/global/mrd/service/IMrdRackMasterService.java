package com.param.global.mrd.service;

import com.param.global.common.Response;
import com.param.global.mrd.dto.RackFilterDto;
import com.param.global.mrd.dto.RackRequestDto;
import in.param.exception.ApplicationException;

public interface IMrdRackMasterService {
    Response saveRackMaster(RackRequestDto rackRequestDto) throws ApplicationException;

    Response getRackById(Integer rackId, Integer orgId, Integer unitId) throws ApplicationException;

    Response getRackList(RackFilterDto rackRequestDto) throws ApplicationException;

    Response updateRack(RackRequestDto rackRequestDto) throws ApplicationException;

    Response updateRackStatus(RackRequestDto rackRequestDto) throws ApplicationException;

    Response getRackCount(RackRequestDto rackRequestDto) throws ApplicationException;

    Response getActiveRackList(Integer orgId, Integer unitId) throws ApplicationException;

}
