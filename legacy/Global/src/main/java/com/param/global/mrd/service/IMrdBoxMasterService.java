package com.param.global.mrd.service;

import com.param.global.common.Response;
import com.param.global.mrd.dto.BoxFilterDto;
import com.param.global.mrd.dto.BoxRequestDto;
import in.param.exception.ApplicationException;

public interface IMrdBoxMasterService {
    Response saveBoxMaster(BoxRequestDto boxRequestDto) throws ApplicationException;

    Response getBoxById(Integer boxId, Integer orgId, Integer unitId) throws ApplicationException;

    Response getBoxList(BoxFilterDto boxRequestDto) throws ApplicationException;

    Response updateBox(BoxRequestDto boxRequestDto) throws ApplicationException;

    Response updateBoxStatus(BoxRequestDto boxRequestDto) throws ApplicationException;

    Response getBoxCount(BoxRequestDto boxRequestDto) throws ApplicationException;

    Response getActiveBoxList(Integer orgId, Integer unitId) throws ApplicationException;

}
