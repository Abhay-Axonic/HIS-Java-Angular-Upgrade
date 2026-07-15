package com.param.global.mrd.dao;

import com.param.global.common.Response;
import com.param.global.mrd.dto.RackFilterDto;
import com.param.global.mrd.dto.RackRequestDto;
import com.param.global.mrd.dto.RackResponseDto;
import in.param.exception.ApplicationException;

import java.math.BigInteger;
import java.util.List;

public interface IMrdRackMasterDao {
    Response saveRackMaster(RackRequestDto rackRequestDto) throws ApplicationException;

    RackResponseDto getRackById(Integer rackId, Integer orgId, Integer unitId) throws ApplicationException;

    List<RackResponseDto> getRackList(RackFilterDto rackRequestDto) throws ApplicationException;

    Response updateRack(RackRequestDto rackRequestDto) throws ApplicationException;

    Response updateRackStatus(RackRequestDto rackRequestDto) throws ApplicationException;

    BigInteger getRackCount(RackFilterDto rackFilterDto) throws ApplicationException;

    Response getActiveRackList(Integer orgId, Integer unitId) throws ApplicationException;

    Boolean checkRackExist(RackRequestDto rackRequestDto);
    Boolean checkRackExistExcludeRackId(RackRequestDto rackRequestDto);
}
