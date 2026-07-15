package com.param.lis.histopathology.transaction.service;

import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.histopathology.transaction.dto.RestainReqDto;
import com.param.lis.histopathology.transaction.dto.TRestainingRequestMasterDto;
import com.param.lis.histopathology.transaction.dto.TSubSpecimanMasterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})
public interface IRestainRequestService {

  public Response getSlidesDetails(TSubSpecimanMasterDto tSubSpecimanMasterDto)
      throws ApplicationException;

  public Response saveRestainRequest(TRestainingRequestMasterDto tRestainingRequestMasterDto)
      throws ApplicationException;

  public Response getRestainRequestWorkList( SearchDto searchDto)
      throws ApplicationException;

  public Response getRestainRequestWorkListCount(SearchDto searchDto)
      throws ApplicationException;

  public Response getRestainRequestWorkListDetails(Integer tRestainingReqId, Integer orgId,
      Integer orgUnitId) throws ApplicationException;

  public Response getRestainRequestWorkListSlides(RestainReqDto dto) throws ApplicationException; 
  
  public Response getMaxSlideNumber(RestainReqDto dto) throws ApplicationException; 
  
}
