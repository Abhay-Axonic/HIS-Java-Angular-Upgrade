package com.param.lis.histopathology.transaction.service;

import com.param.global.dto.global.TTemplateResultDto;
import com.param.lis.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface ITTemplateResultService {

  public Response saveTemplateResult(TTemplateResultDto tTemplateResultDto) throws ApplicationException;
  public Response updateTemplateResult(TTemplateResultDto tTemplateResultDto) throws ApplicationException;
  
  public Response getHistopathReport(Integer templateResId, Integer orgId, Integer orgUnitId)
      throws ApplicationException;
  public Response getHistopathReportDetails(Integer labSampleDtlsId,Integer orgId, Integer orgUnitId) throws ApplicationException;

}
