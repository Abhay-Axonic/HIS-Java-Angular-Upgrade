
package com.param.global.mrd.dao;

import com.param.global.common.Response;
import com.param.global.mrd.dto.RowFilterDto;
import com.param.global.mrd.dto.RowRequestDto;
import com.param.global.mrd.dto.RowResponseDto;
import in.param.exception.ApplicationException;

import java.math.BigInteger;
import java.util.List;

public interface IMrdRowMasterDao {
    Response saveRowMaster(RowRequestDto rowRequestDto) throws ApplicationException;

    RowResponseDto getRowById(Integer rowId, Integer orgId, Integer unitId) throws ApplicationException;

    List<RowResponseDto> getRowList(RowFilterDto rowRequestDto) throws ApplicationException;

    Response updateRow(RowRequestDto rowRequestDto) throws ApplicationException;

    Response updateRowStatus(RowRequestDto rowRequestDto) throws ApplicationException;

    BigInteger getRowCount(RowFilterDto rowFilterDto) throws ApplicationException;

    Response getActiveRowList(Integer orgId, Integer unitId) throws ApplicationException;

    Boolean checkRowExist(RowRequestDto rowRequestDto);
    Boolean checkRowExistExcludeRowId(RowRequestDto rowRequestDto);
}
