package com.param.lis.histopathology.transaction.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.histopathology.transaction.dao.IMicroScopicExaminationDao;
import com.param.lis.histopathology.transaction.dto.TMicroExaDetailsDto;
import com.param.lis.histopathology.transaction.dto.TSubSpecimanMasterDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes", "unchecked"})
public class MicroScopicExaminationServiceImpl
    implements IMicroScopicExaminationService, ICommonConstants, ITransactionConstants, IError {

  final static Logger logger = Logger.getLogger(MicroScopicExaminationServiceImpl.class);

  @Autowired
  IMicroScopicExaminationDao iMicroScopicExaminationDao;

  @Override
  @Transactional
  public Response getMicroscopicWorkList(SearchDto searchDto) throws ApplicationException {
    try {
      return iMicroScopicExaminationDao.getMicroscopicWorkList(searchDto);
    } catch (Exception e) {
      logger.error("Exception", e);
      throw e;
    }
  }

  @Override
  @Transactional
  public Response getMicroscopicWorkListCount(SearchDto searchDto)
      throws ApplicationException {
    try {
      return iMicroScopicExaminationDao.getMicroscopicWorkListCount(searchDto);
    } catch (Exception e) {
      logger.error("Exception", e);
      throw e;
    }
  }

  @Override
  @Transactional
  public Response getMiscroscopicSlides(TSubSpecimanMasterDto tSubSpecimanMasterDto)
      throws ApplicationException {
    try {
      return iMicroScopicExaminationDao.getMiscroscopicSlides(tSubSpecimanMasterDto);
    } catch (Exception e) {
      logger.error("Exception", e);
      throw e;
    }
  }

  @Override
  @Transactional
  public Response saveSlideExaminationsDetails(List<TMicroExaDetailsDto> listTMicroExaDetailsDto)
      throws ApplicationException {
    try {
    	return iMicroScopicExaminationDao.saveSlideExaminationsDetails(listTMicroExaDetailsDto);
    } catch (Exception e) {
      logger.error("Exception", e);
      throw e;
    }
  }

@Override
@Transactional
public Response getMicroscopicWorkListLabSampleDetails(SearchDto searchDto) throws ApplicationException {
	  try {
	      return iMicroScopicExaminationDao.getMicroscopicWorkListLabSampleDetails(searchDto);
	    } catch (Exception e) {
	      logger.error("Exception", e);
	      throw e;
	    }
}

@Override
@Transactional
public Response getMicroscopicWorkListBySubSpeciman(int subSpecimanId) throws ApplicationException {
	try {
	      return iMicroScopicExaminationDao.getMicroscopicWorkListBySubSpeciman(subSpecimanId);
	    } catch (Exception e) {
	      logger.error("Exception", e);
	      throw e;
	    }
		}

}
