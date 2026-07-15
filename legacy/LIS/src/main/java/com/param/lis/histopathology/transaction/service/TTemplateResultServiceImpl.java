package com.param.lis.histopathology.transaction.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.entity.lis.histo.TTemplateResult;
import com.param.entity.lis.transaction.LabSampleDetailsMaster;
import com.param.global.common.ServiceException;
import com.param.global.dto.TemplateTransactionDto;
import com.param.global.dto.global.TTemplateResultDto;
import com.param.global.opd.service.IEncounterMasterService;
import com.param.global.service.IOrderMasterService;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.global.common.Response;
import com.param.lis.histopathology.transaction.dao.ITTemplateResultDao;
import com.param.lis.microbiology.transaction.service.IMicrobiologyService;
import com.param.lis.transaction.dto.LabSampleDetailsMasterDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes", "unchecked"})
public class TTemplateResultServiceImpl
    implements ITTemplateResultService, ICommonConstants, ITransactionConstants, IError {

  final Logger logger = Logger.getLogger(TTemplateResultServiceImpl.class);

  @Autowired
  private ITTemplateResultDao iTTemplateResultDao;

  @Autowired
  IMicrobiologyService iMicrobiologyService;

  
  @Autowired
  IOrderMasterService iOrderMasterService;

	 
	 @Autowired
     IEncounterMasterService iEncounterMasterService;
  
  @Override
  @Transactional
  public Response saveTemplateResult(TTemplateResultDto tTemplateResultDto) throws ApplicationException {
    try {
      Response<LabSampleDetailsMaster, Integer> sampleStatusRes = null;
      Response res=iTTemplateResultDao.getHistopathReportNo(tTemplateResultDto);
      if(tTemplateResultDto.getIsReleased() != null && tTemplateResultDto.getIsReleased() != true ){
      if(res.getObject()!=null && (Boolean)res.getObject()){
    	  return new Response<>(ERROR, ALREADY_EXIST, "Report No already Exists !!", null, null);
      }
      
      }
      Response<TTemplateResult, Integer> tTemplateReslt =
          iTTemplateResultDao.saveTemplateResult(tTemplateResultDto);
     if(tTemplateReslt.getCode().equals(SUCCESS_200)) {

        if (tTemplateResultDto.getSampleStatusId()!= 0) {

          LabSampleDetailsMasterDto labSampleDetailsMasterDto =
              new LabSampleDetailsMasterDto();
          labSampleDetailsMasterDto.setOrgId(tTemplateResultDto.getOrgId());
      	  labSampleDetailsMasterDto.setOrgUnitId(tTemplateResultDto.getOrgUnitId());
          labSampleDetailsMasterDto.setCreatedBy(tTemplateResultDto.getCreatedBy());
          labSampleDetailsMasterDto.setSampleStatusId(tTemplateResultDto.getSampleStatusId());
          labSampleDetailsMasterDto.setLabSampleDtlsId(tTemplateResultDto.getLabSampleDtlsId());
          labSampleDetailsMasterDto.setAssignUnit(tTemplateResultDto.getOrgUnitId());
          labSampleDetailsMasterDto.setPerformedUnit(tTemplateResultDto.getOrgUnitId());
          sampleStatusRes =
              iMicrobiologyService.updateLabSampleDetailsMaster(labSampleDetailsMasterDto);
          
          iOrderMasterService.assignReviewApplicableDate(tTemplateResultDto.getOrderDetailsId());
          
           if(labSampleDetailsMasterDto.getSampleStatusId().equals(REPORT_RELEASED))
          {
        	  //for feedbackform
	             TemplateTransactionDto templateDto = new TemplateTransactionDto();
	             templateDto.setLabSampleDtlsId(labSampleDetailsMasterDto.getLabSampleDtlsId());
	             iEncounterMasterService.checkFeedBackFormSubmitted(templateDto);
          }
          
          
            if (sampleStatusRes.getCode().equals(SUCCESS_200)) {
            return new Response(SUCCESS, SUCCESS_200, "Template Result Saved Successfully.",
                null, null);
          }

        } else {
          return new Response(SUCCESS, SUCCESS_200, "Template Result Saved Successfully.", null,
              null);
        }
        return new Response(ERROR, ERR_500, "Failed to Save Template Result.", null, null);
      } else {

        return new Response(ERROR, ERR_500, "Failed to Save Template Result", null, null);
      }

    } catch (Exception e) {
      logger.error("Exception", e);
      throw e;
    }
  }



  @Override
  @Transactional
  public Response getHistopathReport(Integer templateResId, Integer orgId, Integer orgUnitId)
      throws ApplicationException {
    try {
      return iTTemplateResultDao.getHistopathReport(templateResId, orgId, orgUnitId);
    } catch (Exception e) {
      
      throw e;
    }
  }



@Override
public Response updateTemplateResult(TTemplateResultDto tTemplateResultDto) throws ApplicationException {
	try {
		  return  iTTemplateResultDao.updateTemplateResult(tTemplateResultDto);

	} catch (Exception e) {
		logger.error("Exception", e);
		throw new ServiceException(e.getMessage());
	}

}



@Override
@Transactional
public Response getHistopathReportDetails(Integer labSampleDtlsId,Integer orgId, Integer orgUnitId) throws ApplicationException {
	try{
		return iTTemplateResultDao.getHistopathReportDetails(labSampleDtlsId,orgId,orgUnitId);
	}catch (Exception e) {
		 throw e;
	}
}
	
}


