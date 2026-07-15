package com.param.global.mrd.service;

import com.param.global.common.Response;
import com.param.global.mrd.dto.RowFilterDto;
import com.param.global.mrd.dto.RowRequestDto;
import in.param.exception.ApplicationException;

public interface IMrdRowMasterService {
    Response saveRowMaster(RowRequestDto rowRequestDto) throws ApplicationException;

    Response getRowById(Integer rowId, Integer orgId, Integer unitId) throws ApplicationException;

    Response getRowList(RowFilterDto rowRequestDto) throws ApplicationException;

    Response updateRow(RowRequestDto rowRequestDto) throws ApplicationException;

    Response updateRowStatus(RowRequestDto rowRequestDto) throws ApplicationException;

    Response getRowCount(RowRequestDto rowRequestDto) throws ApplicationException;

    Response getActiveRowList(Integer orgId, Integer unitId) throws ApplicationException;

}
