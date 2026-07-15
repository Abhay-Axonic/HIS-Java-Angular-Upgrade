package com.param.global.mrd.dao;

import com.param.global.common.Response;
import com.param.global.mrd.dto.*;
import in.param.exception.ApplicationException;

import java.math.BigInteger;
import java.util.List;

public interface IMrdBoxMasterDao {
    Response saveBoxMaster(BoxRequestDto boxRequestDto) throws ApplicationException;

    BoxResponseDto getBoxById(Integer boxId, Integer orgId, Integer unitId) throws ApplicationException;

    List<BoxResponseDto> getBoxList(BoxFilterDto boxRequestDto) throws ApplicationException;

    Response updateBox(BoxRequestDto boxRequestDto) throws ApplicationException;

    Response updateBoxStatus(BoxRequestDto boxRequestDto) throws ApplicationException;

    BigInteger getBoxCount(BoxFilterDto boxFilterDto) throws ApplicationException;

    Response getActiveBoxList(Integer orgId, Integer unitId) throws ApplicationException;

    Boolean checkBoxExist(BoxRequestDto boxRequestDto);
    Boolean checkBoxExistExcludeBoxId(BoxRequestDto boxRequestDto);
}
