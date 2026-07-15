package com.param.lis.histopathology.transaction.dao;

import com.param.entity.lis.histo.TRestainingRequestMaster;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.histopathology.transaction.dto.RestainReqDto;
import com.param.lis.histopathology.transaction.dto.TRestainingRequestMasterDto;
import com.param.lis.histopathology.transaction.dto.TSubSpecimanMasterDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})
public interface IRestainRequestDao extends IGenericDao<TRestainingRequestMaster, Integer> {

  public Response getSlidesDetails(TSubSpecimanMasterDto tSubSpecimanMasterDto)
      throws ApplicationException;

  public Response saveRestainRequest(TRestainingRequestMasterDto tRestainingRequestMasterDto)
      throws ApplicationException;

  public Response getRestainRequestWorkList(SearchDto searchDto)
      throws ApplicationException;

  public Response getRestainRequestWorkListCount(SearchDto searchDto)
      throws ApplicationException;
  
  public Response getRestainRequestWorkListDetails(Integer tRestainingDetailId, Integer orgId,
      Integer orgUnitId) throws ApplicationException;
  
  public Response getRestainRequestWorkListSlides(RestainReqDto dto) throws ApplicationException;
  
  public Response getMaxSlideNumber(RestainReqDto dto) throws ApplicationException; 
}
