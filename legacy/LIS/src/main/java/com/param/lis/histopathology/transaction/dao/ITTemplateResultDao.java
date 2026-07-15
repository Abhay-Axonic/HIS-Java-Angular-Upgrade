package com.param.lis.histopathology.transaction.dao;

import com.param.entity.lis.histo.TTemplateResult;
import com.param.global.dto.global.TTemplateResultDto;
import com.param.lis.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface ITTemplateResultDao 
{

  public Response saveTemplateResult(TTemplateResultDto tTemplateResultDto)
      throws ApplicationException;
  
  public Response getHistopathReport(Integer templateResId, Integer orgId, Integer orgUnitId)
      throws ApplicationException;

public Response<TTemplateResult, Integer> updateTemplateResult(TTemplateResultDto tTemplateResultDto);

public Response getHistopathReportNo(TTemplateResultDto tTemplateResultDto);

public Response getHistopathReportDetails(Integer labSampleDtlsId,Integer orgId, Integer orgUnitId)  throws ApplicationException;

}
