package com.param.lis.histopathology.transaction.dao;

import java.util.List;

import com.param.entity.lis.histo.TMicroExaDetails;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.histopathology.transaction.dto.TMicroExaDetailsDto;
import com.param.lis.histopathology.transaction.dto.TSubSpecimanMasterDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})
public interface IMicroScopicExaminationDao extends IGenericDao<TMicroExaDetails, Integer>
{
  public Response getMicroscopicWorkList(SearchDto searchDto)throws ApplicationException;
  public Response getMicroscopicWorkListCount(SearchDto searchDto) throws ApplicationException;
  public Response getMiscroscopicSlides(TSubSpecimanMasterDto tSubSpecimanMasterDto) throws ApplicationException;
  public Response saveSlideExaminationsDetails(List<TMicroExaDetailsDto> listTMicroExaDetailsDto) throws ApplicationException;
  public Response getMicroscopicWorkListLabSampleDetails(SearchDto searchDto)throws ApplicationException;
  public Response getMicroscopicWorkListBySubSpeciman(int subSpecimanId) throws ApplicationException;
	
}
